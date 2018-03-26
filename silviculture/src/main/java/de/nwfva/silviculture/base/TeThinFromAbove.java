/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;
import treegross.treatment.TreatmentElements2;

/**
 *
 * @author jhansen
 */
public class TeThinFromAbove implements TreatmentElement {

    private final String name = "Thinning from above";
    private final String label = "te_above";
    private final String desc = "te_above_info";

    private double maxOut;
    private double thinningFactor;
    private double maxBgradAbsenkung;

    public TeThinFromAbove(double maxOut, double maxOverlap,
            double thinningFactor, double maxBgradAbsenkung) {
        init(maxOut, thinningFactor, maxBgradAbsenkung);
    }

    public TeThinFromAbove() {
        this(50, 0.0, 1.0, 0.3);
    }

    private void init(double maxOut, double thinningFactor, double maxBgradAbsenkung) {
        this.maxOut = maxOut;
        this.thinningFactor = thinningFactor;
        this.maxBgradAbsenkung = maxBgradAbsenkung;
    }

    //logic of treatment element:
    public void doThinning(Stand st) {
        if (st != null) {
            st.trule.thinningIntensity = thinningFactor;
            TreatmentElements2 te2 = new TreatmentElements2();
            double vmaxthinning = st.size * (maxOut - te2.getTreatmentOutVolume(st));
            double thinned = 0.0;
            if (vmaxthinning > 0) {
                // calculate max. density
                double baToTakeOut;
                double maxG = te2.getMaxStandBasalArea(st, true);
                if (st.trule.thinningIntensity == 0.0) {
                    maxG = maxG * 100.0;
                } else {
                    maxG = maxG * (2.0 - st.trule.thinningIntensity);
                }
// berechnen was rausgenommen werden darf                
                baToTakeOut = st.bha - maxG;
                double bgrad1 = st.bha / maxG;
                double bgrad2 = (st.bha - baToTakeOut) / maxG;
                if (bgrad1 - bgrad2 > maxBgradAbsenkung) {
                    bgrad2 = bgrad1 - maxBgradAbsenkung;
                    baToTakeOut = st.bha - (bgrad2 * maxG);
                }
                //Berechnung für die Baumarten wieviel vom Grundflächenanteil entnommen werden kann
                double[] baToTakeSp = new double[20];
                double summe = 0.0;
                // Berechnung der gesamten kronenschirmfläche
                double csagesamt = 0.0;
                for (int j = 0; j < st.ntrees; j++) {
                    if (st.tr[j].out < 0 && st.tr[j].d >= 7.0) {
                        csagesamt = csagesamt + Math.PI * Math.pow((st.tr[j].cw / 200.0), 2.0) * st.tr[j].fac;
                    }
                }

                for (int iik = 0; iik < st.nspecies; iik++) {
                    double gist = 0.0;
                    double gsoll, csasoll;
                    double csaist = 0.0;
                    for (int j = 0; j < st.ntrees; j++) {
                        if (st.sp[iik].code == st.tr[j].code && st.tr[j].out < 0 && st.tr[j].d >= 7.0) {
                            gist = gist + Math.PI * Math.pow((st.tr[j].d / 200.0), 2.0) * st.tr[j].fac;
                            csaist += Math.PI * Math.pow((st.tr[j].cw / 200.0), 2.0) * st.tr[j].fac;
                        }
                    }
                    gist = gist/st.size;
                    csaist = 100.0 * csaist / csagesamt;
                    if (st.sp[iik].trule.targetCrownPercent > 1.0) {
                        csasoll = st.sp[iik].trule.targetCrownPercent;
                    } else {
                        csasoll = csaist;
                    }
                    if (csaist > 0.0) {
                        gsoll = (csasoll / csaist) * (gist);
                    } else {
                        gsoll = 1.0;
                    }
                    baToTakeSp[iik] = gsoll;
                    if (baToTakeSp[iik] < 0.0) {
                        baToTakeSp[iik] = 0.0;
                    }
                    summe += baToTakeSp[iik];
                }
                //Abgleich
                for (int iik = 0; iik < st.nspecies; iik++) {
                    if (summe <= 0.0 ) { baToTakeSp[iik]=0.0;}
                    else { baToTakeSp[iik] = baToTakeSp[iik] * baToTakeOut / summe;}
                }

                // NEU: Stelle die Z-Bäume frei, aber durchforste nur, wenn die angestrebte Grundfläche
                // ueberschritten ist oder der Baum ein extremer Bedränger ist
                for (int iik = 0; iik < st.nspecies; iik++) {
                    double maxbaOutsp = baToTakeSp[iik];
                    boolean doNotEndThinning = true;
                    if (maxbaOutsp <= 0.0) {
                        doNotEndThinning = false;
                    } else {
                        do {
                            // find non crop tree with most competition to other trees, defined as that the maximum overlap area for neighbor trees
                            int indextree = -9;
                            double maxOverlap = -99999.9;
                            for (int i = 0; i < st.ntrees; i++) {
                                if (st.tr[i].d > 7 && st.tr[i].out < 0 && st.tr[i].crop == false && st.tr[i].tempcrop == false
                                        && st.tr[i].habitat == false && st.tr[i].code == st.sp[iik].code) {
                                    double ovlp = 0.0;
                                    double ri = st.tr[i].cw / 2.0;
                                    double distance = 0.0;
                                    double rj = 0.0;
                                    for (int j = 0; j < st.ntrees; j++) {
                                        if (i != j && st.tr[j].out < 0) {
                                            distance = Math.sqrt(Math.pow(st.tr[i].x - st.tr[j].x, 2.0)
                                                    + Math.pow(st.tr[i].y - st.tr[j].y, 2.0));
                                            rj = st.tr[j].cw / 2.0;
                                        }
                                        // only if there is an overlap and ri > rj
                                        if (ri + rj > distance && ri > rj) {
                                            ovlp += te2.overlap(rj, ri, distance);
                                        }
                                    }
                                    if (ovlp > maxOverlap) {
                                        maxOverlap = ovlp;
                                        indextree = i;
                                    }
                                }
                            }

                            // Cut indexOfCropTree a
                            // if merk > 9 then cut tree else stop crop tree release
                            if (indextree == -9) {
                                doNotEndThinning = false;
                            } else {
                                st.tr[indextree].out = st.year;
                                st.tr[indextree].outtype = 2;
                                thinned = thinned + (st.tr[indextree].fac * st.tr[indextree].v);
                                maxbaOutsp = maxbaOutsp - (st.tr[indextree].fac * Math.PI * Math.pow(st.tr[indextree].d / 200.0, 2.0)) / st.size;
                                if (maxbaOutsp <= 0.0) {
                                    doNotEndThinning = false;
                                }
                            }
                        } //stop if max thinning amount is reached or all competitors are taken out
                        while (thinned < vmaxthinning && doNotEndThinning);
                    }
                }

            }

        }
        System.out.println(name + " executed.");
    }

    @Override
    public String toString() {
        return getLabel();
    }

    // interface overrides: 
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return RBHolder.getResourceBundle().getString(label);
    }

    @Override
    public String getDescription() {
        return RBHolder.getResourceBundle().getString(desc);
    }

    @Override
    public void execute(Stand st) {
        doThinning(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(4);
        params.add(new TreatmentElementParameter("maxOut", RBHolder.getResourceBundle().getString("te_maxdfvol"), RBHolder.getResourceBundle().getString("te_maxdfvol_info"), maxOut));
        params.add(new TreatmentElementParameter("thinningFactor", RBHolder.getResourceBundle().getString("te_dfdegree"), RBHolder.getResourceBundle().getString("te_dfdegree_info"), thinningFactor));
        params.add(new TreatmentElementParameter("maxBgradAbsenkung", RBHolder.getResourceBundle().getString("te_bgrad"), RBHolder.getResourceBundle().getString("te_bgrad_info"), maxBgradAbsenkung));
        return params;
    }

    @Override
    public void parse(Node n) {
        //ToDo Error handling
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            switch (aName) {
                case "maxOut":
                    maxOut = Double.parseDouble(value);
                    break;
                case "thinningFactor":
                    thinningFactor = Double.parseDouble(value);
                    break;
                case "maxBgradAbsenkung":
                    maxBgradAbsenkung = Double.parseDouble(value);
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_THINNING;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        init(50, 1.0, 0.4);
    }

}
