package forestsimulator.dbaccess;

import java.time.Duration;

public interface BatchProgressListener {
    
    public void updateProgress(BatchProgress progress);
    
    public void aborted(Duration deltaNanos);
    
    public void finished(Duration deltaNanos);
    
}
