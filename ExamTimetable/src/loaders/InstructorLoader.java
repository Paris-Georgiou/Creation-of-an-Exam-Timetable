package loaders;

import java.util.Iterator;

import org.json.simple.JSONArray;

import helpers.Instructor;
import helpers.Storage;

public class InstructorLoader extends BaseLoader {
		
	public InstructorLoader()
	{
		filename = "files/instructors.txt";
		storage = Storage.getInstance();
	}
	
	protected void writeStorage()
	{
		iterator = json.iterator();
        Long code;
        String name, aday;
        int i, jaSize;
        String[] absence;
        JSONArray ja;
        Iterator <String> jaiterator;
        
		 while (iterator.hasNext()) {
			 j = iterator.next();
			 
			 code = (Long)j.get("code");
			 name = (String)j.get("name");
			 ja = (JSONArray)j.get("absence");
			 
			 jaSize = ja.size();
			 
			 if (jaSize>0) {
				 jaiterator = ja.iterator();
				 absence = new String[jaSize];
				 i=0;
				 
				 while(jaiterator.hasNext()) {
					 aday = (String) jaiterator.next();
					 absence[i++] = aday;
				 }
			 } else {
				 absence = null;
			 }
			 
            storage.addInstructor(new Instructor(name, code.intValue(), absence));
        }
	}
}
