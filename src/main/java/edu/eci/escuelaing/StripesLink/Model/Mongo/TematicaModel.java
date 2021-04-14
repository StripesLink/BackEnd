package edu.eci.escuelaing.StripesLink.Model.Mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.escuelaing.StripesLink.Model.Tablero;

@Document(collection = "tematicas")
public class TematicaModel {

	@Id
	private String id;

	private String name;

	private List<String> palabras;

	@PersistenceConstructor
	public TematicaModel(String name) {
		super();
		this.name = name;
		palabras = new ArrayList();
	}

	public TematicaModel(String name, List<String> palabras) {
		super();
		this.name = name;
		this.palabras = palabras;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPalabras() {
		return palabras;
	}

	public void setPalabras(List<String> palabras) {
		this.palabras = palabras;
	}

}
