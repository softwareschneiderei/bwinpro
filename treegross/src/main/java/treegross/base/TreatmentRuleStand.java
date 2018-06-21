/*
 * TreatmentRuleStand.java
 *
 *  (c) 2002-2008 Juergen Nagel, Northwest German Forest Research Station 
 *      Grätzelstr.2, 37079 Göttingen, Germany
 *      E-Mail: Juergen.Nagel@nw-fva.de
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  http://treegross.sourceforge.net
 */
package treegross.base;

import treegross.base.thinning.ThinningType;
import treegross.treatment.Treatment2;

/**
 * Created on 15. Dezember 2004, 13:48
 *
 * @author	Henriette Duda
 *
 *
 * treatment rules regulating treatments treatment rules that refer to the stand
 * can be defined
 */
public class TreatmentRuleStand implements Cloneable {

    /**
     * treatmentype code 0=LOEWE-Concept of Lower Saxony 1=PNV 2=Net Yield
     * 3=Process
     */
    public int treatmentType;
    /**
     * Max. Harvestvolume (fm per ha), taken out during one harvesting step (sum
     * over all species)
     */
    public double maxHarvestVolume;
    /**
     * min. volume per ha taken out during harvesting (fm per ha)
     */
    public double minHarvestVolume;
    /**
     * max. harvest volume (fm per ha) taken out during one treatment (sum over
     * all species) manThinningVolume - already harvested volume= actual
     * thinning volume thus -> condition for thinning is: the full ammount has
     * not been tapped during harvesting
     */
    public double maxThinningVolume;
    /**
     * min. volume (fm per ha) taken out during thinning in volume per ha
     */
    public double minThinningVolume;
    /**
     * min. volume (fm per ha) taken out during one treatment (sum over all
     * species) sum of harvested and thinned volume per ha
     */
    public double minOutVolume;
    /**
     * max. volume (fm per ha) taken out during one treatment (sum over all
     * species) sum of harvested and thinned volume per ha
     */
    public double maxOutVolume;
    /**
     * actual stand type code
     */
    public int standType;
    /**
     * target stand type code
     */
    public int targetType;
    //** Stand type code at stand status 1. NOT the actual bt/wet. -> important for definition of regeneration rules
    public int standTypeAtStatus1 = -99;
    /**
     * number of wanted habitat trees per ha
     */
    public double nHabitat;
    /**
     * one tree of each species will be maintained (selected as crop tree)
     */
    public boolean protectMinorities;
    /**
     * defines periode between treatments in stand (years)
     */
    public int treatmentStep;
    /**
     * harvesting started so many years ago
     */
    public int harvestingYears;
    /**
     * defines in which periode harvesting must be complete
     */
    public int maxHarvestingPeriode;
    /**
     * true-> harvest layer from below false-> harvest layer from above
     * (relevant if type of harvest=1)
     */
    public boolean harvestLayerFromBelow;
    /**
     * select crop trees if not done
     */
    public boolean selectCropTrees;
    /**
     * reselect crop trees automatically
     */
    public boolean reselectCropTrees;
    /**
     * select crop trees with no preference to certain species
     */
    public boolean selectCropTreesOfAllSpecies;
    /**
     * release crop trees
     */
    public boolean releaseCropTrees;
    /**
     * release crop trees species dependent
     */
    public boolean releaseCropTreesSpeciesDependent;
    /**
     * cut competing crop trees
     */
    public boolean cutCompetingCropTrees;
    /**
     * thin none crop trees (by using temporary crop trees)
     */
    public boolean thinArea;
    /**
     * release temp crop trees species dependent
     */
    public boolean thinAreaSpeciesDependent;
    /**
     * degree of thinning for thinArea
     */
    public double thinningIntensityArea;
    /**
     * degree of thinning intensity
     */
    public double thinningIntensity = 1.0;
    /**
     * type of thinning
     */
    public ThinningType typeOfThinning;
    /**
     * year harvesting was started important for Schirmschlag
     */
    public int typeOfHarvest;
    /**
     * year harvesting was started important for Schirmschlag
     */
    public int startOfHarvest;
    /**
     * harvest or thinning was performed that year
     */
    public int lastTreatment;
    /**
     * decission if a habitat tree <0 is supposed to be selected, considering
     * likelyhood by stand.size
     */
    public boolean selectHabiatPart;
    // added by jhansen
    /**
     * wanted volume of death wood remaining in stand
     */
    public double wantedDeathVol = 20.0;
    /**
     * minimum diameter of death log
     */
    public double minDeathDiameter = 20.0;
    /**
     * skidtrails
     */
    public boolean skidtrails = false;
    /**
     * skidtrails distance m
     */
    public double skidtrailDistance = 20.0;
    /**
     * skidtrails
     */
    public double skidtrailWidth = 20.0;
    /**
     * degree of stocking to clear overstory stand
     */
    public double degreeOfStockingToClearOverStoryStand = 0.3;
    /**
     * habitat tree type 0=hardwoods except Aln, 1= all hardwoods, 2= all
     */
    public int habitatTreeType = 0;
    /**
     * trees protected from harvest and thinning, if DBH > value the protected
     * trees will be marked and treated as additional habitat trees
     */
    public int treeProtectedfromBHD = 150;
    /**
     * automatic Planting active
     */
    public boolean autoPlanting = false;
    /**
     * Planting: clear all trees first
     */
    public boolean onPlantingRemoveAllTrees = false;
    /**
     * Restricts all harvesting to a minimum coverage of the overstory
     */
    public double minimumCoverage = 0.0;
    /**
     * Planting degree of stocking when Planting starts
     */
    public double degreeOfStockingToStartPlanting = 0.3;
    /**
     * Planting: String with species and area per ha [ha/ha] Example plant "211
     * [0.6];511 [0.3]" = 0.6ha beech and 0.3ha spruce
     */
    public String plantingString = "";
    /**
     * Regeneration Process: String with degree of stocking levels Example
     * "0.6;0.4;0.2;0.0" means first harvest cut stand to 0.6 degree stocking,
     * 2nd Period cut Stand to 0.4, then 0.2 and then remove the rest
     */
    public String regenerationProcess = "";
    
    public static TreatmentRuleStand rulesWith(
            ThinningType thinningType,
            boolean selectCropTrees,
            boolean reselectCropTrees,
            boolean releaseCropTrees,
            boolean releaseCropTreesSpeciesDependent,
            double maxThinningVolume,
            boolean thinAreaSpeciesDependent,
            double maxHarvestVolume,
            int maxHarvestingPeriode) {
        return new TreatmentRuleStand(
                thinningType,
                selectCropTrees,
                reselectCropTrees,
                releaseCropTrees,
                releaseCropTreesSpeciesDependent,
                maxThinningVolume,
                thinAreaSpeciesDependent,
                maxHarvestVolume,
                maxHarvestingPeriode
        );
    }
    
    public TreatmentRuleStand() {
        this(null, false, false, false, false, 0d, false, 0d, 0);
    }

    private TreatmentRuleStand(
            ThinningType thinningType,
            boolean selectCropTrees,
            boolean reselectCropTrees,
            boolean releaseCropTrees,
            boolean releaseCropTreesSpeciesDependent,
            double maxThinningVolume,
            boolean thinAreaSpeciesDependent,
            double maxHarvestVolume,
            int maxHarvestingPeriode) {
        super();
        this.typeOfThinning = thinningType;
        this.selectCropTrees = selectCropTrees;
        this.reselectCropTrees = reselectCropTrees;
        this.releaseCropTrees = releaseCropTrees;
        this.releaseCropTreesSpeciesDependent = releaseCropTreesSpeciesDependent;
        this.maxThinningVolume = maxThinningVolume;
        this.thinAreaSpeciesDependent = thinAreaSpeciesDependent;
        this.maxHarvestVolume = maxHarvestVolume;
        this.maxHarvestingPeriode = maxHarvestingPeriode;
    }

    // max percentage of harvested/thinned wood has to be added
    // added by jhansen
    @Override
    public TreatmentRuleStand clone() throws CloneNotSupportedException {
        TreatmentRuleStand clone = (TreatmentRuleStand) super.clone();
        clone.cutCompetingCropTrees = this.cutCompetingCropTrees;
        clone.harvestLayerFromBelow = this.harvestLayerFromBelow;
        clone.harvestingYears = this.harvestingYears;
        clone.lastTreatment = this.lastTreatment;
        clone.maxHarvestVolume = this.maxHarvestVolume;
        clone.maxHarvestingPeriode = this.maxHarvestingPeriode;
        clone.maxOutVolume = this.maxOutVolume;
        clone.maxThinningVolume = this.maxThinningVolume;
        clone.minHarvestVolume = this.minHarvestVolume;
        clone.minOutVolume = this.minOutVolume;
        clone.minThinningVolume = this.minThinningVolume;
        clone.nHabitat = this.nHabitat;
        clone.protectMinorities = this.protectMinorities;
        clone.releaseCropTrees = this.releaseCropTrees;
        clone.releaseCropTreesSpeciesDependent = this.releaseCropTreesSpeciesDependent;
        clone.reselectCropTrees = this.reselectCropTrees;
        clone.selectCropTrees = this.selectCropTrees;
        clone.selectCropTreesOfAllSpecies = this.selectCropTreesOfAllSpecies;
        clone.selectHabiatPart = this.selectHabiatPart;
        clone.standType = this.standType;
        clone.targetType = this.targetType;
        clone.thinArea = this.thinArea;
        clone.thinAreaSpeciesDependent = this.thinAreaSpeciesDependent;
        clone.thinningIntensityArea = this.thinningIntensityArea;
        clone.treatmentStep = this.treatmentStep;
        clone.treatmentType = this.treatmentType;
        clone.typeOfHarvest = this.typeOfHarvest;
        clone.wantedDeathVol = this.wantedDeathVol;
        clone.minDeathDiameter = this.minDeathDiameter;
        clone.degreeOfStockingToClearOverStoryStand = this.degreeOfStockingToClearOverStoryStand;
        clone.habitatTreeType = this.habitatTreeType;
        clone.plantingString = this.plantingString;
        clone.regenerationProcess = this.regenerationProcess;
        clone.standTypeAtStatus1 = this.standTypeAtStatus1;
        return clone;
    }

    /**
     *
     * @param active true = yes skid trails, false = no skid trails
     * @param trailDistance Distance from the center to the next center of the
     * skid trail [m] Default 20m
     * @param trailWidth Width of the skid trail [m]. Default 4m All trees on
     * the skid trail will be removed. The method is excuted only once
     *
     */
    public void setSkidTrails(boolean active, double trailDistance, double trailWidth) {
        this.skidtrails = active;
        this.skidtrailDistance = trailDistance;
        this.skidtrailWidth = trailWidth;
    }

    /**
     *
     * @param type 0= single tree selection, 1=thinning from above, 2= thinning
     * from below,
     * @param intensity Factor to normal stand density 1.0= normal stand
     * density, no thinning set intensity = 0.0
     * @param minVolume Minimum Volume to perform thinning [m³]
     * @param maxVolume Maximum Volume of thinning [m³]
     * @param croptreesOnly Release only crop trees
     *
     */
    public void setThinningRegime(ThinningType type, double intensity, double minVolume, double maxVolume, boolean croptreesOnly) {
        this.typeOfThinning = type;
        this.thinAreaSpeciesDependent = true;
        this.thinArea = true;
        this.selectCropTrees = false;
        this.reselectCropTrees = false;
        this.releaseCropTrees = false;
        this.cutCompetingCropTrees = false;
        this.releaseCropTreesSpeciesDependent = false;
        this.minThinningVolume = minVolume;
        this.maxThinningVolume = maxVolume;
        this.thinningIntensity = intensity;
        if (type == ThinningType.SingleTreeSelection) {
            this.thinArea = true;
            if (croptreesOnly) {
                this.thinArea = false;
            }
            this.selectCropTrees = true;
            this.reselectCropTrees = true;
            this.selectCropTreesOfAllSpecies = false;
            this.releaseCropTrees = true;
            this.cutCompetingCropTrees = true;
            this.releaseCropTreesSpeciesDependent = true;
        }
        // Set thinning from above here by temporay crop trees
        if (type == ThinningType.ThinningFromAbove) {
            this.selectCropTreesOfAllSpecies = false;
        }
        // Set Thinning by q-d-rule
        if (type == ThinningType.ThinningQD) {
            this.thinArea = false;
            if (croptreesOnly) {
                this.thinArea = false; // oder doch true
            }
            this.selectCropTrees = true;
            this.reselectCropTrees = true;
            this.selectCropTreesOfAllSpecies = false;
            this.releaseCropTrees = true;
            this.cutCompetingCropTrees = true;
            this.releaseCropTreesSpeciesDependent = true;
        }
    }

    /*
     * type:
     * 0 = target diameter
     * 1 = schirmschlag
     * 2 = clearcut
     * 3 = by gaps
     * 4 = more realitic version of target diameter harvesting process (based on schirmschlag)
     */
    public void setHarvestRegime(int type, double minVolume, double maxVolume, double degreeToClear, String regenerationProc) {
        this.minHarvestVolume = minVolume;
        this.maxHarvestVolume = maxVolume;
        this.harvestLayerFromBelow = false;
        if (type == 0) {
            this.typeOfHarvest = Treatment2.HT_TARGET_DIAMETER;
            this.maxHarvestingPeriode = 6;
            this.lastTreatment = 0;
            this.degreeOfStockingToClearOverStoryStand = degreeToClear;
        }
        // set Harvest type Schirmschlag
        if (type == 1 || type == 4) {
            //ToDo: reset degreeOfStockingToClearOverStoryStand (std declaration = 0.3) ??????
            // regenerationProcess last value <-> degreeOfStockingToClearOverStoryStand
            this.minThinningVolume = 0;
            this.maxThinningVolume = 900;
            this.thinningIntensityArea = 0.0;
            this.minHarvestVolume = 0.0;
            this.maxHarvestVolume = 500.0;
            if (type == 1) {
                this.typeOfHarvest = Treatment2.HT_SCHIRMSCHLAG;
            } else {
                this.typeOfHarvest = Treatment2.HT_TARGET_DIAMETER_PERIOD;
            }
            this.cutCompetingCropTrees = true;
            this.startOfHarvest = 0;
            this.regenerationProcess = regenerationProc;
            this.reselectCropTrees = true;
            //20.05.2014
            // set st.trule.degreeOfStockingToClearOverStoryStand  = 0
            // to avoid the first harvesting block call line no. 111 ('te.harvestRemainingTrees(...)')
            this.degreeOfStockingToClearOverStoryStand = 0;
        }
        // set Harvest type Clear Cut
        if (type == 2) {
            this.thinningIntensityArea = 0.0;
            this.minHarvestVolume = 0.0;
            this.maxHarvestVolume = 99999.0;
            this.typeOfHarvest = Treatment2.HT_CLEAR_CUT;
            this.maxHarvestingPeriode = 0;
            this.lastTreatment = 0;
        }
        // set Harvest to Gaps the size is for experimental reasons 30m
        if (type == Treatment2.HT_BY_GAPS) {
            this.thinningIntensityArea = 0.0;
            this.typeOfHarvest = 3;
            this.maxHarvestingPeriode = 0;
            this.lastTreatment = 0;
            this.reselectCropTrees = true;
        }
    }

    /**
     *
     * @param habitatTrees number of trees /ha
     * @param typeOfHabitat 1=ei,bu; 0= hardwoods; 2= all trees
     * @param minority protect rare species
     * @param minStocking minimum stocking restricts further harvesting
     * @param diameterProtection protects trees form thinning and harvest if dbh
     * > value
     */
    public void setNatureProtection(int habitatTrees, int typeOfHabitat, boolean minority, double minStocking, int diameterProtection) {
        this.protectMinorities = minority;
        this.nHabitat = habitatTrees;
        this.habitatTreeType = typeOfHabitat;
        this.minimumCoverage = minStocking;
        this.treeProtectedfromBHD = diameterProtection;
    }

    public void setAutoPlanting(boolean active, boolean clearArea, double critCoverage, String plantingStr) {
        this.autoPlanting = active;
        this.onPlantingRemoveAllTrees = clearArea;
        this.degreeOfStockingToStartPlanting = critCoverage;
        this.plantingString = plantingStr;
    }
}
