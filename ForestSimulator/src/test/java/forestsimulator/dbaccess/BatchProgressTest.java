package forestsimulator.dbaccess;

import java.util.Arrays;
import java.util.Collections;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

public class BatchProgressTest {
    
    public BatchProgressTest() {
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void progressNeedsAtLeastOneRule() {
        new BatchProgress(Collections.emptyList(), new CalculationRule("id", 0, 0, 0), 0);
        fail("IllegalArgumentException not thrown with empty rule list.");
    }
    
    @Test
    public void currentRuleStartsWithOne() {
        CalculationRule rule = new CalculationRule("id", 0, 0, 0);
        BatchProgress progress = new BatchProgress(Arrays.asList(rule), rule, 0);
        assertThat(progress.currentRule(), is(1));
    }
    
    @Test
    public void progressProvidesCorrectValuesGiven() {
        CalculationRule rule1 = new CalculationRule("id1", 0, 0, 5);
        CalculationRule rule2 = new CalculationRule("id2", 0, 0, 10);
        BatchProgress progress = new BatchProgress(Arrays.asList(rule1, rule2), rule2, 3);
        assertThat(progress.totalRules(), is(2));
        assertThat(progress.currentRule(), is(2));
        assertThat(progress.currentPass(), is(3));
        assertThat(progress.passesForCurrentRule(), is(rule2.passCount));
        assertThat(progress.currentStep(), is(0));
        assertThat(progress.totalSteps(), is(0));
    }
    
}
