package forestsimulator.dbaccess;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

public class BatchProgress {

    private static final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui"); // NOI18N
    private final Progress stepProgress;
    private final Progress ruleProgress;
    private final Progress passProgress;
    private final CalculationRule currentRule;

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass) {
        this(rules, currentRule, currentPass, new Progress(0, 0));
    }

    public BatchProgress(List<CalculationRule> rules, CalculationRule currentRule, int currentPass, Progress stepProgress) {
        super();
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("Rules list must not be empty.");
        }
        this.currentRule = currentRule;
        this.ruleProgress = new Progress(rules.indexOf(currentRule) + 1, rules.size());
        this.passProgress = new Progress(currentPass, currentRule.passCount);
        this.stepProgress = stepProgress;
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
}
