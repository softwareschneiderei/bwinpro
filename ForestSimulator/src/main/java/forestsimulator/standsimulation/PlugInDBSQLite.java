/*   (c) 2002-2007 Juergen Nagel,  Northwest German Forest Research Station,
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

For more information see:
http://www.nw-fva.de/~nagel/treegross/

 */
package forestsimulator.standsimulation;

import javax.swing.JFrame;
import treegross.base.*;

/**
 * This plug is for connectin an Access Database
 */
public interface PlugInDBSQLite {

    public void startDialog(JFrame frame, Stand st, TgUser userSettings);
}
