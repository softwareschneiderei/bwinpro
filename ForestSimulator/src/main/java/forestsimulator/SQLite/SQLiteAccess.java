/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forestsimulator.SQLite;

import forestsimulator.standsimulation.PlugInDBSQLite;
import forestsimulator.standsimulation.TgUser;
import treegross.base.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author nagel
 */
public class SQLiteAccess implements PlugInDBSQLite {

    @Override
    public void startDialog(JFrame frame, Stand st, TgUser userSettings) {
        try {
            System.out.println("starte Dialog");
            SQLiteDialog dialog = new SQLiteDialog(frame, true, st, userSettings.getDataDir());
            dialog.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(SQLiteAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
