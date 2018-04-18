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

public class TgToolbar extends JToolBar {

    public TgToolbar(ActionListener listener, String path, Locale preferredLanguage) {
        ResourceBundle messages = ResourceBundle.getBundle("forestsimulator/gui");
        JButton growButton = new JButton(new ImageIcon(path + "//icons//userbaum1.jpg"));
        growButton.setRolloverEnabled(true);
        growButton.setToolTipText(messages.getString("TgToolbar.growButton.toolTipText"));
        growButton.setContentAreaFilled(false);
        growButton.setBorderPainted(false);
        growButton.setFocusPainted(false);
        growButton.setActionCommand("Grow");

        growButton.addActionListener(listener);
        add(growButton);

        JButton treatmentButton = new JButton(new ImageIcon(path + "//icons//useraxt1.jpg"));
        treatmentButton.setToolTipText(messages.getString("TgToolbar.treatmentButton.toolTipText"));
        treatmentButton.setContentAreaFilled(false);
        treatmentButton.setBorderPainted(false);
        treatmentButton.setFocusPainted(false);
        treatmentButton.setActionCommand("Treatment");
        treatmentButton.addActionListener(listener);
        add(treatmentButton);
        setFloatable(false);
    }
}
