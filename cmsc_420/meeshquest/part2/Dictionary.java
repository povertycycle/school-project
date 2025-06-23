package cmsc420.meeshquest.part2;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class Dictionary {
	/* ============================================================================================================= */
	/* List of cities sorted in reverse asciibetical order.
	/* List of coordinates sorted in y1 to y2 then x1 to x2 order.
	/* List of cities that are mapped into the PMQuadtree and PRQuadtree.
	/* ============================================================================================================= */	
	TreeMap<String, City> cityList = new TreeMap<String, City>(Comparators.sortNames);
	TreeSet<City> coorList = new TreeSet<City>(Comparators.sortCoordinates);
	TreeSet<City> insideMap = new TreeSet<City>(Comparators.sortCoordinates);
	/* ============================================================================================================= */
	/* Create a city and insert it into the list of cities and list of coordinates. No duplicates.
	/* ============================================================================================================= */	
	public void createCity(City newCity) {
		if (checkDuplicateCoordinates(newCity) == false) {
			if (checkDuplicateName(newCity.getName()) == false) {
				cityList.put(newCity.getName(), newCity);
				coorList.add(newCity);
			}
		}
	}
	/* ============================================================================================================= */
	/* Checks if a city already exists with the same name.
	/* ============================================================================================================= */	
	public boolean checkDuplicateName(String name) { return cityList.containsKey(name); }
	/* ============================================================================================================= */
	/* Checks if a city already exists with the same coordinates. 
	/* ============================================================================================================= */	
	public boolean checkDuplicateCoordinates(City newCity) { 
		Iterator<City> iter = coorList.iterator();
		while (iter.hasNext()) {
			City current = iter.next();
			if (current.x == newCity.x && current.y == newCity.y) {
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
			Iterator iter = insideMap.iterator();
			while (iter.hasNext()) {
				City curr = (City) iter.next();
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
}	
