package treegross.base.thinning;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import treegross.base.Tree;

public class HeightBasedThinningTest {
    
    /*
     * #Pinning
    */
    @Test
    public void obtainReducingHeight() {
        assertThat(new HeightBasedThinning("10.0;0.9;22.0").startReducingAHeight()).isEqualTo(22.0d);
        assertThat(new HeightBasedThinning("11.0;0.9;20.0;20.0;0.7;30.0").startReducingAHeight()).isEqualTo(20.0d);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void reducingHeightIsInfinityWithIncompleteDefinition() {
        assertThat(Double.isInfinite(new HeightBasedThinning("10.0;0.9;").startReducingAHeight())).isTrue();
    }
    
    /*
     * #Pinning
    */
    @Test
    public void shouldReduce() {
        HeightBasedThinning thinning = new HeightBasedThinning("10.0;0.9;22.0");
        
        assertThat(thinning.shouldReduce(21.9)).isFalse();
        assertThat(thinning.shouldReduce(22)).isTrue();
        assertThat(thinning.shouldReduce(22.1)).isTrue();
    }
    
    /*
     * #Pinning
    */
    @Test
    public void thinningFactorByHeight() {
        HeightBasedThinning thinning = new HeightBasedThinning("10.0;0.8;22.0;22.0;0.75;28.0;28.0;0.7;100.0");
        Tree t = new Tree();
        t.h = 9.2d;
        assertThat(thinning.thinningFactorFor(t)).isEqualTo(1d);
        t.h = 17.6d;
        assertThat(thinning.thinningFactorFor(t)).isEqualTo(0.8d);
        t.h = 22d;
        assertThat(thinning.thinningFactorFor(t)).isEqualTo(0.75d);
        t.h = 30.6d;
        assertThat(thinning.thinningFactorFor(t)).isEqualTo(0.7d);
        t.h = 100d;
        assertThat(thinning.thinningFactorFor(t)).isEqualTo(1d);
    }
}
