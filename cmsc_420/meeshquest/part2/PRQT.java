package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;

import cmsc420.meeshquest.part2.Node.Type;

public class PRQT {
	/* ============================================================================================================= */
	/* Fields for PRQuadTree.
	/* first -> the top left coordinate in the section.
	/* second -> the top right coordinate in the section.
	/* third -> the bottom left coordinate in the section.
	/* fourth -> the bottom right coordinate in the section.
	/* midpoint -> the middle coordinate in the section.
	/* ============================================================================================================= */	
	Node root;
	int width, height;
	Point2D.Float[] quadrants;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */	
	public PRQT() {
		quadrants = new Point2D.Float[5];
		this.root = new White(quadrants);
	}
	/* ============================================================================================================= */
	/* Initialize the width and height of the PRQTree.
	/* ============================================================================================================= */	
	public void initialize(int spatialWidth, int spatialHeight) {
		this.width = spatialWidth;
		this.height = spatialHeight;
		quadrants[0] = new Point2D.Float(0, spatialHeight);
		quadrants[1] = new Point2D.Float(spatialWidth, spatialHeight);
		quadrants[2] = new Point2D.Float(0, 0);
		quadrants[3] = new Point2D.Float(spatialWidth, 0);
		quadrants[4] = new Point2D.Float(spatialWidth / 2, spatialHeight / 2);
	}
	/* ============================================================================================================= */
	/* Checks if a city is within the boundary.
	/* ============================================================================================================= */	
	public boolean checkBoundary(City newCity) {
		if (newCity.x > width) { return false; } 
		else {
			if (newCity.y > height) {
				return false;
			}
			else {
				return true;
			}
		}
	}
	/* ============================================================================================================= */
	/* Return the quadrants of the PRQT.
	/* ============================================================================================================= */	
	public Point2D.Float[] getQuadrants() {
		return quadrants;
	}
	/* ============================================================================================================= */
	/* Adds a city to the root of the tree.
	/* ============================================================================================================= */	
	public void addCitytoRoot(City city) {
		if (city.x < width && city.y < height) {
			if (root.nodeType() == Type.WHITE) {
				root = new Black(city, quadrants);
			} else if (root.nodeType() == Type.BLACK) {
				root = root.addCity(city, quadrants);
			} else if (root.nodeType() == Type.GRAY) {
				root = root.addCity(city, quadrants);
			}
		}		
	}
	/* ============================================================================================================= */
	/* Removes the city from the rooot.
	/* ============================================================================================================= */	
	public void removeCityfromRoot(City city) { root = root.removeCity(city); }
}
