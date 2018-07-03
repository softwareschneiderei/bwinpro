package forestsimulator.dbaccess;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import treegross.base.thinning.AgeBasedThinning;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.ThinningDefinitions;

public class ThinningModeTest {
    private static final ThinningDefinitions definition = new ThinningDefinitions("1/0.5/2", "", "");
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-52
     *
     * #Adjusted
    */
    @Test
    public void thinningModeReturnsCorrectModerateThinningForName() {
        assertThat(ThinningMode.forName("age", definition)).isInstanceOf(AgeBasedThinning.class)
                .returns(definition.moderateThinning, thinning -> thinning.moderateThinningDefinition());
        assertThat(ThinningMode.forName("height", definition)).isInstanceOf(HeightBasedThinning.class)
                .returns(definition.moderateThinning, thinning -> thinning.moderateThinningDefinition());
    }
    
    @Test
    public void thinningModeThrowsExceptionForUnknownMode() {
        Assertions.assertThatThrownBy(() -> ThinningMode.forName("undefined", definition))
                .hasMessage("No moderate thinning found for name undefined");
    }
}
