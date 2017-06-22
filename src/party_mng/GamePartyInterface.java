package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.PartyMaxPlayerException;

public interface GamePartyInterface {

	
	public void joinPlayer(Session hostSession, JSONObject json);
	
	public void startParty(JSONObject json);
	
	public void receiveAnswerList(JSONObject json);
	
	public void receiveCompletedVoteList(JSONObject json);
	
	public void restartParty();
	
	
	
	public JSONObject fabricateUpdatePlayerListJson();
	
	public JSONObject fabricateLetterNListJson();

	public JSONObject fabricateVoteListJson();

	public JSONObject fabricateResultNListJson();
	
	public JSONObject fabricateRestartJson();
	
	void sendToAll(JSONObject json);
	
}
