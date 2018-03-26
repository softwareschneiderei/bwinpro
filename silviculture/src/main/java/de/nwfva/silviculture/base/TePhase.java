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
 *
 * ToDo: add logic to check conditions (e.g. Umbau or Normal)
 *
 */
public class TePhase extends XmlElement{

    //ToDo: define conditions for phase
    private final ArrayList<TeHeightInterval> chainLinks;
    private final ArrayList<TePhaseCondition> conditions;
    private String name;
    
    private long executionCounter;

    public TePhase(String name) {
        executionCounter = 0;
        this.name = name;
        chainLinks = new ArrayList<>();
        conditions = new ArrayList<>(2);       
        conditions.add(new PcStandTmpInteger());
    }

    public TePhase() {
        this(TePhase.class.getSimpleName());
    }

    public void checkAndExecute(Stand st) {
        boolean checked = true;
        for (TePhaseCondition c : conditions) {
            if (c.check(st) == false) {
                checked = false;
                break;
            }
        }
        if (checked) {
            System.out.println(name+" executed");
            executionCounter++;
            chainLinks.stream().forEach((e) -> {
                e.checkAndExecute(st);
            });
        }
    }

    public void addCondition(TePhaseCondition c) {
        conditions.add(c);
    }

    public void removeCondition(TePhaseCondition c) {
        conditions.remove(c);
    }

    public ArrayList<TePhaseCondition> getAllConditions() {
        return conditions;
    }

    public void add(TeHeightInterval te) {
        chainLinks.add(te);
    }

    public void remove(int index) {
        chainLinks.remove(index);
    }

    public void remove(TeHeightInterval te) {
        chainLinks.remove(te);
    }

    public void moveUp(int index) {
        if (index >= 1) {
            Collections.swap(chainLinks, index, index - 1);
        }
    }

    public void moveUp(TeHeightInterval te) {
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

    public void moveDown(TeHeightInterval te) {
        int i = chainLinks.indexOf(te);
        if (i >= 0) {
            moveDown(i);
        }
    }

    public ArrayList<TeHeightInterval> getAll() {
        return chainLinks;
    }

    public void set(ArrayList<TeHeightInterval> a) {
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
        sb.append("<").append(getClass().getSimpleName()).append(" name=\"").append(name).append("\">");
        //sb.append("<Conditions><![CDATA[ToDo: write logic: check if all requirements are met for this phase (e.g. BT <-> WET)]]></Conditions>");
        String ls = System.getProperty("line.separator");
        // add all conditions
        conditions.stream().forEach((e) -> {
            sb.append(ls);
            TeTools.toXML(e, sb);
        });
       // add all height intervals
        chainLinks.stream().forEach((e) -> {
            sb.append(ls);
            e.toXML(sb);
        });
        sb.append("</").append(getClass().getSimpleName()).append(">");
    }

    @Override
    public void parse(Node n) {
        // parse name       
        name = n.getAttributes().getNamedItem("name").getNodeValue();
        //System.out.println("phase:" + name);
        // parse height intervals
        chainLinks.clear();
        List<Node> elts = XmlTool.getChilds(n, TeHeightInterval.class.getSimpleName());
        elts.stream().forEach((i) -> {
            TeHeightInterval tei = new TeHeightInterval();
            tei.parse(i);
            add(tei);
        });
        // parse conditions
        parseConditions(n);
    }
    
    public void parseConditions(Node n) {
        conditions.clear();
        List<Node> elts = XmlTool.getChilds(n, TePhaseCondition.class.getSimpleName());
        String className;
        for (Node e : elts) {
            className = e.getAttributes().getNamedItem("class").getNodeValue();
            try {
                Class<?> c = ClassLoader.getSystemClassLoader().loadClass(className);
                //create new instance of class. if class implements TreatmentElement add it
                Object o = c.newInstance();
                if (o instanceof TePhaseCondition) {
                    TePhaseCondition con = (TePhaseCondition) o;
                    con.parse(e);
                    addCondition(con);
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

    private void TeClassMismatchException(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the executionCounter
     */
    public long getExecutionCounter() {
        return executionCounter;
    }
}