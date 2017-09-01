package exceptions;

import org.json.simple.JSONObject;

public class CustomException extends Exception {
	
	public CustomException(String message) {
		super(message);
	}
	
	public JSONObject getErrorJsonMsg(){
		JSONObject json = new JSONObject();
		json.put("type", "Exception");
		json.put("errorType", this.getClass().getSimpleName());
		json.put("errorMessage", this.getMessage());
		
		
		return json;
	}

}
