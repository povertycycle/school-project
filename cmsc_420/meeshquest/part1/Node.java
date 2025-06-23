package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

interface Node {
	
	public enum Type {
		WHITE, BLACK, GREY
	}
	
	Node addCity(City newCity, Point2D.Float[] quadrants);

	Node removeCity(City newCity);

	Type nodeType();
	
	cmsc420.meeshquest.part1.Node[] getNodes();
	
	City getCity();
	
	Point2D.Float[] getQuadrants();
	
}