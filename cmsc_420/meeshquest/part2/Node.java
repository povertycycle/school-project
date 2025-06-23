package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;

interface Node {
	/* ============================================================================================================= */
	/* Type for the PRQTree nodes. 
	/* ============================================================================================================= */	
	public enum Type { WHITE, BLACK, GRAY }
	/* ============================================================================================================= */
	/* Interface methods. 
	/* ============================================================================================================= */	
	Node addCity(City newCity, Point2D.Float[] quadrants);
	Node removeCity(City newCity);
	Type nodeType();
	cmsc420.meeshquest.part2.Node[] getNodes();
	City getCity();
	Point2D.Float[] getQuadrants();
	boolean hasCity(City city);
}