/*
 * ConnectionBulder.java
 *
 * Created on 4. Juni 2009, 10:45
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package forestsimulator.DBAccess;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhansen
 */
public class ConnectionFactory {

    public static final int ACCESS = 1;
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());

    private String charset = "UTF-8";

    Properties props = new Properties();

    public ConnectionFactory() {
        props.put("charSet", charset);
    }

    public ConnectionFactory(String charset) {
        setConnectionCharSet(charset);
    }

    public final void setConnectionCharSet(String cs) {
        charset = cs;
        props.put("charSet", charset);
    }

    public Connection openDBConnection(String database, String username, String password, boolean readonly, boolean autocom) {
        return openDBConnection(ACCESS, database, username, password, readonly, autocom);
    }
    
    public Connection openDBConnection(int dbtype, String database, String username, String password, boolean readonly, boolean autocom) {
        try {
            return openDBConnectionAccess(database, username, password, readonly, autocom);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem opening database connection.", e);
            return null;
        }
    }

    private Connection openDBConnectionAccess(String database, String username, String password, boolean readonly, boolean autocom) {
        Properties props_a = new Properties();
        if (username != null) {
            props_a.put("user", username);
        }
        if (password != null) {
            props_a.put("password", password);
        }
        try {
            Connection result = DriverManager.getConnection("jdbc:ucanaccess://" + database, props_a);
            result.setReadOnly(readonly);
            result.setAutoCommit(autocom);
            return result;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Problem getting connection to database: " + database, ex);
            return null;
        }
    }
}
