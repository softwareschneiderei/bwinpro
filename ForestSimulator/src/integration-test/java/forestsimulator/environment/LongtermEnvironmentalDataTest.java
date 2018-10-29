package forestsimulator.environment;

import forestsimulator.dbaccess.DatabaseEnvironmentalDataProvider;
import java.io.File;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Test;
import treegross.base.StandLocation;
import treegross.dynamic.siteindex.LongtermEnvironmentVariables;

public class LongtermEnvironmentalDataTest {
    private static final Offset<Double> delta = Offset.offset(0.001);
    
    @Test
    public void longtermEnvironmentalDataForRCP45inSubregion101() {
        DatabaseEnvironmentalDataProvider dataProvider = new DatabaseEnvironmentalDataProvider(new File("data_standsimulation/climate_data.mdb"));
        LongtermEnvironmentVariables longtermVariables = new LongtermEnvironmentVariables(dataProvider.environmentalDataFor(new StandLocation("1/01"), "RCP45"));
        assertThat(longtermVariables.growingSeasonMeanTemperature()).isCloseTo(13.43468d, delta);
        assertThat(longtermVariables.growingSeasonPrecipitationSum()).isCloseTo(54.59394d, delta);
        assertThat(longtermVariables.aridityIndex()).isCloseTo(27.34185d, delta);
        assertThat(longtermVariables.nitrogenDeposition().value).isCloseTo(5.269042d, delta);
    }
}
