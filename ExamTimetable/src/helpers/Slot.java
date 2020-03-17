package helpers;

public class Slot /*extends Helper*/{

	int hour;
	int day;
	int month;
	//Course course;
	
	public Slot (int hour, int day, int month)
	{
		this.hour = hour;
		this.day = day;
		this.month = month;
	}
	
	public int getHour() {return hour;}
	public int getDay() {return day;}
	public int getMonth() {return month;}
	
	/*
	public boolean isReserved()
	{
		return (null == course) ? false : true;
	}
	
	public void reserve(Course c) 
	{
		course = c;
	}
	
	public void free()
	{
		course = null;
	}
	
	public String getGeneHash()
	{
		return padLeft(month,2)+padLeft(day,2)+padLeft(hour,2);
	}
	*/
}
