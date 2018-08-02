package treegross.dynamic.siteindex;

import treegross.base.StandLocation;


public interface EnvironmentalDataProvider {
    
    EnvironmentVariables environmentalDataFor(StandLocation location, String scenario);

}
