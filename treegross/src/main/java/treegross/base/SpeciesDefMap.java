/*
 * this class stores all species definitions
 * read from species.txt oder species.xml
 *
 * remember change SpecedDef.java too!!
 *  -> remove load methods
 *
 * change stand.java too
 *  -> remove field speciesfile and add pointer to this class
 *  -> if a new species appears do not load from file but from this class
 */

package treegross.base;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.*;
import org.jdom.DocType;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 *
 * @author jhansen
 */
public class SpeciesDefMap {
    private HashMap<Integer,SpeciesDef> spcdef;
    private URL actualurl;
    private boolean loaded=false;    
    private final String stdModel="ForestSimulatorNWGermanyBC4";
    
    private final static Logger LOGGER = Logger.getLogger(SpeciesDefMap.class.getName());

    public SpeciesDefMap(){
        spcdef = null;
        loaded=false;
        actualurl=null;
    }
    
    public void reload(){
        if(loaded && actualurl!=null){
            readFromURL(actualurl);
        }
    }
    
    public void readInternal(String name){
        URL url;
        if(name!=null)
            url=getClass().getResource("/treegross/model/"+name+".xml");
        else
            url=getClass().getResource("/treegross/model/"+stdModel+".xml");
        readFromURL(url);
    }
    
    public void readFromPath(String path){
        try{
            URL url = new File(path).toURI().toURL();
            readFromURL(url);            
        } catch(MalformedURLException e){
            LOGGER.log(Level.SEVERE, "reading xml file: ",e);
        }        
    }

    public void readFromURL(URL url){
        try{
           readXML(url);   
           actualurl=url;
        } catch(IOException e){
            LOGGER.log(Level.SEVERE, "reading xml file: ",e);
        } catch (JDOMException e) {
            LOGGER.log(Level.SEVERE, "reading xml file: ",e);
        }
    }

    private void readXML(URL url) throws IOException, org.jdom.JDOMException{
        URLConnection urlcon = url.openConnection();
        actualurl=url;
        readXMLStream(urlcon.getInputStream());
    }

    public void readXMLStream(InputStream imps) throws IOException, org.jdom.JDOMException{
        spcdef = new HashMap<Integer,SpeciesDef>();        
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(imps);         
        DocType docType = doc.getDocType();   
        Element rm =  doc.getRootElement();  
        List  list= rm.getChildren("SpeciesDefinition");
        Iterator i = list.iterator();        
        Element def;
        int code, m, handledLikeCode;
        
        while (i.hasNext()) {            
            def = (Element)i.next();
            code =Integer.parseInt(def.getChild("Code").getText());            
            handledLikeCode = Integer.parseInt(def.getChild("HandledLikeCode").getText());
            spcdef.put(code,new SpeciesDef());
            SpeciesDef actual=spcdef.get(code);
            define(code, actual, def, handledLikeCode);
            if(code!=handledLikeCode){  
                boolean found=false;
                Iterator j = list.iterator();       
                while(j.hasNext() && !found){
                    Element parent_def = (Element)j.next();
                    int code_parent = Integer.parseInt(parent_def.getChild("Code").getText());
                    if (handledLikeCode == code_parent){
                        overload(actual, parent_def);
                        found=true;
                    }
                }
            }
            m = actual.colorXML.indexOf(";");
            actual.colorRed=Integer.parseInt(actual.colorXML.substring(0,m));
            actual.colorXML=actual.colorXML.substring(m+1);
            m = actual.colorXML.indexOf(";");
            actual.colorGreen=Integer.parseInt(actual.colorXML.substring(0,m));
            actual.colorXML=actual.colorXML.substring(m+1);
            actual.colorBlue=Integer.parseInt(actual.colorXML);
            actual.setDefined(true);
            //System.out.println(actual.toString());
        }
        loaded=true;
    } 
    
    private void overload(SpeciesDef actual, Element with){
         if (actual.uniformHeightCurveXML.toString().length()==0)
             actual.uniformHeightCurveXML = initTGFunction(with.getChild("UniformHeightCurveXML").getText());
         if (actual.heightVariationXML.toString().length()==0)
             actual.heightVariationXML = initTGFunction(with.getChild("HeightVariation").getText());
         if (actual.diameterDistributionXML.toString().length()==0)
             actual.diameterDistributionXML = initTGFunction(with.getChild("DiameterDistributionXML").getText());
         if (actual.volumeFunctionXML.toString().length()==0)
             actual.volumeFunctionXML = initTGFunction(with.getChild("VolumeFunctionXML").getText());
         if (actual.crownwidthXML.toString().length()==0)
             actual.crownwidthXML = initTGFunction(with.getChild("Crownwidth").getText());
         if (actual.crownbaseXML.toString().length()==0)
             actual.crownbaseXML = initTGFunction(with.getChild("Crownbase").getText());
         if (actual.siteindexXML.toString().length()==0)
             actual.siteindexXML = initTGFunction(with.getChild("SiteIndex").getText());
         if (actual.siteindexHeightXML.toString().length()==0)
             actual.siteindexHeightXML = initTGFunction(with.getChild("SiteIndexHeight").getText());
         if (actual.potentialHeightIncrementXML.toString().length()==0)
             actual.potentialHeightIncrementXML = initTGFunction(with.getChild("PotentialHeightIncrement").getText());
         if (actual.heightIncrementXML.toString().length()==0)
             actual.heightIncrementXML = initTGFunction(with.getChild("HeightIncrement").getText());
         if (actual.diameterIncrementXML.toString().length()==0)
             actual.diameterIncrementXML = initTGFunction(with.getChild("DiameterIncrement").getText());
         if (actual.maximumDensityXML.toString().length()==0)
             actual.maximumDensityXML =initTGFunction(with.getChild("MaximumDensity").getText());
         if (actual.decayXML.toString().length()==0)
             actual.decayXML = initTGFunction(with.getChild("Decay").getText());
         
         if (actual.crownType < 0) actual.crownType =Integer.parseInt(with.getChild("CrownType").getText());
         if (actual.heightIncrementError < 0) actual.heightIncrementError = Double.parseDouble(with.getChild("HeightIncrementError").getText());
         if (actual.diameterIncrementError < 0) actual.diameterIncrementError = Double.parseDouble(with.getChild("DiameterIncrementError").getText());
         if (actual.maximumAge < 0) actual.maximumAge =Integer.parseInt(with.getChild("MaximumAge").getText());
         if (actual.ingrowthXML.trim().length() < 1) actual.ingrowthXML = with.getChild("Ingrowth").getText();
         if (actual.heightCurve < 0) actual.heightCurve =Integer.parseInt(with.getChild("HeightCurve").getText());
         if (actual.targetDiameter < 0) actual.targetDiameter =Double.parseDouble(with.getChild("TargetDiameter").getText());
         if (actual.cropTreeNumber < 0) actual.cropTreeNumber =stripCommentsFromInt(with.getChild("CropTreeNumber").getText(),100);
         if (actual.heightOfThinningStart < 0) actual.heightOfThinningStart =Double.parseDouble(with.getChild("HeightOfThinningStart").getText());
         if (actual.moderateThinning.trim().length() < 1) actual.moderateThinning = with.getChild("ModerateThinning").getText();
         if (actual.colorXML.trim().length() < 1) actual.colorXML = with.getChild("Color").getText();
         if (actual.competitionXML.trim().length() < 1) actual.competitionXML = with.getChild("Competition").getText();
         if (actual.taperFunctionXML.trim().length() < 1) actual.taperFunctionXML = with.getChild("TaperFunction").getText();
         if (actual.stemVolumeFunctionXML.trim().length() < 1){
            try{
                actual.stemVolumeFunctionXML = with.getChild("StemVolumeFunction").getText();
            }catch (Exception e){
                System.out.println("Schaftholz ist: "+actual.stemVolumeFunctionXML);
            }
         }
         if (actual.coarseRootBiomass.trim().length() < 1) actual.coarseRootBiomass = with.getChild("CoarseRootBiomass").getText();
         if (actual.smallRootBiomass.trim().length() < 1) actual.smallRootBiomass = with.getChild("SmallRootBiomass").getText();
         if (actual.fineRootBiomass.trim().length() < 1) actual.fineRootBiomass = with.getChild("FineRootBiomass").getText();
         if (actual.totalRootBiomass.trim().length() < 1) actual.totalRootBiomass = with.getChild("TotalRootBiomass").getText();                   
    }
    
    private void define(int code, SpeciesDef actual, Element def, int hlc){
        actual.code=code;
        actual.handledLikeCode=hlc;
        actual.shortName = def.getChild("ShortName").getText();
        actual.longName = def.getChild("LongName").getText();
        actual.latinName = def.getChild("LatinName").getText();
        actual.internalCode =Integer.parseInt(def.getChild("InternalCode").getText());
        actual.codeGroup =Integer.parseInt(def.getChild("CodeGroup").getText());
        actual.heightCurve =Integer.parseInt(def.getChild("HeightCurve").getText());
        actual.crownType =Integer.parseInt(def.getChild("CrownType").getText());
        actual.heightIncrementError = Double.parseDouble(def.getChild("HeightIncrementError").getText());
        actual.diameterIncrementError = Double.parseDouble(def.getChild("DiameterIncrementError").getText());
        actual.maximumAge =Integer.parseInt(def.getChild("MaximumAge").getText());
        actual.ingrowthXML = def.getChild("Ingrowth").getText();
        actual.targetDiameter =Double.parseDouble(def.getChild("TargetDiameter").getText());
        actual.cropTreeNumber=stripCommentsFromInt(def.getChild("CropTreeNumber").getText(),-9);
        actual.heightOfThinningStart =Double.parseDouble(def.getChild("HeightOfThinningStart").getText());
        actual.moderateThinning = def.getChild("ModerateThinning").getText();
        actual.colorXML = def.getChild("Color").getText();
        actual.competitionXML = def.getChild("Competition").getText();
        actual.taperFunctionXML = def.getChild("TaperFunction").getText();
        try {
            actual.stemVolumeFunctionXML = def.getChild("StemVolumeFunction").getText();
        }catch (Exception e){
             System.out.println("Schaftholz ist: "+actual.stemVolumeFunctionXML);
        }
        actual.coarseRootBiomass = def.getChild("CoarseRootBiomass").getText();
        actual.smallRootBiomass = def.getChild("SmallRootBiomass").getText();
        actual.fineRootBiomass = def.getChild("FineRootBiomass").getText();
        actual.totalRootBiomass = def.getChild("TotalRootBiomass").getText();
        //TGFunctions
        actual.uniformHeightCurveXML = initTGFunction(def.getChild("UniformHeightCurveXML").getText().trim());
        actual.heightVariationXML = initTGFunction(def.getChild("HeightVariation").getText().trim());
        actual.diameterDistributionXML = initTGFunction(def.getChild("DiameterDistributionXML").getText().trim());
        actual.volumeFunctionXML =initTGFunction(def.getChild("VolumeFunctionXML").getText().trim());
        actual.crownwidthXML = initTGFunction(def.getChild("Crownwidth").getText().trim());
        actual.crownbaseXML = initTGFunction(def.getChild("Crownbase").getText().trim());
        actual.siteindexXML = initTGFunction(def.getChild("SiteIndex").getText().trim());
        actual.siteindexHeightXML = initTGFunction(def.getChild("SiteIndexHeight").getText().trim());
        actual.potentialHeightIncrementXML = initTGFunction(def.getChild("PotentialHeightIncrement").getText().trim());
        actual.heightIncrementXML = initTGFunction(def.getChild("HeightIncrement").getText().trim());
        actual.diameterIncrementXML = initTGFunction(def.getChild("DiameterIncrement").getText().trim());
        actual.maximumDensityXML = initTGFunction(def.getChild("MaximumDensity").getText().trim());
        actual.decayXML = initTGFunction(def.getChild("Decay").getText().trim());
    }
    
    private int stripCommentsFromInt(String orig, int stdValue){
        if(orig==null || orig.equals(""))
            return stdValue;       
        return Integer.parseInt(orig.split("[/][*].+?[*][/]")[0].trim());
    }

    public TGFunction initTGFunction(String xmlText){
        if(xmlText==null)
            return new TGTextFunction();
        if(xmlText.length()==0)
            return new TGTextFunction();
        if(xmlText.startsWith("CLASS:")){
            TGClassFunction f= new TGClassFunction();
            f.init(xmlText);
            return f;
        } else{
            TGTextFunction f= new TGTextFunction();
            f.init(xmlText);
            return f;
        }
    }
    
    public boolean isLoaded(){
        return loaded;
    }

    public URL getActualURL(){
        return actualurl;
    }

    public int getSize(){
        return spcdef.size();
    }

    public int[] getSpeciesCodes(){
        if(loaded){
            int[] list= new int[spcdef.size()];
            Iterator<Integer> it=spcdef.keySet().iterator();
            int index=0;
            while(it.hasNext()){
                list[index]=it.next();
                index++;
            }
            return list;
        }
        else return null;
    }

    public SpeciesDef getByCode(int code){
        return spcdef.get(code);
    }

    /** insert a new species only if the map is loaded and the
     * map does not contain a species with code code
     * returns the new and empty SpeciesDef object or null
     * no new species is inserted
     * @param code the species code
     * @return species definition for speecies with defined code
     */
    public SpeciesDef insertSpecies(int code){
        if(loaded && getByCode(code)==null){
            SpeciesDef spec= new SpeciesDef();
            spcdef.put(code, spec);
            return spec;
        }
        return null;
    }

    public void removeSpecies(int code){
        if(loaded){
            spcdef.remove(code);
        }
    } 

    @Override
    public String toString(){
        return "SpeciedDefMap [size: "+getSize()+"; URL:"+getActualURL().toString()+"]";
    }
    
    /* writes species information for all species of one stand to a html file in
     * specified path with specified filename
     * and returns the complete cannonical path of the output file.
    */
    public String listAllSpeciesDefinition(Stand st, String path, String fname){        
        File file= new File(path, fname);
        String filename;
        try {
            filename = file.getCanonicalPath();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return null;
        }
        PrintWriter out=null;
        try {
            out= new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename)));
            out.println("<HTML>");
            out.println("<H2><P align=center>"+"Simulator Species Definition"+"</H2> ");
            for (int i=0;i<st.nspecies;i++){
                out.println("<P>");
                int m = -9;
                if(st.sp[i].spDef.latinName.contains("http")) m = st.sp[i].spDef.latinName.indexOf("http")-1;
                String txt= st.sp[i].spDef.latinName;
                if (m > 1) txt = "<a href="+st.sp[i].spDef.latinName.substring(m+1,st.sp[i].spDef.latinName.length())+">"+st.sp[i].spDef.latinName.substring(0,m)+"</a>";
                out.println("<P><B>Baumart: "+st.sp[i].code+" "+st.sp[i].spDef.longName+"  "+txt+"</B>");
                out.println("<BR>   Kronenbreite [m] = "+st.sp[i].spDef.crownwidthXML);
                out.println("<BR>   Kronenansatz [m] = "+st.sp[i].spDef.crownbaseXML);
                out.println("<BR>   Bonitöt      [m] = "+st.sp[i].spDef.siteindexXML);
                out.println("<BR>   Potentielle Höhenzuwachs [%] = "+st.sp[i].spDef.potentialHeightIncrementXML);
                out.println("<BR>   Höhenzuwachsmodulation [%] = "+st.sp[i].spDef.heightIncrementXML);
                out.println("<BR>   Standardabweichung Höhenzuwachs [m] = "+(new Double(st.sp[i].spDef.heightIncrementError)).toString());
                out.println("<BR>   Grundflächenzuwachs [cm²] = "+st.sp[i].spDef.diameterIncrementXML);
                out.println("<BR>   Standardabweichung Grundflächenzuwachs [m²] = "+(new Double(st.sp[i].spDef.diameterIncrementError)).toString());
                out.println("<BR>   Maximale Dichte [m²/ha] = "+st.sp[i].spDef.maximumDensityXML);
                out.println("<BR>   Volumenfunktion [m³] = "+st.sp[i].spDef.volumeFunctionXML);
                out.println("<BR>   Durchmesserverteilung : "+st.sp[i].spDef.diameterDistributionXML);
                out.println("<BR>   Höhenkurvenfunktion = "+st.sp[i].spDef.heightCurve);
                out.println("<BR>   Einheitshöhenkurve [m] = "+st.sp[i].spDef.uniformHeightCurveXML);
                out.println("<BR>   Höhenkurvenvariation [m] = "+st.sp[i].spDef.heightVariationXML);
                out.println("<BR>   Totholzzerfall [%] = "+st.sp[i].spDef.decayXML);
                out.println("<BR>   Kronendarstellung = "+st.sp[i].spDef.crownType);
                out.println("<BR>   Baumartenfarbe [RGB] = "+st.sp[i].spDef.colorXML);
            }
            out.println("</TABLE>");
            out.println("<br>"+"created by TreeGroSS ("+st.modelRegion+")</br></HTML>");
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return null;
        } finally{
            if(out!=null)
                out.close();
        }
        return filename;
    }
   /* writes species information for active species of one stand to a html file in
     * specified path with specified filename
     * and returns the complete cannonical path of the output file.
    */
    public String listCurrentSpeciesDefinition(Stand st, String path, String fname) throws IOException{
        File file= new File(path, fname);
        String filename=file.getCanonicalPath();
        OutputStream os=new FileOutputStream(filename);
	PrintWriter out= new PrintWriter(new OutputStreamWriter(os));
	out.println("<HTML>");
	out.println("<H2><P align=center>"+"Simulator Species Definition"+"</H2> ");
        if (st.nspecies>0 && st.ingrowthActive==true){
            try {
               String modelPlugIn="treegross.base."+st.sp[0].spDef.ingrowthXML;
               PlugInIngrowth ig = (PlugInIngrowth)Class.forName(modelPlugIn).newInstance();
               out.println("<P><B>Aktivieres Einwuchsmodell : "+ig.getModelName()+"</B>");
            }
            catch(Exception e){
                System.out.println(e);
                System.out.println("ERROR in Class Ingrowth2 ");
            }
        }
        for (int i=0;i<st.nspecies;i++){
            out.println("<P>");
            int m = -9;
            if(st.sp[i].spDef.latinName.contains("http")){
                m = st.sp[i].spDef.latinName.indexOf("http")-1;
            }
            String txt= st.sp[i].spDef.latinName;
            if (m > 1) txt = "<a href="+st.sp[i].spDef.latinName.substring(m+1,st.sp[i].spDef.latinName.length())+">"+st.sp[i].spDef.latinName.substring(0,m)+"</a>";
            out.println("<P><B>Baumart: "+st.sp[i].code+" "+st.sp[i].spDef.longName+"  "+txt+"</B>");
            out.println("<BR>   Kronenbreite [m] = "+st.sp[i].spDef.crownwidthXML);
            out.println("<BR>   Kronenansatz [m] = "+st.sp[i].spDef.crownbaseXML);
            out.println("<BR>   Bonität      [m] = "+st.sp[i].spDef.siteindexXML);
            out.println("<BR>   Potentielle Höhenzuwachs [%] = "+st.sp[i].spDef.potentialHeightIncrementXML);
            out.println("<BR>   Höhenzuwachsmodulation [%] = "+st.sp[i].spDef.heightIncrementXML);
            out.println("<BR>   Standardabweichung Höhenzuwachs [m] = "+(new Double(st.sp[i].spDef.heightIncrementError)).toString());
            out.println("<BR>   Grundflächenzuwachs [cm²] = "+st.sp[i].spDef.diameterIncrementXML);
            out.println("<BR>   Standardabweichung Grundflächenzuwachs [m²] = "+(new Double(st.sp[i].spDef.diameterIncrementError)).toString());
            out.println("<BR>   Maximale Dichte [m²/ha] = "+st.sp[i].spDef.maximumDensityXML);
            out.println("<BR>   Volumenfunktion [m³] = "+st.sp[i].spDef.volumeFunctionXML);
            out.println("<BR>   Durchmesserverteilung : "+st.sp[i].spDef.diameterDistributionXML);
            out.println("<BR>   Höhenkurvenfunktion = "+st.sp[i].spDef.heightCurve);
            out.println("<BR>   Einheitshöhenkurve [m] = "+st.sp[i].spDef.uniformHeightCurveXML);
            out.println("<BR>   Höhenkurvenvariation [m] = "+st.sp[i].spDef.heightVariationXML);
            out.println("<BR>   Totholzzerfall [%] = "+st.sp[i].spDef.decayXML);
            out.println("<BR>   Kronendarstellung = "+st.sp[i].spDef.crownType);
            out.println("<BR>   Baumartenfarbe [RGB] = "+st.sp[i].spDef.colorXML);
        }
	out.println("</TABLE>");
	out.println("<br>"+"created by ForestSimulatorBWINPro "+st.modelRegion+"</br></HTML>");
	out.close();
	return filename;
    }
    
    public String listSpeciesCode(int code, String path, String fname2){	
        File file= new File(path, fname2);
        String filename;
        try {
            filename = file.getCanonicalPath();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return null;
        }
	PrintWriter out=null;
        try {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename)));
            out.println("<HTML>");
            out.println("<H2><P align=center>"+"Species Code"+"</P align=center></H2><P> ");
            SpeciesDef sd=this.getByCode(code);
            int mm = -9;
            if (sd.latinName.contains("http")){
                mm = sd.latinName.indexOf("http")-1;
            }
            String txt= sd.latinName;
            if(mm > 1) txt = "<a href="+sd.latinName.substring(mm+1,sd.latinName.length())+">"+sd.latinName.substring(0,mm)+"</a>";
            out.println("<BR>Baumart: "+sd.code+" "+sd.shortName+" "+sd.longName+"  "+txt+"");
            out.println("</P></TABLE>");
            out.println("<br><hr>"+"created by TreeGrOSS</br></HTML>");
	} catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally{
            if(out!=null)
                out.close();
        }
        return filename;            
    }
}