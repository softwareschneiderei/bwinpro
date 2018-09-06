package treegross.base.thinning;

import static java.text.MessageFormat.format;
import java.util.Objects;
import java.util.Optional;


public class ThinningValueRange<V> {

    final double start;
    final double end;
    private final V value;
    
    public ThinningValueRange(double start, double end, V value) {
        super();
        if (end <= start) {
            throw new IllegalArgumentException(
                    format("End must be greater than start. Given start={0} end={1}", start, end));
        }
        this.start = start;
        this.end = end;
        this.value = value;
    }
    
    public final Optional<V> factorFor(double value) {
        if (value >= start && value < end) {
            return Optional.of(getValue());
        }
        return Optional.empty();
    }
    
    protected V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.start) ^ (Double.doubleToLongBits(this.start) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.end) ^ (Double.doubleToLongBits(this.end) >>> 32));
        hash = 97 * hash + Objects.hashCode(this.value);
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
        final ThinningValueRange<?> other = (ThinningValueRange<?>) obj;
        if (Double.doubleToLongBits(this.start) != Double.doubleToLongBits(other.start)) {
            return false;
        }
        if (Double.doubleToLongBits(this.end) != Double.doubleToLongBits(other.end)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.valueOf(start) + "/" + getValue() + "/" + end;
    }
}
