package awsmodel;

import java.util.ArrayList;
import java.util.Collection;

import awsmodel.AWSStand;
import awsmodel.AWSTreatment;
import awsmodel.AWSTree;
import awsmodel.AWSTree.AWSTreeSpecies;

/**
 * This class is an implementation of the AWSStand and AWSTreatment. Use only for testing.
 * @author Mathieu Fortin - February 2011
 */
public abstract class AWSStandImpl implements AWSStand, AWSTreatment {

	private AWSTreeSpecies dominantSpecies;
	private double d100;
	private double h100;
	private int age;
	private double v;
	private double g;
	private boolean stagnantMoisture;
	private double topex;
	private double wind50;
	private double wind99;
	private boolean carbonateInUpperSoil;
	private int year;
	
	private double cumulatedRemovals;
	private double relativeRemovedVolume;
	private double thinningQuotient;
	private double relativeRemovedVolumeOfPreviousIntervention;
	private int nbYrsSincePreviousIntervention;
	private double relativeRemovedVolumeInPast10Yrs;
	private double thinningQuotientOfPast10Yrs;
	
	private Collection<AWSTree> trees;

	private String id;
	
	protected AWSStandImpl(String id,
			AWSTreeSpecies dominantSpecies,
			double d100,
			double h100,
			int age,
			double v,
			double g,
			boolean stagnantMoisture,
			double topex,
			double wind50,
			double wind99,
			boolean carbonateInUpperSoil,
			int year,
			double cumulatedRemovals,
			double relativeRemovedVolume,
			double thinningQuotient,
			double relativeRemovedVolumeOfPreviousIntervention,
			int nbYrsSincePreviousIntervention,
			double relativeRemovedVolumeInPast10Yrs,
			double thinningQuotientOfPast10Yrs,
			double[] predictedProbabilities) {
		this.id = id;
		this.dominantSpecies = dominantSpecies ;
		this.d100 = d100;
		this.h100 = h100;
		this.age = age;
		this.v = v;
		this.g = g;
		this.stagnantMoisture = stagnantMoisture;
		this.topex = topex;
		this.wind50 = wind50;
		this.wind99 = wind99;
		this.carbonateInUpperSoil = carbonateInUpperSoil;
		this.year = year;
		this.cumulatedRemovals = cumulatedRemovals;
		this.relativeRemovedVolume = relativeRemovedVolume;
		this.thinningQuotient = thinningQuotient;
		this.relativeRemovedVolumeOfPreviousIntervention = relativeRemovedVolumeOfPreviousIntervention;
		this.nbYrsSincePreviousIntervention = nbYrsSincePreviousIntervention;
		this.relativeRemovedVolumeInPast10Yrs = relativeRemovedVolumeInPast10Yrs;
		this.thinningQuotientOfPast10Yrs = thinningQuotientOfPast10Yrs;
		
		init();
	}
	
	private void init() {
		trees = new ArrayList<AWSTree>();
	}
	
	protected void addTree(AWSTreeImpl tree) {
		trees.add(tree);
	}
		
	@Override
	public Object getAWSStandVariable(StandVariable variable) {
		switch (variable) {
		case age:
			return age;
		case carbonateInUpperSoil:
			return carbonateInUpperSoil;
		case d100:
			return d100;
		case DominantSpecies:
			return dominantSpecies;
		case g:
			return g;
		case h100:
			return h100;
		case stagnantMoisture:
			return stagnantMoisture;
		case topex:
			return topex;
		case v:
			return v;
		case wind50:
			return wind50;
		case wind99:
			return wind99;
		case year:
			return year;
		default:
			return null;
		}
	}

	@Override
	public Collection<AWSTree> getAlbrechtWindStormModelTrees() {return trees;}

	@Override
	public Object getAWSTreatmentVariable(TreatmentVariable variable) {
		switch (variable) {
		case cumulatedRemovals:
			return cumulatedRemovals;
		case nbYrsSincePreviousIntervention:
			return nbYrsSincePreviousIntervention;
		case relativeRemovedVolume:
			return relativeRemovedVolume;
		case relativeRemovedVolumeInPast10Yrs:
			return relativeRemovedVolumeInPast10Yrs;
		case relativeRemovedVolumeOfPreviousIntervention:
			return relativeRemovedVolumeOfPreviousIntervention;
		case thinningQuotient:
			return thinningQuotient;
		case thinningQuotientOfPast10Yrs:
			return thinningQuotientOfPast10Yrs;
		default:
			return null;
		}
	}

	protected AWSTreeSpecies getDominantSpecies() {return dominantSpecies;}


	@Override
	public String getStandAndMonteCarloID() {return id;}

//	@Override
//	public void registerProbabilities(double[] probabilities) {}
//
//	@Override
//	public double[] getProbabilities() {return null;}

}
