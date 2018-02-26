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

public class Model
{ 
      public int numberOfModels=0;
      String info=""; // Information about model
      String plugInName=""; // Name of model

  
  

 /* get coefficients and string */
  void initModel(int funNo)
    {    if (funNo<0) // display selected number of models
           { numberOfModels=1 ;}
         if (funNo==0) 
            { info="XML-Model Definition";plugInName="XML"; }
      
  }
 
  
 

   public int getNumberOfModels(){
       initModel(-1);
       return numberOfModels;
   }
   public String getModelInfo(int funNo){
       initModel(funNo);
       return info;
   }
   public String getPlugInName(String s){
       String newPlugInName="default";
       int nmodels= getNumberOfModels();
       for (int i=0;i<nmodels;i++){
           initModel(i);
           if (s.compareTo(info)==0) newPlugInName=plugInName;
       }
      
       return newPlugInName;
   }
  
}
