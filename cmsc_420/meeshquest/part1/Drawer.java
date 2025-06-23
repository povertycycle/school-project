package cmsc420.meeshquest.part1;

import java.awt.Color;
import java.util.Iterator;

import cmsc420.drawing.CanvasPlus;

public class Drawer {

	public static void processMap(CanvasPlus canvas, Dictionary library) {
		Iterator<City> itr = library.insideMap.iterator();
		while (itr.hasNext()) {
			City current = itr.next();
			canvas.addPoint(current.name, current.x, current.y, Color.BLACK);
		}
	}

}
