package awsmodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AlbrechtWindStormModelUI extends JDialog implements ActionListener, WindowListener {
	
//	private AlbrechtWindStormMaker caller;
	protected JCheckBox stochasticOptionCheckBox;
	private JButton ok;
		
	public AlbrechtWindStormModelUI(AlbrechtWindStormModel caller) {
		super();
		addWindowListener(this);
		
//		this.caller = caller;
		stochasticOptionCheckBox = new JCheckBox("Enable stochastic mode");
		stochasticOptionCheckBox.setSelected(caller.isStochasticModeEnabled);		// synchronization with the caller;
		stochasticOptionCheckBox.addItemListener(caller);
		
		ok = new JButton("Ok");
		ok.addActionListener(this);
		
		createUI();
	}
	
	private void createUI() {
		setLayout(new BorderLayout());
		setTitle("Albrecht et al.'s wind damage model");
		
		JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		checkBoxPanel.add(stochasticOptionCheckBox);
		
		add(checkBoxPanel, BorderLayout.NORTH);
		
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		controlPanel.add(ok);
		
		add(controlPanel, BorderLayout.SOUTH);
		
		Dimension prefSize = new Dimension(300,125);
		setPreferredSize(prefSize);
		setMinimumSize(prefSize);
		
		setModal(true);
		setLocationRelativeTo(null);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(ok)) {
			okAction();
		}
	}
	
	
	private void okAction() {
		setVisible(false);
	}
	
	
	/**
	 * Called when the window is closed using the right corner x.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		okAction();
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

	
}
