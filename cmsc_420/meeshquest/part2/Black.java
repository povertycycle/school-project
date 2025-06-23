package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;

public class Black implements Node {
	/* ============================================================================================================= */
	/* Fields for a city and the coordinates for the quadrants.
	/* ============================================================================================================= */	
	City city;
	Point2D.Float[] quadrants;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */		
	public Black(City thisCity, Point2D.Float[] coordinates) {
		this.city = thisCity;
		this.quadrants = coordinates;
	}
	/* ============================================================================================================= */
	/* Adding a city into a black PRQT node will make it Gray.
	/* ============================================================================================================= */	
	public Node addCity(City newCity, Point2D.Float[] coords) {
		return new Gray(newCity, city, coords);
	}
	/* ============================================================================================================= */
	/* Removing a city from a black PRQT node will make it White.
	/* ============================================================================================================= */	
	public Node removeCity(City newCity) {
		if (city.getName().equals(newCity.getName())) {
			city = null;
			return new White(quadrants);
		} else {
			return this;
		}
	}
	/* ============================================================================================================= */
	/* Returns the type of the node, i.e. 'Type.BLACK'.
	/* ============================================================================================================= */	
	public Type nodeType() {
		return Type.BLACK;
	}
	/* ============================================================================================================= */
	/* Returns this node as a list of nodes. 
	/* ============================================================================================================= */	
	public cmsc420.meeshquest.part2.Node[] getNodes() {
		cmsc420.meeshquest.part2.Node[] nodes = new Node[1];
		nodes[0] = this;
		return nodes;
	}
	/* ============================================================================================================= */
	/* Returns the city field.
	/* ============================================================================================================= */	
	public City getCity() {
		return this.city;
	}
	/* ============================================================================================================= */
	/* Returns the quadrants, i.e. the coordinates of the node.
	/* ============================================================================================================= */	
	public Point2D.Float[] getQuadrants() {
		return quadrants;
	}
	/* ============================================================================================================= */
	/* Check if the node has the specified city.
	/* ============================================================================================================= */	
	public boolean hasCity(City city) {
		return this.city.getColor().equals(city.getColor()) && 
				this.city.getName().equals(city.getName()) && 
				this.city.getRadius().equals(city.getRadius()) && 
				this.city.x == city.x && 
				this.city.y == city.y;
	}
}
