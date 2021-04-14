package edu.eci.escuelaing.StripesLink.Service;

import java.util.List;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.Line;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.Ronda;
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
	
	public String addTematica(String name) throws StripesLinkException;
	
	void addWordTematica(String name,String palabra) throws StripesLinkException;

	public void addLineSala(String idSala, Line linea, String nombre) throws StripesLinkException;
	
	boolean findWordTematica(String name,String palabra) throws StripesLinkException;
	
	String chooseTematica() throws StripesLinkException;
	
	String chooseWordTematica(String name) throws StripesLinkException;

	Ronda getRound(String idSala) throws StripesLinkException;
	
	Ronda newRound(String idSala) throws StripesLinkException;

	boolean findWordSala(String idSala, String equipo, String palabra) throws StripesLinkException;

	void cleanSala(String idSala) throws StripesLinkException;

}
