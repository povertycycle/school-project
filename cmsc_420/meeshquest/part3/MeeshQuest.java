package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import cmsc420.drawing.CanvasPlus;
import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.sortedmap.PMNode.PMType;
import cmsc420.sortedmap.Road;
import cmsc420.xml.XmlUtility;

public class MeeshQuest {

    public static void main(String[] args) {
    	
    	Document results = null;
    	/* ========================================================================================================= */
		/* Library & AvlGTree is to contains the cities / keep track of them.
		 * PMQuadTree & PRQuadtree is to contains the mappings of the cities and roads.
		/* ========================================================================================================= */		
    	Dictionary library = new Dictionary();				/* Dictionary for list of Cities created.				 */
    	AirportList airports = new AirportList();			/* Dictionary for list of Airports created.				 */
    	PRQT remoteMap = new PRQT();						/* One remote map.                                  	 */
    	AvlGTree<String, Point2D.Float> avlgTree = null;	/* One AvlGTree for storing City's local coordinates.	 */
    	int pmOrderForTree = -1;							/* The order of a PMQuadTree for the Metropoles.		 */
    	Element root = null;								/*====================================================== */
        try {
        	Document doc = XmlUtility.validateNoNamespace(System.in);
        	//Document doc = XmlUtility.parse(new File("part3.public.test.input.xml"));
        	results = XmlUtility.getDocumentBuilder().newDocument();
        
        	Element commandNode = doc.getDocumentElement();
        	/* ===================================================================================================== */
			/* Documents variables
			/* ===================================================================================================== */		
			String localWidth = commandNode.getAttribute("localSpatialWidth");				/* Local X for PRQT.	 */
			String localHeight = commandNode.getAttribute("localSpatialHeight");			/* Local Y for PRQT.	 */
			String remoteWidth = commandNode.getAttribute("remoteSpatialWidth");			/* Remote X for PMQT.	 */
			String remoteHeight = commandNode.getAttribute("remoteSpatialHeight");			/* Remote Y for PMQT.	 */
			String g = commandNode.getAttribute("g");										/* G for AvlGTree. 		 */
			String pmOrder = commandNode.getAttribute("pmOrder");							/* PM order for PMQT.	 */
																							/* ===================== */
			int remoteSpatialWidth = Integer.parseInt(remoteWidth);
			int remoteSpatialHeight = Integer.parseInt(remoteHeight);
			int localSpatialWidth = Integer.parseInt(localWidth); 
			int localSpatialHeight = Integer.parseInt(localHeight);
			pmOrderForTree = Integer.parseInt(pmOrder);
			/* Instantiate the remote map. */
			if (!remoteWidth.equals("") && !remoteHeight.equals("")) {
				remoteMap.initialize(remoteSpatialWidth, remoteSpatialHeight, localSpatialWidth, localSpatialHeight,
						pmOrderForTree);
			} else {
				throw new SAXException();
			}
			
			/* Instantiate the AvlGTree. */
			if (!g.equals("")) {
				avlgTree = new AvlGTree<String, Point2D.Float>(Comparators.sortNames, Integer.parseInt(g));
			} else {
				throw new SAXException();
			}
			
			/* Instantiate the PMOrder. */
			if (!pmOrder.equals("")) {
				if (pmOrder.equals("3")) pmOrderForTree = 3;
				else if (pmOrder.equals("1")) pmOrderForTree = 1;
				else throw new SAXException();
			} else {
				pmOrderForTree = 3;
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
        				String localX = commandNode.getAttribute("localX");
        				String localY = commandNode.getAttribute("localY");
        				String remoteX = commandNode.getAttribute("remoteX");
        				String remoteY = commandNode.getAttribute("remoteY");
        				String id = commandNode.getAttribute("id");
        				Float localXVal = Float.parseFloat(localX);
        				Float localYVal = Float.parseFloat(localY);
        				Float remoteXVal = Float.parseFloat(remoteX);
        				Float remoteYVal = Float.parseFloat(remoteY);
        				/* ========================================================================================= */
        				/* Insert city into the Dictionary, and AvlGTree. City is not isolated because it is not in
        				/* the map. But when it is mapped without map road, it will be isolated.
        				/* ========================================================================================= */				
        				if (!name.equals("") && !color.equals("") && !radius.equals("") && !localX.equals("") && 
        						!localY.equals("") && !remoteX.equals("") && !remoteY.equals("")) {
        					City c = new City(name, localXVal, localYVal, remoteXVal, remoteYVal, radius, color);
        					/* XML Handler */
            				root.appendChild(XMLHandler.createCity(results, library, name, color, radius, localX,
            						localY, remoteX, remoteY, id, airports));	
            				/* Coordinate for AvlGTree. */
            				Point2D.Float coorLocal = new Point2D.Float(localXVal, localYVal);
            				Point2D.Float coorRemote = new Point2D.Float(remoteXVal, remoteYVal);
            				
            				/* ===================================================================================== */
            				/* Checks if there is any errors in the regex of the variables.
            				/* ===================================================================================== */
            				Lister.checkErrorPattern(name);
            				Lister.checkErrorPattern(color);
            				Lister.checkErrorNumber(id);
            				Lister.checkErrorNumber(localX);
            				Lister.checkErrorNumber(localY);
            				Lister.checkErrorNumber(remoteX);
            				Lister.checkErrorNumber(remoteY);
            				
            				/* Checks for duplicates. */
            				if (
            						/* City duplicate coordinates. */
            						!library.checkDuplicateCoordinates(coorRemote, coorLocal) &&
            						/* City dulpicate name. */
            						!library.checkDuplicateName(name) && 
            						/* Airport duplicate name. */
            						!airports.checkDuplicateName(name) && 
            						/* Airport duplicate coordinates. */
            						!airports.checkDuplicateCoordinate(coorRemote, coorLocal)
            					) {
            					/* ================================================================================= */
                				/* These also checks for duplicate name & coordinates for terminals referenced 
            					/* in aiports. Airports have built-in checker for duplicate coordinates for Terminals.
            					/* Then, add the City into the AvlGTree and the Dictionary.
              					/* ================================================================================= */
                				avlgTree.put(name, coorLocal);
                				library.createCity(c);
            				}				
        				} else {
        					throw new SAXException();
        				}	
        			}
        			/* ============================================================================================= */
        			/* List the cities in the dictionary. 
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("listCities")) {
        				String sortBy = commandNode.getAttribute("sortBy");
        				String id = commandNode.getAttribute("id");
        				
        				/* Checks for SAXException. */
        				if (sortBy.equals("") || id.equals("")) throw new SAXException();
        				
        				/* Checks for sortBy and ID error. */
        				Lister.checkErrorPattern(sortBy);
        				Lister.checkErrorNumber(id);
        				
        				/* XML Handler */
        				root.appendChild(XMLHandler.listCities(results, library, commandNode));
        			}
        			/* ============================================================================================= */
        			/* Clear all structure of Dictionary, PRQuadTree, AvlGTree, and PMQuadtree.
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("clearAll")) {
        				/* Checks for ID error. */
        				String id = commandNode.getAttribute("id");
        				if (id.equals("")) throw new SAXException();
        				Lister.checkErrorNumber(id);
        				
        				/* Clears PRQuadtree / the Remote Map. */
        				remoteMap.clear();       			
        				/* Clears Dictionary. */
        				library.clearAll();
        				/* Clears AvlGTree. */
        				avlgTree.clear();
        				/* XML Handler */
        				root.appendChild(XMLHandler.clearAll(results, id));
        			}
        			/* ============================================================================================= */
        			/* Delete the city from the library. If it is mapped, delete it too. 
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("deleteCity")) {
        				/* Name for the deleted City. */
        				String name = commandNode.getAttribute("name");
        				String id = commandNode.getAttribute("id");
        				/* Checks if the name or ID contains error. */
        				if (name.equals("") || id.equals("")) throw new SAXException();
        				Lister.checkErrorPattern(name);
        				Lister.checkErrorNumber(id);
        				
        				/* XML Handler */
        				root.appendChild(XMLHandler.deleteCity(results, library, name, remoteMap, id));
        				
        				/* City to be deleted. */
        				City city = library.cityList.get(name);
        				if (city != null) {
        					/* Delete from the remoteMap. */
        					if (library.checkMapped(name)) {
        						/* ================================================================================= */
        						/* This will remove city from any local maps and remote maps as well as the 
        						/* Roads that connect them as well as any City that do not have any road connected 
        						/* to them as a result of this deletion. 
        						/* ==================================================================================*/
        						remoteMap.removeCity(city);
        					}
        					/* Then, remove them from the Dictionary and the AvlGTree. */
        					library.deleteAllConnectionsTo(city);
        					library.deleteCity(city);
        					avlgTree.remove(name, city.getLocal());
        				}
        			}
        			/* ============================================================================================= */
        			/* Maps the city into the PRQuadTree, and the PMQuadtree (as an isolated city) 
        			/* and Library.insideMap.
        			/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("mapCity")) {
        				/* Get the name of the city & id tag. */
        				String name = commandNode.getAttribute("name");
        				String id = commandNode.getAttribute("id");
        				/* Checks if the name contains error. */
        				Lister.checkErrorPattern(name);
        				Lister.checkErrorNumber(id);
        				/* ========================================================================================= */
            			/* Checks if the city is created and if the city fulfills the remote and local bounds.
            			/* ========================================================================================= */		
        				if (name.equals("") || id.equals("")) {
        					throw new SAXException();
        				} else {
        					/* XML Handler. */
        					root.appendChild(XMLHandler.mapCity(results, library, name, remoteMap, id));
            				if (library.checkDuplicateName(name)) {
            					if (!library.checkMapped(name)) {
            						City target = library.cityList.get(name);
            						if (
            								0 <= target.getRemoteX() &&
            								target.getRemoteX() < remoteSpatialWidth &&
            								0 <= target.getRemoteY() && 
            								target.getRemoteY() < remoteSpatialHeight &&
            								0 <= target.getLocalX() &&
            								target.getLocalX() <= localSpatialWidth &&
            								0 <= target.getLocalY() &&
            								target.getLocalY() <= localSpatialHeight
            								
            							) {
                						remoteMap.addCity(target);
                						library.insideMap.add(target);
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
        				Lister.checkErrorNumber(id);
        				/* Unmap it from PRQuadTree & PMQuadTree. */
        				if (name.equals("") || id.equals("")) {
        					throw new SAXException();
        				} else {
        					/* XML Handler. */
        					root.appendChild(XMLHandler.unmapCity(results, library, name, remoteMap, id));
            				if (library.checkDuplicateName(name)) {
            					if (library.checkMapped(name)) {
            						/* From PRQuadTree */
                					remoteMap.removeCity(library.cityList.get(name));
                					/* From Library map list. */
                    				library.insideMap.remove(library.cityList.get(name));
            					}
            				}
        				}
        			}
        			/* ============================================================================================= */
        			/* Prints PRQuadtree. As in patch v3.0, this does nothing.
        			/* ============================================================================================= */			
//        			else if (commandNode.getNodeName().equals("printPRQuadtree")) {
//        				/* Get id tag */
//        				String id = commandNode.getAttribute("id");
//        				/* Prints it. */
//        				root.appendChild(XMLHandler.printPRQuadtree(results, library, remoteMap, id));
//        			}
        			/* ============================================================================================= */
        			/* Save the Map into a file.
        			/* ============================================================================================= */			
        			else if (commandNode.getNodeName().equals("saveMap")) {
        				String save_name = commandNode.getAttribute("name");
        				String remoteX = commandNode.getAttribute("remoteX");
        				String remoteY = commandNode.getAttribute("remoteY");
        				String id = commandNode.getAttribute("id");
        				if (save_name.equals("") || remoteX.equals("") || remoteY.equals("") || id.equals("")) {
        					throw new SAXException();
        				} else {
        					float rX = Float.parseFloat(remoteX);
        					float rY = Float.parseFloat(remoteY);
        					CanvasPlus canvas = new CanvasPlus("MeeshQuest");
        					canvas.setFrameSize(Integer.parseInt(localWidth), Integer.parseInt(localHeight));
        					/* New white square.  */
        					canvas.addRectangle(0.0, 0.0, localSpatialWidth, localSpatialHeight, Color.WHITE, true);
        					canvas.addRectangle(0.0, 0.0, localSpatialWidth, localSpatialHeight, Color.GRAY, false);
        					/* XML Handler. */
        					root.appendChild(XMLHandler.saveMap(results, save_name, id, remoteX, remoteY));
        					/* Draw the map if it is not empty. */
        					Metropole m = remoteMap.getMetropole(rX, rY);
        					if (
        							m != null &&
        							m.getMetropole().getRoot().getNodeType() != PMType.WHITE && 
        							remoteMap != null && 
        							library.insideMap.isEmpty() == false
        						) {
        						Drawer.processMap(canvas, library);
        					} 
        					if (m != null) {
        						TreeSet<City> cities = m.getCities();
        						if (cities != null) {
        							Iterator<City> iter = cities.iterator();
            						while (iter.hasNext()) {
                						City curr = iter.next();
                						TreeSet<Road> roads = curr.getRoadList();
                						Iterator<Road> iter2 = roads.iterator();
                						while (iter2.hasNext()) {
                							Road current= iter2.next();
                							double x1 = current.getX1();
                							double y1 = current.getY1();
                							double x2 = current.getX2();
                							double y2 = current.getY2();
                							canvas.addLine(x1, y1, x2, y2, Color.BLACK);
                						}
                					}
                					/* Save the map and close it. */
                					canvas.save(save_name);
                					canvas.dispose();
        						}
            					
        					}
        					
        				}
        			/* ============================================================================================= */
            		/* Range the cities in the PMQuadtree within the area.
            		/* ============================================================================================= */			
        			} else if (commandNode.getNodeName().equals("globalRangeCities")) {
        				/* Cities that are in range sorted by name reversed. */
        				TreeSet<City> inRange = new TreeSet<City>(Comparators.sortCities);
        				/* ========================================================================================= */
            			/* Variables.
            			/* ========================================================================================= */		
        				String remoteX = commandNode.getAttribute("remoteX");
        				String remoteY = commandNode.getAttribute("remoteY");
        				String radiusCircle = commandNode.getAttribute("radius");
        				String id = commandNode.getAttribute("id");
        				Lister.checkErrorNumber(remoteX);
        				Lister.checkErrorNumber(remoteY);
        				Lister.checkErrorNumber(radiusCircle);
        				Lister.checkErrorNumber(id);
        				/* Checks if it is within bounds and checks for save map. */
        				if (remoteX.equals("") || remoteY.equals("") || radiusCircle.equals("") || id.equals("")) {
        					throw new SAXException();				
        				} else {
        					/* Find everything within range with the Library.insideMap. */
        					float x = Float.parseFloat(remoteX);
        					float y = Float.parseFloat(remoteY);
        					float r = Float.parseFloat(radiusCircle);
            				inRange = Lister.findInRange(remoteMap, x, y, r, library);
            				root.appendChild(XMLHandler.globalRangeCities(results, inRange, remoteX, remoteY, 
            						radiusCircle, id));	
        					
        				}    				
        			/* ============================================================================================= */
            		/* Checks the nearest city to a point. 
            		/* ============================================================================================= */			
        			} else if (commandNode.getNodeName().equals("nearestCity")) {
        				/* Variables and get id tag */
        				String localX = commandNode.getAttribute("localX");
        				String localY = commandNode.getAttribute("localY");
        				String remoteX = commandNode.getAttribute("remoteX");
        				String remoteY = commandNode.getAttribute("remoteY");
        				String id = commandNode.getAttribute("id");
        				/* Checks for error. */
        				Lister.checkErrorNumber(localX);
        				Lister.checkErrorNumber(localY);
        				Lister.checkErrorNumber(remoteX);
        				Lister.checkErrorNumber(remoteY);
        				Lister.checkErrorNumber(id);
        				/* Use lister to check in XML. */
        				if (
        						localX.equals("") || 
        						localY.equals("") || 
        						remoteX.equals("") || 
        						remoteY.equals("") || 
        						id.equals("")
        					) {
        					throw new SAXException();
        				} else {
        					/* XML Handler. */
        					root.appendChild(XMLHandler.nearestCity(results, library, localX, localY, 
        							remoteX, remoteY, remoteMap, id));
        				}
        			/* ============================================================================================= */
            		/* Prints the AvlGTree.
            		/* ============================================================================================= */		
        			} else if (commandNode.getNodeName().equals("printAvlTree")) {
        				String id = commandNode.getAttribute("id");
        				Lister.checkErrorNumber(id);
        				if (id.equals("")) {
        					throw new SAXException();
        				} 
        				/* XML Handler */
        				root.appendChild(XMLHandler.printAvlTree(results, avlgTree, id));
        			/* ============================================================================================= */
            		/* Maps the road into the PMQuadTree as well as the two cities (as Non-Isolated).
            		/* ============================================================================================= */		
        			} else if (commandNode.getNodeName().equals("mapRoad")) {
        				/* Get the variables and id tag. */
        				String s = commandNode.getAttribute("start");
        				String e = commandNode.getAttribute("end");
        				String id = commandNode.getAttribute("id");        				
        				if (s.equals("") || e.equals("") || id.equals("")) throw new SAXException();
        				Lister.checkErrorPattern(s);
        				Lister.checkErrorPattern(e);
        				Lister.checkErrorNumber(id);
        				/* XML Handler. */
        				root.appendChild(XMLHandler.mapRoad(results, remoteMap, s, e, id, library, remoteSpatialWidth, 
        						remoteSpatialHeight, localSpatialWidth, localSpatialHeight));
        				if (
        						library.checkDuplicateName(s) && 
        						library.checkDuplicateName(e)
        					) {
        					if (
        							!s.equals(e)
        						) {
        						City start = library.cityList.get(s);
        						City end = library.cityList.get(e);
        						if (start != null && end != null) {
        							Road r = null;
            						r = new Road(start, end);
            						Road r2 = new Road(end, start);
            						boolean allow = true;
            						
            						if (s.equals("") || !library.checkDuplicateName(s)) allow = false;
            						else if (e.equals("") || !library.checkDuplicateName(e)) allow = false;
            						else if (s.equals(e) && !s.equals("") && !e.equals("")) allow = false;
            						else if (start != null && end != null && 
            								!start.isInSameMetropole(end)) allow = false;
            						else if (!remoteMap.checkCityInBound(start) && 
            								!remoteMap.checkCityInBound(end)) allow = false;
            						else if (remoteMap.checkRoadIsMapped(r)) allow = false;
            						else if (remoteMap.roadIntersectAnother(r)) allow = false;
            						else if (remoteMap.roadViolatesPMRules(r, library)) allow = false;
            						
            						if (allow == true) {
            							remoteMap.addCity(start);
										library.insideMap.add(start);      
										remoteMap.addCity(end);
										library.insideMap.add(end);   
										library.insertRoadToCity(r);
										remoteMap.addRoad(r);
            						}

        						}
        						
        					}
        				}  
        			}
        			/* ============================================================================================= */
            		/* Gets the shortest part travelled from a city to another city.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("mapAirport")) {
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
        				float lX = Float.parseFloat(localX);
        				float lY = Float.parseFloat(localY);
        				float rX = Float.parseFloat(remoteX);
        				float rY = Float.parseFloat(remoteY);
        				float tX = Float.parseFloat(terminalX);
        				float tY = Float.parseFloat(terminalY);
        				if (
        						name.equals("") || 
        						localX.equals("") || 
        						localY.equals("") || 
        						remoteX.equals("") || 
        						remoteY.equals("") || 
        						terminalName.equals("") ||
        						terminalX.equals("") ||
        						id.equals("")
        					) {
        					throw new SAXException();
        				}
        				Lister.checkErrorPattern(name);
        				Lister.checkErrorNumber(localX);
        				Lister.checkErrorNumber(localY);
        				Lister.checkErrorNumber(remoteX);
        				Lister.checkErrorNumber(remoteY);
        				Lister.checkErrorPattern(terminalName);
        				Lister.checkErrorNumber(terminalX);
        				Lister.checkErrorPattern(terminalCity);
        				Lister.checkErrorNumber(terminalY);
        				Lister.checkErrorNumber(id);
        				City tC = library.cityList.get(terminalCity);
        				Airport airport = new Airport(name, lX, lY, rX, rY, terminalName, tX, tY, tC);
        				Terminal terminal = new Terminal(terminalName, rX, rY, tX, tY, tC, name);
        				/* XML Handler. */
        				root.appendChild(XMLHandler.mapAirport(results, remoteMap, 
        						commandNode, library, airports, airport));
        				Point2D.Float local = new Point2D.Float(lX, lY);
        				Point2D.Float remote = new Point2D.Float(rX, rY);
        				Point2D.Float localT = new Point2D.Float(tX, tY);
        				City t = new City(terminalName, tX, tY, rX, rY, "noRadius", "noColor", name, terminalCity);
        				Road terminalToCity = null;
        				if (t != null && tC != null) terminalToCity = new Road(t, tC);
        				if (
        						!library.checkDuplicateName(name) &&
        						!airports.checkDuplicateName(name) &&
        						!library.checkDuplicateCoordinates(remote, local) &&
        						!airports.checkDuplicateCoordinate(remote, local) &&
        						!airports.checkDuplicateCoordinate(remote, localT) &&
        						!library.checkDuplicateCoordinates(remote, localT) &&
        						!remoteMap.checkAirportOutOfBounds(airport) &&
        						!airports.checkTerminalName(name) &&
        						!library.checkDuplicateName(terminalName) &&
        						!airports.checkDuplicateName(terminalName) &&
        						!airports.checkTerminalCoordinates(remote, local) &&
        						!airports.terminalOutOfBounds(localSpatialWidth, localSpatialHeight, 
        								remoteSpatialWidth, remoteSpatialHeight, terminal) &&
        						airports.checkConnectingCityExists(airport, library) &&
        						airports.checkConnectingIsMapped(airport, library) &&
        						airports.checkConnectingCityInSameMetropole(airport, library) &&
        						!remoteMap.airportViolatesPMRules(airport) &&
        						!remoteMap.terminalViolatesPMRules(terminal) &&
        						!remoteMap.roadIntersectAnother(terminalToCity)        						
        					) {
        					airports.mappedAirports.put(name, airport);
        					remoteMap.addAirport(airport);
        					remoteMap.addTerminal(airport.getTerminal().first());
        					remoteMap.addRoad(terminalToCity);
        				}
        			}
        			/* ============================================================================================= */
            		/* Maps the Terminal into the PRQT.
            		/* ============================================================================================= */		
        			else if (commandNode.getNodeName().equals("mapTerminal")) {
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
        				
        				if ( 
        						name.equals("") ||
        						localX.equals("") ||
        						localY.equals("") ||
        						remoteX.equals("") ||
        						remoteY.equals("") ||
        						cityName.equals("") ||
        						airportName.equals("") ||
        						id.equals("") 
        					) {
        					throw new SAXException();
        				}
        				Lister.checkErrorNumber(localX);
        				Lister.checkErrorNumber(localY);
        				Lister.checkErrorNumber(remoteX);
        				Lister.checkErrorNumber(remoteY);
        				Lister.checkErrorNumber(id);
        				Lister.checkErrorPattern(name);
        				Lister.checkErrorPattern(cityName);
        				Lister.checkErrorPattern(airportName);
        					
        				
        				City city = library.cityList.get(cityName);
        				Airport airport = airports.getAirport(airportName);
        				Terminal terminal = new Terminal(name, rX, rY, lX, lY, city, airportName);
        				Road r = null;
        				if (city != null) {
        					r = new Road(terminal, city);
        				}
        				/* XML Handler. */
        				root.appendChild(XMLHandler.mapTerminal(results, airports, library, 
        						terminal, remoteMap, commandNode));
        				if (
        						!library.checkDuplicateName(name) &&
        						!airports.checkTerminalName(name) &&
        						!library.checkDuplicateCoordinates(remote, local) &&
        						!library.checkDuplicateCoordinates(remote, local) &&
        						!airports.checkTerminalCoordinates(remote, local) &&
        						!airports.terminalOutOfBounds(localSpatialWidth, localSpatialHeight, 
        								remoteSpatialWidth, remoteSpatialHeight, terminal) &&
        						airports.getAirport(airportName) != null &&
        						airport.inSameMetropoleAsTerminal(terminal) &&
        						library.checkDuplicateName(cityName) &&
        						terminal.checkCityInSameMetropole(city) &&
        						library.checkMapped(cityName) &&
        						!remoteMap.terminalViolatesPMRules(terminal) &&
        						!remoteMap.roadIntersectAnother(r)
        					) {
        					airport.insertTerminal(terminal);
        					remoteMap.addTerminal(terminal);
        					remoteMap.addRoad(r);
        				}
        				
        			}
        			/* ============================================================================================= */
            		/* List all the roads present in PMQuadTree.
            		/* ============================================================================================= */		
//        			else if (commandNode.getNodeName().equals("rangeRoads")) {
//        				/* Variable & get id tag. */
//        				String x = commandNode.getAttribute("x");
//        				String y = commandNode.getAttribute("y");
//        				String radius = commandNode.getAttribute("radius");
//        				String saveName = commandNode.getAttribute("saveMap");
//        				String id = commandNode.getAttribute("id");
//        				
//        				if (x.equals("") || y.equals("") || radius.equals("")) throw new SAXException();
//        				Lister.checkErrorNumber(x);
//        				Lister.checkErrorNumber(y);
//        				Lister.checkErrorNumber(radius);
//        				
//        				/* If savemap is present, draw the map and save it along with the road. */
//        				if (saveName.equals("") == false) {
//        					Drawer.drawSpatialMap(saveName, radius, localWidth, localHeight, x, y, library, pmquadtree, map);
//        				}
//        				/* XML. */
//        				root.appendChild(XMLHandler.rangeRoads(results, pmquadtree, x, y, radius, saveName, id));
//        				
//        			}
        			/* ============================================================================================= */
            		/* Unmaps a Road.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("unmapRoad")) {
        				String start = commandNode.getAttribute("start");
        				String end = commandNode.getAttribute("end");
        				String id = commandNode.getAttribute("id");
        				if (start.equals("") || end.equals("") || id.equals("")) throw new SAXException();
        				Lister.checkErrorNumber(id);
        				Lister.checkErrorPattern(start);
        				Lister.checkErrorPattern(end);
        				City s = library.cityList.get(start);
        				City e = library.cityList.get(end);
        				Road r = null;
        				if (s != null && e != null) r = new Road(s, e);
        				
        				/* XML Handler. */
        				root.appendChild(XMLHandler.unmapRoad(results, remoteMap, start, end, id, library));
        				if (
        						s != null &&
        						e != null &&
        						!s.equals(e) &&
        						remoteMap.checkRoadIsMapped(r)
        					) {
        					remoteMap.removeRoad(r);
        					library.removeRoad(r);
        				}
        			}
        			/* ============================================================================================= */
            		/* Unmaps Airport.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("unmapAirport")) {
        				String name = commandNode.getAttribute("name");
        				String id = commandNode.getAttribute("id");
        				if (name.equals("") || id.equals("")) throw new SAXException();
        				Lister.checkErrorNumber(id);
        				Lister.checkErrorPattern(name);
  
        				/* XML Handler. */
        				root.appendChild((XMLHandler.unmapAirport(results, remoteMap, commandNode, airports)));
        				Airport possible = airports.getAirport(name);
        				if (possible != null) {
        					Iterator<Terminal> iter = possible.getTerminal().iterator();
        					while (iter.hasNext()) {
        						Terminal t = iter.next();
        						remoteMap.removeTerminal(t);
        					}
        					remoteMap.removeAirport(possible);
        					airports.mappedAirports.remove(name);
        				}        				
        			}
        			/* ============================================================================================= */
            		/* Unmaps Terminal.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("unmapTerminal")) {
        				String name = commandNode.getAttribute("name");
        				String id = commandNode.getAttribute("id");
        				if (name.equals("") || id.equals("")) throw new SAXException();
        				Lister.checkErrorNumber(id);
        				Lister.checkErrorPattern(name);
  
        				/* XML Handler. */
        				root.appendChild((XMLHandler.unmapTerminal(results, remoteMap, commandNode, airports)));
        				Terminal possible = airports.getTerminal(name);
        				if (possible != null) {
        					Airport a = airports.getAirport(possible.airportOrigin());
        					a.getTerminal().remove(possible);
        					if (a.getTerminal().isEmpty()) {
        						remoteMap.removeAirport(a);
        						airports.mappedAirports.remove(a.getName());
        					}
        					remoteMap.removeTerminal(possible);
        				}        				
        			}
        			/* ============================================================================================= */
            		/* Prints the PMQuadTree.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("printPMQuadtree")) {
        				String id = commandNode.getAttribute("id");
        				String remoteX = commandNode.getAttribute("remoteX");
        				String remoteY = commandNode.getAttribute("remoteY");
        				Lister.checkErrorNumber(id);
        				Lister.checkErrorNumber(remoteX);
        				Lister.checkErrorNumber(remoteY);
        				if (id.equals("") || remoteX.equals("") || remoteY.equals("")) throw new SAXException();
        				root.appendChild((XMLHandler.printPMQuadtree(results, remoteMap, id, pmOrder, commandNode)));
        			}
        			/* ============================================================================================= */
            		/* Prints the PMQuadTree.
            		/* ============================================================================================= */	
        			else if (commandNode.getNodeName().equals("mst")) {
        				String id = commandNode.getAttribute("id");
        				String start = commandNode.getAttribute("start");
        				Lister.checkErrorNumber(id);
        				Lister.checkErrorPattern(start);
        				if (id.equals("") || start.equals("")) throw new SAXException();
        				root.appendChild((XMLHandler.mst(results, remoteMap, id, commandNode)));
        			}
        			
        			/* ============================================================================================= */
            		/* Return the nearest isolated city from a specified point.
            		/* ============================================================================================= */	
//        			else if (commandNode.getNodeName().equals("nearestIsolatedCity")) {
//        				String id = commandNode.getAttribute("id");
//        				String x = commandNode.getAttribute("x");
//        				String y = commandNode.getAttribute("y");
//        				
//        				if (x.equals("") || y.equals("")) throw new SAXException();
//        				
//        				Lister.checkErrorNumber(x);
//        				Lister.checkErrorNumber(y);
//        				root.appendChild(XMLHandler.nearestIsolatedCity(results, library, id, x, y));
//        			}
        			/* ============================================================================================= */
            		/* Return the nearest road from a point.
            		/* ============================================================================================= */	
//        			else if (commandNode.getNodeName().equals("nearestRoad")) {
//        				String x = commandNode.getAttribute("x");
//        				String y = commandNode.getAttribute("y");
//        				String id = commandNode.getAttribute("id");
//        				if (x.equals("") || y.equals("")) throw new SAXException();
//        				Lister.checkErrorNumber(x);
//        				Lister.checkErrorNumber(y);
//        				root.appendChild(XMLHandler.nearestRoad(results, pmquadtree, id, x, y));
//        			}
        			/* ============================================================================================= */
            		/* Finds nearest city from a road.
            		/* ============================================================================================= */	
//        			else if (commandNode.getNodeName().equals("nearestCityToRoad")) {
//        				String start = commandNode.getAttribute("start");
//        				String end = commandNode.getAttribute("end");
//        				String id = commandNode.getAttribute("id");
//        				if (start.equals("") || end.equals("")) throw new SAXException();
//        				Lister.checkErrorPattern(start);
//        				Lister.checkErrorPattern(end);
//        				
//        				root.appendChild(XMLHandler.nearestCityToRoad(results, pmquadtree, id, start, end, library));
//        			}
        			/* ============================================================================================= */
            		/* Gets the shortest part travelled from a city to another city.
            		/* ============================================================================================= */	
//        			else if (commandNode.getNodeName().equals("shortestPath")) {
//        				String id = commandNode.getAttribute("id");
//        				String start = commandNode.getAttribute("start");
//        				String end = commandNode.getAttribute("end");
//        				String saveMap = commandNode.getAttribute("saveMap");
//        				String saveHTML = commandNode.getAttribute("saveHTML");
//        				Lister.checkErrorPattern(start);
//        				Lister.checkErrorPattern(end);
//        				
//        				if (saveMap.equals("") == false) {
//    						/* Draw the map and save it. */
//        					CanvasPlus canvas = new CanvasPlus("MeeshQuest");
//        					canvas.setFrameSize(Integer.parseInt(localWidth), Integer.parseInt(localHeight));
//        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(localWidth), Float.parseFloat(localHeight), Color.WHITE, true);
//        					canvas.addRectangle(0.0, 0.0, Float.parseFloat(localWidth), Float.parseFloat(localHeight), Color.BLACK, false);
//        					if (map.root.nodeType() != Type.WHITE && map != null && library.insideMap.isEmpty() == false) {
//        						Drawer.processMap(canvas, library);
//        					}             	
//        					canvas.save(saveMap);
//        					canvas.dispose();
//        				}
//        				
//        				root.appendChild(XMLHandler.shortestPath(results, pmquadtree, id, library, start, end, saveMap, saveHTML));
//        			}
        			
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
