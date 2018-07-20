package treegross.dynamic.siteindex;

import java.time.Year;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class DynamicSiteIndexCalculatorTest {
    private static final Offset<Double> delta = Offset.offset(1e-5d);
    
    @Test
    public void testSomeMethod() {
        DynamicSiteIndexCalculator calculator = new DynamicSiteIndexCalculator(spruceModel());
        
        Assertions.assertThat(calculator.computeSiteIndex(Year.of(2018), 39.80825d, testEnvironment()))
                .isCloseTo(39.78825479, delta);
    }

    private static DynamicSiteIndexModelParameters spruceModel() {
        return new DynamicSiteIndexModelParameters(
                1.00003571186121,
                0.99998833695419,
                -9.02561907643062E-03,
                4.76764480882665E-04,
                -2.57562425678908E-03,
                1.00564825578297E-03,
                1.37988034418277E-02
        );
    }

    private static EnvironmentVariables testEnvironment() {
        EnvironmentVariables result = new EnvironmentVariables();
        // Add standardized environmental values 
        result.addGrowingSeason(new GrowingSeasonValues(Year.of(2018), 0.075, 0.063, new AnnualNitrogenDeposition(0.184)));
        return result;
    }
}
