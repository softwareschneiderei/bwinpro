/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package forestsimulator.standsimulation;
import treegross.base.*;
import java.util.*;
import javax.swing.JDialog;

/**
 *
 * @author  nagel
 */
public class TgEditTreegross extends JDialog {
    Stand st;
    EditorPanel editorPanel = new EditorPanel();
    ResourceBundle messages;
        
    /** Creates new form TgEditTreegross */
    public TgEditTreegross(java.awt.Frame parent, boolean modal, Stand stand, Locale preferredLanguage) {
        super(parent, modal);
        initComponents();
        st=stand;
        Locale currentLocale;
        currentLocale = preferredLanguage;
        messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
        jButton1.setText(messages.getString("acceptChanges"));
        editorPanel.setStand(st);
        editorPanel.setLanguage(preferredLanguage);
        editorPanel.loadStand();
        add(editorPanel);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TreeGrOSS Editor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jButton1.setText("Änderungen übernehmen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-800)/2, (screenSize.height-600)/2, 800, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
        
        st = editorPanel.updateStand();
        st.sortbyd();
        st.missingData();
        st.descspecies();
        GenerateXY genxy = new GenerateXY();
        genxy.zufall(st);
        st.descspecies();
        editorPanel.setStand(st);
        editorPanel.loadStand();
        
    }//GEN-LAST:event_jButton1ActionPerformed

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
// TODO add your handling code here:
            if (editorPanel.getpolar()) {
               editorPanel.polar2xy();
               editorPanel.loadStand();
        }
        st = editorPanel.updateStand();

}//GEN-LAST:event_formWindowClosed


    
    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
    
}
