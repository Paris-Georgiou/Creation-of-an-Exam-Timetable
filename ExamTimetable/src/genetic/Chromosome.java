package genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import helpers.*;

import java.util.Random;

public class Chromosome implements Comparable<Chromosome>
{
	Storage storage;
	private int fitness;
/*
 * sets lessons as: lesson[month-day-time-room]
 */
	HashMap<String, String> genes;
/*
 *  sets program as: month[day[time[capacity[room[lesson]]]]]
 */
	HashMap<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>>> schedule;
/*
 *  sets lessons as: month[day[time[department[semester[lesson]]]]]
 */
	HashMap<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>>> groups;
	
	public Chromosome()
	{
		storage = Storage.getInstance();

		initSchedule();
		initGroups();
		
		genes = new HashMap<String, String>();
		ArrayList<String> c = new ArrayList<String>();
		//deep Copy
		for (String value : storage.getCourseList()) {
			c.add(value);
		}	
		
		Random r = new Random();
		while(!c.isEmpty()) {
			
			String l = c.get(r.nextInt(c.size()));
			
			String s = getFreeSlotForCourse(l);
			
			//System.out.println("Assigning course:"+value+" -> "+s);
			
			if (null == s) {
				System.err.println("The timetable is unfeasible to implement");
				System.exit(0);
			}
			
			setSlot(s,l);
			genes.put(l, s);
			
			c.remove(l);
		}
		
		calculateFitness();
				
	}
	
	private String getFreeSlotForCourse(String c)
	{
		Course course = storage.getCourse(c);
		
		int cap = course.getCapacity();
		int department = course.getDepartment().getCode();
		int semester = course.getSemester();
		
		String ret = null;
		
		// System.out.println("searching for capacity:"+cap+" department:"+department+ " semester:"+semester+" course:"+c );
		
			// check month
		  for (Map.Entry<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>>> mEntry : schedule.entrySet()) {
		      String m = mEntry.getKey();
		    	
		    		//  System.out.println("\t searching month:"+m);
		    		  
		      HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> month = mEntry.getValue();
		  
		      // check day
		      for (Map.Entry<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> dEntry: month.entrySet()) {
		  
		    	  String d = dEntry.getKey();

	    		 // System.out.println("\t\t searching day:"+d);
	    	
		    	  HashMap<String, HashMap<Integer, HashMap<Integer, String>>> day = dEntry.getValue();
		   
		    	  	// check time
		    	  	for (Map.Entry<String, HashMap<Integer, HashMap<Integer, String>>> hEntry: day.entrySet()) {
		    	  		String h = hEntry.getKey();
		    	  		HashMap<Integer, HashMap<Integer, String>> hour = hEntry.getValue();
		    	  		
		    	  		// System.out.println("\t\t\t searching hour:"+h);
			  		
	    	  			HashMap<Integer, String> rooms4capacity = hour.get(cap);
		    	  		for (Map.Entry<Integer,String> rcEntry: rooms4capacity.entrySet()) {
							  Integer rc = rcEntry.getKey();
							  if (null == rcEntry.getValue()) {
								  
								  ret = m+"-"+d+"-"+h+"-"+cap+"-"+rc;
								//  System.out.println("\t\t\t\t found candidate:"+ret);
								  break;
							  }
						}
		    	  		
		    	  		
		    	  		if (null != ret) {
	    	  			// check if at the same time there is another lesson for the same department for the same semester that is being examed	
		    	  			// check capacity
		    	  			for (Map.Entry<Integer, HashMap<Integer, String>> cEntry: hour.entrySet()) {
		    	  					HashMap<Integer, String> capacity = cEntry.getValue();
		    	  					// check room
		    	  					for (Map.Entry<Integer,String> clEntry: capacity.entrySet()) {
									  String lesson = clEntry.getValue();
									  if (null != lesson && !lesson.equals(c)){
										  Course l = storage.getCourse(lesson);
										  if (l.getSemester() == semester && l.getDepartment().getCode() == department) {
											  ret = null;
											  break;
										  }				  
										  
									  }
		    	  					}
			    	  			}
		    	  		}
		    	  		
		    	  		// we are done
		    	  		if (null != ret) return ret;
		    	  	}
		      }
	      }
 
		return ret;
	}
	
	public Chromosome(HashMap<String, String> g)
	{
		storage = Storage.getInstance();
		initSchedule();
		initGroups();
		genes = g;
		for (Map.Entry<String,String> gEntry: genes.entrySet()) {
	    	String lesson = gEntry.getKey();
	    	String slot = gEntry.getValue();
	    	setSlot(slot,lesson);
	    }
		calculateFitness();		
	}
		
	private void calculateFitness()
	{
		fitness = 10000;
		int ds_sem,  ds_dept, prv_sem, prv_dept, size;  
		String date;
		// check month
		for (Map.Entry<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>>> mEntry : groups.entrySet()) {
			HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>> month = mEntry.getValue();
			ds_sem = ds_dept = 0;
			// check day
			for (Map.Entry<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>> dEntry: month.entrySet()) {
				
				HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> day = dEntry.getValue();
				
				date = mEntry.getKey()+"-"+dEntry.getKey();
				
				prv_dept = ds_dept;
				ds_sem = ds_dept = 0;
				// check time
				for (Map.Entry<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> hEntry: day.entrySet()) {
					HashMap<Integer, HashMap<Integer, ArrayList<String>>> hour = hEntry.getValue();
					
					// check department
					for (Map.Entry<Integer, HashMap<Integer, ArrayList<String>>> dpEntry: hour.entrySet()) {
						HashMap<Integer, ArrayList<String>> dept = dpEntry.getValue();
						 prv_sem = ds_sem;
						 ds_sem = 0;
						// check semester
						for (Map.Entry<Integer,ArrayList<String>> sEntry: dept.entrySet()) {
							ArrayList<String> lessons = sEntry.getValue();
							size = lessons.size();
							if (size>0) {
								fitness -= (size/2*1000); // a
								ds_sem+=size;
								ds_dept+=size;
							}
							
							for(String lesson:lessons){
								String[] absence = storage.getCourse(lesson).getInstructor().getAbsence();
								if (null!=absence) {
									for(String ab:absence) {
										if (ab.equals(date))
											fitness-=20; // b
									}
								}
							}
							
						}
	
						fitness -= ds_sem/2*15; // c
						if (prv_sem>0 && ds_sem>0)
							fitness -= (prv_sem+ds_sem)/2*10; // d
				
					}
				}
				
				fitness -= ds_dept/2*5; // e
				if (prv_dept>0 && ds_dept>0)
					fitness -= (prv_dept+ds_dept)/2*2; // f
		
			}
		}
	}
	
	public int getFitness() { return fitness; }
	
	
	/*
	 * Chromosome functions
	 */
	public void mutate()
	{
		Random r = new Random();
		String n = null;
		String c = null;
		String s = null;
		int cnt=0;
		while (n == null && cnt++<100) {
			int i = r.nextInt(storage.getCourseNumber());
			
			c = storage.getCourseList().get(i);
			s = genes.get(c);
			n = getFreeSlotForCourse(c);
		}
		
		if (null == n) {
			System.err.println("Unable to mutate");
			System.exit(0);
		} 
		setSlot(n,c);
		unsetSlot(s);
		
		calculateFitness();
	}
	
	//"Reproduces" two chromosomes and generated their "child"
	public Chromosome reproduce(Chromosome x)
	{
		Random r = new Random();
        //Randomly choose the intersection point
		int intersectionPoint = r.nextInt(storage.getCourseNumber()-1) + 1;
		HashMap<String, String> childGenes = new HashMap<String, String>();
		ArrayList<String> courseList = storage.getCourseList();
		String course;
        //The child has the left side of this chromosome up to the intersection point...
		for(int i=0; i<intersectionPoint; i++) {
			course = courseList.get(i);
			childGenes.put(course, genes.get(course));
		}
        //...and the right side of the y chromosome after the intersection point
		for(int i=intersectionPoint; i<storage.getCourseNumber(); i++) {
			course = courseList.get(i);
			childGenes.put(course, x.genes.get(course));
		}
		return new Chromosome(childGenes);
	}
	
	
	@Override
	public int compareTo(Chromosome x) { return this.fitness - x.fitness; }
	
	/*
	 * Helper functions
	 */
	private HashMap<Integer, String> getSlot(String s)
	{
		String[] path = s.split("-");
		HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> month = schedule.get(path[0]);
		HashMap<String, HashMap<Integer, HashMap<Integer, String>>> day = month.get(path[1]);
		HashMap<Integer, HashMap<Integer, String>> hour = day.get(path[2]);
		return hour.get(Integer.parseInt(path[3]));
	}
	
	private ArrayList<String> getGroup(String s, String c)
	{
		String[] path = s.split("-");
		
		Course course = storage.getCourse(c);
		int department = course.getDepartment().getCode();
		int semester = course.getSemester();
		
		
		HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>> month = groups.get(path[0]);
		HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> day = month.get(path[1]);
		HashMap<Integer, HashMap<Integer, ArrayList<String>>> hour = day.get(path[2]);
		HashMap<Integer, ArrayList<String>> dept = hour.get(department);
		return dept.get(semester);
		
	}
	
	private void setSlot(String s, String course)
	{
		String[] path = s.split("-");
		HashMap<Integer, String> slot = getSlot(s);
		slot.put(Integer.parseInt(path[4]), course);
		
		ArrayList<String> sem = getGroup(s,course);
		sem.add(course);
	}
	
	private void unsetSlot(String s) { 
		String[] path = s.split("-");
		HashMap<Integer, String> slot = getSlot(s);
		
		String course = slot.get(Integer.parseInt(path[4]));
		
		slot.put(Integer.parseInt(path[4]), null);
		
		ArrayList<String> sem = getGroup(s,course);
		sem.remove(course);
	}
	
	private void initSchedule() 
	{
		schedule = new HashMap<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>>>();
		
		HashMap<Integer, Room> rooms = storage.getRooms();
		ArrayList<Slot> slots = storage.getSlots();

		for (Slot slot:slots) {
			String m=((Integer)slot.getMonth()).toString();
			if (!schedule.containsKey(m)) {
				schedule.put(m, new HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>>());
			}
			HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> month = schedule.get(m);
						
			String d=((Integer)slot.getDay()).toString();
			if (!month.containsKey(d)) {
				month.put(d, new HashMap<String, HashMap<Integer, HashMap<Integer, String>>>());
			}
			HashMap<String, HashMap<Integer, HashMap<Integer, String>>> day = month.get(d);
			
			String h=((Integer)slot.getHour()).toString();
			if (!day.containsKey(h)) {
				day.put(h, new HashMap<Integer, HashMap<Integer, String>>());
			}
			HashMap<Integer, HashMap<Integer, String>> hour = day.get(h);
			
			for (Room room : rooms.values()) {
			  
			    if (!hour.containsKey(room.getCapacity()))
			    	hour.put(room.getCapacity(), new HashMap<Integer, String>());
			    
			    HashMap<Integer, String> capacity = hour.get(room.getCapacity());
			    capacity.put(room.getCode(), null);
			    
			}
		}
	}
	
	private void initGroups()
	{
		groups = new HashMap<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>>>();
		
		ArrayList<Slot> slots = storage.getSlots();
		HashMap<Integer, Department> departments = storage.getDepartments();
		
		for (Slot slot:slots) {
			String m=((Integer)slot.getMonth()).toString();
			if (!groups.containsKey(m)) {
				groups.put(m, new HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>>());
			}
			HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>> month = groups.get(m);
						
			String d=((Integer)slot.getDay()).toString();
			if (!month.containsKey(d)) {
				month.put(d, new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>());
			}
			HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> day = month.get(d);
			
			String h=((Integer)slot.getHour()).toString();
			if (!day.containsKey(h)) {
				day.put(h, new HashMap<Integer, HashMap<Integer, ArrayList<String>>>());
				HashMap<Integer, HashMap<Integer, ArrayList<String>>> hour = day.get(h);
				
				for(Integer dept: departments.keySet()) {
					hour.put(dept, new HashMap<Integer,  ArrayList<String>>());
					HashMap<Integer,  ArrayList<String>> sem = hour.get(dept);
					for (int i=1;i<9;i++) {
						sem.put(i, new ArrayList<String>());
					}				
				}
			}
				
		}
	}
	
	public void print()
	 {
	  ArrayList<Integer> months = new ArrayList<Integer>();
	  ArrayList<Integer> days = new ArrayList<Integer>();
	  ArrayList<Integer> hours = new ArrayList<Integer>();
	  
	  System.out.println("Score: "+fitness);
	  for (Map.Entry<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>>> mEntry : schedule.entrySet()) {
	   int m = Integer.parseInt(mEntry.getKey()); 
	   if(!months.contains(m)){
	    months.add(m);
	   }
	   
	   HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> month = mEntry.getValue();
	   
	        // check days
	      for (Map.Entry<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> dEntry: month.entrySet()) {
	       int d = Integer.parseInt(dEntry.getKey());
	       if(!days.contains(d)){
	        days.add(d);
	       }
	        
	       HashMap<String, HashMap<Integer, HashMap<Integer, String>>> day = dEntry.getValue();
	         
	         // check time
	       for (Map.Entry<String, HashMap<Integer, HashMap<Integer, String>>> hEntry: day.entrySet()) {
	        int h = Integer.parseInt(hEntry.getKey());
	        if(!hours.contains(h)){
	         hours.add(h);
	        }
	       }
	      }
	  }
	  
	  Collections.sort(months);
	  Collections.sort(days);
	  Collections.sort(hours);
	  
	  for (Integer m:months) {
		  System.out.println("Month: " + m +"\n");
		  for (Integer d:days) {
			  System.out.println("\t Day: " + d);
			  
			  for (Integer h:hours) {
				  System.out.println("\t\t Hour: " + h);
				  
				  for (int i=1; i<4; i++) {
					  HashMap<Integer, String> capacity = schedule.get(m.toString()).get(d.toString()).get(h.toString()).get(i);
					  
					  for (Map.Entry<Integer,String> clEntry: capacity.entrySet()) {
					         
				          Integer cl = clEntry.getKey();
				          String lesson = clEntry.getValue();
				          if (null != lesson) 
				           System.out.println("\t\t\t "+storage.getCourse(lesson)+" -> "+storage.getRoom(cl));
					  }
				  }
			  }
		  }
	  }
	  
	   /*
	  int a=0;
	  
	 
	  
	  for (Map.Entry<String, HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>>> mEntry : schedule.entrySet()) {
	   HashMap<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> month = mEntry.getValue();
	    if(months.isEmpty()){
	     break;
	    }
	    else{
	     System.out.println("Month: " + months.get(0) +"\n");
	     months.remove(0);
	    }
	      for (Map.Entry<String, HashMap<String, HashMap<Integer, HashMap<Integer, String>>>> dEntry: month.entrySet()) {
	       HashMap<String, HashMap<Integer, HashMap<Integer, String>>> day = dEntry.getValue();
	        if(days.isEmpty()){
	         break;
	        }
	        else{
	         System.out.println("Day: " + days.get(0));
	      days.remove(0);
	        }
	        a=0;
	       for (Map.Entry<String, HashMap<Integer, HashMap<Integer, String>>> hEntry: day.entrySet()) {
	        HashMap<Integer, HashMap<Integer, String>> hour = hEntry.getValue();
	        if(a==1){
	         break;
	        }
	        else{
	         a=1;
	         for(int i=0; i<hours.size(); i++){
	          System.out.println("Hour: " + hours.get(i));
	          for (Map.Entry<Integer, HashMap<Integer, String>> cEntry: hour.entrySet()) {
	           HashMap<Integer, String> capacity = cEntry.getValue();
	           
	           for (Map.Entry<Integer,String> lEntry: capacity.entrySet()) {
	            int course = lEntry.getKey();
	            String lesson = lEntry.getValue();
	            if(storage.getCourse(lesson) != null){
	             System.out.print("Lesson: " + storage.getCourse(lesson) + "\t");
	             System.out.println("Room: " + storage.getRoom(course));
	            } 
	           }  
	          }
	         }
	        }
	       }
	      System.out.println();
	      }
	      
	     
	  } */
	  
	 }
}
