package com.watutz.app.objects;


import org.json.JSONObject;

public class ContractObject {

	private String id;
	private JSONObject uParams;


	private boolean isSelected;





	public ContractObject(String id, JSONObject uParams) {

		this.id= id;
		this.uParams= uParams;


	}


	public ContractObject() {
		// TODO Auto-generated constructor stub
	}



	public String getId() {
		return id;
	}
	public void setId(String cName) {
		this.id = cName;
	}

	public JSONObject getParams() {
		return uParams;
	}
	public void setParams(JSONObject params) {
		this.uParams= params;
	}



	@Override
	public String toString() {
		return id;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}