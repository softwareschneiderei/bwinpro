package treegross.base.thinning;

import static java.text.MessageFormat.format;
import java.util.Optional;

public class ThinningFactorRange {
    public final double start;
    public final double end;
    public final double factor;

    public ThinningFactorRange(double start, double end, double factor) {
        super();
        if (end <= start) {
            throw new IllegalArgumentException(
                    format("End must be greater than start. Given start={0} end={1}", start, end));
        }
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
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.start) ^ (Double.doubleToLongBits(this.start) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.end) ^ (Double.doubleToLongBits(this.end) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.factor) ^ (Double.doubleToLongBits(this.factor) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThinningFactorRange other = (ThinningFactorRange) obj;
        if (Double.doubleToLongBits(this.start) != Double.doubleToLongBits(other.start)) {
            return false;
        }
        if (Double.doubleToLongBits(this.end) != Double.doubleToLongBits(other.end)) {
            return false;
        }
        return Double.doubleToLongBits(this.factor) == Double.doubleToLongBits(other.factor);
    }

    @Override
    public String toString() {
        return String.valueOf(start) + "/" + factor + "/" + end;
    }
}
