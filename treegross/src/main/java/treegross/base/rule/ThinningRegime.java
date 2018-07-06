package treegross.base.rule;

import treegross.base.thinning.ScenarioThinningSettings;

public class ThinningRegime {
    public final ScenarioThinningSettings settings;
    public final double minVolume;
    public final double maxVolume;
    public final boolean croptreesOnly;

    /**
     *
     * @param type 0= single tree selection, 1=thinning from above, 2= thinning from below
     * @param minVolume Minimum Volume to perform thinning [m³]
     * @param maxVolume Maximum Volume of thinning [m³]
     * @param cropTreesOnly Release only crop trees
     */
    public ThinningRegime(ScenarioThinningSettings type, double minVolume, double maxVolume, boolean cropTreesOnly) {
        this.settings = type;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        this.croptreesOnly = cropTreesOnly;
    }
}
