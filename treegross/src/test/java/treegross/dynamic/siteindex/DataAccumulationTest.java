package treegross.dynamic.siteindex;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class DataAccumulationTest {
    private static final Offset<Double> delta = Offset.offset(0.001);
    
    @Test
    public void emptyAccumulatorYieldsZero() {
        final DataAccumulation dataAccumulation = new DataAccumulation();

        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(0d, delta);
    }
   
    @Test
    public void accumulationYields5YearMeans() {
        final DataAccumulation dataAccumulation = new DataAccumulation();
        
        dataAccumulation.add(1d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(1d, delta);
        dataAccumulation.add(2d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(1.666d, delta);
        dataAccumulation.add(3d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(2.333d, delta);
        dataAccumulation.add(4d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(3d, delta);
        dataAccumulation.add(5d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(3.666d, delta);
        dataAccumulation.add(6d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(4.666d, delta);
        dataAccumulation.add(7d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(5.666d, delta);
        dataAccumulation.add(8d);
        Assertions.assertThat(dataAccumulation.weighted5YearMean()).isCloseTo(6.666d, delta);
    }
}
