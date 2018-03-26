/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treegross.tools;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @author jhansen
 */
public class XmlTool {
    
    
    public static Element addChild(Element toElt, String name) {      
        Element newElt = toElt.getOwnerDocument().createElement(name);       
        toElt.appendChild(newElt);
        return newElt;
    }

    public static Element addChildString(Element toElt, String name, String text) {
        // do not add empty nodes!
        if (text == null || text.length() == 0) {
            //System.out.println("NOT adding value is null " + name);
            return null;
        }
        Element newElt = toElt.getOwnerDocument().createElement(name);
        newElt.appendChild(toElt.getOwnerDocument().createTextNode(text));
        toElt.appendChild(newElt);
        return newElt;
    }

    public static Node getChild(Node n, String childName) {
        String childNameLower = childName.toLowerCase();
        NodeList modelNodes = n.getChildNodes();
        Node m;
        String nn;
        for (int j = 0; j < modelNodes.getLength(); j++) {
            m = modelNodes.item(j);
            if (m.getNodeType() == Node.ELEMENT_NODE) {
                nn = m.getNodeName().toLowerCase();
                if (nn.equals(childNameLower)) {
                    return m;
                }
            }
        }
        return null;
    }
    
    public static List<Node> getChilds(Node n, String childName) {
        String childNameLower = childName.toLowerCase();
        NodeList modelNodes = n.getChildNodes();
        ArrayList<Node> nl = new ArrayList<>();
        String nn;
        Node m;
        for (int j = 0; j < modelNodes.getLength(); j++) {
            m = modelNodes.item(j);
            if (m.getNodeType() == Node.ELEMENT_NODE) {
                nn = m.getNodeName().toLowerCase();
                if (nn.equals(childNameLower)) {
                    nl.add(m);
                }
            }
        }
        return nl;
    }

    public static String getChildText(Node n, String childName) {
        String result = null;
        Node cn = getChild(n, childName);
        if (cn != null) {
            String tc = cn.getTextContent();
            if (tc != null) {
                result = tc;
            }
        }
        return result;
    }
}
