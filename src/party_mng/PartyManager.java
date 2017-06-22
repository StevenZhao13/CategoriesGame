package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.PartyMaxPlayerException;
import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;
import exceptions.JSONFormatException;


/**
 *	This is a manager for all the Game parties instantiated. 
 * 
 *	It holds all the parties with an array of 65535 size.
 * 			Reason why 65535:
 * 			Player will join the same party thru the share of a four-digit hex code
 * 					eg: 5EA2
 * 			And a four digit code can only represent 65535 (2^16-1) numbers.
 * 
 * 
 * @author zhaoy
 *
 */
public class PartyManager implements PartyManagerInterface {
	public static GameParty[] parties;


	public PartyManager(){
		this.parties = new GameParty[65535]; 
	}


	/**
	 * Return the unique index of Party
	 */
	@Override
	public int initParty(Session hostSession, JSONObject json) 
			throws PartyOverloadException{


		int i;
		boolean isCreated = false;
		for ( i = 0; i < this.parties.length && !isCreated; i++){


			// go thru every party slot until one thats empty is found
			if (this.parties[i] == null){

				this.parties[i] = new GameParty(hostSession, json);
				isCreated = true;

			} else {}
		}


		if (i >= this.parties.length){
			throw new PartyOverloadException("Session Number exceeds maximum");
		} else {

			// Return the index. Index will be converted to HEX on client
			return i;
		}

	}



	/**
	 * This method handles all the business
	 * @throws JSONFormatException PartyNotFoundException
	 */
	@Override
	public void partyBusiness(Session hostSession, JSONObject jsonInput) 
			throws PartyNotFoundException, JSONFormatException{

		String type = (String) jsonInput.get("type");


		// Check for if theres no "type" entry in the Json
		if (type == null)			throw new JSONFormatException("JSON file format can't parse");



		if (type.equals("initiateParty")){
			this.initParty(hostSession, jsonInput);
		} else {
			
			int partyIndex = (int) jsonInput.get("partyID");
			GameParty thisParty; 
			
			// Check for if the requested party even exist. 
			if (this.parties[partyIndex] == null)		throw new PartyNotFoundException("requested specific party not found");
			
			
			thisParty = this.parties[partyIndex];



			//Start case handling 

			switch (type){
			case "joinParty":				thisParty.joinPlayer(hostSession, jsonInput);
			case "StartParty": 				thisParty.startParty(jsonInput);;
			case "SendAnswerList": 			thisParty.receiveAnswerList(jsonInput);;
			case "SendCompletedVoteList": 	thisParty.receiveCompletedVoteList(jsonInput);;
			case "RestartParty": 			thisParty.restartParty();

			default: throw new JSONFormatException("JSON file format can't parse");
			}



		}

		// Will return null if session does not exist. Will handle that in downstream
	}





}
