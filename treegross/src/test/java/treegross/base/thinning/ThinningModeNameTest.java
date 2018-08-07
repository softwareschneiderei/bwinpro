package treegross.base.thinning;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ThinningModeNameTest {
    
    @Test
    public void stringToEnum() {
        assertThat(ThinningModeName.fromName("height")).isEqualTo(ThinningModeName.HEIGHT);
        assertThat(ThinningModeName.fromName("age")).isEqualTo(ThinningModeName.AGE);
    }
    
    @Test
    public void stringToEnumCaseInsensitive() {
        assertThat(ThinningModeName.fromName("Height")).isEqualTo(ThinningModeName.HEIGHT);
        assertThat(ThinningModeName.fromName("AGE")).isEqualTo(ThinningModeName.AGE);
        assertThat(ThinningModeName.fromName("aGe")).isEqualTo(ThinningModeName.AGE);
    }
    
    @Test
    public void throwsIllegalArgumentOnUnknownString() {
        Assertions.assertThatThrownBy(() -> ThinningModeName.fromName("Some mode")).isInstanceOf(IllegalArgumentException.class).hasMessage("No thinning mode for name 'Some mode'");
    }
}
