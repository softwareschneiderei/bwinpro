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
import org.jfree.data.xy.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ChartUtilities;
import java.io.*;


import java.awt.*;
import javax.swing.*;
import java.util.*;

/** draws a stand map and allows thinning by mouse click */
class TgGrafik extends JPanel 
{
    Stand st =new Stand();
    int graphType=1;
/** width and height of the drawing area */
    public int w,h; 
//    public dvert dver= new dvert();
//    public heightdistrib hdist = new heightdistrib(); 
     ChartPanel chartPanel;
     JFreeChart jfreeChart = null;
     String preferredLanguage="en";
     String jpgFilename = "Grafik.jpg";
   

    public TgGrafik(Stand stl)
    {  
        st = stl;
        Dimension scr= Toolkit.getDefaultToolkit().getScreenSize(); 
        setPreferredSize(new Dimension(700,500));      
    }
	
    public void starten()
    {
        Dimension d=getSize();
	if (st.ntrees >0) 
	{
            GraphicDiameterDistribution chart = new GraphicDiameterDistribution( preferredLanguage);
            chartPanel = new ChartPanel(chart.createChart(st) );
            chartPanel.repaint();
            this.removeAll();
            this.add(chartPanel);
            this.repaint();
        }
 

    }

    public void drawGraph()
    {
        if (st.ntrees >0){
            if (graphType==0){
                GraphicSpeciesByCrownSurfaceArea chart = new GraphicSpeciesByCrownSurfaceArea(preferredLanguage);
                jfreeChart=chart.createChart(st);
                chartPanel = new ChartPanel(jfreeChart);
            }
            if (graphType==1){
                GraphicDiameterDistribution chart = new GraphicDiameterDistribution(preferredLanguage);
                jfreeChart=chart.createChart(st);
                chartPanel = new ChartPanel(jfreeChart);
                
            }
            if (graphType==2){
                GraphicDiameterDistributionCT chart = new GraphicDiameterDistributionCT(preferredLanguage);
                jfreeChart=chart.createChart(st);
                chartPanel = new ChartPanel(jfreeChart);
            } 
            if (graphType==3){
                GraphicHeightDiameterPlot chart = new GraphicHeightDiameterPlot(preferredLanguage);
                jfreeChart=chart.createChart(st);
                chartPanel = new ChartPanel(jfreeChart);
            } 
            chartPanel.setPreferredSize(new Dimension(600, 400));
            this.removeAll();
            this.add(chartPanel);
           
        }

    }
    
    public void  neuzeichnen(){
        drawGraph();
    }
    
    public void setGraphType(int i){
        graphType=i;
    }
    
    public void saveToJPEG(String dir){
        File fn= new File(jpgFilename);
        ChartUtilities ut = null;
        try{
            ut.saveChartAsJPEG(fn ,jfreeChart,  600, 400);
        }
      	catch (Exception e){	System.out.println(e); } 

    }
      public void setJPGFilename(String fn){jpgFilename=fn;}


}
//------------------------------------------------------ 
 class GraphicSpeciesByCrownSurfaceArea {
   JFreeChart chart;
   ResourceBundle messages;

   public GraphicSpeciesByCrownSurfaceArea(String preferredLanguage){
      Locale currentLocale;
      currentLocale = new Locale(preferredLanguage, "");
      messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
  }  
  public JFreeChart getChart(){
      return chart;
  }
   
  public JFreeChart createChart(Stand st) {
// create the dataset...
     DefaultPieDataset dataset = new DefaultPieDataset();
     for (int j=0;j<st.nspecies;j++){
           dataset.setValue(st.sp[j].spDef.shortName,  st.sp[j].percCSA);
     }
//     
     JFreeChart chart = ChartFactory.createPieChart(
        messages.getString("speciespercentage"), // chart title
        dataset, // data
        true, // include legend
        true, // tooltips?
        false // URLs?
       );
       PiePlot piePlot = (PiePlot) chart.getPlot();
       for (int i=0;i<st.nspecies;i++){
          piePlot.setSectionPaint(i, new Color(st.sp[i].spDef.colorRed,st.sp[i].spDef.colorGreen,st.sp[i].spDef.colorBlue));
     }
     return chart;
     
	} 
  
  public void redraw(){
 //     repaint();
  } 
  

   }
	
	
//------------------------------------------------------ 
 class GraphicDiameterDistribution {
   JFreeChart chart;
   ResourceBundle messages;

   public GraphicDiameterDistribution(String preferredLanguage)
  {   Locale currentLocale;
      currentLocale = new Locale(preferredLanguage, "");
      messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
  }  
  public JFreeChart getChart(){
      return chart;
  }
  
   
  public JFreeChart createChart(Stand st) {


// create the dataset...
     DefaultCategoryDataset dataset = new DefaultCategoryDataset();
// Start and end of classes      
      int minBar=24;
      int maxBar=0;
      for (int i=0;i< 25;i++){
          double anz2=0;
             for (int k=1;k<st.ntrees;k++){
                 if ((st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out < 0) 
                     ) anz2= anz2+st.tr[k].fac;
             }
             if (anz2 > 0 ){
                if (minBar > i) minBar=i;
                if (maxBar < i) maxBar=i;
             }
     }
     
     for (int i=0;i< 25;i++){
         for (int j=0;j<st.nspecies;j++){
             double anz=0;
             for (int k=0;k<st.ntrees;k++){
                 if ((st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out < 0) 
                     && st.tr[k].code==st.sp[j].code) anz= anz+st.tr[k].fac;
                 
             }
             if (anz >= 0 && i>=minBar && i <= maxBar ){
                  Integer m = (5*i)+2;
                  dataset.addValue(anz/st.size, st.sp[j].spDef.shortName, m.toString());
                 
             }
         }
     }
//     
     JFreeChart chart = ChartFactory.createStackedBarChart(
        messages.getString("diameterDistribution"), // chart title
        messages.getString("dbhClass"), // domain axis label
        messages.getString("numberOfStems"), // range axis label
        dataset, // data
        PlotOrientation.VERTICAL, // orientation
        true, // include legend
        true, // tooltips?
        false // URLs?
       );
     CategoryPlot plot = chart.getCategoryPlot();
//     plot.setBackgroundPaint(Color.lightGray);
//     plot.setDomainGridlinePaint(Color.white);
//     plot.setDomainGridlinesVisible(true);
//     plot.setRangeGridlinePaint(Color.white);
// reenderer
     StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
     renderer.setDrawBarOutline(false);
// set up gradient paints for series...
     for (int i=0;i<st.nspecies;i++){
//          renderer.setSeriesStroke(i,new BasicStroke(1.5f));
//          GradientPaint gp2 = new GradientPaint(
//          0.0f, 1.5f, Color.red,
//            1.0f, 0.5f, Color.green);
          renderer.setSeriesPaint(i, new Color(st.sp[i].spDef.colorRed,st.sp[i].spDef.colorGreen,st.sp[i].spDef.colorBlue));
//          renderer.setSeriesPaint(i, gp2);
     }
     return chart;
	} 
  
  public void redraw(){
 //     repaint();
  } 
   }
 
// -------------------------------------------------------
  class GraphicDiameterDistributionCT {
   JFreeChart chart;
   ResourceBundle messages;

   public GraphicDiameterDistributionCT(String preferredLanguage)
  {   Locale currentLocale;
      currentLocale = new Locale(preferredLanguage, "");
      messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
  }  
  public JFreeChart getChart(){
      return chart;
  }
   
  public JFreeChart createChart(Stand st) {
// create the dataset...
     DefaultCategoryDataset dataset = new DefaultCategoryDataset();
// Start and end of classes      
      int minBar=24;
      int maxBar=0;
      for (int i=0;i< 25;i++){
          double anz2=0;
             for (int k=0;k<st.ntrees;k++){
                 if ((st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out < 0) 
                     && st.tr[k].crop==true) anz2= anz2+st.tr[k].fac;
             }
             if (anz2 > 0 ){
                if (minBar > i) minBar=i;
                if (maxBar < i) maxBar=i;
             }
     }
     for (int i=0;i< 25;i++){
         for (int j=0;j<st.nspecies;j++){
             double anz=0;
             for (int k=0;k<st.ntrees;k++){
                 if ((st.tr[k].d > i*5) && (st.tr[k].d <= (i+1)*5) && (st.tr[k].out < 0) 
                     && st.tr[k].code==st.sp[j].code && st.tr[k].crop==true) anz= anz+st.tr[k].fac;
                 
             }
             if (anz >= 0 && i>=minBar && i <= maxBar ){
                  Integer m = (5*i)+2;
                  dataset.addValue(anz/st.size, st.sp[j].spDef.shortName, m.toString());
                 
             }
         }
     }
//     
     JFreeChart chart = ChartFactory.createStackedBarChart(
        messages.getString("diameterDistributionCT"), // chart title
        messages.getString("dbhClass"), // domain axis label
        messages.getString("numberOfStems"), // range axis label
        dataset, // data
        PlotOrientation.VERTICAL, // orientation
        true, // include legend
        true, // tooltips?
        false // URLs?
       );
     CategoryPlot plot = chart.getCategoryPlot();
     plot.setBackgroundPaint(Color.lightGray);
     plot.setDomainGridlinePaint(Color.white);
     plot.setDomainGridlinesVisible(true);
     plot.setRangeGridlinePaint(Color.white);
// reenderer
     StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
     renderer.setDrawBarOutline(false);
// set up gradient paints for series...
     for (int i=0;i<st.nspecies;i++){
          renderer.setSeriesPaint(i, new Color(st.sp[i].spDef.colorRed,st.sp[i].spDef.colorGreen,st.sp[i].spDef.colorBlue));
     }

      
     return chart;
	} 
  
  public void redraw(){
 //     repaint();
  } 
   }
// -------------------------------------------------------
  class GraphicHeightDiameterPlot {
   JFreeChart chart;
   ResourceBundle messages;

   public GraphicHeightDiameterPlot(String preferredLanguage)
  {   Locale currentLocale;
      currentLocale = new Locale(preferredLanguage, "");
      messages = ResourceBundle.getBundle("forestsimulator.standsimulation.TgJFrame",currentLocale);
  }  
  public JFreeChart getChart(){
      return chart;
  }
   
  public JFreeChart createChart(Stand st) {
// create the dataset...
     XYSeriesCollection dataset = new XYSeriesCollection();
     for (int i=0;i<st.nspecies;i++){
          XYSeries series = new XYSeries(st.sp[i].spDef.shortName);
//          DefaultCategoryDataset dataset = new DefaultCategoryDataset();
          for (int k=0;k<st.ntrees;k++){
                 if ((st.tr[k].d > 0) && st.tr[k].out < 0 && st.tr[k].code==st.sp[i].code) 
                    series.add(st.tr[k].d,st.tr[k].h);
                 
             }
         dataset.addSeries(series);
     }
//     
	JFreeChart chart = ChartFactory.createScatterPlot(
	     messages.getString("heightDiameter"),
	     messages.getString("dbh"),
	     messages.getString("height"),
	     dataset,
             org.jfree.chart.plot.PlotOrientation.VERTICAL,
             true,
	     false,
             false);
     XYPlot plot = chart.getXYPlot();
     plot.setDomainCrosshairVisible(true);
     plot.setRangeCrosshairVisible(true);  
//
//         XYPlot plot = chart.getXYPlot();
     XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
     for (int i=0;i<st.nspecies;i++){
         renderer.setSeriesLinesVisible(i, false);
         renderer.setSeriesPaint(i, new Color(st.sp[i].spDef.colorRed,st.sp[i].spDef.colorGreen,st.sp[i].spDef.colorBlue));
     }
         plot.setRenderer(renderer);

      
     return chart;
	} 
  
  public void redraw(){
 //     repaint();
  }

   }





