package treegross.base;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import treegross.base.rule.ThinningRegime;
import treegross.base.thinning.ThinningType;

public class TreatmentRuleStandTest {

    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForSingleTreeSelection() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        
        rules.setThinningRegime(new ThinningRegime(ThinningType.SingleTreeSelection, 1.0, 0, 120, true));
        
        assertThat(rules).extracting("typeOfThinning", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume", "thinningIntensity")
                .containsExactly(ThinningType.SingleTreeSelection, true, false,
                        true, true, true, true,
                        true, 0d, 120d, 1d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForSingleTreeSelectionNotOnlyCropTrees() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        
        rules.setThinningRegime(new ThinningRegime(ThinningType.SingleTreeSelection, 1.0, 0, 120, false));
        
        assertThat(rules).extracting("typeOfThinning", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume", "thinningIntensity")
                .containsExactly(ThinningType.SingleTreeSelection, true, true,
                        true, true, true, true,
                        true, 0d, 120d, 1d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForFromAbove() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        
        rules.setThinningRegime(new ThinningRegime(ThinningType.ThinningFromAbove, 1.0, 0, 60, true));
        
        assertThat(rules).extracting("typeOfThinning", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume", "thinningIntensity")
                .containsExactly(ThinningType.ThinningFromAbove, true, true,
                        false, false, false, false,
                        false, 0d, 60d, 1d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForFromBelow() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        
        rules.setThinningRegime(new ThinningRegime(ThinningType.ThinningFromBelow, 1.0, 0, 60, true));
        
        assertThat(rules).extracting("typeOfThinning", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume", "thinningIntensity")
                .containsExactly(ThinningType.ThinningFromBelow, true, true,
                        false, false, false, false,
                        false, 0d, 60d, 1d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForQD() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        
        rules.setThinningRegime(new ThinningRegime(ThinningType.ThinningQD, 1.0, 0.5, 58.9, true));
        
        assertThat(rules).extracting("typeOfThinning", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume", "thinningIntensity")
                .containsExactly(ThinningType.ThinningQD, true, false,
                        true, true, true, true,
                        true, 0.5d, 58.9d, 1d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForQDNotOnlyCropTrees() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        
        rules.setThinningRegime(new ThinningRegime(ThinningType.ThinningQD, 1.0, 0.5, 58.9, false));
        
        assertThat(rules).extracting("typeOfThinning", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume", "thinningIntensity")
                .containsExactly(ThinningType.ThinningQD, true, false,
                        true, true, true, true,
                        true, 0.5d, 58.9d, 1d);
    }
}
