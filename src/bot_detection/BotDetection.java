package bot_detection;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import util.UserObject;


public class BotDetection {

	private BufferedWriter bufferedWriterResults, bufferedWriterBots;
	private Scanner scanner;
	private FileWriter fileWriterResults, fileWriterBots;
	private File inputFile, outputFileResults, outputFileBots;
	private Timestamp prevTimestamp, curTimestamp;
	private int prevUserID, curUserID, sessionID, counter;
	private long timeGap, curEpoc, mins;
	private double score;
	private static final String INPUT_PATH = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/"
			+ "user-ct-test-collection-01.csv";
	private static final String OUTPUT_PATH = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/"
			+ "collection-01-Results.csv";
	private static final String OUTPUT_BOTS = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/"
			+ "collection-01-BOTS.csv";
	private static final String HEADER = "userId" + "\t" + "sessionId" + "\t"
			+ "score" + "\t" + "date" + "\t" + "timegap" + "\t" + "query";
	private UserObject userObject;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new BotDetection();
	}

	public BotDetection() throws IOException {
		inputFile = new File(INPUT_PATH);
		outputFileResults = new File(OUTPUT_PATH);
		outputFileBots = new File(OUTPUT_BOTS);
		scanner = new Scanner(inputFile);
		fileWriterResults = new FileWriter(outputFileResults);
		fileWriterBots = new FileWriter(outputFileBots);
		bufferedWriterResults = new BufferedWriter(fileWriterResults);
		bufferedWriterBots = new BufferedWriter(fileWriterBots);

		readCSV(scanner);

		bufferedWriterResults.close();
		bufferedWriterBots.close();
	}

	private void readCSV(Scanner sc) throws IOException {
		boolean firstLine = true;
		boolean firstID = true;
		int prevID = 0;
		int currID = 0;
		ArrayList<String[]> list = new ArrayList<String[]>();
		// while (sc.hasNextLine()) {
		while (counter < 100) {
			//System.out.println(sc.nextLine());
			String[] input = sc.nextLine().split("\t");

			if (firstLine) {
				firstLine = false;
				appendBotsList(HEADER);
				appendResults(HEADER);
				continue;
			}
			//System.out.println("prevID " + prevID + " currID " + currID);
			counter++;

			prevID = currID;
			currID = Integer.parseInt(input[0]);
			if (prevID == currID) {
				
				list.add(input);
				//System.out.println(list.size());
			} else {
				if (firstID) {
					firstID = false;
					list.add(input);
					continue;
				}
				//System.out.println("current ID: " + list.size());
				checkForBots(list);
				list.clear();
				list.add(input);
			}

			// String output = parseLine(input);
			// writeLine(output);
		}

		sc.close();
	}

	private void checkForBots(ArrayList<String[]> list) {
		userObject = new UserObject(list);
		
//		userObject.splitSessions();
//		userObject.printList();
	}

	private String parseLine(String[] in) throws IOException {
		prevUserID = curUserID;
		curUserID = Integer.parseInt(in[0]);
		String query = in[1];
		String queryTime = in[2];
		convertTimeStamp(queryTime);
		// if (curUserID != prevUserID) {
		// timeGap = 0;
		// mins = 0;
		// sessionID = 0;
		// //writeLine("");
		// } else {
		// boolean longGap = compareTimeStamps();
		// mins = timeGap/1000/60;
		// if (longGap){
		// timeGap = 0;
		// mins = 0;
		// sessionID++;
		// }
		//
		// }
		// String processedLine = curUserID + "\t"
		// + sessionID + "\t"
		// + score + "\t"
		// + queryTime + "\t"
		// // + curTimestamp + "\t"
		// + timeGap + "\t"
		// + query;// + "\t"
		// // + curEpoc;
		// if (in.length == 5) {
		// String itemRank = in[3];
		// String clickURL = in[4];
		// processedLine += "\t" + itemRank + "\t" + clickURL;
		// }
		// return processedLine;
		return null;
	}

	private void convertTimeStamp(String time) {
		prevTimestamp = curTimestamp;
		curTimestamp = Timestamp.valueOf(time);
		curEpoc = curTimestamp.getTime();
	}

	private void appendResults(String output) throws IOException {
		bufferedWriterResults.write(output);
		bufferedWriterResults.newLine();
	}

	private void appendBotsList(String output) throws IOException {
		bufferedWriterBots.write(output);
		bufferedWriterBots.newLine();
	}

}
