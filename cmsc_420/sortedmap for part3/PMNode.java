package cmsc420.sortedmap;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeSet;

import cmsc420.meeshquest.part3.City;
import cmsc420.meeshquest.part3.Terminal;
import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.PMQuadtree3.Black;

public interface PMNode {
	/* ============================================================================================================= */
	/* Interface PMNode. 
	/* ============================================================================================================= */
	/* Type of nodes. */
	public enum PMType { 
		WHITE, BLACK, GRAY; 
		public boolean isWhite() {
			return this == WHITE;
		}
		public boolean isBlack() {
			return this == BLACK;
		}
		public boolean isGray() {
			return this == GRAY;
		}
	}
	public PMType getNodeType();
	public Point2D.Float getMidpoint();
	public PMNode[] getChildren();
	public Rectangle[] getRegion();
	public City nearestCity(float x, float y);
	public TreeSet<City> getCities();
	public TreeSet<Road> getRoads();
	public TreeSet<Airport> getAirports();
	public PMNode addCity(City city);
	public PMNode addRoad(Road road);
	public PMNode addAirport(Airport airport);
	public boolean isValid();
	public PMNode removeCity(City city);
	public PMNode removeRoad(Road road);
	public PMNode removeAirport(Airport airport);
	public boolean hasCity(City city);
	public boolean hasRoad(Road road);
	public boolean hasAirport(Airport airport);
	public boolean isNotEmpty();
	public TreeSet<Terminal> getTerminals();
	public PMNode addTerminal(Terminal terminal);
	public PMNode removeTerminal(Terminal terminal);
	public boolean hasTerminal(Terminal terminal);
	public boolean airportViolatesPMRules(Airport airport);
	public boolean isLessThanOneToOne();
	public boolean roadViolatesPMRules(Road r);
	public boolean cityViolatesPMRules(City a);
	public boolean terminalViolatesPMRules(Terminal terminal);
	public int getSize();
	public int getSpatial();
	public TreeSet<City> checkEmptyCity();
	public int citySize();
	public int roadSize();
	public int airportSize();
	public int terminalSize();
	public int getVertices();
}
