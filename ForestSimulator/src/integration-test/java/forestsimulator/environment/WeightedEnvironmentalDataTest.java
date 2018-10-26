/*
 */
package forestsimulator.environment;

import forestsimulator.dbaccess.DatabaseEnvironmentalDataProvider;
import java.io.File;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Test;
import treegross.base.StandLocation;
import treegross.dynamic.siteindex.EnvironmentVariables;

public class WeightedEnvironmentalDataTest {
    private static final Offset<Double> delta = Offset.offset(0.001);
    
    private static final Map<Year, double[]> weightedValues = new HashMap<>();
    
    static {
        weightedValues.put(Year.of(2006), new double[]{16.0306d, 41.58281d, 19.1695d, 8.733943d});
        weightedValues.put(Year.of(2007), new double[]{15.75144d, 48.91582d, 22.8142d, 8.324791});
        weightedValues.put(Year.of(2008), new double[]{15.04856d, 69.50973d, 33.61316d, 7.634554d});
        weightedValues.put(Year.of(2009), new double[]{14.52344d, 64.41814d, 31.65047d, 7.753417d});
        weightedValues.put(Year.of(2010), new double[]{13.92058d, 63.35328d, 31.88154d, 8.176085d});
        weightedValues.put(Year.of(2011), new double[]{14.84033d, 52.57334d, 26.05773d, 6.253073d});
        weightedValues.put(Year.of(2012), new double[]{14.89855d, 52.05309d, 25.58508d, 6.687488d});
        weightedValues.put(Year.of(2013), new double[]{14.88123d, 58.10925d, 28.41905d, 7.709597d});
        weightedValues.put(Year.of(2014), new double[]{14.90028d, 62.79574d, 30.53833d, 6.916132d});
    }
    
    @Test
    public void weigthedMeanValuesForRCP45inSubregion101() {
        DatabaseEnvironmentalDataProvider dataProvider = new DatabaseEnvironmentalDataProvider(new File("data_standsimulation/climate_data.mdb"));
        EnvironmentVariables weightedMeans = dataProvider.environmentalDataFor(new StandLocation("1/01"), "RCP45").calculateWeighted5YearMeans();
        weightedValues.entrySet().forEach((yearToCheck) -> {
            Year year = yearToCheck.getKey();
            double[] expectedValues = yearToCheck.getValue();
            
            assertThat(weightedMeans.growingSeasonMeanTemperatureOf(year)).isCloseTo(expectedValues[0], delta);
            assertThat(weightedMeans.growingSeasonPrecipitationSumOf(year)).isCloseTo(expectedValues[1], delta);
            assertThat(weightedMeans.aridityIndexOf(year)).isCloseTo(expectedValues[2], delta);
            assertThat(weightedMeans.nitrogenDepositionOf(year).value).isCloseTo(expectedValues[3], delta);
        });
    }
}
