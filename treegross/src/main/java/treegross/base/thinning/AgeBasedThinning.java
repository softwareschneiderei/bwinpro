package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import treegross.base.Species;
import treegross.base.Tree;
import static treegross.base.thinning.DynamicThinning.defaultThinningFactor;

public class AgeBasedThinning implements DynamicThinning {

    private final ThinningDefinitions thinningDefinition;
    private final List<ThinningFactorRange> moderateThinningRange;

    public AgeBasedThinning(ThinningDefinitions thinningDefinition) {
        this.thinningDefinition = thinningDefinition;
        moderateThinningRange = new ThinningDefinitionParser().parseDefinition(thinningDefinition.moderateThinning);
    }

    @Override
    public double thinningFactorFor(Tree tree) {
        return firstFactorFoundFor(tree).orElse(defaultThinningFactor);
    }

    private Optional<Double> firstFactorFoundFor(Tree tree) {
        // http://issuetracker.intranet:20002/browse/BWIN-57: verify the condition with domain experts
        return moderateThinningRange.stream()
                .map(range -> range.factorFor(tree.age))
                .filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }
    
    @Override
    public boolean shouldReduce(Species species) {
        // http://issuetracker.intranet:20002/browse/BWIN-57: verify the condition with domain experts
        return species.h100age >= moderateThinningRange.get(0).end;
    }

    @Override
    public String moderateThinningDefinition() {
        return thinningDefinition.moderateThinning;
    }
}
