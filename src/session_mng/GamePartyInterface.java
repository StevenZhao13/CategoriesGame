package session_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

public interface GamePartyInterface {

	
	public boolean joinPlayer(Session hostSession, JSONObject json);
	
	public void startParty(JSONObject json);
	
	public void receiveAnswerList(JSONObject json);
	
	public void receiveCompletedVoteList(JSONObject json);
	
	
	
	
	public void sendUpdatePlayerList();
	
	public void sendLetterNList();
	
	public void sendVoteList();
	
	public void sendResultList();

	public void reset();
	
}
