package com.watutz.app.util.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceDownObject {

	@JsonProperty("service_down")
	private boolean service_down;

	@JsonProperty("title")
	private String title;

	@JsonProperty("title_sw")
	private String title_sw;

	@JsonProperty("title_description")
	private String title_description;

	@JsonProperty("title_description_sw")
	private String title_description_sw;

	public ServiceDownObject(boolean service_down, String title, String title_sw, String title_description, String title_description_sw) {
		this.service_down = service_down;
		this.title = title;
		this.title_sw = title_sw;
		this.title_description = title_description;
		this.title_description_sw = title_description_sw;
	}

	public ServiceDownObject() { }

	public boolean getServiceDown() { return service_down; }
	public void setServiceDown(boolean service_down) { this.service_down = service_down; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getTitle_sw() { return title_sw; }
	public void setTitle_sw(String title_sw) { this.title_sw = title_sw; }

	public String getTitle_description() { return title_description; }
	public void setTitle_description(String title_description) { this.title_description = title_description; }

	public String getTitle_description_sw() { return title_description_sw; }
	public void setTitle_description_sw(String title_description_sw) { this.title_description_sw = title_description_sw; }
}
