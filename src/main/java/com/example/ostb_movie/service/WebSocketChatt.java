package com.example.ostb_movie.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Service
@ServerEndpoint(value="/chatt/{roomId}")
public class WebSocketChatt {
	private static Set<Session> clients = 
			Collections.synchronizedSet(new HashSet<Session>());

	
	@OnOpen
	public void onOpen(Session s, @PathParam("roomId") String roomId) {
		System.out.println("open session : " + s.toString());
		if(!clients.contains(s)) {
			clients.add(s);
			System.out.println("session open : " + s);
		}else {
			System.out.println("이미 연결된 session 임!!!");
		}
	}
	
	
	@OnMessage
	public void onMessage(String msg, Session session, @PathParam("roomId") String roomId) throws Exception{
		System.out.println("receive message : " + msg);
		for(Session s : clients) {
			System.out.println("send data : " + msg);
			s.getBasicRemote().sendText(msg);

		}
		
	}
	
	@OnClose
	public void onClose(Session s, @PathParam("roomId") String roomId) {
		System.out.println("session close : " + s);
		clients.remove(s);
	}
}
