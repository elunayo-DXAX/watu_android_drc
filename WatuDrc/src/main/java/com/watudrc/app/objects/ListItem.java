package com.watudrc.app.objects;

import com.google.gson.annotations.SerializedName;

public class ListItem{

	@SerializedName("assetTypeId")
	private String assetTypeId;

	@SerializedName("applicationNumber")
	private String applicationNumber;

	@SerializedName("loanNumber")
	private String loanNumber;

	@SerializedName("assetType")
	private String assetType;

	public void setAssetTypeId(String assetTypeId){
		this.assetTypeId = assetTypeId;
	}

	public String getAssetTypeId(){
		return assetTypeId;
	}

	public void setApplicationNumber(String applicationNumber){
		this.applicationNumber = applicationNumber;
	}

	public String getApplicationNumber(){
		return applicationNumber;
	}

	public void setLoanNumber(String loanNumber){
		this.loanNumber = loanNumber;
	}

	public String getLoanNumber(){
		return loanNumber;
	}

	public void setAssetType(String assetType){
		this.assetType = assetType;
	}

	public String getAssetType(){
		return assetType;
	}
}