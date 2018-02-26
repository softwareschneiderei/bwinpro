/*
 * LAI.java
 *
 * Created on 11. Juni 2009, 13:23
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package Helpers;

import treegross.base.Stand;

/**
 *
 * @author jhansen
 *
 * source: Hammel, K.; Kennel, M. (2001): Charakterisierung und Analyse der
 * Wasserverfügbarkeit und des Wasserhaushalts von Waldstandorten in Bayern mit
 * dem Simulationsmodell Brook90. Forstliche Forschungsberichte München, 185
 *
 */
public class LeafArea {

    private final double[] a_fi = {2.875, 0.148, 0.0};
    private final double[] a_bu = {2.374, 0.191, 0.0019};
    private final double[] a_la = {1.174, 0.046, 0.0007};
    private final double[] a_ki = {1.548, 0.078, 0.0};
    private final double[] a_ei = {1.406, 0.188, 0.0014};

    private final double s_bl = 0.5;
    private final double s_c = 0.4;

    private double dg, nha;

    /**
     * Creates a new instance of LAI
     */
    public LeafArea() {
    }

    public double getLAOfSpecies(double d, int species) {
        if (d > 0) {
            int type = 0;
            if (species < 200) {
                type = 1;
            }
            if (species >= 200 && species < 500) {
                type = 2;
            }
            if (species >= 500 && species < 700) {
                type = 3;
            }
            if (species >= 700 && species < 800) {
                type = 4;
            }
            if (species >= 800 && species < 900) {
                type = 5;
            }
            switch (type) {
                case 1:
                    return (a_ei[0] * d) + (a_ei[1] * (d * d)) + (a_ei[2] * (d * d * d));
                case 2:
                    return (a_bu[0] * d) + (a_bu[1] * (d * d)) + (a_bu[2] * (d * d * d));
                case 3:
                    return (a_fi[0] * d) + (a_fi[1] * (d * d)) + (a_fi[2] * (d * d * d));
                case 4:
                    return (a_ki[0] * d) + (a_ki[1] * (d * d)) + (a_ki[2] * (d * d * d));
                case 5:
                    return (a_la[0] * d) + (a_la[1] * (d * d)) + (a_la[2] * (d * d * d));
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public double getLAIOfSpecies(int species, double d, double nha) {
        double result = getLAOfSpecies(d, species) * nha;
        if (species < 500) {
            return result * s_bl / 10000.0;
        } else {
            return result * s_c / 10000.0;
        }
    }

    private void setDgAndNhaOfSpecies(Stand st, int code) {
        dg = 0;
        nha = 0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].out == -1 && st.tr[i].code == code) {
                dg += st.tr[i].d * st.tr[i].fac;
                nha += st.tr[i].fac;
            }
        }
        if (nha > 0) {
            dg = dg / nha;
            nha = nha / st.size;
        }
    }

    public double getLAIOfStand(Stand st) {
        double result = 0;
        for (int i = 0; i < st.nspecies; i++) {
            setDgAndNhaOfSpecies(st, st.sp[i].code);
            result += getLAIOfSpecies(st.sp[i].code, dg, nha);
        }
        return result;
    }
}
