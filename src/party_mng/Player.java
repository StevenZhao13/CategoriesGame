package party_mng;

import java.net.InetAddress;

import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exceptions.JSONFormatException;

public class Player {
	Session webSocketSession;		// Session that allows th
	
	String name;
	
	String deviceID; 
	
	String[] answers;
	
	Integer[] rating;
	
	int votePackageReceived;
		

	public Player(Session sess, JSONObject json) throws JSONFormatException{
		this.webSocketSession = sess;

		this.name = (String) json.get("name");		
		// Check format, throw Exception if proper keyword not found
		if (this.name == null)			throw new JSONFormatException("JSON file format can't parse");

		this.deviceID = (String) json.get("deviceID");
		// Check format, throw Exception if proper keyword not found
		if (this.deviceID == null)			throw new JSONFormatException("JSON file format can't parse");

		
		this.rating = new Integer[this.answers.length];
		for (int i = 0; i < this.rating.length; i++){
			this.rating[i] = 0;
		}
		
		this.votePackageReceived = 0;

	}


	/**
	 * 
	 * @param jsonInput
	 * @throws JSONFormatException
	 */
	public void readVotes(JSONObject jsonInput) throws JSONFormatException{
		
		JSONArray arr = (JSONArray) jsonInput.get(this.deviceID);
		// Check format, throw Exception if proper keyword not found
		if (arr == null)			throw new JSONFormatException("JSON file format can't parse");

		
		
		Integer[] voteList = (Integer[]) arr.toArray();
		
		if(voteList.length != this.answers.length)	throw new JSONFormatException("Number of shit not right");
		
		for (int i = 0; i < this.rating.length; i++){
			this.rating[i] += voteList[i];		// voteList values are arbitrary only 0 and 1.
		}
		
		this.votePackageReceived ++ ;
	}
	
	
	
	/**
	 * This method sends the json String passed in asyncronously to the player. 
	 * @param json
	 */
	public void sendJSONFile(JSONObject json){
		this.webSocketSession.getAsyncRemote().sendText(json.toString());	
	}
	
	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
		
	}

	
	public Session getWebSocketSession() {
		return webSocketSession;
	}

	public void setWebSocketSession(Session webSocketSession) {
		this.webSocketSession = webSocketSession;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	
	public Integer[] getRating() {
		return rating;
	}


	public void setRating(Integer[] rating) {
		this.rating = rating;
	}


	public int getVotePackageReceived() {
		return votePackageReceived;
	}


	public void setVotePackageReceived(int votePackageReceived) {
		this.votePackageReceived = votePackageReceived;
	}


	
	
	
}
