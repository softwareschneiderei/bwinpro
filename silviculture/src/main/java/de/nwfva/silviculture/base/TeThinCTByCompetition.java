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
public class TeThinCTByCompetition implements TreatmentElement {

    private final String name = "Release Crop Trees";
    private final String label = "te_rct";
    private final String desc = "te_rct_info";

    private double maxOut;
    private double maxOverlap;
    private double nCompetitorToThin;

    public TeThinCTByCompetition(double maxOut, double maxOverlap,
            double nCompetitorToThin) {
        init(maxOut, maxOverlap, nCompetitorToThin);
    }

    public TeThinCTByCompetition() {
        this(50, 5.0, 1.0);
    }

    private void init(double maxOut, double maxOverlap, double nCompetitorToThin) {
        this.maxOut = maxOut;
        this.maxOverlap = maxOverlap;
        this.nCompetitorToThin = nCompetitorToThin;
    }

    //logic of treatment element:
    public void doThinning(Stand st) {
        if (st != null) {
            double thinNCompetitors = nCompetitorToThin;
            TreatmentElements2 te2 = new TreatmentElements2();
            //Max. Harvestvolume is defined
            //set max thinning volume (vmaxthinning) if outaken amount (vout) 
            //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
            double vmaxthinning = st.size * (maxOut - te2.getTreatmentOutVolume(st));
            double thinned = 0.0;
            if (vmaxthinning > 0) {
                // Thinning is done iteratively tree by tree
                // 1. Calculate the overlap of all crop trees
                // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
                //    Heidi Doebbeler and crown width functions
                // 3. Find tree with the highest differenz in overlap - tolerable overlap
                // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
                // 5. Start with 1. again

                /*double intensity = 2.0 - st.trule.thinningIntensity;
                 if (intensity == 0.0) {
                 intensity = 1.0;
                 }
                 //Festlegen der Grundflächenansenkung
                 double maxStandBasalArea = te2.getMaxStandBasalArea(st, true);

                 if (st.trule.thinningIntensity == 0.0) {
                 maxStandBasalArea = maxStandBasalArea * 100.0;
                 } else {
                 maxStandBasalArea = maxStandBasalArea * (2.0 - st.trule.thinningIntensity);
                 }
                 */
                // NEU: Stelle die Z-Bäume frei, aber durchforste nur, wenn der Baum ein extremer Bedränger ist
                int nct = 0;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].crop) {
                        nct = nct + 1;
                    }
                }

                boolean doNotEndThinning = true;
                thinNCompetitors = thinNCompetitors*nct;
                nct = 0;
                do {
// update competition overlap for crop trees 
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 ) {
                            st.tr[i].updateCompetition();
                        }
                    }
// find crop with most competition, defined as that tree with greates ratio of
// actual c66xy devided by maximum c66
                    int indexOfCroptree = -9;
                    double maxCompetition = -99999.9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].crop) {
                            // calculate maxc66
                            double maxBasalArea = st.tr[i].calculateMaxBasalArea() * st.tr[i].getModerateThinningFactor();
                            if (st.trule.thinningIntensity == 0.0) {
                                maxBasalArea = maxBasalArea * 100.0;
                            } else {
                                maxBasalArea = maxBasalArea * (2.0 - st.trule.thinningIntensity);
                            }
                            double maxN = maxBasalArea / (Math.PI * Math.pow((st.tr[i].d / 200.0), 2.0));
                            double maxC66 = maxN * Math.PI * Math.pow((st.tr[i].cw / 2.0), 2.0) / 10000.0;
                            double c66Ratio = st.tr[i].c66xy / maxC66;
                            // remember tree if c66Ratio is greater than maxCompetition
                            if (c66Ratio > maxCompetition) {
                                indexOfCroptree = i;
                                maxCompetition = c66Ratio;
                            }
                        }
                    }
// release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
// crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
// into the limit of twice the crown radius of the crop tree size 
//                 
// Find neighbor who comes closest , but species 
                    if (indexOfCroptree < 0) {
                        doNotEndThinning = false;
                    } else {
                        double flprozent = 0.0;
                        double flpromin = 99.9;
                        int merk = -9;
                        double h66 = st.tr[indexOfCroptree].h - 0.667 * (st.tr[indexOfCroptree].h - st.tr[indexOfCroptree].cb);
                        double r1 = st.tr[indexOfCroptree].cw / 2.0;
                        for (int i = 0; i < st.tr[indexOfCroptree].nNeighbor; i++) {
                            if (st.tr[st.tr[indexOfCroptree].neighbor[i]].d > 7
                                    && st.tr[st.tr[indexOfCroptree].neighbor[i]].out < 0
                                    && (st.trule.cutCompetingCropTrees || st.tr[st.tr[indexOfCroptree].neighbor[i]].crop == false)
                                    && st.tr[st.tr[indexOfCroptree].neighbor[i]].habitat == false ) {
                                double radius = st.tr[st.tr[indexOfCroptree].neighbor[i]].calculateCwAtHeight(h66) / 2.0;
                                double ent = 0.0;
                                if (radius > 0.0) {
                                    ent = Math.sqrt(Math.pow(st.tr[indexOfCroptree].x - st.tr[st.tr[indexOfCroptree].neighbor[i]].x, 2.0)
                                            + Math.pow(st.tr[indexOfCroptree].y - st.tr[st.tr[indexOfCroptree].neighbor[i]].y, 2.0));
//                                            flprozent = te2.overlap(r1, radius, ent) / (Math.PI * Math.pow(r1, 2.0));
                                    flprozent = ent - radius - r1 * (1.0 + maxOverlap);
                                    // Merken wenn das Flächenprozent größer ist

                                    if (ent < radius + r1 * (1.0 + maxOverlap) && flpromin > flprozent) {
                                        merk = st.tr[indexOfCroptree].neighbor[i];
                                        flpromin = flprozent;
                                    }
                                }

                            }
                        }
                        // if merk > 9 then cut tree else stop crop tree release
                        if (merk == -9) {
                            doNotEndThinning = false;
                        } else {
                            st.tr[merk].out = st.year;
                            st.tr[merk].outtype = 2;
                            thinned += (st.tr[merk].fac * st.tr[merk].v);
                            nct = nct +1;
                            if (nct >= thinNCompetitors) doNotEndThinning = false;
                        }
                    }
                } //stop if max thinning amount is reached or all competitors are taken out
                while (thinned < vmaxthinning && doNotEndThinning);
            }
        }
        System.out.println(name + " executed.");
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
        doThinning(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("maxOut", RBHolder.getResourceBundle().getString("te_maxdfvol"), RBHolder.getResourceBundle().getString("te_maxdfvol_info"), maxOut));
        params.add(new TreatmentElementParameter("maxOverlap", RBHolder.getResourceBundle().getString("te_maxoverlap"), RBHolder.getResourceBundle().getString("te_maxoverlap_info"), maxOverlap));
        params.add(new TreatmentElementParameter("nCompetitorToThin", RBHolder.getResourceBundle().getString("te_NCompThin"), RBHolder.getResourceBundle().getString("te_NCompThin_info"), nCompetitorToThin));
        return params;
    }

    @Override
    public void parse(Node n) {
        //ToDo Error handling
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            switch (aName) {
                case "maxOut":
                    maxOut = Double.parseDouble(value);
                    break;
                case "maxOverlap":
                    maxOverlap = Double.parseDouble(value);
                    break;
                case "nCompetitorToThin":
                    nCompetitorToThin = Double.parseDouble(value);
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_THINNING;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        init(50, 0.2, 1.0);
    }

}
