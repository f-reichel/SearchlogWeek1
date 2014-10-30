package data;

import java.io.*;
import java.sql.Timestamp;
import java.util.Scanner;

public class CSVConverter {

	private BufferedWriter bufferedWriter;
	private Scanner scanner;
	private FileWriter fileWriter;
	private File inputFile, outputFile;
	private Timestamp prevTimestamp, curTimestamp;
	private int prevUserID, curUserID, sessionID, counter;
	private long timeGap, curEpoc, mins;	
	private static final String INPUT_PATH = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/user-ct-test-collection-01.txt";
	private static final String OUTPUT_PATH = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/user-ct-test-collection-01.csv";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new CSVConverter();
	}
	
	private CSVConverter() throws IOException {
		inputFile = new File(INPUT_PATH);
		outputFile = new File(OUTPUT_PATH);
		scanner = new Scanner(inputFile);
		fileWriter = new FileWriter(outputFile);
		bufferedWriter = new BufferedWriter(fileWriter);
		
		scanFile(scanner);
		bufferedWriter.close();
	}
	
	private void scanFile(Scanner sc) throws IOException {
		boolean firstLine = true;
		while (sc.hasNextLine()) {
			String[] input = sc.nextLine().split("\t");
			
			if (firstLine) {
				firstLine = false;
				writeLine("sessionId," +
						"userId," +
						"query," +
						"rawdate," +
						"date," +
						"timegap," +
						"epoc");
				continue;
			}			
			counter++;
			
			String output = parseLine(input);
			writeLine(output);
		}
		
		sc.close();
	}
	
	private void writeLine(String output) throws IOException {
		bufferedWriter.write(output);
		bufferedWriter.newLine();
	}
	
	private String parseLine(String[] in) {
		prevUserID = curUserID;
		curUserID = Integer.parseInt(in[0]);
		String query = in[1];
		String queryTime = in[2];
		if (in.length == 5) {
			String itemRank = in[3];
			String clickURL = in[4];
		}
		convertTimeStamp(queryTime);		
		if (curUserID != prevUserID) {
			timeGap = 0;
			mins = 0;
			sessionID = 0;
		} else {
			boolean longGap = compareTimeStamps();
			mins = timeGap/1000/60;
			if (longGap){
				timeGap = 0;
				mins = 0;
				sessionID++;			
			}
			
		}
		String processedLine = sessionID + ","
				+ curUserID + "," 
				+ query + ","
				+ queryTime + ","
				+ curTimestamp + ","
				+ timeGap + ","
				+ curEpoc;
		return processedLine;
	}

	private boolean compareTimeStamps() {
		if (prevTimestamp == null) {
			timeGap = 0;
			return false;
		}
		long prevEpoc = prevTimestamp.getTime();
		timeGap = curEpoc - prevEpoc;
		long mins = timeGap/1000/60;
		if (mins >= 30) {
			return true;
		} else {
			return false;
		}
	}

	private void convertTimeStamp(String time) {
		prevTimestamp = curTimestamp;		
		curTimestamp = Timestamp.valueOf(time);
		curEpoc = curTimestamp.getTime();
	}
}
