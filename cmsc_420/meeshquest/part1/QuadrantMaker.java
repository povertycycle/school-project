package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

public class QuadrantMaker {
	public static Point2D.Float[] makeQuadrant1(Point2D.Float[] coordinates) {
		Point2D.Float[] quadrant1 = new Point2D.Float[5];
		// same as original top left
		quadrant1[0] = coordinates[0];
		// x = (top right - top left) / 2 and add to top left; y = same as top left or top right  
		quadrant1[1] = new Point2D.Float(((coordinates[1].x - coordinates[0].x) / 2) + coordinates[0].x, 
				coordinates[1].y);
		// x = same as top left or bottom left; y = (top left - bottom left) / 2 and add top bottom left
		quadrant1[2] = new Point2D.Float(coordinates[2].x, 
				((coordinates[0].y - coordinates[2].y) / 2) + coordinates[2].y);
		// same as the middle quadrant of the original
		quadrant1[3] = coordinates[4];
		// midpoint
		quadrant1[4] = new Point2D.Float(((quadrant1[1].x - quadrant1[0].x) / 2) + quadrant1[2].x, 
				((quadrant1[0].y - quadrant1[2].y) / 2) + quadrant1[2].y);
		
		return quadrant1;
		
	}

	public static Point2D.Float[] makeQuadrant2(Point2D.Float[] coordinates) {
		Point2D.Float[] quadrant2 = new Point2D.Float[5];
		// x = (top right - top left) / 2 and add to top left; y = same as top left or top right
		quadrant2[0] = new Point2D.Float(((coordinates[1].x - coordinates[0].x) / 2) + coordinates[0].x, 
				coordinates[1].y);
		// same as original top right
		quadrant2[1] = coordinates[1];
		// same as middle quadrant of the original
		quadrant2[2] = coordinates[4];
		// x = same as top right or bottom right; y = (top right - bottom right) / 2 and add top bottom right
		quadrant2[3] = new Point2D.Float(coordinates[1].x, 
				((coordinates[1].y - coordinates[3].y) / 2) + coordinates[3].y);
		// midpoint
		quadrant2[4] = new Point2D.Float(((quadrant2[1].x - quadrant2[0].x) / 2) + quadrant2[2].x, 
				((quadrant2[0].y - quadrant2[2].y) / 2) + quadrant2[2].y);
		
		return quadrant2;
	}

	public static Point2D.Float[] makeQuadrant3(Point2D.Float[] coordinates) {
		Point2D.Float[] quadrant3 = new Point2D.Float[5];
		// x = same as top left or bottom left; y = (top left - bottom left) / 2 and add top bottom left
		quadrant3[0] = new Point2D.Float(coordinates[2].x, 
				((coordinates[0].y - coordinates[2].y) / 2) + coordinates[2].y);
		// same as middle quadrant of the original
		quadrant3[1] = coordinates[4];
		// same as original bottom left 
		quadrant3[2] = coordinates[2];
		// x = (bottom right - bottom left) / 2 and add bottom left; y = same as bottom left 
		quadrant3[3] = new Point2D.Float(((coordinates[3].x - coordinates[2].x) / 2) + coordinates[2].x, 
				coordinates[2].y);
		// midpoint
		quadrant3[4] = new Point2D.Float(((quadrant3[1].x - quadrant3[0].x) / 2) + quadrant3[2].x, 
				((quadrant3[0].y - quadrant3[2].y) / 2) + quadrant3[2].y);
		
		return quadrant3;
	}

	public static Point2D.Float[] makeQuadrant4(Point2D.Float[] coordinates) {
		Point2D.Float[] quadrant4 = new Point2D.Float[5];
		// same as middle quadrant of the original
		quadrant4[0] = coordinates[4];
		// x = same as top right or bottom right; y = (top right - bottom right) / 2 and add top bottom right 
		quadrant4[1] = new Point2D.Float(coordinates[1].x, 
				((coordinates[1].y - coordinates[3].y) / 2) + coordinates[3].y);
		// x = (bottom right - bottom left) / 2 and add bottom left; y = same as bottom left  
		quadrant4[2] = new Point2D.Float(((coordinates[3].x - coordinates[2].x) / 2) + coordinates[2].x, 
				coordinates[2].y);
		// same as original bottom right
		quadrant4[3] = coordinates[3];
		// midpoint
		quadrant4[4] = new Point2D.Float(((quadrant4[1].x - quadrant4[0].x) / 2) + quadrant4[2].x, 
				((quadrant4[0].y - quadrant4[2].y) / 2) + quadrant4[2].y);
		
		return quadrant4;
	}
}
