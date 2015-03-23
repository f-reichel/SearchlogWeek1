package util;

import java.util.ArrayList;

public class UserObject {

	private ArrayList<String[]> completeList;
	private ArrayList<SessionObject> sessionList;
	private int prevSession, currSession;

	public UserObject(ArrayList<String[]> list) {
		this.completeList = list;
		splitSessions();
	}

	private void splitSessions() {
		boolean firstSession = true;
		ArrayList<String[]> sessionBuffer = new ArrayList<String[]>();
		sessionList = new ArrayList<SessionObject>();
		for (String[] strArr : completeList) {
			//System.out.println("prevSession " + prevSession + " currSession " + currSession);
			prevSession = currSession;
			currSession = Integer.parseInt(strArr[1]);
			if (firstSession) {
				firstSession = false;
				sessionBuffer.add(strArr);
				continue;
			}
			if (prevSession == currSession) {
//				if (firstSession) {
//					firstSession = false;
//					sessionBuffer.add(strArr);
//					continue;
//				}
				sessionBuffer.add(strArr);
			} else {
//				if (firstSession) {
//					firstSession = false;
//					sessionBuffer.add(strArr);
//					continue;
//				}
				SessionObject sessionObject = new SessionObject(sessionBuffer);
				sessionList.add(sessionObject);
			}
		}
	}

	public void printUserObject() {
		//System.out.println("User: " + completeList.get(0)[0] + "\t" + "size: "
		//		+ completeList.size());
		for (String[] row : completeList) {
			String l = "";
			for (String str : row) {
				l += str + "\t";
			}
			//System.out.println(l);
		}
	}

	// public ArrayList<SessionObject> splitSessions() {
	// ArrayList<SessionObject> sessions = new ArrayList<SessionObject>();
	//
	// SessionObject obj = new SessionObject(list);
	//
	//
	// return sessions;

	// }
	
	public boolean checkForFrequentQueries() {
		for (SessionObject session : sessionList) {
			if (session.checkForFrequency()) {
				session.printSession();
				return true;
			}
		}
		
		return false;
	}

	public void printRepeatedQueries() {
		boolean firstRun = true;
		for (String[] row : completeList) {
			String l = "";
			for (String str : row) {
				l += str + "\t";
			}
			if (firstRun) {
				continue;
			}
			// if

			//System.out.println(l);
		}
	}

}
