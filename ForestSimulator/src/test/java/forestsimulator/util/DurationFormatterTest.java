package forestsimulator.util;

import java.time.Duration;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatterTest {
    
    @Test
    public void noDurationGiven() {
        assertThat(DurationFormatter.format(Optional.empty()), is("--:--:--"));
    }
    
    @Test
    public void durationFormattedCorrectly() {
        assertThat(DurationFormatter.format(Optional.of(Duration.ofSeconds(5234))), is("01:27:14"));
    }
}
