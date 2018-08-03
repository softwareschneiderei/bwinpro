package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Species;
import treegross.base.Stand;
import treegross.base.Tree;
import treegross.treatment.TreatmentElements2;

/**
 * Thinning from below
 *
 */
public class FromBelowThinner implements Thinner {
    private final double vout;
    private double thinned;

    public FromBelowThinner(double volumeAlreadyOut) {
        this.vout = volumeAlreadyOut;
    }

    @Override
    public void thin(Stand stand, Species species) {
        //Max. Harvestvolume is defined
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        double vmaxthinning = stand.size * stand.trule.maxThinningVolume - thinned;
        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((stand.size * stand.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = stand.size * stand.trule.maxOutVolume - vout;
        }
        if (vmaxthinning <= 0) {
            return;
        }
        double maxBasalAreaOut = TreatmentElements2.reduceBaOut(stand);
        double baout = 0.0;
        do {
            // update competition overlap for crop trees
            double dmin = 99999.9;
            int merk = -9;
            for (int i = 0; i < stand.ntrees; i++) {
                final Tree tree = stand.tr[i];
                if (tree.isLiving() && dmin > tree.d && !tree.habitat && tree.isOf(species)) {
                    dmin = tree.d;
                    merk = i;
                }
            }
            // Baum entfernen, sofern eine Überlappung besteht
            if (merk > -1 && baout < maxBasalAreaOut) {
                final Tree competitor = stand.tr[merk];
                competitor.takeOut(stand.year, OutType.THINNED);
                thinned += competitor.fac * competitor.v;
                baout += (competitor.fac * Math.PI * Math.pow(competitor.d / 200.0, 2.0)) / stand.size;
            } else {
                return;
            }
            if (baout >= maxBasalAreaOut) {
                return;
            }
        } while (thinned < vmaxthinning);//stop if max thinning amount is reached
    }
}
