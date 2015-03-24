package bot_detection;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import util.QueryObject;
import util.UserObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
import javax.sql.DataSource;

import jdbc.DataSourceFactory;


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
	
	private DataSource dataSource;
	private Connection dbConnection;
    private Statement sqlStatement;
    private ResultSet resultSet;

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {
		new BotDetection();
	}

	public BotDetection() throws IOException, SQLException {
		
		dataSource = DataSourceFactory.getMySQLDataSource();
		dbConnection = dataSource.getConnection();
		sqlStatement = dbConnection.createStatement();
		
		//inputFile = new File(INPUT_PATH);
		outputFileResults = new File(OUTPUT_PATH);
		outputFileBots = new File(OUTPUT_BOTS);
		//scanner = new Scanner(inputFile);
		fileWriterResults = new FileWriter(outputFileResults);
		fileWriterBots = new FileWriter(outputFileBots);
		bufferedWriterResults = new BufferedWriter(fileWriterResults);
		bufferedWriterBots = new BufferedWriter(fileWriterBots);

		//readCSV(scanner);
		queryDB(null);

		bufferedWriterResults.close();
		bufferedWriterBots.close();
	}
	
	private void queryDB(String sqlQuery) throws SQLException, IOException {
//		boolean firstLine = true;
		ArrayList<QueryObject> list = new ArrayList<QueryObject>();
		boolean firstID = true;
		int prevID = 0;
		int currID = 0;
		counter = 0;
//		resultSet = sqlStatement.executeQuery("select * from user_ct_test_collection_01 Limit 100;");
		resultSet = sqlStatement.executeQuery("select * from user_ct_test_collection_01;");
//		resultSet.first();
		appendBotsList(HEADER);
		while (resultSet.next()) {
			counter++;
			int userId = resultSet.getInt(1);
			int sessionId = resultSet.getInt(2);
			Timestamp date = resultSet.getTimestamp(3);
			int timeGap = resultSet.getInt(4);
			long epoc = resultSet.getLong(5);
			String query = resultSet.getString(6);
			int itemRank = resultSet.getInt(7);
			String clickUrl = resultSet.getString(8);
			QueryObject queryObject = new QueryObject(userId, sessionId, date, timeGap, epoc, query, itemRank, clickUrl);
			appendBotsList(queryObject.printContent());
			
			
			prevID = currID;
			currID = userId;
			
			if (prevID == currID) {
				
				list.add(queryObject);
				//System.out.println("(79)" + list.size());
			} else {
				if (firstID) {
					firstID = false;
					list.add(queryObject);
					continue;
				}
				appendBotsList("current ID: " + list.size());
				System.out.println(("current ID: " + list.size()));
				scanForBots(list);
//				checkForBots(list);
				list.clear();
				list.add(queryObject);
			}

			// String output = parseLine(input);
			// writeLine(output);
		}

		resultSet.close();
	}
	
	/*
	private void readCSV(Scanner sc) throws IOException {
		boolean firstLine = true;
		boolean firstID = true;
		int prevID = 0;
		int currID = 0;
		ArrayList<String[]> list = new ArrayList<String[]>();
		// while (sc.hasNextLine()) {
		while (counter < 27) {
			System.out.println(counter + " 1# " + sc.nextLine());
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
				//System.out.println("(79)" + list.size());
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
	*/

	private void scanForBots(ArrayList<QueryObject> list) {
		userObject = new UserObject(list);
		if(userObject.checkForFrequentQueries()) {
			userObject.printUserObject();
		}
	}

//	private void checkForBots(ArrayList<String[]> list) {
//		userObject = new UserObject(list);
//		if(userObject.checkForFrequentQueries()) {
//			userObject.printUserObject();
//		}
//	}

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
