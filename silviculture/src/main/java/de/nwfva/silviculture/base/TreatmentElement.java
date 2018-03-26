/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ArrayList;
import java.util.Locale;
import org.w3c.dom.Node;
import treegross.base.SpeciesDefMap;
import treegross.base.Stand;

/**
 *
 * @author jhansen
 *
 * attention: all implementations must have an empty constructor!! and for gui
 * models (lists etc) all implementations should overwide toString method
 *
 */
public interface TreatmentElement {

    public static final int GROUP_NAT_CONVERSATION = 0;
    public static final int GROUP_PREPARATION = 1;
    public static final int GROUP_THINNING = 2;
    public static final int GROUP_HARVESTING = 3;
    public static final int GROUP_OTHER = 100;
   
    public String getName();

    public String getLabel();

    public String getDescription();

    public int getGroup();

    public void execute(treegross.base.Stand stand);

    public ArrayList<TreatmentElementParameter> getRequiredParameters();

    public void parse(Node n) throws NumberFormatException;
    
    /**
     * Set default values from <code>treegross.base.SpeciesDefMap</code>.
     * If param st (<code>treegross.baseStand</code>) is not null the given stand should be considered.
     * E.g the target diameter of the leading species. 
     * 
     * @param sdm the <code>treegross.base.SpeciesDefMap</code> to get values from
     * @param st the (optional) Stand to 
     */
    public void setDefaults(SpeciesDefMap sdm, Stand st);

}
