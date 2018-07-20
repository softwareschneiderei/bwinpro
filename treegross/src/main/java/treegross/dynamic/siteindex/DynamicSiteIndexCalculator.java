package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nfunk.jep.JEP;
import treegross.base.Tree;

public class DynamicSiteIndexCalculator {
    private static final Logger logger = Logger.getLogger(DynamicSiteIndexCalculator.class.getName());

    private final DynamicSiteIndexModelParameters model;

    public DynamicSiteIndexCalculator(DynamicSiteIndexModelParameters model) {
        super();
        this.model = model;
    }
    
    public double computeSiteIndex(Year year, double previousSiteIndex, EnvironmentVariables environment) {
        // TODO: use JEP for evaluating the function
        logger.log(Level.INFO, "Previous site index: {0}", previousSiteIndex);
        logger.log(Level.INFO, "Mean teamperature: {0}", environment.growingSeasonMeanTemperatureOf(year));
        logger.log(Level.INFO, "Mean precipitation sum: {0}", environment.growingSeasonPrecipitationSumOf(year));
        logger.log(Level.INFO, "Annual nitrogen deposition: {0}", environment.nitrogenDepositionOf(year).value);
        logger.log(Level.INFO, "Aridity index: {0}", environment.aridityIndexOf(year));
        return model.parameter1 * Math.pow(previousSiteIndex, model.parameter2)
                * Math.exp(model.parameter3 * environment.growingSeasonMeanTemperatureOf(year)
                        + model.parameter4 * environment.growingSeasonPrecipitationSumOf(year)
                        + model.parameter5 * environment.aridityIndexOf(year)
                        + model.parameter6 * environment.nitrogenDepositionOf(year).value
                        + model.parameter7 * environment.growingSeasonPrecipitationSumOf(year)
                                * environment.nitrogenDepositionOf(year).value);
    }
    
    public double getValueForTree(Tree t, String func) {
        String function = func;
        JEP jep = new JEP();
        if (function.contains("t.d")) {
            jep.addVariable("t.d", t.d);
        }
        if (function.contains("t.h")) {
            jep.addVariable("t.h", t.h);
        }
        if (function.contains("t.age")) {
            jep.addVariable("t.age", t.age);
        }
        if (function.contains("t.cb")) {
            jep.addVariable("t.cb", t.cb);
        }
        if (function.contains("t.cw")) {
            jep.addVariable("t.cw", t.cw);
        }
        if (function.contains("t.c66c")) {
            jep.addVariable("t.c66c", t.c66c);
        }
        if (function.contains("t.c66")) {
            jep.addVariable("t.c66", t.c66);
        }
        if (function.contains("t.c66cxy")) {
            jep.addVariable("t.c66cxy", t.c66cxy);
        }
        if (function.contains("t.c66xy")) {
            jep.addVariable("t.c66xy", t.c66xy);
        }
        if (function.contains("t.out")) {
            jep.addVariable("t.out", t.out);
        }
        if (function.contains("t.si")) {
            jep.addVariable("t.si", t.si);
        }
        if (function.contains("t.ihpot")) {
            jep.addVariable("t.ihpot", t.ihpot);
        }
        if (function.contains("t.hinc")) {
            jep.addVariable("t.hinc", t.hinc);
        }
        if (function.contains("sp.hg")) {
            jep.addVariable("sp.hg", t.sp.hg);
        }
        if (function.contains("sp.dg")) {
            jep.addVariable("sp.dg", t.sp.dg);
        }
        if (function.contains("sp.h100")) {
            jep.addVariable("sp.h100", t.sp.h100);
        }
        if (function.contains("sp.year")) {
            jep.addVariable("sp.year", t.st.year);
        }
        if (function.contains("sp.gha")) {
            jep.addVariable("sp.gha", t.sp.gha);
        }
        if (function.contains("st.gha")) {
            jep.addVariable("st.gha", t.st.bha);
        }
        jep.addStandardFunctions();
        jep.parseExpression(function);
        return jep.getValue();
    }
}
