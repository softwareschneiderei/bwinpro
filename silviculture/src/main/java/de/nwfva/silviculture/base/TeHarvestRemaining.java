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
public class TeHarvestRemaining implements TreatmentElement {

    private final String name = "Clear stand";
    private final String label = "te_harvest_rest";
    private final String desc = "te_harvest_rest_info";

    private String criterium;
    private double wert;
    private int phaseNo;

    private ArrayList<Object> defCriterium;

    public TeHarvestRemaining(String criterium, double wert, int phaseNo) {
        init(criterium, wert, phaseNo);
    }

    public TeHarvestRemaining() {
        this("no / nein", 0.2, 0);
    }

    private void init(String criterium, double wert, int phaseNo) {
        this.criterium = criterium;
        this.wert = wert;
        this.phaseNo = phaseNo;
        defCriterium = new ArrayList<>(3);
        defCriterium.add("yes / ja");
        defCriterium.add("no / nein");
    }

    //logic of treatment element:
    public void makeHarvest(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            boolean startHarvest = false;
            boolean removeVJ = false;
            if (criterium.contains("yes")) {
                removeVJ = true;
            }
            TreatmentElements2 te2 = new TreatmentElements2();
            double cov = te2.getDegreeOfCover(0, st, true);
            if (cov < wert) {
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 1 && st.tr[i].d >= 7.0 && !st.tr[i].habitat) {
                        if (te2.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) {
                            break;
                        }
                        st.tr[i].out = st.year;
                        st.tr[i].outtype = 3;
                    }
                }
                for (int i = 0; i < st.ntrees; i++) {
                    if (st.tr[i].out < 1 && removeVJ && !st.tr[i].habitat) {
                        st.tr[i].out = st.year;
                        st.tr[i].outtype = 1;
                    }
                }
//Zuruecksetzen des Erntestatus            
            st.status=0;
            st.temp_Integer = phaseNo;

            }
            st.descspecies();
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
        params.add(new TreatmentElementParameter("wert", RBHolder.getResourceBundle().getString("te_coverage"), RBHolder.getResourceBundle().getString("te_coverage_info"), wert));
        params.add(new TreatmentElementParameter("criterium",RBHolder.getResourceBundle().getString("te_regeneration"), RBHolder.getResourceBundle().getString("te_regeneration_info"), criterium,
                TreatmentElementParameter.CONSTRAINT_TYPE_VALUES, defCriterium));
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
            //System.out.println("parsing: "+aName+" value: " +value);
            switch (aName) {
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
        init("no / nein", 0.2,0);
    }
  
}
