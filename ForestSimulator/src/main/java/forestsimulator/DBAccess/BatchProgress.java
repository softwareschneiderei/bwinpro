package forestsimulator.DBAccess;

import java.util.List;

public class BatchProgress {

    private final List<CalculationRule> rules;
    private final CalculationRule currentRule;
    private int finishedPasses;

    BatchProgress(List<CalculationRule> rules) {
        this(rules, rules.get(0), 0);
    }

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass) {
        super();
        this.rules = rules;
        this.currentRule = currentRule;
        this.finishedPasses = currentPass;
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
}
