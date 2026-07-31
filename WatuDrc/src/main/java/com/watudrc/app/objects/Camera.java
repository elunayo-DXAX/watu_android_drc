package com.watudrc.app.objects;

import com.google.gson.annotations.SerializedName;

public class Camera {

	@SerializedName("Front")
	private String front;

	@SerializedName("Main")
	private String main;

	public void setFront(String front){
		this.front = front;
	}

	public String getFront(){
		return front;
	}

	public void setMain(String main){
		this.main = main;
	}

	public String getMain(){
		return main;
	}
}