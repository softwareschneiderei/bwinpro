package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.TreatmentElements2;

public abstract class AreaThinner implements Thinner {

    private final boolean thinArea;

    protected final double vout;
    protected double thinned;
    
    protected AreaThinner(boolean thinArea, double vout) {
        super();
        this.thinArea = thinArea;
        this.vout = vout;
    }
    
    protected final void thinArea(Stand stand) {
        if (thinArea) {
            thinCompetitionFromAbove(stand, thinned, vout);
        }
    }
    
    public void thinCompetitionFromAbove(Stand st, double thinned, double vout) {
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
                if (st.tr[i].isLiving()) {
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
                    final Tree tree = st.tr[i];
                    if (tree.d > 7 && tree.isLiving() && !tree.crop && !tree.tempcrop && !tree.habitat) {
                        double ovlp = 0.0;
                        for (int j = 0; j < tree.nNeighbor; j++) {
                            double distance = Math.sqrt(Math.pow(tree.x - st.tr[tree.neighbor[j]].x, 2.0) + Math.pow(tree.y - st.tr[tree.neighbor[j]].y, 2.0));
                            double ri = tree.cw / 2.0;
                            double rj = st.tr[tree.neighbor[j]].cw / 2.0;
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
                    Tree competitor = st.tr[indextree];
                    competitor.takeOut(st.year, OutType.THINNED);
                    thinned += (competitor.fac * competitor.v);
                    maxBasalAreaOut = maxBasalAreaOut - (competitor.fac * Math.PI * Math.pow(competitor.d / 200.0, 2.0)) / st.size;
                    if (maxBasalAreaOut <= 0.0) {
                        continueThinning = false;
                    }
                }
            } //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && continueThinning);
        }
    }
}
