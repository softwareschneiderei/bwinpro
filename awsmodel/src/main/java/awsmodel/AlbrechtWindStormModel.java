package awsmodel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import awsmodel.AWSStand.StandVariable;
import awsmodel.AWSTreatment.TreatmentVariable;
import awsmodel.AWSTree.AWSTreeSpecies;
import awsmodel.AWSTree.TreeVariable;
import awsmodel.CommonUtility.AWSTreeComparator;


/**
 * This class implements the equations of the different submodels that are part of Albrecht et al.'s wind storm model. This model
 * predict an average probability of wind-induced damage without knowledge or assumption of wind storm occurrence. The wind storm
 * occurrence is implicitly derived from the observed occurrence in the Baden-Wuerttemberg over the 1950-2005 period.
 * IMPORTANT : This model applies to 0.25-ha plot, with regular structure and homogeneous composition.
 * @see <a href=http://www.springerlink.com/content/4028611133q67450/fulltext.pdf> 
 * Albrecht, A., M. Hanewinkel, J. Bauhus, and U. Kohnle. 2010. How does silviculture affect storm damage in forests of south-western Germany? Results
 * from empirical modeling based in long-term observations. European Journal of Forest Research.
 * </a> 
 * @author M. Fortin and A. Albrecht - August 2010
 */
public class AlbrechtWindStormModel extends AWSModelCore implements ItemListener {

	private AWSTree[] treesArray;
	private double numberOfTrees;
	
	private double[] predictionsAtStandLevel;
	private double predictionAtTreeLevel;
	
	private transient AlbrechtWindStormModelUI guiInterface;
	
	
	/**
	 * General constructor.
	 */
	public AlbrechtWindStormModel() {
		super();
	}

	/**
	 * This method makes it possible to enable the stochastic mode. BY DEFAULT, 
	 * THE STOCHASTIC MODE IS DISABLED.
	 * @param enabled
	 */
	public void setStochasticModeEnabled(boolean enabled) {
		isStochasticModeEnabled = enabled;
	}

	public boolean isStochasticModeEnabled() {return isStochasticModeEnabled;}
	
	/**
	 * This method returns the occurrence of observing stand damage. 
	 * @return a Boolean (true = damaged, false = no damage)
	 */
	@SuppressWarnings("rawtypes")
	private Boolean getProbabilityOfStandDamage() {
		try {
			double pred = predictionsAtStandLevel[0];
			AWSTreeSpecies species = (AWSTreeSpecies) getStand().getAWSStandVariable(StandVariable.DominantSpecies);
			double deterministicCutPoint = (Double) ((Map) getCutPoint(SubModelID.firstStep)).get(species);
			return getOutcome(pred, deterministicCutPoint);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	private boolean getOutcome(double pred, double deterministicCutPoint) {
		double cutPoint;
		
		if (isStochasticModeEnabled()) {
			cutPoint = RANDOM_GENERATOR.nextDouble();
		} else {
			cutPoint = deterministicCutPoint;
		}
		
		return cutPoint < pred;
	}
	
	/**
	 * This method returns the occurrence of total damage (>75% of basal area damaged) given that stand damage occurred. 
	 * @return a Boolean (true = total damaged, false = not totally damage)
	 */
	@SuppressWarnings("rawtypes")
	private Boolean getProbabilityOfTotalStandDamage() {
		try {
			double pred = predictionsAtStandLevel[1];
			AWSTreeSpecies species = (AWSTreeSpecies) getStand().getAWSStandVariable(StandVariable.DominantSpecies);
			if (((Map) getCutPoint(SubModelID.secondStep)).get(species) != null) {
				double deterministicCutPoint = (Double) ((Map) getCutPoint(SubModelID.secondStep)).get(species);
				return getOutcome(pred, deterministicCutPoint);
			} else {				// at this point it means we are dealing with oak, pine or larch stands
				return false;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * This method returns the predicted proportion of stand damage (<75% of basal area damaged) given that the stand is not totally damaged. 
	 * @return the proportion (double)
	 */
	private double getProportionOfStandDamageIfNotTotallyDamaged() {
		try {
			return predictionsAtStandLevel[2];
		} catch (Exception e) {
			return -1d;
		}
	}
	
	/**
	 * This method generates three predictions : 1- the probability of stand damage, 
	 * 2- the probability of total damage (>75% of basal area damaged) conditional on the occurrence of stand damaged, and
	 * 3- the proportion of stand damage (<75% of basal area damaged) given that the stand is not totally damaged.
	 * @param stand a AWSStand object
	 * @param treatment a AWSTreatment object
	 * @return an array of three doubles corresponding to the above mentioned probabilities 
	 * @throws Exception
	 */
	public double[] getPredictionForThisStand(AWSStand stand, AWSTreatment treatment) throws Exception {
		boolean isNewTreatment;
		boolean isNewStand;
		
		try {
			if (getStand() == null || !getStand().equals(stand)) {
				setStand(stand);
				isNewStand = true;
				treesArray = null;				// the array is set to null
			} else {
				isNewStand = false;
			}
			
			if (getTreatment() == null || !getTreatment().equals(treatment)) {
				setTreatment(treatment);
				isNewTreatment = true; 
			} else {
				isNewTreatment = false;
			}
			
			if (isNewStand || isNewTreatment) {
				predictionsAtStandLevel = new double[3];
				
				AWSTreeSpecies species = (AWSTreeSpecies) getStandVariable(StandVariable.DominantSpecies);
				
				predictionsAtStandLevel[0] = computeProbabilityOfStandDamage(species);
				predictionsAtStandLevel[1] = computeProbabilityOfTotalDamage(species);
				predictionsAtStandLevel[2] = computeProportionOfDamageIfNotTotallyDamaged(species);
				if (predictionsAtStandLevel[2] > 0.75) {
					predictionsAtStandLevel[2] = 0.75;			// protection in case of overestimation
				}
			}
			
			stand.registerProbabilities(predictionsAtStandLevel);
			
			return predictionsAtStandLevel;
		} catch (Exception e) {
			System.out.println("Error while attempting to calculate prediction for stand " + stand.toString() + " : " + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
	}
	

	/**
	 * This method returns a collection of damaged trees. The probability of damage is also recorded in the tree
	 * through the registerProbability method.
	 * @param stand a AWSStand instance
	 * @param treatment a AWSTreatment instance
	 * @return the list of damaged trees
	 * @throws Exception
	 */
	public Collection<AWSTree> getDamagedTreesOfThisStand(AWSStand stand, AWSTreatment treatment) throws Exception {
		Collection<AWSTree> damagedTrees = new ArrayList<AWSTree>();
		
		getPredictionForThisStand(stand, treatment);		// make sure that the stand predictions are up to date
		
		if (getProbabilityOfStandDamage()) {
			Collection<AWSTree> awsTrees = getStand().getAlbrechtWindStormModelTrees();
			if (getProbabilityOfTotalStandDamage()) {
				for (AWSTree tree : awsTrees) {
					tree.registerProbability(1d);		// a probability of 1 is recorded in this kind of event
					damagedTrees.add(tree);
				}
			} else {
				for (AWSTree tree : awsTrees) {
					if (getResultForThisTree(tree)) {
						damagedTrees.add(tree);
					}
				}
			}
		}
		return damagedTrees;
	}
	
	
	/**
	 * This method provides a prediction of damage for an individual tree. The method assumes the stand probabilities
	 * have been calculated prior to this.
	 * @param tree a AWSTree object
	 * @return a boolean (true = the tree is damaged, otherwise is false)
	 * @throws Exception
	 */
	protected Boolean getResultForThisTree(AWSTree tree) throws Exception {
		try {
			boolean isNewTree;
			if (getTree() == null || !getTree().equals(tree)) {
				setTree(tree);
				isNewTree = true;
			} else  {
				isNewTree = false;
			}

			if (isNewTree) {
				predictionAtTreeLevel = getProbabilityOfDamageForThisTree(tree);
				tree.registerProbability(predictionAtTreeLevel);
			}
			
			double deterministicCutPoint = (Double) getCutPoint(SubModelID.fourthStep);
			return getOutcome(predictionAtTreeLevel, deterministicCutPoint);
			
		} catch (Exception e) {
			System.out.println("Error while attempting to calculate prediction for tree " + tree.toString() + " : " + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
	}

	
	/**
	 * This method computes the probability of observing damage in the stand for a particular tree species.
	 * @param species a TreeSpecies enum variable
	 * @return the probability (double)
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private double computeProbabilityOfStandDamage(AWSTreeSpecies species) throws Exception {
		
		double randomEffect = 0d;
		double[] beta = (double[]) ((Map) getParameters(SubModelID.firstStep)).get(species);
		double[] xVector = new double[beta.length];
		switch (species) {
		case Beech:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.d100);
			xVector[2] = (Double) getTreatmentVariable(TreatmentVariable.cumulatedRemovals);
			xVector[3] = (Double) getTreatmentVariable(TreatmentVariable.relativeRemovedVolume);
			xVector[4] = getReferenceTables().getRelativeH100D100Ratio(species, 
					(Double) getStandVariable(StandVariable.d100),
					(Double) getStandVariable(StandVariable.h100),
					(Integer) getStandVariable(StandVariable.year));
			break;
		case DouglasFir:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.h100);
			xVector[2] = (Double) getStandVariable(StandVariable.h100) * (Double) getStandVariable(StandVariable.d100);
			xVector[3] = (Double) getTreatmentVariable(TreatmentVariable.thinningQuotient);
			xVector[4] = (Double) getTreatmentVariable(TreatmentVariable.relativeRemovedVolumeOfPreviousIntervention);
			randomEffect = getRandomEffect(RandomEffectID.Step1DouglasFir);
			break;
		case Oak:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.v);
			randomEffect = getRandomEffect(RandomEffectID.Step1Oak);
			break;
		case Spruce:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.h100);
			xVector[2] = (Double) getTreatmentVariable(TreatmentVariable.thinningQuotient);
			xVector[3] = (Integer) getTreatmentVariable(TreatmentVariable.nbYrsSincePreviousIntervention);
			boolean stagnantMoisture = (Boolean) getStandVariable(StandVariable.stagnantMoisture);
			if (stagnantMoisture) {						// implementation according to the EFFECT option in the CLASS statement of the LOGISTIC procedure in SAS System
				xVector[4] = 1d;
			} else {
				xVector[4] = -1d;
			}
			xVector[5] = getReferenceTables().getStockDensity(species, 
					(Integer) getStandVariable(StandVariable.age), 
					(Double) getStandVariable(StandVariable.h100),
					(Double) getStandVariable(StandVariable.g));
			xVector[6] = getReferenceTables().getRelativeH100D100Ratio(species, 
					(Double) getStandVariable(StandVariable.d100),
					(Double) getStandVariable(StandVariable.h100),
					(Integer) getStandVariable(StandVariable.year));
			randomEffect = getRandomEffect(RandomEffectID.Step1Spruce);
			break;
		case ScotsPine:
		case EuropeanLarch:
		case JapanLarch:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getTreatmentVariable(TreatmentVariable.relativeRemovedVolumeInPast10Yrs);
			xVector[2] = (Integer) getTreatmentVariable(TreatmentVariable.nbYrsSincePreviousIntervention);
			randomEffect = getRandomEffect(RandomEffectID.Step1PineAndLarch);
			break;
		case SilverFir:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.h100);
			xVector[2] = getReferenceTables().getStockDensity(species, 
					(Integer) getStandVariable(StandVariable.age), 
					(Double) getStandVariable(StandVariable.h100),
					(Double) getStandVariable(StandVariable.g));
			xVector[3] = (Double) getStandVariable(StandVariable.topex);
			randomEffect = getRandomEffect(RandomEffectID.Step1SilverFir);
			break;
		}
		
		double xBeta = CommonUtility.multiplyTwoArraysOfDouble(xVector, beta) + randomEffect;
		double prob = Math.exp(xBeta) / (1d + Math.exp(xBeta));
		if (Double.isNaN(prob)) {
			throw new Exception("computeProbabilityOfStandDamage() yields NaN");
		}
		return prob;
	}
	
	/**
	 * This method computes the probability of observing more than 75% of damage in the stand for a particular tree species.
	 * @param species a TreeSpecies enum variable
	 * @return the probability (double)
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private double computeProbabilityOfTotalDamage(AWSTreeSpecies species) throws Exception {
		double randomEffect = 0d;
		double[] beta = (double[]) ((Map) getParameters(SubModelID.secondStep)).get(species);
		double[] xVector = null;
		if (beta !=null) {
			xVector = new double[beta.length];
		}
		switch (species) {
		case Beech:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.h100);
			xVector[2] = (Double) getStandVariable(StandVariable.topex);
			xVector[3] = (Double) getStandVariable(StandVariable.wind99);
			break;
		case DouglasFir:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.h100);
			xVector[2] = (Double) getTreatmentVariable(TreatmentVariable.cumulatedRemovals);
			xVector[3] = (Double) getTreatmentVariable(TreatmentVariable.relativeRemovedVolumeOfPreviousIntervention);
			boolean carbonateInUpperSoil = (Boolean) getStandVariable(StandVariable.carbonateInUpperSoil);
			if (carbonateInUpperSoil) {
				xVector[4] = 1d;
			} else  {
				// implementation according to the EFFECT option in the CLASS statement of the LOGISTIC procedure in SAS System
				xVector[4] = -1d;
			}
			randomEffect = getRandomEffect(RandomEffectID.Step2DouglasFir);
			break;
		case Spruce:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.h100);
			xVector[2] = getReferenceTables().getRelativeH100D100Ratio(species, 
					(Double) getStandVariable(StandVariable.d100),
					(Double) getStandVariable(StandVariable.h100),
					(Integer) getStandVariable(StandVariable.year));
			break;
		case SilverFir:
			xVector[0] = 1d;	// intercept
			xVector[1] = (Double) getStandVariable(StandVariable.h100);
			xVector[2] = (Double) getStandVariable(StandVariable.wind50);
			randomEffect = getRandomEffect(RandomEffectID.Step2SilverFir);
			break;
		case Oak:				// nothing to do for those two
		case ScotsPine:
		case EuropeanLarch:
		case JapanLarch:
			return 0d;
		}
		
		double xBeta = CommonUtility.multiplyTwoArraysOfDouble(xVector, beta) + randomEffect; 
		double prob = Math.exp(xBeta) / (1d + Math.exp(xBeta));
		if (Double.isNaN(prob)) {
			throw new Exception("computeProbabilityOfTotalDamage() yields NaN");
		}
		return prob;
	}
	

	/**
	 * This method computes the damage proportion from 0 to 75% for a particular tree species.
	 * @param species a TreeSpecies enum variable
	 * @return the probability (double)
	 * @throws Exception
	 */
	private double computeProportionOfDamageIfNotTotallyDamaged(AWSTreeSpecies species) throws Exception {
		double randomEffect = 0d;
		double[] beta = (double[]) (getParameters(SubModelID.thirdStep));
		
		double[] xVector = new double[beta.length];
		xVector[0] = 1d;  // intercept
		if (species == AWSTreeSpecies.DouglasFir) {
			xVector[1] = 1d;
		} else  {
			xVector[1] = 0d;
		}
		xVector[2] = (Double) getStandVariable(StandVariable.h100);
		xVector[3] = (Double) getTreatmentVariable(TreatmentVariable.thinningQuotientOfPast10Yrs);
		randomEffect = getRandomEffect(RandomEffectID.Step3VflLevel) + getRandomEffect(RandomEffectID.Step3FeldLevel);
		
		double xBeta = CommonUtility.multiplyTwoArraysOfDouble(xVector, beta) + randomEffect;
		double prob = Math.exp(xBeta) / (1d + Math.exp(xBeta));
		if (Double.isNaN(prob)) {
			throw new Exception("computeProportionOfDamageIfNotTotallyDamaged() yields NaN");
		}
		return prob;
	}
	
	
	/**
	 * This method computes the probability of observing damage for a particular tree.
	 * @param tree a AlbrechtWindStormModelTree object
	 * @return the probability (double)
	 * @throws Exception
	 */
	private double getProbabilityOfDamageForThisTree(AWSTree tree) throws Exception {
		
		AWSTreeSpecies species = (AWSTreeSpecies) getTreeVariable(TreeVariable.Species);
		
		double[] beta = (double[]) (getParameters(SubModelID.fourthStep));
		double[] xVector = new double[beta.length];
		switch (species) {
		case SilverFir:
			xVector[0] = 1d;
			xVector[1] = 0d;
			xVector[2] = 0d;
			xVector[3] = 0d;
			break;
		case Beech:
		case Oak:
			xVector[0] = 0d;
			xVector[1] = 1d;
			xVector[2] = 0d;
			xVector[3] = 0d;
			break;
		case ScotsPine:
		case EuropeanLarch:
		case JapanLarch:
			xVector[0] = 0d;
			xVector[1] = 0d;
			xVector[2] = 1d;
			xVector[3] = 0d;
			break;
		case Spruce:
		case DouglasFir:
			xVector[0] = 0d;
			xVector[1] = 0d;
			xVector[2] = 0d;
			xVector[3] = 1d;
			break;
		}
		if (getTreeVariable(AWSTreeImpl.TestTreeVariable.RelativeDbh) != null) {
			xVector[4] = (Double) getTreeVariable(AWSTreeImpl.TestTreeVariable.RelativeDbh);
		} else {
			xVector[4] = getRelativeTreeRankInDbh(tree);
		}
		
		if (getTreeVariable(AWSTreeImpl.TestTreeVariable.RelativeDbh) != null) {
			xVector[5] = (Double) getTreeVariable(AWSTreeImpl.TestTreeVariable.RelativeHDRatio);
		} else {
			double referenceH100D100 = getReferenceTables().getRelativeH100D100Ratio((AWSTreeSpecies) getStandVariable(StandVariable.DominantSpecies), 
					(Double) getStandVariable(StandVariable.d100),
					(Double) getStandVariable(StandVariable.h100),
					(Integer) getStandVariable(StandVariable.year));

			xVector[5] = (Double) getTreeVariable(TreeVariable.Height) / (Double) getTreeVariable(TreeVariable.Dbh) / referenceH100D100; // relative h/d - ratio  
		}

		double offset;
		if (getTreeVariable(AWSTreeImpl.TestTreeVariable.RegOffset) != null) {
			offset = (Double) getTreeVariable(AWSTreeImpl.TestTreeVariable.RegOffset);
		} else {
			offset = Math.log(getProportionOfStandDamageIfNotTotallyDamaged() / (1 - getProportionOfStandDamageIfNotTotallyDamaged()));
		}
		
		double xBeta = offset + CommonUtility.multiplyTwoArraysOfDouble(xVector, beta);
		double prob = Math.exp(xBeta) / (1d + Math.exp(xBeta));
		return prob;
	}

	
	/**
	 * This method computes the percentile rank of the tree with respect to the other trees of the stand. If several trees have the same dbh, it returns the maximum rank.
	 * @param tree a AlbrechtWindStormModelTree object
	 * @return the percentile rank (double)
	 */
	private double getRelativeTreeRankInDbh(AWSTree tree) {
		if (this.treesArray == null) {
			setOrderedTreeArrays();
		}
		
		double dbhOfThisTree = (Double) tree.getAWSTreeVariable(AWSTree.TreeVariable.Dbh);

		double numberOfTreesSmallerThanThisTree = 0;
		for (int i = 0; i < treesArray.length; i++) {
			if (dbhOfThisTree < (Double) treesArray[i].getAWSTreeVariable(TreeVariable.Dbh)) {
				break;
			} else  {
				numberOfTreesSmallerThanThisTree += (Double) treesArray[i].getAWSTreeVariable(TreeVariable.Number);
			}
		}
		
		return (double) numberOfTreesSmallerThanThisTree / numberOfTrees;
	}

	
	/**
	 * This method sets the array of AWSTree and the number of trees.
	 */
	@SuppressWarnings("unchecked")
	private void setOrderedTreeArrays() {
		Collection<AWSTree> trees = getStand().getAlbrechtWindStormModelTrees();
		treesArray = new AWSTree[trees.size()];
		
		int pointer = 0;
		numberOfTrees = 0;
		for (AWSTree oneOfTheseTrees : trees) {
			treesArray[pointer] = oneOfTheseTrees;
			numberOfTrees += (Double) oneOfTheseTrees.getAWSTreeVariable(TreeVariable.Number); 
			pointer++;
		}
		
		Arrays.sort(treesArray, new AWSTreeComparator());
	}

	
	/**
	 * This method returns the GUI interface. If the interface has not been created so far, the method creates it and 
	 * sends it to the Event Dispatch Thread. The dialog pops up. 
	 * @return a AlbrechtWindStormMakerUI instance
	 */
	public AlbrechtWindStormModelUI getGuiInterface() {
		if (guiInterface == null) {
			guiInterface = new AlbrechtWindStormModelUI(this);
		}
		return guiInterface;
	}

	
	public void showInterface() {
		if (!getGuiInterface().isVisible()) {
			guiInterface.setVisible(true);
		}
	}

	
	public static void main(String[] args) {
		AlbrechtWindStormModel model = new AlbrechtWindStormModel();
		model.showInterface();
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (guiInterface != null && evt.getSource().equals(guiInterface.stochasticOptionCheckBox)) {
			setStochasticModeEnabled(guiInterface.stochasticOptionCheckBox.isSelected());			
		}
	}
	
}
