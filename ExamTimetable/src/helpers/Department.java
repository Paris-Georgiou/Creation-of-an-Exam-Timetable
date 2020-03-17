package helpers;

public class Department {
	String name;
	int code;
	
	public Department (String name,	int code)
	{
		this.name = name;
		this.code = code;
	}
	
	public int getCode() {return code;}
	public String getName() {return name;}
}
