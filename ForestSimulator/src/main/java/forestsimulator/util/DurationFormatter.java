package forestsimulator.util;

import java.time.Duration;
import java.util.Optional;

public class DurationFormatter {

    public static String format(Optional<Duration> d) {
        if (!d.isPresent()) {
            return "--:--:--";
        }
        Duration duration = d.get();
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, duration.getSeconds() % 60);
    }
}
