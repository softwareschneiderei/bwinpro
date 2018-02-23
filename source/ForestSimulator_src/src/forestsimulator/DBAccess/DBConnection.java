/*
 * DBConnection.java
 *
 * Created on 25. Januar 2006, 10:39
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.DBAccess;
import java.sql.*;

/**
 *
 * @author jhansen
 * this class manages ncon databaseconnections to different databases
 * to add a databasetype to be supported three steps must be accomplished:
 * (1) add an static and public integer, the name is the type name (ACCESS=1)
 *     the value mus be differet from existing types
 * (2) add a private method wich build the connection to the special database in the given array Connections.
 *     the method must return a boolen value indicating the connection is ok or not
 * (3) add code at the marked places in this class according to the code for the access database support
 * 
 */
public class DBConnection {
    public   Connection[] Connections;
    private   int[] Types;
    private  int n=0;  
    static   int nmax=10; // this class can hold 10 different connections
    public static   int MYSQL=2;
    public static   int ACCESS=1;
    public static   int UNKNOWN=0;
    //... further databasetypes
    
    /**
     * Creates a new instance of DBConnection 
    */
    public DBConnection(int ncons) {
        if(ncons>nmax) n=nmax;
        else n=ncons;
        Connections= new Connection[n];
        Types= new int[n];
    }
    
    /** opens an db-connection
     * INT dbtype: the databasetype
     * returns true when the connection is valid 
     * otherwise false
    */
     public boolean openDBConnection(int dbtype, int conindex, String database, String username, String password, boolean readonly, boolean autocom){
        boolean result=false;
        int type=dbtype;
        // if type is unkonwn try to get the type from the databasename extension:
        if (type== UNKNOWN){
            if(database.endsWith(".mdb")) type=ACCESS;
            // further db types...
        }
        // if still is unknown:
        if(type== UNKNOWN) result=false;
        if(type== ACCESS) result=openDBConnectionAccess(conindex, database, username, password, readonly, autocom);
        if(type== MYSQL) result=openDBConnectionMYSQL(conindex, database, username, password, readonly, autocom);
        // further db types...
        return result;
     }
     
     private boolean openDBConnectionAccess(int conindex, String database, String username, String password, boolean readonly, boolean autocom){
        boolean ok=false;
        if(conindex >= 0 && conindex < n){
            try{
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                Connections[conindex]=DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+
                                                                   database,username, password);
                Connections[conindex].setReadOnly(readonly);
                Connections[conindex].setAutoCommit(autocom);
                Types[conindex]=ACCESS;
                if( Connections[conindex].isClosed()==false){ok=true;}
                if(ok) System.out.println("DBConnection: connection["+conindex+"] build successfully.");
                return ok;
            }
            catch (Exception e){
                System.out.println("DBConnection: "+e);
                ok=false;
                return ok;
            }
        }
        else{return false;}
     }
     
     /*
      *database=host of the db +\+ the database name
      * eg: ldap/waldwachstum, waldwachstum, ww2005
     */
     private boolean openDBConnectionMYSQL(int conindex, String database, String username, String password, boolean readonly, boolean autocom){
         boolean ok=false;
         if(conindex >= 0 && conindex < n){
            try{
                //Class.forName("org.gjt.mm.mysql.Driver");
                Class.forName ("com.mysql.jdbc.Driver");              
                Connections[conindex]=DriverManager.getConnection("jdbc:mysql://"+database,username, password);
                Connections[conindex].setReadOnly(readonly);
                Connections[conindex].setAutoCommit(autocom);
                Types[conindex]=MYSQL;
                if( Connections[conindex].isClosed()==false){ok=true;}
                if(ok) System.out.println("DBConnection: connection["+conindex+"] build successfully.");
                return ok;
            }
            catch (Exception e){
                System.out.println("DBConnection: "+e);
                ok=false;
                return ok;
            }
         }
         else{return false;}
     }
     
         
     
     // further dbtypes...
     
     public int getType(int conindex){
         int type= UNKNOWN;
         if(conindex >= 0 && conindex < n){
            type= Types[conindex];
         }
         return type;
     }
     
     /* closes a db connection
      * returns true if the db was opened and successfully closed
      * returns false if the db was already closed
      * or a exception has been ariesed
     */
     public boolean closeDBConnection(int conindex){
        boolean ok=false;
        boolean ac;
        if(conindex >= 0 && conindex < n){
            try{
                if(Connections[conindex].isClosed()==false){
                    ac=Connections[conindex].getAutoCommit();
                    if(ac==false){Connections[conindex].setAutoCommit(true);}// falls autocommit false ist auf true setzen!
                    Connections[conindex].close();
                    if(Connections[conindex].isClosed()==true){ok=true;}
                    if(ok)System.out.println("DBConnection: connection["+conindex+"] closed successfully.");
                    return ok;
                    }
                else{
                    System.out.println("DBConnection: connection["+conindex+"] is already closed.");
                    return false;                   
                }
            }
            catch (Exception e){
                System.out.println("DBConnection: "+e);
                ok=false;
                return ok;
            }
        }
        else{return false;}
     }
     
     public int closeAll(){
         int closedcons=0;
         for (int i=0; i<n; i++){
            if(Connections[i]!=null)if(closeDBConnection(i)) closedcons++;
         }
         System.out.println("DBConnection: "+closedcons+" connection[s] closed.");
         return closedcons;
     }        
    
}
