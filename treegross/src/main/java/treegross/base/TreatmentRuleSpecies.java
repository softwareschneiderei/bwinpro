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
package treegross.base;

/**
 * Created on 15. Dezember 2004, 13:48
 *
 * @author	Henriette Duda
 *
 *
 * treatment rules regulating treatments treatment rules that refer to different
 * treatment for different species can be defined
 */
public class TreatmentRuleSpecies implements Cloneable {

    /**
     * diameter for final harvest (cm)
     */
    public double targetDiameter;
    /**
     * diameter for start of harvesting periode of layer (cm)
     */
    public double targetDiameterLayer;
    /**
     * age for start of harvesting periode of layer (years)
     */
    public double targetAgeLayer;
    /**
     * target percentage of crown surface of living trees (%)
     */
    public double targetCrownPercent;
    /**
     * Min height for croptreeselection (m)
     */
    public double minCropTreeHeight;
    /**
     * max age before harvested (years)
     */
    public double maxAge;
    /**
     * number of desired crop trees [st/ha]
     */
    public int numberCropTreesWanted;
    /**
     * shows the rang of this species in target stand 1= main species in target
     * stand type 2= secondary species 1 in target stand type 3= secondary
     * species 2 in target stand type 0= not relevant in target stand type
     */
    public int targetRang;

    /**
     * 0= normal, 0.125 high, -0.25= low influeces definition of crop tree
     * competion: 0: tree can be a taken out as competitor, if its crown is
     * tangent or overlapping to a crop trees crown >0: (temp)crop tree will be
     * is freed from competitors in a radius around its crown
     * (radius=thinningIntensity*crownwidth of crop tree) (1= (temp)crop tree
     * will be is freed from competitors in a radius of its crownwidth around
     * its crown)
     * <0: tree can be a taken out as competitor, if its crown is overlapping to
     * a crop trees crown more than thinningIntensity*crownwidth of pressed crop
     * tree (-1= full overlapp is allowed -> no tree will be taken out)
     * !thinning Intensity can only be show variation in ammont of thinned
     * trees, if amount for thinng is high enough (maxThinningVolume and
     * maxOutVolume)!
     */
    public double thinningIntensity;

    /**
     * define numbers of treatment regulations
     *
     * @param p_crownPercent diameter for final harvest (cm)
     * @param p_targetDiameter target percentage of crown surface of living
     * trees (%)
     * @param p_thinningHeight Min height for croptreeselection (m)
     * @return TreatmentRuleSpecies
     */
    public TreatmentRuleSpecies loadTreatmentRule(double p_crownPercent, double p_targetDiameter, double p_thinningHeight) {
        targetCrownPercent = p_crownPercent;
        targetDiameter = p_targetDiameter;
        minCropTreeHeight = p_thinningHeight;
        return this;
    }

    @Override
    public TreatmentRuleSpecies clone() {
        TreatmentRuleSpecies clone = new TreatmentRuleSpecies();
        clone.maxAge = this.maxAge;
        clone.minCropTreeHeight = this.minCropTreeHeight;
        clone.numberCropTreesWanted = this.numberCropTreesWanted;
        clone.targetAgeLayer = this.targetAgeLayer;
        clone.targetCrownPercent = this.targetCrownPercent;
        clone.targetDiameter = this.targetDiameter;
        clone.targetDiameterLayer = this.targetDiameterLayer;
        clone.targetRang = this.targetRang;
        clone.thinningIntensity = this.thinningIntensity;
        return clone;
    }
}
