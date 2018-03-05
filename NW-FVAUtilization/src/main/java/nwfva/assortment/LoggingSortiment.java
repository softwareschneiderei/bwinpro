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

/**
 *
 * @author nagel
 */
public class LoggingSortiment {
    
    public String name="";
    public int artvon=0;
    public int artbis=999;
    public int id = 0;
    public double minD=0.0;
    public double maxD=0.0;
    public double minTop=0.0;
    public double maxTop=0.0;
    public double minH = 0.0;
    public double maxH = 0.0;
    public double zugabeProzent = 0.0;
    public double zugabeCm = 0.0;
    public double preis = 0.0;
    public double gewicht = 0.0;
    public double wahrscheinlich = 100.0;
    public boolean nurZBaum =false;
    public boolean mehrfach = false;
    public boolean entnahme = false;
    public boolean bisKronenansatz = false;
    public boolean ausgewaehlt = true;
    public int zeitFunktion=0;
    
    
    /** Creates a new instance of LoggingSortiment */
    public LoggingSortiment() {
    }
    public LoggingSortiment(String s_name, int s_artvon, int s_artbis, double s_minD, double s_maxD, double s_minTop, double s_maxTop,
                            double s_minH, double s_maxH, double s_zugabeProzent, double s_zugabeCm,
                            double s_preis, double s_gewicht, double s_wahrscheinlich, boolean s_nurZBaum,
                            boolean s_mehrfach, boolean s_entnahme, boolean s_bisKronenansatz, boolean s_ausgewaehlt, int s_id,
                            int s_zeit) {
        name = s_name;
        artvon = s_artvon;
        artbis = s_artbis;
        minD = s_minD;
        maxD = s_maxD;
        minTop = s_minTop;
        maxTop = s_maxTop;
        minH = s_minH;
        maxH = s_maxH;
        zugabeProzent = s_zugabeProzent;
        zugabeCm =s_zugabeCm;
        preis = s_preis;
        gewicht = s_gewicht;
        wahrscheinlich = s_wahrscheinlich;
        nurZBaum = s_nurZBaum;
        mehrfach = s_mehrfach;
        entnahme = s_entnahme;
        bisKronenansatz = s_bisKronenansatz;
        ausgewaehlt = s_ausgewaehlt;
        id = s_id;
        zeitFunktion = s_zeit;
    }
    
}
