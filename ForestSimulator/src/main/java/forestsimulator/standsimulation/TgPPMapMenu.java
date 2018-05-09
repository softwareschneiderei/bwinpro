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

class TgPPMapMenu extends JMenuBar {

    JCheckBoxMenuItem[] cmi = new JCheckBoxMenuItem[7];

    public TgPPMapMenu(ActionListener listener, ItemListener Ilistener) {
        ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");

        add(createGraphicsMenu(messages, listener));
        add(createAttributesMenu(messages, Ilistener, listener));
    }

    private JMenu createAttributesMenu(ResourceBundle messages, ItemListener Ilistener, ActionListener listener) {
        JMenuItem mi;
        // 2. main menu item: Edit
        JMenu m = new JMenu(messages.getString("TgPPMapMenu.attributes"));
        cmi[0] = new JCheckBoxMenuItem(messages.getString("TgPPMapMenu.attributes.living_trees"), true);
        cmi[0].addItemListener(Ilistener);
        m.add(cmi[0]);
        cmi[1] = new JCheckBoxMenuItem(messages.getString("TgPPMapMenu.attributes.thinned_trees"), false);
        cmi[1].addItemListener(Ilistener);
        m.add(cmi[1]);
        cmi[2] = new JCheckBoxMenuItem(messages.getString("TgPPMapMenu.attributes.dead_trees"), true);
        cmi[2].addItemListener(Ilistener);
        m.add(cmi[2]);
        mi = new JMenuItem(messages.getString("TgPPMapMenu.attributes.sky_color"));
        mi.setActionCommand("ppSkyColor");
        mi.addActionListener(listener);
        m.add(mi);
        mi = new JMenuItem(messages.getString("TgPPMapMenu.attributes.ground_color"));
        mi.setActionCommand("ppGroundColor");
        mi.addActionListener(listener);
        m.add(mi);
        mi = new JMenuItem(messages.getString("TgPPMapMenu.attributes.stand_ground_color"));
        mi.setActionCommand("ppStandGroundColor");
        mi.addActionListener(listener);
        m.add(mi);
        return m;
    }

    private JMenu createGraphicsMenu(ResourceBundle messages, ActionListener listener) {
        JMenu m;
        JMenuItem mi;
        // 1. main menu item: Stand
        m = new JMenu(messages.getString("TgPPMapMenu.graphic"));
        // 1.1  submenu item below 1
        mi = new JMenuItem(messages.getString("TgPPMapMenu.graphic.refresh"));
        mi.setActionCommand("refreshPPMap");
        mi.addActionListener(listener);
        m.add(mi);
        // 1.2  stand designer
        mi = new JMenuItem(messages.getString("TgPPMapMenu.graphic.save_to_jpg"));
        mi.setActionCommand("savePPMapToJPG");
        mi.addActionListener(listener);
        m.add(mi);
        //1.3 save stand
        mi = new JMenuItem(messages.getString("TgPPMapMenu.graphic.zoom_in"));
        mi.setActionCommand("zoomPPMapIn");
        mi.addActionListener(listener);
        m.add(mi);
        //1.4 save stand
        mi = new JMenuItem(messages.getString("TgPPMapMenu.graphic.zoom_out"));
        mi.setActionCommand("zoomPPMapOut");
        mi.addActionListener(listener);
        m.add(mi);
        return m;
    }
}
