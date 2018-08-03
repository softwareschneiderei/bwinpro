package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Species;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.CropTreeSelection;
import treegross.treatment.CropTreeSpecies;
import treegross.treatment.TreatmentElements2;

/**
 * thin area between crop trees
 *
 */
public class FromAboveThinner extends AreaThinner {

    public FromAboveThinner(double volumeAlreadyOut) {
        super(volumeAlreadyOut);
    }

    // selectCropTreesOfAllSpecies auch hier einbeziehen?
    @Override
    public void thin(Stand stand, Species species) {
        //System.out.println("temporäre Zwischenfelder durchforsten");
        //select temp crop trees (wet species)
        // TODO: http://issuetracker.intranet:20002/browse/BWIN-63 has this to be species specific?
        stand.forTreesMatching(tree -> tree.sp.code == species.code, tree -> tree.tempcrop = false);
        // TODO: http://issuetracker.intranet:20002/browse/BWIN-63 has this to be species specific?
        selectTempCropTreesTargetPercentage(stand);
        // Start thinning for all species
        thinTempCropTreeCompetition(stand, species);
        // TODO: http://issuetracker.intranet:20002/browse/BWIN-63 has this to be species specific?
        thinArea(stand);
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
        }
        //Select Temp Croptrees dependent on target mixture percentage
        new CropTreeSelection().selectTempCropTrees(st, ctspecies);
    }
    
    /**
     * Check all trees if they are no crop tree, if they are competing with a
     * crop tree Mesurement: a-value This void needs CropTreeSelection Thinning
     * degree for each species is taken from st.tr[j].sp.trule.thinningIntensity
     *
     * @param st stand object
     * @param species
     */
    public void thinTempCropTreeCompetition(Stand st, Species species) {
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
            
            double intensity = species.thinningIntensity();
            if (intensity == 0.0) {
                intensity = 1.0;
            }

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
                        double c66Ratio = TreatmentElements2.calculateC66Ratio(st.tr[i], species.thinningIntensity());
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
                    return;
                }
                int merk = findClosestNeighbor(st, indexOfCroptree, intensity);
// if merk > 9 then cut tree else stop crop tree release                    
                if (merk == -9) {
                    return;
                } else {
                    final Tree closestNeighbor = st.tr[merk];
                    closestNeighbor.takeOut(st.year, OutType.THINNED);
                    thinned += closestNeighbor.fac * closestNeighbor.v;
                    maxBasalAreaOut = maxBasalAreaOut - (closestNeighbor.fac * Math.PI * Math.pow(closestNeighbor.d / 200.0, 2.0)) / st.size;
                    if (maxBasalAreaOut <= 0.0) {
                        return;
                    }
                }
            } //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning);
        }
    }

    private int findClosestNeighbor(Stand st, int indexOfCroptree, double intensity) {
        double dist = 9999.0;
        int merk = -9;
        final Tree cropTree = st.tr[indexOfCroptree];
        double h66 = cropTree.cb;
        for (int i = 0; i < cropTree.nNeighbor; i++) {
            final Tree neighbor = st.tr[cropTree.neighbor[i]];
            if (neighbor.d < 7
                    && neighbor.isLiving()
                    && (st.trule.cutCompetingCropTrees || !neighbor.tempcrop)
                    && !neighbor.habitat) {
                double radius = neighbor.calculateCwAtHeight(h66) / 2.0;
                double ent = Math.sqrt(Math.pow(cropTree.x - neighbor.x, 2.0)
                        + Math.pow(cropTree.y - neighbor.y, 2.0));
                if ((ent - radius < cropTree.cw * (0.75 / intensity)) && dist > (ent - radius)) {
                    merk = cropTree.neighbor[i];
                    dist = ent - radius;
                }
            }
        }
        return merk;
    }
}
