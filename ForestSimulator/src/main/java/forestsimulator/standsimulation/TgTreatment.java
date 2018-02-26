/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package forestsimulator.standsimulation;
import treegross.base.*;
import java.io.*; 
import java.text.*;
import java.util.*;

class TgTreatment {
  
   public TgTreatment() {
   }
   
   
		
}

class TgTreatmentRule{
   int code; // species
   double targetdiameter; // diameter for final harvest
   double maxHarvestVolume; // max. volume to harvest in 5 years
   double thinningHeight1, thinningHeight2, thinningHeight3; // heights of thinning
   double maxBA1,maxBA2,maxBA3 ; //thinning level
   double natDensityA0, natDensityB1, natDensityE ; //natural density coefficients
   double cropTrees; //
   double maxThinningVolume;

}
