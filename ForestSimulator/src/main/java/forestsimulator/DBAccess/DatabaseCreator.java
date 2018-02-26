/*
 * DatabaseCreator.java
 *
 * Created on 10. Januar 2006, 12:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.DBAccess;

/**
 *
 * @author jhansen
 *
 * this is the basis for all further databasecreators for special databases 
 * (access, postgres etc).
 * this technique makes it possible to implementate
 * interfaces for other databases easily.
 */
public class DatabaseCreator {
    static int type;
    String newdbpath;
    
    /** Creates a new instance of DatabaseCreator */
    public DatabaseCreator() {
        type=-99;
    }
    
    public boolean showFileSaveDialog(){
        // this class must be overwritten by the special databasecreators
        return false;
    }
    
    public String getNewpath(){
       // this class must be overwritten by the special databasecreators
        return "";
    }
    
    public void setNewpath(String path){
        newdbpath=path;
    }
    
     public boolean createNewDB(String newpath){
         return false;
     }
     
     public int getDatabaseType(){
         return type;
     }
    
}
