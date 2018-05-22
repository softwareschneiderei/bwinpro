package forestsimulator.DBAccess;

public class CalculationRule {

    public final String edvId;
    public final int aufId;
    public final int scenarioId;

    public CalculationRule(String edvId, int aufId, int scenarioId) {
        super();
        this.edvId = edvId;
        this.aufId = aufId;
        this.scenarioId = scenarioId;
    }
}
