package loaders;

import helpers.Department;
import helpers.Storage;

public class DepartmentLoader extends BaseLoader {
		
	public DepartmentLoader()
	{
		filename = "files/departments.txt";
		storage = Storage.getInstance();
	}
	
	protected void writeStorage()
	{
		iterator = json.iterator();
        Long code;
        String name;
        
		 while (iterator.hasNext()) {
			 j = iterator.next();
			 
			 code = (Long)j.get("code");
			 name = (String)j.get("department");
			 
            storage.addDepartment(new Department(name, code.intValue()));
        }
	}
}
