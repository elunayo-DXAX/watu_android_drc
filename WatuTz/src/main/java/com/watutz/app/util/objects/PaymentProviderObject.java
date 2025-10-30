package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentProviderObject{

	@SerializedName("providers")
	private List<ProvidersItem> providers;

	public void setProviders(List<ProvidersItem> providers){
		this.providers = providers;
	}

	public List<ProvidersItem> getProviders(){
		return providers;
	}
}