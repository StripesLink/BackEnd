package edu.eci.escuelaing.StripesLink.Controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.BaseResponse;
import edu.eci.escuelaing.StripesLink.Service.IStripesLinkService;
import edu.eci.escuelaing.StripesLink.Service.StripesLinkException;

@RestController
public class SalaController {

	@Autowired
	private IStripesLinkService service;

	@PostMapping("/createSala")
	private ResponseEntity<?> crearSala() {
		String idSala = service.createSala();
		return ResponseEntity.ok(new BaseResponse(idSala));
	}

	@GetMapping("/getAllSalas")
	private ResponseEntity<?> getAllSalas() {
		return ResponseEntity.ok(service.getAllSalas());
	}
}
