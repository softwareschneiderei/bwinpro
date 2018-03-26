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
public class TeHarvestByGap implements TreatmentElement {

    private final String name = "Lochhieb";
    private final String label = "te_harvest_gaps";
    private final String desc = "te_harvest_gaps_info";

    private double maxVolume;
    private double lochdurchmesser;
    private String criterium;
    private double wert;

    private ArrayList<Object> defCriterium;

    public TeHarvestByGap(double maxVolume, double lochdurchmesser, String criterium, double wert) {
        init(maxVolume, lochdurchmesser, criterium, wert);
    }

    public TeHarvestByGap() {
        this(100.0, 15.0, "1 "+RBHolder.getResourceBundle().getString("te_crop30_d"), 60);
    }

    private void init(double maxVolume, double lochdurchmesser, String criterium, double wert) {
        this.maxVolume = maxVolume;
        this.lochdurchmesser = lochdurchmesser;
        this.criterium = criterium;
        this.wert = wert;
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
            if (startHarvest || st.status > 50) {
                if (st.status < 50) {
                    st.status = 50;
                }
                st.status = st.status + 1;
                TreatmentElements2 te2 = new TreatmentElements2();
                st.trule.maxHarvestVolume = maxVolume;
                double harvested = te2.getHarvestedOutVolume(st);
                double vmaxharvest = st.size * st.trule.maxHarvestVolume - te2.getHarvestedOutVolume(st);

                boolean done = false;
                while ((harvested < vmaxharvest && vmaxharvest > 0) && done == false) {
                    // create a gap, where the sum of the difference between crop tree diameter - d is max
                    // Habitat trees will have a negative impact   
                    //find tree with highest dbh over targetdiameter 
                    int merk = -9;
                    double maxdiff = -9999.9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].crop && st.tr[i].habitat == false) {
                            double diff = 0.0;
                            for (int j = 0; j < st.ntrees; j++) {
                                if (st.tr[j].out < 0 && (st.tr[j].crop || st.tr[j].habitat)) {
                                    double dist = Math.pow(st.tr[j].x - st.tr[i].x, 2.0) + Math.pow(st.tr[j].y - st.tr[i].y, 2.0);
                                    if (dist != 0.0) {
                                        dist = Math.sqrt(dist);
                                    }
                                    if (dist <= lochdurchmesser / 2.0) {
                                        diff += (st.tr[j].d - st.tr[j].sp.trule.targetDiameter);
                                    }
                                }
                            }
                            if (diff > 0.0 && diff > maxdiff) {
                                merk = i;
                                maxdiff = diff;
                            }
                        }
                    }
                    if (merk == - 9) {
                        done = true;
                    } else {
                        //see if there are target diameter trees , then harvest those trees
                        // conditions: no habitat tree, diameter > target diameter, standing, max harvest volume has not been reached
                        // and tree is greater than 12 m
                        for (int i = 0; i < st.ntrees; i++) {
                            if (st.tr[i].habitat == false && st.tr[i].out < 0 && st.tr[i].h > 12) {
                                double dist = Math.pow(st.tr[merk].x - st.tr[i].x, 2.0) + Math.pow(st.tr[merk].y - st.tr[i].y, 2.0);
                                if (dist != 0.0) {
                                    dist = Math.sqrt(dist);
                                }
                                if (dist <= 12.0 && st.tr[i].h > st.tr[merk].h * 0.25) {
//                                    vout += st.tr[i].fac * st.tr[i].v;
                                    harvested += st.tr[i].fac * st.tr[i].v;
                                    st.tr[i].out = st.year;
                                    st.tr[i].outtype = 3;
                                }
                            }
                        }
                    }
                }
                //check if there are  trees, 15 % above target diameter then harvest those
                // conditions: no habitat tree, diameter > target diameter, standing, max harvest volume has not been reached
                if (harvested < vmaxharvest && vmaxharvest > 0) {
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].habitat == false && st.tr[i].d > st.tr[i].sp.trule.targetDiameter * 1.15
                                && st.tr[i].out < 0 && harvested < vmaxharvest) {
                            if (te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                                break;
                            }
//                            vout += st.tr[i].fac * st.tr[i].v;
                            harvested += st.tr[i].fac * st.tr[i].v;
                            st.tr[i].out = st.year;
                            st.tr[i].outtype = 3;
                        }
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
        params.add(new TreatmentElementParameter("lochdurchmesser", RBHolder.getResourceBundle().getString("te_diameter"), RBHolder.getResourceBundle().getString("te_diameter_info"), lochdurchmesser));
        params.add(new TreatmentElementParameter("criterium", RBHolder.getResourceBundle().getString("te_criterium"), RBHolder.getResourceBundle().getString("te_condition"), criterium,
                TreatmentElementParameter.CONSTRAINT_TYPE_VALUES, defCriterium));
        params.add(new TreatmentElementParameter("wert", RBHolder.getResourceBundle().getString("te_value"), RBHolder.getResourceBundle().getString("te_greater_than"), wert));
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
                case "lochdurchmesser":
                    lochdurchmesser = Double.parseDouble(value);
                    break;
                case "criterium":
                    criterium = value;
                    break;
                case "wert":
                    wert = Double.parseDouble(value);
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
        //init with class values 
        init(100.0, 15.0, "1 "+RBHolder.getResourceBundle().getString("te_clearcut_d100"), 60);
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
