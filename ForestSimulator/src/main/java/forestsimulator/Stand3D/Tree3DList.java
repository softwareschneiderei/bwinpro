/*
 * Tree3DList.java
 *
 * Created on 6. April 2006, 14:54
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package forestsimulator.Stand3D;

import java.util.Arrays;
import javax.media.j3d.*;
import treegross.base.OutType;
import treegross.base.Tree;

/**
 *
 * @author jhansen
 */
public class Tree3DList {

    public Tree3D[] trees = null;
    public int[] sts = null;
    private int standyear;

    /**
     * Creates a new instance of Tree3DList with all trees and attaches all
     * trees to the parent group
     */
    public Tree3DList(Tree[] tgtrees, int ntrees, Texture2D[] bltexture, boolean speciescolor, boolean textured, boolean sstatus, Group parent, int[] speciestoshow, boolean showdead, StandBase3D base, int year) {
        setListData(tgtrees, ntrees, bltexture, speciescolor, textured, sstatus, parent, speciestoshow, showdead, base, year);
    }

    Tree3DList() {
        super();
    }

    public final void setListData(Tree[] tgtrees, int ntrees, Texture2D[] bltexture, boolean speciescolor, boolean textured, boolean sstatus, Group parent, int[] speciestoshow, boolean showdead, StandBase3D base, int year) {
        standyear = year;
        sts = speciestoshow;
        int n3dtrees = countRelevantTrees(tgtrees, ntrees);
        System.out.println("Tree3DList (setListData): n relevant trees for 3d rendering: " + n3dtrees + " n trees in treegross stand: " + ntrees);
        trees = new Tree3D[n3dtrees];
        int indexcounter = 0;
        for (int i = 0; i < ntrees; i++) {
            if (!outBefore5(tgtrees[i], standyear)) {
                trees[indexcounter] = new Tree3D(tgtrees[i], bltexture, speciescolor, textured, parent, base);
                trees[indexcounter].setShowTreeStatus(sstatus);
                if (!showSpecies(tgtrees[i].code)) {
                    trees[indexcounter].setVisible(false);
                }
                if (!showdead && !trees[indexcounter].userdata.living && !trees[indexcounter].userdata.standing) {
                    trees[indexcounter].setVisible(false);
                }
                indexcounter++;
            }
        }
    }

    /* count living trees and trees death occurred less then 5 years ago*/
    private int countRelevantTrees(Tree[] tgtrees, int nalltrees) {
        int result = 0;
        for (int i = 0; i < nalltrees; i++) {
            if (!outBefore5(tgtrees[i], standyear)) {
                result++;
            }
        }
        return result;
    }

    /*check if tree is dead and is not removed before standyear - 5  or is standing and dead*/
    private boolean outBefore5(Tree tr, int standyear) {
        boolean result = false;
        if (tr.out != -1 && tr.out < (standyear - 5) && tr.outtype != OutType.STANDING) {
            result = true;
        }
        return result;
    }

    /*check if the species spec should be shown*/
    private boolean showSpecies(int spec) {
        boolean result = false;
        if (sts != null) {
            for (int i = 0; i < sts.length; i++) {
                if (spec == sts[i]) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public void setSpeciesToShow(int[] speciestoshow, boolean showdead) {
        sts = speciestoshow;
        if (trees != null) {
            for (Tree3D tree : trees()) {
                if (showSpecies(tree.userdata.spec)) {
                    if (!showdead && !tree.userdata.living && !tree.userdata.standing) {
                        tree.setVisible(false);
                    } else {
                        tree.setVisible(true);
                    }
                } else {
                    tree.setVisible(false);
                }
            }
        }
    }
    
    public Iterable<Tree3D> trees() {
        return Arrays.asList(trees);
    }
    
    public void setTextured(boolean textured) {
        if (trees != null) {
            for (Tree3D tree : trees) {
                tree.setTextured(textured);
            }
        }
    }

    public void setShowStatus(boolean visible) {
        if (trees != null) {
            for (Tree3D tree : trees) {
                tree.setShowTreeStatus(visible);
            }
        }
    }

    public void setSpeciesColor(boolean colored) {
        if (trees != null) {
            for (Tree3D tree : trees) {
                tree.setSpeciesColorOn(colored);
            }
        }
    }

    public void setShowDead(boolean showdead) {
        if (trees != null) {
            for (Tree3D tree : trees) {
                if (showSpecies(tree.userdata.spec)) {
                    if (!showdead && !tree.userdata.living && !tree.userdata.standing) {
                        tree.setVisible(false);
                    } else {
                        tree.setVisible(true);
                    }
                }
            }
        }
    }

    public int findTreeByUserData(UserData ud) {
        int result = -1;
        if (trees != null) {
            for (int i = 0; i < trees.length; i++) {
                if (ud == trees[i].userdata) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    public int findTreeByName(String treename) {
        int result = -1;
        if (trees != null) {
            for (int i = 0; i < trees.length; i++) {
                if (treename.equals(trees[i].userdata.name)) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    public void detachAllTrees() {
        if (trees == null) {
            return;
        }
        for (int i = 0; i < trees.length; i++) {
            trees[i].deleteTree();
            trees[i] = null;
        }
        trees = null;
    }
}
