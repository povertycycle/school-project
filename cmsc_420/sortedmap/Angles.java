package cmsc420.sortedmap;

import java.lang.*;
import java.io.*;
import java.awt.geom.*;

public class Angles {
	
	public enum Directions { LEFT, RIGHT, STRAIGHT }
	
	public static Directions getDirections(Point2D.Float a, Point2D.Float b, Point2D.Float c) {
		Arc2D.Double arc = new Arc2D.Double();
		arc.setArcByTangent(a, b, c, 1);
		Double angle = arc.getAngleExtent();
		if (-180 < angle && angle <= -45) {
			return Directions.LEFT;
		} else if (-45 <= angle && angle < 45) {
			return Directions.STRAIGHT;
		} else if (45 <= angle && angle < 180 || angle == -180) {
			return Directions.RIGHT;
		} else {
			throw new IllegalArgumentException();
		}
	}

}

