package treegross.base;

public class StandLocation {
    private static final String defaultFederalState = "BW";

    public final String federalState;
    public final String growingRegion;
    public final String growingSubRegion;

    public StandLocation(String growingRegion, String growingSubRegion) {
        this(defaultFederalState, growingRegion, growingSubRegion);
    }
    
    public StandLocation(String growingSubRegion) {
        this(defaultFederalState, regionFrom(growingSubRegion), growingSubRegion);
    }
    
    public StandLocation(String federalState, String growingRegion, String growingSubRegion) {
        this.federalState = federalState;
        this.growingRegion = growingRegion;
        this.growingSubRegion = growingSubRegion;
    }
    
    public static String regionFrom(String growingSubregion) {
        if (growingSubregion.contains("/")) {
            return growingSubregion.split("/")[0];
        }
        return growingSubregion;
    }
}
