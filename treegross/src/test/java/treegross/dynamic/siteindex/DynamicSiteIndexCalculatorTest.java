package treegross.dynamic.siteindex;

import java.time.Year;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.nfunk.jep.ParseException;
import treegross.base.SiteIndex;
import static treegross.base.SiteIndex.si;
import treegross.base.TGTextFunction;

public class DynamicSiteIndexCalculatorTest {
    private static final Offset<Double> delta = Offset.offset(1e-5d);
    private static final TGTextFunction spruceFunction = new TGTextFunction("1.00003571186121 * prevSI ^ 0.99998833695419 * exp(-9.02561907643062E-03 * env.tMean + 4.76764480882665E-04 * env.PSum + -2.57562425678908E-03 * env.AI + 1.00564825578297E-03 * env.NOTotal + 1.37988034418277E-02 * env.PSum * env.NOTotal)");
    
    @Test
    public void calculateDSIforSpruce() throws ParseException {
        DynamicSiteIndexCalculator calculator = new DynamicSiteIndexCalculator(spruceFunction);
        
        assertThat(calculator.computeSiteIndex(Year.of(2018), si(39.80825), testEnvironment()).value)
                .isCloseTo(39.78825479, delta);
    }
    
    /**
     *  #Requirement http://issuetracker.intranet:20002/browse/BWIN-74
     */
    @Test
    public void computeConstantDSIWhenClimateDataMissing() {
        DynamicSiteIndexCalculator calculator = new DynamicSiteIndexCalculator(spruceFunction);
        final SiteIndex previousSiteIndex = si(40.762d);
        
        assertThat(calculator.computeSiteIndex(Year.of(2201), previousSiteIndex, testEnvironment())).isEqualTo(previousSiteIndex);
    }

    private static EnvironmentVariables testEnvironment() {
        EnvironmentVariables result = new EnvironmentVariables();
        // Add standardized environmental values 
        result.addGrowingSeason(new SeasonMeanValues(Year.of(2018), 0.075, 0.063, new AnnualNitrogenDeposition(0.184)));
        return result;
    }
}
