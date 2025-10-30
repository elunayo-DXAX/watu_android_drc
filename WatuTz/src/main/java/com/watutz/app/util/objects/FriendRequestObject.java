package com.watutz.app.util.objects;


public class FriendRequestObject {
	
	private String userKey;
	private String userName;
	private String userEmail;
	private String userPhone;
	
	private String userProfilePic="";
	

	private boolean isSelected;

	
	
	
	public FriendRequestObject(String key, String name, String email, String phone,  String age)
	{
		    this.userKey = key;
	        this.userName = name;
	        this.userEmail = email;
	        this.userPhone = phone;
	        this.userProfilePic = age;
	        		
	}
	
	
	public FriendRequestObject() {
		// TODO Auto-generated constructor stub
	}
	public String getUserKey(){
		return userKey;
	}
	
	public void setUserKey(String pKey) {
		this.userKey = pKey;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String name) {
		this.userName= name;
	}
	
	
	public String getUserEmail(){
		return userEmail;
	}
	
	public void setUserEmail(String email) {
		this.userEmail = email;
	}
	
	public String getUserPhone(){
		return userPhone;
	}
	
	public void setUserPhone(String campus) {
		this.userPhone = campus;
	}
	
	
	public String getUserProfilePic(){
		return userProfilePic;
	}
	
	public void setUserProfilePic(String age) {
		this.userProfilePic= age;
	}
	
	
	
	
	@Override
	public String toString() {
		return userName;
	}	
	
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}