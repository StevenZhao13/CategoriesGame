package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.IllegalAuthorizationException;
import exceptions.JSONFormatException;
import exceptions.PartyMaxPlayerException;
import exceptions.PlayerNotFoundException;

public interface GamePartyInterface {

	public void joinPlayer(Session hostSession, JSONObject json) throws PartyMaxPlayerException, JSONFormatException;
	
	public void sendUpdatedPlayerList();
	
	public void startParty(JSONObject json) throws IllegalAuthorizationException;
	
	public void sendQuestionList();

	public void receiveAnswerList(JSONObject json) throws PlayerNotFoundException, JSONFormatException;
	
	public void attemptSendVoteList();

	public void receiveCompletedVoteList(JSONObject json) throws PlayerNotFoundException, JSONFormatException;
	
	public void attemptSendResultList();

	public void restartParty();
	
	public void endParty();

	public Player findPlayerByDeviceID(String deviceID) throws PlayerNotFoundException;
	
	
	public JSONObject fabricateCreationNotifyJson();
	
	public JSONObject fabricateUpdatePlayerListJson();
	
	public JSONObject fabricateLetterNListJson();

	public JSONObject fabricateVoteListJson();

	public JSONObject fabricateResultNListJson();
	
	public JSONObject fabricateRestartJson();
	
	void sendToAll(JSONObject json);
	
}
