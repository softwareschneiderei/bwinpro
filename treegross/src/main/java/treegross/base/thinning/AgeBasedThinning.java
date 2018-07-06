package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import treegross.base.Species;
import treegross.base.Tree;
import static treegross.base.thinning.ModerateThinning.defaultThinningFactor;

public class AgeBasedThinning implements ModerateThinning {

    private final String thinningDefinition;
    private final List<ThinningFactorRange> ranges;

    public AgeBasedThinning(String thinningDefinition) {
        this.thinningDefinition = thinningDefinition;
        ranges = new ThinningDefinitionParser().parseDefinition(thinningDefinition);
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
        return firstFactorFoundFor(tree).orElse(defaultThinningFactor);
    }

    private Optional<Double> firstFactorFoundFor(Tree tree) {
        return ranges.stream()
                .map(range -> range.factorFor(tree.age))
                .filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }
    
    @Override
    public boolean shouldReduce(Species species) {
        // http://issuetracker.intranet:20002/browse/BWIN-57: verify the condition with domain experts
        return species.h100age >= ranges.get(0).end;
    }

    @Override
    public String definition() {
        return thinningDefinition;
    }
}
