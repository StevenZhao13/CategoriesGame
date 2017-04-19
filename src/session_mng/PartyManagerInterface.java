package session_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;

public interface PartyManagerInterface {
		
	public int initParty(Session hostSession, JSONObject json) throws PartyOverloadException;
		
	public int joinParty(Session hostSession, JSONObject json)throws PartyNotFoundException;
	
	public void partyBusiness(JSONObject json) throws PartyNotFoundException;
	
}
