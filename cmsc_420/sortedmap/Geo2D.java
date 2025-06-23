package cmsc420.sortedmap;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import cmsc420.meeshquest.part2.City;
import cmsc420.meeshquest.part2.Comparators;

public class Geo2D implements Comparable<Geo2D> {
	City city;
	Road road;
	
	public Geo2D(City city) {
		this.city = city;
	}
	public Geo2D(Road road) {
		this.road = road;
	}
	
	public boolean isRoad() { return road != null && city == null; }
	public boolean isCity() { return city != null && road == null; }
	public Road getRoad() { 
		if (road != null) return road; 
		throw new IllegalArgumentException();
	}
	public City getCity() { 
		if (city != null) return city; 
		throw new IllegalArgumentException();
	}
	
	public boolean intersects(Ellipse2D.Float circle) {
		if (road != null) {
			Point2D.Float point1 = road.getStartCity().getCoordinates();
			Point2D.Float point2 = road.getEndCity().getCoordinates();
			if (circle.intersects(point1.getX(), point1.getY(), 0, 0) || circle.intersects(point2.getX(), point2.getY(), 0, 0)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int compareTo(Geo2D arg0) {
		if (this.isCity() && arg0.isCity()) {
			return arg0.getCity().getName().compareTo(this.getCity().getName());
		} else if (this.isRoad() && arg0.isRoad()) {
			return Comparators.sortRoads.compare(this.road, arg0.getRoad());
		} else if (this.isCity() && arg0.isRoad()) {
			return -1;
		} else if (this.isRoad() && arg0.isCity()) {
			return 1;
		} else {
			return 0;
		}
	}
}
