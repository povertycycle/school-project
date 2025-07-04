package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;

public class White implements Node {
	/* ============================================================================================================= */
	/* Field for the coordinate of a white node.
	/* ============================================================================================================= */	
	Point2D.Float[] quadrants;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */	
	public White(Point2D.Float[] coordinates) {	this.quadrants = coordinates; }	
	/* ============================================================================================================= */
	/* Add a city into White node will make it a Black node.
	/* ============================================================================================================= */	
	public Node addCity(City newCity, Point2D.Float[] quadrants) { return new Black(newCity, quadrants); }
	/* ============================================================================================================= */
	/* Remove a city from a White node just making no changes.
	/* ============================================================================================================= */	
	public Node removeCity(City newCity) { return this; }
	/* ============================================================================================================= */
	/* Returns the type, i.e. 'Type.WHITE'
	/* ============================================================================================================= */	
	public Type nodeType() { return Type.WHITE;	}
	/* ============================================================================================================= */
	/* Get the nodes.
	/* ============================================================================================================= */	
	public cmsc420.meeshquest.part2.Node[] getNodes() {
		cmsc420.meeshquest.part2.Node[] whites = new Node[1];
		whites[0] = this;
		return whites;
	}
	/* ============================================================================================================= */
	/* No cities in a white node.
	/* ============================================================================================================= */	
	public City getCity() {	return null; }
	/* ============================================================================================================= */
	/* Get the coordinates points of the white node. 
	/* ============================================================================================================= */	
	public Point2D.Float[] getQuadrants() {	return quadrants; }
	/* ============================================================================================================= */
	/* Has city will return false if it is a white node. 
	/* ============================================================================================================= */	
	public boolean hasCity(City city) {	return false; }
}


