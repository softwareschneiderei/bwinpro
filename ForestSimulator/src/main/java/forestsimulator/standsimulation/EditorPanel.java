package forestsimulator.standsimulation;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import javax.swing.table.DefaultTableModel;

/**
 * EditorPanel
 *
 * http://www.nw-fva.de
 *
 * (c) 2007 Juergen Nagel, Northwest German Forest Research Station,
 * Grätzelstr.2, 37079 Göttingen, Germany E-Mail: Juergen.Nagel@nw-fva.de
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
public class EditorPanel extends JPanel {

    String urlcodebase = "";
    Stand st = null;
    DefaultTableModel data = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Code", "Nr", "Alter", "BHD", "Höhe", "Bon", "KA", "KB", "lebend", "Entnahme",
                "x", "y", "z", "ZBaum", "Habitatb.", "Fac", "Bemerk", "Layer"
            }
    );
    Object[] rowData = {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    DefaultTableModel corners = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "No", "x", "y", "z"
            }
    );
    Object[] rowData2 = {" ", " ", " ", " "};
    private final NumberFormat f = NumberFormat.getInstance(new Locale("en", "US"));
    private final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
    private boolean polar = false;

    public EditorPanel() {
        initComponents();
        st = new Stand();
        ExcelAdapter myAd = new ExcelAdapter(jTable1);
        ExcelAdapter myAd2 = new ExcelAdapter(jTable2);
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        f.setGroupingUsed(false);
        st.setProgramDir(new File(urlcodebase));
        st.ntrees = 0;
        st.nspecies = 0;
        st.ncpnt = 0;
        st.year = 2008;
        changeCoordinateSystemButton.setText(messages.getString("EditorPanel.changeCoordinateSystemButton.toPolar.text"));
    }

    private int getInt(String txt) {
        int erg = -9;
        try {
            erg = Integer.parseInt(txt.trim());
        } catch (Exception e) {
            erg = -9;
        }
        return erg;
    }

    private double getDouble(String txt) {
        double erg = -99.0;
        try {
            erg = Double.parseDouble(txt.trim());
        } catch (Exception e) {
            erg = -9;
        }
        return erg;
    }

    private boolean getBoolean(String txt) {
        return Boolean.parseBoolean(txt.trim());
    }

    public Stand updateStand() {
        st.ntrees = 0;
        st.nspecies = 0;
        st.ncpnt = 0;
// save to class stand
        st.standname = standNameTextField.getText();
        st.size = Double.parseDouble(standSizeTextField.getText());
        st.year = Integer.parseInt(registrationDateYearTextField.getText());
        st.monat = Integer.parseInt(registrationDateMonthTextField.getText());
        st.rechtswert_m = Double.parseDouble(positionRightValueTextField.getText());
        st.hochwert_m = Double.parseDouble(positionTopValueTextField.getText());
        st.hoehe_uNN_m = Double.parseDouble(altitudeTextField.getText());
        st.wuchsgebiet = regionTextField.getText();
        st.wuchsbezirk = districtTextField.getText();
        st.standort = locationTextField.getText();
        st.exposition_Gon = Integer.parseInt(expositionTextField.getText());
        st.hangneigungProzent = Double.parseDouble(gradientTextField.getText());
        st.standortsKennziffer = locationCodeTextField.getText();
        st.center.no = (String) jTable1.getValueAt(0, 0);
        st.center.x = Double.parseDouble((String) jTable1.getValueAt(0, 1));
        st.center.y = Double.parseDouble((String) jTable1.getValueAt(0, 2));
        st.center.z = Double.parseDouble((String) jTable1.getValueAt(0, 3));
        for (int i = 1; i < jTable1.getRowCount(); i++) {
            String xStr = (String) jTable1.getValueAt(i, 1);
            xStr = xStr.trim();
            if (xStr.length() > 0) {
                st.addcornerpoint((String) jTable1.getValueAt(i, 0), Double.parseDouble((String) jTable1.getValueAt(i, 1)),
                        Double.parseDouble((String) jTable1.getValueAt(i, 2)), Double.parseDouble((String) jTable1.getValueAt(i, 3)));
            }
        }
        int m = 0;
        for (int i = 0; i < jTable2.getRowCount(); i++) {
            String dStr = (String) jTable2.getValueAt(i, 3);
            dStr = dStr.trim();
            if (dStr.length() > 0) {
                try {
                    st.addtree(getInt((String) jTable2.getValueAt(i, 0)), (String) jTable2.getValueAt(i, 1), getInt((String) jTable2.getValueAt(i, 2)), getInt((String) jTable2.getValueAt(i, 8)), getDouble((String) jTable2.getValueAt(i, 3)), getDouble((String) jTable2.getValueAt(i, 4)), getDouble((String) jTable2.getValueAt(i, 6)), getDouble((String) jTable2.getValueAt(i, 7)), getDouble((String) jTable2.getValueAt(i, 5)), getDouble((String) jTable2.getValueAt(i, 10)), getDouble((String) jTable2.getValueAt(i, 11)), getDouble((String) jTable2.getValueAt(i, 12)), 0, 0, 0);
                } catch (SpeciesNotDefinedException ex) {
                    Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                st.tr[m].fac = getDouble((String) jTable2.getValueAt(i, 15));
                st.tr[m].outtype = getInt((String) jTable2.getValueAt(i, 9));
                st.tr[m].crop = getBoolean((String) jTable2.getValueAt(i, 13));
                st.tr[m].habitat = getBoolean((String) jTable2.getValueAt(i, 14));
//            st.tr[m].c66=  Double.parseDouble( (String) jTable2.getValueAt(i,15));         
//            st.tr[m].c66c=  Double.parseDouble( (String) jTable2.getValueAt(i,16));         
                if (jTable2.getValueAt(i, 16) != null) {
                    st.tr[m].remarks = (String) jTable2.getValueAt(i, 16);
                } else {
                    st.tr[m].remarks = "";
                }
                if (jTable2.getValueAt(i, 17) != null) {
                    st.tr[m].layer = getInt((String) jTable2.getValueAt(i, 17));
                } else {
                    st.tr[m].layer = 0;
                }
                m = m + 1;
            }
        }
        return st;
    }

    // Stand in die Table1 einladen
    public void loadStand() {
//        for (int j=st.ntrees; j>0; j--) data.removeRow(j-1);
        standNameTextField.setText(st.standname);
        standSizeTextField.setText(Double.toString(st.size));
        registrationDateYearTextField.setText(Integer.toString(st.year));
        registrationDateMonthTextField.setText(Integer.toString(st.monat));
        positionRightValueTextField.setText(Double.toString(st.rechtswert_m));
        positionTopValueTextField.setText(Double.toString(st.hochwert_m));
        altitudeTextField.setText(Double.toString(st.hoehe_uNN_m));
        regionTextField.setText(st.wuchsgebiet);
        districtTextField.setText(st.wuchsbezirk);
        locationTextField.setText(st.standort);
        expositionTextField.setText(Integer.toString(st.exposition_Gon));
        gradientTextField.setText(Double.toString(st.hangneigungProzent));
        locationCodeTextField.setText(st.standortsKennziffer);
// Center and corner points
        corners.addRow(rowData2);
        jTable1.setValueAt(st.center.no, 0, 0);
        jTable1.setValueAt(f.format(st.center.x), 0, 1);
        jTable1.setValueAt(f.format(st.center.y), 0, 2);
        jTable1.setValueAt(f.format(st.center.z), 0, 3);
        for (int i = 0; i < st.ncpnt; i++) {
            corners.addRow(rowData2);
            jTable1.setValueAt(st.cpnt[i].no, i + 1, 0);
            jTable1.setValueAt(f.format(st.cpnt[i].x), i + 1, 1);
            jTable1.setValueAt(f.format(st.cpnt[i].y), i + 1, 2);
            jTable1.setValueAt(f.format(st.cpnt[i].z), i + 1, 3);
        }

// Tree data        
        for (int i = 0; i < st.ntrees; i++) {
            data.addRow(rowData);
            jTable2.setValueAt(Integer.toString(st.tr[i].code), i, 0);
            jTable2.setValueAt(st.tr[i].no, i, 1);
            jTable2.setValueAt(Integer.toString(st.tr[i].age), i, 2);
            jTable2.setValueAt(f.format(st.tr[i].d), i, 3);
            jTable2.setValueAt(f.format(st.tr[i].h), i, 4);
            jTable2.setValueAt(f.format(st.tr[i].si), i, 5);
            jTable2.setValueAt(f.format(st.tr[i].cb), i, 6);
            jTable2.setValueAt(f.format(st.tr[i].cw), i, 7);
            jTable2.setValueAt(Integer.toString(st.tr[i].out), i, 8);
            jTable2.setValueAt(Integer.toString(st.tr[i].outtype), i, 9);
            jTable2.setValueAt(f.format(st.tr[i].x), i, 10);
            jTable2.setValueAt(f.format(st.tr[i].y), i, 11);
            jTable2.setValueAt(f.format(st.tr[i].z), i, 12);
            jTable2.setValueAt(Boolean.toString(st.tr[i].crop), i, 13);
            jTable2.setValueAt(Boolean.toString(st.tr[i].habitat), i, 14);
            f.setMaximumFractionDigits(4);
            f.setMinimumFractionDigits(4);

            jTable2.setValueAt(f.format(st.tr[i].fac), i, 15);
            f.setMaximumFractionDigits(4);
            f.setMinimumFractionDigits(4);
            jTable2.setValueAt(st.tr[i].remarks, i, 16);
            jTable2.setValueAt(Integer.toString(st.tr[i].layer), i, 17);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        readStandButton = new javax.swing.JButton();
        saveStandButton = new javax.swing.JButton();
        clearFormButton = new javax.swing.JButton();
        changeCoordinateSystemButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        standNameLabel = new javax.swing.JLabel();
        standNameTextField = new javax.swing.JTextField();
        standSizeLabel = new javax.swing.JLabel();
        standSizeTextField = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        registrationDatelabel = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        registrationDateMonthTextField = new javax.swing.JTextField();
        registrationDateYearTextField = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        positionRightValueLabel = new javax.swing.JLabel();
        positionRightValueTextField = new javax.swing.JTextField();
        positionTopValueLabel = new javax.swing.JLabel();
        positionTopValueTextField = new javax.swing.JTextField();
        altitudeLabel = new javax.swing.JLabel();
        altitudeTextField = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        regionLabel = new javax.swing.JLabel();
        regionTextField = new javax.swing.JTextField();
        districtLabel = new javax.swing.JLabel();
        districtTextField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        locationTextField = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        expositionLabel = new javax.swing.JLabel();
        expositionTextField = new javax.swing.JTextField();
        gradientLabel = new javax.swing.JLabel();
        gradientTextField = new javax.swing.JTextField();
        locationCodeLabel = new javax.swing.JLabel();
        locationCodeTextField = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        addCornerPointButton = new javax.swing.JButton();
        deleteCornerPointButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        addEmptyLineButton = new javax.swing.JButton();
        deleteTreeButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("forestsimulator/gui"); // NOI18N
        readStandButton.setText(bundle.getString("EditorPanel.readStandButton.text")); // NOI18N
        readStandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readStandButtonActionPerformed(evt);
            }
        });
        jPanel3.add(readStandButton);

        saveStandButton.setText(bundle.getString("EditorPanel.saveStandButton.text")); // NOI18N
        saveStandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveStandButtonActionPerformed(evt);
            }
        });
        jPanel3.add(saveStandButton);

        clearFormButton.setText(bundle.getString("EditorPanel.clearFormButton.text")); // NOI18N
        clearFormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFormButtonActionPerformed(evt);
            }
        });
        jPanel3.add(clearFormButton);

        changeCoordinateSystemButton.setText(bundle.getString("EditorPanel.changeCoordinateSystemButton.text")); // NOI18N
        changeCoordinateSystemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeCoordinateSystemButtonActionPerformed(evt);
            }
        });
        jPanel3.add(changeCoordinateSystemButton);

        add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1));

        jPanel4.setLayout(new java.awt.GridLayout(2, 0));

        jPanel1.setLayout(new java.awt.GridLayout(4, 0));

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        standNameLabel.setText(bundle.getString("EditorPanel.standNameLabel.text")); // NOI18N
        standNameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel9.add(standNameLabel);

        standNameTextField.setPreferredSize(new java.awt.Dimension(200, 19));
        jPanel9.add(standNameTextField);

        standSizeLabel.setText(bundle.getString("EditorPanel.standSizeLabel.text")); // NOI18N
        jPanel9.add(standSizeLabel);

        standSizeTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        jPanel9.add(standSizeTextField);

        registrationDatelabel.setText(bundle.getString("EditorPanel.registrationDatelabel.text")); // NOI18N
        jPanel14.add(registrationDatelabel);

        jPanel9.add(jPanel14);

        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        registrationDateMonthTextField.setPreferredSize(new java.awt.Dimension(31, 19));
        jPanel13.add(registrationDateMonthTextField);

        registrationDateYearTextField.setPreferredSize(new java.awt.Dimension(50, 19));
        jPanel13.add(registrationDateYearTextField);

        jPanel9.add(jPanel13);

        jPanel1.add(jPanel9);

        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        positionRightValueLabel.setText(bundle.getString("EditorPanel.positionRightValueLabel.text")); // NOI18N
        positionRightValueLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel10.add(positionRightValueLabel);

        positionRightValueTextField.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel10.add(positionRightValueTextField);

        positionTopValueLabel.setText(bundle.getString("EditorPanel.positionTopValueLabel.text")); // NOI18N
        jPanel10.add(positionTopValueLabel);

        positionTopValueTextField.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel10.add(positionTopValueTextField);

        altitudeLabel.setText(bundle.getString("EditorPanel.altitudeLabel.text")); // NOI18N
        jPanel10.add(altitudeLabel);

        altitudeTextField.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel10.add(altitudeTextField);

        jPanel1.add(jPanel10);

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        regionLabel.setText(bundle.getString("EditorPanel.regionLabel.text")); // NOI18N
        jPanel11.add(regionLabel);

        regionTextField.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel11.add(regionTextField);

        districtLabel.setText(bundle.getString("EditorPanel.districtLabel.text")); // NOI18N
        jPanel11.add(districtLabel);

        districtTextField.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel11.add(districtTextField);

        locationLabel.setText(bundle.getString("EditorPanel.locationLabel.text")); // NOI18N
        jPanel11.add(locationLabel);

        locationTextField.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel11.add(locationTextField);

        jPanel1.add(jPanel11);

        jPanel12.setLayout(new java.awt.GridLayout(1, 6));

        expositionLabel.setText(bundle.getString("EditorPanel.expositionLabel.text")); // NOI18N
        jPanel12.add(expositionLabel);
        jPanel12.add(expositionTextField);

        gradientLabel.setText(bundle.getString("EditorPanel.gradientLabel.text")); // NOI18N
        jPanel12.add(gradientLabel);

        gradientTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradientTextFieldActionPerformed(evt);
            }
        });
        jPanel12.add(gradientTextField);

        locationCodeLabel.setText(bundle.getString("EditorPanel.locationCodeLabel.text")); // NOI18N
        jPanel12.add(locationCodeLabel);

        locationCodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationCodeTextFieldActionPerformed(evt);
            }
        });
        jPanel12.add(locationCodeTextField);

        jPanel1.add(jPanel12);

        jPanel4.add(jPanel1);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(corners);
        jTable1.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel8.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        addCornerPointButton.setText(bundle.getString("EditorPanel.addCornerPointButton.text")); // NOI18N
        addCornerPointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCornerPointButtonActionPerformed(evt);
            }
        });
        jPanel6.add(addCornerPointButton);

        deleteCornerPointButton.setText(bundle.getString("EditorPanel.deleteCornerPointButton.text")); // NOI18N
        deleteCornerPointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCornerPointButtonActionPerformed(evt);
            }
        });
        jPanel6.add(deleteCornerPointButton);

        jPanel8.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jPanel8);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 200));

        jTable2.setModel(data);
        jTable2.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(jTable2);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        addEmptyLineButton.setText(bundle.getString("EditorPanel.addEmptyLineButton.text")); // NOI18N
        addEmptyLineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmptyLineButtonActionPerformed(evt);
            }
        });
        jPanel7.add(addEmptyLineButton);

        deleteTreeButton.setText(bundle.getString("EditorPanel.deleteTreeButton.text")); // NOI18N
        deleteTreeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteTreeButtonActionPerformed(evt);
            }
        });
        jPanel7.add(deleteTreeButton);

        jPanel5.add(jPanel7, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel5);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void changeCoordinateSystemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeCoordinateSystemButtonActionPerformed
        if (polar) {
            polar2xy();
            changeCoordinateSystemButton.setText(messages.getString("EditorPanel.changeCoordinateSystemButton.toPolar.text"));
            jTable1.getColumnModel().getColumn(1).setHeaderValue("x");
            jTable1.getColumnModel().getColumn(2).setHeaderValue("y");
            jTable1.getTableHeader().resizeAndRepaint();
            jTable2.getColumnModel().getColumn(10).setHeaderValue("x");
            jTable2.getColumnModel().getColumn(11).setHeaderValue("y");
            jTable2.getTableHeader().resizeAndRepaint();
            polar = false;
        } else {
            xy2polar();
            changeCoordinateSystemButton.setText(messages.getString("EditorPanel.changeCoordinateSystemButton.text"));
            jTable1.getColumnModel().getColumn(1).setHeaderValue("Dist");
            jTable1.getColumnModel().getColumn(2).setHeaderValue("Gon");
            jTable1.getTableHeader().resizeAndRepaint();
            jTable2.getColumnModel().getColumn(10).setHeaderValue("Dist");
            jTable2.getColumnModel().getColumn(11).setHeaderValue("Gon");
            jTable2.getTableHeader().resizeAndRepaint();
            polar = true;
        }
        loadStand();
    }//GEN-LAST:event_changeCoordinateSystemButtonActionPerformed

    private void clearFormButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFormButtonActionPerformed
        clearAll();
    }//GEN-LAST:event_clearFormButtonActionPerformed

    private void readStandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readStandButtonActionPerformed
        // read stand
        JFileChooser fc = new JFileChooser();
        TxtFileFilter txtFilter = new TxtFileFilter();
        txtFilter.setExtension("xml");
        fc.addChoosableFileFilter(txtFilter);
        int auswahl = fc.showOpenDialog(this);
        String pa = fc.getSelectedFile().getPath();
        String dn = fc.getSelectedFile().getName();
        String fname = "";

        URL url = null;
        int m = pa.toUpperCase().indexOf("FILE");
        int m2 = pa.toUpperCase().indexOf("HTTP");
        if (m < 0 && m2 < 0) {
            fname = "file:" + System.getProperty("file.separator") + System.getProperty("file.separator") + pa;
        }
        try {
            url = new URL(fname);
        } catch (Exception e) {
        }
        if (url.toString().length() > 0) {
            TreegrossXML2 treegrossXML = new TreegrossXML2();
            st = treegrossXML.readTreegrossStand(st, url);
        }
        loadStand();
    }//GEN-LAST:event_readStandButtonActionPerformed

    private void addEmptyLineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmptyLineButtonActionPerformed
        for (int i = 0; i < 50; i++) {
            data.addRow(rowData);
        }
    }//GEN-LAST:event_addEmptyLineButtonActionPerformed

    private void saveStandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStandButtonActionPerformed
        st.ntrees = 0;
        st.nspecies = 0;
        st.ncpnt = 0;
        JFileChooser fc = new JFileChooser();
        TxtFileFilter txtFilter = new TxtFileFilter();
        txtFilter.setExtension("xml");
        fc.addChoosableFileFilter(txtFilter);
//       fc.setCurrentDirectory(new File(user.getDataDir()));
        fc.setApproveButtonText("speichern");
        int auswahl = fc.showOpenDialog(this);
        String pa = fc.getSelectedFile().getPath();
        String dn = fc.getSelectedFile().getName();
//       Model mo =new Model();
//       st.setModelRegion(mo.getPlugInName(plugIn));
        st = updateStand();
//        for (int j=0;j<st.ntrees;j++) st.tr[j].setMissingData();
//        GenerateXY gxy=new GenerateXY();
//        gxy.zufall(st); 
//        st.sortbyd();
//        st.descspecies();
//       Save stand
        TreegrossXML2 treegrossXML = new TreegrossXML2();
        treegrossXML.saveAsXML(st, pa);
    }//GEN-LAST:event_saveStandButtonActionPerformed

    private void addCornerPointButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCornerPointButtonActionPerformed
        corners.addRow(rowData2);
    }//GEN-LAST:event_addCornerPointButtonActionPerformed

    private void deleteTreeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteTreeButtonActionPerformed
        if (jTable2.getSelectedRowCount() > 0) {
            int m[] = jTable2.getSelectedRows();
            for (int j = 0; j < jTable2.getSelectedRowCount(); j++) {
                for (int i = 0; i < 15; i++) {
                    jTable2.setValueAt("", m[j], i);
                }
            }
        }
    }//GEN-LAST:event_deleteTreeButtonActionPerformed

    private void deleteCornerPointButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCornerPointButtonActionPerformed
        if (jTable1.getSelectedRowCount() > 0) {
            int m[] = jTable1.getSelectedRows();
            for (int j = 0; j < jTable1.getSelectedRowCount(); j++) {
                for (int i = 0; i < 4; i++) {
                    jTable1.setValueAt("", m[j], i);
                }
            }
        }
    }//GEN-LAST:event_deleteCornerPointButtonActionPerformed

    private void gradientTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradientTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gradientTextFieldActionPerformed

    private void locationCodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationCodeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locationCodeTextFieldActionPerformed

    private void clearAll() {
        standNameTextField.setText("");
        standSizeTextField.setText("");
        registrationDateYearTextField.setText("");
        registrationDateMonthTextField.setText("");
        positionRightValueTextField.setText("");
        positionTopValueTextField.setText("");
        altitudeTextField.setText("");
        regionTextField.setText("");
        districtTextField.setText("");
        locationTextField.setText("");
        expositionTextField.setText("");
        gradientTextField.setText("");
        locationCodeTextField.setText("");
        for (int j = 0; j < jTable1.getRowCount(); j++) {
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                jTable1.setValueAt("", j, i);
            }
        }
        for (int j = 0; j < jTable2.getRowCount(); j++) {
            for (int i = 0; i < jTable2.getColumnCount(); i++) {
                jTable2.setValueAt("", j, i);
            }
        }
    }

    public void setCodebase(String url) {
        urlcodebase = url;
        System.out.println("Set url" + url);
        st.setProgramDir(new File(urlcodebase));
    }

    public void setStand(Stand stand) {
        st = stand;
    }

    private void xy2polar() {
        double xm = st.center.x;
        double ym = st.center.y;
        for (int i = 0; i < st.ncpnt; i++) {
            double dx = st.cpnt[i].x - xm;
            double dy = st.cpnt[i].y - ym;
            double ent = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
            double gon = 200.0 * Math.asin((dx) / ent) / Math.PI;
            if (dx >= 0.0 && dy >= 0.0) {
                st.cpnt[i].y = gon;
            }
            if (dx >= 0.0 && dy < 0.0) {
                st.cpnt[i].y = 200.0 - gon;
            }
            if (dx < 0.0 && dy < 0.0) {
                st.cpnt[i].y = Math.abs(gon) + 200.0;
            }
            if (dx < 0.0 && dy >= 0.0) {
                st.cpnt[i].y = 400.0 - Math.abs(gon);
            }
            st.cpnt[i].x = ent;
        }
        for (int i = 0; i < st.ntrees; i++) {
            double dx = st.tr[i].x - xm;
            double dy = st.tr[i].y - ym;
            double ent = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
            double gon = 200.0 * Math.asin((dx) / ent) / Math.PI;
            if (dx >= 0.0 && dy >= 0.0) {
                st.tr[i].y = gon;
            }
            if (dx >= 0.0 && dy < 0.0) {
                st.tr[i].y = 200.0 - gon;
            }
            if (dx < 0.0 && dy < 0.0) {
                st.tr[i].y = Math.abs(gon) + 200.0;
            }
            if (dx < 0.0 && dy >= 0.0) {
                st.tr[i].y = 400.0 - Math.abs(gon);
            }
            st.tr[i].x = ent;
        }
    }

    public void polar2xy() {
        for (int i = 0; i < st.ncpnt; i++) {
            double xp = st.center.x + Math.sin(Math.PI * st.cpnt[i].y / 200.0) * st.cpnt[i].x;
            double yp = st.center.y + Math.cos(Math.PI * st.cpnt[i].y / 200.0) * st.cpnt[i].x;
            st.cpnt[i].x = xp;
            st.cpnt[i].y = yp;
        }
        for (int i = 0; i < st.ntrees; i++) {
            double xp = st.center.x + Math.sin(Math.PI * st.tr[i].y / 200.0) * st.tr[i].x;
            double yp = st.center.y + Math.cos(Math.PI * st.tr[i].y / 200.0) * st.tr[i].x;
            st.tr[i].x = xp;
            st.tr[i].y = yp;
        }
    }

   public boolean getpolar() {
        return polar;
    }

    public Stand getStand() {
        return st;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCornerPointButton;
    private javax.swing.JButton addEmptyLineButton;
    private javax.swing.JLabel altitudeLabel;
    private javax.swing.JTextField altitudeTextField;
    private javax.swing.JButton changeCoordinateSystemButton;
    private javax.swing.JButton clearFormButton;
    private javax.swing.JButton deleteCornerPointButton;
    private javax.swing.JButton deleteTreeButton;
    private javax.swing.JLabel districtLabel;
    private javax.swing.JTextField districtTextField;
    private javax.swing.JLabel expositionLabel;
    private javax.swing.JTextField expositionTextField;
    private javax.swing.JLabel gradientLabel;
    private javax.swing.JTextField gradientTextField;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel locationCodeLabel;
    private javax.swing.JTextField locationCodeTextField;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JTextField locationTextField;
    private javax.swing.JLabel positionRightValueLabel;
    private javax.swing.JTextField positionRightValueTextField;
    private javax.swing.JLabel positionTopValueLabel;
    private javax.swing.JTextField positionTopValueTextField;
    private javax.swing.JButton readStandButton;
    private javax.swing.JLabel regionLabel;
    private javax.swing.JTextField regionTextField;
    private javax.swing.JTextField registrationDateMonthTextField;
    private javax.swing.JTextField registrationDateYearTextField;
    private javax.swing.JLabel registrationDatelabel;
    private javax.swing.JButton saveStandButton;
    private javax.swing.JLabel standNameLabel;
    private javax.swing.JTextField standNameTextField;
    private javax.swing.JLabel standSizeLabel;
    private javax.swing.JTextField standSizeTextField;
    // End of variables declaration//GEN-END:variables

}
