package edu.eci.escuelaing.StripesLink.Model;

public class Ronda {
	private String nameTematica;
	private String Idtematica;
	private String pintorAzul;
	private String pintorRojo;
	private String palabraAzul;
	private String palabraRojo;

	public Ronda(String nameTematica, String idtematica, String pintorAzul, String pintorRojo, String palabraAzul,
			String palabraRojo) {
		super();
		this.nameTematica = nameTematica;
		Idtematica = idtematica;
		this.pintorAzul = pintorAzul;
		this.pintorRojo = pintorRojo;
		this.palabraAzul = palabraAzul;
		this.palabraRojo = palabraRojo;
	}

	public String getNameTematica() {
		return nameTematica;
	}

	public void setNameTematica(String nameTematica) {
		this.nameTematica = nameTematica;
	}

	public String getIdtematica() {
		return Idtematica;
	}

	public void setIdtematica(String idtematica) {
		Idtematica = idtematica;
	}

	public String getPintorAzul() {
		return pintorAzul;
	}

	public void setPintorAzul(String pintorAzul) {
		this.pintorAzul = pintorAzul;
	}

	public String getPintorRojo() {
		return pintorRojo;
	}

	public void setPintorRojo(String pintorRojo) {
		this.pintorRojo = pintorRojo;
	}

	public String getPalabraAzul() {
		return palabraAzul;
	}

	public void setPalabraAzul(String palabraAzul) {
		this.palabraAzul = palabraAzul;
	}

	public String getPalabraRojo() {
		return palabraRojo;
	}

	public void setPalabraRojo(String palabraRojo) {
		this.palabraRojo = palabraRojo;
	}

}
