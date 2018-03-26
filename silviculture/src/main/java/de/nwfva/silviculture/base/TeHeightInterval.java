/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import treegross.base.Stand;
import treegross.tools.XmlTool;

/**
 *
 * @author jhansen
 */
public class TeHeightInterval extends XmlElement {

    private final ArrayList<TreatmentElement> chainLinks;
    private String name;
    private double startHeight, endHeight;

    public TeHeightInterval(String name, double startHeight, double endHeight) {
        this.name = name;
        chainLinks = new ArrayList<>();
        this.startHeight = startHeight;
        this.endHeight = endHeight;
    }

    public TeHeightInterval(String name) {
        this(name, 0, 200);
    }

    public TeHeightInterval() {
        this(TeHeightInterval.class.getSimpleName());
    }

    public void checkAndExecute(Stand st) {
        if (st != null && st.h100 >= startHeight && st.h100 < endHeight) {
            chainLinks.stream().forEach((e) -> {
                e.execute(st);
            });
        }
    }

    public void addTreatmentElement(TreatmentElement te) {
        chainLinks.add(te);
    }

    public void remove(int index) {
        chainLinks.remove(index);
    }

    public void remove(TreatmentElement te) {
        chainLinks.remove(te);
    }

    public void moveUp(int index) {
        if (index >= 1) {
            Collections.swap(chainLinks, index, index - 1);
        }
    }

    public void moveUp(TreatmentElement te) {
        int i = chainLinks.indexOf(te);
        if (i >= 0) {
            moveUp(i);
        }
    }

    public void moveDown(int index) {
        if (index < chainLinks.size() - 1) {
            Collections.swap(chainLinks, index, index + 1);
        }
    }

    public void moveDown(TreatmentElement te) {
        int i = chainLinks.indexOf(te);
        if (i >= 0) {
            moveDown(i);
        }
    }

    public ArrayList<TreatmentElement> getAll() {
        return chainLinks;
    }

    public void set(ArrayList<TreatmentElement> a) {
        chainLinks.clear();
        chainLinks.addAll(a);
    }

    public void clear() {
        chainLinks.clear();
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

    @Override
    public String toXML() {
        StringBuilder sb = new StringBuilder();
        toXML(sb);
        return sb.toString();
    }

    @Override
    public void toXML(StringBuilder sb) {
        sb.append("<").append(getClass().getSimpleName()).append(" name=\"").append(name).append("\" ");
        sb.append("startHeight=\"").append(startHeight).append("\" ");
        sb.append("endHeight=\"").append(endHeight).append("\">");
        String ls = System.getProperty("line.separator");
        chainLinks.stream().forEach((e) -> {
            sb.append(ls);
            TeTools.toXML(e, sb);
        });
        sb.append("</").append(getClass().getSimpleName()).append(">");
    }

    @Override
    public void parse(Node n) {
        chainLinks.clear();
        name = n.getAttributes().getNamedItem("name").getNodeValue();
        startHeight = Double.parseDouble(n.getAttributes().getNamedItem("startHeight").getNodeValue());
        endHeight = Double.parseDouble(n.getAttributes().getNamedItem("endHeight").getNodeValue());
        List<Node> elts = XmlTool.getChilds(n, TreatmentElement.class.getSimpleName());        
        String className;
        for (Node e : elts) {
            className = e.getAttributes().getNamedItem("class").getNodeValue();
            try {
                Class<?> c = ClassLoader.getSystemClassLoader().loadClass(className);
                //create new instance of class. if class implements TreatmentElement add it
                Object o = c.newInstance();
                if (o instanceof TreatmentElement) {
                    //System.out.println(o.getClass());
                    TreatmentElement telt = (TreatmentElement) o;
                    telt.parse(e);
                    this.addTreatmentElement(telt);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(TePhase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the startHeight
     */
    public double getStartHeight() {
        return startHeight;
    }

    /**
     * @param startHeight the startHeight to set
     */
    public void setStartHeight(double startHeight) {
        this.startHeight = startHeight;
    }

    /**
     * @return the endHeight
     */
    public double getEndHeight() {
        return endHeight;
    }

    /**
     * @param endHeight the endHeight to set
     */
    public void setEndHeight(double endHeight) {
        this.endHeight = endHeight;
    }
}
