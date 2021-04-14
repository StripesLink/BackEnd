package edu.eci.escuelaing.StripesLink.Model.Mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.escuelaing.StripesLink.Model.Tablero;

@Document(collection = "salas")
public class SalaModel {

	@Id
	private String id;

	private List<Tablero> tableros;

	private List<String> usersId;

	private int currentTablero = 0;

	private String Tematica;

	public SalaModel(List<Tablero> tableros) {
		this.tableros = tableros;
		usersId = new ArrayList();
	}

	public String getId() {
		return id;
	}

	public List<Tablero> getTableros() {
		return tableros;
	}

	public void setTableros(List<Tablero> tableros) {
		this.tableros = tableros;
	}

	public List<String> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<String> usersId) {
		this.usersId = usersId;
	}

	public int getCurrentTablero() {
		return currentTablero;
	}

	public void setCurrentTablero(int currentTablero) {
		this.currentTablero = currentTablero;
	}

	public String getTematica() {
		return Tematica;
	}

	public void setTematica(String tematica) {
		Tematica = tematica;
	}

}
