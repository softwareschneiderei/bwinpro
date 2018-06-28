package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnvironmentVariables {
    /**
     * Average growing season temperature
     */
    public final Map<Year, Double> meanTemperature = new LinkedHashMap<>();
    /**
     * Average growing season precipitation sum
     */
    public double precipitationSum;
    
    public double aridityIndex;
    
    /**
     * Total annula nitrogen deposition
     */
    public double nitrogenDisposition;
    
}
