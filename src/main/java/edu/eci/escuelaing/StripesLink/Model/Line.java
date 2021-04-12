package edu.eci.escuelaing.StripesLink.Model;

import java.util.ArrayList;
import java.util.List;

public class Line {
	List<Point> points;

	public Line() {
		points = new ArrayList<Point>();
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

}
