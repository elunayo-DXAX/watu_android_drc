package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

public class ProvidersItem{

	@SerializedName("method")
	private String method;

	@SerializedName("minPaymentAmount")
	private String minPaymentAmount;

	@SerializedName("providerId")
	private String providerId;

	@SerializedName("displayName")
	private String displayName;

	@SerializedName("logo")
	private String logo;

	@SerializedName("branchDisplayColor")
	private String branchDisplayColor;

	@SerializedName("providerName")
	private String providerName;

	@SerializedName("paybillNumber")
	private String paybillNumber;

	public void setMethod(String method){
		this.method = method;
	}

	public String getMethod(){
		return method;
	}

	public void setMinPaymentAmount(String minPaymentAmount){
		this.minPaymentAmount = minPaymentAmount;
	}

	public String getMinPaymentAmount(){
		return minPaymentAmount;
	}

	public void setProviderId(String providerId){
		this.providerId = providerId;
	}

	public String getProviderId(){
		return providerId;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setLogo(String logo){
		this.logo = logo;
	}

	public String getLogo(){
		return logo;
	}

	public void setBranchDisplayColor(String branchDisplayColor){
		this.branchDisplayColor = branchDisplayColor;
	}

	public String getBranchDisplayColor(){
		return branchDisplayColor;
	}

	public void setProviderName(String providerName){
		this.providerName = providerName;
	}

	public String getProviderName(){
		return providerName;
	}

	public void setPaybillNumber(String paybillNumber){
		this.paybillNumber = paybillNumber;
	}

	public String getPaybillNumber(){
		return paybillNumber;
	}
}