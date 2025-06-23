package cmsc420.meeshquest.part2;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import cmsc420.geom.Circle2D;
import cmsc420.sortedmap.Geo2D;
import cmsc420.sortedmap.PMQuadtree;
import cmsc420.sortedmap.Road;

public class Lister {
	/* ============================================================================================================= */
	/* Finds the list of cities that are in range.
	/* ============================================================================================================= */	
	public static void findInRange(TreeMap<String, City> inRange, float x, float y, float radius, Dictionary list) {
		
		Iterator<City> itr = list.insideMap.iterator();
		while (itr.hasNext()) {
			City current = itr.next();
			Float xCity = current.x;
			Float yCity = current.y;
			String name = current.getName();
			double euclidean = Math.sqrt(Math.pow((xCity - x), 2) + Math.pow((yCity - y), 2));
			if (euclidean <= (double) radius) {
				inRange.put(name, current);
			} 
		}
	}
	/* ============================================================================================================= */
	/* Finds the list of roads that are in range.
	/* ============================================================================================================= */	
	public static void findInRangeRoad(ArrayList<Road> inRange, float x, float y, float radius, PMQuadtree pmquadtree) {
		
		Iterator<Geo2D> itr = pmquadtree.root.getGeometries().iterator();
		Circle2D.Float circle = new Circle2D.Float(x, y, radius);
		Rectangle area = new Rectangle(0, 0, pmquadtree.spatialWidth, pmquadtree.spatialHeight);
		while (itr.hasNext()) {
			Geo2D current = itr.next();
			if (current.isRoad()) {
				Road curr = current.getRoad();
				if (curr.ptSegDist((double) x, (double) y) <= radius && 
						curr.intersectSquare(area)) {
					inRange.add(curr);
				}
			} 
		}
	}
	/* ============================================================================================================= */
	/* Get the nearest city based on a Point2D.Float
	/* ============================================================================================================= */	
	public static City lookNearest(TreeSet<City> insideMap, String xPoint, String yPoint, TreeMap<String, City> list) {
		City nearest = null;
		String name = "";
		double min = Double.POSITIVE_INFINITY;
		
		Iterator<City> itr = insideMap.iterator();
		while (itr.hasNext()) {
			City current = itr.next();
			/* City should not be isolated. */
			if (current.getIsolatedOrNot() == false) {
				Float xCity = current.x;
				Float yCity = current.y;
				double euclidean = Math.sqrt(Math.pow((xCity - Float.parseFloat(xPoint)), 2) + 
						Math.pow((yCity - Float.parseFloat(yPoint)), 2));
				if (min > euclidean) {
					name = current.getName();
					min = euclidean;
				} else if (min == euclidean) {
					if (name.compareTo(current.getName()) < 0) {
						name = current.getName();
						min = euclidean;
					}	
				}
			}
		}
		nearest = list.get(name);
		return nearest;
	}
	/* ============================================================================================================= */
	/* Checks for error pattern in the name and will throw parserconfigurationexceptions.
	/* ============================================================================================================= */	
	public static void checkErrorPattern(String name) throws ParserConfigurationException {
		String patterns = "([_a-zA-Z][_a-zA-Z0-9]*)";
		if (name.matches(patterns) == false) {
			throw new ParserConfigurationException();
		}
	}
	/* ============================================================================================================= */
	/* Checks for error pattern in the number and will throw parserconfigurationexceptions. 
	/* ============================================================================================================= */	
	public static void checkErrorNumber(String numbers) throws ParserConfigurationException {
		String patterns = "(0|(-?[1-9][0-9]*))";
		if (numbers.matches(patterns) == false) {
			throw new ParserConfigurationException();
		}
	}
	/* ============================================================================================================= */
	/* Sort the cities in an ArrayList by if the city is isolated or not. Isolated cities comes on top of the list.
	/* ============================================================================================================= */	
	public static void sortByIsolated(ArrayList<City> cities, ArrayList<City> sortedByIsolation) {
		ArrayList<City> isolated = new ArrayList<City>();
		ArrayList<City> notIsolated = new ArrayList<City>();
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).getIsolatedOrNot() == true) {
				isolated.add(cities.get(i));
			} else {
				notIsolated.add(cities.get(i));
			}
		}
		isolated.sort(Comparators.sortCities);
		notIsolated.sort(Comparators.sortCities);
		sortedByIsolation.addAll(notIsolated);
		sortedByIsolation.addAll(isolated);	
	}
	/* ============================================================================================================= */
	/* Looks for the nearest isolated city.
	/* ============================================================================================================= */	
	public static City lookNearestIsolated(TreeSet<City> insideMap, String x, String y) {
		City nearest = null;
		String name = "";
		double min = Double.POSITIVE_INFINITY;
		
		Iterator<City> itr = insideMap.iterator();
		while (itr.hasNext()) {
			City current = itr.next();
			/* City has to be isolated. */
			if (current.getIsolatedOrNot() == true) {
				Float xCity = current.x;
				Float yCity = current.y;
				double euclidean = Math.sqrt(Math.pow((xCity - Float.parseFloat(x)), 2) + 
						Math.pow((yCity - Float.parseFloat(y)), 2));
				if (min > euclidean) {
					name = current.getName();
					min = euclidean;
					nearest = current;
				} else if (min == euclidean) {
					if (name.compareTo(current.getName()) < 0) {
						name = current.getName();
						min = euclidean;
						nearest = current;
					}	
				}
			}
		}
		return nearest;
	}
	/* ============================================================================================================= */
	/* Looks for the nearest road from the point.
	/* ============================================================================================================= */	
	public static Road lookNearestRoad(PMQuadtree pmquadtree, String x, String y) {
		Road nearest = null;
		Point2D.Float point = new Point2D.Float(Float.parseFloat(x), Float.parseFloat(y));
		double min = Double.POSITIVE_INFINITY;
		
		Iterator<Geo2D> iter = pmquadtree.root.getGeometries().iterator();
		
		while(iter.hasNext()) {
			Geo2D curr = iter.next();
			if (curr.isRoad()) {
				Road road = curr.getRoad();
				if (road.ptSegDist(point.getX(), point.getY()) < min) {
					min = road.ptSegDist(point.getX(), point.getY());
					nearest = road;
				} else if (road.ptSegDist(point.getX(), point.getY()) == min) {
					if (road.getStartName().compareTo(nearest.getStartName()) > 0) {
						nearest = road;
					} else if (road.getStartName().compareTo(nearest.getStartName()) == 0) {
						if (road.getEndName().compareTo(nearest.getEndName()) > 0) {
							nearest = road;
						}
					}
				}
			}		
		}
		
		
		
		return nearest;
	}
	/* ============================================================================================================= */
	/* Looks for the nearest city to a road.
	/* ============================================================================================================= */	
	public static City lookNearestCityToRoad(PMQuadtree pmquadtree, String start, String end, Dictionary library) {
		City nearest = null;
		City s = library.cityList.get(start);
		City e = library.cityList.get(end);
		double min = Double.POSITIVE_INFINITY;
		if (s == null || e == null) {
			return null;
		} else {
			Road road = new Road(s, e);
			if (!pmquadtree.containsCityOrRoad(new Geo2D(road))) {
				return null;
			}
			Iterator<Geo2D> iter = pmquadtree.root.getGeometries().iterator();
			while (iter.hasNext()) {
				Geo2D curr = iter.next();
				if (curr.isCity() && !curr.getCity().getName().equals(start) && !curr.getCity().getName().equals(end)) {
					if (road.ptSegDist(curr.getCity().getCoordinates()) < min) {
						min = road.ptSegDist(curr.getCity().getCoordinates());
						nearest = curr.getCity();
					} else if (road.ptSegDist(curr.getCity().getCoordinates()) == min) {
						if (nearest.getName().compareTo(curr.getCity().getName()) < 0) {
							nearest = curr.getCity();
						}
					}
				}
			}
			return nearest;
		}
	}
	/* ============================================================================================================= */
	/* Checks if it is within bound of PMQuadtree.
	/* ============================================================================================================= */	
	public static boolean withinBoundsOfPMQT(float x, float y, int width, int height) {
		if (0 <= x && x <= width && 0 <= y && y <= height) return true;
		else return false;
	}
	
	
}
