/* http://www.nw-fva.de
   Version 2013-01-13

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
package nwfva.biomass;

/**
 * Hilfsklasse um die Biomassen- und Nährstoffwerte zu übergeben
 * @author J N.agel
 */
public class BiomassLine {
    
    int year=0;
    double cutVolume = 0.0;
    boolean sortiments = true;
    boolean firewood = true;
    boolean restwood = true;
    double firewoodPerc = 80.0;
    double restwoodPerc = 90.0;
    double needlePerc = 30.0;
    double sortimentsBM = 0.0;
    double barksortimentsBM=0.0;
    double barkfirewoodBM=0.0;
    double barkrestwoodBM=0.0;
    double branchBM=0.0;
    double reisigBM=0.0;
    double firewoodBM =0.0;
    double restwoodBM =0.0;
    double leafBM =0.0;
    double sumC=0.0;
    double sumN=0.0;
    double sumS=0.0;
    double sumP=0.0;
    double sumK=0.0;
    double sumCa=0.0;
    double sumMg=0.0;
    double sumMn=0.0;
    double sumFe=0.0;
    double sumBOup=0.0;
    double sumBNH4=0.0;
    double sumBNO3=0.0;
    
    public BiomassLine clone(){
        BiomassLine clone = new BiomassLine();
        clone.year = this.year;
        clone.cutVolume = this.cutVolume;
        clone.sortiments = this.sortiments;
        clone.firewood = this.firewood;
        clone.restwood = this.restwood;
        clone.firewoodPerc = this.firewoodPerc;
        clone.needlePerc = this.needlePerc;
        clone.sortimentsBM = this.sortimentsBM;
        clone.barksortimentsBM = this.barksortimentsBM;
        clone.barkfirewoodBM = this.barkfirewoodBM;
        clone.barkrestwoodBM = this.barkrestwoodBM;
        clone.branchBM = this.branchBM;
        clone.reisigBM = this.reisigBM;
        clone.firewoodBM = this.firewoodBM;
        clone.restwoodBM = this.restwoodBM;
        clone.leafBM = this.leafBM;
        clone.sumC = this.sumC;
        clone.sumN = this.sumN;
        clone.sumS = this.sumS;
        clone.sumP = this.sumP;
        clone.sumK = this.sumK;
        clone.sumCa = this.sumCa;
        clone.sumMg = this.sumMg;
        clone.sumMn = this.sumMn;
        clone.sumFe = this.sumFe;
        clone.sumBNH4 = this.sumBNH4;
        clone.sumBOup = this.sumBOup;
        clone.sumBNO3 = this.sumBNO3;
        return clone;
    }
}
