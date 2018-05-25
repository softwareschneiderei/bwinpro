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
import java.awt.event.*;
import javax.swing.*;

class TgTreatmentForm extends JPanel implements ActionListener {

    JPanel ptable = new JPanel(new GridLayout(2, 4, 10, 5));
    JPanel mainPanel = new JPanel();
    JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 5));
    JTextField txt[] = new JTextField[4];
    JTextField maxHav = new JTextField();
    JTextField maxThi = new JTextField();
    JButton jb1 = new JButton("use settings");
    Stand st = new Stand();
    TgTreatmentInfo tti = new TgTreatmentInfo();
    TgJFrame frame;

    public TgTreatmentForm(Stand stparent, TgTreatmentInfo ttiparent, TgJFrame frameparent) {
        topPanel.add(new JLabel("Maximum volume to harvest: "));
        topPanel.add(maxHav);
        topPanel.add(new JLabel("Maximum volume for thinning: "));
        topPanel.add(maxThi);
        for (int i = 0; i < 4; i++) {
            txt[i] = new JTextField("");
        }
        JLabel lb01 = new JLabel("Species ");
        JLabel lb02 = new JLabel("Mix % ");
        JLabel lb03 = new JLabel("Target DBH");
        JLabel lb04 = new JLabel("Height of first Thinning");
        ptable.add(lb01);
        ptable.add(lb02);
        ptable.add(lb03);
        ptable.add(lb04);
        for (int i = 0; i < 4; i++) {
            ptable.add(txt[i]);
        }
        jb1.addActionListener(this);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(ptable, BorderLayout.CENTER);
        mainPanel.add(jb1, BorderLayout.SOUTH);
        add(mainPanel);
        frame = frameparent;
        st = stparent;
        tti = ttiparent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cmd = e.getActionCommand();

        if (cmd.equals("use settings")) {
            setRule(tti, st);
        }
    }

// updates the Treatment rules
    void setRule(TgTreatmentInfo tti, Stand st) {
        int merk = 0;
        NumberFormat f = NumberFormat.getInstance();
        f = NumberFormat.getInstance(new Locale("en", "US"));
        f.setMaximumFractionDigits(1);
        f.setMinimumFractionDigits(1);

        for (int i = 0; i < st.nspecies && i < 4; i++) {
            Integer fcode;
            double fx = 0;
            String s = txt[i].getText().trim();
            if (s.length() > 0) {
                fx = Double.parseDouble(txt[i].getText());
                merk = -9;
                for (int j = 0; j < st.nspecies; j++) {
                    if (st.sp[j].code == fx) {
                        merk = j;
                    }
                }
                if (merk >= 0) {
                    if ((txt[i + 1].getText().compareTo("") != 0)) {
                        st.sp[merk].trule.targetCrownPercent = Double.parseDouble(txt[i + 1].getText());
                    }
                    if ((txt[i + 2].getText().compareTo("") != 0)) {
                        st.sp[merk].trule.targetDiameter = Double.parseDouble(txt[i + 2].getText());
                    }
                    if ((txt[i + 3].getText().compareTo("") != 0)) {
                        st.sp[merk].trule.minCropTreeHeight = Double.parseDouble(txt[i + 3].getText());
                    }
                    //if ((.getText().compareTo("")!=0))
                    //{st.sp[merk].trule.thinningIntensity =Double.parseDouble(.getText());}                     
                }
            }
        }

        if ((maxHav.getText().compareTo("") != 0)) {
            st.trule.maxHarvestVolume = Double.parseDouble(maxHav.getText());
        }
        if ((maxThi.getText().compareTo("") != 0)) {
            st.trule.maxThinningVolume = Double.parseDouble(maxThi.getText());
        }
        for (int i = 0; i < st.ntrees; i++) {
            st.tr[i].crop = false;
            st.tr[i].tempcrop = false;
        }

        tti.formUpdate(st);
        frame.zf.neuzeichnen();
        //tfUpdateTrue=true;
        //frame.updatetp();
    }
}

/*
   void formUpdate(stand st) {
       if (st.nspecies>0){
	   NumberFormat f=NumberFormat.getInstance();
	   f=NumberFormat.getInstance(new Locale("en","US"));
	   f.setMaximumFractionDigits(1);
	   f.setMinimumFractionDigits(1);
           
        //if (st.trule.maxHarvestVolume<=0.0) st.trule.maxHarvestVolume=100.0;
        //if (st.trule.maxThinningVolume<=0.0) st.trule.maxThinningVolume=100.0;
        //maxHav.setText(f.format(st.trule.maxHarvestVolume));
        //maxThi.setText(f.format(st.trule.maxThinningVolume));
        
        /*
        for (int i=0;i<16;i++) { txt[i].setText(" "); }
        for (int i=0;i<st.nspecies && i<5 ;i++)
        { 
          txt[i*4].setText(new Integer(st.sp[i].code).toString());
          txt[i*4+1].setText(f.format(st.sp[i].trule.TargetCrownPercent));
          txt[i*4+2].setText(f.format(st.sp[i].trule.TargetDiameter));
          txt[i*4+3].setText(f.format(st.sp[i].trule.minCropTreeHeight));}
 */
 /*
       }
    } */

 /* // set Percentage to 100%
        double sumPercent=0.0;
        for (int i=0;i<st.nspecies && i<5;i++) {
            if ((txt[i*4+1].getText().compareTo("")!=0)){
                sumPercent=sumPercent+Double.parseDouble(txt[i*4+1].getText());
            }   
        }
        for (int i=0;i<st.nspecies && i<5;i++) {
            if ((txt[i*4+1].getText().compareTo("")!=0)){
                txt[i*4+1].setText(f.format(100.0*Double.parseDouble(txt[i*4+1].getText())/sumPercent));
            }
        }*/
