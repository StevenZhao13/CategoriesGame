package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.IllegalAuthorizationException;
import exceptions.IncorrectPhaseException;
import exceptions.JSONFormatException;
import exceptions.PartyMaxPlayerException;
import exceptions.PlayerNotFoundException;

public interface GamePartyInterface {

	public void joinPlayer(Session hostSession, JSONObject json) throws PartyMaxPlayerException, JSONFormatException, IncorrectPhaseException;
	
	public void sendUpdatedPlayerList();
	
	public void startParty(JSONObject json) throws IllegalAuthorizationException, IncorrectPhaseException;
	
	public void sendQuestionList();

	public void receiveAnswerList(JSONObject json) throws PlayerNotFoundException, JSONFormatException, IncorrectPhaseException;
	
	public void attemptSendVoteList();

	public void receiveCompletedVoteList(JSONObject json) throws PlayerNotFoundException, JSONFormatException, IncorrectPhaseException;
	
	public void attemptSendResultList();

	public void restartParty() throws IncorrectPhaseException;
	
	public void notifyTermination();

	
	public Player findPlayerByDeviceID(String deviceID) throws PlayerNotFoundException;
	
	
	public JSONObject fabricateConfirmInitJson();
	
	public JSONObject fabricateConfirmJoinJson();
	
	public JSONObject fabricateUpdatePlayerListJson();
	
	public JSONObject fabricateLetterNCategoryListJson();

	public JSONObject fabricateVoteListJson();

	public JSONObject fabricateResultListJson();
	
	public JSONObject fabricateConfirmRestartJson();
	
	public JSONObject fabricateConfirmTerminateJson();

	
	void sendToAll(JSONObject json);
	
}
