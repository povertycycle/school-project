package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.TreeSet;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.Road;

public interface MetropoleNode {
	/* ============================================================================================================= */
	/* Type for the PRQTree nodes. 
	/* ============================================================================================================= */	
	public enum MetropoleType { WHITE, BLACK, GRAY }
	/* ============================================================================================================= */
	/* Interface methods. 
	/* ============================================================================================================= */	
	MetropoleNode addCity(City newCity, Point2D.Float[] quadrants);
	MetropoleNode addRoad(Road road, Float[] quadrants);
	MetropoleNode addAirport(Airport airport, Float[] quadrants);
	MetropoleNode removeCity(City city);
	MetropoleNode removeRoad(Road road);
	MetropoleNode removeAirport(Airport airport);
	MetropoleNode[] getNodes();
	MetropoleType nodeType();
	TreeSet<City> getCity();
	TreeSet<Road> getRoad();
	TreeSet<Airport> getAirport();
	TreeSet<Terminal> getTerminal();
	Point2D.Float[] getQuadrants();
	boolean hasCity(City city);	
	boolean hasRoad(Road road);
	boolean hasAirport(Airport airport);
	MetropoleNode addMetropole(Metropole metropole, Float[] quadrant1);
	Metropole getMetropole();
	Metropole getMetropole(float remoteX, float remoteY);
	MetropoleNode addTerminal(Terminal terminal, Float[] quadrants);
	boolean hasTerminal(Terminal terminal);
	MetropoleNode removeTerminal(Terminal t);
	TreeSet<City> getCitiesInRange(float remoteX, float remoteY, float radius);
	int getNumberVertices();
}
