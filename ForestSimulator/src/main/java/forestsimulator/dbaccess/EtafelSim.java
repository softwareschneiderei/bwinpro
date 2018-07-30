/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forestsimulator.dbaccess;

import java.sql.*;
import treegross.base.*;
import static treegross.base.SiteIndex.si;

/**
 *
 * @author nagel
 */
public class EtafelSim {

    public Stand newYTStand(Stand st, String name, double size) {
        return StandFactory.newStand(st, name, size);
    }

    public Stand addLayerFromTable(Connection dbconn, Stand st, int art, int yc, int alt, double mixPerc) {
        double hg = 0.0;
        double dg = 0.0;
        double d100 = 0.0;
        double g = 0.0;
        double h100 = 0.0;
        try (PreparedStatement stmt = dbconn.prepareStatement("select * from Startwerte where ( Art = ? AND EKL = ?)")) {
            stmt.setInt(1, art);
            stmt.setInt(2, yc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hg = rs.getDouble("Hg");
                    dg = Double.parseDouble(rs.getObject("Dg").toString());
                    d100 = Double.parseDouble(rs.getObject("Dmax").toString());
                    h100 = Double.parseDouble(rs.getObject("H100").toString());
                    g = Double.parseDouble(rs.getObject("G").toString());
                    alt = (int) (Double.parseDouble(rs.getObject("Alter").toString()));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (hg > 5.0 && dg > 7.0 && g > 0.0) {
            try {
                GenDistribution gdb = new GenDistribution();
                gdb.weibull(st, art, alt, dg, hg, d100, g * st.size * mixPerc, false);
// missing data fuer die Verteilung generieren
                for (int j = 0; j < st.ntrees; j++) {
                    if (st.tr[j].si.undefined()) {
                        // TODO: check if this assignment is correct
                        st.tr[j].si = si((double) yc);
                    }
                }
                SIofDistrib siod = new SIofDistrib();
                FunctionInterpreter fi = new FunctionInterpreter();
                siod.si(st, art, alt, dg, hg);
                for (int j = 0; j < st.ntrees; j++) {
                    if (st.tr[j].h == 0.0) {
                        Tree tree = new Tree();
                        tree.code = art;
                        tree.sp = st.tr[j].sp;
                        tree.sp.dg = dg;
                        tree.sp.hg = hg;
                        tree.sp.h100 = 0.0;
                        tree.sp.d100 = 0.0;
                        tree.d = st.tr[j].d;
                        tree.code = st.tr[j].code;
                        tree.sp = st.tr[j].sp;
                        tree.st = st;
//                                st.tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML) + fi.getValueForTree(tree, tree.sp.spDef.heightVariationXML) * nd.value(3.0);
                        st.tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
                    }
                }
                for (int j = 0; j < st.ntrees; j++) {
                    st.tr[j].setMissingData();
                }
                GenerateXY gxy = null;
                gxy = new GenerateXY();
                gxy.setGroupRadius(0.0);
                gxy.zufall(st);
                st.sortbyd();
                st.descspecies();
            } catch (Exception ex) {
            }

        }
        return st;
    }
}
