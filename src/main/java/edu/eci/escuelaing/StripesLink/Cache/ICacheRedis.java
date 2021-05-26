package edu.eci.escuelaing.StripesLink.Cache;

import java.util.Map;

import edu.eci.escuelaing.StripesLink.Model.Ronda;

public interface ICacheRedis {
	
	public void incrementUsers(String idSala);
	
	public void decrementtUsers(String idSala);
	
	public Map<String, Long> getUsers();

	public void setState(Ronda ronda, String idSala);

	public Ronda getState(String idSala);
}
