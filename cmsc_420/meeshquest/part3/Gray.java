package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.TreeSet;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.Road;

public class Gray implements MetropoleNode {
	/* ============================================================================================================= */
	/* Fields for the 4 nodes and the coordinates of the areas. 
	/* ============================================================================================================= */	
	private MetropoleNode[] fields;
	private Point2D.Float[] quadrants;
	private int pmOrder;
	private int metropoleWidth, metropoleHeight, remoteWidth, remoteHeight;
	/* ============================================================================================================= */
	/* Constructor for Gray node which can have 4 children. 
	/* ============================================================================================================= */	
	public Gray(Metropole metropole, City city, Point2D.Float[] coordinates, int metropoleWidth, int metropoleHeight, 
			int remoteWidth, int remoteHeight) {
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
		int pmOrder = metropole.getPMOrder();
		fields = new MetropoleNode[4];
		Point2D.Float[] quadrant1 = QuadrantMaker.makeQuadrant1(coordinates);
		fields[0] = new White(quadrant1, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant2 = QuadrantMaker.makeQuadrant2(coordinates);
		fields[1] = new White(quadrant2, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant3 = QuadrantMaker.makeQuadrant3(coordinates);
		fields[2] = new White(quadrant3, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant4 = QuadrantMaker.makeQuadrant4(coordinates);
		fields[3] = new White(quadrant4, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		quadrants = coordinates;
		
		/* For Metropole. */
		if (metropole.getXCoordinate() < coordinates[4].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addMetropole(metropole, quadrant3);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q1. */
				fields[0] = fields[0].addMetropole(metropole, quadrant1);
			}
		} else if (metropole.getXCoordinate() >= coordinates[4].x && metropole.getXCoordinate() < coordinates[3].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addMetropole(metropole, quadrant4);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q2. */
				fields[1] = fields[1].addMetropole(metropole, quadrant2);
			}
		}
		
		// for the new City.
		if (city.getRemoteX() < coordinates[4].x) {
			if (city.getRemoteY() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addCity(city, quadrant3);
			} else {
				/* Goes to Q1. */
				fields[0] = fields[0].addCity(city, quadrant1);
			}
		} else {
			if (city.getRemoteY() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addCity(city, quadrant4);
			} else {
				/* Goes to Q2. */
				fields[1] = fields[1].addCity(city, quadrant2);
			}
		}
	}
	public Gray(Metropole metropole, Road road, Point2D.Float[] coordinates, int metropoleWidth, int metropoleHeight, 
			int remoteWidth, int remoteHeight) {
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
		int pmOrder = metropole.getPMOrder();
		fields = new MetropoleNode[4];
		Point2D.Float[] quadrant1 = QuadrantMaker.makeQuadrant1(coordinates);
		fields[0] = new White(quadrant1, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant2 = QuadrantMaker.makeQuadrant2(coordinates);
		fields[1] = new White(quadrant2, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant3 = QuadrantMaker.makeQuadrant3(coordinates);
		fields[2] = new White(quadrant3, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant4 = QuadrantMaker.makeQuadrant4(coordinates);
		fields[3] = new White(quadrant4, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		quadrants = coordinates;
		
		/* For Metropole. */
		if (metropole.getXCoordinate() < coordinates[4].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addMetropole(metropole, quadrant3);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q1. */
				fields[0] = fields[0].addMetropole(metropole, quadrant1);
			}
		} else if (metropole.getXCoordinate() >= coordinates[4].x && metropole.getXCoordinate() < coordinates[3].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addMetropole(metropole, quadrant4);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q2. */
				fields[1] = fields[1].addMetropole(metropole, quadrant2);
			}
		}
		
		// for the new City.
		if (road.getRemoteX() < coordinates[4].x) {
			if (road.getRemoteY() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addRoad(road, quadrant3);
			} else {
				/* Goes to Q1. */
				fields[0] = fields[0].addRoad(road, quadrant1);
			}
		} else {
			if (road.getRemoteY() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addRoad(road, quadrant4);
			} else {
				/* Goes to Q2. */
				fields[1] = fields[1].addRoad(road, quadrant2);
			}
		}
	}
	public Gray(Metropole metropole, Airport airport, Point2D.Float[] coordinates, int metropoleWidth, int metropoleHeight, 
			int remoteWidth, int remoteHeight) {
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
		int pmOrder = metropole.getPMOrder();
		fields = new MetropoleNode[4];
		Point2D.Float[] quadrant1 = QuadrantMaker.makeQuadrant1(coordinates);
		fields[0] = new White(quadrant1, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant2 = QuadrantMaker.makeQuadrant2(coordinates);
		fields[1] = new White(quadrant2, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant3 = QuadrantMaker.makeQuadrant3(coordinates);
		fields[2] = new White(quadrant3, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant4 = QuadrantMaker.makeQuadrant4(coordinates);
		fields[3] = new White(quadrant4, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		quadrants = coordinates;
		
		/* For Metropole. */
		if (metropole.getXCoordinate() < coordinates[4].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addMetropole(metropole, quadrant3);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q1. */
				fields[0] = fields[0].addMetropole(metropole, quadrant1);
			}
		} else if (metropole.getXCoordinate() >= coordinates[4].x && metropole.getXCoordinate() < coordinates[3].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addMetropole(metropole, quadrant4);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q2. */
				fields[1] = fields[1].addMetropole(metropole, quadrant2);
			}
		}
		// for the new City.
		if (airport.getRemoteX() < coordinates[4].x) {
			if (airport.getRemoteY() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addAirport(airport, quadrant3);
			} else {
				/* Goes to Q1. */
				fields[0] = fields[0].addAirport(airport, quadrant1);
			}
		} else {
			if (airport.getRemoteY() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addAirport(airport, quadrant4);
			} else {
				/* Goes to Q2. */
				fields[1] = fields[1].addAirport(airport, quadrant2);
			}
		}
	}
	public Gray(Metropole metropole, Terminal terminal, Float[] coordinates, int metropoleWidth, int metropoleHeight,
			int remoteWidth, int remoteHeight) {
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
		int pmOrder = metropole.getPMOrder();
		fields = new MetropoleNode[4];
		Point2D.Float[] quadrant1 = QuadrantMaker.makeQuadrant1(coordinates);
		fields[0] = new White(quadrant1, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant2 = QuadrantMaker.makeQuadrant2(coordinates);
		fields[1] = new White(quadrant2, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant3 = QuadrantMaker.makeQuadrant3(coordinates);
		fields[2] = new White(quadrant3, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant4 = QuadrantMaker.makeQuadrant4(coordinates);
		fields[3] = new White(quadrant4, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		quadrants = coordinates;
		
		/* For Metropole. */
		if (metropole.getXCoordinate() < coordinates[4].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addMetropole(metropole, quadrant3);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q1. */
				fields[0] = fields[0].addMetropole(metropole, quadrant1);
			}
		} else if (metropole.getXCoordinate() >= coordinates[4].x && metropole.getXCoordinate() < coordinates[3].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addMetropole(metropole, quadrant4);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q2. */
				fields[1] = fields[1].addMetropole(metropole, quadrant2);
			}
		}
		// for the new City.
		if (terminal.getTerminalRemoteX() < coordinates[4].x) {
			if (terminal.getTerminalRemoteY() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addTerminal(terminal, quadrant3);
			} else {
				/* Goes to Q1. */
				fields[0] = fields[0].addTerminal(terminal, quadrant1);
			}
		} else {
			if (terminal.getTerminalRemoteY() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addTerminal(terminal, quadrant4);
			} else {
				/* Goes to Q2. */
				fields[1] = fields[1].addTerminal(terminal, quadrant2);
			}
		}
	}
	public Gray(Metropole metropole, Metropole metropole2, Float[] coordinates, int metropoleWidth,
			int metropoleHeight, int remoteWidth, int remoteHeight) {
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight = metropoleHeight;
		this.remoteWidth = remoteWidth;
		this.remoteHeight = remoteHeight;
		int pmOrder = metropole.getPMOrder();
		fields = new MetropoleNode[4];
		Point2D.Float[] quadrant1 = QuadrantMaker.makeQuadrant1(coordinates);
		fields[0] = new White(quadrant1, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant2 = QuadrantMaker.makeQuadrant2(coordinates);
		fields[1] = new White(quadrant2, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant3 = QuadrantMaker.makeQuadrant3(coordinates);
		fields[2] = new White(quadrant3, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		Point2D.Float[] quadrant4 = QuadrantMaker.makeQuadrant4(coordinates);
		fields[3] = new White(quadrant4, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		this.quadrants = coordinates;
		
		/* For Metropole. */
		if (metropole.getXCoordinate() < coordinates[4].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addMetropole(metropole, quadrant3);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q1. */
				fields[0] = fields[0].addMetropole(metropole, quadrant1);
			}
		} else if (metropole.getXCoordinate() >= coordinates[4].x && metropole.getXCoordinate() < coordinates[3].x) {
			if (metropole.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addMetropole(metropole, quadrant4);
			} else if (metropole.getYCoordinate() >= coordinates[4].y && metropole.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q2. */
				fields[1] = fields[1].addMetropole(metropole, quadrant2);
			}
		}
		/* For Metropole. */
		if (metropole2.getXCoordinate() < coordinates[4].x) {
			if (metropole2.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q3. */
				fields[2] = fields[2].addMetropole(metropole2, quadrant3);
			} else if (metropole2.getYCoordinate() >= coordinates[4].y && metropole2.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q1. */
				fields[0] = fields[0].addMetropole(metropole2, quadrant1);
			}
		} else if (metropole2.getXCoordinate() >= coordinates[4].x && metropole2.getXCoordinate() < coordinates[3].x) {
			if (metropole2.getYCoordinate() < coordinates[4].y) {
				/* Goes to Q4. */
				fields[3] = fields[3].addMetropole(metropole2, quadrant4);
			} else if (metropole2.getYCoordinate() >= coordinates[4].y && metropole2.getYCoordinate() < coordinates[0].y) {
				/* Goes to Q2. */
				fields[1] = fields[1].addMetropole(metropole2, quadrant2);
			}
		}
	}
	/* ============================================================================================================= */
	/* Getter for the private fields. 
	/* ============================================================================================================= */	
	public Point2D.Float[] getQuadrants() {	
		return quadrants; 
	}
	public Metropole getMetropole() {
		return null;
	}
	public MetropoleNode[] getNodes() {
		return fields;
	}
	public Metropole getMetropole(float remoteX, float remoteY) {
		for (int i = 0; i < 4; i++) {
			if (fields[i].getMetropole(remoteX, remoteY) != null) {
				return fields[i].getMetropole(remoteX, remoteY);
			}
		}
		return null;
	}
	/* ============================================================================================================= */
	/* Adding a City, Road, or Airport into a grey PRQT node.
	/* ============================================================================================================= */	
	public MetropoleNode addCity(City newCity, Point2D.Float[] quadrants) {
		if (newCity.getRemoteX() < this.quadrants[4].x) {
			if (newCity.getRemoteY() < this.quadrants[4].y) {
				// goes to Q3
				fields[2] = fields[2].addCity(newCity, QuadrantMaker.makeQuadrant3(quadrants));
			} else {
				// goes to Q1
				fields[0] = fields[0].addCity(newCity, QuadrantMaker.makeQuadrant1(quadrants));
			}
		} else {
			if (newCity.getRemoteY() < this.quadrants[4].y) {
				// goes to Q4
				fields[3] = fields[3].addCity(newCity, QuadrantMaker.makeQuadrant4(quadrants));
			} else {
				// goes to Q2
				fields[1] = fields[1].addCity(newCity, QuadrantMaker.makeQuadrant2(quadrants));
			}
		}
		return this;
	}
	public MetropoleNode addRoad(Road road, Point2D.Float[] quadrants) {
		if (road.getRemoteX() < quadrants[4].x) {
			if (road.getRemoteY() < quadrants[4].y) {
				// goes to Q3
				fields[2] = fields[2].addRoad(road, QuadrantMaker.makeQuadrant3(quadrants));
			} else {
				// goes to Q1
				fields[0] = fields[0].addRoad(road, QuadrantMaker.makeQuadrant1(quadrants));
			}
		} else {
			if (road.getRemoteY() < quadrants[4].y) {
				// goes to Q4
				fields[3] = fields[3].addRoad(road, QuadrantMaker.makeQuadrant4(quadrants));
			} else {
				// goes to Q2
				fields[1] = fields[1].addRoad(road, QuadrantMaker.makeQuadrant2(quadrants));
			}
		}
		return this;
	}
	public MetropoleNode addAirport(Airport airport, Point2D.Float[] quadrants) {
		if (airport.getRemoteX() < quadrants[4].x) {
			if (airport.getRemoteY() < quadrants[4].y) {
				// goes to Q3
				fields[2] = fields[2].addAirport(airport, QuadrantMaker.makeQuadrant3(quadrants));
			} else {
				// goes to Q1
				fields[0] = fields[0].addAirport(airport, QuadrantMaker.makeQuadrant1(quadrants));
			}
		} else {
			if (airport.getRemoteY() < quadrants[4].y) {
				// goes to Q4
				fields[3] = fields[3].addAirport(airport, QuadrantMaker.makeQuadrant4(quadrants));
			} else {
				// goes to Q2
				fields[1] = fields[1].addAirport(airport, QuadrantMaker.makeQuadrant2(quadrants));
			}
		}
		return this;
	}
	public MetropoleNode addTerminal(Terminal terminal, Float[] quadrants) {
		if (terminal.getTerminalRemoteX() < quadrants[4].x) {
			if (terminal.getTerminalRemoteY() < quadrants[4].y) {
				// goes to Q3
				fields[2] = fields[2].addTerminal(terminal, QuadrantMaker.makeQuadrant3(quadrants));
			} else {
				// goes to Q1
				fields[0] = fields[0].addTerminal(terminal, QuadrantMaker.makeQuadrant1(quadrants));
			}
		} else {
			if (terminal.getTerminalRemoteY() < quadrants[4].y) {
				// goes to Q4
				fields[3] = fields[3].addTerminal(terminal, QuadrantMaker.makeQuadrant4(quadrants));
			} else {
				// goes to Q2
				fields[1] = fields[1].addTerminal(terminal, QuadrantMaker.makeQuadrant2(quadrants));
			}
		}
		return this;
	}
	/* ============================================================================================================= */
	/* Remove a City, Road, or Airport from a Black node.
	/* ============================================================================================================= */	
	public MetropoleNode fixNodes() {
		int whiteCount = 0, blackCount = 0, count = 0;
		Metropole[] transfer = new Metropole[4];
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].nodeType() == MetropoleType.WHITE) { 
				whiteCount++; 
			}
			if (fields[i].nodeType() == MetropoleType.BLACK) { 
				blackCount++; 
				transfer[count++] = fields[i].getMetropole(); 
			}
		}
		
		if (whiteCount == 4) {
			return new White(quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
		} else {
			if (blackCount == 1 && whiteCount == 3) {
				return new Black(transfer[0], quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
			} else {
				return this;
			}
		}
	}
	public MetropoleNode removeCity(City city) {	
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i].removeCity(city);
		}
		return fixNodes();
	}
	public MetropoleNode removeRoad(Road road) {
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i].removeRoad(road);
		}
		return fixNodes();
	}
	public MetropoleNode removeAirport(Airport airport) {
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i].removeAirport(airport);
		}
		return fixNodes();
	}
	public MetropoleNode removeTerminal(Terminal terminal) {
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i].removeTerminal(terminal);
		}
		return fixNodes();
	}
	/* ============================================================================================================= */
	/* Return list of cities in the metropole in Grey node.
	/* ============================================================================================================= */	
	public TreeSet<City> getCity() {
		TreeSet<City> cityList = new TreeSet<City>();
		for (int i = 0; i < fields.length; i++) {
			cityList.addAll(fields[i].getCity());
		}
		return cityList;
	}
	public TreeSet<Road> getRoad() {
		TreeSet<Road> roadList = new TreeSet<Road>();
		for (int i = 0; i < fields.length; i++) {
			roadList.addAll(fields[i].getRoad());
		}
		return roadList;
	}
	public TreeSet<Airport> getAirport() {
		TreeSet<Airport> airportList = new TreeSet<Airport>();
		for (int i = 0; i < fields.length; i++) {
			airportList.addAll(fields[i].getAirport());
		}
		return airportList;
	}
	public TreeSet<Terminal> getTerminal() {
		TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
		for (int i = 0; i < fields.length; i++) {
			terminalList.addAll(fields[i].getTerminal());
		}
		return terminalList;
	}
	/* ============================================================================================================= */
	/* Returns the node type, i.e. 'Type.GRAY' 
	/* ============================================================================================================= */	
	public MetropoleType nodeType() { 
		return MetropoleType.GRAY; 
	}
	/* ============================================================================================================= */
	/* Checks if the Metropole Node has the specified City, Road, or Airport. 
	/* ============================================================================================================= */	
	public boolean hasCity(City city) {	
		return fields[0].hasCity(city) || fields[1].hasCity(city) || fields[2].hasCity(city) || fields[3].hasCity(city); 
	}
	public boolean hasRoad(Road road) {
		return fields[0].hasRoad(road) || fields[1].hasRoad(road) || fields[2].hasRoad(road) || fields[3].hasRoad(road);
	}
	public boolean hasAirport(Airport a) {
		return fields[0].hasAirport(a) || fields[1].hasAirport(a) || fields[2].hasAirport(a) || fields[3].hasAirport(a);
	}
	public boolean hasTerminal(Terminal t) {
		return fields[0].hasTerminal(t) || fields[1].hasTerminal(t) || fields[2].hasTerminal(t) || fields[3].hasTerminal(t);
	}
	/* ============================================================================================================= */
	/* Adds the metropole into one of the node.
	/* ============================================================================================================= */	
	public MetropoleNode addMetropole(Metropole metropole, Float[] quadrant1) {
		for (int i = 0; i < 4; i++) {
			Metropole m = fields[i].getMetropole();
			if (m != null && 
					m.getXCoordinate() == metropole.getXCoordinate() && 
					m.getYCoordinate() == metropole.getYCoordinate()) {
				fields[i] = fields[i].addMetropole(metropole, quadrant1);
			}
		}
		return this;
	}
	public TreeSet<City> getCitiesInRange(float remoteX, float remoteY, float radius) {
		TreeSet<City> m = new TreeSet<City>();
		for (int i = 0; i < 4; i++) {
			m.addAll(fields[i].getCitiesInRange(remoteX, remoteY, radius));
		}
		return m;
	}
	public int getNumberVertices() {
		return fields[0].getNumberVertices() + 
				fields[1].getNumberVertices() + 
				fields[2].getNumberVertices() + 
				fields[3].getNumberVertices();
	}
	
	
}
