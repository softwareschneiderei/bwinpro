package treegross.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class SlidingMeanCalculatorTest {
    private static final Offset<Double> delta = Offset.offset(0.001d);

    /**
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-44
     */
    @Test
    public void meanWithEmptyWindowGivesNaN() {
        SlidingMeanCalculator<Double> smc = new SlidingMeanCalculator<>(10);
        
        assertThat(smc.meanOf(value -> value)).isNaN();
    }
    
    /**
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-44
     */
    @Test
    public void meanOfPartiallyFilledWindowIsCorrect() {
        SlidingMeanCalculator<Double> smc = new SlidingMeanCalculator<>(10);
        smc.add(1d);
        smc.add(2d);
        
        assertThat(smc.meanOf(value -> value)).isEqualTo(1.5d);
    }
    
    /**
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-44
     */
    @Test
    public void meanOfFilledWindowIsCorrect() {
        SlidingMeanCalculator<Double> smc = new SlidingMeanCalculator<>(3);
        smc.add(1d);
        smc.add(2d);
        smc.add(2d);
        
        assertThat(smc.meanOf(value -> value)).isEqualTo(1.6666d, delta);
    }
    
    /**
     * #Requirement http://issuetracker.intranet:20002/browse/BWIN-44
     */
    @Test
    public void meanSlides() {
        SlidingMeanCalculator<Double> smc = new SlidingMeanCalculator<>(3);
        smc.add(4d);
        smc.add(2d);
        smc.add(3d);
        
        assertThat(smc.meanOf(value -> value)).isEqualTo(3d, delta);
        
        smc.add(1d);
        
        assertThat(smc.meanOf(value -> value)).isEqualTo(2d, delta);
    }
}
