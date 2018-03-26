/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.gui;

import de.nwfva.silviculture.base.TeChain;
import de.nwfva.silviculture.base.TeHeightInterval;
import de.nwfva.silviculture.base.TePhase;
import de.nwfva.silviculture.base.TePhaseCondition;
import de.nwfva.silviculture.base.TeTools;
import de.nwfva.silviculture.base.TreatmentElement;
import de.nwfva.silviculture.base.TreatmentElementParameter;
import de.nwfva.silviculture.base.XmlElement;
import de.nwfva.silviculture.examplepathbuilder.ExamplePathBuilder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 *
 * Gui Funktionalität strikt vom Modell trennen also nicht in treegross
 * übernehmen, sondern in Frontend-Programme
 *
 */
public class TeGuiTool {

    private static String lastPath = null;

    public static DefaultTreeModel makeTreeModelFromChain(TeChain chain) {
        MutableTreeNode root = new DefaultMutableTreeNode(chain);
        ArrayList<TePhase> phases = chain.getAll();
        ArrayList<TeHeightInterval> intervals;
        ArrayList<TreatmentElement> tes;
        int j, i, a;
        i = 0;
        for (TePhase p : phases) {
            j = 0;
            MutableTreeNode pn = new DefaultMutableTreeNode(p);
            intervals = p.getAll();
            for (TeHeightInterval e : intervals) {
                DefaultMutableTreeNode in = new DefaultMutableTreeNode(e);
                tes = e.getAll();
                a = 0;
                for (TreatmentElement t : tes) {
                    in.insert(new DefaultMutableTreeNode(t), a);
                    a++;
                }
                pn.insert(in, j);
                j++;
            }
            root.insert(pn, i);
            i++;
        }
        DefaultTreeModel m = new DefaultTreeModel(root);
        return m;
    }

    public static void addPhase(TeChain chain, JTree tree, ResourceBundle rb) {
        String phaseTxt = "phase";
        if (rb != null) {
            try {
                phaseTxt = rb.getString("te_phase_txt");
            } catch (Exception ignore) {
            }
        }
        TePhase tep = new TePhase();

        String pName = phaseTxt + (chain.getAll().size() + 1);

        tep.setName(pName);
        chain.addTreatmentElement(tep);
        tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
        TeGuiTool.expandAllNodes(tree);

        TreePath sel = tree.getNextMatch(pName, 0, Position.Bias.Forward);

        tree.setSelectionPath(sel);
        tree.grabFocus();
    }

    public static void removeSelection(TeChain chain, JTree tree, ResourceBundle rb) {
        String hintTxt = "hint";
        String notChainTxt = "This node can not be removed.";
        String selectNodeTxt = "Please select a node to remove.";
        if (rb != null) {
            try {
                hintTxt = rb.getString("te_hint_txt");
                notChainTxt = rb.getString("te_can_not_delete_alert");
                selectNodeTxt = rb.getString("te_select_node_alert");
            } catch (Exception ignore) {
            }
        }
        if (tree.getSelectionCount() > 0) {
            Object n = tree.getSelectionPath().getLastPathComponent();
            if (n instanceof DefaultMutableTreeNode) {
                Object uo = ((DefaultMutableTreeNode) n).getUserObject();
                if (uo instanceof TePhase) {
                    chain.remove((TePhase) uo);
                    tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                    TeGuiTool.expandAllNodes(tree);
                } else if (uo instanceof TeHeightInterval) {
                    DefaultMutableTreeNode pn = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) n).getParent();
                    TePhase tp = (TePhase) pn.getUserObject();
                    tp.remove((TeHeightInterval) uo);
                    tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                    TeGuiTool.expandAllNodes(tree);
                } else if (uo instanceof TreatmentElement) {
                    DefaultMutableTreeNode pn = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) n).getParent();
                    TeHeightInterval ptr = (TeHeightInterval) pn.getUserObject();
                    ptr.remove((TreatmentElement) uo);
                    tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                    TeGuiTool.expandAllNodes(tree);
                } else {
                    JOptionPane.showMessageDialog(null, notChainTxt, hintTxt, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, selectNodeTxt, hintTxt, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void addHeightInterval(TeChain chain, JTree tree, ResourceBundle rb) {
        String hintTxt = "hint";
        String selectPhaseTxt = "Please select a phase node.";
        String intervalTxt = "height interval";
        if (rb != null) {
            try {
                hintTxt = rb.getString("te_hint_txt");
                intervalTxt = rb.getString("te_interval_txt");
                selectPhaseTxt = rb.getString("te_select_phase_alert");
            } catch (Exception ignore) {
            }
        }
        if (tree.getSelectionCount() > 0) {
            TreePath sel = tree.getSelectionPath();
            Object n = sel.getLastPathComponent();
            if (n instanceof DefaultMutableTreeNode) {
                Object uo = ((DefaultMutableTreeNode) n).getUserObject();
                if (uo instanceof TePhase) {
                    TePhase tep = (TePhase) uo;
                    TeHeightInterval tei = new TeHeightInterval();
                    tei.setName(intervalTxt + (tep.getAll().size() + 1));
                    tep.add(tei);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(tei);
                    ((DefaultMutableTreeNode) n).add(newNode);
                    ((DefaultTreeModel) tree.getModel()).reload();
                    TeGuiTool.expandAllNodes(tree);
                    tree.setSelectionPath(sel.pathByAddingChild(newNode));
                    tree.grabFocus();
                } else {
                    JOptionPane.showMessageDialog(null, selectPhaseTxt, hintTxt, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, selectPhaseTxt, hintTxt, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void addTreatmentElement(TeChain chain, JTree tree, JList fromList, ResourceBundle rb) {
        String hintTxt = "hint";
        String selectIntervalTxt = "Please select an interval node and a treatment element.";
        if (rb != null) {
            try {
                hintTxt = rb.getString("te_hint_txt");
                selectIntervalTxt = rb.getString("te_select_interval_alert");
            } catch (Exception ignore) {
            }
        }
        if (tree.getSelectionCount() > 0 && fromList.getSelectedIndex() >= 0) {
            TreePath sel = tree.getSelectionPath();
            Object n = sel.getLastPathComponent();
            if (n instanceof DefaultMutableTreeNode) {
                Object uo = ((DefaultMutableTreeNode) n).getUserObject();
                if (uo instanceof TeHeightInterval) {
                    TeHeightInterval tei = (TeHeightInterval) uo;
                    TeLabelClassPair lcp = (TeLabelClassPair) fromList.getSelectedValue();
                    TreatmentElement tel;
                    try {
                        tel = (TreatmentElement) lcp.getTeClass().newInstance();
                        /*if (rb != null) {
                            tel.setLocale(rb.getLocale());
                        }*/
                    } catch (InstantiationException | IllegalAccessException ex) {
                        Logger.getLogger(TeSimpleTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                    tei.addTreatmentElement(tel);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(tel);
                    ((DefaultMutableTreeNode) n).add(newNode);
                    ((DefaultTreeModel) tree.getModel()).reload();
                    TeGuiTool.expandAllNodes(tree);
                    tree.setSelectionPath(sel.pathByAddingChild(newNode));
                    tree.grabFocus();
                } else {
                    JOptionPane.showMessageDialog(null, selectIntervalTxt, hintTxt, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, selectIntervalTxt, hintTxt, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void moveUp(TeChain chain, JTree tree) {
        if (tree.getSelectionCount() > 0) {
            boolean doUpdate = false;
            TreePath sel = tree.getSelectionPath();
            Object n = sel.getLastPathComponent();
            String searchName = null;
            if (n instanceof DefaultMutableTreeNode) {
                Object uo = ((DefaultMutableTreeNode) n).getUserObject();
                if (uo instanceof TePhase) {
                    TePhase p = (TePhase) uo;
                    chain.moveUp(p);
                    doUpdate = true;
                    searchName = p.getName();
                } else if (uo instanceof TeHeightInterval) {
                    TeHeightInterval hi = (TeHeightInterval) uo;
                    TePhase pp = (TePhase) ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) n).getParent()).getUserObject();
                    pp.moveUp(hi);
                    doUpdate = true;
                    searchName = hi.getName();
                } else if (uo instanceof TreatmentElement) {
                    TreatmentElement te = (TreatmentElement) uo;
                    TeHeightInterval pp = (TeHeightInterval) ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) n).getParent()).getUserObject();
                    pp.moveUp(te);
                    doUpdate = true;
                    searchName = te.getName();
                }
            }
            if (doUpdate) {
                tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                TeGuiTool.expandAllNodes(tree);
                sel = tree.getNextMatch(searchName, 0, Position.Bias.Forward);
                tree.setSelectionPath(sel);
                tree.grabFocus();
            }
        }
    }

    public static void moveDown(TeChain chain, JTree tree) {
        if (tree.getSelectionCount() > 0) {
            boolean doUpdate = false;
            TreePath sel = tree.getSelectionPath();
            Object n = sel.getLastPathComponent();
            String searchName = null;
            if (n instanceof DefaultMutableTreeNode) {
                Object uo = ((DefaultMutableTreeNode) n).getUserObject();
                if (uo instanceof TePhase) {
                    TePhase p = (TePhase) uo;
                    chain.moveDown(p);
                    doUpdate = true;
                    searchName = p.getName();
                } else if (uo instanceof TeHeightInterval) {
                    TeHeightInterval hi = (TeHeightInterval) uo;
                    TePhase pp = (TePhase) ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) n).getParent()).getUserObject();
                    pp.moveDown(hi);
                    doUpdate = true;
                    searchName = hi.getName();
                } else if (uo instanceof TreatmentElement) {
                    TreatmentElement te = (TreatmentElement) uo;
                    TeHeightInterval pp = (TeHeightInterval) ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) n).getParent()).getUserObject();
                    pp.moveDown(te);
                    doUpdate = true;
                    searchName = te.getName();
                }
            }
            if (doUpdate) {
                tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                TeGuiTool.expandAllNodes(tree);
                sel = tree.getNextMatch(searchName, 0, Position.Bias.Forward);
                tree.setSelectionPath(sel);
                tree.grabFocus();
            }
        }
    }

    /**
     * Adds a new <code>DefaultListModel</code> to a <code>JList</code>. The
     * model elements are of type <code>LabelClassPair</code>. The list of found
     * <code>TreatmentElement</code> implementations is sorted by group and
     * label.
     *
     * @param list the list to add new model
     * @param l the local to use for label and description text
     */
    public static void initElementList(JList list, Locale l) {
        DefaultListModel m = new DefaultListModel();
        List<Class<?>> c = TeTools.getAllTreatmentElements();
        ArrayList<TeLabelClassPair> lps = new ArrayList();
        c.stream().forEach((sc) -> {
            try {
                TreatmentElement ni = (TreatmentElement) sc.newInstance();
//                ni.setLocale(l);
                lps.add(new TeLabelClassPair(ni.getLabel(), sc, ni.getGroup()));
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ExamplePathBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Collections.sort(lps);
        lps.stream().forEach((lp) -> {
            m.addElement(lp);
        });
        list.setModel(m);
    }

    public static void setPropertiesPanel(JTree tree, JPanel panel, Object node, ResourceBundle rb, Stand st) {
        if (panel == null || node == null) {
            return;
        }
        String nameTxt = "name";
        String applyTxt = "apply";
        String startHeightTxt = "start height";
        String endHeightTxt = "end height";
        String errorTxt1 = "error";
        if (rb != null) {
            try {
                nameTxt = rb.getString("te_name_txt");
                applyTxt = rb.getString("te_apply_txt");
                startHeightTxt = rb.getString("te_start_height_txt");
                endHeightTxt = rb.getString("te_end_height_txt");
                errorTxt1 = rb.getString("te_error_txt");
            } catch (Exception ignore) {
            }
        }
        final String errorTxt = errorTxt1;
        Insets insets = new Insets(4, 4, 4, 4);
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        Object o = ((DefaultMutableTreeNode) node).getUserObject();
        if (o instanceof TeChain) {
            TeChain c = (TeChain) o;
            panel.add(makeStdHandlingPanel(node, o, tree, panel, rb, st), BorderLayout.NORTH);
            JPanel ppJPanelParams = new JPanel(new GridBagLayout());
            JPanel ppJPanelButtons = new JPanel(new FlowLayout());
            JButton ppJButtonOk = new JButton(applyTxt);
            ppJPanelButtons.add(ppJButtonOk);
            GridBagConstraints cbcL = new java.awt.GridBagConstraints();
            cbcL.fill = GridBagConstraints.HORIZONTAL;
            cbcL.gridx = 0;
            cbcL.gridy = 0;
            cbcL.insets = insets;
            ppJPanelParams.add(new JLabel(nameTxt), cbcL);
            GridBagConstraints cbcT = new java.awt.GridBagConstraints();
            cbcT.fill = GridBagConstraints.HORIZONTAL;
            cbcT.gridx = 1;
            cbcT.gridy = 0;
            cbcT.insets = insets;
            JTextField ppJTextAreaName = new JTextField(c.getName());
            int height = ppJTextAreaName.getPreferredSize().height;
            ppJTextAreaName.setPreferredSize(new Dimension(200, height));
            ppJPanelParams.add(ppJTextAreaName, cbcT);
            GridBagConstraints cbcB = new java.awt.GridBagConstraints();
            cbcB.fill = GridBagConstraints.HORIZONTAL;
            cbcB.gridx = 0;
            cbcB.gridy = 1;
            cbcB.insets = insets;
            cbcB.gridwidth = 2;
            ppJPanelParams.add(ppJPanelButtons, cbcB);
            panel.add(ppJPanelParams, BorderLayout.CENTER);

            ppJButtonOk.addActionListener((ActionEvent e) -> {
                c.setName(ppJTextAreaName.getText());
                ((DefaultTreeModel) tree.getModel()).nodeChanged((DefaultMutableTreeNode) node);
                SwingUtilities.invokeLater(() -> {
                    tree.grabFocus();
                });
            });
            //panel.revalidate();
            //panel.repaint();            
        } else if (o instanceof TePhase) {
            TePhase p = (TePhase) o;
            panel.add(makeStdHandlingPanel(node, p, tree, panel, rb, st), BorderLayout.NORTH);
            JPanel ppJPanelParams = new JPanel(new GridBagLayout());
            JPanel ppJPanelButtons = new JPanel(new FlowLayout());
            JButton ppJButtonOk = new JButton(applyTxt);
            ppJPanelButtons.add(ppJButtonOk);
            GridBagConstraints cbcL = new java.awt.GridBagConstraints();
            cbcL.fill = GridBagConstraints.HORIZONTAL;
            cbcL.gridx = 0;
            cbcL.gridy = 0;
            cbcL.insets = insets;
            ppJPanelParams.add(new JLabel(nameTxt), cbcL);
            GridBagConstraints cbcT = new java.awt.GridBagConstraints();
            cbcT.fill = GridBagConstraints.HORIZONTAL;
            cbcT.gridx = 1;
            cbcT.gridy = 0;
            cbcT.insets = insets;
            JTextField ppJTextAreaName = new JTextField(p.getName());
            int height = ppJTextAreaName.getPreferredSize().height;
            ppJTextAreaName.setPreferredSize(new Dimension(200, height));
            ppJPanelParams.add(ppJTextAreaName, cbcT);

            ArrayList<TePhaseCondition> allc = p.getAllConditions();
            ArrayList<TreatmentElementParameter> allp;
            int defaultHeight, i;
            i = 0;
            int size = 0;
            size = allc.stream().map((c) -> c.getRequiredParameters().size()).reduce(size, Integer::sum);
            JLabel[] lables = new JLabel[size];
            JComponent[] jcs = new JComponent[size];
            for (TePhaseCondition c : allc) {
                allp = c.getRequiredParameters();
                for (TreatmentElementParameter ap : allp) {
                    lables[i] = new JLabel("<html><b>" + ap.getLabel() + "</b><br>" + ap.getDescription() + "</html>");
                    if (ap.getValue().getClass() == Boolean.class) {
                        JCheckBox cb = new JCheckBox();
                        cb.setSelected((Boolean) ap.getValue());
                        jcs[i] = cb;
                    } else if (ap.getConstraintType() == TreatmentElementParameter.CONSTRAINT_TYPE_VALUES) {
                        JComboBox cb = new JComboBox();
                        DefaultComboBoxModel m = new DefaultComboBoxModel();
                        Object selO = null;
                        for (Object so : ap.getConstraintValues()) {
                            if (so.equals(ap.getValue())) {
                                selO = so;
                            }
                            m.addElement(so.toString());
                        }
                        m.setSelectedItem(selO);
                        cb.setModel(m);
                        jcs[i] = cb;
                    } else {
                        jcs[i] = new JTextField(ap.getValue().toString());
                    }
                    jcs[i].getMinimumSize().width = 100;
                    defaultHeight = jcs[i].getSize().height;
                    jcs[i].setMinimumSize(new Dimension(100, defaultHeight));
                    GridBagConstraints cbcLL = new java.awt.GridBagConstraints();
                    cbcLL.fill = GridBagConstraints.HORIZONTAL;
                    cbcLL.gridx = 0;
                    cbcLL.gridy = i + 1;
                    cbcLL.insets = insets;
                    GridBagConstraints cbcTL = new java.awt.GridBagConstraints();
                    cbcTL.gridx = 1;
                    cbcTL.gridy = i + 1;
                    cbcTL.insets = insets;
                    cbcTL.fill = GridBagConstraints.HORIZONTAL;
                    ppJPanelParams.add(lables[i], cbcLL);
                    ppJPanelParams.add(jcs[i], cbcTL);
                    i++;
                }
            }

            GridBagConstraints cbcB = new java.awt.GridBagConstraints();
            cbcB.fill = GridBagConstraints.HORIZONTAL;
            cbcB.gridx = 0;
            cbcB.gridy = i + 1;
            cbcB.insets = insets;
            cbcB.gridwidth = 2;
            ppJPanelParams.add(ppJPanelButtons, cbcB);
            panel.add(ppJPanelParams, BorderLayout.CENTER);

            ppJButtonOk.addActionListener((ActionEvent e) -> {
                //parse params via xml dom node because every TE has method parse(Node n)               
                Document d;
                try {
                    d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(TeGuiTool.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
                Element root = d.createElement("allc");
                int j = 0;
                for (TePhaseCondition c : allc) {
                    Element cond = d.createElement(TePhaseCondition.class.getSimpleName());
                    cond.setAttribute("class", c.getClass().getCanonicalName());
                    for (TreatmentElementParameter param : c.getRequiredParameters()) {
                        Element item = d.createElement(TreatmentElementParameter.class.getSimpleName());
                        item.setAttribute("name", param.getName());
                        if (param.getValue().getClass() == Boolean.class) {
                            item.setAttribute("value", Boolean.toString(((JCheckBox) jcs[j]).isSelected()));
                        } else if (param.getConstraintType() == TreatmentElementParameter.CONSTRAINT_TYPE_VALUES) {
                            item.setAttribute("value", ((JComboBox) jcs[j]).getSelectedItem().toString());
                        } else {
                            item.setAttribute("value", ((JTextField) jcs[j]).getText());
                        }
                        System.out.println(item.getAttribute("name") + " : " + item.getAttribute("value"));
                        cond.appendChild(item);
                        j++;
                    }
                    root.appendChild(cond);
                }
                d.appendChild(root);
                try {
                    p.parseConditions(root);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, nfe.getLocalizedMessage(), errorTxt, JOptionPane.ERROR_MESSAGE);
                }
                p.setName(ppJTextAreaName.getText());
                ((DefaultTreeModel) tree.getModel()).nodeChanged((DefaultMutableTreeNode) node);
                SwingUtilities.invokeLater(() -> {
                    tree.grabFocus();
                });
            });
        } else if (o instanceof TeHeightInterval) {
            TeHeightInterval p = (TeHeightInterval) o;
            panel.add(makeStdHandlingPanel(node, p, tree, panel, rb, st), BorderLayout.NORTH);
            JPanel ppJPanelParams = new JPanel(new GridBagLayout());
            JPanel ppJPanelButtons = new JPanel(new FlowLayout());
            JButton ppJButtonOk = new JButton(applyTxt);
            ppJPanelButtons.add(ppJButtonOk);
            // name
            GridBagConstraints cbcL = new java.awt.GridBagConstraints();
            cbcL.fill = GridBagConstraints.HORIZONTAL;
            cbcL.gridx = 0;
            cbcL.gridy = 0;
            cbcL.insets = insets;
            ppJPanelParams.add(new JLabel(nameTxt), cbcL);
            GridBagConstraints cbcT = new java.awt.GridBagConstraints();
            cbcT.fill = GridBagConstraints.HORIZONTAL;
            cbcT.gridx = 1;
            cbcT.gridy = 0;
            cbcT.insets = insets;
            JTextField ppJTextAreaName = new JTextField(p.getName());
            int height = ppJTextAreaName.getPreferredSize().height;
            ppJTextAreaName.setPreferredSize(new Dimension(200, height));
            ppJPanelParams.add(ppJTextAreaName, cbcT);
            //start height
            GridBagConstraints cbcLsH = new java.awt.GridBagConstraints();
            cbcLsH.fill = GridBagConstraints.HORIZONTAL;
            cbcLsH.gridx = 0;
            cbcLsH.gridy = 1;
            cbcLsH.insets = insets;
            ppJPanelParams.add(new JLabel(startHeightTxt), cbcLsH);
            GridBagConstraints cbcTsH = new java.awt.GridBagConstraints();
            cbcTsH.fill = GridBagConstraints.HORIZONTAL;
            cbcTsH.gridx = 1;
            cbcTsH.gridy = 1;
            cbcTsH.insets = insets;
            JTextField ppJTextAreasH = new JTextField(Double.toString(p.getStartHeight()));
            ppJPanelParams.add(ppJTextAreasH, cbcTsH);
            //end height
            GridBagConstraints cbcLeH = new java.awt.GridBagConstraints();
            cbcLeH.fill = GridBagConstraints.HORIZONTAL;
            cbcLeH.gridx = 0;
            cbcLeH.gridy = 2;
            cbcLeH.insets = insets;
            ppJPanelParams.add(new JLabel(endHeightTxt), cbcLeH);
            GridBagConstraints cbcTeH = new java.awt.GridBagConstraints();
            cbcTeH.fill = GridBagConstraints.HORIZONTAL;
            cbcTeH.gridx = 1;
            cbcTeH.gridy = 2;
            cbcTeH.insets = insets;
            JTextField ppJTextAreaeH = new JTextField(Double.toString(p.getEndHeight()));
            ppJPanelParams.add(ppJTextAreaeH, cbcTeH);
            //button
            GridBagConstraints cbcB = new java.awt.GridBagConstraints();
            cbcB.fill = GridBagConstraints.HORIZONTAL;
            cbcB.gridx = 0;
            cbcB.gridy = 3;
            cbcB.insets = insets;
            cbcB.gridwidth = 2;
            ppJPanelParams.add(ppJPanelButtons, cbcB);
            panel.add(ppJPanelParams, BorderLayout.CENTER);

            ppJButtonOk.addActionListener((ActionEvent e) -> {
                p.setName(ppJTextAreaName.getText());
                try {
                    p.setStartHeight(Double.parseDouble(ppJTextAreasH.getText()));
                    p.setEndHeight(Double.parseDouble(ppJTextAreaeH.getText()));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, nfe.getLocalizedMessage(), errorTxt, JOptionPane.ERROR_MESSAGE);
                }
                ((DefaultTreeModel) tree.getModel()).nodeChanged((DefaultMutableTreeNode) node);
                SwingUtilities.invokeLater(() -> {
                    tree.grabFocus();
                });
            });
        } else if (o instanceof TreatmentElement) {
            TreatmentElement e = (TreatmentElement) o;
//            e.setLocale(rb.getLocale());
            panel.add(makeStdHandlingPanel(node, e, tree, panel, rb, st), BorderLayout.NORTH);
            ArrayList<TreatmentElementParameter> params = e.getRequiredParameters();
            JPanel ppJPanelParams = new JPanel(new GridBagLayout());
            JPanel ppJPanelButtons = new JPanel();
            ppJPanelButtons.setLayout(new FlowLayout());
            JButton ppJButtonOk = new JButton(applyTxt);
            ppJPanelButtons.add(ppJButtonOk);
            JLabel[] lables = new JLabel[params.size()];

            JComponent[] jcs = new JComponent[params.size()];

            int defaultHeight;
            int i = 0;
            for (TreatmentElementParameter p : params) {
                lables[i] = new JLabel("<html><b>" + p.getLabel() + "</b><br>" + p.getDescription() + "</html>");
                if (p.getValue().getClass() == Boolean.class) {
                    JCheckBox cb = new JCheckBox();
                    cb.setSelected((Boolean) p.getValue());
                    jcs[i] = cb;
                } else if (p.getConstraintType() == TreatmentElementParameter.CONSTRAINT_TYPE_VALUES) {
                    JComboBox cb = new JComboBox();
                    DefaultComboBoxModel m = new DefaultComboBoxModel();
                    Object selO = null;
                    for (Object so : p.getConstraintValues()) {
                        if (so.equals(p.getValue())) {
                            selO = so;
                        }
                        m.addElement(so.toString());
                    }
                    m.setSelectedItem(selO);
                    cb.setModel(m);
                    jcs[i] = cb;
                } else {
                    jcs[i] = new JTextField(p.getValue().toString());
                }
                jcs[i].getMinimumSize().width = 100;
                defaultHeight = jcs[i].getSize().height;
                jcs[i].setMinimumSize(new Dimension(100, defaultHeight));
                GridBagConstraints cbcL = new java.awt.GridBagConstraints();
                cbcL.fill = GridBagConstraints.HORIZONTAL;
                cbcL.gridx = 0;
                cbcL.gridy = i;
                cbcL.insets = insets;
                GridBagConstraints cbcT = new java.awt.GridBagConstraints();
                cbcT.gridx = 1;
                cbcT.gridy = i;
                cbcT.insets = insets;
                cbcT.fill = GridBagConstraints.HORIZONTAL;
                ppJPanelParams.add(lables[i], cbcL);
                ppJPanelParams.add(jcs[i], cbcT);
                i++;
            }
            GridBagConstraints cbcB = new java.awt.GridBagConstraints();
            cbcB.fill = GridBagConstraints.HORIZONTAL;
            cbcB.gridx = 0;
            cbcB.gridy = i;
            cbcB.insets = insets;
            cbcB.gridwidth = 2;
            ppJPanelParams.add(ppJPanelButtons, cbcB);
            panel.add(ppJPanelParams, BorderLayout.CENTER);
            ppJButtonOk.addActionListener((ActionEvent ae) -> {
                //parse params via xml dom node because every TE has method parse(Node n)
                TreatmentElementParameter p;
                Document d;
                try {
                    d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(TeGuiTool.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
                Element root = d.createElement(TreatmentElement.class.getSimpleName());
                int j = 0;
                for (TreatmentElementParameter param : params) {
                    p = param;
                    Element item = d.createElement(TreatmentElementParameter.class.getSimpleName());
                    item.setAttribute("name", p.getName());
                    if (p.getValue().getClass() == Boolean.class) {
                        item.setAttribute("value", Boolean.toString(((JCheckBox) jcs[j]).isSelected()));
                    } else if (p.getConstraintType() == TreatmentElementParameter.CONSTRAINT_TYPE_VALUES) {
                        item.setAttribute("value", ((JComboBox) jcs[j]).getSelectedItem().toString());
                    } else {
                        item.setAttribute("value", ((JTextField) jcs[j]).getText());
                    }
                    root.appendChild(item);
                    j++;
                }
                d.appendChild(root);
                try {
                    e.parse(root);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, nfe.getLocalizedMessage(), errorTxt, JOptionPane.ERROR_MESSAGE);
                }
                ((DefaultTreeModel) tree.getModel()).nodeChanged((DefaultMutableTreeNode) node);

                SwingUtilities.invokeLater(() -> {
                    tree.grabFocus();
                });

            });
        }
    }

    private static JPanel makeStdHandlingPanel(Object treeNode, Object elt, JTree tree, JPanel panel, ResourceBundle rb, Stand st) {
        JPanel ppJPanelButtonsAbove = new JPanel(new FlowLayout());
        //default button
        if (elt instanceof TreatmentElement) {
            JButton ppJButtonDef = new JButton("defaults");
            try {
                ppJButtonDef.setText(rb.getString("te_default_btn"));
            } catch (Exception ignore) {
            }
            ppJPanelButtonsAbove.add(ppJButtonDef);
            ppJButtonDef.addActionListener((ActionEvent e) -> {
                TreatmentElement te = (TreatmentElement) elt;
                DefaultMutableTreeNode ntn = (DefaultMutableTreeNode) treeNode;
                TeChain chain = (TeChain) ((DefaultMutableTreeNode) ntn.getRoot()).getUserObject();
                te.setDefaults(chain.getSdm(), st);
                System.out.println(te.getName() + " " + chain.getSdm().getActualURL());
                tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                TeGuiTool.expandAllNodes(tree);

            });
        }
        // loading button
        JButton ppJButtonLoad = new JButton("load");
        try {
            ppJButtonLoad.setText(rb.getString("te_load_btn"));
        } catch (Exception ignore) {
        }
        ppJPanelButtonsAbove.add(ppJButtonLoad);
        if (elt instanceof TeChain) {
            ppJButtonLoad.addActionListener((ActionEvent e) -> {
                TeChain chain = (TeChain) elt;
                TeGuiTool.showLoadChainDialog(panel, chain);
                tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                TeGuiTool.expandAllNodes(tree);
            });
        } else {
            ppJButtonLoad.addActionListener((ActionEvent e) -> {
                String file = showLoadXmlDialog(panel);
                if (file != null) {
                    Node n;
                    if (elt instanceof TreatmentElement) {
                        n = TeTools.getFirstMatchingNode(file, TreatmentElement.class);
                        if (n != null) {
                            String newClassName = n.getAttributes().getNamedItem("class").getNodeValue();
                            if (elt.getClass().getName().equals(newClassName)) {
                                ((TreatmentElement) elt).parse(n);
                            } else {
                                showAlertClassNotFound(elt.getClass().getName());
                            }
                        } else {
                            showAlertClassNotFound(TreatmentElement.class.getName());
                        }
                    } else if (elt instanceof TePhase) {
                        n = TeTools.getFirstMatchingNode(file, TePhase.class);
                        if (n != null) {
                            TePhase oldP = (TePhase) elt;
                            oldP.parse(n);
                        } else {
                            showAlertClassNotFound(TePhase.class.getName());
                        }
                    } else if (elt instanceof TeHeightInterval) {
                        n = TeTools.getFirstMatchingNode(file, TeHeightInterval.class);
                        if (n != null) {
                            TeHeightInterval oldI = (TeHeightInterval) elt;
                            oldI.parse(n);
                        } else {
                            showAlertClassNotFound(TeHeightInterval.class.getName());
                        }
                    }
                    DefaultMutableTreeNode ntn = (DefaultMutableTreeNode) treeNode;
                    TeChain chain = (TeChain) ((DefaultMutableTreeNode) ntn.getRoot()).getUserObject();
                    tree.setModel(TeGuiTool.makeTreeModelFromChain(chain));
                    TeGuiTool.expandAllNodes(tree);
                }
            });
        }

        // saving button
        JButton ppJButtonSave = new JButton("save");
        try {
            ppJButtonSave.setText(rb.getString("te_save_btn"));
        } catch (Exception ignore) {
        }
        ppJPanelButtonsAbove.add(ppJButtonSave);
        ppJButtonSave.addActionListener((ActionEvent e) -> {
            System.out.println("Save " + elt);
            TeGuiTool.showSaveChainDialog(panel, elt, rb);
        });

        return ppJPanelButtonsAbove;
    }

    private static void showAlertClassNotFound(String className) {
        JOptionPane.showMessageDialog(null, "No matching node found for class " + className);
    }

    public static void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    public static void showLoadChainDialog(JPanel parent, TeChain chain) {
        String path = showLoadXmlDialog(parent);
        if (path != null) {
            chain.clear();
            chain.parse(path);
        }
    }

    public static String showLoadXmlDialog(JPanel parent) {
        JFileChooser fc = new JFileChooser();
        if (lastPath == null) {
            fc.setCurrentDirectory(new File(""));
        } else {
            fc.setCurrentDirectory(new File(lastPath));
        }
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
            }

            @Override
            public String getDescription() {
                return "XML";
            }
        });
        int returnVal = fc.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            lastPath = file.getParent();
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    public static void showSaveChainDialog(JPanel parent, Object objectToSave, ResourceBundle rb) {
        JFileChooser fc = new JFileChooser();
        if (lastPath == null) {
            fc.setCurrentDirectory(new File(""));
        } else {
            fc.setCurrentDirectory(new File(lastPath));
        }
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
            }

            @Override
            public String getDescription() {
                return "XML";
            }
        });
        int returnVal = fc.showSaveDialog(parent);
        int c = JOptionPane.NO_OPTION;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file.exists()) {
                String alert = "Die Datei existiert bereits! überschreiben?";
                if (rb != null) {
                    alert = rb.getString("te_file_exists_alert");
                }
                String title = "save";
                if (rb != null) {
                    title = rb.getString("te_save_btn");
                }
                c = JOptionPane.showConfirmDialog(parent, alert, title, JOptionPane.YES_NO_OPTION);
            }
            if (!file.exists() || c == JOptionPane.YES_OPTION) {
                String path = file.getAbsolutePath();
                if (!path.toLowerCase().endsWith(".xml")) {
                    path = path + ".xml";
                }
                try {
                    if (objectToSave instanceof TeChain) {
                        TeTools.writeToFile(path, ((TeChain) objectToSave).toXML());
                    } else if (objectToSave instanceof XmlElement) {
                        TeTools.writeToFile(path, ((XmlElement) objectToSave).toXML());
                    } else if (objectToSave instanceof TreatmentElement) {
                        TreatmentElement te = (TreatmentElement) objectToSave;
                        TeTools.writeToFile(path, TeTools.toXML(te));
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TeGuiTool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void setLastPath(String path){
        if (path != null) lastPath = path+System.getProperty("file.separator")+"silviculture";
    }
}
