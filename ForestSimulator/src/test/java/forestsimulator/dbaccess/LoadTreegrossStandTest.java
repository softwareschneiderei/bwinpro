package forestsimulator.dbaccess;

import org.junit.Test;
import treegross.base.Stand;
import static org.assertj.core.api.Assertions.assertThat;
import treegross.base.thinning.ThinningType;

public class LoadTreegrossStandTest {
    
    /*
     * #Pinning
    */
    @Test
    public void treatmentRulesAreAppliedToStand() {
        Stand stand = new Stand();
        
        new LoadTreegrossStand().applyTreatmentRulesTo(stand);
        
        assertThat(stand.trule)
                .extracting("typeOfThinning", "thinArea", "selectCropTrees", "reselectCropTrees",
                        "selectCropTreesOfAllSpecies", "releaseCropTrees", "cutCompetingCropTrees",
                        "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume",
                        "thinAreaSpeciesDependent", "thinningIntensityArea", "minHarvestVolume",
                        "maxHarvestVolume", "typeOfHarvest", "harvestLayerFromBelow", "maxHarvestingPeriode",
                        "lastTreatment", "protectMinorities", "nHabitat", "thinningIntensity")
                .containsExactly(ThinningType.SingleTreeSelection, false, true, true,
                        false, true, false,
                        true, 0d, 80d,
                        true, 0d, 0d,
                        250d, 0, false, 6,
                        0, false, 0d, 1d);
    }
    
}
