package com.watutz.app.datamanager;


public class KeywordObject {
	private String name;
	private String keyword;

	public KeywordObject(String name, String keyword) {
		super();
		this.name = name;
		this.keyword = keyword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
