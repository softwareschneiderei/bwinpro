package forestsimulator.dbaccess;

import forestsimulator.standsimulation.PlugInDBSQLite;
import forestsimulator.standsimulation.TgUser;
import treegross.base.*;
import javax.swing.JFrame;

/**
 *
 * @author nagel
 */
public class DBAccess implements PlugInDBSQLite {

    @Override
    public void startDialog(JFrame frame, Stand st, TgUser userSettings) {
        System.out.println("starte Dialog");
        DBAccessDialog dialog = new DBAccessDialog(frame, true, st, userSettings);
        dialog.setVisible(true);
    }
}
