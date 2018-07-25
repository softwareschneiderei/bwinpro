/*
 * ConnectionBulder.java
 *
 * Created on 4. Juni 2009, 10:45
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package forestsimulator.dbaccess;

import forestsimulator.gui.TaskSpinner;
import java.io.File;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RootPaneContainer;

/**
 *
 * @author jhansen
 */
public class ConnectionFactory {

    public static final int ACCESS = 1;
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());

    private String charset = "UTF-8";

    Properties props = new Properties();
    private final RootPaneContainer parent;

    public ConnectionFactory() {
        this(null);
    }

    ConnectionFactory(RootPaneContainer parent) {
        super();
        props.put("charSet", charset);
        this.parent = parent;
    }

    public final void setConnectionCharSet(String cs) {
        charset = cs;
        props.put("charSet", charset);
    }

    public Connection openDBConnection(File database, String username, String password) {
        return new TaskSpinner<Connection>(parent).execute(() -> openDBConnection(ACCESS, database.getAbsolutePath(), username, password));
    }
    
    public Connection openDBConnection(String database, String username, String password) {
        return new TaskSpinner<Connection>(parent).execute(() -> openDBConnection(ACCESS, database, username, password));
    }
    
    public Connection openDBConnection(int dbtype, String database, String username, String password) {
        try {
            return openDBConnectionAccess(database, username, password);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Problem opening database connection.", e);
            return null;
        }
    }

    private Connection openDBConnectionAccess(String database, String username, String password) throws SQLException {
        Properties props_a = new Properties();
        if (username != null) {
            props_a.put("user", username);
        }
        if (password != null) {
            props_a.put("password", password);
        }
        return DriverManager.getConnection("jdbc:ucanaccess://" + database, props_a);
    }
}
