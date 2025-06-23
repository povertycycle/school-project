package cmsc420.sortedmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import cmsc420.meeshquest.part2.City;
import cmsc420.meeshquest.part2.Comparators;

public class AdjacencyList {
	TreeMap<City, ArrayList<City>> adjacencylist;
	private int width, height;
	
	public AdjacencyList(int width, int height) {
		adjacencylist = new TreeMap<City, ArrayList<City>>(Comparators.sortCities);
		this.width = width;
		this.height = height;
	}
	
	public void addRoad(Road road) {
		if (adjacencylist.containsKey(road.start)) {
			if (!adjacencylist.get(road.start).contains(road.end)) {
				adjacencylist.get(road.start).add(road.end);
				adjacencylist.get(road.start).sort(Comparators.sortCities);
			}
		}
		else if (adjacencylist.containsKey(road.end)) {
			if (!adjacencylist.get(road.end).contains(road.start)) {
				adjacencylist.get(road.end).add(road.start);
				adjacencylist.get(road.end).sort(Comparators.sortCities);
			}
		} else {
			ArrayList<City> newNeighbors = new ArrayList<City>();
			if (road.start.getName().compareTo(road.end.getName()) > 0) {
				newNeighbors.add(road.start);
				newNeighbors.sort(Comparators.sortCities);
				adjacencylist.put(road.end, newNeighbors);
			} else {
				newNeighbors.add(road.end);
				newNeighbors.sort(Comparators.sortCities);
				adjacencylist.put(road.start, newNeighbors);

			}
			
		}
	}
	
	public boolean isAlreadyMapped(Road road) {
		if (adjacencylist.containsKey(road.start)) {
			ArrayList<City> startNeighbors = adjacencylist.get(road.start);
			if (startNeighbors.contains(road.end)) {
				return true;
			}
		}
		return false;
	}
	
	public TreeMap<City, ArrayList<City>> getList() {
		return this.adjacencylist;
	}
	
	public City[][] toArray() {
		City[][] roadList = new City[adjacencylist.size()][];
		Iterator<City> iter = adjacencylist.keySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			City current = iter.next();
			ArrayList<City> neighbors = adjacencylist.get(current);
			roadList[count] = new City[neighbors.size() + 1];
			roadList[count][0] = current;
			for (int i = 1; i < neighbors.size() + 1; i++) {
				roadList[count][i] = neighbors.get(i - 1);				
			}
			count++;
		}
		return roadList;
	}

	public boolean isEmpty() {
		return this.adjacencylist.isEmpty();
	}

	public Collection<City> keySet() {
		return this.adjacencylist.keySet();
	}

	public ArrayList<City> get(City current) {
		return this.adjacencylist.get(current);
	}
	
	public void addRealConnections(Road road) {
		if (road.insideBoundary(this.width, this.height)) {
			/* Insert start city.*/
			if (this.adjacencylist.containsKey(road.getStartCity()) == false) {
				/* Start city is new to list. */
				ArrayList<City> newNeighbor = new ArrayList<City>();
				newNeighbor.add(road.getEndCity());
				adjacencylist.put(road.getStartCity(), newNeighbor);
			} else {
				/* Start city is already in the list. */
				ArrayList<City> newNeighbors = this.adjacencylist.get(road.getStartCity());
				if (newNeighbors.contains(road.getEndCity()) == false) {
					newNeighbors.add(road.getEndCity());
				}
			}
			/* Insert end city.*/
			if (this.adjacencylist.containsKey(road.getEndCity()) == false) {
				/* Start city is new to list. */
				ArrayList<City> newNeighbor = new ArrayList<City>();
				newNeighbor.add(road.getStartCity());
				adjacencylist.put(road.getEndCity(), newNeighbor);
			} else {
				/* Start city is already in the list. */
				ArrayList<City> newNeighbors = this.adjacencylist.get(road.getEndCity());
				if (newNeighbors.contains(road.getStartCity()) == false) {
					newNeighbors.add(road.getStartCity());
				}
			}
		}		
	}
	
	public boolean containsSource(City source) {
		return this.adjacencylist.containsKey(source);
	}
	
}
