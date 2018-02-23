/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forestsimulator.DBAccess;
import forestsimulator.standsimulation.PlugInDBSQLite;
import treegross.base.*;
import java.awt.*;

/**
 *
 * @author nagel
 */
public class DBAccess implements PlugInDBSQLite {
    
public void startDialog(java.awt.Frame frame, Stand st, String dirx){
    String dir =dirx;
    System.out.println("starte Dialog");
    DBAccessDialog dialog = new DBAccessDialog(frame,true,st, dir);
    dialog.setVisible(true);
    
}    
    

}
