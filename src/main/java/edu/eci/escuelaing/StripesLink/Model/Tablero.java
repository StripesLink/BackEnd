package edu.eci.escuelaing.StripesLink.Model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

	public String color;

	public List<Line> lineas;

	public List<String> usersId;

	public int currentUser;

	public String palabra;
	
	public String pintor;

	public Tablero(String color) {
		this.color = color;
		lineas = new ArrayList();
		usersId = new ArrayList();
		currentUser = 0;
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

	public List<String> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<String> usersId) {
		this.usersId = usersId;
	}

	public int getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(int currentUser) {
		this.currentUser = currentUser;
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	public String getPintor() {
		return pintor;
	}

	public void setPintor(String pintor) {
		this.pintor = pintor;
	}
	
	

}
