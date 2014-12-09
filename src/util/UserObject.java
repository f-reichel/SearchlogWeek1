package util;

import java.util.ArrayList;

public class UserObject {
	
	private ArrayList<String[]> list;

	public UserObject (ArrayList<String[]> list) {
		this.list = list;
	}
	
	public void printList() {
		System.out.println("User: " + list.get(0)[0] + "\t" + "size: " + list.size());
		for (String[] row : list) {
			String l = "";
			for (String str : row) {
				l += str + "\t";
			}
			System.out.println(l);
		}
	}
	
	public ArrayList<SessionObject> splitSessions() {
		ArrayList<SessionObject> sessions = new ArrayList<SessionObject>();
		
		SessionObject obj = new SessionObject(list);
		
		
		return sessions;
		
	}
	
	public void printRepeatedQueries() {
		boolean firstRun = true;
		for (String[] row : list) {
			String l = "";
			for (String str : row) {
				l += str + "\t";
			}
			if (firstRun) {
				continue;
			}
//			if 
			
			System.out.println(l);
		}
	}

}
