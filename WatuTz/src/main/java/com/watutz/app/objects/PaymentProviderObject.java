package com.watutz.app.objects;

import java.util.List;
import com.google.gson.annotations.SerializedName;

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