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
    
    public static final int ACCESS=1;
    public static final int MYSQL=2;    
    public static final int POSTGRESQL=3;
    public static final int ORACLE=4;

    private Exception lastError;
    private String charset="UTF-8";

    Properties props = new Properties();

    
    /** Creates a new instance of ConnectionBulder */
    public ConnectionFactory() {
        props.put ("charSet", charset);
    }
    
    public ConnectionFactory(String charset) {
       setConnectionCharSet(charset);
    }

    public final void setConnectionCharSet(String cs){
        charset=cs;
        props.put ("charSet", charset);
    }
    
    /** opens a db-connection
     * INT dbtype: the databasetype
     * returns true when the connection is valid 
     * otherwise false
    */
     public Connection openDBConnection(int dbtype, String database, String username, String password, boolean readonly, boolean autocom){
        Connection result=null;  
        try{
            switch(dbtype){            
                case ACCESS:     result=openDBConnectionAccess(database, username, password, readonly, autocom); break;
                case MYSQL:      result=openDBConnectionMYSQL(database, username, password, readonly, autocom);  break;
                case POSTGRESQL: result=openDBConnectionPOSTGRESQL(database, username, password, readonly, autocom); break;
                case ORACLE:     result=openDBConnectionORACLE( database, username, password, readonly, autocom); break;
            }
        }catch(Exception e){
            lastError=e;
        }
        return result;
     }

     public Exception getLastError(){
         return lastError;
     }
         
     private Connection openDBConnectionAccess(String database, String username, String password, boolean readonly, boolean autocom){
        Properties props_a = new Properties();
        if (username != null) {
	    props_a.put("user", username);
	}
	if (password != null) {
	    props_a.put("password", password);
	}

        Connection result=null;

        Exception error=null;

        try {
            result = DriverManager.getConnection("jdbc:ucanaccess://" + database, props_a);
        } catch (SQLException ex) {
            error=ex;
        }

        if(result==null){
            System.out.println("trying (*.mdb, *.accdb) odbc driver...");
            try {
                result = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + database, props_a);
            } catch (SQLException ex) {
                System.err.println("neither mdb nor accdb supportet:");
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, error);
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
                result=null;
            }
        }

        if(result!=null){
            try {
                result.setReadOnly(readonly);
                result.setAutoCommit(autocom);
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;                
     }     

     private Connection openDBConnectionPOSTGRESQL(String database, String username, String password, boolean readonly, boolean autocom) throws Exception{
        Class.forName("org.postgresql.Driver");
        Connection result = DriverManager.getConnection ("jdbc:postgresql:"+database,username,password);
        result.setReadOnly(readonly);
        result.setAutoCommit(autocom); 
        return result;
     }    
    
     private Connection openDBConnectionORACLE(String database, String username, String password, boolean readonly, boolean autocom) throws Exception{
       Class.forName("oracle.jdbc.driver.OracleDriver");
       Connection result = DriverManager.getConnection ("jdbc:oracle:thin:@"+database,username,password);
       result.setReadOnly(readonly);
       result.setAutoCommit(autocom);   
       return result;
    }
     
     /*
      *database=host of the db +\+ the database name
      * eg: ldap/waldwachstum, waldwachstum, ww2005
     */
     private Connection openDBConnectionMYSQL(String database, String username, String password, boolean readonly, boolean autocom) throws Exception{
        Class.forName ("com.mysql.jdbc.Driver");
        props.setProperty("password",  password);
        props.setProperty("user", username);    		
        Connection result = DriverManager.getConnection("jdbc:mysql:"+database,props);
        result.setReadOnly(readonly);
        result.setAutoCommit(autocom);
        return result;      
     }
     
     
     public static String[] getAllTypes(){
         String[] result= new String[4];
         result[0]="ACCESS";
         result[1]="POSTGRESQL";
         result[2]="MYSQL";
         result[3]="ORACLE";
         return result;
     }
     
     public int getTypeByName(String typename){
        int result=-1;
        if(typename.compareToIgnoreCase("ACCESS")==0) return ACCESS;
        if(typename.compareToIgnoreCase("POSTGRESQL")==0) return POSTGRESQL;
        if(typename.compareToIgnoreCase("MYSQL")==0) return MYSQL;
        if(typename.compareToIgnoreCase("ORACLE")==0) return ORACLE;
        return result;
     }

     public Connection cloneAccessConection(Connection con){
         if(con!=null){
            try {
                String url= con.getMetaData().getURL();
                return openDBConnectionAccess(url, null, null, con.isReadOnly(), con.getAutoCommit());
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
         }else
             return null;
     }
}
