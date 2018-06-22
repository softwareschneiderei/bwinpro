package treegross.base.rule;

public class SkidTrailRules {
    public final boolean active;
    /**
     * Distance from the center to the next center of the
     * skid trail [m] Default 20m
     */
    public final double distance;
    /**
     * Width of the skid trail [m]. Default 4m
     * All trees on the skid trail will be removed.
     */
    public final double width;

    public SkidTrailRules() {
        this(false);
    }
    
    public SkidTrailRules(boolean active) {
        this(false, 20d, 4d);
    }
    
    public SkidTrailRules(boolean active, double distance, double width) {
        super();
        this.active = active;
        this.distance = distance;
        this.width = width;
    }
}
