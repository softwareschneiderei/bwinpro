package treegross.base;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.ThinningFactorRange;
import treegross.base.thinning.DynamicThinning;
import treegross.base.thinning.ThinningDefinitions;

public class TreeTest {
    
    /*
     * #Pinning
     *
     * #Adjusted
    */
    @Test
    public void thinningFactorByHeight() {
        Tree t = treeWithThinning(10.0, 0.8, 22.0, 22.0, 0.75, 28.0, 28.0, 0.7, 100.0);
        t.h = 9.2d;
        assertThat(t.getModerateThinningFactor()).isEqualTo(1d);
        t.h = 17.6d;
        assertThat(t.getModerateThinningFactor()).isEqualTo(0.8d);
        t.h = 22d;
        assertThat(t.getModerateThinningFactor()).isEqualTo(0.75d);
        t.h = 30.6d;
        assertThat(t.getModerateThinningFactor()).isEqualTo(0.7d);
        t.h = 100d;
        assertThat(t.getModerateThinningFactor()).isEqualTo(1d);
    }

    private static Tree treeWithThinning(final double... thinningDefinition) {
        return new Tree() {
            @Override
            protected DynamicThinning dynamicThinning() {
                return new HeightBasedThinning(new ThinningDefinitions("", "", ""), rangesFor(thinningDefinition));
            }
        };
    }
    
    private static List<ThinningFactorRange> rangesFor(double... factors) {
        List<ThinningFactorRange> ranges = new ArrayList<>();
        for (int i = 0; i < factors.length - 2; i += 3) {
            ranges.add(new ThinningFactorRange(factors[i], factors[ i + 2], factors[i + 1]));
        }
        return ranges;
    }

}
