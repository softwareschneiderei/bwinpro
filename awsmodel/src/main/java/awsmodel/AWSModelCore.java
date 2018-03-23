package awsmodel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import awsmodel.AWSReferenceTables.AWSDefaultValuesSet;
import awsmodel.AWSStand.StandVariable;
import awsmodel.AWSTree.AWSTreeSpecies;


/**
 * This class implements the core of Albrecht et al.'s wind storm model.
 * @author M. Fortin and A. Albrecht - August 2010
 */
public class AWSModelCore {

	/**
	 * This Enum variable defines the different steps to implement in order to obtain predictions.
	 * @author M. Fortin and A. Albrecht - August 2010
	 */
	protected static enum SubModelID {
		/**
		 * The general stand-level occurrence of storm damage (Bernoulli distribution)
		 */
		firstStep,
		/**
		 * The occurrence of total stand damage (>= 75% of basal area affected) conditional on the first modeling step (Bernoulli distribution)
		 */
		secondStep,
		/**
		 * The partial storm damage conditional on the second modeling step, i.e. <= 75% of basal area affected (Binomial distribution)
		 */
		thirdStep,
		/**
		 * The probability of damage at tree level (Bernoulli distribution)
		 */
		fourthStep
	}
	
	protected static enum RandomEffectID {
		Step1Oak,
		Step1DouglasFir,
		Step1Spruce,
		Step1PineAndLarch,
		Step1SilverFir,
		Step2DouglasFir,
		Step2SilverFir,
		Step3VflLevel,
		Step3FeldLevel
	}
	
	
	protected static final Random RANDOM_GENERATOR = new Random();

	private Map<SubModelID, Object> parameters;
	private Map<SubModelID, Object> cutPoints;
	private double[] covarianceParameters;
	private Map<String, double[]> randomDeviatesRegistry;
	
	private AWSReferenceTables referenceTables;

	private AWSStand stand;
	private AWSTreatment treatment;
	private AWSTree tree;
	
	protected boolean isStochasticModeEnabled;
	
	/**
	 * General constructor for Albrecht et al.'s wind storm model
	 */
	protected AWSModelCore() {
		setParameters();
		setCovarianceParameters();
		setCutPoints();
		randomDeviatesRegistry = new HashMap<String, double[]>();
	}

	
	/**
	 * This method records the AWSStand and its random effect vector in the randomDeviatesRegistry member.
	 * @return the random effect if the stochastic mode is enabled or 0 otherwise 
	 */
	protected double getRandomEffect(RandomEffectID randomEffectID) {
		if (isStochasticModeEnabled) {
			if (!randomDeviatesRegistry.containsKey(getStand().getStandAndMonteCarloID())) {
				double[] randomEffectVector = new double[9];
				randomDeviatesRegistry.put(getStand().getStandAndMonteCarloID(), randomEffectVector);
				for (int i = 0; i < randomEffectVector.length; i++) {
					randomEffectVector[i] = RANDOM_GENERATOR.nextGaussian();
				}
			} 
			return randomDeviatesRegistry.get(getStand().getStandAndMonteCarloID())[randomEffectID.ordinal()]
			                                              * covarianceParameters[randomEffectID.ordinal()];
		} else  {
			return 0d;
		}
	}
	
	
	
	protected Object getParameters(SubModelID subModel) {return parameters.get(subModel);}
	
	protected void setStand(AWSStand stand) throws Exception {
		this.stand = stand;
	}
	
	protected AWSStand getStand() {return stand;}
	
	protected void setTreatment(AWSTreatment treatment) throws Exception {
		this.treatment = treatment;
	}
	
	protected AWSTreatment getTreatment() {return treatment;}

	protected void setTree(AWSTree tree) throws Exception {
		this.tree = tree;
	}

	protected AWSTree getTree() {return tree;}
	
	protected Object getStandVariable(AWSStand.StandVariable variable) throws Exception {
		Object obj = stand.getAWSStandVariable(variable);
		if (obj != null) {
			return obj;
		} else {
			return checkForDefaultValues(variable);
		}
	}
	
	protected Object getTreatmentVariable(AWSTreatment.TreatmentVariable variable) throws Exception {
		Object obj = treatment.getAWSTreatmentVariable(variable);
		if (obj != null) {
			return obj;
		} else {
			return checkForDefaultValues(variable);
		}

	}
	
	@SuppressWarnings("rawtypes")
	protected Object getTreeVariable(Enum variable) throws Exception {
		Object obj = tree.getAWSTreeVariable(variable);
		if (obj != null) {
			return obj;
		} else {
			return checkForDefaultValues(variable);
		}

	}
	
	/**
	 * This method checks if default values can be imputed to unavailable variables. In case default values are imputed, the
	 * bundle is modified and unwrapped again to make sure all the variables are in the appropriate format.
	 * @param enumVariable an Enum that represents the variable
	 * @return a default value
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Object checkForDefaultValues(Enum enumVariable) throws Exception {
		
		AWSTreeSpecies species;
		try {
			species = (AWSTreeSpecies) stand.getAWSStandVariable(StandVariable.DominantSpecies);
		} catch (Exception e) {
			System.out.println("Error while imputing default values : Dominant species field is not available!");
			throw e;
		}

		AWSDefaultValuesSet defaultValues = getReferenceTables().getDefaultValues(species);
		Field[] defaultFields = defaultValues.getClass().getDeclaredFields();
		
		Field fieldFound = null;
		for (Field field : defaultFields) {
			if (field.getName().toLowerCase().equals(enumVariable.name().toLowerCase())) {
				fieldFound = field;
			}
		}
		
		if (fieldFound != null) {
			return fieldFound.get(defaultValues);
		} else {
			return null;
		}
	}
	
	protected AWSReferenceTables getReferenceTables() {
		if (referenceTables == null) {
			referenceTables = new AWSReferenceTables();
			referenceTables.init();
		}
		return referenceTables;
	}
	
	/**
	 * This method sets all the parameters of the different submodels that compose the wind storm model.
	 */
	private void setParameters() {
		parameters = new HashMap<SubModelID, Object>();
		
		double[] parms;
		Map<AWSTreeSpecies, double[]> subModel;
		
		subModel = new HashMap<AWSTreeSpecies, double[]>();

		// step 1 (beech)
		parms = new double[5];
		parms[0] = -18.5311647549095;		// intercept
		parms[1] = 0.190366747853153;	// d100
		parms[2] = -5.36155621466199;	// cumulated removals (in % of TVP)
		parms[3] = 4.82341586740609;	// relative removed volumes
		parms[4] = 10.6852705131559;	// relative h100/d100
		subModel.put(AWSTreeSpecies.Beech, parms);
		
		// step 1 (douglas fir)
		parms = new double[5];
		parms[0] = -3.75463172235902;		// intercept
		parms[1] = 0.103276035719517;	// h100
		parms[2] = -0.00111027160751203;	// h100 * d100
		parms[3] = 1.4861239677467;	// thinning quotient
		parms[4] = 1.06771792983846;	// relative removed volume of previous intervention
		subModel.put(AWSTreeSpecies.DouglasFir, parms);
		
		// step 1 (oak)
		parms = new double[2];
		parms[0] = -5.24121382389349;		// intercept
		parms[1] = 0.00587080164978767;	// V
		subModel.put(AWSTreeSpecies.Oak, parms);

		// step 1 (spruce)
		parms = new double[7];
		parms[0] = -3.98419009252941;	// intercept
		parms[1] = 0.0462811201064987;	// h100
		parms[2] = 0.412614199776881;	// thinning quotient
		parms[3] = 0.158302609224583;	// number of yrs since previous intervention
		parms[4] = 0.542173517337992;	// stagnant moisture
		parms[5] = -1.86727575522102;	// stock density
		parms[6] = 2.15920885756004;	// relative h100/d100
		subModel.put(AWSTreeSpecies.Spruce, parms);

		// step 1 (pine and larch)
		parms = new double[3];
		parms[0] = -2.9363287349546;		// intercept
		parms[1] = 2.61504455622099;	// relative removed volume of 10 past yrs
		parms[2] = 0.141161227635848;	// number of yrs since previous intervention
		subModel.put(AWSTreeSpecies.ScotsPine, parms);
		subModel.put(AWSTreeSpecies.EuropeanLarch, parms);
		subModel.put(AWSTreeSpecies.JapanLarch, parms);

		// step 1 (silver fir)
		parms = new double[4];
		parms[0] = -4.50915635084148;		// intercept
		parms[1] = 0.19219469873144;	// h100
		parms[2] = -2.17517503284594;	// stock density
		parms[3] = -0.0248084192972455;	// topex
		subModel.put(AWSTreeSpecies.SilverFir, parms);
		
		parameters.put(SubModelID.firstStep, subModel);
		
		subModel = new HashMap<AWSTreeSpecies, double[]>();
		
		// step 2 (beech)
		parms = new double[4];
		parms[0] = 8.36587544255671;		// intercept
		parms[1] = 0.869405300969321;	// h100
		parms[2] = 0.0438788804761775;	// topex
		parms[3] = -1.17309568859764;	// wind99
		subModel.put(AWSTreeSpecies.Beech, parms);
		
		// step 2 (douglas fir)
		parms = new double[5];
		parms[0] = -4.73620094588818;	// intercept
		parms[1] = 0.186400670322014;	// h100
		parms[2] = -14.0076932455069;	// cumulated removal (in % of TVP)
		parms[3] = 15.3037015287503;	// relative removed volume of previous intervention
		parms[4] = 2.49396544167217;	// carbonate in upper soil
		subModel.put(AWSTreeSpecies.DouglasFir, parms);
		
		// step 2 (spruce)
		parms = new double[3];
		parms[0] = -21.1826934562554;	// intercept
		parms[1] = 0.339059023699707;	// h100
		parms[2] = 7.91664778803771;	// relative h100/d100
		subModel.put(AWSTreeSpecies.Spruce, parms);

		// step 2 (silver fir)
		parms = new double[3];
		parms[0] = -27.699036660044;		// intercept
		parms[1] = 1.37252280865644;	// h100
		parms[2] = -0.552392787209223;	// wind50
		subModel.put(AWSTreeSpecies.SilverFir, parms);

		parameters.put(SubModelID.secondStep, subModel);

		// step 3 - all species
		parms = new double[4];
		parms[0] = -4.96738246286239;		// intercept
		parms[1] = 0.405863827257984;	// douglas fir only
		parms[2] = 0.058196202522022;	// h100
		parms[3] = 0.419455716478645;	// thinning quotient of past 10 yrs
		
		parameters.put(SubModelID.thirdStep, parms);
		
		// step 4 - all species
		parms = new double[6];
		parms[0] = 0d;		// silver fir
		parms[1] = -0.310111262400432;	// beech and oak
		parms[2] = 0.146183630043177;	// pine and larch
		parms[3] = 0.203333118584875;	// spruce and douglas fir
		parms[4] = -0.458919014405052;	// relative tree rank (dbh)		
		parms[5] = -0.132123347596635;	// relative tree h/d-ratio		
		parameters.put(SubModelID.fourthStep, parms);
	}
	
	/**
	 * This method sets the standard deviation of the random effects in this model. WARNING: The input order
	 * must follow the RandomEffectID enum variable order.
	 */
	private void setCovarianceParameters() {
		covarianceParameters = new double[9];
		covarianceParameters[0] = Math.pow(2.15063825859707, 0.5); 		// step 1 - oak Feld(Vfl)
		covarianceParameters[1] = Math.pow(0.200799717998884, 0.5); 	// step 1 - Douglas fir Vfl
		covarianceParameters[2] = Math.pow(0.732771637660504, 0.5); 	// step 1 - Spruce Vfl
		covarianceParameters[3] = Math.pow(0.350862247525468, 0.5);		// step 1 - PineAndLarch Vfl
		covarianceParameters[4] = Math.pow(0.023389148739519, 0.5);		// step 1 - Silver fir Vfl
		covarianceParameters[5] = Math.pow(5.83436608852008, 0.5);		// step 2 - Douglas fir Vfl
		covarianceParameters[6] = Math.pow(4.42657702176871, 0.5);		// step 2 - Silver fir Vfl
		covarianceParameters[7] = Math.pow(0.499421777052644, 0.5);		// step 3 - Vfl (experiment)
		covarianceParameters[8] = Math.pow(0.278850885453606, 0.5);		// step 3 - Feld(Vfl) (plot nested in experiment)
	}
	
	protected Object getCutPoint(SubModelID subModel) {
		return cutPoints.get(subModel);
	}
	
	private void setCutPoints() {
		cutPoints = new HashMap<SubModelID, Object>();
		Map<AWSTreeSpecies, Double> oMap = new HashMap<AWSTreeSpecies, Double>();
		oMap.put(AWSTreeSpecies.Beech, 0.109);
		oMap.put(AWSTreeSpecies.Oak, 0.031);
		oMap.put(AWSTreeSpecies.DouglasFir, 0.262);
		oMap.put(AWSTreeSpecies.EuropeanLarch, 0.136);
		oMap.put(AWSTreeSpecies.JapanLarch, 0.136);
		oMap.put(AWSTreeSpecies.ScotsPine, 0.136);
		oMap.put(AWSTreeSpecies.SilverFir, 0.238);
		oMap.put(AWSTreeSpecies.Spruce, 0.213);

		cutPoints.put(SubModelID.firstStep, oMap);		// put the map of cutpoints for the first step

		oMap = new HashMap<AWSTreeSpecies, Double>();
		oMap.put(AWSTreeSpecies.Beech, 0.078);
		oMap.put(AWSTreeSpecies.DouglasFir, 0.018);
		oMap.put(AWSTreeSpecies.Spruce, 0.068);
		oMap.put(AWSTreeSpecies.SilverFir, 0.031);

		cutPoints.put(SubModelID.secondStep, oMap);
		
		cutPoints.put(SubModelID.fourthStep, 0.449);
	}
}
