package awsmodel;

import awsmodel.AWSTree;

/**
 * Simple implementation of AWSTree for testing.
 * @author M. Fortin - August 2010
 */
public abstract class AWSTreeImpl implements AWSTree {

	protected static enum TestTreeVariable {RelativeDbh, RelativeHDRatio, RegOffset}
	
	private AWSTreeSpecies species;
	private double relativeDbh;
	private double relativeHDRatio;
	private double regOffset;
	
	protected AWSTreeImpl (AWSTreeSpecies species,
			double relativeDbh,
			double relativeHDRatio,
			double regOffset) {
		this.species = species;
		this.relativeDbh = relativeDbh;
		this.relativeHDRatio = relativeHDRatio;
		this.regOffset = regOffset;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAWSTreeVariable(Enum variable) {
		if (variable instanceof AWSTree.TreeVariable) {
			AWSTree.TreeVariable treeVariable = (AWSTree.TreeVariable) variable;
			switch (treeVariable) {
			case Species:
				return species;
			case Dbh:
				return -1d;
			case Height:
				return -1d;
			case Number:
				return 1d;
			default:
				return null;
			}
		} else if (variable instanceof AWSTreeImpl.TestTreeVariable) {
			AWSTreeImpl.TestTreeVariable testTreeVariable = (AWSTreeImpl.TestTreeVariable) variable;
			switch (testTreeVariable) {
			case RelativeDbh:
				return relativeDbh;
			case RelativeHDRatio:
				return relativeHDRatio;
			case RegOffset:
				return regOffset;
			default:
				return null;
			} 
		} else {
			return null;
		}
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

//	@Override
//	public void registerProbability(double probability) {}
//
//	@Override
//	public double getProbability() {return -1;}
	
}
