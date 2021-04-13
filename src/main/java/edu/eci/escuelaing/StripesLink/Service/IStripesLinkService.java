package edu.eci.escuelaing.StripesLink.Service;

import java.util.List;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.Line;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.User;
import edu.eci.escuelaing.StripesLink.Model.UserSalaResponse;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;

public interface IStripesLinkService {

	public void createUser(AuthenticationRequest authenticationRequest) throws StripesLinkException;

	public String login(AuthenticationRequest authenticationRequest) throws StripesLinkException;

	public String createSala(); 
	
	public List<UserSalaResponse> getAllSalas();

	String addUserSala(String idSala) throws StripesLinkException;
	
	void removeUserSala(String idSala) throws StripesLinkException;

	Object getPointsSala(String idSala) throws StripesLinkException;

	
	String getPintorSala(String idSala, String equipo) throws StripesLinkException;
	
	public String addTematica(String name) ;
	
	void addWordTematica(String idTematica,String palabra) throws StripesLinkException;

	public void addLineSala(String idSala, Line linea, String nombre) throws StripesLinkException;
	
	boolean findWordTematica(String idTematica,String palabra) throws StripesLinkException;

}
