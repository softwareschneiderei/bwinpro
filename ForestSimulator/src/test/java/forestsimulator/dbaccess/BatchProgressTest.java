package forestsimulator.dbaccess;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

public class BatchProgressTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void progressNeedsAtLeastOneRule() {
        new BatchProgress(Collections.emptyList(), new CalculationRule("id", 0, 0, 0), 0, Duration.ZERO);
        fail("IllegalArgumentException not thrown with empty rule list.");
    }
    
    @Test
    public void currentRuleStartsWithOne() {
        CalculationRule rule = new CalculationRule("id", 0, 0, 0);
        BatchProgress progress = new BatchProgress(Arrays.asList(rule), rule, 0, Duration.ZERO);
        assertThat(progress.ruleProgress().current, is(1));
    }
    
    @Test
    public void progressProvidesCorrectValuesGiven() {
        CalculationRule rule1 = new CalculationRule("id1", 1, 109, 5);
        CalculationRule rule2 = new CalculationRule("id2", 0, 209, 10);
        BatchProgress progress = new BatchProgress(Arrays.asList(rule1, rule2), rule2, 3, Duration.ofMillis(998));
        assertThat(progress.ruleProgress().total, is(2));
        assertThat(progress.ruleProgress().current, is(2));
        assertThat(progress.passProgress().current, is(3));
        assertThat(progress.passProgress().total, is(10));
        assertThat(progress.stepProgress().current, is(0));
        assertThat(progress.stepProgress().total, is(0));
        assertThat(progress.elapsedNanos(), is(Duration.ofMillis(998)));
    }
    
    @Test
    public void currentRuleTextUsesGivenRule() {
        CalculationRule rule = new CalculationRule("Dgl_MDf_", 2, 109, 30);
        BatchProgress progress = new BatchProgress(Arrays.asList(rule), rule, 0, Duration.ZERO) {
            @Override
            protected String currentRuleFormatString() {
                return "{0} Aufnahme: {1} Szenario: {2}";
            }
        };
        assertThat(progress.currentRuleText(), is("Dgl_MDf_ Aufnahme: 2 Szenario: 109"));
    }
    
    @Test
    public void computeEstimatedRemainingDuration() {
        CalculationRule rule1 = new CalculationRule("id1", 1, 109, 5);
        CalculationRule rule2 = new CalculationRule("id2", 0, 209, 10);
        CalculationRule rule3 = new CalculationRule("id2", 0, 209, 30);
        final int finishedPasses = 10;
        final int remainingPasses = 35;
        final Duration timePerPass = Duration.ofMillis(2500);
        BatchProgress progress = new BatchProgress(Arrays.asList(rule1, rule2, rule3), rule2, 6, new Progress(1, 10), timePerPass.multipliedBy(finishedPasses));
        assertThat(progress.estimatedRemainingNanos().get(), is(timePerPass.multipliedBy(remainingPasses)));
    }
}
