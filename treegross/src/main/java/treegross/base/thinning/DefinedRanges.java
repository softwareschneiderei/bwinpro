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

    public V bestValueFor(double value, V fallBack) {
        if (empty()) {
            return fallBack;
        }
        return firstValueFoundFor(value).orElseGet(() -> {
            if (value >= max().get()) {
                return ranges.get(ranges.size() - 1).getValue();
            }
            ThinningValueRange<V> bestRange = ranges.get(0);
            for (ThinningValueRange<V> range : ranges) {
                if (value < range.end && value > range.start) {
                    bestRange = range;
                } 
            }
            return bestRange.getValue();
        });
    }

    public boolean cover(double min, double max) {
        Optional<Double> start = min();
        Optional<Double> end = max();
        if (!start.isPresent() || !end.isPresent()) {
            return false;
        }
        return start.get() <= min && end.get() >= max;
    }

    private Optional<Double> max() {
        return ranges.stream().map(range -> range.end).max(Double::compare);
    }

    private Optional<Double> min() {
        return ranges.stream().map(range -> range.start).min(Double::compare);
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
