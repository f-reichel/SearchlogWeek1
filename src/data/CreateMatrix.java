package data;

import java.io.*;
import java.util.*;

public class CreateMatrix {
	
//	private BufferedWriter bufferedWriter;
	private Scanner scanner;
//	private FileWriter fileWriter;
	private File inputFile, outputFile;
	private static final String INPUT_PATH = 
			"/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/" +
			"user-ct-test-collection-01.csv";
//	private static final String OUTPUT_PATH = 
//	"/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/#" +
//	"user-ct-filtered.csv";
	private ArrayList<String> matrix;
//	private int counter;
//	private int curUserID, prevUserID;
//	private long timeGap, curEpoc, mins;
//	private int sessionID;
	
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//new CreateMatrix();
	}
	
	private CreateMatrix () throws IOException {
		inputFile = new File(INPUT_PATH);
//		outputFile = new File(OUTPUT_PATH);
		scanner = new Scanner(inputFile);
//		fileWriter = new FileWriter(outputFile);
//		bufferedWriter = new BufferedWriter(fileWriter);
		matrix = new ArrayList<String>();
		readTable(scanner);
	}

	private void readTable(Scanner sc) throws IOException {
		boolean firstLine = true;
		while (sc.hasNextLine()) {
//			String[] input = sc.nextLine().split(",");
			String input = sc.nextLine();
			
			if (firstLine) {
				firstLine = false;
				matrix.add("sessionId," +
						"userId," +
						"query," +
						"rawdate," +
						"date," +
						"timegap," +
						"epoc");
				continue;
			}			
//			counter++;
			
//			String output = parseLine(input);
			matrix.add(input);
		}
		
		sc.close();
	}

//	private void writeLine(String output) throws IOException {
//		bufferedWriter.write(output);
//		bufferedWriter.newLine();
//	}
//
//	private String parseLine(String[] in) {
//		prevUserID = curUserID;
//		curUserID = Integer.parseInt(in[0]);
//		String query = in[1];
//		String queryTime = in[2];
//		if (in.length == 5) {
//			String itemRank = in[3];
//			String clickURL = in[4];
//		}
//		convertTimeStamp(queryTime);		
//		if (curUserID != prevUserID) {
//			timeGap = 0;
//			mins = 0;
//			sessionID = 0;
//		} else {
//			boolean longGap = compareTimeStamps();
//			mins = timeGap/1000/60;
//			if (longGap){
//				timeGap = 0;
//				mins = 0;
//				sessionID++;			
//			}
//			
//		}
//		String processedLine = sessionID + ","
//				+ curUserID + "," 
//				+ query + ","
//				+ queryTime + ","
//				+ curTimestamp + ","
//				+ timeGap + ","
//				+ curEpoc;
//		return processedLine;
//	}
//
//	private boolean compareTimeStamps() {
//		if (prevTimestamp == null) {
//			timeGap = 0;
//			return false;
//		}
//		long prevEpoc = prevTimestamp.getTime();
//		timeGap = curEpoc - prevEpoc;
//		long mins = timeGap/1000/60;
//		if (mins >= 30) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	private void convertTimeStamp(String time) {
//		prevTimestamp = curTimestamp;		
//		curTimestamp = Timestamp.valueOf(time);
//		curEpoc = curTimestamp.getTime();
//	}

}
