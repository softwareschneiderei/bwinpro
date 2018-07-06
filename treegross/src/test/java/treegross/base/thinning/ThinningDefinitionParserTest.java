package treegross.base.thinning;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ThinningDefinitionParserTest {
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-61
     *
     * #Adjusted
    */
    @Test
    public void parseThinningRanges() {
        ThinningDefinitionParser thinning = ThinningDefinitionParser.thinningFactorParser;

        assertThat(thinning.parseDefinition(("10.0/0.8/22.0;22.0/0.75/28.0;28.0/0.7/100.0"))).containsExactly(
                new ThinningValueRange(10d, 22d, 0.8d),
                new ThinningValueRange(22d, 28d, 0.75d),
                new ThinningValueRange(28d, 100d, 0.7d)
        );
    }
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-61
     *
     * #Adjusted
    */
    @Test
    public void incompleteThinningRangeThrows() {
        ThinningDefinitionParser thinning = ThinningDefinitionParser.thinningFactorParser;
        
        Assertions.assertThatThrownBy(() -> thinning.parseDefinition("11.0/0.8"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Illegal thinning factor triple. We need exactly 3 decimal numbers separated by /. Got: 11.0/0.8");
    }
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-61
     *
     * #Adjusted
    */
    @Test
    public void emptyDefinitionYieldsEmptyRanges() {
        ThinningDefinitionParser thinning = ThinningDefinitionParser.thinningTypeParser;
        
        assertThat(thinning.parseDefinition("")).isEmpty();
    }
}
