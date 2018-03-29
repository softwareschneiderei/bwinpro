package awsmodel;


/**
 * This interface provides the method required at the tree level to implement
 * Albrecht et al.'s wind storm model.
 * @author M. Fortin - August 2010
 */
@SuppressWarnings("rawtypes")
public interface AWSTree extends Comparable {
	
	/**
	 * The TreeSpecies enum defines the species considered in the model.
	 * @author M. Fortin - August 2010
	 */
	public enum AWSTreeSpecies {
		Spruce,
		SilverFir,
		DouglasFir,
		ScotsPine,
		EuropeanLarch,
		JapanLarch,
		Oak,
		Beech,
	}
	
	public enum TreeVariable {
		Species(AWSTreeSpecies.Beech),	// a AWSTreeSpecies object
		Height(0d) ,					// a double
		Dbh(0d),						// a double
		Number(0d)						// a double
		;
		
		private Object sample;
		
		private TreeVariable(Object obj) {
			this.sample = obj;
		}
		
		public Object cast(Object obj) throws Exception {return sample.getClass().cast(obj);}
	}

	public Object getAWSTreeVariable(Enum treeVariable);
	
	public void registerProbability(double probability);
	public double getProbability();
	
	
}
