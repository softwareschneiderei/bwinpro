package forestsimulator.dbaccess;

import forestsimulator.util.DurationFormatter;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

public class BatchProgressDialog extends JDialog implements BatchProgressListener {

    private static final ResourceBundle messages = java.util.ResourceBundle.getBundle("forestsimulator/gui"); // NOI18N
        
    private final BatchProcessingControl batchControl;

    public BatchProgressDialog(Frame parent, String databaseFile, BatchProcessingControl batchControl) {
        super(parent);
        initComponents();
        this.batchControl = batchControl;
        infoLabel.setText(MessageFormat.format(messages.getString("BatchProcessingProgressPanel.infoLabel.text"), databaseFile));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressPanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        currentRuleLabel = new javax.swing.JLabel();
        currentRuleDisplay = new javax.swing.JLabel();
        ruleProgressLabel = new javax.swing.JLabel();
        ruleProgress = new javax.swing.JProgressBar();
        passProgress = new javax.swing.JProgressBar();
        passProgressLabel = new javax.swing.JLabel();
        stepProgressLabel = new javax.swing.JLabel();
        stepProgress = new javax.swing.JProgressBar();
        stopButton = new javax.swing.JButton();
        elapsedTimeLabel = new javax.swing.JLabel();
        elapsedTimeDisplay = new javax.swing.JLabel();
        remainingTimeLabel = new javax.swing.JLabel();
        remainingTimeDisplay = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("forestsimulator/gui"); // NOI18N
        setTitle(bundle.getString("BatchProgressDialog.title")); // NOI18N
        setAutoRequestFocus(false);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        infoLabel.setText(bundle.getString("BatchProcessingProgressPanel.infoLabel.text")); // NOI18N

        currentRuleLabel.setText(bundle.getString("BatchProgressDialog.currentRuleLabel.text")); // NOI18N

        ruleProgressLabel.setText(bundle.getString("BatchProcessingProgressPanel.ruleProgressLabel.text")); // NOI18N

        ruleProgress.setString(""); // NOI18N
        ruleProgress.setStringPainted(true);

        passProgress.setString(""); // NOI18N
        passProgress.setStringPainted(true);

        passProgressLabel.setText(bundle.getString("BatchProcessingProgressPanel.passProgressLabel.text")); // NOI18N

        stepProgressLabel.setText(bundle.getString("BatchProgressDialog.stepProgressLabel.text")); // NOI18N

        stepProgress.setString(""); // NOI18N
        stepProgress.setStringPainted(true);

        stopButton.setText(bundle.getString("BatchProcessingProgressPanel.stopButton.text")); // NOI18N
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        elapsedTimeLabel.setText(bundle.getString("BatchProgressDialog.elapsedTimeLabel.text")); // NOI18N

        elapsedTimeDisplay.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        elapsedTimeDisplay.setText("--:--:--"); // NOI18N

        remainingTimeLabel.setText(bundle.getString("BatchProgressDialog.remainingTimeLabel.text")); // NOI18N

        remainingTimeDisplay.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        remainingTimeDisplay.setText("--:--:--");

        javax.swing.GroupLayout progressPanelLayout = new javax.swing.GroupLayout(progressPanel);
        progressPanel.setLayout(progressPanelLayout);
        progressPanelLayout.setHorizontalGroup(
            progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(stopButton))
                    .addGroup(progressPanelLayout.createSequentialGroup()
                        .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(elapsedTimeLabel)
                            .addComponent(remainingTimeLabel)
                            .addComponent(currentRuleLabel))
                        .addGap(16, 16, 16)
                        .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentRuleDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(elapsedTimeDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(remainingTimeDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(progressPanelLayout.createSequentialGroup()
                        .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(progressPanelLayout.createSequentialGroup()
                                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ruleProgressLabel)
                                    .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(passProgressLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(stepProgressLabel, javax.swing.GroupLayout.Alignment.LEADING)))
                                .addGap(66, 66, 66)
                                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(passProgress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                    .addComponent(ruleProgress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(stepProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(infoLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        progressPanelLayout.setVerticalGroup(
            progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel)
                .addGap(18, 18, 18)
                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(currentRuleLabel)
                    .addComponent(currentRuleDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(elapsedTimeLabel)
                    .addComponent(elapsedTimeDisplay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(remainingTimeLabel)
                    .addComponent(remainingTimeDisplay))
                .addGap(18, 18, 18)
                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ruleProgressLabel)
                    .addComponent(ruleProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(passProgressLabel)
                    .addComponent(passProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stepProgressLabel)
                    .addComponent(stepProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(stopButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(progressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(progressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        initiateStop();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        initiateStop();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currentRuleDisplay;
    private javax.swing.JLabel currentRuleLabel;
    private javax.swing.JLabel elapsedTimeDisplay;
    private javax.swing.JLabel elapsedTimeLabel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JProgressBar passProgress;
    private javax.swing.JLabel passProgressLabel;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JLabel remainingTimeDisplay;
    private javax.swing.JLabel remainingTimeLabel;
    private javax.swing.JProgressBar ruleProgress;
    private javax.swing.JLabel ruleProgressLabel;
    private javax.swing.JProgressBar stepProgress;
    private javax.swing.JLabel stepProgressLabel;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateProgress(BatchProgress progress) {
        setVisible(true);
        currentRuleDisplay.setText(progress.currentRuleText());
        elapsedTimeDisplay.setText(DurationFormatter.format(Optional.of(progress.elapsedNanos())));
        if (progress.stepProgress().current == 0) {
            remainingTimeDisplay.setText(DurationFormatter.format(progress.estimatedRemainingNanos()));
        }
        updateProgressBar(ruleProgress, progress.ruleProgress());
        updateProgressBar(passProgress, progress.passProgress());
        updateProgressBar(stepProgress, progress.stepProgress());
    }

    private void updateProgressBar(JProgressBar bar, Progress progress) {
        bar.setMaximum(progress.total);
        bar.setValue(progress.current);
        bar.setString(progressString(progress));
    }

    public String progressString(Progress progress) {
        return progress.current + " / " + progress.total;
    }

    private void initiateStop() {
        batchControl.stopProcessing();
        stopButton.setText(messages.getString("BatchProgressDialog.stopButton.stopping.text")); // NOI18N
        stopButton.setEnabled(false);
    }
    
    @Override
    public void aborted(Duration elapsedTime) {
        dispose();
        showEndDialog(elapsedTime, "BatchProgressDialog.abortedMessage.title", "BatchProgressDialog.abortedMessage.text", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void finished(Duration elapsedTime) {
        dispose();
        showEndDialog(elapsedTime, "BatchProgressDialog.finishedMessage.title", "BatchProgressDialog.finishedMessage.text", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEndDialog(Duration elapsedTime, String titleKey, String messageKey, int messageType) throws HeadlessException {
        JOptionPane.showMessageDialog(
                getParent(),
                MessageFormat.format(messages.getString(messageKey), DurationFormatter.format(Optional.of(elapsedTime))),
                messages.getString(titleKey),
                messageType);
    }
}
