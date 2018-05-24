package forestsimulator.dbaccess;

import java.util.List;

public class BatchProgress {

    private final List<CalculationRule> rules;
    private final CalculationRule currentRule;
    private int finishedPasses;
    private final int currentStep;
    private final int totalSteps;

    BatchProgress(List<CalculationRule> rules) {
        this(rules, rules.get(0), 0);
    }

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass) {
        this(rules, currentRule, currentPass, 0, 0);
    }

    BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass, int currentStep, int totalSteps) {
        super();
        this.rules = rules;
        this.currentRule = currentRule;
        this.finishedPasses = currentPass;
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
        return finishedPasses;
    }
    
    public int currentStep() {
        return currentStep;
    }
    
    public int totalSteps() {
        return totalSteps;
    }
}
