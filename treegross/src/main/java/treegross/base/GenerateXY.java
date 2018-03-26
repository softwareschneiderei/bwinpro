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

import treegross.random.RandomNumber;

public class GenerateXY {

    boolean skidtrail = false;
    double skidtrailDistance = 0.0;
    double skidtrailWidth = 0.0;
    double groupRadius = 0.0;
    double groupCoverArea = 0.0;
    double oldpositionX = 0.0;
    double oldpositionY = 0.0;

    /**
     * Standard constructor
     */
    public GenerateXY() {
    }

    /**
     * Constructor with skidtrails
     * @param skidtrails
     * @param distance
     * @param width
     */
    public GenerateXY(boolean skidtrails, double distance, double width) {
        skidtrail = skidtrails;
        skidtrailDistance = distance;
        skidtrailWidth = width;
    }

    /**
     * calculates xy values for trees in the area defined by cornerpoints, there
     * is a minimum distance of two trees . This distance becomes smaller with
     * each try, max. 20 tries This method can be only used if - all trees have
     * weight factor (fac) = 1.0
     * @param st
     */
    public void zufall(Stand st) {
        double xmin, ymin, xmax, ymax;
        //RandomNumber zz = new RandomNumber();
        //if random is off use fixed random strategy
        int oldRandom = st.random.getRandomType();
        if (!st.random.randomOn) {
            st.random.setRandomType(RandomNumber.PSEUDO_FIXED);
        }

        // determin xmin, xmax, ymin and ymax of area defined by corners
        xmin = ymin = Double.POSITIVE_INFINITY;
        xmax = ymax = Double.NEGATIVE_INFINITY;
        int i, j, ic;
        double e;//=0.0;
        double emin = 0.0;
        //ic=0;
        for (i = 0; i < st.ncpnt; i++) {
            if (st.cpnt[i].x < xmin) {
                xmin = st.cpnt[i].x;
            }
            if (st.cpnt[i].y < ymin) {
                ymin = st.cpnt[i].y;
            }
            if (st.cpnt[i].x > xmax) {
                xmax = st.cpnt[i].x;
            }
            if (st.cpnt[i].y > ymax) {
                ymax = st.cpnt[i].y;
            }
        }
        // generate Coordinates for each tree
        if (groupRadius > 0.0) {
            st = shuffleTrees(st);
        }
        double xg = 0.0;
        double yg = 0.0;
        double ntry;
        int m3 = 0;
        double maxtry = 40;
        for (i = 0; i < st.ntrees; i++) {
            if (st.tr[i].x <= 0.0 && st.tr[i].y <= 0.0) {
                ntry = 0;
                do {
                    if (skidtrail == false || skidtrailDistance == 0.0) {
                        if (groupRadius == 0.0 || groupCoverArea == 0.0) {
                            xg = xmin + st.random.nextUniform() * (xmax - xmin);
                            yg = ymin + st.random.nextUniform() * (ymax - ymin);
                            oldpositionX = xg;
                            oldpositionY = yg;
                            if (groupRadius > 0.0) {
                                groupCoverArea = getCoverArea(st);
                                if (groupCoverArea > 0.85) {
                                    groupCoverArea = 0.0;
                                }
                            }
                        } else {  // Group position
                            xg = oldpositionX - groupRadius + st.random.nextUniform() * (2.0 * groupRadius);
                            yg = oldpositionY - groupRadius + st.random.nextUniform() * (2.0 * groupRadius);
                            groupCoverArea = getCoverArea(st);
                            if (groupCoverArea > 0.85) {
                                groupCoverArea = 0.0;
                            }
                        }
                    } else { // skidtrail
                        if (groupRadius == 0.0 || groupCoverArea == 0.0) {
                            int xkorr = (int) ((xmax - xmin) / skidtrailDistance);
                            xg = xmin + st.random.nextUniform() * (xmax - xmin) - xkorr * skidtrailWidth;
                            yg = ymin + st.random.nextUniform() * (ymax - ymin);
                            oldpositionX = xg;
                            oldpositionY = yg;
                            if (groupRadius > 0.0) {
                                groupCoverArea = getCoverArea(st);
                                if (groupCoverArea > 0.85) {
                                    groupCoverArea = 0.0;
                                }
                            }
                        } else {  // Group position
                            xg = oldpositionX - groupRadius + st.random.nextUniform() * (2.0 * groupRadius);
                            yg = oldpositionY - groupRadius + st.random.nextUniform() * (2.0 * groupRadius);
                            groupCoverArea = getCoverArea(st);
                            if (groupCoverArea > 0.85) {
                                groupCoverArea = 0.0;
                            }
                        }
                        // Skidtrail korrigieren
                        double xlow = xmin - skidtrailDistance / 2.0;
                        do {
                            xlow = xlow + skidtrailDistance;
                            if (xg > xlow) {
                                xg = xg + skidtrailWidth;
                            }
                        } while (xlow < xmax);
                    }
                    emin = 99999.0;
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
                    ntry++;  //if coordinate invalid
                    //if (ntry>1) System.out.println(i+" entfernung "+e+"  "+st.tr[i].cw+" m3 "+
                    //st.tr[m3].cw+" ntry "+ntry+" "+
                    //(((st.tr[i].cw+st.tr[m3].cw)/2.0)*((maxtry-ntry)/maxtry)));
                } while ((0 == pnpoly(xg, yg, st)/* && ntry < maxtry*/) || ((ic == 1) && ntry < maxtry));
                st.tr[i].x = xg;
                st.tr[i].y = yg;
            }
        }
        st.sortbyd();
        if (oldRandom != st.random.getRandomType()) {
            st.random.setRandomType(oldRandom);
        }
    }

    public void raster(Stand st, double xspacing, double yspacing, double xstart, double ystart) {
        double xmin, ymin, xmax, ymax;
        // determin xmin, xmax, ymin and ymax of area defined by corners
        xmin = 99999.9;
        ymin = xmin;
        xmax = -xmin;
        ymax = xmax;
        for (int i = 0; i < st.ncpnt; i++) {
            if (st.cpnt[i].x < xmin) {
                xmin = st.cpnt[i].x;
            }
            if (st.cpnt[i].y < ymin) {
                ymin = st.cpnt[i].y;
            }
            if (st.cpnt[i].x > xmax) {
                xmax = st.cpnt[i].x;
            }
            if (st.cpnt[i].y > ymax) {
                ymax = st.cpnt[i].y;
            }
        }
        // put trees on the field
        st.sortbyNo();
        double xx = xstart;
        double yy;//=0.0;
        do {
            yy = ystart;
            do {
                // select a tree with missing coorinates from the list
                int merk = -9;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].x <= 0.0 && st.tr[i].y <= 0.0 && merk < 0) {
                        merk = i;
                    }
                }
                if (merk >= 0 && pnpoly(yy, xx, st) == 1) {
                    // Check if there is another tree, no tree planted in a distance of 2*diameter    
                    boolean plant = true;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].x > 0.0 && st.tr[i].y > 0.0 && merk != i) {
                            double e = Math.sqrt(Math.pow(xx - st.tr[i].x, 2.0) + Math.pow(st.tr[i].y - yy, 2.0));
                            if (e < 2.0 * st.tr[i].d / 100.0) {
                                plant = false;
                            }
                        }
                    }
                    if (plant) {
                        st.tr[merk].x = xx;
                        st.tr[merk].y = yy;
                    }
                }
                yy += yspacing;
            } while (yy < ymax);
            xx += xspacing;
        } while (xx < xmax);
        // if there is a rest, do by random
        zufall(st);
        st.sortbyd();
    }

    /**
     * check if a point is in polygon , if return is 0 then outside
     */
    public int pnpoly(double x, double y, Stand st) {
        int i, j, c, m;
        c = 0;
        m = st.ncpnt;
        //System.out.println("pnpoly "+m+" "+x+" y "+y);
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
        return c;
    }

    /*
     * set group Radius to control if trees get grouped
     */
    public void setGroupRadius(double radius) {
        groupRadius = radius;
    }

    /*
     * return the cover area for a point
     */
    private double getCoverArea(Stand st) {
        double co = 0.0;
        for (int k = 0; k < st.ntrees; k++) {
            if (st.tr[k].out < 0 && st.tr[k].x >= 0.0 && st.tr[k].y > 0.0) {
                double d = Math.pow((oldpositionX - st.tr[k].x), 2.0) + Math.pow((oldpositionY - st.tr[k].y), 2.0);
                if (d > 0.0) {
                    d = Math.sqrt(d);
                }
                if (d <= groupRadius) {
                    co += Math.PI * Math.pow((st.tr[k].cw / 2.0), 2.0);
                }
            }
        }
        co = co / (Math.PI * Math.pow(groupRadius, 2.0));
        return co;
    }
    
    /*
     * shuffle trees without coordinate, so that not all big trees will fall in a cluster
     */
    private Stand shuffleTrees(Stand st) {
        //give all trees with no coordinates a negative random number
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].x <= 0.0 && st.tr[i].y <= 0.0) {
                st.tr[i].x = st.random.nextUniform() - 100;
            }
        }
        //sort all trees by x ascending  
        for (int i = 0; i < st.ntrees - 1; i++) {
            for (int j = i + 1; j < st.ntrees; j++) {
                if (st.tr[i].x > st.tr[j].x) {
                    st.tr[st.ntrees + 1] = st.tr[i];
                    st.tr[i] = st.tr[j];
                    st.tr[j] = st.tr[st.ntrees + 1];
                }
            }
        }
        return st;
    }

}
