package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

public class UserRegisterObject{

	@SerializedName("success")
	private boolean success;

	@SerializedName("auth_token")
	private String authToken;

	@SerializedName("user")
	private User user;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setAuthToken(String authToken){
		this.authToken = authToken;
	}

	public String getAuthToken(){
		return authToken;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}
}