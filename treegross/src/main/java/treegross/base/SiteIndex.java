package treegross.base;

/**
 *  Wraps the site index value for type safety
 */
public class SiteIndex {

    public final double value;

    private SiteIndex(double value) {
        this.value = value;
    }
    
    public static SiteIndex si(double value) {
        return new SiteIndex(value);
    }
}
