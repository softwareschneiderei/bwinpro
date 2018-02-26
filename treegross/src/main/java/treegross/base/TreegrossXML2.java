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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.output.XMLOutputter;
import org.jdom.input.*;
//import org.jdom.DocType;
import java.net.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

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

    //static Document doc;

    static Element rootElt;
    private final static Logger LOGGER = Logger.getLogger(TreegrossXML2.class.getName());

    /**
     * Creates a new instance of TreegrossXML2
     */
    public TreegrossXML2() {
    }

    /**
     * Creates an Treegross xml
     *
     * @param st
     * @param filename
     */
    public void saveAsXML(Stand st, String filename) {
        NumberFormat f;
        f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        Element elt;
        Document doc = new Document();
        rootElt = new Element("Bestand");
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"treegross.xsl\"");
        doc.addContent(pi);
        doc.setRootElement(rootElt);

        //Bestandesinformation 
        if (st.nspecies > 0) {
            rootElt = addString(rootElt, "Id", "1");
            rootElt = addString(rootElt, "Kennung", st.standname);
            rootElt = addString(rootElt, "Allgemeines", " ");
            rootElt = addString(rootElt, "Flaechengroesse_m2", Double.toString(st.size * 10000));
            rootElt = addString(rootElt, "HauptbaumArtCodeStd", Integer.toString(st.sp[0].code));
            rootElt = addString(rootElt, "HauptbaumArtCodeLokal", Integer.toString(st.sp[0].code));
            rootElt = addString(rootElt, "AufnahmeJahr", Integer.toString(st.year));
            rootElt = addString(rootElt, "AufnahmeMonat", Integer.toString(st.monat));
            rootElt = addString(rootElt, "DatenHerkunft", st.datenHerkunft);
            rootElt = addString(rootElt, "Standort", st.standort);
            rootElt = addString(rootElt, "Hochwert_m", Double.toString(st.hochwert_m));
            rootElt = addString(rootElt, "Rechtswert_m", Double.toString(st.rechtswert_m));
            rootElt = addString(rootElt, "Hoehe_uNN_m", Double.toString(st.hoehe_uNN_m));
            rootElt = addString(rootElt, "Exposition_Gon", Integer.toString(st.exposition_Gon));
            rootElt = addString(rootElt, "Hangneigung_Prozent", Double.toString(st.hangneigungProzent));
            rootElt = addString(rootElt, "Wuchsgebiet", st.wuchsgebiet);
            rootElt = addString(rootElt, "Wuchsbezirk", st.wuchsbezirk);
            rootElt = addString(rootElt, "Standortskennziffer", st.standortsKennziffer);
        }
        /* Baumarten */
        for (int i = 0; i < st.nspecies; i++) {
            elt = new Element("Baumartencode");
            elt = addString(elt, "Code", Integer.toString(st.sp[i].code));
            elt = addString(elt, "deutscherName", st.sp[i].spDef.longName);
            elt = addString(elt, "lateinischerName", st.sp[i].spDef.latinName);
            rootElt.addContent(elt);
        }
        /* Add center points */
        if (st.ncpnt > 0) {
            elt = new Element("Eckpunkt");
            elt = addString(elt, "Nr", st.center.no);
            elt = addString(elt, "RelativeXKoordinate_m", f.format(st.center.x));
            elt = addString(elt, "RelativeYKoordinate_m", f.format(st.center.y));
            elt = addString(elt, "RelativeBodenhoehe_m", f.format(st.center.z));
            rootElt.addContent(elt);
        }

        /* Add corner points */
        for (int i = 0; i < st.ncpnt; i++) {
            elt = new Element("Eckpunkt");
            elt = addString(elt, "Nr", st.cpnt[i].no);
            elt = addString(elt, "RelativeXKoordinate_m", f.format(st.cpnt[i].x));
            elt = addString(elt, "RelativeYKoordinate_m", f.format(st.cpnt[i].y));
            elt = addString(elt, "RelativeBodenhoehe_m", f.format(st.cpnt[i].z));
            rootElt.addContent(elt);
        }
        /* Add Bäume */
        for (int i = 0; i < st.ntrees; i++) {
            //System.out.println("test "+i);
            elt = new Element("Baum");
            elt = addString(elt, "Nr", Integer.toString(i + 1));
            elt = addString(elt, "Kennung", st.tr[i].no);
            elt = addString(elt, "BaumartcodeStd", "0");
            elt = addString(elt, "BaumartcodeLokal", Integer.toString(st.tr[i].code));
            elt = addString(elt, "Alter_Jahr", Integer.toString(st.tr[i].age));
            elt = addString(elt, "BHD_mR_cm", f.format(st.tr[i].d));
            elt = addString(elt, "Hoehe_m", f.format(st.tr[i].h));
            elt = addString(elt, "Kronenansatz_m", f.format(st.tr[i].cb));
            elt = addString(elt, "MittlererKronenDurchmesser_m", f.format(st.tr[i].cw));
            elt = addString(elt, "SiteIndex_m", f.format(st.tr[i].si));
            elt = addString(elt, "RelativeXKoordinate_m", f.format(st.tr[i].x));
            elt = addString(elt, "RelativeYKoordinate_m", f.format(st.tr[i].y));
            elt = addString(elt, "RelativeBodenhoehe_m", f.format(st.tr[i].z));

            boolean lebend = true;
            if (st.tr[i].out > -1) {
                lebend = false;
            }

            elt = addString(elt, "Lebend", Boolean.toString(lebend));

            boolean entnommen = false;
            if (st.tr[i].outtype >= 2) {
                entnommen = true;
            }

            elt = addString(elt, "Entnommen", Boolean.toString(entnommen));
            elt = addString(elt, "AusscheideMonat", "3");
            elt = addString(elt, "AusscheideJahr", Integer.toString(st.tr[i].out));

            String grund = "";
            if (st.tr[i].outtype == 1) {
                grund = "Mortalität";
            }
            if (st.tr[i].outtype == 2) {
                grund = "Durchforstung";
            }
            if (st.tr[i].outtype == 3) {
                grund = "Ernte";
            }
            if (st.tr[i].outtype > 3) {
                grund = "anderer";
            }
            elt = addString(elt, "AusscheideGrund", grund);

            elt = addString(elt, "ZBaum", Boolean.toString(st.tr[i].crop));
            elt = addString(elt, "ZBaumtemporaer", Boolean.toString(st.tr[i].tempcrop));
            elt = addString(elt, "HabitatBaum", Boolean.toString(st.tr[i].habitat));
            elt = addString(elt, "KraftscheKlasse", "0");
            elt = addString(elt, "Schicht", Integer.toString(st.tr[i].layer));
            f.setMaximumFractionDigits(4);
            f.setMinimumFractionDigits(4);
            elt = addString(elt, "Flaechenfaktor", f.format(st.tr[i].fac));
            elt = addString(elt, "Volumen_cbm", f.format(st.tr[i].v));
            elt = addString(elt, "VolumenTotholz_cbm", f.format(st.tr[i].volumeDeadwood));
            elt = addString(elt, "Bemerkung", st.tr[i].remarks);
            f.setMaximumFractionDigits(2);
            f.setMinimumFractionDigits(2);
            rootElt.addContent(elt);
        }
        /* Abspeichern des doc */
        try {
            File file = new File(filename);
            FileOutputStream result = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(doc, result);
            result.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    public treegross.base.Stand readTreegrossStand(treegross.base.Stand stl, URL url) {
        try {
            URLConnection urlcon = url.openConnection();
            return this.readTreegrossStandFromStream(stl, urlcon.getInputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * reads a treegross xml-File from InputStream
     *
     * @param stl TreeGrOSS Stand
     * @param iss
     * @return TreeGrOSS Stand
     */
    public Stand readTreegrossStandFromStream(Stand stl, InputStream iss) {
        Stand st;
        st = stl;
        try {
            SAXBuilder builder = new SAXBuilder();
            //URLConnection urlcon = url.openConnection();
            Document doc = builder.build(iss);
            //DocType docType = doc.getDocType();     
            Element bestand = doc.getRootElement();
            st.id = bestand.getChild("Id").getText();
            st.addName(bestand.getChild("Kennung").getText());
            st.addsize(Double.parseDouble(bestand.getChild("Flaechengroesse_m2").getText()) / 10000.0);
            st.monat = Integer.parseInt(bestand.getChild("AufnahmeMonat").getText());
            st.year = Integer.parseInt(bestand.getChild("AufnahmeJahr").getText());
            st.datenHerkunft = bestand.getChild("DatenHerkunft").getText();
            st.standort = bestand.getChild("Standort").getText();
            st.rechtswert_m = Double.parseDouble(bestand.getChild("Rechtswert_m").getText());
            st.hochwert_m = Double.parseDouble(bestand.getChild("Hochwert_m").getText());
            st.hoehe_uNN_m = Double.parseDouble(bestand.getChild("Hoehe_uNN_m").getText());
            st.exposition_Gon = Integer.parseInt(bestand.getChild("Exposition_Gon").getText());
            st.hangneigungProzent = Double.parseDouble(bestand.getChild("Hangneigung_Prozent").getText());
            st.wuchsgebiet = bestand.getChild("Wuchsgebiet").getText();
            st.wuchsbezirk = bestand.getChild("Wuchsbezirk").getText();
            st.standortsKennziffer = bestand.getChild("Standortskennziffer").getText();

            st.ncpnt = 0;
            st.ntrees = 0;
            st.nspecies = 0;
            st.center.no = "undefined";

            List eckpunkte = bestand.getChildren("Eckpunkt");

            Iterator i = eckpunkte.iterator();

            int count_ep = 0;
            while (i.hasNext()) {
                Element eckpunkt = (Element) i.next();
                count_ep++;
                String nrx = eckpunkt.getChild("Nr").getText();
                if (nrx.contains("circle") || nrx.contains("polygon")) {
                    st.center.no = nrx;
                    st.center.x = Double.parseDouble(eckpunkt.getChild("RelativeXKoordinate_m").getText());
                    st.center.y = Double.parseDouble(eckpunkt.getChild("RelativeYKoordinate_m").getText());
                    st.center.z = Double.parseDouble(eckpunkt.getChild("RelativeBodenhoehe_m").getText());
                } else {
                    st.addcornerpoint(eckpunkt.getChild("Nr").getText(),
                            Double.parseDouble(eckpunkt.getChild("RelativeXKoordinate_m").getText()),
                            Double.parseDouble(eckpunkt.getChild("RelativeYKoordinate_m").getText()),
                            Double.parseDouble(eckpunkt.getChild("RelativeBodenhoehe_m").getText()));
                }
            }
//       if no corner point wa stored in xml create square with size equal to stand size
            if (count_ep == 0) {
                double length = Math.sqrt(st.size * 10000);
                System.out.println("no corner points sotred in xml. adding sqare coordinates (length=" + length + ")");
                st.addcornerpoint("polygon_1", 0, 0, 0);
                st.addcornerpoint("polygon_2", 0, length, 0);
                st.addcornerpoint("polygon_3", length, length, 0);
                st.addcornerpoint("polygon_4", length, 0, 0);
            }

            List baeume = bestand.getChildren("Baum");
            i = baeume.iterator();
            while (i.hasNext()) {
                Element baum = (Element) i.next();
                int out;// = -1 ;
                //if (Boolean.parseBoolean(baum.getChild("Entnommen").getText())==false) // wenn so dann muss hier flase nuss hier mit true abgeglichen werden
                out = Integer.parseInt(baum.getChild("AusscheideJahr").getText());
                int outtype = 0;
                String ausGrund = baum.getChild("AusscheideGrund").getText();
                if (ausGrund.contains("Mort")) {
                    outtype = 1;
                }
                if (ausGrund.contains("Durch")) {
                    outtype = 2;
                }
                if (ausGrund.contains("Ernte")) {
                    outtype = 3;
                }
                try {
                    st.addXMLTree(Integer.parseInt(baum.getChild("BaumartcodeLokal").getText()),
                            baum.getChild("Kennung").getText(),
                            Integer.parseInt(baum.getChild("Alter_Jahr").getText()),
                            out, outtype,
                            Double.parseDouble(baum.getChild("BHD_mR_cm").getText()),
                            Double.parseDouble(baum.getChild("Hoehe_m").getText()),
                            Double.parseDouble(baum.getChild("Kronenansatz_m").getText()),
                            Double.parseDouble(baum.getChild("MittlererKronenDurchmesser_m").getText()),
                            Double.parseDouble(baum.getChild("SiteIndex_m").getText()),
                            Double.parseDouble(baum.getChild("Flaechenfaktor").getText()),
                            Double.parseDouble(baum.getChild("RelativeXKoordinate_m").getText()),
                            Double.parseDouble(baum.getChild("RelativeYKoordinate_m").getText()),
                            Double.parseDouble(baum.getChild("RelativeBodenhoehe_m").getText()),
                            Boolean.parseBoolean(baum.getChild("ZBaum").getText()),
                            Boolean.parseBoolean(baum.getChild("ZBaumtemporaer").getText()),
                            Boolean.parseBoolean(baum.getChild("HabitatBaum").getText()),
                            Integer.parseInt(baum.getChild("Schicht").getText()),
                            Double.parseDouble(baum.getChild("VolumenTotholz_cbm").getText()),
                            baum.getChild("Bemerkung").getText()
                    );
                } catch (NumberFormatException ne) {
                    System.err.println("ERROR while parseing numeric tree data for tree: " + baum.getChild("Kennung").getText());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (JDOMException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        } catch (SpeciesNotDefinedException e) {
            LOGGER.log(Level.SEVERE, "treegross", e);
        }
        return st;
    }

    private Element addString(Element elt, String variable, String text) {
        Element var = new Element(variable);
        var.addContent(text);
        elt.addContent(var);
        return elt;
    }
}
