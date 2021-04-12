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

	@MessageMapping("/newPoints.{idSala}.{equipo}")
	public void handlePointEvent(List<Point> pts, @DestinationVariable String idSala,
			@DestinationVariable String equipo) throws Exception {
		System.out.println("Nueva conexion a la sala:" + idSala);
		persistence.addPoints(idSala, pts);
		msgt.convertAndSend("/topic/Sala." + idSala + "." + equipo, pts);
	}
}
