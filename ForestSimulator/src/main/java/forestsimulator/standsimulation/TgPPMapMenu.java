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

class TgPPMapMenu extends JMenuBar 
{	
    JCheckBoxMenuItem[] cmi = new JCheckBoxMenuItem[7];
    JMenu subm = new JMenu("DBH- Factor");
//    JComboBox dbhFactor = new JComboBox();
    JComboBox dbhFactor= new JComboBox();
    String country;

    
    public TgPPMapMenu(ActionListener listener, ItemListener Ilistener,String preferredLanguage)
	{
                Locale currentLocale;
                ResourceBundle messages;
                currentLocale = new Locale(preferredLanguage, "");
                messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
                JMenu m; // Hauptmenupunkt
		JMenuItem mi; //Untermenupunkt
                JMenu subm = new JMenu(messages.getString("show"));
                
// 1. main menu item: Stand
		m = new JMenu(messages.getString("Graphic")); 
// 1.1  submenu item below 1		
		mi = new JMenuItem(messages.getString("refresh")); 
		mi.setActionCommand("refreshPPMap"); 
		mi.addActionListener(listener); 
		m.add(mi);
// 1.2  stand designer		
		mi = new JMenuItem(messages.getString("save_to_jpg")); 
		mi.setActionCommand("savePPMapToJPG"); 
		mi.addActionListener(listener); 
		m.add(mi); 
//1.3 save stand		                
                mi = new JMenuItem(messages.getString("zoom_in")); 
		mi.setActionCommand("zoomPPMapIn"); 
		mi.addActionListener(listener); 
		m.add(mi); 
//1.4 save stand		                
                mi = new JMenuItem(messages.getString("zoom_out")); 
		mi.setActionCommand("zoomPPMapOut"); 
		mi.addActionListener(listener); 
		m.add(mi); 
                add(m);
                
// 2. main menu item: Edit
                
		m = new JMenu(messages.getString("Attributes")); 
//2.1 add Tree
                cmi[0] = new JCheckBoxMenuItem(messages.getString("Living_trees"), true);
		cmi[0].addItemListener(Ilistener); 
		m.add(cmi[0]); 
//2.1 add Tree
                cmi[1] = new JCheckBoxMenuItem(messages.getString("Thinned_trees"), false);
		cmi[1].addItemListener(Ilistener); 
		m.add(cmi[1]); 
//2.2 Treatment parameters                
                cmi[2]= new JCheckBoxMenuItem(messages.getString("Dead_trees"), true);  
		cmi[2].addItemListener(Ilistener); 
		m.add(cmi[2]); 
//2.2 Treatment parameters                
		mi = new JMenuItem(messages.getString("Sky_Color")); 
		mi.setActionCommand("ppSkyColor"); 
		mi.addActionListener(listener); 
		m.add(mi); 
		mi = new JMenuItem(messages.getString("Ground_Color")); 
		mi.setActionCommand("ppGroundColor"); 
		mi.addActionListener(listener); 
		m.add(mi); 
		mi = new JMenuItem(messages.getString("Stand_Ground_Color")); 
		mi.setActionCommand("ppStandGroundColor"); 
		mi.addActionListener(listener); 
		m.add(mi); 
                
               
                
                add(m);
        }
}