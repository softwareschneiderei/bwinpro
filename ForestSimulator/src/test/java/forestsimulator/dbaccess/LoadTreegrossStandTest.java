package forestsimulator.dbaccess;

import org.junit.Test;
import treegross.base.Stand;
import static org.assertj.core.api.Assertions.assertThat;

public class LoadTreegrossStandTest {
    
    /*
     * #Pinning
     *
     * #Adjusted
    */
    @Test
    public void treatmentRulesAreAppliedToStand() {
        Stand stand = new Stand();
        
        new LoadTreegrossStand().applyTreatmentRulesTo(stand);
        
        assertThat(stand.trule)
                .extracting("thinArea", "selectCropTrees", "reselectCropTrees",
                        "selectCropTreesOfAllSpecies", "releaseCropTrees", "cutCompetingCropTrees",
                        "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume",
                        "thinAreaSpeciesDependent", "thinningIntensityArea", "minHarvestVolume",
                        "maxHarvestVolume", "typeOfHarvest", "harvestLayerFromBelow", "maxHarvestingPeriode",
                        "lastTreatment", "protectMinorities", "nHabitat")
                .containsExactly(false, true, true,
                        false, true, false,
                        true, 0d, 80d,
                        true, 0d, 0d,
                        250d, 0, false, 6,
                        0, false, 0d);
    }
    
}
