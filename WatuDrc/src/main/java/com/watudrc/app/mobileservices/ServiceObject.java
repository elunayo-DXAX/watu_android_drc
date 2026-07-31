package com.watudrc.app.mobileservices;

import java.util.List;


public class ServiceObject{
	
	
	private String result;
	private List<String> params;
	private boolean isSelected;


	
	
	public ServiceObject(String res, List<String> param) {
	
		this.result= res;
		this.params= param;
		
		
	}
	
	
	public ServiceObject() {
		// TODO Auto-generated constructor stub
	}

	
		
	public String getResult() {
		return result;
	}
	public void setResult(String res) {
		this.result = res;
	}
	
	public List<String>getParams() {
		return params;
	}
	public void setParams(List<String> para) {
		this.params = para;
	}
	
		
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}