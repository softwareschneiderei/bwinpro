package treegross.base.thinning;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ThinningDefinitionParserTest {
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-61
    */
    @Test
    public void parseThinningRanges() {
        ThinningDefinitionParser thinning = new ThinningDefinitionParser();

        assertThat(thinning.parseDefinition(("10.0/0.8/22.0;22.0/0.75/28.0;28.0/0.7/100.0"))).containsExactly(
                new ThinningFactorRange(10d, 22d, 0.8d),
                new ThinningFactorRange(22d, 28d, 0.75d),
                new ThinningFactorRange(28d, 100d, 0.7d)
        );
    }
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-61
    */
    @Test
    public void incompleteThinningRangeThrows() {
        ThinningDefinitionParser thinning = new ThinningDefinitionParser();
        
        Assertions.assertThatThrownBy(() -> thinning.parseDefinition("11.0/0.8"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Illegal thinning factor triple. We need exactly 3 decimal numbers separated by /. Got: 11.0/0.8");
    }
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-61
    */
    @Test
    public void emptyDefinitionYieldsEmptyRanges() {
        ThinningDefinitionParser thinning = new ThinningDefinitionParser();
        
        assertThat(thinning.parseDefinition("")).isEmpty();
    }
}
