package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;

import cmsc420.meeshquest.part3.AirportList.Airport;

public class Terminal implements Comparable<Terminal> {
	
	private String terminalName;
	private float terminalRemoteX, terminalRemoteY, terminalLocalX, terminalLocalY;
	private City terminalCity;
	private String airport;

	public Terminal(String terminalName, float terminalRemoteX, float terminalRemoteY, float terminalLocalX, 
			float terminalLocalY, City terminalCity, String airport) {
		this.setTerminalCity(terminalCity);
		this.setTerminalName(terminalName);
		this.terminalRemoteX = terminalRemoteX;
		this.terminalRemoteY = terminalRemoteY;
		this.terminalLocalX = terminalLocalX;
		this.terminalLocalY = terminalLocalY;
		this.airport = airport;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public float getTerminalLocalX() {
		return terminalLocalX;
	}

	public float getTerminalLocalY() {
		return terminalLocalY;
	}

	public City getTerminalCity() {
		return terminalCity;
	}

	public void setTerminalCity(City terminalCity) {
		this.terminalCity = terminalCity;
	}
	public float getTerminalRemoteX() {
		return terminalRemoteX;
	}
	public float getTerminalRemoteY() {
		return terminalRemoteY;
	}
	public Point2D.Float getLocalCoordinates() {
		return new Point2D.Float(terminalLocalX, terminalLocalY);
	}

	public boolean checkCityInSameMetropole(City city) {
		if (this.terminalRemoteX == city.getRemoteX() && this.terminalRemoteY == city.getRemoteY()) return true;
		return false;
	}

	public String airportOrigin() {
		return this.airport;
	}

	public City toCity() {
		City dummy = new City(terminalName, terminalLocalX, terminalLocalY, 
				terminalRemoteX, terminalRemoteY, "noRadius", "noColor", airport, terminalCity.getName());
		return dummy;
	}

	public int compareTo(Terminal arg0) {
		return this.getTerminalName().compareTo(arg0.getTerminalName());
	}
}
