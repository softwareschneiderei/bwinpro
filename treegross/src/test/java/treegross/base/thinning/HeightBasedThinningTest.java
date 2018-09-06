package treegross.base.thinning;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import treegross.base.Tree;

public class HeightBasedThinningTest {
    
    private static final String UNUSED = "";

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

    private static DefinedRanges<Double> rangesFor(double... factors) {
        List<ThinningValueRange<Double>> ranges = new ArrayList<>();
        for (int i = 0; i < factors.length - 2; i += 3) {
            ranges.add(new ThinningValueRange(factors[i], factors[ i + 2], factors[i + 1]));
        }
        return new DefinedRanges(ranges);
    }
}
