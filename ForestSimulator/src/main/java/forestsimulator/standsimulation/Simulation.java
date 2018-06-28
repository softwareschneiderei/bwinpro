package forestsimulator.standsimulation;

import forestsimulator.util.StopWatch;
import java.util.function.Consumer;
import java.util.logging.Logger;
import treegross.base.Stand;
import treegross.treatment.Treatment2;

public class Simulation {
    public static final Consumer<Stand> publishNothing = stand -> {};
    private static final Logger logger = Logger.getLogger(Simulation.class.getSimpleName());
    
    private final Stand st;
    private final Treatment2 treat = new Treatment2();
    private final boolean applyTreatment;
    private final boolean executeMortality;

    public Simulation(Stand st, boolean applyTreatment, boolean executeMortality) {
        super();
        this.st = st;
        this.applyTreatment = applyTreatment;
        this.executeMortality = executeMortality;
    }

    public void executeStep(int time, Consumer<Stand> publishStand) {
        if (applyTreatment) {
            StopWatch treatment = new StopWatch("Applying treatment").start();
            treat.executeManager2(st);
            treatment.printElapsedTime();
        }
        if (executeMortality) {
            StopWatch mortality = new StopWatch("Executing mortality").start();
            st.descspecies();
            st.executeMortality();
            mortality.printElapsedTime();
        }
        st.descspecies();
        StopWatch grow = new StopWatch("Growing").start();
        st.grow(time, st.ingrowthActive);
        grow.printElapsedTime();
        StopWatch save = new StopWatch("Publishing stand").start();
        publishStand.accept(st);
        save.printElapsedTime();
    }
}
