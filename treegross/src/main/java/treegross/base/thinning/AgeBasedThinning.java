package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import treegross.base.Species;
import treegross.base.Tree;
import static treegross.base.thinning.DynamicThinning.defaultThinningFactor;

public class AgeBasedThinning implements DynamicThinning {

    private final ThinningDefinitions thinningDefinition;
    private final List<ThinningFactorRange> moderateThinningRange;
    private final List<ThinningFactorRange> intensityRange;

    public AgeBasedThinning(ThinningDefinitions thinningDefinition) {
        this.thinningDefinition = thinningDefinition;
        moderateThinningRange = new ThinningDefinitionParser().parseDefinition(thinningDefinition.moderateThinning);
        intensityRange = new ThinningDefinitionParser().parseDefinition(thinningDefinition.thinningIntensity);
    }

    @Override
    public double thinningFactorFor(Tree tree) {
        return firstFactorFoundFor(tree, moderateThinningRange).orElse(defaultThinningFactor);
    }

    private Optional<Double> firstFactorFoundFor(Tree tree, List<ThinningFactorRange> factorRange) {
        // http://issuetracker.intranet:20002/browse/BWIN-57: verify the condition with domain experts
        return factorRange.stream()
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

    @Override
    public double intensityFor(Tree tree) {
        return firstFactorFoundFor(tree, intensityRange).orElse(defaultIntensity);
    }
}
