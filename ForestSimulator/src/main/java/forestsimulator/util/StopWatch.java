package forestsimulator.util;

public class StopWatch {

    private final String name;
    private long start;
    private long end;
    
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
        System.out.println(name + ": " + deltaNanos() / 1e6 + " ms passed.");
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
