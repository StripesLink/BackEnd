package edu.eci.escuelaing.StripesLink.Model;

import java.util.ArrayList;
import java.util.List;

public class Line {
	List<Point> points;

	String brushColor;

	int brushRadius;

	public Line() {
		points = new ArrayList<Point>();
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public String getBrushColor() {
		return brushColor;
	}

	public void setBrushColor(String brushColor) {
		this.brushColor = brushColor;
	}

	public int getBrushRadius() {
		return brushRadius;
	}

	public void setBrushRadius(int brushRadius) {
		this.brushRadius = brushRadius;
	}

}
