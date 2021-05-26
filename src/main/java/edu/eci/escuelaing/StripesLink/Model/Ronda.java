package edu.eci.escuelaing.StripesLink.Model;

import java.io.Serializable;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

public class Ronda extends JdkSerializationRedisSerializer implements Serializable {
	private String nameTematica;
	private String pintorAzul;
	private String pintorRojo;
	private String palabraAzul;
	private String palabraRojo;

	public Ronda(String nameTematica, String pintorAzul, String pintorRojo, String palabraAzul,
			String palabraRojo) {
		super();
		this.nameTematica = nameTematica;
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
