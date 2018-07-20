package treegross.dynamic.siteindex;

import java.time.Year;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nfunk.jep.JEP;
import treegross.base.TGFunction;

public class DynamicSiteIndexCalculator {
    private static final Logger logger = Logger.getLogger(DynamicSiteIndexCalculator.class.getName());

    private final JEP evaluator;
    private final TGFunction model;

    public DynamicSiteIndexCalculator(TGFunction model) {
        super();
        this.model = model;
        evaluator = new JEP();
        evaluator.addStandardFunctions();
        evaluator.addVariable("prevSI", 0);
        evaluator.addVariable("env.tMean", 0);
        evaluator.addVariable("env.PSum", 0);
        evaluator.addVariable("env.NOTotal", 0);
        evaluator.addVariable("env.AI", 0);
        evaluator.parseExpression(modelFunctionText(model));
    }

    public double computeSiteIndex(Year year, double previousSiteIndex, EnvironmentVariables environment) {
        logger.log(Level.INFO, "Previous site index: {0}", previousSiteIndex);
        evaluator.setVarValue("prevSI", previousSiteIndex);
        logger.log(Level.INFO, "Mean teamperature: {0}", environment.growingSeasonMeanTemperatureOf(year));
        evaluator.setVarValue("env.tMean", environment.growingSeasonMeanTemperatureOf(year));
        logger.log(Level.INFO, "Mean precipitation sum: {0}", environment.growingSeasonPrecipitationSumOf(year));
        evaluator.setVarValue("env.PSum", environment.growingSeasonPrecipitationSumOf(year));
        logger.log(Level.INFO, "Annual nitrogen deposition: {0}", environment.nitrogenDepositionOf(year).value);
        evaluator.setVarValue("env.NOTotal", environment.nitrogenDepositionOf(year).value);
        logger.log(Level.INFO, "Aridity index: {0}", environment.aridityIndexOf(year));
        evaluator.setVarValue("env.AI", environment.aridityIndexOf(year));
        return evaluator.getValue();
    }

    private static String modelFunctionText(TGFunction model) {
        if (model.undefined()) {
            return "prevSI";
        }
        return model.getFunctionText();
    }

    public String functionText() {
        return model.getFunctionText();
    }
}
