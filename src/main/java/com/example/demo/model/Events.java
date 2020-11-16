package com.example.demo.model;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Events implements Serializable {

//	private String uuid;
//	private String tstamp;
//	private String source;
//	
//	public Events() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	public Events(String uuid, String tstamp, String source, String date, String event_type, String event_category,
//			String event_action, String event_label, String event_value, String created_at, String last_updated_at,
//			String location, String id) {
//		super();
//		this.uuid = uuid;
//		this.tstamp = tstamp;
//		this.source = source;
//		this.date = date;
//		this.event_type = event_type;
//		this.event_category = event_category;
//		this.event_action = event_action;
//		this.event_label = event_label;
//		this.event_value = event_value;
//		this.created_at = created_at;
//		this.last_updated_at = last_updated_at;
//		this.location = location;
//		this.id = id;
//	}
//	public String getUuid() {
//		return uuid;
//	}
//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}
//	public String getTstamp() {
//		return tstamp;
//	}
//	public void setTstamp(String tstamp) {
//		this.tstamp = tstamp;
//	}
//	public String getSource() {
//		return source;
//	}
//	public void setSource(String source) {
//		this.source = source;
//	}
//	public String getDate() {
//		return date;
//	}
//	public void setDate(String date) {
//		this.date = date;
//	}
//	public String getEvent_type() {
//		return event_type;
//	}
//	public void setEvent_type(String event_type) {
//		this.event_type = event_type;
//	}
//	public String getEvent_category() {
//		return event_category;
//	}
//	public void setEvent_category(String event_category) {
//		this.event_category = event_category;
//	}
//	public String getEvent_action() {
//		return event_action;
//	}
//	public void setEvent_action(String event_action) {
//		this.event_action = event_action;
//	}
//	public String getEvent_label() {
//		return event_label;
//	}
//	public void setEvent_label(String event_label) {
//		this.event_label = event_label;
//	}
//	public String getEvent_value() {
//		return event_value;
//	}
//	public void setEvent_value(String event_value) {
//		this.event_value = event_value;
//	}
//	public String getCreated_at() {
//		return created_at;
//	}
//	public void setCreated_at(String created_at) {
//		this.created_at = created_at;
//	}
//	public String getLast_updated_at() {
//		return last_updated_at;
//	}
//	public void setLast_updated_at(String last_updated_at) {
//		this.last_updated_at = last_updated_at;
//	}
//	public String getLocation() {
//		return location;
//	}
//	public void setLocation(String location) {
//		this.location = location;
//	}
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
//	private String date;
//	private String event_type;
//	private String event_category;
//	private String event_action;
//	private String event_label;
//	private String event_value;
//	private String created_at;
//	private String last_updated_at;
//	private String location;
//	private String id;
//
//	@Override
//	public String toString() {
//		return "Events [uuid=" + uuid + ", tstamp=" + tstamp + ", source=" + source + ", date=" + date + ", event_type="
//				+ event_type + ", event_category=" + event_category + ", event_action=" + event_action
//				+ ", event_label=" + event_label + ", event_value=" + event_value + ", created_at=" + created_at
//				+ ", last_updated_at=" + last_updated_at + ", location=" + location + ", id=" + id + "]";
//	}
//	
	private String uuid;
	private String tstamp;
	private String source;
	private Date date;
	private String event_type;
	private String event_category;
	private String event_action;
	private String event_label;
	private int event_value;
	private String created_at;
	private String last_updated_at;
	private String location;
	@Id
	private String id;
	
	
	public Events() {
		super();
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tsv) {
		this.tstamp = tsv;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date2) {
		this.date = (Date) date2;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getEvent_category() {
		return event_category;
	}
	public void setEvent_category(String event_category) {
		this.event_category = event_category;
	}
	public String getEvent_action() {
		return event_action;
	}
	public void setEvent_action(String event_action) {
		this.event_action = event_action;
	}
	public String getEvent_label() {
		return event_label;
	}
	public void setEvent_label(String event_label) {
		this.event_label = event_label;
	}
	public int getEvent_value() {
		return event_value;
	}
	public void setEvent_value(int event_value) {
		this.event_value = event_value;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String tsv) {
		this.created_at = tsv;
	}
	public String getLast_updated_at() {
		return last_updated_at;
	}
	public void setLast_updated_at(String tsv) {
		this.last_updated_at = tsv;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getId() {
		return id;
	}
	public void setId(String tsv) {
		this.id = tsv;
	}
	@Override
	public String toString() {
		return "Events [uuid=" + uuid + ", tstamp=" + tstamp + ", source=" + source + ", date=" + date + ", event_type="
				+ event_type + ", event_category=" + event_category + ", event_action=" + event_action
				+ ", event_label=" + event_label + ", event_value=" + event_value + ", created_at=" + created_at
				+ ", last_updated_at=" + last_updated_at + ", location=" + location + ", id=" + id + "]";
	}
//	
//	
}
