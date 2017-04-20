package session_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.PartyMaxPlayerException;
import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;
import exceptions.JSONFormatException;


public class PartyManager implements PartyManagerInterface {
	public GameParty[] parties;
	
	
	public PartyManager(){
		// The maximum amount of number a four-digit hex can represent
		// is 16^4-1 = 65535, so we set the size of array to be 65535
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
	 * 
	 */
	@Override
	public void joinParty(Session hostSession, JSONObject json)
			throws PartyNotFoundException, PartyMaxPlayerException{
		
		Integer partyIndex = (Integer) json.get("partyID");
		if (this.parties[partyIndex] == null){
			throw new PartyNotFoundException("requested specific party not found");
		} else {
			this.parties[partyIndex].joinPlayer(hostSession, json);
		}
 		
	}

	
	
	/**
	 * This method, by inputing the correct index, returns the exact party
	 * @throws JSONFormatException PartyNotFoundException
	 */
	@Override
	public void partyBusiness(JSONObject json) 
			throws PartyNotFoundException, JSONFormatException{
		
		int index = (int) json.get("partyID");
		GameParty handler; 
		if (this.parties[index] == null){
			throw new PartyNotFoundException("requested specific party not found");
		}
		
		handler = this.parties[index];
		
		String type = (String) json.get("type");
		
		//Start case handling 
		
		switch (type){
		case "StartParty": 				handler.startParty(json);;
		case "SendAnswerList": 			handler.receiveAnswerList(json);;
		case "SendCompletedVoteList": 	handler.receiveCompletedVoteList(json);;
		case "RestartParty": 			handler.restartParty();
		
		
		default: throw new JSONFormatException("JSON file format can't parse");
		}
		
		
		
		// Will return null if session does not exist. Will handle that in downstream
	}



	
	
}
