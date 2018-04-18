/** TreeGrOSS : class mymenubar is the main Menu
 *
 */
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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import treegross.base.Stand;

class MainMenubar extends JMenuBar {

    JCheckBoxMenuItem[] cmi = new JCheckBoxMenuItem[7];
    String language = "en";
    private final ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
    String country;

    public MainMenubar(ActionListener listener, ItemListener itemlistener, Locale preferredLanguage, Stand st, boolean accessInput) {
        add(createStandMenu(listener, accessInput));
        add(createEditMenu(itemlistener, listener));
        add(createViewMenu(itemlistener));
        add(createProcessMenu(listener, accessInput));
        add(createReportsMenu(listener, st, accessInput));
        add(createSettingsMenu(listener));
        add(createHelpMenu(listener));
    }

    private JMenu createHelpMenu(ActionListener listener) {
        JMenu helpMenu = new JMenu(messages.getString("MainMenubar.help"));
        JMenuItem about = new JMenuItem(messages.getString("MainMenubar.help.about"));
        about.setActionCommand("About");
        about.addActionListener(listener);
        helpMenu.add(about);
        JMenuItem license = new JMenuItem(messages.getString("MainMenubar.help.license"));
        license.setActionCommand("License");
        license.addActionListener(listener);
        helpMenu.add(license);
        JMenuItem introduction = new JMenuItem(messages.getString("MainMenubar.help.introduction"));
        introduction.setActionCommand("Introduction");
        introduction.addActionListener(listener);
        helpMenu.add(introduction);
        JMenuItem changes = new JMenuItem(messages.getString("MainMenubar.help.changes"));
        changes.setActionCommand("Changes");
        changes.addActionListener(listener);
        helpMenu.add(changes);
        JMenuItem checksum = new JMenuItem(messages.getString("MainMenubar.help.checksum"));
        checksum.setActionCommand("checksum");
        checksum.addActionListener(listener);
        helpMenu.add(checksum);
        JMenuItem textToXML = new JMenuItem(messages.getString("MainMenubar.help.textToXML"));
        textToXML.setActionCommand("Bwin62");
        textToXML.addActionListener(listener);
        helpMenu.add(textToXML);
        return helpMenu;
    }

    private JMenu createSettingsMenu(ActionListener listener) {
        JMenu settingsMenu = new JMenu(messages.getString("MainMenubar.properties"));
        JMenuItem settings = new JMenuItem(messages.getString("MainMenubar.properties.settings"));
        settings.setActionCommand("Program Settings");
        settings.addActionListener(listener);
        settingsMenu.add(settings);
        JMenuItem speciesManager = new JMenuItem(messages.getString("MainMenubar.properties.speciesManager"));
        speciesManager.setActionCommand("Species Manager");
        speciesManager.addActionListener(listener);
        settingsMenu.add(speciesManager);
        return settingsMenu;
    }

    private JMenu createReportsMenu(ActionListener listener, Stand st, boolean accessInput) {
        JMenu reportsMenu = new JMenu(messages.getString("MainMenubar.reports"));
        JMenuItem treeValues = new JMenuItem(messages.getString("MainMenubar.reports.treeValues"));
        treeValues.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.ALT_MASK));
        treeValues.setActionCommand("Tree values");
        treeValues.addActionListener(listener);
        reportsMenu.add(treeValues);
        JMenuItem standTable = new JMenuItem(messages.getString("MainMenubar.reports.standTable"));
        standTable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.ALT_MASK));
        standTable.setActionCommand("Stand table");
        standTable.addActionListener(listener);
        reportsMenu.add(standTable);
        JMenuItem structureTable = new JMenuItem(messages.getString("MainMenubar.reports.structureTable"));
        structureTable.setActionCommand("Structure table");
        structureTable.addActionListener(listener);
        reportsMenu.add(structureTable);
        JMenuItem treeTable = new JMenuItem(messages.getString("MainMenubar.reports.treeTable"));
        treeTable.setActionCommand("tree_table");
        treeTable.addActionListener(listener);
        treeTable.setVisible(false);
        reportsMenu.add(treeTable);
        if (!st.sortingModul.equalsIgnoreCase("none")) {
            JMenuItem sorting = new JMenuItem(messages.getString("MainMenubar.reports.sorting"));
            sorting.setActionCommand("Sorting");
            sorting.addActionListener(listener);
            reportsMenu.add(sorting);
        }
        if (!st.biomassModul.equalsIgnoreCase("none")) {
            JMenuItem nutrientBalance = new JMenuItem(messages.getString("MainMenubar.reports.nutrientBalance"));
            nutrientBalance.setActionCommand("NutrientBalance");
            nutrientBalance.addActionListener(listener);
            reportsMenu.add(nutrientBalance);
        }
        if (!st.deadwoodModulName.equalsIgnoreCase("none")) {
            JMenuItem deadwood = new JMenuItem(messages.getString("MainMenubar.reports.deadwood"));
            deadwood.setActionCommand("Deadwood");
            deadwood.addActionListener(listener);
            reportsMenu.add(deadwood);
        }
        if (accessInput) {
            JMenuItem roots = new JMenuItem(messages.getString("MainMenubar.reports.roots"));
            roots.setActionCommand("Roots");
            roots.addActionListener(listener);
            reportsMenu.add(roots);
        }
        if (accessInput) {
            JMenuItem rootingDialog = new JMenuItem(messages.getString("MainMenubar.reports.rootingDialog"));
            rootingDialog.setActionCommand("RootingDialog");
            rootingDialog.addActionListener(listener);
            reportsMenu.add(rootingDialog);
        }
        JMenuItem definition = new JMenuItem(messages.getString("MainMenubar.reports.definition"));
        definition.setActionCommand("Definition");
        definition.addActionListener(listener);
        reportsMenu.add(definition);
        JMenuItem speciesCode = new JMenuItem(messages.getString("MainMenubar.reports.speciesCode"));
        speciesCode.setActionCommand("species_code");
        speciesCode.addActionListener(listener);
        reportsMenu.add(speciesCode);
        return reportsMenu;
    }

    private JMenu createProcessMenu(ActionListener listener, boolean accessInput) {
        JMenu processMenu = new JMenu(messages.getString("MainMenubar.process"));
        JMenuItem grow = new JMenuItem(messages.getString("MainMenubar.process.grow"));
        grow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
        grow.setActionCommand("Grow");
        grow.addActionListener(listener);
        processMenu.add(grow);
        JMenuItem treatment = new JMenuItem(messages.getString("MainMenubar.process.treatment"));
        treatment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
        treatment.setActionCommand("Treatment");
        treatment.addActionListener(listener);
        processMenu.add(treatment);
        if (accessInput) {
            JMenuItem growBack = new JMenuItem(messages.getString("MainMenubar.process.growBack"));
            growBack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK));
            growBack.setActionCommand("grow_back");
            growBack.addActionListener(listener);
            processMenu.add(growBack);
        }
        return processMenu;
    }

    private JMenu createViewMenu(ItemListener Ilistener) {
        JMenu viewMenu = new JMenu(messages.getString("MainMenubar.view"));
        cmi[0] = new JCheckBoxMenuItem(messages.getString("MainMenubar.view.standmap"), true);
        cmi[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.CTRL_MASK));
        cmi[0].addItemListener(Ilistener);
        viewMenu.add(cmi[0]);
        cmi[1] = new JCheckBoxMenuItem(messages.getString("MainMenubar.view.parallelProjection"), true);
        cmi[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.CTRL_MASK));
        cmi[1].addItemListener(Ilistener);
        viewMenu.add(cmi[1]);
        cmi[2] = new JCheckBoxMenuItem(messages.getString("MainMenubar.view.graphics"), false);
        cmi[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, Event.CTRL_MASK));
        cmi[2].addItemListener(Ilistener);
        viewMenu.add(cmi[2]);
        cmi[5] = new JCheckBoxMenuItem(messages.getString("MainMenubar.view.standInfo"), true);
        cmi[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK));
        cmi[5].addItemListener(Ilistener);
        viewMenu.add(cmi[5]);
        cmi[4] = new JCheckBoxMenuItem(messages.getString("MainMenubar.view.treatmentInfo"), true);
        cmi[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.CTRL_MASK));
        cmi[4].addItemListener(Ilistener);
        viewMenu.add(cmi[4]);
        return viewMenu;
    }

    private JMenu createEditMenu(ItemListener Ilistener, ActionListener listener) {
        JMenu editMenu = new JMenu(messages.getString("MainMenubar.edit"));
        cmi[3] = new JCheckBoxMenuItem(messages.getString("MainMenubar.edit.standAddTrees"), false);
        cmi[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.CTRL_MASK));
        cmi[3].addItemListener(Ilistener);
        editMenu.add(cmi[3]);
        JMenuItem standData = new JMenuItem(messages.getString("MainMenubar.edit.standData"));
        standData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
        standData.setActionCommand("edit");
        standData.addActionListener(listener);
        editMenu.add(standData);
        return editMenu;
    }

    private JMenu createStandMenu(ActionListener listener, boolean accessInput) {
        JMenu standMenu = new JMenu(messages.getString("MainMenubar.stand"));
        JMenuItem newStand = new JMenuItem(messages.getString("MainMenubar.stand.new"));
        newStand.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        newStand.setActionCommand("new");
        newStand.addActionListener(listener);
        standMenu.add(newStand);
        JMenu openMenu = new JMenu(messages.getString("MainMenubar.stand.open"));
        JMenuItem openTreeGroSSFile = new JMenuItem(messages.getString("MainMenubar.stand.open.xml"));
        openTreeGroSSFile.setActionCommand("openTreegrossXML");
        openTreeGroSSFile.addActionListener(listener);
        openMenu.add(openTreeGroSSFile);
        //  Open from Access-Database
        if (accessInput) {
            JMenuItem openAccessDatabase = new JMenuItem(messages.getString("MainMenubar.stand.open.access"));
            openAccessDatabase.setActionCommand("openAccess");
            openAccessDatabase.addActionListener(listener);
            openMenu.add(openAccessDatabase);
        }
        standMenu.add(openMenu);
        JMenu saveMenu = new JMenu(messages.getString("MainMenubar.stand.save"));
        JMenuItem saveTreeGroSS = new JMenuItem(messages.getString("MainMenubar.stand.save.xml"));
        saveTreeGroSS.setActionCommand("saveTreegrossXML");
        saveTreeGroSS.addActionListener(listener);
        saveMenu.add(saveTreeGroSS);
        standMenu.add(saveMenu);
        JMenuItem sqlLite = new JMenuItem(messages.getString("MainMenubar.stand.sqlite"));
        sqlLite.setActionCommand("SQlite");
        sqlLite.addActionListener(listener);
        standMenu.add(sqlLite);
        JMenuItem exit = new JMenuItem(messages.getString("MainMenubar.stand.exit"));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
        exit.setActionCommand("exit");
        exit.addActionListener(listener);
        standMenu.add(exit);
        return standMenu;
    }
}
