package cmsc420.meeshquest.part1;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

public class Lister {

	public static void findInRange(TreeMap<String, City> inRange, 
			float x, float y, float radius, Dictionary list) {
		
		Iterator<City> itr = list.insideMap.iterator();
		while (itr.hasNext()) {
			City current = itr.next();
			Float xCity = current.x;
			Float yCity = current.y;
			String name = current.name;
			double euclidean = Math.sqrt(Math.pow((xCity - x), 2) + Math.pow((yCity - y), 2));
			if (euclidean <= (double) radius) {
				inRange.put(name, current);
			} 
		}
	}

	public static City lookNearest(TreeSet<City> insideMap, String xPoint, String yPoint, TreeMap<String, City> list) {
		City nearest = null;
		String name = "";
		double min = 1000;
		
		Iterator<City> itr = insideMap.iterator();
		while (itr.hasNext()) {
			City current = itr.next();
			Float xCity = current.x;
			Float yCity = current.y;
			double euclidean = Math.sqrt(Math.pow((xCity - Float.parseFloat(xPoint)), 2) + 
					Math.pow((yCity - Float.parseFloat(yPoint)), 2));
			if (min > euclidean) {
				name = current.name;
				min = euclidean;
			} else if (min == euclidean) {
				if (name.compareTo(current.name) > 1) {
					name = current.name;
					min = euclidean;
				}	
			}
		}
		nearest = list.get(name);
		return nearest;
	}
	
	public static void checkErrorPattern(String name) throws ParserConfigurationException {
		String patterns = "([_a-zA-Z][_a-zA-Z0-9]*)";
		if (name.matches(patterns) == false) {
			throw new ParserConfigurationException();
		}
	}

	public static void checkErrorNumber(String numbers) throws ParserConfigurationException {
		String patterns = "(0|(-?[1-9][0-9]*))";
		if (numbers.matches(patterns) == false) {
			throw new ParserConfigurationException();
		}
		
	}
	
	
}
