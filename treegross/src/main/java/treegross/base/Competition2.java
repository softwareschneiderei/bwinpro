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
package treegross.base;

/** TreeGrOSS : class competition defines the competition indicies
 *  http://treegross.sourceforge.net
 *
 *  @version 	2.0 30-NOV-2004
 *  @author	Juergen Nagel  
 * 
 *  For more information see:
 *  NAGEL, J. (1999): Konzeptionelle �berlegungen zum schrittweisen Aufbau eines
 *  waldwachstumskundlichen Simulationssystems f�r Nordwestdeutschland.
 *  Schriften aus der Forstlichen Fakult�t der Universit�t G�ttingen und der
 *  Nieders. Forstl. Versuchsanstalt, Band 128, J.D. Sauerl�nder's Verlag,
 *  Frankfurt a.M., S.122
 *
 * or
 *
 * BWINPro User's Manual http://www.nfv.gwdg
 */
public class Competition2 implements PlugInCompetition {

    @Override
    public double getc66(Tree t) {
        double h66=t.h-(2.0*(t.h-t.cb)/3.0);
        double c66=t.fac*Math.PI*Math.pow((t.cw/2.0),2.0);
        double cri;
            for (int i=0; i< t.st.ntrees; i++){
                if (t.st.tr[i].out < 0) {
                    if (t.st.tr[i].cb >= h66)
                        cri= t.st.tr[i].cw/2.0;
                    else
                        cri=t.st.tr[i].calculateCwAtHeight(h66)/2.0;
                    c66+=t.st.tr[i].fac*Math.PI*cri*cri;
                }
            }
        
        c66 = c66/(10000*t.st.size);
        return c66;
    }

    @Override
    public double getc66c(Tree t) {
        double h66=t.h-(2.0*(t.h-t.cb)/3.0);
        double c66=0.0;
        double cri;
            for (int i=0; i< t.st.ntrees; i++){
                if ( t.st.tr[i].out>=t.st.year && t.st.tr[i].outtype!=1) {
                    if (t.st.tr[i].cb >= h66) cri= t.st.tr[i].cw/2.0;
                    else cri=t.st.tr[i].calculateCwAtHeight(h66)/2.0;
                    c66=c66+t.st.tr[i].fac*Math.PI*cri*cri;
                }
            }
        c66 = c66/(10000*t.st.size);
        return c66;
    }
    
    @Override
    public void replaceC66xyAndC66cxy(Tree t, double influenceZoneRadius){ 
        // if there are 15 neighbor trees detected the influenece zone needs to be corrected
        // why at all and why 15 ??????
        /*if (t.nNeighbor >= 15 )
            influenceZoneRadius=Math.sqrt(Math.pow(t.st.tr[t.neighbor[t.nNeighbor-1]].x-t.x,2.0)+
                Math.pow(t.st.tr[t.neighbor[t.nNeighbor-1]].y-t.y,2.0));*/

        // influenece zone should be at least 2m radius
        if (influenceZoneRadius < 2.0)
            influenceZoneRadius=2.0;
        
        double h66=t.cb+(t.h-t.cb)/3.0; // height for the cross section of subject tree
        t.c66xy=0.0; // set values to 0
        t.c66cxy=0.0;
        
        // 1. we check how many percent of the influence zone is in the
        // stand pologon. The percentage is stored in perc 
        double perc=getPercCircleInStand(influenceZoneRadius, t.x, t.y, t.st);        
       
        // Now we check for each tree             
        // add trees own area
        // this is done in loop over all trees
        //t.c66xy = t.fac * Math.PI * Math.pow((t.cw/2.0),2.0);
        double e;
        /*for (int k=0; k<t.nNeighbor; k++){*/
        //calculate with all trees in stand:
        int j;
        double overlap;
        double percOverlapInStand;
        double cri;
        for (int k=0; k<t.st.ntrees; k++){
            //int j = t.neighbor[k];
            j=k;
            // Experimental: F�r Mischbest�nde die Gewichte festlegen
            // Buche �berlappt von Buche gewicht=1.0;
            // Buche �berlappt von Fichte gewicht=0.84;
            // Fichte �berlappt von Buche gewicht=0.98;
            double gewicht =1.0;
            if (t.code == 211 && t.st.tr[j].code == 511) gewicht=1.69;
            if (t.code == 511 && t.st.tr[j].code == 211) gewicht=0.64;
            
            
            

            e=Math.pow(t.x-t.st.tr[j].x,2.0)+Math.pow(t.y-t.st.tr[j].y,2.0); 
            if (e != 0.0){
            if (e>0)
                e=Math.sqrt(e);
            //cri=0.0;
            // neighbours gehen nur max bis influienceZone ein vgl. stand
            // 2. Achtung wenn baum j mit seiner vollen kronenbreite in die influenzsZone ragt,
            //    ist dies nicht f�r die Kronenbreite in h66 garantiert, dann kann
            //    es sein, dass overlap= 0 ist und getoverlapPerc NaN !!!!
            //    daher: vorher abfragen ob overlap >0
            //    und methode overlap p�fen, op �berhaupt punkte im �berlappungsbereich sind
            if (e<influenceZoneRadius+t.st.tr[j].cw/2.0 && t.st.tr[j].h>h66 && t.st.tr[j].out<0){
                if (t.st.tr[j].cb >= h66)
                    cri= t.st.tr[j].cw/2.0;
                else
                    cri=t.st.tr[j].calculateCwAtHeight(h66)/2.0;
                // reduce overlap area -> use only percentage inside the stand
                overlap=overlap(cri,influenceZoneRadius,e);
                //nur wenn �berlappung c66xy erh�hen
                if(overlap>0){
                    percOverlapInStand=getPercOverlapInStand(influenceZoneRadius, t.x, t.y, cri, t.st.tr[j].x,t.st.tr[j].y, t.st);
                    t.c66xy+=t.st.tr[j].fac*(overlap * percOverlapInStand)*gewicht;
                }
            }
            else if (e<influenceZoneRadius+t.st.tr[j].cw/2.0 && t.st.tr[j].h>h66 && t.st.tr[j].out>=t.st.year && t.st.tr[j].outtype!=1 ){
                if (t.st.tr[j].cb >= h66)
                    cri= t.st.tr[j].cw/2.0;
                else
                    cri=t.st.tr[j].calculateCwAtHeight(h66)/2.0;
                // reduce overlap area -> use only percentage inside the stand
                overlap=overlap(cri,influenceZoneRadius,e);
                //nur wenn �berlappung c66xy erh�hen
                if(overlap>0){
                    percOverlapInStand=getPercOverlapInStand(influenceZoneRadius, t.x, t.y, cri, t.st.tr[j].x,t.st.tr[j].y, t.st);
                    t.c66cxy+=t.st.tr[j].fac*(overlap * percOverlapInStand)*gewicht;
                }
            }
        }
        }
        double div=perc*Math.PI*Math.pow(influenceZoneRadius,2.0);
        t.c66xy=t.c66xy/div;
        t.c66cxy=t.c66cxy/div;
    }

    /** calculate overlap area of two circle only if they overlap */
    private static double overlap(double r1, double r2, double e){
        double x,y,f,r1s,r2s;        
        //f=0.0;
        //r1 should always be the smaller radius
        if(r1>r2){
            x=r1;
            r1=r2;
            r2=x;
        }
        if (e>=(r1+r2))
            return 0.0;  //no overlap area =0

        r1s=r1*r1;
        // partly or full overlap
        if((e+r1)<=r2)
            return Math.PI*r1s;

        // part. overlap
        x=e*e;
        r2s=r2*r2;

        y=Math.sqrt((-e+r1+r2)*(e+r1-r2)*(e-r1+r2)*(e+r1+r2));

        f=(r1s*Math.acos((x+r1s-r2s)/(2*e*r1)))+
          (r2s*Math.acos((x+r2s-r1s)/(2*e*r2)))-
          (0.5*y);

        if(f<0)
            return 0;
        if(Double.isNaN(f))
            return 0;        
        return f;
    }
    
    /** get percentage of influence zone area inside the stand*/
    private double getPercCircleInStand(double radius, double x, double y, Stand st){      
        int pan=0; // number of points inside influence zone total
        int pani=0; //number of points inside influence zone and inside plot area
        double xpx=x-radius+radius/20;
        double ypy;
        int in;
        for(int k=0; k<10; k++){
            ypy=y-radius+radius/20;
            for(int kk=0; kk<10; kk++){
                if (Math.sqrt(Math.pow(xpx-x,2.0)+Math.pow(ypy-y,2.0))<=radius){
                    pan++;
                    in=pnpoly(xpx,ypy,st);
                    if (in!=0)
                        pani++;
                } 
                ypy+=2.0*radius/10.0;
            }
            xpx+=2.0*radius/10.0;
        }
        return (double)(pani)/(double)(pan);
    }
    
    /** get percentage of overlap area inside the stand
     * @param radius_cz: radius of influenze zone
     * @param x_cz: x coordinate of influenze zone (the tree with these influenze zone)
     * @param y_cz: y coordinate of influenze zone (the tree with these influenze zone)
     * @param radius_crown: the crown radius in h66 or max crone radius (cri) of a neighbour tree
     * @param x_crown: the x coordinate of the neighbour tree;
     * @param y_crown: the y coordinate of the neighbour tree;
     * @param st: the stand containing both trees
     *
     * @return returns the percentage of overlap area in stand polygon
      */
    private double getPercOverlapInStand(double radius_cz, double x_cz, double y_cz,
            double radius_crown, double x_crown, double y_crown, Stand st){
        int pan=0; // number of points inside overlap zone total
        int pani=0; //number of points inside overlap zone and inside plot area
        double xpx=x_crown-radius_crown+radius_crown/20;
        double ypy;
        int in;
        for(int k=0; k<10; k++){
            ypy=y_crown-radius_crown+radius_crown/20;
            for(int kk=0; kk<10; kk++){
                // is point in crown circle
                if(Math.sqrt(Math.pow(xpx-x_crown,2.0)+Math.pow(ypy-y_crown,2.0))<=radius_crown){
                    // ist point in influencezone circle
                    if(Math.sqrt(Math.pow(xpx-x_cz,2.0)+Math.pow(ypy-y_cz,2.0))<=radius_cz){
                        pan++;
                        in=pnpoly(xpx,ypy,st);
                        if(in!=0)
                            pani++;
                    }
                } 
                ypy+=2.0*radius_crown/10.0;
            }
            xpx+=2.0*radius_crown/10.0;
        }
        // neu zur Sicherheit
        if(pan>0)
            return (double)(pani)/(double)(pan);
        else
            return 0;
    }
   
    /** check if a point is in polygon , if return is 0 then outside*/
    private int pnpoly(double x, double y, Stand st){
        int i,j,c,m;
        c=0;
        m=st.ncpnt;
        //System.out.println("pnpoly "+m+" "+x+" y "+y);
        j=m-1;
        for (i=0;i< m;i++){
            if ((((st.cpnt[i].y<=y)&&(y<st.cpnt[j].y)) ||
            ((st.cpnt[j].y<=y)&&(y<st.cpnt[i].y))) &&
            (x<(st.cpnt[j].x-st.cpnt[i].x)*(y-st.cpnt[i].y)/
            (st.cpnt[j].y-st.cpnt[i].y)+st.cpnt[i].x)) {
                if (c==0){
                    c=1;
                }else{
                    c=0;
                }
            }
            j=i;
        }
        return c;
    } 
}