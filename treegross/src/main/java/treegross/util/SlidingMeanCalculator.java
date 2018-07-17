package treegross.util;

import java.util.LinkedList;
import java.util.function.Function;

public class SlidingMeanCalculator<E> extends LinkedList<E> {
    private final int windowSize;

    public SlidingMeanCalculator(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > windowSize) { super.remove(); }
        return true;
    }
    
    public double meanOf(Function<E, Double> extractor) {
        return stream().map(extractor).reduce(0d, (sum, value) -> sum + value) / size();
    }
    
    public int windowSize() {
        return windowSize;
    }
}
