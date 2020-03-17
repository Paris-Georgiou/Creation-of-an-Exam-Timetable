package loaders;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import helpers.Storage;


public abstract class BaseLoader {
	String filename;
	JSONArray json;
	Storage storage;
	Iterator<JSONObject> iterator;
    JSONObject j;
	/**
	 * Loads concrete class' file 
	 * files must be in json array format
	 * Stores file contents in json variable
	 * Is used to load files created by PHP script out of eclass' view-source 
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public void loadFile() throws FileNotFoundException, IOException, ParseException
	{
		JSONParser parser = new JSONParser();
		FileReader reader = new FileReader(filename);
		Object obj = parser.parse(reader);
		json = (JSONArray) obj;
//		System.out.println(json);
		writeStorage();
	}
	
	/**
	 * Extracts json data into Storage class
	 */
	protected abstract void writeStorage();
	
}
