package helpers;

public class Course /*extends Helper */{
	String name;
	String code;
	Instructor instructor;
	Department department;
	int semester;
	int capacity;
	
	public Course (String name,	String code, int semester, int capacity, 
			Instructor instructor, Department department)
	{
		this.name = name;
		this.code = code;
		this.instructor = instructor;
		this.department = department;
		this.semester = semester;
		this.capacity = capacity;
	}
	
	public String getCode() {return code;}
	public String getName() {return name;}
	public Instructor getInstructor() {return instructor;}
	public Department getDepartment() {return department;}
	public int getSemester() {return semester;}
	public int getCapacity() {return capacity;}
	
	public String toString()
	{
		return "["+department.getName()+"-"+semester+"] ~ "+ name+" ("+code+")";
	}
	
}
