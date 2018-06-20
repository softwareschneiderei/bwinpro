package treegross.base;

public class StandFactory {
    
    private StandFactory() {
        super();
    }

    public static Stand newStand(Stand st, String name, double size) {
        st.newStand();
        st.setSize(size);
        st.standname = name;
        st.year = 2014;
        double len = Math.sqrt(10000 * st.size);
        st.addcornerpoint("ECK1", 0.0, 0.0, 0.0);
        st.addcornerpoint("ECK2", 0.0, len, 0.0);
        st.addcornerpoint("ECK3", len, len, 0.0);
        st.addcornerpoint("ECK4", len, 0.0, 0.0);
        st.center.no = "polygon";
        st.center.x = len / 2.0;
        st.center.y = len / 2.0;
        st.center.z = 0.0;
        return st;
    }
}
