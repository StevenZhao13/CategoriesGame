package session_mng;

import javax.websocket.Session;

import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;

public interface PartyManagerInterface {
		
	public int initParty(Session hostSession) throws PartyOverloadException;
		
	public GameParty getSpecificParty(int index) throws PartyNotFoundException;
	
	public void terminateSpecificParty(int index);

	
	
}
