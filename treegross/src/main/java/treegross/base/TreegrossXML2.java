/* 
 * @(#) TreegrossXML2.java  
 *  (c) 2002-2010 Juergen Nagel, Northwest German Research Station,
 *      Grätzelstr.2, 37079 Göttingen, Germany
 *      E-Mail: Juergen.Nagel@nw-fva.de
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */
package treegross.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;
import treegross.tools.XmlTool;

/**
 * TreeGrOSS : TreegrossXML2.java version 7.5 18-Mar-2010 author	Juergen Nagel
 *
 * This is the 2nd format to define a forest stand by XML. The class can read
 * and write a treegross xml file
 *
 * http://www.nw-fva.de/~nagel/treegross/
 *
 */
public class TreegrossXML2 {
    
    private final static Logger LOGGER = Logger.getLogger(TreegrossXML2.class.getName());

    /**
     * Creates a new instance of TreegrossXML2
     */
    public TreegrossXML2() {
    }
    
    /**
     * save a treegross stand as xml file
     *
     * changes: jhansen, 02.12.2015: avoid usage of library jdom
     *
     *
     * @param st the Stand to save
     * @param fileName the full filename for xml file to write
     */
    public void saveAsXML(Stand st, String fileName) {
        NumberFormat f;
        f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            String PItext = "type='text/xsl' href='treegross.xsl'";
            ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", PItext);
            doc.appendChild(pi);
           Element rootElt = doc.createElement("Bestand");
            doc.appendChild(rootElt);
            // add stand informations 
            if (st.nspecies > 0) {
                XmlTool.addChildString(rootElt, "Id", st.id);
                XmlTool.addChildString(rootElt, "Kennung", st.standname);
                XmlTool.addChildString(rootElt, "Allgemeines", " ");
                XmlTool.addChildString(rootElt, "Flaechengroesse_m2", Double.toString(st.size * 10000));
                XmlTool.addChildString(rootElt, "HauptbaumArtCodeStd", Integer.toString(st.sp[0].code));
                XmlTool.addChildString(rootElt, "HauptbaumArtCodeLokal", Integer.toString(st.sp[0].code));
                XmlTool.addChildString(rootElt, "AufnahmeJahr", Integer.toString(st.year));
                XmlTool.addChildString(rootElt, "AufnahmeMonat", Integer.toString(st.monat));
                XmlTool.addChildString(rootElt, "DatenHerkunft", st.datenHerkunft);
                XmlTool.addChildString(rootElt, "Standort", st.standort);
                XmlTool.addChildString(rootElt, "Hochwert_m", Double.toString(st.hochwert_m));
                XmlTool.addChildString(rootElt, "Rechtswert_m", Double.toString(st.rechtswert_m));
                XmlTool.addChildString(rootElt, "Hoehe_uNN_m", Double.toString(st.hoehe_uNN_m));
                XmlTool.addChildString(rootElt, "Exposition_Gon", Integer.toString(st.exposition_Gon));
                XmlTool.addChildString(rootElt, "Hangneigung_Prozent", Double.toString(st.hangneigungProzent));
                XmlTool.addChildString(rootElt, "Wuchsgebiet", st.wuchsgebiet);
                XmlTool.addChildString(rootElt, "Wuchsbezirk", st.wuchsbezirk);
                XmlTool.addChildString(rootElt, "Standortskennziffer", st.standortsKennziffer);
                XmlTool.addChildString(rootElt, "BT", Integer.toString(st.bt));
                XmlTool.addChildString(rootElt, "WET", Integer.toString(st.wet));
                XmlTool.addChildString(rootElt, "Standortskennziffer", st.standortsKennziffer);
            }
            // add all species
            for (int i = 0; i < st.nspecies; i++) {
                Element elt;
                elt = doc.createElement("Baumartencode");
                XmlTool.addChildString(elt, "Code", Integer.toString(st.sp[i].code));
                XmlTool.addChildString(elt, "deutscherName", st.sp[i].spDef.longName);
                XmlTool.addChildString(elt, "lateinischerName", st.sp[i].spDef.latinName);
                rootElt.appendChild(elt);
            }
            // add center point
            if (st.ncpnt > 0) {
                Element elt;
                elt = doc.createElement("Eckpunkt");
                XmlTool.addChildString(elt, "Nr", st.center.no);
                XmlTool.addChildString(elt, "RelativeXKoordinate_m", f.format(st.center.x));
                XmlTool.addChildString(elt, "RelativeYKoordinate_m", f.format(st.center.y));
                XmlTool.addChildString(elt, "RelativeBodenhoehe_m", f.format(st.center.z));
                rootElt.appendChild(elt);
            }
            // add corner points
            for (int i = 0; i < st.ncpnt; i++) {
                Element elt;
                elt = doc.createElement("Eckpunkt");
                XmlTool.addChildString(elt, "Nr", st.cpnt[i].no);
                XmlTool.addChildString(elt, "RelativeXKoordinate_m", f.format(st.cpnt[i].x));
                XmlTool.addChildString(elt, "RelativeYKoordinate_m", f.format(st.cpnt[i].y));
                XmlTool.addChildString(elt, "RelativeBodenhoehe_m", f.format(st.cpnt[i].z));
                rootElt.appendChild(elt);
            }            
            // add trees:
            boolean lebend, entnommen;
            for (int i = 0; i < st.ntrees; i++) {
                Element elt;
                elt = doc.createElement("Baum");
                XmlTool.addChildString(elt, "Nr", Integer.toString(i + 1));
                XmlTool.addChildString(elt, "Kennung", st.tr[i].no);
                XmlTool.addChildString(elt, "BaumartcodeStd", "0");
                XmlTool.addChildString(elt, "BaumartcodeLokal", Integer.toString(st.tr[i].code));
                XmlTool.addChildString(elt, "Alter_Jahr", Integer.toString(st.tr[i].age));
                XmlTool.addChildString(elt, "BHD_mR_cm", f.format(st.tr[i].d));
                XmlTool.addChildString(elt, "Hoehe_m", f.format(st.tr[i].h));
                XmlTool.addChildString(elt, "Kronenansatz_m", f.format(st.tr[i].cb));
                XmlTool.addChildString(elt, "MittlererKronenDurchmesser_m", f.format(st.tr[i].cw));
                XmlTool.addChildString(elt, "SiteIndex_m", f.format(st.tr[i].si));
                XmlTool.addChildString(elt, "RelativeXKoordinate_m", f.format(st.tr[i].x));
                XmlTool.addChildString(elt, "RelativeYKoordinate_m", f.format(st.tr[i].y));
                XmlTool.addChildString(elt, "RelativeBodenhoehe_m", f.format(st.tr[i].z));
                lebend = (st.tr[i].out == -1);
                XmlTool.addChildString(elt, "Lebend", Boolean.toString(lebend));
                entnommen = (st.tr[i].outtype >= 2);
                XmlTool.addChildString(elt, "Entnommen", Boolean.toString(entnommen));
                XmlTool.addChildString(elt, "AusscheideMonat", "3");
                XmlTool.addChildString(elt, "AusscheideJahr", Integer.toString(st.tr[i].out));
                String grund;
                switch (st.tr[i].outtype) {
                    case 1:
                        grund = "Mortalität";
                        break;
                    case 2:
                        grund = "Durchforstung";
                        break;
                    case 3:
                        grund = "Ernte";
                        break;
                    default:
                        grund = "anderer";
                }
                XmlTool.addChildString(elt, "AusscheideGrund", grund);
                XmlTool.addChildString(elt, "ZBaum", Boolean.toString(st.tr[i].crop));
                XmlTool.addChildString(elt, "ZBaumtemporaer", Boolean.toString(st.tr[i].tempcrop));
                XmlTool.addChildString(elt, "HabitatBaum", Boolean.toString(st.tr[i].habitat));
                XmlTool.addChildString(elt, "KraftscheKlasse", "0");
                XmlTool.addChildString(elt, "Schicht", Integer.toString(st.tr[i].layer));
                f.setMaximumFractionDigits(4);
                f.setMinimumFractionDigits(4);
                XmlTool.addChildString(elt, "Flaechenfaktor", f.format(st.tr[i].fac));
                XmlTool.addChildString(elt, "Volumen_cbm", f.format(st.tr[i].v));
                XmlTool.addChildString(elt, "VolumenTotholz_cbm", f.format(st.tr[i].volumeDeadwood));
                XmlTool.addChildString(elt, "Bemerkung", st.tr[i].remarks);
                f.setMaximumFractionDigits(2);
                f.setMinimumFractionDigits(2);
                rootElt.appendChild(elt);
            }
            // save to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            
            StreamResult result = new StreamResult(new File(fileName));
            // Output to console for testing
            //StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
            //result.getOutputStream().close();

        } catch (ParserConfigurationException | TransformerException ex) {
            LOGGER.log(Level.SEVERE, "treegross", ex);
        }
    }

    public Stand readTreegrossStand(Stand stl, URL url) {
        InputStream is = null;
        try {
            URLConnection urlcon = url.openConnection();
            is = urlcon.getInputStream();
            return readTreegrossStandFromStream(stl, is);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "treegross", ex);
                }
            }
        }
        return null;
    }

    /**
     * reads a treegross xml-File from InputStream
     * 
     * changes 
     * -------------------------------------------------------------------------
     * jhansen, 02.12.2015:
     * avoid usage of library jdom
     * -------------------------------------------------------------------------
     * @param stl TreeGrOSS Stand
     * @param iss
     * @return TreeGrOSS Stand
     */
    public Stand readTreegrossStandFromStream(Stand stl, InputStream iss) {
        Stand st;
        if (stl != null) {
            st = stl;
        } else {
            st = new Stand();
        }
        st.newStand();
        try {
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(iss);
            Element standElt = d.getDocumentElement();            

            st.id = XmlTool.getChildText(standElt, "Id");
            st.addName(XmlTool.getChildText(standElt, "Kennung"));
            st.addsize(Double.parseDouble(XmlTool.getChildText(standElt, "Flaechengroesse_m2")) / 10000.0);
            st.monat = Integer.parseInt(XmlTool.getChildText(standElt, "AufnahmeMonat"));
            st.year = Integer.parseInt(XmlTool.getChildText(standElt, "AufnahmeJahr"));
            st.datenHerkunft = XmlTool.getChildText(standElt, "DatenHerkunft");
            st.standort = XmlTool.getChildText(standElt, "Standort");
            st.rechtswert_m = Double.parseDouble(XmlTool.getChildText(standElt, "Rechtswert_m"));
            st.hochwert_m = Double.parseDouble(XmlTool.getChildText(standElt, "Hochwert_m"));
            st.hoehe_uNN_m = Double.parseDouble(XmlTool.getChildText(standElt, "Hoehe_uNN_m"));
            st.exposition_Gon = Integer.parseInt(XmlTool.getChildText(standElt, "Exposition_Gon"));
            st.hangneigungProzent = Double.parseDouble(XmlTool.getChildText(standElt, "Hangneigung_Prozent"));
            st.wuchsgebiet = XmlTool.getChildText(standElt, "Wuchsgebiet");
            st.wuchsbezirk = XmlTool.getChildText(standElt, "Wuchsbezirk");
            st.standortsKennziffer = XmlTool.getChildText(standElt, "Standortskennziffer");
            try{
                st.bt = Integer.parseInt(XmlTool.getChildText(standElt, "BT"));
            } catch (Exception e) { st.bt =-99 ;}
    
            
            try{
                st.wet = Integer.parseInt(XmlTool.getChildText(standElt, "WET"));
            } catch (Exception e) { st.wet =-99 ;}
            st.ncpnt = 0;
            st.ntrees = 0;
            st.nspecies = 0;
            st.center.no = "undefined";

            NodeList cornerpoints = standElt.getElementsByTagName("Eckpunkt");
            Node n;
            if (cornerpoints != null && cornerpoints.getLength() > 0) {
                for (int i = 0; i < cornerpoints.getLength(); i++) {
                    n = cornerpoints.item(i);
                    String nrx = XmlTool.getChildText(n, "Nr");
                    if (nrx.contains("circle") || nrx.contains("polygon")) {
                        st.center.no = nrx;
                        st.center.x = Double.parseDouble(XmlTool.getChildText(n, "RelativeXKoordinate_m"));
                        st.center.y = Double.parseDouble(XmlTool.getChildText(n, "RelativeYKoordinate_m"));
                        st.center.z = Double.parseDouble(XmlTool.getChildText(n, "RelativeBodenhoehe_m"));
                    } else {
                        st.addcornerpoint(XmlTool.getChildText(n, "Nr"),
                                Double.parseDouble(XmlTool.getChildText(n, "RelativeXKoordinate_m")),
                                Double.parseDouble(XmlTool.getChildText(n, "RelativeYKoordinate_m")),
                                Double.parseDouble(XmlTool.getChildText(n, "RelativeBodenhoehe_m")));
                    }
                }
            } else {
                // if no corner point wa stored in xml create square with size equal to stand size
                double length = Math.sqrt(st.size * 10000);
                LOGGER.log(Level.WARNING, "No corner points stored in xml. Adding square coordinates (length={0})", length);
                st.addcornerpoint("polygon_1", 0, 0, 0);
                st.addcornerpoint("polygon_2", 0, length, 0);
                st.addcornerpoint("polygon_3", length, length, 0);
                st.addcornerpoint("polygon_4", length, 0, 0);
            }

            NodeList trees = standElt.getElementsByTagName("Baum");
            for (int i = 0; i < trees.getLength(); i++) {
                n = trees.item(i);
                int out;// = -1 ;
                //if (Boolean.parseBoolean(baum.getChild("Entnommen").getText())==false) // wenn so dann muss hier flase nuss hier mit true abgeglichen werden
                out = Integer.parseInt(XmlTool.getChildText(n, "AusscheideJahr"));
                int outtype = 0;
                String ausGrund = XmlTool.getChildText(n, "AusscheideGrund");
                if (ausGrund.contains("Mort")) {
                    outtype = 1;
                } else if (ausGrund.contains("Durch")) {
                    outtype = 2;
                } else if (ausGrund.contains("Ernte")) {
                    outtype = 3;
                }
                try {
                    st.addXMLTree(Integer.parseInt(XmlTool.getChildText(n, "BaumartcodeLokal")),
                            XmlTool.getChildText(n, "Kennung"),
                            Integer.parseInt(XmlTool.getChildText(n, "Alter_Jahr")),
                            out, outtype,
                            Double.parseDouble(XmlTool.getChildText(n, "BHD_mR_cm")),
                            Double.parseDouble(XmlTool.getChildText(n, "Hoehe_m")),
                            Double.parseDouble(XmlTool.getChildText(n, "Kronenansatz_m")),
                            Double.parseDouble(XmlTool.getChildText(n, "MittlererKronenDurchmesser_m")),
                            Double.parseDouble(XmlTool.getChildText(n, "SiteIndex_m")),
                            Double.parseDouble(XmlTool.getChildText(n, "Flaechenfaktor")),
                            Double.parseDouble(XmlTool.getChildText(n, "RelativeXKoordinate_m")),
                            Double.parseDouble(XmlTool.getChildText(n, "RelativeYKoordinate_m")),
                            Double.parseDouble(XmlTool.getChildText(n, "RelativeBodenhoehe_m")),
                            Boolean.parseBoolean(XmlTool.getChildText(n, "ZBaum")),
                            Boolean.parseBoolean(XmlTool.getChildText(n, "ZBaumtemporaer")),
                            Boolean.parseBoolean(XmlTool.getChildText(n, "HabitatBaum")),
                            Integer.parseInt(XmlTool.getChildText(n, "Schicht")),
                            Double.parseDouble(XmlTool.getChildText(n, "VolumenTotholz_cbm")),
                            XmlTool.getChildText(n, "Bemerkung")
                    );
                } catch (NumberFormatException ne) {
                    LOGGER.log(Level.SEVERE, "treegross", "ERROR while parseing numeric tree data for tree: " + XmlTool.getChildText(n, "Kennung"));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | SpeciesNotDefinedException ex) {
            LOGGER.log(Level.SEVERE, "treegross", ex);
        }
        return st;
    }
    
}
