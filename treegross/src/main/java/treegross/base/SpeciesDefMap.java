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
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;
import treegross.base.thinning.HeightBasedThinning;
import treegross.dynamic.siteindex.DynamicSiteIndexCalculator;

/**
 *
 * @author jhansen
 */
public class SpeciesDefMap {

    public static final int defaultCropTreeNumber = 100;
    private static final Logger logger = Logger.getLogger(SpeciesDefMap.class.getName());
    protected final Map<Integer, SpeciesDef> spcdef;
    private URL actualurl;
    private boolean loaded = false;
    private final String stdModel = "ForestSimulatorNWGermanyBC4";

    public SpeciesDefMap() {
        spcdef = new HashMap<>();
        loaded = false;
        actualurl = null;
    }

    public void reload() {
        if (loaded && actualurl != null) {
            readFromURL(actualurl);
        }
    }

    public void readInternal(String name) {
        URL url = getClass().getResource("/treegross/model/" + stdModel + ".xml");
        if (name != null) {
            url = getClass().getResource("/treegross/model/" + name + ".xml");
        }
        readFromURL(url);
    }

    public void readFromPath(File path) {
        try {
            readXMLStream(new FileInputStream(path));
            actualurl = path.toURI().toURL();
        } catch (IOException | JDOMException ex) {
            logger.log(Level.SEVERE, "Could not load species definition", ex);
        }
    }

    private void readFromURL(URL url) {
        try {
            readXML(url);
            actualurl = url;
        } catch (IOException | JDOMException e) {
            logger.log(Level.SEVERE, "reading xml file: ", e);
        }
    }

    private void readXML(URL url) throws IOException, org.jdom.JDOMException {
        URLConnection urlcon = url.openConnection();
        actualurl = url;
        readXMLStream(urlcon.getInputStream());
    }

    private void readXMLStream(InputStream imps) throws IOException, org.jdom.JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(imps);
        Element rm = doc.getRootElement();
        List<Element> speciesDefinitions = rm.getChildren("SpeciesDefinition");

        for (Element definition : speciesDefinitions) {
            final SpeciesDef current = speciesDefinitionFrom(definition);
            if (current.code != current.handledLikeCode) {
                for (Element parent_def : speciesDefinitions) {
                    int code_parent = Integer.parseInt(parent_def.getChild("Code").getText());
                    if (current.handledLikeCode == code_parent) {
                        overload(current, parent_def);
                        break;
                    }
                }
            }
            parseColor(current);
            current.setDefined(true);
            spcdef.put(current.code, current);
        }
        loaded = true;
    }

    public SpeciesDef speciesDefinitionFrom(Element def) {
        SpeciesDef speciesDefinition = new SpeciesDef();
        speciesDefinition.code = Integer.parseInt(def.getChild("Code").getText());
        speciesDefinition.handledLikeCode = Integer.parseInt(def.getChild("HandledLikeCode").getText());
        speciesDefinition.shortName = def.getChild("ShortName").getText();
        speciesDefinition.longName = def.getChild("LongName").getText();
        speciesDefinition.latinName = def.getChild("LatinName").getText();
        speciesDefinition.internalCode = Integer.parseInt(def.getChild("InternalCode").getText());
        speciesDefinition.codeGroup = Integer.parseInt(def.getChild("CodeGroup").getText());
        speciesDefinition.heightCurve = Integer.parseInt(def.getChild("HeightCurve").getText());
        speciesDefinition.crownType = Integer.parseInt(def.getChild("CrownType").getText());
        speciesDefinition.heightIncrementError = Double.parseDouble(def.getChild("HeightIncrementError").getText());
        speciesDefinition.diameterIncrementError = Double.parseDouble(def.getChild("DiameterIncrementError").getText());
        speciesDefinition.maximumAge = Integer.parseInt(def.getChild("MaximumAge").getText());
        speciesDefinition.ingrowthXML = def.getChild("Ingrowth").getText();
        speciesDefinition.targetDiameter = Double.parseDouble(def.getChild("TargetDiameter").getText());
        speciesDefinition.cropTreeNumber = stripCommentsFromInt(def.getChild("CropTreeNumber").getText(), SpeciesDefMap.defaultCropTreeNumber);
        speciesDefinition.heightOfThinningStart = Double.parseDouble(def.getChild("HeightOfThinningStart").getText());
        speciesDefinition.moderateThinning = new HeightBasedThinning(def.getChild("ModerateThinning").getText());
        speciesDefinition.colorXML = def.getChild("Color").getText();
        speciesDefinition.competitionXML = def.getChild("Competition").getText();
        speciesDefinition.taperFunctionXML = def.getChild("TaperFunction").getText();
        try {
            speciesDefinition.stemVolumeFunctionXML = def.getChild("StemVolumeFunction").getText();
        } catch (Exception e) {
            SpeciesDefMap.logger.log(Level.INFO, "Schaftholz ist nicht definiert.", e);
        }
        speciesDefinition.coarseRootBiomass = def.getChild("CoarseRootBiomass").getText();
        speciesDefinition.smallRootBiomass = def.getChild("SmallRootBiomass").getText();
        speciesDefinition.fineRootBiomass = def.getChild("FineRootBiomass").getText();
        speciesDefinition.totalRootBiomass = def.getChild("TotalRootBiomass").getText();
        speciesDefinition.uniformHeightCurveXML = initTGFunction(def.getChild("UniformHeightCurveXML").getText());
        speciesDefinition.heightVariationXML = initTGFunction(def.getChild("HeightVariation").getText());
        speciesDefinition.diameterDistributionXML = initTGFunction(def.getChild("DiameterDistributionXML").getText());
        speciesDefinition.volumeFunctionXML = initTGFunction(def.getChild("VolumeFunctionXML").getText());
        speciesDefinition.crownwidthXML = initTGFunction(def.getChild("Crownwidth").getText());
        speciesDefinition.crownbaseXML = initTGFunction(def.getChild("Crownbase").getText());
        speciesDefinition.siteindexXML = initTGFunction(def.getChild("SiteIndex").getText());
        speciesDefinition.siteindexHeightXML = initTGFunction(def.getChild("SiteIndexHeight").getText());
        speciesDefinition.potentialHeightIncrementXML = initTGFunction(def.getChild("PotentialHeightIncrement").getText());
        speciesDefinition.heightIncrementXML = initTGFunction(def.getChild("HeightIncrement").getText());
        speciesDefinition.diameterIncrementXML = initTGFunction(def.getChild("DiameterIncrement").getText());
        speciesDefinition.maximumDensityXML = initTGFunction(def.getChild("MaximumDensity").getText());
        speciesDefinition.decayXML = initTGFunction(def.getChild("Decay").getText());
        speciesDefinition.dsiCalculator = new DynamicSiteIndexCalculator(initTGFunction(def.getChildText("DynamicSiteIndex")));
        return speciesDefinition;
    }

    private void parseColor(final SpeciesDef current) throws NumberFormatException {
        int m = current.colorXML.indexOf(";");
        current.colorRed = Integer.parseInt(current.colorXML.substring(0, m));
        current.colorXML = current.colorXML.substring(m + 1);
        m = current.colorXML.indexOf(";");
        current.colorGreen = Integer.parseInt(current.colorXML.substring(0, m));
        current.colorXML = current.colorXML.substring(m + 1);
        current.colorBlue = Integer.parseInt(current.colorXML);
    }

    private void overload(SpeciesDef actual, Element with) {
        if (actual.uniformHeightCurveXML.undefined()) {
            actual.uniformHeightCurveXML = initTGFunction(with.getChild("UniformHeightCurveXML").getText());
        }
        if (actual.heightVariationXML.undefined()) {
            actual.heightVariationXML = initTGFunction(with.getChild("HeightVariation").getText());
        }
        if (actual.diameterDistributionXML.undefined()) {
            actual.diameterDistributionXML = initTGFunction(with.getChild("DiameterDistributionXML").getText());
        }
        if (actual.volumeFunctionXML.undefined()) {
            actual.volumeFunctionXML = initTGFunction(with.getChild("VolumeFunctionXML").getText());
        }
        if (actual.crownwidthXML.undefined()) {
            actual.crownwidthXML = initTGFunction(with.getChild("Crownwidth").getText());
        }
        if (actual.crownbaseXML.undefined()) {
            actual.crownbaseXML = initTGFunction(with.getChild("Crownbase").getText());
        }
        if (actual.siteindexXML.undefined()) {
            actual.siteindexXML = initTGFunction(with.getChild("SiteIndex").getText());
        }
        if (actual.siteindexHeightXML.undefined()) {
            actual.siteindexHeightXML = initTGFunction(with.getChild("SiteIndexHeight").getText());
        }
        if (actual.potentialHeightIncrementXML.undefined()) {
            actual.potentialHeightIncrementXML = initTGFunction(with.getChild("PotentialHeightIncrement").getText());
        }
        if (actual.heightIncrementXML.undefined()) {
            actual.heightIncrementXML = initTGFunction(with.getChild("HeightIncrement").getText());
        }
        if (actual.diameterIncrementXML.undefined()) {
            actual.diameterIncrementXML = initTGFunction(with.getChild("DiameterIncrement").getText());
        }
        if (actual.maximumDensityXML.undefined()) {
            actual.maximumDensityXML = initTGFunction(with.getChild("MaximumDensity").getText());
        }
        if (actual.decayXML.undefined()) {
            actual.decayXML = initTGFunction(with.getChild("Decay").getText());
        }

        if (actual.crownType < 0) {
            actual.crownType = Integer.parseInt(with.getChild("CrownType").getText());
        }
        if (actual.heightIncrementError < 0) {
            actual.heightIncrementError = Double.parseDouble(with.getChild("HeightIncrementError").getText());
        }
        if (actual.diameterIncrementError < 0) {
            actual.diameterIncrementError = Double.parseDouble(with.getChild("DiameterIncrementError").getText());
        }
        if (actual.maximumAge < 0) {
            actual.maximumAge = Integer.parseInt(with.getChild("MaximumAge").getText());
        }
        if (actual.ingrowthXML.trim().isEmpty()) {
            actual.ingrowthXML = with.getChild("Ingrowth").getText();
        }
        if (actual.heightCurve < 0) {
            actual.heightCurve = Integer.parseInt(with.getChild("HeightCurve").getText());
        }
        if (actual.targetDiameter < 0) {
            actual.targetDiameter = Double.parseDouble(with.getChild("TargetDiameter").getText());
        }
        if (actual.cropTreeNumber < 0) {
            actual.cropTreeNumber = stripCommentsFromInt(with.getChild("CropTreeNumber").getText(), defaultCropTreeNumber);
        }
        if (actual.heightOfThinningStart < 0) {
            actual.heightOfThinningStart = Double.parseDouble(with.getChild("HeightOfThinningStart").getText());
        }
        if (actual.moderateThinning == null) {
            actual.moderateThinning = new HeightBasedThinning(with.getChild("ModerateThinning").getText());
        }
        if (actual.colorXML.trim().isEmpty()) {
            actual.colorXML = with.getChild("Color").getText();
        }
        if (actual.competitionXML.trim().isEmpty()) {
            actual.competitionXML = with.getChild("Competition").getText();
        }
        if (actual.taperFunctionXML.trim().isEmpty()) {
            actual.taperFunctionXML = with.getChild("TaperFunction").getText();
        }
        if (actual.stemVolumeFunctionXML.trim().isEmpty()) {
            try {
                actual.stemVolumeFunctionXML = with.getChild("StemVolumeFunction").getText();
            } catch (Exception e) {
                System.out.println("Schaftholz ist: " + actual.stemVolumeFunctionXML);
            }
        }
        if (actual.coarseRootBiomass.trim().isEmpty()) {
            actual.coarseRootBiomass = with.getChild("CoarseRootBiomass").getText();
        }
        if (actual.smallRootBiomass.trim().isEmpty()) {
            actual.smallRootBiomass = with.getChild("SmallRootBiomass").getText();
        }
        if (actual.fineRootBiomass.trim().isEmpty()) {
            actual.fineRootBiomass = with.getChild("FineRootBiomass").getText();
        }
        if (actual.totalRootBiomass.trim().isEmpty()) {
            actual.totalRootBiomass = with.getChild("TotalRootBiomass").getText();
        }
    }

    public TGFunction initTGFunction(String xmlText) {
        if (xmlText == null || xmlText.isEmpty()) {
            return new TGTextFunction();
        }
        if (xmlText.startsWith("CLASS:")) {
            TGClassFunction f = new TGClassFunction();
            f.init(xmlText);
            return f;
        } else {
            return new TGTextFunction(xmlText);
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public URL getActualURL() {
        return actualurl;
    }

    public int getSize() {
        return spcdef.size();
    }

    public int[] getSpeciesCodes() {
        if (loaded) {
            int[] list = new int[spcdef.size()];
            Iterator<Integer> it = spcdef.keySet().iterator();
            int index = 0;
            while (it.hasNext()) {
                list[index] = it.next();
                index++;
            }
            return list;
        } else {
            return null;
        }
    }

    public SpeciesDef getByCode(int code) {
        return spcdef.get(code);
    }

    /**
     * insert a new species only if the map is loaded and the map does not
     * contain a species with code code returns the new and empty SpeciesDef
     * object or null no new species is inserted
     *
     * @param code the species code
     * @return species definition for speecies with defined code
     */
    public SpeciesDef insertSpecies(int code) {
        if (loaded && getByCode(code) == null) {
            SpeciesDef spec = new SpeciesDef();
            spcdef.put(code, spec);
            return spec;
        }
        return null;
    }

    public void removeSpecies(int code) {
        if (loaded) {
            spcdef.remove(code);
        }
    }

    @Override
    public String toString() {
        return "SpeciedDefMap [size: " + getSize() + "; URL:" + getActualURL() + "]";
    }

    /* writes species information for all species of one stand to a html file in
     * specified path with specified filename
     * and returns the complete cannonical path of the output file.
     */
    public String listAllSpeciesDefinition(Stand st, String path, String fname) {
        File file = new File(path, fname);
        String filename;
        try {
            filename = file.getCanonicalPath();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            out.println("<HTML>");
            out.println("<H2><P align=center>" + "Simulator Species Definition" + "</H2> ");
            for (int i = 0; i < st.nspecies; i++) {
                out.println("<P>");
                int m = -9;
                if (st.sp[i].spDef.latinName.contains("http")) {
                    m = st.sp[i].spDef.latinName.indexOf("http") - 1;
                }
                String txt = st.sp[i].spDef.latinName;
                if (m > 1) {
                    txt = "<a href=" + st.sp[i].spDef.latinName.substring(m + 1, st.sp[i].spDef.latinName.length()) + ">" + st.sp[i].spDef.latinName.substring(0, m) + "</a>";
                }
                out.println("<P><B>Baumart: " + st.sp[i].code + " " + st.sp[i].spDef.longName + "  " + txt + "</B>");
                out.println("<BR>   Kronenbreite [m] = " + st.sp[i].spDef.crownwidthXML);
                out.println("<BR>   Kronenansatz [m] = " + st.sp[i].spDef.crownbaseXML);
                out.println("<BR>   Bonitöt      [m] = " + st.sp[i].spDef.siteindexXML);
                out.println("<BR>   Potentielle Höhenzuwachs [%] = " + st.sp[i].spDef.potentialHeightIncrementXML);
                out.println("<BR>   Höhenzuwachsmodulation [%] = " + st.sp[i].spDef.heightIncrementXML);
                out.println("<BR>   Standardabweichung Höhenzuwachs [m] = " + (new Double(st.sp[i].spDef.heightIncrementError)).toString());
                out.println("<BR>   Grundflächenzuwachs [cm²] = " + st.sp[i].spDef.diameterIncrementXML);
                out.println("<BR>   Standardabweichung Grundflächenzuwachs [m²] = " + (new Double(st.sp[i].spDef.diameterIncrementError)).toString());
                out.println("<BR>   Maximale Dichte [m²/ha] = " + st.sp[i].spDef.maximumDensityXML);
                out.println("<BR>   Volumenfunktion [m³] = " + st.sp[i].spDef.volumeFunctionXML);
                out.println("<BR>   Durchmesserverteilung : " + st.sp[i].spDef.diameterDistributionXML);
                out.println("<BR>   Höhenkurvenfunktion = " + st.sp[i].spDef.heightCurve);
                out.println("<BR>   Einheitshöhenkurve [m] = " + st.sp[i].spDef.uniformHeightCurveXML);
                out.println("<BR>   Höhenkurvenvariation [m] = " + st.sp[i].spDef.heightVariationXML);
                out.println("<BR>   Totholzzerfall [%] = " + st.sp[i].spDef.decayXML);
                out.println("<BR>   Kronendarstellung = " + st.sp[i].spDef.crownType);
                out.println("<BR>   Baumartenfarbe [RGB] = " + st.sp[i].spDef.colorXML);
            }
            out.println("</TABLE>");
            out.println("<br>" + "created by TreeGroSS (" + st.modelRegion + ")</br></HTML>");
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
        return filename;
    }

    /* writes species information for active species of one stand to a html file in
     * specified path with specified filename
     * and returns the complete cannonical path of the output file.
     */
    public String listCurrentSpeciesDefinition(Stand st, String path, String fname) throws IOException {
        File file = new File(path, fname);
        String filename = file.getCanonicalPath();
        OutputStream os = new FileOutputStream(filename);
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(os))) {
            out.println("<HTML>");
            out.println("<H2><P align=center>" + "Simulator Species Definition" + "</H2> ");
            if (st.nspecies > 0 && st.ingrowthActive == true) {
                try {
                    String modelPlugIn = "treegross.base." + st.sp[0].spDef.ingrowthXML;
                    PlugInIngrowth ig = (PlugInIngrowth) Class.forName(modelPlugIn).newInstance();
                    out.println("<P><B>Aktivieres Einwuchsmodell : " + ig.getModelName() + "</B>");
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("ERROR in Class Ingrowth2 ");
                }
            }
            for (int i = 0; i < st.nspecies; i++) {
                out.println("<P>");
                int m = -9;
                if (st.sp[i].spDef.latinName.contains("http")) {
                    m = st.sp[i].spDef.latinName.indexOf("http") - 1;
                }
                String txt = st.sp[i].spDef.latinName;
                if (m > 1) {
                    txt = "<a href=" + st.sp[i].spDef.latinName.substring(m + 1, st.sp[i].spDef.latinName.length()) + ">" + st.sp[i].spDef.latinName.substring(0, m) + "</a>";
                }
                out.println("<P><B>Baumart: " + st.sp[i].code + " " + st.sp[i].spDef.longName + "  " + txt + "</B>");
                out.println("<BR>   Kronenbreite [m] = " + st.sp[i].spDef.crownwidthXML);
                out.println("<BR>   Kronenansatz [m] = " + st.sp[i].spDef.crownbaseXML);
                out.println("<BR>   Bonität      [m] = " + st.sp[i].spDef.siteindexXML);
                out.println("<BR>   Potentielle Höhenzuwachs [%] = " + st.sp[i].spDef.potentialHeightIncrementXML);
                out.println("<BR>   Höhenzuwachsmodulation [%] = " + st.sp[i].spDef.heightIncrementXML);
                out.println("<BR>   Standardabweichung Höhenzuwachs [m] = " + (new Double(st.sp[i].spDef.heightIncrementError)).toString());
                out.println("<BR>   Grundflächenzuwachs [cm²] = " + st.sp[i].spDef.diameterIncrementXML);
                out.println("<BR>   Standardabweichung Grundflächenzuwachs [m²] = " + (new Double(st.sp[i].spDef.diameterIncrementError)).toString());
                out.println("<BR>   Maximale Dichte [m²/ha] = " + st.sp[i].spDef.maximumDensityXML);
                out.println("<BR>   Volumenfunktion [m³] = " + st.sp[i].spDef.volumeFunctionXML);
                out.println("<BR>   Durchmesserverteilung : " + st.sp[i].spDef.diameterDistributionXML);
                out.println("<BR>   Höhenkurvenfunktion = " + st.sp[i].spDef.heightCurve);
                out.println("<BR>   Einheitshöhenkurve [m] = " + st.sp[i].spDef.uniformHeightCurveXML);
                out.println("<BR>   Höhenkurvenvariation [m] = " + st.sp[i].spDef.heightVariationXML);
                out.println("<BR>   Totholzzerfall [%] = " + st.sp[i].spDef.decayXML);
                out.println("<BR>   Kronendarstellung = " + st.sp[i].spDef.crownType);
                out.println("<BR>   Baumartenfarbe [RGB] = " + st.sp[i].spDef.colorXML);
            }
            out.println("</TABLE>");
            out.println("<br>" + "created by ForestSimulatorBWINPro " + st.modelRegion + "</br></HTML>");
        }
        return filename;
    }

    public String listSpeciesCode(int code, String path, String fname2) {
        File file = new File(path, fname2);
        String filename;
        try {
            filename = file.getCanonicalPath();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            out.println("<HTML>");
            out.println("<H2><P align=center>" + "Species Code" + "</P align=center></H2><P> ");
            SpeciesDef sd = this.getByCode(code);
            int mm = -9;
            if (sd.latinName.contains("http")) {
                mm = sd.latinName.indexOf("http") - 1;
            }
            String txt = sd.latinName;
            if (mm > 1) {
                txt = "<a href=" + sd.latinName.substring(mm + 1, sd.latinName.length()) + ">" + sd.latinName.substring(0, mm) + "</a>";
            }
            out.println("<BR>Baumart: " + sd.code + " " + sd.shortName + " " + sd.longName + "  " + txt + "");
            out.println("</P></TABLE>");
            out.println("<br><hr>" + "created by TreeGrOSS</br></HTML>");
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return filename;
    }

    public int stripCommentsFromInt(String orig, int defaultValue) {
        if (orig == null || orig.equals("")) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(orig.split("[/][*].+?[*][/]")[0].trim());
        } catch (NumberFormatException e) {
            logger.log(Level.INFO, "Integer value not defined, using default.", e);
            return defaultValue;
        }
    }
}
