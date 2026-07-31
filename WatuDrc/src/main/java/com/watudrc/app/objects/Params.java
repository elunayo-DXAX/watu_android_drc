package com.watudrc.app.objects;

import com.google.gson.annotations.SerializedName;

public class Params {

	@SerializedName("Storage")
	private String storage;

	@SerializedName("Battery")
	private String battery;

	@SerializedName("Image URL")
	private String imageURL;

	@SerializedName("Display")
	private String display;

	@SerializedName("RAM")
	private String rAM;

	public void setStorage(String storage){
		this.storage = storage;
	}

	public String getStorage(){
		return storage;
	}

	public void setBattery(String battery){
		this.battery = battery;
	}

	public String getBattery(){
		return battery;
	}

	public void setImageURL(String imageURL){
		this.imageURL = imageURL;
	}

	public String getImageURL(){
		return imageURL;
	}

	public void setDisplay(String display){
		this.display = display;
	}

	public String getDisplay(){
		return display;
	}

	public void setRAM(String rAM){
		this.rAM = rAM;
	}

	public String getRAM(){
		return rAM;
	}
}