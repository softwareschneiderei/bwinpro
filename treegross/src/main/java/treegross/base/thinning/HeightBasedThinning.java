package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import treegross.base.Species;
import treegross.base.Tree;

public class HeightBasedThinning implements DynamicThinning {

    private final ThinningDefinitions thinningDefinition;
    private final List<ThinningFactorRange> moderateThinningRanges;
    private final List<ThinningFactorRange> intensityRanges;

    public HeightBasedThinning(ThinningDefinitions thinningDefinition) {
        this(thinningDefinition, new ThinningDefinitionParser().parseDefinition(thinningDefinition.moderateThinning));
    }
    
    public HeightBasedThinning(ThinningDefinitions thinningDefinition, List<ThinningFactorRange> moderateThinningRanges) {
        super();
        this.thinningDefinition = thinningDefinition;
        this.moderateThinningRanges = moderateThinningRanges;
        this.intensityRanges = new ThinningDefinitionParser().parseDefinition(thinningDefinition.thinningIntensity);
    }

    @Override
    public double thinningFactorFor(Tree tree) {
        return firstFactorFoundFor(tree, moderateThinningRanges).orElse(defaultThinningFactor);
    }

    private Optional<Double> firstFactorFoundFor(Tree tree, List<ThinningFactorRange> ranges) {
        return ranges.stream()
                .map(range -> range.factorFor(tree.h))
                .filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }
    
    @Override
    public boolean shouldReduce(Species species) {
        return species.h100 >= startReducingAHeight();
    }

    @Override
    public String moderateThinningDefinition() {
        return thinningDefinition.moderateThinning;
    }
    
    protected double startReducingAHeight() throws NumberFormatException {
        if (moderateThinningRanges.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        return moderateThinningRanges.get(0).end;
    }

    @Override
    public double intensityFor(Tree tree) {
        return firstFactorFoundFor(tree, moderateThinningRanges).orElse(defaultIntensity);
    }
}
