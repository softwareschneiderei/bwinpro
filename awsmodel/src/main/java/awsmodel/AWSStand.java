package awsmodel;

import java.util.Collection;

import awsmodel.AWSTree.AWSTreeSpecies;


/**
 * The AlbrechtWindStormModelStand interface defines the stand-level methods necessary to 
 * Albrecht et al.'s wind storm model. IMPORTANT: All methods should return -1 in case of 
 * null value.
 * @author M. Fortin and A. Albrecht - August 2010
 */
public interface AWSStand {
	
	/**
	 * This Enum variable defines all the stand-level variables required by this model.
	 * @author M. Fortin and A. Albrecht - August 2010
	 */
	public enum StandVariable {
		/**
		 * Dominant species
		 */
		DominantSpecies(AWSTreeSpecies.Beech),
		/**
		 * Age of the stand
		 */
		age(0),
		/**
		 * Year of the measurement
		 */
		year(0),
		/**
		 * Dominant diameter of the 100 thickest per ha (cm)
		 */
		d100(0d),
		/**
		 * Dominant height of the 100 thickest per ha (m)
		 */
		h100(0d),					
		/**
		 * Merchantable volume per ha with 6.5-cm minimum diameter (m3/ha)
		 */
		v(0d),
		/**
		 * Stand basal area (m2/ha)
		 */
		g(0d),
		/**
		 * A binary variable which is true when site is waterlogged ("hydromorphie" in French), i.e. presence of stagnant water signs in the first 70 cm of the soil profile
		 */
		stagnantMoisture(true),	
		/**
		 * The topex-to-distance index (calculated using GIS) distance 1000m, 8 directions, west weighted (E-1 SE-1 S-1 SW-2 W-3 NW-2 N-1 NE-1) reference Ruel et al. (1997)
		 */
		topex(0d),					
		/**
		 * The modeled maximum gust speed on Dec 26, 1999 (during Lothar storm) obtained by the wind model KAMM (m/sec) reference Heneka et al. (2006)
		 */
		wind99(0d),					
		/**
		 * A binary variable which is true if there is free calcium carbonate (CaCO3) in the upper 30 cm of the soil 
		 */
		carbonateInUpperSoil(true),
		/**
		 * The modeled maximum gust speed with an exceedance probability of 0.02/yr using model KAMM (m/sec) reference Heneka et al. (2006)
		 */
		wind50(0d);					
		
		private Object sample;
		
		private StandVariable(Object obj) {
			this.sample = obj;
		}
	
		public Object cast(Object obj) throws Exception {return sample.getClass().cast(obj);}
		
	}

	public Object getAWSStandVariable(StandVariable standVariable);
	
	/**
	 * An id that is different for each Monte Carlo iteration if the stochastic mode is enabled
	 * @return a String id
	 */
	public String getStandAndMonteCarloID();
	
	/**
	 * This methods makes the trees of the stand accessible.
	 * @return a Collection of AlbrechtWindStormModelTree objects.
	 */
	public Collection<AWSTree> getAlbrechtWindStormModelTrees();
	
	/**
	 * This method is optional. It serves to save the stand-level probabilities.
	 * @param probabilities the three stand-level probabilities
	 */
	public void registerProbabilities(double[] probabilities);
	
	/**
	 * This method serves to retrieve the probabilities associated to this stand. It is optional.
	 * @return the array of the probabilities
	 */
	public double[] getProbabilities();
	
}
