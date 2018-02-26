/** SimWald : class tgppmap draws a parallel stand projection
   Version 09-AUG-2002 */
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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

/** draws a parallel stand projection */
class TgPPmap extends JPanel implements MouseListener
{	
        private int x=-100,y=-100;
        Stand st =new Stand(); 
	
        /** variables for scaling the graph */
	double sk,xmax,ymax,xmin,ymin; 
	
        /** width and height of the drawing area */
	public int w,cwidth,cheight,d,h;
	Image img1;
	Image img2;
        int zoomStatus=0; //0=normal, 1=select lower corner, 2= select upper corner
        int xlzoom=0; int ylzoom=0; int xuzoom=0; int yuzoom=0;
        boolean doJPEG=false;
        
        
        StringBuffer s = new StringBuffer(500);
        TgJFrame frame;
        boolean screenToJpeg=false;
        boolean livingTrees=true;
        boolean thinnedTrees=false;
        boolean deadTrees=false;
        Color skyColor = new Color(193,245,245); 
        Color groundColor = new Color(193,245,162); 
        Color standGroundColor = new Color(109,245,81); 
        Dimension screensize;
	     
	public TgPPmap(Stand stl, TgJFrame parent)
	{	
            setBackground(Color.white); 
            addMouseListener(this); 
            
            Dimension scr= Toolkit.getDefaultToolkit().getScreenSize(); 
            setPreferredSize(new Dimension((((scr.width-140)/2)-(scr.width/50)), (scr.height/2)-(scr.height/50)));                           

            st=stl; 
            frame = parent;
            
            setLocation(cwidth,0); 
	    
//            Toolkit tk = Toolkit.getDefaultToolkit();
//	    img1 = tk.createImage("sky.jpg");
//	    img2 = tk.createImage("grass.jpg");
//	    MediaTracker tracker = new MediaTracker(this);
//	    tracker.addImage(img1,0);
//	    tracker.addImage(img2,0);
	    
//            try { tracker.waitForAll(); }
//	       catch (InterruptedException e ) {}
            
            
        }
            
        
        
	/** draws graph */
	public void paintComponent (Graphics g)
	{	
                double redu=1/(Math.tan(30*Math.PI/180.0)); 
		double xr,yr,xl;
                int wmin=0; int wStrich=0;
                double zoomFactor=0.0;
                int hStrich=0;
	    
	    //	System.out.println("Reduktion bei 30 Grad "+redu); 
		st.sortbyy();
		// get component size
		super.paintComponent(g); 
// next few lines are to print the jpg file always in the same size and then reset the size for the screen
               if (screenToJpeg==true) {
                    setSize(screensize);
                    screenToJpeg=false;
                }
               if (doJPEG==true) {
                    screensize= getSize();
                         setSize(new Dimension(1390,1400));
//IWF                    setSize(new Dimension(768,576));
                    screenToJpeg=true;
                }
//                
		Dimension d=getSize(); 
//		System.out.println("Gelbe Grafik "+d.width+"  "+d.height); 
		Color cbrowne = new Color(218,110,36);
		Color clightb = new Color(159,98,57); 
                Color cSilver = new Color(227,227,212); 
	
		
		w=d.width; h=d.height; 
		// draw filed new
//
                BufferedImage img = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
                if (doJPEG==true)
                {
                   g = img.getGraphics();
                }
//
		g.setColor(Color.white); 
		g.fillRect(0,0,w,h); 
        
		// determine scale factor for graph
		int i,k,kk; 
		xmax=-999.9; ymax=-999.9; ymin=99999.9; xmin=99999.9; 
		for (i=0; i<st.ntrees; i++)
		{	
                    if (st.tr[i].x>xmax) xmax=st.tr[i].x; 
                    if (st.tr[i].x<xmin) xmin=st.tr[i].x; 
                    if (st.tr[i].y>ymax) ymax=st.tr[i].y; 
                    if (st.tr[i].y<ymin) ymin=st.tr[i].y; 
		} //end of for loop
		
                for (i=0; i<st.ncpnt; i++)
		{
                    if (st.cpnt[i].x>xmax) xmax=st.cpnt[i].x; 
                    if (st.cpnt[i].x<xmin) xmin=st.cpnt[i].x; 
                    if (st.cpnt[i].y>ymax) ymax=st.cpnt[i].y; 
                    if (st.cpnt[i].y<ymin) ymin=st.cpnt[i].y; 
		} //end of for loop
		
            // add 1.0 m for a boundary
		xmin=xmin-1.0; xmax=xmax+1.0; ymin=ymin-1.0; ymax=ymax+1.0; 
            // scale
            // Zoom
                if (xlzoom!=0 && xuzoom!=0 && ylzoom!=0 && yuzoom!=0)
                {
                    zoomFactor=h/(ylzoom-yuzoom);
                    hStrich=ylzoom;
                    wmin=xlzoom;
                    wStrich=(int)w*w/(xuzoom-xlzoom);
                }
                else 
                {
                    wmin=0;
                    zoomFactor=1.0;
                    hStrich=h;
                    wStrich=w;
//                    System.out.println("Normal");
                }
 		
                sk=(wStrich-20)/((xmax-xmin)+(Math.cos(30.0*Math.PI/180.0)*(ymax-ymin)/redu)); 
		// 40.0 for 40m max. height
		double sky=(h-60)/(40.0+Math.sin(30.0*Math.PI/180.0)*(ymax-ymin)/redu);
                
                sky=sky*zoomFactor;
                
	//choose the smaller of the two scaling values
                //System.out.println("sk sky "+sk+" "+sky);
		
                if (sky<sk) sk=sky; 
	
        // draw stand area as rectangle
//                System.out.println("w,h,wmin,hmax"+w+" "+h+" "+wmin+" "+zoomFactor+" "+sk+" "+sky);
		int xp,yp,xp2,yp2,ra,rax,ray; 
		int x1,y1,x2,y2,xpp,ypp; 
		double ha,hr,hr1,ran,wink,dist,astst; 
		int xpoly[]; 
		xpoly= new int[50]; 
		int ypoly[]; 
		ypoly= new int[50]; 
                
	// Stand area is calculated	
		xp=10+(int)((xmin-xmin)*sk); 
		yp=h-40-(int)((ymin-ymin)*sk); 
		xp2=10+(int)((xmax-xmin)*sk); 
		yp2=h-40-(int)((ymax-ymin)*sk);
        // draw stand area
                int yppx=0;
                for (int j=0;j<st.ncpnt;j++)
                {
		   xl=(st.cpnt[j].y-ymin)/redu; 
		   xr=Math.cos(30.0*Math.PI/180.0)*xl; 
		   yr=Math.sin(30.0*Math.PI/180.0)*xl; 
                   xpoly[j]=10-wmin+(int)((st.cpnt[j].x+xr-xmin)*sk); 
		   ypoly[j]=(int)(h+(h-hStrich)*zoomFactor-40-(yr)*sk); 
                   if (yppx < ypoly[j]) yppx=ypoly[j];
                }
		
                if (yppx>2*h/3) yppx=(int)(2.0*h/3.0);
//		g.drawImage(img1,0,0,w,yppx,this);
                g.setColor(skyColor);
                g.fillRect(0,0,w,yppx);
		
//                g.setColor(new java.awt.Color(204,255,153));
                g.setColor(groundColor);
                g.fillRect(0,yppx,wStrich,hStrich);
	
//		Color cfl = new Color(156,151,48); 
		g.setColor(standGroundColor); 
		g.fillPolygon(xpoly,ypoly,st.ncpnt); 
//		 System.out.println("tgppmap, xmin,..."+xmin+" "+xmax+" "+ymin+" "+ymax);
		
                // draw tree stems 
		//
		for (i=0; i<st.ntrees; i++)
		if (st.tr[i].out<1 || (st.tr[i].out>=st.year-10 
		    && st.tr[i].outtype==1) || (st.tr[i].out==st.year && st.tr[i].outtype==2)  )
		  
		{
                // decide if tree to plot
                   boolean plotTree = false;
                   if (st.tr[i].out < 0 && livingTrees==true) plotTree=true;
                   if (st.tr[i].out == st.year && st.tr[i].outtype==2 && thinnedTrees==true) plotTree=true;
                   if (st.tr[i].out > st.year-10 && st.tr[i].outtype==1 && deadTrees==true) plotTree=true;
                   if (plotTree==true){ 
                //get Tree Color from Color.txt
                    Color treecolor = new java.awt.Color(st.tr[i].sp.spDef.colorRed, 
                                    st.tr[i].sp.spDef.colorGreen,st.tr[i].sp.spDef.colorBlue);
                                      
                //relative correction
			xl=(st.tr[i].y-ymin)/redu; 
			xr=Math.cos(30.0*Math.PI/180.0)*xl; 
			yr=Math.sin(30.0*Math.PI/180.0)*xl; 
			xp=10-wmin+(int)((st.tr[i].x+xr-xmin)*sk); 
			yp=(int)(h+(h-hStrich)*zoomFactor-40-(yr)*sk); 
			ra=(int)(2*sk*st.tr[i].d/200); 
			if (ra==0) ra=1; 
		//oval second values is the width and height
			if (st.tr[i].out<=0) g.setColor(Color.green); 
					
		// draw stem base to top
			x1=(int)(xp); 
			y2=(int)(yp-sk*st.tr[i].h); 
			x2=(int)(xp+sk*st.tr[i].d/100.0);
			if (x2 == x1) x2=x1+1; 
			y1=(int)(yp); 
                        //System.out.println(x1+"  "+y1+"  "+x2+"  "+y2);
			xpoly[0]=x1; ypoly[0]=y1; 
			xpoly[1]=x2; ypoly[1]=y1; 
			xpoly[2]=(int)((x1+x2)/2.0); ypoly[2]=y2; 
		        xpoly[3]=(int)((x1+x2)/2.0); ypoly[3]=y2; 
		    
			if (st.tr[i].code < 500) g.setColor(clightb); 
			if (st.tr[i].code > 500) g.setColor(cbrowne); 
		// dead wood draw stems
			if (st.tr[i].out>0 && st.tr[i].outtype==1) 
                        {
                            g.setColor(cSilver);
                        }
			g.drawPolygon(xpoly,ypoly,4);
			g.fillPolygon(xpoly,ypoly,4);
                        
//wenn der Baum ausgezeichnet ist eine rote Markierung
			if (st.tr[i].out==st.year && st.tr[i].outtype==2)
			{ 
                              g.setColor(Color.yellow); 
                              xpoly[0]=x1; ypoly[0]=(int)(yp-sk*1.3); 
                              xpoly[1]=x2; ypoly[1]=(int)(yp-sk*1.3); 
			      xpoly[2]=x2; ypoly[2]=(int)(yp-sk*1.3)-2; 
                              xpoly[3]=x1; ypoly[3]=(int)(yp-sk*1.3)-2; 
 		              g.fillPolygon(xpoly,ypoly,4);
                        } 
                        
// zeichnen der Kronen Dreieck für eine Fichte, Oval für Buche 
			if (st.tr[i].sp.spDef.crownType>0 && (st.tr[i].out<0 || st.tr[i].outtype>1))  //conifer
			{ 
                            xpoly[0]=(int)(xp); 
                            ypoly[0]=(int)(yp-sk*st.tr[i].h);
                            xpoly[1]=(int)(xp-sk*st.tr[i].cw/2.0); 
                            ypoly[1]=(int)(yp-sk*st.tr[i].cb);
                            xpoly[2]=(int)(xp+sk*st.tr[i].cw/2.0); 
                            ypoly[2]=(int)(yp-sk*st.tr[i].cb);
                            
                            g.setColor(treecolor);
                            if (st.tr[i].out<0) {
                                g.fillPolygon(xpoly,ypoly,3);
                            }
                            
	                  
                            g.setColor(treecolor.brighter());
                            g.drawPolygon(xpoly,ypoly,3);
		
			}
			if (st.tr[i].sp.spDef.crownType==0 && (st.tr[i].out<0 || st.tr[i].outtype>1))  //Laubbaum
			{ 
                            xpoly[0]=(int)(xp); 
                            ypoly[0]=(int)(yp-sk*(st.tr[i].h-((st.tr[i].h-st.tr[i].cb)/2.0)));
                            rax=(int)(sk*st.tr[i].cw/2.0);
                            ray=(int)(sk*((st.tr[i].h-st.tr[i].cb)/2.0));
                            
                            g.setColor(treecolor);
                         
                            if (st.tr[i].out<0) {
                               g.fillOval(xpoly[0]-rax,ypoly[0]-ray,2*rax,2*ray);
                            }
		                           
                            g.setColor(treecolor.darker());
                            g.drawOval(xpoly[0]-rax,ypoly[0]-ray,2*rax,2*ray);
			}
			// draw tree end
                   }
		} // end of for loop for drawing

//-----------------------------------------------------------------------------                
//Copy to JPEG
  
               if (doJPEG==true)
               {
                   try
                   {
                       Integer nrx = new Integer(st.year);
                       File outputfile= new File(frame.workingDir, "sv"+nrx.toString()+".jpg");
                       ImageIO.write(img, "jpg", outputfile);
                       doJPEG=false;
                       neuzeichnen();

                   }
                   catch (Exception e)
                   {
                       System.out.println(e); 
                   }		 
                }                
                
 // Zoom controll
           
           g.setColor(Color.red); 
           if (zoomStatus==1) g.drawString("select lower left corner",50,h-5); 
           if (zoomStatus==2) g.drawString("select upper right corner",50,h-5); 
               
	}
	/** method to renew the graph */
	public void neuzeichnen()	
        {
            cwidth=88; cheight=99;  repaint();
            st.sortbyd();
        }
	public void setdh(int dx, int hx)
	{	cheight=hx; 
		cwidth=dx; 
	}
        
        	/** method to zoom by mouse */
	public void mousePressed (MouseEvent e)
	{	int x=e.getX(); int y=e.getY(); 
                if (zoomStatus>0){
                    if (zoomStatus==1) {
                        xlzoom=x; 
		        ylzoom=y; 

                    }
                    if (zoomStatus==2) {
                        xuzoom=x; 
		        yuzoom=y; 
                    }
                    zoomStatus=zoomStatus+1;
                    if (zoomStatus==3) zoomStatus=0;
                    repaint();
                }
                
        }
        	/** empty methods */
	public void mouseReleased (MouseEvent e) {	}
	public void mouseEntered (MouseEvent e) {	}
	public void mouseExited (MouseEvent e) {	}
	public void mouseClicked (MouseEvent e) {	}
        public void setLivingTrees(boolean status) {livingTrees=status; }
        public void setThinnedTrees(boolean status){thinnedTrees=status; }
        public void setDeadTrees(boolean status){deadTrees=status;} 
        public void setSkyColor(Color c){skyColor= c;} 
        public Color getSkyColor(){return skyColor;} 
        public void setGroundColor(Color c){groundColor= c;} 
        public Color getGroundColor(){return groundColor;} 
        public void setStandGroundColor(Color c){standGroundColor= c;} 
        public Color getStandGroundColor(){return standGroundColor;} 
//
        void getJPEG(){
            doJPEG=true;
       }    
        

}
