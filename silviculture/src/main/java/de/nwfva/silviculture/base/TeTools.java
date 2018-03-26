/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import de.nwfva.silviculture.examplepathbuilder.ExamplePathBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author jhansen
 */
public class TeTools {


    public final static char PKG_SEPARATOR = '.';
    public final static char DIR_SEPARATOR = '/';
    public final static String CLASS_FILE_SUFFIX = ".class";

    public static String toXML(TreatmentElement elt) {
        StringBuilder sb = new StringBuilder();
        toXML(elt, sb);
        return sb.toString();
    }

    public static void toXML(TreatmentElement elt, StringBuilder sb) {
        String ls = System.getProperty("line.separator");
        ArrayList<TreatmentElementParameter> rp = elt.getRequiredParameters();
        sb.append("<").append(TreatmentElement.class.getSimpleName()).append(" name=\"").append(elt.getName()).append("\" label=\"");
        sb.append(elt.getLabel()).append("\" class=\"").append(elt.getClass().getCanonicalName()).append("\">");
        rp.stream().forEach((p) -> {
            sb.append(ls);
            //sb.append("\t");
            p.toXML(sb);
        });
        sb.append(ls);
        sb.append("</").append(TreatmentElement.class.getSimpleName()).append(">");
    }

    public static String toXML(TePhaseCondition c) {
        StringBuilder sb = new StringBuilder();
        toXML(c, sb);
        return sb.toString();
    }

    public static void toXML(TePhaseCondition c, StringBuilder sb) {
        String ls = System.getProperty("line.separator");
        ArrayList<TreatmentElementParameter> rp = c.getRequiredParameters();
        sb.append("<").append(TePhaseCondition.class.getSimpleName());
        sb.append(" class=\"").append(c.getClass().getCanonicalName()).append("\">");
        rp.stream().forEach((p) -> {
            sb.append(ls);
            p.toXML(sb);
        });
        sb.append(ls);
        sb.append("</").append(TePhaseCondition.class.getSimpleName()).append(">");
    }

    public static void writeToFile(String file, String content) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(file, "UTF-8")) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
            out.println(content);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TeTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getPackageName() {
        String p = TeTools.class.getName();
        return p.substring(0, p.length() - (TeTools.class.getSimpleName().length() + 1));
    }

    public static List<Class<?>> getAllTreatmentElements() {
        List<Class<?>> classes = new ArrayList<>();
        String p = getPackageName();
        //System.out.println("p: " + p);
        p = p.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        //System.out.println(p);
        URL url = Thread.currentThread().getContextClassLoader().getResource(p);
        //System.out.println(url);
        if (url.toString().startsWith("jar")) {
            try {
                JarFile jarFile = new JarFile(url.toString().split("!")[0].replace("jar:file:", ""));
                Class<?> c;
                Enumeration allEntries = jarFile.entries();
                while (allEntries.hasMoreElements()) {
                    JarEntry entry = (JarEntry) allEntries.nextElement();
                    if (!entry.isDirectory() && entry.getName().startsWith(p + DIR_SEPARATOR)) {
                        String name = entry.getName();
                        int endIndex = name.length() - CLASS_FILE_SUFFIX.length();
                        String className = name.substring(0, endIndex).replace(DIR_SEPARATOR, PKG_SEPARATOR);
                        //System.out.println(className);
                        try {
                            c = Class.forName(className);
                            if (!c.isInterface() && TreatmentElement.class.isAssignableFrom(c)) {
                                //System.out.println(c.getSimpleName());
                                classes.add(c);
                                //classes.add(Class.forName("de.nwfva.silviculture.model.TeSTrails"));
                            }
                        } catch (ClassNotFoundException ignore) {
                            Logger.getLogger(TeTools.class.getName()).log(Level.SEVERE, null, ignore);
                        }
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(TeTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            File scannedDir = new File(url.getFile());
            String resource;
            p = p.replace(DIR_SEPARATOR, PKG_SEPARATOR);
            Class<?> c;
            for (File file : scannedDir.listFiles()) {
                //System.out.println(file);
                resource = file.getName();
                if (resource.endsWith(CLASS_FILE_SUFFIX)) {
                    int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
                    String className = p + PKG_SEPARATOR + resource.substring(0, endIndex);
                    //System.out.println(className);               
                    try {
                        c = Class.forName(className);
                        if (!c.isInterface() && TreatmentElement.class.isAssignableFrom(c)) {
                            //System.out.println(c.getSimpleName());
                            classes.add(c);
                            //classes.add(Class.forName("de.nwfva.silviculture.model.TeSTrails"));
                        }
                    } catch (ClassNotFoundException ignore) {
                        Logger.getLogger(TeTools.class.getName()).log(Level.SEVERE, null, ignore);
                    }
                }
            }
        }
        return classes;
    }
    
    public static Node getFirstMatchingNode(String xmlFile, Class classToMatch) {
        Node result = null;
        try {
            // read back;
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(xmlFile));
            NodeList nl = d.getElementsByTagName(classToMatch.getSimpleName());
            if (nl.getLength() > 0) {
                result = nl.item(0);
            } else {
                Logger.getLogger(ExamplePathBuilder.class.getName()).log(Level.WARNING, "no items found matching class {0}", classToMatch);
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ExamplePathBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
