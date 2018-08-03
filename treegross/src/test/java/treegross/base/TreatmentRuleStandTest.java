package treegross.base;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import treegross.base.rule.ThinningRegime;
import treegross.base.thinning.SpeciesThinningSettings;
import static treegross.base.thinning.SpeciesThinningSettings.ageBasedScenarioSetting;
import treegross.base.thinning.ThinningType;

public class TreatmentRuleStandTest {
    private static final String UNUSED = "";

    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForSingleTreeSelection() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        final SpeciesThinningSettings settings = ageBasedScenarioSetting(ThinningType.SingleTreeSelection, UNUSED);
        
        rules.setThinningRegime(new ThinningRegime(0, 120, true));
        
        assertThat(rules).extracting("thinningSettings", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume")
                .containsExactly(settings, true, false,
                        true, true, true, true,
                        true, 0d, 120d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForSingleTreeSelectionNotOnlyCropTrees() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        final SpeciesThinningSettings settings = ageBasedScenarioSetting(ThinningType.SingleTreeSelection, UNUSED);
        
        rules.setThinningRegime(new ThinningRegime(0, 120, false));
        
        assertThat(rules).extracting("thinningSettings", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume")
                .containsExactly(settings, true, true,
                        true, true, true, true,
                        true, 0d, 120d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForFromAbove() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        final SpeciesThinningSettings settings = ageBasedScenarioSetting(ThinningType.ThinningFromAbove, 1.2d);
        
        rules.setThinningRegime(new ThinningRegime(0, 60, true));
        
        assertThat(rules).extracting("thinningSettings", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume")
                .containsExactly(settings, true, true,
                        false, false, false, false,
                        false, 0d, 60d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForFromBelow() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        final SpeciesThinningSettings settings = ageBasedScenarioSetting(ThinningType.ThinningFromBelow, UNUSED);
        
        rules.setThinningRegime(new ThinningRegime(0, 60, true));
        
        assertThat(rules).extracting("thinningSettings", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume")
                .containsExactly(settings, true, true,
                        false, false, false, false,
                        false, 0d, 60d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForQD() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        final SpeciesThinningSettings settings = ageBasedScenarioSetting(ThinningType.ThinningQD, UNUSED);
        
        rules.setThinningRegime(new ThinningRegime(0.5, 58.9, true));
        
        assertThat(rules).extracting("thinningSettings", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume")
                .containsExactly(settings, true, false,
                        true, true, true, true,
                        true, 0.5d, 58.9d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForQDNotOnlyCropTrees() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        final SpeciesThinningSettings settings = ageBasedScenarioSetting(ThinningType.ThinningQD, UNUSED);
        
        rules.setThinningRegime(new ThinningRegime(0.5, 58.9, false));
        
        assertThat(rules).extracting("thinningSettings", "thinAreaSpeciesDependent", "thinArea",
                "selectCropTrees", "reselectCropTrees", "releaseCropTrees", "cutCompetingCropTrees",
                "releaseCropTreesSpeciesDependent", "minThinningVolume", "maxThinningVolume")
                .containsExactly(settings, true, false,
                        true, true, true, true,
                        true, 0.5d, 58.9d);
    }
}
