package cmsc420.sortedmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Dijkstra {
	/* ============================================================================================================= */
	/* Kruskal's dijkstra psuedocode.
	/* ============================================================================================================= */	
//	public static void dijkstra(AdjacencyList graph, TreeMap<Road, Double> weights, City source, 
//			TreeMap<City, Double> distanceToSource, TreeMap<City, City> origin) {
//		/* V[G] */
//		Set<City> cities = graph.adjacencylist.keySet();
//		Iterator iter = cities.iterator();
//		
//		while (iter.hasNext()) {
//			City current = (City) iter.next();
//			distanceToSource.put(current, Double.POSITIVE_INFINITY);
//			origin.put(current, null);
//		}
//		
//		Set<City> outside = new HashSet<City>();
//		outside.addAll(cities);
//		distanceToSource.put(source, 0d);
//		while (outside.isEmpty() == false) {
//			City u = extractMin(outside, distanceToSource);
//			outside.remove(u);
//			if (u != null) {
//				ArrayList<City> neighbors = graph.adjacencylist.get(u);
//				for (int i = 0; i < neighbors.size(); i++) {
//					City v = neighbors.get(i);
//					if (outside.contains(v) && distanceToSource.get(u) + weights.get(new Road(u, v)) < distanceToSource.get(v)) {
//						Double newValue = distanceToSource.get(u) + weights.get(new Road(u, v));
//						distanceToSource.put(v, newValue);
//						origin.put(v, u);
//					}
//				}	
//			} else {
//				outside.clear();
//			}
//		}		
//	}
//	
//	public static City extractMin(Set outside, TreeMap<City, Double> d) {
//		City least = null;
//		double min = 1000000;
//		Iterator iter = outside.iterator();
//		while (iter.hasNext()) {
//			City curr = (City) iter.next();
//			if (d.get(curr) < min) {
//				least = curr;
//				min = d.get(curr);
//			}
//		}
//		return least;	
//	}
//
//	public static ArrayList<City> process(City start, City end, TreeMap<Road, Double> weights,
//			TreeMap<City, Double> distanceToSource, TreeMap<City, City> origin, AdjacencyList connections) {
//		ArrayList<City> reversed = new ArrayList<City>();
//		ArrayList<City> route = new ArrayList<City>();
//		City current = end;
//		
//		reversed.add(end);
//		
//		while (current != start && current != null) {
//			City currentSource = origin.get(current);
//			reversed.add(currentSource);
//			current = currentSource;		
//		}
//		
//		for (int i = reversed.size() - 2; i >= 0; i--) {
//			route.add(reversed.get(i));
//		}
//		
//		return route;
//	}
//
//	
}
