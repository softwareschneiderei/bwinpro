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
package forestsimulator.roots;
import java.io.*;
import java.io.IOException.*;

/**
 *
 * @author nagel
 */
public class StartBrowser {
   static String url;
//start local HTML Page
public StartBrowser( String urlx ) {
    url = urlx;

}

public void start() {
    try {
       String trenn =System.getProperty("file.separator"); 
       if (trenn.indexOf("/") < 0 ) {
          Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler "+ url );
       } else {
          Runtime.getRuntime().exec("firefox " + url);
       }
    } catch (IOException ioe) {
    ioe.printStackTrace();
     }
 System.out.println("StartBrowser");
}
}
