/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.examplepathbuilder;

import de.nwfva.silviculture.base.TeChain;
import de.nwfva.silviculture.base.TeHeightInterval;
import de.nwfva.silviculture.base.TePhase;
import de.nwfva.silviculture.base.TeSTrails;
import de.nwfva.silviculture.base.TeSwitchPhaseCondition;
import de.nwfva.silviculture.base.TeThinFromAbove;
import de.nwfva.silviculture.base.TeThinFromBelow;
import de.nwfva.silviculture.base.TeTools;
import de.nwfva.silviculture.base.TreatmentElement;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 */
public class ExamplePathBuilder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //fool around with TeSTrails
        TeSTrails te = new TeSTrails();
        te.execute(null);
        System.out.println(te.getDescription());
        System.out.println(te.getRequiredParameters().get(0).getValue().getClass());
        System.out.println(te.getRequiredParameters().get(1).getValue().getClass());

        System.out.println(TeTools.toXML(te));

        // create a treatment elment chain
        TeChain chain = new TeChain(null);
        chain.setName("chain1");

        TePhase p1 = new TePhase();
        p1.setName("phase1");         
        TeHeightInterval hi1 = new TeHeightInterval("hi1",0,12);     
        p1.add(hi1);
        hi1.addTreatmentElement(new TeThinFromAbove());
        chain.addTreatmentElement(p1);

        TePhase p2 = new TePhase();
        p2.addCondition(new TeSwitchPhaseCondition());
        p2.setName("phase2");
        TeHeightInterval hi2 = new TeHeightInterval("hi2",0,12);
        p2.add(hi2);
        hi2.addTreatmentElement(new TeThinFromBelow());
        chain.addTreatmentElement(p2);

        //System.out.println(chain.toXML()); 
        //write chain to xml
        String file = "/home/jhansen/Schreibtisch/testxml.xml";
        try {
            TeTools.writeToFile(file, chain.toXML());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExamplePathBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        // read chain from xml and execute
        TeChain chain2 = new TeChain(null);
        chain2.parse(file);
        chain2.execute(new Stand());

        // print chain2 to std out
        System.out.println(chain2.toXML());

        // list all internal! TreatmentElements ToDo: write method to scan for 
        //TreatmentElement implementations in complete class path
        List<Class<?>> c = TeTools.getAllTreatmentElements();
        System.out.println("available TreatmentElements");
        c.stream().forEach((sc) -> {
            System.out.print(sc.getCanonicalName());
            try {
                // make instance and print description
                TreatmentElement ni = (TreatmentElement) sc.newInstance();
                System.out.print(": " + ni.getLabel() + " -> " + ni.getDescription() + "\n");
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ExamplePathBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

}
