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

/**
 *
 * @author jhansen
 */
public class TeThinCTByQD implements TreatmentElement {

    private final String name = "Thin by QD Rule";
    private final String label = "te_qd";
    private final String desc = "te_qd_info";

    private double minDistance;

    public TeThinCTByQD(double minDistance) {
        init(minDistance);
    }

    public TeThinCTByQD() {
        this(0.0);
    }

    private void init(double minDistance) {
        this.minDistance = minDistance;
    }

    //logic of treatment element:
    public void doThinning(Stand st) {
        if (st != null) {
            double ent;
            for (int i = 0; i < st.ntrees; i++) {
                if (st.tr[i].out < 0 && st.tr[i].crop) {
                    // we found a crop tree and remove now all other trees
                    for (int j = 0; j < st.ntrees; j++) {
                        if (st.tr[j].d > 7 && st.tr[j].out < 0 && st.tr[j].crop == false && st.tr[j].habitat == false) {
                            ent = Math.sqrt(Math.pow(st.tr[i].x - st.tr[j].x, 2.0) + Math.pow(st.tr[i].y - st.tr[j].y, 2.0));
                            if (((st.tr[i].cw + st.tr[j].cw) / 2.0) > ent - minDistance) {
                                st.tr[j].out = st.year;
                                st.tr[j].outtype = 2;
                            }
                        }
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
        params = new ArrayList<>(3);
        params.add(new TreatmentElementParameter("minDistance", RBHolder.getResourceBundle().getString("te_qdvalue"), RBHolder.getResourceBundle().getString("te_value"), minDistance));
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
                case "minDistance":
                    minDistance = Double.parseDouble(value);
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
         init(0.0);
    }

    
}
