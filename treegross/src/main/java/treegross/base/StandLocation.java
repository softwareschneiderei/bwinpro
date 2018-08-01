package treegross.base;

public class StandLocation {

    public final String federalState;
    public final String growingSubRegion;
    public final String growingRegion;

    public StandLocation(String federalState, String growingSubRegion) {
        this(federalState, regionFrom(growingSubRegion), growingSubRegion);
    }
    
    public StandLocation(String federalState, String growingRegion, String growingSubRegion) {
        this.federalState = federalState;
        this.growingRegion = growingRegion;
        this.growingSubRegion = growingSubRegion;
    }
    
    private static String regionFrom(String growthSubregion) {
        return growthSubregion.split("/")[0];
    }
}
