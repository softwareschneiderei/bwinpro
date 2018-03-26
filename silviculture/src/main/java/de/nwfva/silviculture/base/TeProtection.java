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
public class TeProtection implements TreatmentElement {

    private final String name = "nature protection";
    private final String label = "te_protection";
    private final String desc = "te_protection_info";

    private int nhabitat;
    private String hardwoodOnly;
    private boolean minorities;
    private double minimumCoverage;
    private double protectDBH;

    private ArrayList<Object> defhardwoodOnly;

    public TeProtection(int nhabitat, String hardwoodOnly, boolean minorities, double minimumCoverage, double protectDBH) {
        init(nhabitat, hardwoodOnly, minorities, minimumCoverage, protectDBH);
    }

    public TeProtection() {
        this(0, "3 "+ RBHolder.getResourceBundle().getString("te_protection_all"), true, 0.0, 999.9);
    }

    private void init(int nhabitat, String hardwoodOnly, boolean minorities, double minimumCoverage, double protectDBH) {
        this.nhabitat = nhabitat;
        this.hardwoodOnly = hardwoodOnly;
        defhardwoodOnly = new ArrayList<>(3);
        defhardwoodOnly.add("1 "+ RBHolder.getResourceBundle().getString("te_protection_hardwood"));
        defhardwoodOnly.add("2 "+ RBHolder.getResourceBundle().getString("te_protection_beechoak"));
        defhardwoodOnly.add("3 "+ RBHolder.getResourceBundle().getString("te_protection_all"));
        this.minorities = minorities;
        this.minimumCoverage = minimumCoverage;
        this.protectDBH = protectDBH;
    }

    //logic of treatment element:
    public void makeProtection(Stand st) {
        //Do nothing but printing line of text to stdout:
        if (st != null) {
            st.trule.protectMinorities = minorities;
            st.trule.nHabitat = nhabitat;
            st.trule.habitatTreeType = 2;
            if (hardwoodOnly.startsWith("3")) {
                st.trule.habitatTreeType = 2;
            }
            if (hardwoodOnly.startsWith("2")) {
                st.trule.habitatTreeType = 0;
            }
            if (hardwoodOnly.startsWith("1")) {
                st.trule.habitatTreeType = 1;
            }
            st.trule.minimumCoverage = minimumCoverage;
            st.trule.treeProtectedfromBHD = (int) Math.round(protectDBH);
            TreatmentElements2 te2 = new TreatmentElements2();
            te2.markTreesAsHabitatTreesByDiameter(st);
            if (st.trule.nHabitat > 0) {
                //habitat trees are selected, habitat trees can not be harvested or chosen as
                // crop trees
                te2.selectHabitatTrees(st);
            }
            // Start minority selection, by choosing one crop tree per species
            if (st.trule.protectMinorities == true) {
                //protect minoritys (select one tree per species)
                te2.SelectOneCropTreePerSpecies(st, true);
            }
        }
        System.out.println(name + " executed. Habitatbäume: " + nhabitat);
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
        makeProtection(st);
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("nhabitat",  RBHolder.getResourceBundle().getString("te_habitat"), RBHolder.getResourceBundle().getString("te_habitat_info"), nhabitat));
        params.add(new TreatmentElementParameter("hardwoodOnly", RBHolder.getResourceBundle().getString("te_habitattype"), RBHolder.getResourceBundle().getString("te_habitattype_info"), hardwoodOnly, TreatmentElementParameter.CONSTRAINT_TYPE_VALUES, defhardwoodOnly));
        params.add(new TreatmentElementParameter("minorities", RBHolder.getResourceBundle().getString("te_minorities"), RBHolder.getResourceBundle().getString("te_minorities_info"), minorities));
        params.add(new TreatmentElementParameter("minimumCoverage", RBHolder.getResourceBundle().getString("te_mincoverage"), RBHolder.getResourceBundle().getString("te_mincoverage_info"), minimumCoverage));
        params.add(new TreatmentElementParameter("protectDBH", RBHolder.getResourceBundle().getString("te_bigtree"), RBHolder.getResourceBundle().getString("te_bigtree_info"), protectDBH));
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
            //System.out.println("parsing: "+aName+" value: " +value);
            switch (aName) {
                case "nhabitat":
                    nhabitat = Integer.parseInt(value);
                    break;
                case "hardwoodOnly":
                    hardwoodOnly = value;
                    break;
                case "minorities":
                    minorities = Boolean.parseBoolean(value);
                    break;
                case "minimumCoverage":
                    minimumCoverage = Double.parseDouble(value);
                    break;
                case "protectDBH":
                    protectDBH = Double.parseDouble(value);
                    break;
            }
        }
    }

    @Override
    public int getGroup() {
        return TreatmentElement.GROUP_NAT_CONVERSATION;
    }

    @Override
    public void setDefaults(SpeciesDefMap sdm, Stand st) {
        init(3, "1 "+ RBHolder.getResourceBundle().getString("te_protection_hardwood"), true, 0.0, 999.9);
    }
    
}
