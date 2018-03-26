/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import org.w3c.dom.Node;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 */
public interface TePhaseCondition {

    public boolean check(Stand st);

    public ArrayList<TreatmentElementParameter> getRequiredParameters();
    
    public void parse(Node n);
}
