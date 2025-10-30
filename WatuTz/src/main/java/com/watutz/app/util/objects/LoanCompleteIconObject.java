package com.watutz.app.util.objects;


import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanCompleteIconObject {

	@JsonProperty("name")
	private String name;

	@JsonProperty("image_url")
	private String image_url;

	@JsonProperty("image_url_sw")
	private String image_url_sw;

	@JsonProperty("redirect_link")
	private String redirect_link;

	@JsonProperty("type")
	private String type;






	public LoanCompleteIconObject(String name, String image_url, String image_url_sw, String redirect_link, String type) {

		this.name= name;
		this.image_url= image_url;
		this.image_url_sw= image_url_sw;
		this.redirect_link= redirect_link;
		this.type= type;


	}


	public LoanCompleteIconObject() {
		// TODO Auto-generated constructor stub
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}


	public String getImage_url_sw() {
		return image_url_sw;
	}
	public void setImage_url_sw(String image_url_sw) {
		this.image_url_sw = image_url_sw;
	}

	public String getRedirect_link() {
		return redirect_link;
	}
	public void setRedirect_link(String redirectLink) {
		this.redirect_link= redirectLink;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return name;
	}
	

}