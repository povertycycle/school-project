package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.TreeSet;
import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.Road;

public class Black implements MetropoleNode {
	/* ============================================================================================================= */
	/* Fields for a city and the coordinates for the quadrants.
	/* ============================================================================================================= */	
	private Point2D.Float[] quadrants;
	private Metropole metropole;
	private int pmOrder;
	private int metropoleWidth, metropoleHeight, remoteWidth, remoteHeight;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */
			
	public Black(City city, Point2D.Float[] coordinates, int pmOrder, int metropoleWidth, int metropoleHeight,
			int remoteWidth, int remoteHeight) {
		float metropoleXPosition = city.getRemoteX();
		float metropoleYPosition = city.getRemoteY();
		this.pmOrder = pmOrder;
		this.quadrants = coordinates;
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
		this.metropole = new Metropole(metropoleXPosition, metropoleYPosition, 
				metropoleWidth, metropoleHeight, pmOrder);
		this.metropole.addCity(city);
				
	}
	public Black(Road road, Point2D.Float[] coordinates, int pmOrder, int metropoleWidth, int metropoleHeight,
			int remoteWidth, int remoteHeight) {
		float metropoleXPosition = road.getRemoteX();
		float metropoleYPosition = road.getRemoteY();
		this.quadrants = coordinates;
		this.pmOrder = pmOrder;
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
		this.metropole = new Metropole(metropoleXPosition, metropoleYPosition, metropoleWidth, metropoleHeight, pmOrder);
		metropole.addRoad(road);
	}
	public Black(Metropole metropole, Float[] quadrants, int pmOrder, int metropoleWidth, int metropoleHeight,
			int remoteWidth, int remoteHeight) {
		this.metropole = metropole;
		this.quadrants = quadrants;
		this.pmOrder = pmOrder;
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
		return this.metropole;
	}
	public MetropoleNode[] getNodes() {
		MetropoleNode[] black = new MetropoleNode[1];
		black[0] = this;
		return black;
	}
	public Metropole getMetropole(float remoteX, float remoteY) {
		if (this.metropole.getXCoordinate() == remoteX && this.metropole.getYCoordinate() == remoteY) {
			return this.metropole;
		} else {
			return null;
		}
	}
	/* ============================================================================================================= */
	/* Adding a City, Road, or Airport into a black PRQT node of different remote coordinates will make it Gray.
	/* ============================================================================================================= */	
	public MetropoleNode addCity(City city, Point2D.Float[] coords) {
		float thisMetropoleX = this.metropole.getXCoordinate();
		float thisMetropoleY = this.metropole.getYCoordinate();
		if (thisMetropoleX == city.getRemoteX() && thisMetropoleY == city.getRemoteY()) {
			this.metropole.addCity(city);
			return this;
		} else {
			return new Gray(metropole, city, coords, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		}
	}
	public MetropoleNode addRoad(Road road, Point2D.Float[] coords) {
		float thisMetropoleX = this.metropole.getXCoordinate();
		float thisMetropoleY = this.metropole.getYCoordinate();
		if (thisMetropoleX == road.getRemoteX() && thisMetropoleY == road.getRemoteY()) {
			this.metropole.addRoad(road);
			return this;
		} else {
			return new Gray(metropole, road, coords, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		}
	}
	public MetropoleNode addAirport(Airport airport, Point2D.Float[] coords) {
		float thisMetropoleX = this.metropole.getXCoordinate();
		float thisMetropoleY = this.metropole.getYCoordinate();
		if (thisMetropoleX == airport.getRemoteX() && thisMetropoleY == airport.getRemoteY()) {
			this.metropole.addAirport(airport);
			return this;
		} else {
			return new Gray(metropole, airport, coords, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		}
	}
	public MetropoleNode addTerminal(Terminal terminal, Point2D.Float[] coords) {
		float thisMetropoleX = this.metropole.getXCoordinate();
		float thisMetropoleY = this.metropole.getYCoordinate();
		if (thisMetropoleX == terminal.getTerminalRemoteX() && thisMetropoleY == terminal.getTerminalRemoteY()) {
			this.metropole.addTerminal(terminal);
			return this;
		} else {
			return new Gray(metropole, terminal, coords, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		}
	}
	/* ============================================================================================================= */
	/* Remove a City, Road, or Airport from a Black node.
	/* ============================================================================================================= */	
	public MetropoleNode removeCity(City city) {
		metropole.deleteCity(city);
		if (metropole.getMetropole().rootIsWhite()) {
			return new White(quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		} else {
			return this;
		}
	}
	public MetropoleNode removeRoad(Road road) {
		metropole.deleteRoad(road);
		if (metropole.getMetropole().rootIsWhite()) {
			return new White(quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		} else {
			return this;
		}
	}
	public MetropoleNode removeAirport(Airport airport) {
		metropole.deleteAirport(airport);
		if (metropole.getMetropole().rootIsWhite()) {
			return new White(quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		} else {
			return this;
		}
	}
	public MetropoleNode removeTerminal(Terminal t) {
		metropole.deleteTerminal(t);
		if (metropole.getMetropole().rootIsWhite()) {
			return new White(quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		} else {
			return this;
		}
	}
	/* ============================================================================================================= */
	/* Return list of cities in the metropole in Black node.
	/* ============================================================================================================= */	
	public TreeSet<City> getCity() {	
		return metropole.getCities(); 
	}
	public TreeSet<Road> getRoad() {
		return metropole.getRoads();
	}
	public TreeSet<Airport> getAirport() {
		return metropole.getAirports();
	}
	public TreeSet<Terminal> getTerminal() {
		return metropole.getTerminals();
	}
	/* ============================================================================================================= */
	/* Returns the type of the node, i.e. 'Type.BLACK'.
	/* ============================================================================================================= */	
	public MetropoleType nodeType() {
		return MetropoleType.BLACK;
	}
	/* ============================================================================================================= */
	/* Check if the node has the specified city.
	/* ============================================================================================================= */	
	public boolean hasCity(City city) {
		return metropole.hasCity(city);
	}
	public boolean hasRoad(Road road) {
		return metropole.hasRoad(road);
	}
	public boolean hasAirport(Airport airport) {
		return metropole.hasAirport(airport);
	}
	public boolean hasTerminal(Terminal terminal) {
		return metropole.hasTerminal(terminal);
	}
	/* ============================================================================================================= */
	/* Adding a metropole into a black node checks its coordinate and make it grey if they have different coordinates.
	/* ============================================================================================================= */
	public MetropoleNode addMetropole(Metropole metropole, Float[] quadrant1) {
		float thisX = this.metropole.getXCoordinate();
		float thisY = this.metropole.getYCoordinate();
		if (thisX == metropole.getXCoordinate() && thisY == metropole.getYCoordinate()) {
			this.metropole.addMetropole(metropole);
			return this;
		} else {
			return new Gray(this.metropole, metropole, this.quadrants, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		}
	}
	public TreeSet<City> getCitiesInRange(float remoteX, float remoteY, float radius) {
		float thisX = this.metropole.getXCoordinate();
		float thisY = this.metropole.getYCoordinate();
		TreeSet<City> m = new TreeSet<City>();
		if (
				remoteX - radius <= thisX && 
				thisX <= remoteX + radius && 
				remoteY - radius <= thisY && 
				thisY <= remoteY + radius
			) {
			m.addAll(metropole.getCities());
		}
		return m;
	}
	public int getNumberVertices() {
		return this.metropole.getMetropole().getNumberVertices();
	}
	
	
}
