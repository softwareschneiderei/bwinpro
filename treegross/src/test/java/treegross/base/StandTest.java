package treegross.base;

import java.util.Arrays;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StandTest {
    
    public StandTest() {
    }
    
    @Test
    public void sortingByDiameter() {
        Stand stand = new Stand();
        final Tree[] trees = treesWith(StandTest::treeWithDiameter, 2.1, 1.2, 1.4, 0.8, 3.2);
        setTrees(stand, trees);
        stand.sortbyd();
        assertEquals(
                Arrays.asList(3.2, 2.1, 1.4, 1.2, 0.8),
                Arrays.stream(stand.tr, 0, stand.ntrees).map(tree -> tree.d).collect(Collectors.toList()));
    }

    @Test
    public void sortingByHeight() {
        Stand stand = new Stand();
        final Tree[] trees = treesWith(StandTest::treeWithHeight, 12.5, 8.8, 11.0, 14.1);
        setTrees(stand, trees);
        stand.sortbyh();
        assertEquals(
                Arrays.asList(14.1, 12.5, 11.0, 8.8),
                Arrays.stream(stand.tr, 0, stand.ntrees).map(tree -> tree.h).collect(Collectors.toList()));
    }

    @Test
    public void sortingByY() {
        Stand stand = new Stand();
        final Tree[] trees = treesWith(StandTest::treeWithY, 0.0, 7.1, 5.9, 4.3, 9.2);
        setTrees(stand, trees);
        stand.sortbyy();
        assertEquals(
                Arrays.asList(9.2, 7.1, 5.9, 4.3, 0.0),
                Arrays.stream(stand.tr, 0, stand.ntrees).map(tree -> tree.y).collect(Collectors.toList()));
    }

    @Test
    public void sortingByNumber() {
        Stand stand = new Stand();
        final Tree[] trees = Stream.of("23", "99", "12", "15", "183").map(StandTest::treeWithNumber).collect(Collectors.toList()).toArray(new Tree[0]);
        setTrees(stand, trees);
        stand.sortbyNo();
        assertEquals(
                Arrays.asList("12", "15", "183", "23", "99"),
                Arrays.stream(stand.tr, 0, stand.ntrees).map(tree -> tree.no).collect(Collectors.toList()));
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
