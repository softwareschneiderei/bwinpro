/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 */
public class PcStandTmpInteger implements TePhaseCondition {

    private int standIndicator = 0;
    private boolean pcSwitch = false;

    @Override
    public boolean check(Stand st) {
        if (!pcSwitch) {
            return true;
        }
        if (st != null) {
            return standIndicator == st.temp_Integer;
        }
        return false;
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(2);
        params.add(new TreatmentElementParameter("switch", RBHolder.getResourceBundle().getString("te_pcStand_switch"), RBHolder.getResourceBundle().getString("te_pcStand_switch_info"), pcSwitch));
        params.add(new TreatmentElementParameter("standTmpInteger", RBHolder.getResourceBundle().getString("te_pcStand_indicator"), RBHolder.getResourceBundle().getString("te_pcStand_indicator_info"), standIndicator));
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
                case "switch":
                    pcSwitch = Boolean.parseBoolean(value);
                    break;
                case "standTmpInteger":
                    standIndicator = Integer.parseInt(value);
                    break;
            }
        }
    }
}