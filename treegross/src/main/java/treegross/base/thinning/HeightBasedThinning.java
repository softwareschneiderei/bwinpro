package treegross.base.thinning;

import treegross.base.Tree;

public class HeightBasedThinning implements ModerateThinning {

    private final String thinningDefinition;
    private final DefinedRanges<Double> ranges;

    public HeightBasedThinning(String thinningDefinition) {
        this(thinningDefinition, ThinningDefinitionParser.thinningFactorParser.parseDefinition(thinningDefinition));
    }
    
    public HeightBasedThinning(String thinningDefinition, DefinedRanges<Double> ranges) {
        super();
        this.thinningDefinition = thinningDefinition;
        this.ranges = ranges;
    }

    /**
     * The height of norm tree is used to select the thinning factor. It should be set
     * to h100 of its species.
     * 
     * @param tree norm tree for a species
     * @return thinning factor based on the age of the norm tree
     */
    @Override
    public double thinningFactorFor(Tree tree) {
        return ranges.firstValueFoundFor(tree.h).orElse(defaultThinningFactor);
    }

    @Override
    public String definition() {
        return thinningDefinition;
    }
}
