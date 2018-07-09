package treegross.dynamic.siteindex;


public interface EnvironmentalDataProvider {
    
    EnvironmentVariables environmentalDataFor(String region, String subRegion, String scenario);

}
