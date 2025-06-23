package cmsc420.sortedmap;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.TreeSet;
import java.util.Iterator;

import cmsc420.meeshquest.part3.AirportList;
import cmsc420.meeshquest.part3.City;
import cmsc420.meeshquest.part3.Terminal;
import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.PMNode.PMType;

public class PMQuadtree1 implements PMQuadtree {
	/* ============================================================================================================= */
	/* Fields.
	/* ============================================================================================================= */	
	public PMNode root;
	public int localSpatialWidth, localSpatialHeight;
	AirportList airportList;
	/* Constructor for PMQuadtree */
	public PMQuadtree1(int width, int height) {
		this.localSpatialWidth = width;
		this.localSpatialHeight = height;
		this.root = new PMQuadtree1.White(new Point2D.Float(0, 0), localSpatialWidth, localSpatialHeight);
		airportList = new AirportList();
	}
	/* ============================================================================================================= */
	/* White Node inner class.
	/* ============================================================================================================= */	
	class White implements PMNode {
		/* ========================================================================================================= */
		/* Fields.
		/* ========================================================================================================= */	
		int localSpatialHeight, localSpatialWidth;
		Point2D.Float origin;
		/* ========================================================================================================= */
		/* Constructor
		/* ========================================================================================================= */	
		public White(Point2D.Float origin, int width, int height) {  
			this.origin = origin;
			this.localSpatialHeight = height; 
			this.localSpatialWidth = width; 
		}
		/* ========================================================================================================= */
		/* Public methods.
		/* ========================================================================================================= */
		public PMType getNodeType() { 
			return PMType.WHITE; 
		}
		public boolean isValid() { 
			return true; 
		}
		public Point2D.Float getMidpoint() { 
			return new Point2D.Float(-1, -1); 
		}
		public PMNode[] getChildren() { 
			return new PMNode[0]; 
		}
		public Rectangle[] getRegion() { 
			return new Rectangle[0]; 
		}
		public boolean isNotEmpty() {
			return false;
		}
		public PMNode returnInsertLocation(Airport airport) {
			float x = airport.getLocalX();
			float y = airport.getLocalY();
			if (0 <= x && x <= this.localSpatialWidth && 0 <= y && y <= this.localSpatialHeight) {
				return this;
			}
			return null;
		}
		public boolean isLessThanOneToOne() {
			return this.localSpatialWidth < 1;
		}
		public int getSpatial() {
			return this.localSpatialHeight;
		}
		/* ========================================================================================================= */
		/* City methods.
		/* ========================================================================================================= */
		public City nearestCity(float x, float y) { 
			return null; 
		}
		public TreeSet<City> getCities() {
			return new TreeSet<City>();
		}
		public PMNode addCity(City city) {
			return new Black(city, this.origin, this.localSpatialWidth, this.localSpatialHeight);
		}
		public PMNode removeCity(City city) {
			return this;
		}
		public boolean hasCity(City city) {
			return false;
		}
		public boolean cityViolatesPMRules(City a) {
			if (isLessThanOneToOne()) return true;
			return false;
		}
		public TreeSet<City> checkEmptyCity() {
			return new TreeSet<City>();
		}
		public int citySize() {
			return 0;
		}
		/* ========================================================================================================= */
		/* Road methods.
		/* ========================================================================================================= */
		public TreeSet<Road> getRoads() {
			return new TreeSet<Road>();
		}
		public PMNode addRoad(Road road) {
			return new Black(road, this.origin, this.localSpatialWidth, this.localSpatialHeight);
		}
		public PMNode removeRoad(Road road) {
			return this;
		}
		public boolean hasRoad(Road road) {
			return false;
		}
		public boolean roadViolatesPMRules(Road r) {
			if (isLessThanOneToOne()) return true;
			return false;
		}
		public int roadSize() {
			return 0;
		}
		/* ========================================================================================================= */
		/* Airport methods.
		/* ========================================================================================================= */
		public TreeSet<Airport> getAirports() {
			return new TreeSet<Airport>();
		}
		public PMNode addAirport(Airport airport) {
			return new Black(airport, this.origin, this.localSpatialWidth, this.localSpatialHeight);
		}
		public PMNode removeAirport(Airport airport) {
			return this;
		}
		public boolean hasAirport(Airport airport) {
			return false;
		}
		public boolean airportViolatesPMRules(Airport airport) {
			if (isLessThanOneToOne()) return true;
			return false;
		}
		public int airportSize() {
			return 0;
		}
		/* ========================================================================================================= */
		/* Terminal methods.
		/* ========================================================================================================= */
		public TreeSet<Terminal> getTerminals() {
			return new TreeSet<Terminal>();
		}
		public PMNode addTerminal(Terminal terminal) {
			return new Black(terminal, this.origin, this.localSpatialWidth, this.localSpatialHeight);
		}
		public PMNode removeTerminal(Terminal terminal) {
			return this;
		}
		public boolean hasTerminal(Terminal terminal) {
			return false;
		}
		public boolean terminalViolatesPMRules(Terminal terminal) {
			if (isLessThanOneToOne()) return true;
			return false;
		}
		public int terminalSize() {
			return 0;
		}
		public int getSize() {
			return 0;
		}
		public int getVertices() {
			return 0;
		}	
		
	}
	/* ============================================================================================================= */
	/* Black Node inner class.
	/* ============================================================================================================= */
	class Black implements PMNode {
		/* ========================================================================================================= */
		/* Fields.
		/* ========================================================================================================= */	
		int cities, roads, airports, terminals;
		int spatialWidth, spatialHeight;
		City city, commonCityForRoads;
		TreeSet<Road> roadList;
		Airport airport;
		Terminal terminal;
		Point2D.Float origin;
		/* ========================================================================================================= */
		/* Constructor
		/* ========================================================================================================= */	
		public Black(City city, Point2D.Float origin, int width, int height) {
			this.origin = origin;
			this.spatialWidth = width;
			this.spatialHeight = height;
			cities++;
			this.city = city;
			commonCityForRoads = city;
			roadList = new TreeSet<Road>();
		}
		public Black(Road road, Point2D.Float origin, int width, int height) {
			this.origin = origin;
			this.spatialWidth = width;
			this.spatialHeight = height;
			roads++;
			roadList = new TreeSet<Road>();
			roadList.add(road);
		}
		public Black(Airport airport, Point2D.Float origin, int width, int height) {
			this.origin = origin;
			this.spatialWidth = width;
			this.spatialHeight = height;
			airports++;
			this.airport = airport;
			roadList = new TreeSet<Road>();
		}
		public Black(Terminal terminal, Point2D.Float origin, int width, int height) {
			this.origin = origin;
			this.spatialWidth = width;
			this.spatialHeight = height;
			terminals++;
			this.terminal = terminal;
			roadList = new TreeSet<Road>();
		}
		public Black(PMNode black, Point2D.Float origin, int width, int height) {
			this.origin = origin;
			this.spatialWidth = width;
			this.spatialHeight = height;
			TreeSet<City> cityList = black.getCities();
			TreeSet<Airport> airportList = black.getAirports();
			TreeSet<Terminal> terminalList = black.getTerminals();
			roadList = new TreeSet<Road>();
			if (!cityList.isEmpty()) {
				cities++;
				this.city = cityList.first();
			}
			else if (!airportList.isEmpty()) {
				airports++;
				this.airport = airportList.first();
			}
			else if (!terminalList.isEmpty()) {
				terminals++;
				this.terminal = terminalList.first();
			}
		}
		/* ========================================================================================================= */
		/* Public methods.
		/* ========================================================================================================= */
		public PMType getNodeType() { 
			return PMType.BLACK; 
		}
		public boolean isValid() { 
			if (cities + airports + terminals > 1) {
				return false;
			}
			return true;
		}
		public Point2D.Float getMidpoint() { 
			return new Point2D.Float(-1, -1); 
		}
		public PMNode[] getChildren() { 
			return new PMNode[0]; 
		}
		public Rectangle[] getRegion() { 
			return new Rectangle[0]; 
		}
		public boolean isNotEmpty() {
			if (cities != 0 || roads != 0 || airports != 0 || roads != 0) return true;
			return false;
		}
		public PMNode returnInsertLocation(Airport airport) {
			float x = airport.getLocalX();
			float y = airport.getLocalY();
			if (0 <= x && x <= this.spatialWidth && 0 <= y && y <= this.spatialHeight) {
				return this;
			}
			return null;
		}
		public boolean isLessThanOneToOne() {
			return this.spatialWidth < 1;
		}
		public int getSpatial() {
			return this.spatialWidth;
		}
		/* ========================================================================================================= */
		/* City methods.
		/* ========================================================================================================= */
		public City nearestCity(float x, float y) { 
			if (city != null) return city;
			return null;
		}
		public TreeSet<City> getCities() {
			TreeSet<City> cityList = new TreeSet<City>();
			if (this.city != null) cityList.add(this.city);
			return cityList;
		}
		public PMNode addCity(City city) {
			if (this.city != null || this.airport != null || this.terminal != null) {
				TreeSet<City> cityList = new TreeSet<City>();
				if (this.city != null) cityList.add(this.city);
				cityList.add(city);
				
				TreeSet<Airport> airportList = new TreeSet<Airport>();
				if (this.airport != null) airportList.add(this.airport);
				
				TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
				if (this.terminal != null) terminalList.add(this.terminal);
				
				return new Gray(cityList, this.roadList, airportList, terminalList, 
						this.spatialWidth, this.spatialHeight, this.origin);
			} else {
				this.city = city;
				Iterator<Road> iter = roadList.iterator();
				TreeSet<City> cityList = new TreeSet<City>();
				if (this.city != null) cityList.add(this.city);
				
				TreeSet<Airport> airportList = new TreeSet<Airport>();
				if (this.airport != null) airportList.add(this.airport);
				
				TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
				if (this.terminal != null) terminalList.add(this.terminal);
				while (iter.hasNext()) {
					Road curr = iter.next();
					if (!curr.IsEither(this.city)) {
						return new Gray(cityList, roadList, airportList, terminalList, 
								this.spatialWidth, this.spatialHeight, this.origin);
					}
				}
				cities++;
				return this;
			}			
		}
		public PMNode removeCity(City city) {
			if (this.city == null) return this;
			if (this.city.getName().equals(city.getName())) {
				this.city = null;
				cities--;
			}
			if (isNotEmpty()) return this;
			return new White(this.origin, this.spatialWidth, this.spatialHeight);
		}
		public boolean hasCity(City city) {
			if (this.city == null) return false;
			return this.city.getName().equals(city.getName());
		}
		public boolean cityViolatesPMRules(City a) {
			if (this.city != null || this.airport != null || this.terminal != null) {
				TreeSet<City> cityList = new TreeSet<City>();
				TreeSet<Airport> airportList = new TreeSet<Airport>();
				if (airport != null) airportList.add(this.airport);
				TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
				if (terminal != null) terminalList.add(this.terminal);
				if (this.city != null) {
					cityList.add(this.city);
				}
				PMNode g = new Gray(cityList, roadList, airportList, terminalList, 
						this.spatialWidth, this.spatialHeight, this.origin);
				return g.cityViolatesPMRules(a);
			} 
			return false;
		}
		public TreeSet<City> checkEmptyCity() {
			TreeSet<City> d = new TreeSet<City>();
			if (this.city != null && this.city.getRoadList().isEmpty()) {
				d.add(this.city);
			}
			return d;
		}
		public int citySize() {
			return cities;
		}
		/* ========================================================================================================= */
		/* Road methods.
		/* ========================================================================================================= */
		public TreeSet<Road> getRoads() {
			return this.roadList;
		}
		public PMNode addRoad(Road road) {
			if (this.city == null) {
				return helperProcessWithoutCity(road);
			} else {
				if (this.commonCityForRoads == null) throw new IllegalStateException();
				return helperProcessWithCity(road);
			}
		}
		private PMNode helperProcessWithCity(Road road) {
			TreeSet<City> cityList = new TreeSet<City>();
			if (this.city != null) cityList.add(this.city);
			
			TreeSet<Airport> airportList = new TreeSet<Airport>();
			if (this.airport != null) airportList.add(this.airport);
			
			TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
			if (this.terminal != null) terminalList.add(this.terminal);
			
			if (road.IsEither(commonCityForRoads)) {
				processRoad(road);
				return this;
			} else {
				processRoad(road);
				return new Gray(cityList, roadList, airportList, terminalList, 
					this.spatialWidth, this.spatialHeight, this.origin);
			}
		}
		private PMNode helperProcessWithoutCity(Road road) {
			TreeSet<City> cityList = new TreeSet<City>();
			if (this.city != null) cityList.add(this.city);
			
			TreeSet<Airport> airportList = new TreeSet<Airport>();
			if (this.airport != null) airportList.add(this.airport);
			
			TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
			if (this.terminal != null) terminalList.add(this.terminal);
						
			if (roadList.size() == 0) {
				processRoad(road);
				return this;
			} else {
				roadList.add(road);
				return new Gray(cityList, roadList, airportList, terminalList, 
						this.spatialWidth, this.spatialHeight, this.origin);
			}
		}
		private void processRoad(Road road) {
			if (this.roadList == null) {
				this.roadList = new TreeSet<Road>();
			}
			this.roadList.add(road);
			roads++;
			if (this.city != null && road.IsEither(this.city)) {
				this.city.addRoad(road);
			}
		}
		public PMNode removeRoad(Road road) {
			Iterator<Road> iter = roadList.iterator();
			City a = road.getStartCity();
			City b = road.getEndCity();
			while (iter.hasNext()) {
				Road r = iter.next();
				if (r.isSameAs(road)) {
					if (this.city != null && this.city.getName().equals(a.getName())) {
						this.city.removeRoad(b);
					}
					if (this.city != null && this.city.getName().equals(b.getName())) {
						this.city.removeRoad(a);
					}
					if (this.city != null && this.city.isAlone()) {
						this.city = null;
						cities--;
					}
					roads--;
					iter.remove();
				}
			}
			if (isNotEmpty()) return this;
			return new White(this.origin, this.spatialWidth, this.spatialHeight);
		}
		public boolean hasRoad(Road road) {
			return this.roadList.contains(road);
		}
		public boolean roadViolatesPMRules(Road r) {
			if (airport != null) {
				float x = airport.getLocalX();
				float y = airport.getLocalY();
				if (r.intersectsLine(x, y, x, y)) return true;
			}
			return false;
		}
		public int roadSize() {
			return roads;
		}
		/* ========================================================================================================= */
		/* Airport methods.
		/* ========================================================================================================= */
		public TreeSet<Airport> getAirports() {
			TreeSet<Airport> airportList = new TreeSet<Airport>();
			if (airport != null) airportList.add(this.airport);
			return airportList;
		}
		public PMNode addAirport(Airport airport) {
			if (this.city != null || this.airport != null || this.terminal != null) {
				TreeSet<City> cityList = new TreeSet<City>();
				if (this.city != null) cityList.add(this.city);
				
				TreeSet<Airport> airportList = new TreeSet<Airport>();
				if (this.airport != null) airportList.add(this.airport);
				airportList.add(airport);
				
				TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
				if (this.terminal != null) terminalList.add(this.terminal);
				
				return new Gray(cityList, roadList, airportList, terminalList, 
						this.spatialWidth, this.spatialHeight, this.origin);
			} else {
				this.airport = airport;
				airports++;
				return this;
			}			
		}
		public PMNode removeAirport(Airport airport) {
			if (this.airport == null) return this;
			if (this.airport.getName().equals(airport.getName())) {
				this.airport = null;
				airports--;
			}
			if (isNotEmpty()) return this;
			return new White(this.origin, this.spatialWidth, this.spatialHeight);
		}
		public boolean hasAirport(Airport airport) {
			if (this.airport == null) return false;
			return airport.getName().equals(this.airport.getName());
		}
		public boolean airportViolatesPMRules(Airport airport) {
			if (this.city != null || this.airport != null || this.terminal != null) {
				TreeSet<City> cityList = new TreeSet<City>();
				if (city != null) cityList.add(this.city);
				TreeSet<Airport> airportList = new TreeSet<Airport>();
				TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
				if (terminal != null) terminalList.add(this.terminal);
				if (this.airport != null) {
					airportList.add(this.airport);		
				}
				PMNode g = new Gray(cityList, roadList, airportList, terminalList, 
						this.spatialWidth, this.spatialHeight, this.origin);
				return g.airportViolatesPMRules(airport);
			} 
			return false;
		}
		public int airportSize() {
			return airports;
		}
		/* ========================================================================================================= */
		/* Terminal methods.
		/* ========================================================================================================= */
		public TreeSet<Terminal> getTerminals() {
			TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
			if (terminal != null) terminalList.add(terminal);
			return terminalList;
		}
		public PMNode addTerminal(Terminal terminal) {
			if (this.city != null || this.airport != null || this.terminal != null) {
				TreeSet<City> cityList = new TreeSet<City>();
				if (this.city != null) cityList.add(this.city);
				
				TreeSet<Airport> airportList = new TreeSet<Airport>();
				if (this.airport != null) airportList.add(this.airport);
				
				TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
				if (this.terminal != null) terminalList.add(this.terminal);
				terminalList.add(terminal);
				
				return new Gray(cityList, roadList, airportList, terminalList, 
						this.spatialWidth, this.spatialHeight, this.origin);
			} else {
				this.terminal = terminal;
				terminals++;
				return this;
			}			
		}
		public PMNode removeTerminal(Terminal terminal) {
			if (this.terminal == null) return this;
			if (this.terminal.getTerminalName().equals(terminal.getTerminalName())) {
				this.terminal = null;
				terminals--;
			}
			if (isNotEmpty()) return this;
			return new White(this.origin, this.spatialWidth, this.spatialHeight);
		}
		public boolean hasTerminal(Terminal terminal) {
			if (this.terminal == null) return false;
			return this.terminal.getTerminalName().equals(terminal.getTerminalName());
		}
		public boolean terminalViolatesPMRules(Terminal terminal) {
			if (this.city != null || this.airport != null || this.terminal != null) {
				TreeSet<City> cityList = new TreeSet<City>();
				if (city != null) cityList.add(this.city);
				TreeSet<Airport> airportList = new TreeSet<Airport>();
				if (airport != null) airportList.add(this.airport);
				TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
				if (this.terminal != null) {
					terminalList.add(this.terminal);
				}
				terminals++;
				terminalList.add(terminal);
				PMNode g = new Gray(cityList, roadList, airportList, terminalList, 
							this.spatialWidth, this.spatialHeight, this.origin);
				g.terminalViolatesPMRules(terminal);

			} 
			return false;
		}
		public int terminalSize() {
			return terminals;
		}
		public int getSize() {
			return cities + airports + roads + terminals;
		}
		public int getVertices() {
			return cities + airports + terminals;
		}
	}
	/* ============================================================================================================= */
	/* Gray Node inner class.
	/* ============================================================================================================= */	
	class Gray implements PMNode {
		/* ========================================================================================================= */
		/* Fields.
		/* ========================================================================================================= */	
		PMNode[] children = new PMNode[4];
		Rectangle[] regions = new Rectangle[4]; 
		Point2D.Float origin;
		Point2D.Float[] origins = new Point2D.Float[4];
		int spatialWidth, spatialHeight;
		/* ========================================================================================================= */
		/* Constructor
		/* ========================================================================================================= */	
		public Gray(TreeSet<City> cityList, TreeSet<Road> roadList, TreeSet<Airport> airportList, 
				TreeSet<Terminal> terminalList, int width, int height, Point2D.Float origin) { 
			this.origin = origin;
			this.spatialHeight = height;
			this.spatialWidth = width;
			createOrigins();
			createRegions(this.spatialWidth, this.spatialHeight);
			createChildren();

			Iterator<City> iterCity = cityList.iterator();
			Iterator<Road> iterRoad = roadList.iterator();
			Iterator<Airport> iterAirport = airportList.iterator();
			Iterator<Terminal> iterTerminal = terminalList.iterator();

			while (iterCity.hasNext()) {
				City c = iterCity.next();
				double x1 = c.getLocalX();
				double y1 = c.getLocalY();
				for (int j = 0; j < 4; j++) {
					if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
						children[j] = children[j].addCity(c); 
					}
				}
			}
			while (iterRoad.hasNext()) {
				Road r = iterRoad.next();
				for (int j = 0; j < 4; j++) {
					if (r.intersectSection(regions[j])) { 
						children[j] = children[j].addRoad(r); 
					}
				}
			}
			while (iterAirport.hasNext()) {
				Airport a = iterAirport.next();
				double x1 = a.getLocalX();
				double y1 = a.getLocalY();
				for (int j = 0; j < 4; j++) {
					if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
						children[j] = children[j].addAirport(a); 
					}
				}
			}
			while (iterTerminal.hasNext()) {
				Terminal t = iterTerminal.next();
				double x1 = t.getTerminalLocalX();
				double y1 = t.getTerminalLocalY();
				for (int j = 0; j < 4; j++) {
					if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
						children[j] = children[j].addTerminal(t); 
					}
				}
			}

		}
		/* ========================================================================================================= */
		/* Public methods.
		/* ========================================================================================================= */
		private void createOrigins() {
			this.origins[0] = new Point2D.Float(origin.x, origin.y + spatialHeight / 2);
			this.origins[1] = new Point2D.Float(origin.x + spatialWidth / 2, origin.y + spatialHeight / 2);
			this.origins[2] = new Point2D.Float(origin.x, origin.y);
			this.origins[3] = new Point2D.Float(origin.x + spatialWidth / 2, origin.y);		
		}
		private void createChildren() {
			for (int i = 0; i < 4; i++) { 
				this.children[i] = new White(origins[i], spatialWidth/2, spatialHeight/2); 
			}
		}
		private void createRegions(int spatialWidth, int spatialHeight) {
			int xMidpoint = spatialWidth / 2;
			int yMidpoint = spatialHeight / 2;
			for (int i = 0; i < 4; i++) {
				this.regions[i] = new Rectangle((int) origins[i].x, (int) origins[i].y, xMidpoint, yMidpoint);
			}
		}
		public PMType getNodeType() { 
			return PMType.GRAY; 
		}
		public boolean isValid() { 
			return true;
		}
		public Point2D.Float getMidpoint() { 
			return new Point2D.Float(origin.x + (float) spatialWidth / 2, origin.y + (float) spatialHeight / 2); 
		}
		public PMNode[] getChildren() { 
			return this.children; 
		}
		public Rectangle[] getRegion() { 
			return this.regions; 
		}
		public boolean isNotEmpty() {
			return children[0].isNotEmpty() || 
					children[1].isNotEmpty() || 
					children[2].isNotEmpty() || 
					children[3].isNotEmpty();
		}
		public boolean isLessThanOneToOne() {
			return this.spatialWidth < 1;
		}
		public int getSpatial() {
			return this.spatialWidth;
		}
		/* ========================================================================================================= */
		/* City methods.
		/* ========================================================================================================= */
		public City nearestCity(float x, float y) { 
			if (this.getMidpoint().getX() != x && this.getMidpoint().getY() != y) {
				for (int i = 0; i < 4; i++) {
					if (regions[i].contains(x, y)) {
						return children[i].nearestCity(x, y);
					}
				}
			}
			return null;
		}
		public TreeSet<City> getCities() {
			TreeSet<City> cityList = new TreeSet<City>();
			for (int i = 0; i < 4; i++) {
				cityList.addAll(children[i].getCities());
			}
			return cityList;
		}
		public PMNode addCity(City c) {
			double x1 = c.getLocalX();
			double y1 = c.getLocalY();
			for (int j = 0; j < 4; j++) {
				if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
					children[j] = children[j].addCity(c); 
				}
			}
			return this;
		}
		public PMNode removeCity(City city) {
			TreeSet<City> cityList = this.getCities();
			TreeSet<Road> roadList = this.getRoads();
			TreeSet<Airport> airportList = this.getAirports();
			TreeSet<Terminal> terminalList = this.getTerminals();

			PMNode white = new White(this.origin, this.spatialWidth, this.spatialHeight);
			
			Iterator<City> iter = cityList.iterator();
			Iterator<Road> iter2 = roadList.iterator();
			Iterator<Airport> iter3 = airportList.iterator();
			Iterator<Terminal> iter4 = terminalList.iterator();

			while (iter.hasNext()) {
				City c = iter.next();
				if (!c.getName().equals(city.getName())) white = white.addCity(c);
			}
			while (iter2.hasNext()) {
				Road r = iter2.next();
				white = white.addRoad(r);
			}
			while (iter3.hasNext()) {
				Airport a = iter3.next();
				white = white.addAirport(a);
			}
			while (iter4.hasNext()) {
				Terminal t = iter4.next();
				white = white.addTerminal(t);
			}
			return white;
		}
		public boolean hasCity(City city) {
			return children[0].hasCity(city) || 
					children[1].hasCity(city) || 
					children[2].hasCity(city) || 
					children[3].hasCity(city);
		}
		public boolean cityViolatesPMRules(City a) {
			double x1 = a.getLocalX();
			double y1 = a.getLocalY();
			boolean[] loc = new boolean[4];
			for (int j = 0; j < 4; j++) {
				if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
					if (children[j].getNodeType() == PMType.BLACK && regions[j].width <= 1) return true;
					loc[j] = children[j].cityViolatesPMRules(a);
				}
			}
			return loc[0] || loc[1] || loc[2] || loc[3];
		}
		public TreeSet<City> checkEmptyCity() {
			TreeSet<City> d = new TreeSet<City>();
			for (int i = 0; i < 4; i ++) {
				d.addAll(children[i].checkEmptyCity());
			}
			return d;
		}
		public int citySize() {
			return children[0].citySize() + children[1].citySize() + children[2].citySize() + children[3].citySize();
		}
		/* ========================================================================================================= */
		/* Road methods.
		/* ========================================================================================================= */
		public TreeSet<Road> getRoads() {
			TreeSet<Road> roadList = new TreeSet<Road>();
			for (int i = 0; i < 4; i++) {
				TreeSet<Road> roads = children[i].getRoads();
				roadList.addAll(roads);
			}
			return roadList;
		}
		public PMNode addRoad(Road r) {
			for (int j = 0; j < 4; j++) {
				if (r.intersectSection(regions[j])) { 
					children[j] = children[j].addRoad(r); 
				}
			}
			return this;
		}
		public PMNode removeRoad(Road road) {
			TreeSet<City> cityList = this.getCities();
			TreeSet<Road> roadList = this.getRoads();
			TreeSet<Airport> airportList = this.getAirports();
			TreeSet<Terminal> terminalList = this.getTerminals();
			City one = road.getStartCity();
			City two = road.getEndCity();
			
			PMNode white = new White(this.origin, this.spatialWidth, this.spatialHeight);
			
			Iterator<City> iter = cityList.iterator();
			Iterator<Road> iter2 = roadList.iterator();
			Iterator<Airport> iter3 = airportList.iterator();
			Iterator<Terminal> iter4 = terminalList.iterator();

			while (iter.hasNext()) {
				City c = iter.next();
				c.getRoadList().remove(road);
				if (c.isAlone() == false) white = white.addCity(c);
			}
			while (iter2.hasNext()) {
				Road r = iter2.next();
				if (!r.isSameAs(road)) white = white.addRoad(r);
			}
			while (iter3.hasNext()) {
				Airport a = iter3.next();
				white = white.addAirport(a);
			}
			while (iter4.hasNext()) {
				Terminal t = iter4.next();
				white = white.addTerminal(t);
			}
			return white;
		}
		public boolean hasRoad(Road road) {
			return children[0].hasRoad(road) || 
					children[1].hasRoad(road) || 
					children[2].hasRoad(road) || 
					children[3].hasRoad(road);
		}
		public boolean roadViolatesPMRules(Road r) {
			double x1 = r.getStartCity().getLocalX();
			double y1 = r.getStartCity().getLocalY();
			double x2 = r.getEndCity().getLocalX();
			double y2 = r.getEndCity().getLocalY();
			Point2D.Float a = new Point2D.Float((float) x1, (float) y1);
			Point2D.Float b = new Point2D.Float((float) x2, (float) y2);
			Line2D.Float l = new Line2D.Float(a, b);
			boolean[] loc = new boolean[4];
			for (int j = 0; j < 4; j++) {
				if (l.intersects(regions[j])) { 
					if (children[j].getNodeType() == PMType.BLACK && regions[j].width <= 1) return true; 
					loc[j] = children[j].roadViolatesPMRules(r);
				}
			}
			return loc[0] || loc[1] || loc[2] || loc[3];
		}
		public int roadSize() {
			return children[0].roadSize() + children[1].roadSize() + children[2].roadSize() + children[3].roadSize();
		}
		/* ========================================================================================================= */
		/* Airport methods.
		/* ========================================================================================================= */
		public TreeSet<Airport> getAirports() {
			TreeSet<Airport> airportList = new TreeSet<Airport>();
			for (int i = 0; i < 4; i++) {
				airportList.addAll(children[i].getAirports());
			}
			return airportList;
		}
		public PMNode addAirport(Airport a) {
			double x1 = a.getLocalX();
			double y1 = a.getLocalY();
			for (int j = 0; j < 4; j++) {
				if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
					children[j] = children[j].addAirport(a); 
				}
			}
			return this;
		}
		public PMNode removeAirport(Airport airport) {
			TreeSet<City> cityList = this.getCities();
			TreeSet<Road> roadList = this.getRoads();
			TreeSet<Airport> airportList = this.getAirports();
			TreeSet<Terminal> terminalList = this.getTerminals();
			
			TreeSet<Terminal> tRemove = airport.getTerminal();
			
			PMNode white = new White(this.origin, this.spatialWidth, this.spatialHeight);
			
			Iterator<City> iter = cityList.iterator();
			Iterator<Road> iter2 = roadList.iterator();
			Iterator<Airport> iter3 = airportList.iterator();
			Iterator<Terminal> iter4 = terminalList.iterator();

			while (iter.hasNext()) {
				City c = iter.next();
				white = white.addCity(c);
			}
			while (iter2.hasNext()) {
				Road r = iter2.next();
				white = white.addRoad(r);
			}
			while (iter3.hasNext()) {
				Airport a = iter3.next();
				if (!a.getName().equals(airport.getName())) white = white.addAirport(a);
			}
			while (iter4.hasNext()) {
				Terminal t = iter4.next();
				if (!tRemove.contains(t)) white = white.addTerminal(t);
			}
			return white;
		}
		public boolean hasAirport(Airport airport) {
			return children[0].hasAirport(airport) || 
					children[1].hasAirport(airport) || 
					children[2].hasAirport(airport) || 
					children[3].hasAirport(airport);
		}
		public boolean airportViolatesPMRules(Airport a) {
			double x1 = a.getLocalX();
			double y1 = a.getLocalY();
			boolean[] loc = new boolean[4];
			for (int j = 0; j < 4; j++) {
				if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
					if (children[j].getNodeType() == PMType.BLACK && regions[j].width <= 1) return true;
					loc[j] = children[j].airportViolatesPMRules(a);
				}
			}
			return loc[0] || loc[1] || loc[2] || loc[3];
		}
		public int airportSize() {
			return children[0].airportSize() + children[1].airportSize() + 
					children[2].airportSize() + children[3].airportSize();
		}
		/* ========================================================================================================= */
		/* Terminal methods.
		/* ========================================================================================================= */
		public TreeSet<Terminal> getTerminals() {
			TreeSet<Terminal> terminalList = new TreeSet<Terminal>();
			for (int i = 0; i < 4; i++) {
				terminalList.addAll(children[i].getTerminals());
			}
			return terminalList;
		}
		public PMNode addTerminal(Terminal t) {
			double x1 = t.getTerminalLocalX();
			double y1 = t.getTerminalLocalY();
			for (int j = 0; j < 4; j++) {
				if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
					children[j] = children[j].addTerminal(t); 
				}
			}
			return this;		
		}
		public PMNode removeTerminal(Terminal terminal) {
			TreeSet<City> cityList = this.getCities();
			TreeSet<Road> roadList = this.getRoads();
			TreeSet<Airport> airportList = this.getAirports();
			TreeSet<Terminal> terminalList = this.getTerminals();
			
			PMNode white = new White(this.origin, this.spatialWidth, this.spatialHeight);
			
			Iterator<City> iter = cityList.iterator();
			Iterator<Road> iter2 = roadList.iterator();
			Iterator<Airport> iter3 = airportList.iterator();
			Iterator<Terminal> iter4 = terminalList.iterator();

			while (iter.hasNext()) {
				City c = iter.next();
				white = white.addCity(c);
			}
			while (iter2.hasNext()) {
				Road r = iter2.next();
				white = white.addRoad(r);
			}
			while (iter3.hasNext()) {
				Airport a = iter3.next();
				white = white.addAirport(a);
			}
			while (iter4.hasNext()) {
				Terminal t = iter4.next();
				if (!t.getTerminalName().equals(terminal.getTerminalName())) white = white.addTerminal(t);
			}
			return white;
		}
		public boolean hasTerminal(Terminal terminal) {
			return children[0].hasTerminal(terminal) || 
					children[1].hasTerminal(terminal) || 
					children[2].hasTerminal(terminal) || 
					children[3].hasTerminal(terminal);
		}
		public boolean terminalViolatesPMRules(Terminal t) {
			double x1 = t.getTerminalLocalX();
			double y1 = t.getTerminalLocalY();
			boolean[] loc = new boolean[4];
			for (int j = 0; j < 4; j++) {
				if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
					if (children[j].getNodeType() == PMType.BLACK && regions[j].width <= 1) return true;
					loc[j] = children[j].terminalViolatesPMRules(t);
				}
			}
			return loc[0] || loc[1] || loc[2] || loc[3];
		}
		public int terminalSize() {
			return children[0].terminalSize() + children[1].terminalSize() + 
					children[2].terminalSize() + children[3].terminalSize();
		}
		public int getSize() {
			return children[0].getSize() + children[1].getSize() + children[2].getSize() + children[3].getSize();
		}
		public int getVertices() {
			return children[0].getVertices() + children[1].getVertices() + 
					children[2].getVertices() + children[3].getVertices();
		}
	}
	/* ============================================================================================================= */
	/* PM Tree public methods.
	/* ============================================================================================================= */	
	public boolean cityIsOutOfBounds(City city) {
		float x = city.getLocalX();
		float y = city.getLocalY();
		if (0 <= x && x <= localSpatialWidth && 0 <= y && y <= localSpatialHeight) {
			return false;
		} else {
			return true;
		}
	}
	public boolean roadIsOutOfBounds(Road road) {
		if (road.intersects(new Rectangle(0, 0, localSpatialWidth, localSpatialHeight))) return false;
		else return true;
	}
	
	public boolean airportIsOutOfBounds(Airport airport) {
		float x = airport.getLocalX();
		float y = airport.getLocalY();
		if (0 <= x && x <= localSpatialWidth && 0 <= y && y <= localSpatialHeight) {
			return false;
		} else {
			return true;
		}
	}
	public boolean terminalIsOutOfBounds(Terminal terminal) {
		float x = terminal.getTerminalLocalX();
		float y = terminal.getTerminalLocalY();
		if (0 <= x && x <= localSpatialWidth && 0 <= y && y <= localSpatialHeight) {
			return false;
		} else {
			return true;
		}
	}
	public boolean rootIsWhite() { 
		return root.getNodeType() == PMType.WHITE; 
	}
	public boolean rootIsBlack() { 
		return root.getNodeType() == PMType.BLACK; 
	}
	public boolean rootIsGray() { 
		return root.getNodeType() == PMType.GRAY; 
	}
	public boolean isEmpty() { 
		return root == null; 
	}
	public boolean hasCity(City city) {
		return this.root.hasCity(city);
	}
	public boolean hasRoad(Road road) {
		return this.root.hasRoad(road);
	}
	public boolean hasAirport(Airport airport) {
		return this.root.hasAirport(airport);
	}
	public boolean hasTerminal(Terminal terminal) {
		return this.root.hasTerminal(terminal);
	}
	public boolean roadListIsNotEmpty() {
		return !this.root.getRoads().isEmpty();
	}
	public Point2D.Float getMidpoint() { 
		return new Point2D.Float((float) localSpatialWidth / 2, (float) localSpatialHeight / 2); 
	}
	public void clear() { 
		this.root = new White(new Point2D.Float(0, 0), localSpatialWidth, localSpatialHeight);	
	}
	public Rectangle[] getRegions() { 
		return this.root.getRegion(); 
	}
	public int getSpatialWidth() { 
		return this.localSpatialWidth; 
	}
	public int getSpatialHeight() { 
		return this.localSpatialHeight;
	}
	public PMNode getRoot() { 
		return this.root; 
	}
	public void deleteCity(City city) {
		this.root = this.root.removeCity(city);
	}
	public void deleteRoad(Road road) {
		this.root = this.root.removeRoad(road);
	}
	public void deleteAirport(Airport airport) {
		this.root = this.root.removeAirport(airport);
	}
	public void deleteTerminal(Terminal terminal) {
		this.root = this.root.removeTerminal(terminal);
	}
	public void addCity(City city) {
		this.root = this.root.addCity(city);
	}
	public void addRoad(Road road) {
		this.root = root.addRoad(road);
	}
	public void addAirport(Airport airport) {
		airportList.getAirportList().put(airport.getName(), airport);
		this.root = root.addAirport(airport);
	}
	public void addTerminal(Terminal terminal) {
		this.root = root.addTerminal(terminal);
	}
	public boolean airportViolatesPMRules(Airport airport) {
		return this.root.airportViolatesPMRules(airport);
	}
	public boolean roadViolatesPMRules(Road r) {
		return this.root.roadViolatesPMRules(r);
	}
	public boolean cityViolatesPMRules(City a) {
		return this.root.cityViolatesPMRules(a);
	}
	public boolean terminalViolatesPMRules(Terminal terminal) {
		return this.root.terminalViolatesPMRules(terminal);
	}
	public int getNumberVertices() {
		return this.root.getVertices();
	}
}


