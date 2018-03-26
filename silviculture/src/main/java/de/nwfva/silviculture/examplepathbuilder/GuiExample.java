/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.examplepathbuilder;

import de.nwfva.silviculture.gui.TeSimpleTreePanel;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 *
 * @author jhansen
 */
public class GuiExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            // Set System L&F
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignore) {
            }
            JFrame mainFrame = new JFrame("Test Treatment Chain");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            TeSimpleTreePanel tesp = new TeSimpleTreePanel((ResourceBundle) null, null, null);
            tesp.setExecuteButtonVisible(false);
            mainFrame.setContentPane(tesp);
            mainFrame.pack();
            mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            mainFrame.setVisible(true);
        });
    }

}
