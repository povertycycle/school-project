package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;
import java.security.Policy.Parameters;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cmsc420.meeshquest.part1.Node.Type;

public class XMLHandler {
	public static Element createCity(Document results, Dictionary list, Element commandNode) {
		String name = commandNode.getAttribute("name");
		String color = commandNode.getAttribute("color");
		String radius = commandNode.getAttribute("radius");
		Float x = Float.parseFloat(commandNode.getAttribute("x"));
		Float y = Float.parseFloat(commandNode.getAttribute("y"));
		City newCity = new City(name, x, y, radius, color);
		
		Element elt = null, output = null;
		
		if (list.checkDuplicateCoordinates(newCity)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "duplicateCityCoordinates");
		}
		
		else if (list.checkDuplicateName(name)) {
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
	
	public static Element listCities(Document results, Dictionary list, Element commandNode) {
		String type = commandNode.getAttribute("sortBy");
		
		Element elt = null, output = null;
		
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
	
	public static Element clearAll(Document results) {
		
		Element	elt = results.createElement("success");
		Element command = results.createElement("command");
		Element parameters = results.createElement("parameters");
		Element output = results.createElement("output");
		elt.appendChild(command);
		elt.appendChild(parameters);
		elt.appendChild(output);
		command.setAttribute("name", "clearAll");
		return elt;
		
	}
	
	public static Element mapCity(Document results, Dictionary list, String name, PQRT region) {
		City newCity = list.cityList.get(name);
		
		Element elt = null, output = null;
		
		if (list.checkDuplicateName(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "nameNotInDictionary");
		} 
		
		else if (list.checkMapped(name)) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityAlreadyMapped");
		}
		
		else if (region.checkBoundary(newCity) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityOutOfBounds");
		}
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "mapCity");

		Element nameCity = results.createElement("name");
		parameter.appendChild(nameCity);
		nameCity.setAttribute("value", name);
		
		
		if (output != null) {
			elt.appendChild(output);
		}
		return elt;
	}
	
	public static Element unmapCity(Document results, Dictionary list, String name, PQRT region) {
		City newCity = list.cityList.get(name);
		
		Element elt = null, output = null;
		
		if (list.checkDuplicateName(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "nameNotInDictionary");
		} 
		
		else if (list.checkMapped(name) == false) {
			elt = results.createElement("error");
			elt.setAttribute("type", "cityNotMapped");
		}
		
		if (elt == null) {
			elt = results.createElement("success");
			output = results.createElement("output");
		}
		
		Element command = results.createElement("command");
		Element parameter = results.createElement("parameters");

		elt.appendChild(command);
		elt.appendChild(parameter);
		command.setAttribute("name", "unmapCity");

		Element nameCity = results.createElement("name");
		parameter.appendChild(nameCity);
		nameCity.setAttribute("value", name);
		
		
		if (output != null) {
			elt.appendChild(output);
		}
		return elt;
	}


	public static Node deleteCity(Document results, Dictionary list, String name, PQRT map) {
		
		Element elt = null, output = null;
		
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
				cityUnmapped.setAttribute("color", possibleCity.color);
				cityUnmapped.setAttribute("radius", possibleCity.radius);
			}	
			elt.appendChild(output);

		}
		return elt;
	}
	
	public static Node printPRQuadtree(Document results, Dictionary list, PQRT map) {
		Element elt = null, output = null;
		
		if (list.insideMap.isEmpty() || map == null || map.root == null || map.root.nodeType() == Type.WHITE) {
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
		command.setAttribute("name", "printPRQuadtree");
		
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
				node.setAttribute("name", map.root.getNodes()[0].getCity().name);
				node.setAttribute("x", Integer.toString((int) map.root.getNodes()[0].getCity().x));
				node.setAttribute("y", Integer.toString((int) map.root.getNodes()[0].getCity().y));
				quadtree.appendChild(node);
			} else if (map.root.nodeType() == Type.GREY) {
				Point2D.Float midpoint = map.quadrants[4];
				node = results.createElement("gray");
				quadtree.appendChild(node);
				node.setAttribute("x", Integer.toString((int) midpoint.x));
				node.setAttribute("y", Integer.toString((int) midpoint.y));
			}
			
			if (map.root.nodeType() == Type.GREY) {
				int i = 0;
				cmsc420.meeshquest.part1.Node[] outer_fields = map.root.getNodes();
				while (i < 4) {
					
					node.appendChild(recursivePrint(outer_fields[i], results, list));
					i++;
				}
			}
			
		}
		return elt;
	}
	
	public static Node recursivePrint(cmsc420.meeshquest.part1.Node root_node, Document results, 
			Dictionary list) {
		boolean non_gray = false;
		
		Element node = null;
		
		if (root_node == null || root_node.nodeType() == Type.WHITE) {
			node = results.createElement("white");
			non_gray = true;
		} else if (root_node.nodeType() == Type.BLACK) {
			node = results.createElement("black");
			node.setAttribute("name", root_node.getCity().name);
			node.setAttribute("x", Integer.toString((int) root_node.getCity().x));
			node.setAttribute("y", Integer.toString((int) root_node.getCity().y));
			non_gray = true;
		} else if (root_node.nodeType() == Type.GREY) {
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

	public static Node saveMap(Document results, String save_name) {
		Element elt = results.createElement("success");
		Element output = results.createElement("output");
		Element command = results.createElement("command");
		elt.appendChild(command);
		command.setAttribute("name", "saveMap");
		Element parameter = results.createElement("parameters");
		elt.appendChild(parameter);
		Element name = results.createElement("name");
		parameter.appendChild(name);
		name.setAttribute("value", save_name);
		elt.appendChild(output);
		
		return elt;
	}

	public static Node rangeCities(Document results, TreeMap<String, City> inRange, String xPoint, String yPoint,
			String radiusCircle, String saveName) {
		
		Element elt = null, output = null;
		
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

	public static Node nearestCity(Document results, Dictionary list, String xPoint, String yPoint, PQRT map) {
		
		Element elt = null, output = null;
		
		if (list.insideMap.isEmpty() || map == null || map.root == null || map.root.nodeType() == Type.WHITE) {
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
		command.setAttribute("name", "nearestCity");
		Element x = results.createElement("x");
		Element y = results.createElement("y");
		parameter.appendChild(x);
		parameter.appendChild(y);
		x.setAttribute("value", xPoint);
		y.setAttribute("value", yPoint);
		
		if (output != null) {
			elt.appendChild(output);
			City nearest = Lister.lookNearest(list.insideMap, xPoint, yPoint, list.cityList);
			if (nearest != null) {
				Element city = results.createElement("city");
				city.setAttribute("name", nearest.name);
				city.setAttribute("x", Integer.toString((int) nearest.x));
				city.setAttribute("y", Integer.toString((int) nearest.y));
				city.setAttribute("color", nearest.color);
				city.setAttribute("radius", nearest.radius);
				output.appendChild(city);
			}
		}
		
		
		return elt;
	}
}
