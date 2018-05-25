package forestsimulator.dbaccess;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BinaryOperator;

public class BatchProgress {

    private static final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui"); // NOI18N
    private static final BinaryOperator<Integer> sumInts = (a, b) -> a + b;
    
    private final List<CalculationRule> rules;
    private final CalculationRule currentRule;
    private final long currentPass;
    private final Progress stepProgress;
    private final Progress ruleProgress;
    private final Progress passProgress;
    private final Duration elapsed;

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass, Duration elapsed) {
        this(rules, currentRule, currentPass, new Progress(0, 0), elapsed);
    }

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass, Progress stepProgress, Duration elapsed) {
        super();
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("Rules list must not be empty.");
        }
        this.rules = rules;
        this.currentRule = currentRule;
        this.currentPass = currentPass;
        this.stepProgress = stepProgress;
        this.elapsed = elapsed;
        this.ruleProgress = new Progress(rules.indexOf(currentRule) + 1, rules.size());
        this.passProgress = new Progress(currentPass, currentRule.passCount);
    }
    
    public String currentRuleText() {
        return MessageFormat.format(currentRuleFormatString(), currentRule.edvId, currentRule.aufId, currentRule.scenarioId);
    }

    protected String currentRuleFormatString() {
        return messages.getString("BatchProgress.currentRule.text");
    }
    
    public Progress ruleProgress() {
        return ruleProgress;
    }
    
    public Progress passProgress() {
        return passProgress;
    }
    
    public Progress stepProgress() {
        return stepProgress;
    }
    
    public Duration elapsedNanos() {
        return elapsed;
    }
    
    public Optional<Duration> estimatedRemainingNanos() {
        long totalPasses = rules.stream().map(rule -> rule.passCount).reduce(0, sumInts);
        long finishedPasses = rules.stream().limit(rules.indexOf(currentRule)).map(rule -> rule.passCount).reduce(0, sumInts) + finishedPassesOfCurrentRule();
        return nanosPerPass(finishedPasses).map(duration -> duration.multipliedBy(totalPasses - finishedPasses));
    }

    private Optional<Duration> nanosPerPass(long finishedPasses) {
        if (finishedPasses == 0) {
            return Optional.empty();
        }
        return Optional.of(elapsed.dividedBy(finishedPasses));
    }
    
    private long finishedPassesOfCurrentRule() {
        return currentPass - 1L;
    }
}
