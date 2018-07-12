package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.CropTreeSelection;
import treegross.treatment.CropTreeSpecies;
import treegross.treatment.TreatmentElements2;

/**
 * thin area between crop trees
 *
 */
public class FromAboveThinner implements Thinner {

    private final double vout;
    private double thinned;

    public FromAboveThinner(double volumeAlreadyOut) {
        this.vout = volumeAlreadyOut;
    }

    // selectCropTreesOfAllSpecies auch hier einbeziehen?
    @Override
    public void thin(Stand stand) {
        //System.out.println("temporäre Zwischenfelder durchforsten");
        //select temp crop trees (wet species)
        stand.resetTempCropTrees();
        selectTempCropTreesTargetPercentage(stand);
        // Start thinning for all species
        thinTempCropTreeCompetition(stand);
        if (stand.trule.thinArea) {
            thinCompetitionFromAbove(stand, thinned, vout);
        }
    }

    /**
     * select croptrees dependent on target percentages of species
     *
     * @param st stand object
     */
    public void selectTempCropTreesTargetPercentage(Stand st) {

        CropTreeSpecies ctspecies[] = new CropTreeSpecies[30];

        //Distance CropTrees
        double dist_ct;
        //Number of Crop Trees
        int n_ct_ha;
        //Find Crownwidth
        //Define CTSpecies
        //Initialize Temp Croptrees dependent on target mixture percentage and height of first thinning
        for (int i = 0; i < st.nspecies; i++) {
            //if (st.sp[i].nha>0){
            Tree atree = new Tree();
            atree.st = st;
            atree.code = st.sp[i].code;
            atree.d = st.sp[i].d100;
            atree.sp = st.sp[i];
            dist_ct = atree.calculateCw();

            //Number of temp crop trees dependent on calcualted distance and target mixture percent
            // number: 1/2 of matching tree number
            n_ct_ha = (int) ((10000.0 / ((Math.PI * Math.pow(dist_ct, 2.0)) / 4)) * st.sp[i].trule.targetCrownPercent / 100.0);

            //Initialize Temp CropTreeSpecies
            ctspecies[i] = new CropTreeSpecies();
            ctspecies[i].addCtsp(st.sp[i].code, n_ct_ha, dist_ct, st.sp[i].trule.minCropTreeHeight);
            //}

        }
        //Select Temp Croptrees dependent on target mixture percentage
        new CropTreeSelection().selectTempCropTrees(st, ctspecies);
    }
    
    public static void thinCompetitionFromAbove(Stand st, double thinned, double vout) {
        //set max thinning volume (vmaxthinning) if outaken amount (vout)
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        double vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;
        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }
        if (vmaxthinning > 0) {
            // Thinning is done iteratively tree by tree
            // 1. Calculate the overlap of all crop trees
            // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
            //    Heidi Doebbeler and crown width functions
            // 3. Find tree with the highest differenz in overlap - tolerable overlap
            // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
            // 5. Start with 1. again
            // Festlegen der Grundflächenabsenkung
            st.bha = 0.0;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out == -1) {
                    st.bha += Math.PI * Math.pow(st.tr[i].d / 200.0, 2.0) * st.tr[i].fac;
                }
            }
            st.bha = st.bha / st.size;
            double maxBasalAreaOut = TreatmentElements2.reduceBaOut(st);
            boolean continueThinning = true;
            if (maxBasalAreaOut <= 0.0) {
                return;
            }
            do {
                // find non crop tree with most competition to other trees, defined as that the maximum overlap area for neighbor trees
                int indextree = -9;
                double maxOverlap = -99999.9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].d > 7 && st.tr[i].isLiving() && st.tr[i].crop == false && st.tr[i].tempcrop == false && st.tr[i].habitat == false) {
                        double ovlp = 0.0;
                        for (int j = 0; j < st.tr[i].nNeighbor; j++) {
                            double distance = Math.sqrt(Math.pow(st.tr[i].x - st.tr[st.tr[i].neighbor[j]].x, 2.0) + Math.pow(st.tr[i].y - st.tr[st.tr[i].neighbor[j]].y, 2.0));
                            double ri = st.tr[i].cw / 2.0;
                            double rj = st.tr[st.tr[i].neighbor[j]].cw / 2.0;
                            // only if there is an overlap and ri > rj
                            if (ri + rj > distance && ri > rj) {
                                ovlp += TreatmentElements2.overlap(rj, ri, distance);
                            }
                        }
                        if (ovlp > maxOverlap) {
                            maxOverlap = ovlp;
                            indextree = i;
                        }
                    }
                }
                // release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
                // crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
                // into the limit of twice the crown radius of the crop tree size
                // if merk > 9 then cut tree else stop crop tree release
                if (indextree == -9) {
                    continueThinning = false;
                } else {
                    st.tr[indextree].out = st.year;
                    st.tr[indextree].outtype = OutType.THINNED;
                    thinned = thinned + (st.tr[indextree].fac * st.tr[indextree].v);
                    maxBasalAreaOut = maxBasalAreaOut - (st.tr[indextree].fac * Math.PI * Math.pow(st.tr[indextree].d / 200.0, 2.0)) / st.size;
                    if (maxBasalAreaOut <= 0.0) {
                        continueThinning = false;
                    }
                }
            } //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && continueThinning);
        }
    }
    
    /**
     * Check all trees if they are no crop tree, if they are competing with a
     * crop tree Mesurement: a-value This void needs CropTreeSelection Thinning
     * degree for each species is taken from st.tr[j].sp.trule.thinningIntensity
     *
     * @param st stand object
     */
    public void thinTempCropTreeCompetition(Stand st) {
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        double vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;

        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }

        if (vmaxthinning > 0) {
        // Thinning is done iteratively tree by tree
            // 1. Calculate the overlap of all crop trees
            // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
            //    Heidi Doebbeler and crown width functions
            // 3. Find tree with the highest differenz in overlap - tolerable overlap
            // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
            // 5. Start with 1. again 

            double maxBasalAreaOut = TreatmentElements2.reduceBaOut(st);
            
            double intensity = st.trule.thinningIntensity;
            if (intensity == 0.0) {
                intensity = 1.0;
            }

            boolean continueThinning = true;
            if (maxBasalAreaOut <= 0.0) {
                return;
            }
            do {
// update competition overlap for crop trees 
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].isLiving() && st.tr[i].tempcrop) {
                        st.tr[i].updateCompetition();
                    }
                }
// find crop with most competition, defined as that tree with greates ratio of
// actual c66xy devided by maximum c66
                int indexOfCroptree = -9;
                double maxCompetition = -99999.9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].isLiving() && st.tr[i].tempcrop) {
// calculate maxc66
                        double c66Ratio = TreatmentElements2.calculateC66Ratio(st.tr[i], st.trule.thinningIntensity);
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
// Find neighbor who comes closest 
                if (indexOfCroptree < 0) {
                    continueThinning = false;
                } else {
                    double dist = 9999.0;
                    int merk = -9;
                    double h66 = st.tr[indexOfCroptree].cb;
                    for (int i = 0; i < st.tr[indexOfCroptree].nNeighbor; i++) {
                        if (st.tr[st.tr[indexOfCroptree].neighbor[i]].d < 7
                                && st.tr[st.tr[indexOfCroptree].neighbor[i]].out < 0
                                && (st.trule.cutCompetingCropTrees || st.tr[st.tr[indexOfCroptree].neighbor[i]].tempcrop == false)
                                && !st.tr[st.tr[indexOfCroptree].neighbor[i]].habitat) {
                            double radius = st.tr[st.tr[indexOfCroptree].neighbor[i]].calculateCwAtHeight(h66) / 2.0;
                            double ent = Math.sqrt(Math.pow(st.tr[indexOfCroptree].x - st.tr[st.tr[indexOfCroptree].neighbor[i]].x, 2.0)
                                    + Math.pow(st.tr[indexOfCroptree].y - st.tr[st.tr[indexOfCroptree].neighbor[i]].y, 2.0));
                            if ((ent - radius < st.tr[indexOfCroptree].cw * (0.75 / intensity)) && dist > (ent - radius)) {
                                merk = st.tr[indexOfCroptree].neighbor[i];
                                dist = ent - radius;
                            }
                        }
                    }
// if merk > 9 then cut tree else stop crop tree release                    
                    if (merk == -9) {
                        continueThinning = false;
                    } else {
                        st.tr[merk].out = st.year;
                        st.tr[merk].outtype = OutType.THINNED;
                        thinned += st.tr[merk].fac * st.tr[merk].v;
                        maxBasalAreaOut = maxBasalAreaOut - (st.tr[merk].fac * Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0)) / st.size;
                        if (maxBasalAreaOut <= 0.0) {
                            continueThinning = false;
                        }
                    }
                }

            } //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && continueThinning);
        }
    }

}
