/*   (c) 2002-2007 Juergen Nagel,  Northwest German Forest Research Station,
 Grätzelstr.2, 37079 Göttingen, Germany
 E-Mail: Juergen.Nagel@nw-fva.de
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 For more information see:
 http://www.nw-fva.de/~nagel/treegross/

 */
package treegross.base;

/**
 * This class is the plugin adapter for the regional competition model. The
 * regional model needs to be based on the stand and tree classes.
 *
 * - Version 7.1 2-AUG-2007
 */
public interface PlugInCompetition {
// Interfaces

    /**
     * Interface to get the current competition index c66 of a tree.
     *
     * @param t Tree object
     * @return the c66
     */
    public double getc66(Tree t);

    /**
     * Interface to get the current competition index c66c of a tree. c66c is
     * the release index. It is c66 before and after thinning.
     *
     * @param t Tree object
     * @return c66c (c=change)
     */
    public double getc66c(Tree t);

    /**
     * Interface to replace the distance dependent competition indeces c66 and
     * c66c of a tree. by the current value.
     *
     * @param t Tree object
     * @param influenceZoneRadius double
     */
    public void replaceC66xyAndC66cxy(Tree t, double influenceZoneRadius);

}
