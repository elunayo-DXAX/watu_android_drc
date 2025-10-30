package com.watutz.app.util.objects;


public class Vendor {

	private String cName;
	private String cPhone;


	private boolean isSelected;





	public Vendor(String cName, String cPhone) {

		this.cName= cName;
		this.cPhone= cPhone;


	}


	public Vendor() {
		// TODO Auto-generated constructor stub
	}



	public String getName() {
		return cName;
	}
	public void setName(String cName) {
		this.cName = cName;
	}

	public String getPhone() {
		return cPhone;
	}
	public void setPhone(String cPhone) {
		this.cPhone= cPhone;
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