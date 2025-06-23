package cmsc420.sortedmap;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import cmsc420.geom.Circle2D;
import cmsc420.meeshquest.part3.City;
import cmsc420.meeshquest.part3.Comparators;
import cmsc420.meeshquest.part3.Terminal;

@SuppressWarnings("serial")
public class Road extends Line2D.Float implements Comparable<Road> {
	/* ============================================================================================================= */
	/* Fields.
	/* ============================================================================================================= */	
	City start, end;
	Terminal terminal;
	/* ============================================================================================================= */
	/* Constructors.
	/* ============================================================================================================= */	
	public Road(City start, City end) {
		super(start.getLocalX(), start.getLocalY(), end.getLocalX(), end.getLocalY());
		int cmp = Comparators.sortNames.compare(start.getName(), end.getName());
		if (cmp > 0) {
			this.start = start;
			this.end = end;
		} else {
			this.start = end;
			this.end = start;
		}
		terminal = null;
	}
	public Road(Terminal terminal, City terminalCity) {
		super(terminal.getTerminalLocalX(), terminal.getTerminalLocalY(), 
				terminalCity.getLocalX(), terminalCity.getLocalY());
		this.start = terminalCity;
		this.terminal = terminal;
		end = new City(terminal.getTerminalName(), terminal.getTerminalLocalX(), terminal.getTerminalLocalY(), 
				terminal.getTerminalRemoteX(), terminal.getTerminalRemoteY(), "noRadius", "noColor");
	}
	/* ============================================================================================================= */
	/* Public methods.
	/* ============================================================================================================= */	
	public Terminal getTerminal() { 
		return terminal; 
	}
	public City getStartCity() { 
		return start; 
	}
	public City getEndCity() { 
		return end; 
	}
	public String getStartName() { 
		if (start != null) return start.getName(); 
		else return null; 
	}
	public String getEndName() { 
		if (end != null) return end.getName(); 
		else return null; 
	}
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
	public boolean intersectSquare(Rectangle2D square) {
		return square.intersectsLine(this);
	}
	public Road getReversedCopy() {
		return new Road(this.end, this.start);
	}
	public boolean isSameAs(Road road) {
		City start2 = road.start;
		City end2 = road.end;
		String start2Name = start2.getName();
		String end2Name = end2.getName();
		String start1Name = this.start.getName();
		String end1Name = this.end.getName();
		
		if (start2Name.equals(start1Name) && end2Name.equals(end1Name)) return true;
		if (start2Name.equals(end1Name) && end2Name.equals(start1Name)) return true;
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
	public int compareTo(Road arg0) {
		return Comparators.sortRoads.compare(arg0, this);
	}
	public float getRemoteX() {
		if (start.getRemoteX() == end.getRemoteX()) return start.getRemoteX();
		else return -1;
	}
	public float getRemoteY() {
		if (start.getRemoteY() == end.getRemoteY()) return start.getRemoteY();
		else return -1;
	}
	public boolean hasCity(City city) {
		if (start.getName().equals(city.getName()) || end.getName().equals(city.getName())) {
			return true;
		}
		return false;
	}
	public boolean connectedTo(Road r) {
		City s1 = this.getStartCity();
		City e1 = this.getEndCity();
		City s2 = r.getStartCity();
		City e2 = r.getEndCity();
		if (s1.getLocalX() == s2.getLocalX() && s1.getLocalY() == s2.getLocalY()) return true;
		if (s1.getLocalX() == e2.getLocalX() && s1.getLocalY() == e2.getLocalY()) return true;
		if (e1.getLocalX() == s2.getLocalX() && e1.getLocalY() == s2.getLocalY()) return true;
		if (e1.getLocalX() == e2.getLocalX() && e1.getLocalY() == e2.getLocalY()) return true;
		return false;
	}
	public boolean IsEither(City curr) {
		if (this.getStartName().equals(curr.getName())) return true;
		if (this.getEndName().equals(curr.getName())) return true;
		return false;
	}
//	public boolean sameCityIntersect(Road r) {
//		City s1 = this.getStartCity();
//		City e1 = this.getEndCity();
//		City s2 = r.getStartCity();
//		City e2 = r.getEndCity();
//		City commonCity = findCommonCity(this, r);
//		if (commonCity != null) {
//			City a = null;
//			City b = null;
//			if (s1.getName().equals(commonCity.getName())) a = e1;
//			else a = s1;
//			if (s2.getName().equals(commonCity.getName())) b = e2;
//			else b = s2;
//			Arc2D.Double arc = new Arc2D.Double();
//			arc.setArcByTangent(a, commonCity, b, 1);
//			double angle = arc.getAngleExtent();
//			if (angle == 0 || angle == -180) return true;
//		}
//		return false;
//	}
	public static City findCommonCity(Road road, Road r) {
		City s1 = road.getStartCity();
		City e1 = road.getEndCity();
		City s2 = r.getStartCity();
		City e2 = r.getEndCity();
		if (s1.getName().equals(s2.getName())) return s1;
		if (s1.getName().equals(e2.getName())) return s1;
		if (e1.getName().equals(s2.getName())) return e1;
		if (e1.getName().equals(e2.getName())) return e1;
		return null;
	}
	public boolean intersectSection(Rectangle rectangle) {
		Line2D.Float a = new Line2D.Float((float) rectangle.getMinX(), (float) rectangle.getMinY(), 
				(float) rectangle.getMinX(), (float) rectangle.getMaxY());
		Line2D.Float b = new Line2D.Float((float) rectangle.getMinX(), (float) rectangle.getMaxY(), 
				(float) rectangle.getMaxX(), (float) rectangle.getMaxY());
		Line2D.Float c = new Line2D.Float((float) rectangle.getMinX(), (float) rectangle.getMinY(), 
				(float) rectangle.getMaxX(), (float) rectangle.getMinY());
		Line2D.Float d = new Line2D.Float((float) rectangle.getMaxX(), (float) rectangle.getMinY(), 
				(float) rectangle.getMaxX(), (float) rectangle.getMaxY());
		return this.intersectsLine(a) || this.intersectsLine(b) || this.intersectsLine(c) || this.intersectsLine(d);
	}
}
