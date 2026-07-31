package com.watudrc.app.objects;


public class Country {
	
	private String cName,cCode;
	private Integer cRes;


	private boolean isSelected;





	public Country(String cName,String cCode, Integer cRes) {

		this.cName= cName;
		this.cCode= cCode;
		this.cRes= cRes;


	}


	public Country() {
		// TODO Auto-generated constructor stub
	}



	public String getName() {
		return cName;
	}
	public void setName(String cName) {
		this.cName = cName;
	}

	public String getCode() {
		return cCode;
	}
	public void setCode(String cName) {
		this.cCode = cCode;
	}

	public Integer getRes() {
		return cRes;
	}
	public void setRes(Integer cRes) {
		this.cRes= cRes;
	}



	@Override
	public String toString() {
		return cName;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}