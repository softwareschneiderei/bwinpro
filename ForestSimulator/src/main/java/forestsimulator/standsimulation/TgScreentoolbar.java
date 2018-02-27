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
 * @author  Jnagel
 */
package forestsimulator.standsimulation;
import treegross.base.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class TgScreentoolbar extends JToolBar
{
    JButton jButton1;
    JButton jButton2;
    JButton jButton3;
   
    
    /** Creates a new instance of tgtoolbar */
    public TgScreentoolbar(ActionListener listener, String path) 
    {
       
        jButton1 = new JButton(new ImageIcon(path+"//icons//screen1.jpg"));
        jButton1.setActionCommand("screen1");
        jButton1.addActionListener(listener);
        add(jButton1);
        jButton2 = new JButton(new ImageIcon(path+"//icons//screen2.jpg"));
        jButton2.setActionCommand("screen2");
        jButton2.addActionListener(listener);
        add(jButton2);
        jButton3 = new JButton(new ImageIcon(path+"//icons//screen3.jpg"));
        jButton3.setActionCommand("screen3");
        jButton3.addActionListener(listener);
        add(jButton3);

    }
    
}

