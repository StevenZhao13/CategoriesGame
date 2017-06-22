package party_mng;

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
	 * Initiating a session. 
	 * @param hostSession
	 */
	public GameParty(Session hostSession, JSONObject Input){
		this.players = new Player[MAX_PLAYER];
		this.players[0] = new Player(hostSession, Input);
		
	}


	/**
	 * 
	 */
	@Override
	public void joinPlayer(Session hostSession, JSONObject jsonInput){

		int i;
		for (i = 0; i < this.players.length; i++){
			if (this.players[i] == null){
				this.players[i] = new Player(hostSession, jsonInput);
			} else {}
		}

		if (i >= this.players.length){
			// Logic to notify the player that the max player count has been reached
		}

		
		// Sending 
		// Fabricated JSON 
		// to all 
		JSONObject jsonResponse = this.fabricateUpdatePlayerListJson();
		this.sendToAll(jsonResponse);
	}






	/**
	 * 
	 */
	@Override
	public void startParty(JSONObject jsonInput) {



	}

	
	
	



	/**
	 * This method is invoked upon receiving compiled answer list from user.
	 * If all answers arrives, Will invoke sending of vote list to players
	 * 
	 * FUDGING BUNCHY!
	 *  Optimize if you have time
	 * 
	 */
	@Override
	public void receiveAnswerList(JSONObject jsonInput) {
		String deviceid = (String) jsonInput.get("deviceID");


		boolean correctPlayerFound = false;
		for (int i = 0; i < this.players.length; i++){
			// Check for current player exist. 
			if (this.players[i] != null){

				if (deviceid.equals(this.players[i].deviceID)){
					correctPlayerFound = true;

					JSONArray cars = (JSONArray) jsonInput.get("categories");
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
		boolean allCheck = true;

		for (int i = 0; i < this.players.length && allCheck == true; i++){
			// Check for current player exist. 
			if (this.players[i] != null){
				// Check for if the answer is in.
				if (this.players[i].getAnswers() == null){
					// If any of the answer is not in, then set it to false
					allCheck = false;
				} else {}
			} else {}
		}

		
		// If all results are in, send vote list
		if (allCheck){
			JSONObject jsonResponse = this.fabricateUpdatePlayerListJson();
			this.sendToAll(jsonResponse);;
		}
	}








	/**
	 * 
	 */
	@Override
	public void receiveCompletedVoteList(JSONObject jsonInput) {
		// TODO Auto-generated method stub

	}

	
	
	
	
	


	
	/**
	 * This method fabricate a JSON that contains the updated player list of this Game Party
	 * 
	 */
	@Override
	public JSONObject fabricateUpdatePlayerListJson() {
		JSONObject json = new JSONObject();
		json.put("type", "SendPlayerList");

		JSONArray jarray = new JSONArray();

		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){
				jarray.add(this.players[i].getName());
			} else {}
		}

		json.put("names", jarray);
		
		
		return json;
	}


	
	/**
	 * 
	 */
	@Override
	public JSONObject fabricateLetterNListJson() {
		JSONObject json = new JSONObject();

		json.put("type", "SendLetterNList");
		json.put("letter", ToolBox.rollLetter());

		JSONArray jarray = new JSONArray();
		jarray.addAll(ResourceManager.getInstance().getRandomCatList());
		json.put("categoryList", jarray);


		return json;
	}


	@Override
	public JSONObject fabricateVoteListJson() {
		// Prep the JSON. Is same for all the players
		JSONObject json = new JSONObject();

		json.put("type", "SendLetterNList");
		json.put("letter", ToolBox.rollLetter());


		return json;
	}


	@Override
	public JSONObject fabricateResultNListJson() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public JSONObject fabricateRestartJson() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void restartParty() {
		// TODO Auto-generated method stub
		
	}


	
	
	
	/**
	 * Utility method that sends a json file to all the players
	 * @param jsonResponse
	 */
	@Override
	public void sendToAll(JSONObject jsonResponse){

		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){

				// If not empty, Sending JSON package
				this.players[i].sendJSONFile(jsonResponse);
			} else {}
		}
	}




}
