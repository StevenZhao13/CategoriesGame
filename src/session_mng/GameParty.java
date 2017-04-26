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


		this.sendJsonToAll(json);
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


		this.sendJsonToAll(json);
	}


	/**
	 * 
	 */
	@Override
	public void receiveAnswerList(JSONObject json) {
		String deviceid = (String) json.get("deviceID");


		boolean correctPlayerFound = false;
		for (int i = 0; i < this.players.length; i++){
			// Check for current player exist. 
			if (this.players[i] != null){

				if (deviceid.equals(this.players[i].deviceID)){
					correctPlayerFound = true;

					JSONArray cars = (JSONArray) json.get("categories");
					String[] arr = (String[]) cars.toArray();

					this.players[i].setAnswers(arr);
				}
			} else {}
		}


		// Handle situation of player not found.
		// TO be work on
		if (correctPlayerFound == false){

		}



		// Go thru every player to check if all answers are in. 
		boolean allIn = true;
		for (int i = 0; i < this.players.length && allIn == true; i++){
			// Check for current player exist. 
			if (this.players[i] != null){
				// Check for if the answer is in.
				if (this.players[i].getAnswers() == null){
					// If any of the answer is not in, then set it to false
					allIn = false;
				} else {}
			} else {}
		}
		
		
		
		// If all results are in, send vote list
		if (allIn)				this.sendVoteList();
	}



	/**
	 * 
	 */
	@Override
	public void sendVoteList() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restartParty() {
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




	private void sendJsonToAll(JSONObject json){
		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){
				// Sending JSON package
				this.players[i].getWebSocketSession().getAsyncRemote().sendObject(json);
			} else {}
		}

	}
}
