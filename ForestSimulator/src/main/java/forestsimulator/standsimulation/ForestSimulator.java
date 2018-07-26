/** http://www.nw-fva.de
   Version 19-02-2009

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

import java.io.IOException;
import javax.swing.JFrame;
import treegross.base.*;

public class ForestSimulator {

    public static void main(String args[]) throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
        System.setProperty("swing.plaf.metal.controlFont", "Tahoma");
        System.setProperty("swing.aatext", "true");
        System.getProperties().list(System.out);
        Stand st = new Stand();
        
        JFrame mainWindow = new TgJFrame(st);
        mainWindow.setVisible(true);
    }
}
