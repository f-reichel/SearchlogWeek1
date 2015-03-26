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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import jdbc.DataSourceFactory;


public class BotDetection {

	private BufferedWriter bufferedWriterResults, bufferedWriterBots;
	private FileWriter fileWriterResults, fileWriterBots;
	private File outputFileResults, outputFileBots, inputFile;
	private static final String SOURCE_FOLDER = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/";
	private static final String INPUT_PATH = SOURCE_FOLDER	+ "user_ct_test_collection_01.csv";
	private static final String OUTPUT_PATH = SOURCE_FOLDER	+ "collection-01-Results.csv";
	private static final String OUTPUT_BOTS = SOURCE_FOLDER	+ "collection-01-BOTS.csv";
	private static final String HEADER = "userId" + "\t" + "sessionId" + "\t"
			+ "score" + "\t" + "date" + "\t" + "timegap" + "\t" + "query";
	private static final String SAMPLE_USER_IDS = SOURCE_FOLDER + "sampleusers.txt";
	private static final String SAMPLE_QUERY_DATES = SOURCE_FOLDER + "sample_query_dates.txt";
	private UserObject userObject;

	private DataSource dataSource;
	private Connection dbConnection;
	private Statement sqlStatement;
	private ResultSet resultSet;
	private boolean processFile = true;
	private ArrayList<Integer> userIDs;
	private static final int LIMIT = 50;
	private int counter;
	private Scanner scanner;

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

		inputFile = new File(INPUT_PATH);
		outputFileResults = new File(OUTPUT_PATH);
		outputFileBots = new File(OUTPUT_BOTS);
		scanner = new Scanner(inputFile);
		fileWriterResults = new FileWriter(outputFileResults);
		fileWriterBots = new FileWriter(outputFileBots);
		bufferedWriterResults = new BufferedWriter(fileWriterResults);
		bufferedWriterBots = new BufferedWriter(fileWriterBots);

		//readCSV(scanner);
		generateUserDaysMatrixScanner(scanner);
		//generateUserDaysMatrix();
//		queryDB(null);

		bufferedWriterResults.close();
		bufferedWriterBots.close();
	}
	
	

	private void generateUserDaysMatrixScanner(Scanner scanner) {
		ArrayList<Integer> IDs = getUserIdsCSV();
		ArrayList<Date> dates = getDatesCSV();
		int[][] matrix = new int[IDs.size()][dates.size()];
		System.out.println(matrix.length + "," + matrix[0].length);
		for (int i = 0; i < IDs.size(); i++) {
			int userId = IDs.get(i);
			System.out.print(( i+1) + " " + userId + ": ");
			//ArrayList<String> tmpQueriesCSV = getQueriesForUserCSV(IDs.get(i));
			ResultSet tmpQueries = null;
			try {
				tmpQueries = getQueriesForUser(IDs.get(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int j = 0; j < dates.size(); j++) {
					int value = 0;
					value = queriesOnADay(tmpQueries, dates.get(j));
					System.out.print(value + ",");
					matrix[i][j] = value;
			}
			System.out.println();
		}
	}

	private void generateUserDaysMatrix() throws SQLException {
		ArrayList<Integer> IDs = getUserIds();
		ArrayList<Date> dates = getDates();
		int[][] matrix = new int[IDs.size()][dates.size()];
		System.out.println(matrix.length + "," + matrix[0].length);
		for (int i = 0; i < IDs.size(); i++) {
			int userId = (int)IDs.get(i);
			System.out.print(userId + ": ");
			ResultSet tmpQueries = getQueriesForUser((int)IDs.get(i));
			for (int j = 0; j < dates.size(); j++) {
				int value = queriesOnADay(tmpQueries, dates.get(j));
				System.out.print(value + ",");
				matrix[i][j] = value;
				
			}
			System.out.println();
		}
	}
	
	private int queriesOnADayCSV(ArrayList<String> tmpQueriesCSV, Date date) {
		int counter = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");		
		String dateRef = df.format(date);
		for (int i = 0; i < tmpQueriesCSV.size(); i++) {
			String str = tmpQueriesCSV.get(i);
			if(str.contains(dateRef)) {
				counter++;
			}
			
		}
		return counter;
	}

	private int queriesOnADay(ResultSet tmpQueries, Date date) {
		int counter = 0;
		try {
		while (tmpQueries.next()) {
			Date tmpDate = tmpQueries.getDate(3);
			//System.out.println(tmpDate);
			//System.out.println(date);
			if (date.equals(tmpDate)) {
				counter++;
			}
		}
		tmpQueries.beforeFirst();
		} catch (SQLException e) {
			
		}
		
		return counter;
	}

	private ArrayList<String> getQueriesForUserCSV(Integer userId) {
		boolean firstLine = true;
		ArrayList<String> list = new ArrayList<String>();
		File queriesTxt = new File(INPUT_PATH);
		try {
			Scanner sc = new Scanner(queriesTxt);
			while(sc.hasNext()) {
				if (firstLine) {
					firstLine = false;
					sc.nextLine();
					continue;
				}
				String str = sc.nextLine();
				if (str.startsWith(String.valueOf(userId), 1)) {
//					String[] strArr = str.split("\t");
					list.add(str);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;		
	}

	private ResultSet getQueriesForUser(int id) throws SQLException {
		ResultSet queryResult = sqlStatement.executeQuery("select * from user_ct_test_collection_01 where userId = " + id + ";");
		return queryResult;		
	}

	private ArrayList<Integer> getUserIds() throws SQLException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		resultSet = sqlStatement.executeQuery("select sampleusers from sample_users");
		//resultSet = sqlStatement.executeQuery("select distinct userId from user_ct_test_collection_01");
		while(resultSet.next()) {
			list.add(resultSet.getInt(1));
		}
		return list;		
	}
	
	private ArrayList<Integer> getUserIdsCSV() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		File usersTxt = new File(SAMPLE_USER_IDS);
		try {
			Scanner sc = new Scanner(usersTxt);
			while(sc.hasNext()) {
				list.add(sc.nextInt());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;		
	}

	private ArrayList<Date> getDates() throws SQLException {
		ArrayList<Date> list = new ArrayList<Date>();
		resultSet = sqlStatement.executeQuery("select distinct date(date) from user_ct_test_collection_01 order by date");
		while(resultSet.next()) {
			list.add(resultSet.getDate(1));
		}
		return list;		
	}
	
	private ArrayList<Date> getDatesCSV () {
		ArrayList<Date> list = new ArrayList<Date>();
		File queryDates = new File(SAMPLE_QUERY_DATES);
		try {
			Scanner sc = new Scanner(queryDates);
			while(sc.hasNext()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date result = sdf.parse(sc.next());
				list.add(result);
			}
			sc.close();
		} catch (FileNotFoundException  e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;		
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
