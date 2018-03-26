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
public class TeSwitchPhaseCondition implements TePhaseCondition {

    private boolean on = false;

    @Override
    public boolean check(Stand st) {
        //System.out.println("check -> " + on);
        return on;
    }

    @Override
    public ArrayList<TreatmentElementParameter> getRequiredParameters() {
        ArrayList<TreatmentElementParameter> params;
        params = new ArrayList<>(1);
        params.add(new TreatmentElementParameter("switch", "Schalter", "Schaltet diese Phase manuell an/aus", on));
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
                    on = Boolean.parseBoolean(value);
                    break;
            }
        }
    }

}
