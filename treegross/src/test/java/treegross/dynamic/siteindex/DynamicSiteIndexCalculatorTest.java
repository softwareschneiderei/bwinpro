package treegross.dynamic.siteindex;

import java.time.Year;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.nfunk.jep.ParseException;
import treegross.base.TGTextFunction;

public class DynamicSiteIndexCalculatorTest {
    private static final Offset<Double> delta = Offset.offset(1e-5d);
    
    @Test
    public void calculateDSIforSpruce() throws ParseException {
        DynamicSiteIndexCalculator calculator = new DynamicSiteIndexCalculator(new TGTextFunction("1.00003571186121 * prevSI ^ 0.99998833695419 * exp(-9.02561907643062E-03 * env.tMean + 4.76764480882665E-04 * env.PSum + -2.57562425678908E-03 * env.AI + 1.00564825578297E-03 * env.NOTotal + 1.37988034418277E-02 * env.PSum * env.NOTotal)"));
        
        Assertions.assertThat(calculator.computeSiteIndex(Year.of(2018), 39.80825d, testEnvironment()))
                .isCloseTo(39.78825479, delta);
    }

    private static EnvironmentVariables testEnvironment() {
        EnvironmentVariables result = new EnvironmentVariables();
        // Add standardized environmental values 
        result.addGrowingSeason(new GrowingSeasonValues(Year.of(2018), 0.075, 0.063, new AnnualNitrogenDeposition(0.184)));
        return result;
    }
}