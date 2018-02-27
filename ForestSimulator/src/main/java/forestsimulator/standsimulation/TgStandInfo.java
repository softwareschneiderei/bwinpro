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
import java.text.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

class TgStandInfo extends JPanel // implements ActionListener
{     
      int nrow=0; 
      javax.swing.JTable jTable1 = new javax.swing.JTable();
      JCheckBox jCheckBox1 = new JCheckBox();
      javax.swing.table.DefaultTableModel yieldTable;
      Object[] rowData={" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "};
      Stand st = null;
 
   public TgStandInfo(String preferredLanguage) {
     Locale currentLocale;
     ResourceBundle messages;
     currentLocale = new Locale(preferredLanguage, "");
     messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
    
     yieldTable= new javax.swing.table.DefaultTableModel(
            new Object [][] {  },
            new String [] {
                messages.getString("sp"),"ly",messages.getString("Age"),messages.getString("Dg"),
                messages.getString("Hg"),messages.getString("D100"),messages.getString("H100"),
                messages.getString("nha"),messages.getString("gha"),messages.getString("vha"),
                messages.getString("noutha"),messages.getString("goutha"),
                messages.getString("voutha"),messages.getString("mix")
            }
        );
     
      JScrollPane spane = new JScrollPane();
      jTable1.setModel(yieldTable);
      
      Dimension scr= Toolkit.getDefaultToolkit().getScreenSize();             
      spane.setPreferredSize(new Dimension((scr.width-scr.width/50), (scr.height/4)));       
      spane.getViewport().add(jTable1);      
      
      setLayout(new BorderLayout());
      add(spane, BorderLayout.CENTER);
      jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
      jCheckBox1.setText("getrennt Schichten (ly)");
      add(jCheckBox1, BorderLayout.NORTH );


//
//      yieldTable.
      
      }
   void formUpdate(Stand stand) {
           
           st=stand;
           if (jCheckBox1.isSelected()) makeTableByLayer();
           else makeTable();

           
    } 
   
   private void makeTable(){
    	   NumberFormat f=NumberFormat.getInstance();
	   f=NumberFormat.getInstance(new Locale("en","US"));
	   f.setMaximumFractionDigits(1);
	   f.setMinimumFractionDigits(1);
           f.setGroupingUsed(false);
           
           double sum_nha=0.0;
           double sum_gha=0.0;
           double sum_vol=0.0;
           double sum_nhaout=0.0;
           double sum_ghaout=0.0;
           double sum_vhaout=0.0;
           nrow = yieldTable.getRowCount();
           for (int j=nrow; j>0; j--) yieldTable.removeRow(j-1);
           nrow=0;
           for (int i=0;i<st.nspecies;i++){
               yieldTable.addRow(rowData);
               jTable1.setValueAt(st.sp[i].spDef.shortName,nrow,0);
               jTable1.setValueAt(0,nrow,1);
               jTable1.setValueAt(f.format(st.sp[i].dg),nrow,3);
               jTable1.setValueAt(f.format(st.sp[i].h100age),nrow,2);
               jTable1.setValueAt(f.format(st.sp[i].hg),nrow,4);
               jTable1.setValueAt(f.format(st.sp[i].d100),nrow,5);
               jTable1.setValueAt(f.format(st.sp[i].h100),nrow,6);
               jTable1.setValueAt(f.format(st.sp[i].nha),nrow,7);
               jTable1.setValueAt(f.format(st.sp[i].gha),nrow,8);
               jTable1.setValueAt(f.format(st.sp[i].vol),nrow,9);
               jTable1.setValueAt(f.format(st.sp[i].nhaout),nrow,10);
               jTable1.setValueAt(f.format(st.sp[i].ghaout),nrow,11);
               jTable1.setValueAt(f.format(st.sp[i].vhaout),nrow,12);
               jTable1.setValueAt(f.format(st.sp[i].percBA),nrow,13);
               sum_nha=sum_nha+st.sp[i].nha;
               sum_gha=sum_gha+st.sp[i].gha;
               sum_vol=sum_vol+st.sp[i].vol;
               sum_nhaout=sum_nhaout+st.sp[i].nhaout;
               sum_ghaout=sum_ghaout+st.sp[i].ghaout;
               sum_vhaout=sum_vhaout+st.sp[i].vhaout;
               nrow=nrow+1;
           }
           
           yieldTable.addRow(rowData);
           jTable1.setValueAt("Sum",nrow,0);          
           jTable1.setValueAt(f.format(sum_nha),nrow,7);
           jTable1.setValueAt(f.format(sum_gha),nrow,8);
           jTable1.setValueAt(f.format(sum_vol),nrow,9);
           jTable1.setValueAt(f.format(sum_nhaout),nrow,10);
           jTable1.setValueAt(f.format(sum_ghaout),nrow,11);
           jTable1.setValueAt(f.format(sum_vhaout),nrow,12);
           jTable1.setValueAt(f.format(st.degreeOfDensity),nrow,13);    
    }
   
    private void makeTableByLayer(){
    	   NumberFormat f=NumberFormat.getInstance();
	   f=NumberFormat.getInstance(new Locale("en","US"));
	   f.setMaximumFractionDigits(1);
	   f.setMinimumFractionDigits(1);
           f.setGroupingUsed(false);
           
    //     define new groups       
           int lay =0;
           for (int i=0;i<st.nspecies;i++){
               for (int j=0;j<st.ntrees;j++){
                   if (st.sp[i].code==st.tr[j].code){
                       lay = st.tr[j].layer;
                       if (lay == 4) lay = 0;
                       st.tr[j].group = i*4+lay;
                   }
               }
           }
           Groups gr = new Groups(st);
           double sum_nha=0.0;
           double sum_gha=0.0;
           double sum_vol=0.0;
           double sum_nhaout=0.0;
           double sum_ghaout=0.0;
           double sum_vhaout=0.0;
           nrow = yieldTable.getRowCount();
           for (int j=nrow; j>0; j--) yieldTable.removeRow(j-1);
           nrow=0;
           int la=0;
           for (int l=0;l<4;l++){
               la = l;
               if (la==0)la=4;
           for (int i=0;i<st.nspecies;i++){
               int grp = i*4+l;
               if (gr.getNha(grp)>0 || gr.getVaus(grp,1,st.year)>0 ){
                  yieldTable.addRow(rowData);
                  jTable1.setValueAt(st.sp[i].spDef.shortName,nrow,0);
                  jTable1.setValueAt(la,nrow,1);
                  jTable1.setValueAt(f.format(gr.getDg(grp)),nrow,3);
                  jTable1.setValueAt(f.format(gr.getAge(grp)),nrow,2);
                  jTable1.setValueAt(f.format(gr.getHg(grp)),nrow,4);
                  jTable1.setValueAt("",nrow,5);
                  jTable1.setValueAt("",nrow,6);
                  jTable1.setValueAt(f.format(gr.getNha(grp)),nrow,7);
                  jTable1.setValueAt(f.format(gr.getGha(grp)),nrow,8);
                  jTable1.setValueAt(f.format(gr.getVha(grp)),nrow,9);
                  jTable1.setValueAt(f.format(gr.getNaus(grp,1,st.year)),nrow,10);
                  jTable1.setValueAt(f.format(gr.getGaus(grp,1,st.year)),nrow,11);
                  jTable1.setValueAt(f.format(gr.getVaus(grp,1,st.year)),nrow,12);
                  jTable1.setValueAt(f.format(gr.getFlAnteilCS(grp)),nrow,13);
                  sum_nha=sum_nha+gr.getNha(grp);
                  sum_gha=sum_gha+gr.getGha(grp);
                  sum_vol=sum_vol+gr.getVha(grp);
                  sum_nhaout=sum_nhaout+gr.getNaus(grp,1,st.year);
                  sum_ghaout=sum_ghaout+gr.getGaus(grp,1,st.year);
                  sum_vhaout=sum_vhaout+gr.getVaus(grp,1,st.year);
                  nrow=nrow+1;
               }
               // layer 3 trees under 7cm
               if (l == 3){
                   double deck = 0;
                   for (int l3=0;l3<st.ntrees;l3++){
                       if(st.tr[l3].code==st.sp[i].code && st.tr[l3].layer==3 && st.tr[l3].out<0 && st.tr[l3].d < 7.0){
                           deck = deck+Math.PI*Math.pow(st.tr[l3].cw/2.0,2.0);
                       }
                   }
                   if (deck > 0){
                     yieldTable.addRow(rowData);
                     jTable1.setValueAt(st.sp[i].spDef.shortName,nrow,0);
                     jTable1.setValueAt(l,nrow,1);
                     jTable1.setValueAt("",nrow,3);
                     jTable1.setValueAt("",nrow,2);
                     jTable1.setValueAt("",nrow,4);
                     jTable1.setValueAt("",nrow,5);
                     jTable1.setValueAt("",nrow,6);
                     jTable1.setValueAt("",nrow,7);
                     jTable1.setValueAt("",nrow,8);
                     jTable1.setValueAt("",nrow,9);
                     jTable1.setValueAt("",nrow,10);
                     jTable1.setValueAt("",nrow,11);
                     jTable1.setValueAt("",nrow,12);
                     jTable1.setValueAt(f.format(0.0001*deck/st.size),nrow,13);
                  nrow=nrow+1;
                       
                   }
               }
               
               }
           }
           
           yieldTable.addRow(rowData);
           jTable1.setValueAt("Sum",nrow,0);          
           jTable1.setValueAt(f.format(sum_nha),nrow,7);
           jTable1.setValueAt(f.format(sum_gha),nrow,8);
           jTable1.setValueAt(f.format(sum_vol),nrow,9);
           jTable1.setValueAt(f.format(sum_nhaout),nrow,10);
           jTable1.setValueAt(f.format(sum_ghaout),nrow,11);
           jTable1.setValueAt(f.format(sum_vhaout),nrow,12);
           jTable1.setValueAt(f.format(st.degreeOfDensity),nrow,13); 
 
    }

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()==false){
            jCheckBox1.setSelected(false);
            makeTable();
         }
        else {
            jCheckBox1.setSelected(true);
            makeTableByLayer();
      }
//       formUpdate(stand);
       
    } 
      
}      

