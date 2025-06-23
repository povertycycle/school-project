package cmsc420.meeshquest.part3;

import java.util.Comparator;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.Road;

public class Comparators {
	/* ============================================================================================================= */
	/* Comparator for names in reverse asciibetical order.
	/* ============================================================================================================= */	
	public static final Comparator<String> sortNames = new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.compareTo(s1);
			}
	};
	/* ============================================================================================================= */
	/* Comparator for coordinates in sequence of y1 to y2 and x1 to x2.
	/* ============================================================================================================= */	
	public static final Comparator<City> sortCoordinates = new Comparator<City>() {
			public int compare(City city1, City city2) {
				float localX1 = city1.getLocalX();
				float localY1 = city1.getLocalY();
				float remoteX1 = city1.getRemoteX();
				float remoteY1 = city1.getRemoteY();
				float localX2 = city2.getLocalX();
				float localY2 = city2.getLocalY();
				float remoteX2 = city2.getRemoteX();
				float remoteY2 = city2.getRemoteY();
				
				/* Remote Y */
				if (remoteY2 > remoteY1) {
					return -1;
				} else if (remoteY2 < remoteY1) {
					return 1;
				} else {
					/* Remote X */
					if (remoteX2 > remoteX1) {
						return -1;
					} else if (remoteX2 < remoteX1) {
						return 1;
					} else {
						/* Local Y */
						if (localY2 > localY1) {
							return -1;
						} else if (localY2 < localY1) {
							return 1;
						} else {
							/* Local X */
							if (localX2 > localX1) {
								return -1;
							} else if (localX2 < localX1) {
								return 1;
							} else {
								return 0;
							}
						}
					}
				}
			}
	};
	/* ============================================================================================================= */
	/* Comparator for Roads (bidirectional) in sequence of their names. 
	/* ============================================================================================================= */	
	public static final Comparator<Road> sortRoads = new Comparator<Road>() { 
		public int compare(Road o1, Road o2) { 
			String start1 = o1.getStartName(); 
			String end1 = o1.getEndName(); 
			String start2 = o2.getStartName(); 
			String end2 = o2.getEndName(); 

			if (start2.equals(start1)) { 
				return end2.compareTo(end1); 
			} else{ 
				return start2.compareTo(start1); 
			}                
		} 
	};
	/* ============================================================================================================= */
	/* Comparator for Cities based on their names.
	/* ============================================================================================================= */	
	public static final Comparator<City> sortCities = new Comparator<City>() {
		public int compare(City arg0, City arg1) {
			String name1 = arg0.getName();
			String name2 = arg1.getName();		
			return name2.compareTo(name1);
		}
	};
	/* ============================================================================================================= */
	/* Comparator for Cities based on their names.
	/* ============================================================================================================= */	
	public static final Comparator<Airport> sortAirport = new Comparator<Airport>() {
		public int compare(Airport o1, Airport o2) {
			if (o1.getName().equals(o2.getName())) {
				return 0;
			} else {
				return 1;
			}
		}
		
	};
	
}