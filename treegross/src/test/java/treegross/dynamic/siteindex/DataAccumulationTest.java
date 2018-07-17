package treegross.dynamic.siteindex;

import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class DataAccumulationTest {
    
    @Test
    public void accumulationYields5YearMeans() {
        List<Double> yearlyValues = Arrays.asList(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d);
        
        Assertions.assertThat(new DataAccumulation().weighted5YearMean(yearlyValues, yearlyValues.size())).containsExactly(new double[]{
                    1d,
                    1.666d,
                    2.333d,
                    3d,
                    3.666d,
                    4.666d,
                    5.666d,
                    6.666d
                },
                Offset.offset(0.001));
    }
}
