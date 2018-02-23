/* http://www.nw-fva.de
   Version 2013-01-13

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Gr�tzelstr.2, 37079 G�ttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */

package nwfva.assortment;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import treegross.base.*;

/**
 * Sortierungsdialog f�r den ForestSimulator auf diesen kann das LoggingPanel
 * gelegt werden
 * @author  J. Nagel
 */
public class SortingDialog extends javax.swing.JDialog {
    private static final Logger log = Logger.getLogger( nwfva.assortment.NWFVA_Nutzung.class.getName() );
    Stand st = null;
    LoggingPanel lp = null;
    Locale currentLocale;
    ResourceBundle messages;
    /** Creates new form SortingDialog */
    public SortingDialog(java.awt.Frame parent, boolean modal, Stand stand, String programDir, boolean interactive,
        String workingDir , String preferredLanguage, FileHandler logHandler) {
        super(parent, modal);
        initComponents();
        if (logHandler == null){
          try{
           Handler handler = new FileHandler("log.txt",true );
           handler.setFormatter(new SimpleFormatter());
           log.addHandler( logHandler );
           log.info( "Sortierungs-Dialog gestartet" );
          }
          catch (Exception e){};
        }
        else log.addHandler(logHandler );
        log.info("Sorting - Dialog");
        currentLocale = new Locale(preferredLanguage, "");
        messages = ResourceBundle.getBundle("nwfva.assortment.SortingDialog",currentLocale);
        jButton1.setText(messages.getString("startSorting"));

        st = stand;
        String Test = programDir;
        lp = new LoggingPanel(st, programDir, interactive, workingDir,preferredLanguage);
//        System.out.println(programDir+"  WD:"+workingDir);
        add(lp);
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
        setTitle("Sorting Dialog");

        jButton1.setText("Entnahmemengen auf der Basis der Sortimente berechnen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, java.awt.BorderLayout.PAGE_END);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-994)/2, (screenSize.height-623)/2, 994, 623);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        lp.calculate();
        dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
    
}
