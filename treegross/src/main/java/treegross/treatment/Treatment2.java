/*
 * Treatment2.java
 *
 *  (c) 2002-2006 Juergen Nagel, Northwest German Forest Research Station, 
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
package treegross.treatment;

import treegross.base.Stand;

/**
 * @author	Henriette Duda for more information see: Duda, H. (2006): Vergleich
 * forstlicher Managementstrategien. Dissertation Universität Göttingen, S. 180
 * http://webdoc.sub.gwdg.de/diss/2006/duda/
 */
public class Treatment2 {

    public static final int HT_TARGET_DIAMETER = 0;
    /*Realitic version of target diameter harvesting process*/
    public static final int HT_TARGET_DIAMETER_PERIOD = 1;
    public static final int HT_SCHIRMSCHLAG = 8;
    public static final int HT_CLEAR_CUT = 9;
    public static final int HT_BY_GAPS = 3;
    
    /*constants for thinning types*/
    public static final int TT_SINGLE_TREE_SELECTION = 0;
    public static final int TT_FROM_ABOVE = 1;
    public static final int TT_FROM_BELOW = 2;
    public static final int TT_QD = 3;
    public static final int TT_CLEAR_CUT = 9;

    TreatmentElements2 te = new TreatmentElements2();

    /**
     * performs a stand treatment according to user defined preferences this is
     * a regesigned rotutine from Henriette Duda's treatment
     *
     * @param st stand object
     */
    public void executeManager2(Stand st) {
        st.sortbyd();
        st.descspecies();
        if (st.status == 0) {
            st.status = 1;
        }
        //set min outVolume (lowest)
        if (st.trule.minHarvestVolume <= st.trule.minThinningVolume) {
            st.trule.minOutVolume = st.trule.minHarvestVolume;
        } else {
            st.trule.minOutVolume = st.trule.minThinningVolume;
        }
        //set max outVolume (highest)
        if (st.trule.maxHarvestVolume >= st.trule.maxThinningVolume) {
            st.trule.maxOutVolume = st.trule.maxHarvestVolume;
        } else {
            st.trule.maxOutVolume = st.trule.maxThinningVolume;
        }

        // reset amount of volume taken out 
        // only, if this treatment takes place at least one year after last treatment
        if (st.year > st.trule.lastTreatment) {
            //vout, thinned, harvested is set =0
            te.resetOutTake(st);
        }
        //
        // create skidtrails, this is only done once
        //
        if (st.trule.skidtrails) {
            te.createSkidtrails(st);
        }

        // do treatment, 
        // if actual year > year of last treatment + treatment step
        // or if actual year is the same as year of last treatment
        if (st.year >= st.trule.lastTreatment + st.trule.treatmentStep || st.year == st.trule.lastTreatment) {

            // Nature protection block
            // Protect by diameter
            te.markTreesAsHabitatTreesByDiameter(st);

            // 1. habitat tree
            // 2. crop tree per species to protect minorities
            if (st.trule.nHabitat > 0) {
                //habitat trees are selected, habitat trees can not be harvested or chosen as
                // crop trees
                te.selectHabitatTrees(st);
            }

            // Start minority selection, by choosing one crop tree per species
            if (st.trule.protectMinorities) {
                //protect minoritys (select one tree per species)
                te.SelectOneCropTreePerSpecies(st, true);
            }

            // Harvesting block            
            if (st.degreeOfDensity < st.trule.degreeOfStockingToClearOverStoryStand && st.status > 1) {
                te.harvestRemainingTrees(st, true, -1, 1.3);
            }

            if (st.trule.typeOfHarvest == HT_TARGET_DIAMETER) {
                // target diameter trees are harvested
                te.harvestTargetDiameter(st);
            }

            // Perform a realistic target dimater, if 30% of trees reach target diameter
            if (st.trule.typeOfHarvest == HT_TARGET_DIAMETER_PERIOD && (te.percentOfBasalAreaAboveTargetDiameter(st) > 0.3 || st.status > 1)) {
                te.harvestTargetDiameterInPeriod(st);
            }

            // Perform a Schirmschlag, if 30% of trees reach target diameter
            if (st.trule.typeOfHarvest == HT_SCHIRMSCHLAG && (te.percentOfBasalAreaAboveTargetDiameter(st) > 0.3 || st.status > 1)) {
                te.harvestSchirmschlag(st);
            }

            // Perform a clearcut, if 30% of trees reach target diameter
            if ((st.trule.typeOfHarvest == HT_CLEAR_CUT && te.percentOfBasalAreaAboveTargetDiameter(st) > 0.3) || st.status > 98) {
                te.harvestClearCut(st);
            }

            // Perfom Lochhieb            
            if (st.trule.typeOfHarvest == HT_BY_GAPS) {
                te.harvestByGaps(st);
            }

            //if harvest amount was not high enough: set harvested trees alive
            // this is when harvest amount is less than the minimum harvest volume
            // Idea: You do not bring in a machine for one tree
            te.checkMinHarvestVolume(st);
            // Crop tree selection
            //
            //   if the number of wanted crop trees is changed and lower than the selected one
            //   than reset crop trees
            //
            double sumcroptrees = 0;
            for (int i = 0; i < st.nspecies; i++) {
                sumcroptrees += st.sp[i].trule.numberCropTreesWanted;
            }

            if (te.getNCropTrees(st) > sumcroptrees * st.size) {
                te.resetCropTrees(st);
            }
            //
            // Selection and reselection of Crop Trees
            if (te.getNCropTrees(st) <= 0 || st.trule.reselectCropTrees) {
                // select defined number of crop trees
                //number is reduced, depending on dg of leading layer
                if (st.trule.selectCropTrees) {
                    te.selectNCropTrees(st);
                }
            }

            // Thinning by releasing the crop trees
            if (st.trule.releaseCropTrees && st.trule.typeOfThinning == Treatment2.TT_SINGLE_TREE_SELECTION) {
                te.thinCropTreeCompetition(st);
                if (st.trule.thinArea) {
                    te.thinCompetitionFromAbove(st);
                }
            }

            // thin area between crop trees
            // selectCropTreesOfAllSpecies auch hier einbeziehen?
            //public double degreeOfThinningArea; 
            if (st.trule.typeOfThinning == Treatment2.TT_FROM_ABOVE) {
                //System.out.println("temporäre Zwischenfelder durchforsten");
                //select temp crop trees (wet species)
                te.resetTempCropTrees(st);
                te.selectTempCropTreesTargetPercentage(st);
                // Start thinning for all species
                te.thinTempCropTreeCompetition(st);
                if (st.trule.thinArea) {
                    te.thinCompetitionFromAbove(st);
                }
            }

            // Thinning from below
            if (st.trule.typeOfThinning == Treatment2.TT_FROM_BELOW) {
                te.thinFromBelow(st);
            }
            
            // Thinning by QD
            if (st.trule.typeOfThinning == Treatment2.TT_QD) {
                te.thinByQD(st);
            }

            //if thinning amount was not high enough: set thinned trees alive
            te.checkMinThinningVolume(st);

            //if treatment amount was not high enough: set outtaken trees alive
            te.checkMinTreatmentOutVolume(st);
        }

        //if trees has been taken out this year
        if (te.getHarvestedOutVolume(st) > 0 || te.getThinnedOutVolume(st) > 0) {
            st.trule.lastTreatment = st.year;
        }

        if (st.trule.autoPlanting && te.getDegreeOfCover(0, st, false) < st.trule.degreeOfStockingToStartPlanting) {
            //remove trees only if aktivated and stand status is not "growing=1"
            if (st.trule.onPlantingRemoveAllTrees /*&& st.status>1*/) {
                //protected species
                int ps = -1;
                if (st.trule.plantingString.length() >= 3) {
                    ps = Integer.parseInt(st.trule.plantingString.substring(0, 3));
                    //System.out.println("protected species: "+ps);
                }
                // why height 1.3m????
                /*te.harvestRemainingTrees(st,false, ps, 1.3);*/
                te.harvestRemainingTrees(st, false, ps, 10);
            }
            te.startPlanting(st);
        }
    }

    /**
     * stand is sorted by diameter and species are describes
     *
     * @param st stand object
     */
    public void updateStandAfterThinning(Stand st) {
        st.sortbyd();
        st.descspecies();
    }

    /**
     * unselct all (temp)croptrees
     *
     * @param st stand object
     */
    public void resetAllCropTrees(Stand st) {
        te.resetTempCropTrees(st);
        te.resetCropTrees(st);
    }

    /**
     *
     * @param st stand to be treated
     * @param active true = yes skid trails, false = no skid trails
     * @param trailDistance Distance from the center to the next center of the
     * skid trail [m] Default 20m
     * @param trailWidth Width of the skid trail [m]. Default 4m All trees on
     * the skid trail will be removed. The method is excuted only once
     *
     */
    public void setSkidTrails(Stand st, boolean active, double trailDistance, double trailWidth) {
        st.trule.skidtrails = active;
        st.trule.skidtrailDistance = trailDistance;
        st.trule.skidtrailWidth = trailWidth;
    }

    /**
     *
     * @param st Stand object to be treated
     * @param type 0= single tree selection, 1=thinning from above, 2= thinning
     * from below,
     * @param intensity Factor to normal stand density 1.0= normal stand
     * density, no thinning set intensity = 0.0
     * @param minVolume Minimum Volume to perform thinning [m³]
     * @param maxVolume Maximum Volume of thinning [m³]
     * @param croptreesOnly Release only crop trees
     *
     */
    public void setThinningRegime(Stand st, int type, double intensity, double minVolume, double maxVolume, boolean croptreesOnly) {
        st.trule.thinAreaSpeciesDependent = true;
        st.trule.thinArea = true;
        st.trule.selectCropTrees = false;
        st.trule.reselectCropTrees = false;
        st.trule.releaseCropTrees = false;
        st.trule.cutCompetingCropTrees = false;
        st.trule.releaseCropTreesSpeciesDependent = false;
        st.trule.minThinningVolume = minVolume;
        st.trule.maxThinningVolume = maxVolume;
        st.trule.thinningIntensity = intensity;
        if (type == 0) {
            st.trule.typeOfThinning = Treatment2.TT_SINGLE_TREE_SELECTION;
            st.trule.thinArea = true;
            if (croptreesOnly) {
                st.trule.thinArea = false;
            }
            st.trule.selectCropTrees = true;
            st.trule.reselectCropTrees = true;
            st.trule.selectCropTreesOfAllSpecies = false;
            st.trule.releaseCropTrees = true;
            st.trule.cutCompetingCropTrees = true;
            st.trule.releaseCropTreesSpeciesDependent = true;
        }
        // Set thinning from above here by temporay crop trees
        if (type == 1) {
            st.trule.typeOfThinning = Treatment2.TT_FROM_ABOVE;
            st.trule.selectCropTreesOfAllSpecies = false;
        }
        // Set thinning from below
        if (type == 2) {
            st.trule.typeOfThinning = Treatment2.TT_FROM_BELOW;
        }
        // Set Thinning by q-d-rule
        if (type == 3) {
            st.trule.typeOfThinning = Treatment2.TT_QD;
            st.trule.thinArea = false;
            if (croptreesOnly) {
                st.trule.thinArea = false; // oder doch true
            }
            st.trule.selectCropTrees = true;
            st.trule.reselectCropTrees = true;
            st.trule.selectCropTreesOfAllSpecies = false;
            st.trule.releaseCropTrees = true;
            st.trule.cutCompetingCropTrees = true;
            st.trule.releaseCropTreesSpeciesDependent = true;
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
    public void setHarvestRegime(Stand st, int type, double minVolume, double maxVolume, double degreeToClear, String regenerationProc) {
        st.trule.minHarvestVolume = minVolume;
        st.trule.maxHarvestVolume = maxVolume;
        st.trule.harvestLayerFromBelow = false;
        if (type == 0) {
            st.trule.typeOfHarvest = HT_TARGET_DIAMETER;
            st.trule.maxHarvestingPeriode = 6;
            st.trule.lastTreatment = 0;
            st.trule.degreeOfStockingToClearOverStoryStand = degreeToClear;
        }
        // set Harvest type Schirmschlag
        if (type == 1 || type == 4) {
            //ToDo: reset degreeOfStockingToClearOverStoryStand (std declaration = 0.3) ??????
            // regenerationProcess last value <-> degreeOfStockingToClearOverStoryStand
            st.trule.minThinningVolume = 0;
            st.trule.maxThinningVolume = 900;
            st.trule.thinningIntensityArea = 0.0;
            st.trule.minHarvestVolume = 0.0;
            st.trule.maxHarvestVolume = 500.0;
            if (type == 1) {
                st.trule.typeOfHarvest = HT_SCHIRMSCHLAG;
            } else {
                st.trule.typeOfHarvest = HT_TARGET_DIAMETER_PERIOD;
            }
            st.trule.cutCompetingCropTrees = true;
            st.trule.startOfHarvest = 0;
            st.trule.regenerationProcess = regenerationProc;
            st.trule.reselectCropTrees = true;
            //20.05.2014
            // set st.trule.degreeOfStockingToClearOverStoryStand  = 0
            // to avoid the first harvesting block call line no. 111 ('te.harvestRemainingTrees(...)')
            st.trule.degreeOfStockingToClearOverStoryStand = 0;
        }
// set Harvest type Clear Cut
        if (type == 2) {
            st.trule.thinningIntensityArea = 0.0;
            st.trule.minHarvestVolume = 0.0;
            st.trule.maxHarvestVolume = 99999.0;
            st.trule.typeOfHarvest = HT_CLEAR_CUT;
            st.trule.maxHarvestingPeriode = 0;
            st.trule.lastTreatment = 0;
        }
        // set Harvest to Gaps the size is for experimental reasons 30m
        if (type == HT_BY_GAPS) {
            st.trule.thinningIntensityArea = 0.0;
            st.trule.typeOfHarvest = 3;
            st.trule.maxHarvestingPeriode = 0;
            st.trule.lastTreatment = 0;
            st.trule.reselectCropTrees = true;
        }
    }

    /**
     *
     * @param st Stand to be treated
     * @param habitatTrees number of trees /ha
     * @param typeOfHabitat 1=ei,bu; 0= hardwoods; 2= all trees
     * @param minority protect rare species
     * @param minStocking minimum stocking restricts further harvesting
     * @param diameterProtection protects trees form thinning and harvest if dbh
     * > value
     */
    public void setNatureProtection(Stand st, int habitatTrees, int typeOfHabitat, boolean minority, double minStocking, int diameterProtection) {
        st.trule.protectMinorities = minority;
        st.trule.nHabitat = habitatTrees;
        st.trule.habitatTreeType = typeOfHabitat;
        st.trule.minimumCoverage = minStocking;
        st.trule.treeProtectedfromBHD = diameterProtection;
    }

    public void setAutoPlanting(Stand st, boolean active, boolean clearArea, double critCoverage, String plantingStr) {
        st.trule.autoPlanting = active;
        st.trule.onPlantingRemoveAllTrees = clearArea;
        st.trule.degreeOfStockingToStartPlanting = critCoverage;
        st.trule.plantingString = plantingStr;
    }

    public void setBT(Stand st) {
        if (st.bt > 0) {
            int art1 = (int) (Math.floor(st.bt / 10));
            int art2 = st.bt - art1 * 10;
            int mixPerc1 = 100;
            int mixPerc2 = 0;
            if (art2 > 0) {
                mixPerc1 = 70;
                mixPerc2 = 30;
            }
            for (int i = 0; i < st.nspecies; i++) {
                st.sp[i].trule.targetCrownPercent = 0.0;
                st.sp[i].trule.numberCropTreesWanted = 0;
            }
            for (int k = 0; k < 2; k++) {
                int art = art1;
                int mix = mixPerc1;
                if (k > 0) {
                    art = art2;
                    mix = mixPerc2;
                }
                for (int i = 0; i < st.nspecies; i++) {
                    if (art == 1 && (st.sp[i].code == 111 || st.sp[i].code == 112)) {
                        double anz = 0;
                        for (int j = 0; j < st.nspecies; j++) {
                            if (st.sp[j].code == 111 || st.sp[j].code == 112) {
                                anz = anz + 1.0;
                            }
                        }
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(1 * mix / anz);
                    }
                    if (art == 2 && st.sp[i].code == 211) {
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(1.2 * mix);
                    }
                    if (art == 3 && (st.sp[i].code == 311 || st.sp[i].code == 321)) {
                        double anz = 0;
                        for (int j = 0; j < st.nspecies; j++) {
                            if (st.sp[j].code == 311 || st.sp[j].code == 321) {
                                anz = anz + 1.0;
                            }
                        }
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(1.2 * mix / anz);
                    }
                    if (art == 4 && st.sp[i].code == 421) {
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(1.2 * mix);
                    }
                    if (art == 5 && st.sp[i].code == 511) {
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(2.0 * mix);
                    }
                    if (art == 6 && st.sp[i].code == 611) {
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(1.8 * mix);
                    }
                    if (art == 7 && st.sp[i].code == 711) {
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(2.0 * mix);
                    }
                    if (art == 8 && st.sp[i].code == 811) {
                        st.sp[i].trule.targetCrownPercent = mix;
                        st.sp[i].trule.numberCropTreesWanted = (int) Math.round(2.0 * mix);
                    }
                }
            }
        }

    }

    public String getPlantingStr(int wet) {
        String txt = "211[1.0];";
        if (wet > 0) {
            int art1 = (int) (Math.floor(wet / 10));
            int art2 = wet - art1 * 10;
            Double mixPerc1 = 1.0;
            Double mixPerc2 = 0.0;
            if (art2 > 0) {
                mixPerc1 = 0.7;
                mixPerc2 = 0.3;
            }
            txt = art1 + "11[" + mixPerc1.toString() + "];";
            if (art2 > 0) {
                txt = txt + art2 + "11[" + mixPerc2.toString() + "];";
            }
        }
        return txt;
    }
}
