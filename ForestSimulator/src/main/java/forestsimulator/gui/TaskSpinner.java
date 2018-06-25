package forestsimulator.gui;

import java.awt.Component;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;


/**
 * The TaskSpinner shows a {@link forestsimulator.gui.BlockingSpinner} in the glass pane of the given parent.
 * 
 * It preserves the original glass pane to allow using it on {@link javax.swing.JInternalFrame}.
 * 
 * @param <RESULT> 
 */
public class TaskSpinner<RESULT> {

    private static final Logger logger = Logger.getLogger(TaskSpinner.class.getName());
    private final RootPaneContainer parent;
    private final Component originalGlassPane;
    private volatile boolean finished = false;
    
    public TaskSpinner(RootPaneContainer parent) {
        super();
        this.parent = parent;
        if (this.parent != null) {
            this.originalGlassPane = parent.getGlassPane();
        } else {
            this.originalGlassPane = null;
        }
    }
    
    public RESULT execute(Supplier<RESULT> longRunningTask) {
        try {
            showSpinnerIfLong();
            return longRunningTask.get();
        } finally {
            finished = true;
            hideSpinner();
        }
    }

    private void hideSpinner() {
        if (parent == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            parent.getGlassPane().setVisible(false);
            parent.setGlassPane(originalGlassPane);
        });
    }

    private void showSpinnerIfLong() {
        if (parent == null) {
            return;
        }
        new Thread(() -> {
            synchronized(this) {
                try {
                    wait(500);
                    if (finished) {
                        return;
                    }
                    showSpinner();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.log(Level.SEVERE, "Waiting interrupted", ex);
                }
            }
        }, "Waiting for spinner thread").start();
    }
    
    private void showSpinner() {
        SwingUtilities.invokeLater(() -> {
            parent.setGlassPane(new BlockingSpinner(parent).getPane());
            parent.getGlassPane().setVisible(true);
        });
    }
}
