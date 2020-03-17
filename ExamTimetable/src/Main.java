//import java.util.Scanner;

import genetic.*;
import helpers.Storage;
import loaders.*;

public class Main
{
//	private static Scanner sc;

	public static void main(String[] args)
	{
//		sc = new Scanner(System.in);
//		String input;
		
		Storage storage = Storage.getInstance();
		storage.registerLoader(new DepartmentLoader());
		storage.registerLoader(new SlotLoader());
		storage.registerLoader(new RoomLoader());
		storage.registerLoader(new InstructorLoader());
		storage.registerLoader(new CourseLoader());
		
	//	do {
				
			storage.runLoaders();
			
			Genetic g = new Genetic();
			Chromosome x = g.geneticAlgorithm(30, 0.75, 9000, 1000);
			x.print();
			
		//	System.out.println("Press 'q' to quit, anything else to reload files and restart");
	//		input = sc.nextLine();
	//	} while (!input.equals("q"));
		
	}

}
