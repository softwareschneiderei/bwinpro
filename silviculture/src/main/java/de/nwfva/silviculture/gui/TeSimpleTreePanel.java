/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.gui;

import de.nwfva.silviculture.base.RBHolder;
import de.nwfva.silviculture.base.TeChain;
import de.nwfva.silviculture.base.TePhase;
import de.nwfva.silviculture.base.TeTools;
import de.nwfva.silviculture.base.TreatmentElement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 */
public class TeSimpleTreePanel extends javax.swing.JPanel implements ActionListener {

    private TeChain chain;   
    private SpeciesDefMap sdm;
    private Stand stand;


    /**
     * Creates new form mainPanel
     *
     * @param rb resource bundle to use can be null
     * @param sdm the species definition map used to intialize default values
     * @param st an initial stand to use the buuldes path for, can be set later via <code>setStand</code>
     */
    public TeSimpleTreePanel(ResourceBundle rb, SpeciesDefMap sdm, Stand st) {    
        RBHolder.setResourceBundle(rb);
        stand = st;
        if(sdm!= null) {
        this.sdm = sdm;
        } else {
            this.sdm = new SpeciesDefMap();
            this.sdm.readInternal(null);            
        }
        System.out.println("using sdm: " + this.sdm.getActualURL());
        
        initComponents();        
        initElementList();
        initActionListener();
        jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTextArea1.setEditable(false);
        jTextArea1.setCursor(null);
        jTextArea1.setOpaque(false);
        jTextArea1.setFocusable(false);
        jTextArea1.setFont(UIManager.getFont("Label.font"));
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setLineWrap(true);       
        internationalize(RBHolder.getResourceBundle());
        initEmptyChain();
    }

    public TeSimpleTreePanel(Locale l, SpeciesDefMap sdm) {
        this(ResourceBundle.getBundle("de/nwfva/silviculture/gui/TeGuiText", l), sdm, null);
    }

    public final void internationalize(ResourceBundle bundle) {
        // first set std
        applyResourceBundle(ResourceBundle.getBundle("de/nwfva/silviculture/gui/TeGuiText"));
        if (bundle != null) {
            applyResourceBundle(bundle);
        }
    }
    
    public void setExecuteButtonVisible(boolean aFlag) {
        this.jButton10.setVisible(aFlag);
    }

    public void internationalize(Locale l) {
        internationalize(ResourceBundle.getBundle("de/nwfva/silviculture/gui/TeGuiText", l));
    }

    private void applyResourceBundle(ResourceBundle bundle) {       
        String value;
        try {
            value = RBHolder.getResourceBundle().getString("te_description_border_text");
            ((TitledBorder) jPanel5.getBorder()).setTitle(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_chain_border_text");
            ((TitledBorder) jScrollPane2.getBorder()).setTitle(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_properties_border_text");
            ((TitledBorder) jPanel6.getBorder()).setTitle(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_available_te_border_text");
            ((TitledBorder) jScrollPane1.getBorder()).setTitle(value);
        } catch (Exception ignore) {
        }
        //buttons
        try {
            value = RBHolder.getResourceBundle().getString("te_add_te_btn");
            jButton1.setText(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_add_phase_btn");
            jButton2.setText(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_add_interval_btn");
            jButton7.setText(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_clear_chain_btn");
            jButton6.setText(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_remove_btn");
            jButton3.setText(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_up_btn");
            jButton8.setText(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_down_btn");
            jButton9.setText(value);
        } catch (Exception ignore) {
        }
        try {
            value = RBHolder.getResourceBundle().getString("te_execute_btn");
            jButton10.setText(value);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(jButton1)) {
            TeGuiTool.addTreatmentElement(getChain(), jTree1, jList1, RBHolder.getResourceBundle());
        } else if (o.equals(jButton2)) {
            TeGuiTool.addPhase(getChain(), jTree1, RBHolder.getResourceBundle());
        } else if (o.equals(jButton3)) {
            TeGuiTool.removeSelection(getChain(), jTree1, RBHolder.getResourceBundle());
        } else if (o.equals(jButton6)) {
            initEmptyChain();
        } else if (o.equals(jButton7)) {
            TeGuiTool.addHeightInterval(getChain(), jTree1, RBHolder.getResourceBundle());
        } else if (o.equals(jButton8)) {
            TeGuiTool.moveUp(chain, jTree1);
        } else if (o.equals(jButton9)) {
            TeGuiTool.moveDown(chain, jTree1);
        } else if (o.equals(jButton10)) {
            //important: use thread and do not interrupt main thread execution
            Runnable r = () -> {
                jButton10.setEnabled(false);
                this.revalidate();
                this.repaint();                
                chain.execute(stand);
                jButton10.setEnabled(true);
                this.revalidate();
            };
            new Thread(r).start();
        }
    }

    private void initActionListener() {
        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
        jButton3.addActionListener(this);       
        jButton6.addActionListener(this);
        jButton7.addActionListener(this);
        jButton8.addActionListener(this);
        jButton9.addActionListener(this);
        jButton10.addActionListener(this);
    }

    private void initElementList() {        
        if (RBHolder.getResourceBundle() != null) {
            TeGuiTool.initElementList(jList1, RBHolder.getResourceBundle().getLocale());
        } else {
            TeGuiTool.initElementList(jList1, null);
        }
    }

    private void initEmptyChain() {
        String phaseTxt = "phase";
        String chainTxt = "chain";
        if (RBHolder.getResourceBundle() != null) {
            try {
                phaseTxt = RBHolder.getResourceBundle().getString("te_phase_txt");
                chainTxt = RBHolder.getResourceBundle().getString("te_chain_txt");
            } catch (Exception ignore) {
            }
        }
        chain = new TeChain(sdm);
        chain.addTreatmentElement(new TePhase(phaseTxt + "1"));
        chain.setName(chainTxt + "1");        
        jTree1.setModel(TeGuiTool.makeTreeModelFromChain(getChain()));        
        TeGuiTool.expandAllNodes(jTree1);
    }

    /**
     * @return the chain
     */
    public TeChain getChain() {
        return chain;
    }

    /**
     * @param chain the chain to set
     */
    public void setChain(TeChain chain) {
        this.chain = chain;
        jTree1.setModel(TeGuiTool.makeTreeModelFromChain(chain));
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setMinimumSize(new java.awt.Dimension(800, 92));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(200, 300));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("available TEs"));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("TE description"));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setOpaque(false);
        jPanel5.add(jTextArea1, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setPreferredSize(new java.awt.Dimension(200, 300));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setPreferredSize(new java.awt.Dimension(500, 332));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("treatment element chain"));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setMinimumSize(new java.awt.Dimension(250, 33));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 332));

        jTree1.setMaximumSize(new java.awt.Dimension(250, 64));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("element properties"));
        jPanel6.setMinimumSize(new java.awt.Dimension(250, 43));
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 33));
        jPanel6.setLayout(new java.awt.GridBagLayout());
        jSplitPane1.setRightComponent(jPanel6);

        jPanel2.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jButton1.setText("add TE ");
        jPanel7.add(jButton1);

        jButton2.setText("add phase");
        jPanel7.add(jButton2);

        jButton7.setText("add height interval");
        jPanel7.add(jButton7);

        jButton6.setText("clear");
        jPanel7.add(jButton6);

        jButton3.setText("remove");
        jPanel7.add(jButton3);

        jButton8.setText("up");
        jPanel7.add(jButton8);

        jButton9.setText("down");
        jPanel7.add(jButton9);

        jButton10.setText("execute");
        jPanel7.add(jButton10);

        add(jPanel7, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        String pText = "Properties";
        if (RBHolder.getResourceBundle() != null) {
            pText = RBHolder.getResourceBundle().getString("te_properties_border_text");
        }
        ((TitledBorder) jPanel6.getBorder()).setTitle(pText);
        jPanel6.removeAll();
        if (evt.getPath() != null) {
            Object o = evt.getPath().getLastPathComponent();
            if (o instanceof DefaultMutableTreeNode) {
                Object uo = ((DefaultMutableTreeNode) o).getUserObject();
                ((TitledBorder) jPanel6.getBorder()).setTitle(pText + " - " + uo.toString());
                TeGuiTool.setPropertiesPanel(jTree1, jPanel6, o, RBHolder.getResourceBundle(), stand);
            }
        }
        TeGuiTool.expandAllNodes(jTree1);
        revalidate();
        repaint();
    }//GEN-LAST:event_jTree1ValueChanged

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        jTextArea1.setText("");
        if (!evt.getValueIsAdjusting() && jList1.getSelectedIndex() >= 0) {
            TeLabelClassPair lcp = (TeLabelClassPair) jList1.getSelectedValue();
            try {
                TreatmentElement elt = (TreatmentElement) lcp.getTeClass().newInstance();
                //elt.setLocale(RBHolder.getResourceBundle().getLocale());
                jTextArea1.setText(elt.getDescription());                
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(TeSimpleTreePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jList1ValueChanged

    public JButton getButton10() {
        return jButton10;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the stand
     */
    public Stand getStand() {
        return stand;
    }

    /**
     * @param stand the stand to set
     */
    public void setStand(Stand stand) {
        this.stand = stand;
        TeGuiTool.setLastPath(stand.programDir);
        System.out.println(stand.bt);
        String txt = getChain().getAll().toString();
//        if (txt.indexOf(new Integer(stand.bt).toString()) > -1) setBT = false;
//        System.out.println(txt + " txt st.bt "+stand.bt);
        
        if (!getChain().getAll().isEmpty() && stand.bt >= 10 && stand.trule.standType < 10) {
            String file = stand.programDir + System.getProperty("file.separator")
                    + "silviculture" + System.getProperty("file.separator") + "phase_wet" + stand.bt + ".xml";
            System.out.println(file);
            initEmptyChain();
            if (new File(file).exists()) {
                Node n = TeTools.getFirstMatchingNode(file, TePhase.class);
                if (n != null) {
                    TePhase oldP = getChain().getAll().get(0);
                    oldP.parse(n);
                    jTree1.setModel(TeGuiTool.makeTreeModelFromChain(getChain()));
                    TeGuiTool.expandAllNodes(jTree1);
                }
            }
            stand.trule.standType =  stand.bt;  
        }
    }

}
