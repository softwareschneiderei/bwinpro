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
package nwfva.assortment;
import java.io.*;
import java.util.logging.Logger;

/**
 * Hilfsklasse um mit dem Browser eine Datei zu starten
 * @author J. Nagel
 */
public class StartBrowser {
   private static final Logger log = Logger.getLogger( nwfva.assortment.NWFVA_Nutzung.class.getName() );
   static String url;
//start local HTML Page
/**
 * 
 * @param urlx Url der Browserseite
 */
   public StartBrowser( String urlx ) {
    url = urlx;

}

/**
 *  Starten des Browsers und Aufrufen der übergebenen Seite
 */
public void start() {
    try {
       String trenn =System.getProperty("file.separator"); 
       if (trenn.indexOf("/") < 0 ) {
          Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler "+ url );
       } else {
          Runtime.getRuntime().exec("firefox " + url);
       }
    } catch (IOException ioe) {
     log.info(ioe.toString());
     }
// System.out.println("StartBrowser");
}
}
