package treegross.base.thinning;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import treegross.base.Species;
import treegross.base.Tree;

public class HeightBasedThinningTest {
    
    private static final String UNUSED = "";

    /*
     * #Pinning
     *
     * #Adjusted
    */
    @Test
    public void obtainReducingHeight() {
        assertThat(new HeightBasedThinning(UNUSED, rangesFor(10d, 0.9d, 22d)).startReducingAHeight()).isEqualTo(22.0d);
        assertThat(new HeightBasedThinning(UNUSED, rangesFor(11.0, 0.9, 20.0, 20.0, 0.7, 30.0)).startReducingAHeight()).isEqualTo(20.0d);
    }
    
    /*
     * #Pinning
     *
     * #Adjusted
    */
    @Test
    public void reducingHeightIsInfinityWithIncompleteDefinition() {
        assertThat(Double.isInfinite(new HeightBasedThinning(UNUSED, rangesFor(10.0,0.9)).startReducingAHeight())).isTrue();
    }
    
    /*
     * #Pinning
     *
     * #Adjusted
    */
    @Test
    public void shouldReduce() {
        HeightBasedThinning thinning = new HeightBasedThinning(UNUSED, rangesFor(10.0,0.9,22.0));
        Species sp = new Species();
        sp.h100 = 21.9;
        
        assertThat(thinning.shouldReduce(sp)).isFalse();
        
        sp.h100 = 22;
        assertThat(thinning.shouldReduce(sp)).isTrue();
        
        sp.h100 = 22.1;
        assertThat(thinning.shouldReduce(sp)).isTrue();
    }
    
    /*
     * #Pinning
     *
     * #Adjusted
    */
    @Test
    public void thinningFactorByHeight() {
        HeightBasedThinning thinning = new HeightBasedThinning(
                UNUSED, rangesFor(10.0, 0.8, 22.0, 22.0, 0.75, 28.0, 28.0, 0.7, 100.0));
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

    private static List<ThinningFactorRange> rangesFor(double... factors) {
        List<ThinningFactorRange> ranges = new ArrayList<>();
        for (int i = 0; i < factors.length - 2; i += 3) {
            ranges.add(new ThinningFactorRange(factors[i], factors[ i + 2], factors[i + 1]));
        }
        return ranges;
    }
}
