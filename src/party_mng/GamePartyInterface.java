package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.IllegalAuthorizationException;
import exceptions.PartyMaxPlayerException;

public interface GamePartyInterface {

	public void joinPlayer(Session hostSession, JSONObject json) throws PartyMaxPlayerException;
	
	public void sendUpdatedPlayerList();
	
	public void startParty(JSONObject json) throws IllegalAuthorizationException;
	
	public void sendQuestionList();

	public void receiveAnswerList(JSONObject json);
	
	public void sendVoteList();

	public void receiveCompletedVoteList(JSONObject json);
	
	public void sendResultList();

	public void restartParty();
	
	
	
	public JSONObject fabricateCreationNotifyJson();
	
	public JSONObject fabricateUpdatePlayerListJson();
	
	public JSONObject fabricateLetterNListJson();

	public JSONObject fabricateVoteListJson();

	public JSONObject fabricateResultNListJson();
	
	public JSONObject fabricateRestartJson();
	
	void sendToAll(JSONObject json);
	
}
