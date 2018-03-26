/*
 * LoadTreegrossStand.java
 *
 * Created on 15. Februar 2006, 14:12
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.DBAccess;
import java.sql.*;
import treegross.base.*;
import treegross.treatment.*;
import treegross.random.RandomNumber;

/**
 *
 * @author nagel
 */
public class LoadTreegrossStand {
    
    int schritte =0;
    int ebaum = 0;
    int bestand =0;
    int baumart =0;
    int durchforstung_an =0;
    int scenario = 0;
    /** Creates a new instance of LoadTreegrossStand */
    public LoadTreegrossStand() {
    }

    public Stand loadFromDB(Connection dbconn,Stand stl, String idx,int selectedAufn, boolean missingDataAutomatisch,
                            boolean missingDataReplace){
       

        String flaechenName="";
        String abtName="";
        try{          
           Statement stmt = dbconn.createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Versfl where edv_id = \'"+idx.substring(0, 6)+"\'  ");
           if (rs.next()) {
              flaechenName= rs.getObject("forstamt").toString();
              abtName=rs.getObject("abt").toString();
           }
       	}	catch (Exception e)  {	System.out.println(e); }	
        java.io.File f= new java.io.File(" ");
        String localPath="";
        try { localPath= f.getCanonicalPath(); } catch ( Exception e){};
//        System.out.println("Load "+idx+" "+selectedAufn);
        String SAuf = new Integer(selectedAufn).toString();
        stl.addName(flaechenName+" "+abtName+" Auf: "+SAuf);
//                   (String)(jComboBox5.getSelectedItem()));
        try{          
           Statement stmt = dbconn.createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Auf where edvid = \'"+idx+"\' And auf = " + selectedAufn  );
           if (rs.next()) {
              stl.year = rs.getInt("Jahr");
              stl.addsize(rs.getDouble("flha"));
           }
       	}	catch (Exception e)  {	System.out.println(e); }	
        stl.status=0;

        stl.ntrees=0;
        stl.nspecies=0;
        stl.ncpnt=0;
        int ndh=0;
 // B�ume hinzuf�gen       
        try{          
           Statement stmt = dbconn.createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Baum where edvid = \'"+idx+"\' And auf = " + selectedAufn   );
           while (rs.next()) {
             String edv = rs.getObject("edvid").toString();
             edv=edv.trim();
             if (edv==null || edv.length()<1) break;
             int art = rs.getInt("art");
             String nr = rs.getObject("nr").toString();
             int anzahl = rs.getInt("anzahl");
             int age =(int)Math.round((rs.getDouble("alt")));
             String aus ="";
             Object auso=rs.getObject("a");
             if (auso != null) aus=auso.toString().trim();
//             System.out.println(nr);
             String ein ="";
             Object eino=rs.getObject("e");
             if (eino != null) ein=eino.toString().trim();
             int g0 = rs.getInt("g0");
             int g5 = rs.getInt("g5");
             int g10 = rs.getInt("g10");
             int g15 = rs.getInt("g15");
             int g20 = rs.getInt("g20");
             int g25 = rs.getInt("g25");
             int g30 = rs.getInt("g30");
             int g35 = rs.getInt("g35");
             double kb=(g0+g5+g10+g15+g20+g25+g30+g35)/40.0;
             String zfx ="";
             Object zfxo=rs.getObject("zf");
             if (zfxo != null) zfx=zfxo.toString().trim();
             int zf=0;
             if (zfx.compareTo("z")==0 || zfx.compareTo("Z")==0) zf=1;
             String oux ="";
             Object ouxo=rs.getObject("ou");
             if (ouxo != null) oux=ouxo.toString().trim();
             int ou=0;
             if (oux.indexOf("o")>-1 || oux.indexOf("O")>-1) ou=1;
             if (oux.compareTo("u")>-1 || oux.compareTo("U")>-1) ou=2;
             if (aus != null) aus=aus.trim() ;else aus="";
             String rx ="";
             Object rxo=rs.getObject("r");
             if (rxo != null) rx=rxo.toString().trim();
             int r=0; 
             if (rx.compareTo("r")==0 || rx.compareTo("R")==0) r=1;
             if (ein != null) ein=ein.trim(); else ein="";
             aus=aus.trim();
             int out=-1;
// Der einfache Einwachser wurde mit -99 kodiert, jetzt als Einw
// Der einfache ausscheidende mit der Jahreszahl             
             String rm="";
             if (aus.trim().length()>0) 
                              out=stl.year;
             if (ein.trim().length()>0 ) rm="Einw";
//           double d = ex.getDouble("dmess")/10.0;
             double d = rs.getDouble("d")/10.0;
             double h = rs.getDouble("h")/10.0;
             double ka = rs.getDouble("k")/10.0;
             double fac = rs.getDouble("repfl");
              if (anzahl==0) {
                 anzahl=1; fac=0.0;}
// B�ume mit h�herem Repr�sentationsfaktor als 1 klonen
//
             if (fac >= 2.0){
                 int az = (int) fac;
                 fac = fac /az;
                 anzahl = anzahl*az;
             }
             if (r==1) 
             fac=0;
             int nx=0;
             if (h > 0) ndh=ndh+1;
             if (d > 0){
               for( int i=0;i<anzahl;i++) {
                  String nrx = nr;
                  if (i > 0) nrx=nr+"_"+i;
                  stl.addtreeNFV(art,nrx,age,out, d,h,ka, kb,-9.0,-9.0,-9.0,-9.0,zf,nx,nx,ou, fac, rm); }
                  if (out > 0) stl.tr[stl.ntrees-1].outtype=2;
             } 
          }
       	}	catch (Exception e)  {	System.out.println(e); }	
             System.out.println("fertig");
  
//
//  koordinaten hinzuf�gen
//        
           try{          
              Statement stmt = dbconn.createStatement(); 
              ResultSet rs = stmt.executeQuery("select * from Stammv where edvid = \'"+idx+"\' ORDER BY nr");
              while (rs.next()) {
                double xp = rs.getDouble("x");
                double yp = rs.getDouble("y");
                String nox = rs.getObject("nr").toString();
                nox=nox.trim();
                int artx = rs.getInt("art");
                for (int i=0; i<stl.ntrees;i++) {
                   if ( (nox.compareTo(stl.tr[i].no.trim())==0) && (artx==stl.tr[i].code)){
                       stl.tr[i].x=xp;
                       stl.tr[i].y=yp;
                   }
                }
             }
          }	catch (Exception e)  {	System.out.println(e); }	
//
//  Eckpunkte hinzuf�gen
//
           try{
              Statement stmt = dbconn.createStatement();
              ResultSet rs = stmt.executeQuery("select * from Stammv where edvid = \'"+idx+"\' order by nr");
              while (rs.next()) {
                double xp = rs.getDouble("x");
                double yp = rs.getDouble("y");
                String nox = rs.getObject("nr").toString();
                nox=nox.trim();
                if (nox.indexOf("ECK") > -1) {
                       stl.addcornerpoint(nox, xp, yp, 0.0);
                       stl.center.no="polygon";
                }
             }
          }	catch (Exception e)  {	System.out.println(e); }
        
//data quality
/*            for (int i=0; i<stl.ntrees;i++) {
                if (stl.tr[i].d > 0) stl.tr[i].remarks=stl.tr[i].remarks+"D;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"d";
                if (stl.tr[i].h > 0) stl.tr[i].remarks=stl.tr[i].remarks+"H;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"h;";
                if (stl.tr[i].cb > 0) stl.tr[i].remarks=stl.tr[i].remarks+"A;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"a;";
                if (stl.tr[i].cw > 0) stl.tr[i].remarks=stl.tr[i].remarks+"B;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"b;";
                if (stl.tr[i].x > -9.0 && stl.tr[i].y > -9.0 ) stl.tr[i].remarks=stl.tr[i].remarks+"X";
                else stl.tr[i].remarks=stl.tr[i].remarks+"x";
            }
 */
// an dieser Stelle werden alle gemessenen H�hen nach hmeasured in Tree gespeichert.
// Dadurch wird es m�glich sp�ter verschiedene H�henkurven zu erzeugen           
/*        for (int i=0;i<stl.ntrees;i++) if(stl.tr[i].h > 0) 
            stl.tr[i].hMeasuredValue=stl.tr[i].h; else stl.tr[i].hMeasuredValue=0.0;
       if (missingDataReplace)
             for (int i=0;i<stl.ntrees;i++) if(stl.tr[i].h > 0) stl.tr[i].h=0.0;
 */
//
// replace missing data at all             
//       if (missingDataReplace){      
// update missing data automatically
/*        if (missingDataAutomatisch!= true) {
            MissingDataDialog mdDialog = new MissingDataDialog(jFrame,true,stl);
            mdDialog.setVisible(true);
        }
        else {  
            GenerateMissingHeights gmh = new GenerateMissingHeights();
            gmh.replaceMissingHeights(stl,true);
        }
*/
/*        stl.sortbyd();
        stl.missingData();
        stl.descspecies();
        int kk=0;
        GenerateXY genxy = new GenerateXY();
        genxy.zufall(stl);
        stl.descspecies();
       }
 */
// Ersatz fehlender Daten Ende             
        int nxx=0;
        for (int i=1;i<stl.ntrees;i++) if(stl.tr[i].ou==2) nxx=nxx+1;
        return stl;
    }
    
    public Stand loadRules(Connection dbconn,Stand stl, String idx,int auf, Treatment2 t2, int scen){
        durchforstung_an =0;
        try{          
           Statement stmt = dbconn.createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Vorschrift where (edvid = \'"+idx+" \' AND auf = "+auf+" AND Szenario = "+scen+" )");
           if (rs.next()) {
              int jj = rs.getInt("Zufall");
              if (jj == 0) stl.random.setRandomType(RandomNumber.OFF);else stl.random.setRandomType(RandomNumber.PSEUDO);
                  jj = rs.getInt("Einwuchs");
              if (jj == 0) stl.ingrowthActive=false; else stl.ingrowthActive=true;
                  stl.temp_Integer = rs.getInt("Schritte");
              ebaum = rs.getInt("EBaum");
              bestand = rs.getInt("Bestand");
              baumart = rs.getInt("Baumart");
              durchforstung_an = rs.getInt("Durchforstung");
              if (scen ==0) scenario = scen; else scenario = rs.getInt("Szenario");


           }
       	 }   catch (Exception e)  {	System.out.println(e); }
        
        if (durchforstung_an == 1 && scenario > 0){
              stl.distanceDependent=true;
              stl.trule.typeOfThinning=0;
              stl.trule.thinArea=false;
              stl.trule.selectCropTrees=true;
              stl.trule.reselectCropTrees=true;
              stl.trule.selectCropTreesOfAllSpecies=false;
              stl.trule.releaseCropTrees=true;
              stl.trule.cutCompetingCropTrees=false;
              stl.trule.releaseCropTreesSpeciesDependent =true;
              stl.trule.minThinningVolume=0;
              stl.trule.maxThinningVolume=80;
              stl.trule.thinAreaSpeciesDependent =true;
              stl.trule.thinningIntensityArea=0.0;
              stl.trule.minHarvestVolume=0.0;
              stl.trule.maxHarvestVolume=250.0;
              stl.trule.typeOfHarvest=0;
              stl.trule.harvestLayerFromBelow =false;
              stl.trule.maxHarvestingPeriode=6; 
              stl.trule.lastTreatment =0;
              stl.trule.protectMinorities=false;
              stl.trule.nHabitat=0;
              stl.trule.thinningIntensity = 1.0;
              loadScenario(dbconn, stl, t2, scenario);
        }
        
        
        return stl;
 
    }

    public void loadScenario(Connection dbconn, Stand st, Treatment2 t2, int scenarioNo){
        int szArtIndex = 0;
        try{
           Statement stmt = dbconn.createStatement();
           ResultSet rs = stmt.executeQuery("select * from Szenario where (SzenarioNr = "+scenarioNo+"  )");
           if (rs.next()) {

//               ebaum = rs.getInt("EBaum");
// Skidtrails
              boolean skidtrails = rs.getBoolean("Skidtrails");
              double skidtrailDistance = rs.getDouble("SkidtrailDistance");
              double skidtrailWidth = rs.getDouble("SkidtrailWidth");
              t2.setSkidTrails(st,skidtrails,skidtrailDistance,skidtrailWidth);
// Set thinning  and intensity
           int thType=rs.getInt("ThinningType");
           double thIntensity=rs.getDouble("ThinningIntensity");
           double thVolMin=rs.getDouble("ThinningVolumeMin");
           double thVolMax=rs.getDouble("ThinningVolumeMax");
           boolean ctreesOnly=rs.getBoolean("ThinningCropTreeOnly");
           t2.setThinningRegime(st, thType, thIntensity, thVolMin, thVolMax, ctreesOnly) ;
// set Harvesting Regime
           int hvType=rs.getInt("HarvestType");
           double hvVolMin=rs.getDouble("HarvestVolumeMin");
           double hvVolMax=rs.getDouble("HarvestVolumeMax");
           double hvFinalCut=rs.getDouble("HarvestFinalCut");
           Object hvp = rs.getObject("HarvestProcess");
           String hvProcess = "";
           if (hvp != null) hvProcess=hvp.toString();
           t2.setHarvestRegime(st, hvType, hvVolMin, hvVolMax, hvFinalCut, hvProcess);
// Set nature conversation
           int haType=rs.getInt("HabitatType");
           int haTrees=rs.getInt("HabitatTrees");
           boolean haMino = rs.getBoolean("HabitatMinority");
           double haMinCov=rs.getDouble("HabitatMinCoverage");
           int haBHD=rs.getInt("HabitatBHDProtect");
           t2.setNatureProtection(st, haTrees, haType, haMino, haMinCov,haBHD);
// Planting rules
           boolean pl = rs.getBoolean("Planting");
           boolean plRemove = rs.getBoolean("PlantingRemoveAll");
           double plStart=rs.getDouble("PlantingStart");
           Object pls = rs.getObject("PlantSpecies");
           String plSpecies = "";
           if (pls != null) plSpecies=pls.toString();
//
           szArtIndex=rs.getInt("SzenarioArtIndex");
           t2.setAutoPlanting( st, pl,  plRemove, plStart, plSpecies);
           }
     	 }   catch (Exception e)  {	System.out.println(e); }

      try{
           Statement stmt = dbconn.createStatement();
           ResultSet rs2 = stmt.executeQuery("select * from SzenarioArt where (SzenarioArtIndex = "+szArtIndex+"  )");
           while (rs2.next()) {
                int artx=rs2.getInt("Code");
                int heightx=rs2.getInt("height");
                int targetx=rs2.getInt("targetDBH");
                int cropx=rs2.getInt("CropTrees");
                int mixx=rs2.getInt("Mix");
                int merk = -9;
                String moderateTh = rs2.getObject("ModerateThinning").toString();
                for (int j = 0; j < st.nspecies; j++){
                      if (st.sp[j].code == artx) merk= j;
                      }
                if (merk > -9){
                st.sp[merk].trule.minCropTreeHeight = heightx;
                st.sp[merk].trule.targetDiameter = targetx;
                double mixxx = mixx*1.0;
                st.sp[merk].trule.targetCrownPercent = mixxx;
                st.sp[merk].trule.numberCropTreesWanted = cropx;
                st.sp[merk].spDef.moderateThinning  = moderateTh;
                System.out.println("springt raus");
             }
          System.out.println("springt raus");

           }
       
     	 }   catch (Exception e)  {	System.out.println(e); }



    }

    
    public int getEBaum(){return ebaum;}
    public int getBestand(){return bestand;}
    public int getBaumart(){return baumart;}
    public int getDurchf(){return durchforstung_an;}
    
    public void saveBaum(Connection dbconn,Stand st, String ids, int aufs, int sims, int nwieder){
        try{
           Statement stmt = dbconn.createStatement();
           for (int i=0;i <st.ntrees; i++){
               if (st.tr[i].no.indexOf("_")<0){
               Double dd= st.tr[i].d;
               Double hh= st.tr[i].h;
               Double ka= st.tr[i].cb;
               Double kb= st.tr[i].cw;
               Double vv= st.tr[i].v;
               Double cc66= st.tr[i].c66;
               Double cc66c= st.tr[i].c66c;
               Double cc66xy= st.tr[i].c66xy;
               Double cc66cxy= st.tr[i].c66cxy;
               Double ssi= st.tr[i].si;
               Double xx = st.tr[i].x;
               Double yy = st.tr[i].y;
               int zbx = 0;
               if (st.tr[i].crop==true)zbx=1;
               
               
           
               stmt.execute("INSERT INTO ProgBaum (  edvid, auf, simschritt, wiederholung,szenario,nr, art, alt, aus, d, h, ka, kb, v, c66,c66c, c66xy, c66cxy, si,x,y, zb) "+
                     "values (  '"+ids+"', "+aufs+", "+sims+", "+nwieder+", "+scenario+",'"+st.tr[i].no+"',"+st.tr[i].code+", "+
                      st.tr[i].age+" , "+st.tr[i].out+" , "+dd.toString()+" , "+hh.toString()+
                      " , "+ka.toString()+" , "+kb.toString()+" , "+vv.toString()+
                      " , "+cc66.toString()+" , "+cc66c.toString()+
                      " , "+cc66xy.toString()+" , "+cc66cxy.toString()+" , "+ssi.toString()+" , "+xx.toString()+" , "+yy.toString()+
                      ", "+zbx+" )");
           }
           }
        
           
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }
    public void saveSpecies(Connection dbconn,Stand st, String ids, int aufs, int sims, int nwieder){
        try{
           Statement stmt = dbconn.createStatement();
           for (int i=0; i< st.nspecies;i++){
               Double ggha= st.sp[i].gha;
               Double vvha= st.sp[i].vol;
               Double ddg= st.sp[i].dg;
               Double hhg= st.sp[i].hg;
               Double dd100= st.sp[i].d100;
               Double hh100= st.sp[i].h100;
               Double nnha= st.sp[i].nha;
               Double nnhaa= st.sp[i].nhaout;
               Double gghaa= st.sp[i].ghaout;
               Double aalt= st.sp[i].h100age;
               Double vvhaa = st.sp[i].vhaout;
               Double gpro = 100.0*st.sp[i].gha/st.bha;
               int art = st.sp[i].code;
               
           
               stmt.execute("INSERT INTO ProgArt (  edvid, auf, art, wiederholung,szenario, gpro, simschritt, alt, nha, gha, vha,"+
                       "dg,hg,d100,h100,nhaa,ghaa,vhaa) "+
                     "values (  '"+ids+"', "+aufs+", "+art+", "+nwieder+", "+scenario+", "+gpro+", "+sims+","+aalt+","+nnha+", "+
                      ggha+" , "+vvha+" , "+ddg+
                      " , "+hhg+" , "+dd100+" , "+hh100+
                      " , "+nnhaa+" , "+gghaa+" ,"+vvhaa+" )"); 
        
           }
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }
    public void saveSpeciesV2(Connection dbconn,Stand st, String ids, int aufs, int sims, int nwieder){
        try{
           Statement stmt = dbconn.createStatement();
           for (int i=0; i< st.nspecies;i++){
               Double ggha= st.sp[i].gha;
               Double vvha= st.sp[i].vol;
               Double ddg= st.sp[i].dg;
               Double hhg= st.sp[i].hg;
               Double dd100= st.sp[i].d100;
               Double hh100= st.sp[i].h100;
               Double nnha= st.sp[i].nha;
               Double nnhaa= st.sp[i].nhaout;
               Double gghaa= st.sp[i].ghaout;
               Double aalt= st.sp[i].h100age;
               Double vvhaa = st.sp[i].vhaout;
               Double gpro = 100.0*st.sp[i].gha/st.bha;
               int art = st.sp[i].code;
               
               
// SUMME Grundfl�che und Volumen der Nutzung  
               vvhaa =0.0;
               gghaa =0.0;
               nnhaa =0.0;
               for (int ik =0; ik <st.ntrees ; ik++){
                  if (st.tr[ik].out > 0 && st.tr[ik].code==st.sp[i].code){
                    nnhaa = nnhaa + 1.0*st.tr[ik].fac/st.size;
                    gghaa = gghaa + Math.PI*Math.pow((st.tr[ik].d/200),2.0)*st.tr[ik].fac/st.size;
                    vvhaa = vvhaa + st.tr[ik].v*st.tr[ik].fac/st.size;
                  }
               }

               
           
               stmt.execute("INSERT INTO ProgArt (  edvid, auf, art, wiederholung,szenario, gpro, simschritt, alt, nha, gha, vha,"+
                       "dg,hg,d100,h100,nhaa,ghaa,vhaa) "+
                     "values (  '"+ids+"', "+aufs+", "+art+", "+nwieder+", "+scenario+", "+gpro+", "+sims+","+aalt+","+nnha+", "+
                      ggha+" , "+vvha+" , "+ddg+
                      " , "+hhg+" , "+dd100+" , "+hh100+
                      " , "+nnhaa+" , "+gghaa+" ,"+vvhaa+" )"); 
        
           }
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }

    
    public void saveStand(Connection dbconn,Stand st, String ids, int aufs, int sims,int nwieder){
        try{
           Statement stmt = dbconn.createStatement();
               st.descspecies();
               Double ggha= st.bha;
               Double vvha= st.getVhaResidual(0);
               Double ddg= st.dg;
               Double hhg= st.hg;
               Double dd100= st.d100;
               Double hh100= st.h100;
               Double nnha= st.nha;
               Double nnhaa= st.nhaout;
               Double gghaa= st.bhaout;
               Double aalt= 1.0*st.year;
               Double vvhaa = st.getVhaTargetDiameter(0)+st.getVhaThinning(0);
               double vvhaaz = st.getVhaTargetDiameter(0);
           
               stmt.execute("INSERT INTO ProgBestand (  edvid, auf, simschritt, wiederholung, szenario, alt, nha, gha, vha,"+
                       "dg,hg,d100,h100,nhaa,ghaa,vhaa, vhaazst) "+
                     "values (  '"+ids+"', "+aufs+", "+sims+", "+nwieder+", "+scenario+","+aalt+","+nnha+", "+
                      ggha+" , "+vvha+" , "+ddg+
                      " , "+hhg+" , "+dd100+" , "+hh100+
                      " , "+nnhaa+" , "+gghaa+" ,"+vvhaa+" ,"+vvhaaz+" )");
        
           
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }
    
        public void saveStandV2(Connection dbconn,Stand st, String ids, int aufs, int sims,int nwieder,int scena ){
        try{
           Statement stmt = dbconn.createStatement();
               st.descspecies();
               Double ggha= st.bha;
               Double vvha= st.getVhaResidual(0);
               Double ddg= st.dg;
               Double hhg= st.hg;
               Double dd100= st.d100;
               Double hh100= st.h100;
               Double nnha= st.nha;
               Double nnhaa= st.nhaout;
               Double gghaa= st.bhaout;
               Double aalt= 1.0*st.year;
               Double vvhaa = st.getVhaTargetDiameter(0)+st.getVhaThinning(0);
               double vvhaaz = st.getVhaTargetDiameter(0);
               
               // SUMME Grundfl�che und Volumen der Nutzung  
               vvhaa =0.0;
               vvhaaz =0.0;
               gghaa =0.0;
               for (int ik =0; ik <st.ntrees ; ik++){
                  if (st.tr[ik].out > 0){
                    gghaa = gghaa + Math.PI*Math.pow((st.tr[ik].d/200),2.0)*st.tr[ik].fac/st.size;
                    vvhaa = vvhaa + st.tr[ik].v*st.tr[ik].fac/st.size;
                    if (st.tr[ik].outtype==1) vvhaaz = vvhaaz + st.tr[ik].v*st.tr[ik].fac/st.size;
                  }
               }

           
               stmt.execute("INSERT INTO ProgBestand (  edvid, auf, simschritt, wiederholung, szenario, alt, nha, gha, vha,"+
                       "dg,hg,d100,h100,nhaa,ghaa,vhaa, vhaazst) "+
                     "values (  '"+ids+"', "+aufs+", "+sims+", "+nwieder+", "+scena+","+aalt+","+nnha+", "+
                      ggha+" , "+vvha+" , "+ddg+
                      " , "+hhg+" , "+dd100+" , "+hh100+
                      " , "+nnhaa+" , "+gghaa+" ,"+vvhaa+" ,"+vvhaaz+" )");
        
           
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }

    public void saveXMLToDB(Connection dbconn,Stand st){
       try{
           Statement stmt = dbconn.createStatement();
                    
          for (int i=0;i<st.ncpnt;i++) {
                  Double xx = new Double(st.cpnt[i].x);
                  Double yy = new Double(st.cpnt[i].y);
                  String nrx = "ECK"+i;
                  stmt.execute("INSERT INTO Stammv ( edvid, nr, art, auf, x, y, z) "+
                              "values (  '"+st.standname+"','"+nrx+"', -99, 1, "+
                              xx.toString()+" , "+yy.toString()+", 0.0)"); 

            }
           for (int i=0;i<st.ntrees;i++) {
                  Double xx = new Double(st.tr[i].x);
                  Double yy = new Double(st.tr[i].y);
                  stmt.execute("INSERT INTO Stammv ( edvid, nr, art, auf, x, y, z) "+
                              "values (  '"+st.standname+"','"+st.tr[i].no+"',"+st.tr[i].code+",1, "+
                              xx.toString()+" , "+yy.toString()+", 0.0)"); 
                 }
           for (int i=0;i<st.ntrees;i++) {
                  int dd = (int)(Math.round(st.tr[i].d*10));
                  int hh = (int)(Math.round(st.tr[i].h*10));
                  int ka = (int)(Math.round(st.tr[i].cb*10));
                  int kb = (int)(Math.round(st.tr[i].cw*5));
                  String aus = "";
                  if (st.tr[i].out > 0) aus="1";
                          stmt.execute("INSERT INTO Baum ( edvid, nr, art, auf, anzahl, repfl, mh, alt, dmess,d,"+
                               " h, k, g0, g5, g10, g15, g20, g25, g30, g35, a ) "+
                              "values (  '"+st.standname+"','"+st.tr[i].no+"',"+st.tr[i].code+",1,1,1,13, "+
                              st.tr[i].age+","+dd+","+dd+","+hh+", "+ka+", "+kb+","+kb+","+kb+","+kb+","+
                              kb+","+kb+","+kb+","+kb+",'"+aus+"' )");
                 }
// In auf Datei schreiben
           String idx = st.standname+" 1";
           
           stmt.execute("INSERT INTO Auf ( id, edvid, auf, monat, jahr, flha ) "+
                              "values (  '"+idx+"','"+st.standname+"',1,1,"+st.year+","+st.size+" )");

                }
                catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }

    }
    
           public Stand addLayerFromStartwert(Connection dbconn,Stand st, int bestand, int art, double mix){
           double hg =0.0;
           double dg= 0.0;
           double d100= 0.0;
           double g =0.0;
           double h100 =0.0;
           int alt=0;
           double yc =0;
           int szNr =0;
           try{          
              Statement stmt = dbconn.createStatement(); 
              ResultSet rs = stmt.executeQuery("select * from Startwerte where ( Bestand = "+bestand+" AND Art = "+art+" )");
              if (rs.next()) {
                 hg = Double.parseDouble(rs.getObject("Hg").toString());
                 dg = Double.parseDouble(rs.getObject("Dg").toString());
                 d100 = Double.parseDouble(rs.getObject("Dmax").toString());
                 h100 = Double.parseDouble(rs.getObject("H100").toString());
                 g = Double.parseDouble(rs.getObject("G").toString());
                 alt = (int)(Double.parseDouble(rs.getObject("Alter").toString()));
                 art = (int)(Double.parseDouble(rs.getObject("Art").toString()));
              }
           }	catch (Exception e)  {	System.out.println(e); }
//
//
           if ( hg > 5.0 && dg > 7.0 && g > 0.0){
           try {
               GenDistribution gdb = new GenDistribution();
               gdb.weibull(st, art, alt, dg, hg, d100, g * st.size*mix,false);
// missing data fuer die Verteilung generieren
//                for (int j = 0; j < st.ntrees; j++) {
//                  if (st.tr[j].si <= -9) st.tr[j].si = yc;
//                  }
                SIofDistrib siod = new SIofDistrib();
                FunctionInterpreter fi = new FunctionInterpreter();
                siod.si(st, art, alt, dg, hg);
                for (int j = 0; j < st.ntrees; j++) {
                   if (st.tr[j].h == 0.0) {
                      Tree tree = new Tree();
                      tree.code = art;
                      tree.sp = st.tr[j].sp;
                      tree.sp.dg = dg;
                      tree.sp.hg = hg;
                      tree.sp.h100 = 0.0;
                      tree.sp.d100 = 0.0;
                      tree.d = st.tr[j].d;
                      tree.code = st.tr[j].code;
                      tree.sp = st.tr[j].sp;
                      tree.st = st;
//                                st.tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML) + fi.getValueForTree(tree, tree.sp.spDef.heightVariationXML) * nd.value(3.0);
                      st.tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML) ;
                   }
                }
                for (int j = 0; j < st.ntrees; j++) {
                      st.tr[j].setMissingData();
                }
                GenerateXY gxy = null;
                gxy = new GenerateXY();
                gxy.setGroupRadius(0.0);
                gxy.zufall(st);
                st.sortbyd();
                st.descspecies();
                } catch (Exception ex) {}
           
           }
          
           return st;
       } 
  
      public Stand newStand(Stand st, String name, double size){
                 st.newStand();
                 st.ncpnt=0;
                 st.nspecies=0;
                 st.ntrees=0;
                 st.addsize(size);
                 st.standname=name;
                 st.year=2014;
                 double len = Math.sqrt(10000*st.size);  
                 st.addcornerpoint("ECK1",0.0,0.0,0.0);
                 st.addcornerpoint("ECK2",0.0,len,0.0);
                 st.addcornerpoint("ECK3",len,len,0.0);
                 st.addcornerpoint("ECK4",len,0.0,0.0);
                 st.center.no="polygon";
                 st.center.x =len/2.0;
                 st.center.y =len/2.0;
                 st.center.z =0.0;
           return st;
       } 
           
    
    
}
