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

class TgGrafikMenu extends JMenuBar {

    public TgGrafikMenu(ActionListener listener) {
        ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
        JMenu m; // Hauptmenupunkt
        JMenuItem mi; //Untermenupunkt

// 1. main menu item: Stand
        m = new JMenu(messages.getString("TgGrafikMenu.graphic"));
// 1.1  submenu item below 1		
        mi = new JMenuItem(messages.getString("TgGrafikMenu.graphic.refresh"));
        mi.setActionCommand("refreshPPMap");
        mi.addActionListener(listener);
        m.add(mi);
// 1.2  stand designer		
        mi = new JMenuItem(messages.getString("TgGrafikMenu.graphic.save_to_jpg"));
        mi.setActionCommand("saveChartToJPG");
        mi.addActionListener(listener);
        m.add(mi);
        add(m);

// 2. main menu item: Edit
        m = new JMenu(messages.getString("TgGrafikMenu.attributes"));
//2.2 Different graphics                
        mi = new JMenuItem(messages.getString("SpeciesByCrownSurfaceArea"));
        mi.setActionCommand("SpeciesByCrownSurfaceArea");
        mi.addActionListener(listener);
        m.add(mi);
        mi = new JMenuItem(messages.getString("DiameterDistribution"));
        mi.setActionCommand("DiameterDistribution");
        mi.addActionListener(listener);
        m.add(mi);
        mi = new JMenuItem(messages.getString("DiameterDistributionCT"));
        mi.setActionCommand("DiameterDistributionCT");
        mi.addActionListener(listener);
        m.add(mi);
        mi = new JMenuItem(messages.getString("HeightDiameterPlot"));
        mi.setActionCommand("HeightDiameterPlot");
        mi.addActionListener(listener);
        m.add(mi);

        add(m);
    }
}
