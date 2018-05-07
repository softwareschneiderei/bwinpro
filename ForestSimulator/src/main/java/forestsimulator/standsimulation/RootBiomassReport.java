/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package forestsimulator.standsimulation;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.output.XMLOutputter;
import java.util.*;
import java.text.*;
import treegross.base.*;
import java.io.*;

/**
 *
 * @author nagel
 */
public class RootBiomassReport {

    static Document doc;
    static Element rootElt;

    public void report(Stand st, String programDir, String workingDir) {
        // Calculate and write xml
        String pa = "";
        pa = workingDir + System.getProperty("file.separator") + "rootbiomass.xml";
//
        NumberFormat f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        f.setGroupingUsed(false);
        Element elt;
        Element elt2;
        Element elt3;
        /**
         * Creates an Treegross xml
         */
        Document doc = new Document();
        rootElt = new Element("Rootbiomass");
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                "type=\"text/xsl\" href=\"rootbiomass.xsl\"");
        doc.addContent(pi);
        doc.setRootElement(rootElt);
        try {
// File 
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

// Table of functions
            for (int jj = 0; jj < st.nspecies; jj++) {
                elt = new Element("Functions");
                elt = addString(elt, "Code", Integer.toString(st.sp[jj].code));
                elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
                elt = addString(elt, "Component", "CoarseRoot");
                elt = addString(elt, "Function", st.sp[jj].spDef.coarseRootBiomass);
                rootElt.addContent(elt);
                elt = new Element("Functions");
                elt = addString(elt, "Code", Integer.toString(st.sp[jj].code));
                elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
                elt = addString(elt, "Component", "FineRoot");
                elt = addString(elt, "Function", st.sp[jj].spDef.fineRootBiomass);
                rootElt.addContent(elt);
                elt = new Element("Functions");
                elt = addString(elt, "Code", Integer.toString(st.sp[jj].code));
                elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
                elt = addString(elt, "Component", "SmallRoot");
                elt = addString(elt, "Function", st.sp[jj].spDef.smallRootBiomass);
                rootElt.addContent(elt);
                elt = new Element("Functions");
                elt = addString(elt, "Code", Integer.toString(st.sp[jj].code));
                elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
                elt = addString(elt, "Component", "TotalRoot");
                elt = addString(elt, "Function", st.sp[jj].spDef.totalRootBiomass);
                rootElt.addContent(elt);

            }

// Calculate stand total root biomass
            FunctionInterpreter fi = new FunctionInterpreter();
            for (int jj = 0; jj < st.nspecies; jj++) {
                double sumCR = 0.0;
                double sumFR = 0.0;
                double sumSR = 0.0;
                double sumTR = 0.0;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].code == st.sp[jj].code) {
                        sumCR = sumCR + st.tr[i].fac * fi.getValueForTree(st.tr[i], st.sp[jj].spDef.coarseRootBiomass);
                        sumFR = sumFR + st.tr[i].fac * fi.getValueForTree(st.tr[i], st.sp[jj].spDef.fineRootBiomass);
                        sumSR = sumSR + st.tr[i].fac * fi.getValueForTree(st.tr[i], st.sp[jj].spDef.smallRootBiomass);
                        sumTR = sumTR + st.tr[i].fac * fi.getValueForTree(st.tr[i], st.sp[jj].spDef.totalRootBiomass);
                    }
                }
                sumCR = sumCR / st.size;
                sumFR = sumFR / st.size;
                sumSR = sumSR / st.size;
                sumTR = sumTR / st.size;
                elt = new Element("Species");
                elt = addString(elt, "Code", Integer.toString(st.sp[jj].code));
                elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
                elt = addString(elt, "SumCoarseRoots", f.format(sumCR));
                elt = addString(elt, "SumFineRoots", f.format(sumFR));
                elt = addString(elt, "SumSmallRoots", f.format(sumSR));
                elt = addString(elt, "SumTotalRoots", f.format(sumTR));
                rootElt.addContent(elt);
            }

            for (int jj = 0; jj < st.nspecies; jj++) {
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].code == st.sp[jj].code) {
                        elt = new Element("Tree");
                        elt = addString(elt, "Code", Integer.toString(st.tr[i].code));
                        elt = addString(elt, "Shortname", st.tr[i].sp.spDef.shortName);
                        elt = addString(elt, "Shortname", st.tr[i].no);
                        elt = addString(elt, "Age", Integer.toString(st.tr[i].age));
                        elt = addString(elt, "DBH", f.format(st.tr[i].d));
                        elt = addString(elt, "Height", f.format(st.tr[i].h));
                        elt = addString(elt, "CoarseRoots", f.format(fi.getValueForTree(st.tr[i], st.sp[jj].spDef.coarseRootBiomass)));
                        elt = addString(elt, "FineRoots", f.format(fi.getValueForTree(st.tr[i], st.sp[jj].spDef.fineRootBiomass)));
                        elt = addString(elt, "SmallRoots", f.format(fi.getValueForTree(st.tr[i], st.sp[jj].spDef.smallRootBiomass)));
                        elt = addString(elt, "TotalRoots", f.format(fi.getValueForTree(st.tr[i], st.sp[jj].spDef.totalRootBiomass)));
                        rootElt.addContent(elt);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try (FileOutputStream result = new FileOutputStream(pa)) {
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(doc, result);
            StartBrowser startBrowser = new StartBrowser(("file:///" + pa));
            startBrowser.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Element addString(Element elt, String variable, String text) {
        Element var = new Element(variable);
        var.addContent(text);
        elt.addContent(var);
        return elt;
    }
}
