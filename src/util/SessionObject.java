package util;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SessionObject {
	
	private ArrayList<String[]> list;
	private int currGap, prevGap;
	
	public SessionObject(ArrayList<String[]> list) {
		this.list = list;
	}
	
	public boolean checkForFrequency() {
		boolean firstGap = true;
		
		for (String[] strArr : list) {
//			prevGap = currGap;
			currGap = Integer.parseInt(strArr[4]);
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
		for (String[] row : list) {
			String l = "";
			for (String str : row) {
				l += str + "\t";
			}
			//System.out.println(l);
			
		}
	}

}
