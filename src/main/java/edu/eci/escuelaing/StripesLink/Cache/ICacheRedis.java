package edu.eci.escuelaing.StripesLink.Cache;

import java.util.Map;

public interface ICacheRedis {
	
	public void incrementUsers(String idSala);
	
	public void decrementtUsers(String idSala);
	
	public Map<String, Long> get();
}
