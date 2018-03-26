/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwfva.biomass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jdom.DocType;
import org.jdom.input.SAXBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import treegross.base.FunctionInterpreter;
import treegross.base.Tree;
import treegross.tools.XmlTool;

/**
 *
 * @author nagel
 */
public class Biomass {
    
    String xmlname ="";
    String localPath =""; 
    BiomassSetting2 bs[] = new BiomassSetting2[50];
    int nbs = 0;
    NumberFormat f;
    NumberFormat f4;

    
    public void calcAssortmentlist(String xmlname, String xmlout) {
        InputStream is = null;
        f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        f.setGroupingUsed(false);
        f4 = NumberFormat.getInstance(new Locale("en", "US"));
        f4.setMaximumFractionDigits(4);
        f4.setMinimumFractionDigits(4);
        f4.setGroupingUsed(false);

        try {
            this.xmlname = xmlname;
            File myFile = new File(xmlname);
            URL url = myFile.toURI().toURL();
            URLConnection urlcon = url.openConnection();
            is = urlcon.getInputStream();
            Element sortierung = null;
            try {

                Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
                sortierung = d.getDocumentElement();
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                System.out.println("Fehler");
            }
// Ausgabe vorbereiten
            DocumentBuilderFactory docFactory2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            try {

                docBuilder = docFactory2.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                doc.appendChild(doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"biomasse.xsl\""));
//                doc.createComment("Testing");

                // add root element (stand)
                Element rootElt = doc.createElement("Biomass");
                doc.appendChild(rootElt);

                treegross.tools.XmlTool xmltool2 = new treegross.tools.XmlTool();

// Durcharbeiten der Parzellen und erstellen einer weiteren XML            
                NodeList sortimente = sortierung.getElementsByTagName("Sortiment");
                Node n3;
                FunctionInterpreter fi = new FunctionInterpreter();
                if (sortimente != null && sortimente.getLength() > 0) {
                    for (int ip = 0; ip < sortimente.getLength(); ip++) {
                        n3 = sortimente.item(ip);
                        String sortname = XmlTool.getChildText(n3, "Name");
                        String art = XmlTool.getChildText(n3, "Art");
                        String baumnr = XmlTool.getChildText(n3, "Baum_Nr");
                        String entnahme = XmlTool.getChildText(n3, "Entnahme");
                        String jahr = XmlTool.getChildText(n3, "Entnahmejahr");
                        String typ = XmlTool.getChildText(n3, "Entnahmetyp");
                        String faktor = XmlTool.getChildText(n3, "fac_ha");
                        double d = Double.parseDouble(XmlTool.getChildText(n3, "BHD"));
                        double h = Double.parseDouble(XmlTool.getChildText(n3, "Hoehe"));
                        double vmr = Double.parseDouble(XmlTool.getChildText(n3, "Vol_mR"));
                        double vor = Double.parseDouble(XmlTool.getChildText(n3, "Vol_oR"));
                        double vsmr = Double.parseDouble(XmlTool.getChildText(n3, "VsmR"));
                        double vsor = Double.parseDouble(XmlTool.getChildText(n3, "VsoR"));
// Index der Biomassefuntionen suchen
                        int artnr = Integer.parseInt(art);
                        int index = getbsIndex(artnr);
// pass variables by tree object                    
                        Tree atree = new Tree();
                        atree.d = d;
                        atree.h = h;
                        double v = fi.getValueForTree(atree, bs[index].stemVolumeFunction);
                        double fac = v/vsmr;
                        double vmrc = vmr * fac;
                        double vorc = vor * fac;
                        vsmr = vsmr *fac;
                        vsor = vsor *fac;
                        double woodbm = (vorc/vsor)* (fi.getValueForTree(atree, bs[index].stemBMkg) 
                              + fi.getValueForTree(atree, bs[index].stumpBMkg));
                        double barkbm = ((vmrc-vorc)/(vsmr-vsor)) * (fi.getValueForTree(atree, bs[index].barkBMkg)
                              + fi.getValueForTree(atree, bs[index].stumpBarkBMkg));
                        double woodC = woodbm * fi.getValueForTree(atree, bs[index].woodFacC);
                        double barkC = barkbm * fi.getValueForTree(atree, bs[index].barkFacC);
                        double woodN = woodbm * fi.getValueForTree(atree, bs[index].woodFacN);
                        double barkN = barkbm * fi.getValueForTree(atree, bs[index].barkFacN);
                        double woodS = woodbm * fi.getValueForTree(atree, bs[index].woodFacS);
                        double barkS = barkbm * fi.getValueForTree(atree, bs[index].barkFacS);
                        double woodP = woodbm * fi.getValueForTree(atree, bs[index].woodFacP);
                        double barkP = barkbm * fi.getValueForTree(atree, bs[index].barkFacP);
                        double woodK = woodbm * fi.getValueForTree(atree, bs[index].woodFacK);
                        double barkK = barkbm * fi.getValueForTree(atree, bs[index].barkFacK);
                        double woodCa = woodbm * fi.getValueForTree(atree, bs[index].woodFacCa);
                        double barkCa = barkbm * fi.getValueForTree(atree, bs[index].barkFacCa);
                        double woodMg = woodbm * fi.getValueForTree(atree, bs[index].woodFacMg);
                        double barkMg = barkbm * fi.getValueForTree(atree, bs[index].barkFacMg);
                        System.out.println(baumnr + " " + art);

                        Element elt = null;
                        elt = doc.createElement("Sortiment");
                        xmltool2.addChildString(elt, "Art", art);
                        xmltool2.addChildString(elt, "Baum_Nr", baumnr);
                        xmltool2.addChildString(elt, "Name", sortname);
                        xmltool2.addChildString(elt, "fac_ha", faktor);
                        xmltool2.addChildString(elt, "Entnahmejahr", jahr);
                        xmltool2.addChildString(elt, "Entnahme", entnahme);
                        xmltool2.addChildString(elt, "Entnahmetyp", typ);
                        xmltool2.addChildString(elt, "Typ", "Holz");
                        xmltool2.addChildString(elt, "BHD", f.format(d));
                        xmltool2.addChildString(elt, "Hoehe", f.format(h));
                        xmltool2.addChildString(elt, "Vol_mR", f4.format(vorc));
                        xmltool2.addChildString(elt, "Biomasse", f4.format(woodbm));
                        xmltool2.addChildString(elt, "C", f4.format(woodC/1000.0));
                        xmltool2.addChildString(elt, "N", f4.format(woodN));
                        xmltool2.addChildString(elt, "S", f4.format(woodS));
                        xmltool2.addChildString(elt, "P", f4.format(woodP));
                        xmltool2.addChildString(elt, "K", f4.format(woodK));
                        xmltool2.addChildString(elt, "Ca", f4.format(woodCa));
                        xmltool2.addChildString(elt, "Mg", f4.format(woodMg));
                        rootElt.appendChild(elt);
// Rinde
                        elt = doc.createElement("Sortiment");
                        xmltool2.addChildString(elt, "Art", art);
                        xmltool2.addChildString(elt, "Baum_Nr", baumnr);
                        xmltool2.addChildString(elt, "Name", sortname);
                        xmltool2.addChildString(elt, "fac_ha", faktor);
                        xmltool2.addChildString(elt, "Entnahmejahr", jahr);
                        xmltool2.addChildString(elt, "Entnahme", entnahme);
                        xmltool2.addChildString(elt, "Entnahmetyp", typ);
                        xmltool2.addChildString(elt, "Typ", "Rinde");
                        xmltool2.addChildString(elt, "BHD", f.format(d));
                        xmltool2.addChildString(elt, "Hoehe", f.format(h));
                        xmltool2.addChildString(elt, "Vol_mR", f4.format((vmrc-vorc)));
                        xmltool2.addChildString(elt, "Biomasse", f4.format(barkbm));
                        xmltool2.addChildString(elt, "C", f4.format(barkC/1000.0));
                        xmltool2.addChildString(elt, "N", f4.format(barkN));
                        xmltool2.addChildString(elt, "S", f4.format(barkS));
                        xmltool2.addChildString(elt, "P", f4.format(barkP));
                        xmltool2.addChildString(elt, "K", f4.format(barkK));
                        xmltool2.addChildString(elt, "Ca", f4.format(barkCa));
                        xmltool2.addChildString(elt, "Mg", f4.format(barkMg));

                        rootElt.appendChild(elt);
// wenn Restholz dann auch BLätter und Reisig                        
                        if (sortname.indexOf("Restholz") > -1) {
                            double branch = fi.getValueForTree(atree, bs[index].branchBMkg) ;
                            double reisig = fi.getValueForTree(atree, bs[index].reisigBMkg) ;
                            double reisigC = reisig * fi.getValueForTree(atree, bs[index].reisigFacC);
                            double reisigN = reisig * fi.getValueForTree(atree, bs[index].reisigFacN);
                            double reisigS = reisig * fi.getValueForTree(atree, bs[index].reisigFacS);
                            double reisigP = reisig * fi.getValueForTree(atree, bs[index].reisigFacP);
                            double reisigK = reisig * fi.getValueForTree(atree, bs[index].reisigFacK);
                            double reisigCa = reisig * fi.getValueForTree(atree, bs[index].reisigFacCa);
                            double reisigMg = reisig * fi.getValueForTree(atree, bs[index].reisigFacMg);
                            double branchC = branch * fi.getValueForTree(atree, bs[index].branchFacC);
                            double branchN = branch * fi.getValueForTree(atree, bs[index].branchFacN);
                            double branchS = branch * fi.getValueForTree(atree, bs[index].branchFacS);
                            double branchP = branch * fi.getValueForTree(atree, bs[index].branchFacP);
                            double branchK = branch * fi.getValueForTree(atree, bs[index].branchFacK);
                            double branchCa = branch * fi.getValueForTree(atree, bs[index].branchFacCa);
                            double branchMg = branch * fi.getValueForTree(atree, bs[index].branchFacMg);
                            
                            elt = doc.createElement("Sortiment");
                            xmltool2.addChildString(elt, "Art", art);
                            xmltool2.addChildString(elt, "Baum_Nr", baumnr);
                            xmltool2.addChildString(elt, "Name", "Reisig");
                            xmltool2.addChildString(elt, "fac_ha", faktor);
                            xmltool2.addChildString(elt, "Entnahmejahr", jahr);
                            xmltool2.addChildString(elt, "Entnahme", entnahme);
                            xmltool2.addChildString(elt, "Entnahmetyp", typ);
                            xmltool2.addChildString(elt, "Typ", "Reisig");
                            xmltool2.addChildString(elt, "BHD", f.format(d));
                            xmltool2.addChildString(elt, "Hoehe", f.format(h));
                            xmltool2.addChildString(elt, "Vol_mR", "0.0");
                            xmltool2.addChildString(elt, "Biomasse", f4.format(reisig+branch));
                            xmltool2.addChildString(elt, "C", f4.format((reisigC+branchC)/1000.0));
                            xmltool2.addChildString(elt, "N", f4.format(reisigN+branchN));
                            xmltool2.addChildString(elt, "S", f4.format(reisigS+branchS));
                            xmltool2.addChildString(elt, "P", f4.format(reisigP+branchP));
                            xmltool2.addChildString(elt, "K", f4.format(reisigK+branchK));
                            xmltool2.addChildString(elt, "Ca", f4.format(reisigCa+branchCa));
                            xmltool2.addChildString(elt, "Mg", f4.format(reisigMg+branchMg));
                            rootElt.appendChild(elt);

                            double blaetter = fi.getValueForTree(atree, bs[index].leafBMkg);
                            double blaetterC = blaetter * fi.getValueForTree(atree, bs[index].leafFacC);
                            double blaetterN = blaetter * fi.getValueForTree(atree, bs[index].leafFacN);
                            double blaetterS = blaetter * fi.getValueForTree(atree, bs[index].leafFacS);
                            double blaetterP = blaetter * fi.getValueForTree(atree, bs[index].leafFacP);
                            double blaetterK = blaetter * fi.getValueForTree(atree, bs[index].leafFacK);
                            double blaetterCa = blaetter * fi.getValueForTree(atree, bs[index].leafFacCa);
                            double blaetterMg = blaetter * fi.getValueForTree(atree, bs[index].leafFacMg);
                            elt = doc.createElement("Sortiment");
                            xmltool2.addChildString(elt, "Art", art);
                            xmltool2.addChildString(elt, "Baum_Nr", baumnr);
                            xmltool2.addChildString(elt, "Name", "Blaetter");
                            xmltool2.addChildString(elt, "fac_ha", faktor);
                            xmltool2.addChildString(elt, "Entnahmejahr", jahr);
                            xmltool2.addChildString(elt, "Entnahme", entnahme);
                            xmltool2.addChildString(elt, "Entnahmetyp", typ);
                            xmltool2.addChildString(elt, "Typ", "Blaetter");
                            xmltool2.addChildString(elt, "BHD", f.format(d));
                            xmltool2.addChildString(elt, "Hoehe", f.format(h));
                            xmltool2.addChildString(elt, "Vol_mR", "0.0");
                            xmltool2.addChildString(elt, "Biomasse", f4.format(blaetter));
                            xmltool2.addChildString(elt, "C", f4.format(blaetterC/1000));
                            xmltool2.addChildString(elt, "N", f4.format(blaetterN));
                            xmltool2.addChildString(elt, "S", f4.format(blaetterS));
                            xmltool2.addChildString(elt, "P", f4.format(blaetterP));
                            xmltool2.addChildString(elt, "K", f4.format(blaetterK));
                            xmltool2.addChildString(elt, "Ca", f4.format(blaetterCa));
                            xmltool2.addChildString(elt, "Mg", f4.format(blaetterMg));

                            rootElt.appendChild(elt);

                        }

                    }
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);

                StreamResult result = new StreamResult(new File(xmlout));
                transformer.transform(source, result);

            } catch (Exception ex) {
                System.out.println("Fehler");
            }

        } catch (IOException e) {
            System.out.println("Fehler");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    System.out.println("Fehler");
                }
            }
        }

    }
    
    
    public void loadnbs(String biomassXML) {
        nbs = 0;
        try {
            File myFile = new File(biomassXML);
            URL url = myFile.toURI().toURL();
    
            SAXBuilder builder = new SAXBuilder();
            URLConnection urlcon = url.openConnection();

            org.jdom.Document doc = builder.build(urlcon.getInputStream());

            DocType docType = doc.getDocType();
//
            org.jdom.Element sets = doc.getRootElement();
            List setList = sets.getChildren("Species");
            Iterator i = setList.iterator();

            while (i.hasNext()) {
                org.jdom.Element set = (org.jdom.Element) i.next();
                bs[nbs] = new BiomassSetting2();
                bs[nbs].code = Integer.parseInt(set.getChild("Code").getText());
                bs[nbs].specieslist = set.getChild("Specieslist").getText();
                bs[nbs].stumpBMkg = set.getChild("StumpBMkg").getText();
                bs[nbs].stumpBarkBMkg = set.getChild("StumpBarkBMkg").getText();
                bs[nbs].leafBMkg = set.getChild("LeafBMkg").getText();
                bs[nbs].stemBMkg = set.getChild("StemBMkg").getText();
                bs[nbs].barkBMkg = set.getChild("BarkBMkg").getText();
                bs[nbs].branchBMkg = set.getChild("BranchBMkg").getText();
                try {
                   bs[nbs].reisigBMkg = set.getChild("ReisigBMkg").getText();
                } catch (Exception e) {
                   bs[nbs].reisigBMkg = "0.0"; 
                }
                bs[nbs].woodFacC = set.getChild("WoodFacC").getText();
                bs[nbs].woodFacN = set.getChild("WoodFacN").getText();
                bs[nbs].woodFacS = set.getChild("WoodFacS").getText();
                bs[nbs].woodFacP = set.getChild("WoodFacP").getText();
                bs[nbs].woodFacK = set.getChild("WoodFacK").getText();
                bs[nbs].woodFacCa = set.getChild("WoodFacCa").getText();
                bs[nbs].woodFacMg = set.getChild("WoodFacMg").getText();
                bs[nbs].woodFacMn = set.getChild("WoodFacMn").getText();
                bs[nbs].woodFacFe = set.getChild("WoodFacFe").getText();
                bs[nbs].woodFacBOup = set.getChild("WoodFacBOup").getText();
                bs[nbs].woodFacBNH4 = set.getChild("WoodFacBNH4").getText();
                bs[nbs].woodFacBNO3 = set.getChild("WoodFacBNO3").getText();
                bs[nbs].barkFacC = set.getChild("BarkFacC").getText();
                bs[nbs].barkFacN = set.getChild("BarkFacN").getText();
                bs[nbs].barkFacS = set.getChild("BarkFacS").getText();
                bs[nbs].barkFacP = set.getChild("BarkFacP").getText();
                bs[nbs].barkFacK = set.getChild("BarkFacK").getText();
                bs[nbs].barkFacCa = set.getChild("BarkFacCa").getText();
                bs[nbs].barkFacMg = set.getChild("BarkFacMg").getText();
                bs[nbs].barkFacMn = set.getChild("BarkFacMn").getText();
                bs[nbs].barkFacFe = set.getChild("BarkFacFe").getText();
                bs[nbs].barkFacBOup = set.getChild("BarkFacBOup").getText();
                bs[nbs].barkFacBNH4 = set.getChild("BarkFacBNH4").getText();
                bs[nbs].barkFacBNO3 = set.getChild("BarkFacBNO3").getText();
                bs[nbs].branchFacC = set.getChild("BranchFacC").getText();
                bs[nbs].branchFacN = set.getChild("BranchFacN").getText();
                bs[nbs].branchFacS = set.getChild("BranchFacS").getText();
                bs[nbs].branchFacP = set.getChild("BranchFacP").getText();
                bs[nbs].branchFacK = set.getChild("BranchFacK").getText();
                bs[nbs].branchFacCa = set.getChild("BranchFacCa").getText();
                bs[nbs].branchFacMg = set.getChild("BranchFacMg").getText();
                bs[nbs].branchFacMn = set.getChild("BranchFacMn").getText();
                bs[nbs].branchFacFe = set.getChild("BranchFacFe").getText();
                bs[nbs].branchFacBOup = set.getChild("BranchFacBOup").getText();
                bs[nbs].branchFacBNH4 = set.getChild("BranchFacBNH4").getText();
                bs[nbs].branchFacBNO3 = set.getChild("BranchFacBNO3").getText();
                try {
                    bs[nbs].reisigFacC = set.getChild("ReisigFacC").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacC = "0.0";
                }
                try {
                    bs[nbs].reisigFacN = set.getChild("ReisigFacN").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacN = "0.0";
                }
                try {
                    bs[nbs].reisigFacS = set.getChild("ReisigFacS").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacS = "0.0";
                }
                try {
                    bs[nbs].reisigFacP = set.getChild("ReisigFacP").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacP = "0.0";
                }
                try {
                    bs[nbs].reisigFacK = set.getChild("ReisigFacK").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacK = "0.0";
                }
                try {
                    bs[nbs].reisigFacCa = set.getChild("ReisigFacCa").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacCa = "0.0";
                }
                try {
                    bs[nbs].reisigFacMg = set.getChild("ReisigFacMg").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacMg = "0.0";
                }
                try {
                    bs[nbs].reisigFacMn = set.getChild("ReisigFacMn").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacMn = "0.0";
                }
                try {
                    bs[nbs].reisigFacFe = set.getChild("ReisigFacFe").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacFe = "0.0";
                }
                try {
                    bs[nbs].reisigFacBOup = set.getChild("ReisigFacBOup").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacBOup = "0.0";
                }
                try {
                    bs[nbs].reisigFacBNH4 = set.getChild("ReisigFacBNH4").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacBNH4 = "0.0";
                }
                try {
                    bs[nbs].reisigFacBNO3 = set.getChild("ReisigFacBNO3").getText();
                } catch (Exception e) {
                    bs[nbs].reisigFacBNO3 = "0.0";
                }

                bs[nbs].leafFacC = set.getChild("LeafFacC").getText();
                bs[nbs].leafFacN = set.getChild("LeafFacN").getText();
                bs[nbs].leafFacS = set.getChild("LeafFacS").getText();
                bs[nbs].leafFacP = set.getChild("LeafFacP").getText();
                bs[nbs].leafFacK = set.getChild("LeafFacK").getText();
                bs[nbs].leafFacCa = set.getChild("LeafFacCa").getText();
                bs[nbs].leafFacMg = set.getChild("LeafFacMg").getText();
                bs[nbs].leafFacMn = set.getChild("LeafFacMn").getText();
                bs[nbs].leafFacFe = set.getChild("LeafFacFe").getText();
                bs[nbs].leafFacBOup = set.getChild("LeafFacBOup").getText();
                bs[nbs].leafFacBNH4 = set.getChild("LeafFacBNH4").getText();
                bs[nbs].leafFacBNO3 = set.getChild("LeafFacBNO3").getText();
                bs[nbs].woodDensity = set.getChild("WoodDensity").getText();
                bs[nbs].stemVolumeFunction = set.getChild("StemVolume").getText();
                bs[nbs].taperFuctionClass = set.getChild("TaperClass").getText();
                bs[nbs].taperFunctionNumber = Integer.parseInt(set.getChild("TaperFunctionNumber").getText());
                nbs = nbs + 1;
            }

        } catch (Exception e) {
            System.out.println("Fehler: Keine Biomassenfunktionen geladen");
        }

    }
    
    private int getbsIndex(int code) {
        int index = -9;
        for (int i = 0; i < nbs; i++) {
            if (code == bs[i].code) {
                index = i;
            }
        }
        if (index < 0) {
            for (int i = 0; i < nbs; i++) {
                String co = Integer.toString(code);
                if (bs[i].specieslist.indexOf(co) > -1) {
                    index = i;
                }
            }
        }
        if (index < 0) {
            String co = Integer.toString(code);
            co = co.substring(0,1) + "11";
            int cod = Integer.parseInt(co);
            for (int i = 0; i < nbs; i++) {
                if (cod == bs[i].code) {
                    index = i;
                }
            }
        }
        return index;
    }
  
}
