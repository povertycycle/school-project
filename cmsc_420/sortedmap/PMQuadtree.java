package cmsc420.sortedmap;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.w3c.dom.Element;

import cmsc420.geom.Circle2D;
import cmsc420.geom.Geometry2D;
import cmsc420.meeshquest.part2.City;
import cmsc420.meeshquest.part2.Comparators;;

public class PMQuadtree {
	
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
	/* Root. */
	public PMNode root;
	/* Width and height. */
	public int spatialWidth, spatialHeight;
	/* AdjacencyList */
	AdjacencyList cityRoads;
	AdjacencyList realConnections;
	/* Constructor for PMQuadtree */
	public PMQuadtree(int width, int height) {
		this.spatialWidth = width;
		this.spatialHeight = height;
		this.root = new White(new Point2D.Float(0, 0), spatialWidth, spatialHeight);
		cityRoads = new AdjacencyList(this.spatialWidth, this.spatialHeight);
		realConnections = new AdjacencyList(this.spatialWidth, this.spatialHeight);
	}
	
	/* ============================================================================================================= */
	/* Inner class PMNode. 
	/* ============================================================================================================= */
	public class PMNode {
		/* Fields for PMNode. */
		private Type type;
		/* Constructor. */
		public PMNode(Type type) { this.type = type; }
		/* Get the type of the node. */
		public Type getNodeType() {	return type; }
		/* Add Geometry. */
		public PMNode add(Geo2D g) { throw new UnsupportedOperationException(); }
		/* Get geometries. */
		public TreeSet<Geo2D> getGeometries() { throw new UnsupportedOperationException(); }
		/* Returns unsupported */
		public boolean hasGeo(Geo2D g) { throw new UnsupportedOperationException(); } 
		/* Checks if city is isolated. */
		public boolean isIsolated(Geo2D first) { throw new UnsupportedOperationException(); }
		/* Returns midpoint, */
		public Point2D.Float getMidpoint() { throw new UnsupportedOperationException(); }
		/* Get the children. */
		public PMNode[] getChildren() { throw new UnsupportedOperationException(); }
		/* Remove the node. */
		public PMNode remove(Geo2D g) { throw new UnsupportedOperationException(); }
		/* Get the regions. */
		public Rectangle[] getRegion() { throw new UnsupportedOperationException(); }
		/* Nearest City. */
		public City nearestCity(float x, float y) { throw new UnsupportedOperationException(); }
	}
	/* ============================================================================================================= */
	/* White Node inner class.
	/* ============================================================================================================= */	
	class White extends PMNode {
		/* Height & Width */
		int spatialHeight, spatialWidth;
		/* Bottom left corner origin. */
		Point2D.Float origin;
		/* Constructor */
		public White(Point2D.Float origin, int width, int height) { 
			super(Type.WHITE); 
			this.origin = origin;
			this.spatialHeight = height; 
			this.spatialWidth = width; 
		}
		/* WHITE */
		public Type getNodeType() { return Type.WHITE; }
		/* Add the geometry into the node and return a new Black. */
		public PMNode add(Geo2D g) { return new Black(g, this.origin, this.spatialWidth, this.spatialHeight); }
		/* Removes a geometry will throw new error for White node. */ 
		public PMNode remove(Geometry2D g) { throw new UnsupportedOperationException(); }
		/* Validator. */
		public boolean isValid() { return true; }
		/* White does not have geometry. */
		public TreeSet<Geo2D> getGeometries() { return new TreeSet<Geo2D>(); }
		/* Returns false */
		public boolean hasGeo(Geo2D g) { return false; }
		/* Checks if the city is isolated */
		public boolean isIsolated(Geo2D first) { return false;}
		/* Returns midpoint, */
		public Point2D.Float getMidpoint() { throw new UnsupportedOperationException(); }
		/* Get the children. */
		public PMNode[] getChildren() { throw new UnsupportedOperationException(); }
		/* Remove the node. */
		public PMNode remove(Geo2D g) { throw new UnsupportedOperationException(); }
		/* Get region. */
		public Rectangle[] getRegion() { return null; }
		/* Nearest non-isolated city to a point. */
		public City nearestCity(float x, float y) { return null; }
	}
	/* ============================================================================================================= */
	/* Black Node inner class.
	/* ============================================================================================================= */	
	class Black extends PMNode {
		/* List of available geometry. */
		TreeSet<Geo2D> geometry;
		/* Number of cities and roads. */
		int cities, roads;
		/* Width & height. */
		int spatialWidth, spatialHeight;
		/* Bottom left corner origin. */
		Point2D.Float origin;
		/* Constructor. */
		public Black(Geo2D g, Point2D.Float origin, int width, int height) {
			super(Type.BLACK);
			geometry = new TreeSet<Geo2D>();
			this.origin = origin;
			this.spatialWidth = width;
			this.spatialHeight = height;
			if (g.isCity()) {
				cities++;
			} else if (g.isRoad()) {
				roads++;
				cityRoads.addRoad(g.getRoad());
				realConnections.addRealConnections(g.getRoad());
			}
			geometry.add(g);
		}
		public Black(Point2D.Float origin, int width, int height) {
			super(Type.BLACK);
			geometry = new TreeSet<Geo2D>();
			this.origin = origin;
			this.spatialWidth = width;
			this.spatialHeight = height;
		}
		/* Add the geometry into the list of geometry. */	
		public PMNode add(Geo2D g) {
			if (!isOutOfBounds(g)){
				geometry.add(g);
				if (g.isRoad()) {
					roads++;						
					cityRoads.addRoad(g.getRoad());
					realConnections.addRealConnections(g.getRoad());
				} else if (g.isCity()) {
					cities++;
				}
			}
			
			if (this.isValid()) return this;
			else return partition(geometry);
		}
		/* Checks if the node is valid, i.e. contains only one City at maximum. */
		private boolean isValid() {
			return cities < 2;
		}
		/* Removes the geometry from g. */
		public PMNode remove(Geo2D g) {
			geometry.remove(g);
			if (geometry.isEmpty()) return new White(this.origin, this.spatialWidth, this.spatialHeight);
			else return this;
		}
		/* Partition Black node to Grey node that has more than 1 cities. */
		public PMNode partition(TreeSet<Geo2D> list) {
			return new Gray(list, cities, roads, this.spatialWidth, this.spatialHeight, this.origin);
		}
		/* Get node type. */
		public Type getNodeType() { return Type.BLACK; }
		/* Get number of cities and roads. */
		public int getNumberOfCities() { return this.cities; }
		public int getNumberOfRoads() { return this.roads; }
		/* Returns geometries. */
		public TreeSet<Geo2D> getGeometries() { return this.geometry; }
		/* Checks if geometries has the city. */
		public boolean hasGeo(Geo2D g) { 
			if (g.isRoad()) {
				Iterator<Geo2D> iter = geometry.iterator();
				while (iter.hasNext()){
					Geo2D curr = iter.next();
					if (curr.isRoad()) {
						if (curr.road.isSameAs(g.road)) {
							return true;
						}
					}
				}
				return false;
			} else {
				return this.geometry.contains(g);
			}			
		}
		/* Checks if the city is isolated */
		/* Checks if the city is isolated */
		public boolean isIsolated(Geo2D first) {
			if (first.isRoad()) throw new IllegalArgumentException();
			if (!geometry.contains(first)) { return false; }
			City city = first.city;
			if (cityRoads.adjacencylist.get(city) == null) {
				return true;
			}
			else return false;
		}
		/* Get the midpoint. */
		public Point2D.Float getMidpoint() { return this.origin; }
		/* Get the children. */
		public PMNode[] getChildren() { throw new UnsupportedOperationException(); }
		/* Get region. */
		public Rectangle[] getRegion() { return null; }
		/* Get city. */
		public City getCity() { 
			Iterator iter = geometry.iterator();
			City city = null;
			while (iter.hasNext()) {
				Geo2D curr = (Geo2D) iter.next();
				if (curr.isCity()) city = curr.getCity();
			}
			return city;
		}
		/* Nearest City */
		public City nearestCity(float x, float y) { return getCity(); }
	}
	/* ============================================================================================================= */
	/* Gray Node inner class.
	/* ============================================================================================================= */	
	class Gray extends PMNode {
		/* Fields */
		PMNode[] children = new PMNode[4];
		Rectangle[] regions = new Rectangle[4]; 
		Point2D.Float origin;
		Point2D.Float[] origins = new Point2D.Float[4];
		int cities, roads;
		int spatialWidth, spatialHeight;
		/* Constructor. */
		public Gray(TreeSet<Geo2D> list, int cities, int roads, int width, int height, Point2D.Float origin) { 
			super(Type.GRAY);
			this.origin = origin;
			this.cities = cities;
			this.roads = roads;
			this.spatialHeight = height;
			this.spatialWidth = width;
			createOrigins();
			createRegions(this.spatialWidth, this.spatialHeight);
			createChildren();
			Iterator<Geo2D> iter = list.iterator();
			while (iter.hasNext()) {
				Geo2D g = iter.next();
				if (g.isCity()) {
					double x1 = g.city.x;
					double y1 = g.city.y;
					for (int j = 0; j < 4; j++) {
						if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
							children[j] = children[j].add(g); 
						}
					}

				} else if (g.isRoad()) {
					double x1 = g.road.getX1();
					double y1 = g.road.getY1();
					double x2 = g.road.getX2();
					double y2 = g.road.getY2();
					Point2D.Float a = new Point2D.Float((float) x1, (float) y1);
					Point2D.Float b = new Point2D.Float((float) x2, (float) y2);
					Line2D.Float l = new Line2D.Float(a, b);
					for (int j = 0; j < 4; j++) {
						if (l.intersects(regions[j])) { 
							children[j] = children[j].add(g); 
						}
					}
				}

			}			
		}
		/* Helper to initialize the 4 origins of the 4 nodes. */
		private void createOrigins() {
			/* Top left origin. */
			this.origins[0] = new Point2D.Float(origin.x, origin.y + spatialHeight / 2);
			/* Top right origin. */
			this.origins[1] = new Point2D.Float(origin.x + spatialWidth / 2, origin.y + spatialHeight / 2);
			/* Bottom left origin. */
			this.origins[2] = new Point2D.Float(origin.x, origin.y);
			/* Bottom right origin. */
			this.origins[3] = new Point2D.Float(origin.x + spatialWidth / 2, origin.y);		
		}
		/* Helper to create children as white nodes. */
		private void createChildren() {
			for (int i = 0; i < 4; i++) { this.children[i] = new White(origins[i], spatialWidth/2, spatialHeight/2); }
		}
		/* Adds the geometry into the children. */
		private void createRegions(int spatialWidth, int spatialHeight) {
			int xMidpoint = spatialWidth / 2;
			int yMidpoint = spatialHeight / 2;
			for (int i = 0; i < 4; i++) {
				this.regions[i] = new Rectangle((int) origins[i].x, (int) origins[i].y, xMidpoint, yMidpoint);
			}
		}
		/* Get the midpoint. */
		public Point2D.Float getMidpoint() { 
			return new Point2D.Float(origin.x + (float) spatialWidth / 2, origin.y + (float) spatialHeight / 2); 
		}
		/* Add node to gray node. */
		public PMNode add(Geo2D g) {
			
			if (g.isCity()) {
				double x1 = g.city.x;
				double y1 = g.city.y;
				for (int j = 0; j < 4; j++) {
					if (regions[j].intersectsLine(x1, y1, x1, y1)) { 
						this.children[j] = children[j].add(g); 
					}
				}
				
			} else if (g.isRoad()) {
				double x1 = g.road.getX1();
				double y1 = g.road.getY1();
				double x2 = g.road.getX2();
				double y2 = g.road.getY2();
				Point2D.Float a = new Point2D.Float((float) x1, (float) y1);
				Point2D.Float b = new Point2D.Float((float) x2, (float) y2);
				Line2D.Float l = new Line2D.Float(a, b);
				for (int j = 0; j < 4; j++) {
					if (l.intersects(regions[j])) { 
						this.children[j] = children[j].add(g); 
					}
				}
			}
			return this;
		}
		/* Removes the node from the gray node. */
		public PMNode remove(Geo2D g) {
			for (int j = 0; j < 4; j++) {
				if (g.isCity()) {
					double x1 = g.city.x;
					double y1 = g.city.y;
					if (regions[j].intersectsLine(x1, y1, x1, y1)) { return children[j] = children[j].remove(g); }
				} else if (g.isRoad()) {
					double x1 = g.road.getX1();
					double y1 = g.road.getY1();
					double x2 = g.road.getX2();
					double y2 = g.road.getY2();
					Point2D.Float a = new Point2D.Float((float) x1, (float) y1);
					Point2D.Float b = new Point2D.Float((float) x2, (float) y2);
					Line2D.Float l = new Line2D.Float(a, b);
					if (l.intersects(regions[j])) { return children[j] = children[j].remove(g); }
				}
			}
			int whiteNodes = 0;
			int blackNodes = 0;
			int greyNodes = 0;
			PMNode blackNode = null;
			TreeSet<Geo2D> combinations = new TreeSet<Geo2D>();
			for (int i = 0; i < 4; i++) {
				if (children[i].getNodeType() == Type.WHITE) { whiteNodes++; }
				else if (children[i].getNodeType() == Type.BLACK) { blackNodes++; }
				else { greyNodes++; }
			}
			if (blackNodes == 1) {
				for (int i = 0; i < 4; i++) {
					if (children[i].getNodeType() == Type.BLACK) { blackNode = children[i]; }
				}
			}
			if (whiteNodes == 4) { return new White(this.origin, spatialWidth, spatialHeight); }
			if (blackNodes == 1 && whiteNodes == 3) { return blackNode; }
			if (greyNodes != 4) {
				for (int i = 0; i < 4; i++) {
					if (children[i].getNodeType() != Type.WHITE) {
						combinations.addAll(children[i].getGeometries());
					}
				}
			}
			Black b = new Black(this.origin, spatialWidth, spatialHeight);
			Iterator<Geo2D> iter = combinations.iterator();
			while (iter.hasNext()) {
				b.add(iter.next());
			}
			if (b.isValid()) return b;
			else { return this;	}
		
		}
		/* Get all the geometries of the gray nodes. */
		public TreeSet<Geo2D> getGeometries() {
			TreeSet<Geo2D> combinations = new TreeSet<Geo2D>();
			for (int i = 0; i < 4; i++) {
				combinations.addAll(children[i].getGeometries());
			}
			return combinations;
		}
		/* Checks if the nodes contains the city. */
		public boolean hasGeo(Geo2D g) {
			for (int i = 0; i < 4; i++) {
				if (children[i].hasGeo(g)) {
					return true;
				}
			}
			return false;
		}
		/* Checks in every node if the city is isolated. */
		public boolean isIsolated(Geo2D first) {
			for (int i = 0; i < 4; i++) {
				if (children[i].isIsolated(first)) {
					return true;
				}
			}
			return false;
		}
		/* Get the children. */
		public PMNode[] getChildren() { return this.children; }
		/* Get the region. */
		public Rectangle[] getRegion() { return this.regions; }
		
	}
	/* Checks if the Geometry is out of bounds. */
	public boolean isOutOfBounds(Geo2D g) {
		if (g == null) throw new NullPointerException();
		if (g.isCity()) {
			float x = g.city.x;
			float y = g.city.y;
			if (0 <= x && x <= spatialWidth && 0 <= y && y <= spatialHeight) {
				return false;
			} else {
				return true;
			}
		} else if (g.isRoad()) {
			if (g.getRoad().intersects(new Rectangle(0, 0, spatialWidth, spatialHeight))) return false;
			else return true;
		} else {
			throw new IllegalArgumentException();
		}
	}
	/* Checks if the city is isolated */
	public boolean isIsolated(Geo2D first) {
		if (first.isRoad()) throw new IllegalArgumentException();
		return root.isIsolated(first);
	}
	/* Check if road is mapped in the PMQuadtree. */
	public boolean roadIsMapped(Geo2D road) {
		if (road.isCity()) throw new IllegalArgumentException();
		return root.hasGeo(road);
	}
	/* Checks if road list is not empty. */
	public boolean roadListIsNotEmpty(float x, float y, float radius) {
		TreeSet<Geo2D> listOfGeometries = root.getGeometries();
		if (!listOfGeometries.isEmpty()) { 
			Circle2D.Float circle = new Circle2D.Float(x, y, radius);
			Iterator<Geo2D> iter = listOfGeometries.iterator();
			while (iter.hasNext()) {
				Geo2D curr = iter.next();
				if (curr.isRoad()) {
					if (curr.getRoad().intersectCircle(circle)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/* Checks if the root is empty. */
	public boolean isEmpty() { return root == null; }
	/* Checks if the root is White. */
	public boolean isWhite() { return root.getNodeType() == Type.WHITE; }
	/* Checks if the root is Black. */
	public boolean isBlack() { return root.getNodeType() == Type.BLACK; }
	/* Checks if the root is Gray. */
	public boolean isGray() { return root.getNodeType() == Type.GRAY; }
	/* Returns the midpoint. */
	public Point2D.Float getMidpoint() { return new Point2D.Float((float) spatialWidth / 2, (float) spatialHeight / 2); }
	/* Checks if the tree contains specified City or Road. */
	public boolean containsCityOrRoad(Geo2D g) {
		return this.root.hasGeo(g);
	}
	/* Returns the adjacencylist */
	public AdjacencyList getAdjacencyList() { return this.cityRoads; }
	/* Clear */
	public void clear() { this.root = new White(new Point2D.Float(0, 0), spatialWidth, spatialHeight);	}
	/* List of roads connections. */
	public AdjacencyList getConnections() { return this.realConnections; }
	/* Get the regions. */
	public Rectangle[] getRegions() { return this.root.getRegion(); }
}


