package edu.eci.escuelaing.StripesLink.Service;

import java.util.List;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.User;
import edu.eci.escuelaing.StripesLink.Model.UserSalaResponse;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;

public interface IStripesLinkService {

	public void createUser(AuthenticationRequest authenticationRequest) throws StripesLinkException;

	public String login(AuthenticationRequest authenticationRequest) throws StripesLinkException;

	public String createSala(); 
	
	public List<UserSalaResponse> getAllSalas();

	void AddUserSala(String idSala) throws StripesLinkException;
}
