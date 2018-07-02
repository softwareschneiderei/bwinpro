package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import treegross.base.Species;
import treegross.base.Tree;

public class HeightBasedThinning implements ModerateThinning {

    private final String thinningDefinition;
    private final List<ThinningFactorRange> ranges;

    public HeightBasedThinning(String thinningDefinition) {
        this(thinningDefinition, new ThinningDefinitionParser().parseDefinition(thinningDefinition));
    }
    
    public HeightBasedThinning(String thinningDefinition, List<ThinningFactorRange> ranges) {
        super();
        this.thinningDefinition = thinningDefinition;
        this.ranges = ranges;
    }

    @Override
    public double thinningFactorFor(Tree tree) {
        return firstFactorFoundFor(tree).orElse(defaultThinningFactor);
    }

    private Optional<Double> firstFactorFoundFor(Tree tree) {
        return ranges.stream()
                .map(range -> range.factorFor(tree.h))
                .filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }
    
    @Override
    public boolean shouldReduce(Species species) {
        return species.h100 >= startReducingAHeight();
    }

    @Override
    public String definition() {
        return thinningDefinition;
    }
    
    protected double startReducingAHeight() throws NumberFormatException {
        if (ranges.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        return ranges.get(0).end;
    }
}
