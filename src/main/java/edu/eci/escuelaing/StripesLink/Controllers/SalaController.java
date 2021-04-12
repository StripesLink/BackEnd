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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.escuelaing.StripesLink.Model.AuthenticationRequest;
import edu.eci.escuelaing.StripesLink.Model.BaseResponse;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.UserSalaResponse;
import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;
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

	@PutMapping("/addUserSala")
	private ResponseEntity<?> addUserSala(@RequestParam String idSala) {
		try {
			return new ResponseEntity<>(new BaseResponse(service.AddUserSala(idSala)), HttpStatus.CREATED);
		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getPointsSala")
	private ResponseEntity<?> getPointSalas(@RequestParam String idSala) {
		try {
			return ResponseEntity.ok(service.getPointsSala(idSala));
		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/addPointsSala")
	private ResponseEntity<?> addPointSalas(@RequestParam String idSala, @RequestBody List<Point> pts) {
		try {
			service.addPoints(idSala,pts,"abc");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (StripesLinkException e) {
			Logger.getLogger(SalaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
}
