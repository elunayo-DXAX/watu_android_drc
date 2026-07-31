package com.watudrc.app.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceDownObject {

	@JsonProperty("service_down")
	private boolean service_down;

	@JsonProperty("title")
	private String title;

	@JsonProperty("category")
	private String category;

	@JsonProperty("title_fr")
	private String title_fr;

	@JsonProperty("title_description")
	private String title_description;

	@JsonProperty("title_description_fr")
	private String title_description_fr;

	public ServiceDownObject(boolean service_down, String title,String category, String title_fr, String title_description, String title_description_fr) {
		this.service_down = service_down;
		this.title = title;
		this.category = category;
		this.title_fr = title_fr;
		this.title_description = title_description;
		this.title_description_fr = title_description_fr;
	}

	public ServiceDownObject() { }

	public boolean getServiceDown() { return service_down; }
	public void setServiceDown(boolean service_down) { this.service_down = service_down; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }


	public String getTitle_fr() { return title_fr; }
	public void setTitle_fr(String title_fr) { this.title_fr = title_fr; }

	public String getTitle_description() { return title_description; }
	public void setTitle_description(String title_description) { this.title_description = title_description; }

	public String getTitle_description_fr() { return title_description_fr; }
	public void setTitle_description_fr(String title_description_fr) { this.title_description_fr = title_description_fr; }
}
