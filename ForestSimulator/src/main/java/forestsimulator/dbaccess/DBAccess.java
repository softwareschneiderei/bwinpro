package forestsimulator.dbaccess;

import forestsimulator.standsimulation.PlugInDBSQLite;
import treegross.base.*;
import java.io.File;
import javax.swing.JFrame;

/**
 *
 * @author nagel
 */
public class DBAccess implements PlugInDBSQLite {

    @Override
    public void startDialog(JFrame frame, Stand st, File dirx) {
        System.out.println("starte Dialog");
        DBAccessDialog dialog = new DBAccessDialog(frame, true, st, dirx);
        dialog.setVisible(true);
    }
}
