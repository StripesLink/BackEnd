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
import edu.eci.escuelaing.StripesLink.Model.Line;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.Tablero;
import edu.eci.escuelaing.StripesLink.Model.User;
import edu.eci.escuelaing.StripesLink.Model.UserSalaResponse;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaRepository;
import edu.eci.escuelaing.StripesLink.Model.Mongo.TematicaModel;
import edu.eci.escuelaing.StripesLink.Model.Mongo.TematicaRepository;
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
	private TematicaRepository tematicaRepository;

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
	public String addUserSala(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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
			return (sala.getCurrentTablero() == 0) ? "Rojo" : "Azul";
		} else {
			throw new StripesLinkException("Sala no existe");
		}
	}
	@Override
	public void addWordTematica(String idTematica,String palabra) throws StripesLinkException {
		Optional<TematicaModel> m = tematicaRepository.findById(idTematica);
		if (m.isPresent()) {
			TematicaModel tematica = m.get();
			if (tematica.getPalabras().contains(palabra))
				throw new StripesLinkException("Palabra ya esta en esta tematica");
			List<String> palabras=tematica.getPalabras();
			palabras.add(palabra);
			tematica.setPalabras(palabras);
			tematicaRepository.save(tematica);
			
			
		} else {
			throw new StripesLinkException("Tematica no existe");
		}
	}

	@Override
	public Object getPointsSala(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			Object a = new Object() {
				List<Line> Azul = sala.getTableros().get(0).getLineas();
				List<Line> Rojo = sala.getTableros().get(1).getLineas();
				public List<Line> getAzul() {
					return Azul;
				}
				public void setAzul(List<Line> azul) {
					Azul = azul;
				}
				public List<Line> getRojo() {
					return Rojo;
				}
				public void setRojo(List<Line> rojo) {
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
			 int posTablero=-1;
			 for (int i=0;i<tableros.size();i++){ 
				if (tableros.get(i).getColor().charAt(0)== equipo.charAt(0)){
					posTablero=i;		
					break;	
				}	 
			 }
			if (tableros.get(posTablero) == null || posTablero==-1){
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
	public void addLineSala(String idSala, Line pts, String name) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			// UserDetails userDetails = (UserDetails)
			// SecurityContextHolder.getContext().getAuthentication()
			// .getPrincipal();
			UserModel user = userRepository.findByUsername(name);
			Tablero tablero = null;
			for (Tablero t : sala.getTableros()) {
				if (t.getUsersId().contains(user.getId())) {
					tablero = t;
					break;
				}
			}
			if (tablero == null)
				throw new StripesLinkException("Usuario no esta en ningun tablero");
			tablero.getLineas().add(pts);
			List<Tablero> tableros = sala.getTableros();
			int index = (tableros.get(0).getColor() == tablero.getColor()) ? 0 : 1;
			tableros.set(index, tablero);
			sala.setTableros(tableros);
			salaRepository.save(sala);
		} else {
			throw new StripesLinkException("Sala no existe");
		}
	}
	
	@Override
	public String addTematica(String name) {
		TematicaModel newTematica = tematicaRepository.save(new TematicaModel(name));
		return newTematica.getId();
		
	}

	@Override
	public void removeUserSala(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			Tablero tablero = null;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserModel user = userRepository.findByUsername(userDetails.getUsername());
			if (!sala.getUsersId().contains(user.getId()))
				throw new StripesLinkException("Usuario no esta en esta sala");

			for (Tablero t : sala.getTableros()) {
				if (t.getUsersId().contains(user.getId())) {
					int num = (t.getColor().equals("Azul") ? 0 : 1);
					List<String> users = t.getUsersId();
					users.remove(user.getId());
					t.setUsersId(users);
					List<Tablero> tableros = sala.getTableros();
					tableros.remove(num);
					tableros.add(num, t);
					sala.setTableros(tableros);
					sala.setCurrentTablero(num);
					break;
				}
			}
			
			List<String> users = sala.getUsersId();
			users.remove(user.getId());
			sala.setUsersId(users);
			salaRepository.save(sala);
		} else {
			throw new StripesLinkException("Sala no existe");
		}

	}
	
	@Override
	public boolean findWordTematica(String idTematica,String palabra) throws StripesLinkException {
		Optional<TematicaModel> m = tematicaRepository.findById(idTematica);
		if (m.isPresent()) {
			TematicaModel tematica = m.get();
			if (tematica.getPalabras().contains(palabra))
				return true;
			return false;
		} else {
			throw new StripesLinkException("Tematica no existe");
		}
	}
		
		
	

	

}
