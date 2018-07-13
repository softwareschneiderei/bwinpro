package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class EnvironmentVariablesTest {
    
    @Test
    public void aridityIndexComputedCorrectly() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(new GrowingSeasonValues(Year.of(2017), 20, 80, new AnnualNitrogenDeposition(86))));
        
        Assertions.assertThat(environment.aridityIndexOf(Year.of(2017))).isCloseTo(32, Offset.offset(0.0001d));
    }
}
