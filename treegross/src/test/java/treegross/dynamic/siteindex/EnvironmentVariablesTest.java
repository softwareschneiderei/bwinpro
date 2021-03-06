package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class EnvironmentVariablesTest {
    
    @Test
    public void growingSeasonsAreOrderedByYearAscending() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(
                new SeasonMeanValues(Year.of(2015), 20, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2017), 20, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2016), 20, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2013), 20, 80, 18, new AnnualNitrogenDeposition(86))
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
                new SeasonMeanValues(Year.of(2012), 17, 75, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2013), 18, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2014), 19, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2015), 21, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2016), 22, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2017), 23, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2018), 24, 80, 18, new AnnualNitrogenDeposition(86))
                ));
        assertThat(environment.calculate5YearMeans()).extracting(value -> value.meanTemperature).usingDefaultElementComparator().
                containsExactly(17d, 17.2d, 17.6d, 18.4d, 19.4d, 20.6d, 21.8d);
        assertThat(environment.calculate5YearMeans()).extracting(value -> value.meanPrecipitationSum).usingDefaultElementComparator().
                containsExactly(75d, 76d, 77d, 78d, 79d, 80d, 80d);
    }
    
    /**
     *  #Requirement http://issuetracker.intranet:20002/browse/BWIN-74
     */
    @Test
    public void reportsNoMissingDataForCompletePeriod() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(
                new SeasonMeanValues(Year.of(2013), 18, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2014), 19, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2015), 21, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2016), 22, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2017), 23, 80, 18, new AnnualNitrogenDeposition(86))
                ));
        assertThat(environment.dataMissingFor(Year.of(2013), Year.of(2017))).isFalse();
    }
    
    /**
     *  #Requirement http://issuetracker.intranet:20002/browse/BWIN-74
     */
    @Test
    public void reportsMissingDataForEndOfPeriod() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(
                new SeasonMeanValues(Year.of(2013), 18, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2014), 19, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2015), 21, 80, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2016), 22, 80, 18, new AnnualNitrogenDeposition(86))
                ));
        assertThat(environment.dataMissingFor(Year.of(2013), Year.of(2017))).isTrue();
    }
    
    /**
     *  #Requirement http://issuetracker.intranet:20002/browse/BWIN-74
     */
    @Test
    public void reportsMissingDataInBetween() {
        EnvironmentVariables environment = new EnvironmentVariables();
        environment.addGrowingSeasons(Arrays.asList(
                new SeasonMeanValues(Year.of(2012), 17, 75, 18, new AnnualNitrogenDeposition(86)),
                new SeasonMeanValues(Year.of(2014), 19, 80, 18, new AnnualNitrogenDeposition(86))
                ));
        assertThat(environment.dataMissingFor(Year.of(2012), Year.of(2014))).isTrue();
    }
}
