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

//import treegross.treatment.*;
import java.text.*;
import java.util.*;
import treegross.base.*;
import treegross.treatment.*;


/**
 *
 * @author  nagel
 */
public class TgTreatmentMan3 extends javax.swing.JPanel {
    Stand st = null;
    javax.swing.table.DefaultTableModel data= null;
    Object[] rowData={" "," "," "," "," "," "};
    NumberFormat f=NumberFormat.getInstance();
    ResourceBundle messages;
    Locale currentLocale;
    TgYieldTable  yt  = null;
    Treatment2 treat = new Treatment2();
    
    /** Creates new form TgTreatmentMan3 */
    public TgTreatmentMan3(Stand stparent,  TgJFrame frameparent, String preferredLanguage) {
        initComponents();
        st = stparent;
        Locale currentLocale;
        yt = frameparent.yt;
        currentLocale = new Locale(preferredLanguage, "");
        messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
        jComboBox1.removeAllItems();
        jComboBox1.addItem(messages.getString("singleTreeSelection"));
        jComboBox1.addItem(messages.getString("thinningFromAbove"));
        jComboBox1.addItem(messages.getString("thinningFromBelow"));
        jComboBox1.addItem(messages.getString("thinningQD"));
        jComboBox1.addItem(messages.getString("thinningCM"));
        jComboBox2.removeAllItems();
        jComboBox2.addItem(messages.getString("targetDiameter"));
        jComboBox2.addItem(messages.getString("shelter"));
        jComboBox2.addItem(messages.getString("clearCut"));
        jComboBox2.addItem(messages.getString("clearGaps"));
        jLabel1.setText(messages.getString("typeOfThinning"));
        jLabel2.setText(messages.getString("typeOfHarvest"));
        jLabel3.setText(messages.getString("protection"));
        jLabel4.setText(messages.getString("thinningIntensity"));
        jLabel5.setText(messages.getString("speciesSettings"));
        jLabel6.setText(messages.getString("skidtrailDistance"));
        jLabel7.setText(messages.getString("skidtrailWidth"));
        jLabel8.setText(messages.getString("maxThinning"));
        jLabel9.setText(messages.getString("maxHarvest"));
        jLabel10.setText(messages.getString("simulationtime"));
        jLabel14.setText(messages.getString("senario"));
        jLabel12.setText(messages.getString("skidtrails"));
        jLabel11.setText(messages.getString("years"));
        jLabel21.setText(messages.getString("minCover"));
        jLabel22.setText(messages.getString("protectDbh"));
        jLabel13.setText(messages.getString("planting"));
        jLabel20.setText(messages.getString("plantat"));

        jCheckBox1.setText(messages.getString("randomEffects"));
        jCheckBox2.setText(messages.getString("ingrowthModell"));
        jCheckBox3.setText(messages.getString("skidtrails"));
        jCheckBox4.setText(messages.getString("releaseOnlyCropTrees"));
        jCheckBox5.setText(messages.getString("riskModel"));
        jCheckBox6.setText(messages.getString("minorities"));
        jCheckBox7.setText(messages.getString("removeunderstory"));
        jCheckBox8.setText(messages.getString("active"));
        jButton2.setText(messages.getString("startSimulation"));
        jComboBox3.removeAllItems();
        jComboBox3.addItem(messages.getString("hardwood"));
        jComboBox3.addItem(messages.getString("allhardwood"));
        jComboBox3.addItem(messages.getString("allspecies"));
        jComboBox4.removeAllItems();
        jComboBox4.addItem("0.0 "+messages.getString("none"));
        jComboBox4.addItem("0.8 "+messages.getString("low"));
        jComboBox4.addItem("1.0 "+messages.getString("moderate"));
        jComboBox4.addItem("1.2 "+messages.getString("heavy"));
        jComboBox4.addItem("1.5 "+messages.getString("veryheavy"));
        jComboBox4.addItem("0.9");
        jComboBox4.addItem("1.1");
        jComboBox4.addItem("1.3");
        jComboBox4.addItem("1.4");
        jComboBox4.addItem("1.6");
        jComboBox4.addItem("1.7");
        jComboBox4.addItem("1.8");
        jComboBox4.addItem("1.9");
        
        data= new javax.swing.table.DefaultTableModel(
            new Object [][] {  },
            new String [] {
               messages.getString("species"), messages.getString("code"), messages.getString("thinningHeight"),
                  messages.getString("targetD"), messages.getString("croptrees"), messages.getString("mixture")
            }
        );
        jTable1.setModel(data);
        
        jComboBox4.setSelectedIndex(2);
        
        loadTable();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jCheckBox4 = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Simulation: Dauer");
        jPanel1.add(jLabel10);

        jTextField5.setText("5");
        jTextField5.setPreferredSize(new java.awt.Dimension(22, 20));
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField5);

        jLabel11.setText("Jahre");
        jPanel1.add(jLabel11);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Zufallseffekte nutzen");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel1.add(jCheckBox1);

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Einwuchsmodell aktivieren");
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel1.add(jCheckBox2);

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("Risikomodell aktivieren");
        jPanel1.add(jCheckBox5);

        jButton2.setText("Simulation starten");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridLayout(2, 0));

        jPanel4.setLayout(new java.awt.GridLayout(6, 0));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Szenarioeinstellungen");
        jPanel11.add(jLabel14);

        jPanel4.add(jPanel11);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel12.setText("Erschließung:  ");
        jPanel6.add(jLabel12);

        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel6.add(jCheckBox3);

        jLabel6.setText("Gassenabstand");
        jPanel6.add(jLabel6);

        jTextField1.setText("20.0");
        jTextField1.setPreferredSize(new java.awt.Dimension(35, 20));
        jPanel6.add(jTextField1);

        jLabel7.setText("Gassenbreite");
        jPanel6.add(jLabel7);

        jTextField2.setText("4.0");
        jTextField2.setPreferredSize(new java.awt.Dimension(30, 20));
        jPanel6.add(jTextField2);

        jPanel4.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Durchforstungsart:");
        jPanel7.add(jLabel1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Auslesedurchforstung", "Hochdurchforstung", "Niederdurchforstung" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel7.add(jComboBox1);

        jLabel4.setText("Durchforstungsstärke:");
        jPanel7.add(jLabel4);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "null", "schwach", "mäßig", "stark" }));
        jPanel7.add(jComboBox4);

        jLabel8.setText("Durchforstungsmenge min.");
        jPanel7.add(jLabel8);

        jTextField3.setText("0");
        jTextField3.setPreferredSize(new java.awt.Dimension(35, 20));
        jPanel7.add(jTextField3);

        jLabel15.setText("max");
        jPanel7.add(jLabel15);

        jTextField6.setText("60");
        jTextField6.setPreferredSize(new java.awt.Dimension(35, 20));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField6);

        jCheckBox4.setText("nur Z-Bäume freistellen");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });
        jPanel7.add(jCheckBox4);

        jPanel4.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("Erntetyp:");
        jPanel8.add(jLabel2);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Zielstärkennutzung", "Schirmschlag", "Kahlschlag" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel8.add(jComboBox2);

        jLabel9.setText("Erntemenge min.");
        jPanel8.add(jLabel9);

        jTextField7.setText("10");
        jTextField7.setPreferredSize(new java.awt.Dimension(35, 20));
        jPanel8.add(jTextField7);

        jLabel16.setText("max.");
        jPanel8.add(jLabel16);

        jTextField4.setText("120");
        jTextField4.setPreferredSize(new java.awt.Dimension(35, 20));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel8.add(jTextField4);

        jLabel17.setText("Räumung bei <");
        jPanel8.add(jLabel17);

        jTextField8.setText("0.3");
        jTextField8.setPreferredSize(new java.awt.Dimension(135, 20));
        jPanel8.add(jTextField8);

        jPanel4.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText("Naturschutz:");
        jPanel9.add(jLabel3);

        jLabel18.setText("Habitatbäume [n/ha]");
        jPanel9.add(jLabel18);

        jTextField9.setText("0");
        jTextField9.setPreferredSize(new java.awt.Dimension(35, 20));
        jPanel9.add(jTextField9);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "nur Laubholz", "nur Nadelholz", "alle" }));
        jPanel9.add(jComboBox3);

        jCheckBox6.setText("Minderheitenschutz");
        jPanel9.add(jCheckBox6);

        jLabel21.setText("Mindestbeschirmung");
        jPanel9.add(jLabel21);

        jTextField12.setText("0.0");
        jTextField12.setPreferredSize(new java.awt.Dimension(26, 20));
        jPanel9.add(jTextField12);

        jLabel22.setText("geschützt ab BHD ");
        jPanel9.add(jLabel22);

        jTextField13.setText("150");
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });
        jPanel9.add(jTextField13);

        jPanel4.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel13.setText("Pflanzung: ");
        jPanel10.add(jLabel13);

        jCheckBox8.setText("aktiviert");
        jPanel10.add(jCheckBox8);

        jCheckBox7.setText("Unterstand entfernen ;");
        jPanel10.add(jCheckBox7);

        jLabel19.setText("Pflanzen ab B°  <");
        jPanel10.add(jLabel19);

        jTextField10.setText("0.1");
        jTextField10.setPreferredSize(new java.awt.Dimension(35, 20));
        jPanel10.add(jTextField10);

        jLabel20.setText("; Baumartencode (fl/ha)");
        jPanel10.add(jLabel20);

        jTextField11.setPreferredSize(new java.awt.Dimension(365, 20));
        jPanel10.add(jTextField11);

        jPanel4.add(jPanel10);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel5.setText("Einstellungen der Baumarten:");
        jPanel5.add(jLabel5, java.awt.BorderLayout.NORTH);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1069, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField4ActionPerformed

private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox4ActionPerformed

private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField5ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    // TODO add your handling code here:
    if (st.trule.lastTreatment>= st.year) st.trule.lastTreatment=0; 
    loadSettingsToStandRule();
   

//    HessRied_Steuerung hrs = new HessRied_Steuerung() ;
//    hrs.setTreatment(st, 1, 1);
//    st.ingrowthActive=true;

    int simTime = Integer.parseInt(jTextField5.getText());
    int nSimSteps = (int) Math.ceil(Double.parseDouble(jTextField5.getText())/st.timeStep);
    for (int i=0;i<nSimSteps;i++){
//        System.out.println("St.status+ harvesttype:"+st.status+"  "+st.trule.typeOfHarvest+" "+st.trule.lastTreatment);
        st.descspecies();
        st.executeMortality();

        st.descspecies();
        treat.executeManager2(st);
        st.descspecies();
        int time = st.timeStep;
        if (simTime < st.timeStep) time= simTime;
        yt.enterStandDesc(st);
        st.grow(time,st.ingrowthActive);
        simTime = simTime -st.timeStep;
    }
}//GEN-LAST:event_jButton2ActionPerformed

private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox3ActionPerformed

private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField6ActionPerformed

private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
    if (jComboBox2.getSelectedIndex()==0){
        jTextField8.setVisible(true);
        jLabel17.setVisible(true);
        jLabel9.setVisible(true);
        jLabel16.setVisible(true);
        jTextField7.setVisible(true);
        jTextField4.setVisible(true);
        jLabel17.setText(messages.getString("vjbo"));
        jTextField8.setText("0.3");
    }
    if (jComboBox2.getSelectedIndex()==1){
        jTextField8.setVisible(true);
        jLabel17.setVisible(true);
        jLabel9.setVisible(false);
        jLabel16.setVisible(false);
        jTextField7.setVisible(false);
        jTextField4.setVisible(false);
        jLabel17.setText(messages.getString("vjgz"));
        if (st.trule.regenerationProcess.length()>1) jTextField8.setText(st.trule.regenerationProcess);
            else jTextField8.setText("0.7;0.4;0.2;0.0;");
    }
    if (jComboBox2.getSelectedIndex()==2){
        jLabel9.setVisible(false);
        jLabel16.setVisible(false);
        jTextField7.setVisible(false);
        jTextField4.setVisible(false);
        jTextField8.setVisible(false);
        jLabel17.setVisible(false);
    }

    // TODO add your handling code here:
}//GEN-LAST:event_jComboBox2ActionPerformed

private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField13ActionPerformed

private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jComboBox1ActionPerformed
   
    public void formUpdate(Stand stand){
        loadTable();
    }
/** loads the species settings    */
    public void loadTable(){
        jCheckBox2.setSelected(st.ingrowthActive);

        if (st.random.getRandomType() > 10) jCheckBox1.setSelected(true); else jCheckBox1.setSelected(false); 
        f=NumberFormat.getInstance(new Locale("en","US"));
        f.setMaximumFractionDigits(0);
        f.setMinimumFractionDigits(0);
        f.setGroupingUsed(false);
        for (int i=data.getRowCount(); i>0 ; i=i-1){
            data.removeRow(i-1);
        }
        String pflArten ="";
        if (st.nspecies == 1) st.sp[0].trule.targetCrownPercent=100.0;
        for (int i=0; i< st.nspecies; i++){
               data.addRow(rowData);
               jTable1.setValueAt(st.sp[i].spDef.shortName,i,0);
               jTable1.setValueAt(st.sp[i].code,i,1);
               jTable1.setValueAt(f.format(st.sp[i].trule.minCropTreeHeight),i,2);
               jTable1.setValueAt(f.format(st.sp[i].trule.targetDiameter),i,3);
               jTable1.setValueAt(f.format(st.sp[i].trule.targetCrownPercent),i,5);
               Integer nct = (int) Math.round(st.sp[i].trule.numberCropTreesWanted/(st.sp[i].trule.targetCrownPercent/100.0));
               if (nct <= 1) nct = st.sp[i].spDef.cropTreeNumber;
 //              if (nct <= 2) nct = numberOfCropTrees(i,st.sp[i].trule.targetDiameter,
 //                                                          st.sp[i].trule.targetCrownPercent);
               
               jTable1.setValueAt(nct.toString(),i,4);
               
               Double flant = Math.round(st.sp[i].trule.targetCrownPercent/(10.0 ))/10.0;
               pflArten = pflArten + st.sp[i].code + "["+flant.toString()+"];";
               
        }
        if (st.status ==0) jTextField11.setText(pflArten);
    }
    
    public void loadSettingsToStandRule() {
        
      if (jCheckBox1.isSelected() )  st.random.setRandomType(11); else st.random.setRandomType(10);
      st.ingrowthActive = jCheckBox2.isSelected();
      st.riskActive = jCheckBox5.isSelected();
      st.distanceDependent=true;
// Planting rules
      treat.setAutoPlanting(st,jCheckBox8.isSelected(),  jCheckBox7.isSelected(), Double.parseDouble(jTextField10.getText()), jTextField11.getText() );
// Skidtrails      
      treat.setSkidTrails(st,jCheckBox3.isSelected(),Double.parseDouble(jTextField1.getText()),Double.parseDouble(jTextField2.getText()));
      jCheckBox3.setSelected(false);
      if (st.ntrees > 0){  
// Set thinning  and intensity
        double thIntensity=0.0;
        String thtxt = jComboBox4.getSelectedItem().toString();
        thtxt = thtxt.substring(0,3);
        thIntensity = Double.parseDouble(thtxt);
/*        if (jComboBox4.getSelectedIndex() == 1) thIntensity = 0.8;
        if (jComboBox4.getSelectedIndex() == 2) thIntensity = 1.0;
        if (jComboBox4.getSelectedIndex() == 3) thIntensity = 1.2;
        if (jComboBox4.getSelectedIndex() == 4) thIntensity = 1.5;
*/      
        boolean ctreesOnly=false;
        if (jCheckBox4.isSelected()) ctreesOnly=true;

        treat.setThinningRegime(st, jComboBox1.getSelectedIndex(), thIntensity, Double.parseDouble(jTextField3.getText()),
                 Double.parseDouble(jTextField6.getText()), ctreesOnly) ;
// set Harvesting Regime
        double clearFak=0.0;
        if (jComboBox2.getSelectedIndex()==0) clearFak=Double.parseDouble(jTextField8.getText());
        treat.setHarvestRegime(st, jComboBox2.getSelectedIndex(), Double.parseDouble(jTextField7.getText()),
                Double.parseDouble(jTextField4.getText()), clearFak, jTextField8.getText());
// Set nature conversation
        treat.setNatureProtection(st, Integer.parseInt(jTextField9.getText()), jComboBox3.getSelectedIndex(), 
                jCheckBox6.isSelected(),Double.parseDouble(jTextField12.getText()),Integer.parseInt(jTextField13.getText()));
      }
 //      
      for (int i=0; i < jTable1.getRowCount(); i++){
          int merk = -9;
          for (int j = 0; j < st.nspecies; j++){
              int codex = (Integer) (jTable1.getValueAt(i,1));
              if (st.sp[j].code == codex) merk= j;
          }
          if (merk > -9){
             st.sp[merk].trule.minCropTreeHeight = Double.parseDouble((String)(jTable1.getValueAt(i,2))); 
             st.sp[merk].trule.targetCrownPercent = Double.parseDouble((String)(jTable1.getValueAt(i,5))); 
             st.sp[merk].trule.targetDiameter = Double.parseDouble((String)(jTable1.getValueAt(i,3))); 
             st.sp[merk].trule.numberCropTreesWanted = (int)(Integer.parseInt((String)(jTable1.getValueAt(i,4)))*st.sp[merk].trule.targetCrownPercent/100.0);
          }
       }
      double sum=0.0; 
      for (int i=0; i< st.nspecies; i++) sum=sum+st.sp[i].trule.targetCrownPercent;
      for (int i=0; i< st.nspecies; i++) st.sp[i].trule.targetCrownPercent=100.0*st.sp[i].trule.targetCrownPercent/sum;
//
      loadTable();
    };

    public int numberOfCropTrees(int speciesIndex, double diameter, double percentage){
        Tree atree = new Tree();
        atree.st = st;
        atree.code=st.sp[speciesIndex].code;
        atree.sp=st.sp[speciesIndex];
        atree.d=diameter;
        atree.h=st.sp[speciesIndex].hg;
        double dist_ct=0.0;
        dist_ct=atree.calculateCw();
//Number of crop trees dependent on calcualted distance and actual mixture percent
        return  (int)((10000.0/((Math.PI*Math.pow(dist_ct,2.0))/4))*percentage/100.0);            
    } 
    
    public void selectCropTrees(){
        loadSettingsToStandRule();
        TreatmentElements2 te2 =new TreatmentElements2();
        te2.selectNCropTrees(st);
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
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
