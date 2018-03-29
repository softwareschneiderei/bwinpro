package awsmodel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import awsmodel.AWSTree.AWSTreeSpecies;



/**
 * This class handles the data and the methods that provides the yield tables and the relative dominant height/dominant 
 * diameter ratio for the Albrecht Wind Storm Model.
 * @author M. Fortin and A. Albrecht - August 2010
 */
public class AWSReferenceTables {

	private static class YieldElement {
		private double hDom;
		private double basalArea;
		
		public YieldElement() {}

		public void setHDom(double hDom) {this.hDom = hDom;}
		@SuppressWarnings("unused")
		public double getHDom() {return hDom;}
		
		public void setBasalArea(double basalArea) {this.basalArea = basalArea;}
		public double getBasalArea() {return basalArea;}
	}
	
	/**
	 * This nested class provides a set of default values. NOTE : the fields must be public and their
	 * names should match those of the Enum variables StandVariable, TreeVariable, and Treatment variable.
	 * @author M. Fortin - August 2010
	 */
	protected static class AWSDefaultValuesSet {

		public double topex;
		public double wind99;
		public double wind50;
		public int year = 2005;
		
		public AWSDefaultValuesSet (double topex, double wind99, double wind50) {
			this.topex = topex;
			this.wind99 = wind99;
			this.wind50 = wind50;
		}
	}
	
	/**
	 * A Map object whose structure is the species, the age class, the dominant height class, and a collection
	 * of YieldElement objects. 
	 */
	private Map<AWSTreeSpecies, Map<Integer, Map<Integer, ArrayList<YieldElement>>>> yieldTables;
	private Map<AWSTreeSpecies, Map<Integer, Double>> relativeH100D100Ratios;
	private Map<AWSTreeSpecies, AWSDefaultValuesSet> defaultValuesMap;
	
	/**
	 * Empty constructor
	 */
	protected AWSReferenceTables() {}
	
	
	protected void init() {
		yieldTables = new HashMap<AWSTreeSpecies, Map<Integer, Map<Integer, ArrayList<YieldElement>>>>();
		relativeH100D100Ratios = new HashMap<AWSTreeSpecies, Map<Integer, Double>>();
		defaultValuesMap = new HashMap<AWSTreeSpecies, AWSDefaultValuesSet>();
		
		try {
			instantiateYieldTables();
			instantiateRelativeH100D100Ratios();
		} catch (Exception e) {
			System.out.println("Problem while reading the ratios tables " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * This method reads the yield tables
	 * @param filename the path of the file that contains the yield tables
	 * @throws Exception
	 */
	private void instantiateYieldTables() throws Exception {
		
		AWSTreeSpecies species;
		Map<Integer, Map<Integer, ArrayList<YieldElement>>> tempMap;
		Map<Integer, ArrayList<YieldElement>> tempMap2;
		YieldElement yieldElement;
		String token;
		int id;

		yieldTables.clear();

		InputStream is = ClassLoader.getSystemResourceAsStream("awsmodel/HITAB_FE.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String str = in.readLine();
		while (str != null) {
			// comment / blank line : goes to next line
			if (!str.startsWith("#") && str.trim().length() != 0) {
				species = null;
				tempMap = null;
				tempMap2 = null;
				yieldElement = null;
				StringTokenizer tkz = new StringTokenizer(str, ";");
				id = 0;
				while (tkz.hasMoreTokens()) {
					token = tkz.nextToken();
					if (id == 0) {
						int speciesId = Integer.parseInt(token);
						if (speciesId > AWSTree.AWSTreeSpecies.values().length) {
							break;
						} else {
							species = AWSTree.AWSTreeSpecies.values()[speciesId - 1];
							if (!yieldTables.containsKey(species)) {
								yieldTables.put(species, new HashMap<Integer, Map<Integer, ArrayList<YieldElement>>>());
							}
							tempMap = yieldTables.get(species);
						}
					}
					if (id == 2) {
						int age = (int) Double.parseDouble(token);
						if (tempMap != null && !tempMap.containsKey(age)) {
							tempMap.put(age, new HashMap<Integer, ArrayList<YieldElement>>());
						}
						tempMap2 = tempMap.get(age);
					}
					if (id == 5) {
						double hDom = Double.parseDouble(token);
						int heightClass = (int) Math.round(hDom / 5d) * 5;
						if (tempMap2 != null && !tempMap2.containsKey(heightClass)) {
							tempMap2.put(heightClass, new ArrayList<YieldElement>());
						}
						yieldElement = new YieldElement();
						tempMap2.get(heightClass).add(yieldElement);
						yieldElement.setHDom(Double.parseDouble(token));
					}
					if (id == 6) {
						if (yieldElement != null) {
							yieldElement.setBasalArea(Double.parseDouble(token));
						}
					}
					if (id > 6) {
						break;
					}
					id++;
				}
			}
			str = in.readLine();
		}

	}

	/**
	 * This private method fills the defaultValuesMap and the relativeH100D100Ratios map objects from 
	 * a txt file specified in filename.
	 * @param filename
	 * @throws Exception
	 */
	private void instantiateRelativeH100D100Ratios() throws Exception {

		AWSTreeSpecies species;
		Map<Integer, Double> tempMap;

		defaultValuesMap.clear();
		relativeH100D100Ratios.clear();

		InputStream is = ClassLoader.getSystemResourceAsStream("awsmodel/MeanVal.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String str = in.readLine();
		while (str != null) {
			// comment / blank line : goes to next line
			if (!str.startsWith("#") && str.trim().length() != 0) {
				species = null;
				tempMap = null;
				StringTokenizer tkz = new StringTokenizer(str, ";");
				
				String speciesName = tkz.nextToken().trim().toLowerCase();
				int yearClass = (int) Double.parseDouble(tkz.nextToken());
				double meanhd100 = Double.parseDouble(tkz.nextToken());
				double topex = Double.parseDouble(tkz.nextToken());
				double wind99 = Double.parseDouble(tkz.nextToken());
				double wind50 = Double.parseDouble(tkz.nextToken());
				
				if (speciesName.equals("beech")) {
					species = AWSTreeSpecies.Beech;
				} else if (speciesName.equals("douglasfir")) {
					species = AWSTreeSpecies.DouglasFir;
				} else if (speciesName.equals("oak")) {
					species = AWSTreeSpecies.Oak;
				} else if (speciesName.equals("spruce")) {
					species = AWSTreeSpecies.Spruce;
				} else if (speciesName.equals("pine+larch")) {
					species = AWSTreeSpecies.ScotsPine;
				} else if (speciesName.equals("silverfir")) {
					species = AWSTreeSpecies.SilverFir;
				} else throw new Exception();
				if (!relativeH100D100Ratios.containsKey(species)) {
					relativeH100D100Ratios.put(species, new HashMap<Integer, Double>());
				}
				tempMap = relativeH100D100Ratios.get(species);
				if (!tempMap.containsKey(yearClass)) {
					tempMap.put(yearClass, meanhd100);
				}
				if (!defaultValuesMap.containsKey(species)) {
					defaultValuesMap.put(species, new AWSDefaultValuesSet(topex, wind99, wind50));
				}
			}
			str = in.readLine();
		}

	}

	
	
	
	/**
	 * This method returns the ratio between the current basal area and the
	 * optimal basal area according to the yield tables. 
	 * @param species a AWSTreeSpecies object
	 * @param age an integer
	 * @param hDom the dominant height
	 * @param basalArea the current basal area (m2/ha)
	 * @return the optimal basal area (a double)
	 * @throws Exception
	 */
	protected double getStockDensity(AWSTreeSpecies species, int age, double hDom, double basalArea) throws Exception {
		try {
			Map<Integer, Map<Integer, ArrayList<YieldElement>>> yieldTablesForThisSpecies = yieldTables.get(species);
			int roundAge = (int) Math.round((double) age / 5d) * 5;
			int heightClass = (int) Math.round(hDom / 5d) * 5;

			if (!yieldTablesForThisSpecies.keySet().contains(roundAge)) {	// imputation if age class is not available
				Object[] availableAges = yieldTablesForThisSpecies.keySet().toArray();
				Arrays.sort(availableAges);
				int minAge = (Integer) availableAges[0];
				int maxAge = (Integer) availableAges[availableAges.length - 1];
				if (roundAge < minAge) {
					roundAge = minAge;
				} else if (roundAge > maxAge) {
					roundAge = maxAge;
				}
			}

			Map<Integer, ArrayList<YieldElement>> heightClasses = yieldTablesForThisSpecies.get(roundAge);

			if (!heightClasses.keySet().contains(heightClass)) {			// imputation if the height class is not available
				Object[] availableHeights = heightClasses.keySet().toArray();
				Arrays.sort(availableHeights);
				int minHeight = (Integer) availableHeights[0];
				int maxHeight = (Integer) availableHeights[availableHeights.length - 1];
				if (heightClass < minHeight) {
					heightClass = minHeight;
				} else if (heightClass > maxHeight) {
					heightClass = maxHeight;
				}
			}

			ArrayList<YieldElement> yieldElements = heightClasses.get(heightClass);
			double optimalBasalArea = 0d;
			for (int i = 0; i < yieldElements.size(); i++) {
				if (i == 0 || yieldElements.get(i).getBasalArea() > optimalBasalArea) {
					optimalBasalArea = yieldElements.get(i).getBasalArea();
				}
			}
			return basalArea / optimalBasalArea;
		} catch (Exception e) {
			throw new Exception("Error while extracting the stock density.");
		}
	}

	/**
	 * This method returns the relative ratio between h100:d100 with respect to average observed ratios
	 * by 5-m dominant height class.
	 * @param species a AWSTreeSpecies object
	 * @param d100 the dominant diameter (cm)
	 * @param h100 the dominant height (m)
	 * @return the relative ratio (a double)
	 */
	protected double getRelativeH100D100Ratio(AWSTreeSpecies species, double d100, double h100, int year) {
		Map<Integer, Double> ratiosForThisSpecies = relativeH100D100Ratios.get(species);
		if (ratiosForThisSpecies != null) {
			int yearClass = (int) Math.round(year / 5d) * 5;
			double meanRatioForThisYearClass = ratiosForThisSpecies.get(yearClass);
			return h100 / d100 / meanRatioForThisYearClass;
		} else  {
			return -1d;
		}
	}
	
	/**
	 * This method returns a set of default values for a particular species.
	 * @param species a AWSTreeSpecies instance
	 * @return a DefaultValues object
	 */
	protected AWSDefaultValuesSet getDefaultValues(AWSTreeSpecies species) {return defaultValuesMap.get(species);}
	
//	/*
//	 * For debugging only
//	 */
//	public static void main(String[] args) {
//		AWSReferenceTables yieldTables = new AWSReferenceTables();
//		yieldTables.init();
//	}

}
