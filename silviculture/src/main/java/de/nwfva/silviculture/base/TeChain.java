/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import de.nwfva.silviculture.examplepathbuilder.ExamplePathBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;
import treegross.tools.XmlTool;

/**
 *
 * @author jhansen
 */
public class TeChain {
    
    private final ArrayList<TePhase> phases;
    private String name;
    private SpeciesDefMap sdm;
    
    public TeChain(SpeciesDefMap sdm) {
        this.sdm = sdm;
        System.out.println(this.sdm.getActualURL());
        name = TePhase.class.getClass().getSimpleName();
        phases = new ArrayList<>();
    }
    
    public TeChain(SpeciesDefMap sdm, String xmlFile) {
        this(sdm);
        parse(xmlFile);
    }
    
    public void execute(Stand st) {
        phases.stream().forEach((p) -> {
            p.checkAndExecute(st);
        });
    }
    
    public final void parse(String xmlFile) {
        phases.clear();
        name = "undefined";
        try {
            // read back;
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(xmlFile));
            
            NodeList nl = d.getElementsByTagName(getClass().getSimpleName());
            if (nl.getLength() <= 0) {
                return;
            }

            // we assume that there is only one TeChain element -> the root element
            Node chainElt = nl.item(0);

            // get Name of chain
            name = chainElt.getAttributes().getNamedItem("name").getNodeValue();

            // iterate through sub elementes (TePhase reatmentElement, TreatmentElementParameter)            
            List<Node> xPhases = XmlTool.getChilds(chainElt, TePhase.class.getSimpleName());
            xPhases.stream().forEach((p) -> {
                TePhase tep = new TePhase();
                tep.parse(p);                
                this.addTreatmentElement(tep);
            });           
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ExamplePathBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addTreatmentElement(TePhase tep) {
        phases.add(tep);
    }
    
    public void remove(int index) {
        phases.remove(index);
    }
    
    public void remove(TePhase phase) {
        phases.remove(phase);
    }
    
    public void moveUp(int index) {
        if (index >= 1) {
            Collections.swap(phases, index, index - 1);
        }
    }

    public void moveUp(TePhase te) {
        int i = phases.indexOf(te);
        if (i >= 0) {
            moveUp(i);
        }
    }
    
    public void moveDown(int index) {
        if (index < phases.size() - 1) {
            Collections.swap(phases, index, index + 1);
        }
    }

    public void moveDown(TePhase te) {
        int i = phases.indexOf(te);
        if (i >= 0) {
            moveDown(i);
        }
    }    
    
    public ArrayList<TePhase> getAll() {
        return phases;
    }
    
    public void clear() {
        phases.clear();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the sdm
     */
    public SpeciesDefMap getSdm() {
        return sdm;
    }

    /**
     * @param sdm the sdm to set
     */
    public void setSdm(SpeciesDefMap sdm) {
        this.sdm = sdm;
    }
    
    public String toXML() {
        StringBuilder sb = new StringBuilder();
        toXML(sb);
        return sb.toString();
    }
    
    public void toXML(StringBuilder sb) {
        sb.append("<").append(getClass().getSimpleName()).append(" name=\"").append(name).append("\">");
        String ls = System.getProperty("line.separator");
        phases.stream().forEach((e) -> {
            sb.append(ls);
            e.toXML(sb);
        });
        sb.append("</").append(getClass().getSimpleName()).append(">");
    }
    
    @Override
    public String toString(){
        return name;
    }    
}
