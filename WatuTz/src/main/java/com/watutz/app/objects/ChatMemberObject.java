package com.watutz.app.objects;


public class ChatMemberObject{
	
	private String userKey;
	private String userName;
	private String userPhone;
	private String userEmail;
	

	private boolean isSelected;
	


	
	
	public ChatMemberObject(String mKey, String mName, String mCampus,String mEmail) {
		
		this.userKey= mKey;
		this.userName= mName;
		this.userPhone= mCampus;
		this.userEmail= mEmail;
	
		
	}
	
	
	public ChatMemberObject() {
		// TODO Auto-generated constructor stub
	}
	

	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String mKey) {
		this.userKey= mKey;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String mName) {
		this.userName = mName;
	}

	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String mCampus) {
		this.userPhone= mCampus;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String mEmail) {
		this.userEmail = mEmail;
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