package forestsimulator.environment;

import forestsimulator.dbaccess.DatabaseEnvironmentalDataProvider;
import java.io.File;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Ignore;
import org.junit.Test;
import treegross.base.StandLocation;
import treegross.dynamic.siteindex.EnvironmentNormalizer;
import treegross.dynamic.siteindex.EnvironmentVariables;

public class NormalizedEnvironmentalDataTest {
    private static final Offset<Double> delta = Offset.offset(0.001);
    
    private static final Map<Year, double[]> normalizedValues = new HashMap<>();
    
    static {
        normalizedValues.put(Year.of(2006), new double[]{0.1932257d, -0.2383256d, -0.298895d, 0.6575962d});
        normalizedValues.put(Year.of(2007), new double[]{0.1724466d, -0.1040063d, -0.1655939d, 0.5799441d});
        normalizedValues.put(Year.of(2008), new double[]{0.1201287d, 0.2732133d, 0.229367d, 0.4489455d});
        normalizedValues.put(Year.of(2009), new double[]{0.08104107d, 0.1799504d, 0.1575836d, 0.4715042d});
        normalizedValues.put(Year.of(2010), new double[]{0.03616802d, 0.1604453d, 0.1660348d, 0.5517214d});
        normalizedValues.put(Year.of(2011), new double[]{0.1046285d, -0.03701136d, -0.04696525d, 0.1867573d});
        normalizedValues.put(Year.of(2012), new double[]{0.1089625d, -0.04654077d, -0.06425196d, 0.2692039d});
        normalizedValues.put(Year.of(2013), new double[]{0.1076734d, 0.06439016d, 0.03939776d, 0.4631877d});
        normalizedValues.put(Year.of(2014), new double[]{0.1090916d, 0.1502329d, 0.1169081d, 0.3125977d});
    }
    
    @Test
    public void normalizedEnvironmentalDataForRCP45inSubregion101() {
        DatabaseEnvironmentalDataProvider dataProvider = new DatabaseEnvironmentalDataProvider(new File("data_standsimulation/climate_data.mdb"));
        EnvironmentVariables normalized = EnvironmentNormalizer.normalize(dataProvider.environmentalDataFor(new StandLocation("1/01"), "RCP45"));
        normalizedValues.entrySet().forEach((yearToCheck) -> {
            Year year = yearToCheck.getKey();
            double[] expectedValues = yearToCheck.getValue();
            
            assertThat(normalized.growingSeasonMeanTemperatureOf(year)).isCloseTo(expectedValues[0], delta);
            assertThat(normalized.growingSeasonPrecipitationSumOf(year)).isCloseTo(expectedValues[1], delta);
            assertThat(normalized.aridityIndexOf(year)).isCloseTo(expectedValues[2], delta);
            assertThat(normalized.nitrogenDepositionOf(year).value).isCloseTo(expectedValues[3], delta);
        });
    }
}
