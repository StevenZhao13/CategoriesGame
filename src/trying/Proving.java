package trying;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import resource_mng.ResourceManager;

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
		System.out.println("-----------------------");




		ArrayList<String> stuff = ResourceManager.getInstance().getRandomCatList();

		System.out.println("shit goes on");
		for (int i = 0; i < stuff.size(); i++){
			System.out.println("number " +i+ ": " + stuff.get(i));
		}

		
		
		
	}


	
	
	
}
