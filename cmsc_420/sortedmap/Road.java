package cmsc420.sortedmap;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import cmsc420.geom.Circle2D;
import cmsc420.meeshquest.part2.City;
import cmsc420.meeshquest.part2.Comparators;

public class Road extends Line2D.Float implements Comparable<Road> {
	City start, end;
	/* Constructor. */
	public Road(City start, City end) {
		super(start.x, start.y, end.x, end.y);
		int cmp = Comparators.sortNames.compare(start.getName(), end.getName());
		if (cmp > 0) {
			this.start = start;
			this.end = end;
		} else {
			this.start = end;
			this.end = start;
		}
		
	}
	/* Returns starting city. */
	public City getStartCity() { return start; }
	/* Returns ending city. */
	public City getEndCity() { return end; }
	/* Returns starting city name. */
	public String getStartName() { if (start != null) return start.getName(); else return null; }
	/* Returns ending city name. */
	public String getEndName() { if (end != null) return end.getName(); else return null; }
	/* Returns if road intersect a circle. */
	public boolean intersectCircle(Circle2D.Float circle) {
		Point2D.Float midpoint = new Point2D.Float((float) circle.getCenterX(), (float) circle.getCenterY());
		Line2D.Float road = new Line2D.Float(start.x, start.y, end.x, end.y);
		double shortestDistance = road.ptSegDist(midpoint);
		if (shortestDistance <= circle.getRadius()) {
			return true;
		} else {
			return false;
		}
	}
	/* Checks if the road intersects the rectangle. */
	public boolean intersectSquare(Rectangle2D square) {
		return square.intersectsLine(this);
	}
	public boolean isSameAs(Road road) {
		City start2 = road.start;
		City end2 = road.end;
		String start2Name = start2.getName();
		String end2Name = end2.getName();
		String start1Name = this.start.getName();
		String end1Name = this.end.getName();
		
		if (!start2Name.equals(end2Name) && !(start1Name).equals(end2Name)) {
			if (start2Name.equals(start1Name) && end2Name.equals(end1Name)) {
				return true;
			} else if (start2Name.equals(end1Name) && start1Name.equals(end2Name)) {
				return true;
			}
		}
		return false;		
	}
	
	public boolean insideBoundary(int width, int height) {
		return 0 <= start.x && start.x <= width && 0 <= start.y && start.y <= height &&
				0 <= end.x && end.x <= width && 0 <= end.y && end.y <= height;
	}
	
	public double distanceBetween() {
		return Point2D.distance(start.x, start.y, end.x, end.y);
	}
	
	public Point2D.Float midPoint() {
		return new Point2D.Float((start.x + end.x) / 2, (start.y + end.y) / 2);
	}
	@Override
	public int compareTo(Road arg0) {
		return Comparators.sortRoads.compare(arg0, this);
	}
}
