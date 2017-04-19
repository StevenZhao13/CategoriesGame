package session_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;

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
	public int initParty(Session hostSession, JSONObject json) throws PartyOverloadException{
		
		
		int i;
		boolean isCreated = false;
		for ( i = 0; i < this.parties.length && !isCreated; i++){
		// go thru every party slot until one thats empty is found
			
			if (this.parties[i] == null){
				// Create new party
				
				//******************
				// TO-BE WORKED ON!
				//******************
				this.parties[i] = new GameParty(hostSession);
				isCreated = true;
				
			} else {}
		}
		
		
		if (i >= this.parties.length){
			throw new PartyOverloadException("Session Number exceeds maximum");
		} else {
			return i;
		}
		
	}

		
	/**
	 * 
	 */
	@Override
	public int joinParty(Session hostSession, JSONObject json)
			throws PartyNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	/**
	 * This method, by inputing the correct index, returns the exact party
	 */
	@Override
	public void partyBusiness(JSONObject json) throws PartyNotFoundException{
		
		
		int index = (int) json.get("partyID");
		
		
		if (this.parties[index] == null){
			throw new PartyNotFoundException("Specific party not found");
		}
		// Will return null if session does not exist. Will handle that in downstream
	}



	
	
}
