package de.nwfva.silviculture.base;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class TeHarvestTargetDBHPeriod implements TreatmentElement {

    private final String name = "harvest by TargetDBH in Period";
    private final String label = "harvest_targetDBH";
    private final String desc = "harvest_targetDBH_info";

    private int zeit;
    private String criterium;
    private double wert;
    private String exceptSp;
    private int phaseNo;

    private ArrayList<Object> defCriterium;

    public TeHarvestTargetDBHPeriod(int zeit, String criterium, double wert, String exceptSp, int phaseNo) {
        init(zeit,  criterium, wert, exceptSp, phaseNo);
    }

    public TeHarvestTargetDBHPeriod() {
        this(30, "1 "+RBHolder.getResourceBundle().getString("te_crop30_d"), 60, "111",0);
    }

    private void init(int zeit,  String criterium, double wert, String exceptSp, int phaseNo) {
        this.zeit = zeit;
        this.criterium = criterium;
        this.wert = wert;
        this.exceptSp = exceptSp;
        this.phaseNo = phaseNo;
        defCriterium = new ArrayList<>(3);
        defCriterium.add("1 "+ RBHolder.getResourceBundle().getString("te_crop30_d"));
        defCriterium.add("2 "+ RBHolder.getResourceBundle().getString("te_clearcut_age"));
        defCriterium.add("3 "+ RBHolder.getResourceBundle().getString("te_clearcut_h100"));
    }

    //logic of treatment element:
    public void makeHarvest(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            st.descspecies();
            boolean startHarvest = false;
            if (criterium.startsWith("1") && st.status < 50) {
        // Check if 30% of crop trees > wert
                double anz = 0;
                double anzGtWert = 0;
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].crop == true) {
                        anz = anz + st.tr[i].fac;
                        if (st.tr[i].d > wert){
                            anzGtWert = anzGtWert + st.tr[i].fac;
                        }
                    }
                }
                int sumctwanted = 0;
                for (int i = 0; i < st.nspecies; i++) {
                    sumctwanted = sumctwanted + st.sp[i].trule.numberCropTreesWanted;
                }
                if (anz < 1 ) st.status=1;
                if (anz > 0.0 && anz/st.size > sumctwanted*0.4 ){
                    if (anzGtWert/anz > 0.3) 
                        startHarvest = true;
                }
            }
            if (criterium.startsWith("2") && st.sp[0].h100age >= wert) {
                startHarvest = true;
            }
            if (criterium.startsWith("3") && st.h100 >= wert) {
                startHarvest = true;
            }
            if (startHarvest || st.status > 50) {
                if (st.status < 50) {
                    st.status = 50;
                }
                st.status = st.status + 1;
                TreatmentElements2 te2 = new TreatmentElements2();
                double degree = st.bha / te2.getMaxStandBasalArea(st, true);
                double reduction;
                if (zeit - (5.0 * (st.status - 51.0)) <= 0.0) {
                    reduction = degree;
                } else {
                    reduction = (5.0 / (zeit - (5.0 * (st.status - 51.0)))) * (st.bha / te2.getMaxStandBasalArea(st, true));
                }
                degree = degree - reduction;
                double baHarv = 0.0;
                double baOut;
                double baOut50;

                baOut = st.bha - te2.getMaxStandBasalArea(st, true) * degree;
                if (baOut < 0.0) {
                    baOut = 0.0;
                }
                baOut50 = baOut * 0.5;
                int merk, kill, count;
                while (baHarv < baOut50) {
                    // Harvest crop trees
                    //double max = Double.NEGATIVE_INFINITY;
                    merk = -9;
                    // remove first 50% basal area of non crop trees
                    count = 0;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].age > zeit &&
                                !st.tr[i].habitat && st.tr[i].crop == false ) {
                            count++;
                        }
                    }
                    // break if degree to small or no harvest tree found (merk=-9)
                    if ((te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || count <= 0) {
                        break;
                    }
                    kill = (int) Math.floor(st.random.nextUniform() * count);
                    count = 0;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].age > zeit &&
                                !st.tr[i].habitat && st.tr[i].crop == false ) {
                            if (count == kill) {
                                merk = i;
                            }
                            count++;
                        }
                    }
                    st.tr[merk].out = st.year;
                    st.tr[merk].outtype = 3;
                    baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
                }
                //
                // Entnahme der Crop Trees super dicken Z-Bäume (> 20% Zielstärke), dies ist nötig! 
                //            
                while (baHarv < baOut) {
                    // Harvest crop trees
                    double min = Double.POSITIVE_INFINITY;
                    merk = -9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].age > zeit 
                                && !st.tr[i].habitat && st.tr[i].crop && exceptSp.indexOf(new Integer(st.tr[i].code).toString()) < 0) {
                            double diff = st.tr[i].d - st.tr[i].sp.trule.targetDiameter*1.2;
                            if (min > diff && diff > 0.0) {
                                merk = i;
                                min = diff;
                            }
                        }
                    }
                    // break if degree to small or no harvest tree found(merk=-9)
                    if ((te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || merk == -9) {
                        break;
                    }
                    st.tr[merk].out = st.year;
                    st.tr[merk].outtype = 3;
                    //st.tr[merk].no+="_ss";
                    baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
                }
                //
                // Entnahme der Crop Trees von unten
                //            
                while (baHarv < baOut) {
                    // Harvest crop trees
                    double min = Double.POSITIVE_INFINITY;
                    merk = -9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].age > zeit 
                                && !st.tr[i].habitat && st.tr[i].crop && exceptSp.indexOf(new Integer(st.tr[i].code).toString()) < 0) {
                            double diff = st.tr[i].d - st.tr[i].sp.trule.targetDiameter;
                            if (min > diff) {
                                merk = i;
                                min = diff;
                            }
                        }
                    }
                    // break if degree to small or no harvest tree found(merk=-9)
                    if ((te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || merk == -9) {
                        break;
                    }
                    st.tr[merk].out = st.year;
                    st.tr[merk].outtype = 3;
                    //st.tr[merk].no+="_ss";
                    baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
                }
                ///
                while (baHarv < baOut) {
                    // Harvest crop trees
                    //double max = Double.NEGATIVE_INFINITY;
                    merk = -9;
                    // remove first 50% basal area of non crop trees
                    count = 0;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].age > zeit && 
                                !st.tr[i].habitat && st.tr[i].crop == false && exceptSp.indexOf(new Integer(st.tr[i].code).toString()) < 0) {
                            count++;
                        }
                    }
                    // break if degree to small or no harvest tree found (merk=-9)
                    if ((te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || count <= 0) {
                        break;
                    }
                    kill = (int) Math.floor(st.random.nextUniform() * count);
                    count = 0;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].age > zeit &&
                                !st.tr[i].habitat && st.tr[i].crop == false && exceptSp.indexOf(new Integer(st.tr[i].code).toString()) < 0) {
                            if (count == kill) {
                                merk = i;
                            }
                            count++;
                        }
                    }
                    st.tr[merk].out = st.year;
                    st.tr[merk].outtype = 3;
                    baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
                }
            double cov = te2.getDegreeOfCover(0, st, true);
            if (zeit - (5.0 * (st.status - 50.0))<=0.1) {
                st.status =0;
                st.temp_Integer = phaseNo;
            }

            }
        }
        //System.out.println(name + " executed");
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
        makeHarvest(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("zeit",  RBHolder.getResourceBundle().getString("te_timespan"), RBHolder.getResourceBundle().getString("te_timespan_info"), zeit));
        params.add(new TreatmentElementParameter("criterium", RBHolder.getResourceBundle().getString("te_criterium"), RBHolder.getResourceBundle().getString("te_condition"), criterium,
                TreatmentElementParameter.CONSTRAINT_TYPE_VALUES, defCriterium));
        params.add(new TreatmentElementParameter("wert",RBHolder.getResourceBundle().getString("te_value"), RBHolder.getResourceBundle().getString("te_greater_than"), wert));
        params.add(new TreatmentElementParameter("exceptSp", RBHolder.getResourceBundle().getString("te_exceptSp"), RBHolder.getResourceBundle().getString("te_exceptSp_info"), exceptSp));
        params.add(new TreatmentElementParameter("phaseNo",RBHolder.getResourceBundle().getString("te_phaseNo"), RBHolder.getResourceBundle().getString("te_phaseNo_info"), phaseNo));
        return params;
    }

    @Override
    public void parse(Node n) {
        List<Node> params = treegross.tools.XmlTool.getChilds(n, TreatmentElementParameter.class.getSimpleName());
        String aName, value;
        for (Node p : params) {
            aName = p.getAttributes().getNamedItem("name").getNodeValue();
            value = p.getAttributes().getNamedItem("value").getNodeValue();
            switch (aName) {
                case "zeit":
                    zeit = Integer.parseInt(value);
                    break;
                case "criterium":
                    criterium = value;
                    break;
                case "wert":
                    wert = Double.parseDouble(value);
                    break;
                case "exceptSp":
                    exceptSp = value;
                    break;
                case "phaseNo":
                    phaseNo = Integer.parseInt(value);
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_HARVESTING;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
