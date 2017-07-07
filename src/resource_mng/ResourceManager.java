package resource_mng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * ResourceManager that handles loading the static resource & allocating them for use of games.
 * 
 * Utilizes singleton design.
 * 
 * @author zhaoy
 *
 */
public class ResourceManager {
	private static ResourceManager singletonResManager;
	
	
	public String[] categoriesRes;
	
	
	
	public ResourceManager(){
	}

	
	
	/**
	 * Singleton get method that ensures the exist of only one 
	 * @return
	 */
	public static ResourceManager getInstance(){
		if (singletonResManager == null){
			singletonResManager = new ResourceManager();	
			singletonResManager.loadRes();
		}
		
		return singletonResManager;
	}
	
	
	
	/**
	 * 
	 * @param path
	 */
	public void loadRes(){

		System.out.println("loading resource to resource manager");

		StringBuilder pathSB = new StringBuilder();
		pathSB.append(System.getProperty("user.dir"));
		pathSB.append("\\WebContent\\WEB-INF\\res");
		
		String path = pathSB.toString();
		try {

			System.out.println("accessing: " + path + "\\cat.json");

			FileReader fr = new FileReader(path+ "\\cat.json");
			
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(fr);

			JSONObject jsonObject =  (JSONObject) obj;

			JSONArray cars = (JSONArray) jsonObject.get("categories");
			Object[] arr = cars.toArray();

			categoriesRes = new String[arr.length];

			for (int i = 0; i < arr.length; i++){
				categoriesRes[i] = (String) arr[i];
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
		
		System.out.println("loading done");

		
	}


	/**
	 * This method returns 10 random categories
	 * @return in the form of an ArrayList containing Strings. Is thread safe.
	 * 
	 */
	public ArrayList<String> getRandomCatList(){
		System.out.println("getting random list");

		
		// Create array first for 10 unique categories indexes
		int[] indexes = getRandomTen(this.categoriesRes.length);


		// Fetch actual Strings in accord to the randomized ten indexes 
		// from resource stash.
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for (int i = 0; i < indexes.length; i++){
			ret.add(categoriesRes[ indexes[i] ]);	
		}

		return ret;	
	}
	
	
	
	
	/**
	 * ---LOW PERFORMANCE, COME BACK!---
	 * 
	 * @param length
	 * @return
	 */
	public static int[] getRandomTen(int length){
		int[] ret = new int[10];

		for (int i = 0; i < ret.length; i++){
			boolean duplicate;
			int rando;
			do{
				duplicate = false ;
				
				rando = (int) (length * Math.random());
				
				boolean duplicateFound = false;
				for (int j = 0; j < i && !duplicateFound; j ++ ){
//					System.out.println("comparing these two: " + rando + " and " + ret[j]);
					
					if (rando == ret[j]){		
						duplicate = true;
						duplicateFound = true;
					}
				}
				
			} while (duplicate);

//			System.out.println("Its it! its: " + rando);
			ret[i] = rando;
		}
		return ret;
	}


}
