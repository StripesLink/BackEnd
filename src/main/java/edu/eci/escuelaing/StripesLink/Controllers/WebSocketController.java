package edu.eci.escuelaing.StripesLink.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import edu.eci.escuelaing.StripesLink.Model.Line;
import edu.eci.escuelaing.StripesLink.Model.Point;
import edu.eci.escuelaing.StripesLink.Model.Ronda;
import edu.eci.escuelaing.StripesLink.Model.WinnerMessage;
import edu.eci.escuelaing.StripesLink.Service.IStripesLinkService;
import edu.eci.escuelaing.StripesLink.Service.StripesLinkException;

@Controller
public class WebSocketController {

	@Autowired
	SimpMessagingTemplate msgt;

	@Autowired
	IStripesLinkService service;

	@MessageMapping("/newPoints.{idSala}.{equipo}")
	public void handlePointEvent(Line pts, @DestinationVariable String idSala, @DestinationVariable String equipo,
			Principal p) throws Exception {
		System.out.println("Nueva conexion a la sala:" + idSala);
		service.addLineSala(idSala, pts, p.getName());
		msgt.convertAndSend("/topic/Sala." + idSala + "." + equipo, pts);
	}

	@MessageMapping("/chat.{idSala}.{equipo}")
	public void handleChatEvent(String msg, @DestinationVariable String idSala, @DestinationVariable String equipo,
			Principal p) throws Exception {
		String palabra = msg.replace('"',' ').trim();
		System.out.println("Nueva conexion a la sala-chat:" + idSala);
		if (service.findWordSala(idSala, equipo, palabra)) {
			System.out.println("Gano usuario" + p.getName());
			service.cleanSala(idSala);
			Ronda newRonda;
            try {
                newRonda = service.newRound(idSala);
            }catch(StripesLinkException e) {
                newRonda = null;
            }
			msgt.convertAndSend("/topic/Sala." + idSala + ".Ganador", new WinnerMessage(p.getName(), newRonda));
		}
		msgt.convertAndSend("/topic/Chat." + idSala + "." + equipo, msg);
	}
}
