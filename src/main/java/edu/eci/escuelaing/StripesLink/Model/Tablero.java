package edu.eci.escuelaing.StripesLink.Model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

	public String color;

	public List<Line> lineas;

	public String status;

	public List<String> usersId;

	public Tablero(String color) {
		this.color = color;
		lineas = new ArrayList();
		usersId = new ArrayList();
		status = "Created";
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<Line> getLineas() {
		return lineas;
	}

	public void setLineas(List<Line> lineas) {
		this.lineas = lineas;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<String> usersId) {
		this.usersId = usersId;
	}
}
