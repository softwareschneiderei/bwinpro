package treegross.base.rule;

import treegross.base.thinning.ThinningType;

public class ThinningRegime {
    public final ThinningType type;
    public final double intensity;
    public final double minVolume;
    public final double maxVolume;
    public final boolean croptreesOnly;

    /**
     *
     * @param type 0= single tree selection, 1=thinning from above, 2= thinning from below
     * @param intensity Factor to normal stand density 1.0 = normal stand density, no thinning set intensity = 0.0
     * @param minVolume Minimum Volume to perform thinning [m³]
     * @param maxVolume Maximum Volume of thinning [m³]
     * @param cropTreesOnly Release only crop trees
     */
    public ThinningRegime(ThinningType type, double intensity, double minVolume, double maxVolume, boolean cropTreesOnly) {
        this.type = type;
        this.intensity = intensity;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        this.croptreesOnly = cropTreesOnly;
    }
}
