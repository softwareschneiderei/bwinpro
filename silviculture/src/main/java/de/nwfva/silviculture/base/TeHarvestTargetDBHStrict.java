/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.TreatmentElements2;

/**
 *
 * @author jhansen
 */
public class TeHarvestTargetDBHStrict implements TreatmentElement {

   private final String name = "harvest by target dbh";
    private final String label = "harvest_targetD";
    private final String desc = "harvest_targetD_info";

    private double maxVolume;
    private String neuezst;

    public TeHarvestTargetDBHStrict(double maxVolume, String neuezst) {
        init(maxVolume,  neuezst);
    }

    public TeHarvestTargetDBHStrict() {
        this(100.0,  "default");
        //ToDO: use values: in  makeHarvest st.sp[].trule targetDiameter is used
    }

    private void init(double maxVolume,  String neuezst) {
        this.maxVolume = maxVolume;
        this.neuezst = neuezst;
    }

    //logic of treatment element:
    public void makeHarvest(Stand st) {
        // overwrite with new target diameter
        if (neuezst.equals("default") == false) {
            String[] sArray = neuezst.split(";");
            for (int i = 0; i < sArray.length; i++) {
                int mm = sArray[i].indexOf("=");
                int artx = 0;
                double z = -9.0;
                try {
                    artx = Integer.parseInt(sArray[i].substring(0, 3));
                    z = Double.parseDouble(sArray[i].substring(mm + 1, sArray[i].length()));

                    for (int j = 0; j < st.nspecies; j++) {
                        if (st.sp[j].code == artx) {
                            st.sp[j].trule.targetDiameter = z;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Fehler im Zielstärkenstring");
                }

            }
           
        }
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            TreatmentElements2 te2 = new TreatmentElements2();
            st.trule.maxHarvestVolume = maxVolume;
            double harvested = te2.getHarvestedOutVolume(st);
            double vmaxharvest = st.size * st.trule.maxHarvestVolume - harvested;

            // if there is a amount to be harvested
            if (vmaxharvest > 0 && harvested < vmaxharvest) {
                //Sort st.tr by difference targetdiameter -diameter ascending        
                Tree trtemp;
                for (int i = 0; i < st.ntrees - 1; i++) {
                    for (int j = i + 1; j < st.ntrees; j++) {
                        if ((st.tr[i].sp.trule.targetDiameter - st.tr[i].d) > (st.tr[j].sp.trule.targetDiameter - st.tr[j].d)) {
                            trtemp = st.tr[i];
                            st.tr[i] = st.tr[j];
                            st.tr[j] = trtemp;
                        }
                    }
                }
                //see if there are target diameter trees , then harvest those trees
                // conditions: no habitat tree, diameter > target diameter, standing, max harvest volume has not been reached
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].habitat == false && st.tr[i].d > st.tr[i].sp.trule.targetDiameter
                            && st.tr[i].out < 0 && harvested < vmaxharvest) {
                        if (te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                            break;
                        }
                        harvested += st.tr[i].fac * st.tr[i].v;
                        st.tr[i].out = st.year;
                        st.tr[i].outtype = 3;
                        //st.tr[i].no+="_zs";
                    }
                }
            }

        }
        System.out.println(name + " executed");
    }

    @Override
    public String toString() {
        return getLabel();
    }

    // interface overrides: 
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return RBHolder.getResourceBundle().getString(label);
    }

    @Override
    public String getDescription() {
        return RBHolder.getResourceBundle().getString(desc);
    }

    @Override
    public void execute(Stand st) {
        makeHarvest(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("maxVolume", RBHolder.getResourceBundle().getString("te_maxvolume"), RBHolder.getResourceBundle().getString("te_maxvolume_info"), maxVolume));
        params.add(new TreatmentElementParameter("neuezst", "Zielstärke", "neue Zielstärke", neuezst));
        return params;
    }

    @Override
    public void parse(Node n) {
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            switch (aName) {
                case "maxVolume":
                    maxVolume = Double.parseDouble(value);
                    break;
                case "neuezst":
                    neuezst = value;
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_HARVESTING;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        System.out.println(st);
        //init with class values 
        init(100, "default");
        // try to init with stand values if stand is not null
        if (st != null) {
            if (st.sp.length > 0) {
                neuezst="";
                for (int i=0;i< st.nspecies;i++){
                    String txt = Double.toString(st.sp[i].spDef.targetDiameter);
                    String tart = Integer.toString(st.sp[i].code);
                    neuezst = neuezst+tart + "=" + txt + ";";
                }
                return;
            }
        }
        //if st is null use beach values from species def map        
        if (sdm != null && sdm.getByCode(211) != null) {
            neuezst="default";
        }
    } 
    
}
