/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forestsimulator.DBAccess;
import java.sql.*;
import treegross.base.*;

/**
 *
 * @author nagel
 */
public class LoadProbekreis {
    
    public Stand loadFromDB(Connection dbconn, Stand st, String idx, int kreis, String idx2, int type  ){
        
        st.status=0;
        st.ntrees=0;
        st.nspecies=0;
        st.ncpnt=0;
        st.size=0.25;
        st.standname=idx+kreis;
        if (type > 1) st.size = 0.0530939;
// Stichjahr und BT         
        try{
             Statement stmt = dbconn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from tblDatPh2 where DatOrga_Key = \'"+idx+
                     "\' AND DatPh2_KSPNr = "+kreis+" ");
              if (rs.next()) {
                  int jahr = rs.getInt("DatPh2_STJ");
                  String bt = rs.getString("DatPh2_BT");
                  st.year = jahr;
                  st.bt = Integer.parseInt(bt);
              }
          }	catch (Exception e)  {	System.out.println(e); }
        if (type == 3){
        st.year = 1900;    
        try{
             Statement stmt = dbconn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from tblDatPh2 where DatOrga_Key = \'"+idx2+
                     "\' AND DatPh2_KSPNr = "+kreis+" ");
              if (rs.next()) {
                  int jahr = rs.getInt("DatPh2_STJ");
                  String bt = rs.getString("DatPh2_BT");
                  st.year = jahr;
                  st.bt = Integer.parseInt(bt);
              }
          }	catch (Exception e)  {	System.out.println(e); }
        
        }
        
        
// Probekreis aufbauen
        double xp=0.0; double yp=0.0; double radius=0.0;
        radius=Math.sqrt(10000.0*st.size/Math.PI);
        for (int i=0;i<18;i++){ 
            xp=radius+Math.sin(Math.PI*i*20.0/180.0)*radius;
            yp=radius+Math.cos(Math.PI*i*20.0/180.0)*radius;
            st.addcornerpoint("ECKP"+i,xp,yp,0.0);
        }
        st.center.no="circle";
        st.center.x =radius;
        st.center.y =radius;
        st.center.z =0.0;
// B�ume aufbauen
        try{
             Statement stmt = dbconn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from tblDatPh2_Vorr where DatOrga_Key = \'"+idx+
                     "\' AND DatPh2_KSPNr = "+kreis+
                           " order by DatPh2_Vorr_BestSchicht");
              while (rs.next()) {
                  
                int art = rs.getInt("DatPh2_Vorr_BA");
                int ax = (int) Math.floor(art/100);
                if (art-ax*100 > 0){
                   double win = rs.getDouble("DatPh2_Vorr_Richtung");
                   double ent = rs.getDouble("DatPh2_Vorr_Abstand");
                   double d2 = 0.0;
                   double d2k =0.0;
                   double k2 = 0.0;
                   double h2 = 0.0;
                   int out = -1;
                   if (type == 3){
                      Statement stmt2 = dbconn.createStatement();
                      ResultSet rs2 = stmt2.executeQuery("select * from tblDatPh2_Vorr where DatOrga_Key = \'"+idx2+
                           "\' AND DatPh2_KSPNr = "+kreis+
                           " AND DatPh2_Vorr_Richtung = "+win+" AND DatPh2_Vorr_Abstand = "+ent+" ");
                  
                      if (rs2.next()){
                          int artx = rs2.getInt("DatPh2_Vorr_BA");
                          d2 = rs2.getDouble("DatPh2_Vorr_BHD")/10.0;
                          d2k = rs2.getDouble("DatPh2_Vorr_BHDKlup")/10.0;
                          if (d2k > 0) d2= (d2+d2k)/2.0;
                          if (d2 < 1.0 || artx != art) out = st.year;
                          h2 = rs2.getDouble("DatPh2_Vorr_Hoehe")/10.0;
                          k2 = rs2.getDouble("DatPh2_Vorr_Krone")/10.0;
                      }
                   }
                   ent = ent/100.0;
                   
                   int alt = rs.getInt("DatPh2_Vorr_Alter");
                   double d1 = rs.getDouble("DatPh2_Vorr_BHD")/10.0;
                   double d1k = rs.getDouble("DatPh2_Vorr_BHDKlup")/10.0;
                   double d = d1;
                   if (d1k > 0) d= (d1+d1k)/2.0;
                   double h = rs.getDouble("DatPh2_Vorr_Hoehe")/10.0;
                   double ka = rs.getDouble("DatPh2_Vorr_Krone")/10.0;
                   String nr = rs.getString("DatPh2_Vorr_ID");
                   xp=radius+Math.sin(Math.PI*win/200.0)*ent;
                   yp=radius+Math.cos(Math.PI*win/200.0)*ent;
                   double fac = 88.419*st.size;
                   if (d >= 30.0) fac = 18.835*st.size;
                   if (type ==3){
                      if (d2 > d) {
                          d = d2;
                          h = h2;
                          ka = k2;
                      }
                   }
//
// halben Durchmesserzuwachs f�r ausscheidenden Bestand
                   if (d > 0.3 && out > -1 && type == 3){
                   if (art > 100 && art <200) d = ( d + 2.438+1.128*d -0.001505*d*d)/2.0;
                   if (art > 200 && art <300) d = ( d + 1.011+1.187*d -0.002268*d*d)/2.0;
                   if (art > 300 && art <400) d = ( d + 2.571+1.118*d -0.001435*d*d)/2.0;
                   if (art > 400 && art <500) d = ( d + 2.24715+1.0664*d -0.000764*d*d)/2.0;
                   if (art > 500 && art <600) d = ( d + 2.8312+1.162*d -0.0029129*d*d)/2.0;
                   if (art > 600 && art <700) d = ( d + 1.1182+1.4063*d -0.0048088*d*d)/2.0;
                   if (art > 700 && art <800) d = ( d - 0.709355+1.40345*d -0.006898*d*d)/2.0;
                   if (art > 800            ) d = ( d + 3.520399+1.095531*d -0.0012453*d*d)/2.0;
                   }
                   
                   if (art == 810) art=811;
                   if (art == 320) art=321;
                   if (art == 340) art=341;
                   if (art == 330) art=331;
                   if (art == 410) art=411;
                   if (art == 420) art=421;
                   
//
                   int anzahl=0;
// B�ume mit h�herem Repr�sentationsfaktor als 1 klonen
//
/*                   if (fac >= 1.0){
                      int az = (int) fac;
                      fac = fac /az;
                      anzahl = az;
                   }
*/
                   anzahl=1;
                   if (ka <= 0.1) ka=-9.0;
                   if (d > 1.3){
                     for( int i=0;i<anzahl;i++) {
                        String nrx = nr;
                        if (i > 0) nrx=nr+"_"+i;
                        if (i > 0) {
                            h = -9.0;
                            xp = -9.0;
                            yp = -9.0;
                        }
                        
                        st.addtreeNFV(art,nrx,alt,out, d,h,ka, -9.0,-9.0,xp,yp,-9.0,0,0,0,0, fac, ""); }
//                  if (out > 0) stl.tr[stl.ntrees-1].outtype=2;
                   } 
                }
             }
          }	catch (Exception e)  {	System.out.println(e); }


        
        return st;
    }
    
    public Stand loadAusscheidend(Stand st){
           
        
    return st;
    }

// F�r ElSalto Gemacht: Probefl�che mit 50 x 50m; Polar Grad
public Stand loadFromElSalto(Connection dbconn, Stand st,  int kreis ){
        
        st.status=0;
        st.ntrees=0;
        st.nspecies=0;
        st.ncpnt=0;
        st.size=0.25;
        st.standname="Plot:"+kreis;
// Stichjahr und BT         
        try{
             Statement stmt = dbconn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from SPIS WHERE Plot = "+kreis+" ");
              if (rs.next()) {
                  int jahr = rs.getInt("Aufnahmejahr");
                  st.year = jahr;
                  st.size = rs.getDouble("Flaechengrosse");
              }
          }	catch (Exception e)  
                {	System.out.println(e); }
       
        
// Probekreis aufbauen
/*        double xp=0.0; double yp=0.0; double radius=0.0;
        radius=Math.sqrt(10000.0*st.size/Math.PI);
        for (int i=0;i<18;i++){ 
            xp=radius+Math.sin(Math.PI*i*20.0/180.0)*radius;
            yp=radius+Math.cos(Math.PI*i*20.0/180.0)*radius;
            st.addcornerpoint("ECKP"+i,xp,yp,0.0);
        }
        st.center.no="circle";
        st.center.x =radius;
        st.center.y =radius;
        st.center.z =0.0;
 */
// Quadrat mit 50 x 50 m grad        
        double xp=0.0; double yp=0.0; double radius=0.0;
        radius=35.35;
        for (int i=0;i<4;i++){ 
            xp=25+Math.sin(Math.PI*((i*90.0)+45.0)/180.0)*radius;
            yp=25+Math.cos(Math.PI*((i*90.0)+45.0)/180.0)*radius;
            st.addcornerpoint("ECKP"+i,xp,yp,0.0);
        }
        st.center.no="polygon";
        st.center.x =25;
        st.center.y =25;
        st.center.z =0.0;        
        
        
// B�ume aufbauen
        try{
             Statement stmt = dbconn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from SPIS where Plot = "+kreis+" ");
              while (rs.next()) {
                  
                int art = rs.getInt("Artencode");
                double win = rs.getDouble("Gon");
                double ent = rs.getDouble("Entfernung");
                   int out = -1;
                   int alt = 17;
                   double d = rs.getDouble("BHD");
                   double h = rs.getDouble("Hoehe");
                   double ka = rs.getDouble("Kronenansatz");
                   double kb = rs.getDouble("Kronenbreite");
                   String nr = rs.getString("Nummer");
                   xp=25+Math.sin(Math.PI*win/180.0)*ent;
                   yp=25+Math.cos(Math.PI*win/180.0)*ent;
                   double fac = 1.0;
//
                   st.addtreeNFV(art,nr,alt,out, d,h,ka, kb,-9.0,xp,yp,-9.0,0,0,0,0, fac, ""); }
          }	catch (Exception e)  {	System.out.println(e); }
        return st;
    }

    
    
}
