package treegross.dynamic.siteindex;

import java.util.HashMap;
import java.util.Map;

public class DynamicSiteIndexModel {
    private final Map<String, DynamicSiteIndexModelParameters> parameters = new HashMap<>();

    public DynamicSiteIndexModel() {
        addParametersFor("Fi", new DynamicSiteIndexModelParameters(
                1.00003571186121,
                0.99998833695419,
                -9.02561907643062E-03,
                4.76764480882665E-04,
                -2.57562425678908E-03,
                1.00564825578297E-03,
                1.37988034418277E-02
        ));
    }
    
    public final void addParametersFor(String speciesCode, DynamicSiteIndexModelParameters parameters) {
        this.parameters.put(speciesCode, parameters);
    }
    
    public DynamicSiteIndexModelParameters parametersForSpecies(String speciesCode) {
        return parameters.getOrDefault(speciesCode, new DynamicSiteIndexModelParameters());
    }
}
