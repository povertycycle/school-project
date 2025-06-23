package cmsc420.meeshquest.part3;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.Angles;
import cmsc420.sortedmap.Angles.Directions;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.sortedmap.AvlGTree.AVLGNode;
import cmsc420.sortedmap.Dijkstra;
import cmsc420.sortedmap.PMNode;
import cmsc420.sortedmap.PMQuadtree;
import cmsc420.sortedmap.PMQuadtree3;
import cmsc420.sortedmap.Road;

public class XMLHandler {
	/* ============================================================================================================= */
	/* XML documentation for createCity
	/* ============================================================================================================= */	
	public static Element createCity(Document results, Dictionary list, String name, String color, String radius, 
			String localX, String localY, String remoteX, String remoteY, String id, AirportList airports) {

		/* The City remote and local X and Y coordinates. */
		Float localXVal = Float.parseFloat(localX);
		Float localYVal = Float.parseFloat(localY);
		Float remoteXVal = Float.parseFloat(remoteX);
		Float remoteYVal = Float.parseFloat(remoteY);
			
		/* Local and remote coordinates. */
		Point2D.Float coorLocal = new Point2D.Float(localXVal, localYVal);
		Point2D.Float coorRemote = new Point2D.Float(remoteXVal, remoteYVal);
		
		Element elt = null, output = null;
		
		/* Checks if the City already exists by coordinates. */
		if (
				list.checkDuplicateCoordinates(coorRemote, coorLocal) ||
				airports.checkDuplicateCoordinate(coorRemote, coorLocal)
			) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateCityCoordinates");
		}
		
		/* Otherwise checks if the City already exists by name. */
		else if (
				list.checkDuplicateName(name) || 
				airports.checkDuplicateName(name) 
			) { 
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateCityName");
		} 
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}

		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "createCity");
		command.setAttribute("id", id);
		
		Element nameCity = results.createElement("name");
		Element xLocalCity = results.createElement("localX");
		Element yLocalCity = results.createElement("localY");
		Element xRemoteCity = results.createElement("remoteX");
		Element yRemoteCity = results.createElement("remoteY");
		Element radiusCity = results.createElement("radius");
		Element colorCity = results.createElement("color");
		parameter.appendChild(nameCity);
		parameter.appendChild(xLocalCity);
		parameter.appendChild(yLocalCity);
		parameter.appendChild(xRemoteCity);
		parameter.appendChild(yRemoteCity);
		parameter.appendChild(radiusCity);
		parameter.appendChild(colorCity);
		nameCity.setAttribute("value", name);
		xLocalCity.setAttribute("value", localX);
		yLocalCity.setAttribute("value", localY);
		xRemoteCity.setAttribute("value", remoteX);
		yRemoteCity.setAttribute("value", remoteY);
		radiusCity.setAttribute("value", radius);
		colorCity.setAttribute("value", color);
		
		if (output != null) {
			elt.appendChild(output);
		}
		
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for listCities.
	/* ============================================================================================================= */	
	public static Element listCities(Document results, Dictionary list, Element commandNode) {
		String type = commandNode.getAttribute("sortBy");
		String id = commandNode.getAttribute("id");
		
		Element elt = null, output = null;
		
		/* Checks if the Dictionary is empty. */
		if (list.isEmpty()) {
			elt = results.createElement("error");
			elt.setAttribute("type", "noCitiesToList");
		}
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");
		
		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "listCities");
		command.setAttribute("id", id);
		
		Element sortBy = results.createElement("sortBy");
		parameter.appendChild(sortBy);
		sortBy.setAttribute("value", type);
		
		Element cityList = results.createElement("cityList");
		if (output!= null && cityList != null) {
			output.appendChild(cityList);
		}
		
		if (list.isEmpty() == false) {
			/* Sort by name. */
			if (type.equals("name")) {
				SortedMap<String, City> sortedNames = list.cityList;
				Iterator<City> iterator = sortedNames.values().iterator();
				while (iterator.hasNext()) {
					City current = iterator.next();
					Element city = results.createElement("city");
					city.setAttribute("name", current.getName());
					city.setAttribute("localX", Integer.toString((int) current.getLocalX()));
					city.setAttribute("localY", Integer.toString((int) current.getLocalY()));
					city.setAttribute("remoteX", Integer.toString((int) current.getRemoteX()));
					city.setAttribute("remoteY", Integer.toString((int) current.getRemoteY()));
					city.setAttribute("color", current.getColor());
					city.setAttribute("radius", current.getRadius());
					cityList.appendChild(city);
				}
				
			} else if (type.equals("coordinate")) {
				/* Sort by coordinate. */
				SortedSet<City> sortedCoor = list.coorList;
				Iterator<City> iterator = sortedCoor.iterator();
				while (iterator.hasNext()) {
					City current = iterator.next();
					Element city = results.createElement("city");
					city.setAttribute("name", current.getName());
					city.setAttribute("localX", Integer.toString((int) current.getLocalX()));
					city.setAttribute("localY", Integer.toString((int) current.getLocalY()));
					city.setAttribute("remoteX", Integer.toString((int) current.getRemoteX()));
					city.setAttribute("remoteY", Integer.toString((int) current.getRemoteY()));
					city.setAttribute("color", current.getColor());
					city.setAttribute("radius", current.getRadius());			
					cityList.appendChild(city);
				}
			}
		}
		
		if (output != null) {
			elt.appendChild(output);
		}
		
		return elt;
		
	}
	/* ============================================================================================================= */
	/* XML documentation for clearAll.
	/* ============================================================================================================= */	
	public static Element clearAll(Document results, String id) {
		
		Element	elt = results.createElement("success");
		Element command = results.createElement("command");
		Element parameters = results.createElement("parameters");
		Element output = results.createElement("output");
		elt.appendChild(command);
		elt.appendChild(parameters);
		elt.appendChild(output);
		command.setAttribute("name", "clearAll");
		command.setAttribute("id", id);
		
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for mapCity.
	/* ============================================================================================================= */	
	public static Element mapCity(Document results, Dictionary list, String name, PRQT remoteMap, String id) {
		City newCity = list.cityList.get(name);
		
		Element elt = null, output = null;
		/* Checks if the city to be mapped is not present in dictionary. */
		if (list.checkDuplicateName(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "nameNotInDictionary");
		} 
		
		/* Checks if the city is already mapped. */
		else if (list.checkMapped(name)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityAlreadyMapped");
		}
		
		/* Checks if the city is out of bounds to be mapped. */
		else if (!remoteMap.checkCityInBound(newCity)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityOutOfBounds");
		}
		
		/* Otherwise there is no error. */
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "mapCity");
		command.setAttribute("id", id);
		
		Element nameCity = results.createElement("name");
		parameter.appendChild(nameCity);
		nameCity.setAttribute("value", name);
		
		if (output != null) {
			elt.appendChild(output);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for unmapCity.
	/* ============================================================================================================= */	
	public static Element unmapCity(Document results, Dictionary list, String name, PRQT region, String id) {
		
		Element elt = null, output = null;
		
		/* Checks if the name is in dictionary. */
		if (list.checkDuplicateName(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "nameNotInDictionary");
		} 
		
		/* Otherwise checks if the city is mapped. */
		else if (list.checkMapped(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityNotMapped");
		}
		
		/* Otherwise there is no error. */
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "unmapCity");
		command.setAttribute("id", id);
		
		Element nameCity = results.createElement("name");
		parameter.appendChild(nameCity);
		nameCity.setAttribute("value", name);
		
		if (output != null) {
			City possibleCity = list.cityList.get(name);
			Element cityUnmapped = results.createElement("cityUnmapped");
			if (list.checkMapped(name)) {
				output.appendChild(cityUnmapped);
			}
			if (possibleCity != null) {
				cityUnmapped.setAttribute("name", name);
				cityUnmapped.setAttribute("localX", Integer.toString((int) possibleCity.getLocalX()));
				cityUnmapped.setAttribute("localY", Integer.toString((int) possibleCity.getLocalY()));
				cityUnmapped.setAttribute("remoteX", Integer.toString((int) possibleCity.getRemoteX()));
				cityUnmapped.setAttribute("remoteY", Integer.toString((int) possibleCity.getRemoteY()));
				cityUnmapped.setAttribute("color", possibleCity.getColor());
				cityUnmapped.setAttribute("radius", possibleCity.getRadius());
			}	
			/* Get the list of Cities connected to the City to be deleted. */
			TreeSet<Road> roadList = possibleCity.getRoadList();
			if (
					roadList != null && 
					!roadList.isEmpty()
				) {
				SortedSet<Road> sorted = roadList;
				Iterator<Road> iter = sorted.iterator();
				while (iter.hasNext()) {
					Road r = iter.next();
					Element roadUnmapped = results.createElement("roadUnmapped");
					roadUnmapped.setAttribute("start", r.getStartName());
					roadUnmapped.setAttribute("end", r.getEndName());
					output.appendChild(roadUnmapped);
				}
			}
			elt.appendChild(output);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for deleteCity.
	/* ============================================================================================================= */	
	public static Node deleteCity(Document results, Dictionary list, String name, PRQT remoteMap, String id) {
		
		Element elt = null, output = null;
		
		/* Checks if the city exists. */
		if (list.checkDuplicateName(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityDoesNotExist");
		} 
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}

		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "deleteCity");
		command.setAttribute("id", id);
		
		Element nameCity = results.createElement("name");
		parameter.appendChild(nameCity);
		nameCity.setAttribute("value", name);
		
		/* Possible City to be deleted. */
		City c = list.cityList.get(name);
		City possibleCity = null;
		Metropole m = null;
		if (c != null) m = remoteMap.getMetropole(c.getRemoteX(), c.getRemoteY());
		if (m != null && m.hasCity(c)) possibleCity = list.getInsideMap(name);
		if (output != null) {
			Element cityUnmapped = results.createElement("cityUnmapped");
			if (possibleCity != null) {
				output.appendChild(cityUnmapped);
				cityUnmapped.setAttribute("name", name);
				cityUnmapped.setAttribute("localX", Integer.toString((int) possibleCity.getLocalX()));
				cityUnmapped.setAttribute("localY", Integer.toString((int) possibleCity.getLocalY()));
				cityUnmapped.setAttribute("remoteX", Integer.toString((int) possibleCity.getRemoteX()));
				cityUnmapped.setAttribute("remoteY", Integer.toString((int) possibleCity.getRemoteY()));
				cityUnmapped.setAttribute("color", possibleCity.getColor());
				cityUnmapped.setAttribute("radius", possibleCity.getRadius());
				/* Get the list of Cities connected to the City to be deleted. */
				TreeSet<Road> roadList = possibleCity.getRoadList();
				
				if (
						roadList != null && 
						!roadList.isEmpty()
					) {
					SortedSet<Road> sorted = roadList;
					Iterator<Road> iter = sorted.iterator();
					while (iter.hasNext()) {
						Road r = iter.next();
						Element roadUnmapped = results.createElement("roadUnmapped");
						roadUnmapped.setAttribute("start", r.getStartName());
						roadUnmapped.setAttribute("end", r.getEndName());
						output.appendChild(roadUnmapped);
					}
				}
			}	
			
			elt.appendChild(output);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for printPRQuadTree.
	/* ============================================================================================================= */	
//	public static Node printPRQuadtree(Document results, Dictionary list, PRQT map, String id) {
//		Element elt = null, output = null;
//		
//		/* Checks if the map is empty. */
//		if (list.insideMap.isEmpty() || map == null || map.root == null || map.root.nodeType() == Type.WHITE) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "mapIsEmpty");
//		} 
//		
//		/* Otherwise there is no error. */
//		if (elt == null) {
//			elt = results.createElement("success");
//			output = results.createElement("output");
//		}
//		
//		Element command = results.createElement("command");
//		Element parameter = results.createElement("parameters");
//
//		elt.appendChild(command);
//		elt.appendChild(parameter);
//		command.setAttribute("name", "printPRQuadtree");
//		/* Optional id. */
//		if (!id.equals("")) {
//			try {
//				Lister.checkErrorNumber(id);
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			}
//			command.setAttribute("id", id);
//		}
//		
//		Element quadtree = results.createElement("quadtree");
//		
//		if (output != null) {
//			elt.appendChild(output);
//			output.appendChild(quadtree);
//			Element node = null;
//			
//			if (map.root.nodeType() == Type.WHITE) {
//				node = results.createElement("white");
//				quadtree.appendChild(node);
//			} else if (map.root.nodeType() == Type.BLACK) {
//				node = results.createElement("black");
//				node.setAttribute("name", map.root.getNodes()[0].getCity().getName());
//				node.setAttribute("x", Integer.toString((int) map.root.getNodes()[0].getCity().x));
//				node.setAttribute("y", Integer.toString((int) map.root.getNodes()[0].getCity().y));
//				quadtree.appendChild(node);
//			} else if (map.root.nodeType() == Type.GRAY) {
//				Point2D.Float midpoint = map.quadrants[4];
//				node = results.createElement("gray");
//				quadtree.appendChild(node);
//				node.setAttribute("x", Integer.toString((int) midpoint.x));
//				node.setAttribute("y", Integer.toString((int) midpoint.y));
//			}
//			
//			if (map.root.nodeType() == Type.GRAY) {
//				int i = 0;
//				cmsc420.meeshquest.part3.MetropoleNode[] outer_fields = map.root.getNodes();
//				while (i < 4) {
//					node.appendChild(recursivePrint(outer_fields[i], results, list));
//					i++;
//				}
//			}
//		}
//		return elt;
//	}
	/* ============================================================================================================= */
	/* Recursive helper for PRQuadTree.
	/* ============================================================================================================= */	
//	public static Node recursivePrint(cmsc420.meeshquest.part3.MetropoleNode root_node, Document results, 
//			Dictionary list) {
//		boolean non_gray = false;
//		
//		Element node = null;
//		/* If the element is white. */
//		if (root_node == null || root_node.nodeType() == Type.WHITE) {
//			node = results.createElement("white");
//			non_gray = true;
//		}
//		/* If the element is black. */
//		else if (root_node.nodeType() == Type.BLACK) {
//			node = results.createElement("black");
//			node.setAttribute("name", root_node.getCity().getName());
//			node.setAttribute("x", Integer.toString((int) root_node.getCity().x));
//			node.setAttribute("y", Integer.toString((int) root_node.getCity().y));
//			non_gray = true;
//		} 
//		/* If the element is gray, recursive print. */
//		else if (root_node.nodeType() == Type.GRAY) {
//			Point2D.Float midpoint = root_node.getQuadrants()[4];
//			node = results.createElement("gray");
//			node.setAttribute("x", Integer.toString((int) midpoint.x));
//			node.setAttribute("y", Integer.toString((int) midpoint.y));
//		}
//		
//		if (non_gray == false) {
//			for (int i = 0; i < 4; i++) {
//				node.appendChild(recursivePrint(root_node.getNodes()[i], results, list));	
//			}	
//		}
//		return node;
//	}
	/* ============================================================================================================= */
	/* XML documentation for saveMap.
	/* ============================================================================================================= */	
	public static Node saveMap(Document results, String save_name, String id, String x, String y) {
		Element elt = results.createElement("success");
		Element output = results.createElement("output");
		Element command = results.createElement("command");
		elt.appendChild(command);
		command.setAttribute("name", "saveMap");
		command.setAttribute("id", id);
		
		Element parameter = results.createElement("parameters");
		elt.appendChild(parameter);
		Element name = results.createElement("name");
		parameter.appendChild(name);
		Element remoteX = results.createElement("remoteX");
		Element remoteY = results.createElement("remoteY");
		parameter.appendChild(remoteX);
		parameter.appendChild(remoteY);
		name.setAttribute("value", save_name);
		remoteX.setAttribute("value", x);
		remoteY.setAttribute("value", y);
		elt.appendChild(output);
		
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for globalRangeCities.
	/* ============================================================================================================= */	
	public static Node globalRangeCities(Document results, TreeSet<City> inRange, String xPoint, String yPoint,
			String radiusCircle, String id) {
		
		Element elt = null, output = null;
		
		/* Checks if there exists city to be printed. */
		if (inRange.isEmpty()) {
			elt = results.createElement("error");
			elt.setAttribute("type", "noCitiesExistInRange");
		} 
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "globalRangeCities");
		command.setAttribute("id", id);
		
		Element x = results.createElement("remoteX");
		Element y = results.createElement("remoteY");
		Element r = results.createElement("radius");
		x.setAttribute("value", xPoint);
		y.setAttribute("value", yPoint);
		r.setAttribute("value", radiusCircle);
		parameter.appendChild(x);
		parameter.appendChild(y);
		parameter.appendChild(r);
		
		
		if (inRange.isEmpty() == false) {
			Element cityList = results.createElement("cityList");
			output.appendChild(cityList);
			
			Iterator<City> iter = inRange.iterator();
			while (iter.hasNext()) {
				City current = iter.next();
				Element city = results.createElement("city");
				city.setAttribute("name", current.getName());
				city.setAttribute("localX", Integer.toString((int) current.getLocalX()));
				city.setAttribute("localY", Integer.toString((int) current.getLocalY()));
				city.setAttribute("remoteX", Integer.toString((int) current.getRemoteX()));
				city.setAttribute("remoteY", Integer.toString((int) current.getRemoteY()));
				city.setAttribute("color", current.getColor());
				city.setAttribute("radius", current.getRadius());					
				cityList.appendChild(city);
			}
		}
		if (output != null) {
			elt.appendChild(output);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for nearestCity.
	/* ============================================================================================================= */	
	public static Node nearestCity(Document results, Dictionary list, String localX, String localY, 
			String remoteX, String remoteY, PRQT map, String id) {
		
		Element elt = null, output = null;
		Metropole m = map.getMetropole(Float.parseFloat(remoteX), Float.parseFloat(remoteY));
		if (m == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityNotFound");
		}
		TreeSet<City> cityList = null;
		City nearest = null;
		if (m != null) {
			cityList = m.getCities();
			nearest = Lister.lookNearest(localX, localY, cityList);
		}
		
		/* Checks if the city exists. */
		if (m != null && (list.insideMap.isEmpty() || nearest == null)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityNotFound");
		} 
		
		/* Otherwise there is no error. */
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "nearestCity");
		command.setAttribute("id", id);
		
		Element xLocal = results.createElement("localX");
		Element yLocal = results.createElement("localY");
		Element xRemote = results.createElement("remoteX");
		Element yRemote = results.createElement("remoteY");
		parameter.appendChild(xLocal);
		parameter.appendChild(yLocal);
		parameter.appendChild(xRemote);
		parameter.appendChild(yRemote);
		xLocal.setAttribute("value", localX);
		yLocal.setAttribute("value", localY);
		xRemote.setAttribute("value", remoteX);
		yRemote.setAttribute("value", remoteY);
		
		if (output != null) {
			elt.appendChild(output);
			if (nearest != null) {
				Element city = results.createElement("city");
				city.setAttribute("name", nearest.getName());
				city.setAttribute("localX", Integer.toString((int) nearest.getLocalX()));
				city.setAttribute("localY", Integer.toString((int) nearest.getLocalY()));
				city.setAttribute("remoteX", Integer.toString((int) nearest.getRemoteX()));
				city.setAttribute("remoteY", Integer.toString((int) nearest.getRemoteY()));
				city.setAttribute("color", nearest.getColor());
				city.setAttribute("radius", nearest.getRadius());
				output.appendChild(city);
			}
		}
		
		
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for printAvlTree.
	/* ============================================================================================================= */		
	public static Node printAvlTree(Document results, AvlGTree<String, Point2D.Float> tree, String id) {
		
		Element elt = null, output = null;
		
		/* Checks if the tree is empty. */
		if (tree == null || tree.root == null || tree.isEmpty()) {
			elt = results.createElement("error");
			elt.setAttribute("type", "emptyTree");
		}
		
		/* Otherwise there is no error. */
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "printAvlTree");
		command.setAttribute("id", id);
		if (output != null) {
			elt.appendChild(output);
			Element avlgtree = results.createElement("AvlGTree");
			output.appendChild(avlgtree);
			avlgtree.setAttribute("cardinality", Integer.toString(tree.size()));
			avlgtree.setAttribute("height", Integer.toString(tree.height()));
			avlgtree.setAttribute("maxImbalance", Integer.toString(tree.g()));
			avlgtree.appendChild(recursiveTreePrint(tree.root, results));
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* Helper for printing AvlGTree.
	/* ============================================================================================================= */	
	public static Node recursiveTreePrint(AVLGNode root_node, Document results) {
		Element elt = null;
		if (root_node != null) {
			AVLGNode left = root_node.getLeft();
			AVLGNode right = root_node.getRight();
			Element node = results.createElement("node");
			node.setAttribute("key", (String) root_node.getKey());
			double x = ((Point2D) root_node.getValue()).getX();
			double y = ((Point2D) root_node.getValue()).getY();
			node.setAttribute("value", "(" + Integer.toString((int) x) + "," + Integer.toString((int) y) + ")");
			node.appendChild(recursiveTreePrint(left, results));
			node.appendChild(recursiveTreePrint(right, results));
			elt = node;
		} else {
			elt = results.createElement("emptyChild");
		}	
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for mapRoad.
	/* ============================================================================================================= */	
	public static Node mapRoad(Document results, PRQT remoteMap, String s, String e, String id, Dictionary library, 
			int remoteSpatialWidth, int remoteSpatialHeight, int localSpatialWidth, int localSpatialHeight) {
		Element elt = null, output = null;
		City start, end;
		start = library.cityList.get(s);
		end = library.cityList.get(e);
		Road r = null;
		if (start != null && end != null) { 
			r = new Road(start, end); 
		}
		if (s.equals("") || !library.checkDuplicateName(s)) {
				elt = results.createElement("error");
				elt.setAttribute("type", "startPointDoesNotExist");
		}
		else if (e.equals("") || !library.checkDuplicateName(e)) {
				elt = results.createElement("error");
				elt.setAttribute("type", "endPointDoesNotExist");
		}
		else if (s.equals(e) && !s.equals("") && !e.equals("")) {
			elt = results.createElement("error");
			elt.setAttribute("type", "startEqualsEnd");
		}
		else if (start != null && end != null && !start.isInSameMetropole(end)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadNotInOneMetropole");
		}
		else if (!remoteMap.checkCityInBound(start) && !remoteMap.checkCityInBound(end)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadOutOfBounds");
		}
		else if (remoteMap.checkRoadIsMapped(r)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadAlreadyMapped");
		}
		else if (remoteMap.roadIntersectAnother(r)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadIntersectsAnotherRoad");
		} 
		else if (remoteMap.roadViolatesPMRules(r, library)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadViolatesPMRules");
		}

		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "mapRoad");
		command.setAttribute("id", id);
		
		Element st = results.createElement("start");
		Element en = results.createElement("end");
		parameter.appendChild(st);
		parameter.appendChild(en);
		st.setAttribute("value", s);
		en.setAttribute("value", e);
	

		if (output != null) {
			elt.appendChild(output);
			Element mapped = results.createElement("roadCreated");
			output.appendChild(mapped);
			mapped.setAttribute("end", e);
			mapped.setAttribute("start", s);
		}
		return elt;
		
	}
	/* ============================================================================================================= */
	/* XML documentation for mapAirport. 
	/* ============================================================================================================= */	
	public static Node mapAirport(Document results, PRQT remoteMap, Element commandNode, 
			Dictionary library, AirportList airports, Airport airport) {
		String name = commandNode.getAttribute("name");
		String localX = commandNode.getAttribute("localX");
		String localY = commandNode.getAttribute("localY");
		String remoteX = commandNode.getAttribute("remoteX");
		String remoteY = commandNode.getAttribute("remoteY");
		String terminalName = commandNode.getAttribute("terminalName");
		String terminalX = commandNode.getAttribute("terminalX");
		String terminalY = commandNode.getAttribute("terminalY");
		String terminalCity = commandNode.getAttribute("terminalCity");
		String id = commandNode.getAttribute("id");
		int localSpatialWidth = remoteMap.getMetropoleX();
		int localSpatialHeight = remoteMap.getMetropoleY();
		int remoteSpatialWidth = remoteMap.getRemoteX();
		int remoteSpatialHeight = remoteMap.getRemoteY();
		float rX = Float.parseFloat(remoteX);
		float rY = Float.parseFloat(remoteY);
		float tX = Float.parseFloat(terminalX);
		float tY = Float.parseFloat(terminalY);
		City city = library.cityList.get(terminalCity);
		Terminal terminal = new Terminal(name, rX, rY, tX, tY, city, name);
		
		Element elt = null, output = null;
		Point2D.Float remote = new Point2D.Float(Float.parseFloat(remoteX), Float.parseFloat(remoteY));
		Point2D.Float local = new Point2D.Float(Float.parseFloat(localX), Float.parseFloat(localY));
		Road r = null;
		if (airport.getTerminalCity() != null && airport.getTerminal().first() != null) {
			r = new Road(airport.getTerminal().first(), airport.getTerminalCity());
		}
		
		if (library.checkDuplicateName(name) || airports.checkDuplicateName(name)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateAirportName");
		}
		else if (library.checkDuplicateCoordinates(remote, local) || airports.checkDuplicateCoordinate(remote, local)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateAirportCoordinates");
		}
		else if (remoteMap.checkAirportOutOfBounds(airport)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "airportOutOfBounds");
		}
		else if (airports.checkTerminalName(terminalName) || library.checkMapped(terminalName)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateTerminalName");
		}
		else if (library.checkDuplicateCoordinates(remote, terminal.getLocalCoordinates()) ||
				airports.checkTerminalCoordinates(remote, terminal.getLocalCoordinates())) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateTerminalCoordinates");
		}
		else if (airports.terminalOutOfBounds(localSpatialWidth, localSpatialHeight, 
				remoteSpatialWidth, remoteSpatialHeight, terminal)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "terminalOutOfBounds");
		}
		else if (!airports.checkConnectingCityExists(airport, library)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "connectingCityDoesNotExist");
		}
		else if (!airports.checkConnectingCityInSameMetropole(airport, library)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "connectingCityNotInSameMetropole");
		}
		else if (remoteMap.airportViolatesPMRules(airport)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "airportViolatesPMRules");
		}
		else if (!airports.checkConnectingIsMapped(airport, library)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "connectingCityNotMapped");
		} 
		else if (remoteMap.terminalViolatesPMRules(terminal)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "terminalViolatesPMRules");
		}
		else if (remoteMap.roadIntersectAnother(r)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadIntersectsAnotherRoad");
		}
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "mapAirport");
		command.setAttribute("id", id);
		Element airportName = results.createElement("name");
		Element airportLocalX = results.createElement("localX");
		Element airportLocalY = results.createElement("localY");
		Element airportRemoteX = results.createElement("remoteX");
		Element airportRemoteY = results.createElement("remoteY");
		Element airportTerminalName = results.createElement("terminalName");
		Element airportTerminalX= results.createElement("terminalX");
		Element airportTerminalY = results.createElement("terminalY");
		Element airportTerminalCity = results.createElement("terminalCity");
		airportName.setAttribute("value", name);
		airportLocalX.setAttribute("value", localX);
		airportLocalY.setAttribute("value", localY);
		airportRemoteX.setAttribute("value", remoteX);
		airportRemoteY.setAttribute("value", remoteY);
		airportTerminalName.setAttribute("value", terminalName);
		airportTerminalX.setAttribute("value", terminalX);
		airportTerminalY.setAttribute("value", terminalY);
		airportTerminalCity.setAttribute("value", terminalCity);
		parameter.appendChild(airportName);
		parameter.appendChild(airportLocalX);
		parameter.appendChild(airportLocalY);
		parameter.appendChild(airportRemoteX);
		parameter.appendChild(airportRemoteY);
		parameter.appendChild(airportTerminalName);
		parameter.appendChild(airportTerminalX);
		parameter.appendChild(airportTerminalY);
		parameter.appendChild(airportTerminalCity);
		
		if (output != null) {
			elt.appendChild(output);
		}
		return elt;
	}
//	/* ============================================================================================================= */
//	/* XML documentatoin for rangeRoads. 
//	/* ============================================================================================================= */	
//	public static Node rangeRoads(Document results, PMQuadtree pmquadtree, String x, String y, String radius, String saveName, String id) {
//		
//		Element elt = null, output = null;
//		
//		ArrayList<Road> inRange = new ArrayList<Road>();
//		Lister.findInRangeRoad(inRange, Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(radius), pmquadtree);
//		
//		if (inRange.isEmpty()) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "noRoadsExistInRange");
//		} 
//		
//		if (elt == null) {
//			elt = results.createElement("success");
//			output = results.createElement("output");
//		}
//		
//		Element command = results.createElement("command");
//		Element parameter = results.createElement("parameters");
//
//		elt.appendChild(command);
//		elt.appendChild(parameter);
//		command.setAttribute("name", "rangeRoads");
//		if (!id.equals("")) {
//			try {
//				Lister.checkErrorNumber(id);
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			}
//			command.setAttribute("id", id);
//		}
//		Element xValue = results.createElement("x");
//		Element yValue = results.createElement("y");
//		Element radiusValue = results.createElement("radius");
//		xValue.setAttribute("value", x);
//		yValue.setAttribute("value", y);
//		radiusValue.setAttribute("value", radius);
//		parameter.appendChild(xValue);
//		parameter.appendChild(yValue);
//		parameter.appendChild(radiusValue);
//		
//		if (saveName.equals("") == false) {
//			Element save_Name = results.createElement("saveMap");
//			save_Name.setAttribute("value", saveName);
//			parameter.appendChild(save_Name);
//		}
//		
//		if (inRange.isEmpty() == false) {
//			Element roads = results.createElement("roadList");
//			output.appendChild(roads);
//					
//			inRange.sort(Comparators.sortRoads);
//			for (int i = 0; i < inRange.size(); i++) {
//				Road current = inRange.get(i);
//				String endName = current.getEndName();
//				String startName = current.getStartName();
//				Element road = results.createElement("road");
//				road.setAttribute("end", endName);
//				road.setAttribute("start", startName);
//				roads.appendChild(road);
//			}
//			
//			output.appendChild(roads);
//		}
//		if (output != null) {
//			elt.appendChild(output);
//		}
//		return elt;
//		
//	}
	/* ============================================================================================================= */
	/* XML documentation for mapTerminal. 
	/* ============================================================================================================= */	
	public static Node mapTerminal(Document results, AirportList airports, Dictionary library, 
			Terminal terminal, PRQT remoteMap, Element commandNode) {
		String name = commandNode.getAttribute("name");
		String localX = commandNode.getAttribute("localX");
		String localY = commandNode.getAttribute("localY");
		String remoteX = commandNode.getAttribute("remoteX");
		String remoteY = commandNode.getAttribute("remoteY");
		String cityName = commandNode.getAttribute("cityName");
		String airportName = commandNode.getAttribute("airportName");
		String id = commandNode.getAttribute("id");
		float lX = Float.parseFloat(localX);
		float lY = Float.parseFloat(localY);
		float rX = Float.parseFloat(remoteX);
		float rY = Float.parseFloat(remoteY);
		Point2D.Float remote = new Point2D.Float(rX, rY);
		Point2D.Float local = new Point2D.Float(lX, lY);
		City city = library.cityList.get(cityName);
		int localSpatialWidth = remoteMap.getMetropoleX();
		int localSpatialHeight = remoteMap.getMetropoleY();
		int remoteSpatialWidth = remoteMap.getRemoteX();
		int remoteSpatialHeight = remoteMap.getRemoteY();
		
		Airport airport = airports.getAirport(airportName);
		Road r = null;
		if (city != null) {
			r = new Road(terminal, city);
		}
		
		Element elt = null, output = null;
		
		if (airports.checkTerminalName(name) || library.checkDuplicateName(name)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateTerminalName");
		} 
		else if (airports.checkTerminalCoordinates(remote, local) || library.checkDuplicateCoordinates(remote, local)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateTerminalCoordinates");
		} 
		else if (airports.terminalOutOfBounds(localSpatialWidth, localSpatialHeight, 
				remoteSpatialWidth, remoteSpatialHeight, terminal)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "terminalOutOfBounds");
		} 
		else if (airports.getAirport(airportName) == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "airportDoesNotExist");
		} 
		else if (!airport.inSameMetropoleAsTerminal(terminal)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "airportNotInSameMetropole");
		} 
		else if (!library.checkDuplicateName(cityName)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "connectingCityDoesNotExist");
		} 
		else if (!terminal.checkCityInSameMetropole(city)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "connectingCityNotInSameMetropole");
		} 
		else if (!library.checkMapped(cityName)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "connectingCityNotMapped");
		} 
		else if (remoteMap.terminalViolatesPMRules(terminal)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "terminalViolatesPMRules");
		} 
		else if (remoteMap.roadIntersectAnother(r)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadIntersectsAnotherRoad");
		}
			
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "mapTerminal");
		command.setAttribute("id", id);
		
		Element terminalName = results.createElement("name");
		Element terminalLocalX = results.createElement("localX");
		Element terminalLocalY = results.createElement("localY");
		Element terminalRemoteX = results.createElement("remoteX");
		Element terminalRemoteY = results.createElement("remoteY");
		Element terminalCityName = results.createElement("cityName");
		Element terminalAirportName = results.createElement("airportName");
		terminalName.setAttribute("value", name);
		terminalLocalX.setAttribute("value", localX);
		terminalLocalY.setAttribute("value", localY);
		terminalRemoteX.setAttribute("value", remoteX);
		terminalRemoteY.setAttribute("value", remoteY);
		terminalCityName.setAttribute("value", cityName);
		terminalAirportName.setAttribute("value", airportName);
		parameter.appendChild(terminalName);
		parameter.appendChild(terminalLocalX);
		parameter.appendChild(terminalLocalY);
		parameter.appendChild(terminalRemoteX);
		parameter.appendChild(terminalRemoteY);
		parameter.appendChild(terminalCityName);
		parameter.appendChild(terminalAirportName);
		
		if (output != null) {
			elt.appendChild(output);
		}

		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for unmapRoad. 
	/* ============================================================================================================= */	
	public static Node unmapRoad(Document results, PRQT remoteMap, String start, String end, 
			String id, Dictionary library) {
		
		City s = library.cityList.get(start);
		City e = library.cityList.get(end);
		Road r = null;
		if (s != null && e != null) r = new Road(s, e);
		
		Element elt = null, output = null;
		
		
		
		
		if (s == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "startPointDoesNotExist");
		} 
		else if (e == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "endPointDoesNotExist");
		} 
		else if (s != null && e != null && s.equals(e)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "startEqualsEnd");
		} 
		else if (!remoteMap.checkRoadIsMapped(r)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadNotMapped");
		} 

		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "unmapRoad");
		command.setAttribute("id", id);
		Element st = results.createElement("start");
		Element en = results.createElement("end");
		st.setAttribute("value", start);
		en.setAttribute("value", end);
		parameter.appendChild(st);
		parameter.appendChild(en);
		
		if (output != null) {
			Element roadDeleted = results.createElement("roadDeleted");
			roadDeleted.setAttribute("start", start);
			roadDeleted.setAttribute("end", end);
			elt.appendChild(output);
			output.appendChild(roadDeleted);
		}
		return elt;	
	}
	/* ============================================================================================================= */
	/* XML documentation for unmapAirport.
	/* ============================================================================================================= */	
	public static Node unmapAirport(Document results, PRQT remoteMap, Element commandNode, AirportList airports) {
		Element elt = null, output = null;
		String name = commandNode.getAttribute("name");
		String id = commandNode.getAttribute("id");
		
		Airport airport = airports.getAirport(name);
		
		if (airport == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "airportDoesNotExist");
		}
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "unmapAirport");
		command.setAttribute("id", id);
		
		Element n = results.createElement("name");
		n.setAttribute("value", name);
		parameter.appendChild(n);
		
		if (output != null) {
			TreeSet<Terminal> terminals = airport.getTerminal();
			Iterator<Terminal> iter = terminals.iterator();
			while (iter.hasNext()) {
				Terminal curr = iter.next();
				Element terminalUnmapped = results.createElement("terminalUnmapped");
				terminalUnmapped.setAttribute("airportName", curr.airportOrigin());
				terminalUnmapped.setAttribute("name", curr.getTerminalName());
				terminalUnmapped.setAttribute("cityName", curr.getTerminalCity().getName());
				terminalUnmapped.setAttribute("localX", Integer.toString((int) curr.getTerminalLocalX()));
				terminalUnmapped.setAttribute("localY", Integer.toString((int) curr.getTerminalLocalY()));
				terminalUnmapped.setAttribute("remoteX", Integer.toString((int) curr.getTerminalRemoteX()));
				terminalUnmapped.setAttribute("remoteY", Integer.toString((int) curr.getTerminalRemoteY()));
				
				output.appendChild(terminalUnmapped);
			}
			elt.appendChild(output);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for unmapTerminal. 
	/* ============================================================================================================= */	
	public static Node unmapTerminal(Document results, PRQT remoteMap, Element commandNode, AirportList airports) {
		Element elt = null, output = null;
		String name = commandNode.getAttribute("name");
		String id = commandNode.getAttribute("id");
		
		Terminal terminal = airports.getTerminal(name);
		
		if (terminal == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "terminalDoesNotExist");
		}
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "unmapTerminal");
		command.setAttribute("id", id);
		
		Element n = results.createElement("name");
		n.setAttribute("value", name);
		parameter.appendChild(n);
		
		if (output != null) {
			Airport a = airports.mappedAirports.get(terminal.airportOrigin());
			if (a.getTerminal().isEmpty()) {
				Element airportUnmaped = results.createElement("airportUnmapped");
				airportUnmaped.setAttribute("name", a.getName());
				output.appendChild(airportUnmaped);
			}	
			elt.appendChild(output);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for printPMQuadtree. 
	/* ============================================================================================================= */	
	public static Node printPMQuadtree(Document results, PRQT remoteMap, String id, 
			String order, Element commandNode) {

		Element elt = null, output = null;
		String remoteX = commandNode.getAttribute("remoteX");
		String remoteY = commandNode.getAttribute("remoteY");
		float rX = Float.parseFloat(remoteX);
		float rY = Float.parseFloat(remoteY);
		Metropole m = remoteMap.getMetropole(rX, rY);
		
		if (0 > rX || rX >= remoteMap.getRemoteX() || 0 > rY || rY >= remoteMap.getRemoteY()) {
			elt = results.createElement("error");
			elt.setAttribute("type", "metropoleIsOutOfBounds");
		}
		else if (m == null || m.getMetropole() == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "metropoleIsEmpty");
		} 
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "printPMQuadtree");
		command.setAttribute("id", id);
		Element x = results.createElement("remoteX");
		Element y = results.createElement("remoteY");
		x.setAttribute("value", remoteX);
		y.setAttribute("value", remoteY);
		parameter.appendChild(x);
		parameter.appendChild(y);

		Element quadtree = results.createElement("quadtree");
		quadtree.setAttribute("order", order);
		
		if (output != null) {
			elt.appendChild(output);
			output.appendChild(quadtree);
			Element node = null;
			PMQuadtree pmquadtree = m.getMetropole();
			
			if (pmquadtree.rootIsBlack()) {
				node = results.createElement("black");
				node.setAttribute("cardinality", Integer.toString(pmquadtree.getRoot().getSize()));
				quadtree.appendChild(node);
				blackHelperNode(pmquadtree.getRoot(), results, pmquadtree, node);
			} else if (pmquadtree.rootIsGray()) {
				Point2D.Float midpoint = pmquadtree.getMidpoint();
				node = results.createElement("gray");
				quadtree.appendChild(node);
				node.setAttribute("x", Integer.toString((int) midpoint.x));
				node.setAttribute("y", Integer.toString((int) midpoint.y));
			}
			
			if (pmquadtree.rootIsGray()) {
				int i = 0;
				PMNode[] children = pmquadtree.getRoot().getChildren();
				while (i < 4) {
					node.appendChild(recursivePMQTPrint(children[i], results, pmquadtree));
					i++;
				}
			}
			
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* Helper for printing PMQuadtree. 
	/* ============================================================================================================= */	
	public static Node recursivePMQTPrint(PMNode children, Document results, PMQuadtree pmquadtree) {
		boolean non_gray = false;
		
		Element node = null;
		
		if (children.getNodeType().isWhite()) {
			node = results.createElement("white");
			non_gray = true;
		} else if (children.getNodeType().isBlack()) {
			node = results.createElement("black");
			node.setAttribute("cardinality", Integer.toString(children.getSize()));
			blackHelperNode(children, results, pmquadtree, node);
			
			non_gray = true;
		} else if (children.getNodeType().isGray()) {
			Point2D.Float midpoint = children.getMidpoint();
			node = results.createElement("gray");
			node.setAttribute("x", Integer.toString((int) midpoint.x));
			node.setAttribute("y", Integer.toString((int) midpoint.y));
		}
		if (non_gray == false) {
			for (int i = 0; i < 4; i++) {
				node.appendChild(recursivePMQTPrint(children.getChildren()[i], results, pmquadtree));	
			}	
		}
		return node;
	}
	/* ============================================================================================================= */
	/* Helper when PMQuadtree has a black node.
	/* ============================================================================================================= */	
	public static void blackHelperNode(PMNode pmnode, Document results, PMQuadtree pmquadtree, Element node) {
		if (pmnode.getNodeType().isBlack()) {
			TreeSet<City> cities = pmnode.getCities();
			TreeSet<Road> roads = pmnode.getRoads();
			TreeSet<Airport> airports = pmnode.getAirports();
			TreeSet<Terminal> terminals = pmnode.getTerminals();
			
			if (cities != null && !cities.isEmpty()) {
				Element geo = null;
				City a = cities.first();
				geo = results.createElement("city");
				geo.setAttribute("color", a.getColor());
				geo.setAttribute("localX", Integer.toString((int) a.getLocalX()));
				geo.setAttribute("localY", Integer.toString((int) a.getLocalY()));
				geo.setAttribute("name", a.getName());
				geo.setAttribute("radius", a.getRadius());
				geo.setAttribute("remoteX", Integer.toString((int) a.getRemoteX()));
				geo.setAttribute("remoteY", Integer.toString((int) a.getRemoteY()));
				node.appendChild(geo);
			} else if (airports != null && !airports.isEmpty()) {
				Element geo = null;
				Airport a = airports.first();
				geo = results.createElement("airport");
				geo.setAttribute("localX", Integer.toString((int) a.getLocalX()));
				geo.setAttribute("localY", Integer.toString((int) a.getLocalY()));
				geo.setAttribute("name", a.getName());
				geo.setAttribute("remoteX", Integer.toString((int) a.getRemoteX()));
				geo.setAttribute("remoteY", Integer.toString((int) a.getRemoteY()));
				node.appendChild(geo);
			} else if (terminals != null && !terminals.isEmpty()) {
				Element geo = null;
				Terminal a = terminals.first();
				geo = results.createElement("terminal");
				geo.setAttribute("airportName", a.airportOrigin());
				geo.setAttribute("cityName", a.getTerminalCity().getName());
				geo.setAttribute("localX", Integer.toString((int) a.getTerminalLocalX()));
				geo.setAttribute("localY", Integer.toString((int) a.getTerminalLocalY()));
				geo.setAttribute("name", a.getTerminalName());
				geo.setAttribute("remoteX", Integer.toString((int) a.getTerminalRemoteX()));
				geo.setAttribute("remoteY", Integer.toString((int) a.getTerminalRemoteY()));
				node.appendChild(geo);
			}
			Iterator<Road> iter = roads.descendingIterator();
			while (iter.hasNext()) {
				Element geo = null;
				Road a = iter.next();
				geo = results.createElement("road");
				geo.setAttribute("end", a.getEndName());
				geo.setAttribute("start", a.getStartName());
				node.appendChild(geo);
			}		
		}
	}	
	/* ============================================================================================================= */
	/* XML documentation for nearestIsolatedCity. 
	/* ============================================================================================================= */	
//	public static Node nearestIsolatedCity(Document results, Dictionary library, String id, String x, String y) {
//
//		Element elt = null, output = null;
//		City nearest = Lister.lookNearestIsolated(library.insideMap, x, y);
//		
//		/* Checks if the city exists. */
//		if (nearest == null) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "cityNotFound");
//		} 
//		
//		/* Otherwise there is no error. */
//		if (elt == null) {
//			elt = results.createElement("success");
//			output = results.createElement("output");
//		}
//		
//		Element command = results.createElement("command");
//		Element parameter = results.createElement("parameters");
//
//		elt.appendChild(command);
//		elt.appendChild(parameter);
//		command.setAttribute("name", "nearestIsolatedCity");
//		if (!id.equals("")) {
//			try {
//				Lister.checkErrorNumber(id);
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			}
//			command.setAttribute("id", id);
//		}
//		Element xPoint = results.createElement("x");
//		Element yPoint = results.createElement("y");
//		parameter.appendChild(xPoint);
//		parameter.appendChild(yPoint);
//		xPoint.setAttribute("value", x);
//		yPoint.setAttribute("value", y);
//		
//		if (output != null) {
//			elt.appendChild(output);
//			if (nearest != null) {
//				Element city = results.createElement("isolatedCity");
//				city.setAttribute("name", nearest.getName());
//				city.setAttribute("x", Integer.toString((int) nearest.x));
//				city.setAttribute("y", Integer.toString((int) nearest.y));
//				city.setAttribute("color", nearest.getColor());
//				city.setAttribute("radius", nearest.getRadius());
//				output.appendChild(city);
//			}
//		}
//		
//		
//		return elt;
//	}
	/* ============================================================================================================= */
	/* XML documentation for nearestRoad. 
	/* ============================================================================================================= */	
//	public static Node nearestRoad(Document results, PMQuadtree pmquadtree, String id, String x, String y) {
//		Element elt = null, output = null;
//		
//		Road nearest = Lister.lookNearestRoad(pmquadtree, x, y);
//		
//		if (nearest == null) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "roadNotFound");
//		} 
//		
//		/* Otherwise there is no error. */
//		if (elt == null) {
//			elt = results.createElement("success");
//			output = results.createElement("output");
//		}
//		
//		Element command = results.createElement("command");
//		Element parameter = results.createElement("parameters");
//
//		elt.appendChild(command);
//		elt.appendChild(parameter);
//		command.setAttribute("name", "nearestRoad");
//		if (!id.equals("")) {
//			try {
//				Lister.checkErrorNumber(id);
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			}
//			command.setAttribute("id", id);
//		}
//		Element xPoint = results.createElement("x");
//		Element yPoint = results.createElement("y");
//		parameter.appendChild(xPoint);
//		parameter.appendChild(yPoint);
//		xPoint.setAttribute("value", x);
//		yPoint.setAttribute("value", y);
//		
//		if (output != null) {
//			elt.appendChild(output);
//			if (nearest != null) {
//				Element road = results.createElement("road");
//				road.setAttribute("end", nearest.getEndName());
//				road.setAttribute("start", nearest.getStartName());
//				output.appendChild(road);
//			}
//		}
//		
//		return elt;
//	}
	/* ============================================================================================================= */
	/* XML documentation for nearestCityToRoad. 
	/* ============================================================================================================= */	
//	public static Node nearestCityToRoad(Document results, PMQuadtree pmquadtree, String id, String start, String end, Dictionary library) {
//		Element elt = null, output = null;
//		Geo2D road = null;
//		City nearest = Lister.lookNearestCityToRoad(pmquadtree, start, end, library);
//		if (!start.equals("") && !end.equals("")) {
//			City s = library.cityList.get(start);
//			City e = library.cityList.get(end);
//			Road r = null;
//			if (s != null && e != null) {
//				r = new Road(s, e);
//				road = new Geo2D(r);
//			}
//		}
//		if (road == null || pmquadtree.containsCityOrRoad(road) == false) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "roadIsNotMapped");
//		} else if (nearest == null) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "noOtherCitiesMapped");
//		}
//		/* Otherwise there is no error. */
//		if (elt == null) {
//			elt = results.createElement("success");
//			output = results.createElement("output");
//		}
//		
//		Element command = results.createElement("command");
//		Element parameter = results.createElement("parameters");
//
//		elt.appendChild(command);
//		elt.appendChild(parameter);
//		command.setAttribute("name", "nearestCityToRoad");
//		if (!id.equals("")) {
//			try {
//				Lister.checkErrorNumber(id);
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			}
//			command.setAttribute("id", id);
//		}
//		Element startPoint = results.createElement("start");
//		Element endPoint = results.createElement("end");
//		parameter.appendChild(startPoint);
//		parameter.appendChild(endPoint);
//		startPoint.setAttribute("value", start);
//		endPoint.setAttribute("value", end);
//		
//		if (output != null) {
//			elt.appendChild(output);
//			if (nearest != null) {
//				Element city = results.createElement("city");
//				city.setAttribute("name", nearest.getName());
//				city.setAttribute("x", Integer.toString((int) nearest.x));
//				city.setAttribute("y", Integer.toString((int) nearest.y));
//				city.setAttribute("color", nearest.getColor());
//				city.setAttribute("radius", nearest.getRadius());
//				output.appendChild(city);
//			}
//		}
//		
//		return elt;
//	}
//	public static Node shortestPath(Document results, PMQuadtree pmquadtree, String id, Dictionary library, 
//			String start, String end, String saveMap, String saveHTML) {
//		Element elt = null, output = null;
//		/* d. */
//		TreeMap<City, Double> distanceToSource = new TreeMap<City, Double>();
//		/* Pi. */
//		TreeMap<City, City> origin = new TreeMap<City, City>();
//		/* weights. */
//		TreeMap<Road, Double> weights = new TreeMap<Road, Double>();
//		Iterator iter = pmquadtree.getRoot().getGeometries().iterator();
//		while (iter.hasNext()) {
//			Geo2D curr = (Geo2D) iter.next();
//			if (curr.isRoad()) {
//				Road r = curr.getRoad();
//				weights.put(r, r.distanceBetween());
//			}			
//		}
//		if (!start.equals("") && library.checkMapped(start) && !end.equals("") && library.checkMapped(end)) {
//			City source = library.cityList.get(start);
//			City fin = library.cityList.get(end);
//			if (pmquadtree.getConnections().containsSource(source) && pmquadtree.getConnections().containsSource(fin))
//			Dijkstra.dijkstra(pmquadtree.getConnections(), weights, source, distanceToSource, origin);
//		}		
//		if (start.equals("") || !library.checkMapped(start)) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "nonExistentStart");
//		} else if (end.equals("") || !library.checkMapped(end)) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "nonExistentEnd");
//		} 
//		
//		City source = library.cityList.get(start);
//		City fin = library.cityList.get(end);
//		ArrayList<City> route = new ArrayList<City>();
//		if (source != null && fin != null && 
//				pmquadtree.getConnections().containsSource(source) && pmquadtree.getConnections().containsSource(fin)) {
//			route = Dijkstra.process(source, fin, weights, distanceToSource, origin, pmquadtree.getConnections());
//		}
//		if (source != null && fin != null && source.getName().equals(fin.getName())) {
//			route.add(fin);				
//			distanceToSource.put(fin, 0d);
//		}		
//		if (elt == null && (route.isEmpty() || distanceToSource.get(fin) == Double.POSITIVE_INFINITY)) {
//			elt = results.createElement("error");
//			elt.setAttribute("type", "noPathExists");
//		} 
//		if (elt == null) {
//			elt = results.createElement("success");
//			output = results.createElement("output");
//		}
//
//		Element command = results.createElement("command");
//		Element parameter = results.createElement("parameters");
//
//		elt.appendChild(command);
//		elt.appendChild(parameter);
//		command.setAttribute("name", "shortestPath");
//		if (!id.equals("")) {
//			try {
//				Lister.checkErrorNumber(id);
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			}
//			command.setAttribute("id", id);
//		}
//		Element startPoint = results.createElement("start");
//		Element endPoint = results.createElement("end");
//		parameter.appendChild(startPoint);
//		parameter.appendChild(endPoint);
//		startPoint.setAttribute("value", start);
//		endPoint.setAttribute("value", end);
//		if (!saveMap.equals("")) {
//			Element saveMap2 = results.createElement("saveMap");
//			saveMap2.setAttribute("value", saveMap);
//			parameter.appendChild(saveMap2);
//		}
//		if (!saveHTML.equals("")) {
//			Element saveHTML2 = results.createElement("saveHTML");
//			saveHTML2.setAttribute("value", saveHTML);
//			parameter.appendChild(saveHTML2);
//		}
//		
//		if (output != null) {
//			elt.appendChild(output);
//			Element path = results.createElement("path");
//			output.appendChild(path);
//			double distance = distanceToSource.get(library.cityList.get(end));
//			path.setAttribute("length", String.format("%.3f", distance));
//			if (source.getName().equals(fin.getName())) path.setAttribute("hops", "0");
//			else path.setAttribute("hops", Integer.toString(route.size()));
//			if (!source.getName().equals(fin.getName())) {
//				Element begin = results.createElement("road");
//				begin.setAttribute("start", start);
//				begin.setAttribute("end", route.get(0).getName());
//				path.appendChild(begin);
//				City a = library.cityList.get(start);
//				City prevB = a;
//				for (int i = 0; i < route.size() - 1; i++) {
//					a = prevB;
//					City b = library.cityList.get(route.get(i).getName());
//					City c = library.cityList.get(route.get(i+1).getName());
//					Directions dir = Angles.getDirections(a, b, c);
//					if (dir == Directions.LEFT) {
//						path.appendChild(results.createElement("left"));
//					} else if (dir == Directions.RIGHT) {
//						path.appendChild(results.createElement("right"));
//					} else if (dir == Directions.STRAIGHT) {
//						path.appendChild(results.createElement("straight"));
//					}
//					Element roadstart = results.createElement("road");
//					roadstart.setAttribute("start", b.getName());
//					roadstart.setAttribute("end", c.getName());
//					path.appendChild(roadstart);
//					prevB = b;
//				}
//			}
//		}
//		
//		return elt;
//	}
	public static Node mst(Document results, PRQT remoteMap, String id, Element commandNode) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
