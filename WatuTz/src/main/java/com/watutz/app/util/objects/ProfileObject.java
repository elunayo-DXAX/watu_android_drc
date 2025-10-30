package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

public class ProfileObject{

	@SerializedName("firstname")
	private String firstname;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("gender")
	private String gender;

	@SerializedName("dateOfBirth")
	private String dateOfBirth;

	@SerializedName("lastname")
	private String lastname;

	public void setFirstname(String firstname){
		this.firstname = firstname;
	}

	public String getFirstname(){
		return firstname;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setDateOfBirth(String dateOfBirth){
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	public String getLastname(){
		return lastname;
	}
}