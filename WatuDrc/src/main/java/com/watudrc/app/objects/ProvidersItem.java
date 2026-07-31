package com.watudrc.app.objects;

import com.google.gson.annotations.SerializedName;

public class ProvidersItem{

	@SerializedName("method")
	private String method;

	@SerializedName("providerId")
	private String providerId;

	@SerializedName("providerName")
	private String providerName;

	public void setMethod(String method){
		this.method = method;
	}

	public String getMethod(){
		return method;
	}

	public void setProviderId(String providerId){
		this.providerId = providerId;
	}

	public String getProviderId(){
		return providerId;
	}

	public void setProviderName(String providerName){
		this.providerName = providerName;
	}

	public String getProviderName(){
		return providerName;
	}
}