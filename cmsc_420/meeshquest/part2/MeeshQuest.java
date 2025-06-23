package cmsc420.meeshquest.part2;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.drawing.CanvasPlus;
import cmsc420.meeshquest.part2.Node.Type;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.sortedmap.Geo2D;
import cmsc420.sortedmap.PMQuadtree;
import cmsc420.sortedmap.Road;
import cmsc420.xml.XmlUtility;

public class MeeshQuest {

    public static void main(String[] args) {
    	
    	Document results = null;
    	/* ========================================================================================================= */
		/* Library & AvlGTree is to contains the cities / keep track of them.
		 * PMQuadTree & PRQuadtree is to contains the mappings of the cities and roads.
		/* ========================================================================================================= */		
    	Dictionary library = new Dictionary();
    	PRQT map = new PRQT();
    	AvlGTree<String, Point2D.Float> avlgtree = null;
    	PMQuadtree pmquadtree = null;
    	Element root = null;
        try {
        	Document doc = XmlUtility.validateNoNamespace(System.in);
        	//Document doc = XmlUtility.parse(new File("part2.public.avlg.input.xml"));
        	results = XmlUtility.getDocumentBuilder().newDocument();
        
        	Element commandNode = doc.getDocumentElement();
        	/* ===================================================================================================== */
			/* Documents variables
			/* ===================================================================================================== */		
			String width = commandNode.getAttribute("spatialWidth");
			String height = commandNode.getAttribute("spatialHeight");
			String g = commandNode.getAttribute("g");
			String pmOrder = commandNode.getAttribute("pmOrder");
			if (!width.equals("") && !height.equals("") && !pmOrder.equals("")) {
				map.initialize(Integer.parseInt(width), Integer.parseInt(height));
				pmquadtree = new PMQuadtree(Integer.parseInt(width), Integer.parseInt(height));
			} else {
				throw new SAXException();
			} 
			if (!g.equals("")) {
				avlgtree = new AvlGTree<String, Point2D.Float>(Comparators.sortNames, Integer.parseInt(g));
			} else {
				throw new SAXException();
			}
			
        	root = results.createElement("results");
        	results.appendChild(root);

        	final NodeList nl = commandNode.getChildNodes();
        	for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);
        			/* ============================================================================================= */
    				/* Insert city into the Dictionary, and AvlGTree.
    				/* ============================================================================================= */		
        			if (commandNode.getNodeName().equals("createCity")) {
        				/* ========================================================================================= */
        				/* City variables.
        				/* ========================================================================================= */
        				String name = commandNode.getAttribute("name");
        				String color = commandNode.getAttribute("color");
        				String radius = commandNode.getAttribute("radius");
        				String x = commandNode.getAttribute("x");
        				String y = commandNode.getAttribute("y");
        				String id = commandNode.getAttribute("id");
        				Float xVal = Float.parseFloat(x);
        				Float yVal = Float.parseFloat(y);
        				/* ========================================================================================= */
        				/* Checks if there is any errors in the regex of the variables.
        				/* ========================================================================================= */
        				Lister.checkErrorPattern(name);
        				Lister.checkErrorPattern(color);
        				Lister.checkErrorNumber(commandNode.getAttribute("x"));
        				Lister.checkErrorNumber(commandNode.getAttribute("y"));
        				/* ========================================================================================= */
        				/* Insert city into the Dictionary, and AvlGTree. City is not isolated because it is not in
        				 * the map. But when it is mapped without map road, it will be isolated.
        				/* ========================================================================================= */				
        				if (!name.equals("") && !color.equals("") && !radius.equals("") && !x.equals("") && !y.equals("")) {
        					City newCity = new City(name, xVal, yVal, radius, color, false);        				
            				root.appendChild(XMLHandler.createCity(results, library, commandNode));	
            				Point2D.Float coordinate = new Point2D.Float(xVal, yVal);
            				/* No duplicated city. */
            				if (!library.checkDuplicateCoordinates(newCity) && !library.checkDuplicateName(name)) {
                				avlgtree.put(name, coordinate);
            				}
            				library.createCity(newCity);            				
        				} else {
        					throw new SAXException();
        				}	
        			}
        			/* ============================================================================================= */
        			/* List the cities in the dictionary. 
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("listCities")) {
        				root.appendChild(XMLHandler.listCities(results, library, commandNode));
        			}
        			/* ============================================================================================= */
        			/* Clear all structure of Dictionary, PRQuadTree, AvlGTree, and PMQuadtree.
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("clearAll")) {
        				/* Get id tag */
        				String id = commandNode.getAttribute("id");
        				/* Clears PRQuadtree. */
        				Iterator<City> itr = library.insideMap.iterator();
        				while (itr.hasNext()) {
        					City curr = itr.next();
        					map.removeCityfromRoot(curr);
        				}        			
        				/* Clears Dictionary. */
        				library.clearAll();
        				/* Clears AvlGTree. */
        				avlgtree.clear();
        				/* Clears PMQuadtree. */
        				pmquadtree.clear();
        				root.appendChild(XMLHandler.clearAll(results, id));
        			}
        			/* ============================================================================================= */
        			/* Delete the city from the library. If it is mapped, delete it too. 
        			/* 								*** Not updated for part2 ***
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("deleteCity")) {
        				/* Name for the deleted city. */
        				String name = commandNode.getAttribute("name");
        				String id = commandNode.getAttribute("id");
        				/* Checks if the name contains error. */
        				Lister.checkErrorPattern(name);
        				/* Delete the city from the PRQuadTree, Library. */
        				if (name.equals("")) {
        					throw new SAXException();
        				} else {
        					root.appendChild(XMLHandler.deleteCity(results, library, name, map, id));
            				City city = library.cityList.get(name);
            				if (city != null) {
            					if (library.checkMapped(name)) {
            						map.removeCityfromRoot(city);
                				}
            					library.deleteCity(city);
            				}
        				}
        			}
        			/* ============================================================================================= */
        			/* Maps the city into the PRQuadTree, and the PMQuadtree (as an isolated city) 
        			 * and Library.insideMap.
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("mapCity")) {
        				/* Get the name of the city & id tag. */
        				String name = commandNode.getAttribute("name");
        				String id = commandNode.getAttribute("id");
        				/* Checks if the name contains error. */
        				Lister.checkErrorPattern(name);
        				/* Map the city if the city exists in the library, else say it does not exists and cannot be
        				   mapped. */
        				if (name.equals("")) {
        					throw new SAXException();
        				} else {
        					root.appendChild(XMLHandler.mapCity(results, library, name, pmquadtree, id));
        					/* Checks if city exists in dictionary. */
            				if (library.checkDuplicateName(name)) {
            					/* Checks if it has not yet been mapped. */
            					if (library.checkMapped(name) == false) {
            						/* Get the city from the library. */
            						City target = library.cityList.get(name);
            						/* Checks if it is out of bounds. */
            						if (target.x < map.width && target.y < map.height && target.y >= 0 && target.x >= 0) {
            							/* Since it is mapped from the mapCity command, it will be an isolated city. */
                						target.setToIsolated();
            							/* Add to the PRQuadTree. */
                						map.addCitytoRoot(target);
                						/* Add into dictionary list of cities that are already mapped. */
                						library.insideMap.add(target);
                					}
            						if (target.x <= pmquadtree.spatialWidth && target.y <= pmquadtree.spatialHeight
            							&& target.x >= 0 && target.y >= 0) {
            							/* Since it is mapped from the mapCity command, it will be an isolated city. */
                						target.setToIsolated();
            							/* Make the target city to be mapped. */
                						Geo2D targetCity = new Geo2D(target);
                						/* Maps into the PMQuadTree as an Isolated City. */	
                						pmquadtree.root = pmquadtree.root.add(targetCity);
            						
            						}
            					}
            				}
        				}	
        			}
        			/* ============================================================================================= */
        			/* Unmap the city from PRQuadTree and PMQuadTree.
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("unmapCity")) {
        				/* Name of the city & id tag. */
        				String name = commandNode.getAttribute("name");
        				String id = commandNode.getAttribute("id");
        				/* Checks error pattern. */
        				Lister.checkErrorPattern(name);
        				/* Unmap it from PRQuadTree & PMQuadTree. */
        				if (name.equals("")) {
        					throw new SAXException();
        				} else {
        					root.appendChild(XMLHandler.unmapCity(results, library, name, map, id));
            				if (library.checkDuplicateName(name)) {
            					if (library.checkMapped(name)) {
            						/* From PRQuadTree */
                					map.removeCityfromRoot(library.cityList.get(name));
                					/* From Library map list. */
                    				library.insideMap.remove(library.cityList.get(name));
                    				/* From PMQuadTree. */
                    				Geo2D city = new Geo2D(library.cityList.get(name));
                    				pmquadtree.root = pmquadtree.root.remove(city);
            					}
            				}
        				}
        			}
        			/* ============================================================================================= */
        			/* Prints PRQuadtree.
        			/* ============================================================================================= */			
        			else if (commandNode.getNodeName().equals("printPRQuadtree")) {
        				/* Get id tag */
        				String id = commandNode.getAttribute("id");
        				/* Prints it. */
        				root.appendChild(XMLHandler.printPRQuadtree(results, library, map, id));
        			}
        			/* ============================================================================================= */
        			/* Save the Map into a file.
        			/* ============================================================================================= */			
        			else if (commandNode.getNodeName().equals("saveMap")) {
        				/* Get the save name. */
        				String save_name = commandNode.getAttribute("name");
        				/* Draw on canvas. */
        				if (save_name.equals("")) {
        					throw new SAXException();
        				} else {
        					CanvasPlus canvas = new CanvasPlus("MeeshQuest");
        					canvas.setFrameSize(Integer.parseInt(width), Integer.parseInt(height));
        					/* New white square.  */
        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.WHITE, true);
        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.GRAY, false);
        					root.appendChild(XMLHandler.saveMap(results, save_name, commandNode.getAttribute("id")));
        					/* Draw the map if it is not empty. */
        					if (map.root.nodeType() != Type.WHITE && map != null && library.insideMap.isEmpty() == false) {
        						Drawer.processMap(canvas, library);
        					} 
        					/* Draw the roads. */
        					TreeMap<City, ArrayList<City>> roadList = pmquadtree.getAdjacencyList().getList();
        					Iterator<City> iter = roadList.keySet().iterator();
        					/* Iterate through the City keys. */
        					while (iter.hasNext()) {
        						City current = iter.next();
        						double xOrigin = current.getX();
        						double yOrigin = current.getY();
        						ArrayList<City> neighbors = roadList.get(current);
        						Iterator<City> iter2 = neighbors.iterator();
        						/* Iterator through the city neighbors. */
        						while (iter2.hasNext()) {
        							City currentNeighbor = iter2.next();
        							double xEnd = currentNeighbor.getX();
        							double yEnd = currentNeighbor.getY();
        							canvas.addLine(xOrigin, yOrigin, xEnd, yEnd, Color.BLACK);
        						}
        					}
        					/* Save the map and close it. */
        					canvas.save(save_name);
        					canvas.dispose();
        				}
        			/* ============================================================================================= */
            		/* Range the cities in the PMQuadtree within the area.
            		/* ============================================================================================= */			
        			} else if (commandNode.getNodeName().equals("rangeCities")) {
        				/* Cities that are in range sorted by name reversed. */
        				TreeMap<String, City> inRange = new TreeMap<String, City>(Comparators.sortNames);
        				/* ========================================================================================= */
            			/* Variables.
            			/* ========================================================================================= */		
        				String saveMap = commandNode.getAttribute("saveMap");
        				String xPoint = commandNode.getAttribute("x");
        				String yPoint = commandNode.getAttribute("y");
        				String radiusCircle = commandNode.getAttribute("radius");
        				String id = commandNode.getAttribute("id");
        				/* Checks if it is within bounds and checks for save map. */
        				if (!xPoint.equals("") && !yPoint.equals("") && !radiusCircle.equals("")) {
        					if (saveMap.equals("") == false && Float.parseFloat(width) >= Float.parseFloat(radiusCircle)) {
        						/* Draw the map and save it. */
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
            				/* Find everything within range with the Library.insideMap. */
            				Lister.findInRange(inRange, Float.parseFloat(xPoint), 
            						Float.parseFloat(yPoint), Float.parseFloat(radiusCircle), library);
            				root.appendChild(XMLHandler.rangeCities(results, inRange, xPoint, yPoint, 
            						radiusCircle, saveMap, id));			
        				} else {
        					throw new SAXException();
        				}    				
        			/* ============================================================================================= */
            		/* Checks the nearest city to a point. 
            		/* ============================================================================================= */			
        			} else if (commandNode.getNodeName().equals("nearestCity")) {
        				/* Variables and get id tag */
        				String x = commandNode.getAttribute("x");
        				String y = commandNode.getAttribute("y");
        				String id = commandNode.getAttribute("id");
        				/* Checks for error. */
        				Lister.checkErrorNumber(x);
        				Lister.checkErrorNumber(y);
        				/* Use lister to check in XML. */
        				if (!x.equals("") && !y.equals("")) {
        					root.appendChild(XMLHandler.nearestCity(results, library, x, y, map, id));
        				} else {
        					throw new SAXException();
        				}
        			/* ============================================================================================= */
            		/* Prints the AvlGTree.
            		/* ============================================================================================= */		
        			} else if (commandNode.getNodeName().equals("printAvlTree")) {
        				root.appendChild(XMLHandler.printAvlTree(results, avlgtree, commandNode.getAttribute("id")));
        			/* ============================================================================================= */
            		/* Maps the road into the PMQuadTree as well as the two cities (as Non-Isolated).
            		/* ============================================================================================= */		
        			} else if (commandNode.getNodeName().equals("mapRoad")) {
        				/* Get the variables and id tag. */
        				String s = commandNode.getAttribute("start");
        				String e = commandNode.getAttribute("end");
        				String id = commandNode.getAttribute("id");        				
        				if (s.equals("") || e.equals("")) throw new SAXException();
        				Lister.checkErrorPattern(s);
        				Lister.checkErrorPattern(e);
        				/* Prints XML */
        				root.appendChild(XMLHandler.mapRoad(results, pmquadtree, s, e, id, library));
        				/* Checks if start or endpoint exists in library. */
        				if (library.checkDuplicateName(s) && library.checkDuplicateName(e)) {
        					/* Checks if the start and end are equal to one another. */
        					if (!s.equals(e)) {
        						/* Checks if the two cities are not isolated. */
        						if (library.cityList.get(s).getIsolatedOrNot() == false 
        								&& library.cityList.get(e).getIsolatedOrNot() == false) {
        							Road r = null;
        							r = new Road(library.cityList.get(s), library.cityList.get(e));
        							Geo2D road = new Geo2D(r);
        							Geo2D dummyRoad = new Geo2D(new Road(library.cityList.get(e), library.cityList.get(s)));
        							/* Checks if road or city already exists. */
        							if (!pmquadtree.containsCityOrRoad(road) && !pmquadtree.containsCityOrRoad(dummyRoad)) {
        								/* Checks if the city A is out of bounds and add into PMQuadTree if it doesnt. */
        								City a = r.getStartCity();
        								a.setToNotIsolated();
        								Geo2D cityA = new Geo2D(a);
        								/* Adding into PMQuadTree and PRQuadTree. */
        								if (library.checkMapped(a.getName()) == false) {
        									if (0 <= a.x && a.x < map.width && 0 <= a.y && a.y < map.height) {
            									map.addCitytoRoot(a);
            								}
        									if (Lister.withinBoundsOfPMQT(a.x, a.y, pmquadtree.spatialWidth, pmquadtree.spatialHeight)) {
        										pmquadtree.root = pmquadtree.root.add(cityA);
        										library.insideMap.add(a);
            								}
        								}      								
        								/* Checks if the city B is out of bounds and add into PMQuadTree if it doesnt. */
        								City b = r.getEndCity();
        								b.setToNotIsolated();
        								Geo2D cityB = new Geo2D(b);
        								/* Adding into PMQuadTree and PRQuadTree. */
        								if (library.checkMapped(b.getName()) == false) {
        									if (0 <= b.x && b.x < map.width && 0 <= b.y && b.y < map.height) {
            									map.addCitytoRoot(b);
            								}
        									if (Lister.withinBoundsOfPMQT(b.x, b.y, pmquadtree.spatialWidth, pmquadtree.spatialHeight)) {
        										pmquadtree.root = pmquadtree.root.add(cityB);
        										library.insideMap.add(b);
            								}
        								}      		
        								/* Checks if the road is already mapped and if not add it into PMQuadTree. */
        								if (!pmquadtree.containsCityOrRoad(road) && !pmquadtree.containsCityOrRoad(dummyRoad) &&
        										r.intersectSquare(new Rectangle(0, 0, pmquadtree.spatialWidth, pmquadtree.spatialHeight))) {
        									pmquadtree.root = pmquadtree.root.add(road);
        								}
        							}
        						}
        					}
        				}  
        			}
        			/* ============================================================================================= */
            		/* List all the roads present in PMQuadTree.
            		/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("rangeRoads")) {
        				/* Variable & get id tag. */
        				String x = commandNode.getAttribute("x");
        				String y = commandNode.getAttribute("y");
        				String radius = commandNode.getAttribute("radius");
        				String saveName = commandNode.getAttribute("saveMap");
        				String id = commandNode.getAttribute("id");
        				
        				if (x.equals("") || y.equals("") || radius.equals("")) throw new SAXException();
        				Lister.checkErrorNumber(x);
        				Lister.checkErrorNumber(y);
        				Lister.checkErrorNumber(radius);
        				
        				/* If savemap is present, draw the map and save it along with the road. */
        				if (saveName.equals("") == false) {
        					Drawer.drawSpatialMap(saveName, radius, width, height, x, y, library, pmquadtree, map);
        				}
        				/* XML. */
        				root.appendChild(XMLHandler.rangeRoads(results, pmquadtree, x, y, radius, saveName, id));
        				
        			}
        			/* ============================================================================================= */
            		/* Prints the PMQuadTree.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("printPMQuadtree")) {
        				root.appendChild((XMLHandler.printPMQuadtree(results, pmquadtree, commandNode.getAttribute("id"), pmOrder)));
        			}
        			/* ============================================================================================= */
            		/* Return the nearest isolated city from a specified point.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("nearestIsolatedCity")) {
        				String id = commandNode.getAttribute("id");
        				String x = commandNode.getAttribute("x");
        				String y = commandNode.getAttribute("y");
        				
        				if (x.equals("") || y.equals("")) throw new SAXException();
        				
        				Lister.checkErrorNumber(x);
        				Lister.checkErrorNumber(y);
        				root.appendChild(XMLHandler.nearestIsolatedCity(results, library, id, x, y));
        			}
        			/* ============================================================================================= */
            		/* Return the nearest road from a point.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("nearestRoad")) {
        				String x = commandNode.getAttribute("x");
        				String y = commandNode.getAttribute("y");
        				String id = commandNode.getAttribute("id");
        				if (x.equals("") || y.equals("")) throw new SAXException();
        				Lister.checkErrorNumber(x);
        				Lister.checkErrorNumber(y);
        				root.appendChild(XMLHandler.nearestRoad(results, pmquadtree, id, x, y));
        			}
        			/* ============================================================================================= */
            		/* Finds nearest city from a road.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("nearestCityToRoad")) {
        				String start = commandNode.getAttribute("start");
        				String end = commandNode.getAttribute("end");
        				String id = commandNode.getAttribute("id");
        				if (start.equals("") || end.equals("")) throw new SAXException();
        				Lister.checkErrorPattern(start);
        				Lister.checkErrorPattern(end);
        				
        				root.appendChild(XMLHandler.nearestCityToRoad(results, pmquadtree, id, start, end, library));
        			}
        			/* ============================================================================================= */
            		/* Gets the shortest part travelled from a city to another city.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("shortestPath")) {
        				String id = commandNode.getAttribute("id");
        				String start = commandNode.getAttribute("start");
        				String end = commandNode.getAttribute("end");
        				String saveMap = commandNode.getAttribute("saveMap");
        				String saveHTML = commandNode.getAttribute("saveHTML");
        				Lister.checkErrorPattern(start);
        				Lister.checkErrorPattern(end);
        				
        				if (saveMap.equals("") == false) {
    						/* Draw the map and save it. */
        					CanvasPlus canvas = new CanvasPlus("MeeshQuest");
        					canvas.setFrameSize(Integer.parseInt(width), Integer.parseInt(height));
        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.WHITE, true);
        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.BLACK, false);
        					if (map.root.nodeType() != Type.WHITE && map != null && library.insideMap.isEmpty() == false) {
        						Drawer.processMap(canvas, library);
        					}             	
        					canvas.save(saveMap);
        					canvas.dispose();
        				}
        				
        				root.appendChild(XMLHandler.shortestPath(results, pmquadtree, id, library, start, end, saveMap, saveHTML));
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
