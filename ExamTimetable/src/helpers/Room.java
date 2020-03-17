package helpers;

public class Room {
	String name;
	int code;
	int capacity;
	
	public Room (String name, int code, int capacity)
	{
		this.name = name;
		this.code = code;
		this.capacity = capacity;
	}
	
	public int getCode() {return code;}
	public int getCapacity() {return capacity;}
	public String getName() {return name;}
	
	public String toString() { return name; }
}
