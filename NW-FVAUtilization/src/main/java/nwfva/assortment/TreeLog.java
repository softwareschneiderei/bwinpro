/* http://www.nw-fva.de
   Version 2013-01-11

   (c) 2013 Juergen Nagel, Northwest German Forest Research Station, 
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
 * Hilfsklasse um ein Sortiment mit seinen Maßen zu speichern 
 * @author nagel
 */
public class TreeLog {
    
    public String sortName = "";
    public boolean removed = true;
    public double startHeight = 0.0;
    public double length = 0.0;
    public double volHuber_mR = 0.0;
    public double volHuber_oR = 0.0;
    public double vol_oR = 0.0;
    public double vol_mR = 0.0;
    public double time = 0.0;
    public double meanD = 0.0;
    
    public TreeLog(){
    }
 
/**
 * Konstruktor
 * @param sname Sortimentsname aus XML Datei
 * @param rem Sortiment wird entnommen, bzw. im Bestand gelassen
 * @param sHeight Starthöhe des Sortiments im Stamm [m]
 * @param len Länge ausgehaltenen Stammstücks [m]
 * @param vHmR Volumen nach Huber mit Rinde [m³]
 * @param vHoR Volumen nach Huber ohne Rinde [m³]
 * @param vmR Volumen nach Schaftformfunktion mit Rinde [m³]
 * @param voR Volumen nach Schaftformfunktion ohne Rinde [m³]
 * @param t Arbeitszeit [Minuten]
 * @param mD Mittendurchmesser [cm]
 */
    public TreeLog(String sname, boolean rem, double sHeight, double len, 
            double vHmR,double vHoR, double vmR,double voR ,double t, double mD){
         sortName = sname;
         removed = rem;
         startHeight = sHeight;
         length = len;
         volHuber_mR = vHmR;
         volHuber_oR = vHoR;
         vol_mR = vmR;
         vol_oR = voR;
         time = t;
         meanD = mD;
    }

    
}
