package forestsimulator.gui;

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

public class BlockingSpinner {
    
    private final JPanel pane;

    public BlockingSpinner(RootPaneContainer parent) {
        pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        pane.setSize(parent.getContentPane().getSize());
        pane.setBackground(new Color(255, 255, 255, 1));

        final JLabel spinner = new JLabel(new ImageIcon(getClass().getResource("/images/loading-spinner-200px.gif")));
        spinner.setOpaque(true);
//        spinner.setBackground(new Color(255, 255, 255, 1));
        pane.add(spinner);
        pane.setVisible(false);
    }

    public JPanel getPane() {
        return pane;
    }
}
