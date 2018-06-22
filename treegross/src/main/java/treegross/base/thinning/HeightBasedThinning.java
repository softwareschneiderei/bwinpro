package treegross.base.thinning;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import treegross.base.Tree;

public class HeightBasedThinning implements ModerateThinning {

    private final String thinningDefinition;
    private final List<ThinningFactorRange> ranges;

    public HeightBasedThinning(String thinningDefinition) {
        super();
        this.thinningDefinition = thinningDefinition;
        ranges = parseDefinition();
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
    
    private List<ThinningFactorRange> parseDefinition() throws NumberFormatException {
        List<ThinningFactorRange> result = new ArrayList<>();
        if (thinningDefinition.length() > 4) {
            String[] tokens = thinningDefinition.split(";");
            // added by jhansen
            int end = tokens.length / 3;

            for (int i = 0; i < end; i++) {
                ThinningFactorRange range = new ThinningFactorRange(
                        Double.parseDouble(tokens[i * 3]),
                        Double.parseDouble(tokens[i * 3 + 2]),
                        Double.parseDouble(tokens[i * 3 + 1]));
                result.add(range);
            }
        }
        return result;
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
