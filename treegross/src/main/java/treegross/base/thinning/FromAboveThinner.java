package treegross.base.thinning;

import treegross.base.Stand;
import treegross.treatment.TreatmentElements2;

/**
 * thin area between crop trees
 *
 */
public class FromAboveThinner implements Thinner {

    // selectCropTreesOfAllSpecies auch hier einbeziehen?
    @Override
    public void thin(TreatmentElements2 te, Stand stand) {
        //System.out.println("temporäre Zwischenfelder durchforsten");
        //select temp crop trees (wet species)
        stand.resetTempCropTrees();
        te.selectTempCropTreesTargetPercentage(stand);
        // Start thinning for all species
        te.thinTempCropTreeCompetition(stand);
        if (stand.trule.thinArea) {
            te.thinCompetitionFromAbove(stand);
        }
    }
}
