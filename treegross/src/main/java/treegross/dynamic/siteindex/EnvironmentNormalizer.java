package treegross.dynamic.siteindex;

public class EnvironmentNormalizer {

    private EnvironmentNormalizer() {
        super();
    }
    
    public static EnvironmentVariables normalize(EnvironmentVariables variables) {
        LongtermEnvironmentVariables longtermVariables = new LongtermEnvironmentVariables(variables);
        
        return standardized(longtermVariables, variables.calculateWeighted5YearMeans());
    }
    
    private static EnvironmentVariables standardized(LongtermEnvironmentVariables longtermVariables, EnvironmentVariables environment) {
        EnvironmentVariables result = new EnvironmentVariables();
        for (SeasonMeanValues yearlyValues : environment) {
            result.addGrowingSeason(new SeasonMeanValues(
                    yearlyValues.year,
                    standardizeValues(yearlyValues.meanTemperature, longtermVariables.growingSeasonMeanTemperature()),
                    standardizeValues(yearlyValues.meanPrecipitationSum, longtermVariables.growingSeasonPrecipitationSum()),
                    yearlyValues.aridityIndex,
                    new AnnualNitrogenDeposition(standardizeValues(yearlyValues.nitrogenDeposition.value, longtermVariables.nitrogenDeposition().value))));
        }
        return result;
    }

    private static double standardizeValues(double yearlyMean, double longtermMean) {
        return (yearlyMean - longtermMean) / longtermMean;
    }
}
