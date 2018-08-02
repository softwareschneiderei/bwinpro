package treegross.util;

import java.util.function.Function;


public interface MeanCalculator<E> {
    
    void add(E value);
    
    double meanOf(Function<E, Double> mapper);
    
    int windowSize();

    
    default void fillCalculatorWindow(E value) {
        for (int i = 0; i < windowSize(); i++) {
            add(value);
        }
    }
}
