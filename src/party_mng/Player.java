package party_mng;

import java.net.InetAddress;

import javax.websocket.Session;

import org.json.simple.JSONObject;

public class Player {
	Session webSocketSession;		// Session that allows th
	
	String name;
	
	String deviceID; 
	
	String[] answers;
	
	public Player(Session sess, JSONObject json){
		this.webSocketSession = sess;
		
		this.name = (String) json.get("name");
		
		this.deviceID = (String) json.get("deviceID");
		
	}

	
	
	public void sendJSONFile(JSONObject json){
		this.webSocketSession.getAsyncRemote().sendText(json.toString());	
	}
	
	
	
	public Session getWebSocketSession() {
		return webSocketSession;
	}

	public void setWebSocketSession(Session webSocketSession) {
		this.webSocketSession = webSocketSession;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
	
	
	
}
