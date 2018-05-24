package forestsimulator.dbaccess;

import java.util.List;

public class BatchProgress {

    private final List<CalculationRule> rules;
    private final CalculationRule currentRule;
    private int currentPass;
    private final int currentStep;
    private final int totalSteps;

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass) {
        this(rules, currentRule, currentPass, 0, 0);
    }

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass, int currentStep, int totalSteps) {
        super();
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("Rules list must not be empty.");
        }
        this.rules = rules;
        this.currentRule = currentRule;
        this.currentPass = currentPass;
        this.currentStep = currentStep;
        this.totalSteps = totalSteps;
    }
    
    public int totalRules() {
        return this.rules.size();
    }
    
    public int currentRule() {
        return rules.indexOf(currentRule) + 1;
    }

    public int passesForCurrentRule() {
        return currentRule.passCount;
    }
    
    public int currentPass() {
        return currentPass;
    }
    
    public int currentStep() {
        return currentStep;
    }
    
    public int totalSteps() {
        return totalSteps;
    }
}
