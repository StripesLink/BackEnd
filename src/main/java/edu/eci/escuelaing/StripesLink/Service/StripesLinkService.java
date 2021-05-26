package edu.eci.escuelaing.StripesLink.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Round;
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

import edu.eci.escuelaing.StripesLink.Cache.ICacheRedis;
import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.Line;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.Ronda;
import edu.eci.escuelaing.StripesLink.Model.Tablero;
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

	@Autowired
	private ICacheRedis cache;

	@PostConstruct
	public void seedDataSalas() {
		List<SalaModel> salas = salaRepository.findAll();
		if (salas.size() < 1) {
			for (int i = 0; i < 10; i++) {
				createSala();
			}
		}
	}

	@PostConstruct
	public void seedDataTematicas() {
		if (tematicaRepository.findAll().size() < 1) {
			List<String> tematicas = Arrays.asList("peliculas", "animales", "acciones", "frutas");
			List<Object> palabras = new ArrayList<Object>();
			palabras.add(Arrays.asList("titanic", "harry potter", "los vengadores", "godzilla", "frozen"));
			palabras.add(Arrays.asList("perro", "gato", "canario", "pez", "pantera"));
			palabras.add(Arrays.asList("nadar", "saltar", "dormir", "escribir", "leer", "conducir"));
			palabras.add(Arrays.asList("manzana", "pera", "cereza", "banana", "naranja", "piña", "mora"));
			for (int i = 0; i < tematicas.size(); i++) {
				tematicaRepository.save(new TematicaModel(tematicas.get(i), (List<String>) palabras.get(i)));
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
		Map<String, Long> count = cache.getUsers();
		System.out.println("-----" + count);
		List<UserSalaResponse> salasDOM = new ArrayList<UserSalaResponse>();
		System.out.println("-------------rediss" + count.isEmpty());
		if (count.isEmpty()) {
			List<SalaModel> salas = salaRepository.findAll();
			int cont = 1;
			salas.forEach((x) -> {
				salasDOM.add(new UserSalaResponse(x.getId(), x.getUsersId().size()));
				cache.incrementUsers(x.getId());
			});
		} else {
			count.forEach((key, data) -> salasDOM.add(new UserSalaResponse(key, Math.toIntExact(data))));
		}
		Collections.sort(salasDOM);
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
			cache.incrementUsers(idSala);
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
	public void addWordTematica(String name, String palabra) throws StripesLinkException {
		TematicaModel tematica = tematicaRepository.findByName(name);
		if (tematica != null) {
			palabra = palabra.toLowerCase();
			if (tematica.getPalabras().contains(palabra))
				throw new StripesLinkException("Palabra ya esta en esta tematica");
			List<String> palabras = tematica.getPalabras();
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
		String pintor = null;
		if (m.isPresent()) {
			SalaModel sala = m.get();
			Tablero tablero = null;
			for (Tablero t : sala.getTableros()) {
				if (t.getColor().equals(equipo)) {
					tablero = t;
					break;
				}
			}

			if (tablero == null)
				throw new StripesLinkException("Tablero no existe");

			List<String> usuarios = tablero.getUsersId();
			if (usuarios.size() < 1)
				throw new StripesLinkException("Tablero" + equipo + "no tiene usuarios");

			int current = tablero.getCurrentUser();

			if (current == usuarios.size() - 1) {
				tablero.setCurrentUser(0);
				pintor = usuarios.get(current);
			} else {
				pintor = usuarios.get(current);
				tablero.setCurrentUser(current + 1);
			}

			int index = (sala.getTableros().get(0).getColor() == tablero.getColor()) ? 0 : 1;
			List<Tablero> tableros = sala.getTableros();
			tableros.set(index, tablero);
			sala.setTableros(tableros);
			salaRepository.save(sala);
		}
		return userRepository.findById(pintor).get().getUsername();
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
	public String addTematica(String name) throws StripesLinkException {
		List<TematicaModel> tematicas = tematicaRepository.findAll();
		name = name.toLowerCase();
		for (TematicaModel t : tematicas) {
			if (t.getName().equals(name)) {
				throw new StripesLinkException("Tematica ya se encuentra agregada");
			}
		}
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
					if (users.size() <= 1) {
						cache.setState(null, idSala);
						sala.setTematica(null);
						sala.getTableros().get(0).setLineas(new ArrayList<Line>());
						sala.getTableros().get(1).setLineas(new ArrayList<Line>());
					}
					users.remove(user.getId());
					cache.decrementtUsers(idSala);
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
	public boolean findWordTematica(String name, String palabra) throws StripesLinkException {
		TematicaModel tematica = tematicaRepository.findByName(name);
		if (tematica != null) {
			palabra = palabra.toLowerCase();
			if (tematica.getPalabras().contains(palabra))
				return true;
			return false;
		} else {
			throw new StripesLinkException("Tematica no existe");
		}
	}

	@Override
	public boolean findWordSala(String idSala, String equipo, String palabra) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			Tablero tablero = (sala.getTableros().get(0).getColor().equals(equipo)) ? sala.getTableros().get(0)
					: sala.getTableros().get(1);
			if (tablero.getPalabra().equals(palabra)) {
				return true;
			}
			return false;
		} else {
			throw new StripesLinkException("Tematica no existe");
		}
	}

	@Override
	public String chooseTematica() throws StripesLinkException {
		List<TematicaModel> tematicas = tematicaRepository.findAll();
		if (tematicas == null || tematicas.size() == 0) {
			throw new StripesLinkException("No hay Tematicas");

		} else {
			Random rand = new Random();
			TematicaModel tematica = tematicas.get(rand.nextInt(tematicas.size()));
			return tematica.getName();
		}
	}

	@Override
	public String chooseWordTematica(String name) throws StripesLinkException {
		TematicaModel tematica = tematicaRepository.findByName(name);
		if (tematica != null) {
			if (tematica.getPalabras() == null || tematica.getPalabras().size() == 0)
				throw new StripesLinkException("No hay palabras en esta tematica");
			Random rand = new Random();
			String palabra = tematica.getPalabras().get(rand.nextInt(tematica.getPalabras().size()));
			return palabra;
		} else {
			throw new StripesLinkException("Tematica no existe");
		}
	}

	@Override
	public Ronda getRound(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			String nameTematica, palabraAzul, palabraRojo, pintorAzul, pintorRojo = null;
			Ronda ronda = cache.getState(idSala);
			System.out.print("-------cacheeeee afueraaaaa");
			if (ronda != null) {
				System.out.print("-------cacheeeee" + ronda.getNameTematica());
				return ronda;
			} else {
				try {
					nameTematica = chooseTematica();
					palabraAzul = chooseWordTematica(nameTematica);
					palabraRojo = chooseWordTematica(nameTematica);
					pintorAzul = getPintorSala(idSala, "Azul");
					pintorRojo = getPintorSala(idSala, "Rojo");
				} catch (StripesLinkException e) {
					throw new StripesLinkException("La sala debe tener por lo menos dos jugadores");
				}
			}
			ronda = new Ronda(nameTematica, pintorAzul, pintorRojo, palabraAzul, palabraRojo);
			cache.setState(ronda, idSala);
			return ronda;
		} else {
			throw new StripesLinkException("Sala no existe");
		}
	}

	@Override
	public Ronda newRound(String idSala) throws StripesLinkException {
		try {
			Optional<SalaModel> m = salaRepository.findById(idSala);
			if (m.isPresent()) {
				SalaModel sala = m.get();
				sala.getTableros().get(0).setLineas(new ArrayList<Line>());
				sala.getTableros().get(1).setLineas(new ArrayList<Line>());
				salaRepository.save(sala);
				cache.setState(null, idSala);
				return getRound(idSala);
			} else {
				throw new StripesLinkException("No existe la sala");
			}
		} catch (StripesLinkException e) {
			throw new StripesLinkException(e.getMessage());
		}
	}

	@Override
	public void cleanSala(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			List<Tablero> tableros = sala.getTableros();
			Tablero t1 = tableros.get(0);
			Tablero t2 = tableros.get(1);
			t1.setLineas(new ArrayList<Line>());
			t1.setPintor(null);
			t1.setPalabra(null);
			t2.setLineas(new ArrayList<Line>());
			t2.setPintor(null);
			t2.setPalabra(null);
			sala.setTematica(null);
			tableros.set(0, t1);
			tableros.set(1, t2);
			sala.setTableros(tableros);
			salaRepository.save(sala);
		} else {
			throw new StripesLinkException("No existe la sala");
		}
	}

	@Override
	public int getUsersSala(String idSala) throws StripesLinkException {
		Optional<SalaModel> m = salaRepository.findById(idSala);
		if (m.isPresent()) {
			SalaModel sala = m.get();
			return sala.getUsersId().size();
		} else {
			throw new StripesLinkException("No existe la sala");
		}
	}

}
