package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class AirportList {
	/* ============================================================================================================= */
	/* AirportList fields to contain list of airports.
	/* ============================================================================================================= */	
	TreeMap<String, Airport> mappedAirports;
	/* ============================================================================================================= */
	/* Innerclass Airport
	/* ============================================================================================================= */	
	public static class Airport implements Comparable<Airport> {
		/* ========================================================================================================= */
		/* Fields for an Airport.
		/* ========================================================================================================= */	
		private String name;
		private float localX, localY, remoteX, remoteY, terminalX, terminalY;
		private City terminalCity;
		private TreeSet<Terminal> terminals;
		/* ========================================================================================================= */
		/* Constructor.
		/* ========================================================================================================= */	
		public Airport(String name, float localX, float localY, float remoteX, float remoteY, String terminalName, 
				float terminalLocalX, float terminalLocalY, City terminalCity) {
			this.name = name;
			this.localX = localX;
			this.localY = localY;
			this.remoteX = remoteX;
			this.remoteY = remoteY;
			Terminal referenceTerminal = new Terminal(terminalName, remoteX, remoteY, 
					terminalLocalX, terminalLocalY, terminalCity, name);
			this.terminalCity =  terminalCity;
			this.terminals = new TreeSet<Terminal>();
			this.terminals.add(referenceTerminal);
		}
		/* ========================================================================================================= */
		/* Getter for private fields.
		/* ========================================================================================================= */	
		public float getLocalX() { 
			return this.localX; 
		}
		public float getLocalY() { 
			return this.localY;
		}
		public float getRemoteX() { 
			return this.remoteX; 
		}
		public float getRemoteY() { 
			return this.remoteY; 
		}
		public String getName() {
			return name;
		}
		public float getTerminalX() {
			return terminalX;
		}
		public float getTerminalY() {
			return terminalY;
		}
		public City getTerminalCity() {
			return terminalCity;
		}
		public TreeSet<Terminal> getTerminal() {
			return terminals;
		}
		/* ============================================================================================================= */
		/* 
		/* ============================================================================================================= */
		public boolean inSameMetropoleAsTerminal(Terminal terminal) {
			if (
					this.remoteX == terminal.getTerminalRemoteX() &&
					this.remoteY == terminal.getTerminalRemoteY()
				) {
				return true;
			}
			return false;
		}
		public void insertTerminal(Terminal terminal2) {
			this.terminals.add(terminal2);
		}
		public int compareTo(Airport arg0) {
			if (this.getName().compareTo(arg0.getName()) > 0) return -1;
			else if (this.getName().compareTo(arg0.getName()) < 0) return 1;
			else return 0;
		}
	}
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */	
	public AirportList() {
		mappedAirports = new TreeMap<String, Airport>();
	}
	/* ============================================================================================================= */
	/* Maps an Airport if there is no same exact Airport.
	/* ============================================================================================================= */	
	public void mapAirport(Airport airport) {
		if (!checkDuplicateName(airport.getName())) {
			mappedAirports.put(airport.getName(), airport);
		}
	}
	/* ============================================================================================================= */
	/* Deletes an Airport. 
	/* ============================================================================================================= */
	public void unMapAirport(Airport airport) {
		mappedAirports.remove(airport.getName());
	}
	/* ============================================================================================================= */
	/* Checks if an airport already exists with the same name.
	/* ============================================================================================================= */	
	public boolean checkDuplicateName(String name) { 
		Iterator<String> iter = mappedAirports.keySet().iterator();
		while (iter.hasNext()) {
			String current = iter.next();
			if (
					mappedAirports.get(current).getName().equals(name)
			) {
				return true;
			}
		}
		return false;
	}
	/* ============================================================================================================= */
	/* Checks if an aiport already exists with the same coordinates. 
	/* ============================================================================================================= */	
	public boolean checkDuplicateCoordinate(Point2D.Float coorRemote, Point2D.Float coorLocal) {
		float localX = coorLocal.x;
		float localY = coorLocal.y;
		float remoteX = coorRemote.x;
		float remoteY = coorRemote.y;
		
		Iterator<String> iter = mappedAirports.keySet().iterator();
		while (iter.hasNext()) {
			String current = iter.next();
			Airport a = mappedAirports.get(current);
			if (
					a.localX == localX &&
					a.localY == localY &&
					a.remoteX == remoteX &&
					a.remoteY == remoteY
				) {
					return true;
			}
		}
		return false;		
	}
	/* ============================================================================================================= */
	/* Checks if an aiport is in bounds. 
	/* ============================================================================================================= */	
	public boolean checkAirportInBounds(int localSpatialWidth, int localSpatialHeight, 
			int remoteSpatialWidth, int remoteSpatialHeight, Airport airport) {
		float localX = airport.getLocalX();
		float localY = airport.getLocalY();
		float remoteX = airport.getRemoteX();
		float remoteY = airport.getRemoteY();
		
		/* Within bounds of PRQT. */
		if (
				0 <= remoteX && 
				remoteX < remoteSpatialWidth && 
				0 <= remoteY && 
				remoteY < remoteSpatialHeight
			) {
			/* Within bounds of PMQT. */
			if (
					0 <= localX &&
					localX <= localSpatialWidth &&
					0 <= localY &&
					localY <= localSpatialHeight	
				) {
					return true;
			}
		}
		return false;
	}
	/* ============================================================================================================= */
	/* Checks if the connecting City exists. 
	/* ============================================================================================================= */	
	public boolean checkConnectingIsMapped(Airport airport, Dictionary library) {
		TreeMap<String, City> cityList = library.cityList;
		City connecting = cityList.get(airport.getTerminalCity().getName());
		if (connecting == null) return false;
		return true;
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public boolean checkConnectingCityInSameMetropole(Airport airport, Dictionary library) {
		TreeMap<String, City> cityList = library.cityList;
		City connecting = cityList.get(airport.getTerminalCity().getName());
		if (connecting != null) {
			if (
				connecting.getRemoteX() == airport.getRemoteX() &&
				connecting.getRemoteY() == airport.getRemoteY()				
			) {
				return true;
			}
		} 
		return false;
	}
	/* ============================================================================================================= */
	/* Checks if the terminal already exists with the same name. 
	/* ============================================================================================================= */	
	public boolean checkTerminalName(String terminalName) {
		Iterator<String> iter = mappedAirports.keySet().iterator();
		while (iter.hasNext()) {
			String current = iter.next();
			Airport a = mappedAirports.get(current);
			TreeSet<Terminal> terminals = a.getTerminal();
			if (a.getTerminalCity().getName().equals(terminalName) || a.getName().equals(terminalName)) {
				return true;
			}
			Iterator<Terminal> iter2 = terminals.iterator();
			while (iter2.hasNext()) {
				Terminal t = iter2.next();
				if (t.getTerminalName().equals(terminalName) || t.getTerminalCity().getName().equals(terminalName)) {					
					return true;
				}
			}
			
		}
		return false;
	}
	/* ============================================================================================================= */
	/* Checks if the terminal already exists with the same name. 
	/* ============================================================================================================= */	
	public boolean checkTerminalCoordinates(Point2D.Float coorRemote, Point2D.Float coorLocal) {
		float localX = coorLocal.x;
		float localY = coorLocal.y;
		float remoteX = coorRemote.x;
		float remoteY = coorRemote.y;
		
		Iterator<String> iter = mappedAirports.keySet().iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			Airport c = mappedAirports.get(s);
			TreeSet<Terminal> curr = c.getTerminal();
			Iterator<Terminal> iter2 = curr.iterator();
			while (iter2.hasNext()) {
				Terminal current = iter2.next();
				if (
						current.getTerminalLocalX() == localX &&
						current.getTerminalLocalY() == localY &&
						c.remoteX == remoteX &&
						c.remoteY == remoteY 
					) {
						return true;
				}
			}
		}
		return false;		
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */	
	public boolean terminalOutOfBounds(int localSpatialWidth, int localSpatialHeight, 
			int remoteSpatialWidth, int remoteSpatialHeight, Terminal terminal) {
		float localX = terminal.getTerminalLocalX();
		float localY = terminal.getTerminalLocalY();
		float remoteX = terminal.getTerminalRemoteX();
		float remoteY = terminal.getTerminalRemoteY();
		
		/* Within bounds of PRQT. */
		if (
				0 <= remoteX && 
				remoteX < remoteSpatialWidth && 
				0 <= remoteY && 
				remoteY < remoteSpatialHeight
			) {
			/* Within bounds of PMQT. */
			if (
					0 <= localX &&
					localX <= localSpatialWidth &&
					0 <= localY &&
					localY <= localSpatialHeight	
				) {
					return false;
			}
		}
		return true;
	}
	/* ============================================================================================================= */
	/* Get Airport List. 
	/* ============================================================================================================= */	
	public TreeMap<String, Airport> getAirportList() {
		return this.mappedAirports;
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public boolean checkConnectingCityExists(Airport airport, Dictionary library) {
		TreeMap<String, City> list = library.cityList;
		if (airport.getTerminalCity() == null) return false;
		if (list.containsKey(airport.getTerminalCity().getName())) {
			City c = list.get(airport.getTerminalCity().getName());
			if (c.equals(airport.getTerminalCity())) return true;
		}
		return false;
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public Airport getAirport(String airportName) {
		Airport potentialAirport = null;
		potentialAirport = mappedAirports.get(airportName);
		return potentialAirport;
	}
	public Terminal getTerminal(String name) {
		Iterator<String> iter = mappedAirports.keySet().iterator();
		while (iter.hasNext()) {
			Airport a = mappedAirports.get(iter.next());
			TreeSet<Terminal> ts = a.getTerminal();
			Iterator<Terminal> iter2 = ts.iterator();
			while (iter2.hasNext()) {
				Terminal t = iter2.next();
				if (t.getTerminalName().equals(name)) return t;
			}
		}
		return null;
	}
	
}	
