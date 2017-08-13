package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import exceptions.IllegalAuthorizationException;
import exceptions.JSONFormatException;
import exceptions.CustomException;
import exceptions.PartyMaxPlayerException;
import exceptions.PartyNotFoundException;
import exceptions.PartyOverloadException;

public interface PartyManagerInterface {
		
	public void initParty(Session hostSession, JSONObject json)
			throws PartyOverloadException, JSONFormatException;
	

	public void terminateParty(int index, JSONObject json)
			throws CustomException;
	
		
	public void partyBusiness(Session hostSession, JSONObject json) 
			throws CustomException, JSONFormatException;

}
