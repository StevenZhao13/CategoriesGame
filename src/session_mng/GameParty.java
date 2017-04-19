package session_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.PartyNotFoundException;

public class GameParty implements GamePartyInterface{
	public static int MAX_PLAYER = 8;
	private Player[] players;
	
	
	
	/**
	 * 
	 * @param hostSession
	 */
	public GameParty(Session hostSession){
		this.players = new Player[MAX_PLAYER];
		this.players[0] = new Player(hostSession);
	}
	

	@Override
	public void sendUpdatePlayerList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendLetterNList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendVoteList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean joinPlayer(Session hostSession, JSONObject json) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startParty(JSONObject json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveAnswerList(JSONObject json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveCompletedVoteList(JSONObject json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendResultList() {
		// TODO Auto-generated method stub
		
	}


}
