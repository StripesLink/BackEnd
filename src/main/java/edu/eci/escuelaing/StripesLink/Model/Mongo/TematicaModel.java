package edu.eci.escuelaing.StripesLink.Model.Mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.escuelaing.StripesLink.Model.Tablero;
import edu.eci.escuelaing.StripesLink.Model.User;

@Document(collection = "tematicas")

public class TematicaModel {

	@Id
	private String id;
	
	private String name;
	private String palabra;

	private List<String> palabras;
	
	

	public TematicaModel(String name) {
		this.name=name;
		palabras = new ArrayList();
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPalabra() {
		return palabra;
	}



	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}



	public List<String> getPalabras() {
		return palabras;
	}



	public void setPalabras(List<String> palabras) {
		this.palabras = palabras;
	}

	
}
