package edu.eci.escuelaing.StripesLink.Model;

public class UserSalaResponse {
	private String idSala;
	private int numUsers;

	public UserSalaResponse() {
	}

	public UserSalaResponse(String idSala, int numUsers) {
		this.idSala = idSala;
		this.numUsers = numUsers;
	}

	public String getIdSala() {
		return idSala;
	}

	public void setIdSala(String idSala) {
		this.idSala = idSala;
	}

	public int getNumUsers() {
		return numUsers;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
	}
}
