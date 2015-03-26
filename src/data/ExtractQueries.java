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
	private static final String SAMPLE_OUTPUT_PATH = SOURCE_FOLDER	+ "user_ct_test_collection_01-sample.csv";
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
	
	private File inputFile, outputFileSample;
	private Scanner scanner;
	
	public static void main(String[] args) {
		new ExtractQueries();
	}
	
	public ExtractQueries() {
		ArrayList<Integer> IDs = getUserIdsCSV();
		ArrayList<String> queries = getQueriesForUserCSV(IDs);
		writeResults(queries);
	}
	
	private void writeResults(ArrayList<String> queries) {
		outputFileSample = new File(SAMPLE_OUTPUT_PATH);
		try {
			FileWriter writer = new FileWriter(outputFileSample);
			writer.write(HEADER);
			writer.append("\n");
			for (String str : queries) {
				writer.write(str);
				writer.append("\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private ArrayList<String> getQueriesForUserCSV(ArrayList<Integer> users) {
		int counter = 0;
		boolean userHit = false;
		ArrayList<String> list = new ArrayList<String>();
		File queriesTxt = new File(INPUT_PATH);
		Scanner sc = null;
		try {
			sc = new Scanner(queriesTxt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sc.nextLine();
		while(sc.hasNext()) {
//			if (counter > 3) {
//				break;
//			}
			String str = sc.nextLine();
			if (str.startsWith(String.valueOf(users.get(counter)), 1)) {
				list.add(str);
				userHit = true;
			} else {
				if (userHit) {
					counter++;
					userHit = false;
				}
				if ((counter < users.size()) && (str.startsWith(String.valueOf(users.get(counter)), 1))) {
					list.add(str);
				}
			}
		}
		sc.close();
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
