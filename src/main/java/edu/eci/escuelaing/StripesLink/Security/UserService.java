package edu.eci.escuelaing.StripesLink.Security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.eci.escuelaing.StripesLink.Model.Mongo.UserModel;
import edu.eci.escuelaing.StripesLink.Model.Mongo.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Entroooo--------a-----cargarUsuarioooo"+username);
		UserModel foundedUser = userRepository.findByUsername(username);
		if (foundedUser == null)
			return null;

		String name = foundedUser.getUsername();
		String pwd = foundedUser.getPassword();

		return new User(name, pwd, new ArrayList<>());
	}

}
