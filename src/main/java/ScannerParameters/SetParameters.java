package ScannerParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Log.CreateLog;

public class SetParameters {

	public static CreateLog log = new CreateLog();

	public ArrayList<String> setUpParameters() throws IOException {

		ArrayList<String> parameterArray = new ArrayList<String>();
		// create a scanner so we can read the command-line input
		try {
			Scanner sc = new Scanner(System.in);
			// prompt for the first parameter
			System.out.println("Enter the first parameter:");
			// get their input as a String
			parameterArray.add(sc.next());
			System.out.println("The first parameter was:" + parameterArray.get(0));
			log.createLog("first parameter:" + parameterArray.get(0));
			// prompt for the second parameter
			System.out.println("Enter the second parameter:");
			parameterArray.add(sc.next());
			System.out.println("The second parameter:" + parameterArray.get(1));
			log.createLog("second parameter:" + parameterArray.get(1));
			sc.close();
		} catch (NumberFormatException e) {
			System.out.println("Error exception");
		}
		return parameterArray;

	}

}
