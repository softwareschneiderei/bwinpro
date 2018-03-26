/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import org.w3c.dom.Node;

/**
 *
 * @author jhansen
 */
public abstract class XmlElement {
    public abstract String toXML();
    public abstract void toXML(StringBuilder sb);
    public abstract void parse(Node n);
}
