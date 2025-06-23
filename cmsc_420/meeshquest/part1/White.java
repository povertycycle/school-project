package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

public class White implements Node {
	
	Point2D.Float[] quadrants;
	
	public White(Point2D.Float[] coordinates) {
		this.quadrants = coordinates;
	}
	
	public Node addCity(City newCity, Point2D.Float[] quadrants) {
		return new Black(newCity, quadrants);
	}
	
	public Node removeCity(City newCity) {
		return this;
	}
	
	public Type nodeType() {
		return Type.WHITE;
	}

	public cmsc420.meeshquest.part1.Node[] getNodes() {
		cmsc420.meeshquest.part1.Node[] whites = new Node[1];
		whites[0] = this;
		return whites;
	}
	
	public City getCity() {
		return null;
	}
	
	public Point2D.Float[] getQuadrants() {
		return quadrants;
	}

}


