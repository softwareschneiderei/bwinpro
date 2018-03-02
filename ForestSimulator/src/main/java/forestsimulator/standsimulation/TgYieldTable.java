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

class TgYieldTable {
   int nytmax=800;
   int nyt;
   YieldTableLine yt[]=new YieldTableLine[nytmax]; // limited to 400 entries
   String filename ="standtable.html";
   ResourceBundle messages;

   void TgYieldTable() {
     nyt=0;
   }   
	void enterStandDesc(Stand st) {
 // check if that year and species is already in table, then replace values   
 // new yield table line          
	  for (int i=0;i<st.nspecies;i++) {
              int merk=-9;
              if (nyt >0){
                 for (int j=0;j<nyt;j++) 
                    if (yt[j].year==st.year && yt[j].code==st.sp[i].code) merk=j;
              }
              if (merk < 0) {  //make new line
         	      yt[nyt]=new YieldTableLine();
                      merk=nyt;
              	      nyt=nyt+1;

              }
	      yt[merk].year=st.year;
	      yt[merk].code=st.sp[i].code;
	      yt[merk].age=(int)(st.sp[i].h100age);
	      yt[merk].dg=st.sp[i].dg;
	      yt[merk].hg=st.sp[i].hg;
	      yt[merk].d100=st.sp[i].d100;
	      yt[merk].h100=st.sp[i].h100;
	      yt[merk].nha=st.sp[i].nha;
	      yt[merk].gha=st.sp[i].gha;
	      yt[merk].vha=st.sp[i].vol;
              yt[merk].nhaout=0.0;
              yt[merk].ghaout=0.0;
              yt[merk].vhaout=0.0;
              yt[merk].nhamort=0.0;
              yt[merk].ghamort=0.0;
              yt[merk].vhamort=0.0;             

//	      yt[merk].nhaout=st.sp[i].nhaout;
//	      yt[merk].ghaout=st.sp[i].ghaout;
//	      yt[merk].vhaout=st.sp[i].vhaout;
              for (int j=0;j<st.ntrees;j++)
                  if (st.tr[j].out == st.year && st.tr[j].code == yt[merk].code ){
                      if (st.tr[j].out == st.year &&  st.tr[j].outtype >1 ){
                          yt[merk].nhaout= yt[merk].nhaout + st.tr[j].fac;
                          yt[merk].ghaout= yt[merk].ghaout + Math.PI*Math.pow((st.tr[j].d/200.0),2.0)*st.tr[j].fac;
                          yt[merk].vhaout= yt[merk].vhaout + st.tr[j].v*st.tr[j].fac;
                      }
                      if (st.tr[j].out == st.year && st.tr[j].outtype ==1 ){
                          yt[merk].nhamort= yt[merk].nhamort + st.tr[j].fac;
                          yt[merk].ghamort= yt[merk].ghamort + Math.PI*Math.pow((st.tr[j].d/200.0),2.0)*st.tr[j].fac;
                          yt[merk].vhamort= yt[merk].vhamort + st.tr[j].v*st.tr[j].fac;
                      }
                  }
              yt[merk].nhaout=yt[merk].nhaout/st.size;
              yt[merk].ghaout=yt[merk].ghaout/st.size;
              yt[merk].vhaout=yt[merk].vhaout/st.size;
              yt[merk].nhamort=yt[merk].nhamort/st.size;
              yt[merk].ghamort=yt[merk].ghamort/st.size;
              yt[merk].vhamort=yt[merk].vhamort/st.size;              
              
              
// crop tree values
              yt[merk].nhaz=0.0;
              yt[merk].ghaz=0.0;
              yt[merk].vhaz=0.0;
              yt[merk].nhaaz=0.0;
              yt[merk].ghaaz=0.0;
              yt[merk].vhaaz=0.0;
              
              for (int j=0;j<st.ntrees;j++)
                  if (st.tr[j].crop==true && st.tr[j].code == yt[merk].code ){
                      if (st.tr[j].out < 1){
                          yt[merk].nhaz= yt[merk].nhaz + st.tr[j].fac;
                          yt[merk].ghaz= yt[merk].ghaz + Math.PI*Math.pow((st.tr[j].d/200.0),2.0)*st.tr[j].fac;
                          yt[merk].vhaz= yt[merk].vhaz + st.tr[j].v*st.tr[j].fac;
                      }
                      if (st.tr[j].out == st.year && st.tr[j].outtype >1 ){
                          yt[merk].nhaaz= yt[merk].nhaaz + st.tr[j].fac;
                          yt[merk].ghaaz= yt[merk].ghaaz + Math.PI*Math.pow((st.tr[j].d/200.0),2.0)*st.tr[j].fac;
                          yt[merk].vhaaz= yt[merk].vhaaz + st.tr[j].v*st.tr[j].fac;
                      }
                  }
              if (yt[merk].ghaz>0.0 && yt[merk].nhaz > 0.0) yt[merk].dgz=200.0*Math.sqrt((yt[merk].ghaz/yt[merk].nhaz)/Math.PI);
              else yt[merk].dgz=0.0;
              yt[merk].hgz =0.0;
              if (yt[merk].dgz > 0.0){
                 if (st.sp[i].heightcurveUsed.indexOf("Einheit")>-1){
//                   UniformHeight ufh = new UniformHeight();
                     FunctionInterpreter fi = new FunctionInterpreter();
                     Tree tree = new Tree();
                     tree.d=yt[merk].dgz;
                     tree.sp=st.sp[i];
                     yt[merk].hgz=fi.getValueForTree(tree,tree.sp.spDef.uniformHeightCurveXML);
                     
                 } 
                 else {
                    HeightCurve m =new HeightCurve(); 
                    int htnr = Integer.parseInt(st.sp[i].heightcurveUsed.substring(0, 1));
                    yt[merk].hgz= m.getHeight(htnr,yt[merk].dgz,st.sp[i].heightcurveUsedP0,st.sp[i].heightcurveUsedP1,st.sp[i].heightcurveUsedP2);
                 }

              }
              yt[merk].nhaz=yt[merk].nhaz/st.size;
              yt[merk].ghaz=yt[merk].ghaz/st.size;
              yt[merk].vhaz=yt[merk].vhaz/st.size;
              yt[merk].nhaaz=yt[merk].nhaaz/st.size;
              yt[merk].ghaaz=yt[merk].ghaaz/st.size;
              yt[merk].vhaaz=yt[merk].vhaaz/st.size;
              
              
        
	      }
	}
	void writeTable(Stand st,String path, String fname, Locale preferredLanguage) {
         messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",preferredLanguage);
	  try {
		File file= new File(path, fname);
                filename=file.getCanonicalPath();
                OutputStream os=new FileOutputStream(filename); 
		PrintWriter out= new PrintWriter(
			new OutputStreamWriter(os)); 
		out.println("<HTML>"); 
		out.println("<H2><P align=center>"+messages.getString("stand_development_table")+"</P align=center></H2> "); 
		out.println("<P><B>"+messages.getString("stand")+st.standname); 
		out.println("<BR>"+messages.getString("stand_size")+st.size); 
		out.println("<BR>"+messages.getString("year")+st.year+"</B></P>"); 
		String ss;
		char c=34;
         NumberFormat f=NumberFormat.getInstance();
	 f=NumberFormat.getInstance(new Locale("en","US"));
	 f.setMaximumFractionDigits(1);
	 f.setMinimumFractionDigits(1);
	 ss=String.valueOf(c);
	 out.println("<HR>"); 
         out.println("<TABLE BORDER>");

         out.println("<TR><TH BGCOLOR="+ss+"#C0C0C0"+ss+
	            "><FONT SIZE=2>"+messages.getString("year")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
	            "><FONT SIZE=2>"+messages.getString("species")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("age")+
	            "<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("Dg")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
	            "><FONT SIZE=2>"+messages.getString("Hg")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+messages.getString("D100")+
		            "<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("H100")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+messages.getString("N_ha")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("G_ha")+
		            "<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("V_ha")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+messages.getString("n_ha_out")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+messages.getString("g_ha_out")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("v_ha_out")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+"Mort N/ha"+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+"Mort G/ha"+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+"Mort V/ha"+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
                 
		            "></TR>");
         

	   for (int i=0;i<nyt;i++){

         out.println("<TR><TD><FONT SIZE=2>"+yt[i].year+"<TD><FONT SIZE=2>"+yt[i].code+"<TD><FONT SIZE=2>"+yt[i].age+
		               "<TD><FONT SIZE=2>"+f.format(yt[i].dg)+"<TD><FONT SIZE=2>"+f.format(yt[i].hg)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].d100)+"<TD><FONT SIZE=2>"+f.format(yt[i].h100)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].nha)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].gha)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].vha)+"<TD><FONT SIZE=2>"+f.format(yt[i].nhaout)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].ghaout)+"<TD><FONT SIZE=2>"+f.format(yt[i].vhaout)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].nhamort)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].ghamort)+"<TD><FONT SIZE=2>"+f.format(yt[i].vhamort)+
                       "</TR>");
       } 
      out.println("</HR></TABLE>"); 
      out.println("<P></P>");
      
      	 out.println("<HR>"); 
	 out.println("<B>Tabelle 2: Z-Bäume Crop tree</B>"); 
         out.println("<TABLE BORDER>");

         out.println("<TR><TH BGCOLOR="+ss+"#C0C0C0"+ss+
	            "><FONT SIZE=2>"+messages.getString("year")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
	            "><FONT SIZE=2>"+messages.getString("species")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("age")+
	            "<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("Dg")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
	            "><FONT SIZE=2>"+messages.getString("Hg")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+messages.getString("N_ha")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("G_ha")+
		            "<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("V_ha")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+messages.getString("n_ha_out")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "><FONT SIZE=2>"+messages.getString("g_ha_out")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+"><FONT SIZE=2>"+messages.getString("v_ha_out")+"<TH BGCOLOR="+ss+"#C0C0C0"+ss+
		            "></TR>");
         

	   for (int i=0;i<nyt;i++){

         out.println("<TR><TD><FONT SIZE=2>"+yt[i].year+"<TD><FONT SIZE=2>"+yt[i].code+"<TD><FONT SIZE=2>"+yt[i].age+
		               "<TD><FONT SIZE=2>"+f.format(yt[i].dgz)+"<TD><FONT SIZE=2>"+f.format(yt[i].hgz)+
                       
                       "<TD><FONT SIZE=2>"+f.format(yt[i].nhaz)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].ghaz)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].vhaz)+"<TD><FONT SIZE=2>"+f.format(yt[i].nhaaz)+
                       "<TD><FONT SIZE=2>"+f.format(yt[i].ghaaz)+"<TD><FONT SIZE=2>"+f.format(yt[i].vhaaz)+
                       "</TR>");
       } 
      out.println("</HR></TABLE>"); 
      out.println("<P></P>");
        out.println("<BR>"+messages.getString("created")+st.modelRegion+"</BR></HTML>"); 
	   out.close();
	   }
	   catch (Exception e) {	System.out.println(e); 	}   
 
     }
   public String getFilename() {
       return filename;
   }
   public void setYieldTableNew(){
       nyt = 0;
   }

   class YieldTableLine {

  int year;
  int code;
  int age;
  double dg;
  double hg;
  double d100;
  double h100;
  double nha;
  double gha;
  double vha;
  double nhaout;
  double ghaout;
  double vhaout;
  double dgz;
  double hgz;
  double nhaz;
  double ghaz;
  double vhaz;
  double nhaaz;
  double ghaaz;
  double vhaaz;
  double nhamort;
  double ghamort;
  double vhamort;

}


}

