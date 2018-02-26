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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TgTreatmentInfo extends JPanel {
    int nrow=20; int ncol=4;
    JPanel ptable = new JPanel(new GridLayout(20,4,10,5));
    int ncells=ncol*nrow;
    JLabel txt[] = new JLabel[ncells];

    public TgTreatmentInfo() {
        NumberFormat f=NumberFormat.getInstance();
        f=NumberFormat.getInstance(new Locale("en","US"));
        f.setMaximumFractionDigits(1);
        f.setMinimumFractionDigits(1);  
              
        
        for (int i=0;i<ncells;i++) { 
            txt[i]=new JLabel(""); 
            txt[i].setForeground(Color.BLACK);
        }

        txt[0].setForeground(Color.DARK_GRAY);
        txt[2].setForeground(Color.DARK_GRAY);
        
        txt[0].setText("Maximum volume to harvest: ");       
        txt[1].setText("");
        txt[2].setText("Maximum volume for thinning: ");
        txt[3].setText(""); 

        for (int i=4; i<=7; i++){txt[i].setForeground(Color.DARK_GRAY);}
        txt[4].setText("Species ");
        txt[5].setText("Target Mix% ");
        txt[6].setText("Target DBH ");
        txt[7].setText("Height of first Thinning ");  
        
       

        for (int i=0;i<ncells;i++) ptable.add(txt[i]); 

        JScrollPane spane = new JScrollPane();

        Dimension scr= Toolkit.getDefaultToolkit().getScreenSize();             
        spane.setPreferredSize(new Dimension(((scr.width/2)+(scr.width/5)), (scr.height/10))); 

        
        spane.getViewport().add(ptable);      

        setLayout(new BorderLayout());
        add(spane, BorderLayout.CENTER);

    }
    void formUpdate(Stand st) {
        //set color back to black
        for (int i=8;i<ncells;i++) { 
            txt[i].setForeground(Color.BLACK);
        }        
        txt[1].setForeground(Color.BLACK);
        txt[3].setForeground(Color.BLACK);
        
        NumberFormat f=NumberFormat.getInstance();
        f=NumberFormat.getInstance(new Locale("en","US"));
        f.setMaximumFractionDigits(1);
        f.setMinimumFractionDigits(1);  
        
        NumberFormat f2=NumberFormat.getInstance();
        f2=NumberFormat.getInstance(new Locale("en","US"));
        f2.setMaximumFractionDigits(0);
        f2.setMinimumFractionDigits(0);             

        for (int i=0;i<18;i++){
            txt[(i+1)*ncol+4].setText("");
            txt[(i+1)*ncol+5].setText("");
            txt[(i+1)*ncol+6].setText("");
            txt[(i+1)*ncol+7].setText("");

        }
        double sumTargetCrownPercent=0.0;
        for (int i=0;i<st.nspecies;i++){
            txt[(i+1)*ncol+4].setText(new Integer(st.sp[i].code).toString());
            txt[(i+1)*ncol+5].setText(f.format(st.sp[i].trule.targetCrownPercent));
            txt[(i+1)*ncol+6].setText(f.format(st.sp[i].trule.targetDiameter));
            txt[(i+1)*ncol+7].setText(f.format(st.sp[i].trule.minCropTreeHeight));
            sumTargetCrownPercent=sumTargetCrownPercent+Math.round(st.sp[i].trule.targetCrownPercent);
        }
        
        txt[(st.nspecies+1)*ncol+4].setForeground(Color.DARK_GRAY); 
        txt[(st.nspecies+1)*ncol+4].setText("Sum Target Crown Percentage: ");
        if (sumTargetCrownPercent>=101||sumTargetCrownPercent<=99){txt[(st.nspecies+1)*ncol+5].setForeground(Color.RED);}
        else {txt[(st.nspecies+1)*ncol+5].setForeground(Color.DARK_GRAY);}
        txt[(st.nspecies+1)*ncol+5].setText(f2.format(sumTargetCrownPercent));
        txt[(st.nspecies+1)*ncol+6].setText("");
        txt[(st.nspecies+1)*ncol+7].setText("");
        
        
        if (st.trule.maxHarvestVolume==0.0){txt[1].setForeground(Color.RED);}
        else {txt[1].setForeground(Color.BLACK);}
        txt[1].setText(f.format(st.trule.maxHarvestVolume));        
        
        if (st.trule.maxThinningVolume==0.0){txt[3].setForeground(Color.RED);}
        else {txt[3].setForeground(Color.BLACK);}
        
        txt[3].setText(f.format(st.trule.maxThinningVolume)); 
        

    } 
          
      
}      

