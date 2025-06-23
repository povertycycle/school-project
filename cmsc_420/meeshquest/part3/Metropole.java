package cmsc420.meeshquest.part3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.PMNode;
import cmsc420.sortedmap.PMQuadtree;
import cmsc420.sortedmap.PMQuadtree1;
import cmsc420.sortedmap.PMQuadtree3;
import cmsc420.sortedmap.Road;

public class Metropole {
	/* ============================================================================================================= */
	/* Fields that make up a metropole. Local is the spatialWidth and Height and Remote is the position in PRQT
	/* ============================================================================================================= */	
	private int metropoleWidth, metropoleHeight;
	private float metropolePRQTX, metropolePRQTY;
	private PMQuadtree metropole;
	private int pmOrder;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */	
	public Metropole(float xPosition, float yPosition, int metropoleWidth, int metropoleHeight, int pmOrder) {
		this.metropolePRQTX = xPosition;
		this.metropolePRQTY = yPosition;
		this.metropoleWidth = metropoleWidth;
		this.metropoleHeight= metropoleHeight;
		this.pmOrder = pmOrder;
		if (pmOrder == 1) {
			this.metropole = new PMQuadtree1(metropoleWidth, metropoleHeight);
		} else if (pmOrder == 3) {
			this.metropole = new PMQuadtree3(metropoleWidth, metropoleHeight);
		}
	}
	/* ============================================================================================================= */
	/* Public methods.
	/* ============================================================================================================= */	
	public float getXCoordinate() {
		return this.metropolePRQTX;
	}
	public float getYCoordinate() {
		return this.metropolePRQTY;
	}
	public PMQuadtree getMetropole() {
		return this.metropole;
	}
	public int getPMOrder() { 
		return this.pmOrder;
	}
	public void addCity(City city) {
		if (city.getRemoteX() == this.metropolePRQTX && city.getRemoteY() == this.metropolePRQTY) {
				this.metropole.addCity(city);
		}
	}
	public void addRoad(Road road) {
		if (road.getRemoteX() == this.metropolePRQTX && road.getRemoteY() == this.metropolePRQTY) {
			if (!this.metropole.hasRoad(road)) {
				this.metropole.addRoad(road);
			}
		}
	}
	public void addAirport(Airport airport) {
		if (airport.getRemoteX() == this.metropolePRQTX && airport.getRemoteY() == this.metropolePRQTY) {
			if (!this.metropole.hasAirport(airport)) {
				this.metropole.addAirport(airport);
			}
		}
	}
	public void addTerminal(Terminal terminal) {
		if (terminal.getTerminalRemoteX() == this.metropolePRQTX && 
				terminal.getTerminalRemoteY() == this.metropolePRQTY) {
			if (!this.metropole.hasTerminal(terminal)) {
				this.metropole.addTerminal(terminal);
			}
		}
	}
	public TreeSet<City> getCities() {
		return this.metropole.getRoot().getCities();
	}
	public TreeSet<Road> getRoads() {
		return this.metropole.getRoot().getRoads();
	}
	public TreeSet<Airport> getAirports() {
		return this.metropole.getRoot().getAirports();
	}
	public TreeSet<Terminal> getTerminals() {
		return this.metropole.getRoot().getTerminals();
	}
	public boolean hasCity(City city) {
		return this.metropole.hasCity(city);
	}
	public boolean hasRoad(Road road) {
		return this.metropole.hasRoad(road);
	}
	public boolean hasAirport(Airport airport) {
		return this.metropole.hasAirport(airport);
	}
	public boolean hasTerminal(Terminal terminal) {
		return this.metropole.hasTerminal(terminal);
	}
	public void deleteCity(City city) {
		this.metropole.deleteCity(city);
	}
	public void deleteRoad(Road road) {
		this.metropole.deleteRoad(road);
	}
	public void deleteAirport(Airport airport) {
		this.metropole.deleteAirport(airport);
	}
	public void deleteTerminal(Terminal terminal) {
		this.metropole.deleteTerminal(terminal);
	}
	public void addMetropole(Metropole metropole) {
		TreeSet<City> cityList = metropole.getCities();
		TreeSet<Road> roadList = metropole.getRoads();
		TreeSet<Airport> airportList = metropole.getAirports();
		TreeSet<Terminal> terminalList = metropole.getTerminals();
		Iterator<City> iter = cityList.iterator();
		Iterator<Road> iter2 = roadList.iterator();
		Iterator<Airport> iter3 = airportList.iterator();
		Iterator<Terminal> iter4 = terminalList.iterator();
		while (iter.hasNext()) {
			this.metropole.addCity(iter.next());
		}
		while (iter2.hasNext()) {
			this.metropole.addRoad(iter2.next());
		}
		while (iter3.hasNext()) {
			this.metropole.addAirport(iter3.next());
		}
		while (iter4.hasNext()) {
			this.metropole.addTerminal(iter4.next());
		}
	}
	public boolean roadViolatesPMRules(Road r) {
		return this.metropole.roadViolatesPMRules(r);
	}
	public boolean airportViolatesPMRules(Airport airport) {
		return this.metropole.airportViolatesPMRules(airport);
	}
	public boolean cityViolatesPMRules(City a) {
		return this.metropole.cityViolatesPMRules(a);
	}
	public boolean terminalViolatesPMRules(Terminal terminal) {
		return this.metropole.terminalViolatesPMRules(terminal);
	}
	
	
	
	

}
