package forestsimulator.standsimulation;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

/**  EditorPanel  
 *
 *   http://www.nw-fva.de
 *
 *  (c) 2007 Juergen Nagel, Northwest German Forest Research Station, 
 *      Grätzelstr.2, 37079 Göttingen, Germany
 *      E-Mail: Juergen.Nagel@nw-fva.de
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT  WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 */
public class EditorPanel extends javax.swing.JPanel {
    String urlcodebase = "";
    Stand st = null;
    javax.swing.table.DefaultTableModel data= new javax.swing.table.DefaultTableModel(
            new Object [][] {  },
            new String [] {
               "Code", "Nr", "Alter", "BHD", "Höhe",  "Bon", "KA", "KB", "lebend", "Entnahme",
               "x","y","z","ZBaum","Habitatb.","Fac","Bemerk","Layer"
            }
        );
    Object[] rowData={" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "};
    javax.swing.table.DefaultTableModel corners= new javax.swing.table.DefaultTableModel(
            new Object [][] {  },
            new String [] {
               "No", "x","y","z"
            }
        );
    Object[] rowData2={" "," "," "," "};
    NumberFormat f=NumberFormat.getInstance();
    ResourceBundle messages;
    boolean polar = false; 

    
    /**
     * Creates new form EditorPanel
     */
    public EditorPanel() {
        initComponents();
        polar = false;
        st= new Stand();
        ExcelAdapter myAd = new ExcelAdapter(jTable1);
        ExcelAdapter myAd2 = new ExcelAdapter(jTable2);
        f=NumberFormat.getInstance(new Locale("en","US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        f.setGroupingUsed(false);
        st.setProgramDir(urlcodebase.toString());
//        System.out.println(urlcodebase);
        st.ntrees=0;
	st.nspecies=0;
	st.ncpnt=0;
	st.year=2008;
        jButton8.setText("xy2polar");

//        ExcelAdapter myAd = new ExcelAdapter(jTable1);
//        loadStand();
    }
    
    private int getInt(String txt){
        int erg = -9;
        try {
           erg=Integer.parseInt(txt.trim());
        }
        catch (Exception e){ erg=-9; }
        return erg;
    }
    private double getDouble(String txt){
        double erg = -99.0;
        try {
           erg=Double.parseDouble(txt.trim());
        }
        catch (Exception e){ erg=-9; }
        return erg;
    }
    private boolean getBoolean(String txt){
        boolean erg = false;
        try {
           erg=Boolean.parseBoolean(txt.trim());
        }
        catch (Exception e){ erg=false; }
        return erg;
    }

    
    public Stand updateStand(){
        st.ntrees=0;
        st.nspecies=0;
        st.ncpnt=0;
// save to class stand
        st.standname=jTextField1.getText();
        st.size=Double.parseDouble(jTextField2.getText());
        st.year=Integer.parseInt(jTextField3.getText());
        st.monat=Integer.parseInt(jTextField4.getText());
        st.rechtswert_m =Double.parseDouble(jTextField5.getText());
        st.hochwert_m =Double.parseDouble(jTextField6.getText());
        st.hoehe_uNN_m =Double.parseDouble(jTextField7.getText());
        st.wuchsgebiet=jTextField8.getText();
        st.wuchsbezirk=jTextField9.getText();
        st.standort=jTextField10.getText();
        st.exposition_Gon=Integer.parseInt(jTextField11.getText());
        st.hangneigungProzent =Double.parseDouble(jTextField12.getText());
        st.standortsKennziffer=jTextField13.getText();
        st.bt=Integer.parseInt(jTextField14.getText());
        st.wet=Integer.parseInt(jTextField15.getText());


//
        st.center.no=(String) jTable1.getValueAt(0,0);
        st.center.x =Double.parseDouble( (String) jTable1.getValueAt(0,1));
        st.center.y =Double.parseDouble( (String) jTable1.getValueAt(0,2));
        st.center.z =Double.parseDouble( (String) jTable1.getValueAt(0,3)) ;       
        for (int i=1; i< jTable1.getRowCount() ; i++){
            String xStr = (String) jTable1.getValueAt(i,1);
            xStr=xStr.trim();
            if (xStr.length()>0){
              st.addcornerpoint((String) jTable1.getValueAt(i,0), Double.parseDouble( (String) jTable1.getValueAt(i,1)), 
              Double.parseDouble( (String) jTable1.getValueAt(i,2)),Double.parseDouble( (String) jTable1.getValueAt(i,3))) ;       
        }}
//
        int m=0;
        for (int i=0; i< jTable2.getRowCount() ; i++){
            String dStr = (String) jTable2.getValueAt(i,3);
            dStr=dStr.trim();
            if (dStr.length()>0){
            try {
                   st.addtree(getInt((String) jTable2.getValueAt(i,0)), (String) jTable2.getValueAt(i,1), getInt((String) jTable2.getValueAt(i,2)), getInt((String) jTable2.getValueAt(i,8)), getDouble((String) jTable2.getValueAt(i,3)), getDouble((String) jTable2.getValueAt(i,4)), getDouble((String) jTable2.getValueAt(i,6)), getDouble((String) jTable2.getValueAt(i,7)), getDouble((String) jTable2.getValueAt(i,5)), getDouble((String) jTable2.getValueAt(i,10)), getDouble((String) jTable2.getValueAt(i,11)), getDouble((String) jTable2.getValueAt(i,12)), 0, 0, 0);
                } catch (Exception ex) {
                        Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            st.tr[m].fac=getDouble( (String) jTable2.getValueAt(i,15));
            st.tr[m].outtype=getInt( (String) jTable2.getValueAt(i,9));
            st.tr[m].crop=getBoolean((String) jTable2.getValueAt(i,13));
            st.tr[m].habitat=getBoolean((String) jTable2.getValueAt(i,14));
//            st.tr[m].c66=  Double.parseDouble( (String) jTable2.getValueAt(i,15));         
//            st.tr[m].c66c=  Double.parseDouble( (String) jTable2.getValueAt(i,16));         
            if (jTable2.getValueAt(i,16)!=null) st.tr[m].remarks=(String) jTable2.getValueAt(i,16);
              else st.tr[m].remarks="";
            if (jTable2.getValueAt(i,17)!=null) st.tr[m].layer=getInt((String) jTable2.getValueAt(i,17));
              else st.tr[m].layer=0;

            m=m+1;
        }}
       return st;
    }
    
        // Stand in die Table1 einladen
    public void loadStand(){
//        for (int j=st.ntrees; j>0; j--) data.removeRow(j-1);
        jTextField1.setText(st.standname);
        jTextField2.setText(new Double(st.size).toString());
        jTextField3.setText(new Integer(st.year).toString());
        jTextField4.setText(new Integer(st.monat).toString());
        jTextField5.setText(new Double(st.rechtswert_m).toString());
        jTextField6.setText(new Double(st.hochwert_m).toString());
        jTextField7.setText(new Double(st.hoehe_uNN_m).toString());
        jTextField8.setText(st.wuchsgebiet);
        jTextField9.setText(st.wuchsbezirk);
        jTextField10.setText(st.standort);
        jTextField11.setText(new Integer(st.exposition_Gon).toString());
        jTextField12.setText(new Double(st.hangneigungProzent).toString());
        jTextField13.setText(st.standortsKennziffer);
        jTextField14.setText(new Integer(st.bt).toString());
        jTextField15.setText(new Integer(st.wet).toString());
// Center and corner points
           corners.addRow(rowData2);
           jTable1.setValueAt(st.center.no,0,0);
           jTable1.setValueAt(f.format(st.center.x),0,1);
           jTable1.setValueAt(f.format(st.center.y),0,2);
           jTable1.setValueAt(f.format(st.center.z),0,3);
        for (int i=0; i< st.ncpnt; i++){
           corners.addRow(rowData2);
           jTable1.setValueAt(st.cpnt[i].no,i+1,0);
           jTable1.setValueAt(f.format(st.cpnt[i].x),i+1,1);
           jTable1.setValueAt(f.format(st.cpnt[i].y),i+1,2);
           jTable1.setValueAt(f.format(st.cpnt[i].z),i+1,3);
        }
      
// Tree data        
        int j=0;
        for (int i=0; i< st.ntrees; i++){
           data.addRow(rowData);
           jTable2.setValueAt(new Integer(st.tr[i].code).toString(),i,0);
           jTable2.setValueAt(st.tr[i].no.toString(),i,1);
           jTable2.setValueAt(new Integer(st.tr[i].age).toString(),i,2);
           jTable2.setValueAt(f.format(st.tr[i].d),i,3);
           jTable2.setValueAt(f.format(st.tr[i].h),i,4);
           jTable2.setValueAt(f.format(st.tr[i].si),i,5);
           jTable2.setValueAt(f.format(st.tr[i].cb),i,6);
           jTable2.setValueAt(f.format(st.tr[i].cw),i,7);
           jTable2.setValueAt(new Integer(st.tr[i].out).toString(),i,8);
           jTable2.setValueAt(new Integer(st.tr[i].outtype).toString(),i,9);
           jTable2.setValueAt(f.format(st.tr[i].x),i,10);
           jTable2.setValueAt(f.format(st.tr[i].y),i,11);
           jTable2.setValueAt(f.format(st.tr[i].z),i,12);
           jTable2.setValueAt(new Boolean(st.tr[i].crop).toString(),i,13);
           jTable2.setValueAt(new Boolean(st.tr[i].habitat).toString(),i,14);
           f.setMaximumFractionDigits(4);
           f.setMinimumFractionDigits(4);

           jTable2.setValueAt(f.format(st.tr[i].fac),i,15);
           f.setMaximumFractionDigits(4);
           f.setMinimumFractionDigits(4);
           jTable2.setValueAt(st.tr[i].remarks,i,16);
           jTable2.setValueAt(new Integer(st.tr[i].layer).toString(),i,17);
         }

    }

    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton6.setText("Bestand lesen");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton6);

        jButton2.setText("Bestand Speichern");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jButton7.setText("Maske löschen");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton7);

        jButton8.setText("Polar - > xy");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton8);

        add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1));

        jPanel4.setLayout(new java.awt.GridLayout(2, 0));

        jPanel1.setLayout(new java.awt.GridLayout(5, 0));

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setText("Bestand");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel9.add(jLabel1);

        jTextField1.setPreferredSize(new java.awt.Dimension(200, 19));
        jPanel9.add(jTextField1);

        jLabel2.setText("Bestandesgröße [ha]");
        jPanel9.add(jLabel2);

        jTextField2.setPreferredSize(new java.awt.Dimension(50, 19));
        jPanel9.add(jTextField2);

        jLabel4.setText("Aufnahme Monat /J ahr");
        jPanel14.add(jLabel4);

        jPanel9.add(jPanel14);

        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        jTextField4.setPreferredSize(new java.awt.Dimension(31, 19));
        jPanel13.add(jTextField4);

        jTextField3.setPreferredSize(new java.awt.Dimension(50, 19));
        jPanel13.add(jTextField3);

        jPanel9.add(jPanel13);

        jPanel1.add(jPanel9);

        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        jLabel5.setText("Lage: Rechtswert [m]");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel10.add(jLabel5);

        jTextField5.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel10.add(jTextField5);

        jLabel6.setText("Hochwert [m]");
        jPanel10.add(jLabel6);

        jTextField6.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel10.add(jTextField6);

        jLabel7.setText("Höhen überNN. [m]");
        jPanel10.add(jLabel7);

        jTextField7.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel10.add(jTextField7);

        jPanel1.add(jPanel10);

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        jLabel8.setText("Wuchsgebiet");
        jPanel11.add(jLabel8);

        jTextField8.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel11.add(jTextField8);

        jLabel9.setText("Wuchsbezirk");
        jPanel11.add(jLabel9);

        jTextField9.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel11.add(jTextField9);

        jLabel10.setText("Standort");
        jPanel11.add(jLabel10);

        jTextField10.setPreferredSize(new java.awt.Dimension(91, 19));
        jPanel11.add(jTextField10);

        jPanel1.add(jPanel11);

        jPanel12.setLayout(new java.awt.GridLayout());

        jLabel11.setText("Exposition [Gon]");
        jPanel12.add(jLabel11);
        jPanel12.add(jTextField11);

        jLabel12.setText("Hangneigung [%]");
        jPanel12.add(jLabel12);
        jPanel12.add(jTextField12);

        jLabel13.setText("Standortskennziffer");
        jPanel12.add(jLabel13);
        jPanel12.add(jTextField13);

        jPanel1.add(jPanel12);

        jPanel15.setLayout(new java.awt.GridLayout(1, 6));

        jLabel14.setText("BT");
        jPanel15.add(jLabel14);

        jTextField14.setText("jTextField14");
        jPanel15.add(jTextField14);

        jLabel3.setText("WET");
        jPanel15.add(jLabel3);

        jTextField15.setText("jTextField15");
        jPanel15.add(jTextField15);
        jPanel15.add(jLabel15);
        jPanel15.add(jLabel16);

        jPanel1.add(jPanel15);

        jPanel4.add(jPanel1);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(corners);
        jTable1.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel8.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jButton3.setText("Eckpunkt hinzufügen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3);

        jButton5.setText("Eckpunkt löschen");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5);

        jPanel8.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jPanel8);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 200));

        jTable2.setModel(data);
        jTable2.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(jTable2);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jButton1.setText("Leere Zeilen hinzufügen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton1);

        jButton4.setText("ausgewählten Baum löschen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton4);

        jPanel5.add(jPanel7, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel5);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

    if (polar){
        polar2xy();
        jButton8.setText("xy2polar");
        jTable1.getColumnModel().getColumn(1).setHeaderValue("x");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("y");
        jTable1.getTableHeader().resizeAndRepaint();
        jTable2.getColumnModel().getColumn(10).setHeaderValue("x");
        jTable2.getColumnModel().getColumn(11).setHeaderValue("y");
        jTable2.getTableHeader().resizeAndRepaint();

        polar=false;
    }
    else {
        xy2polar();
        polar=true;
        jButton8.setText("polar2xy");
        jTable1.getColumnModel().getColumn(1).setHeaderValue("Dist");
        jTable1.getColumnModel().getColumn(2).setHeaderValue("Gon");
        jTable1.getTableHeader().resizeAndRepaint();
        jTable2.getColumnModel().getColumn(10).setHeaderValue("Dist");
        jTable2.getColumnModel().getColumn(11).setHeaderValue("Gon");
        jTable2.getTableHeader().resizeAndRepaint();
    }
        loadStand();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
//  Maske löschen
        clearAll();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
// read stand
      JFileChooser fc = new JFileChooser();
      TxtFileFilter txtFilter = new TxtFileFilter();
      txtFilter.setExtension("xml");
      fc.addChoosableFileFilter(txtFilter);
      int auswahl = fc.showOpenDialog(this);
      String pa= fc.getSelectedFile().getPath();
      String dn= fc.getSelectedFile().getName();
      String fname="";

      URL url =null;
      int m = pa.toUpperCase().indexOf("FILE");
      int m2 = pa.toUpperCase().indexOf("HTTP");
      if ( m < 0 && m2 <0 ) fname="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+pa;
      try {
                 url = new URL(fname);}
       catch (Exception e){ }
      if (url.toString().length() >0){
              TreegrossXML2 treegrossXML = new TreegrossXML2();
              st=treegrossXML.readTreegrossStand(st,url);
        } 
        
       loadStand();
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
                for (int i=0; i<50; i++) data.addRow(rowData);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
      st.ntrees=0;
      st.nspecies=0;
      st.ncpnt=0;
//
       JFileChooser fc = new JFileChooser();
       TxtFileFilter txtFilter = new TxtFileFilter();
       txtFilter.setExtension("xml");
       fc.addChoosableFileFilter(txtFilter);
//       fc.setCurrentDirectory(new File(user.getDataDir()));
       fc.setApproveButtonText("speichern");
       int auswahl = fc.showOpenDialog(this);
       String pa= fc.getSelectedFile().getPath();
       String dn= fc.getSelectedFile().getName();
//       Model mo =new Model();
//       st.setModelRegion(mo.getPlugInName(plugIn));

       st = updateStand();
//
//        for (int j=0;j<st.ntrees;j++) st.tr[j].setMissingData();
//        GenerateXY gxy=new GenerateXY();
//        gxy.zufall(st); 
//        st.sortbyd();
//        st.descspecies();
//       Save stand
       TreegrossXML2 treegrossXML = new TreegrossXML2();
       treegrossXML.saveAsXML(st,pa);

//

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
                corners.addRow(rowData2);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (jTable2.getSelectedRowCount()>0) {
            int m[]=jTable2.getSelectedRows();
            for (int j=0;j<jTable2.getSelectedRowCount();j++)
               for (int i=0;i<15;i++) jTable2.setValueAt("",m[j],i);
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (jTable1.getSelectedRowCount()>0) {
            int m[]=jTable1.getSelectedRows();
            for (int j=0;j<jTable1.getSelectedRowCount();j++)
               for (int i=0;i<4;i++) jTable1.setValueAt("",m[j],i);
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void clearAll(){
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField12.setText("");
        jTextField13.setText("");
        jTextField14.setText("");
        jTextField15.setText("");
        for (int j=0;j<jTable1.getRowCount();j++)
             for (int i=0;i<jTable1.getColumnCount();i++) jTable1.setValueAt("",j,i);
        for (int j=0;j<jTable2.getRowCount();j++)
             for (int i=0;i<jTable2.getColumnCount();i++) jTable2.setValueAt("",j,i);
        
    }
    
    public void setCodebase(String url){
        urlcodebase = url;
        System.out.println("Set url"+url);
        st.setProgramDir(urlcodebase.toString());
    }
    
    public void setStand(Stand stand) {
        st = stand;
    }
    public void setLanguage(String language) {
        Locale currentLocale;
        currentLocale = new Locale(language, "");
        messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
        jLabel1.setText(messages.getString("standname"));
        jLabel2.setText(messages.getString("standsize"));
        jLabel4.setText(messages.getString("monthYear"));
        jLabel5.setText(messages.getString("coordRight"));
        jLabel6.setText(messages.getString("coordHeight"));
        jLabel7.setText(messages.getString("elevation"));
        jLabel8.setText(messages.getString("region"));
        jLabel9.setText(messages.getString("subregion"));
        jLabel10.setText(messages.getString("sitetype"));
        jLabel11.setText(messages.getString("exposition"));
        jLabel12.setText(messages.getString("slope"));
        jLabel13.setText(messages.getString("siteCode"));
        jButton3.setText(messages.getString("addCornerPoint"));
        jButton5.setText(messages.getString("delCornerPoint"));
        jButton1.setText(messages.getString("addEmptyLines"));
        jButton4.setText(messages.getString("delSelectedTree"));
        jButton6.setText(messages.getString("readStand"));
        jButton2.setText(messages.getString("saveStand"));
        jButton7.setText(messages.getString("clearTable"));
        jButton8.setText("xy2polar");

    }
    
    private void xy2polar(){
        double xm = st.center.x;
        double ym = st.center.y;
        for (int i=0; i < st.ncpnt; i++){
            double dx = st.cpnt[i].x-xm;
            double dy = st.cpnt[i].y-ym;
            double ent = Math.sqrt(Math.pow(dx, 2.0)+Math.pow(dy, 2.0));
            double gon = 200.0*Math.asin((dx)/ent)/Math.PI;
            if (dx >=0.0 && dy >=0.0) st.cpnt[i].y=gon;
            if (dx >=0.0 && dy <0.0) st.cpnt[i].y=200.0-gon;
            if (dx <0.0 && dy <0.0) st.cpnt[i].y=Math.abs(gon)+200.0;
            if (dx <0.0 && dy >=0.0) st.cpnt[i].y=400.0-Math.abs(gon);
            st.cpnt[i].x=ent;
        }
        for (int i=0; i < st.ntrees; i++){
            double dx = st.tr[i].x-xm;
            double dy = st.tr[i].y-ym;
            double ent = Math.sqrt(Math.pow(dx, 2.0)+Math.pow(dy, 2.0));
            double gon = 200.0*Math.asin((dx)/ent)/Math.PI;
            if (dx >=0.0 && dy >=0.0) st.tr[i].y=gon;
            if (dx >=0.0 && dy <0.0) st.tr[i].y=200.0-gon;
            if (dx <0.0 && dy <0.0) st.tr[i].y=Math.abs(gon)+200.0;
            if (dx <0.0 && dy >=0.0) st.tr[i].y=400.0-Math.abs(gon);
            st.tr[i].x=ent;
        }
    }    
    public void polar2xy(){
        for (int i=0; i < st.ncpnt; i++){
            double xp=st.center.x+Math.sin(Math.PI*st.cpnt[i].y/200.0)*st.cpnt[i].x;
            double yp=st.center.y+Math.cos(Math.PI*st.cpnt[i].y/200.0)*st.cpnt[i].x;
            st.cpnt[i].x=xp;
            st.cpnt[i].y=yp;
        }
        for (int i=0; i < st.ntrees; i++){
            double xp=st.center.x+Math.sin(Math.PI*st.tr[i].y/200.0)*st.tr[i].x;
            double yp=st.center.y+Math.cos(Math.PI*st.tr[i].y/200.0)*st.tr[i].x;
            st.tr[i].x=xp;
            st.tr[i].y=yp;
        }
    
        
    };
   public boolean getpolar(){
       
       return  polar;
   }      
    

    public Stand getStand() {
        return st ;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
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
