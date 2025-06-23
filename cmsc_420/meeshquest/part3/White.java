package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.TreeSet;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.Road;

public class White implements MetropoleNode {
	/* ============================================================================================================= */
	/* Field for the coordinate of a white node.
	/* ============================================================================================================= */	
	private Point2D.Float[] quadrants;
	private int pmOrder;
	private Metropole metropole;
	private int metropoleWidth, metropoleHeight, remoteWidth, remoteHeight;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */	
	public White(Point2D.Float[] coordinates, int pmOrder, int metropoleWidth, int metropoleHeight, 
			int remoteWidth, int remoteHeight) {	
		this.quadrants = coordinates;
		this.pmOrder = pmOrder;
		this.metropole = null;
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
	}
	/* ============================================================================================================= */
	/* Getter for the private fields. 
	/* ============================================================================================================= */	
	public Point2D.Float[] getQuadrants() {	
		return quadrants; 
	}
	public Metropole getMetropole() {
		return metropole;
	}
	public MetropoleNode[] getNodes() {
		MetropoleNode[] whites = new MetropoleNode[1];
		whites[0] = this;
		return whites;
	}
	public Metropole getMetropole(float remoteX, float remoteY) {
		return null;
	}
	/* ============================================================================================================= */
	/* Add City, Road, or Airport into a Metropole. Cannot insert Airport into White Node, because there is no
	/* connecting City existing in the White node.
	/* ============================================================================================================= */	
	public MetropoleNode addCity(City newCity, Point2D.Float[] quadrants) {
		return new Black(newCity, quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
	}
	public MetropoleNode addRoad(Road road, Point2D.Float[] quadrants) {
		return new Black(road, quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
	}
	public MetropoleNode addAirport(Airport airport, Point2D.Float[] quadrants) {
		return this;
	}
	/* ============================================================================================================= */
	/* Remove a City, Road, or Airport from a White node just making no changes.
	/* ============================================================================================================= */	
	public MetropoleNode removeCity(City city) { 
		return this; 
	}
	public MetropoleNode removeRoad(Road road) {
		return this;
	}
	public MetropoleNode removeAirport(Airport airport) {
		return this;
	}
	public MetropoleNode removeTerminal(Terminal t) {
		return this;
	}
	/* ============================================================================================================= */
	/* No cities in a white node.
	/* ============================================================================================================= */	
	public TreeSet<City> getCity() {	
		return new TreeSet<City>(); 
	}
	public TreeSet<Road> getRoad() {
		return new TreeSet<Road>();
	}
	public TreeSet<Airport> getAirport() {
		return new TreeSet<Airport>();
	}
	public TreeSet<Terminal> getTerminal() {
		return new TreeSet<Terminal>();
	}
	/* ============================================================================================================= */
	/* Returns the type, i.e. 'Type.WHITE'
	/* ============================================================================================================= */	
	public MetropoleType nodeType() { 
		return MetropoleType.WHITE;	
	}
	/* ============================================================================================================= */
	/* Checks if the Metropole Node has the specified City, Road, or Airport. 
	/* ============================================================================================================= */	
	public boolean hasCity(City city) {	
		return false; 
	}
	public boolean hasRoad(Road road) {
		return false;
	}
	public boolean hasAirport(Airport airport) {
		return false;
	}
	public boolean hasTerminal(Terminal terminal) {
		return false;
	}
	/* ============================================================================================================= */
	/* Adds metropole into a white node makes it a Black node. 
	/* ============================================================================================================= */	
	public MetropoleNode addMetropole(Metropole metropole, Float[] quadrant1) {
		return new Black(metropole, quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
	}
	public MetropoleNode addCity(City newCity) {
		return this.addCity(newCity, quadrants);
	}
	public MetropoleNode addRoad(Road road) {
		return this.addRoad(road, quadrants);
	}
	public MetropoleNode addAirport(Airport airport) {
		return this;
	}
	public MetropoleNode addTerminal(Terminal terminal, Float[] quadrants) {
		return this;
	}
	public TreeSet<City> getCitiesInRange(float remoteX, float remoteY, float radius) {
		return new TreeSet<City>();
	}
	public int getNumberVertices() {
		return 0;
	}
}


