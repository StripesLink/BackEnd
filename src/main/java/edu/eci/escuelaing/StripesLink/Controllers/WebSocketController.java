package edu.eci.escuelaing.StripesLink.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import edu.eci.escuelaing.StripesLink.Model.Mongo.SalaModel;


@Controller
public class WebSocketController {

	@Autowired
	SimpMessagingTemplate msgt;
	
	@MessageMapping("/sala.{numero}")
	public void handlePointEvent(String id, @DestinationVariable String numero) throws Exception {
		System.out.println("Nueva conexion a la sala:" + id);
		//msgt.convertAndSend("/topic/newpoint." + numero,  );
	}
}
