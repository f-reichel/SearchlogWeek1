package util;

import java.sql.Timestamp;

public class QueryObject {
	private int userId, sessionId, itemRank, timeGap;
	private long epoc;
	private String query, clickUrl;
	private Timestamp date;
	
	public QueryObject(int userId, int sessionId, Timestamp date, int timeGap, long epoc, String query, int itemRank, String clickUrl) {
		this.clickUrl = clickUrl;
		this.epoc = epoc;
		this.itemRank = itemRank;
		this.query = query;
		this.sessionId = sessionId;
		this.timeGap = timeGap;
		this.userId = userId;
		this.date = date;
	}

	public int getUserId() {
		return userId;
	}

	public int getSessionId() {
		return sessionId;
	}

	public int getItemRank() {
		return itemRank;
	}

	public int getTimeGap() {
		return timeGap;
	}

	public long getEpoc() {
		return epoc;
	}

	public String getQuery() {
		return query;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public Timestamp getDate() {
		return date;
	}
	
	public String printContent() {
		String row = 
				userId + "\t"
				+ sessionId + "\t"
				+ date + "\t"
				+ timeGap + "\t"
				+ epoc + "\t"
				+ query + "\t"
				+ itemRank + "\t"
				+ clickUrl;
		return row;
	}
}
