package helpers;

public class Instructor {
	String name;
	int code;
	String[] absence;
	
	public Instructor (String name,	int code, String[] absence)
	{
		this.name = name;
		this.code = code;
		this.absence = absence;
	}
	
	public int getCode() {return code;}
	public String[] getAbsence() {return absence;}
	public String getName() {return name;}
}
