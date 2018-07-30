package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Map;
import treegross.base.SiteIndex;

public class DynamicSiteIndexProgression {
    public int year;
    public final SiteIndex si0;
    /**
     * one site index per year
     */
    public Map<Year, SiteIndex> siIntermediates = new LinkedHashMap<>();

    public DynamicSiteIndexProgression(SiteIndex initialSiteIndex) {
        super();
        si0 = initialSiteIndex;
    }
    
    public SiteIndex endSiteIndex() {
        return siIntermediates.entrySet().stream()
                .max((a, b) -> a.getKey().compareTo(b.getKey()))
                .map(entry -> entry.getValue())
                .orElse(si0);
    }
}
