package server_component;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/hello")
public class WebSocketEndPoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());

	@OnOpen
	public void OnOpen(Session session){
        logger.info("Connected ... " + session.getId());

	}
	
	
	@OnMessage
	public String sayHello(String message, Session session){
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
