package awsmodel;


public interface AWSTreatment {
	
	/**
	 * This Enum variable defines all the treatment-related variables required by this model.
	 * @author M. Fortin and A. Albrecht - August 2010
	 */
	public enum TreatmentVariable {
		/**
		 * The cumulative merchantable volume of all thinnings to date as a ratio of total volume production (current volume + all thinnings to date)
		 */
		cumulatedRemovals(0d),	
		/**
		 * 	The ratio between removed volume and volume prior to thinning (values between 0 to 1)
		 */
		relativeRemovedVolume(0d),
		/**
		 * The ratio between mean quadratic diameter (mqd) of the removed trees and mqd of the stand prior to thinning 
		 */
		thinningQuotient(0d),									
		/**
		 * The ratio between removed volume and volume prior to the last thinning (values between 0 to 1), which is on average 5 years (3-7 yrs) before the current date
		 */
		relativeRemovedVolumeOfPreviousIntervention(0d),
		/**
		 * The number of years since the last thinning
		 */
		nbYrsSincePreviousIntervention((int) 0),						
		/**
		 * The average ratio between removed volume and volume prior to thinning within the past 10 years.
		 */
		relativeRemovedVolumeInPast10Yrs(0d),					
		/**
		 * The average ratio between mean quadratic diameter (mqd) of the removed trees and mqd of the stand prior to thinning within the past 10 years
		 */
		thinningQuotientOfPast10Yrs(0d)							
		;
		
		private Object sample;
		
		private TreatmentVariable(Object obj) {
			this.sample = obj;
		}
		
		public Object cast(Object obj) throws Exception {return sample.getClass().cast(obj);}
	}
	

	public Object getAWSTreatmentVariable(TreatmentVariable treatmentVariable);
	
}
