package party_mng;

import javax.websocket.Session;

import org.json.simple.JSONObject;

import resource_mng.ResourceManager;
import exceptions.IllegalAuthorizationException;
import exceptions.CustomException;
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
	public static PartyManager singletonPartyManager;
	
	public GameParty[] parties;


	public PartyManager(){
		this.parties = new GameParty[65535]; 
	}
	

	
	/**
	 * Singleton get method that returns the explicit instance of this class
	 * @return
	 */
	public static PartyManager getInstance(){
		if (singletonPartyManager == null){
			singletonPartyManager = new PartyManager();	
		}
		
		return singletonPartyManager;
	}

	

	/**
	 * This method is:
	 * 		Called upon for creating a new session in the Parties stash.
	 * 
	 * Does:
	 * 		1. Finds a proper vacant spot in the memory arracy for Parties/
	 * 			1a. If theres is proper spot Creates new instance of the Party and puts it in there. 
	 * 			2b. If theres none, throws exception. 
	 * 		
	 * @param hostSession
	 * @param jsonInput
	 */
	@Override
	public void initParty(Session hostSession, JSONObject jsonInput) 
			throws PartyOverloadException{
		
		// 1. Find the proper slot for the new Party to be hosted.
		int i;
		boolean isCreated = false;
		for ( i = 0; i < this.parties.length && !isCreated; i++){

			// go thru every party slot until one thats empty is found
			if (this.parties[i] == null){
				
				// 2. Initiate new instance of the Party
				this.parties[i] = new GameParty(hostSession, jsonInput, i);
				isCreated = true;
				
			} else {}
		}

		// 1b. If all the slots are occupied, throws exception 
		if (i >= this.parties.length)			throw new PartyOverloadException("Session Number exceeds maximum");		

		
		
	}
	
	/**
	 * 
	 * @param hostSession
	 * @param jsonInput
	 */
	@Override
	public void terminateParty(Session hostSession, JSONObject jsonInput)
			throws CustomException {
		// TODO Auto-generated method stub
		
	}




	/**
	 * This method is:
	 * 		Called upon anytime a message is being received from a client
	 * 
	 * It does: 
	 * 		
	 * @param hostSession
	 * @param jsonInput
	 * @throws JSONFormatException
	 * @throws CustomException 
	 */
	@Override
	public void partyBusiness(Session hostSession, JSONObject jsonInput) 
			throws CustomException, JSONFormatException{	
		
		// Checking Json format correctness. If not parseable throw exception for upstream handling.
		if (jsonInput.get("type") == null)			throw new JSONFormatException("JSON file format can't parse");		
		
		
		String type = (String) jsonInput.get("type");

		if (type.equals("InitiateParty")){
			// 1. Check if the inquiry is about creating a new Party, which can not be handled within a certain party.
			this.initParty(hostSession, jsonInput);
		
		} else if (type.equals("TerminateParty")) {
			// 2. 
			
			
		} else {
			// 3. If its an business inside a party, it proceeds here. 
		
			// Checking Json format correction. If not correct throw exception for upstream handling.
			if (jsonInput.get("partyID") == null)			throw new JSONFormatException("JSON file format can't parse");

			
			int partyIndex = (int) jsonInput.get("partyID");
			
			// Check for if the requested party even exist. 
			if (this.parties[partyIndex] == null)			throw new PartyNotFoundException("requested specific party not found");
			
			
			GameParty thisParty = this.parties[partyIndex];

			// Start case handling 

			switch (type){
			case "JoinParty":				thisParty.joinPlayer(hostSession, jsonInput);
			case "StartParty": 				thisParty.startParty(jsonInput);
			case "SendAnswerList": 			thisParty.receiveAnswerList(jsonInput);
			case "SendCompletedVoteList": 	thisParty.receiveCompletedVoteList(jsonInput);
			case "RestartParty": 			thisParty.restartParty();

			// 
			default: throw new JSONFormatException("JSON file format can't parse");
			}

		}
		// Will return null if session does not exist. Will handle that in downstream
	}
}
