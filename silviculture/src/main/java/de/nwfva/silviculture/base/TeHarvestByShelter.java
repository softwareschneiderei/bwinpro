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
public class TeHarvestByShelter implements TreatmentElement {

    private final String name = "Schirmschlag";
    private final String label = "te_harvest_shelter";
    private final String desc = "te_harvest_shelter_info";

    private int zeit;
    private String art;
    private String criterium;
    private double wert;
    private int phaseNo;

    private ArrayList<Object> defCriterium;
    private ArrayList<Object> defArt;

    public TeHarvestByShelter(int zeit,  String criterium, double wert, int phaseNo) {
        init(zeit, criterium, wert, phaseNo);
    }

    public TeHarvestByShelter() {
        this(30, "1 "+RBHolder.getResourceBundle().getString("te_crop30_d"), 60, 0);
    }

    private void init(int zeit,  String criterium, double wert, int phaseNo) {
        this.zeit = zeit;
        this.criterium = criterium;
        this.wert = wert;
        this.phaseNo = phaseNo;
        defCriterium = new ArrayList<>(3);
        defCriterium.add("1 "+ RBHolder.getResourceBundle().getString("te_crop30_d"));
        defCriterium.add("2 "+ RBHolder.getResourceBundle().getString("te_clearcut_age"));
        defCriterium.add("3 "+ RBHolder.getResourceBundle().getString("te_clearcut_h100"));
 
    }

    //logic of treatment element:
    public void makeHarvest(Stand st) {        
        if (st != null) {
            st.descspecies();
            boolean startHarvest = false;
            int codex = 0;
            double gmax = 0;
            
            int mm = 0;
            for (int i = 0; i < st.nspecies; i++) {
                if (gmax < st.sp[i].gha) {
                    gmax = st.sp[i].gha;
                    mm = i;
                }
            }
            if (mm == -9) {
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
                if (anz < 1) st.status=1;
                if (anz > 0.0){
                    if (anzGtWert/anz > 0.3 && anz/st.size > sumctwanted*0.4) startHarvest = true;
                }
            }
                if (criterium.startsWith("2") && st.sp[0].h100age >= wert) {
                    startHarvest = true;
                }
                if (criterium.startsWith("3") && st.h100 >= wert) {
                    startHarvest = true;
                }
            } else {
                if (criterium.startsWith("1") && (st.sp[mm].d100 >= wert)) {
                    startHarvest = true;
                }
                if (criterium.startsWith("2") && st.sp[mm].h100age >= wert) {
                    startHarvest = true;
                }
                if (criterium.startsWith("3") && st.sp[mm].h100 >= wert) {
                    startHarvest = true;
                }
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
                baOut = st.bha - te2.getMaxStandBasalArea(st, true) * degree;
                if (baOut < 0.0) {
                    baOut = 0.0;
                }
                while (baHarv < baOut) {
                    // Harvest crop trees
                    double max = -9999999.0;
                    int merk = -9;
                    for (int i = 0; i < st.ntrees; i++) {
                        if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && !st.tr[i].habitat && st.tr[i].d >= (st.tr[i].sp.trule.targetDiameter * 0.3)) {
                            double diff = st.tr[i].d - st.tr[i].sp.trule.targetDiameter;
                            if (max < diff) {
                                merk = i;
                                max = diff;
                            }
                        }
                    }
                    // break if degree to small or no harvest tree found (merk=-9)
                    if ((te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) || merk == -9) {
                        break;
                    }
                    st.tr[merk].out = st.year;
                    st.tr[merk].outtype = 3;
                    //st.tr[merk].no+="_ss";
                    baHarv += Math.PI * Math.pow(st.tr[merk].d / 200.0, 2.0) * (st.tr[merk].fac / st.size);
                }
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
        params.add(new TreatmentElementParameter("zeit", RBHolder.getResourceBundle().getString("te_timespan"), RBHolder.getResourceBundle().getString("te_timespan_info"), zeit));
        params.add(new TreatmentElementParameter("criterium", RBHolder.getResourceBundle().getString("te_criterium"), RBHolder.getResourceBundle().getString("te_condition"), criterium,
                TreatmentElementParameter.CONSTRAINT_TYPE_VALUES, defCriterium));
        params.add(new TreatmentElementParameter("wert", RBHolder.getResourceBundle().getString("te_value"), RBHolder.getResourceBundle().getString("te_greater_than"), wert));
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
                case "art":
                    art = value;
                    break;
                case "criterium":
                    criterium = value;
                    break;
                case "wert":
                    wert = Double.parseDouble(value);
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
        //init with class values 
        init(30,  "1 "+RBHolder.getResourceBundle().getString("te_clearcut_d100"), 60, 0);
        // try to init with stand values if stand is not null
        if (st != null) {
            if (st.sp.length > 0) {
                wert = st.sp[0].trule.targetDiameter;
                return;
            }
        }
        //if st is null use beach values from species def map        
        if (sdm != null && sdm.getByCode(211) != null) {
            wert = sdm.getByCode(211).targetDiameter;
        }
    }
    
}
