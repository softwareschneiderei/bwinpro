package forestsimulator.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StopWatch {

    private static final Logger logger = Logger.getLogger(StopWatch.class.getSimpleName());
    private final String name;
    private long start;
    private long end;
    
    static {
        logger.setLevel(Level.FINE);
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.FINE);
        logger.addHandler(h);
    }
    
    public StopWatch(String name) {
        super();
        this.name = name;
    }

    public StopWatch start() {
        this.start = System.nanoTime();
        return this;
    }
    
    public void stop() {
        this.end = System.nanoTime();
    }
    
    public StopWatch printElapsedTime() {
        stop();
        logger.log(Level.FINE, "{0}: {1} ms passed.", new Object[]{name, deltaNanos() / 1e6});
        return this;
    }
    
    public long deltaNanos() {
        final long delta = this.end - this.start;
        if (delta < 0) {
            throw new IllegalStateException("Stop must be called before computing delta.");
        }
        return delta;
    }
}
