package treegross.dynamic.siteindex;

import java.util.HashMap;
import java.util.Map;

public class Regression {
    public double rxy;
    private final Map<String, ModelParameters> parameters = new HashMap<>();
    
    public void addParametersFor(String speciesCode, ModelParameters parameters) {
        this.parameters.put(speciesCode, parameters);
    }
    
    public ModelParameters parametersForSpecies(String speciesCode) {
        return parameters.getOrDefault(speciesCode, new ModelParameters());
    }
}
