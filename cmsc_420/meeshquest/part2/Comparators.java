package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;
import java.util.Comparator;

import cmsc420.sortedmap.Geo2D;
import cmsc420.sortedmap.PMQuadtree.PMNode;
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
	public static final Comparator<Point2D.Float> sortCoordinates = new Comparator<Point2D.Float>() {
			public int compare(Point2D.Float coor1, Point2D.Float coor2) {
				float x1 = coor1.x;
				float x2 = coor2.x;
				float y1 = coor1.y;
				float y2 = coor2.y;
				
				if (y2 > y1) {
					return -1;
				} else if (y2 < y1) {
					return 1;
				} else {
					if (x2 > x1) {
						return -1;
					} else if (x2 < x1) {
						return 1;
					} else {
						return 0;
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
	
}