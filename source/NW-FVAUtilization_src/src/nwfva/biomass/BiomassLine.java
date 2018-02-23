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

    
    
    /** Creates a new instance of LoggingSortiment */
    public BiomassLine() {
    }

    public BiomassLine clone(){
        BiomassLine clone = new BiomassLine();
        clone.year = new Integer(this.year);
        clone.cutVolume = new Double(this.cutVolume);
        clone.sortiments = new Boolean(this.sortiments);
        clone.firewood = new Boolean(this.firewood);
        clone.restwood = new Boolean(this.restwood);
        clone.firewoodPerc = new Double(this.firewoodPerc);
        clone.needlePerc = new Double(this.needlePerc);
        clone.sortimentsBM = new Double(this.sortimentsBM);
        clone.barksortimentsBM = new Double(this.barksortimentsBM);
        clone.barkfirewoodBM = new Double(this.barkfirewoodBM);
        clone.barkrestwoodBM = new Double(this.barkrestwoodBM);
        clone.branchBM = new Double(this.branchBM);
        clone.reisigBM = new Double(this.reisigBM);
        clone.firewoodBM = new Double(this.firewoodBM);
        clone.restwoodBM = new Double(this.restwoodBM);
        clone.leafBM = new Double(this.leafBM);
        clone.sumC = new Double(this.sumC);
        clone.sumN = new Double(this.sumN);
        clone.sumS = new Double(this.sumS);
        clone.sumP = new Double(this.sumP);
        clone.sumK = new Double(this.sumK);
        clone.sumCa = new Double(this.sumCa);
        clone.sumMg = new Double(this.sumMg);
        clone.sumMn = new Double(this.sumMn);
        clone.sumFe = new Double(this.sumFe);
        clone.sumBNH4 = new Double(this.sumBNH4);
        clone.sumBOup = new Double(this.sumBOup);
        clone.sumBNO3 = new Double(this.sumBNO3);
        return clone;
    }
    
    
}
