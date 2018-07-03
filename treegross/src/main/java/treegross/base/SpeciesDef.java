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

import treegross.base.thinning.DynamicThinning;

public class SpeciesDef implements Comparable, Cloneable {

    public int code;
    public int internalCode;
    public String shortName;
    public String longName;
    public String latinName;
    public int codeGroup;
    public int handledLikeCode;
    public int heightCurve = -1;
    //public int taperFunction;
    public int crownType;
    public double criticalCrownClosure;
    public int maximumAge;
    public double targetDiameter;
    public double heightOfThinningStart;
    public int colorGreen, colorRed, colorBlue;
    public boolean defined = false;
    public String modelRegion = "default";
    public double diameterIncrementError = 0.0;
    public double heightIncrementError = 0.0;
    public DynamicThinning dynamicThinning;
    public String colorXML = "100,200,300";
    public String competitionXML = "";
    public String ingrowthXML = "";
    public String taperFunctionXML = "";
    public String coarseRootBiomass = "";
    public String smallRootBiomass = "";
    public String fineRootBiomass = "";
    public String totalRootBiomass = "";
    public String stemVolumeFunctionXML = "";
    /*number of cropp trees per ha*/
    public int cropTreeNumber = -9;
    // TG functions (text or class)
    public TGFunction uniformHeightCurveXML;
    public TGFunction heightVariationXML;
    public TGFunction diameterDistributionXML;
    public TGFunction volumeFunctionXML;
    public TGFunction crownwidthXML;
    public TGFunction crownbaseXML;
    public TGFunction siteindexXML;
    public TGFunction siteindexHeightXML;
    public TGFunction potentialHeightIncrementXML;
    public TGFunction heightIncrementXML;
    public TGFunction diameterIncrementXML;
    public TGFunction maximumDensityXML;
    public TGFunction decayXML;

    /**
     * Creates a new instance of Class
     */
    public SpeciesDef() {
    }

    @Override
    public SpeciesDef clone() {
        SpeciesDef clone = new SpeciesDef();
        clone.code = this.code;
        clone.internalCode = this.internalCode;
        clone.shortName = this.shortName;
        clone.longName = this.longName;
        clone.latinName = this.latinName;
        clone.codeGroup = this.codeGroup;
        clone.handledLikeCode = this.handledLikeCode;
        clone.heightCurve = this.heightCurve;
        //clone.taperFunction= new Integer(this.taperFunction);
        clone.crownType = this.crownType;
        clone.criticalCrownClosure = this.criticalCrownClosure;
        clone.maximumAge = this.maximumAge;
        clone.targetDiameter = this.targetDiameter;
        clone.heightOfThinningStart = this.heightOfThinningStart;
        clone.colorGreen = this.colorGreen;
        clone.colorRed = this.colorRed;
        clone.colorBlue = this.colorBlue;
        clone.defined = this.defined;
        clone.modelRegion = this.modelRegion;
        clone.stemVolumeFunctionXML = this.stemVolumeFunctionXML;
        clone.diameterIncrementError = this.diameterIncrementError;
        clone.heightIncrementError = this.heightIncrementError;
        clone.dynamicThinning = this.dynamicThinning;
        clone.colorXML = this.colorXML;
        clone.competitionXML = this.competitionXML;
        clone.ingrowthXML = this.ingrowthXML;
        clone.taperFunctionXML = this.taperFunctionXML;
        clone.coarseRootBiomass = this.coarseRootBiomass;
        clone.smallRootBiomass = this.smallRootBiomass;
        clone.fineRootBiomass = this.fineRootBiomass;
        clone.totalRootBiomass = this.totalRootBiomass;
        clone.cropTreeNumber = this.cropTreeNumber;
        // TGFunctions
        clone.volumeFunctionXML = volumeFunctionXML.clone();
        clone.uniformHeightCurveXML = uniformHeightCurveXML.clone();
        clone.diameterDistributionXML = diameterDistributionXML.clone();
        clone.crownwidthXML = crownwidthXML.clone();
        clone.crownbaseXML = crownbaseXML.clone();
        clone.siteindexXML = siteindexXML.clone();
        clone.diameterIncrementXML = diameterIncrementXML.clone();
        clone.heightIncrementXML = heightIncrementXML.clone();
        clone.heightVariationXML = heightVariationXML.clone();
        clone.siteindexHeightXML = siteindexHeightXML.clone();
        clone.potentialHeightIncrementXML = potentialHeightIncrementXML.clone();
        clone.maximumDensityXML = maximumDensityXML.clone();
        clone.decayXML = decayXML.clone();
        return clone;
    }

    @Override
    public int compareTo(Object o) {
        if (code == ((SpeciesDef) o).code) {
            return 1;
        } else {
            return 0;
        }
    }

    void setDefined(boolean defined) {
        this.defined = defined;
    }

    @Override
    public String toString() {
        String result = "*****\nSpecies Defenition: " + code + " defined: " + defined + "\n";
        result += "diameter increment: " + diameterIncrementXML + "\n*****";
        return result;
    }
}
