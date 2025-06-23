package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class Comparators {
	
	public static final Comparator<String> sortNames = new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.compareTo(s1);
			}
	};
	
	// do for this too
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

	
	
}