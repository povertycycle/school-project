package cmsc420.sortedmap;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.meeshquest.part3.City;
import cmsc420.meeshquest.part3.PRQT;
import cmsc420.meeshquest.part3.Terminal;

public class Prim {

	int minKey(int key[], Boolean[] mstSet, int V) { 
		int min = Integer.MAX_VALUE, min_index = -1; 

		for (int v = 0; v < V; v++) 
			if (mstSet[v] == false && key[v] < min) { 
				min = key[v]; 
				min_index = v; 
			} 
		return min_index; 
	} 

	TreeMap<Vertex, Vertex> primMST(PRQT remoteMap, City startingCity) {
		TreeSet<Vertex> v = new TreeSet<Vertex>();
		TreeMap<Vertex, Boolean> visited = new TreeMap<Vertex, Boolean>();
		TreeMap<Vertex, Vertex> mst = new TreeMap<Vertex, Vertex>();
		if (remoteMap.getMetropoleRoot() != null) {
			TreeSet<City> c = remoteMap.getMetropoleRoot().getCity();
			TreeSet<Airport> a = remoteMap.getMetropoleRoot().getAirport();
			TreeSet<Terminal> t = remoteMap.getMetropoleRoot().getTerminal();
			Iterator<City> iter = c.iterator();
			Iterator<Airport> iter2 = a.iterator();
			Iterator<Terminal> iter3 = t.iterator();
			while(iter.hasNext()) {
				Vertex vertex = new Vertex(iter.next());
				v.add(vertex);
				visited.put(vertex, false);
			}
			while(iter2.hasNext()) {
				Vertex vertex = new Vertex(iter2.next());
				v.add(vertex);
				visited.put(vertex, false);
			}
			while(iter3.hasNext()) {
				Vertex vertex = new Vertex(iter3.next());
				v.add(vertex);
				visited.put(vertex, false);
			}
		}
		
		Vertex current = new Vertex(startingCity);
		mst.put(current, null);
		
		while (v.isEmpty() == false) {
			v.remove(current);
			TreeSet<Vertex> neighbor = new TreeSet<Vertex>();
			if (current.c != null) {
				TreeSet<Road> roadList = current.c.getRoadList();
				Iterator<Road> iter = roadList.iterator();
				while (iter.hasNext()) {
					Road r = iter.next();
					if (r.getStartName().equals(current.c.getName())) {
						Vertex vt = new Vertex(r.getEndCity());
						if (visited.get(vt) == false) neighbor.add(vt);
					} else {
						Vertex vt = new Vertex(r.getStartCity());
						if (visited.get(vt) == false) neighbor.add(vt);
					}
				}
				neighbor.remove(current);
			} else if (current.a != null) {
				TreeSet<Terminal> ts = current.a.getTerminal();
				Iterator<Terminal> iter = ts.iterator();
				while (iter.hasNext()) {
					Vertex vt = new Vertex(iter.next());
					if (visited.get(vt) == false) neighbor.add(vt);
				}
				TreeSet<Airport> as = remoteMap.getMetropoleRoot().getAirport();
				Iterator<Airport> iter2 = as.iterator();
				while (iter2.hasNext()) {
					Vertex vt = new Vertex(iter2.next());
					if (visited.get(vt) == false) neighbor.add(vt);
				}
				neighbor.remove(current);
			} else if (current.t != null) {
				Vertex vt = new Vertex(current.t.getTerminalCity());
				if (visited.get(vt) == false) neighbor.add(vt);
			}
			
			double dist = Double.POSITIVE_INFINITY;
			Vertex shortest = null;
			Iterator<Vertex> iter = neighbor.iterator();
			while (iter.hasNext()) {
				Vertex curr = iter.next();
				if (curr.getDistanceTo(current) < dist) {
					shortest = curr;
				} else if (curr.getDistanceTo(current) == dist) {
					if (shortest.getName().compareTo(curr.getName()) < 0) {
						shortest = curr;
					}
				}
			}
			mst.put(shortest, current);
			current = shortest;
		}

		return mst;

	}
}
