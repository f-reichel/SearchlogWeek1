package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExtractQueries {
	
	private static final String SOURCE_FOLDER = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/";
	private static final String INPUT_PATH = SOURCE_FOLDER	+ "user_ct_test_collection_01.csv";
//	private static final String INPUT_PATH = SOURCE_FOLDER	+ "user-ct-test-collection-01-offby1error.csv";
	private static final String SAMPLE_OUTPUT_PATH = SOURCE_FOLDER	+ "user_ct_test_collection_01-sample.csv";
	private static final String DUMP_OUTPUT_PATH = SOURCE_FOLDER	+ "user_ct_test_collection_01-dump.csv";
	private static final String SEP = ",";
	private static final String HEADER = 
			"\"userId\"" + SEP
			+ "\"sessionId\"" + SEP
			+ "\"date\"" + SEP
			+ "\"timegap\"" + SEP
			+ "\"epoc\"" + SEP
			+ "\"query\"" + SEP
			+ "\"itemRank\"" + SEP
			+ "\"clickUrl\"";
	
	private static final String SAMPLE_USER_IDS = SOURCE_FOLDER + "sampleusers.txt";
//	private static final String SAMPLE_USER_IDS = SOURCE_FOLDER + "sampleusers_test.txt";
	
	private File inputFile, outputFileDump;
	private Scanner scanner;
	private FileWriter dumpWriter;
	private File outputFileSample;
	
	public static void main(String[] args) {
		new ExtractQueries();
	}
	
	public ExtractQueries() {
		outputFileDump = new File(DUMP_OUTPUT_PATH);
		try {
			dumpWriter = new FileWriter(outputFileDump);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer> IDs = getUserIdsCSV();
		ArrayList<String> queries = getQueriesForUserCSV(IDs);
		writeResults(queries);
	}
	
	private void writeResults(ArrayList<String> queries) {
		outputFileSample = new File(SAMPLE_OUTPUT_PATH);
		try {
			FileWriter writer = new FileWriter(outputFileSample);
			writer.write(HEADER);
			for (String str : queries) {
				writer.append("\n");
				writer.write(str);
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void writeDump(String str) {
			try {
				dumpWriter.write(str);
				dumpWriter.append("\n");
				dumpWriter.flush();			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private ArrayList<String> getQueriesForUserCSV(ArrayList<Integer> users) {
		int counter = 0;
		boolean userHit = false;
		boolean firstLine = true;
		ArrayList<String> list = new ArrayList<String>();
		File queriesTxt = new File(INPUT_PATH);
		Scanner sc = null;
		try {
			sc = new Scanner(queriesTxt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int lines = 0;
		while(sc.hasNext()) {
			if(firstLine) {
				firstLine = false;
				continue;
			}
//			if (lines >= 3558029) {
//				System.out.println(lines);
//			}
			lines++;
//			if (counter > 3) {
//				break;
//			}
			String str = sc.nextLine();
			String uid = String.valueOf(users.get(counter));
//			if ("3881226".equals(uid)) {
//				System.out.println("STOP");
//				
//			}
			writeDump(str);
			String refID = String.valueOf(users.get(counter));
			if (str.startsWith(refID, 1)) {
				list.add(str);
				userHit = true;
			} else {
				if (userHit) {
					if (counter < users.size() -1) {
						counter++;
					}
					if (counter == 999) {
						System.out.println("STOP");
					}
					System.out.println(counter + ": " + str);
					userHit = false;
				}
				String succID = String.valueOf(users.get(counter));
				if ((counter < users.size()) && (str.startsWith(succID, 1))) {
					list.add(str);
					userHit = true;
				}
			}
		}
		sc.close();
		try {
			dumpWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("lines: " + lines);
		System.out.println("retrieved: " + list.size());
		return list;
			
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
	
}
