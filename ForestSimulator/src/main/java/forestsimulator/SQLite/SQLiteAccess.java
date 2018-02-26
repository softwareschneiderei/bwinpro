/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forestsimulator.SQLite;
import forestsimulator.standsimulation.PlugInDBSQLite;
import treegross.base.*;
import java.awt.*;

/**
 *
 * @author nagel
 */
public class SQLiteAccess implements PlugInDBSQLite {
    
public void startDialog(java.awt.Frame frame, Stand st, String dirx){
    String dir =dirx;
    System.out.println("starte Dialog");
    SQLiteDialog dialog = new SQLiteDialog(frame,true,st, dir);
    dialog.setVisible(true);
    
}    
    

}
