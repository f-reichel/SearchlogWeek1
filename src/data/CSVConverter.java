package data;

import java.io.*;
import java.sql.Timestamp;

public class CSVConverter {

	private BufferedReader bufferedReader;
	private FileReader fileReader;
	private File file;
	private static final String PATH = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/user-ct-test-collection-01.txt";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new CSVConverter();

	}
	
	private CSVConverter() throws IOException {
		file = new File(PATH);
		fileReader = new FileReader(file);
		bufferedReader = new BufferedReader(fileReader);
		
		readFile(bufferedReader);
	}
	
	private void readFile(BufferedReader reader) throws IOException {
		//while (reader.readLine() != null) {
		for (int i = 0; i < 100; i++) {
			String str = reader.readLine();
			//System.out.println(str);
			if (i > 0) {
				processLine(str);
			}
		}
		//}
		reader.close();
	}
	
	private void processLine(String line) {
		String[] data = line.split("\t");
		//System.out.println(data.length);
		String userID = data[0];
		String query = data[1];
		String queryTime = data[2];
		if (data.length == 5) {
			String itemRank = data[3];
			String clickURL = data[4];
		}
		convertTimeStamp(queryTime);
	}
	
	private void convertTimeStamp(String time) {
		System.out.println(time);
		int year = Integer.parseInt(time.substring(0, 4));
		int month = Integer.parseInt(time.substring(5, 7));
		int date = Integer.parseInt(time.substring(8, 10));
		int hour = Integer.parseInt(time.substring(11, 13));
		int minute = Integer.parseInt(time.substring(14, 16));
		int second = Integer.parseInt(time.substring(17, 19));
		int nano = 0;
		//System.out.println(year + ";" + month + ";" + date + ";" + hour + ";" + minute + ";" + second);
		
		Timestamp t = new Timestamp(year - 1900, month - 1, date, hour, minute, second, nano);
		System.out.println(t);
		long epoc = t.getTime();
		System.out.println(epoc);
	}
	

}
