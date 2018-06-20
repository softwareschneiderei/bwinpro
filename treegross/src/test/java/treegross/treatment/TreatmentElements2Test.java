package treegross.treatment;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class TreatmentElements2Test {
    
    /*
     * #Pinning
    */
    @Test
    public void obtainReducingHeight() {
        assertThat(new TreatmentElements2().startReducingAHeight("10.0;0.9;22.0")).isEqualTo(22.0d);
        assertThat(new TreatmentElements2().startReducingAHeight("11.0;0.9;20.0;20.0;0.7;30.0")).isEqualTo(20.0d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void reducingHeightIsInfinityWithIncompleteDefinition() {
        assertThat(Double.isInfinite(new TreatmentElements2().startReducingAHeight("10.0;0.9;"))).isTrue();
    }
}
