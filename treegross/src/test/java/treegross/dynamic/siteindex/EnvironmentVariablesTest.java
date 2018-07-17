package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class EnvironmentVariablesTest {
    
    @Test
    public void aridityIndexComputedCorrectly() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(new GrowingSeasonValues(Year.of(2017), 20, 80, new AnnualNitrogenDeposition(86))));
        
        assertThat(environment.aridityIndexOf(Year.of(2017))).isCloseTo(32, Offset.offset(0.0001d));
    }
    
    @Test
    public void growingSeasonsAreOrderedByYearAscending() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(
                new GrowingSeasonValues(Year.of(2015), 20, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2017), 20, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2016), 20, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2013), 20, 80, new AnnualNitrogenDeposition(86))
                ));
        
        assertThat(environment).extracting("year").containsExactly(
                Year.of(2013),
                Year.of(2015),
                Year.of(2016),
                Year.of(2017));
    }
    
    @Test
    public void calculate5YearMeans() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(
                new GrowingSeasonValues(Year.of(2012), 17, 75, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2013), 18, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2014), 19, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2015), 21, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2016), 22, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2017), 23, 80, new AnnualNitrogenDeposition(86)),
                new GrowingSeasonValues(Year.of(2018), 24, 80, new AnnualNitrogenDeposition(86))
                ));
        assertThat(environment.calculate5YearMeans()).extracting(value -> value.meanTemperature).usingDefaultElementComparator().
                containsExactly(17d, 17.2d, 17.6d, 18.4d, 19.4d, 20.6d, 21.8d);
        assertThat(environment.calculate5YearMeans()).extracting(value -> value.meanPrecipitationSum).usingDefaultElementComparator().
                containsExactly(75d, 76d, 77d, 78d, 79d, 80d, 80d);
    }
}
