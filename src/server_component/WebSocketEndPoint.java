package server_component;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
		try {
			obj = parser.parse(message);
			JSONObject jsonObject =  (JSONObject) obj;
			
			
			
			
			
		} catch (ParseException e1) {
			session.getAsyncRemote().sendText("");
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
