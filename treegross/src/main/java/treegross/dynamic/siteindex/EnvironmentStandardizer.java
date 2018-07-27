package treegross.dynamic.siteindex;

public class EnvironmentStandardizer {

    private EnvironmentStandardizer() {
        super();
    }
    
    public static EnvironmentVariables standardize(EnvironmentVariables variables) {
        EnvironmentVariables mean5YearVariables = variables.calculate5YearMeans();
        LongtermEnvironmentVariables longTermVariables = new LongtermEnvironmentVariables(mean5YearVariables);
        
        return longTermVariables.standardized(variables.calculateWeighted5YearMeans());
    }
}
