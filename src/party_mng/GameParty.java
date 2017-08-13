package party_mng;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import resource_mng.ResourceManager;
import toolbox.ToolBox;
import exceptions.IllegalAuthorizationException;
import exceptions.IncorrectPhaseException;
import exceptions.JSONFormatException;
import exceptions.PartyMaxPlayerException;
import exceptions.PlayerNotFoundException;

public class GameParty implements GamePartyInterface{

	private int partyID;


	private int GamePhase;
	public static int PREP_PHASE = 0, ANSWERING_PHASE = 1, VOTING_PHASE = 2, RESULT_PHASE = 3; 

	public static int MAX_PLAYER = 8;	// 8 max player count

	private Player host;
	private Player[] players;			// Array holding pointers to all the players.
	private int playerCount;

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
	 * @throws JSONFormatException 
	 */
	public GameParty(Session hostSession, JSONObject Input, int partyID) throws JSONFormatException{
		this.partyID = partyID;
		this.GamePhase = GameParty.PREP_PHASE;

		this.players = new Player[GameParty.MAX_PLAYER];
		this.host = new Player(hostSession, Input);
		this.players[0] = this.host;

		this.playerCount = 0;	this.playerCount ++ ;

		JSONObject message = this.fabricateConfirmInitJson();
		this.host.sendAsyncJSONFile(message);
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
	 * @throws JSONFormatException 
	 */
	@Override
	public void joinPlayer(Session hostSession, JSONObject jsonInput) throws IncorrectPhaseException, PartyMaxPlayerException, JSONFormatException{
		if (!(this.GamePhase == GameParty.PREP_PHASE)){
			throw new IncorrectPhaseException("Wrong phase");
		} else {


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


			this.playerCount ++ ;

			JSONObject message = this.fabricateConfirmInitJson();

			this.host.sendAsyncJSONFile(message);

			this.sendUpdatedPlayerList();
		}
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
	 * @throws IncorrectPhaseException 
	 * 		
	 */
	@Override
	public void startParty(JSONObject jsonInput) throws IllegalAuthorizationException, IncorrectPhaseException{
		if (!(this.GamePhase == GameParty.PREP_PHASE)){
			throw new IncorrectPhaseException("Wrong phase");
		} else {

			// Cool start the shit

			this.GamePhase = GameParty.ANSWERING_PHASE;

			this.sendQuestionList();
		}
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
	 * @throws JSONFormatException 
	 * @throws IncorrectPhaseException 
	 * 
	 */
	@Override
	public void receiveAnswerList(JSONObject jsonInput) throws PlayerNotFoundException, JSONFormatException, IncorrectPhaseException{
		if (!(this.GamePhase == GameParty.ANSWERING_PHASE)){
			throw new IncorrectPhaseException("Wrong phase");

		} else {


			// Acquire ID from JsonInput
			String deviceID = (String) jsonInput.get("deviceID");
			// Check format, throw Exception if proper keyword not found
			if (deviceID == null)			throw new JSONFormatException("JSON file format can't parse");


			// Finds the proper player with the right ID
			Player playerWhoSent = this.findPlayerByDeviceID(deviceID);


			// Acquire obtained answers from JsonInput
			JSONArray arr = (JSONArray) jsonInput.get("answerList");
			// Check format, throw Exception if proper keyword not found
			if (arr == null)			throw new JSONFormatException("JSON file format can't parse");		

			String[] answerList = (String[]) arr.toArray();



			// Fill in the answer
			playerWhoSent.setAnswers(answerList);


			// TRY SEND VOTE LIST
			this.attemptSendVoteList();
		}
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
			if (this.players[i] != null && this.players[i].getAnswers() == null){
				// If any of the answer is not in, then set it to false
				allCheck = false;
			} else {}
		}


		// If all results are in, send vote list
		if (allCheck){
			this.GamePhase = GameParty.VOTING_PHASE;

			JSONObject voteList = this.fabricateVoteListJson();
			this.sendToAll(voteList);;
		} else {}

	}





	/**
	 * This method is:
	 * 		Called upon receiving completed vote list from users.
	 * @throws JSONFormatException 
	 * @throws IncorrectPhaseException 
	 * 
	 */
	@Override
	public void receiveCompletedVoteList(JSONObject jsonInput) throws PlayerNotFoundException, JSONFormatException, IncorrectPhaseException{
		if (!(this.GamePhase == GameParty.VOTING_PHASE)){
			throw new IncorrectPhaseException("Wrong phase");

		} else {

			// Acquire ID from JsonInput
			String deviceID = (String) jsonInput.get("deviceID");
			// Check format, throw Exception if proper keyword not found
			if (deviceID == null)			throw new JSONFormatException("JSON file format can't parse");

			// Making sure the player with the specified ID exist, if not throws error to be handled upstream
			// This pointer serves no specific purpose. 
			Player playerWhoSent = this.findPlayerByDeviceID(deviceID);



			// Acquire list of player device IDs for vote regging one by one
			JSONArray arr = (JSONArray) jsonInput.get("IDList");
			// Check format, throw Exception if proper keyword not found
			if (arr == null)			throw new JSONFormatException("JSON file format can't parse");
			String[] IDList = (String[]) arr.toArray();


			// Assign votes to each players
			for (int i = 0; i < IDList.length; i++){
				Player voteReceiver = this.findPlayerByDeviceID(IDList[i]);

				voteReceiver.readVotes(jsonInput);
			}


			// TRY SEND RESULTS
			this.attemptSendResultList();
		}
	}



	/**
	 * This method is:
	 * 		Called upon for attempting to send result list to all the players
	 * 
	 * It does:
	 * 		1. Check if all the players have their votes in.
	 * 		1a. If yes, fabricate the result list and sends it.
	 * 		1b. If no, do nothing.
	 * 		
	 */
	@Override
	public void attemptSendResultList(){

		// Assume all votes are in UNTIL proven false
		boolean allCheck = true;


		for (int i = 0; i < this.players.length && allCheck == true; i++){
			// Check for current player exist. 
			if (this.players[i] != null && this.players[i].getVotePackageReceived() + 1 != this.playerCount){
				// If any of the answer is not in, then set it to false
				allCheck = false;
			} else {}
		}


		// If all results are in, send vote list
		if (allCheck){
			this.GamePhase = GameParty.RESULT_PHASE;

			JSONObject voteList = this.fabricateResultListJson();
			this.sendToAll(voteList);;
		} else {}

	}



	/**
	 * @throws IncorrectPhaseException 
	 * 
	 */
	@Override
	public void restartParty() throws IncorrectPhaseException {
		if (!(this.GamePhase == GameParty.VOTING_PHASE)){
			throw new IncorrectPhaseException("Wrong phase");

		} else {
			this.GamePhase = GameParty.PREP_PHASE;
			
			Player newHost = new Player(this.host);
			this.host = newHost;
			
			
			for (int i = 0; i < this.players.length; i++){
				// Check for current player exist. 
				if (this.players[i] != null){
					// If exist, create a new clean player to take place of the old one
					Player newPlayer = new Player(this.players[i]);
					this.players[i] = newPlayer;
				} else {}
			}




			JSONObject json = this.fabricateConfirmRestartJson();
			this.sendToAll(json);
		}
	}



	/**
	 * 
	 */
	@Override
	public void notifyTermination() {


		JSONObject json = this.fabricateConfirmTerminateJson();
		this.sendToAll(json);;
	}



	/**
	 * 
	 */
	public Player findPlayerByDeviceID(String deviceID) throws PlayerNotFoundException{
		Player ret = null;

		boolean correctPlayerFound = false;

		for (int i = 0; i < this.players.length; i++){

			// Check for current player exist. 
			if (this.players[i] != null && deviceID.equals(this.players[i].deviceID)){
				correctPlayerFound = true;

				ret = this.players[i];

			} else {}

		}

		if (correctPlayerFound == false){
			throw new PlayerNotFoundException("Requested player not found in the party");
		}

		return ret;
	}




	/**
	 * 
	 * =DONE PROPER=
	 * @return
	 */
	@Override
	public JSONObject fabricateConfirmInitJson() {
		JSONObject json = new JSONObject();
		json.put("type", "ConfirmInit");
		json.put("partyID", this.partyID);

		return json;
	}


	/**
	 * 
	 * =DONE PROPER=
	 * @return
	 */
	@Override
	public JSONObject fabricateConfirmJoinJson() {
		JSONObject json = new JSONObject();
		json.put("type", "ConfirmJoin");

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
		json.put("type", "UpdatePlayerList");

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
	public JSONObject fabricateLetterNCategoryListJson() {
		JSONObject json = new JSONObject();

		json.put("type", "LetterNCategoryList");
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
		json.put("type", "VoteList");

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
	public JSONObject fabricateResultListJson() {

		JSONObject json = new JSONObject();

		json.put("type", "ResultList");


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

				JSONArray arrayOfRatings = new JSONArray();

				// Puts the an=swers into JSonArray
				Integer[] answerArray = this.players[i].getRating();
				for (int j = 0; j < answerArray.length; j++){
					arrayOfRatings.add(answerArray[j]);
				}

				// Puts the JSONArray with all the answer in it to the JSON, associated with its respective ID
				json.put(this.players[i].getDeviceID(), arrayOfRatings);

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
	public JSONObject fabricateConfirmRestartJson() {
		JSONObject json = new JSONObject();

		json.put("type", "ConfirmRestart");

		return json;
	}



	/**
	 * 
	 * 
	 * @return
	 */
	@Override
	public JSONObject fabricateConfirmTerminateJson() {
		JSONObject json = new JSONObject();
		json.put("type", "ConfirmTerminate");

		return json;
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
				this.players[i].sendAsyncJSONFile(jsonResponse);
			} else {}
		}
	}











}
