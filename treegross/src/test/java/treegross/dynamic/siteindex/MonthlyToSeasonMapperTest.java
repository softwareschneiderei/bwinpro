package treegross.dynamic.siteindex;

import java.time.Year;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class MonthlyToSeasonMapperTest {
    private static final Offset<Double> delta = Offset.offset(0.001d);
    
    @Test
    public void meanValuesCalculatedCorrectly() {
        MonthlyToSeasonMapper mapper = new MonthlyToSeasonMapper();
        final SeasonMeanValues seasonValues = mapper.mapMonthlies(Year.of(2018),
                new MonthlyValues(4, 70, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(10, 38, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(15, 67, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(19, 48, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(24, 70, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(17, 165, new AnnualNitrogenDeposition(80d))
        );
        assertThat(seasonValues.meanTemperature).isCloseTo(14.833d, delta);
        assertThat(seasonValues.meanPrecipitationSum).isCloseTo(76.333d, delta);
        assertThat(seasonValues.nitrogenDeposition.value).isCloseTo(80d, delta);
        assertThat(seasonValues.aridityIndex).isCloseTo(36.8862400838d, delta);
    }

    @Test
    public void meanValuesForRCP45inSubregion101() {
        MonthlyToSeasonMapper mapper = new MonthlyToSeasonMapper();
        final SeasonMeanValues seasonValues = mapper.mapMonthlies(Year.of(2006),
                new MonthlyValues(8.41883872502035, 6.87371310468574, new AnnualNitrogenDeposition(8.73394253153122d)),
                new MonthlyValues(12.3991326602039, 18.5097589784251, new AnnualNitrogenDeposition(8.73394253153122d)),
                new MonthlyValues(17.3157113871677, 73.8298043288373, new AnnualNitrogenDeposition(8.73394253153122d)),
                new MonthlyValues(17.6894314540372, 64.9219153525187, new AnnualNitrogenDeposition(8.73394253153122d)),
                new MonthlyValues(21.6521188946207, 45.5976205098033, new AnnualNitrogenDeposition(8.73394253153122d)),
                new MonthlyValues(18.7083562265637, 39.7639998815689, new AnnualNitrogenDeposition(8.73394253153122d))
        );
        assertThat(seasonValues.meanTemperature).isCloseTo(16.0306d, delta);
        assertThat(seasonValues.meanPrecipitationSum).isCloseTo(41.58281d, delta);
        assertThat(seasonValues.nitrogenDeposition.value).isCloseTo(8.73394253153122d, delta);
        assertThat(seasonValues.aridityIndex).isCloseTo(19.1695d, delta);
    }
}
