package treegross.base.thinning;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DefinedRanges<V> {
    private final List<ThinningValueRange<V>> ranges;

    public DefinedRanges(ThinningValueRange<V>... thinningValueRange) {
        this(Arrays.asList(thinningValueRange));
    }
    
    public DefinedRanges(List<ThinningValueRange<V>> valueRanges) {
        ranges = valueRanges;
    }
    
    public boolean empty() {
        return ranges.isEmpty();
    }

    public Optional<V> firstValueFoundFor(double value) {
        return ranges.stream()
                .map(range -> range.factorFor(value))
                .filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }
    
    public boolean cover(double min, double max) {
        Optional<Double> start = ranges.stream().map(range -> range.start).min(Double::compare);
        Optional<Double> end = ranges.stream().map(range -> range.end).max(Double::compare);
        if (!start.isPresent() || !end.isPresent()) {
            return false;
        }
        return start.get() <= min && end.get() >= max;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.ranges);
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
        final DefinedRanges other = (DefinedRanges) obj;
        if (!Objects.equals(this.ranges, other.ranges)) {
            return false;
        }
        return true;
    }

}
