/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;
import treegross.treatment.TreatmentElements2;

/**
 *
 * @author jhansen
 */
public class TeHarvestByClearCut implements TreatmentElement {
    
  
    private final String name = "Kahlschlag";
    private final String label = "te_clearcut";
    private final String desc = "te_clearcut_info";
    
    private String criterium;
    private double wert;
    private int phaseNo;

    private ArrayList<Object> defCriterium;

    public TeHarvestByClearCut(String criterium, double wert, int phaseNo) {
        init(criterium, wert, phaseNo);
    }

    public TeHarvestByClearCut() {
        this("1 "+RBHolder.getResourceBundle().getString("te_crop30_d"), 60,0);
    }

    private void init(String criterium, double wert, int phaseNo) {
        this.criterium = criterium;
        this.wert = wert;
        this.phaseNo = phaseNo;
        defCriterium = new ArrayList<>(3);
        defCriterium.add("1 "+ RBHolder.getResourceBundle().getString("te_crop30_d"));
        defCriterium.add("2 "+ RBHolder.getResourceBundle().getString("te_clearcut_age"));
        defCriterium.add("3 "+ RBHolder.getResourceBundle().getString("te_clearcut_h100"));
    }

    //logic of treatment element:
    public void makeHarvest(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            boolean startHarvest = false;
            if (criterium.startsWith("1") && st.status < 50) {
        // Check if 30% of crop trees > wert
                double anz = 0;
                double anzGtWert = 0;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].crop == true) {
                        anz = anz + st.tr[i].fac;
                        if (st.tr[i].d > wert){
                            anzGtWert = anzGtWert + st.tr[i].fac;
                        }
                    }
                }
                int sumctwanted = 0;
                for (int i = 0; i < st.nspecies; i++) {
                    sumctwanted = sumctwanted + st.sp[i].trule.numberCropTreesWanted;
                }

                if (anz < 1) st.status=1;
                if (anz > 0.0){
                    if (anzGtWert/anz > 0.3 && anz/st.size > sumctwanted*0.4) startHarvest = true;
                }
            }
            if (criterium.startsWith("2") && st.sp[0].h100age >= wert) {
                startHarvest = true;
            }
            if (criterium.startsWith("3") && st.h100 >= wert) {
                startHarvest = true;
            }
            if (startHarvest) {
                TreatmentElements2 te2 = new TreatmentElements2();
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 1 && st.tr[i].d >= 7.0 && !st.tr[i].habitat) {
                        if (te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                            break;
                        }
                        st.tr[i].out = st.year;
                        st.tr[i].outtype = 3;
                    }
                }
                st.temp_Integer = phaseNo;
            }
        }
        //System.out.println(name + " executed");
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
        params.add(new TreatmentElementParameter("criterium", RBHolder.getResourceBundle().getString("te_criterium"), RBHolder.getResourceBundle().getString("te_condition"), criterium,
                TreatmentElementParameter.CONSTRAINT_TYPE_VALUES, defCriterium));
        params.add(new TreatmentElementParameter("wert", RBHolder.getResourceBundle().getString("te_value"), RBHolder.getResourceBundle().getString("te_greater_than"), wert));
        params.add(new TreatmentElementParameter("phaseNo",RBHolder.getResourceBundle().getString("te_phaseNo"), RBHolder.getResourceBundle().getString("te_phaseNo_info"), phaseNo));
        return params;
    }

    @Override
    public void parse(Node n) {
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            //System.out.println("parsing: "+aName+" value: " +value);
            switch (aName) {
                case "criterium":
                    criterium = value;
                    break;
                case "wert":
                    wert = Double.parseDouble(value);
                    break;
                case "phaseNo":
                    phaseNo = Integer.parseInt(value);
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
        init("1 "+RBHolder.getResourceBundle().getString("te_clearcut_d100"), 60, 0);
        // try to init with stand values if stand is not null
        if (st != null) {
            if (st.sp.length > 0) {
                wert = st.sp[0].trule.targetDiameter;
                return;
            }
        }
        //if st is null use beach values from species def map        
        if (sdm != null && sdm.getByCode(211) != null) {
            wert = sdm.getByCode(211).targetDiameter;
        }
    }    
}
