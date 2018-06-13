package forestsimulator.dbaccess;

import forestsimulator.standsimulation.Simulation;
import forestsimulator.util.StandGeometry;
import forestsimulator.util.StopWatch;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import treegross.base.GenerateXY;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.Treatment2;

public class AllCalculationRulesProcessor extends SwingWorker<Void, BatchProgress> implements BatchProcessingControl {

    private static final Logger logger = Logger.getLogger(AllCalculationRulesProcessor.class.getName());
    static {
        logger.setLevel(Level.FINE);
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.FINE);
        logger.addHandler(h);
    }
    
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final String aktivesDatenfile;
    private Stand st;
    private BatchProgressListener progressListener;
    private volatile boolean shouldStop;
    private final StopWatch wholeBatchTiming = new StopWatch("Whole batch");

    public AllCalculationRulesProcessor(String aktivesDatenfile, Stand st, boolean notifyStandListeners) {
        super();
        this.aktivesDatenfile = aktivesDatenfile;
        this.st = st;
        this.st.notificationsEnabled(notifyStandListeners);
    }

    public void setProgressListener(BatchProgressListener l) {
        this.progressListener = l;
    }

    @Override
    protected Void doInBackground() throws Exception {
        LoadTreegrossStand lts = new LoadTreegrossStand();
        StopWatch openDatabase = new StopWatch("Open Database").start();
        try (Connection con = connectionFactory.openDBConnection(aktivesDatenfile, "", "")) {
            openDatabase.printElapsedTime();
            wholeBatchTiming.start();
            List<CalculationRule> rules = gettingRules(con);
            logger.log(Level.FINE, "Number of calculation rules: {0}", rules.size());
            for (CalculationRule rule : rules) {
                if (shouldStop) {
                    logger.log(Level.FINE, "Processing aborted before next rule.");
                    progressListener.aborted();
                    return null;
                }
                StopWatch oneRule = new StopWatch("One rule").start();
                logger.log(Level.FINE, "Calculating {0} with auf: {1} and scenario: {2}", new Object[]{rule.edvId, rule.aufId, rule.scenarioId});
                logger.log(Level.FINE, "Repetitions: {0}", rule.passCount);
                for (int pass = 1; pass <= rule.passCount; pass++) {
                    if (shouldStop) {
                        logger.log(Level.FINE, "Processing aborted before next pass.");
                        progressListener.aborted();
                        return null;
                    }
                    StopWatch onePass = new StopWatch("One pass").start();
                    applyCalculationRule(lts, con, rules, rule, pass);
                    onePass.printElapsedTime();
                }
                oneRule.printElapsedTime();
            }
            wholeBatchTiming.printElapsedTime();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem in batch processing", e);
        }
        return null;
    }

    @Override
    protected void done() {
        st.notificationsEnabled(true);
    }

    private void applyCalculationRule(final LoadTreegrossStand lts, final Connection con, List<CalculationRule> rules, final CalculationRule rule, final int pass) {
        st = lts.loadFromDB(con, st, rule.edvId, rule.aufId, true, true);
        StopWatch sortByD = new StopWatch("Sort tree").start();
        st.sortbyd();
        sortByD.printElapsedTime();
        StopWatch missingdata = new StopWatch("Missing data").start();
        st.missingData();
        missingdata.printElapsedTime();
        GenerateXY gxy = new GenerateXY();
        gxy.zufall(st);
        // Test if all trees are in area
        markTreesAsDead(tree -> StandGeometry.pnpoly(tree.x, tree.y, st) == 0);
        // Define all trees with fac = 0.0 as dead zu that there is no growth
        markTreesAsDead(tree -> tree.fac == 0.0);
        Treatment2 treat = new Treatment2();
        st = lts.loadRules(con, st, rule.edvId, rule.aufId, treat, rule.scenarioId);
        saveStand(con, st, lts, rule, 0, pass);
        Simulation simulation = new Simulation(st, treat);
        for (int step = 0; step < st.temp_Integer; step++) {
            if (shouldStop) {
                logger.log(Level.FINE, "Processing aborted before next step.");
                progressListener.aborted();
                return;
            }
            publish(new BatchProgress(rules, rule, pass, new Progress(step, st.temp_Integer), wholeBatchTiming.split()));
            final int currentStep = step;
            StopWatch stepTime = new StopWatch("Step " + step).start();
            simulation.executeStep(lts.getDurchf(), 5, (Stand t) -> {
                saveStand(con, t, lts, rule, currentStep + 1, pass);
            });
            st.sortbyd();
            st.missingData();
            st.descspecies();
            stepTime.printElapsedTime();
        }
        if (lts.getEBaum() == 2) {
            lts.saveBaum(con, st, rule.edvId, rule.aufId, st.temp_Integer, pass + 1);
        }
    }

    private void markTreesAsDead(Predicate<Tree> condition) {
        for (int k = 0; k < st.ntrees; k++) {
            if (condition.test(st.tr[k])) {
                st.tr[k].out = 1900;
                st.tr[k].outtype = 1;
            }
        }
        st.descspecies();
    }

    private void saveStand(Connection con, Stand st, LoadTreegrossStand lts, CalculationRule rule, int step, int pass) {
        if (lts.getEBaum() == 1) {
            lts.saveBaum(con, st, rule.edvId, rule.aufId, step, pass);
        }
        if (lts.getBaumart() == 1) {
            lts.saveSpecies(con, st, rule.edvId, rule.aufId, step, pass);
        }
        if (lts.getBestand() == 1) {
            lts.saveStand(con, st, rule.edvId, rule.aufId, step, pass);
        }
    }

    @Override
    protected void process(List<BatchProgress> chunks) {
        logger.log(Level.FINE, "Processing chunks");
        BatchProgress lastProgress = chunks.get(chunks.size() - 1);
        progressListener.updateProgress(lastProgress);
    }

    private List<CalculationRule> gettingRules(final Connection con) {
        StopWatch gettingRules = new StopWatch("Getting rules").start();
        List<CalculationRule> rules = new ArrayList<>();
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Vorschrift")) {
            while (rs.next()) {
                rules.add(new CalculationRule(rs.getString("edvid"), rs.getInt("auf"), rs.getInt("Szenario"), rs.getInt("wiederholung")));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Problem getting rules from database.", e);
        }
        gettingRules.printElapsedTime();
        return rules;
    }

    @Override
    public void stopProcessing() {
        this.shouldStop = true;
    }
}
