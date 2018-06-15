/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package treegross.base;

/**
 * This class allows to analyse a stand by groups Each tree is assigned to a
 * group value in tr.group
 *
 * @author nagel
 */
public class Groups {

    Stand st = null;

    public Groups(Stand stx) {
        st = stx;
    }

    public int getNumberOfGroups() {
        int ngr = -9;
        for (int i = 0; i < st.ntrees; i++) {
            if (ngr < st.tr[i].group) {
                ngr = st.tr[i].group;
            }
        }
        return ngr;
    }

    public double getGha(int gr) {
        double bha = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].out < 1) {
                bha += st.tr[i].fac * Math.PI * (st.tr[i].d / 200.0) * (st.tr[i].d / 200.0);
            }
        }
        bha = bha / st.size;
        return bha;
    }

    public double getNha(int gr) {
        double nha = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].out < 1) {
                nha += st.tr[i].fac;
            }
        }
        nha = nha / st.size;
        return nha;
    }

    public double getVha(int gr) {
        double vha = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].out < 1) {
                vha += st.tr[i].fac * st.tr[i].v;
            }
        }
        vha = vha / st.size;
        return vha;
    }

    public double getDg(int gr) {
        double dg = 0.0;
        double nha = getNha(gr);
        if (nha > 0.0) {
            dg = 200.0 * Math.sqrt((getGha(gr) / getNha(gr)) / Math.PI);
        }
        return dg;
    }

    public double getSI(int gr) {
        double si = 0.0;
        double n = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].out < 1) {
                si += st.tr[i].si;
                n++;
            }
        }
        if (n > 0.0) {
            si = si / n;
        } else {
            si = 0.0;
        }
        return si;
    }

    public int getSpeciesCode(int gr) {
        int code = 0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr) {
                code = st.tr[i].code;
                return code;
            }
        }
        return code;
    }

    public double getHg(int gr) {
        double hg = 0.0;
        double dg = getDg(gr);
        int code = getSpeciesCode(gr);
        int merk = 0;
        for (int i = 0; i < st.nspecies; i++) {
            if (st.sp[i].code == code) {
                merk = i;
            }
        }
        HeightCurve hc = new HeightCurve();
        if (dg > 0) {
    // Vorsicht Einheitshöhenkurve        
            if (st.sp[merk].heightcurveUsed.contains("Einh")){
                    Tree tree = new Tree();
                    tree.d = dg;
                    tree.sp = st.sp[merk];
                    tree.sp.dg = st.sp[merk].dg;
                    tree.sp.hg = st.sp[merk].hg;
                    FunctionInterpreter fi = new FunctionInterpreter();
                    hg = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
            }
            else {
                int ncurve = Integer.parseInt(st.sp[merk].heightcurveUsed.substring(0, 1));
                hg = hc.getHeight(ncurve, dg, st.sp[merk].heightcurveUsedP0, st.sp[merk].heightcurveUsedP1, st.sp[merk].heightcurveUsedP2);
            }
        }
        return hg;
    }

    public double getAge(int gr) {
        double age = 0.0;
        double n = 0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].out < 1) {
                age += st.tr[i].age;
                n++;
            }
        }
        if (n > 0) {
            age = age / n;
        } else {
            age = 0;
        }
        return age;
    }

    public double getVaus(int gr, OutType typ, int fromYear) {
        double vha = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].outtype.atLeast(typ) && st.tr[i].out >= fromYear) {
                vha += st.tr[i].fac * st.tr[i].v;
            }
        }
        vha = vha / st.size;
        return vha;
    }
    
    public double getNaus(int gr, OutType typ, int fromYear) {
        double nha = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].outtype.atLeast(typ) && st.tr[i].out >= fromYear) {
                nha += st.tr[i].fac ;
            }
        }
        nha = nha / st.size;
        return nha;
    }
    
    public double getGaus(int gr, OutType typ, int fromYear) {
        double Gha = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].group == gr && st.tr[i].d >= 7.0 && st.tr[i].outtype.atLeast(typ) && st.tr[i].out >= fromYear) {
                Gha += st.tr[i].fac * Math.PI * (st.tr[i].d / 200.0) * (st.tr[i].d / 200.0);
            }
        }
        Gha = Gha / st.size;
        return Gha;
    }

    public double getFlAnteilCS(int gr) {
        double antfl = 0.0;
        double fl = 0.0;
        double sum = 0.0;
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].d >= 7.0 && st.tr[i].out < 1) {
                sum += Math.PI * Math.pow(st.tr[i].cw / 2.0, 2.0);
                if (st.tr[i].group == gr) {
                    fl += Math.PI * Math.pow(st.tr[i].cw / 2.0, 2.0);
                }
            }
        }
        if (sum > 0) {
            antfl = fl / sum;
        }
        return antfl;
    }

    /**
     * This will reset the grouping variable in a way, that different age groups
     * will be defined per species
     */
    public void setAutoGrouping() {
        // helper array to classify all trees of all species by agegroups
        Group grp[] = new Group[500];
        // set groupinge variable to 0
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].group = 0;
        }
        // Cycle over all species
        for (int i = 0; i < st.nspecies; i++) {
            int ngrp = 0;
            for (int j = 0; j < 500; j++) {
                grp[j] = new Group();
            }
            for (int j = 0; j < st.ntrees; j++) {
                if (st.tr[j].code == st.sp[i].code && st.tr[j].out < 0) {
                    int merk = -9;
                    for (int jj = 0; jj < ngrp; jj++) {
                        if (grp[jj].age == st.tr[j].age) {
                            merk = jj;
                        }
                    }
                    if (merk < 0) {
                        merk = ngrp;
                        ngrp = ngrp + 1;
                    }
                    grp[merk].age = st.tr[j].age;
                    grp[merk].avage = st.tr[j].age;
                    grp[merk].gha = grp[merk].gha + Math.PI * Math.pow(st.tr[j].d / 200.0, 2.0);
                    grp[merk].nha = grp[merk].nha + 1;
                }
            }
            System.out.println(st.sp[i].code + "  " + ngrp);
            // sort grp by age
            int agemax = 0;
            for (int j = 0; j < ngrp - 1; j++) {
                for (int k = j + 1; k < ngrp; k++) {
                    if (grp[j].age < grp[k].age) {
                        Group grpt;
                        grpt = grp[j];
                        grp[j] = grp[k];
                        grp[k] = grpt;
                    }
                }
            }
            // aggregate groups
            int agedist = 999;
            while (ngrp > 3) {
                int merk = 9999999;
                for (int j = 0; j < ngrp - 1; j++) {
                    if (agedist > grp[j].avage - grp[j + 1].avage) {
                        merk = j;
                    }
                }
                if (merk > 0) {
                    grp[merk].avage = (grp[merk].avage * grp[merk].nha + grp[merk + 1].avage * grp[merk + 1].nha) / (grp[merk].nha + grp[merk + 1].nha);
                    grp[merk].gha = grp[merk].gha + grp[merk + 1].gha;
                    grp[merk].nha = grp[merk].nha + grp[merk + 1].nha;
                    for (int j = merk + 1; j < ngrp - 1; j++) {
                        grp[j] = grp[j + 1];
                    }
                    ngrp = ngrp - 1;
                }
            }
            for (int j = 0; j < ngrp; j++) {
                System.out.println(st.sp[i].code + "  " + grp[j].avage);
            }
        }
    }
}

class Group {

    double avage = 0.0;
    int age = 0;
    double gha = 0;
    int nha = 0;

    public Group() {
    }
}
