package treegross.base.thinning;

import treegross.base.OutType;
import treegross.base.Species;
import treegross.base.Stand;
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
        thinFromBelow(stand);
    }

    public void thinFromBelow(Stand st) {
        //Max. Harvestvolume is defined
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        double vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;

        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }

        if (vmaxthinning > 0) {
            double maxBasalAreaOut = TreatmentElements2.reduceBaOut(st);

            double baout = 0.0;
            boolean doNotEndThinning = true;
            do {
                // update competition overlap for crop trees
                double dmin = 99999.9;
                int merk = -9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].isLiving()
                            && dmin > st.tr[i].d
                            && !st.tr[i].habitat) {
                        dmin = st.tr[i].d;
                        merk = i;
                    }
                }
                // Baum entfernen, sofern eine Überlappung besteht
                if (merk > -1 && baout < maxBasalAreaOut) {
                    st.tr[merk].out = st.year;
                    st.tr[merk].outtype = OutType.THINNED;
                    thinned = thinned + st.tr[merk].fac * st.tr[merk].v;
                    baout = baout + (st.tr[merk].fac * Math.PI * Math.pow((st.tr[merk].d / 200.0), 2.0)) / st.size;
                } else {
                    doNotEndThinning = false;
                }

                if (baout >= maxBasalAreaOut) {
                    doNotEndThinning = false;
                }
            } //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && doNotEndThinning);
        }
    }
}
