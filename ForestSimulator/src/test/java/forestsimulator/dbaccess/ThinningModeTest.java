package forestsimulator.dbaccess;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import treegross.base.thinning.AgeBasedThinning;
import treegross.base.thinning.HeightBasedThinning;
import treegross.base.thinning.ThinningModeName;

public class ThinningModeTest {
    private static final String definition = "1/0.5/2";
    
    /*
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-52
    */
    @Test
    public void thinningModeReturnsCorrectModerateThinningForName() {
        assertThat(ModerateThinningMode.forName(ThinningModeName.AGE, definition)).isInstanceOf(AgeBasedThinning.class)
                .returns(definition, thinning -> thinning.definition());
        assertThat(ModerateThinningMode.forName(ThinningModeName.HEIGHT, definition)).isInstanceOf(HeightBasedThinning.class)
                .returns(definition, thinning -> thinning.definition());
    }
    
    @Test
    public void thinningModeThrowsExceptionForUnknownMode() {
        Assertions.assertThatThrownBy(() -> ModerateThinningMode.forName(null, definition))
                .hasMessage("No moderate thinning found for name null");
    }
}
