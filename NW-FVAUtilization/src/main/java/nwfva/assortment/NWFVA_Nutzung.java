/* http://www.nw-fva.de
   Version 2013-01-11

   (c) 2013 Juergen Nagel, Northwest German Forest Research Station, 
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
package nwfva.assortment;

import java.awt.Color;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import nwfva.biomass.BiomassDialog;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;
import treegross.base.TreegrossXML2;

/**
 * Das Package dient zur Berechnung von Sortimenten und/oder Biomasse und
 * Nährstoffen und sofern Sortimente, Biomassen und Nährstoffeangaben vorliegen
 * in das Paket Forestsimulator integriert werden. Das Package greift auf die
 * Package TreeGrOSS zurück.
 * 
 * In NWFVA_Nutzung ist eine Desktopanwendung, mit der sich Daten im TreeGrOSS XML auswerten lassen. 
 * Das Programm ist in zwei Stufen aufgebaut. Es kann nur zur Berechnung von Sortimenten
 * verwendet werden und sofern Biomassefunktionen und Nährstoffkonzentrationswerte vorliegen
 * auch zusätzlich für die Berechnung von Biomassen und Nähstoffen verwendet werden.
 * Voraussetzung ist, dass es im Programmverzeichnis ein Unterverzeichnis ./user gibt.
 * In diesem Unterverzeichnis muss 1.) das Unterverzeichnis user/models vorhanden sein,
 * in welchem die Baumarteneinstellungen für den ForestSimulator in einem XML File hinterlegt sind.
 * 2. wird das Unterzeichnis /user/moduls benötigt, welches wiederum die beiden Unter-
 * Verzeichnisse assortment mit der Standardeinstellung für die Sortimente (Assortments.xml) und
 * biomass mit den Biomassefunktionen und Nährstoffkonzentrationwerten (BiomassNWGermany.xml)
 * enthalten muss.
 * 
 * 
 * @author J. Nagel
 */
public class NWFVA_Nutzung extends javax.swing.JFrame {
    Stand st = null;
    FileHandler logHandler = null;
    String localPath = "";
    String programDir ="";
    String version = "Version 0.2 2014-06-08";
    private static final Logger log = Logger.getLogger( nwfva.assortment.NWFVA_Nutzung.class.getName() );

    /**
     * 
     */
    public NWFVA_Nutzung() {
        initComponents();
        try{
           logHandler = new FileHandler("log.txt");
           logHandler.setFormatter(new SimpleFormatter());
           log.addHandler( logHandler );
           log.info( "NWFVA_Nutzung Dialog " );
        }
        catch (Exception e){};
        java.io.File f = new java.io.File("");
        try{ 
           localPath= f.getCanonicalPath();
        } catch (Exception e){};
        st = new Stand();
        st.modelRegion="ForestSimulatorNWGermanyBC4";
        st.FileXMLSettings="ForestSimulatorNWGermanyBC4.xml";
        programDir=localPath+System.getProperty("file.separator")+"user";
        SpeciesDefMap SDM = new SpeciesDefMap();
        SDM.readFromPath(programDir+System.getProperty("file.separator")+"models"+System.getProperty("file.separator")+st.FileXMLSettings);
        st.setSDM(SDM);
        st.setProgramDir(new File(programDir));
        loadGeneralSettings(programDir);
         
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NW-FVA Biomass Calculator");

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jLabel1.setText("Keine Datei geladen");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        jMenuItem1.setText("read TreeGrOSS File");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Auswertung");

        jMenuItem2.setText("Sortimente");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Biomasse & Nährstoffe");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Hilfe");

        jMenuItem4.setText("Info");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-860)/2, (screenSize.height-627)/2, 860, 627);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        TxtFileFilter txtFilter = new TxtFileFilter();
        txtFilter.setExtension("xml");
        fc.addChoosableFileFilter(txtFilter);
        fc.setCurrentDirectory(new File(localPath));
        int auswahl = fc.showOpenDialog(this);
        String pa= fc.getSelectedFile().getPath();
        String dn= fc.getSelectedFile().getName();
          
 //         st.setModelRegion(mo.getPlugInName(plugIn));
        TreegrossXML2 treegrossXML2 = new TreegrossXML2();
        URL url = null;
        String fname=pa;
        int m = pa.toUpperCase().indexOf("FILE");
        int m2 = pa.toUpperCase().indexOf("HTTP");
        if ( m < 0 && m2 <0 ) { fname="file:"+System.getProperty("file.separator")
               +System.getProperty("file.separator")+System.getProperty("file.separator")+pa;}
        try {
               url = new URL(fname); 
               st=treegrossXML2.readTreegrossStand(st,url);
               jLabel1.setText(url.toString());
            }
        catch (Exception e2){log.info( "File: "+fname+" nicht gefunden" );  }
 //
 // berechnung der Volumen wenn d und h bekannt, dies gilt für die ausgeschiedenen Bäume
 //       
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].d >= 7.0 && st.tr[i].h > 1.3 && st.tr[i].v <= 0.0){
                st.tr[i].v = st.tr[i].calculateVolume();
            }
        }        

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        BiomassDialog dialog = new BiomassDialog(this,true,st,programDir,false,localPath, logHandler);
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        SortingDialog dialog = new SortingDialog(this,true,st,programDir,false,localPath, new Locale("de"), logHandler);
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // Hilfe Info
        JTextArea about = new JTextArea("GPXReport"+version+" \n http://nw-fva.de   \n (c) 2013 juergen.nagel@nw-fva.de ");
        about.setBackground(Color.LIGHT_GRAY);
        JOptionPane.showMessageDialog(this, about, "About", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_jMenuItem4ActionPerformed

/**
 * Lädt die generellen Modelleinstellungen aus der Datei mit den Baumarteneinstellungen für
 * das TreeGrOSS Paket und den ForestSimulator.
 * @param Dir Verzeichnis in dem die XML Datei mit den Einstellungen zu finden ist. Diese Datei sollte
 * möglichst im Programmverzeichnis unter user/models/ForestSimulatorNWGermanyBC2.xml abgelegt sein.
 */    
    public void loadGeneralSettings(String Dir){
        java.io.File file;
        String fname="";
        try {
            URL url =null;
            int m = Dir.toUpperCase().indexOf("FILE");
            int m2 = Dir.toUpperCase().indexOf("HTTP");
            String trenn =System.getProperty("file.separator");
            fname=Dir+System.getProperty("file.separator")+"models"+System.getProperty("file.separator")+st.FileXMLSettings;
           if ( m < 0 && m2 <0 ) fname="file:"+trenn+trenn+trenn+fname;
            System.out.println("SpeciesDef: URL: "+fname);
            try {
                 url = new URL(fname);}
            catch (Exception e){
                 JTextArea about = new JTextArea("Genral Settings: Url file not found: "+fname);
                 JOptionPane.showMessageDialog(null, about, "About", JOptionPane.INFORMATION_MESSAGE);
            }
         SAXBuilder builder = new SAXBuilder();
         URLConnection urlcon = url.openConnection();

         Document doc = builder.build(urlcon.getInputStream());
         
         DocType docType = doc.getDocType();
//        
         Element sortimente =  doc.getRootElement();  
         java.util.List Setting = sortimente.getChildren("GeneralSettings");
         Iterator i = Setting.iterator();
         
         while (i.hasNext()) {
            Element setting = (Element) i.next();
            st.modelRegion=setting.getChild("ModelRegion").getText();
            if ( Boolean.parseBoolean(setting.getChild("ErrorComponent").getText())){
                st.random.setRandomType(treegross.random.RandomNumber.PSEUDO);}
            else ; {  st.random.setRandomType(treegross.random.RandomNumber.OFF);}
            st.ingrowthActive=Boolean.parseBoolean(setting.getChild("IngrowthModul").getText());
            st.deadwoodModulName=setting.getChild("DeadwoodModul").getText();
            try { st.deadwoodModulName=setting.getChild("DebriswoodModul").getText();
                 } catch (Exception e){ st.deadwoodModulName="none";}
            try { st.sortingModul=setting.getChild("SortingModul").getText();
                 } catch (Exception e){ st.sortingModul="none";}
            try { st.biomassModul=setting.getChild("BiomassModul").getText();
                 } catch (Exception e){ st.biomassModul="none";}
            try { st.timeStep = Integer.parseInt(setting.getChild("TimeStep").getText());
                 } catch (Exception e){ st.timeStep = 5;}
            break;
            
         }

       } catch (Exception e) {
               e.printStackTrace();
               JTextArea about = new JTextArea("TgDesign file not found: "+fname);
               JOptionPane.showMessageDialog(null, about, "About", JOptionPane.INFORMATION_MESSAGE);
               log.info("SpeciesDef General settings: File nicht gefunden: "+fname);
              }
           
        
    }    
    
    
    /**
     * @param args 
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NWFVA_Nutzung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NWFVA_Nutzung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NWFVA_Nutzung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NWFVA_Nutzung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NWFVA_Nutzung().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
