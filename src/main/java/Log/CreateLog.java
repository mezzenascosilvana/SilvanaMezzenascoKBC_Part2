package Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class CreateLog {

	// our log file
	FileWriter logfile; 

	public void createLog(String value) throws IOException {


		// Ask if the file exists, otherwise create one with the name log.txt
		if (new File("log.txt").exists() == false) {
			logfile = new FileWriter(new File("log.txt"), false);
		}
		logfile = new FileWriter(new File("log.txt"), true);
		Calendar currentDay = Calendar.getInstance(); 
		// Star to write in file
		logfile.write((String.valueOf(currentDay.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(currentDay.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(currentDay.get(Calendar.YEAR)) + ";"
				+ String.valueOf(currentDay.get(Calendar.HOUR_OF_DAY)) + ":"
				+ String.valueOf(currentDay.get(Calendar.MINUTE)) + ":"
				+ String.valueOf(currentDay.get(Calendar.SECOND))) + ";" + value + "\r\n");
		// Close file
		logfile.close();
	}

}
