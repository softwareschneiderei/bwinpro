package treegross.base.thinning;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Test;

public class ThinningFactorRangeTest {
    
    @Test
    public void ensureEndIsGreaterThanStart() {
        assertThatThrownBy(() -> new ThinningFactorRange(20, 18, 0.7))
                .hasMessage("End must be greater than start. Given start=20 end=18");
    }
}
