package edu.eci.escuelaing.StripesLink.Model;

public class WinnerMessage {
	private String username;
	private Ronda ronda;
	
	public WinnerMessage(String username, Ronda ronda) {
		super();
		this.username = username;
		this.ronda = ronda;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Ronda getRonda() {
		return ronda;
	}
	public void setRonda(Ronda ronda) {
		this.ronda = ronda;
	}
	
	
}
