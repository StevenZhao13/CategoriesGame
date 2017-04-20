package tryshit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Proving {

	
	public static void main(String[] args){
		
		JSONObject json = new JSONObject();
		json.put("type", "SendPlayerList");
		
		JSONArray arr = new JSONArray();
		
		JSONObject foo = new JSONObject();
		foo.put("hehe", "sed");

		arr.add(foo);
		
		
		json.put("names", arr);
		
		arr.add("tyrone");
		arr.add("alice");
		arr.add("blake");
		arr.add("lizzie");

		System.out.println(json.toJSONString());
		System.out.println(json.toString());


		
		
	}
}
