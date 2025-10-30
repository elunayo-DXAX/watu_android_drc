package com.watutz.app.objects;

import com.google.gson.annotations.SerializedName;

public class AdditionalParams {

	@SerializedName("camera")
	private Camera camera;

	public void setCamera(Camera camera){
		this.camera = camera;
	}

	public Camera getCamera(){
		return camera;
	}
}