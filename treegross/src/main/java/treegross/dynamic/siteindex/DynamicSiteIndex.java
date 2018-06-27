package treegross.dynamic.siteindex;

import java.util.ArrayList;
import java.util.List;

public class DynamicSiteIndex {
    public int year;
    public double si0;
    /**
     * one site index per year
     */
    public List<Double> siIntermediates = new ArrayList<>();
    public double siEnd;
}
