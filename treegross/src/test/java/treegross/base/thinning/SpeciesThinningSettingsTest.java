package treegross.base.thinning;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import treegross.base.Tree;
import static treegross.base.thinning.SpeciesThinningSettings.heightBasedScenarioSetting;

public class SpeciesThinningSettingsTest {
    
    @Test
    public void correctThinningTypeForHeight() {
        SpeciesThinningSettings settings = heightBasedScenarioSetting("10.0/sts/22.0;22.0/tfb/28.0;28.0/tfa/100.0", 0.8);
        assertThat(settings.typeFor(treeWithHeight(15d))).isEqualTo(ThinningType.SingleTreeSelection);
        assertThat(settings.typeFor(treeWithHeight(22d))).isEqualTo(ThinningType.ThinningFromBelow);
        assertThat(settings.typeFor(treeWithHeight(99.9))).isEqualTo(ThinningType.ThinningFromAbove);
        assertThat(settings.typeFor(treeWithHeight(100d))).isEqualTo(ThinningType.SingleTreeSelection);
    }

    private Tree treeWithHeight(double treeHeight) {
        Tree result = new Tree();
        result.h = treeHeight;
        return result;
    }
}
