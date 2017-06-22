package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.JSONFormatException;
import exceptions.PartyMaxPlayerException;
import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;

public interface PartyManagerInterface {
		
	public int initParty(Session hostSession, JSONObject json)
			throws PartyOverloadException;
	
	public void partyBusiness(Session hostSession, JSONObject json) 
			throws PartyNotFoundException, JSONFormatException;

}
