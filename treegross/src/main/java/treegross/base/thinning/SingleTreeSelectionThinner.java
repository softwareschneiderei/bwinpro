package treegross.base.thinning;

import treegross.base.Stand;
import treegross.treatment.TreatmentElements2;

/**
 * Thinning by releasing the crop trees
 *
 */
public class SingleTreeSelectionThinner implements Thinner {

    @Override
    public void thin(TreatmentElements2 te, Stand stand) {
        if (!stand.trule.releaseCropTrees) {
            return;
        }
        te.thinCropTreeCompetition(stand);
        if (stand.trule.thinArea) {
            te.thinCompetitionFromAbove(stand);
        }
    }
}
