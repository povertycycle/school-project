package cmsc420.meeshquest.part2;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Circle2D;
import cmsc420.meeshquest.part2.Node.Type;
import cmsc420.sortedmap.Geo2D;
import cmsc420.sortedmap.PMQuadtree;
import cmsc420.sortedmap.Road;

public class Drawer {
	/* ============================================================================================================= */
	/* Draw all the cities into the canvas that are mapped.
	/* ============================================================================================================= */	
	public static void processMap(CanvasPlus canvas, Dictionary library) {
		Iterator<City> itr = library.insideMap.iterator();
		while (itr.hasNext()) {
			City current = itr.next();
			canvas.addPoint(current.getName(), current.x, current.y, Color.BLACK);
		}
	}
	/* ============================================================================================================= */
	/* Draw the map with all the cities and roads.
	/* ============================================================================================================= */	
	public static void drawSpatialMap(String saveName, String radius, String width, String height, String x, String y,
			Dictionary library, PMQuadtree pmquadtree, PRQT map) throws IOException {
		if (saveName.equals("") == false) {
			CanvasPlus canvas = new CanvasPlus("MeeshQuest");
			canvas.setFrameSize(Integer.parseInt(width), Integer.parseInt(height));
			canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.WHITE, true);
			canvas.addRectangle(0.0, 0.0, Float.parseFloat(width), Float.parseFloat(height), Color.GRAY, false);
			if (map.root.nodeType() != Type.WHITE && library.insideMap.isEmpty() == false) {
				processMap(canvas, library);
			}
			if (pmquadtree.roadListIsNotEmpty(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(radius))) {
				TreeSet<Geo2D> listOfGeometries = pmquadtree.root.getGeometries();
				if (!listOfGeometries.isEmpty()) { 
					Circle2D.Float circle = new Circle2D.Float(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(radius));
					for (int j = 0; j < listOfGeometries.size(); j++) {
						Iterator<Geo2D> iter = listOfGeometries.iterator();
						while (iter.hasNext()) {
							Geo2D curr = iter.next();
							if(curr.isRoad()) {
								if (curr.getRoad().intersectCircle(circle)) {
									Road r = curr.getRoad();
									canvas.addLine(r.x1, r.y1, r.x2, r.y2, Color.BLACK);
								}
							}
						}
					}
				}						
			}
			canvas.addCircle(Float.parseFloat(x), Float.parseFloat(y), 
					Float.parseFloat(radius), Color.BLUE, false);
			canvas.save(saveName);
			canvas.dispose();
		}
	}

}
