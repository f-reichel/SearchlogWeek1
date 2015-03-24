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
	private boolean processFile = true;
	private ArrayList<Integer> userIDs;
	private static final int LIMIT = 50;

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {
		new BotDetection();
	}

	public BotDetection() throws IOException, SQLException {

		userIDs = new ArrayList<Integer>();
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
		resultSet = sqlStatement.executeQuery("select distinct userId from user_ct_test_collection_01;");
		while(resultSet.next()) {
			userIDs.add(resultSet.getInt(1));			
		}
		System.out.println("Total number of Users: " + userIDs.size());
 		int index = 0;
		for(int i : userIDs) {

			resultSet = sqlStatement.executeQuery("select * from user_ct_test_collection_01 where userId = " + i + ";");
			if (index >= LIMIT) {
				break;
			}
			index++;


			//		resultSet = sqlStatement.executeQuery("select * from user_ct_test_collection_01 Limit 100000;");
			//		resultSet = sqlStatement.executeQuery("select * from user_ct_test_collection_01;");
			//		appendBotsList(HEADER);
			if (processFile) {
//				int index = 0;
				//resultSet.first();
				int row = 0;
				list.clear();
				while (resultSet.next()) {
//					row = resultSet.getRow();
//					System.out.println(row);
					//counter++;
					int userId = resultSet.getInt(1);
					int sessionId = resultSet.getInt(2);
					Timestamp date = resultSet.getTimestamp(3);
					int timeGap = resultSet.getInt(4);
					long epoc = resultSet.getLong(5);
					String query = resultSet.getString(6);
					int itemRank = resultSet.getInt(7);
					String clickUrl = resultSet.getString(8);
					QueryObject queryObject = new QueryObject(userId, sessionId, date, timeGap, epoc, query, itemRank, clickUrl);
					
					list.add(queryObject);
				}
				appendBotsList(i + ": "+ list.size());
				System.out.println(i + ": "+ list.size());
				scanForBots(list);
			}
		}

		resultSet.close();
	}

	private void scanForBots(ArrayList<QueryObject> list) {
		userObject = new UserObject(list);
//		System.out.println(userObject.checkForFrequentQueries());
		if(userObject.checkForFrequentQueries()) {
			try {
				appendBotsList(userObject.getAllQueries());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void appendResults(String output) {
		try {
			bufferedWriterResults.write(output);
			bufferedWriterResults.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void appendBotsList(String output) throws IOException {
		bufferedWriterBots.write(output);
		bufferedWriterBots.newLine();
		bufferedWriterBots.flush();
	}
	
	private void appendBotsList(ArrayList<QueryObject> list) throws IOException {
		for (QueryObject queryObject : list) {
			bufferedWriterBots.write(queryObject.printContent());
			bufferedWriterBots.newLine();
		}
	}

}
