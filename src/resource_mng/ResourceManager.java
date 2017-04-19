package resource_mng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ResourceManager {

	public String[] categoriesRes;



	public ResourceManager(){
		System.out.println("Initializing resource manager");

	}




	public void loadRes(String path){

		System.out.println("loading resource to resource manager");


		JSONParser parser = new JSONParser();

		try {

			System.out.println("accessing: " + path+ "/cat.json");

			FileReader fr = new FileReader(path+ "/cat.json");
			Object obj = parser.parse(fr);

			JSONObject jsonObject =  (JSONObject) obj;

			JSONArray cars = (JSONArray) jsonObject.get("categories");
			Iterator<String> iterator = cars.iterator();
			Object[] arr = cars.toArray();

			this.categoriesRes = new String[arr.length];

			for (int i = 0; i < arr.length; i++){
				this.categoriesRes[i] = (String) arr[i];
			}


			fr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {

		}

	}
}
