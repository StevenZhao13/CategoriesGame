package party_mng;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import resource_mng.ResourceManager;
import toolbox.ToolBox;
import exceptions.IllegalAuthorizationException;
import exceptions.PartyMaxPlayerException;
import exceptions.PartyNotFoundException;

public class GameParty implements GamePartyInterface{
	
	public static int MAX_PLAYER = 8;
	private int partyID;
	
	
	private static int PREP_PHASE = 0, ANSWERING_PHASE = 1, VOTE_PHASE = 2, RESULT_PHASE = 3; 
	private int GamePhase;
	
	private Player host;
	private Player[] players;			// Array holding pointers to all the players.
	// "players[0]" holds the same pointer as "host" holds.
	// We holds it in array "players" 

	
	/**
	 * Constructo
	 * 
	 * This constructor does:
	 * 		1. Instantiate the host player. 
	 * 
	 * @param hostSession
	 * @param Input
	 */
	public GameParty(Session hostSession, JSONObject Input, int partyID){
		this.partyID = partyID;
		this.players = new Player[MAX_PLAYER];
		
		
		this.host = new Player(hostSession, Input);
		this.players[0] = this.host;

		
		JSONObject message = this.fabricateCreationNotifyJson();
		this.host.sendJSONFile(message);
	}
	
	
	
	/**
	 * 
	 */
	@Override
	public void joinPlayer(Session hostSession, JSONObject jsonInput) throws PartyMaxPlayerException{

		int playerIndex;
		boolean slotFound = false;
		
		for (playerIndex = 0; playerIndex < this.players.length && !slotFound ; playerIndex++){
			if (this.players[playerIndex] == null){
				this.players[playerIndex] = new Player(hostSession, jsonInput);
				slotFound = true;
			} else {}
		}
		
		
		// Logic to notify the player that the max player count has been reached
		if (playerIndex >= this.players.length){
			throw new PartyMaxPlayerException("Max player has been reached for the game.");
		}

		this.sendUpdatedPlayerList();
	}


	
	@Override
	public void sendUpdatedPlayerList() {
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
	public void startParty(JSONObject jsonInput) throws IllegalAuthorizationException{
		
		// Cool start the shit
		
		


	}


	public void sendQuestionList(){
		
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
	}




	public void sendVoteList(){
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


	public void sendResultList(){
	
	}


	@Override
	public void restartParty() {
		// TODO Auto-generated method stub

	}

	
	
	
	
	/**
	 * @return
	 */
	@Override
	public JSONObject fabricateCreationNotifyJson() {
		JSONObject json = new JSONObject();
		json.put("type", "ConfirmInit");
		json.put("partyID", this.partyID);
		
		return json;
	}




	/**
	 * This method fabricate a JSON that contains the updated player list of this Game Party
	 * 
	 * @return
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

		json.put("nameList", jarray);

		return json;
	}



	/**
	 * 
	 * @return
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


	/**
	 * 
	 * 
	 * @return
	 */
	@Override
	public JSONObject fabricateVoteListJson() {
		// Prep the JSON. Is same for all the players
		JSONObject json = new JSONObject();

		json.put("type", "SendLetterNList");
		json.put("letter", ToolBox.rollLetter());


		return json;
	}


	
	/**
	 * 
	 * 
	 * @return
	 */
	@Override
	public JSONObject fabricateResultNListJson() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 
	 * 
	 * @return
	 */
	@Override
	public JSONObject fabricateRestartJson() {
		// TODO Auto-generated method stub
		return null;
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
