package cmsc420.meeshquest.part2;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cmsc420.geom.Circle2D;
import cmsc420.meeshquest.part2.Node.Type;
import cmsc420.sortedmap.AdjacencyList;
import cmsc420.sortedmap.Angles;
import cmsc420.sortedmap.Angles.Directions;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.sortedmap.AvlGTree.AVLGNode;
import cmsc420.sortedmap.Dijkstra;
import cmsc420.sortedmap.Geo2D;
import cmsc420.sortedmap.PMQuadtree;
import cmsc420.sortedmap.PMQuadtree.PMNode;
import cmsc420.sortedmap.Road;

public class XMLHandler {
	/* ============================================================================================================= */
	/* XML documentation for createCity
	/* ============================================================================================================= */	
	public static Element createCity(Document results, Dictionary list, Element commandNode) {
		/* Variable names from the commandNode. */
		String name = commandNode.getAttribute("name");
		String color = commandNode.getAttribute("color");
		String radius = commandNode.getAttribute("radius");
		String id = commandNode.getAttribute("id");
		
		/* The city x and y coordinate. */
		Float x = Float.parseFloat(commandNode.getAttribute("x"));
		Float y = Float.parseFloat(commandNode.getAttribute("y"));
		City newCity = new City(name, x, y, radius, color, false);
		
		/* Element to be returned. */
		Element elt = null, output = null;
		
		/* Checks if the city already exists by coordinates. */
		if (list.checkDuplicateCoordinates(newCity)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateCityCoordinates");
		}
		
		/* Otherwise checks if the city already exists by name. */
		else if (list.checkDuplicateName(name)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateCityName");
		} 
		
		/* If the element is not null, i.e. it is not error. */
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}

		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "createCity");
		
		/* Optional id. */
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", commandNode.getAttribute("id"));
		}
		Element nameCity = results.createElement("name");
		Element xCity = results.createElement("x");
		Element yCity = results.createElement("y");
		Element radiusCity = results.createElement("radius");
		Element colorCity = results.createElement("color");
		parameter.appendChild(nameCity);
		parameter.appendChild(xCity);
		parameter.appendChild(yCity);
		parameter.appendChild(radiusCity);
		parameter.appendChild(colorCity);
		nameCity.setAttribute("value", commandNode.getAttribute("name"));
		xCity.setAttribute("value", commandNode.getAttribute("x"));
		yCity.setAttribute("value", commandNode.getAttribute("y"));
		radiusCity.setAttribute("value", commandNode.getAttribute("radius"));
		colorCity.setAttribute("value", commandNode.getAttribute("color"));
		
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
		
		/* Checks if the list is empty. */
		if (list.isEmpty()) {
			elt = results.createElement("error");
			elt.setAttribute("type", "noCitiesToList");
		}
		
		/* Otherwise if element is null, i.e no error. */
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");
		
		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "listCities");
		/* Optional id. */
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element sortBy = results.createElement("sortBy");
		parameter.appendChild(sortBy);
		sortBy.setAttribute("value", type);
		Element cityList = results.createElement("cityList");
		if (output!= null && cityList != null) {
			output.appendChild(cityList);
		}
		
		if (list.isEmpty() == false) {
			if (type.equals("name")) {
				SortedMap<String, City> sortedNames = list.cityList;
				Iterator<City> iterator = sortedNames.values().iterator();
				while (iterator.hasNext()) {
					City current = iterator.next();
					Element city = results.createElement("city");
					city.setAttribute("color", current.getColor());
					city.setAttribute("name", current.getName());
					city.setAttribute("radius", current.getRadius());
					city.setAttribute("x", Integer.toString(current.getIntX()));
					city.setAttribute("y", Integer.toString(current.getIntY()));			
					cityList.appendChild(city);
				}
				
			} else if (type.equals("coordinate")) {
				SortedSet<City> sortedCoor = list.coorList;
				Iterator<City> iterator = sortedCoor.iterator();
				while (iterator.hasNext()) {
					City current = iterator.next();
					Element city = results.createElement("city");
					city.setAttribute("color", current.getColor());
					city.setAttribute("name", current.getName());
					city.setAttribute("radius", current.getRadius());
					city.setAttribute("x", Integer.toString(current.getIntX()));
					city.setAttribute("y", Integer.toString(current.getIntY()));
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
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for mapCity.
	/* ============================================================================================================= */	
	public static Element mapCity(Document results, Dictionary list, String name, PMQuadtree region, String id) {
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
		else if (region.isOutOfBounds(new Geo2D(newCity))) {
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
		
		/* Optional id. */
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		
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
		/* Optional id. */
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		
		Element nameCity = results.createElement("name");
		parameter.appendChild(nameCity);
		nameCity.setAttribute("value", name);
		
		if (output != null) {
			elt.appendChild(output);
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for deleteCity.
	/* ============================================================================================================= */	
	public static Node deleteCity(Document results, Dictionary list, String name, PRQT map, String id) {
		
		Element elt = null, output = null;
		
		/* Checks if the city exists. */
		if (list.checkDuplicateName(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityDoesNotExist");
		} 
		
		/* Otherwise delete it. */
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}

		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "deleteCity");
		
		/* Optional id. */
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		
		Element nameCity = results.createElement("name");
		parameter.appendChild(nameCity);
		nameCity.setAttribute("value", name);
		
		City possibleCity = list.cityList.get(name);
		if (output != null) {
			Element cityUnmapped = results.createElement("cityUnmapped");
			if (list.checkMapped(name)) {
				output.appendChild(cityUnmapped);
			}
			if (possibleCity != null) {
				cityUnmapped.setAttribute("name", name);
				cityUnmapped.setAttribute("x", Integer.toString((int) possibleCity.x));
				cityUnmapped.setAttribute("y", Integer.toString((int) possibleCity.y));
				cityUnmapped.setAttribute("color", possibleCity.getColor());
				cityUnmapped.setAttribute("radius", possibleCity.getRadius());
			}	
			elt.appendChild(output);

		}
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for printPRQuadTree.
	/* ============================================================================================================= */	
	public static Node printPRQuadtree(Document results, Dictionary list, PRQT map, String id) {
		Element elt = null, output = null;
		
		/* Checks if the map is empty. */
		if (list.insideMap.isEmpty() || map == null || map.root == null || map.root.nodeType() == Type.WHITE) {
			elt = results.createElement("error");
			elt.setAttribute("type", "mapIsEmpty");
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
		command.setAttribute("name", "printPRQuadtree");
		/* Optional id. */
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		
		Element quadtree = results.createElement("quadtree");
		
		if (output != null) {
			elt.appendChild(output);
			output.appendChild(quadtree);
			Element node = null;
			
			if (map.root.nodeType() == Type.WHITE) {
				node = results.createElement("white");
				quadtree.appendChild(node);
			} else if (map.root.nodeType() == Type.BLACK) {
				node = results.createElement("black");
				node.setAttribute("name", map.root.getNodes()[0].getCity().getName());
				node.setAttribute("x", Integer.toString((int) map.root.getNodes()[0].getCity().x));
				node.setAttribute("y", Integer.toString((int) map.root.getNodes()[0].getCity().y));
				quadtree.appendChild(node);
			} else if (map.root.nodeType() == Type.GRAY) {
				Point2D.Float midpoint = map.quadrants[4];
				node = results.createElement("gray");
				quadtree.appendChild(node);
				node.setAttribute("x", Integer.toString((int) midpoint.x));
				node.setAttribute("y", Integer.toString((int) midpoint.y));
			}
			
			if (map.root.nodeType() == Type.GRAY) {
				int i = 0;
				cmsc420.meeshquest.part2.Node[] outer_fields = map.root.getNodes();
				while (i < 4) {
					node.appendChild(recursivePrint(outer_fields[i], results, list));
					i++;
				}
			}
		}
		return elt;
	}
	/* ============================================================================================================= */
	/* Recursive helper for PRQuadTree.
	/* ============================================================================================================= */	
	public static Node recursivePrint(cmsc420.meeshquest.part2.Node root_node, Document results, 
			Dictionary list) {
		boolean non_gray = false;
		
		Element node = null;
		/* If the element is white. */
		if (root_node == null || root_node.nodeType() == Type.WHITE) {
			node = results.createElement("white");
			non_gray = true;
		}
		/* If the element is black. */
		else if (root_node.nodeType() == Type.BLACK) {
			node = results.createElement("black");
			node.setAttribute("name", root_node.getCity().getName());
			node.setAttribute("x", Integer.toString((int) root_node.getCity().x));
			node.setAttribute("y", Integer.toString((int) root_node.getCity().y));
			non_gray = true;
		} 
		/* If the element is gray, recursive print. */
		else if (root_node.nodeType() == Type.GRAY) {
			Point2D.Float midpoint = root_node.getQuadrants()[4];
			node = results.createElement("gray");
			node.setAttribute("x", Integer.toString((int) midpoint.x));
			node.setAttribute("y", Integer.toString((int) midpoint.y));
		}
		
		if (non_gray == false) {
			for (int i = 0; i < 4; i++) {
				node.appendChild(recursivePrint(root_node.getNodes()[i], results, list));	
			}	
		}
		return node;
	}
	/* ============================================================================================================= */
	/* XML documentation for saveMap.
	/* ============================================================================================================= */	
	public static Node saveMap(Document results, String save_name, String id) {
		Element elt = results.createElement("success");
		Element output = results.createElement("output");
		Element command = results.createElement("command");
		elt.appendChild(command);
		command.setAttribute("name", "saveMap");
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element parameter = results.createElement("parameters");
		elt.appendChild(parameter);
		Element name = results.createElement("name");
		parameter.appendChild(name);
		name.setAttribute("value", save_name);
		elt.appendChild(output);
		
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for rangeCities.
	/* ============================================================================================================= */	
	public static Node rangeCities(Document results, TreeMap<String, City> inRange, String xPoint, String yPoint,
			String radiusCircle, String saveName, String id) {
		
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
		command.setAttribute("name", "rangeCities");
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element xValue = results.createElement("x");
		Element yValue = results.createElement("y");
		Element radiusValue = results.createElement("radius");
		xValue.setAttribute("value", xPoint);
		yValue.setAttribute("value", yPoint);
		radiusValue.setAttribute("value", radiusCircle);
		parameter.appendChild(xValue);
		parameter.appendChild(yValue);
		parameter.appendChild(radiusValue);
		
		if (saveName.equals("") == false) {
			Element save_Name = results.createElement("saveMap");
			save_Name.setAttribute("value", saveName);
			parameter.appendChild(save_Name);
		}
		
		if (inRange.isEmpty() == false) {
			Element cityList = results.createElement("cityList");
			output.appendChild(cityList);
			
			SortedMap<String, City> sortedNames = inRange;
			Iterator<City> iterator = sortedNames.values().iterator();
			while (iterator.hasNext()) {
				City current = iterator.next();
				Element city = results.createElement("city");
				city.setAttribute("name", current.getName());
				city.setAttribute("x", Integer.toString(current.getIntX()));
				city.setAttribute("y", Integer.toString(current.getIntY()));
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
	public static Node nearestCity(Document results, Dictionary list, String xPoint, String yPoint, PRQT map, String id) {
		
		Element elt = null, output = null;
		City nearest = Lister.lookNearest(list.insideMap, xPoint, yPoint, list.cityList);
		
		/* Checks if the city exists. */
		if (list.insideMap.isEmpty() || nearest == null) {
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
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element x = results.createElement("x");
		Element y = results.createElement("y");
		parameter.appendChild(x);
		parameter.appendChild(y);
		x.setAttribute("value", xPoint);
		y.setAttribute("value", yPoint);
		
		if (output != null) {
			elt.appendChild(output);
			if (nearest != null) {
				Element city = results.createElement("city");
				city.setAttribute("name", nearest.getName());
				city.setAttribute("x", Integer.toString((int) nearest.x));
				city.setAttribute("y", Integer.toString((int) nearest.y));
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
		/* Optional id. */
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		if (output != null) {
			elt.appendChild(output);
			Element avlgtree = results.createElement("AvlGTree");
			output.appendChild(avlgtree);
			avlgtree.setAttribute("cardinality", Integer.toString(tree.size()));
			avlgtree.setAttribute("height", Integer.toString(tree.height(tree.root)));
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
	public static Node mapRoad(Document results, PMQuadtree pmquadtree, String s, String e, String id, Dictionary library) {
		Element elt = null, output = null;
		City start, end;
		start = library.cityList.get(s);
		end = library.cityList.get(e);
		Geo2D g1 = null, g2 = null;
		Road r = null;
		if (start != null && end != null) { 
			r = new Road(start, end); 
			g1 = new Geo2D(r); 
			Road r2 = new Road(end, start);
			g2 = new Geo2D(r2);
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
		else if (start.getIsolatedOrNot() == true || end.getIsolatedOrNot() == true) {
			elt = results.createElement("error");
			elt.setAttribute("type", "startOrEndIsIsolated");

		}
		else if (pmquadtree.containsCityOrRoad(g1) || pmquadtree.containsCityOrRoad(g2)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadAlreadyMapped");
		}
		else if (r!= null && !r.intersectSquare(new Rectangle(0, 0, pmquadtree.spatialWidth, pmquadtree.spatialHeight))) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadOutOfBounds");
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
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e2) {
				e2.printStackTrace();
			}
			command.setAttribute("id", id);
		}
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
	/* XML documentatoin for rangeRoads. 
	/* ============================================================================================================= */	
	public static Node rangeRoads(Document results, PMQuadtree pmquadtree, String x, String y, String radius, String saveName, String id) {
		
		Element elt = null, output = null;
		
		ArrayList<Road> inRange = new ArrayList<Road>();
		Lister.findInRangeRoad(inRange, Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(radius), pmquadtree);
		
		if (inRange.isEmpty()) {
			elt = results.createElement("error");
			elt.setAttribute("type", "noRoadsExistInRange");
		} 
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "rangeRoads");
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element xValue = results.createElement("x");
		Element yValue = results.createElement("y");
		Element radiusValue = results.createElement("radius");
		xValue.setAttribute("value", x);
		yValue.setAttribute("value", y);
		radiusValue.setAttribute("value", radius);
		parameter.appendChild(xValue);
		parameter.appendChild(yValue);
		parameter.appendChild(radiusValue);
		
		if (saveName.equals("") == false) {
			Element save_Name = results.createElement("saveMap");
			save_Name.setAttribute("value", saveName);
			parameter.appendChild(save_Name);
		}
		
		if (inRange.isEmpty() == false) {
			Element roads = results.createElement("roadList");
			output.appendChild(roads);
					
			inRange.sort(Comparators.sortRoads);
			for (int i = 0; i < inRange.size(); i++) {
				Road current = inRange.get(i);
				String endName = current.getEndName();
				String startName = current.getStartName();
				Element road = results.createElement("road");
				road.setAttribute("end", endName);
				road.setAttribute("start", startName);
				roads.appendChild(road);
			}
			
			output.appendChild(roads);
		}
		if (output != null) {
			elt.appendChild(output);
		}
		return elt;
		
	}
	/* ============================================================================================================= */
	/* XML documentation for printPMQuadtree. 
	/* ============================================================================================================= */	
	public static Node printPMQuadtree(Document results, PMQuadtree pmquadtree, String id, String order) {

		Element elt = null, output = null;
		
		if (pmquadtree == null || pmquadtree.isEmpty() || pmquadtree.root.getNodeType().isWhite()) {
			elt = results.createElement("error");
			elt.setAttribute("type", "mapIsEmpty");
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
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element quadtree = results.createElement("quadtree");
		quadtree.setAttribute("order", order);
		
		if (output != null) {
			elt.appendChild(output);
			output.appendChild(quadtree);
			Element node = null;
			
			if (pmquadtree.isBlack()) {
				node = results.createElement("black");
				node.setAttribute("cardinality", Integer.toString(pmquadtree.root.getGeometries().size()));
				quadtree.appendChild(node);
				blackHelperNode(pmquadtree.root, results, pmquadtree, node);
			} else if (pmquadtree.isGray()) {
				Point2D.Float midpoint = pmquadtree.getMidpoint();
				node = results.createElement("gray");
				quadtree.appendChild(node);
				node.setAttribute("x", Integer.toString((int) midpoint.x));
				node.setAttribute("y", Integer.toString((int) midpoint.y));
			}
			
			if (pmquadtree.isGray()) {
				int i = 0;
				PMNode[] children = pmquadtree.root.getChildren();
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
	public static Node recursivePMQTPrint(PMNode children, Document results, PMQuadtree tree) {
		boolean non_gray = false;
		
		Element node = null;
		
		if (children.getNodeType().isWhite()) {
			node = results.createElement("white");
			non_gray = true;
		} else if (children.getNodeType().isBlack()) {
			node = results.createElement("black");
			node.setAttribute("cardinality", Integer.toString(children.getGeometries().size()));
			blackHelperNode(children, results, tree, node);
			
			non_gray = true;
		} else if (children.getNodeType().isGray()) {
			Point2D.Float midpoint = children.getMidpoint();
			node = results.createElement("gray");
			node.setAttribute("x", Integer.toString((int) midpoint.x));
			node.setAttribute("y", Integer.toString((int) midpoint.y));
		}
		if (non_gray == false) {
			for (int i = 0; i < 4; i++) {
				node.appendChild(recursivePMQTPrint(children.getChildren()[i], results, tree));	
			}	
		}
		return node;
	}
	/* ============================================================================================================= */
	/* Helper when PMQuadtree has a black node.
	/* ============================================================================================================= */	
	public static void blackHelperNode(PMNode pmnode, Document results, PMQuadtree pmquadtree, Element node) {
		if (pmnode.getNodeType().isBlack()) {
			TreeSet<Geo2D> geometries = pmnode.getGeometries();
			ArrayList<City> cities = new ArrayList<City>();
			ArrayList<Road> roads = new ArrayList<Road>();
			ArrayList<City> sortedByIsolation = new ArrayList<City>();
			Iterator<Geo2D> iter = geometries.iterator();
			while (iter.hasNext()) {
				Geo2D curr = iter.next();
				if (curr.isCity()) {
					cities.add(curr.getCity());
				} else if (curr.isRoad()) {
					roads.add(curr.getRoad());
				}
			}
			Lister.sortByIsolated(cities, sortedByIsolation);
			for (int i = cities.size() - 1; i >= 0; i--) {
				Element geo = null;
				City a = cities.get(i);
				String color = a.getColor();
				String name = a.getName();
				String radius = a.getRadius();
				String x = Integer.toString((int) a.x);
				String y = Integer.toString((int) a.y);
				if (a.getIsolatedOrNot() == true) {
					geo = results.createElement("isolatedCity");
				} else {
					geo = results.createElement("city");
				}
				geo.setAttribute("color", color);
				geo.setAttribute("name", name);
				geo.setAttribute("radius", radius);
				geo.setAttribute("x", x);
				geo.setAttribute("y", y);
				node.appendChild(geo);
			}
			roads.sort(Comparators.sortRoads);
			for (int i = 0; i < roads.size(); i++) {
				Element geo = null;
				Road a = roads.get(i);
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
	public static Node nearestIsolatedCity(Document results, Dictionary library, String id, String x, String y) {

		Element elt = null, output = null;
		City nearest = Lister.lookNearestIsolated(library.insideMap, x, y);
		
		/* Checks if the city exists. */
		if (nearest == null) {
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
		command.setAttribute("name", "nearestIsolatedCity");
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element xPoint = results.createElement("x");
		Element yPoint = results.createElement("y");
		parameter.appendChild(xPoint);
		parameter.appendChild(yPoint);
		xPoint.setAttribute("value", x);
		yPoint.setAttribute("value", y);
		
		if (output != null) {
			elt.appendChild(output);
			if (nearest != null) {
				Element city = results.createElement("isolatedCity");
				city.setAttribute("name", nearest.getName());
				city.setAttribute("x", Integer.toString((int) nearest.x));
				city.setAttribute("y", Integer.toString((int) nearest.y));
				city.setAttribute("color", nearest.getColor());
				city.setAttribute("radius", nearest.getRadius());
				output.appendChild(city);
			}
		}
		
		
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for nearestRoad. 
	/* ============================================================================================================= */	
	public static Node nearestRoad(Document results, PMQuadtree pmquadtree, String id, String x, String y) {
		Element elt = null, output = null;
		
		Road nearest = Lister.lookNearestRoad(pmquadtree, x, y);
		
		if (nearest == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadNotFound");
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
		command.setAttribute("name", "nearestRoad");
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element xPoint = results.createElement("x");
		Element yPoint = results.createElement("y");
		parameter.appendChild(xPoint);
		parameter.appendChild(yPoint);
		xPoint.setAttribute("value", x);
		yPoint.setAttribute("value", y);
		
		if (output != null) {
			elt.appendChild(output);
			if (nearest != null) {
				Element road = results.createElement("road");
				road.setAttribute("end", nearest.getEndName());
				road.setAttribute("start", nearest.getStartName());
				output.appendChild(road);
			}
		}
		
		return elt;
	}
	/* ============================================================================================================= */
	/* XML documentation for nearestCityToRoad. 
	/* ============================================================================================================= */	
	public static Node nearestCityToRoad(Document results, PMQuadtree pmquadtree, String id, String start, String end, Dictionary library) {
		Element elt = null, output = null;
		Geo2D road = null;
		City nearest = Lister.lookNearestCityToRoad(pmquadtree, start, end, library);
		if (!start.equals("") && !end.equals("")) {
			City s = library.cityList.get(start);
			City e = library.cityList.get(end);
			Road r = null;
			if (s != null && e != null) {
				r = new Road(s, e);
				road = new Geo2D(r);
			}
		}
		if (road == null || pmquadtree.containsCityOrRoad(road) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "roadIsNotMapped");
		} else if (nearest == null) {
			elt = results.createElement("error");
			elt.setAttribute("type", "noOtherCitiesMapped");
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
		command.setAttribute("name", "nearestCityToRoad");
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element startPoint = results.createElement("start");
		Element endPoint = results.createElement("end");
		parameter.appendChild(startPoint);
		parameter.appendChild(endPoint);
		startPoint.setAttribute("value", start);
		endPoint.setAttribute("value", end);
		
		if (output != null) {
			elt.appendChild(output);
			if (nearest != null) {
				Element city = results.createElement("city");
				city.setAttribute("name", nearest.getName());
				city.setAttribute("x", Integer.toString((int) nearest.x));
				city.setAttribute("y", Integer.toString((int) nearest.y));
				city.setAttribute("color", nearest.getColor());
				city.setAttribute("radius", nearest.getRadius());
				output.appendChild(city);
			}
		}
		
		return elt;
	}
	public static Node shortestPath(Document results, PMQuadtree pmquadtree, String id, Dictionary library, 
			String start, String end, String saveMap, String saveHTML) {
		Element elt = null, output = null;
		/* d. */
		TreeMap<City, Double> distanceToSource = new TreeMap<City, Double>();
		/* Pi. */
		TreeMap<City, City> origin = new TreeMap<City, City>();
		/* weights. */
		TreeMap<Road, Double> weights = new TreeMap<Road, Double>();
		Iterator iter = pmquadtree.root.getGeometries().iterator();
		while (iter.hasNext()) {
			Geo2D curr = (Geo2D) iter.next();
			if (curr.isRoad()) {
				Road r = curr.getRoad();
				weights.put(r, r.distanceBetween());
			}			
		}
		if (!start.equals("") && library.checkMapped(start) && !end.equals("") && library.checkMapped(end)) {
			City source = library.cityList.get(start);
			City fin = library.cityList.get(end);
			if (pmquadtree.getConnections().containsSource(source) && pmquadtree.getConnections().containsSource(fin))
			Dijkstra.dijkstra(pmquadtree.getConnections(), weights, source, distanceToSource, origin);
		}		
		if (start.equals("") || !library.checkMapped(start)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "nonExistentStart");
		} else if (end.equals("") || !library.checkMapped(end)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "nonExistentEnd");
		} 
		
		City source = library.cityList.get(start);
		City fin = library.cityList.get(end);
		ArrayList<City> route = new ArrayList<City>();
		if (source != null && fin != null && 
				pmquadtree.getConnections().containsSource(source) && pmquadtree.getConnections().containsSource(fin)) {
			route = Dijkstra.process(source, fin, weights, distanceToSource, origin, pmquadtree.getConnections());
		}
		if (source != null && fin != null && source.getName().equals(fin.getName())) {
			route.add(fin);				
			distanceToSource.put(fin, 0d);
		}		
		if (elt == null && (route.isEmpty() || distanceToSource.get(fin) == Double.POSITIVE_INFINITY)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "noPathExists");
		} 
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}

		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "shortestPath");
		if (!id.equals("")) {
			try {
				Lister.checkErrorNumber(id);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			command.setAttribute("id", id);
		}
		Element startPoint = results.createElement("start");
		Element endPoint = results.createElement("end");
		parameter.appendChild(startPoint);
		parameter.appendChild(endPoint);
		startPoint.setAttribute("value", start);
		endPoint.setAttribute("value", end);
		if (!saveMap.equals("")) {
			Element saveMap2 = results.createElement("saveMap");
			saveMap2.setAttribute("value", saveMap);
			parameter.appendChild(saveMap2);
		}
		if (!saveHTML.equals("")) {
			Element saveHTML2 = results.createElement("saveHTML");
			saveHTML2.setAttribute("value", saveHTML);
			parameter.appendChild(saveHTML2);
		}
		
		if (output != null) {
			elt.appendChild(output);
			Element path = results.createElement("path");
			output.appendChild(path);
			double distance = distanceToSource.get(library.cityList.get(end));
			path.setAttribute("length", String.format("%.3f", distance));
			if (source.getName().equals(fin.getName())) path.setAttribute("hops", "0");
			else path.setAttribute("hops", Integer.toString(route.size()));
			if (!source.getName().equals(fin.getName())) {
				Element begin = results.createElement("road");
				begin.setAttribute("start", start);
				begin.setAttribute("end", route.get(0).getName());
				path.appendChild(begin);
				City a = library.cityList.get(start);
				City prevB = a;
				for (int i = 0; i < route.size() - 1; i++) {
					a = prevB;
					City b = library.cityList.get(route.get(i).getName());
					City c = library.cityList.get(route.get(i+1).getName());
					Directions dir = Angles.getDirections(a, b, c);
					if (dir == Directions.LEFT) {
						path.appendChild(results.createElement("left"));
					} else if (dir == Directions.RIGHT) {
						path.appendChild(results.createElement("right"));
					} else if (dir == Directions.STRAIGHT) {
						path.appendChild(results.createElement("straight"));
					}
					Element roadstart = results.createElement("road");
					roadstart.setAttribute("start", b.getName());
					roadstart.setAttribute("end", c.getName());
					path.appendChild(roadstart);
					prevB = b;
				}
			}
		}
		
		return elt;
	}
}
