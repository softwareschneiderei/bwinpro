package treegross.base.thinning;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ThinningDefinitionParserTest {
    
    /*
     * #Pinning
    */
    @Test
    public void thinningFactorByHeight() {
        ThinningDefinitionParser thinning = new ThinningDefinitionParser();
        assertThat(thinning.parseDefinition(("10.0;0.8;22.0;22.0;0.75;28.0;28.0;0.7;100.0"))).containsExactly(
                new ThinningFactorRange(10d, 22d, 0.8d),
                new ThinningFactorRange(22d, 28d, 0.75d),
                new ThinningFactorRange(28d, 100d, 0.7d)
        );
    }
}
