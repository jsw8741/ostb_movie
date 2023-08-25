package com.example.ostb_movie.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.config.ServerEndpointConfigurator;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Service
@ServerEndpoint(value="/chatt/{roomId}",configurator = ServerEndpointConfigurator.class)
public class WebSocketChatt {
	  private static Map<String, Set<Session>> roomSessions = new HashMap<>();
	  private final ChattService chattService;
	  
	    public WebSocketChatt(ChattService chattService) {
	        this.chattService = chattService;
	    }
	  
	    @OnOpen
	    public void onOpen(Session session, @PathParam("roomId") String roomId) {
	        roomSessions.computeIfAbsent(roomId, key -> Collections.synchronizedSet(new HashSet<>()))
	            .add(session);
	    }

	    @OnMessage
	    public void onMessage(String msg, Session session, @PathParam("roomId") String roomId) throws Exception {
	        Set<Session> clientsInRoom = roomSessions.getOrDefault(roomId, Collections.emptySet());
	        for (Session s : clientsInRoom) {
	            s.getBasicRemote().sendText(msg);
	        }
	    }

	    @OnClose
	    public void onClose(Session session,@PathParam("roomId") String roomId) {
	        Set<Session> clientsInRoom = roomSessions.getOrDefault(roomId, Collections.emptySet());
	        clientsInRoom.remove(session);
	        chattService.updateChatt(roomId);
	    }
}
