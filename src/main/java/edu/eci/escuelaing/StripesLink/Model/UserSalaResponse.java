package edu.eci.escuelaing.StripesLink.Model;

public class UserSalaResponse implements Comparable< UserSalaResponse >{
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
	
	@Override
    public int compareTo(UserSalaResponse o) {
        return this.getIdSala().compareTo(o.getIdSala());
    }
}
