package forestsimulator.dbaccess;

public interface BatchProgressListener {
    
    public void updateProgress(BatchProgress progress);
    
    public void aborted();
    
}
