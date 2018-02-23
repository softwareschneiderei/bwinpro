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
    
    
public void report(Stand st,String programDir, String workingDir ){
     // Calculate and write xml
       String pa="";
       String dn="";
       pa = workingDir+System.getProperty("file.separator")+"rootbiomass.xml";
//
       NumberFormat f=NumberFormat.getInstance();
       f=NumberFormat.getInstance(new Locale("en","US"));
       f.setMaximumFractionDigits(2);
       f.setMinimumFractionDigits(2);
       f.setGroupingUsed(false);
       Element elt;
       Element elt2;
       Element elt3;
/** Creates an Treegross xml */
       Document doc = new Document();
       rootElt = new Element("Rootbiomass");
       ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                 "type=\"text/xsl\" href=\"rootbiomass.xsl\"");
       doc.addContent(pi);
       doc.setRootElement(rootElt);
       try {
// File 
        rootElt= addString(rootElt, "Id","1");
        rootElt= addString(rootElt, "Kennung",st.standname);
        rootElt= addString(rootElt, "Allgemeines"," ");
        rootElt= addString(rootElt, "Flaechengroesse_m2",new Double(st.size*10000).toString());
        rootElt= addString(rootElt, "HauptbaumArtCodeStd",new Integer(st.sp[0].code).toString());
        rootElt= addString(rootElt, "HauptbaumArtCodeLokal",new Integer(st.sp[0].code).toString());
        rootElt= addString(rootElt, "AufnahmeJahr",new Integer(st.year).toString());
        rootElt= addString(rootElt, "AufnahmeMonat",new Integer(st.monat).toString());
        rootElt= addString(rootElt, "DatenHerkunft",st.datenHerkunft);
        rootElt= addString(rootElt, "Standort",st.standort);
        rootElt= addString(rootElt, "Hochwert_m",new Double(st.hochwert_m).toString());
        rootElt= addString(rootElt, "Rechtswert_m",new Double(st.rechtswert_m).toString());
        rootElt= addString(rootElt, "Hoehe_uNN_m",new Double(st.hoehe_uNN_m).toString());
        rootElt= addString(rootElt, "Exposition_Gon",new Integer(st.exposition_Gon).toString());
        rootElt= addString(rootElt, "Hangneigung_Prozent",new Double(st.hangneigungProzent).toString());
        rootElt= addString(rootElt, "Wuchsgebiet",st.wuchsgebiet);
        rootElt= addString(rootElt, "Wuchsbezirk",st.wuchsbezirk);
        rootElt= addString(rootElt, "Standortskennziffer",st.standortsKennziffer);
    
// Table of functions
        for (int jj=0; jj< st.nspecies; jj++){
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(st.sp[jj].code).toString());
              elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
              elt = addString(elt, "Component", "CoarseRoot");
              elt = addString(elt, "Function", st.sp[jj].spDef.coarseRootBiomass);
              rootElt.addContent(elt);
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(st.sp[jj].code).toString());
              elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
              elt = addString(elt, "Component", "FineRoot");
              elt = addString(elt, "Function", st.sp[jj].spDef.fineRootBiomass);
              rootElt.addContent(elt);
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(st.sp[jj].code).toString());
              elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
              elt = addString(elt, "Component", "SmallRoot");
              elt = addString(elt, "Function", st.sp[jj].spDef.smallRootBiomass);
              rootElt.addContent(elt);
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(st.sp[jj].code).toString());
              elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
              elt = addString(elt, "Component", "TotalRoot");
              elt = addString(elt, "Function", st.sp[jj].spDef.totalRootBiomass);
              rootElt.addContent(elt);
               
           }
           
// Calculate stand total root biomass
           FunctionInterpreter fi = new FunctionInterpreter();
           for (int jj=0; jj<st.nspecies; jj++){
              double sumCR = 0.0;
              double sumFR = 0.0;
              double sumSR = 0.0;
              double sumTR = 0.0;
              for (int i=0;i < st.ntrees;i++)
                  if (st.tr[i].out < 0 && st.tr[i].code == st.sp[jj].code) {
                      sumCR = sumCR + st.tr[i].fac*fi.getValueForTree(st.tr[i], st.sp[jj].spDef.coarseRootBiomass);
                      sumFR = sumFR + st.tr[i].fac*fi.getValueForTree(st.tr[i], st.sp[jj].spDef.fineRootBiomass);
                      sumSR = sumSR + st.tr[i].fac*fi.getValueForTree(st.tr[i], st.sp[jj].spDef.smallRootBiomass);
                      sumTR = sumTR + st.tr[i].fac*fi.getValueForTree(st.tr[i], st.sp[jj].spDef.totalRootBiomass);
              }
              sumCR = sumCR/st.size;
              sumFR = sumFR/st.size;
              sumSR = sumSR/st.size;
              sumTR = sumTR/st.size;
              elt = new Element("Species");
              elt = addString(elt, "Code", new Integer(st.sp[jj].code).toString());
              elt = addString(elt, "Shortname", st.sp[jj].spDef.shortName);
              elt = addString(elt, "SumCoarseRoots", f.format(sumCR));
              elt = addString(elt, "SumFineRoots", f.format(sumFR));
              elt = addString(elt, "SumSmallRoots", f.format(sumSR));
              elt = addString(elt, "SumTotalRoots", f.format(sumTR));
              rootElt.addContent(elt);
           }
       
       for (int jj=0; jj< st.nspecies; jj++){
              for (int i=0;i < st.ntrees;i++)
                  if (st.tr[i].out < 0 && st.tr[i].code == st.sp[jj].code) {
               elt = new Element("Tree");
               elt = addString(elt, "Code", new Integer(st.tr[i].code).toString());
               elt = addString(elt, "Shortname", st.tr[i].sp.spDef.shortName);
               elt = addString(elt, "Shortname", st.tr[i].no);
               elt = addString(elt, "Age", new Integer(st.tr[i].age).toString());
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
       catch (Exception e){
		System.out.println(e); 
		} 
   
           
        try {
            File file = new File(pa);
            FileOutputStream result = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter();
//            outputter.setNewlines(true);
//            outputter.setIndent("  ");
            outputter.output(doc,result);
//
//            
            String seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+System.getProperty("file.separator")+pa;
            StartBrowser startBrowser = new StartBrowser(seite);   
            startBrowser.start();
        }
        catch (IOException e){
            e.printStackTrace();
        }


}    
    Element addString(Element elt, String variable, String text){
            Element var = new Element(variable);
            var.addContent(text);  
            elt.addContent(var);
            return elt;
    }

}
