package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

public class ErrorResponseObject{

	@SerializedName("error")
	private Error error;

	public void setError(Error error){
		this.error = error;
	}

	public Error getError(){
		return error;
	}
}