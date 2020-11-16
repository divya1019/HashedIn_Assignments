package com.example.demo.dataTransfer;

public class EventDataTransfer {
	private String page;
	private Object count;
	
	
	
	public EventDataTransfer(String page, Object count) {
		super();
		this.page = page;
		this.count = count;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public Object getCount() {
		return count;
	}
	public void setCount(Object count) {
		this.count = count;
	}
}
