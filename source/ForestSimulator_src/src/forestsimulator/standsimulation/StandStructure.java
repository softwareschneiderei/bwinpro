/* 
 * @(#) stand.java  
 *  (c) 2002-2005 Juergen Nagel, Forest Research Station of  Lower Saxony, 
 *      Grätzelstr.2, 37079 Göttingen, Germany
 *      E-Mail: Juergen.Nagel@nfv.gwdg.de
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

/** structure : caculates structural indicies for class stand in
 *  TreeGrOSS. There are methods for Shannon-Index, A-Profil, Percentage of
 *  trees with different species neighbor, percentage of height differentiation,
 *  percentage of diameter differentiation 
 *  
 *  http://treegross.sourceforge.net
 *  For more information see:
 *   NAGEL, J. (1999): Konzeptionelle Überlegungen zum schrittweisen Aufbau eines
 *      waldwachstumskundlichen Simulationssystems für Nordwestdeutschland.
 *      Schriften aus der Forstlichen Fakultät der Universität Göttingen und der
 *      Nieders. Forstl. Versuchsanstalt, Band 128, J.D. Sauerländer's Verlag,
 *      Frankfurt a.M., S.122 
 *   or BWINPro User's Manual http://www.nfv.gwdg/nfvabw01.htm
 *
 *  @version 	2.0 11-NOV-2004
 *  @author		Juergen Nagel  
 */

package forestsimulator.standsimulation;
import treegross.base.*;
public class StandStructure {
  
/** shannon calculates the Shannon-Index for the living trees of class stand */ 
   public double shannon_N(Stand st)  
   	{  int i,j;  //Zaehler
	   double n;
           double nsum=0.0;
	   double h=0.0; //Arbeitsvariablen
      for (j=0;j<st.nspecies;j++)
       {  n=0;
          for (i=0;i<st.ntrees;i++)  // number of trees for each species
            if (st.tr[i].out<0 && st.tr[i].d >= 7.0 && st.tr[i].code==st.sp[j].code) n=n+1;
          nsum=nsum+n;
          if (n>0) h=h-(n/nsum)*Math.log(n/nsum);
       } 
      return h;
 }
/** shannon calculates the Shannon-Index for the living trees of class stand */ 
   public double shannon_G(Stand st)  
   	{  int i,j;  //Zaehler
	   double g=0.0;;
           double gsum=0.0;
	   double h=0.0; //Arbeitsvariablen
      for (j=0;j<st.nspecies;j++)
       {  g=0;
          for (i=0;i<st.ntrees;i++)  // number of trees for each species
            if (st.tr[i].out<0 && st.tr[i].d >= 7.0 && st.tr[i].code==st.sp[j].code) g=g+Math.PI*Math.pow(st.tr[i].d/200,2.0);
          gsum=gsum+g;
          if (g>0) h=h-(g/gsum)*Math.log(g/gsum);
       } 
      return h;
      }   
   
   
// 
/** calculates the A the species profile index by Pretzsch for class stand */       
   public double a_index(Stand st)
       { // get max. height of all trees
         double hmax=-9999.9;double a=0.0;
         int i,j ;
         for (i=0;i<st.ntrees;i++)
           if (st.tr[i].out < 0 && hmax < st.tr[i].h) hmax=st.tr[i].h;
        System.out.println("hmax-wert: "+hmax);   
        // calculate shannon per height zone
         double hzone1,hzone2,hzone3; hzone1=0; hzone2=0; hzone3=0;  
         for (i=0;i<st.ntrees;i++)  // number of trees for each species
            if (st.tr[i].out<0)
              { if (st.tr[i].h>=hmax*0.8) hzone1=hzone1+1;
                if (st.tr[i].h>=hmax*0.5 && st.tr[i].h<hmax*0.8) hzone2=hzone2+1;
                if (st.tr[i].h<hmax*0.5 ) hzone3=hzone3+1;
              }
         // 
         double n1,n2,n3;
         for (j=0;j<st.nspecies;j++)
          {  n1=0;n2=0;n3=0;
             for (i=0;i<st.ntrees;i++)  
               if (st.tr[i].out<0 && st.tr[i].code==st.sp[j].code)
                 { if (st.tr[i].h>=hmax*0.8) n1=n1+1;
                   if (st.tr[i].h>=hmax*0.5 && st.tr[i].h<hmax*0.8) n2=n2+1;
                   if (st.tr[i].h<hmax*0.5 ) n3=n3+1;
                 }
             if (n1>0) a=a-(n1/hzone1)*Math.log(n1/hzone1);
             if (n2>0) a=a-(n2/hzone2)*Math.log(n2/hzone2);
             if (n3>0) a=a-(n3/hzone3)*Math.log(n3/hzone3);
          } 
       a=a/Math.log(3*st.nspecies);   
       return a;
       }      
//Ende Artprofil von Pretzsch  
/** percentage of trees with a neighbour of a different species */
       public double neighbormix(Stand st)
       { int i,j,merk;merk=0;
         double anz, anzmix,emax,e; anz=0;anzmix=0;
         for (i=0;i<st.ntrees;i++)  
           if (st.tr[i].out<0)
             { emax=99999.0;
               for (j=0;j<st.ntrees;j++)  
                  if (st.tr[j].out<0 && i!=j)
                    { e=Math.sqrt(Math.pow(st.tr[i].x-st.tr[j].x,2)+Math.pow(st.tr[i].y-st.tr[j].y,2));
                      if (e<emax) {merk=j;emax=e;}
                    }  
               if (st.tr[i].code!=st.tr[merk].code) anzmix=anzmix+1;
               anz=anz+1;     
             }
        return 100.0*anzmix/anz;    
        }
/** percentage of height differenciation, (see Th von Gadow), modified if next tree
    is more than both trees height in distance away, then the difference is 1.0 because
    there is a gap in the stand */
       public double th(Stand st)
       { int i,j,merk,nx;merk=0;nx=0;
         double value,emax,e,anz; value=0;anz=0;e=0;
         for (i=0;i<st.ntrees;i++)  
           if (st.tr[i].out<0)
             { emax=99999.0;
               for (j=0;j<st.ntrees;j++)  
                  if (st.tr[j].out<0 && i!=j)
                    { e=Math.sqrt(Math.pow(st.tr[i].x-st.tr[j].x,2)+Math.pow(st.tr[i].y-st.tr[j].y,2));
                      if (e<emax) {merk=j;emax=e;}
                    }  
               e=Math.sqrt(Math.pow(st.tr[i].x-st.tr[merk].x,2)+Math.pow(st.tr[i].y-st.tr[merk].y,2));
               if (e > st.tr[i].h+st.tr[merk].h) value=value+1.0;
               else
                 { if (st.tr[i].h > st.tr[merk].h) value=value+1.0-st.tr[merk].h/st.tr[i].h;
                   if (st.tr[i].h < st.tr[merk].h) value=value+1.0-st.tr[i].h/st.tr[merk].h;
                 }  
               anz=anz+1;     
             }
        return 100.0*value/anz; 
        }   
/** percentage of diameter differenciation, (see Td von Gadow), modified if next tree
    is more than both trees height in distance away, then the difference is 1.0 because
    there is a gap in the stand */
       public double td(Stand st)
       { int i,j,merk,nx;merk=0;nx=0;
         double value,emax,e,anz; value=0;anz=0;e=0;
         for (i=0;i<st.ntrees;i++)  
           if (st.tr[i].out<0)
             { emax=99999.0;
               for (j=0;j<st.ntrees;j++)  
                  if (st.tr[j].out<0 && i!=j)
                    { e=Math.sqrt(Math.pow(st.tr[i].x-st.tr[j].x,2)+Math.pow(st.tr[i].y-st.tr[j].y,2));
                      if (e<emax) {merk=j;emax=e;}
                    }  
               e=Math.sqrt(Math.pow(st.tr[i].x-st.tr[merk].x,2)+Math.pow(st.tr[i].y-st.tr[merk].y,2));
               if (e > st.tr[i].h+st.tr[merk].h) value=value+1.0;
               else
                 { if (st.tr[i].d > st.tr[merk].d) value=value+1.0-st.tr[merk].d/st.tr[i].d;
                   if (st.tr[i].d < st.tr[merk].d) value=value+1.0-st.tr[i].d/st.tr[merk].d;
                 }  
               anz=anz+1;     
             }
        return 100*value/anz;    
        }
/** percentage of art differenciation, if next tree is of different species
     then the difference is 1.0 because
    there is a gap in the stand */
       public double tart(Stand st)
       { int i,j,merk,nx;merk=0;nx=0;
         double value,emax,e,anz; value=0;anz=0;e=0;
         for (i=0;i<st.ntrees;i++)  
           if (st.tr[i].out<0)
             { emax=99999.0;
               for (j=0;j<st.ntrees;j++)  
                  if (st.tr[j].out<0 && i!=j)
                    { e=Math.sqrt(Math.pow(st.tr[i].x-st.tr[j].x,2)+Math.pow(st.tr[i].y-st.tr[j].y,2));
                      if (e<emax) {merk=j;emax=e;}
                    }  
               e=Math.sqrt(Math.pow(st.tr[i].x-st.tr[merk].x,2)+Math.pow(st.tr[i].y-st.tr[merk].y,2));
               if (e > st.tr[i].h+st.tr[merk].h) value=value+1.0;
               else
                 { if (st.tr[i].code != st.tr[merk].code) value=value+1.0;
                 }  
               anz=anz+1;     
             }
        return 100*value/anz;    
 
       }
       
 




		
}
