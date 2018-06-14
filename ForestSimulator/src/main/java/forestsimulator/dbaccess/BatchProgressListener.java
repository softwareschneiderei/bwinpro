package forestsimulator.dbaccess;

import java.time.Duration;

public interface BatchProgressListener {
    
    public void updateProgress(BatchProgress progress);
    
    public void aborted();
    
    public void finished(Duration deltaNanos);
    
}
