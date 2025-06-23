package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

public class Black implements Node {

	City city;
	Point2D.Float[] quadrants;
	
	public Black(City thisCity, Point2D.Float[] coordinates) {
		this.city = thisCity;
		this.quadrants = coordinates;
	}

	public Node addCity(City newCity, Point2D.Float[] coords) {
		return new Gray(newCity, city, coords);
	}

	public Node removeCity(City newCity) {
		if (city.name.equals(newCity.name)) {
			city = null;
			return new White(quadrants);
		} else {
			return this;
		}
	}
	
	public Type nodeType() {
		return Type.BLACK;
	}
	
	public cmsc420.meeshquest.part1.Node[] getNodes() {
		cmsc420.meeshquest.part1.Node[] nodes = new Node[1];
		nodes[0] = this;
		return nodes;
	}
	
	public City getCity() {
		return city;
	}
	
	public Point2D.Float[] getQuadrants() {
		return quadrants;
	}
	
}
