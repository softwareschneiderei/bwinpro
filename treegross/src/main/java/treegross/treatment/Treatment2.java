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

import treegross.base.Species;
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
    
    TreatmentElements2 te = new TreatmentElements2();

    /**
     * performs a stand treatment according to user defined preferences this is
     * a regesigned routine from Henriette Duda's treatment
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
        if (st.trule.skidtrails.active) {
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
                st.resetCropTrees();
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

            for (Species species : st.species()) {
                st.trule.thinningSettings.type().thinner(te.vout).thin(st, species);
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
        st.resetTempCropTrees();
        st.resetCropTrees();
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
