package forestsimulator.Stand3D;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import WaldPlaner.Tools.WPMath;
import java.awt.Graphics2D;


public class TransparentWindow extends JDialog implements MouseMotionListener, KeyListener, MouseListener{
    private Graphics2D tempImgGraphics;
    private Image backgroundImage,temporaryImage;
    private Point mousePointer;
    private UserData u;

public TransparentWindow(UserData ud, Container home,int x, int y) {
    setModal(true);
    setUndecorated(true);
    init();
    setUserData(ud, home.getGraphics(), home.getLocationOnScreen().x+x,home.getLocationOnScreen().y+y);
}

private void setUserData(UserData ud, Graphics g, int x, int y){
    u=ud;
    if(isVisible()) setVisible(false);    
    int w=100;
    int h=100;
    if(u!=null){       
        int w2=0;
        int h2=0;
        if(g!=null) w2= (int)g.getFontMetrics().getStringBounds("Baum"+u.name.trim(),g).getWidth()+20;
        if(w2>w)w=w2;
        h2= 5*g.getFontMetrics().getHeight()+3;
        if (h2>h) h=h2;
    }
    setBounds(0, 0,w,h);  
    capture();  
    setLocation(x,y);   
    setVisible(true);  
}

public void init(){
    addMouseMotionListener(this);
    this.setAlwaysOnTop(true);
    int w=100;
    int h=100;    
    setBounds(0,0,w,h);    
    this.addKeyListener(this);
    this.addMouseListener(this);
}

public void capture() {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    try {
        Robot aRobot = new Robot();
        Rectangle rect = new Rectangle(0,0,dim.width,dim.height);        
        backgroundImage = aRobot.createScreenCapture(rect);
    }catch(AWTException awte) {
        System.out.println("robot exception occurred");
    }
}

public void mouseDragged(MouseEvent aMouseEvent) {
    Point aPoint = aMouseEvent.getPoint();
    int x = getX()+aPoint.x-mousePointer.x;
    int y = getY()+aPoint.y-mousePointer.y;
    setLocation(x,y);
    Graphics graphics = getGraphics();
    paint(graphics);
}


public void mouseMoved(MouseEvent aMouseEvent){
    mousePointer = aMouseEvent.getPoint();
}

public void keyPressed(KeyEvent e){}
public void keyReleased(KeyEvent e){}
public void keyTyped(KeyEvent e){this.dispose();}

public void mouseExited(MouseEvent e){}
public void mouseEntered(MouseEvent e){}
public void mouseReleased(MouseEvent e){}
public void mousePressed(MouseEvent e){}
public void mouseClicked(MouseEvent e){
    int x=e.getX();
    int y=e.getY();
    if (x>this.getWidth()-15 && x < this.getWidth() && y>0 && y<15)this.dispose();
}

public void paint(Graphics graphics){
    if (temporaryImage == null) {
        temporaryImage = createImage(getWidth(),getHeight());
        tempImgGraphics = (Graphics2D) temporaryImage.getGraphics();
    }    
    tempImgGraphics.drawImage(backgroundImage,0,0,getWidth(),getHeight(),
                              getX(),getY(),getX()+getWidth(),getY()+getHeight(),null);
   
    //often used coords/values:
    int x1=getWidth()-15;
    int x2=getWidth()-5;
    int y1=4;
    int y2=14;
    int textheight= tempImgGraphics.getFontMetrics().getHeight()+2;
    
    //transparent
    float alpha = 0.5f;
    tempImgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));  
    tempImgGraphics.setColor(Color.black);  
    tempImgGraphics.fillRoundRect(0,0, getWidth()-1, getHeight()-1, 10, 10);
    
    tempImgGraphics.setColor(Color.red); 
    tempImgGraphics.fillRect(x1,y1, 10, 10);
        
    // non transparent
    tempImgGraphics.setColor(Color.red);
    alpha = 1f;
    tempImgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));     
    tempImgGraphics.drawRect(x1,y1,10,10);
    tempImgGraphics.drawLine(x1+3,y1+3,x2-3,y2-3); 
    tempImgGraphics.drawLine(x2-3,y1+3,x1+3,y2-3); 
    tempImgGraphics.drawRoundRect(0,0, getWidth()-1, getHeight()-1,10,10);
    tempImgGraphics.setColor(Color.red);
    tempImgGraphics.drawString("Baum: "+u.name.trim(),5,textheight-2);
    tempImgGraphics.drawLine(0, textheight+2, getWidth()-1, textheight+2);
    tempImgGraphics.drawString("BHD "+Double.toString(Math.round(u.dbh)),5,textheight*2);
    tempImgGraphics.drawString("Höhe "+Double.toString(Math.round(u.h)),5, (3*textheight));
    String t="Alter "+u.age;
    if(!u.living && u.standing) t="tot / stehend";
    if(!u.living && !u.standing) t="tot / entnommen";
    tempImgGraphics.drawString(t,5, (4*textheight));
    tempImgGraphics.drawString("Art: "+u.specname,5, (5*textheight));
    graphics.drawImage(temporaryImage,0,0,null);    
}

}