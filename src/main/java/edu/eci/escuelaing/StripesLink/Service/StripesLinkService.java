package edu.eci.escuelaing.StripesLink.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.eci.escuelaing.StripesLink.Controllers.UserController;
import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.Tablero;
import edu.eci.escuelaing.StripesLink.Model.User;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaRepository;
import edu.eci.escuelaing.StripesLink.Model.Mongo.UserModel;
import edu.eci.escuelaing.StripesLink.Model.Mongo.UserRepository;
import edu.eci.escuelaing.StripesLink.Security.JwtUtils;
import edu.eci.escuelaing.StripesLink.Security.UserService;

@Service
public class StripesLinkService implements IStripesLinkService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SalaRepository salaRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	//LOAD SALAS PUBLICAS
	/*public StripesLinkService() {
		List<SalaModel> salas = getAllSalas();
		if (salas.size() < 1) {
			for (int i = 0; i < 10; i++) {
				createSala();
			}
		}
	}*/

	@Override
	public void createUser(AuthenticationRequest authenticationRequest) throws StripesLinkException {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		UserModel userModel = new UserModel();
		userModel.setUsername(username);
		userModel.setPassword(password);

		if (userRepository.findByUsername(username) == null)
			try {
				userRepository.save(userModel);
			} catch (Exception e) {
				throw new StripesLinkException("No se pudo guardar un usuario");
			}
		else {
			throw new StripesLinkException("Usuario ya existe");
		}
	}

	@Override
	public String login(AuthenticationRequest authenticationRequest) throws StripesLinkException {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (BadCredentialsException e) {
			throw new StripesLinkException("Credenciales incorrectas");
		}

		UserDetails loadedUser = userService.loadUserByUsername(username);

		return jwtUtils.generateToken(loadedUser);
	}

	@Override
	public String createSala() {
		List<Tablero> tableros = new ArrayList<Tablero>();
		tableros.add(new Tablero("Azul"));
		tableros.add(new Tablero("Rojo"));
		SalaModel newSala = salaRepository.save(new SalaModel(tableros));
		return newSala.getId();
	}

	@Override
	public List<SalaModel> getAllSalas() {
		return salaRepository.findAll();
	}

}
