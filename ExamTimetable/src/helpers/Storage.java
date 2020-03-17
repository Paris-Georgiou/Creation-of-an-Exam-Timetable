package helpers;
import java.util.ArrayList;
import java.util.HashMap;

import loaders.*;

public class Storage {
	protected static Storage instance;
	
	ArrayList<BaseLoader> loaders;
	
	HashMap<Integer, Department> departments;
	HashMap<Integer, Instructor> instructors;
	HashMap<Integer, Room> rooms;
	HashMap<String, Course> courses; 
	ArrayList<Slot> slots;

	/* helper structures */
	// keeps sequence of lessons for reproduction
	ArrayList<String> courseList;
	//  sets lessons as: capacity[lessons]
	HashMap<Integer, ArrayList<String>> courseCapacities;
	
	public void addSlot(Slot s)	{ slots.add(s);	}
	public ArrayList<Slot> getSlots() { return slots; }
	
	public void addRoom(Room r)	{ rooms.put(r.getCode(), r); }
	public HashMap<Integer, Room> getRooms() { return rooms; }
	public Room getRoom(int r) { return rooms.get(r); }
	
	public void addDepartment(Department d) { departments.put(d.getCode(), d); }
	public Department getDepartment(int d) { return departments.get(d);	}
	public HashMap<Integer, Department> getDepartments() { return departments; }
	
	public void addInstructor(Instructor i)	{ instructors.put(i.getCode(), i); }
	public Instructor getInstructor(int i) { return instructors.get(i);	}
	
	public void addCourse(Course c)	
	{ 
		courses.put(c.getCode(), c);
		courseList.add(c.getCode());
		
		if (!courseCapacities.containsKey(c.getCapacity()))
			courseCapacities.put(c.getCapacity(), new ArrayList<String>());
		courseCapacities.get(c.getCapacity()).add(c.getCode());
	}
	public Course getCourse(String c) { return courses.get(c);	}
	public HashMap<String, Course> getCourses() { return courses; }
	
	public ArrayList<String> getCourseList() { return courseList; }
	public int getCourseNumber() { return courseList.size(); }
	public HashMap<Integer, ArrayList<String>> getCourseCapacities() { return courseCapacities; }
	
	public void registerLoader(BaseLoader loader) {	loaders.add(loader); }
	public void runLoaders()
	{
		for(BaseLoader l: loaders){
			try {
				l.loadFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	protected Storage()
	{
		loaders = new ArrayList<BaseLoader>();
		
		instructors = new HashMap<Integer, Instructor>();
		departments = new HashMap<Integer, Department>();
		slots = new ArrayList<Slot>();
		rooms = new HashMap<Integer, Room>();
		courses = new HashMap<String, Course>();
		courseCapacities = new HashMap<Integer, ArrayList<String>>();
		courseList = new ArrayList<String>();
	}
	
	public static Storage getInstance()
	{
		if (null == instance)
			instance = new Storage();
		
		return instance;
	}
	
	public String toString()
	{
		return "Departments: " + departments.toString() + "\n" +
			   "Instructors: " + instructors.toString() + "\n" +
			   "Slots: " + slots.toString() + "\n";
	}
}
