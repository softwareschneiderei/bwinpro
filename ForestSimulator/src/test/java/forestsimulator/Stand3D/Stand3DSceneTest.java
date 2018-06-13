package forestsimulator.Stand3D;

import javax.media.j3d.Group;
import javax.media.j3d.Texture2D;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import treegross.base.Species;
import treegross.base.Stand;
import treegross.base.Tree;

public class Stand3DSceneTest {
    
    @Test
    public void markersCycleThrough() {
        final Tree sampleTree = sampleTree();
        UserData d = new UserData(sampleTree, 0, 0, 0);
        d.marker = TreeMarker.NOT;
        Stand3DScene.updateMarker(new Stand(), d, mockTreeList(d));
        assertThat(d.marker, is(TreeMarker.THINNING));

        d.marker = TreeMarker.THINNING;
        Stand3DScene.updateMarker(new Stand(), d, mockTreeList(d));
        assertThat(d.marker, is(TreeMarker.CROP));

        d.marker = TreeMarker.CROP;
        Stand3DScene.updateMarker(new Stand(), d, mockTreeList(d));
        assertThat(d.marker, is(TreeMarker.TEMP_CROP));

        d.marker = TreeMarker.TEMP_CROP;
        Stand3DScene.updateMarker(new Stand(), d, mockTreeList(d));
        assertThat(d.marker, is(TreeMarker.HABITAT));

        d.marker = TreeMarker.HABITAT;
        Stand3DScene.updateMarker(new Stand(), d, mockTreeList(d));
        assertThat(d.marker, is(TreeMarker.NOT));
    }

    private static Tree sampleTree() {
        Tree result = new Tree();
        result.sp = new Species();
        return result;
    }

    private static Tree3DList mockTreeList(UserData userdata) {
        Tree3DList result = new Tree3DList();
        result.trees = new Tree3D[1];
        Tree3D t = new Tree3D(sampleTree(), new Texture2D[6], false, false, new Group(), new StandBase3D(null, 0, null, true, null) {
            @Override
            public double getHeightAtPoint(double x, double z, boolean invinitive) {
                return 0d;
            }
        });
        t.userdata = userdata;
        result.trees[0] = t;
        return result;
    }
}
