package treegross.base.thinning;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class DefinedRangesTest {
    
    @Test
    public void checkingCoverageWithoutRanges() {
        DefinedRanges<Double> ranges = new DefinedRanges<>();
        
        assertThat(ranges.cover(0, 5)).isFalse();
    }
    
    @Test
    public void checkingCoverageWithOneRange() {
        DefinedRanges<Double> ranges = new DefinedRanges<>(new ThinningValueRange(0, 99, 4.5));
        
        assertThat(ranges.cover(0, 99)).isTrue();
        assertThat(ranges.cover(0, 99.1)).isFalse();
    }
    
    @Test
    public void emptyRangesUseFallbackForBestValue() {
        DefinedRanges<Double> ranges = new DefinedRanges<>();
        
        assertThat((double) ranges.bestValueFor(-1, 7.1)).isEqualTo(7.1);
    }
    
    @Test
    public void bestValueBeforeStartIsThatOfFirstRange() {
        DefinedRanges<Double> ranges = new DefinedRanges<>(new ThinningValueRange(0, 10, 0.6), new ThinningValueRange(10, 20, 1.8));
        
        assertThat((double) ranges.bestValueFor(-1, 7.1)).isEqualTo(0.6);
    }
    
    @Test
    public void bestValueAfterEndIsThatOfLastRange() {
        DefinedRanges<Double> ranges = new DefinedRanges<>(new ThinningValueRange(0, 10, 0.6), new ThinningValueRange(10, 20, 1.8));
        
        assertThat((double) ranges.bestValueFor(21, 7.1)).isEqualTo(1.8);
    }
    
    @Test
    public void bestValueForGapUsesValueOfPreviousRange() {
        DefinedRanges<Double> ranges = new DefinedRanges<>(new ThinningValueRange(0, 10, 0.6), new ThinningValueRange(15, 20, 1.8));
        
        assertThat((double) ranges.bestValueFor(12, 7.1)).isEqualTo(0.6);
    }

    @Test
    public void bestValueForRangeIsCorrect() {
        DefinedRanges<Double> ranges = new DefinedRanges<>(new ThinningValueRange(0, 10, 0.6), new ThinningValueRange(15, 20, 1.8));
        
        assertThat((double) ranges.bestValueFor(8, 7.1)).isEqualTo(0.6);
        assertThat((double) ranges.bestValueFor(15, 7.1)).isEqualTo(1.8);
    }
}
