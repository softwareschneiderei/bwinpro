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

    public MainMenubar(ActionListener listener, ItemListener Ilistener, Locale preferredLanguage, Stand st, boolean accessInput) {
        add(createStandMenu(listener, accessInput));
        add(createEditMenu(Ilistener, listener));
        add(createViewMenu(Ilistener));
        add(createProcessMenu(listener, accessInput));

//1.5 save HTML-Reports                
        JMenu reportMenu = new JMenu(messages.getString("Reports"));
//1.5.2 Tree values                
        JMenuItem treatment = new JMenuItem(messages.getString("Tree_values"));
        treatment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.ALT_MASK));
        treatment.setActionCommand("Tree values");
        treatment.addActionListener(listener);
        reportMenu.add(treatment);
//1.5.3 Stand table                
        treatment = new JMenuItem(messages.getString("Stand_table"));
        treatment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.ALT_MASK));
        treatment.setActionCommand("Stand table");
        treatment.addActionListener(listener);
        reportMenu.add(treatment);
//1.5.4 Structure table                
        treatment = new JMenuItem(messages.getString("Structure_table"));
        treatment.setActionCommand("Structure table");
        treatment.addActionListener(listener);
        reportMenu.add(treatment);
//1.5.5 Structure table                
        treatment = new JMenuItem("Baumtabelle");
        treatment.setActionCommand("tree_table");
        treatment.addActionListener(listener);
        treatment.setVisible(false);
        reportMenu.add(treatment);
//4.3 Sorting  
        if (st.sortingModul.equalsIgnoreCase("none") == false) {
            treatment = new JMenuItem(messages.getString("Sorting"));
            treatment.setActionCommand("Sorting");
            treatment.addActionListener(listener);
            reportMenu.add(treatment);
        }
//4.4 Nutrient Export
        if (st.biomassModul.equalsIgnoreCase("none") == false) {
            treatment = new JMenuItem(messages.getString("NutrientBalance"));
            treatment.setActionCommand("NutrientBalance");
            treatment.addActionListener(listener);
            reportMenu.add(treatment);
        }
//4.3 Deadwood
        if (st.deadwoodModulName.equalsIgnoreCase("none") == false) {
            treatment = new JMenuItem(messages.getString("Deadwood"));
            treatment.setActionCommand("Deadwood");
            treatment.addActionListener(listener);
            reportMenu.add(treatment);
        }
//4.3 Roots
        if (accessInput) {
            treatment = new JMenuItem(messages.getString("Roots"));
            treatment.setActionCommand("Roots");
            treatment.addActionListener(listener);
            reportMenu.add(treatment);
        }
//4.3 Rooting Dialog
        if (accessInput) {
            treatment = new JMenuItem(messages.getString("RootingDialog"));
            treatment.setActionCommand("RootingDialog");
            treatment.addActionListener(listener);
            reportMenu.add(treatment);
        }
//4.3 Definition                
        treatment = new JMenuItem(messages.getString("Definition"));
        treatment.setActionCommand("Definition");
        treatment.addActionListener(listener);
        reportMenu.add(treatment);
//4.3 Species code                
        treatment = new JMenuItem(messages.getString("MainMenubar.species_code"));
        treatment.setActionCommand("species_code");
        treatment.addActionListener(listener);
        reportMenu.add(treatment);

        add(reportMenu);

//5. main menu item: Properties
        JMenu settingsMenu = new JMenu(messages.getString("Properties"));
//5.1 set Working dir               
        treatment = new JMenuItem(messages.getString("Program_Settings"));
        treatment.setActionCommand("Program Settings");
        treatment.addActionListener(listener);
        settingsMenu.add(treatment);

//5.2 Species Manager                
        treatment = new JMenuItem(messages.getString("Species_Manager"));
        treatment.setActionCommand("Species Manager");
        treatment.addActionListener(listener);
        settingsMenu.add(treatment);
        add(settingsMenu);

//6.main menu item: Help                
        JMenu helpMenu = new JMenu(messages.getString("Help"));
//6.2 About
        treatment = new JMenuItem(messages.getString("About"));
        treatment.setActionCommand("About");
        treatment.addActionListener(listener);
        helpMenu.add(treatment);
//1.5.1 Info page               
/*		mi = new JMenuItem(messages.getString("Info_page")); 
		mi.setActionCommand("Info page"); 
		mi.addActionListener(listener); 
		m.add(mi);  
         */
//1.5.1 Info page               
        treatment = new JMenuItem(messages.getString("License"));
        treatment.setActionCommand("License");
        treatment.addActionListener(listener);
        helpMenu.add(treatment);
//1.5.1 Course               
        treatment = new JMenuItem(messages.getString("Introduction"));
        treatment.setActionCommand("Introduction");
        treatment.addActionListener(listener);
        helpMenu.add(treatment);
//1.5.1 Course               
        treatment = new JMenuItem(messages.getString("Changes"));
        treatment.setActionCommand("Changes");
        treatment.addActionListener(listener);
        helpMenu.add(treatment);
//1.6.1 Kontrollsumme               
        treatment = new JMenuItem(messages.getString("checksum"));
        treatment.setActionCommand("checksum");
        treatment.addActionListener(listener);
        helpMenu.add(treatment);
//1.6.1 Bwin62.txt_to XML
        treatment = new JMenuItem("Bwin62.txt->xml");
        treatment.setActionCommand("Bwin62");
        treatment.addActionListener(listener);
        helpMenu.add(treatment);

        add(helpMenu);
    }

    private JMenu createProcessMenu(ActionListener listener, boolean accessInput) {
        //4. main menu item: process
        JMenu processMenu = new JMenu(messages.getString("Process"));
        //4.1 Grow
        JMenuItem grow = new JMenuItem(messages.getString("Grow"));
        grow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
        grow.setActionCommand("Grow");
        grow.addActionListener(listener);
        processMenu.add(grow);
        //4.2 Treatment
        JMenuItem treatment = new JMenuItem(messages.getString("Treatment"));
        treatment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
        treatment.setActionCommand("Treatment");
        treatment.addActionListener(listener);
        processMenu.add(treatment);
        //4.3 grow backwards
        if (accessInput) {
            JMenuItem growBack = new JMenuItem("grow back");
            growBack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK));
            growBack.setActionCommand("grow_back");
            growBack.addActionListener(listener);
            processMenu.add(growBack);
        }
        return processMenu;
    }

    private JMenu createViewMenu(ItemListener Ilistener) {
        // 3. main menu item: Window
        JMenu viewMenu = new JMenu(messages.getString("View"));
        //3.1 Standmap
        cmi[0] = new JCheckBoxMenuItem(messages.getString("Standmap"), true);
        cmi[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.CTRL_MASK));
        cmi[0].addItemListener(Ilistener);
        viewMenu.add(cmi[0]);
        //3.2 Parallel Projection
        cmi[1] = new JCheckBoxMenuItem(messages.getString("Parallel_Projection"), true);
        cmi[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.CTRL_MASK));
        cmi[1].addItemListener(Ilistener);
        viewMenu.add(cmi[1]);
        //3.3 Graphics
        cmi[2] = new JCheckBoxMenuItem(messages.getString("Graphics"), false);
        cmi[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, Event.CTRL_MASK));
        cmi[2].addItemListener(Ilistener);
        viewMenu.add(cmi[2]);
        //3.4 Stand Info
        cmi[5] = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("forestsimulator/gui").getString("Stand_Info"), true);
        cmi[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK));
        cmi[5].addItemListener(Ilistener);
        viewMenu.add(cmi[5]);
        //3.5 Stand Info
        cmi[4] = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("forestsimulator/gui").getString("Treatment_Info"), true);
        cmi[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.CTRL_MASK));
        cmi[4].addItemListener(Ilistener);
        viewMenu.add(cmi[4]);
        return viewMenu;
    }

    private JMenu createEditMenu(ItemListener Ilistener, ActionListener listener) {
        // 2. main menu item: Edit
        JMenu editMenu = new JMenu(messages.getString("Edit"));
        //2.1 add Tree
        cmi[3] = new JCheckBoxMenuItem(messages.getString("Stand_(Add_Trees)"), false);
        cmi[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.CTRL_MASK));
        cmi[3].addItemListener(Ilistener);
        editMenu.add(cmi[3]);
        //2.2 Edit data
        JMenuItem standData = new JMenuItem("Stand data");
        standData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
        standData.setActionCommand("edit");
        standData.addActionListener(listener);
        editMenu.add(standData);
        return editMenu;
    }

    private JMenu createStandMenu(ActionListener listener, boolean accessInput) {
        // 1. main menu item: Stand
        JMenu standMenu = new JMenu(messages.getString("MainMenubar.stand"));
        // 1.1  submenu item below 1
        JMenuItem newStand = new JMenuItem(messages.getString("new"));
        newStand.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        newStand.setActionCommand("new");
        newStand.addActionListener(listener);
        standMenu.add(newStand);
        // 1.2  stand designer
        JMenu subm = new JMenu(messages.getString("open"));
        JMenuItem openTreeGroSSFile = new JMenuItem("TreeGrOSS xml-File");
        openTreeGroSSFile.setActionCommand("openTreegrossXML");
        openTreeGroSSFile.addActionListener(listener);
        subm.add(openTreeGroSSFile);
        //  Open from Access-Database
        if (accessInput) {
            JMenuItem openAccessDatabase = new JMenuItem("open_Access_File");
            openAccessDatabase.setActionCommand("openAccess");
            openAccessDatabase.addActionListener(listener);
            subm.add(openAccessDatabase);
        }
//                
        standMenu.add(subm);
        subm = new JMenu(messages.getString("save"));
//                  mi = new JMenuItem(messages.getString("save"));
//                  mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
//	    	  mi.setActionCommand("save");
//		  mi.addActionListener(listener);
//		  subm.add(mi);
//		  m.add(mi);
        JMenuItem saveTreeGroSS = new JMenuItem("TreeGrOSS xml-File");
        saveTreeGroSS.setActionCommand("saveTreegrossXML");
        saveTreeGroSS.addActionListener(listener);
//		  m.add(mi);
        subm.add(saveTreeGroSS);
        standMenu.add(subm);
        JMenuItem sqlLite = new JMenuItem("SQLite Database");
        sqlLite.setActionCommand("SQlite");
        sqlLite.addActionListener(listener);
        standMenu.add(sqlLite);
        JMenuItem exit = new JMenuItem(messages.getString("exit"));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
        exit.setActionCommand("exit");
        exit.addActionListener(listener);
        standMenu.add(exit);
        return standMenu;
    }
}
