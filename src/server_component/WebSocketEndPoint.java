package server_component;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import exceptions.JSONFormatException;
import exceptions.CustomException;
import party_mng.PartyManager;


@ServerEndpoint("/hello")
public class WebSocketEndPoint {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	
	
	@OnOpen
	public void onOpen(Session session){
		logger.info("Connected ... " + session.getId());

	}


	
	@OnMessage
	public String onMessage(String message, Session session){
		JSONParser parser = new JSONParser();
		Object obj;
		JSONObject jsonObject;

		try {
			obj = parser.parse(message);
			jsonObject =  (JSONObject) obj;
			PartyManager.getInstance().partyBusiness(session, jsonObject);

		} catch (ParseException e) {
			session.getAsyncRemote().sendText("Malformed message. JSON string can not be parsed at all.");
			
		} catch (CustomException e) {
			session.getAsyncRemote().sendText(e.getErrorJsonMsg().toString());

		}


		switch (message) {
		case "quit":
			try {
				session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game ended"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			break;
		}
		
		return message;
	}

	
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
	}
	
	
}
