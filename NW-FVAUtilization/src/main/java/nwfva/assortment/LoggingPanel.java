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

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.*;
import nwfva.biomass.BiomassSetting;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.input.*;
import org.jdom.output.XMLOutputter;
import treegross.base.*;

/**
 * Panel, welches auf einen Dialog gelegt werden kann Der Dialog greift auf
 * verschiedene Sprachfile zu
 *
 * @author J. Nagel
 */
public class LoggingPanel extends JPanel {

    private static final Logger log = Logger.getLogger(nwfva.assortment.NWFVA_Nutzung.class.getName());

    BiomassSetting nbs[] = new BiomassSetting[50];
    LoggingSortiment ls[] = new LoggingSortiment[500];
    TimeEstimateFunction tef[] = new TimeEstimateFunction[150];
    int nnbs = 0;

    int nls = 0;
    int ntef = 0;
    static Document doc;
    static Element rootElt;
    String urlString = "";
    boolean combo5Active = true;
    Stand st = null;
    String urlcodebase = "";
    javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
    int nlist = 0;
    boolean neuspeichern = false;
    String fname = "";
    String proDir;
    boolean dialogActive = true;
    String workDir = "";
    int timeframe = 0;
    double fellingHeight = 0.3;
    ResourceBundle messages;

    /**
     *
     * @param stand TreeGrOSS
     * @param programDir Programmverzeichnis
     * @param interActive soll der File Dialog angezeigt werden
     * @param workingDir Ausgabeverzeichnis
     * @param preferredLanguage Sprache
     */
    public LoggingPanel(Stand stand, String programDir, boolean interActive, String workingDir, Locale preferredLanguage) {
        initComponents();
        dialogActive = interActive;
        jPanel8.setVisible(interActive);
        proDir = programDir + System.getProperty("file.separator") + "moduls" + System.getProperty("file.separator") + "assortment";
        workDir = workingDir;
        messages = ResourceBundle.getBundle("nwfva.assortment.LoggingPanel", preferredLanguage);
        jLabel1.setText(messages.getString("name"));
        jLabel2.setText(messages.getString("speciesfrom"));
        jLabel3.setText(messages.getString("speciesto"));
        jLabel10.setText(messages.getString("wertigkeit"));
        jLabel11.setText(messages.getString("preis"));
        jLabel12.setText(messages.getString("percentageoftrees"));
        jLabel14.setText(messages.getString("einstellen"));
        jLabel15.setText(messages.getString("sortimente"));
        jLabel16.setText(messages.getString("fellcut"));
        jCheckBox1.setText(messages.getString("entnahme"));
        jCheckBox3.setText(messages.getString("toCB"));
        jComboBox1.removeAllItems();
        jComboBox1.addItem(messages.getString("addition%"));
        jComboBox1.addItem(messages.getString("additioncm"));
        jComboBox3.removeAllItems();
        jComboBox3.addItem(messages.getString("alltrees"));
        jComboBox3.addItem(messages.getString("croptrees"));
        jComboBox4.removeAllItems();
        jComboBox4.addItem(messages.getString("multiple"));
        jComboBox4.addItem(messages.getString("ones"));
        jButton3.setText(messages.getString("new"));
        jButton4.setText(messages.getString("delete"));

        int m = programDir.toUpperCase().indexOf("FILE");
        int m2 = programDir.toUpperCase().indexOf("HTTP");
        String fname = proDir + System.getProperty("file.separator") + "Assortments.xml";
        String fname2 = proDir + System.getProperty("file.separator") + "EST_Zeiten_Jacke.xml";
        if (m < 0 && m2 < 0) {
            fname = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + System.getProperty("file.separator") + fname;
        }
        if (m < 0 && m2 < 0) {
            fname2 = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + System.getProperty("file.separator") + fname2;
        }
        try {
            URL url = new URL(fname);
            loadls(url);
        } catch (IOException e) {
            log.info("File nicht gefunden: " + fname);
        }

        try {
            URL url = new URL(fname2);
            loadtef(url);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("File nicht gefunden: " + fname2);
        }
        loadxmlFiles();
        st = stand;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel13 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setPreferredSize(new java.awt.Dimension(608, 221));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 204));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("nwfva/assortment/LoggingPanel"); // NOI18N
        jLabel13.setText(bundle.getString("LoggingPanel.jLabel13.text")); // NOI18N
        jPanel8.add(jLabel13);

        jTextField13.setPreferredSize(new java.awt.Dimension(511, 19));
        jPanel8.add(jTextField13);

        jButton5.setText(bundle.getString("LoggingPanel.jButton5.text")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton5);

        add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel14.setText(bundle.getString("LoggingPanel.jLabel14.text")); // NOI18N
        jPanel10.add(jLabel14);

        jTextField15.setText(bundle.getString("LoggingPanel.jTextField15.text")); // NOI18N
        jTextField15.setPreferredSize(new java.awt.Dimension(60, 20));
        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });
        jPanel10.add(jTextField15);

        jLabel16.setText(bundle.getString("LoggingPanel.jLabel16.text")); // NOI18N
        jPanel10.add(jLabel16);

        jTextField16.setText(bundle.getString("LoggingPanel.jTextField16.text")); // NOI18N
        jTextField16.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel10.add(jTextField16);

        jPanel1.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridLayout(5, 0));

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jPanel9.setLayout(new java.awt.GridLayout(1, 8));

        jLabel1.setText(bundle.getString("LoggingPanel.jLabel1.text")); // NOI18N
        jPanel9.add(jLabel1);

        jTextField1.setText(bundle.getString("LoggingPanel.jTextField1.text")); // NOI18N
        jPanel9.add(jTextField1);

        jLabel2.setText(bundle.getString("LoggingPanel.jLabel2.text")); // NOI18N
        jPanel9.add(jLabel2);

        jTextField2.setText(bundle.getString("LoggingPanel.jTextField2.text")); // NOI18N
        jPanel9.add(jTextField2);

        jLabel3.setText(bundle.getString("LoggingPanel.jLabel3.text")); // NOI18N
        jPanel9.add(jLabel3);

        jTextField3.setText(bundle.getString("LoggingPanel.jTextField3.text")); // NOI18N
        jPanel9.add(jTextField3);

        jPanel4.add(jPanel9);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jCheckBox1.setSelected(true);
        jCheckBox1.setText(bundle.getString("LoggingPanel.jCheckBox1.text")); // NOI18N
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jCheckBox1);

        jPanel4.add(jPanel12);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.GridLayout(2, 8));

        jLabel4.setText(bundle.getString("LoggingPanel.jLabel4.text")); // NOI18N
        jPanel5.add(jLabel4);

        jLabel6.setText(bundle.getString("LoggingPanel.jLabel6.text")); // NOI18N
        jPanel5.add(jLabel6);

        jLabel7.setText(bundle.getString("LoggingPanel.jLabel7.text")); // NOI18N
        jPanel5.add(jLabel7);

        jLabel8.setText(bundle.getString("LoggingPanel.jLabel8.text")); // NOI18N
        jPanel5.add(jLabel8);

        jLabel9.setText(bundle.getString("LoggingPanel.jLabel9.text")); // NOI18N
        jPanel5.add(jLabel9);

        jLabel5.setText(bundle.getString("LoggingPanel.jLabel5.text")); // NOI18N
        jPanel5.add(jLabel5);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Zugabe %", "Zugabe cm" }));
        jPanel5.add(jComboBox1);
        jPanel5.add(jPanel7);

        jTextField4.setText(bundle.getString("LoggingPanel.jTextField4.text")); // NOI18N
        jPanel5.add(jTextField4);

        jTextField5.setText(bundle.getString("LoggingPanel.jTextField5.text")); // NOI18N
        jPanel5.add(jTextField5);

        jTextField6.setText(bundle.getString("LoggingPanel.jTextField6.text")); // NOI18N
        jPanel5.add(jTextField6);

        jTextField7.setText(bundle.getString("LoggingPanel.jTextField7.text")); // NOI18N
        jPanel5.add(jTextField7);

        jTextField8.setText(bundle.getString("LoggingPanel.jTextField8.text")); // NOI18N
        jPanel5.add(jTextField8);

        jTextField9.setText(bundle.getString("LoggingPanel.jTextField9.text")); // NOI18N
        jPanel5.add(jTextField9);

        jTextField10.setText(bundle.getString("LoggingPanel.jTextField10.text")); // NOI18N
        jPanel5.add(jTextField10);

        jCheckBox3.setText(bundle.getString("LoggingPanel.jCheckBox3.text")); // NOI18N
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel5.add(jCheckBox3);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.GridLayout(2, 4));

        jLabel10.setText(bundle.getString("LoggingPanel.jLabel10.text")); // NOI18N
        jPanel6.add(jLabel10);

        jLabel11.setText(bundle.getString("LoggingPanel.jLabel11.text")); // NOI18N
        jPanel6.add(jLabel11);

        jLabel12.setText(bundle.getString("LoggingPanel.jLabel12.text")); // NOI18N
        jPanel6.add(jLabel12);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "alle Bäume", "Z- Bäume" }));
        jPanel6.add(jComboBox3);

        jTextField11.setText(bundle.getString("LoggingPanel.jTextField11.text")); // NOI18N
        jPanel6.add(jTextField11);

        jTextField12.setText(bundle.getString("LoggingPanel.jTextField12.text")); // NOI18N
        jPanel6.add(jTextField12);

        jTextField14.setText(bundle.getString("LoggingPanel.jTextField14.text")); // NOI18N
        jPanel6.add(jTextField14);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mehrfach", "einfach" }));
        jPanel6.add(jComboBox4);

        jPanel2.add(jPanel6);

        jPanel13.setLayout(new java.awt.BorderLayout());

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel13.add(jComboBox2, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel13);

        jButton3.setText(bundle.getString("LoggingPanel.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3);

        jButton2.setText(bundle.getString("LoggingPanel.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jButton4.setText(bundle.getString("LoggingPanel.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4);

        jPanel2.add(jPanel3);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel11.setPreferredSize(new java.awt.Dimension(140, 144));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.GridLayout(2, 0));

        jLabel15.setText(bundle.getString("LoggingPanel.jLabel15.text")); // NOI18N
        jPanel14.add(jLabel15);

        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        jPanel14.add(jComboBox5);

        jPanel11.add(jPanel14, java.awt.BorderLayout.NORTH);

        jList1.setModel(listModel);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jPanel11.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel11, java.awt.BorderLayout.WEST);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
// TODO add your handling code here:
        loadFromList();
    }//GEN-LAST:event_jList1ValueChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int m = jList1.getSelectedIndex();
        for (int i = m + 1; i < nls; i++) {
            ls[i - 1] = ls[i];
        }
        nls = nls - 1;
        nlist = nlist - 1;
        String fname = proDir + System.getProperty("file.separator") + jComboBox5.getSelectedItem().toString();

        savels(fname);
        nlist = 0;

        int m1 = proDir.toUpperCase().indexOf("FILE");
        int m2 = proDir.toUpperCase().indexOf("HTTP");
        if (m1 < 0 && m2 < 0) {
            fname = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + System.getProperty("file.separator") + fname;
        }

        listModel.removeAllElements();
        try {
            URL url = new URL(fname);
            loadls(url);
        } catch (IOException e) {
            log.info(e.toString());
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTextField1.setText(" ");
        jTextField2.setText("100");
        jTextField3.setText("999");
        jTextField4.setText("7.0");
        jTextField5.setText("99.9");
        jTextField6.setText("7.0");
        jTextField7.setText("99.9");
        jTextField8.setText("1.0");
        jTextField9.setText("18.0");
        jComboBox1.setSelectedIndex(0);
        jTextField10.setText("0.0");
        jTextField11.setText("50");
        jTextField12.setText("50.0");
        jTextField14.setText("100.0");
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(1);
        jCheckBox1.setSelected(true);
        neuspeichern = true;

        /*        double zugabepro = 0.0;
        double zugabeabs = 0.0;
        if (jComboBox1.getSelectedIndex()==0) zugabepro=Double.parseDouble(jTextField10.getText());
         else zugabeabs=Double.parseDouble(jTextField10.getText());
        boolean zb = true;
        if (jComboBox3.getSelectedIndex()==0) zb=false;
        boolean mehrf = true;
        if (jComboBox4.getSelectedIndex() > 0) mehrf=false;
        
        ls[nls]= new LoggingSortiment(jTextField1.getText(),
                Integer.parseInt(jTextField2.getText()),Integer.parseInt(jTextField3.getText()),
                    Double.parseDouble(jTextField4.getText()),Double.parseDouble(jTextField5.getText()),
                    Double.parseDouble(jTextField6.getText()),Double.parseDouble(jTextField7.getText()),
                    Double.parseDouble(jTextField8.getText()),Double.parseDouble(jTextField9.getText()),
                    zugabepro, zugabeabs,Double.parseDouble(jTextField12.getText()),
                    Double.parseDouble(jTextField11.getText()),Double.parseDouble(jTextField14.getText()),
                    zb, mehrf , jCheckBox1.isSelected(), jCheckBox3.isSelected(), true,
                    nls  );
        nls = nls+1;
        System.out.println("Array Länge "+ls.length);
        savels("C://Dokumente und Einstellungen//nagel//Eigene Dateien//jnProgramme//TreeGrOSSLogging//loggingSortiment.xml");
        try{
          URL url = new URL(urlString+"loggingSortiment.xml");
          loadls(url);
        }
        catch (IOException e){
            e.printStackTrace();
        }
         */
// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        double zugabepro = 0.0;
        double zugabeabs = 0.0;
        if (jComboBox1.getSelectedIndex() == 0) {
            zugabepro = Double.parseDouble(jTextField10.getText());
        } else {
            zugabeabs = Double.parseDouble(jTextField10.getText());
        }
        boolean zb = true;
        if (jComboBox3.getSelectedIndex() == 0) {
            zb = false;
        }
        boolean mehrf = true;
        if (jComboBox4.getSelectedIndex() > 0) {
            mehrf = false;
        }
        int m = nlist;
        if (neuspeichern == false) {
            m = jList1.getSelectedIndex();
        }
        ls[m] = new LoggingSortiment(jTextField1.getText(),
                Integer.parseInt(jTextField2.getText()), Integer.parseInt(jTextField3.getText()),
                Double.parseDouble(jTextField4.getText()), Double.parseDouble(jTextField5.getText()),
                Double.parseDouble(jTextField6.getText()), Double.parseDouble(jTextField7.getText()),
                Double.parseDouble(jTextField8.getText()), Double.parseDouble(jTextField9.getText()),
                zugabepro, zugabeabs, Double.parseDouble(jTextField12.getText()),
                Double.parseDouble(jTextField11.getText()), Double.parseDouble(jTextField14.getText()),
                zb, mehrf, jCheckBox1.isSelected(), jCheckBox3.isSelected(), true,
                nls, jComboBox2.getSelectedIndex());
        if (neuspeichern) {
            listModel.addElement((String) ls[nlist].name);
            nlist = nlist + 1;
        } else {
            listModel.setElementAt((String) ls[m].name, m);
        }
        neuspeichern = false;
        nls = nlist;
        savels(proDir + System.getProperty("file.separator") + jComboBox5.getSelectedItem().toString());

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
        if (jComboBox5.getItemCount() > 0) {
            int m = proDir.toUpperCase().indexOf("FILE");
            int m2 = proDir.toUpperCase().indexOf("HTTP");
            fname = proDir + System.getProperty("file.separator") + jComboBox5.getSelectedItem().toString();
            if (m < 0 && m2 < 0) {
                fname = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + System.getProperty("file.separator") + fname;
            }

            try {
                URL url = new URL(fname);
                loadls(url);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("File nicht gefunden: " + fname);
            }
            jList1.setSelectedIndex(0);
            loadFromList();

        }

    }//GEN-LAST:event_jComboBox5ActionPerformed

    /**
     * Speichern des Ergebnisberichts mit den Sortimenten und Einzelstücken
     *
     * @param fn der XML Datei für den Bericht
     */
    public void savels(String fn) {

        NumberFormat f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        f.setGroupingUsed(false);
        Element elt;
        Element elt2;
        /**
         * Creates an Treegross xml
         */
        Document doc = new Document();
        rootElt = new Element("Sortimente");
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                "type=\"text/xsl\" href=\"loggingSortimente.xsl\"");
        doc.addContent(pi);
        doc.setRootElement(rootElt);
        //         
        /* Alle Sortimente */;
        for (int i = 0; i < nlist; i++) {
            elt = new Element("Sortiment");
            elt = addString(elt, "Id", new Integer(i).toString());
            elt = addString(elt, "Name", ls[i].name);
            elt = addString(elt, "Art_von", new Integer(ls[i].artvon).toString());
            elt = addString(elt, "Art_bis", new Integer(ls[i].artbis).toString());
            elt = addString(elt, "minD", f.format(ls[i].minD));
            elt = addString(elt, "maxD", f.format(ls[i].maxD));
            elt = addString(elt, "minTop", f.format(ls[i].minTop));
            elt = addString(elt, "maxTop", f.format(ls[i].maxTop));
            elt = addString(elt, "minH", f.format(ls[i].minH));
            elt = addString(elt, "maxH", f.format(ls[i].maxH));
            elt = addString(elt, "ZugabeProzent", f.format(ls[i].zugabeProzent));
            elt = addString(elt, "ZugabeCm", f.format(ls[i].zugabeCm));
            elt = addString(elt, "Preis", f.format(ls[i].preis));
            elt = addString(elt, "Gewicht", f.format(ls[i].gewicht));
            elt = addString(elt, "Wahrscheinlichkeit", f.format(ls[i].wahrscheinlich));
            elt = addString(elt, "nurZBaum", new Boolean(ls[i].nurZBaum).toString());
            elt = addString(elt, "mehrfach", new Boolean(ls[i].mehrfach).toString());
            elt = addString(elt, "Entnahme", new Boolean(ls[i].entnahme).toString());
            elt = addString(elt, "bisKA", new Boolean(ls[i].bisKronenansatz).toString());
            elt = addString(elt, "ausgewaehlt", new Boolean(ls[i].ausgewaehlt).toString());
            elt = addString(elt, "Zeitbedarfsfunktion", new Integer(ls[i].zeitFunktion).toString());
            rootElt.addContent(elt);
        }
        /* Abspeichern des doc */
        try (FileOutputStream result = new FileOutputStream(fn)) {
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(doc, result);
        } catch (IOException e) {
            log.info(e.toString());
        }
    }

    Element addString(Element elt, String variable, String text) {
        Element var = new Element(variable);
        var.addContent(text);
        elt.addContent(var);
        return elt;
    }

    /**
     * Berechnung der Sortimente
     */
    public void calculate() {
        TreeLog tl[] = new TreeLog[100];
        int ntl = 0;

        String pa = "";
        String dn = "";
// set Time frame
        if (jTextField15.getText().contains("all")) {
            timeframe = -9999;
        } else {
            timeframe = Integer.parseInt(jTextField15.getText());
        }
//       if (jTextField15.getText().indexOf("all out")>-1) timeframe = 0;
// set felling height usually 0.3m
        fellingHeight = Double.parseDouble(jTextField16.getText());
        if (dialogActive) {
            JFileChooser fc = new JFileChooser();
            int auswahl = fc.showOpenDialog(this);
            pa = fc.getSelectedFile().getPath();
            dn = fc.getSelectedFile().getName();
        } else {
            pa = workDir + System.getProperty("file.separator") + "sortierung.xml";
        }
// ausgewählte markieren
        for (int i = 0; i < nlist; i++) {
            ls[i].ausgewaehlt = false;
        }
        int[] indices = jList1.getSelectedIndices(); //get Selected Assortments from list
        for (int i = 0; i < indices.length; i++) {
            ls[indices[i]].ausgewaehlt = true;
        }
//
        NumberFormat f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        f.setGroupingUsed(false);
        Element elt;
        Element elt2;
        Element elt3;
        Element elt4;
        /**
         * Creates an Treegross xml
         */
        Document doc = new Document();
        rootElt = new Element("Stoffhaushalt");
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                "type=\"text/xsl\" href=\"treegrosslogging.xsl\"");
        doc.addContent(pi);
        doc.setRootElement(rootElt);
//
//	    System.out.println("Neuen Bericht erzeugen nach try");
        /**
         * all data is writen in File info/treelist.html
         */
        try {
// TimeFarme
            elt = new Element("TimeFrame");
            if (timeframe < 0) {
                elt = addString(elt, "Start", "Simulation begin to");
                elt = addString(elt, "End", Integer.toString(st.year));
            }
            if (timeframe == st.year) {
                elt = addString(elt, "Start", "current year");
                elt = addString(elt, "End", Integer.toString(st.year));
            }
            if (timeframe > 0 && st.year < st.year) {
                elt = addString(elt, "Start", "only simulation year");
                elt = addString(elt, "End", Integer.toString(st.year));
            }
            rootElt.addContent(elt);

// Sortimente nach xml
            for (int j = 0; j < nls; j++) {
                if (ls[j].ausgewaehlt) {
                    elt = new Element("Sortiment_gesucht");
                    elt = addString(elt, "Code", ls[j].name);
                    elt = addString(elt, "Art_von", Integer.toString(ls[j].artvon));
                    elt = addString(elt, "Art_bis", Integer.toString(ls[j].artbis));
                    elt = addString(elt, "L_min", f.format(ls[j].minH));
                    elt = addString(elt, "L_min", f.format(ls[j].minH));
                    elt = addString(elt, "L_max", f.format(ls[j].maxH));
                    elt = addString(elt, "D_min", f.format(ls[j].minD));
                    elt = addString(elt, "D_max", f.format(ls[j].maxD));
                    elt = addString(elt, "T_min", f.format(ls[j].minTop));
                    elt = addString(elt, "T_max", f.format(ls[j].maxTop));
                    elt = addString(elt, "ZugP", f.format(ls[j].zugabeProzent));
                    elt = addString(elt, "ZugCm", f.format(ls[j].zugabeCm));
                    rootElt.addContent(elt);
                }
            }

// Sortierung
            TreeSplitter splitter = new TreeSplitter();
            splitter.setAssortments(ls, nls);
            JSortiererNFV sortierer = new JSortiererNFV(st.sp[0].spDef.taperFunctionXML);
            double sumv = 0.0;
            double sumvt = 0.0;
            double sumvn = 0.0;
// Nach Jahren
            int dfYears[] = new int[100];
            int ndfYears = 0;
            for (int l = 0; l < st.ntrees; l++) {
                boolean neu = true;
                for (int ll = 0; ll < ndfYears; ll++) {
                    if (dfYears[ll] == st.tr[l].out) {
                        neu = false;
                    }
                }
                if (neu && st.tr[l].out > 0) {
                    dfYears[ndfYears] = st.tr[l].out;
                    ndfYears = ndfYears + 1;
                }
            }
            boolean neu = true;
            for (int ll = 0; ll < ndfYears; ll++) {
                if (dfYears[ll] == st.year) {
                    neu = false;
                }
            }
            if (neu) {
                dfYears[ndfYears] = st.year;
                ndfYears = ndfYears + 1;
            }
            for (int l = 0; l < ndfYears - 1; l++) {
                for (int ll = l; ll < ndfYears; ll++) {
                    if (dfYears[ll] < dfYears[l]) {
                        int merk = dfYears[ll];
                        dfYears[ll] = dfYears[l];
                        dfYears[l] = merk;
                    }
                }
            }

            for (int l = 0; l < ndfYears; l++) {
                elt4 = new Element("Jahr");
                elt4 = addString(elt4, "Year", Integer.toString(dfYears[l]));

                for (int k = 0; k < st.nspecies; k++) {
                    elt = new Element("Art");
                    elt = addString(elt, "Code", Integer.toString(st.sp[k].code));
//            
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.sp[k].code == st.tr[i].code && (st.tr[i].out == dfYears[l] || (st.tr[i].out < 0 && dfYears[l] == st.year))) {
                            double volumen = 0.0;
                            double volumenToth = 0.0;
                            double volumenEntn = 0.0;
                            double biomasse = 0.0;
// use stem volume function
                            double vol = 0.0;

                            FunctionInterpreter fi = new FunctionInterpreter();
                            if (st.tr[i].sp.spDef.volumeFunctionXML.getFunctionText().length() > 5) {
                                vol = fi.getValueForTree(st.tr[i], st.tr[i].sp.spDef.volumeFunctionXML);
                            } else {
                                vol = st.tr[i].v;
                            }

                            if (st.tr[i].out < 0) {
                                volumen = vol;
                            } else {
                                if (st.tr[i].outtype == 1) {
                                    volumenToth = volumenToth + vol;
                                } else { // Sotierung
// alle ausgewählten Sortimente durchlaufen
                                    splitter.splitTree(st.tr[i], fellingHeight);
                                    tl = splitter.getTreeLogs();
                                    ntl = splitter.getNumberOfLogs();
// Prüfen , ob das Stück aus dem Wald genommen wird oder nicht
                                    for (int jj = 0; jj < ntl; jj++) {

                                        if (tl[jj].removed) {
                                            volumenEntn = volumenEntn + tl[jj].vol_mR;
                                        } else {
                                            volumenToth = volumenToth + tl[jj].vol_mR;
                                        }
// Sortimentsstücke nach xml
//
                                        elt3 = new Element("Sortiment");
                                        elt3 = addString(elt3, "Jahr", Integer.toString(st.tr[i].age));
                                        elt3 = addString(elt3, "Art", Integer.toString(st.tr[i].code));
                                        elt3 = addString(elt3, "Baum_Nr", st.tr[i].no);
                                        elt3 = addString(elt3, "BHD", f.format(st.tr[i].d));
                                        elt3 = addString(elt3, "Name", tl[jj].sortName);
                                        elt3 = addString(elt3, "Entnahmejahr", Integer.toString(st.tr[i].out));
                                        elt3 = addString(elt3, "Entnahmetyp", Integer.toString(st.tr[i].outtype));
                                        elt3 = addString(elt3, "Starthoehe", f.format(tl[jj].startHeight));
                                        elt3 = addString(elt3, "Laenge", f.format(tl[jj].length));
                                        elt3 = addString(elt3, "VolHuber_mR", f.format(tl[jj].volHuber_mR * st.tr[i].fac / st.size));
                                        elt3 = addString(elt3, "VolHuber_oR", f.format(tl[jj].volHuber_oR * st.tr[i].fac / st.size));

//                           double vx = tl[jj].vol_mR;
                                        elt3 = addString(elt3, "VoluZug_mR", f.format(tl[jj].vol_mR * st.tr[i].fac / st.size));
                                        elt3 = addString(elt3, "VoluZug_oR", f.format(tl[jj].vol_oR * st.tr[i].fac / st.size));
                                        elt3 = addString(elt3, "Zeitbedarf_minha", f.format(tl[jj].time * st.tr[i].fac / st.size));
                                        elt3 = addString(elt3, "D_mR", f.format(tl[jj].meanD));
                                        elt.addContent(elt3);
                                    }
                                }
                            }
                            if (volumenEntn > vol) {
                                volumenEntn = vol;
                            }
                            if (st.tr[i].out > 0) {
                                volumenToth = vol - volumenEntn;
                            }
                            elt2 = new Element("Baum");
                            elt2 = addString(elt2, "Nr", st.tr[i].no);
                            elt2 = addString(elt2, "Baumart", Integer.toString(st.tr[i].code));
                            elt2 = addString(elt2, "BHD", f.format(st.tr[i].d));
                            elt2 = addString(elt2, "Hoehe", f.format(st.tr[i].h));
                            elt2 = addString(elt2, "KA", f.format(st.tr[i].cb));
                            elt2 = addString(elt2, "v_lebend", f.format(volumen / st.size));
                            elt2 = addString(elt2, "v_toth", f.format(volumenToth / st.size));
                            elt2 = addString(elt2, "v_entnommen", f.format(volumenEntn / st.size));
                            elt.addContent(elt2);

                            double fx = 1.0;
                            if ((volumenEntn + volumenToth) > 0) {
                                fx = 0.0;
                            }
                            sumv = sumv + volumen;
                            sumvt = sumvt + volumenToth;
                            sumvn = sumvn + volumenEntn;
                        }
                    }
                    elt4.addContent(elt);
                } // for k species
                rootElt.addContent(elt4);
            } // dfYears
        } catch (Exception e) {
            System.out.println(e);
        }
        try (FileOutputStream result = new FileOutputStream(pa)) {
            XMLOutputter outputter = new XMLOutputter();
//            outputter.setNewlines(true);
//            outputter.setIndent("  ");
            outputter.output(doc, result);
            if (dialogActive == false) {
                String seite = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator")
                        + System.getProperty("file.separator") + pa;
                StartBrowser startBrowser = new StartBrowser(seite);
                startBrowser.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Laden der XML-datei mit den vorgegebenen Sortimenten
     *
     * @param url Verzeichnis mit XML Datei
     */
    public void loadls(URL url) {
        nls = 0;
        nlist = 0;
        combo5Active = false;
        combo5Active = true;
        try {
            SAXBuilder builder = new SAXBuilder();
            URLConnection urlcon = url.openConnection();
            Document doc = builder.build(urlcon.getInputStream());

            DocType docType = doc.getDocType();
            Element sortimente = doc.getRootElement();
            List Sortiment = sortimente.getChildren("Sortiment");
            Iterator i = Sortiment.iterator();
            listModel.removeAllElements();
            while (i.hasNext()) {
                Element sortiment = (Element) i.next();
                ls[nls] = new LoggingSortiment(sortiment.getChild("Name").getText(),
                        Integer.parseInt(sortiment.getChild("Art_von").getText()), Integer.parseInt(sortiment.getChild("Art_bis").getText()),
                        Double.parseDouble(sortiment.getChild("minD").getText()), Double.parseDouble(sortiment.getChild("maxD").getText()),
                        Double.parseDouble(sortiment.getChild("minTop").getText()), Double.parseDouble(sortiment.getChild("maxTop").getText()),
                        Double.parseDouble(sortiment.getChild("minH").getText()), Double.parseDouble(sortiment.getChild("maxH").getText()),
                        Double.parseDouble(sortiment.getChild("ZugabeProzent").getText()), Double.parseDouble(sortiment.getChild("ZugabeCm").getText()),
                        Double.parseDouble(sortiment.getChild("Preis").getText()), Double.parseDouble(sortiment.getChild("Gewicht").getText()),
                        Double.parseDouble(sortiment.getChild("Wahrscheinlichkeit").getText()),
                        Boolean.parseBoolean(sortiment.getChild("nurZBaum").getText()),
                        Boolean.parseBoolean(sortiment.getChild("mehrfach").getText()),
                        Boolean.parseBoolean(sortiment.getChild("Entnahme").getText()),
                        Boolean.parseBoolean(sortiment.getChild("bisKA").getText()),
                        Boolean.parseBoolean(sortiment.getChild("ausgewaehlt").getText()),
                        nls, Integer.parseInt(sortiment.getChild("Zeitbedarfsfunktion").getText())
                );
                listModel.addElement((String) ls[nls].name);
                nlist = nlist + 1;
                nls = nls + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadxmlFiles() {
        jComboBox5.removeAllItems();
        String fn = proDir;
        java.io.File verzeichnis = new java.io.File(fn);
// Liste mit Dateien erstellen 
        String entries[] = verzeichnis.list();
        int m = 0;
        if (entries != null) {
            m = entries.length;
        }
        for (int i = 0; i < m; i++) {
            if (entries[i].indexOf(".xml") > 0) {
                jComboBox5.addItem(entries[i]);
            }
        }

    }

    private void loadFromList() {
        int m = jList1.getSelectedIndex(); //get SelectedIndex from List
        if (m > -1) {
            jTextField1.setText(ls[m].name);
            jTextField2.setText(Integer.toString(ls[m].artvon));
            jTextField3.setText(Integer.toString(ls[m].artbis));
            jTextField4.setText(Double.toString(ls[m].minD));
            jTextField5.setText(Double.toString(ls[m].maxD));
            jTextField6.setText(Double.toString(ls[m].minTop));
            jTextField7.setText(Double.toString(ls[m].maxTop));
            jTextField8.setText(Double.toString(ls[m].minH));
            jTextField9.setText(Double.toString(ls[m].maxH));
            if (ls[m].zugabeProzent > 0) {
                jComboBox1.setSelectedIndex(0);
            } else {
                jComboBox1.setSelectedIndex(1);
            }
            if (jComboBox1.getSelectedIndex() == 0) {
                jTextField10.setText(Double.toString(ls[m].zugabeProzent));
            } else {
                jTextField10.setText(Double.toString(ls[m].zugabeCm));
            }
            jTextField11.setText(Double.toString(ls[m].gewicht));
            jTextField12.setText(Double.toString(ls[m].preis));
            jTextField14.setText(Double.toString(ls[m].wahrscheinlich));
            if (ls[m].nurZBaum) {
                jComboBox3.setSelectedIndex(1);
            } else {
                jComboBox3.setSelectedIndex(0);
            }
            if (ls[m].mehrfach) {
                jComboBox4.setSelectedIndex(0);
            } else {
                jComboBox4.setSelectedIndex(1);
            }
            jCheckBox1.setSelected(ls[m].entnahme);
            jCheckBox3.setSelected(ls[m].bisKronenansatz);
            jComboBox2.setSelectedIndex(ls[m].zeitFunktion);
        }
    }

    /**
     * Lädt die XML-Datei mit Arbeitszeiten für die Aushaltung von Sortimenten
     *
     * @param url Verzeichnis der XML Datei
     */
    public void loadtef(URL url) {
        try {
            SAXBuilder builder = new SAXBuilder();
            URLConnection urlcon = url.openConnection();

            Document doc = builder.build(urlcon.getInputStream());

            DocType docType = doc.getDocType();
//
            Element functions = doc.getRootElement();
            List Function = functions.getChildren("ooo_row");
            Iterator i = Function.iterator();

            while (i.hasNext()) {
                Element function = (Element) i.next();
                tef[ntef] = new TimeEstimateFunction(Integer.parseInt(function.getChild("id").getText()),
                        function.getChild("Baumartengruppe").getText(),
                        function.getChild("Sortiment").getText(),
                        function.getChild("Taetigkeit").getText(),
                        Integer.parseInt(function.getChild("Min_Mittendurchmesser").getText().trim()),
                        Integer.parseInt(function.getChild("Max_Mittendurchmesser").getText().trim()),
                        Double.parseDouble(function.getChild("a").getText().trim()),
                        Double.parseDouble(function.getChild("b").getText().trim()),
                        Double.parseDouble(function.getChild("c").getText().trim())
                );
                ntef = ntef + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jComboBox2.removeAllItems();
        for (int i = 0; i < ntef; i++) {
            jComboBox2.addItem(tef[i].speciesGroupName + " :" + tef[i].estName + " :" + tef[i].description);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

}
