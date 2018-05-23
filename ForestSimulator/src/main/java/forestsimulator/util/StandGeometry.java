package forestsimulator.util;

import treegross.base.Stand;

public class StandGeometry {
    
    /**
     * check if a point is in polygon , if return is 0 then outside
     * @param x
     * @param y
     * @param st
     * @return 0 if point is outside, 1 otherwise
     */
    public static int pnpoly(double x, double y, Stand st) {
        int c = 0;
        int m = st.ncpnt;
        //      System.out.println("pnpoly "+m+" "+x+" y "+y);
        int j = m - 1;
        for (int i = 0; i < m; i++) {
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
