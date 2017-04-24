package session_mng;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import resource_mng.ResourceManager;
import toolbox.ToolBox;
import exceptions.PartyMaxPlayerException;
import exceptions.PartyNotFoundException;

public class GameParty implements GamePartyInterface{
	public static int MAX_PLAYER = 8;
	private Player[] players;
	
	
	
	/**
	 * 
	 * @param hostSession
	 */
	public GameParty(Session hostSession, JSONObject json){
		this.players = new Player[MAX_PLAYER];
		this.players[0] = new Player(hostSession, json);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void joinPlayer(Session hostSession, JSONObject json) 
			throws PartyMaxPlayerException{
		
		int i;
		for (i = 0; i < this.players.length; i++){
			if (this.players[i] == null){
				this.players[i] = new Player(hostSession, json);
			} else {}
		}
		
		if (i >= this.players.length){
			throw new PartyMaxPlayerException("Max Player Reached");
		}
		
		this.sendUpdatePlayerList();
	}

	

	/**
	 * This method sends to every player in the list
	 * a brand new list of player 
	 */
	@Override
	public void sendUpdatePlayerList() {
		
		// Prep the JSON. Is same for all the players
		JSONObject json = new JSONObject();
		json.put("type", "SendPlayerList");

		JSONArray jarray = new JSONArray();
		
		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){
				jarray.add(this.players[i].getName());
			} else {}
		}
		
		json.put("names", jarray);
		
		
		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){
				
				// Sending new player to all players
				this.players[i].getWebSocketSession().getAsyncRemote().sendObject(json);
			} else {}
		}
	}
	
	
	
	/**
	 * 
	 */
	@Override
	public void startParty(JSONObject json) {
		
		

	}

	
	/**
	 * 
	 */
	@Override
	public void sendLetterNList() {
		
		// Prep the JSON. Is same for all the players
		JSONObject json = new JSONObject();
		
		json.put("type", "SendLetterNList");
		json.put("letter", ToolBox.rollLetter());
		
		JSONArray jarray = new JSONArray();
		jarray.addAll(ResourceManager.getRandomCatList());
		json.put("categoryList", jarray);
		
		
		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){
				this.players[i].getWebSocketSession().getAsyncRemote().sendObject(json);
			} else {}
		}
		
	}

	@Override
	public void sendVoteList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restartParty() {
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
