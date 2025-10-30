package com.watutz.app.objects;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("id_number")
	private String idNumber;

	@SerializedName("gender")
	private String gender;

	@SerializedName("date_of_birth")
	private String dateOfBirth;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("id")
	private String id;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setIdNumber(String idNumber){
		this.idNumber = idNumber;
	}

	public String getIdNumber(){
		return idNumber;
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

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}


	public void setName(String lastName){
	}

	public String getName(){
		return firstName+" "+lastName;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setPhone_number(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhone_number(){
		return phoneNumber;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}


	public void setImage(String avatar){
		this.avatar = avatar;
	}

	public String getImage(){
		return avatar;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}