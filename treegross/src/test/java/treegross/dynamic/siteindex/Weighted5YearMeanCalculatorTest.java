package treegross.dynamic.siteindex;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class Weighted5YearMeanCalculatorTest {
    private static final Offset<Double> delta = Offset.offset(0.001);
    
    @Test
    public void emptyAccumulatorYieldsZero() {
        final Weighted5YearMeanCalculator<Double> dataAccumulation = new Weighted5YearMeanCalculator<>();

        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(0d, delta);
    }
   
    @Test
    public void accumulationYields5YearMeans() {
        final Weighted5YearMeanCalculator<Double> dataAccumulation = new Weighted5YearMeanCalculator<>();
        
        dataAccumulation.add(1d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(1d, delta);
        dataAccumulation.add(2d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(1.666d, delta);
        dataAccumulation.add(3d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(2.333d, delta);
        dataAccumulation.add(4d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(3d, delta);
        dataAccumulation.add(5d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(3.666d, delta);
        dataAccumulation.add(6d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(4.666d, delta);
        dataAccumulation.add(7d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(5.666d, delta);
        dataAccumulation.add(8d);
        Assertions.assertThat(dataAccumulation.meanOf(Weighted5YearMeanCalculatorTest::getValue)).isCloseTo(6.666d, delta);
    }
    
    private static double getValue(Double d) {
        return d;
    }
}
