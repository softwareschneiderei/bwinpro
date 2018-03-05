package forestsimulator.DBAccess;

import java.sql.Connection;
import java.sql.Statement;
import treegross.base.Stand;
import treegross.base.Tree;

/**
 *
 * @author nagel
 */
public class MixedTreeInfo {

    public void saveTreeInfo(Connection dbconn, Stand st, String ids, int aufs) {
        try (Statement stmt = dbconn.createStatement()) {
            for (int i = 0; i < st.ntrees; i++) {
                if (!st.tr[i].no.contains("_")) {
                    Double dd = st.tr[i].d;
                    Double hh = st.tr[i].h;
                    Double ka = st.tr[i].cb;
                    Double kb = st.tr[i].cw;
                    Double vv = st.tr[i].v;
                    Double cc66 = st.tr[i].c66;
                    Double cc66c = st.tr[i].c66c;
                    Double cc66xy = st.tr[i].c66xy;
                    Double cc66cxy = st.tr[i].c66cxy;
                    Double ssi = st.tr[i].si;
                    Double xx = st.tr[i].x;
                    Double yy = st.tr[i].y;
                    int zbx = 0;
                    if (st.tr[i].crop == true) {
                        zbx = 1;
                    }
// Berechnung des C66art und C66NoArt 
                    Double c66art = -99.9;
                    Double c66noart = -99.9;
                    Double c66art2 = -99.9;
                    Double c66noart2 = -99.9;
                    Double c66artW = -99.9;
                    Double c66noartW = -99.9;
                    if (cc66xy > -9) {
                        c66art = getc66byArt(st.tr[i], 211, 1.0);
                        c66noart = getc66byArt(st.tr[i], 511, 1.0);
                        c66art2 = getc66byArt(st.tr[i], 211, 2.0);
                        c66noart2 = getc66byArt(st.tr[i], 511, 2.0);
                        c66artW = getc66byArt(st.tr[i], 211, -2.0);
                        c66noartW = getc66byArt(st.tr[i], 511, -2.0);
                    }
                    /*               stmt.execute("INSERT INTO MixTreeInfo (  edvid, auf, nr, art) "+
                     "values (  '"+st.standname+"', "+st.year+",'"+st.tr[i].no+"',"+st.tr[i].code+" "+
                      " )");               
                     */
                    System.out.println("Baum. " + st.tr[i].no + "'," + st.tr[i].code);
                    stmt.execute("INSERT INTO MixTreeInfo (  edvid, auf, simschritt, nr, art, alt, aus, d, h, ka, kb, v, c66,c66c, c66xy, c66cxy, si,x,y, "
                            + "c66art, c66noart,c66art2, c66noart2,c66artW, c66noartW, zb) "
                            + "values (  '" + ids + "', " + aufs + ", " + st.year + ",'" + st.tr[i].no + "'," + st.tr[i].code + ", "
                            + st.tr[i].age + " , " + st.tr[i].out + " , " + dd.toString() + " , " + hh.toString()
                            + " , " + ka.toString() + " , " + kb.toString() + " , " + vv.toString()
                            + " , " + cc66.toString() + " , " + cc66c.toString()
                            + " , " + cc66xy.toString() + " , " + cc66cxy.toString() + " , " + ssi.toString() + " , " + xx.toString() + " , " + yy.toString()
                            + " , " + c66art.toString() + " , " + c66noart.toString()
                            + " , " + c66art2.toString() + " , " + c66noart2.toString()
                            + " , " + c66artW.toString() + " , " + c66noartW.toString() + ", " + zbx + " )");
                }
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println("Datenbank Fehler MixTreeInfo :" + e);
        }
    }

    private double getc66byArt(Tree t, int artsoll, double fac) {
        double c66a = 0.0;
        double influenceZoneRadius = t.cw * fac;
        if (fac < 0.0) {
            influenceZoneRadius = Math.asin(30 * Math.PI / 180) * (t.h - t.cb);
        }

        if (influenceZoneRadius < 2.0) {
            influenceZoneRadius = 2.0;
        }

        double h66 = t.cb + (t.h - t.cb) / 3.0; // height for the cross section of subject tree

        // 1. we check how many percent of the influence zone is in the
        // stand pologon. The percentage is stored in perc 
        double perc = getPercCircleInStand(influenceZoneRadius, t.x, t.y, t.st);

        // Now we check for each tree             
        // add trees own area
        // this is done in loop over all trees
        //t.c66xy = t.fac * Math.PI * Math.pow((t.cw/2.0),2.0);
        double e;
        /*for (int k=0; k<t.nNeighbor; k++){*/
        //calculate with all trees in stand:
        int j;
        double overlap;
        double percOverlapInStand;
        double cri;
        for (int k = 0; k < t.st.ntrees; k++) {
            //int j = t.neighbor[k];
            j = k;
            int artist = t.st.tr[j].code;
            if (artist < 500) {
                artist = 211;
            }
            if (artist > 500) {
                artist = 511;
            }
            if (artist == artsoll && !t.st.tr[j].no.equals(t.no)) {
                e = Math.pow(t.x - t.st.tr[j].x, 2.0) + Math.pow(t.y - t.st.tr[j].y, 2.0);
                if (e > 0) {
                    e = Math.sqrt(e);
                }
                //cri=0.0;
                // neighbours gehen nur max bis influienceZone ein vgl. stand
                // 2. Achtung wenn baum j mit seiner vollen kronenbreite in die influenzsZone ragt,
                //    ist dies nicht für die Kronenbreite in h66 garantiert, dann kann
                //    es sein, dass overlap= 0 ist und getoverlapPerc NaN !!!!
                //    daher: vorher abfragen ob overlap >0
                //    und methode overlap püfen, op überhaupt punkte im überlappungsbereich sind
                if (e < influenceZoneRadius + t.st.tr[j].cw / 2.0 && t.st.tr[j].h > h66 && t.st.tr[j].out < 0) {
                    if (t.st.tr[j].cb >= h66) {
                        cri = t.st.tr[j].cw / 2.0;
                    } else {
                        cri = t.st.tr[j].calculateCwAtHeight(h66) / 2.0;
                    }
                    // reduce overlap area -> use only percentage inside the stand
                    overlap = overlap(cri, influenceZoneRadius, e);
                    //nur wenn überlappung c66xy erhöhen
                    if (overlap > 0) {
                        percOverlapInStand = getPercOverlapInStand(influenceZoneRadius, t.x, t.y, cri, t.st.tr[j].x, t.st.tr[j].y, t.st);
                        c66a += t.st.tr[j].fac * (overlap * percOverlapInStand);
                    }
                } else if (e < influenceZoneRadius + t.st.tr[j].cw / 2.0 && t.st.tr[j].h > h66 && t.st.tr[j].out >= t.st.year && t.st.tr[j].outtype != 1) {
                    if (t.st.tr[j].cb >= h66) {
                        cri = t.st.tr[j].cw / 2.0;
                    } else {
                        cri = t.st.tr[j].calculateCwAtHeight(h66) / 2.0;
                    }
                    // reduce overlap area -> use only percentage inside the stand
                    overlap = overlap(cri, influenceZoneRadius, e);
                    //nur wenn überlappung c66xy erhöhen
                    if (overlap > 0) {
                        percOverlapInStand = getPercOverlapInStand(influenceZoneRadius, t.x, t.y, cri, t.st.tr[j].x, t.st.tr[j].y, t.st);
                    }
                }
            }
        }
        double div = perc * Math.PI * Math.pow(influenceZoneRadius, 2.0);
        if (div == 0d) {
            return Double.NaN;
        }
        c66a = c66a / div;
        return c66a;
    }

    /**
     * calculate overlap area of two circle only if they overlap
     */
    private static double overlap(double r1, double r2, double e) {
        double x, y, f, r1s, r2s;
        //f=0.0;
        //r1 should always be the smaller radius
        if (r1 > r2) {
            x = r1;
            r1 = r2;
            r2 = x;
        }
        if (e >= (r1 + r2)) {
            return 0.0;  //no overlap area =0
        }
        r1s = r1 * r1;
        // partly or full overlap
        if ((e + r1) <= r2) {
            return Math.PI * r1s;
        }

        // part. overlap
        x = e * e;
        r2s = r2 * r2;

        y = Math.sqrt((-e + r1 + r2) * (e + r1 - r2) * (e - r1 + r2) * (e + r1 + r2));

        f = (r1s * Math.acos((x + r1s - r2s) / (2 * e * r1)))
                + (r2s * Math.acos((x + r2s - r1s) / (2 * e * r2)))
                - (0.5 * y);

        if (f < 0) {
            return 0;
        }
        if (Double.isNaN(f)) {
            return 0;
        }
        return f;
    }

    /**
     * get percentage of influence zone area inside the stand
     */
    private double getPercCircleInStand(double radius, double x, double y, Stand st) {
        int pan = 0; // number of points inside influence zone total
        int pani = 0; //number of points inside influence zone and inside plot area
        double xpx = x - radius + radius / 20;
        double ypy;
        for (int k = 0; k < 10; k++) {
            ypy = y - radius + radius / 20;
            for (int kk = 0; kk < 10; kk++) {
                if (Math.sqrt(Math.pow(xpx - x, 2.0) + Math.pow(ypy - y, 2.0)) <= radius) {
                    pan++;
                    if (pnpoly(xpx, ypy, st) != 0) {
                        pani++;
                    }
                }
                ypy += 2.0 * radius / 10.0;
            }
            xpx += 2.0 * radius / 10.0;
        }
        return pani / (double) (pan);
    }

    /**
     * get percentage of overlap area inside the stand
     *
     * @param radius_cz: radius of influenze zone
     * @param x_cz: x coordinate of influenze zone (the tree with these
     * influenze zone)
     * @param y_cz: y coordinate of influenze zone (the tree with these
     * influenze zone)
     * @param radius_crown: the crown radius in h66 or max crone radius (cri) of
     * a neighbour tree
     * @param x_crown: the x coordinate of the neighbour tree;
     * @param y_crown: the y coordinate of the neighbour tree;
     * @param st: the stand containing both trees
     *
     * @return returns the percentage of overlap area in stand polygon
     */
    private double getPercOverlapInStand(double radius_cz, double x_cz, double y_cz,
            double radius_crown, double x_crown, double y_crown, Stand st) {
        int pan = 0; // number of points inside overlap zone total
        int pani = 0; //number of points inside overlap zone and inside plot area
        double xpx = x_crown - radius_crown + radius_crown / 20;
        double ypy;
        int in;
        for (int k = 0; k < 10; k++) {
            ypy = y_crown - radius_crown + radius_crown / 20;
            for (int kk = 0; kk < 10; kk++) {
                // is point in crown circle
                if (Math.sqrt(Math.pow(xpx - x_crown, 2.0) + Math.pow(ypy - y_crown, 2.0)) <= radius_crown) {
                    // ist point in influencezone circle
                    if (Math.sqrt(Math.pow(xpx - x_cz, 2.0) + Math.pow(ypy - y_cz, 2.0)) <= radius_cz) {
                        pan++;
                        in = pnpoly(xpx, ypy, st);
                        if (in != 0) {
                            pani++;
                        }
                    }
                }
                ypy += 2.0 * radius_crown / 10.0;
            }
            xpx += 2.0 * radius_crown / 10.0;
        }
        // neu zur Sicherheit
        if (pan > 0) {
            return (double) (pani) / (double) (pan);
        } else {
            return 0;
        }
    }

    /**
     * check if a point is in polygon , if return is 0 then outside
     */
    private int pnpoly(double x, double y, Stand st) {
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

}
