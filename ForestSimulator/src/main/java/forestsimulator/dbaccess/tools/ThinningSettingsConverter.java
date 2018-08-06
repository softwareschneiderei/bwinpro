package forestsimulator.dbaccess.tools;

import forestsimulator.dbaccess.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class ThinningSettingsConverter {
    
    public static void main(String[] args) throws SQLException {
        System.out.println("Updating database");
        ConnectionFactory conn = new ConnectionFactory();
        for (String database : Arrays.asList("data_standsimulation/localdata.mdb", "data_standsimulation/benchmark.mdb", "data_standsimulation/verification_database.mdb")) {
            try(Connection db = conn.openDBConnection(database, "", "")) {
                System.out.println("Updated " + updateModerateThinning(db, "10.0;0.8;22.0;22.0;0.8;28.0;28.0;0.7;100.0", "10.0/0.8/22.0;22.0/0.8/28.0;28.0/0.7/100.0"));
                System.out.println("Updated " + updateModerateThinning(db, "10.0;0.9;22.0;22.0;0.9;28.0;28.0;0.8;100.0", "10.0/0.9/22.0;22.0/0.9/28.0;28.0/0.8/100.0"));
                System.out.println("Updated " + updateModerateThinning(db, "10.0;0.5;22.0;22.0;0.6;28.0;28.0;0.9;100.0", "10.0/0.5/22.0;22.0/0.6/28.0;28.0/0.9/100.0"));
                db.commit();
            }
            try(Connection db = conn.openDBConnection(database, "", "")) {
                System.out.println("Updated szenarioart with intensity " + makeThinningIntensitySpeciesSpecific(db, "0.8"));
                System.out.println("Updated szenarioart with intensity " + makeThinningIntensitySpeciesSpecific(db, "1"));
                System.out.println("Updated szenarioart with intensity " + makeThinningIntensitySpeciesSpecific(db, "1.2"));
                db.commit();
            }
            try(Connection db = conn.openDBConnection(database, "", "")) {
                System.out.println("Updated scenario " + updateThinningIntensity(db, "0,8", "10.0/0.8/22.0;22.0/0.8/28.0;28.0/0.7/100.0"));
                System.out.println("Updated scenario " + updateThinningIntensity(db, "1", "10.0/1.0/22.0;22.0/1.0/28.0;28.0/0.9/100.0"));
                System.out.println("Updated scenario " + updateThinningIntensity(db, "1,2", "10.0/1.2/22.0;22.0/1.1/28.0;28.0/1.0/100.0"));
                db.commit();
            }
        }
    }

    private static int updateModerateThinning(final Connection db, String src, String dest) throws SQLException {
        try (PreparedStatement st = db.prepareStatement("update szenarioart set"
                    + " moderatethinning = ? where moderatethinning = ?")) {
            st.setString(1, dest);
            st.setString(2, src);
            return st.executeUpdate();
        }
    }
    
    private static int updateThinningIntensity(final Connection db, String src, String dest) throws SQLException {
        try (PreparedStatement st = db.prepareStatement("update SzenarioArt set"
                    + " ThinningIntensity = ? where ThinningIntensity = ?")) {
            st.setString(1, dest);
            st.setString(2, src);
            return st.executeUpdate();
        }
    }

    private static int makeThinningIntensitySpeciesSpecific(Connection db, String value) throws SQLException {
        try (PreparedStatement st = db.prepareStatement("update szenarioart set thinningintensity = ? where szenarioartindex in (select szenarionr from szenario where thinningintensity = ?);")) {
            st.setString(1, value);
            st.setString(2, value);
            return st.executeUpdate();
        }
    }
}
