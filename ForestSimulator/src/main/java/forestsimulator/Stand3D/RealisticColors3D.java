/*
 * RealisticColors3D.java
 *
 * Created on 8. Mai 2006, 12:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;
import javax.vecmath.Color3f;

/**
 *
 * @author jhansen
 */
public class RealisticColors3D {
    
    public static Color3f getCrownColor(int species, int crowntype){
       Color3f result= new Color3f();
       if(crowntype == 1){       
            if(species==511){result.x=0.3f; result.y=0.6f; result.z=0.3f; return result;}            
            if(species>=810 && species<=814){ result.x=0.5f; result.y=0.9f; result.z=0.0f; return result;}
            if(species==521){ result.x=0.3f; result.y=0.6f; result.z=0.4f; return result;}
            result.x=0.3f; result.y=0.5f; result.z=0.3f; return result;
       }
       else{                  
            if (species==211 || species==221) {result.x=0.4f; result.y=0.8f; result.z=0.0f; return result;}  
            if(species>=411 && species <=414){result.x=0.6f; result.y=0.9f; result.z=0.0f; return result;} 
            result.x=0.2f; result.y=0.7f; result.z=0.0f; return result;
       }
    }
    
    public static Color3f getTrunkColor(int species,int crowntype){
       Color3f result= new Color3f();
       if(crowntype == 1){                  
            if(species>=810 && species<=814){result.x=0.5f; result.y=0.35f; result.z=0.35f; return result;}
            else if(species==521){ result.x=0.4f; result.y=0.45f; result.z=0.25f; return result;}
            result.x=0.4f; result.y=0.3f; result.z=0.2f; return result;
       }
       else{                  
            if (species>=411 && species <=414){result.x=0.99f; result.y=0.99f; result.z=0.99f; return result;}
            if(species==311 || (species>=320 && species<=323)){ result.x=0.45f; result.y=0.53f; result.z=0.35f; return result;}
            result.x=0.4f; result.y=0.45f; result.z=0.4f; return result;
        }
    }    
}
