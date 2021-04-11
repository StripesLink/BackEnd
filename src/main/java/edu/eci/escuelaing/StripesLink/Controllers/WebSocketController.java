package edu.eci.escuelaing.StripesLink.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Service.IStripesLinkService;

@Controller
public class WebSocketController {

	@Autowired
	SimpMessagingTemplate msgt;

	@Autowired
	IStripesLinkService persistence;

	@MessageMapping("/newPoint.{idSala}")
	public void handlePointEvent(Point pt, int numtablero, @DestinationVariable String idSala) throws Exception {
		System.out.println("Nueva conexion a la sala:" + idSala);
		persistence.newPointSala(idSala, pt, numtablero);
		msgt.convertAndSend("/topic/newSala.{idSala}", pt);
	}

	@MessageMapping("/newSala.{idSala}")
	public void handleSalaEvent(int numtablero, @DestinationVariable String idSala) throws Exception {
		System.out.println("Nueva conexion a la sala:" + idSala);
		System.out.println(persistence.getPointsSala(idSala, numtablero));
		msgt.convertAndSend("/topic/newSala." + idSala, persistence.getPointsSala(idSala, numtablero));
	}
}
