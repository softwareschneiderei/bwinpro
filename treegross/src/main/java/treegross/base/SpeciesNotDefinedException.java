/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package treegross.base;

/**
 *
 * @author jhansen
 */
public class SpeciesNotDefinedException extends Exception {

    public SpeciesNotDefinedException(Tree t, SpeciesDefMap sdm) {
        super("Species " + t.code + " undefined in SpeciesDefMap: " + sdm.toString());
    }
}
