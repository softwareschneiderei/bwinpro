package forestsimulator.dbaccess.tools;

import java.util.HashMap;
import java.util.Map;
import treegross.base.TGFunction;
import treegross.base.TGTextFunction;

public class DynamicSiteIndexModel {
    private final Map<String, TGFunction> parameters = new HashMap<>();

    public DynamicSiteIndexModel() {
        addParametersFor("Fi", new TGTextFunction(
                "1.00003571186121 * prevSI ^ 0.99998833695419"
                        + " * exp(-9.02561907643062E-03 * env.tMean"
                        + " + 4.76764480882665E-04 * env.PSum"
                        + " + -2.57562425678908E-03 * env.AI"
                        + " + 1.00564825578297E-03 * env.NOTotal"
                        + " + 1.37988034418277E-02 * env.PSum"
                        + " * env.NOTotal)"));
    }
    
    public final void addParametersFor(String speciesCode, TGFunction function) {
        this.parameters.put(speciesCode, function);
    }
    
    public TGFunction parametersForSpecies(String speciesCode) {
        return parameters.getOrDefault(speciesCode, new TGTextFunction("prevSI"));
    }
}
