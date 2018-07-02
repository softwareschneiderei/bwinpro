/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forestsimulator.SQLite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import treegross.base.GenerateXY;
import treegross.base.OutType;
import treegross.base.Stand;

/**
 *
 * @author nagel
 */
public class JPanelPlots extends JPanel {

    private final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
    Stand st = null;
    File dir = null;

    String bestaende[] = new String[1000];
    String bestand = "";
    int nBestaende = 0;
    int standID = -9;

    public JPanelPlots(Stand stx, File dirx) throws IOException {
        initComponents();
        st = stx;
        dir = new File(dirx, "fsplots.db");
        if (dir.exists()) {
            filenameLabel.setText(dir.getCanonicalPath());
        } else {
            JFileChooser fc = new JFileChooser();
            DBFileFilter txtFilter = new DBFileFilter();
            txtFilter.setExtension("db");
            fc.addChoosableFileFilter(txtFilter);
            fc.showOpenDialog(this);
            dir = fc.getSelectedFile();
        }
        filenameLabel.setText(dir.getCanonicalPath());
        setVisible(true);
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
        filenameLabel = new javax.swing.JLabel();
        changeDatabaseButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        standIdHeading = new javax.swing.JLabel();
        standNameHeading = new javax.swing.JLabel();
        yearHeading = new javax.swing.JLabel();
        areaSizeHeading = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        navigateToFirstButton = new javax.swing.JButton();
        navigateBackButton = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        saveActiveStandButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        navigateForwardButton = new javax.swing.JButton();
        navigateToLastButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("forestsimulator/gui"); // NOI18N
        filenameLabel.setText(bundle.getString("JPanelPlots.filenameLabel.text")); // NOI18N
        jPanel1.add(filenameLabel);

        changeDatabaseButton.setText(bundle.getString("JPanelPlots.changeDatabaseButton.text")); // NOI18N
        changeDatabaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeDatabaseButtonActionPerformed(evt);
            }
        });
        jPanel1.add(changeDatabaseButton);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridLayout(2, 4));

        standIdHeading.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        standIdHeading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        standIdHeading.setText(bundle.getString("JPanelPlots.standIdHeading.text")); // NOI18N
        standIdHeading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        standIdHeading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(standIdHeading);

        standNameHeading.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        standNameHeading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        standNameHeading.setText(bundle.getString("JPanelPlots.standNameHeading.text")); // NOI18N
        standNameHeading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        standNameHeading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(standNameHeading);

        yearHeading.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        yearHeading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        yearHeading.setText(bundle.getString("JPanelPlots.yearHeading.text")); // NOI18N
        yearHeading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        yearHeading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(yearHeading);

        areaSizeHeading.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        areaSizeHeading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        areaSizeHeading.setText(bundle.getString("JPanelPlots.areaSizeHeading.text")); // NOI18N
        areaSizeHeading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        areaSizeHeading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(areaSizeHeading);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(jLabel8);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(jLabel9);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(jLabel10);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(jLabel11);

        jPanel3.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel6.setLayout(new java.awt.GridLayout(1, 4));

        navigateToFirstButton.setText(bundle.getString("JPanelPlots.navigateToFirstButton.text")); // NOI18N
        navigateToFirstButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                navigateToFirstButtonActionPerformed(evt);
            }
        });
        jPanel6.add(navigateToFirstButton);

        navigateBackButton.setText(bundle.getString("JPanelPlots.navigateBackButton.text")); // NOI18N
        navigateBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                navigateBackButtonActionPerformed(evt);
            }
        });
        jPanel6.add(navigateBackButton);

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        jPanel6.add(searchField);

        searchButton.setText(bundle.getString("JPanelPlots.searchButton.text")); // NOI18N
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel6.add(searchButton);

        saveActiveStandButton.setBackground(new java.awt.Color(246, 187, 239));
        saveActiveStandButton.setText(bundle.getString("JPanelPlots.saveActiveStandButton.text")); // NOI18N
        saveActiveStandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActiveStandButtonActionPerformed(evt);
            }
        });
        jPanel6.add(saveActiveStandButton);

        deleteButton.setText(bundle.getString("JPanelPlots.deleteButton.text")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel6.add(deleteButton);

        navigateForwardButton.setText(bundle.getString("JPanelPlots.navigateForwardButton.text")); // NOI18N
        navigateForwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                navigateForwardButtonActionPerformed(evt);
            }
        });
        jPanel6.add(navigateForwardButton);

        navigateToLastButton.setText(bundle.getString("JPanelPlots.navigateToLastButton.text")); // NOI18N
        navigateToLastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                navigateToLastButtonActionPerformed(evt);
            }
        });
        jPanel6.add(navigateToLastButton);

        jPanel3.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void changeDatabaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeDatabaseButtonActionPerformed
        JFileChooser fc = new JFileChooser();
        DBFileFilter dbFilter = new DBFileFilter();
        dbFilter.setExtension("db");
        fc.addChoosableFileFilter(dbFilter);
        fc.setFileFilter(dbFilter);
        fc.setAcceptAllFileFilterUsed(true);
        fc.setCurrentDirectory(dir);

        int auswahl = fc.showOpenDialog(this);
        try {
            dir = fc.getSelectedFile();
            filenameLabel.setText(dir.getCanonicalPath());
            setVisible(true);
//           nBestaende=loadBestaende();
        } catch (IOException eio) {
            System.out.println(eio);
        }
    }//GEN-LAST:event_changeDatabaseButtonActionPerformed

    private void saveActiveStandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActiveStandButtonActionPerformed
        // save stand to database
        if (st.center.no.equalsIgnoreCase("circle")) {
            try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", "")) {
                try (PreparedStatement stm = cn.prepareStatement("INSERT INTO stand (name, size_ha, month, year, lat, lon, masl, region, district, sitetype, exposition_gon, slope_percentage)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                    stm.setString(1, st.standname);
                    stm.setDouble(2, st.size);
                    stm.setInt(3, st.monat);
                    stm.setInt(4, st.year);
                    stm.setDouble(5, st.hochwert_m);
                    stm.setDouble(6, st.rechtswert_m);
                    stm.setDouble(7, st.hoehe_uNN_m);
                    stm.setString(8, st.wuchsgebiet);
                    stm.setString(9, st.wuchsgebiet); // TODO: This looks like a bug, check with FVA
                    stm.setString(10, st.standort);
                    stm.setDouble(11, st.exposition_Gon);
                    stm.setDouble(12, st.hangneigungProzent);

                    stm.execute();
                }
//  Bestandes ID merken
                standID = -9;
                try (Statement stmt = cn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT _id FROM stand ORDER BY _id DESC LIMIT 1")) {
                    while (rs.next()) {
                        standID = rs.getInt("_id");
                    }
                }
//           System.out.println("Letzte Id: = "+standID);
//  alle Bäume speichern 
                try (PreparedStatement ps = cn.prepareStatement("INSERT INTO trees (standid, code, name, year, age, dbh, h, si, cb, cw, alive,"
                        + " status, azimuth, distance, z, crop, habitat, fac, remarks, layer)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                    for (int i = 0; i < st.ntrees; i++) {
                        int w = 0;
                        double e = 0.0;
                        double dx = st.tr[i].x - st.center.x;
                        double dy = st.tr[i].y - st.center.y;
                        double ent = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
                        double gon = 200.0 * Math.asin((dx) / ent) / Math.PI;
                        if (dx >= 0.0 && dy >= 0.0) {
                            w = (int) Math.round(gon);
                        }
                        if (dx >= 0.0 && dy < 0.0) {
                            w = (int) Math.round(200.0 - gon);
                        }
                        if (dx < 0.0 && dy < 0.0) {
                            w = (int) Math.round(Math.abs(gon) + 200);
                        }
                        if (dx < 0.0 && dy >= 0.0) {
                            w = (int) Math.round(400.0 - Math.abs(gon));
                        }
                        ps.setInt(1, standID);
                        ps.setInt(2, st.tr[i].code);
                        ps.setString(3, st.tr[i].no);
                        ps.setInt(4, st.year);
                        ps.setInt(5, st.tr[i].age);
                        ps.setDouble(6, st.tr[i].d);
                        ps.setDouble(7, st.tr[i].h);
                        ps.setDouble(8, st.tr[i].si);
                        ps.setDouble(9, st.tr[i].cb);
                        ps.setDouble(10, st.tr[i].cw);
                        ps.setInt(11, st.tr[i].out);
                        ps.setInt(12, st.tr[i].outtype.ordinal());
                        ps.setInt(13, w);
                        ps.setDouble(14, ent);
                        ps.setDouble(15, st.tr[i].z);
                        ps.setBoolean(16, st.tr[i].crop);
                        ps.setBoolean(17, st.tr[i].habitat);
                        ps.setDouble(18, st.tr[i].fac);
                        ps.setString(19, st.tr[i].remarks);
                        ps.setInt(20, st.tr[i].layer);

                        ps.execute();
                    }
                }
            } catch (SQLException eio) {
                System.out.println(eio);
            }
            lastDataset();
        } else {
            JTextArea about = new JTextArea(messages.getString("JPanelPlots.save_stand.no_plots.message"));
            JOptionPane.showMessageDialog(this, about, messages.getString("JPanelPlots.save_stand.no_plots.title"), JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_saveActiveStandButtonActionPerformed

    private void navigateToFirstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navigateToFirstButtonActionPerformed
        // find First
        firstDataset();
    }//GEN-LAST:event_navigateToFirstButtonActionPerformed

    private void navigateToLastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navigateToLastButtonActionPerformed
        // Find last record
        lastDataset();

    }//GEN-LAST:event_navigateToLastButtonActionPerformed

    private void navigateBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navigateBackButtonActionPerformed
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); PreparedStatement stm = cn.prepareStatement("SELECT _id FROM stand WHERE _id < ? ORDER BY _id DESC LIMIT 1")) {
            stm.setInt(1, standID);
            int idold = standID;
            standID = -9;
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    standID = rs.getInt("_id");
                }
            }
            if (standID == -9) {
                standID = idold;
            }
            jLabel8.setText(String.valueOf(standID));
        } catch (SQLException eio) {
            System.out.println(eio);
        }
        dispStandID();
    }//GEN-LAST:event_navigateBackButtonActionPerformed

    private void navigateForwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_navigateForwardButtonActionPerformed
        // Next standid
        nextDataset();
        dispStandID();
        // Find next

    }//GEN-LAST:event_navigateForwardButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // delete standID
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", "")) {
            try (Statement stm = cn.createStatement()) {
                stm.execute("DELETE FROM stand WHERE _id = " + standID);
                stm.execute("DELETE FROM trees WHERE standid = " + standID);
            }
        } catch (SQLException eio) {
            System.out.println(eio);
        }
        int wert = nextDataset();
        if (wert < 0) {
            firstDataset();
        }
        dispStandID();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        //search stand first by id then by name
        String suche = searchField.getText();
        boolean found = false;
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); Statement stm = cn.createStatement()) {
            try (ResultSet rs = stm.executeQuery("SELECT * FROM stand WHERE _id = " + suche + " ;")) {
                while (rs.next()) {
                    standID = rs.getInt("_id");
                    found = true;
                }
            }
        } catch (Exception eio) {
            System.out.println(eio);
        }
        if (found != true) {
            try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); Statement stm = cn.createStatement()) {
                try (ResultSet rs = stm.executeQuery("SELECT * FROM stand WHERE name like '" + suche + "' ;")) {
                    while (rs.next()) {
                        standID = rs.getInt("_id");
                        found = true;
                    }
                }
            } catch (Exception eio) {
                System.out.println(eio);
            }
        }
        if (found) {
            dispStandID();
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

    private void dispStandID() {
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); Statement stm = cn.createStatement()) {
            try (ResultSet rs = stm.executeQuery("SELECT * FROM stand WHERE _id = " + standID + " ;")) {
                while (rs.next()) {
                    jLabel8.setText(String.valueOf(standID));
                    jLabel9.setText(rs.getString("name"));
                    jLabel10.setText(String.valueOf(rs.getInt("year")));
                    jLabel11.setText(rs.getString("size_ha"));
                }
            }
        } catch (Exception eio) {
            System.out.println(eio);
        }
    }

    private int nextDataset() {
        int erg = -9;
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); Statement stm = cn.createStatement()) {
            int idold = standID;
            standID = -9;
            try (ResultSet rs = stm.executeQuery("SELECT _id FROM stand WHERE _id > " + standID + " ORDER BY _id LIMIT 1;")) {
                while (rs.next()) {
                    standID = rs.getInt("_id");
                    erg = standID;
                }
            }
            if (standID == -9) {
                standID = idold;
            }
            jLabel8.setText(String.valueOf(standID));
        } catch (Exception eio) {
            System.out.println(eio);
        }
        dispStandID();
        return erg;
    }

    private int firstDataset() {
        int erg = -9;
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); Statement stm = cn.createStatement()) {
            standID = -9;
            try (ResultSet rs = stm.executeQuery("SELECT _id FROM stand ORDER BY _id LIMIT 1;")) {
                while (rs.next()) {
                    standID = rs.getInt("_id");
                }
            }
            jLabel8.setText(String.valueOf(standID));
        } catch (SQLException eio) {
            System.out.println(eio);
        }
        dispStandID();
        return erg;
    }

    private int lastDataset() {
        int erg = -9;
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); Statement stm = cn.createStatement()) {
            standID = -9;
            try (ResultSet rs = stm.executeQuery("SELECT _id FROM stand ORDER BY _id DESC LIMIT 1;")) {
                while (rs.next()) {
                    standID = rs.getInt("_id");
                    erg = standID;
                }
            }
            jLabel8.setText(String.valueOf(standID));
        } catch (SQLException eio) {
            System.out.println(eio);
        }
        dispStandID();
        return erg;
    }

    public Stand createStand() {
        try (Connection cn = DriverManager.getConnection("jdbc:sqlite:" + dir, "", ""); Statement stm = cn.createStatement()) {
            try (ResultSet rs = stm.executeQuery("SELECT * FROM stand WHERE _id = " + standID)) {
                while (rs.next()) {
                    st.standname = rs.getString("name");
                    st.year = rs.getInt("year");
                    st.size = Double.parseDouble(rs.getString("size_ha"));
                    st.monat = rs.getInt("month");
                    st.rechtswert_m = Double.parseDouble(rs.getString("lat"));
                    st.hochwert_m = Double.parseDouble(rs.getString("lon"));
                    st.hoehe_uNN_m = Double.parseDouble(rs.getString("masl"));
                    st.wuchsgebiet = rs.getString("region");
                    st.wuchsbezirk = rs.getString("district");
                    st.standort = rs.getString("sitetype");
                    st.exposition_Gon = (int) (Math.round(Double.parseDouble(rs.getString("exposition_gon"))));
                    st.hangneigungProzent = Double.parseDouble(rs.getString("slope_percentage"));
                }
            }
            double radius = Math.sqrt(st.size * 10000 / Math.PI);
            st.ncpnt = 0;
            st.center.no = "circle";
            st.center.x = radius;
            st.center.y = radius;
            st.center.z = 0.0;
            for (int i = 0; i < 20; i++) {
                double w = 20.0 * i;
                double xx = radius + Math.sin(Math.PI * w / 200.0) * radius;
                double yy = radius + Math.cos(Math.PI * w / 200.0) * radius;
                double zz = 0.0;
                st.addcornerpoint("Eck" + i, xx, yy, zz);
            }
            try (ResultSet rs = stm.executeQuery("SELECT * FROM trees WHERE standid = " + standID)) {
                st.ntrees = 0;
                while (rs.next()) {
                    String na = rs.getString("name");
                    int cc = rs.getInt("code");
                    int aa = rs.getInt("age");
                    double dd = Double.parseDouble(rs.getString("dbh"));
                    double hh = Double.parseDouble(rs.getString("h"));
                    double si = Double.parseDouble(rs.getString("si"));
                    double ccb = Double.parseDouble(rs.getString("cb"));
                    double ccw = Double.parseDouble(rs.getString("cw"));
                    int oout = rs.getInt("alive");
                    OutType oouttype = OutType.values()[rs.getInt("status")];
                    boolean ccrop = Boolean.parseBoolean(rs.getString("crop"));
                    boolean hhabitat = Boolean.parseBoolean(rs.getString("habitat"));
                    boolean tz = false;
                    String rm = rs.getString("remarks");
                    double e = Double.parseDouble(rs.getString("distance"));
                    double w = Double.parseDouble(rs.getString("azimuth"));
                    double zz = Double.parseDouble(rs.getString("z"));
                    double ff = Double.parseDouble(rs.getString("fac"));
                    int lay = rs.getInt("layer");
                    double xx = radius + Math.sin(Math.PI * w / 200.0) * e;
                    double yy = radius + Math.cos(Math.PI * w / 200.0) * e;
                    int nclone = 1;
                    if (ff >= 2.0) {
                        nclone = (int) Math.floor(ff);
                        ff = ff / Math.floor(ff);
                    }
                    for (int k = 0; k < nclone; k++) {
                        String nam = na;
                        if (k > 0) {
                            nam = na + "_" + k;
                            xx = -9.0;
                            yy = -9.0;
                        }
                        st.addXMLTree(cc, nam, aa, oout, oouttype, dd, hh, ccb, ccw, si, ff, xx, yy, zz, ccrop, tz, hhabitat, 0, 0.0, rm);
                        st.tr[st.ntrees - 1].layer = lay;
                    }
                }
            }
        } catch (Exception eio) {
            System.out.println(eio);
        }
        st.missingData();
        GenerateXY gen = new GenerateXY();
        gen.zufall(st);
        st.descspecies();
        return (st);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaSizeHeading;
    private javax.swing.JButton changeDatabaseButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel filenameLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton navigateBackButton;
    private javax.swing.JButton navigateForwardButton;
    private javax.swing.JButton navigateToFirstButton;
    private javax.swing.JButton navigateToLastButton;
    private javax.swing.JButton saveActiveStandButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel standIdHeading;
    private javax.swing.JLabel standNameHeading;
    private javax.swing.JLabel yearHeading;
    // End of variables declaration//GEN-END:variables
}
