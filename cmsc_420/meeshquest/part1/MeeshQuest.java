package cmsc420.meeshquest.part1;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.drawing.CanvasPlus;
import cmsc420.meeshquest.part1.Node.Type;
import cmsc420.xml.XmlUtility;

public class MeeshQuest {

    public static void main(String[] args) {
    	
    	Document results = null;
    	Dictionary library = new Dictionary();
    	PQRT map = new PQRT();
    	Element root = null;
        try {
        	Document doc = XmlUtility.validateNoNamespace(System.in);
        	// Document doc = XmlUtility.parse(new File("part1.deleteCity1.input.xml"));
        	results = XmlUtility.getDocumentBuilder().newDocument();
        
        	Element commandNode = doc.getDocumentElement();

			String width = commandNode.getAttribute("spatialWidth");
			String height = commandNode.getAttribute("spatialHeight");
			if (width != "" && height != "") {
				map.initialize(Integer.parseInt(width), Integer.parseInt(height));
			} else {
				throw new SAXException();
			}
			
        	root = results.createElement("results");
        	results.appendChild(root);

        	final NodeList nl = commandNode.getChildNodes();
        	for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);
                
        			if (commandNode.getNodeName().equals("createCity")) {
        				
        				String name = commandNode.getAttribute("name");
        				String color = commandNode.getAttribute("color");
        				String radius = commandNode.getAttribute("radius");
        				Float x = Float.parseFloat(commandNode.getAttribute("x"));
        				Float y = Float.parseFloat(commandNode.getAttribute("y"));
        				
        				Lister.checkErrorPattern(name);
        				Lister.checkErrorPattern(color);
        				Lister.checkErrorNumber(commandNode.getAttribute("x"));
        				Lister.checkErrorNumber(commandNode.getAttribute("y"));
        				
        				if (!name.equals("") && !color.equals("") && !radius.equals("") &&
        						!commandNode.getAttribute("x").equals("") && !commandNode.getAttribute("y").equals("")) {
        					City newCity = new City(name, x, y, radius, color);        				
            				root.appendChild(XMLHandler.createCity(results, library, commandNode));	
            				library.createCity(newCity);
        				} else {
        					throw new SAXException();
        				}		
        				
        			} else if (commandNode.getNodeName().equals("listCities")) {
        				
        				root.appendChild(XMLHandler.listCities(results, library, commandNode));
        				
        			} else if (commandNode.getNodeName().equals("clearAll")) {
        				
        				Iterator<City> itr = library.insideMap.iterator();
        				while (itr.hasNext()) {
        					City curr = itr.next();
        					map.removeCityfromRoot(curr);
        				}        				
        				library.clearAll();
        				root.appendChild(XMLHandler.clearAll(results));
        				
        			} else if (commandNode.getNodeName().equals("deleteCity")) {
        				
        				String name = commandNode.getAttribute("name");
        				
        				Lister.checkErrorPattern(name);
        				
        				if (name.equals("")) {
        					throw new SAXException();
        				} else {
        					root.appendChild(XMLHandler.deleteCity(results, library, name, map));
            				City city = library.cityList.get(name);
            				if (city != null) {
            					if (library.checkMapped(name)) {
            						map.removeCityfromRoot(city);
                				}
            					library.deleteCity(city);
            				}
        				}
        				
        				
        			} else if (commandNode.getNodeName().equals("mapCity")) {
        				
        				String name = commandNode.getAttribute("name");
        				
        				Lister.checkErrorPattern(name);
        				
        				if (name.equals("")) {
        					throw new SAXException();
        				} else {
        					root.appendChild(XMLHandler.mapCity(results, library, name, map));
            				if (library.checkDuplicateName(name)) {
            					if (library.checkMapped(name) == false) {
            						City target = library.cityList.get(name);
            						if (target.x < map.width && target.y < map.height) {
                						map.addCitytoRoot(library.cityList.get(name));
                    					library.insideMap.add(library.cityList.get(name));
            						}
            					}
            				}
        				}			
        				
        			} else if (commandNode.getNodeName().equals("unmapCity")) {
        				
        				String name = commandNode.getAttribute("name");
        				
        				Lister.checkErrorPattern(name);
        				
        				if (name.equals("")) {
        					throw new SAXException();
        				} else {
        					root.appendChild(XMLHandler.unmapCity(results, library, name, map));
            				if (library.checkDuplicateName(name)) {
            					if (library.checkMapped(name)) {
                					map.removeCityfromRoot(library.cityList.get(name));
                    				library.insideMap.remove(library.cityList.get(name));
            					}
            				}
        				}
        				
        				
        			} else if (commandNode.getNodeName().equals("printPRQuadtree")) {
        				
        				root.appendChild(XMLHandler.printPRQuadtree(results, library, map));
        				
        			} else if (commandNode.getNodeName().equals("saveMap")) {
        				
        				String save_name = commandNode.getAttribute("name");
        				        				
        				if (save_name.equals("")) {
        					throw new SAXException();
        				} else {
        					CanvasPlus canvas = new CanvasPlus("MeeshQuest");
        					canvas.setFrameSize(Integer.parseInt(width), Integer.parseInt(height));
        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.WHITE, true);
        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.BLACK, false);
        					root.appendChild(XMLHandler.saveMap(results, save_name));
        					if (map.root.nodeType() != Type.WHITE && map != null && library.insideMap.isEmpty() == false) {
        						Drawer.processMap(canvas, library);
        					} 
        					canvas.save(save_name);
        					canvas.dispose();
        				}
        				
        			} else if (commandNode.getNodeName().equals("rangeCities")) {
        				
        				TreeMap<String, City> inRange = new TreeMap<String, City>(Comparators.sortNames);
        				String saveMap = commandNode.getAttribute("saveMap");
        				String xPoint = commandNode.getAttribute("x");
        				String yPoint = commandNode.getAttribute("y");
        				String radiusCircle = commandNode.getAttribute("radius");
        				
        				if (!xPoint.equals("") && !yPoint.equals("") && !radiusCircle.equals("")) {
        					if (saveMap.equals("") == false && Float.parseFloat(width) >= Float.parseFloat(radiusCircle)) {
        						
            					CanvasPlus canvas = new CanvasPlus("MeeshQuest");
            					canvas.setFrameSize(Integer.parseInt(width), Integer.parseInt(height));
            					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.WHITE, true);
            					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.BLACK, false);
            					
            					if (map.root.nodeType() != Type.WHITE && map != null && library.insideMap.isEmpty() == false) {
            						Drawer.processMap(canvas, library);
            					} 
            					
            					canvas.addCircle(Float.parseFloat(xPoint), Float.parseFloat(yPoint), 
            							Float.parseFloat(radiusCircle), Color.BLUE, false);
            					canvas.save(saveMap);
            					canvas.dispose();
            				}
            				
            				Lister.findInRange(inRange, Float.parseFloat(xPoint), 
            						Float.parseFloat(yPoint), Float.parseFloat(radiusCircle), library);
            				root.appendChild(XMLHandler.rangeCities(results, inRange, xPoint, yPoint, radiusCircle, saveMap));
            				
        				} else {
        					throw new SAXException();
        				}    				
        				
        			} else if (commandNode.getNodeName().equals("nearestCity")) {
        				
        				String x = commandNode.getAttribute("x");
        				String y = commandNode.getAttribute("y");
        				
        				Lister.checkErrorNumber(x);
        				Lister.checkErrorNumber(y);

        				if (!x.equals("") && !y.equals("")) {
        					root.appendChild(XMLHandler.nearestCity(results, library, x, y, map));
        				} else {
        					throw new SAXException();
        				}
        			} 
    
        		}
        	}
        } catch (SAXException | IOException | ParserConfigurationException e) {
        	
        	// Borrowed FatalError processing from part0 MeeshQuest canonical 
        	try {
                results = XmlUtility.getDocumentBuilder().newDocument();
                Element fatalError = results.createElement("fatalError");
                results.appendChild(fatalError);
        	} catch (ParserConfigurationException e1) {
        		System.exit(-1);
        	}
        	
		} finally {
            try {
				XmlUtility.print(results);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
        }
    }
}
