package util;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SessionObject {
	
	private ArrayList<QueryObject> list;
	private int currGap, prevGap;
	
	public SessionObject(ArrayList<QueryObject> sessionBuffer) {
		this.list = sessionBuffer;
	}
	
	public boolean checkForFrequency() {
		boolean firstGap = true;
		
		for (QueryObject queryObject : list) {
//			prevGap = currGap;
			currGap = queryObject.getTimeGap();
			if (firstGap) {
				firstGap = false;
				continue;
			}
			if (currGap < 100) {
				return true;
			}
			
		}
		
		
		return false;
	}

	public void printSession() {
		//System.out.println("User: " + list.get(0)[0] + "\t" + "Session: " + list.get(0)[1] + "\t" + "size: "
		//		+ list.size());
		for (QueryObject row : list) {
			
			System.out.println(row.printContent());
			
		}
	}

}
