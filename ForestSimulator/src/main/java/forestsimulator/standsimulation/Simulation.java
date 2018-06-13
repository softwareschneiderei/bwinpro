package forestsimulator.standsimulation;

import forestsimulator.util.StopWatch;
import java.util.function.Consumer;
import treegross.base.Stand;
import treegross.treatment.Treatment2;

public class Simulation {

    private final Stand st;
    private final Treatment2 treat;

    public Simulation(Stand st, Treatment2 treat) {
        super();
        this.st = st;
        this.treat = treat;
    }

    public void executeStep(int time, Consumer<Stand> publishStand) {
        executeStep(true, time, publishStand);
    }
    
    public void executeStep(boolean applyTreatment, int time, Consumer<Stand> publishStand) {
        if (applyTreatment) {
            StopWatch treatment = new StopWatch("Applying treatment").start();
            treat.executeManager2(st);
            treatment.printElapsedTime();
        }
        st.descspecies();
        st.executeMortality();
        st.descspecies();
        StopWatch save = new StopWatch("Publishing stand").start();
        publishStand.accept(st);
        save.printElapsedTime();
        StopWatch grow = new StopWatch("Growing").start();
        st.grow(time, st.ingrowthActive);
        grow.printElapsedTime();
    }
}
