/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forestsimulator.roots;

/**
 *
 * @author nagel
 */



public class RootsSpeciesSetting {
   int referenceNumber = 0;
   String shortname = null;
   int code = 0;
   String coarseRootFun = null;
   String fineRootFun = null;
   String totalRootFun = null;
   String smallRootFun = null;

   
   public RootsSpeciesSetting() {
    }
   
   public RootsSpeciesSetting(int rno, String sn, int c, String cr, String fr, String tr, String sr) {
       referenceNumber =rno;
       shortname= sn;
       code = c;
       coarseRootFun=cr;
       fineRootFun =fr;
       totalRootFun = tr;
       smallRootFun = sr;
    }

}
