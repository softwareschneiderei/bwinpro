package forestsimulator.DBAccess;

public interface BatchProgressListener {
    
    public void updateProgress(BatchProgress progress);
    
    public void aborted();
    
}
