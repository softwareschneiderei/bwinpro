package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicSiteIndex {
    public int year;
    public final double si0;
    /**
     * one site index per year
     */
    // TODO: Introduce SiteIndex type instead of double
    public Map<Year, Double> siIntermediates = new LinkedHashMap<>();

    public DynamicSiteIndex(double initialSiteIndex) {
        super();
        si0 = initialSiteIndex;
    }
    
    public double endSiteIndex() {
        return siIntermediates.entrySet().stream()
                .max((a, b) -> a.getKey().compareTo(b.getKey()))
                .map(entry -> entry.getValue())
                .orElse(si0);
    }
}
