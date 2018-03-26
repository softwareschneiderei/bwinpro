/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.gui;

/**
 *
 * @author jhansen
 */
public class TeLabelClassPair implements Comparable<TeLabelClassPair> {

    private final String label;
    private final Class teClass;
    private final int group;

    public TeLabelClassPair(String label, Class c, int group) {
        this.label = label;
        this.teClass = c;
        this.group = group;
    }

    public String getLabel() {
        return label;
    }

    public Class getTeClass() {
        return teClass;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public int compareTo(TeLabelClassPair o) {
        String otherCompString = o.getGroup() + "_" + o.getLabel();
        String thisCompString = group + "_" + label;
        return thisCompString.compareTo(otherCompString);
    }

    /**
     * @return the group
     */
    public int getGroup() {
        return group;
    }
}
