package cmsc420.meeshquest.part3;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.PMNode;
import cmsc420.sortedmap.PMQuadtree;
import cmsc420.sortedmap.Road;

public class PRQT {
	/* ============================================================================================================= */
	/* PQRT Now contains Metropoles instead of nodes after patch v3.0 and remote spatial coordinates.
	/* First -> the top left coordinate in the section. 
11	/* Second -> the top right coordinate in the section. 
12	/* Third -> the bottom left coordinate in the section. 
13	/* Fourth -> the bottom right coordinate in the section. 
14	/* Midpoint -> the middle coordinate in the section. 
	/* ============================================================================================================= */	
	private int remoteWidth, remoteHeight, metropoleWidth, metropoleHeight, pmOrder;
	private MetropoleNode rootNode;
	private Point2D.Float[] quadrants;
	/* ============================================================================================================= */
	/* Constructor.
	/* ============================================================================================================= */	
	public PRQT() {
		this.quadrants = new Point2D.Float[5];
	}
	/* ============================================================================================================= */
	/* Initialize the width and height of the PRQTree.
	/* ============================================================================================================= */	
	public void initialize(int remoteSpatialWidth, int remoteSpatialHeight, 
			int localSpatialWidth, int localSpatialHeight, int pmOrder) {
		remoteWidth = remoteSpatialWidth;
		remoteHeight = remoteSpatialHeight;
		metropoleWidth = localSpatialWidth;
		metropoleHeight = localSpatialHeight;
		this.pmOrder = pmOrder;
		this.quadrants[0] = new Point2D.Float(0, remoteHeight); 
		this.quadrants[1] = new Point2D.Float(remoteWidth, remoteHeight); 
		this.quadrants[2] = new Point2D.Float(0, 0); 
		this.quadrants[3] = new Point2D.Float(remoteWidth, 0); 
		this.quadrants[4] = new Point2D.Float(remoteWidth / 2, remoteHeight / 2); 
		this.rootNode = new White(quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
	}
	/* ============================================================================================================= */
	/* Getter for private fields.
	/* ============================================================================================================= */	
	public Point2D.Float[] getQuadrants() {
		return quadrants;
	}
	public int getMetropoleX() {
		return metropoleWidth;
	}
	public int getMetropoleY() {
		return metropoleHeight;
	}
	public MetropoleNode getMetropoleRoot() {
		return this.rootNode;
	}
	public int getRemoteX() {
		return remoteWidth;
	}
	public int getRemoteY() {
		return remoteHeight;
	}
	/* ============================================================================================================= */
	/* Clear all.
	/* ============================================================================================================= */	
	public void clear() { 
		this.rootNode = new White(quadrants, pmOrder, metropoleWidth, metropoleHeight, remoteWidth, remoteHeight);
	}
	/* ============================================================================================================= */
	/* Remove a specified city in the PRQT by going into the PMQuadTree.
	/* ============================================================================================================= */
	public Metropole getMetropole(float remoteX, float remoteY) {
		if (
				0 <= remoteX &&
				0 <= remoteY &&
				remoteX < remoteWidth &&
				remoteY < remoteHeight
			) {
			return rootNode.getMetropole(remoteX, remoteY);
		}
		return null;
	}
	public TreeSet<City> getCitiesInRange(float remoteX, float remoteY, float radius) {
		return this.rootNode.getCitiesInRange(remoteX, remoteY, radius);
	}
	/* ============================================================================================================= */
	/* Remove a specified city in the PRQT by going into the PMQuadTree.
	/* ============================================================================================================= */
	public boolean checkCityInBound(City c) {
		if (
				0 <= c.getRemoteX() &&
				c.getRemoteX() < remoteWidth &&
				0 <= c.getRemoteY() && 
				c.getRemoteY() < remoteHeight &&
				0 <= c.getLocalX() &&
				c.getLocalX() <= metropoleWidth &&
				0 <= c.getLocalY() &&
				c.getLocalY() <= metropoleHeight				
			)  {
			return true;
		}
		return false;
	}
	/* ============================================================================================================= */
	/* Checks if the road is already mapped.
	/* ============================================================================================================= */
	public boolean checkRoadIsMapped(Road r) {
		if (r == null) return false;
		Metropole m = this.rootNode.getMetropole(r.getRemoteX(), r.getRemoteY());
		if (m == null) return false;
		TreeSet<Road> roadList = m.getRoads();
		Road r2 = r.getReversedCopy();
		return roadList.contains(r) || roadList.contains(r2);
	}
	/* ============================================================================================================= */
	/* Checks if the road intersect another road.
	/* ============================================================================================================= */
	public boolean roadIntersectAnother(Road r) {
		Metropole m = this.rootNode.getMetropole(r.getRemoteX(), r.getRemoteY());
		if (m == null) return false;
		TreeSet<Road> roadList = m.getRoads();
		Iterator<Road> iter = roadList.iterator();
		while (iter.hasNext()) {
			Road curr = iter.next();
			if (curr.intersectsLine(r) && !curr.connectedTo(r)) {
				return true;
			} else if (curr.connectedTo(r)) {
				City common = Road.findCommonCity(r, curr);
				double dist1 = r.distanceBetween();
				double dist2 = curr.distanceBetween();
				if (dist2 < dist1) {
					if (curr.getStartName().equals(common.getName())) {
						City e = curr.getEndCity();
						float x = e.getLocalX();
						float y = e.getLocalY();
						if (r.intersectsLine(x, y, x, y)) return true;
					} else if (r.getEndName().equals(common.getName())) {
						City e = curr.getStartCity();
						float x = e.getLocalX();
						float y = e.getLocalY();
						if (r.intersectsLine(x, y, x, y)) return true;
					}
				} else {
					if (r.getStartName().equals(common.getName())) {
						City e = r.getEndCity();
						float x = e.getLocalX();
						float y = e.getLocalY();
						if (curr.intersectsLine(x, y, x, y)) return true;
					} else if (r.getEndName().equals(common.getName())) {
						City e = r.getStartCity();
						float x = e.getLocalX();
						float y = e.getLocalY();
						if (curr.intersectsLine(x, y, x, y)) return true;
					}
				}
				
			}
		}
		TreeSet<City> list = m.getCities();
		Iterator<City> iter2 = list.iterator();
		while (iter2.hasNext()) {
			City curr = iter2.next();
			if (!r.IsEither(curr)) {
				double x = curr.getLocalX();
				double y = curr.getLocalY();
				if (r.intersectsLine(x, y, x, y)) return true;
			}
		}
		
		return false;
	}
	/* ============================================================================================================= */
	/* Add a specified city in the PRQT by going into the PMQuadTree.
	/* ============================================================================================================= */	
	public void addCity(City city) {
		if (!rootNode.hasCity(city)) rootNode = rootNode.addCity(city, quadrants);
	}
	/* ============================================================================================================= */
	/* Remove a specified city in the PRQT by going into the PMQuadTree.
	/* ============================================================================================================= */	
	public void removeCity(City city) {
		TreeSet<Road> roadList = new TreeSet<Road>();
		roadList.addAll(city.getRoadList());
		Iterator<Road> iter = roadList.iterator();
		while (iter.hasNext()) {
			Road r = iter.next();
			rootNode = rootNode.removeRoad(r);
		}
		rootNode = rootNode.removeCity(city);
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public void addRoad(Road r) {
		rootNode = rootNode.addRoad(r, quadrants);
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public void removeRoad(Road r) {
		rootNode = rootNode.removeRoad(r);
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public void addAirport(Airport airport) {
		if (!rootNode.hasAirport(airport)) rootNode = rootNode.addAirport(airport, quadrants);
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public void removeAirport(Airport possible) {
		rootNode = rootNode.removeAirport(possible);		
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public void addTerminal(Terminal terminal) {
		if (!rootNode.hasTerminal(terminal)) rootNode = rootNode.addTerminal(terminal, quadrants);
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public void removeTerminal(Terminal t) {
		rootNode = rootNode.removeTerminal(t);	
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public boolean checkDuplicateAirportName(String name) {
		TreeSet<Airport> list = rootNode.getAirport();
		Iterator<Airport> iter = list.iterator();
		while (iter.hasNext()) {
			Airport curr = iter.next();
			if (curr.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public boolean checkAirportOutOfBounds(Airport airport) {
		if (
				0 <= airport.getRemoteX() &&
				airport.getRemoteX() < remoteWidth &&
				0 <= airport.getRemoteY() && 
				airport.getRemoteY() < remoteHeight &&
				0 <= airport.getLocalX() &&
				airport.getLocalX() <= metropoleWidth &&
				0 <= airport.getLocalY() &&
				airport.getLocalY() <= metropoleHeight				
			)  {
			return false;
		}
		return true;
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public boolean airportViolatesPMRules(Airport airport) {
		Metropole m = rootNode.getMetropole(airport.getRemoteX(), airport.getRemoteY());
		if (m == null) m = new Metropole(airport.getRemoteX(), airport.getRemoteY(), 
				metropoleWidth, metropoleHeight, pmOrder);
		return m.airportViolatesPMRules(airport);
	}
	/* ============================================================================================================= */
	/* Checks if the road intersect another road.
	/* ============================================================================================================= */
	public boolean roadViolatesPMRules(Road r, Dictionary library) {
		City a = r.getStartCity();
		City b = r.getEndCity();
		boolean aMap = false, bMap = false;
		Metropole m = rootNode.getMetropole(r.getRemoteX(), r.getRemoteY());
		if (m == null) m = new Metropole(r.getRemoteX(), r.getRemoteY(), metropoleWidth, metropoleHeight, pmOrder);
		if (!library.insideMap.contains(a)) {
			if (m.cityViolatesPMRules(a)) return true;
			m.addCity(a);
			library.insideMap.add(a);
			aMap = true;
		}
		if (!library.insideMap.contains(b)) {
			if (m.cityViolatesPMRules(b)) {
				if (aMap) {
					m.deleteCity(a);
					library.insideMap.remove(a);
				}
				return true;
			}
			m.addCity(b);
			library.insideMap.add(b);
			bMap = true;
		}
		boolean violation = m.roadViolatesPMRules(r);
		if (violation) {
			if (aMap) {
				m.deleteCity(a);
				library.insideMap.remove(a);
			}
			if (bMap) {
				m.deleteCity(b);
				library.insideMap.remove(b);
			}
		}
		return violation;
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public boolean terminalViolatesPMRules(Terminal terminal) {
		Metropole m = rootNode.getMetropole(terminal.getTerminalRemoteX(), terminal.getTerminalRemoteY());
		if (m == null) m = new Metropole(terminal.getTerminalRemoteX(), terminal.getTerminalRemoteY(), 
				metropoleWidth, metropoleHeight, pmOrder);
		Road r = new Road(terminal, terminal.getTerminalCity());
		if (m.roadViolatesPMRules(r)) return true;
		return m.terminalViolatesPMRules(terminal);
	}
	/* ============================================================================================================= */
	/* 
	/* ============================================================================================================= */
	public int getNumberVertices() {
		return this.rootNode.getNumberVertices();
	}
	
	
	
		

}
