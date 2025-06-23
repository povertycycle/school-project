package cmsc420.sortedmap;

import cmsc420.meeshquest.part3.City;
import cmsc420.meeshquest.part3.Terminal;
import cmsc420.meeshquest.part3.AirportList.Airport;

public interface PMQuadtree {
	/* Type of nodes. */
	public enum Type { 
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

	void clear();

	int getSpatialWidth();

	int getSpatialHeight();

	PMNode getRoot();
	
	boolean roadListIsNotEmpty();

	boolean isEmpty();

	boolean rootIsBlack();
	
	java.awt.geom.Point2D.Float getMidpoint();
	
	boolean rootIsWhite();

	boolean rootIsGray();
	
	void addCity(City city);
	
	void addRoad(Road road);
	
	void addAirport(Airport airport);
	
	void addTerminal(Terminal terminal);
	
	boolean hasCity(City city);

	boolean hasRoad(Road road);

	boolean hasAirport(Airport airport);
	
	boolean hasTerminal(Terminal terminal);

	void deleteCity(City city);

	void deleteRoad(Road road);

	void deleteAirport(Airport airport);
	
	void deleteTerminal(Terminal terminal);

	boolean airportViolatesPMRules(Airport airport);

	boolean roadViolatesPMRules(Road r);

	boolean cityViolatesPMRules(City a);

	boolean terminalViolatesPMRules(Terminal terminal);

	int getNumberVertices();

}
