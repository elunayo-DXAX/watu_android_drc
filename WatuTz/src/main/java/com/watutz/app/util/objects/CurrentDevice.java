package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

public class CurrentDevice {

	@SerializedName("additionalParams")
	private AdditionalParams additionalParams;

	@SerializedName("modelCode")
	private String modelCode;



	@SerializedName("name")
	private String name;

	@SerializedName("params")
	private Params params;

	public void setAdditionalParams(AdditionalParams additionalParams){
		this.additionalParams = additionalParams;
	}

	public AdditionalParams getAdditionalParams(){
		return additionalParams;
	}

	public void setModelCode(String modelCode){
		this.modelCode = modelCode;
	}

	public String getModelCode(){
		return modelCode;
	}



	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setParams(Params params){
		this.params = params;
	}

	public Params getParams(){
		return params;
	}
}