package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

public class City extends Point2D.Float {
	
	String name, color;
	String radius;
	
	/* Constructor */
	public City(String cityName, float x, float y, String r, String colour) {
		super(x, y);
		this.name = cityName;
		this.radius = r;
		this.color = colour;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getRadius() {
		return radius;
	}
	
	public Point2D.Float getCoordinates() {
		Point2D.Float coordinates = new Point2D.Float(this.x, this.y);
		return coordinates;
	}
	
	public int getIntX() {
		return (int) x;
	}
	
	public int getIntY() {
		return (int) y;
	}
		
}