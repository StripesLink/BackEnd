package edu.eci.escuelaing.StripesLink.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.Tablero;
import edu.eci.escuelaing.StripesLink.Model.UserSalaResponse;
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

	@PostConstruct
	public void seedData() {
		List<UserSalaResponse> salas = getAllSalas();
		if (salas.size() < 1) {
			for (int i = 0; i < 10; i++) {
				createSala();
			}
		}
	}

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
	public List<UserSalaResponse> getAllSalas() {
		List<SalaModel> salas = salaRepository.findAll();
		List<UserSalaResponse> salasDOM = new ArrayList<UserSalaResponse>();
		salas.forEach((x) -> salasDOM.add(new UserSalaResponse(x.getId(), x.getUsersId().size())));
		return salasDOM;
	}

	@Override
	public String AddUserSala(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserModel user = userRepository.findByUsername(userDetails.getUsername());
			System.out.println("-------------" + userDetails.getUsername());
			if (sala.getUsersId().contains(user.getId()))
				throw new StripesLinkException("Usuario ya esta en esta sala");
			sala.getUsersId().add(user.getId());
			sala.getTableros().get(sala.getCurrentTablero()).usersId.add(user.getId());
			int newTablero = (sala.getCurrentTablero() == 0) ? 1 : 0;
			sala.setCurrentTablero(newTablero);
			salaRepository.save(sala);
			return (sala.getCurrentTablero() == 0) ? "Azul" : "Rojo";
		} else {
			throw new StripesLinkException("Sala no existe");
		}
	}

	@Override
	public Object getPointsSala(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			Object a = new Object() {
				List<Point> Azul = sala.getTableros().get(0).getPuntos();
				List<Point> Rojo = sala.getTableros().get(1).getPuntos();

				public List<Point> getAzul() {
					return Azul;
				}

				public void setAzul(List<Point> azul) {
					Azul = azul;
				}

				public List<Point> getRojo() {
					return Rojo;
				}

				public void setRojo(List<Point> rojo) {
					Rojo = rojo;
				}

			};
			System.out.print("---------------" + a);
			return a;
		} else
			throw new StripesLinkException("Sala no existe");
	}
	
	@Override
	public String getPintorSala(String idSala, String equipo) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		String pintor=null;
		if (m.isPresent()) {
			SalaModel sala = m.get();
			 List<Tablero> tableros=sala.getTableros();
			 int posTablero=0 ;
			 for (int i=0;i<tableros.size();i++){ 
				if ( tableros.get(i).getColor() == equipo){
					posTablero=i;
					break;	
				}	 
			 }
			if (tableros.get(posTablero) == null){
				throw new StripesLinkException("Tablero no existe");	
			}
			
			List<String> usuarios =tableros.get(posTablero).getUsersId();
			int current=tableros.get(posTablero).getCurrentUser();
			if (current == usuarios.size()-1){
				tableros.get(posTablero).setCurrentUser(0);
				pintor=tableros.get(posTablero).getUsersId().get(current);	
			}else{
				
				pintor=tableros.get(posTablero).getUsersId().get(current);
				tableros.get(posTablero).setCurrentUser(current+1);
			}
			sala.setTableros(tableros);
			salaRepository.save(sala);
		}
			return pintor;
	}

	@Override
	public void newPointSala(String idSala, Point pt, int tablero) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			sala.getTableros().get(tablero).getPuntos().add(pt);
			salaRepository.save(sala);
		} else {
			throw new StripesLinkException("Sala no existe");
		}
	}

	@Override
	public void addPoints(String idSala, List<Point> pts) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserModel user = userRepository.findByUsername(userDetails.getUsername());
			Tablero tablero = null;
			for (Tablero t : sala.getTableros()) {
				if (t.getUsersId().contains(user.getId())) {
					tablero = t;
					break;
				}
			}
			if (tablero == null)
				throw new StripesLinkException("Usuario no esta en ningun tablero");
			tablero.getPuntos().addAll(pts);
			List<Tablero> tableros = sala.getTableros();
			int index = (tableros.get(0).getColor() == tablero.getColor()) ? 0 : 1;
			tableros.set(index, tablero);
			sala.setTableros(tableros);
			salaRepository.save(sala);
		} else {
			throw new StripesLinkException("Sala no existe");
		}
	}
}
