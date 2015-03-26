package data;

import java.io.File;

public class ExtractQueries {
	
	private static final String SOURCE_FOLDER = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/";
	private static final String INPUT_PATH = SOURCE_FOLDER	+ "user_ct_test_collection_01.csv";
	private static final String OUTPUT_PATH = SOURCE_FOLDER	+ "user_ct_test_collection_01-sample.csv";
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
	
	public static void main(String[] args) {
		new ExtractQueries();
	}
	
	public ExtractQueries() {
		
	}
	
}
