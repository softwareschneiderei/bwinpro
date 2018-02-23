/* 
 * @(#) CropTreeSelection.java  
 *
 *  (c) 2002-2008 Juergen Nagel, Northwest Forest Research Station, 
 *      Grätzelstr.2, 37079 Göttingen, Germany
 *      E-Mail: Juergen.Nagel@nw-fva.de
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  http://www.nw-fva.de
 */

package treegross.treatment;
/**
 * @author	Henriette Duda 
 * for more information see:
 * Duda, H. (2006): Vergleich forstlicher Managementstrategien. 
 * Dissertation Universität Göttingen, S. 180 
 * http://webdoc.sub.gwdg.de/diss/2006/duda/ 
 */
public class CropTreeSpecies {
    
   int code;
   double nha;
   double dist;
   double min_height;
   
    /** Creates a new instance of CTSpecies */
    public CropTreeSpecies() {
        code=0;
        nha=0;
        dist=0.0; 
        min_height=0;    
    }

    public CropTreeSpecies(int code, double nha, double dist, double min_height) {
        addCtsp(code, nha, dist, min_height);
    }
    
    void resetCTSpecies(){
        code=0;
        nha=0;
        dist=0.0; 
        min_height=0;
    }
    
    /**
     * Crop tree species settings
     * @param code
     * @param nha
     * @param dist
     * @param min_height
     */
    public final void addCtsp(int code, double nha, double dist, double min_height){
        this.code = code;
        this.nha = nha;
        this.dist= dist;
        this.min_height=min_height;      
    }    
} 