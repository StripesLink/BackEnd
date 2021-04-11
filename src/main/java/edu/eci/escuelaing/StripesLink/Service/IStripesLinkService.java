package edu.eci.escuelaing.StripesLink.Service;

import java.util.List;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.User;
import edu.eci.escuelaing.StripesLink.Model.UserSalaResponse;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;

public interface IStripesLinkService {

	public void createUser(AuthenticationRequest authenticationRequest) throws StripesLinkException;

	public String login(AuthenticationRequest authenticationRequest) throws StripesLinkException;

	public String createSala(); 
	
	public List<UserSalaResponse> getAllSalas();

	String AddUserSala(String idSala) throws StripesLinkException;

	Object getPointsSala(String idSala) throws StripesLinkException;

	void newPointSala(String idSala, Point pt, int tablero) throws StripesLinkException;

	public void addPoints(String idSala, List<Point> pts) throws StripesLinkException;
}
