package treegross.base.thinning;

import java.util.Optional;

public class ThinningFactorRange {
    public final double start;
    public final double end;
    public final double factor;

    public ThinningFactorRange(double start, double end, double factor) {
        this.start = start;
        this.end = end;
        this.factor = factor;
    }

    Optional<Double> factorFor(double value) {
        if (value >= start && value < end) {
            return Optional.of(factor);
        }
        return Optional.empty();
    }
}
