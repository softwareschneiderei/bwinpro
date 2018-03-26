/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.List;

/**
 *
 * @author jhansen
 */
public class TreatmentElementParameter/*<V>*/ {

    public static final int CONSTRAINT_TYPE_NONE = 0;
    public static final int CONSTRAINT_TYPE_MIN_MAX = 1;
    public static final int CONSTRAINT_TYPE_VALUES = 2;

    private String name;
    private String label;
    private String description;
    private /*V*/ Object value;
    private final Class initialType;

    private int constraintType;
    private List<Object> constraintValues;

    public TreatmentElementParameter(String name, String label, String description,
            Object value, int constraintType, List<Object> constraintValues) {
        this.name = name;
        this.label = label;
        this.description = description;
        this.value = value;
        initialType = value.getClass();
        this.constraintType = constraintType;
        this.constraintValues = constraintValues;
    }

    public TreatmentElementParameter(String name, String label, String description, Object value) {
        this(name, label, description, value, CONSTRAINT_TYPE_NONE, null);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the value
     */
    public /*V*/ Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     * @throws java.lang.Exception
     */
    public void setValue(/*V*/Object value) throws Exception {
        if (value.getClass() == initialType) {
            this.value = value;
        } else {
            throw new Exception("IncompatibleTypes");
        }
    }

    /**
     * @return the constraintType
     */
    public int getConstraintType() {
        return constraintType;
    }

    /**
     * @param constraintType the constraintType to set
     */
    public void setConstraintType(int constraintType) {
        this.constraintType = constraintType;
    }

    /**
     * @return the constraintValues
     */
    public List<Object> getConstraintValues() {
        return constraintValues;
    }

    /**
     * @param constraintValues the constraintValues to set
     */
    public void setConstraintValues(List<Object> constraintValues) {
        this.constraintValues = constraintValues;
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder();
        toXML(sb);
        return sb.toString();
    }

    public void toXML(StringBuilder sb) {
        sb.append("<").append(getClass().getSimpleName()).append(" name=\"").append(name);
        sb.append("\" label=\"").append(label);
        sb.append("\" class=\"").append(value.getClass().getCanonicalName());
        sb.append("\" value=\"").append(value).append("\" />");
    }
}
