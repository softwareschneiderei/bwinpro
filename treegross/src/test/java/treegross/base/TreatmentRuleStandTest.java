package treegross.base;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import treegross.base.rule.ThinningRegime;

public class TreatmentRuleStandTest {
    /*
     * #Pinning
    */
    @Test
    public void applyingThinningRegimeForSingleTreeSelection() {
        TreatmentRuleStand rules = new TreatmentRuleStand();
        
        rules.setThinningRegime(new ThinningRegime(0, 120, true));
        
        assertThat(rules).extracting("thinAreaSpeciesDependent", "minThinningVolume", "maxThinningVolume", "cropTreesOnly")
                .containsExactly(true, 0d, 120d, true);
    }
}
