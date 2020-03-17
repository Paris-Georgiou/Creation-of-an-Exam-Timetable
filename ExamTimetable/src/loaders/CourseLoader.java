package loaders;

import helpers.Course;
import helpers.Storage;

public class CourseLoader extends BaseLoader {
		
	public CourseLoader()
	{
		filename = "files/courses.txt";
		storage = Storage.getInstance();
	}
	
	protected void writeStorage()
	{
		iterator = json.iterator();
        Long semester, capacity, instructor, department;
        String name, code;
        
        
		 while (iterator.hasNext()) {
			 j = iterator.next();
			 
			 semester = (Long)j.get("semester");
			 capacity = (Long)j.get("capacity");
			 instructor = (Long)j.get("instructor");
			 department = (Long)j.get("department");
			 name = (String)j.get("course");
			 code = (String)j.get("code");
			 
            storage.addCourse(new Course (name,	code, semester.intValue(), capacity.intValue(), 
        			storage.getInstructor(instructor.intValue()), storage.getDepartment(department.intValue())));
        }
	}
}
