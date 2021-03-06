package treegross.base;

import java.util.Arrays;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Test;
import static treegross.base.SiteIndex.si;

public class StandTest {
    
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
    public void addingTreefacToFullStandThrows() throws SpeciesNotDefinedException {
        Stand stand = new Stand();
        stand.ntrees = stand.maxStandTrees;
        
        assertThatThrownBy(() -> {
            stand.addtreefac(1, "", 10, 0, 5, 7, 0, 0, SiteIndex.undefined, 0, 0, 0, 0, 0, 0, 0);
        }).hasMessage("Maximum tree number reached! Tree not added! no name 0 d=5 species=1 height=7");
    }

    /*
     * #Pinning
    */
    @Test
    public void addingTreefacWorksAsExpectedAndCorrectsZ() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.year = 2014;
        s.clear();
        addSpeciesDef(s, 9);
        
        s.addtreefac(9, "n1", 12, 2, 12.3, 2.4, 3.5, 4.3, si(15.1), 2.1, 3.2, -1.9, 0, 1, 1, 1.5);
        assertThat(s.trees()).size().isEqualTo(1);
        assertThat(s.trees()).first()
                .extracting("code", "no", "age", "out", "d", "h", "cb", "cw", "x", "y", "z",
                        "outtype", "fac", "origin", "year", "layer", "ou", "remarks", "si", "group",
                        "crop", "tempcrop", "habitat")
                .containsExactly(9, "n1", 12, 2, 12.3, 2.4, 3.5, 4.3, 2.1, 3.2, 0.0,
                        OutType.STANDING, 1.5, 0, 2014, Layer.NONE, 0, null, si(15.1), -1,
                        false, true, true);
    }

    /*
     * #Pinning
    */
    @Test
    public void addingTreeCorrectsZ() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.year = 2013;
        s.clear();
        addSpeciesDef(s, 7);
        
        assertThat(s.addTree(7, "n1", 18, 1, 12.9, 6.4, 5.5, 4.3, si(15.1), 2.1, 3.2, -0.1, 0, 1, 0)).isTrue();
        assertThat(s.trees()).size().isEqualTo(1);
        assertThat(s.trees()).first()
                .extracting("code", "no", "age", "out", "d", "h", "cb", "cw", "x", "y", "z",
                        "outtype", "fac", "origin", "year", "layer", "ou", "remarks", "si", "group",
                        "crop", "tempcrop", "habitat")
                .containsExactly(7, "n1", 18, 1, 12.9, 6.4, 5.5, 4.3, 2.1, 3.2, 0.0,
                        OutType.STANDING, 1.0, 0, 2013, Layer.NONE, 0, null, si(15.1), -1,
                        false, true, false);
    }

    /*
     * #Pinning
    */
    @Test
    public void addingTreeFromPlantingAppliesOriginAndCorrectsZ() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.year = 2013;
        s.clear();
        addSpeciesDef(s, 7);
        
        assertThat(s.addTreeFromPlanting(7, "n1", 18, 1, 12.9, 6.4, 5.5, 4.3, si(15.1), 2.1, 3.2, -0.5, 0, 1, 0)).isTrue();
        assertThat(s.trees()).size().isEqualTo(1);
        assertThat(s.trees()).first()
                .extracting("code", "no", "age", "out", "d", "h", "cb", "cw", "x", "y", "z",
                        "outtype", "fac", "origin", "year", "layer", "ou", "remarks", "si", "group",
                        "crop", "tempcrop", "habitat")
                .containsExactly(7, "n1", 18, 1, 12.9, 6.4, 5.5, 4.3, 2.1, 3.2, 0.0,
                        OutType.STANDING, 1.0, 1, 2013, Layer.NONE, 0, null, si(15.1), -1,
                        false, true, false);
    }

    /*
     * #Pinning
    */
    @Test
    public void addingTreeFromNaturalIngrowthAppliesOriginAndLayerAndCorrectsZ() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.year = 2013;
        s.clear();
        addSpeciesDef(s, 7);
        
        s.addTreeFromNaturalIngrowth(7, "n10", 19, 1, 12.9, 6.4, 5.5, 4.3, si(15.1), 2.1, 3.2, -0.5, 0, 1, 0);
        assertThat(s.trees()).size().isEqualTo(1);
        assertThat(s.trees()).first()
                .extracting("code", "no", "age", "out", "d", "h", "cb", "cw", "x", "y", "z",
                        "outtype", "fac", "origin", "year", "layer", "ou", "remarks", "si", "group",
                        "crop", "tempcrop", "habitat")
                .containsExactly(7, "n10", 19, 1, 12.9, 6.4, 5.5, 4.3, 2.1, 3.2, 0.0,
                        OutType.STANDING, 1.0, 2, 2013, Layer.UNDERSTORY, 0, null, si(15.1), -1,
                        false, true, false);
    }

    /*
     * #Pinning
    */
    @Test
    public void addingTreeToFullStandReturnsFalse() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.ntrees = s.maxStandTrees;
        
        assertThat(s.addTree(7, "n1", 18, 1, 12.9, 6.4, 5.5, 4.3, si(15.1), 2.1, 3.2, -0.1, 0, 1, 0)).isFalse();
    }

    /*
     * #Pinning
    */
    @Test
    public void addingTreeFromDBWorksAndUsesGivenZ() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.year = 2015;
        s.clear();
        addSpeciesDef(s, 1);
        
        s.addtreeNFV(1, "number", 23, 2, 12.3, 2.4, 3.5, 4.3, si(15.1), 2.1, 3.2, -4.31, 1, 0, 1, 1.5, Layer.ZWISCHENSTAND, "remark");
        assertThat(s.trees()).size().isEqualTo(1);
        assertThat(s.trees()).first()
                .extracting("code", "no", "age", "out", "d", "h", "cb", "cw", "x", "y", "z",
                        "outtype", "fac", "origin", "year", "layer", "ou", "remarks", "si", "group",
                        "crop", "tempcrop", "habitat")
                .containsExactly(1, "number", 23, 2, 12.3, 2.4, 3.5, 4.3, 2.1, 3.2, -4.31,
                        OutType.STANDING, 1.5, 0, 2015, Layer.ZWISCHENSTAND, 2, "remark", si(15.1), -1,
                        true, false, true);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void addingTreeNFVWorksAndUsesGivenZ() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.year = 2015;
        s.clear();
        addSpeciesDef(s, 1);
        
        s.addtreeNFV(1, "number", 23, -1, 12.3, 2.4, 3.5, 4.3, si(15.1), 2.1, 3.2, -4.31, 1, 0, 1, 1.5, Layer.ZWISCHENSTAND, "remark");
        assertThat(s.trees()).size().isEqualTo(1);
        assertThat(s.trees()).first()
                .extracting("code", "no", "age", "out", "d", "h", "cb", "cw", "x", "y", "z",
                        "outtype", "fac", "origin", "year", "layer", "ou", "remarks", "si", "group",
                        "crop", "tempcrop", "habitat")
                .containsExactly(1, "number", 23, -1, 12.3, 2.4, 3.5, 4.3, 2.1, 3.2, -4.31,
                        OutType.STANDING, 1.5, 0, 2015, Layer.ZWISCHENSTAND, 2, "remark", si(15.1), -1,
                        true, false, true);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void addingXMLTreeWorksAndUsesGivenZ() throws SpeciesNotDefinedException {
        Stand s = new Stand();
        s.year = 2015;
        s.clear();
        addSpeciesDef(s, 1);
        
        s.addXMLTree(1, "number", 23, 9, OutType.FALLEN,
                2.4, 12.4, 3.5, 4.3,
                si(0.89), 1.7, 15.1, 2.1, -2.22, true, false, false,
                Layer.UPPERSTORY, 9.32, "remark");
        assertThat(s.trees()).size().isEqualTo(1);
        assertThat(s.trees()).first()
                .extracting("code", "no", "age", "out", "d", "h", "cb", "cw", "x", "y", "z",
                        "outtype", "fac", "origin", "year", "layer", "ou", "remarks", "si", "group",
                        "crop", "tempcrop", "habitat", "volumeDeadwood")
                .containsExactly(1, "number", 23, 9, 2.4, 12.4, 3.5, 4.3, 15.1, 2.1, -2.22,
                        OutType.FALLEN, 1.7, 0, 0, Layer.UPPERSTORY, 0, "remark", si(0.89), 0,
                        true, false, false, 9.32);
    }
    
    /*
     * #Pinning
    */
    @Test
    public void speciesForCodeFindsExisting() {
        Stand s = new Stand();
        s.addSpeciesToStand(new Species(109));
        s.addSpeciesToStand(new Species(110));
        
        assertThat(s.speciesFor(110)).isPresent().get().extracting("code").containsExactly(110);
    }

    /*
     * #Pinning
    */
    @Test
    public void speciesForCodeReturnsEmptyWhenNotFound() {
        Stand s = new Stand();
        s.addSpeciesToStand(new Species(209));
        s.addSpeciesToStand(new Species(210));
        
        assertThat(s.speciesFor(110)).isEmpty();
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
    
    private void setTrees(Stand stand, final Tree... trees) {
        System.arraycopy(trees, 0, stand.tr, 0, trees.length);
        stand.ntrees = trees.length;
    }

    private void addSpeciesDef(Stand s, int code) {
        s.setSDM(new SpeciesDefMap() {
            @Override
            public SpeciesDef insertSpecies(int code) {
                SpeciesDef spec= new SpeciesDef();
                spcdef.put(code, spec);
                return spec;
            }
        });
        s.getSDM().insertSpecies(code);
    }
}
