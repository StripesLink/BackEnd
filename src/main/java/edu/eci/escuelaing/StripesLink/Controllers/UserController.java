package edu.eci.escuelaing.StripesLink.Controllers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.BaseResponse;
import edu.eci.escuelaing.StripesLink.Model.User;
import edu.eci.escuelaing.StripesLink.Model.Mongo.UserModel;
import edu.eci.escuelaing.StripesLink.Model.Mongo.UserRepository;
import edu.eci.escuelaing.StripesLink.Security.JwtUtils;
import edu.eci.escuelaing.StripesLink.Security.UserService;
import edu.eci.escuelaing.StripesLink.Service.IStripesLinkService;
import edu.eci.escuelaing.StripesLink.Service.StripesLinkException;

@RestController
public class UserController {
	@Autowired
	private IStripesLinkService service;

	@GetMapping("/dashboard")
	private String testingToken() {
		return "Welcome to the Dashboard" + SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@PostMapping("/createUser")
	private ResponseEntity<?> registrarUsuario(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			service.createUser(authenticationRequest);
			return new ResponseEntity<>("Usuario " + authenticationRequest.getUsername() + " creado correctamente",
					HttpStatus.CREATED);
		} catch (StripesLinkException e) {
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/login")
	private ResponseEntity<?> autenticarUsuario(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			String token = service.login(authenticationRequest);
			return ResponseEntity.ok(new BaseResponse(token));
		} catch (StripesLinkException e) {
			Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
