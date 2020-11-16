package com.example.demo.dataTransfer;

public class UserDataTransfer {
	private String userId;
	private String overallTimeSpent;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getOverallTimeSpent() {
		return overallTimeSpent;
	}
	public void setOverallTimeSpent(String overallTimeSpent) {
		this.overallTimeSpent = overallTimeSpent;
	}
	public UserDataTransfer(String userId, String count) {
		super();
		this.userId = userId;
		this.overallTimeSpent = count;
	}
}
