/*
 * @(#) ingrowth.java
 *  (c) 2002-2010 Juergen Nagel, Northwest German Research Station,
 *      Grätzelstr.2, 37079 Göttingen, Germany
 *      E-Mail: Juergen.Nagel@nw-fva.de
 *
 *  This program is free software; you can redistribute it &&/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */
/**
 * TreeGrOSS : class ingrowth predicts the amount of ingrowing tree for. a 5
 * year cycle. Ingrowth are tree which grow in diameter from below 7 cm to
 * greater or equal 7 cm. This is the major class for several other classes of
 * this package http://treegross.sourceforge.net
 *
 * @version 24-JUN-2011
 * @author	Juergen Nagel
 */
package treegross.base;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import treegross.random.RandomNumber;

public class Ingrowth2 implements PlugInIngrowth {

    double p0, p1, p2, p3, p4;
    double heightNew;
    int ageNew = 0;
    private final String igModelName = "Einwuchs Nordwestdeutschland";
    //RandomNumber rn = new RandomNumber();
    int[] igspecies = {111, 112, 113, 211, 221, 311, 321, 331, 365, 342, 354,
        411, 412, 421, 422, 430, 431, 441, 451, 452,
        511, 512, 513, 521, 525,/*531,*/ 611, 711, 731, 811, 812, 999};

    ArrayList<Integer> existingSpecies = new ArrayList<Integer>();

    int nExistingSpecies = 0;

    private final static Logger LOGGER = Logger.getLogger(Ingrowth2.class.getName());

    @Override
    public String getModelName() {
        return igModelName;
    }

    @Override
    public int checkSpecies(Stand st) throws Exception {
        int result = -99;
        for (int i = 0; i < st.nspecies; i++) {
            result = st.sp[i].code;
            for (int j = 0; j < igspecies.length; j++) {
                if (st.sp[i].code == igspecies[j]) {
                    result = -99;
                }
            }
        }
        return result;
    }

    /**
     * the ingrowth predicting only works for the species code of Lower Saxony.
     * You need to adjust this method if you use another species code. The
     * statistical data was derived from inventory plot data of Lower Saxony.
     *
     * @param st treegross Stand
     */
    @Override
    public void predictIngrowth(Stand st) {
        // minimum und maximum x,y coordinates to establish Regeneration points
        p0 = 0.0;
        p1 = 0.0;
        p2 = 0.0;
        p3 = 0.0;
        p4 = 0.0;

        double xmax = 0.0;
        double ymax = 0.0;
        double xmin = Double.POSITIVE_INFINITY;
        double ymin = Double.POSITIVE_INFINITY;

        for (int i = 0; i < st.ncpnt; i++) {
            if (st.cpnt[i].x > xmax) {
                xmax = st.cpnt[i].x;
            }
            if (st.cpnt[i].y > ymax) {
                ymax = st.cpnt[i].y;
            }
            if (st.cpnt[i].x < xmin) {
                xmin = st.cpnt[i].x;
            }
            if (st.cpnt[i].y < ymin) {
                ymin = st.cpnt[i].y;
            }
        }

        //double maxArea=(xmax-xmin)*(ymax-ymin);
        double intervall = Math.sqrt(500.0);
        int nRegenerationPoints = (int) (Math.ceil((xmax - xmin) / intervall) * Math.ceil((ymax - ymin) / intervall)); //500qm Plots 22.36m x 22.36m

        // Start checking all RegenrationPoints xr,yr
        double xr = xmin + intervall / 2.0;
        double yr = ymin + intervall / 2.0;
        for (int j = 0; j < nRegenerationPoints; j++) {
            // make RegenSubPlot
            double xrmin = xr - intervall / 2.0;
            double yrmin = yr - intervall / 2.0;
            double xrmax = xr + intervall / 2.0;
            double yrmax = yr + intervall / 2.0;
            // how much of the RegenSubPlotArea is with plot, we use a grid 10x10
            double xg = xrmin + intervall / 20.0;
            double yg = yrmin + intervall / 20.0;
            double percentInsidePlot = 0.0;
            for (int jj = 0; jj < 10; jj++) {
                for (int kk = 0; kk < 10; kk++) {
                    double xc = xg + jj * intervall / 10.0;
                    double yc = yg + kk * intervall / 10.0;
                    if (pointInPolygon(xc, yc, st) == true) {
                        percentInsidePlot = percentInsidePlot + 1.0;
                    }
                }
            }
            // at least 10% of subplot should be inside the plot
            if (percentInsidePlot > 10.0) {
                // calculate c66- class for of all trees below 7cm in subplot xr,yr
                double c66kl = 0.0;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].x >= xrmin && st.tr[i].x <= xrmax
                            && st.tr[i].y >= yrmin && st.tr[i].y <= yrmax) {
                        c66kl = c66kl + Math.PI * Math.pow(st.tr[i].cw / 2.0, 2.0);
                    }
                }
                c66kl = c66kl / (intervall * intervall * (percentInsidePlot / 100.0));
                c66kl = 0.1 + Math.round((c66kl - 0.1) * 5.0) / 5.0;
                if (c66kl < 0.0) {
                    c66kl = 0.1;
                }
                if (c66kl > 2.5) {
                    c66kl = 2.5;
                }
                // find dominant species
                int merk = 0;
                double baMax = 0.0;
                nExistingSpecies = 0;
                for (int i = 0; i < st.nspecies; i++) {
                    double baTemp = 0.0;
                    for (int ii = 0; ii < st.ntrees; ii++) {
                        if (st.tr[ii].out < 0 && st.tr[ii].x >= xrmin && st.tr[ii].x <= xrmax
                                && st.tr[ii].y >= yrmin && st.tr[ii].y <= yrmax
                                && st.tr[ii].code == st.sp[i].spDef.internalCode
                                && st.tr[ii].age > 50) {
                            baTemp = baTemp + Math.PI * Math.pow(st.tr[ii].cw / 2.0, 2.0);
                        }
                    }

                    // Fill array with existing species
                    if (baTemp > 0) {
                        existingSpecies.add(st.sp[i].code);
                        nExistingSpecies++;
                    }

                    if (baMax < baTemp) {
                        merk = i;
                        baMax = baTemp;
                    }
                }
                //added by jan hansen
                //catch an error if there is an empty stand
                int dominantSpecies = 0;
                if (st.sp[merk] != null) {
                    dominantSpecies = (int) (st.sp[merk].spDef.internalCode / 100);
//                    dominantSpecies=st.sp[merk].spDef.ingrowth;
                }
                // Vergrasung und Traubenkirsche Hessisches Ried
                c66kl = traubenkirsche(st, dominantSpecies, c66kl);
                c66kl = vergrasung(st, dominantSpecies, c66kl);
                //
                int anzahl = 0;
                // check if there should be ingrowth
                if (ingrowthYes(dominantSpecies, c66kl, st)) {
                    anzahl = (int) (Math.round(numberOfIngrowth(dominantSpecies, c66kl) * (percentInsidePlot / 100.0)));
                    //System.out.println("ingrowth");
                }

                for (int i = 0; i < anzahl; i++) {
                    // The treeCode of the new tree is taken from the existing tree with a possiblity of 50 %
                    int treeCode = -9;
                    double ranNum = st.random.nextUniform();
                    if (ranNum < 0.5 && nExistingSpecies > 0) {
                        /*ranNum = ranNum * 2.0;
                       
                         int merk2 = (int) (Math.floor(ranNum*nExistingSpecies));
                         if (merk2 > nExistingSpecies) merk2=nExistingSpecies;
                         if (merk2  < 0 ) merk2 = 0;*/
                        int merk2 = (int) Math.round(st.random.nextUniform() * (nExistingSpecies - 1));
                        //treeCode = existingSpecies[merk2];
                        treeCode = existingSpecies.get(merk2);
                    }
                    if (treeCode < 0) {
                        treeCode = getNewSpecies(st, dominantSpecies, c66kl);
                    }

                    // Änderung hessisches Ried treeCode kann negativ sein, dann wird kein Baum generiert
                    if (treeCode > 0) {
                        double siteIndex = 0.0;
                        // Find Site Index
                        int nSiteIndex = 0;
                        for (int ii = 0; ii < st.ntrees; ii++) {
                            if (st.tr[ii].code == treeCode && st.tr[ii].si > 0) {
                                siteIndex = siteIndex + st.tr[ii].si;
                                nSiteIndex++;
                            }
                        }
                        if (nSiteIndex > 0) {
                            siteIndex = siteIndex / nSiteIndex;
                        } else {
                            for (int ii = 0; ii < st.ntrees; ii++) {
                                if (st.tr[ii].si > 0 && st.tr[ii].code < 900) {
                                    siteIndex = siteIndex + st.tr[ii].si;
                                    nSiteIndex++;
                                }
                            }
                            siteIndex = siteIndex / nSiteIndex;
                        }
                        //  site index aus genutzten Baumen und auch hbon ermitteln
                        if (siteIndex == 0.0) {
                            siteIndex = 25.0;
                            if (treeCode > 200 && treeCode < 400) {
                                siteIndex = 28.0;
                            }
                            if (treeCode > 200 && treeCode < 300) {
                                siteIndex = 28.0;
                            }
                            if (treeCode > 500 && treeCode < 600) {
                                siteIndex = 31.0;
                            }
                            if (treeCode > 600 && treeCode < 700) {
                                siteIndex = 40.0;
                            }
                            if (treeCode > 700 && treeCode < 800) {
                                siteIndex = 25.0;
                            }
                            if (treeCode > 800 && treeCode < 900) {
                                siteIndex = 31.0;
                            }
                        }

                        if (treeCode != -1) {
                            try {
                                // Change of Model, we will start one tree as a placeholder for regeneration 5yr, dbh 0.25 cm, Height 0.5 m, cw = 5m2
                                st.addTreeFromNaturalIngrowth(treeCode, "v" + st.ntrees + "_" + st.year, 5, -1, 0.25, 0.5, 0.1, 2.52, siteIndex, -9.0, -9.0, 0, 0, 0, 0); //first is site index
                            } catch (SpeciesNotDefinedException ex) {
                                LOGGER.log(Level.SEVERE, "treegross", ex);
                            }
                        }
                    }
                }

                for (int ii = 0; ii < st.ntrees; ii++) {
                    st.tr[ii].setMissingData();
                }

                generateXYInSubplot(st, xrmin, xrmax, yrmin, yrmax);
            }
            xr += intervall;
            if (xr - intervall / 2.0 >= xmax) {
                xr = xmin + intervall / 2.0;
                yr += intervall;
            }
        }
    } // RegenerationPoints

    private boolean ingrowthYes(int domSp, double c66kl, Stand st) {
        double a = 0.0;
        double b = 0.0;
        if (domSp < 1) {
            domSp = 2;
        }
        if (domSp > 8) {
            domSp = 5;
        }
        if (domSp == 1) {
            a = 0.237;
            b = -0.6551;
        }
        if (domSp == 2) {
            a = 0.2551;
            b = -0.5288;
        }
        if (domSp == 3) {
            a = 0.2446;
            b = -0.4435;
        }
        if (domSp == 4) {
            a = 0.2446;
            b = -0.4435;
        }
        if (domSp == 5) {
            a = 0.1659;
            b = -0.6086;
        }
        if (domSp == 6) {
            a = 0.1800;
            b = -0.8022;
        }
        if (domSp == 7) {
            a = 0.2946;
            b = -0.2795;
        }
        if (domSp == 8) {
            a = 0.2829;
            b = -0.7482;
        }
        double ran = st.random.nextUniform();
        return (a * Math.pow(c66kl, b) > ran);
    }

    // Is there Ingrowth on a plot, dependent on c66kl and dominantSpecies
    private int numberOfIngrowth(int domSp, double c66kl) {
        double a = 0.0;
        double b = 0.0;
        if (domSp < 1) {
            domSp = 2;
        }
        if (domSp > 8) {
            domSp = 5;
        }
        if (domSp == 1) {
            a = 3.2874;
            b = -1.1275;
        }
        if (domSp == 2) {
            a = 3.14664;
            b = -0.94789;
        }
        if (domSp == 3) {
            a = 2.80772;
            b = -0.87383;
        }
        if (domSp == 4) {
            a = 2.80772;
            b = -0.87383;
        }
        if (domSp == 5) {
            a = 2.7331;
            b = -0.7096;
        }
        if (domSp == 6) {
            a = 2.7331;
            b = -0.7096;
        }
        if (domSp == 7) {
            a = 2.9338;
            b = -1.1701;
        }
        if (domSp == 8) {
            a = 2.5012;
            b = -0.4793;
        }
        return (int) (Math.exp(a + b * c66kl));
    }

    // Species
    private int getNewSpecies(Stand st, int domSp, double c66kl) {
        //System.out.println("Dominate Art"+domSp+"  "+c66kl);
        int speciesNew;//=211;
        double p[] = {0.254, 0.724, 0.752, 0.752, 0.752, 0.752, 0.921, 0.970, 0.970, 0.985,
            1.00, 1.0, 1.0, 1.00};
        int spe[] = {111, 112, 211, 221, 321, 342, 411, 411, 431, 451, 511, 711, 731, 811};
        double hnew[] = {7.0, 7.2, 7.4, 4.6, 6.8, 6.0, 7.2, 4.5, 5.0, 6.4, 5.6, 6.5, 3.1, 8.7};
        int anew[] = {18, 11, 22, 22, 22, 22, 10, 10, 10, 10, 20, 20, 20, 20};

        //int nspe=0;       
        if (domSp == 1) {
            if (c66kl > 1.6) {
                c66kl = 1.5;
            }
            int tspe[] = {111, 112, 211, 221, 321, 342, 411, 411, 431, 451, 511, 711, 731, 811};
            spe = tspe;
            double hmean[] = {7.0, 7.2, 7.4, 4.6, 6.8, 6.0, 7.2, 4.5, 5.0, 6.4, 5.6, 6.5, 3.1, 8.7};
            hnew = hmean;
            //nspe=14;

            if (c66kl >= 0.0 && c66kl < 0.2) {
                double tmp[] = {0.254, 0.724, 0.752, 0.752, 0.752, 0.752, 0.921, 0.970, 0.970, 0.985,
                    1.00, 1.0, 1.0, 1.0};
                int atmp[] = {18, 11, 22, 22, 22, 22, 10, 10, 10, 10, 20, 20, 20, 20};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.2 && c66kl < 0.4) {
                double tmp[] = {0.203, 0.811, 0.849, 0.862, 0.862, 0.862, 0.875, 0.875, 0.888, 0.901, 0.964,
                    0.989, 1.002, 1.002};
                int atmp[] = {18, 23, 25, 14, 14, 14, 23, 23, 15, 19, 24, 22, 25, 25};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.4 && c66kl < 0.6) {
                double tmp[] = {0.177, 0.673, 0.786, 0.821, 0.821, 0.821, 0.835, 0.870, 0.870, 0.898, 0.990,
                    0.997, 0.997, 0.997};
                int atmp[] = {19, 20, 23, 10, 10, 10, 20, 23, 23, 14, 19, 26, 26, 26};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.6 && c66kl < 0.8) {
                double tmp[] = {0.169, 0.662, 0.859, 0.887, 0.887, 0.887, 0.901, 0.901, 0.901, 0.915, 0.985,
                    0.985, 0.985, 0.999};
                int atmp[] = {20, 22, 21, 16, 16, 16, 15, 15, 15, 24, 25, 25, 25, 15};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.8 && c66kl < 1.0) {
                double tmp[] = {0.043, 0.532, 0.766, 0.787, 0.787, 0.787, 0.808, 0.808, 0.808, 0.851, 1.000,
                    1.000, 1.000, 1.000};
                int atmp[] = {22, 22, 38, 25, 25, 25, 19, 19, 19, 22, 17, 17, 17, 17};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.0 && c66kl < 1.2) {
                double tmp[] = {0.217, 0.608, 0.847, 0.890, 0.890, 0.933, 0.933, 0.933, 0.933, 0.955, 0.998,
                    0.998, 0.998, 0.998,};
                int atmp[] = {18, 20, 31, 23, 23, 18, 18, 18, 18, 20, 21, 21, 21, 21};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.2 && c66kl < 1.4) {
                double tmp[] = {0.000, 0.333, 0.666, 0.666, 0.666, 0.777, 0.777, 0.777, 0.777, 0.999, 0.999,
                    0.999, 0.999, 0.999};
                int atmp[] = {0, 22, 33, 23, 23, 18, 18, 18, 18, 28, 28, 28, 28, 28};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.4) {
                double tmp[] = {0.000, 0.250, 0.625, 0.750, 0.875, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000,
                    1.000, 1.000, 1.000};
                int atmp[] = {0, 21, 20, 23, 15, 21, 21, 21, 21, 21, 21, 21, 21, 21};
                anew = atmp;
                p = tmp;
            }
        }
        // dominant Species 2 beech type
        if (domSp > 1 && domSp < 3) {
            if (c66kl > 1.6) {
                c66kl = 1.5;
            }
            int tspe[] = {111, 112, 211, 221, 311, 321, 331, 342, 365, 411, 441, 451, 452, 511, 513, 521, 611, 711, 811};
            spe = tspe;
            double hmean[] = {7.4, 7.8, 7.3, 5.5, 15.3, 9.3, 9.2, 8.1, 8.3, 7.0, 7.6, 6.6, 3.2, 6.2, 6.9, 4.5, 6.6, 6.0, 7.1};
            hnew = hmean;
            //nspe=19;
            if (c66kl >= 0.0 && c66kl < 0.2) {
                double tmp[] = {0.007, 0.007, 0.918, 0.918, 0.918, 0.918, 0.918, 0.918, 0.918, 0.940, 0.944, 0.944, 0.944, 0.996, 0.996, 0.996, 0.996, 0.996, 1.000};
                int atmp[] = {24, 24, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 17, 17, 17, 17, 17, 9};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.2 && c66kl < 0.4) {
                double tmp[] = {0.008, 0.008, 0.942, 0.942, 0.942, 0.942, 0.942, 0.942, 0.942, 0.965, 0.965, 0.965, 0.965, 0.988, 0.988, 0.988, 0.992, 0.992, 1.000};
                int atmp[] = {22, 22, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 18, 18, 18, 10, 10, 28};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.4 && c66kl < 0.6) {
                double tmp[] = {0.000, 0.008, 0.919, 0.919, 0.919, 0.919, 0.919, 0.919, 0.923, 0.935, 0.935, 0.951, 0.951, 0.991, 0.995, 0.995, 0.995, 0.995, 0.999};
                int atmp[] = {0, 20, 22, 22, 22, 22, 22, 22, 19, 39, 39, 18, 18, 22, 35, 35, 35, 35, 23};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.6 && c66kl < 0.8) {
                double tmp[] = {0.030, 0.060, 0.872, 0.872, 0.872, 0.885, 0.889, 0.893, 0.893, 0.906, 0.906, 0.906, 0.906, 0.974, 0.974, 0.974, 0.983, 0.992, 1.000};
                int atmp[] = {25, 26, 30, 30, 30, 16, 17, 18, 18, 16, 16, 16, 16, 17, 17, 17, 9, 17, 7};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.8 && c66kl < 1.0) {
                double tmp[] = {0.046, 0.059, 0.934, 0.954, 0.954, 0.954, 0.954, 0.954, 0.954, 0.954, 0.954, 0.954, 0.954, 0.980, 0.980, 1.000, 1.000, 1.000, 1.000};
                int atmp[] = {26, 21, 30, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 22, 22, 12, 12, 12, 12};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.0 && c66kl < 1.2) {
                double tmp[] = {0.008, 0.008, 0.969, 0.969, 0.969, 0.969, 0.969, 0.969, 0.969, 0.969, 0.969, 0.977, 0.977, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000,};
                int atmp[] = {30, 30, 33, 33, 33, 33, 33, 33, 33, 33, 33, 34, 34, 32, 32, 32, 32, 32, 32,};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.2 && c66kl < 1.4) {
                double tmp[] = {0.000, 0.000, 0.961, 0.961, 0.969, 0.969, 0.969, 0.977, 0.977, 0.977, 0.977, 0.985, 0.985, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00};
                int atmp[] = {0, 0, 33, 33, 37, 37, 37, 24, 24, 24, 24, 37, 37, 126, 126, 126, 126, 126, 126};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.4) {
                double tmp[] = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                int atmp[] = {0, 0, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42};
                anew = atmp;
                p = tmp;
            }
        }
        // dominant Species 3+4
        if (domSp > 2 && domSp < 5) {
            if (c66kl > 1.4) {
                c66kl = 1.3;
            }
            int tspe[] = {111, 112, 211, 221, 311, 321, 342, 354, 411, 412, 421, 430, 431, 441, 451, 452, 511, 513, 611, 711, 811};
            spe = tspe;
            double hmean[] = {7.6, 8.1, 7.3, 3.7, 9.3, 8.1, 8.9, 7.3, 7.8, 8.7, 18.6, 4.4, 9.8, 7.5, 6.5, 5.7, 6.3, 9.8, 7.2, 7.5, 9.8};
            hnew = hmean;
            //nspe=21;
            if (c66kl >= 0.0 && c66kl < 0.2) {
                double tmp[] = {0.000, 0.051, 0.229, 0.229, 0.229, 0.263, 0.263, 0.271, 0.618, 0.643, 0.685, 0.685, 0.685, 0.710, 0.752, 0.752, 0.769, 0.769, 0.769, 0.989, 0.997};
                int atmp[] = {0, 16, 20, 20, 20, 12, 12, 10, 15, 33, 14, 14, 14, 10, 11, 11, 21, 21, 21, 12, 17};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.2 && c66kl < 0.4) {
                double tmp[] = {0.116, 0.195, 0.256, 0.262, 0.268, 0.268, 0.268, 0.268, 0.628, 0.701, 0.738, 0.744, 0.750, 0.768, 0.835, 0.835, 0.939, 0.939, 0.939, 1.000, 1.000};
                int atmp[] = {15, 17, 20, 11, 28, 28, 28, 28, 17, 14, 12, 10, 11, 9, 13, 13, 14, 14, 14, 16, 16};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.4 && c66kl < 0.6) {
                double tmp[] = {0.016, 0.016, 0.154, 0.154, 0.154, 0.178, 0.194, 0.194, 0.560, 0.576, 0.592, 0.592, 0.592, 0.600, 0.706, 0.706, 0.885, 0.893, 0.893, 0.991, 0.999};
                int atmp[] = {20, 20, 21, 21, 21, 18, 28, 28, 17, 23, 28, 28, 28, 18, 16, 16, 22, 28, 28, 16, 15};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.6 && c66kl < 0.8) {
                double tmp[] = {0.021, 0.032, 0.117, 0.117, 0.213, 0.266, 0.266, 0.266, 0.745, 0.745, 0.766, 0.766, 0.766, 0.766, 0.830, 0.830, 0.936, 0.936, 0.957, 1.000, 1.000};
                int atmp[] = {40, 20, 22, 22, 17, 17, 17, 17, 20, 20, 16, 16, 16, 16, 19, 19, 20, 20, 17, 20, 20};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.8 && c66kl < 1.0) {
                double tmp[] = {0.000, 0.028, 0.334, 0.334, 0.334, 0.445, 0.445, 0.445, 0.528, 0.528, 0.556, 0.556, 0.556, 0.556, 0.723, 0.723, 0.890, 0.890, 0.946, 1.002, 1.002};
                int atmp[] = {0, 13, 21, 21, 21, 32, 32, 32, 33, 33, 23, 23, 23, 23, 13, 13, 22, 22, 19, 33, 33};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.0 && c66kl < 1.2) {
                double tmp[] = {0.107, 0.107, 0.357, 0.357, 0.393, 0.429, 0.429, 0.429, 0.608, 0.608, 0.679, 0.679, 0.679, 0.679, 0.715, 0.751, 0.965, 0.965, 0.965, 1.001, 1.001,};
                int atmp[] = {25, 25, 35, 35, 52, 34, 34, 34, 25, 25, 18, 18, 18, 18, 20, 15, 20, 20, 20, 26, 26};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.2) {
                double tmp[] = {0.053, 0.053, 0.421, 0.421, 0.421, 0.474, 0.474, 0.474, 0.579, 0.579, 0.579, 0.579, 0.579, 0.579, 0.737, 0.737, 0.948, 0.948, 0.948, 0.948, 1.001};
                int atmp[] = {25, 25, 37, 37, 37, 23, 23, 23, 18, 18, 18, 18, 18, 18, 22, 22, 15, 15, 15, 15, 15};
                anew = atmp;
                p = tmp;
            }
        }

        // dominant Species 5 beech type
        if (domSp == 5) {
            if (c66kl > 1.6) {
                c66kl = 1.5;
            }
            int tspe[] = {112, 113, 211, 321, 411, 412, 421, 441, 451, 511, 512, 525,/*511,611,711,811,812,*/}; //551
            spe = tspe;
            double hmean[] = {6.9, 8.4, 8.0, 6.2, 7.7, 6.8, 7.6, 7.2, 7.6, 6.7, 5.6, 4.2, 7.4, 5.9, 7.9, 7.9, 10.5};
            hnew = hmean;
            //nspe=17;
            if (c66kl >= 0.0 && c66kl < 0.2) {
                double tmp[] = {0.000, 0.000, 0.065, 0.065, 0.102, 0.102, 0.102, 0.107, 0.107, 0.986, 0.986, 0.986, 0.986, 0.986, 1.000, 1.000, 1.000};
                int atmp[] = {0, 0, 20, 20, 14, 14, 14, 19, 19, 15, 15, 15, 15, 15, 15, 15, 15};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.2 && c66kl < 0.4) {
                double tmp[] = {0.031, 0.031, 0.080, 0.080, 0.127, 0.127, 0.132, 0.135, 0.148, 0.964, 0.967, 0.967, 0.967, 0.972, 0.995, 1.000, 1.000};
                int atmp[] = {22, 22, 27, 27, 18, 18, 12, 10, 18, 18, 29, 29, 29, 11, 19, 16, 16};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.4 && c66kl < 0.6) {
                double tmp[] = {0.016, 0.020, 0.134, 0.134, 0.250, 0.259, 0.259, 0.259, 0.270, 0.964, 0.964, 0.964, 0.966, 0.982, 0.995, 0.997, 0.999};
                int atmp[] = {22, 15, 27, 27, 18, 20, 20, 20, 17, 20, 20, 20, 5, 15, 17, 15, 10};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.6 && c66kl < 0.8) {
                double tmp[] = {0.003, 0.003, 0.152, 0.169, 0.218, 0.218, 0.218, 0.218, 0.256, 0.950, 0.950, 0.953, 0.953, 0.960, 0.995, 0.998, 0.998};
                int atmp[] = {11, 11, 29, 14, 22, 22, 22, 22, 22, 25, 25, 15, 15, 21, 22, 22, 22};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.8 && c66kl < 1.0) {
                double tmp[] = {0.000, 0.000, 0.101, 0.101, 0.163, 0.163, 0.163, 0.163, 0.192, 0.966, 0.966, 0.966, 0.966, 0.980, 0.999, 0.999, 0.999};
                int atmp[] = {0, 0, 46, 46, 21, 21, 21, 21, 23, 26, 26, 26, 26, 29, 29, 29, 29};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.0 && c66kl < 1.2) {
                double tmp[] = {0.000, 0.000, 0.254, 0.254, 0.282, 0.296, 0.296, 0.296, 0.352, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000};
                int atmp[] = {0, 0, 43, 43, 18, 18, 18, 18, 22, 24, 24, 24, 24, 24, 24, 24, 24};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.2 && c66kl < 1.4) {
                double tmp[] = {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
                int atmp[] = {0, 0, 34, 34, 34, 34, 34, 34, 34, 27, 27, 27, 27, 27, 27, 27, 27};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.4) {
                double tmp[] = {0.0, 0.0, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
                int atmp[] = {0, 0, 66, 66, 66, 66, 66, 66, 66, 31, 31, 31, 31, 31, 31, 31, 31};
                anew = atmp;
                p = tmp;
            }

        }
        // dominant Species 5 beech type
        if (domSp == 6) {
            if (c66kl > 1.0) {
                c66kl = 0.9;
            }
            int tspe[] = {111, 112, 211, 411, 511, 611, 711};
            spe = tspe;
            double hmean[] = {9.5, 6.6, 7.4, 7.4, 5.5, 7.2, 8.7};
            hnew = hmean;
            //nspe=7;
            if (c66kl >= 0.0 && c66kl < 0.2) {
                double tmp[] = {0, 0, 0, 0, 0, 1, 1};
                int atmp[] = {0, 0, 0, 0, 0, 8, 8};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.2 && c66kl < 0.4) {
                double tmp[] = {0.000, 0.000, 0.018, 0.036, 0.179, 1.000, 1.000};
                int atmp[] = {0, 0, 12, 9, 10, 8, 8};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.4 && c66kl < 0.6) {
                double tmp[] = {0.000, 0.071, 0.071, 0.071, 0.214, 0.928, 0.999};
                int atmp[] = {0, 18, 18, 18, 20, 11, 15};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.6 && c66kl < 0.8) {
                double tmp[] = {0.048, 0.048, 0.096, 0.191, 0.381, 1.000, 1.000};
                int atmp[] = {15, 15, 30, 15, 32, 16, 16};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.8) {
                double tmp[] = {0, 0, 0, 0, 0, 1, 1};
                int atmp[] = {0, 0, 0, 0, 0, 18, 18};
                anew = atmp;
                p = tmp;
            }
        }
        // dominant Species 7 beech type
        if (domSp == 7) {
            if (c66kl > 1.4) {
                c66kl = 1.3;
            }
            int tspe[] = {111, 112, 113, 211, 311, 411, 412, 421, 431, 441, 451, 511, 513, 521/*531*/, 611, 711, 811, 812};
            spe = tspe;
            double hmean[] = {7.4, 7.0, 6.7, 6.9, 7.0, 7.4, 9.4, 6.9, 2.2, 5.9, 6.9, 6.1, 4.9, 3.6, 6.2, 7.8, 7.5, 4.4};
            hnew = hmean;
            //nspe=18;
            if (c66kl >= 0.0 && c66kl < 0.2) {
                double tmp[] = {0.139, 0.139, 0.139, 0.139, 0.139, 0.153, 0.153, 0.153, 0.153, 0.153, 0.153, 0.167, 0.167, 0.167, 0.167, 1.000, 1.000, 1.000};
                int atmp[] = {17, 17, 17, 17, 17, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 14, 14};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.2 && c66kl < 0.4) {
                double tmp[] = {0.010, 0.074, 0.074, 0.077, 0.080, 0.202, 0.202, 0.202, 0.202, 0.205, 0.205, 0.384, 0.384, 0.384, 0.387, 0.995, 0.998, 0.998};
                int atmp[] = {20, 21, 21, 20, 35, 17, 17, 17, 17, 15, 15, 21, 21, 21, 11, 18, 15, 15};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.4 && c66kl < 0.6) {
                double tmp[] = {0.005, 0.012, 0.015, 0.024, 0.024, 0.135, 0.154, 0.154, 0.154, 0.154, 0.163, 0.413, 0.415, 0.417, 0.451, 0.996, 0.999, 1.001};
                int atmp[] = {17, 20, 12, 13, 13, 20, 13, 13, 13, 13, 18, 22, 25, 25, 14, 23, 12, 10};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.6 && c66kl < 0.8) {
                double tmp[] = {0.002, 0.007, 0.007, 0.042, 0.042, 0.194, 0.203, 0.205, 0.207, 0.207, 0.230, 0.580, 0.580, 0.580, 0.594, 0.994, 0.999, 0.999};
                int atmp[] = {25, 22, 22, 17, 17, 18, 18, 20, 20, 20, 21, 24, 24, 24, 22, 23, 18, 18};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.8 && c66kl < 1.0) {
                double tmp[] = {0.006, 0.006, 0.006, 0.031, 0.031, 0.156, 0.162, 0.168, 0.168, 0.168, 0.180, 0.680, 0.680, 0.680, 0.711, 0.999, 0.999, 0.999};
                int atmp[] = {25, 25, 25, 14, 14, 24, 15, 15, 15, 15, 25, 23, 23, 23, 20, 25, 25, 25};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.0 && c66kl < 1.2) {
                double tmp[] = {0.05, 0.05, 0.05, 0.05, 0.05, 0.50, 0.50, 0.50, 0.50, 0.50, 0.50, 0.95, 0.95, 0.95, 0.95, 1.00, 1.00, 1.00};
                int atmp[] = {25, 25, 25, 25, 25, 22, 22, 22, 22, 22, 22, 25, 25, 25, 25, 29, 29, 29};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.2) {
                double tmp[] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
                int atmp[] = {0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
                anew = atmp;
                p = tmp;
            }
        }
        // dominant Species 8 beech type
        if (domSp >= 8) {
            if (c66kl > 1.4) {
                c66kl = 1.3;
            }
            int tspe[] = {112, 211, 321, 411, 421, 422, 441, 451, 511, 611, 711, 811, 812};
            spe = tspe;
            double hmean[] = {6.7, 8.3, 6.1, 7.6, 7.1, 6.0, 9.6, 7.1, 6.7, 7.2, 7.6, 7.7, 8.3};
            hnew = hmean;
            //nspe=13;
            if (c66kl >= 0.0 && c66kl < 0.2) {
                double tmp[] = {0.083, 0.500, 0.500, 0.750, 0.750, 0.833, 0.833, 0.833, 0.833, 0.833, 0.916, 0.999, 0.999};
                int atmp[] = {11, 19, 19, 12, 12, 12, 12, 12, 12, 12, 16, 16, 16};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.2 && c66kl < 0.4) {
                double tmp[] = {0.022, 0.435, 0.435, 0.522, 0.522, 0.522, 0.544, 0.544, 0.587, 0.739, 0.782, 0.999, 0.999};
                int atmp[] = {10, 20, 20, 17, 17, 17, 9, 9, 30, 11, 20, 19, 19};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.4 && c66kl < 0.6) {
                double tmp[] = {0.014, 0.405, 0.434, 0.535, 0.564, 0.564, 0.564, 0.636, 0.882, 0.896, 0.925, 0.997, 0.997};
                int atmp[] = {23, 27, 12, 18, 12, 12, 12, 16, 24, 20, 8, 19, 19};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.6 && c66kl < 0.8) {
                double tmp[] = {0.000, 0.852, 0.852, 0.852, 0.852, 0.852, 0.852, 0.868, 0.901, 0.950, 0.966, 0.982, 0.998};
                int atmp[] = {0, 34, 34, 34, 34, 34, 34, 23, 28, 12, 20, 22, 27};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 0.8 && c66kl < 1.0) {
                double tmp[] = {0.000, 0.824, 0.824, 0.883, 0.883, 0.883, 0.883, 0.912, 0.971, 0.971, 0.971, 1.000, 1.000};
                int atmp[] = {0, 32, 32, 21, 21, 21, 21, 15, 20, 20, 20, 10, 10};
                anew = atmp;
                p = tmp;
            }
            if (c66kl >= 1.0) {
                double tmp[] = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                int atmp[] = {0, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
                anew = atmp;
                p = tmp;
            }
        }
        // dominant Species 6
        // dominant Species 6
        // dominant Species 6

        //RandomNumber rn = new RandomNumber();
        int nspe = spe.length;

        double ran = st.random.nextUniform();
        int merk = -1;
        if (ran <= p[0]) {
            merk = 0;    //Zustand 1
        }
        for (int i = 0; i < nspe - 1; i++) {
            if (ran > p[i] && ran <= p[i + 1]) {      //Zustand 2
                merk = i + 1;
            }
        }

        if (nspe > 0) {
            if (ran > p[nspe - 1]) {
                merk = nspe - 1;
            }
        }

        if (merk == -1) {
            merk = nspe;
        }

        speciesNew = spe[merk];
//      System.out.println("Ingrowth2  speciesNew "+speciesNew+"  ");

        // check if speciesNew in static and bhd > 15.0cm
        boolean seedTreeThere = false;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out < 0 && st.tr[i].code == speciesNew && st.tr[i].d > 15.0) {
                seedTreeThere = true;
            }
        }
        if (seedTreeThere == false) {
            ran = st.random.nextUniform();
            if (ran > 0.05 && (speciesNew < 400 || speciesNew > 500)) // added by jan hansen: check first if the stand has at least one species defined to avoid a nullpointer exception
            {
                if (st.sp[0] != null) {
                    speciesNew = st.sp[0].spDef.internalCode;
                }
            }
        }

        heightNew = hnew[merk];
        ageNew = anew[merk];
        if (heightNew <= 0.0 || ageNew == 0) {
            //System.out.print("fehler: Höhe =0.0");
            speciesNew = -1;
        }
        // Speziell Hessische Ried
        if (speciesNew < 400 && speciesNew >= 500) {
            speciesNew = -9;
        }

        return speciesNew;
    }

    /**
     * check if a point is in polygon , if return is 0 then outside
     */
    boolean pointInPolygon(double x, double y, Stand st) {
        int i, j, c, m;
        c = 0;
        m = st.ncpnt;
        boolean answer;//=false;
        //      System.out.println("pnpoly "+m+" "+x+" y "+y);
        j = m - 1;
        for (i = 0; i < m; i++) {
            if ((((st.cpnt[i].y <= y) && (y < st.cpnt[j].y))
                    || ((st.cpnt[j].y <= y) && (y < st.cpnt[i].y)))
                    && (x < (st.cpnt[j].x - st.cpnt[i].x) * (y - st.cpnt[i].y)
                    / (st.cpnt[j].y - st.cpnt[i].y) + st.cpnt[i].x)) {

                if (c == 0) {
                    c = 1;
                } else {
                    c = 0;
                }
            }
            j = i;
        }
        answer = c != 0;
        return answer;
    }

    public void generateXYInSubplot(Stand st, double xmin, double xmax, double ymin, double ymax) {
        //RandomNumber zz = new RandomNumber();
        //if random is off use fixed random strategy
        int oldRandom = st.random.getRandomType();
        if (!st.random.randomOn) {
            st.random.setRandomType(RandomNumber.PSEUDO_FIXED);
        }
        // determin xmin, xmax, ymin and ymax of area defined by corners
        int i, j, ic;
        double e;//=0.0;
        //double emin=0.0;
        //ic=0;
        // generate Coordinates for each tree
        double xg = 0.0;
        double yg = 0.0;
        double ntry;
        int m3 = 0;
        double maxtry = 40;
        for (i = 0; i < st.ntrees; i++) {
            if (st.tr[i].x <= 0.0 && st.tr[i].y <= 0.0) {
                ntry = 0;
                do {
                    xg = xmin + st.random.nextUniform() * (xmax - xmin);
                    yg = ymin + st.random.nextUniform() * (ymax - ymin);
                    //emin=99999.0;
                    ic = 0; // tree has valid coordinate i.e. 1st tree
                    // minimum distance of the new location to a tree
                    for (j = 0; j < st.ntrees; j++) {
                        if (j != i && st.tr[j].x > 0.0 && st.tr[j].y > 0.0 && st.tr[j].out < 0) {
                            ic = 1;
                            e = Math.sqrt(Math.pow((xg - st.tr[j].x), 2) + Math.pow((yg - st.tr[j].y), 2));
                            if (e < ((st.tr[i].d + st.tr[j].d) / 200.0)) {
                                break;
                            }
                            if (e > (st.tr[i].cw + st.tr[j].cw) / 2.0) {
                                ic = 0;
                            }
                            // 1. case tree j is smaller than crownbase of i
                            if (st.tr[j].h < st.tr[i].cb) {
                                ic = 0;
                            }
                            // 2. case tree i is smaller than crownbase of j
                            if (st.tr[i].h < st.tr[j].cb) {
                                ic = 0;
                            }
                            //  3. case trees crown are in the same layer
                            if (ic == 1) {
                                double h66i = st.tr[i].cb + (st.tr[i].h - st.tr[i].cb) / 3.0;
                                double h66j = st.tr[j].cb + (st.tr[j].h - st.tr[j].cb) / 3.0;
                                double eki = st.tr[i].cw / 2.0;
                                double ekj = st.tr[j].cw / 2.0;
                                if (h66i > h66j && h66i < st.tr[j].h) {
                                    double prob = Math.pow(st.tr[j].cw / 2.0, 2.0) / ((st.tr[j].h - st.tr[j].cb) * (2.0 / 3.0));
                                    ekj = Math.sqrt(prob * (st.tr[j].h - h66i));
                                }
                                if (h66j > h66i && h66j < st.tr[i].h) {
                                    double prob = Math.pow(st.tr[i].cw / 2.0, 2.0) / ((st.tr[i].h - st.tr[i].cb) * (2.0 / 3.0));
                                    eki = Math.sqrt(prob * (st.tr[i].h - h66j));
                                }
                                if (((maxtry - ntry) / maxtry) * (eki + ekj) < e) {
                                    ic = 0;
                                }
                            }
                            if (ic == 1) {
                                break; // xyverwerfen
                            }
                        }
                    }

                    ntry = ntry + 1;  //if coordinate invalid
                    //	           if (ntry>1) System.out.println(i+" entfernung "+e+"  "+st.tr[i].cw+" m3 "+
                    //	                      st.tr[m3].cw+" ntry "+ntry+" "+
                    //	           (((st.tr[i].cw+st.tr[m3].cw)/2.0)*((maxtry-ntry)/maxtry)));
                } while ((false == pointInPolygon(xg, yg, st)) || ((ic == 1) && ntry < maxtry));
                st.tr[i].x = xg;
                st.tr[i].y = yg;
            }
        }
        if (oldRandom != st.random.getRandomType()) {
            st.random.setRandomType(oldRandom);
        }
    }

    //Traubenkirsche für s Hessische Ried
    public double traubenkirsche(Stand st, int dominantSpecies, double c66kl) {
        if (c66kl < 1.0 && dominantSpecies != 2) {
            int ntr = 0;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].code == 452 && st.tr[i].out < 0) {
                    ntr = ntr + 1;
                }
            }
            // keine Traubenkirsche vorhanden 2% Wahrscheinlichkeit
            if (ntr == 0) {
                if (st.random.nextUniform() < 0.02) {
                    ntr = 1;
                }
            } // Traubenkirsche vorhanden 5 % Wahrscheinlickeit
            else {
                ntr = 0;
                if (st.random.nextUniform() < 0.05) {
                    ntr = 1;
                }
            }
            for (int i = 0; i < ntr; i++) {
                try {
                    st.addTreeFromNaturalIngrowth(452, "tkir", 5, -1, 0.25, 0.5, 0.1, 2.52, 10.0, -9.0, -9.0, 0, 0, 0, 0);  //first is site index
                } catch (SpeciesNotDefinedException e) {
                    System.err.println(e);
                }
                c66kl += Math.PI * Math.pow(2.52 / 2.0, 2.0) / 500.0;
            }
        }
        return c66kl;
    }

    // Vergasung für s Hessische Ried
    public double vergrasung(Stand st, int dominantSpecies, double c66kl) {
        int ngrass = 0;
        if (c66kl >= 0.0 && c66kl < 0.2) {
            ngrass = 50;
        }
        if (c66kl >= 0.2 && c66kl < 0.4) {
            ngrass = 25;
        }
        if (c66kl >= 0.4 && c66kl < 0.6) {
            ngrass = 12;
        }
        if (c66kl >= 0.6 && c66kl < 0.8) {
            ngrass = 6;
        }
        if (dominantSpecies == 2) {
            ngrass = (int) (Math.round(ngrass / 2.0));
        }
        ngrass = (int) (Math.round(st.random.nextUniform() * ngrass));
        for (int i = 0; i < ngrass; i++) {
            try {
                st.addTreeFromNaturalIngrowth(999, "grass", 5, -1, 0.25, 0.5, 0.1, 2.52, 2, -9.0, -9.0, 0, 0, 0, 0);  //first is site index
            } catch (SpeciesNotDefinedException e) {
                System.err.println(e);
            }
            c66kl += Math.PI * Math.pow(2.52 / 2.0, 2.0) / 500.0;
        }
        return c66kl;
    }
}
