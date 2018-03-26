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

class MyMenubar extends JMenuBar
{	
    JCheckBoxMenuItem[] cmi = new JCheckBoxMenuItem[7];
    String language="en";
    String country;

    public MyMenubar(ActionListener listener, ItemListener Ilistener, String preferredLanguage, Stand st, boolean accessInput)
	{	Locale currentLocale;
                ResourceBundle messages;
                currentLocale = new Locale(preferredLanguage, "");
                messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
                JMenu m; // Hauptmenupunkt
		JMenuItem mi; //Untermenupunkt
                JMenu subm = new JMenu(messages.getString("show"));
                
// 1. main menu item: Stand
		m = new JMenu(messages.getString("Stand")); 
// 1.1  submenu item below 1		
		mi = new JMenuItem(messages.getString("new")); 
                mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		mi.setActionCommand("new"); 
		mi.addActionListener(listener); 
		m.add(mi);
// 1.2  stand designer		
                subm = new JMenu(messages.getString("open")); 
		mi = new JMenuItem("TreeGrOSS xml-File"); 
		mi.setActionCommand("openTreegrossXML"); 
		mi.addActionListener(listener); 
		subm.add(mi); 
//  Open from Access-Database
        if (accessInput) {
		   mi = new JMenuItem("open_Access_File"); 
		   mi.setActionCommand("openAccess"); 
		   mi.addActionListener(listener); 
		   subm.add(mi);  
        }
//                

		m.add(subm); 
//1.4 save stand		                
                subm = new JMenu(messages.getString("save")); 
//                  mi = new JMenuItem(messages.getString("save")); 
//                  mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
//	    	  mi.setActionCommand("save"); 
//		  mi.addActionListener(listener); 
//		  subm.add(mi); 
//		  m.add(mi); 
// 1.2  Save to xml File		
  		  mi = new JMenuItem("TreeGrOSS xml-File"); 
		  mi.setActionCommand("saveTreegrossXML"); 
		  mi.addActionListener(listener); 
//		  m.add(mi); 
		  subm.add(mi); 
		m.add(subm); 

//1.6 exit programm		                
                mi = new JMenuItem("SQLite Database"); 
		mi.setActionCommand("SQlite"); 
		mi.addActionListener(listener); 
		m.add(mi);                 
                

//1.6 exit programm		                
                mi = new JMenuItem(messages.getString("exit")); 
                mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
		mi.setActionCommand("exit"); 
		mi.addActionListener(listener); 
		m.add(mi); 
		add(m); 
                
// 2. main menu item: Edit
                
		m = new JMenu(messages.getString("Edit")); 
//2.1 add Tree
                cmi[3] = new JCheckBoxMenuItem(messages.getString("Stand_(Add_Trees)"), false);
                cmi[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.CTRL_MASK));
		cmi[3].addItemListener(Ilistener); 
		m.add(cmi[3]); 
//2.2 Edit data
                mi = new JMenuItem("Stand data"); 
                mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
		mi.setActionCommand("edit"); 
		mi.addActionListener(listener); 
		m.add(mi); 

                //2.2 Treatment parameters                
//                cmi[4] = new JCheckBoxMenuItem(messages.getString("Treatment_(Parameters)"), true);  
//                cmi[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, Event.CTRL_MASK));
//		cmi[4].addItemListener(Ilistener); 
//		m.add(cmi[4]); 
                
		add(m); 
                
// 3. main menu item: Window
                m = new JMenu(messages.getString("View"));
//3.1 Standmap                
		cmi[0] = new JCheckBoxMenuItem(messages.getString("Standmap"), true); 
                cmi[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.CTRL_MASK));
		cmi[0].addItemListener(Ilistener); 
		m.add(cmi[0]); 
//3.2 Parallel Projection		
                cmi[1] = new JCheckBoxMenuItem(messages.getString("Parallel_Projection"), true);
                cmi[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.CTRL_MASK));
		cmi[1].addItemListener(Ilistener); 
		m.add(cmi[1]); 
//3.3 Graphics		                
                cmi[2] = new JCheckBoxMenuItem(messages.getString("Graphics"), false);  
                cmi[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, Event.CTRL_MASK));
		cmi[2].addItemListener(Ilistener); 
		m.add(cmi[2]); 
//3.4 Stand Info                
                cmi[5] = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("forestsimulator/standsimulation/TgJFrame").getString("Stand_Info"), true);  
                cmi[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK));
		cmi[5].addItemListener(Ilistener); 
		m.add(cmi[5]); 
                
//3.5 Stand Info                
                cmi[4] = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("forestsimulator/standsimulation/TgJFrame").getString("Treatment_Info"), true);  
                cmi[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.CTRL_MASK));
		cmi[4].addItemListener(Ilistener); 
		m.add(cmi[4]);                  
//
                add(m);

//4. main menu item: process
		
                m= new JMenu(messages.getString("Process"));
//4.1 Grow                
                mi = new JMenuItem(messages.getString("Grow"));
                mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
        	mi.setActionCommand("Grow"); 
        	mi.addActionListener(listener); 
                m.add(mi);  
//4.2 Treatment                
                mi = new JMenuItem(messages.getString("Treatment"));
                mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
        	mi.setActionCommand("Treatment"); 
		mi.addActionListener(listener); 
                m.add(mi);  
//4.3 grow backwards
        if (accessInput) {
                mi = new JMenuItem("grow back");
                mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK));
        	mi.setActionCommand("grow_back");
		mi.addActionListener(listener);
                m.add(mi);
        }
//4.4 Special Treatment                
/*                mi = new JMenuItem("Special Treatment");
                mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
        	mi.setActionCommand("specialTreatment"); 
                mi.addActionListener(listener);
                m.add(mi);
*/
        //4.4 select crop tree 
        if (accessInput) {
                mi = new JMenuItem(messages.getString("select_crop_trees"));
        	mi.setActionCommand("select_crop_trees");
		mi.addActionListener(listener);
                m.add(mi);
        }
        //4.5 deselect crop trees
        if (accessInput) {
                mi = new JMenuItem(messages.getString("deselect_crop_trees"));
        	mi.setActionCommand("deselect_crop_trees");
		mi.addActionListener(listener);
                m.add(mi);
        }
                //4.6 thin by list
        if (accessInput) {
                mi = new JMenuItem(messages.getString("thin_by_list"));
        	mi.setActionCommand("thin_by_list");
		mi.addActionListener(listener);
                m.add(mi);
        }
                add(m);
                
//1.5 save HTML-Reports                
                m= new JMenu(messages.getString("Reports"));
//1.5.2 Tree values                
		mi = new JMenuItem(messages.getString("Tree_values")); 
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.ALT_MASK)); 
		mi.setActionCommand("Tree values"); 
		mi.addActionListener(listener); 
		m.add(mi);  
//1.5.3 Stand table                
		mi = new JMenuItem(messages.getString("Stand_table")); 
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.ALT_MASK)); 
		mi.setActionCommand("Stand table"); 
		mi.addActionListener(listener); 
		m.add(mi);  
//1.5.4 Structure table                
		mi = new JMenuItem(messages.getString("Structure_table")); 
		mi.setActionCommand("Structure table"); 
		mi.addActionListener(listener); 
		m.add(mi);  
//1.5.5 Structure table                
		mi = new JMenuItem("Baumtabelle"); 
		mi.setActionCommand("tree_table"); 
		mi.addActionListener(listener); 
                mi.setVisible(false);
		m.add(mi);  
//4.3 Sorting  
                if (st.sortingModul.equalsIgnoreCase("none") == false){
                    mi = new JMenuItem(messages.getString("Sorting"));
                    mi.setActionCommand("Sorting");
                    mi.addActionListener(listener);
                    m.add(mi);
                }
//4.4 Nutrient Export
                if (st.biomassModul.equalsIgnoreCase("none") == false){
                    mi = new JMenuItem(messages.getString("NutrientBalance"));
                    mi.setActionCommand("NutrientBalance");
                    mi.addActionListener(listener);
                    m.add(mi);
                }    
//4.3 Deadwood
               if (st.deadwoodModulName.equalsIgnoreCase("none") == false ) {
                    mi = new JMenuItem(messages.getString("Deadwood"));
                    mi.setActionCommand("Deadwood");
                    mi.addActionListener(listener);
                    m.add(mi);
               }
//4.3 Roots
        if (accessInput) {
            mi = new JMenuItem(messages.getString("Roots"));
        	mi.setActionCommand("Roots"); 
            mi.addActionListener(listener);
            m.add(mi);
        }
//4.3 Rooting Dialog
       if (accessInput) {
            mi = new JMenuItem(messages.getString("RootingDialog"));
        	mi.setActionCommand("RootingDialog");
            mi.addActionListener(listener);
            m.add(mi);
        }
//4.3 Definition                
        mi = new JMenuItem(messages.getString("Definition"));
        mi.setActionCommand("Definition");
        mi.addActionListener(listener);
        m.add(mi);
//4.3 Species code                
        mi = new JMenuItem(messages.getString("Species_code"));
        mi.setActionCommand("species_code");
        mi.addActionListener(listener);
        m.add(mi);
                                                               
        add(m);                
                
//5. main menu item: Properties

                m = new JMenu(messages.getString("Properties"));
//5.1 set Working dir               
                mi = new JMenuItem(messages.getString("Program_Settings"));
        	mi.setActionCommand("Program Settings"); 
                mi.addActionListener(listener);
                m.add(mi);
                
//5.2 Species Manager                
                mi = new JMenuItem(messages.getString("Species_Manager"));
        	mi.setActionCommand("Species Manager"); 
                mi.addActionListener(listener);
                m.add(mi);
                add(m);
                
//6.main menu item: Help                
                m= new JMenu(messages.getString("Help"));
//6.2 About
                mi = new JMenuItem(messages.getString("About"));
        	mi.setActionCommand("About"); 
                mi.addActionListener(listener);
                m.add(mi);
//1.5.1 Info page               
/*		mi = new JMenuItem(messages.getString("Info_page")); 
		mi.setActionCommand("Info page"); 
		mi.addActionListener(listener); 
		m.add(mi);  
 */
//1.5.1 Info page               
		mi = new JMenuItem(messages.getString("License")); 
		mi.setActionCommand("License"); 
		mi.addActionListener(listener); 
		m.add(mi);  
//1.5.1 Course               
		mi = new JMenuItem(messages.getString("Introduction")); 
		mi.setActionCommand("Introduction"); 
		mi.addActionListener(listener); 
		m.add(mi); 
//1.5.1 Course               
		mi = new JMenuItem(messages.getString("Changes")); 
		mi.setActionCommand("Changes"); 
		mi.addActionListener(listener); 
		m.add(mi);                 
//1.6.1 Kontrollsumme               
		mi = new JMenuItem(messages.getString("checksum")); 
		mi.setActionCommand("checksum"); 
		mi.addActionListener(listener); 
		m.add(mi);
//1.6.1 Bwin62.txt_to XML
		mi = new JMenuItem("Bwin62.txt->xml");
		mi.setActionCommand("Bwin62");
		mi.addActionListener(listener);
		m.add(mi);

                add(m);
        }
 
    
}