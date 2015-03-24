package util;

import java.util.ArrayList;

public class UserObject {

	private ArrayList<QueryObject> completeList;
	private ArrayList<SessionObject> sessionList;
	private int prevSession, currSession;

	public UserObject(ArrayList<QueryObject> list) {
		this.completeList = list;
		splitSessions();
	}

	private void splitSessions() {
		boolean firstSession = true;
		ArrayList<QueryObject> sessionBuffer = new ArrayList<QueryObject>();
		sessionList = new ArrayList<SessionObject>();
		for (QueryObject queryObject : completeList) {
			//System.out.println("prevSession " + prevSession + " currSession " + currSession);
			prevSession = currSession;
			currSession = queryObject.getSessionId();
			if (firstSession) {
				firstSession = false;
				sessionBuffer.add(queryObject);
				continue;
			}
			if (prevSession == currSession) {
//				if (firstSession) {
//					firstSession = false;
//					sessionBuffer.add(strArr);
//					continue;
//				}
				sessionBuffer.add(queryObject);
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
		for (QueryObject row : completeList) {
			System.out.println(row.printContent());
			}
		}
	
	public ArrayList<QueryObject> getAllQueries() {
		return completeList;
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
//				session.printSession();
				return true;
			}
		}
		
		return false;
	}

//	public void printRepeatedQueries() {
//		boolean firstRun = true;
//		for (String[] row : completeList) {
//			String l = "";
//			for (String str : row) {
//				l += str + "\t";
//			}
//			if (firstRun) {
//				continue;
//			}
//			// if
//
//			//System.out.println(l);
//		}
//	}

}
