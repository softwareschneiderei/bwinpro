package treegross.base.rule;

public class ThinningRegime {
    /**
     * min. volume (fm per ha) taken out during thinning in volume per ha
     */
    public final double minVolume;
    /**
     * max. harvest volume (fm per ha) taken out during one treatment (sum over
     * all species) manThinningVolume - already harvested volume= actual
     * thinning volume thus -> condition for thinning is: the full ammount has
     * not been tapped during harvesting
     */
    public final double maxVolume;
    public final boolean croptreesOnly;

    /**
     *
     * @param minVolume Minimum Volume to perform thinning [m³]
     * @param maxVolume Maximum Volume of thinning [m³]
     * @param cropTreesOnly Release only crop trees
     */
    public ThinningRegime(double minVolume, double maxVolume, boolean cropTreesOnly) {
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        this.croptreesOnly = cropTreesOnly;
    }
}
