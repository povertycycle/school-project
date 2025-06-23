package cmsc420.sortedmap;

import cmsc420.meeshquest.part3.AirportList.Airport;
import cmsc420.meeshquest.part3.City;
import cmsc420.meeshquest.part3.Terminal;

public class Vertex implements Comparable<Vertex> {
	City c;
	Airport a;
	Terminal t;
	boolean visited;
	Vertex(City c) {
		this.c = c;
	}
	Vertex(Airport a) {
		this.a = a;
	}
	Vertex(Terminal t) {
		this.t = t;
	}
	
	public int compareTo(Vertex arg0) {
		float x1Local = 0, y1Local = 0, x2Local = 0, y2Local = 0, x1Remote = 0, y1Remote = 0, x2Remote = 0, y2Remote = 0;
		if (this.c != null) {
			x1Local = c.getLocalX();
			y1Local = c.getLocalY();
			x1Remote = c.getRemoteX();
			y1Remote = c.getRemoteY();
		} else if (this.a != null) {
			x1Local = a.getLocalX();
			y1Local = a.getLocalY();
			x1Remote = a.getRemoteX();
			y1Remote = a.getRemoteY();
		} else if (this.t != null) {
			x1Local = t.getTerminalLocalX();
			y1Local = t.getTerminalLocalY();
			x1Remote = t.getTerminalRemoteX();
			y1Remote = t.getTerminalRemoteY();
		}
		if (arg0.c != null) {
			x2Local = arg0.c.getLocalX();
			y2Local = arg0.c.getLocalY();
			x2Remote = arg0.c.getRemoteX();
			y2Remote = arg0.c.getRemoteY();
		} else if (arg0.a != null) {
			x2Local = arg0.a.getLocalX();
			y2Local = arg0.a.getLocalY();
			x2Remote = arg0.a.getRemoteX();
			y2Remote = arg0.a.getRemoteY();
		} else if (arg0.t != null) {
			x2Local = arg0.t.getTerminalLocalX();
			y2Local = arg0.t.getTerminalLocalY();
			x2Remote = arg0.t.getTerminalRemoteX();
			y2Remote = arg0.t.getTerminalRemoteY();
		}
		if (x2Remote == x1Remote && y2Remote == y1Remote) {
			if (x1Local == x2Local && y1Local == y2Local) return 0;
			else return -1;
		} else {
			return 1;
		}
	}
	public double getDistanceTo(Vertex arg0) {
		float x1Local = 0, y1Local = 0, x2Local = 0, y2Local = 0, x1Remote = 0, y1Remote = 0, x2Remote = 0, y2Remote = 0;
		if (this.c != null) {
			x1Local = c.getLocalX();
			y1Local = c.getLocalY();
			x1Remote = c.getRemoteX();
			y1Remote = c.getRemoteY();
		} else if (this.a != null) {
			x1Local = a.getLocalX();
			y1Local = a.getLocalY();
			x1Remote = a.getRemoteX();
			y1Remote = a.getRemoteY();
		} else if (this.t != null) {
			x1Local = t.getTerminalLocalX();
			y1Local = t.getTerminalLocalY();
			x1Remote = t.getTerminalRemoteX();
			y1Remote = t.getTerminalRemoteY();
		}
		if (arg0.c != null) {
			x2Local = arg0.c.getLocalX();
			y2Local = arg0.c.getLocalY();
			x2Remote = arg0.c.getRemoteX();
			y2Remote = arg0.c.getRemoteY();
		} else if (arg0.a != null) {
			x2Local = arg0.a.getLocalX();
			y2Local = arg0.a.getLocalY();
			x2Remote = arg0.a.getRemoteX();
			y2Remote = arg0.a.getRemoteY();
		} else if (arg0.t != null) {
			x2Local = arg0.t.getTerminalLocalX();
			y2Local = arg0.t.getTerminalLocalY();
			x2Remote = arg0.t.getTerminalRemoteX();
			y2Remote = arg0.t.getTerminalRemoteY();
		}
		if (x1Remote == x2Remote && y1Remote == y2Remote) {
			return Math.sqrt((Math.pow(x1Local - x2Local ,2) + Math.pow(y1Local - y2Local, 2)));
		} else {
			return Math.sqrt((Math.pow(x1Remote - x2Remote ,2) + Math.pow(y1Remote - y2Remote, 2)));
		}
	}
	public String getName() {
		if (c != null) return c.getName();
		else if (a != null) return a.getName();
		else return t.getTerminalName();
	}
}
