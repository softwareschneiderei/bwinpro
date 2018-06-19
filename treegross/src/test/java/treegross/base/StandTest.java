package treegross.base;

import java.util.Arrays;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import org.junit.Test;

public class StandTest {
    
    public StandTest() {
    }
    
    /*
     * #Pinning
    */
    @Test
    public void sortingByDiameter() {
        Stand stand = new Stand();
        final Tree[] trees = treesWith(StandTest::treeWithDiameter, 2.1, 1.2, 1.4, 0.8, 3.2);
        setTrees(stand, trees);

        stand.sortbyd();

        assertThat(Arrays.stream(stand.tr, 0, stand.ntrees)).extracting(tree -> tree.d).containsExactly(3.2, 2.1, 1.4, 1.2, 0.8);
    }

    /*
     * #Pinning
    */
    @Test
    public void sortingByHeight() {
        Stand stand = new Stand();
        final Tree[] trees = treesWith(StandTest::treeWithHeight, 12.5, 8.8, 11.0, 14.1);
        setTrees(stand, trees);
        
        stand.sortbyh();
        
        assertThat(Arrays.stream(stand.tr, 0, stand.ntrees)).extracting(tree -> tree.h).containsExactly(14.1, 12.5, 11.0, 8.8);
    }

    /*
     * #Pinning
    */
    @Test
    public void sortingByY() {
        Stand stand = new Stand();
        final Tree[] trees = treesWith(StandTest::treeWithY, 0.0, 7.1, 5.9, 4.3, 9.2);
        setTrees(stand, trees);

        stand.sortbyy();

        assertThat(Arrays.stream(stand.tr, 0, stand.ntrees)).extracting(tree -> tree.y).containsExactly(9.2, 7.1, 5.9, 4.3, 0.0);
    }

    /*
     * #Pinning
     *
     * CHECK: Lexicographic sorting may be undesired
    */
    @Test
    public void sortingByNumber() {
        Stand stand = new Stand();
        final Tree[] trees = Stream.of("23", "99", "12", "15", "183").map(StandTest::treeWithNumber).collect(Collectors.toList()).toArray(new Tree[0]);
        setTrees(stand, trees);

        stand.sortbyNo();
        
        assertThat(Arrays.stream(stand.tr, 0, stand.ntrees)).extracting(tree -> tree.no).containsExactly("12", "15", "183", "23", "99");
    }
    
    /*
     * #Pinning
    */
    @Test
    public void clearResetsArrayCounts() {
        Stand stand = new Stand();
        stand.ntrees = 17;
        stand.ncpnt = 4;
        stand.nspecies = 2;
        
        stand.clear();
        assertThat(stand.ntrees).isEqualTo(0);
        assertThat(stand.ncpnt).isEqualTo(0);
        assertThat(stand.nspecies).isEqualTo(0);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void addingTreesToFullStandThrows() throws SpeciesNotDefinedException {
        Stand stand = new Stand();
        stand.ntrees = stand.maxStandTrees;
        
        assertThatThrownBy(() -> {
            stand.addtreefac(1, "", 10, 0, 5, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }).hasMessage("Maximum tree number reached! Tree not added! no name 0 d=5 species=1 height=7");
    }

    /*
     * #Pinning
    */
    @Test
    public void addTreeWorksAsExpected() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.clear();
        
        s.addtreeNFV(0, "number", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "remark");
        assertThat(s.trees()).size().isEqualTo(1);
    }

    private Tree[] treesWith(DoubleFunction<Tree> mapper, double... diameters) {
        return Arrays.stream(diameters).mapToObj(mapper).collect(Collectors.toList()).toArray(new Tree[0]);
    }
    
    private static Tree treeWithDiameter(double diameter) {
        Tree t = new Tree();
        t.d = diameter;
        return t;
    }

    private static Tree treeWithHeight(double height) {
        Tree t = new Tree();
        t.h = height;
        return t;
    }
    
    private static Tree treeWithY(double y) {
        Tree t = new Tree();
        t.y = y;
        return t;
    }
    
    private static Tree treeWithNumber(String number) {
        Tree t = new Tree();
        t.no = number;
        return t;
    }
    
    private void setTrees(Stand stand, final Tree[] trees) {
        System.arraycopy(trees, 0, stand.tr, 0, trees.length);
        stand.ntrees = trees.length;
    }
}
