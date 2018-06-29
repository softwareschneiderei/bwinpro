package treegross.dynamic.siteindex;

import java.time.Month;
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnvironmentVariables {
    /**
     * Average growing season temperature
     */
    // TODO: change double to MeanTemperature type
    public final Map<Year, Double> meanTemperature = new LinkedHashMap<>();
    /**
     * Average growing season precipitation sum
     */
    // TODO: change double to Precipitation type
    public final Map<Year, Double> precipitationSum = new LinkedHashMap<>();
    
    /**
     * Total annula nitrogen deposition
     */
    public Map<Year, AnnualNitrogenDeposition> nitrogenDisposition = new LinkedHashMap<>();

    
    public double aridityIndexFor(Year year) {
        return Month.values().length * precipitationSum.get(year) / (meanTemperature.get(year) + 10);
    }
}
