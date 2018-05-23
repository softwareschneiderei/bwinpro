package forestsimulator.DBAccess;

import forestsimulator.util.StandGeometry;
import forestsimulator.util.StopWatch;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import treegross.base.GenerateXY;
import treegross.base.Stand;
import treegross.treatment.Treatment2;

public class AllCalculationRulesProcessor extends SwingWorker<Void, BatchProgress> {
    
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final String aktivesDatenfile;
    private Stand st;
    
    public AllCalculationRulesProcessor(String aktivesDatenfile, Stand st) {
        super();
        this.aktivesDatenfile = aktivesDatenfile;
        this.st = st;
    }

    @Override
    protected Void doInBackground() throws Exception {
        LoadTreegrossStand lts = new LoadTreegrossStand();
        StopWatch wholeBatchTiming = new StopWatch("Whole batch").start();
        try (Connection con = connectionFactory.openDBConnection(aktivesDatenfile, "", "")) {
            List<CalculationRule> rules = gettingRules(con);
            System.out.println("Number of calculation rules:" + rules.size());
            for (CalculationRule rule : rules) {
                StopWatch oneRule = new StopWatch("One rule").start();
                int nwiederh = 0;
                System.out.println("Calculating " + rule.edvId + " with auf: " + rule.aufId + " and scenario: " + rule.scenarioId);
                StopWatch getRepetitionCount = new StopWatch("Repetition count").start();
                try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM Vorschrift WHERE edvid = ? AND auf = ? AND Szenario = ?")) {
                    stmt.setString(1, rule.edvId);
                    stmt.setInt(2, rule.aufId);
                    stmt.setInt(3, rule.scenarioId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            nwiederh = rs.getInt("wiederholung");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Problem: " + " " + e);
                }
                getRepetitionCount.printElapsedTime();
                System.out.println("Repetitions:" + nwiederh);
                for (int iw = 0; iw < nwiederh; iw++) {
                    StopWatch onePass = new StopWatch("One pass").start();
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
                        lts.saveBaum(con, st, rule.edvId, rule.aufId, 0, iw + 1);
                    }
                    if (baumart == 1) {
                        lts.saveSpecies(con, st, rule.edvId, rule.aufId, 0, iw + 1);
                    }
                    if (bestand == 1) {
                        lts.saveStand(con, st, rule.edvId, rule.aufId, 0, iw + 1);
                    }
                    for (int i = 0; i < st.temp_Integer; i++) {
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
                            lts.saveStand(con, st, rule.edvId, rule.aufId, i + 1, iw + 1);
                        }
                        if (ebaum == 1) {
                            lts.saveBaum(con, st, rule.edvId, rule.aufId, i + 1, iw + 1);
                        }
                        if (baumart == 1) {
                            lts.saveSpecies(con, st, rule.edvId, rule.aufId, i + 1, iw + 1);
                        }
                        save.printElapsedTime();
                        StopWatch grow = new StopWatch("Growing").start();
                        st.grow(5, st.ingrowthActive);
                        grow.printElapsedTime();
                        st.sortbyd();
                        st.missingData();
                        st.descspecies();
                    }
                    if (ebaum == 2) {
                        lts.saveBaum(con, st, rule.edvId, rule.aufId, st.temp_Integer, iw + 1);
                    }
                    onePass.printElapsedTime();
                }
                oneRule.printElapsedTime();
            }
        } catch (Exception e) {
            System.out.println("Problem: " + " " + e);
        }
        wholeBatchTiming.printElapsedTime();
        return null;
    }

    @Override
    protected void process(List<BatchProgress> chunks) {
        super.process(chunks); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    private List<CalculationRule> gettingRules(final Connection con) {
        StopWatch gettingRules = new StopWatch("Getting rules").start();
        List<CalculationRule> rules = new ArrayList<>();
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Vorschrift")) {
            while (rs.next()) {
                rules.add(new CalculationRule(rs.getString("edvid"), rs.getInt("auf"), rs.getInt("Szenario")));
            }
        } catch (SQLException e) {
            System.out.println("Problem: " + " " + e);
        }
        gettingRules.printElapsedTime();
        return rules;
    }
}
