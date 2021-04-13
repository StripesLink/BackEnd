package edu.eci.escuelaing.StripesLink.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.BaseResponse;
import edu.eci.escuelaing.StripesLink.Model.Line;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.UserSalaResponse;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;
import edu.eci.escuelaing.StripesLink.Service.IStripesLinkService;
import edu.eci.escuelaing.StripesLink.Service.StripesLinkException;

@RestController
@RequestMapping("sala")
public class SalaController {

	@Autowired
	private IStripesLinkService service;

	@PostMapping
	private ResponseEntity<?> crearSala() {
		String idSala = service.createSala();
		return ResponseEntity.ok(new BaseResponse(idSala));
	}

	@GetMapping
	private ResponseEntity<?> getAllSalas() {
		return ResponseEntity.ok(service.getAllSalas());
	}

	@PutMapping("/user/{idSala}")
	private ResponseEntity<?> addUserSala(@PathVariable String idSala) {
		try {
			return new ResponseEntity<>(new BaseResponse(service.addUserSala(idSala)), HttpStatus.CREATED);
		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/points/{idSala}")
	private ResponseEntity<?> getPointSalas(@PathVariable String idSala) {
		try {
			return ResponseEntity.ok(service.getPointsSala(idSala));
		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	
	@GetMapping("/pintor/{idSala}/{equipo}")
	private ResponseEntity<?> getPintorSala(@PathVariable String idSala,@PathVariable String equipo) {
		try {
			return ResponseEntity.ok(service.getPintorSala(idSala,equipo));
		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping("/points/{idSala}/{name}")
	private ResponseEntity<?> addPointSalas(@PathVariable String idSala, @RequestBody Line pts,
			@PathVariable String name) {
		try {
			service.addLineSala(idSala, pts, name);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}


	@PutMapping("/removeUser/{idSala}")
	private ResponseEntity<?> removeUserSala(@PathVariable String idSala) {
		try {
			service.removeUserSala(idSala);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
