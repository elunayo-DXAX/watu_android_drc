package com.watutz.app.objects;


public class NotificationObject {

	private String id;
	private String title;
	private String body;
	private String type;
	private String data;
	private String action;

	private String date;

	private String time;

	private long timestamp;


	private boolean isSelected;
	private boolean isRead;





	public NotificationObject(String id,String title, String body, String type, String data, String date, String time, String action, long timestamp) {

		this.id= id;
		this.title= title;
		this.body= body;

		this.type= type;
		this.data= data;

		this.date= date;
		this.time= time;
		this.action= action;

		this.timestamp= timestamp;


	}


	public NotificationObject() {
		// TODO Auto-generated constructor stub
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type= type;
	}


	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data= data;
	}


	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date= date;
	}


	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp= timestamp;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time= time;
	}



	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body= body;
	}


	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action= action;
	}



	@Override
	public String toString() {
		return body;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
}