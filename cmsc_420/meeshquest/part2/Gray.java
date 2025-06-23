package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;

public class Gray implements Node {
	/* ============================================================================================================= */
	/* Fields for the 4 nodes and the coordinates of the areas. 
	/* ============================================================================================================= */	
	private Node[] fields;
	private Point2D.Float[] quadrants;
	/* ============================================================================================================= */
	/* Constructor for Gray node which can have 4 children. 
	/* ============================================================================================================= */	
	public Gray(City newCity, City city, Point2D.Float[] coordinates) {
		fields = new Node[4];
		Point2D.Float[] quadrant1 = QuadrantMaker.makeQuadrant1(coordinates);
		fields[0] = new White(quadrant1);
		Point2D.Float[] quadrant2 = QuadrantMaker.makeQuadrant2(coordinates);
		fields[1] = new White(quadrant2);
		Point2D.Float[] quadrant3 = QuadrantMaker.makeQuadrant3(coordinates);
		fields[2] = new White(quadrant3);
		Point2D.Float[] quadrant4 = QuadrantMaker.makeQuadrant4(coordinates);
		fields[3] = new White(quadrant4);
		quadrants = coordinates;
		
		// for city A
		if (city.x < coordinates[4].x) {
			if (city.y < coordinates[4].y) {
				// goes to Q3
				fields[2] = fields[2].addCity(city, quadrant3);
			} else if (city.y >= coordinates[4].y && city.y < coordinates[0].y) {
				// goes to Q1
				fields[0] = fields[0].addCity(city, quadrant1);
			}
		} else if (city.x >= coordinates[4].x && city.x < coordinates[3].x) {
			if (city.y < coordinates[4].y) {
				// goes to Q4
				fields[3] = fields[3].addCity(city, quadrant4);
			} else if (city.y >= coordinates[4].y && city.y < coordinates[0].y) {
				// goes to Q2
				fields[1] = fields[1].addCity(city, quadrant2);
			}
		}
		
		// for the new city.
		if (newCity.x < coordinates[4].x) {
			if (newCity.y < coordinates[4].y) {
				// goes to Q3
				fields[2] = fields[2].addCity(newCity, quadrant3);
			} else {
				// goes to Q1
				fields[0] = fields[0].addCity(newCity, quadrant1);
			}
		} else {
			if (newCity.y < coordinates[4].y) {
				// goes to Q4
				fields[3] = fields[3].addCity(newCity, quadrant4);
			} else {
				// goes to Q2
				fields[1] = fields[1].addCity(newCity, quadrant2);
			}
		}
	}
	/* ============================================================================================================= */
	/* Add a new city into the Gray node. 
	/* ============================================================================================================= */	
	public Node addCity(City newCity, Point2D.Float[] quadrants) {
		if (newCity.x < quadrants[4].x) {
			if (newCity.y < quadrants[4].y) {
				// goes to Q3
				fields[2] = fields[2].addCity(newCity, QuadrantMaker.makeQuadrant3(quadrants));
			} else {
				// goes to Q1
				fields[0] = fields[0].addCity(newCity, QuadrantMaker.makeQuadrant1(quadrants));
			}
		} else {
			if (newCity.y < quadrants[4].y) {
				// goes to Q4
				fields[3] = fields[3].addCity(newCity, QuadrantMaker.makeQuadrant4(quadrants));
			} else {
				// goes to Q2
				fields[1] = fields[1].addCity(newCity, QuadrantMaker.makeQuadrant2(quadrants));
			}
		}
		return this;
	}
	/* ============================================================================================================= */
	/* Removes a city from the Gray node and checks if the region will still be a Gray or will be a Black.
	/* ============================================================================================================= */	
	public Node removeCity(City newCity) {	
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i].removeCity(newCity);
		}
		
		int whiteCount = 0, blackCount = 0, count = 0;
		City[] transfer = new City[4];
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].nodeType() == Type.WHITE) { whiteCount++; }
			if (fields[i].nodeType() == Type.BLACK) { blackCount++; transfer[count++] = fields[i].getCity(); }
		}
		
		if (whiteCount == 4) {
			return new White(quadrants);
		} else {
			if (blackCount == 1 && whiteCount == 3) {
				return new Black(transfer[0], quadrants);
			} else {
				return this;
			}
		}
	}
	/* ============================================================================================================= */
	/* Returns the node type, i.e. 'Type.GRAY' 
	/* ============================================================================================================= */	
	public Type nodeType() { return Type.GRAY; }
	/* ============================================================================================================= */
	/* Get the 4 nodes of the children
	/* ============================================================================================================= */	
	public cmsc420.meeshquest.part2.Node[] getNodes() {	return fields; }
	/* ============================================================================================================= */
	/* Get city will return null because it is not a Black node, it will have multiple cities. 
	/* ============================================================================================================= */	
	public City getCity() {	return null; }
	/* ============================================================================================================= */
	/* Get the coordinates of the node. 
	/* ============================================================================================================= */	
	public Point2D.Float[] getQuadrants() {	return quadrants; }
	/* ============================================================================================================= */
	/* Checks if the Gray node has the specified city.
	/* ============================================================================================================= */	
	public boolean hasCity(City city) {
		for (int i = 0; i < 4; i++) {
			if (fields[i].hasCity(city)) {
				return true;
			}
		}
		return false;
	}
}
