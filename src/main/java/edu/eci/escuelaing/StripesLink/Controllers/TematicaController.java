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
@RequestMapping("tematica")
public class TematicaController {

	@Autowired
	private IStripesLinkService service;
	
	@PostMapping("/{name}")
	private ResponseEntity<?> addTematica(@PathVariable String name) {
			String idTematica = service.addTematica(name);
			return ResponseEntity.ok(new BaseResponse(idTematica));
	}
	
	@PutMapping("/{idTematica}/{palabra}")
	private ResponseEntity<?> addWordTematica(@PathVariable String idTematica,@PathVariable String palabra) {
		try {
			service.addWordTematica(idTematica,palabra);
			return new ResponseEntity<>( HttpStatus.CREATED);
		} catch (StripesLinkException e) {
			Logger.getLogger(TematicaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/{idTematica}/{palabra}")
	private ResponseEntity<?> findWordTematica(@PathVariable String idTematica,@PathVariable String palabra) {
		try {
			
			return new ResponseEntity<>(service.findWordTematica(idTematica,palabra), HttpStatus.CREATED);
		} catch (StripesLinkException e) {
			Logger.getLogger(TematicaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping
	private ResponseEntity<?> chooseTematica() {
		try {
			return new ResponseEntity<>(service.chooseTematica(), HttpStatus.CREATED);
		} catch (StripesLinkException e) {
			Logger.getLogger(TematicaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/{idTematica}")
	private ResponseEntity<?> chooseWordTematica(@PathVariable String idTematica) {
		try {
			return new ResponseEntity<>(service.chooseWordTematica(idTematica), HttpStatus.CREATED);
		} catch (StripesLinkException e) {
			Logger.getLogger(TematicaController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}



}
