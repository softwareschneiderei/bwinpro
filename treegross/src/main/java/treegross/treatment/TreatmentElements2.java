/*
 * TreatmentElements2.java
 *
 *
 *  (c) 2002-2008 Juergen Nagel, Northwest Forest Research Station, 
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
 *  http://www.nw-fva.de
 */
package treegross.treatment;

import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.base.GenerateXY;
import treegross.base.OutType;
import treegross.base.Species;
import treegross.base.SpeciesNotDefinedException;
import treegross.base.Stand;
import treegross.base.Tree;

/**
 * @author	Henriette Duda for more information see: Duda, H. (2006): Vergleich
 * forstlicher Managementstrategien. Dissertation Universität Göttingen, S. 180
 * http://webdoc.sub.gwdg.de/diss/2006/duda/
 */
public class TreatmentElements2 {

    /**
     * maximal harvest volume
     */
    double vmaxharvest;

    /**
     * maximal thinningvolume
     */
    double vmaxthinning;

    /**
     * The actual harvested volume
     */
    double vout;

    /**
     * volume harvested
     */
    double harvested;

    /**
     * volume thinned
     */
    double thinned;

    //Choose trees for stand target mixture dependant on scenario
    CropTreeSelection ctselect = new CropTreeSelection();

    HabitatTreeSelection htselect = new HabitatTreeSelection();

    private final static Logger LOGGER = Logger.getLogger(TreatmentElements2.class.getName());

    /**
     * set volume of trees, which are taken out to 0. This sets the variables
     * vout, harvested, thinned, vmaxharvest, vmaxthinning = 0.0
     *
     * @param st stand object
     */
    public void resetOutTake(Stand st) {
        // reset actual harvest volume
        vout = 0;
        //reset counter for harvested and thinned ammount
        harvested = 0;
        thinned = 0;
        //reset max volumes
        vmaxharvest = 0;
        vmaxthinning = 0;
    }

    /**
     * unselect all crop trees
     *
     * @param st stand object
     */
    public void resetCropTrees(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].crop = false;
        }
    }

    /**
     * unselect all temp crop trees
     *
     * @param st the stand
     */
    public void resetTempCropTrees(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].tempcrop = false;
        }
    }

    /**
     * unselect all habitat trees
     *
     * @param st stand object
     */
    public void resetHabitatTrees(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].habitat = false;
        }
    }

    /**
     * to obtain at least one tree of each species helps to secure rare species
     * and to create a high bioderversity
     *
     * @param st stand object
     * @param forceSelection
     */
    public void SelectOneCropTreePerSpecies(Stand st, boolean forceSelection) {
        double rn;

        if (forceSelection) {
            rn = 0;
        } else {
            rn = st.random.nextUniform();
        }
        // if standsize < 1ha select trees with likelihood of standsize
        if (rn <= st.size) {
            CropTreeSpecies ctspecies[] = new CropTreeSpecies[30];
            //Distance CropTrees
            double dist_ct;

            // Sort species that the species with the least trees will be first
            // sort ascening
            Species sptemp;
            for (int i = 0; i < st.nspecies - 1; i++) {
                for (int j = i + 1; j < st.nspecies; j++) {
                    if (st.sp[i].nha > st.sp[j].nha) {
                        sptemp = st.sp[i];
                        st.sp[i] = st.sp[j];
                        st.sp[j] = sptemp;
                    }
                }
            }

            //Define CTSpecies
            //Initialize Croptreespecies dependent on target mixture percentage and height of first thinning
            for (int i = 0; i < st.nspecies; i++) {
                //calculate distance dependent on crownwidth (dependent on species and TargetDBH)
                Tree atree = new Tree();
                atree.st = st;
                atree.code = st.sp[i].code;
                atree.sp = st.sp[i];
                //distance is depending on crownwidth of tree of that species, that has reached target diameter
                atree.d = st.sp[i].trule.targetDiameter;
                dist_ct = atree.calculateCw();

                //Initialize CropTreeSpecies
                ctspecies[i] = new CropTreeSpecies();
                ctspecies[i].addCtsp(st.sp[i].code, 1.0 / st.size, dist_ct, st.sp[i].trule.minCropTreeHeight);
            }

            //Select Croptrees dependent on CTSpecies
            ctselect.selectCropTrees(st, ctspecies);
        }
    }

    /**
     * select croptrees dependent on target percentages of species
     *
     * @param st stand object
     */
    public void selectCropTreeTargetPercentage(Stand st) {

        CropTreeSpecies ctspecies[] = new CropTreeSpecies[30];

        //Distance CropTrees
        double dist_ct;
        //Number of Crop Trees
        int n_ct_ha;

        //Define CTSpecies
        //Initialize Croptreespecies dependent on target mixture percentage and height of first thinning
        for (int i = 0; i < st.nspecies; i++) {
            //if (st.sp[i].nha>0){
            //calculate distance dependent on crownwidth (dependent on species and TargetDBH)
            Tree atree = new Tree();
            atree.st = st;
            atree.code = st.sp[i].code;
            atree.sp = st.sp[i];
            atree.d = st.sp[i].trule.targetDiameter;
            dist_ct = atree.calculateCw();

            //Number of crop trees dependent on calcualted distance and target mixture percent
            n_ct_ha = (int) ((10000.0 / ((Math.PI * Math.pow(dist_ct, 2.0)) / 4)) * st.sp[i].trule.targetCrownPercent / 100.0);

            //Initialize CropTreeSpecies
            ctspecies[i] = new CropTreeSpecies(st.sp[i].code, n_ct_ha, dist_ct, st.sp[i].trule.minCropTreeHeight);
            //}
        }
        //Select Croptrees dependent on CTSpecies
        ctselect.selectCropTrees(st, ctspecies);
    }

    /**
     * select croptrees dependent on number wanted number is reduced to a number
     * that fit actual dg of species in its leading layer number is reduced by
     * targetCrownPercent of species
     *
     * @param st stand object
     */
    public void selectNCropTrees(Stand st) {

        CropTreeSpecies ctspecies[] = new CropTreeSpecies[30];

        /**
         * Number of crop trees to select
         */
        int n_ct_ha;

        /**
         * distance of crop trees to select
         */
        double dist_ct;

        //Initialize Croptreespecies dependent on target mixture percentage and height of first thinning
        for (int i = 0; i < st.nspecies; i++) {
            if (st.sp[i].trule.numberCropTreesWanted == 0) {
                dist_ct = 0.01;
            } //            else dist_ct=0.90* Math.sqrt((st.size*100.0*st.sp[i].trule.targetCrownPercent/(st.sp[i].trule.numberCropTreesWanted*st.size)));
            else {
                dist_ct = 0.80 * Math.sqrt((100.0 * st.sp[i].trule.targetCrownPercent / (st.sp[i].trule.numberCropTreesWanted)));
            }
            //Initialize CropTreeSpecies
            ctspecies[i] = new CropTreeSpecies();
            ctspecies[i].addCtsp(st.sp[i].code, st.sp[i].trule.numberCropTreesWanted, dist_ct, st.sp[i].trule.minCropTreeHeight);
        }
        //Select Croptrees dependent on CTSpecies
        ctselect.selectCropTrees(st, ctspecies);
    }

    /**
     * select croptrees dependent on target percentages of species
     *
     * @param st stand object
     */
    public void selectTempCropTreesTargetPercentage(Stand st) {

        CropTreeSpecies ctspecies[] = new CropTreeSpecies[30];

        //Distance CropTrees
        double dist_ct;
        //Number of Crop Trees
        int n_ct_ha;
        //Find Crownwidth
        //Define CTSpecies
        //Initialize Temp Croptrees dependent on target mixture percentage and height of first thinning
        for (int i = 0; i < st.nspecies; i++) {
            //if (st.sp[i].nha>0){
            Tree atree = new Tree();
            atree.st = st;
            atree.code = st.sp[i].code;
            atree.d = st.sp[i].d100;
            atree.sp = st.sp[i];
            dist_ct = atree.calculateCw();

            //Number of temp crop trees dependent on calcualted distance and target mixture percent
            // number: 1/2 of matching tree number
            n_ct_ha = (int) ((10000.0 / ((Math.PI * Math.pow(dist_ct, 2.0)) / 4)) * st.sp[i].trule.targetCrownPercent / 100.0);

            //Initialize Temp CropTreeSpecies
            ctspecies[i] = new CropTreeSpecies();
            ctspecies[i].addCtsp(st.sp[i].code, n_ct_ha, dist_ct, st.sp[i].trule.minCropTreeHeight);
            //}

        }
        //Select Temp Croptrees dependent on target mixture percentage
        ctselect.selectTempCropTrees(st, ctspecies);
    }

    public void selectHabitatTrees(Stand st) {
        htselect.selectHabitatTrees(st);
    }

    /**
     * harvest trees with dbh> targetdiameter, start with trees no habitat tree
     * will be taken out trees are taken out until max harvest volume is reached
     *
     * @param st the stand
     */
    public void harvestTargetDiameter(Stand st) {

        //set max harvest volume (vmaxharvest) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        vmaxharvest = st.size * st.trule.maxHarvestVolume - harvested;

        //reduce max harvest if max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        // minus outaken amount (vout) is less than set max harvest volume (vmaxharvest)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxharvest) {
            vmaxharvest = st.size * st.trule.maxOutVolume - vout;
        }

        // if there is a amount to be harvested
        if (vmaxharvest > 0) {
            sortStandByTargetDiameter(st);
            //see if there are target diameter trees , then harvest those trees
            // conditions: no habitat tree, diameter > target diameter, standing, max harvest volume has not been reached
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].habitat == false && st.tr[i].d > st.tr[i].sp.trule.targetDiameter
                        && st.tr[i].out < 0 && harvested < vmaxharvest) {
                    if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                        break;
                    }
                    vout = vout + st.tr[i].fac * st.tr[i].v;
                    harvested += st.tr[i].fac * st.tr[i].v;
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = OutType.HARVESTED;
                    //st.tr[i].no+="_zs";
                }
            }
        }
    }

    protected void sortStandByTargetDiameter(Stand st) {
        //Sort st.tr by difference targetdiameter -diameter ascending
        Tree trtemp;
        for (int i = 0; i < st.ntrees - 1; i++) {
            for (int j = i + 1; j < st.ntrees; j++) {
                if ((st.tr[i].sp.trule.targetDiameter - st.tr[i].d) > (st.tr[j].sp.trule.targetDiameter - st.tr[j].d)) {
                    trtemp = st.tr[i];
                    st.tr[i] = st.tr[j];
                    st.tr[j] = trtemp;
                }
            }
        }
    }

    /**
     * harvest trees with dbh> targetdiameter, start with trees no habitat tree
     * will be taken out trees are taken out until max harvest volume is reached
     *
     * @param st the stand
     */
    public void harvestByGaps(Stand st) {

        //set max harvest volume (vmaxharvest) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        vmaxharvest = st.size * st.trule.maxHarvestVolume - harvested;

        //reduce max harvest if max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        // minus outaken amount (vout) is less than set max harvest volume (vmaxharvest)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxharvest) {
            vmaxharvest = st.size * st.trule.maxOutVolume - vout;
        }
        boolean done = false;
        while ((harvested < vmaxharvest && vmaxharvest > 0) && done == false) {
        // create a gap, where the sum of the difference between crop tree diameter - d is max
            // Habitat trees will have a negative impact   
            //find tree with highest dbh over targetdiameter 
            int merk = -9;
            double maxdiff = -9999.9;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && st.tr[i].crop && st.tr[i].habitat == false) {
                    double diff = 0.0;
                    for (int j = 0; j < st.ntrees; j++) {
                        if (st.tr[j].out < 0 && (st.tr[j].crop || st.tr[j].habitat)) {
                            double dist = Math.pow(st.tr[j].x - st.tr[i].x, 2.0) + Math.pow(st.tr[j].y - st.tr[i].y, 2.0);
                            if (dist != 0.0) {
                                dist = Math.sqrt(dist);
                            }
                            if (dist <= 12) {
                                diff += (st.tr[j].d - st.tr[j].sp.trule.targetDiameter);
                            }
                        }
                    }
                    if (diff > 0.0 && diff > maxdiff) {
                        merk = i;
                        maxdiff = diff;
                    }
                }
            }
            if (merk == - 9) {
                done = true;
            } else {
                //see if there are target diameter trees , then harvest those trees
                // conditions: no habitat tree, diameter > target diameter, standing, max harvest volume has not been reached
                // and tree is greater than 12 m
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].habitat == false && st.tr[i].out < 0 && st.tr[i].h > 12) {
                        double dist = Math.pow(st.tr[merk].x - st.tr[i].x, 2.0) + Math.pow(st.tr[merk].y - st.tr[i].y, 2.0);
                        if (dist != 0.0) {
                            dist = Math.sqrt(dist);
                        }
                        if (dist <= 12.0 && st.tr[i].h > st.tr[merk].h * 0.25) {
                            vout += st.tr[i].fac * st.tr[i].v;
                            harvested += st.tr[i].fac * st.tr[i].v;
                            st.tr[i].out = st.year;
                            st.tr[i].outtype = OutType.HARVESTED;
                        }
                    }
                }
            }
        }
            //check if there are  trees, 15 % above target diameter then harvest those
        // conditions: no habitat tree, diameter > target diameter, standing, max harvest volume has not been reached
        if (harvested < vmaxharvest && vmaxharvest > 0) {
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].habitat == false && st.tr[i].d > st.tr[i].sp.trule.targetDiameter * 1.15
                        && st.tr[i].out < 0 && harvested < vmaxharvest) {
                    if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                        break;
                    }
                    vout += st.tr[i].fac * st.tr[i].v;
                    harvested += st.tr[i].fac * st.tr[i].v;
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = OutType.HARVESTED;
                }
            }
        }

    }

    /**
     * Check all trees if they are a crop tree, if they are competing with
     * another crop tree Mesurement: overlapping of their crowns crop tree that
     * diamteter exceeds target diameter most will be taken out first This void
     * needs CropTreeSelection
     *
     * @param st stand object
     */
    public void harvestCompetingCropTrees(Stand st) {
        //set max harvest volume (vmaxharvest) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        vmaxharvest = st.size * st.trule.maxHarvestVolume - harvested;

        //reduce max harvest if max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        // minus outaken amount (vout) is less than set max harvest volume (vmaxharvest)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxharvest) {
            vmaxharvest = st.size * st.trule.maxOutVolume - vout;
        }

        if (vmaxharvest > 0) {
            sortStandByTargetDiameter(st);

            /**
             * distance of two trees
             */
            double dist_trees;
            double dist_min;

            /**
             * height for the cross section of crop tree cb(i)+ (h(i)+cb(i)/3)
             */
            double h66_i;
            /**
             * cb(j)+ (h(j)+cb(j)/3)
             */

            for (int i = 0; i < st.ntrees; i++) {
                //check only crop trees for competing crop trees
                if (st.tr[i].out < 1 && st.tr[i].crop == true) {
                    for (int j = 0; j < st.ntrees; j++) {
                        //competitor has to be a crop tree                            
                        if (st.tr[j].out < 0 && st.tr[j].crop == true && st.tr[i].no.compareTo(st.tr[j].no) != 0 && !st.tr[j].habitat) {
                            //caluclate distance between crop trees                                
                            dist_trees = Math.sqrt(Math.pow(st.tr[i].x - st.tr[j].x, 2.0) + Math.pow(st.tr[i].y - st.tr[j].y, 2.0));

                            //calculate height of considered trees in 1/3 of the height their crown
                            //geändert index auf i von j
                            h66_i = st.tr[i].cb + (st.tr[i].h - st.tr[i].cb) / 3;

                            //tree (j) can only be a competitor 
                            // if it is higher than potentially pressed tree (i) in its h66
                            if (h66_i < st.tr[j].h) {
                                //potentially pressed tree (i) is higher than tree (j)
                                //dist_min= (st.tr[j].calculateCwAtHeight(h66_i)+ st.tr[i].calculateCwAtHeight(h66_i))/2;
                                dist_min = (st.tr[i].calculateCwAtHeight(h66_i) + st.tr[j].calculateCwAtHeight(h66_i)) / 2;
                            } // tree is no competitor
                            else {
                                dist_min = 0.0;
                            }

                            if (harvested < vmaxharvest
                                    && dist_trees < dist_min
                                    && (st.tr[j].sp.trule.targetDiameter - st.tr[j].d) <= (st.tr[i].sp.trule.targetDiameter - st.tr[i].d)) {
                                st.tr[j].out = st.year;
                                st.tr[j].outtype = OutType.HARVESTED;
                                vout += st.tr[j].fac * st.tr[j].v;
                                harvested += st.tr[j].fac * st.tr[j].v;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * clearcut of all overstory trees h >= st.h100*0.4 all trees marked outtype
     * = 99 Stand status is set to 99
     *
     * @param st Stand
     */
    public void harvestClearCut(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 1 && st.tr[i].h >= (st.tr[i].si * 0.6) && !st.tr[i].habitat) {
                if (getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                    break;
                }
                st.tr[i].out = st.year;
                st.tr[i].outtype = OutType.HARVESTED;
                //st.tr[i].no+="_ks";
            }
        }
        //clear all remaining trees if wanted
        if (st.trule.onPlantingRemoveAllTrees) {
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && !st.tr[i].habitat) {
                    if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                        break;
                    }
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = OutType.HARVESTED;
                    //st.tr[i].no+="_ks";
                }
            }
        }
        st.status = 1;
    }

    public double percentOfBasalAreaAboveTargetDiameter(Stand st) {
        // Check basal area of target trees
        double perc = 0.0;
        double sum = 0.0;
        double sumTarget = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && !st.tr[i].habitat) {
                sum += Math.pow(st.tr[i].d, 2.0) * st.tr[i].fac;
                if (st.tr[i].d > st.tr[i].sp.trule.targetDiameter) {
                    sumTarget += Math.pow(st.tr[i].d, 2.0) * st.tr[i].fac;
                }
            }
        }
        if (sum > 0) {
            perc = sumTarget / sum;
        }
        return perc;
    }

    /**
     * shelter wood harvest Harvest target diameter trees according to
     * st.regenerationProcess the desired degree of stocking is determined by
     * st.status
     *
     * @param st stand object
     */
    public void harvestSchirmschlag(Stand st) {
        String rp = st.trule.regenerationProcess;
        String[] rpArray = rp.split(";");
        double degree = harvestingDegree(st, rpArray);
        double baHarv = 0.0;
        double baOut;
        if (degree > 0.0) {
            //auch beim Schirmschlag ModerateThinningFactor berücksichtigen -> entspricht so
            //etwa dem alten ET-Bestockungsgrad 1.0, der um 1-degree Prozent reduzuiert wird
            baOut = st.bha - getMaxStandBasalArea(st, true) * degree;
            if (baOut < 0.0) {
                baOut = 0.0;
            }
            while (baHarv < baOut) {
                // Harvest crop trees
                double max = -9999999.0;
                int merk = -9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && !st.tr[i].habitat && st.tr[i].d >= (st.tr[i].sp.trule.targetDiameter * 0.3)) {
                        double diff = st.tr[i].d - st.tr[i].sp.trule.targetDiameter;
                        if (max < diff) {
                            merk = i;
                            max = diff;
                        }
                    }
                }
                // break if degree to small or no harvest tree found (merk=-9)
                if ((getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || merk == -9) {
                    break;
                }
                st.tr[merk].out = st.year;
                st.tr[merk].outtype = OutType.HARVESTED;
                //st.tr[merk].no+="_ss";
                baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
            }
        }
        //20.05.2014
        if (degree == 0.0) {
            if (Double.parseDouble(rpArray[rpArray.length - 1]) == 0) {
                harvestRemainingTrees(st, true, -1, 1.3);
            } else {
                st.status = 1;
                st.trule.standTypeAtStatus1 = -1;
            }
        }
    }

    /**
     * Realitic version of target diameter harvesting process. The time frame
     * and the desired basal area are given by the st.regenerationProcess As
     * soon as this harvesting regime is started the basal area will be reduced
     * to the level given by st.trule.regenerationProcess, i.e. 0.7;0.4;0.2;0.0;
     * means that in the first time step the basal will be lower to 0.7, in the
     * second to 0.4, and so on of the normal basal area. The harvesting is
     * organised in that way, that 50 % of the basal which needs to be removed
     * will be gained of non crop trees by random selection. The other 50 % rsp.
     * the rest of the basal area is gained by crop trees from the lower end.
     *
     * Harvest target diameter trees according to st.regenerationProcess the
     * desired degree of stocking is determined by st.status
     *
     * @param st stand object
     */
    public void harvestTargetDiameterInPeriod(Stand st) {
        String rp = st.trule.regenerationProcess;
        String[] rpArray = rp.split(";");
        double degree = harvestingDegree(st, rpArray);
        double baHarv = 0.0;
        double baOut, baOut50;
        if (degree > 0.0) {
            //auch beim Schirmschlag ModerateThinningFactor berücksichtigen -> entspricht so
            //etwa dem alten ET-Bestockungsgrad 1.0, der um 1-degree Prozent reduzuiert wird
            baOut = st.bha - getMaxStandBasalArea(st, true) * degree;
            if (baOut < 0.0) {
                baOut = 0.0;
            }
            baOut50 = baOut * 0.5;
            baHarv = harvestCropTrees(baHarv, baOut50, st);           
            //
            // Entnahme der Crop Trees von unten
            //            
            while (baHarv < baOut) {
                // Harvest crop trees
                double min = Double.POSITIVE_INFINITY;
                int merk = -9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].d >= 7.0
                            && !st.tr[i].habitat && st.tr[i].crop) {
                        double diff = st.tr[i].d - st.tr[i].sp.trule.targetDiameter;
                        if (min > diff) {
                            merk = i;
                            min = diff;
                        }
                    }
                }
                // break if degree to small or no harvest tree found(merk=-9)
                if ((getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || merk == -9) {
                    break;
                }
                st.tr[merk].out = st.year;
                st.tr[merk].outtype = OutType.HARVESTED;
                //st.tr[merk].no+="_ss";
                baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
            }
            harvestCropTrees(baHarv, baOut, st); // Harvest crop trees
            //double max = Double.NEGATIVE_INFINITY;
            // remove first 50% basal area of non crop trees
            // break if degree to small or no harvest tree found (merk=-9)
        }
        //20.05.2014
        if (degree == 0.0) {
            if (Double.parseDouble(rpArray[rpArray.length - 1]) == 0) {
                this.harvestRemainingTrees(st, true, -1, 1.3);
            } else {
                st.status = 1;
                st.trule.standTypeAtStatus1 = -1;
            }
        }
    }

    protected double harvestingDegree(Stand st, String[] rpArray) {
        double degree = 0d;
        int index = st.status - 1;
        if (index >= 0 && index < rpArray.length) {
            if (rpArray[index] != null) {
                try {
                    degree = Double.parseDouble(rpArray[index]);
                } catch (NumberFormatException e) {
                }
            }
        }
        if (degree == 0.0) {
            st.status = 98;
        }
        st.status++;
        return degree;
    }

    protected double harvestCropTrees(double baHarv, double baOutLimit, Stand st) {
        while (baHarv < baOutLimit) {
            // Harvest crop trees
            //double max = Double.NEGATIVE_INFINITY;
            int merk = -9;
            // remove first 50% basal area of non crop trees
            int count = 0;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && !st.tr[i].habitat && st.tr[i].crop == false) {
                    count++;
                }
            }
            // break if degree to small or no harvest tree found (merk=-9)
            if ((getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || count <= 0) {
                break;
            }
            int kill = (int) Math.floor(st.random.nextUniform() * count);
            count = 0;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && !st.tr[i].habitat && st.tr[i].crop == false) {
                    if (count == kill) {
                        merk = i;
                    }
                    count++;
                }
            }
            st.tr[merk].out = st.year;
            st.tr[merk].outtype = OutType.HARVESTED;
            baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
        }
        return baHarv;
    }

    /**
     * harvest all remaining trees or only overstory trees this is used for
     * final removal *
     *
     * @param st stand object
     * @param overstoryOnly
     * @param protect_spec
     * @param min_height
     */
    public void harvestRemainingTrees(Stand st, boolean overstoryOnly, int protect_spec, double min_height) {
        double hx = min_height;
        double prot_height = 0;
        //old: 0.4;
        for (int i = 0; i < st.nspecies; i++) {
            // no grass hbon!!!
            if (st.sp[i].code != 999) {
                if (overstoryOnly) {
                    hx = st.sp[i].hbon * 0.6;
                }
                prot_height = st.sp[i].hbon * 0.2;
                break;
            }
        }

        for (int i = 0; i < st.ntrees; i++) {
            // if not overstory only remove grass
            if (!overstoryOnly) {
                if (st.tr[i].code == 999) {
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = OutType.HARVESTED;
                }
            }
            if (st.tr[i].out < 1 && st.tr[i].h >= hx && st.tr[i].habitat == false) {
                if (getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                    break;
                }
                // bei nachbesserung die gew. art schützen, wenn sie kleiner
                // als die definierte höhe ist
                if (protect_spec > 0) {
                    if (st.tr[i].code == protect_spec && st.tr[i].h < prot_height) {
                        continue;
                    }
                }

                st.tr[i].out = st.year;
                st.tr[i].outtype = OutType.HARVESTED;
            }
        }
        st.status = 1;
        st.trule.standTypeAtStatus1 = -1;
    }
    
    private double reduceBaOut(Stand st, double maxBa) {
        double maxBasalAreaOut = st.bha - maxBa;
        double baFac = st.bha / maxBa;
        
        if (baFac > 1.2 && shouldReduce(st)) {
            maxBasalAreaOut = maxBasalAreaOut * (1.2 / baFac);
        }
        return maxBasalAreaOut;
    }    

    private boolean shouldReduce(Stand st) {
        // http://issuetracker.intranet:20002/browse/BWIN-57: why only the first species?
        final Species species = st.sp[0];
        return species.spDef.moderateThinning.shouldReduce(species);
    }

    /**
     * Returns maximum basal area for a treegross stand <code>Stand</code>. If
     * withModerateThinningFactor is <code>true</code> the natural maximum is
     * reduced with the ModerateThinningFactor and the calculated basal area is
     * similar to yield table basal area for degree of stocking 1.0.
     *
     * @param st <code>Stand</code>V
     * @param withModerateThinningFactor <code>boolean</code>      
     * @return maximum basal area or reduced maximum basal [m²/ha] area as
     * <code>double</code>
     */
    public double getMaxStandBasalArea(Stand st, boolean withModerateThinningFactor) {
        double maxBA = 0.0;
        for (int i = 0; i < st.nspecies; i++) {
            Tree atree = new Tree();
            atree.sp = st.sp[i];
            atree.st = st;
            atree.d = st.sp[i].d100;
            atree.h = st.sp[i].h100;
            atree.age = (int) Math.round(st.sp[i].h100age);
            atree.cw = atree.calculateCw();
            atree.code = st.sp[i].code;
            //bei allen Durchforstungen:
            if (withModerateThinningFactor) {
                maxBA += atree.calculateMaxBasalArea() * atree.getModerateThinningFactor()
                        * (st.sp[i].percCSA / 100.0);
            } else {
                maxBA += atree.calculateMaxBasalArea() * (st.sp[i].percCSA / 100.0);
            }
        }
        return maxBA;
    }

    /**
     * Check all trees if they are no crop tree, if they are competing with a
     * crop tree Mesurement: is the overlap of the crown This void needs
     * CropTreeSelection
     *
     * @param st stand object
     */
    public void thinCropTreeCompetition(Stand st) {

        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;

        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }

        if (vmaxthinning > 0) {
        // Thinning is done iteratively tree by tree
            // 1. Calculate the overlap of all crop trees
            // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
            //    Heidi Doebbeler and crown width functions
            // 3. Find tree with the highest differenz in overlap - tolerable overlap
            // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
            // 5. Start with 1. again

            double intensity = 2.0 - st.trule.thinningIntensity;
            if (intensity == 0.0) {
                intensity = 1.0;
            }
            //Festlegen der Grundflächenansenkung
            double maxStandBasalArea = getMaxStandBasalArea(st, true);

            if (st.trule.thinningIntensity == 0.0) {
                maxStandBasalArea = maxStandBasalArea * 100.0;
            } else {
                maxStandBasalArea = maxStandBasalArea * (2.0 - st.trule.thinningIntensity);
            }

            //double maxBasalAreaOut = st.bha - maxStandBasalArea;
            double maxBasalAreaOut = reduceBaOut(st, maxStandBasalArea);

            boolean doNotEndThinning = true;
            if (maxBasalAreaOut <= 0.0) {
                doNotEndThinning = false;
            } else {
                do {
// update competition overlap for crop trees 
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].crop) {
                            st.tr[i].updateCompetition();
                        }
                    }
// find crop with most competition, defined as that tree with greates ratio of
// actual c66xy devided by maximum c66
                    int indexOfCroptree = -9;
                    double maxCompetition = -99999.9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].crop) {
                            // calculate maxc66
                            double maxBasalArea = st.tr[i].calculateMaxBasalArea() * st.tr[i].getModerateThinningFactor();
                            if (st.trule.thinningIntensity == 0.0) {
                                maxBasalArea = maxBasalArea * 100.0;
                            } else {
                                maxBasalArea = maxBasalArea * (2.0 - st.trule.thinningIntensity);
                            }
                            double maxN = maxBasalArea / (Math.PI * Math.pow((st.tr[i].d / 200.0), 2.0));
                            double maxC66 = maxN * Math.PI * Math.pow((st.tr[i].cw / 2.0), 2.0) / 10000.0;
                            double c66Ratio = st.tr[i].c66xy / maxC66;
                            // remember tree if c66Ratio is greater than maxCompetition
                            if (c66Ratio > maxCompetition) {
                                indexOfCroptree = i;
                                maxCompetition = c66Ratio;
                            }
                        }
                    }
// release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
// crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
// into the limit of twice the crown radius of the crop tree size 
//                 
// Find neighbor who comes closest 
                    if (indexOfCroptree < 0) {
                        doNotEndThinning = false;
                    } else {
                        double dist = 9999.0;
                        int merk = -9;
                        double h66 = st.tr[indexOfCroptree].cb;
                        for (int i = 0; i < st.tr[indexOfCroptree].nNeighbor; i++) {
                            if (st.tr[st.tr[indexOfCroptree].neighbor[i]].d > 7
                                    && st.tr[st.tr[indexOfCroptree].neighbor[i]].out < 0
                                    && (st.trule.cutCompetingCropTrees || st.tr[st.tr[indexOfCroptree].neighbor[i]].crop == false)
                                    && st.tr[st.tr[indexOfCroptree].neighbor[i]].habitat == false) {
                                double radius = st.tr[st.tr[indexOfCroptree].neighbor[i]].calculateCwAtHeight(h66) / 2.0;
                                double ent = Math.sqrt(Math.pow(st.tr[indexOfCroptree].x - st.tr[st.tr[indexOfCroptree].neighbor[i]].x, 2.0)
                                        + Math.pow(st.tr[indexOfCroptree].y - st.tr[st.tr[indexOfCroptree].neighbor[i]].y, 2.0));
                                if ((ent - radius < st.tr[indexOfCroptree].cw * (0.75 / intensity)) && dist > (ent - radius)) {
                                    merk = st.tr[indexOfCroptree].neighbor[i];
                                    dist = ent - radius;
                                }
                            }
                        }
                        // if merk > 9 then cut tree else stop crop tree release
                        if (merk == -9) {
                            doNotEndThinning = false;
                        } else {
                            st.tr[merk].out = st.year;
                            st.tr[merk].outtype = OutType.THINNED;
                            thinned += (st.tr[merk].fac * st.tr[merk].v);
                            maxBasalAreaOut = maxBasalAreaOut - (st.tr[merk].fac * Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0)) / st.size;
                            if (maxBasalAreaOut <= 0.0) {
                                doNotEndThinning = false;
                            }
                        }
                    }
                } //stop if max thinning amount is reached or all competitors are taken out
                while (thinned < vmaxthinning && doNotEndThinning);
            }
        }
    }

    
    /**
     * Thinning by Q-D-Rule is a specail thinning from Rheinland-Pfalz The
     * selected crop trees are released so that there will beno crown contact
     * after one growing cyle. In this routine it is simlypied the crop trees
     * get 25 cm distance to the crown. This strong thinning is performed when
     * the height of the crop tree is between greater 35% or smalerequal 80% of
     * the site-index height at age 100. If the height is 100% only trees will
     * be removed which have contact to the crown of the crop tree. The thinning
     * method assumes that there is no competition of other crop trees and that
     * the number of crop trees is low
     *
     * @param st
     */
    public void thinByQD(Stand st) {
        double ent;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 0 && st.tr[i].crop) {
                // we found a crop tree and remove now all other trees
                for (int j = 0; j < st.ntrees; j++) {
                    if (st.tr[j].d > 7 && st.tr[j].out < 0 && st.tr[j].crop == false && st.tr[j].habitat == false) {
                        ent = Math.sqrt(Math.pow(st.tr[i].x - st.tr[j].x, 2.0) + Math.pow(st.tr[i].y - st.tr[j].y, 2.0));
                        if (st.tr[i].h > st.tr[i].si * 0.8) {
                            if (ent < 0.9 * (st.tr[i].cw + st.tr[j].cw) / 2.0 && st.tr[j].h * 1.1 > st.tr[i].cb) {
                                st.tr[j].out = st.year;
                                st.tr[j].outtype = OutType.THINNED;
                            }
                        }
                        if (st.tr[i].h > st.tr[i].si * 0.3 && st.tr[i].h < st.tr[i].si * 0.8) {
                            if (ent < 0.20 + (st.tr[i].cw + st.tr[j].cw) / 2.0) {
                                st.tr[j].out = st.year;
                                st.tr[j].outtype = OutType.THINNED;
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Will perform a thinning until given volume is reached        
    public void removethinCropTreeCompetition(Stand st, int species, double volout) {
        // Thinning is done iteratively tree by tree
        // 1. Calculate the overlap of all crop trees
        // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
        //    Heidi Doebbeler and crown width functions
        // 3. Find tree with the highest differenz in overlap - tolerable overlap
        // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
        // 5. Start with 1. again 

        double intensity = 3.0;
        if (intensity == 0.0) {
            intensity = 1.0;
        }

        double volRem = 0.0;
        boolean doNotEndThinning = true;
        if (volRem >= volout) {
            doNotEndThinning = false;
        } else {
            do {
// update competition overlap for crop trees 
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].crop) {
                        st.tr[i].updateCompetition();
                    }
                }
// find crop with most competition, defined as that tree with greates ratio of
// actual c66xy devided by maximum c66
                int indexOfCroptree = -9;
                double maxCompetition = -99999.9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].crop) {
// calculate maxc66
                        double maxBasalArea = st.tr[i].calculateMaxBasalArea() * st.tr[i].getModerateThinningFactor();
                        if (st.trule.thinningIntensity == 0.0) {
                            maxBasalArea = maxBasalArea * 100.0;
                        } else {
                            maxBasalArea = maxBasalArea * (2.0 - st.trule.thinningIntensity);
                        }
                        double maxN = maxBasalArea / (Math.PI * Math.pow((st.tr[i].d / 200.0), 2.0));
                        double maxC66 = maxN * Math.PI * Math.pow((st.tr[i].cw / 2.0), 2.0) / 10000.0;
                        double c66Ratio = st.tr[i].c66xy / maxC66;
// remember tree if c66Ratio is greater than maxCompetition
                        if (c66Ratio > maxCompetition) {
                            indexOfCroptree = i;
                            maxCompetition = c66Ratio;
                        }
                    }
                }
// release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
// crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
// into the limit of twice the crown radius of the crop tree size 
//                 
// Find neighbor who comes closest 
                if (indexOfCroptree < 0) {
                    doNotEndThinning = false;
                } else {
                    double dist = 9999.0;
                    int merk = -9;
                    double h66 = st.tr[indexOfCroptree].cb;
                    for (int i = 0; i < st.tr[indexOfCroptree].nNeighbor; i++) {
                        if (st.tr[st.tr[indexOfCroptree].neighbor[i]].d > 7 && st.tr[st.tr[indexOfCroptree].neighbor[i]].code == species
                                && st.tr[st.tr[indexOfCroptree].neighbor[i]].out < 0 && (st.trule.cutCompetingCropTrees
                                || st.tr[st.tr[indexOfCroptree].neighbor[i]].crop == false) && st.tr[i].habitat == false) {
                            double radius = st.tr[st.tr[indexOfCroptree].neighbor[i]].calculateCwAtHeight(h66) / 2.0;
                            double ent = Math.sqrt(Math.pow(st.tr[indexOfCroptree].x - st.tr[st.tr[indexOfCroptree].neighbor[i]].x, 2.0)
                                    + Math.pow(st.tr[indexOfCroptree].y - st.tr[st.tr[indexOfCroptree].neighbor[i]].y, 2.0));
                            if ((ent - radius < st.tr[indexOfCroptree].cw * (0.75 / intensity)) && dist > (ent - radius)) {
                                merk = st.tr[indexOfCroptree].neighbor[i];
                                dist = ent - radius;
                            }
                        }
                    }
// if merk > 9 then cut tree else stop crop tree release                    
                    if (merk == -9) {
                        doNotEndThinning = false;
                    } else {
                        st.tr[merk].out = st.year;
                        st.tr[merk].outtype = OutType.THINNED;
                        volRem = volRem - (st.tr[merk].fac * st.tr[merk].v) / st.size;
                        if (volRem >= volout) {
                            doNotEndThinning = false;
                        }
                    }
                }

            } //stop if max thinning amount is reached or all competitors are taken out
            while (volRem <= volout && doNotEndThinning);
        }
// if goal not reached remove other non crop trees of species 
        if (volRem <= volout) {
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && st.tr[i].crop == false && st.tr[i].code == species) {
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = OutType.THINNED;
                    volRem = volRem - (st.tr[i].fac * st.tr[i].v) / st.size;
                    if (volRem >= volout) {
                        break;
                    }
                }
            }
        }
// if goal not reached remove  crop trees of species 
        if (volRem <= volout) {
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && st.tr[i].crop && st.tr[i].code == species) {
                    st.tr[i].out = st.year;
                    st.tr[i].outtype = OutType.THINNED;
                    volRem = volRem - (st.tr[i].fac * st.tr[i].v) / st.size;
                    if (volRem >= volout) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Check all trees if they are no crop tree, if they are competing with a
     * crop tree Mesurement: a-value This void needs CropTreeSelection Thinning
     * degree for each species is taken from st.tr[j].sp.trule.thinningIntensity
     *
     * @param st stand object
     */
    public void thinTempCropTreeCompetition(Stand st) {
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;

        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }

        if (vmaxthinning > 0) {
        // Thinning is done iteratively tree by tree
            // 1. Calculate the overlap of all crop trees
            // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
            //    Heidi Doebbeler and crown width functions
            // 3. Find tree with the highest differenz in overlap - tolerable overlap
            // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
            // 5. Start with 1. again 

//
//      Festlegen der Grundflächenansenkung   
            double maxStandBasalArea = getMaxStandBasalArea(st, true);

            if (st.trule.thinningIntensity == 0.0) {
                maxStandBasalArea = maxStandBasalArea * 100.0;
            } else {
                maxStandBasalArea = maxStandBasalArea * (2.0 - st.trule.thinningIntensity);
            }

            //double maxBasalAreaOut = st.bha - maxStandBasalArea;
            double maxBasalAreaOut = reduceBaOut(st, maxStandBasalArea);
            
            double intensity = st.trule.thinningIntensity;
            if (intensity == 0.0) {
                intensity = 1.0;
            }

            boolean doNotEndThinning = true;
            if (maxBasalAreaOut <= 0.0) {
                doNotEndThinning = false;
            } else {
                do {
// update competition overlap for crop trees 
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].tempcrop) {
                            st.tr[i].updateCompetition();
                        }
                    }
// find crop with most competition, defined as that tree with greates ratio of
// actual c66xy devided by maximum c66
                    int indexOfCroptree = -9;
                    double maxCompetition = -99999.9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].tempcrop) {
// calculate maxc66
                            double maxBasalArea = st.tr[i].calculateMaxBasalArea() * st.tr[i].getModerateThinningFactor();
                            if (st.trule.thinningIntensity == 0.0) {
                                maxBasalArea = maxBasalArea * 100.0;
                            } else {
                                maxBasalArea = maxBasalArea * (2.0 - st.trule.thinningIntensity);
                            }
                            double maxN = maxBasalArea / (Math.PI * Math.pow((st.tr[i].d / 200.0), 2.0));
                            double maxC66 = maxN * Math.PI * Math.pow((st.tr[i].cw / 2.0), 2.0) / 10000.0;
                            double c66Ratio = st.tr[i].c66xy / maxC66;
// remember tree if c66Ratio is greater than maxCompetition
                            if (c66Ratio > maxCompetition) {
                                indexOfCroptree = i;
                                maxCompetition = c66Ratio;
                            }
                        }
                    }
// release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
// crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
// into the limit of twice the crown radius of the crop tree size 
//                 
// Find neighbor who comes closest 
                    if (indexOfCroptree < 0) {
                        doNotEndThinning = false;
                    } else {
                        double dist = 9999.0;
                        int merk = -9;
                        double h66 = st.tr[indexOfCroptree].cb;
                        for (int i = 0; i < st.tr[indexOfCroptree].nNeighbor; i++) {
                            if (st.tr[st.tr[indexOfCroptree].neighbor[i]].d < 7
                                    && st.tr[st.tr[indexOfCroptree].neighbor[i]].out < 0
                                    && (st.trule.cutCompetingCropTrees || st.tr[st.tr[indexOfCroptree].neighbor[i]].tempcrop == false)
                                    && !st.tr[st.tr[indexOfCroptree].neighbor[i]].habitat) {
                                double radius = st.tr[st.tr[indexOfCroptree].neighbor[i]].calculateCwAtHeight(h66) / 2.0;
                                double ent = Math.sqrt(Math.pow(st.tr[indexOfCroptree].x - st.tr[st.tr[indexOfCroptree].neighbor[i]].x, 2.0)
                                        + Math.pow(st.tr[indexOfCroptree].y - st.tr[st.tr[indexOfCroptree].neighbor[i]].y, 2.0));
                                if ((ent - radius < st.tr[indexOfCroptree].cw * (0.75 / intensity)) && dist > (ent - radius)) {
                                    merk = st.tr[indexOfCroptree].neighbor[i];
                                    dist = ent - radius;
                                }
                            }
                        }
// if merk > 9 then cut tree else stop crop tree release                    
                        if (merk == -9) {
                            doNotEndThinning = false;
                        } else {
                            st.tr[merk].out = st.year;
                            st.tr[merk].outtype = OutType.THINNED;
                            thinned = thinned + (st.tr[merk].fac * st.tr[merk].v);
                            maxBasalAreaOut = maxBasalAreaOut - (st.tr[merk].fac * Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0)) / st.size;
                            if (maxBasalAreaOut <= 0.0) {
                                doNotEndThinning = false;
                            }
                        }
                    }

                } //stop if max thinning amount is reached or all competitors are taken out
                while (thinned < vmaxthinning && doNotEndThinning);
            }
        }
        // System.out.println("");
    }

    public void thinCompetitionFromAbove(Stand st) {
        //set max thinning volume (vmaxthinning) if outaken amount (vout)
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;

        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }

        if (vmaxthinning > 0) {
            // Thinning is done iteratively tree by tree
            // 1. Calculate the overlap of all crop trees
            // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
            //    Heidi Doebbeler and crown width functions
            // 3. Find tree with the highest differenz in overlap - tolerable overlap
            // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
            // 5. Start with 1. again

            // Festlegen der Grundflächenabsenkung
            st.bha = 0.0;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out == -1) {
                    st.bha += Math.PI * Math.pow(st.tr[i].d / 200.0, 2.0) * st.tr[i].fac;
                }
            }
            st.bha = st.bha / st.size;

            double maxStandBasalArea = getMaxStandBasalArea(st, true);

            if (st.trule.thinningIntensity == 0.0) {
                maxStandBasalArea = maxStandBasalArea * 100.0;
            } else {
                maxStandBasalArea = maxStandBasalArea * (2.0 - st.trule.thinningIntensity);
            }

            //double maxBasalAreaOut = st.bha - maxStandBasalArea;
            double maxBasalAreaOut = reduceBaOut(st, maxStandBasalArea);

            boolean doNotEndThinning = true;
            if (maxBasalAreaOut <= 0.0) {
                doNotEndThinning = false;
            } else {
                do {
                    // find non crop tree with most competition to other trees, defined as that the maximum overlap area for neighbor trees
                    int indextree = -9;
                    double maxOverlap = -99999.9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].d > 7 && st.tr[i].out < 0 && st.tr[i].crop == false && st.tr[i].tempcrop == false && st.tr[i].habitat == false) {
                            double ovlp = 0.0;
                            for (int j = 0; j < st.tr[i].nNeighbor; j++) {
                                double distance = Math.sqrt(Math.pow(st.tr[i].x - st.tr[st.tr[i].neighbor[j]].x, 2.0)
                                        + Math.pow(st.tr[i].y - st.tr[st.tr[i].neighbor[j]].y, 2.0));
                                double ri = st.tr[i].cw / 2.0;
                                double rj = st.tr[st.tr[i].neighbor[j]].cw / 2.0;
                                // only if there is an overlap and ri > rj
                                if (ri + rj > distance && ri > rj) {
                                    ovlp += overlap(rj, ri, distance);
                                }
                            }
                            if (ovlp > maxOverlap) {
                                maxOverlap = ovlp;
                                indextree = i;
                            }
                        }
                    }

                    // release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
                    // crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
                    // into the limit of twice the crown radius of the crop tree size
                    // if merk > 9 then cut tree else stop crop tree release
                    if (indextree == -9) {
                        doNotEndThinning = false;
                    } else {
                        st.tr[indextree].out = st.year;
                        st.tr[indextree].outtype = OutType.THINNED;
                        thinned = thinned + (st.tr[indextree].fac * st.tr[indextree].v);
                        maxBasalAreaOut = maxBasalAreaOut - (st.tr[indextree].fac * Math.PI * Math.pow(st.tr[indextree].d / 200.0, 2.0)) / st.size;
                        if (maxBasalAreaOut <= 0.0) {
                            doNotEndThinning = false;
                        }
                    }
                } //stop if max thinning amount is reached or all competitors are taken out
                while (thinned < vmaxthinning && doNotEndThinning);
            }
        }
    }

    public void thinFromBelow(Stand st) {
        //Max. Harvestvolume is defined
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning = st.size * st.trule.maxThinningVolume - thinned;

        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size * st.trule.maxOutVolume - vout) < vmaxthinning) {
            vmaxthinning = st.size * st.trule.maxOutVolume - vout;
        }

        if (vmaxthinning > 0) {
            // calculate max. density
            double baToTakeOut;

            double maxG = getMaxStandBasalArea(st, true);

            if (st.trule.thinningIntensity == 0.0) {
                maxG = maxG * 100.0;
            } else {
                maxG = maxG * (2.0 - st.trule.thinningIntensity);
            }

            //baToTakeOut = st.bha - maxG;            
            baToTakeOut = reduceBaOut(st, maxG);

            double baout = 0.0;
            boolean doNotEndThinning = true;
            do {
                // update competition overlap for crop trees
                double dmin = 99999.9;
                int merk = -9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0
                            && dmin > st.tr[i].d
                            && !st.tr[i].habitat) {
                        dmin = st.tr[i].d;
                        merk = i;
                    }
                }
                // Baum entfernen, sofern eine Überlappung besteht
                if (merk > -1 && baout < baToTakeOut) {
                    st.tr[merk].out = st.year;
                    st.tr[merk].outtype = OutType.THINNED;
                    thinned = thinned + st.tr[merk].fac * st.tr[merk].v;
                    baout = baout + (st.tr[merk].fac * Math.PI * Math.pow((st.tr[merk].d / 200.0), 2.0)) / st.size;
                } else {
                    doNotEndThinning = false;
                }

                if (baout >= baToTakeOut) {
                    doNotEndThinning = false;
                }
            } //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && doNotEndThinning);
        }
    }

    /**
     * return total volume per ha taken put during year (harvested, thinned and fallen)
     *
     * @param st stand object
     * @return the volume taken out  */
    public double getTotalOutVolume(Stand st) {
        double outvolume = 0.0;
        for (int i = 0; i < st.ntrees; i++) //volume sum of outaken (thinned or harvested) and died trees in the current year
        {
            if (st.tr[i].out == st.year) {
                outvolume = outvolume + st.tr[i].fac * st.tr[i].v;
            }
        }
        return outvolume / st.size;
    }

    /**
     * return total volume per ha taken put during year (thinned, harvested)
     *
     * @param st stand object
     * @return tratment volume
     */
    public double getTreatmentOutVolume(Stand st) {
        double volume = 0.0;
        for (int i = 0; i < st.ntrees; i++) //volume sum of outaken (thinned or harvested) trees in the current year            
        {
            if (st.tr[i].out == st.year && st.tr[i].outtype.treated()) {
                volume = volume + st.tr[i].fac * st.tr[i].v;
            }
        }
        return volume / st.size;
    }

    /**
     * return total volume per ha taken put during year(harvested)
     *
     * @param st stand object
     * @return volume taken out by harvesing
     */
    public double getHarvestedOutVolume(Stand st) {
        double volume = 0.0;
        for (int i = 0; i < st.ntrees; i++) //volume sum of harvested trees in the current year               
        {
            if (st.tr[i].out == st.year && st.tr[i].outtype == OutType.HARVESTED) {
                volume = volume + st.tr[i].fac * st.tr[i].v;
            }
        }
        return volume / st.size;
    }

    /**
     * return total volume per ha taken put during year (thinned)
     *
     * @param st stand object
     * @return volume taken out by thinning
     */
    public double getThinnedOutVolume(Stand st) {
        double volume = 0.0;
        for (int i = 0; i < st.ntrees; i++) //volume sum of thinned trees in the current year               
        {
            if (st.tr[i].out == st.year && st.tr[i].outtype == OutType.THINNED) {
                volume = volume + st.tr[i].fac * st.tr[i].v;
            }
        }
        return volume / st.size;
    }

    /**
     * returns number of crop trees per ha selected
     *
     * @param st treegross.base.stand object
     * @return crop tree number
     */
    public double getNCropTrees(Stand st) {
        double nCT = 0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].crop == true && st.tr[i].out == -1) {
                nCT++;
            }
        }
        return nCT;
    }

    /**
     * unselect all trees taken out during harvesting that year if min ammount
     * for treatment per ha has not been exceeded
     *
     * @param st treegross.base.stand object
     */
    public void checkMinHarvestVolume(Stand st) {
        if (getHarvestedOutVolume(st) < st.trule.minHarvestVolume) {
            for (int i = 0; i < st.ntrees; i++) {
                //reset harvested trees in the current year
                if (st.tr[i].out == st.year && st.tr[i].outtype == OutType.HARVESTED && !st.tr[i].outBySkidtrail) {
                    st.tr[i].out = -1;
                    st.tr[i].outtype = OutType.STANDING;
                }
            }
        }
    }

    /**
     * unselect all trees taken out during treatment that year if min ammount
     * for treatment per ha has not been exceeded
     *
     * @param st treegross.base.stand object
     */
    public void checkMinThinningVolume(Stand st) {
        if (getThinnedOutVolume(st) < st.trule.minThinningVolume) {
            for (int i = 0; i < st.ntrees; i++) {
                //reset thinned trees in the current year
                if (st.tr[i].out == st.year && st.tr[i].outtype == OutType.THINNED && !st.tr[i].outBySkidtrail) {
                    st.tr[i].out = -1;
                    st.tr[i].outtype = OutType.STANDING;
                }
            }
        }
    }

    /**
     * unselect all trees taken out that year if min ammount for treatment per
     * ha has not been exceeded
     *
     * @param st stand object
     */
    public void checkMinTreatmentOutVolume(Stand st) {
        if (this.getTreatmentOutVolume(st) < st.trule.minOutVolume * st.size) {
            for (int i = 0; i < st.ntrees; i++) {
                //reset (thinned or harvested) trees in the current year
                if (st.tr[i].out == st.year && st.tr[i].outtype.treated() && !st.tr[i].outBySkidtrail) {
                    st.tr[i].out = -1;
                    st.tr[i].outtype = OutType.STANDING;
                }
            }
        }
    }

    /**
     * sets harvsetigPeriode=0 to start time mesurement for harvesting cycle
     * @param st the stand
     */
    public void setStartHarvestingYears(Stand st) {
        st.trule.harvestingYears = 0;
    }

    /**
     * returns year, in which a harvesting action was performed to stand
     * @param st the stand
     */
    public void setHarvestingYearsAddStep(Stand st) {
        st.trule.harvestingYears = st.trule.harvestingYears + st.trule.treatmentStep;
    }

    /**
     * calculate overlap area of two circle only if they overlap
     */
    private double overlap(double r1, double r2, double e) {
        double x, y, f;
        f = 0.0;
        //r1 should always be the smaller radius
        if (r1 > r2) {
            x = r1;
            r1 = r2;
            r2 = x;
        }

        if (e - (r1 + r2) >= 0) {
            f = 0.0;   //no overlap area =0
        }
        // partly or full overlap
        if (e - (r1 + r2) < 0) {
            if (e + r1 <= r2) {
                f = Math.PI * r1 * r1;
            } //full overlap
            else {
                x = (Math.pow(e, 2.0) + Math.pow(r1, 2.0) - Math.pow(r2, 2.0)) / (2.0 * e);
                y = Math.sqrt(Math.pow(r1, 2.0) - Math.pow(x, 2.0));
                f = r1 * r1 * Math.acos((e * e + r1 * r1 - r2 * r2) / (2 * e * r1)) + r2 * r2 * Math.acos((e * e + r2 * r2 - r1 * r1) / (2 * e * r2)) - e * y;
            }
        }
        return f;
    }

    /*
     * create skidtrails 
     */
    public void createSkidtrails(Stand st) {
        if (st.sp[0].trule.minCropTreeHeight * 0.8 < st.sp[0].h100) {
            double xmin = Double.POSITIVE_INFINITY;
            double xmax = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < st.ncpnt; i++) {
                if (st.cpnt[i].x < xmin) {
                    xmin = st.cpnt[i].x;
                }
                if (st.cpnt[i].x > xmax) {
                    xmax = st.cpnt[i].x;
                }
            }
            xmin = xmin - st.trule.skidtrails.distance / 2.0;
            do {
                xmin = xmin + st.trule.skidtrails.distance;
                double x2 = xmin + st.trule.skidtrails.width;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].x > xmin && st.tr[i].x < x2) {
                        st.tr[i].out = st.year;
                        st.tr[i].outBySkidtrail = true;
                        if (st.tr[i].d < st.tr[i].sp.spDef.targetDiameter) {
                            st.tr[i].outtype = OutType.THINNED;
                        } else {
                            st.tr[i].outtype = OutType.HARVESTED;
                        }
                    }
                }
            } while (xmin < xmax);
        }
    }

    /**
     * mark trees as habitat trees by diameter
     * @param st the stand
     */
    public void markTreesAsHabitatTreesByDiameter(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 0 && st.tr[i].d >= st.trule.treeProtectedfromBHD && !st.tr[i].habitat) {
                st.tr[i].habitat = true;
                //st.tr[i].no+="_mb";
            }
        }
    }

    public int numberOfCropTrees(Species sp, double diameter, double percentage) {
        Tree atree = new Tree();
        atree.code = sp.code;
        atree.sp = sp;
        atree.d = diameter;
        atree.h = sp.hg;
        double dist_ct;
        dist_ct = atree.calculateCw();
        //Number of crop trees dependent on calcualted distance and actual mixture percent
        return (int) ((10000.0 / ((Math.PI * Math.pow(dist_ct, 2.0)) / 4)) * percentage / 100.0);
    }

    public void removeTargetTreesBySpecies(Stand st, int species, double volume) {
        //set max harvest volume (vmaxharvest) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        vmaxharvest = st.size * volume;
        // reset crop tress
        resetCropTrees(st);
        // Select all trees of species as crop trees
        for (int i = 0; i < st.nspecies; i++) {
            if (species == st.sp[i].code) {
                st.sp[i].trule.numberCropTreesWanted = 1000;
                st.sp[i].trule.targetCrownPercent = 99.9;
                st.sp[i].trule.targetDiameter = 7.0;
                st.sp[i].trule.minCropTreeHeight = 8.0;
            } else {
                st.sp[i].trule.numberCropTreesWanted = 0;
                st.sp[i].trule.targetCrownPercent = 0.1;
                st.sp[i].trule.targetDiameter = 200.0;
                st.sp[i].trule.minCropTreeHeight = 12.0;
            }
        }
        selectNCropTrees(st);
        if (vmaxharvest > 0) {
            sortStandByTargetDiameter(st);

            /**
             * distance of two trees
             */
            double dist_trees, dist_min;

            /**
             * height for the cross section of crop tree cb(i)+ (h(i)+cb(i)/3)
             */
            double h66_i;
            /**
             * cb(j)+ (h(j)+cb(j)/3)
             */

            for (int i = 0; i < st.ntrees; i++) {
                //check only crop trees for competing crop trees
                if (st.tr[i].out < 1 && st.tr[i].crop) {
                    for (int j = 0; j < st.ntrees; j++) {
                        //competitor has to be a crop tree                            
                        if (st.tr[j].out < 0 && st.tr[j].crop && st.tr[i].no.equals(st.tr[j].no) == false) {
                            //caluclate distance between crop trees                                
                            dist_trees = Math.sqrt(Math.pow(st.tr[i].x - st.tr[j].x, 2.0) + Math.pow(st.tr[i].y - st.tr[j].y, 2.0));

                            //calculate height of considered trees in 1/3 of the height their crown
                            h66_i = st.tr[i].cb + (st.tr[i].h - st.tr[i].cb) / 3;

                            //tree (j) can only be a competitor 
                            // if it is higher than potentially pressed tree (i) in its h66
                            if (h66_i < st.tr[j].h) {
                                //potentially pressed tree (i) is higher than tree (j)
                                //calcualte minimal distance, trees has to have to be competitive
                                // from crown of higher competitor in its h66(j)
                                dist_min = (st.tr[i].calculateCwAtHeight(h66_i) + st.tr[j].calculateCwAtHeight(h66_i)) / 2;
                            } // tree is no competitor
                            else {
                                dist_min = 0.0;
                            }

                            if (harvested < vmaxharvest
                                    && dist_trees < dist_min
                                    && (st.tr[j].sp.trule.targetDiameter - st.tr[j].d) <= (st.tr[i].sp.trule.targetDiameter - st.tr[i].d)) {
                                st.tr[j].out = st.year;
                                st.tr[j].outtype = OutType.HARVESTED;
                                vout = vout + st.tr[j].fac * st.tr[j].v;
                                harvested += st.tr[j].fac * st.tr[j].v;
                            }
                        }
                    }
                }
            }
        }
    }

    public double getDegreeOfCover(int code, Stand st, boolean overstoryOnly) {
        double degree;
        double ks = 0.0;

        double hx = 0;
        if (overstoryOnly) {
            //old: 0.4;
            for (int i = 0; i < st.nspecies; i++) {
                // no grass hbon!!!
                if (st.sp[i].code != 999) {
                    hx = st.sp[i].hbon * 0.4;
                    break;
                }
            }
        }

        for (int j = 0; j < st.ntrees; j++) {
            if ((code == st.tr[j].code || code == 0) && st.tr[j].out < 0 && st.tr[j].h > hx && st.tr[j].code < 900) {
                ks += st.tr[j].fac * Math.PI * Math.pow(st.tr[j].cw / 2.0, 2.0);
            }
        }
        degree = ks / (10000.0 * st.size);
        return degree;
    }

    public void startPlanting(Stand st) {
        // generate placeholder trees
        String ps = st.trule.plantingString;
        if (ps == null) {
            return;
        }
        if (ps.length() == 0) {
            return;
        }

        int art;
        String[] psa = ps.split(";");
        String[] sp_perc;
        for (String psa1 : psa) {
            sp_perc = psa1.split("\\[");
            art = Integer.parseInt(sp_perc[0]);
            double ha = Double.parseDouble(sp_perc[1].substring(0, sp_perc[1].length() - 1).trim());
            //System.out.println("planting: "+art+" ->"+ha);
            //  site index aus genutzten Bäumen und auch hbon ermitteln
            double site = 25.0;
            if (art > 200 && art < 400) {
                site = 28.0;
            }
            if (art > 200 && art < 300) {
                site = 28.0;
            }
            if (art > 500 && art < 600) {
                site = 31.0;
            }
            if (art > 600 && art < 700) {
                site = 40.0;
            }
            if (art > 700 && art < 800) {
                site = 25.0;
            }
            if (art > 800 && art < 900) {
                site = 31.0;
            }
            for (int j = 0; j < st.ntrees; j++) {
                if (art == st.tr[j].code && site < st.tr[j].si) {
                    site = st.tr[j].si;
                }
            }
            double spcov = getDegreeOfCover(art, st, false);
            // get crown width at dbh = 7 cm of species at point of ingrowth            
            Tree atree = new Tree(art, "atree", 20, -1, OutType.STANDING, 7.0, 8.0, 2.0, 0.0, -99, 1.0, 0.0, 0.0, 0.0, false, false,
                    false, 3, 0.0, "");
            try {
                atree.sp = st.addspecies(atree);
            } catch (SpeciesNotDefinedException ex) {
                LOGGER.log(Level.SEVERE, "treegross", ex);
            }
            double cbx = atree.calculateCw();
            double gx = Math.PI * Math.pow(cbx / 2.0, 2.0);
            // number missing of placeholders (npl)
            int npl = 0;
            if (spcov < ha) {
                npl = (int) Math.round(10000.0 * (ha - spcov) / gx);
            }
            // maximum number of regeneration placeholders is 3000
            if (npl > 3000) {
                npl = 3000;
            }
            npl = (int) Math.round(npl * st.size);
            // System.out.println("auto Pflanzen: art anzahl "+art+npl);
            double ra = st.random.nextUniform();
            // create trees
            for (int j = 0; j < npl; j++) {
                try {
                    if (!st.addTreeFromPlanting(art, "p" + st.ntrees + "_" + st.year, 5, -1, 0.25, 0.5 * ra, 0.1, cbx, site, -9.0, -9.0, 0, 0, 0, 0)) {
                        break;
                    }
                } catch (SpeciesNotDefinedException e) {
                    LOGGER.log(Level.SEVERE, "treegross", e);
                }
            }
            for (int i = 0; i < st.ntrees; i++) {
                st.tr[i].setMissingData();
            }
            GenerateXY xy = new GenerateXY();
            xy.zufall(st);
        }
    }
}
