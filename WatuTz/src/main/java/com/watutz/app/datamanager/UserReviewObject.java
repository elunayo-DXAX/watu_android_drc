package com.watutz.app.datamanager;


public class UserReviewObject {

	private String authorName;
	private String authorUrl;
	private float phone;
	private String text;
	private long time;

	public UserReviewObject() {
		super();
	}

	public UserReviewObject(String authorName, String authorUrl, float phone, String text, long time) {
		super();
		this.authorName = authorName;
		this.authorUrl = authorUrl;
		this.phone = phone;
		this.text = text;
		this.time = time;
	}

	public float getPhone() {
		return phone;
	}

	public void setPhone(float phone) {
		this.phone = phone;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

}
