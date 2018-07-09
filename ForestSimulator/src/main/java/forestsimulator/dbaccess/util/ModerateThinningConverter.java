package forestsimulator.dbaccess.util;

import forestsimulator.dbaccess.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class ModerateThinningConverter {
    
    public static void main(String[] args) throws SQLException {
        System.out.println("Updating database");
        ConnectionFactory conn = new ConnectionFactory();
        for (String database : Arrays.asList("data_standsimulation/localdata.mdb", "data_standsimulation/benchmark.mdb")) {
            try(Connection db = conn.openDBConnection(database, "", "")) {
                System.out.println("Updated " + update(db, "10.0;0.8;22.0;22.0;0.8;28.0;28.0;0.7;100.0", "10.0/0.8/22.0;22.0/0.8/28.0;28.0/0.7/100.0"));
                System.out.println("Updated " + update(db, "10.0;0.9;22.0;22.0;0.9;28.0;28.0;0.8;100.0", "10.0/0.9/22.0;22.0/0.9/28.0;28.0/0.8/100.0"));
                System.out.println("Updated " + update(db, "10.0;0.5;22.0;22.0;0.6;28.0;28.0;0.9;100.0", "10.0/0.5/22.0;22.0/0.6/28.0;28.0/0.9/100.0"));
                db.commit();
            }
            try(Connection db = conn.openDBConnection(database, "", "")) {
                System.out.println("Updated scenario " + updateScenario(db, "0,8", "10.0/0.8/22.0;22.0/0.8/28.0;28.0/0.7/100.0"));
                System.out.println("Updated scenario " + updateScenario(db, "1", "10.0/1.0/22.0;22.0/1.0/28.0;28.0/0.9/100.0"));
                System.out.println("Updated scenario " + updateScenario(db, "1,2", "10.0/1.2/22.0;22.0/1.1/28.0;28.0/1.0/100.0"));
                db.commit();
            }
        }
    }

    private static int update(final Connection db, String src, String dest) throws SQLException {
        int rows = db.createStatement().executeUpdate("update szenarioart set"
                + " moderatethinning = '" + dest + "' where"
                + " moderatethinning = '" + src + "'");
        return rows;
    }
    
    private static int updateScenario(final Connection db, String src, String dest) throws SQLException {
        int rows = db.createStatement().executeUpdate("update Szenario set"
                + " ThinningIntensity = '" + dest + "' where"
                + " ThinningIntensity = '" + src + "'");
        return rows;
    }
    
}
