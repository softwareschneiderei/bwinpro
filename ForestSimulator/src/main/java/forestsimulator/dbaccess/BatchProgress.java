package forestsimulator.dbaccess;

import java.util.List;

public class BatchProgress {

    private final Progress stepProgress;
    private final Progress ruleProgress;
    private final Progress passProgress;

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass) {
        this(rules, currentRule, currentPass, new Progress(0, 0));
    }

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass, Progress stepProgress) {
        super();
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("Rules list must not be empty.");
        }
        this.ruleProgress = new Progress(rules.indexOf(currentRule) + 1, rules.size());
        this.passProgress = new Progress(currentPass, currentRule.passCount);
        this.stepProgress = stepProgress;
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
}
