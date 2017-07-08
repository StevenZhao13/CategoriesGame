package party_mng;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import resource_mng.ResourceManager;
import toolbox.ToolBox;
import exceptions.IllegalAuthorizationException;
import exceptions.JSONFormatException;
import exceptions.PartyMaxPlayerException;
import exceptions.PartyNotFoundException;
import exceptions.PlayerNotFoundException;

public class GameParty implements GamePartyInterface{
	
	private int partyID;
	
	
	private int GamePhase;
	public static int PREP_PHASE = 0, ANSWERING_PHASE = 1, VOTE_PHASE = 2, RESULT_PHASE = 3; 

	
	private Player host;
	private Player[] players;			// Array holding pointers to all the players.
	public static int MAX_PLAYER = 8;	// 8 max player count

	// "players[0]" holds the same pointer as "host" holds.
	// We holds it in array "players" 

	
	/**
	 * This method is:
	 * 		Constructor
	 * 
	 * This constructor does:
	 * 		1. Instantiate the host player. 
	 * 
	 * @param hostSession
	 * @param Input
	 * @param partyID
	 */
	public GameParty(Session hostSession, JSONObject Input, int partyID){
		this.partyID = partyID;
		this.GamePhase = GameParty.PREP_PHASE;
		
		this.players = new Player[GameParty.MAX_PLAYER];
		this.host = new Player(hostSession, Input);
		this.players[0] = this.host;
		
		JSONObject message = this.fabricateCreationNotifyJson();
		this.host.sendJSONFile(message);
	}
	
	
	
	/**
	 * This method is:
	 * 		Called upon when a player request comes in, tryna join a party.
	 * 
	 * This method does:
	 * 		1. Finds a vacant spot
	 * 		1a. Instantiate the new Player and fits it in the spot
	 * 		1b. throws PartyMaxPlayerException 
	 * 
	 * @param hostSession
	 * @param jsonInput
	 * @throws PartyMaxPlayerException
	 */
	@Override
	public void joinPlayer(Session hostSession, JSONObject jsonInput) throws PartyMaxPlayerException{
		if (!(this.GamePhase == GameParty.PREP_PHASE)){
			
		}

		
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



	/**
	 * This method is:
	 * 		Called to send updated player list to all the players
	 * It does:
	 * 		1. fabricate
	 * 		2. send
	 */
	@Override
	public void sendUpdatedPlayerList() {
		// Sending
		// Fabricated JSON
		// to all
		JSONObject jsonResponse = this.fabricateUpdatePlayerListJson();
		this.sendToAll(jsonResponse);
	}



	/**
	 * This method is:
	 * 		Called when the host reuqests, to start the game
	 * It does:
	 * 		Switch game phase
	 * 		And send the question list
	 * 		
	 */
	@Override
	public void startParty(JSONObject jsonInput) throws IllegalAuthorizationException{
		if (!(this.GamePhase == GameParty.PREP_PHASE)){
			
		}

		// Cool start the shit
		
		this.GamePhase = GameParty.ANSWERING_PHASE;
		
		this.sendQuestionList();
	}


	
	/**
	 * This method is:
	 * 		Called upon for sending question list to all the players.
	 * 	It does:
	 * 		1. fabricate
	 * 		2. send

	 */
	public void sendQuestionList(){
		JSONObject questionList = this.fabricateUpdatePlayerListJson();
		this.sendToAll(questionList);
	}




	/**
	 * This method is
	 * 		Called upon receiving compiled answer list from user.
	 * If all answers arrives, Will invoke sending of vote list to players
	 * 
	 * FUDGING BUNCHY!
	 *  Optimize if you have time
	 * 
	 */
	@Override
	public void receiveAnswerList(JSONObject jsonInput) throws PlayerNotFoundException{
		if (!(this.GamePhase == GameParty.ANSWERING_PHASE)){
			
		}

		
		String deviceId = (String) jsonInput.get("deviceID");

		boolean correctPlayerFound = false;
		for (int i = 0; i < this.players.length; i++){
			// Check for current player exist. 
			if (this.players[i] != null){

				if (deviceId.equals(this.players[i].deviceID)){
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
			throw new PlayerNotFoundException("Requested player not found in the party");
		}
		
		this.attemptSendVoteList();
	}




	/**
	 * This method is:
	 * 		Called upon for attempting to send vote list to all the players
	 * 
	 * It does:
	 * 		1. Check if all the players have their answer list in.
	 * 		1a. If yes, fabricate the vote list and sends it.
	 * 		1b. If no, do nothing.
	 * 		
	 */
	public void attemptSendVoteList(){
		
		
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
			JSONObject voteList = this.fabricateUpdatePlayerListJson();
			this.sendToAll(voteList);;
		} else {}
	}




	/**
	 * This method is:
	 * 		Called upon receiving completed vote list from users.
	 * 
	 */
	@Override
	public void receiveCompletedVoteList(JSONObject jsonInput) throws PlayerNotFoundException{
		if (!(this.GamePhase == GameParty.VOTE_PHASE)){
			
		}
		
		String deviceId = (String) jsonInput.get("deviceID");

		boolean correctPlayerFound = false;
		for (int i = 0; i < this.players.length; i++){
			// Check for current player exist. 
			if (this.players[i] != null){

				if (deviceId.equals(this.players[i].deviceID)){
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
			throw new PlayerNotFoundException("Requested player not found in the party");
		}
		
		this.attemptSendVoteList();
	}

	
	
	/**
	 * 
	 */
	@Override
	public void attemptSendResultList(){
	
	}


	/**
	 * 
	 */
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

		// puts in Identifier of type
		json.put("type", "SendVoteList");
		
		// Puts the array in there for player ID look-up
		JSONArray arrayOfIDs = new JSONArray();
		
		// Compiling the array
		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){
				arrayOfIDs.add(this.players[i].getDeviceID());
			} else {}
		}
		json.put("idList", arrayOfIDs);
		
		
		// Puts each players alongside their array of answers in it.
		
		// Iterate thru every player
		for (int i = 0; i < this.players.length; i++){
			if (this.players[i] != null){
				// For each existing player
				
				JSONArray arrayOfAnswers = new JSONArray();
				
				// Puts the answers into JSonArray
				String[] answerArray = this.players[i].getAnswers();
				for (int j = 0; j < answerArray.length; j++){
					arrayOfAnswers.add(answerArray[j]);
				}
				
				// Puts the JSONArray with all the answer in it to the JSON, associated with its respective ID
				json.put(this.players[i].getDeviceID(), arrayOfAnswers);
			} else {}
		}
		
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
