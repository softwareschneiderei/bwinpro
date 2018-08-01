/*
 * Stand3DScene.java
 *
 * Created on 22. Februar 2006, 12:11
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package forestsimulator.Stand3D;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.Enumeration;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import treegross.base.Stand;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.geometry.Cone;
import forestsimulator.util.StopWatch;
import java.awt.image.BufferedImage;
import javax.swing.event.MouseInputAdapter;
import treegross.base.OutType;

/**
 *
 * @author jhansen
 *
 * attention: 3d computergraphics use a left-hand-coord.-system to convert the
 * right handed stand-coordinates into the 3d system all realworld x-coordinates
 * must be multiplicated with -1
 *
 * also the axises have annother order x=x; y=z; z=y
 */
public class Stand3DScene extends JPanel {

    private Stand st;
    private final SimpleUniverse universe;
    private Texture2D[] textures = null;
    private final Point3d atpoint = new Point3d(); // starting point the user looks at
    private Point3d frompoint = new Point3d(); // starting point of the user's position 
    private PickCanvas pickCanvas;
    private TransformGroup pickgroup;
    private TransformGroup forestergroup;
    private Shape3D mesh = null;
    private CollisionDetector cd = null;
    private Tree3DList treelist = null;
    private TransformGroup sceneTG;
    private int[] sts;             // the species codes of the species to show
    private StandBase3D base;
    private final FieldSquares meshconstructor;
    private KoordSystem3D coord;
    private LinearFog fog;
    private String selectedTree = "";
    //private ExponentialFog fogexp;
    public boolean speccolor = false;
    public boolean showdead = false;
    public boolean textured = false;
    public boolean showtreeinfo = true;
    public boolean showfog = false;
    public boolean showmesh = true;
    public boolean showstatus = false;
    private final Canvas3D canvas;
    private BranchGroup scene;

    /**
     * Creates a new instance of Stand3DScene _st Stand: a treegross stand ts
     * boolean: true shows the tree status sc boolean: true shows the trees in
     * defined species colors sd boolean: true shows all dead (died or
     * harvested) trees speciestoshow: array of integer the species code(s) of
     * the species to show if is null all species will be shown
     */
    public Stand3DScene(boolean sc, boolean sd, boolean tex, boolean status, boolean info, boolean sfog, boolean smesh, Texture2D[] alltextures) {
        super();
        speccolor = sc;
        showdead = sd;
        textured = tex;
        showstatus = status;
        showtreeinfo = info;
        showfog = sfog;
        showmesh = smesh;
        textures = copyTextures(alltextures);
        
        meshconstructor = new FieldSquares(5.0);
        initSwing();
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        add(canvas, BorderLayout.CENTER);
        universe = new SimpleUniverse(canvas);
        setupUniverse();
        // add mousepicking:
        canvas.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pick(e);
            }
        });
    }

    private void setupUniverse() {
        // add orbit behavior
        OrbitBehavior orbit
                = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL | OrbitBehavior.PROPORTIONAL_ZOOM);
        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
        orbit.setSchedulingBounds(bounds);
        universe.getViewingPlatform().setViewPlatformBehavior(orbit);
        // set the max viewing distance:
        universe.getViewer().getView().setBackClipDistance(2000); // range of view 2000 meters
        universe.getViewer().getView().setFrontClipDistance(0.01);
    }

    public void loadStand(Stand stand, int[] speciestoshow) {
        st = stand;
        setShowingSpecies(speciestoshow);
        setupUniverse();
        //canvas.getView().setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);
        BranchGroup newScene = createScene();
        if (scene != null) {
            universe.getLocale().replaceBranchGraph(scene, newScene);
        } else {
            universe.getLocale().addBranchGraph(newScene);
        }
        scene = newScene;
        // Set up the HUD
        //hud3d =new HUD3D(universe);   
        pickCanvas = new PickCanvas(canvas, scene);
        pickCanvas.setMode(PickCanvas.GEOMETRY);
        pickCanvas.setTolerance(0f);
        System.out.println("Stand3DScene: scene build.");
    }

    private Texture2D[] copyTextures(Texture2D[] source) {
        return source.clone();
    }

    private void initSwing() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public final BranchGroup createScene() {
        BranchGroup rootobj = new BranchGroup();
        rootobj.setCapability(BranchGroup.ALLOW_DETACH);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
        fog = new LinearFog(new Color3f(0.8f, 0.8f, 0.9f));
        fog.setFrontDistance(7.0);
        fog.setBackDistance(300.0);
        fog.setInfluencingBounds(bounds);
        fog.setCapability(Fog.ALLOW_INFLUENCING_BOUNDS_READ);
        fog.setCapability(Fog.ALLOW_INFLUENCING_BOUNDS_WRITE);

        /*fogexp= new ExponentialFog(new Color3f(0.8f, 0.8f, 0.9f));
        fogexp.setDensity(0.12f);
        fogexp.setInfluencingBounds(bounds);
        fogexp.setCapability(Fog.ALLOW_INFLUENCING_BOUNDS_READ);
        fogexp.setCapability(Fog.ALLOW_INFLUENCING_BOUNDS_WRITE);*/
        setFogEnable(showfog);

        sceneTG = new TransformGroup();
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        makeStandBase();
        coord = new KoordSystem3D(base);
        //add all trees to the list
        treelist = new Tree3DList(st.tr, st.ntrees, textures, speccolor, textured, showstatus, sceneTG, sts, showdead, base, st.year);
        sceneTG.addChild(coord.getAxis());
        //Directional light:
        Color3f lColor1 = new Color3f(0.7f, 0.7f, 0.7f);
        Vector3f lDir1 = new Vector3f(0.5f, -1.0f, 0.5f);
        DirectionalLight dlight = new DirectionalLight(true, lColor1, lDir1);
        dlight.setInfluencingBounds(bounds);
        //Ambient light:
        AmbientLight lightA = new AmbientLight(true, new Color3f(0.3f, 0.3f, 0.3f));
        lightA.setInfluencingBounds(bounds);
        lightA.setCapability(Light.ALLOW_INFLUENCING_BOUNDS_READ);
        sceneTG.addChild(lightA);
        sceneTG.addChild(dlight);

        sceneTG.setBoundsAutoCompute(true);
        System.out.println("stand bounds: " + sceneTG.getBounds().toString());
        // tree marker:
        makePickMarker();
        // forester:
        makeForester();

        makeMesh();
        rootobj.addChild(sceneTG);
        //starting point
        BoundingSphere b = (BoundingSphere) sceneTG.getBounds();
        double r = b.getRadius();
        b.getCenter(atpoint);//look at stand center
        frompoint = new Point3d(r * -1.2, r * 1.2, r * -1.2);//look from stand border*2 
        setViewAndPosition(frompoint, atpoint);
        //keybehavior
        WalkBehavior navigator
                = new WalkBehavior(universe.getViewingPlatform().getViewPlatformTransform(), frompoint, atpoint, forestergroup, cd, base);
        rootobj.addChild(navigator);
        // Set up the background
        Color3f bgColor = new Color3f(0.8f, 0.8f, 0.9f);
        Background bgNode = new Background(bgColor);
        bgNode.setApplicationBounds(bounds);
        rootobj.addChild(bgNode);
        rootobj.addChild(fog);
        //rootobj.addChild(fogexp);
        rootobj.compile();
        // compile must be called at least
        System.out.println("Stand3DScene: scene objects build.");
        return rootobj;
    }

    private void makeStandBase() {
        base = new StandBase3D(st.cpnt, st.ncpnt, textures, textured, sceneTG);
    }

    private void makePickMarker() {
        pickgroup = new TransformGroup();
        pickgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        pickgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        Appearance a = new Appearance();
        Cone pickmarker = new Cone(0.4f, -1.0f, Cone.GENERATE_NORMALS | Cone.GEOMETRY_NOT_SHARED, a);
        pickmarker.getShape(Cone.CAP).getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        pickmarker.getShape(Cone.BODY).getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        pickgroup.addChild(pickmarker);
        sceneTG.addChild(pickgroup);
    }

    private void makeMesh() {
        mesh = new Shape3D();
        //meshconstructor.makeMesh(st.cpnt, st.ncpnt, mesh);
        meshconstructor.makeMesh(base, mesh);
        sceneTG.addChild(mesh);
        setMeshVisible(showmesh);
    }

    public void setMeshVisible(boolean visible) {
        showmesh = visible;
        if (mesh != null) {
            mesh.getAppearance().getRenderingAttributes().setVisible(visible);
        }
    }

    private void makeForester() {
        Appearance a = new Appearance();
        a.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(0.6f, 0.3f, 0.0f);
        a.setColoringAttributes(ca);
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(0, -10, 0));
        forestergroup = new TransformGroup();
        forestergroup.setTransform(trans);
        forestergroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        forestergroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        FrustrumNew forester = new FrustrumNew(0f, 0f, 0f, 0.5f, 0.1f, 2f, 6, a);
        forestergroup.addChild(forester.getShape());
        sceneTG.addChild(forestergroup);
        // Create a new Behavior object that will perform the collision
        // detection on the specified object, and add it into
        // the scene graph.
        cd = new CollisionDetector(forester.getShape());
        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
        cd.setSchedulingBounds(bounds);
        // Add the behavior to the scene graph
        sceneTG.addChild(cd);
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

    public Stand getStand() {
        return st;
    }

    public BufferedImage getScreenShot() {
        BufferedImage img = ImageUtilities3D.getScreenShot(universe);
        return img;
    }

    public final void setShowingSpecies(int[] spec) {
        if (spec != null) {
            sts = spec;
        } else {
            setShowAllSpecies();
        }
        if (treelist != null) {
            treelist.setSpeciesToShow(sts, showdead);
        }
    }

    private void setShowAllSpecies() {
        sts = new int[st.nspecies];
        for (int i = 0; i < st.nspecies; i++) {
            sts[i] = st.sp[i].code;
        }
    }

    public void setShowDeadTrees(boolean showd) {
        showdead = showd;
        if (treelist != null) {
            treelist.setShowDead(showd);
        }
    }

    public void setSpeciesColor(boolean speciescolor) {
        speccolor = speciescolor;
        if (treelist != null) {
            treelist.setSpeciesColor(speciescolor);
        }
        if (base != null) {
            base.setTexttured(false);
        }
    }

    public void setTextured(boolean tex) {
        textured = tex;
        if (treelist != null) {
            treelist.setTextured(textured);
        }
        if (base != null) {
            base.setTexttured(textured);
        }
    }

    public void setStatus(boolean visible) {
        showstatus = visible;
        if (treelist != null) {
            treelist.setShowStatus(visible);
        }
    }

    public void setFogEnable(boolean enable) {
        showfog = enable;
        if (fog != null) {
            if (enable) {
                fog.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0));
            } else {
                fog.setInfluencingBounds(null);
            }
        }
    }

    /* the list holding the tree geometry will be updated
     * (after growing or harvesting the stand)
     */
    public void setNewTreeDataAndRefresh(Stand stand) {
        if (stand != null) {
            st = stand;
        }
        if (treelist == null) {
            return;
        }
        StopWatch detach = new StopWatch("detach").start();
        treelist.detachAllTrees();
        detach.printElapsedTime();
        StopWatch setList = new StopWatch("set list").start();
        treelist.setListData(st.tr, st.ntrees, textures, speccolor, textured, showstatus, sceneTG, sts, showdead, base, st.year);
        setList.printElapsedTime();
    }

    public void setViewAndPosition(Point3d lookfrom, Point3d lookat) {
        TransformGroup tgview = universe.getViewingPlatform().getViewPlatformTransform();
        Transform3D translate = new Transform3D();
        Vector3d upvector = new Vector3d(0, 1, 0);
        translate.lookAt(lookfrom, lookat, upvector);
        translate.invert();
        tgview.setTransform(translate);
    }

    public void setViewAndPosition(Transform3D viewandpos) {
        universe.getViewingPlatform().getViewPlatformTransform().setTransform(viewandpos);
    }

    public void setViewAndPositionStart() {
        setViewAndPosition(frompoint, atpoint);
    }

    public void getLastViewAndPosition(Transform3D targettrans) {
        universe.getViewingPlatform().getViewPlatformTransform().getTransform(targettrans);
    }

    public void setPickFocus() {
        grabFocus();
    }

    public String getSelectedTree() {
        return selectedTree;
    }

    public void harvestTreeInStand(String treeno) {
        for (int i = 0; i < st.ntrees; i++) {
            if (st.tr[i].no.equals(treeno)) {
                st.tr[i].takeOut(st.year, OutType.THINNED);
                treelist.trees[treelist.findTreeByName(treeno)].harvestTree(base);
                i = st.ntrees; // sofortiger Abbruch
            }
        }
    }

    private void movePickMarker(double x, double y, double z) {
        Transform3D movepick = new Transform3D();
        movepick.setTranslation(new Vector3d(x, y, z));
        pickgroup.setTransform(movepick);
    }

    private void pick(MouseEvent e) {
        pickCanvas.setShapeLocation(e);
        PickResult result = pickCanvas.pickClosest();
        UserData tud = null;
        if (result != null) {
            if (result.getObject().getUserData() != null) {
                if (result.getObject().getUserData().getClass() == UserData.class) {
                    tud = (UserData) result.getObject().getUserData();
                }
            }
        }
        if (tud != null /*&& tud.standing*/) {
            movePickMarker(-tud.x, base.getHeightAtPoint(-tud.x, tud.y, false) + tud.h + 1.4, tud.y);
            //if(showtreeinfo) hud3d.setText2D("Baum: "+tud.name.trim()+" BHD: "+Math.round(tud.dbh)+" Höhe: "+Math.round(tud.h));
            selectedTree = tud.name;
            if (showtreeinfo && e.getButton() == MouseEvent.BUTTON1) {
                TransparentWindow treeinfowindow = new TransparentWindow(tud, this, e.getX(), e.getY());
            }
            if (e.getButton() == MouseEvent.BUTTON3 && tud.living) {
                updateMarker(st, tud, treelist);
            }
        } else {
            movePickMarker(0, 0, 0);
        }
    }

    // This is static for easier testing. If creation and testing of Stand3DScene is improved
    // this can become an instance method again...
    static void updateMarker(Stand st, UserData tud, Tree3DList treelist) {
        switch (tud.marker) {
            case NOT:
                tud.marker = TreeMarker.THINNING;
                treelist.trees[treelist.findTreeByUserData(tud)].updateMarker();
                break;
            case THINNING:
                tud.marker = TreeMarker.CROP;
                if (st.standname.contains("SIMWALD")) {
                    tud.marker = TreeMarker.NOT;
                }
                treelist.trees[treelist.findTreeByUserData(tud)].updateMarker();
                setTreeStatusInStand(st, tud);
                break;
            case CROP:
                tud.marker = TreeMarker.TEMP_CROP;
                treelist.trees[treelist.findTreeByUserData(tud)].updateMarker();
                setTreeStatusInStand(st, tud);
                break;
            case TEMP_CROP:
                tud.marker = TreeMarker.HABITAT;
                treelist.trees[treelist.findTreeByUserData(tud)].updateMarker();
                setTreeStatusInStand(st, tud);
                break;
            default:
                tud.marker = TreeMarker.NOT;
                treelist.trees[treelist.findTreeByUserData(tud)].updateMarker();
                setTreeStatusInStand(st, tud);
                break;
        }
    }

    private static void setTreeStatusInStand(Stand st, UserData ud) {
        boolean crop = false;
        boolean tempcrop = false;
        boolean habitat = false;
        for (int i = 0; i < st.ntrees; i++) {
            String name = ud.name;
            if (st.tr[i].no.compareTo(name) == 0) {
                if (ud.marker == TreeMarker.CROP) {
                    crop = true;
                }
                if (ud.marker == TreeMarker.TEMP_CROP) {
                    tempcrop = true;
                }
                if (ud.marker == TreeMarker.HABITAT) {
                    habitat = true;
                }
                st.tr[i].crop = crop;
                st.tr[i].tempcrop = tempcrop;
                st.tr[i].habitat = habitat;
                st.notifyStandChanged("treestatus changed");
                i = st.ntrees; // sofortiger abbruch
            }
        }
    }

    private void harvestTreeInStand(BranchGroup tree) {
        for (int i = 0; i < st.ntrees; i++) {
            UserData ud = (UserData) tree.getUserData();
            String name = ud.name;
            if (st.tr[i].no.compareTo(name) == 0) {
                st.tr[i].out = st.year;
                st.tr[i].outtype = OutType.THINNED;
                treelist.trees[treelist.findTreeByUserData(ud)].harvestTree(base);
                i = st.ntrees; // sofortiger Abbruch
            }
        }
    }

    /**
     * removes all marked trees from the stand
     * and let them fall in the view
     */
    public void harvestAllMarkedTrees() {
        movePickMarker(0, 0, 0);
        Enumeration e = sceneTG.getAllChildren();
        UserData ud;
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o.getClass() == BranchGroup.class) {
                BranchGroup dummy = (BranchGroup) o;
                Object u = dummy.getUserData();
                if (u != null && u.getClass() == UserData.class) {
                    ud = (UserData) dummy.getUserData();
                } else {
                    ud = null;
                }
                if (ud != null) {
                    if ((ud.marker == TreeMarker.THINNING || ud.marker == TreeMarker.HARVESTING) && ud.type == 1) {
                        harvestTreeInStand(dummy);
                    }
                }
            }
        }
        st.notifyStandChanged("trees harvested");
    }
}
