package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import cmsc420.sortedmap.Road;

public class Dictionary {
	/* ============================================================================================================= */
	/* List of cities sorted in reverse asciibetical order.
	/* List of coordinates sorted in y1 to y2 then x1 to x2 order or their local coordinates.
	/* List of cities that are mapped into the PMQuadtree and PRQuadtree.
	/* ============================================================================================================= */	
	TreeMap<String, City> cityList = new TreeMap<String, City>(Comparators.sortNames);
	TreeSet<City> coorList = new TreeSet<City>(Comparators.sortCoordinates);
	TreeSet<City> insideMap = new TreeSet<City>(Comparators.sortCoordinates);
	/* ============================================================================================================= */
	/* Create a city and insert it into the list of cities and list of coordinates. No duplicates.
	/* ============================================================================================================= */	
	public void createCity(City newCity) {
		Point2D.Float local = newCity.getLocal();
		Point2D.Float remote = newCity.getRemote();
		if (checkDuplicateCoordinates(remote, local) == false) {
			if (checkDuplicateName(newCity.getName()) == false) {
				cityList.put(newCity.getName(), newCity);
				coorList.add(newCity);
			}
		}
	}
	/* ============================================================================================================= */
	/* Checks if a city already exists with the same name.
	/* ============================================================================================================= */	
	public boolean checkDuplicateName(String name) { 
		return cityList.containsKey(name); 
	}
	/* ============================================================================================================= */
	/* Checks if a city already exists with the same local AND remote coordinates. 
	/* ============================================================================================================= */	
	public boolean checkDuplicateCoordinates(Point2D.Float remoteCoor, Point2D.Float localCoor) { 
		Iterator<City> iter = coorList.iterator();
		while (iter.hasNext()) {
			City current = iter.next();
			if (
					current.getLocalX() == localCoor.getX() && 
					current.getLocalY() == localCoor.getY() && 
					current.getRemoteX() == remoteCoor.getX() && 
					current.getRemoteY() == remoteCoor.getY()
				) {
				return true;
			}
		} return false;
	}
	/* ============================================================================================================= */
	/* Checks if the dictionary is empty or not.
	/* ============================================================================================================= */	
	public boolean isEmpty() {
		if (cityList.isEmpty() && coorList.isEmpty() && insideMap.isEmpty()) return true;
		return false;
	}
	/* ============================================================================================================= */
	/* Clear all method for the dictionary.
	/* ============================================================================================================= */	
	public void clearAll() {
		cityList.clear();
		coorList.clear();
		insideMap.clear();
	}
	/* ============================================================================================================= */
	/* Checks if the city is already mapped. 
	/* ============================================================================================================= */	
	public boolean checkMapped(String name) {
		City getCity = cityList.get(name);
		if (getCity == null) { return false; } 
		else {
			Iterator<City> iter = insideMap.iterator();
			while (iter.hasNext()) {
				City curr = iter.next();
				if (curr.getName().equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	/* ============================================================================================================= */
	/* Delete the specified city in the dictionary. 
	/* ============================================================================================================= */	
	public void deleteCity(City newCity) {
		if (cityList.containsKey(newCity.getName())) {
			cityList.remove(newCity.getName());
			coorList.remove(newCity);
			insideMap.remove(newCity);
		}
	}
	/* ============================================================================================================= */
	/* Delete the specified city in the dictionary. 
	/* ============================================================================================================= */	
	public City getInsideMap(String name) {
		Iterator<City> iter = insideMap.iterator();
		while (iter.hasNext()) {
			City curr = iter.next();
			if (curr.getName().equals(name)) return curr;
		}
		return null;
	}
	public void deleteAllConnectionsTo(City city) {
		Iterator<City> iter = insideMap.iterator();
		while (iter.hasNext()) {
			City curr = iter.next();
			TreeSet<Road> t = curr.getRoadList();
			Iterator<Road> iter2 = t.iterator();
			while (iter2.hasNext()) {
				Road r = iter2.next();
				if (r.IsEither(curr)) iter2.remove();
			}
		}
		
	}
	public void insertRoadToCity(Road r) {
		Iterator<City> iter = insideMap.iterator();
		while (iter.hasNext()) {
			City c = iter.next();
			if (r.IsEither(c)) {
				c.getRoadList().add(r);
			} 
		}
	}
	public void removeRoad(Road r) {
		Iterator<City> iter = insideMap.iterator();
		while (iter.hasNext()) {
			City c = iter.next();
			c.getRoadList().remove(r);
		}
	}
}	
