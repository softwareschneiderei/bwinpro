package treegross.dynamic.siteindex;

import java.time.Year;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class MonthlyToSeasonMapperTest {
    private static final Offset<Double> delta = Offset.offset(0.001d);
    
    @Test
    public void meanValuesCalculatedCorrectly() {
        MonthlyToSeasonMapper mapper = new MonthlyToSeasonMapper();
        final GrowingSeasonValues seasonValues = mapper.mapMonthlies(Year.of(2018),
                new MonthlyValues(4, 70, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(10, 38, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(15, 67, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(19, 48, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(24, 70, new AnnualNitrogenDeposition(80d)),
                new MonthlyValues(17, 165, new AnnualNitrogenDeposition(80d))
        );
        Assertions.assertThat(seasonValues.meanTemperature).isCloseTo(14.833d, delta);
        Assertions.assertThat(seasonValues.meanPrecipitationSum).isCloseTo(76.333d, Offset.offset(0.001d));
        Assertions.assertThat(seasonValues.nitrogenDeposition.value).isCloseTo(80d, Offset.offset(0.001d));
    }
}
