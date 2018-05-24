package forestsimulator.dbaccess;

public class CalculationRule {

    public final String edvId;
    public final int aufId;
    public final int scenarioId;
    public final int passCount;

    public CalculationRule(String edvId, int aufId, int scenarioId, int passCount) {
        super();
        this.edvId = edvId;
        this.aufId = aufId;
        this.scenarioId = scenarioId;
        this.passCount = passCount;
    }
}
