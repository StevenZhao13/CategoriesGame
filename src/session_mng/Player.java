package session_mng;

import java.net.InetAddress;

import javax.websocket.Session;

public class Player {
	Session webSocketSession;		// session 
	
	
	
	String[] answers;
	
	public Player(Session sess){
		this.webSocketSession = sess;
		
	}
}
