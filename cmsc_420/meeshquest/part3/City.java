package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeSet;

import cmsc420.sortedmap.Road;

@SuppressWarnings("serial")
public class City extends Point2D.Float implements Comparable<City> {
	/* ============================================================================================================= */
	/* Fields of City class which has name, color, radius, and if the city is isolated.
	/* ============================================================================================================= */	
	private String name, color, airportName, cityNameIfIAmATerminal;
	private String radius;
	private float remoteX, remoteY;
	private TreeSet<Road> roadList;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */	
	public City(String cityName, float localX, float localY, float remoteX, float remoteY, 
			String r, String colour) {
		// super x and y becomes local x and y
		super(localX, localY);
		this.remoteX = remoteX;
		this.remoteY = remoteY;
		this.name = cityName;
		this.radius = r;
		this.color = colour;
		this.roadList = new TreeSet<Road>();
	}
	public City(String terminalName, float localX, float localY, float remoteX, float remoteY, 
			String r, String colour, String airportName, String cityName) {
		// super x and y becomes local x and y
		super(localX, localY);
		this.remoteX = remoteX;
		this.remoteY = remoteY;
		this.name = terminalName;
		this.radius = r;
		this.color = colour;
		this.airportName = airportName;
		this.cityNameIfIAmATerminal = cityName;
		this.roadList = new TreeSet<Road>();
	}
	/* ============================================================================================================= */
	/* Returns the name of the city.
	/* ============================================================================================================= */	
	public String getName() { 
		return this.name; 
	}
	/* ============================================================================================================= */
	/* Returns the color of the city.
	/* ============================================================================================================= */	
	public String getColor() { 
		return this.color; 
	}
	/* ============================================================================================================= */
	/* Returns the radius of the city.
	/* ============================================================================================================= */	
	public String getRadius() {	
		return radius;
	}
	/* ============================================================================================================= */
	/* Returns the x value of the coordinate as an int.
	/* ============================================================================================================= */	
	public float getLocalX() { 
		return this.x;
	}
	/* ============================================================================================================= */
	/* Returns the y value of the coordinate as an int.
	/* ============================================================================================================= */	
	public float getLocalY() { 
		return this.y; 
	}
	/* ============================================================================================================= */
	/* Returns the x value of the coordinate as an int.
	/* ============================================================================================================= */	
	public float getRemoteX() { 
		return this.remoteX; 
	}
	/* ============================================================================================================= */
	/* Returns the y value of the coordinate as an int.
	/* ============================================================================================================= */	
	public float getRemoteY() { 
		return this.remoteY;
	}
	/* ============================================================================================================= */
	/* Returns the local coordinates.
	/* ============================================================================================================= */	
	public Point2D.Float getLocal() { 
		return new Point2D.Float(x, y); 
	}
	/* ============================================================================================================= */
	/* Returns the remote coordinates.
	/* ============================================================================================================= */	
	public Point2D.Float getRemote() { 
		return new Point2D.Float(remoteX, remoteY); 
	}
	/* ============================================================================================================= */
	/* Checks if the cities are in the same metropole.
	/* ============================================================================================================= */	
	public boolean isInSameMetropole(City c) {
		if (
				this.getRemoteX() == c.getRemoteX() &&
				this.getRemoteY() == c.getRemoteY()
				) {
			return true;
		} 
		return false;
	}
	/* ============================================================================================================= */
	/* Comparator for the city.
	/* ============================================================================================================= */	
	public int compareTo(City o) {
		if (o == null) throw new NullPointerException();
		return Comparators.sortNames.compare(this.name, o.name);
	}
	/* ============================================================================================================= */
	/* It is terminal if it radius and color is "noRadius" and "noColor".
	/* ============================================================================================================= */	
	public boolean isTerminal() {
		if (this.radius.equals("noRadius") && this.color.equals("noColor")) return true;
		else return false;
	}
	/* ============================================================================================================= */
	/* Returns the airport name if this is a terminal.
	/* ============================================================================================================= */	
	public String getAirportName() { 
		return this.airportName; 
	}
	/* ============================================================================================================= */
	/* Returns the remote coordinates.
	/* ============================================================================================================= */	
	public String getTerminalsCityName() { 
		return this.cityNameIfIAmATerminal; 
	}
	/* ============================================================================================================= */
	/* Returns the list of Roads connected to this City.
	/* ============================================================================================================= */	
	public TreeSet<Road> getRoadList() {
		return roadList;
	}
	/* ============================================================================================================= */
	/* Adds the connected city to this road list.
	/* ============================================================================================================= */	
	public void addRoad(Road b) {
		if (!roadList.contains(b)) {
			roadList.add(b);
		}
	}
	/* ============================================================================================================= */
	/* Removes the connected city to this road list.
	/* ============================================================================================================= */	
	public void removeRoad(City b) {
		if (roadList.contains(b)) {
			roadList.remove(b);
		}
	}
	/* ============================================================================================================= */
	/* Removes the connected city to this road list.
	/* ============================================================================================================= */	
	public boolean isAlone() {
		return roadList.isEmpty();
	}
		
}