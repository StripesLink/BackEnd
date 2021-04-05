package edu.eci.escuelaing.StripesLink.Model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

	public String color;

	public List<Point> puntos;

	public String status;

	public Tablero(String color) {
		this.color = color;
		puntos = new ArrayList();
		status = "Created";
	}

	public String getNumero() {
		return color;
	}

	public void setNumero(String color) {
		this.color = color;
	}

	public List<Point> getPuntos() {
		return puntos;
	}

	public void setPuntos(List<Point> puntos) {
		this.puntos = puntos;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
