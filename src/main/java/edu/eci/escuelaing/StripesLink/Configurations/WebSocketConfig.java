package edu.eci.escuelaing.StripesLink.Configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;

import edu.eci.escuelaing.StripesLink.Security.JwtFilterRequest;
import edu.eci.escuelaing.StripesLink.Security.JwtUtils;
import edu.eci.escuelaing.StripesLink.Security.UserService;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/connectSocket").setAllowedOriginPatterns("*").withSockJS().setWebSocketEnabled(false)
				.setSessionCookieNeeded(false);
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		System.out.println("----------------------------1");
		registration.interceptors(new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				System.out.println("----------------------------2");
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					List<String> tokenList = accessor.getNativeHeader("Authorization");
					System.out.println("----------Autenticacion Socket---------------" + tokenList.get(0));
					String jwtToken = null;
					if (tokenList == null || tokenList.size() < 1) {
						return message;
					} else {
						jwtToken = tokenList.get(0);
						if (jwtToken == null) {
							return message;
						}
					}
					String username = null;
					if (tokenList.get(0) != null && tokenList.get(0).startsWith("Bearer ")) {
						jwtToken = tokenList.get(0).substring(7);
						username = jwtUtils.extractUsername(jwtToken);
					}

					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UserDetails currentUserDetails = userService.loadUserByUsername(username);
						Boolean tokenValidated = jwtUtils.validateToken(jwtToken, currentUserDetails);
						if (tokenValidated) {
							UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
									currentUserDetails, null, currentUserDetails.getAuthorities());

							SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
							accessor.setUser(usernamePasswordAuthenticationToken);
						}
					}
				}
				return message;

			}
		});
	}
}
