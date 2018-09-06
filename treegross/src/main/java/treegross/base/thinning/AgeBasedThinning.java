package treegross.base.thinning;

import treegross.base.Tree;
import static treegross.base.thinning.ModerateThinning.defaultThinningFactor;

public class AgeBasedThinning implements ModerateThinning {

    private final String thinningDefinition;
    private final DefinedRanges<Double> ranges;

    public AgeBasedThinning(String thinningDefinition) {
        this.thinningDefinition = thinningDefinition;
        ranges = ThinningDefinitionParser.thinningFactorParser.parseDefinition(thinningDefinition);
    }

    /**
     * The age of norm tree is used to select the thinning factor. It should be set
     * to h100age of its species.
     * 
     * @param tree norm tree for a species
     * @return thinning factor based on the age of the norm tree
     */
    @Override
    public double thinningFactorFor(Tree tree) {
        return ranges.firstValueFoundFor(tree.age).orElse(defaultThinningFactor);
    }

    @Override
    public String definition() {
        return thinningDefinition;
    }
}
