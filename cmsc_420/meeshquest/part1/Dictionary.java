package cmsc420.meeshquest.part1;

import java.util.TreeMap;
import java.util.TreeSet;

public class Dictionary {
	
	// Comparator sort reverse asciibetically.
	TreeMap<String, City> cityList = new TreeMap<String, City>(Comparators.sortNames);

	// Comparator sort based on instruction on pdf.
	TreeSet<City> coorList = new TreeSet<City>(Comparators.sortCoordinates);
	
	// Contains all cities that has been mapped.
	TreeSet<City> insideMap = new TreeSet<City>(Comparators.sortCoordinates);
	

	public void createCity(City newCity) {
		if (checkDuplicateCoordinates(newCity) == false) {
			if (checkDuplicateName(newCity.name) == false) {
				cityList.put(newCity.name, newCity);
				coorList.add(newCity);
			}
		}
	}
	
	public boolean checkDuplicateName(String name) {
		return cityList.containsKey(name);
	}
	
	public boolean checkDuplicateCoordinates(City newCity) {
		return coorList.contains(newCity);
	}
	
	public boolean isEmpty() {
		if (cityList.isEmpty() && coorList.isEmpty() && insideMap.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void clearAll() {
		cityList.clear();
		coorList.clear();
		insideMap.clear();
	}

	public boolean checkMapped(String name) {
		City getCity = cityList.get(name);
		if (getCity == null) {
			return false;
		} else {
			if (insideMap.contains(getCity)) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	public void deleteCity(City newCity) {
		if (cityList.containsKey(newCity.name)) {
			cityList.remove(newCity.name);
			coorList.remove(newCity);
			insideMap.remove(newCity);
		}
	}
}	
