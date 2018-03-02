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
/**
 *
 * @author  sschimpf
 */
package forestsimulator.standsimulation;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class TgStandMapMenu extends JMenuBar 
{
    JCheckBoxMenuItem[] cmi = new JCheckBoxMenuItem[10];
    JMenu subm = new JMenu("DBH- Factor");
//    JComboBox dbhFactor = new JComboBox();
    JComboBox dbhFactor= new JComboBox();
    
    public TgStandMapMenu(ActionListener listener, ItemListener Ilistener, Locale preferredLanguage)
	{	
        Locale currentLocale;
        ResourceBundle messages;
        currentLocale = preferredLanguage;
        messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
        JMenu m; // Hauptmenupunkt
		JMenuItem mi; //Untermenupunkt
        JMenu subm = new JMenu(messages.getString("show"));
                
// 1. main menu item: Stand
		m = new JMenu(messages.getString("Graphic")); 
// 1.1  submenu item below 1		
		mi = new JMenuItem(messages.getString("refresh")); 
		mi.setActionCommand("refreshStandMap"); 
		mi.addActionListener(listener); 
		m.add(mi);
// 1.2  stand designer		
		mi = new JMenuItem(messages.getString("save_to_jpg")); 
		mi.setActionCommand("saveStandMapToJPG"); 
		mi.addActionListener(listener); 
		m.add(mi); 
//1.3 save stand		                
                mi = new JMenuItem(messages.getString("zoom_in")); 
		mi.setActionCommand("zoomStandMapIn"); 
		mi.addActionListener(listener); 
		m.add(mi); 
//1.4 save stand		                
                mi = new JMenuItem(messages.getString("zoom_out")); 
		mi.setActionCommand("zoomStandMapOut"); 
		mi.addActionListener(listener); 
		m.add(mi); 
                add(m);
                
// 2. main menu item: Edit
                
		m = new JMenu(messages.getString("Attributes")); 
//2.1 add Tree
                cmi[0] = new JCheckBoxMenuItem(messages.getString("Stand_information"), true);
		cmi[0].addItemListener(Ilistener); 
		m.add(cmi[0]); 
//2.1 add Tree
                cmi[1] = new JCheckBoxMenuItem(messages.getString("Crown_surface"), false);
		cmi[1].addItemListener(Ilistener); 
		m.add(cmi[1]); 
//2.2 Treatment parameters                
                cmi[2]= new JCheckBoxMenuItem(messages.getString("Tree_number"), false);  
		cmi[2].addItemListener(Ilistener); 
		m.add(cmi[2]); 
//2.2 Treatment parameters                
///                dbhFactor = new JComboBox("Tree diameter factor");  
//		cmi[2].addItemListener(Ilistener); 
                subm = new JMenu(messages.getString("DBH_Factor")); 
		mi = new JMenuItem(messages.getString("Factor1")); 
		mi.setActionCommand("Factor=1"); 
		mi.addActionListener(listener); 
		subm.add(mi);  
		mi = new JMenuItem(messages.getString("Factor3")); 
		mi.setActionCommand("Factor=3"); 
		mi.addActionListener(listener); 
		subm.add(mi);  
		mi = new JMenuItem(messages.getString("Factor5")); 
		mi.setActionCommand("Factor=5"); 
		mi.addActionListener(listener); 
		subm.add(mi);  

		m.add(subm); 
                
               
                
                add(m);
//
// left Mouse
                m = new JMenu(messages.getString("Mouse")); 
                cmi[8] = new JCheckBoxMenuItem(messages.getString("MouseThinning"), true);
		cmi[8].addItemListener(Ilistener); 
		m.add(cmi[8]); 
//2.1 add Tree
                cmi[9] = new JCheckBoxMenuItem(messages.getString("MouseCropTree"), false);
		cmi[9].addItemListener(Ilistener); 
		m.add(cmi[9]); 
                add(m);

                
                
                
        }
}