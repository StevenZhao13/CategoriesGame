package session_mng;

import javax.websocket.Session;

import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;

public class PartyManager implements PartyManagerInterface {
	public GameParty[] sessions;
	
	
	public PartyManager(){
		// The maximum amount of number a four-digit hex can represent
		// is 16^4-1 = 65535, so we set the size of array to be 65535
		this.sessions = new GameParty[65535]; 
		
	}

	
	/**
	 * Return the unique index of Party
	 */
	@Override
	public int initParty(Session hostSession) throws PartyOverloadException{
		
		int i;
		boolean isCreated = false;
		for ( i = 0; i < this.sessions.length && !isCreated; i++){
		// go thru every session slot until one thats empty is found
			
			if (this.sessions[i] == null){
				// Create new session
				
				//******************
				// TO-BE WORKED ON!
				//******************
				this.sessions[i] = new GameParty(hostSession);
				isCreated = true;
				
			} else {}
		}
		
		
		if (i >= this.sessions.length){
			throw new PartyOverloadException("Session Number exceeds maximum");
		} else {
			return i;
		}
		
	}

	
	/**
	 * 
	 */
	public boolean joinParty(int index, Session hostSession) throws PartyNotFoundException{
		
		
		return false;
	}
	
	
	
	/**
	 * This method, by inputing the correct index, returns the exact Session
	 */
	@Override
	public GameParty getSpecificParty(int index) throws PartyNotFoundException{
		
		if (this.sessions[index] == null){
			throw new PartyNotFoundException("Specific session not found");
		}
		return this.sessions[index];
		// Will return null if session does not exist. Will handle that in downstream
	}

	
	
	/**
	 * This method terminate a specific session
	 */
	@Override
	public void terminateSpecificParty(int index) {
		
		// Simply set the pointer to the session to null
		// Then GC will do the work
		this.sessions[index] = null;
	}
}
