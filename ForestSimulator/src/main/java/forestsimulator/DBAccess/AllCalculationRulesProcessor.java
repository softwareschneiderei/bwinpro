package forestsimulator.DBAccess;

import forestsimulator.util.StandGeometry;
import forestsimulator.util.StopWatch;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import treegross.base.GenerateXY;
import treegross.base.Stand;
import treegross.treatment.Treatment2;

public class AllCalculationRulesProcessor extends SwingWorker<Void, BatchProgress> implements BatchProcessingControl {

    private static final Logger logger = Logger.getLogger(AllCalculationRulesProcessor.class.getName());
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final String aktivesDatenfile;
    private Stand st;
    private BatchProgressListener progressListener;
    private volatile boolean shouldStop;

    public AllCalculationRulesProcessor(String aktivesDatenfile, Stand st) {
        super();
        this.aktivesDatenfile = aktivesDatenfile;
        this.st = st;
    }

    public void setProgressListener(BatchProgressListener l) {
        this.progressListener = l;
    }

    @Override
    protected Void doInBackground() throws Exception {
        LoadTreegrossStand lts = new LoadTreegrossStand();
        StopWatch wholeBatchTiming = new StopWatch("Whole batch").start();
        try (Connection con = connectionFactory.openDBConnection(aktivesDatenfile, "", "")) {
            List<CalculationRule> rules = gettingRules(con);
            logger.log(Level.FINE, "Number of calculation rules: {0}", rules.size());
            for (CalculationRule rule : rules) {
                if (shouldStop) {
                    System.out.println("Processing aborted before next rule.");
                    progressListener.aborted();
                    return null;
                }
                StopWatch oneRule = new StopWatch("One rule").start();
                logger.log(Level.FINE, "Calculating {0} with auf: {1} and scenario: {2}", new Object[]{rule.edvId, rule.aufId, rule.scenarioId});
                logger.log(Level.FINE, "Repetitions: {0}", rule.passCount);
                for (int pass = 0; pass < rule.passCount; pass++) {
                    if (shouldStop) {
                        logger.log(Level.FINE, "Processing aborted before next pass.");
                        progressListener.aborted();
                        return null;
                    }
                    publish(new BatchProgress(rules, rule, pass + 1));
                    StopWatch onePass = new StopWatch("One pass").start();
                    applyCalculationRule(lts, con, rule, pass);
                    onePass.printElapsedTime();
                }
                oneRule.printElapsedTime();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem in batch processing", e);
        }
        wholeBatchTiming.printElapsedTime();
        return null;
    }

    private void applyCalculationRule(LoadTreegrossStand lts, final Connection con, CalculationRule rule, int pass) {
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
        for (int k = 0; k < st.ntrees; k++) {
            if (StandGeometry.pnpoly(st.tr[k].x, st.tr[k].y, st) == 0) {
                st.tr[k].out = 1900;
                st.tr[k].outtype = 1;
            }
        }
        st.descspecies();
// Define all trees with fac = 0.0 as dead zu that there is no growth
        for (int k = 0; k < st.ntrees; k++) {
            if (st.tr[k].fac == 0.0) {
                st.tr[k].out = 1900;
                st.tr[k].outtype = 1;
            }
        }
        st.descspecies();
        Treatment2 t2 = new Treatment2();
        st = lts.loadRules(con, st, rule.edvId, rule.aufId, t2, rule.scenarioId);
        int ebaum = lts.getEBaum();
        int baumart = lts.getBaumart();
        int bestand = lts.getBestand();
        int durchf = lts.getDurchf();
        if (ebaum == 1) {
            lts.saveBaum(con, st, rule.edvId, rule.aufId, 0, pass + 1);
        }
        if (baumart == 1) {
            lts.saveSpecies(con, st, rule.edvId, rule.aufId, 0, pass + 1);
        }
        if (bestand == 1) {
            lts.saveStand(con, st, rule.edvId, rule.aufId, 0, pass + 1);
        }
        for (int i = 0; i < st.temp_Integer; i++) {
            if (shouldStop) {
                logger.log(Level.FINE, "Processing aborted before next step.");
                progressListener.aborted();
                return;
            }
            StopWatch step = new StopWatch("Step " + i).start();
            if (durchf == 1) {
                st.descspecies();
                st.sortbyd();
                t2.executeManager2(st);
                st.descspecies();
            }
            st.executeMortality();
            st.descspecies();
            StopWatch save = new StopWatch("Saving").start();
            if (bestand == 1) {
                lts.saveStand(con, st, rule.edvId, rule.aufId, i + 1, pass + 1);
            }
            if (ebaum == 1) {
                lts.saveBaum(con, st, rule.edvId, rule.aufId, i + 1, pass + 1);
            }
            if (baumart == 1) {
                lts.saveSpecies(con, st, rule.edvId, rule.aufId, i + 1, pass + 1);
            }
            save.printElapsedTime();
            StopWatch grow = new StopWatch("Growing").start();
            st.grow(5, st.ingrowthActive);
            grow.printElapsedTime();
            st.sortbyd();
            st.missingData();
            st.descspecies();
            step.printElapsedTime();
        }
        if (ebaum == 2) {
            lts.saveBaum(con, st, rule.edvId, rule.aufId, st.temp_Integer, pass + 1);
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
