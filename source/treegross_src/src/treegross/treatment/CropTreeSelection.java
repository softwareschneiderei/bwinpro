/* 
 * @(#) CropTreeSelection.java  
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

import treegross.base.*;

/**
 * @author	Henriette Duda for more information see: Duda, H. (2006): Vergleich
 * forstlicher Managementstrategien. Dissertation Universität Göttingen, S. 180
 * http://webdoc.sub.gwdg.de/diss/2006/duda/ sort trees into layers by given
 * definitions
 *
 * class CropTreeSelection selects a certain amount of crop trees from stand It
 * starts with the strongest tree and the species with the least basal area.
 * When selecting the next crop tree there has to be a minimum distance to the
 * next crop tree. The percentage of crop tree for the species is defines by the
 * percBA.
 *
 * This routine can be only used, if : - all trees have fac = 1.0 - all trees
 * have x,y coordinates
 */
public class CropTreeSelection {

    public CropTreeSelection() {
    }

    /**
     * method select starts the selection process, it expects the number of crop
     * trees per species, which you load first with method addCtsp() thickest
     * trees are selected first, habitat trees can not become crop trees
     *
     * @param st
     * @param ctsp
     */
    public void selectCropTrees(Stand st, CropTreeSpecies ctsp[]) {

        /**
         * number of wanted crop trees of that species
         */
        int nCTSpTarget;
        /**
         * index of potential new crop tree
         */
        int j;
        /**
         * flag for end of selection
         */
        boolean endselection;
        /**
         * flag for end of selection within taht species
         */
        boolean enoughtreesofspecies;
        /**
         * flag for selected tree per round (check of all trees)
         */
        boolean selectedtree;
        /**
         * flag for selected species
         */
        boolean selectedspecies;
        /**
         * remember speciesindex
         */
        int rIndex;
        /**
         * sum of wanted crop trees over all aspecies
         */
        int sumCT;

        // sort st.tr by diameter descending        
        Tree trtemp;
        for (int a = 0; a < st.ntrees - 1; a++) {
            for (int b = a + 1; b < st.ntrees; b++) {
                if (st.tr[a].d < st.tr[b].d) {
                    trtemp = st.tr[a];
                    st.tr[a] = st.tr[b];
                    st.tr[b] = trtemp;
                }
            }
        }
        // Sort species that the species with the least crop trees will be first
        CropTreeSpecies ctsptemp;
        for (int a = 0; a < st.nspecies - 1; a++) {
            for (int b = a + 1; b < st.nspecies; b++) {
                if (ctsp[a].nha > ctsp[b].nha) {
                    ctsptemp = ctsp[a];
                    ctsp[a] = ctsp[b];
                    ctsp[b] = ctsptemp;
                }
            }
        }
        // count sum of wanted crop trees over all aspecies
        sumCT = 0;
        for (int a = 0; a < st.nspecies; a++) {
            //number of wanted crop tree of that species
            int nCTSp_a = (int) (ctsp[a].nha * st.size);
            sumCT = sumCT + nCTSp_a;
        }

        //average distance of crop trees is dependant on stand size and sum of crop trees
        //double averageDist=2.0*Math.sqrt(st.size*10000.0/(Math.PI*sumct));           
        endselection = false;
        /*enoughtreesofspecies=false;
         selectedtree=false;
         selectedspecies=false;*/
        rIndex = 0;
        //j=0;        

        //this loop runs until endselection=true; 
        do {
            //true means all crop trees wanted are selected            
            selectedspecies = true;

            for (int i = 0; i < st.nspecies; i++) {

                //new round starts with first tree
                j = 0;
                //no tree selected this round
                selectedtree = false;

                // number of wanted crop trees of that species
                // rounded to full trees in stand
                nCTSpTarget = (int) (ctsp[i].nha * st.size);

                // number of already selected crop trees of species
                int nselectedcroptrees = getNcroptrees(st, ctsp[i].code);

                // if number of wanted crop trees of species is reached
                enoughtreesofspecies = nselectedcroptrees >= nCTSpTarget;

                // select another tree of that species
                //if not enough trees of that species are selected yet
                if (enoughtreesofspecies == false) {
                    selectedtree = false;
                    //do until tree is found, or all trees have been checked
                    do {
                        //if layer (that contains most of trees of this species) is high enough
                        if (st.ntrees > 0 && st.h100 >= ctsp[i].min_height) {
                            //search for potential crop tree 
                            //potential crop tree has to be:
                            // alive, 
                            // of that species,
                            // no crop tree yet,
                            // no habitat tree
                            // higher than min crop tree height
                            if (ctsp[i].code == st.tr[j].code
                                    && st.tr[j].d >= 7.0
                                    && st.tr[j].outtype <= 0
                                    && st.tr[j].crop == false
                                    && st.tr[j].habitat == false
                                    && st.tr[j].h > st.tr[j].sp.trule.minCropTreeHeight) {
                                //find nearest already selected crop or habitat tree to potential crop tree
                                //set distance to nearstest crop/habitat tree alive to 9999999
                                double distNearest = Double.POSITIVE_INFINITY;
                                // find nearest crop or habitat tree alive (remember distance)                                
                                for (int m = 0; m < st.ntrees; m++) {
                                    if ((st.tr[m].crop && st.tr[m].outtype <= 0)
                                            || (st.tr[m].habitat && st.tr[m].outtype <= 0)) {
                                        //distance potential crop tree to considered tree
                                        double dist = Math.sqrt(Math.pow(st.tr[j].x - st.tr[m].x, 2.0)
                                                + Math.pow(st.tr[j].y - st.tr[m].y, 2.0));
                                        // remember distance and species if considered tree is the nearest found yet
                                        if (dist < distNearest) {
                                            distNearest = dist;
                                            // find and remember speciesindex of nearest croptree in crop tress species list
                                            for (int n = 0; n < st.nspecies; n++) {
                                                if (ctsp[n].code == st.tr[m].code) {
                                                    rIndex = n;
                                                }
                                            }
                                        }
                                    }
                                }
                                //select tree if nearest crop tree is far enough away
                                // "far enough" means: both crowns may not interfere with each other when trees have reached
                                // target diameter (ctsp[i].dist= crown diameter of crop tree with target diameter)
                                // distance has to be at least 
                                //    radius tree i (having reached target diameter) + radius tree m (having reached target diameter)
                                if (distNearest > ((ctsp[i].dist * 0.5) + (ctsp[rIndex].dist * 0.5))) {
                                    //System.out.println(" CTS Anzahl selected "+j+" "+" "+averageDist);
                                    st.tr[j].crop = true;
                                    selectedtree = true;
                                }
                            }
                        }
                        j++;
                    } //round ends, if tree is found or all trees have been checked
                    while (selectedtree == false && j < st.ntrees);
                }
                // if it was possible to find a tree last round 
                // and there is the requirement of an other tree of that species start new selection round
                if (selectedtree) {
                    selectedspecies = false;
                }

            }// for-cycle through all species
            // if crop trees for last species have been selected or no futher tree of that species can be found
            if (selectedspecies) {
                endselection = true;
            }

        }//do
        while (endselection == false);
        //actualize n_croptrees
        for (int i = 0; i < st.nspecies; i++) {
            getNcroptrees(st, ctsp[i].code);
        }
    }

    /**
     * method select starts the selection process, it expects the number of crop
     * trees per species, which you load first with method addCtsp() thickest
     * trees are selected first
     *
     * @param st
     * @param ctsp
     */
    public void selectTempCropTrees(Stand st, CropTreeSpecies ctsp[]) {
        /**
         * number of wanted crop trees of that species
         */
        int nCTSpTarget;
        /**
         * index of potential new temp crop tree
         */
        int j;
        /**
         * flag for end of selection
         */
        boolean endselection;
        /**
         * flag for end of selection within that species
         */
        boolean enoughtreesofspecies;
        /**
         * flag for selected tree per round (check of all trees)
         */
        boolean selectedtree;
        /**
         * flag for selected species
         */
        boolean selectedspecies;
        /**
         * remember speciesindex
         */
        int rIndex;
        /**
         * sum of wanted temp crop trees over all aspecies
         */
        int sumCT;
        // sort st.tr by diameter descending        
        Tree trtemp;
        for (int a = 0; a < st.ntrees - 1; a++) {
            for (int b = a + 1; b < st.ntrees; b++) {
                if (st.tr[a].d < st.tr[b].d) {
                    trtemp = st.tr[a];
                    st.tr[a] = st.tr[b];
                    st.tr[b] = trtemp;
                }
            }
        }
        // Sort species that the species with the least temp crop trees will be first
        CropTreeSpecies ctsptemp;
        for (int a = 0; a < st.nspecies - 1; a++) {
            for (int b = a + 1; b < st.nspecies; b++) {
                if (ctsp[a].nha > ctsp[b].nha) {
                    ctsptemp = ctsp[a];
                    ctsp[a] = ctsp[b];
                    ctsp[b] = ctsptemp;
                }
            }
        }

        // count sum of wanted temp crop trees over all aspecies
        sumCT = 0;
        for (int a = 0; a < st.nspecies; a++) {
            //number of wanted crop tree of that species
            int nCTSp_a = (int) (ctsp[a].nha * st.size);
            sumCT = sumCT + nCTSp_a;
        }

        //average distance of temp crop trees is dependant on stand size and sum of temp crop trees
        //double averageDist=2.0*Math.sqrt(st.size*10000.0/(Math.PI*sumct));           
        endselection = false;
        /*enoughtreesofspecies=false;
         selectedtree=false;
         selectedspecies=false;*/
        rIndex = 0;
        //j=0;        

        //this loop runs until endselection=true; 
        do {
            //true means all temp crop trees wanted are selected            
            selectedspecies = true;

            for (int i = 0; i < st.nspecies; i++) {
                //new round starts with first tree
                j = 0;
                //no tree selected- this round
                selectedtree = false;
                // number of wanted temp crop trees of that species
                // rounded to full trees in stand
                nCTSpTarget = (int) (ctsp[i].nha * st.size);

                // number of already selected temp crop trees of species
                int nselectedcroptrees = getNcroptrees(st, ctsp[i].code);

                // if number of wanted temp crop trees of species is reached
                enoughtreesofspecies = nselectedcroptrees >= nCTSpTarget;

                // select another tree of that species
                //if not enough trees of that species are selected yet
                if (enoughtreesofspecies == false) {
                    selectedtree = false;
                    //do until tree is found, or all trees have been checked                    
                    do {
                        //if layer (that contains most of trees of this species) is high enough
                        if (st.ntrees > 0 && st.h100 >= ctsp[i].min_height) {
                            //search for potential temp crop tree 
                            //potential temp crop tree has to be:
                            // alive, 
                            // of that species,
                            // no crop tree yet,
                            // no temp crop tree yet,
                            // no habitat tree
                            // higher than min temp crop tree height
                            if (ctsp[i].code == st.tr[j].code
                                    && st.tr[j].outtype <= 0
                                    && st.tr[j].crop == false
                                    && st.tr[j].tempcrop == false
                                    && st.tr[j].habitat == false) {
                                //find nearest already selected (temp)crop or habitat tree to potential temp crop tree
                                //set distance to nearest (temp)crop/habitat tree alive to 9999999
                                double distNearest = 99999.9;
                                // find nearest (temp)crop or habitat tree alive (remember distance)                                
                                for (int m = 0; m < st.ntrees; m++) {
                                    if ((st.tr[m].crop == true && st.tr[m].outtype <= 0)
                                            || (st.tr[m].tempcrop == true && st.tr[m].outtype <= 0)
                                            || (st.tr[m].habitat == true && st.tr[m].outtype <= 0)) {
                                        //distance potential temp crop tree to considered tree
                                        double dist = Math.sqrt(Math.pow(st.tr[j].x - st.tr[m].x, 2.0)
                                                + Math.pow(st.tr[j].y - st.tr[m].y, 2.0));
                                        // remember distance and species if considered tree is the nearest found yet
                                        if (dist < distNearest) {
                                            distNearest = dist;
                                            // find and remember speciesindex of nearest (temp)crop/habitat tree in temp crop trees species list
                                            for (int n = 0; n < st.nspecies; n++) {
                                                if (ctsp[n].code == st.tr[m].code) {
                                                    rIndex = n;
                                                }
                                            }
                                        }
                                    }
                                }

                                //select tree if nearest crop tree is far enough away
                                // "far enough" means: matches between two crop trees
                                if (distNearest > ((ctsp[i].dist / 2) + (ctsp[rIndex].dist / 2))) {
                                    //System.out.println(" CTS Anzahl selected "+j+" "+" "+averageDist);
                                    st.tr[j].tempcrop = true;
                                    selectedtree = true;
                                }
                            }
                        }
                        j++;

                    } //round ends, if tree is found or all trees have been checked
                    while (selectedtree == false && j < st.ntrees);
                }
                // if it was possible to find a tree last round 
                // and there is the requirement of an other tree of that species start new selection round
                if (selectedtree) {
                    selectedspecies = false;
                }

            }// for-cycle through all species
            // if crop trees for last species have been selected or no futher tree of that species can be found
            if (selectedspecies) {
                endselection = true;
            }

        }//do
        while (!endselection);
    }

    /**
     * get number of croptrees you get all crop trees by giving 0 for species
     * code
     *
     * @param st
     * @param spe
     * @return croptree number
     */
    public int getNcroptrees(Stand st, int spe) {
        int m = 0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 0 && st.tr[i].crop == true && ((st.tr[i].code == spe) || (spe == 0))) {
                m++;
            }
        }
        return m;
    }

    /**
     * get number of sum croptrees + temp croptrees you get all crop trees by
     * giving 0 for species code
     *
     * @param st
     * @param spe
     * @return crop tree number
     */
    public int getNallcroptrees(Stand st, int spe) {
        int m = 0;
        for (int i = 0; i < st.ntrees; i++) {
            if ((st.tr[i].out < 0 && st.tr[i].crop == true && ((st.tr[i].code == spe) || (spe == 0)))
                    || (st.tr[i].out < 0 && st.tr[i].tempcrop == true && ((st.tr[i].code == spe) || (spe == 0)))) {
                m++;
            }
        }
        return m;
    }

    /**
     * cropt tree reset, cancels all selection of crop trees
     *
     * @param st
     */
    public void resetAllCropTrees(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].crop = false;
            st.tr[i].tempcrop = false;
        }
    }

    /**
     * cropt tree reset, cancels all selection of crop trees
     */
    void resetTempCropTrees(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].tempcrop = false;
        }
    }

    /**
     * cropt tree reset, cancels all selection of crop trees
     */
    void resetCropTrees(Stand st) {
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].crop = false;
        }
    }
}
