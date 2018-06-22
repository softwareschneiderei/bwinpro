package treegross.base.thinning;

import java.util.List;
import java.util.Optional;
import treegross.base.Tree;

public class HeightBasedThinning implements ModerateThinning {

    private final String thinningDefinition;
    private final List<ThinningFactorRange> ranges;

    public HeightBasedThinning(String thinningDefinition) {
        super();
        this.thinningDefinition = thinningDefinition;
        ranges = new ThinningDefinitionParser().parseDefinition(thinningDefinition);
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
    public boolean shouldReduce(double h100) {
        return h100 >= startReducingAHeight();
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
