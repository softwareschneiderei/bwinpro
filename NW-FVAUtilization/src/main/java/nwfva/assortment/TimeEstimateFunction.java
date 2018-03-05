/* http://www.nw-fva.de
   Version 20-01-2010

   (c) 2010 Juergen Nagel, Northwest German Forest Research Station,
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

package nwfva.assortment;

/**
 *
 * @author nagel
 *
 * This class is needed to save and provide the time estimate funtions which
 * were provided by Prof. H. Jacke of the IFA Institute
 * The time estimate functions are stored in a the xml-file
 * named EST_Zeiten_Jacke.xml
 */
public class TimeEstimateFunction {
/** Id-Number of function, which is linked to an assortment */
    int id;
/** Species group name by Jacke. This is only displayed */
    String speciesGroupName;
/** EST- Name of function */
    String estName;
/** Description of waht the function can be used for */
    String description;
/** Minimum middle diameter of assortment */
    int minDiameter;
/** Maximum middle diameter of assortment */
    int maxDiameter;
/** Coefficient a */
    double coeffA;
/** Coefficient b */
    double coeffB;
/** Coefficient c */
    double coeffC;

public  TimeEstimateFunction() {
    
}
public  TimeEstimateFunction(int id,String speciesGroupName,String estName,String description,
        int minDiameter, int maxDiameter,double coeffA, double coeffB, double coeffC) {
    this.id =id;
    this.speciesGroupName=speciesGroupName;
    this.estName=estName;
    this.description=description;
    this.minDiameter=minDiameter;
    this.maxDiameter=maxDiameter;
    this.coeffA=coeffA;
    this.coeffB=coeffB;
    this.coeffC=coeffC;
}

public double getTime(double middleDiameter){
    double md = 0.0;
    md = coeffA*Math.pow(middleDiameter,coeffB) + coeffC;
    return md;
}


}
