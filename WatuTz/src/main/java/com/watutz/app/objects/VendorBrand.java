package com.watutz.app.objects;


public class VendorBrand {

	private String id;
	private String cPhone;
	private String cName;


	private boolean isSelected;





	public VendorBrand(String cName, String cPhone) {

		this.cName= cName;
		this.cPhone= cPhone;


	}


	public VendorBrand() {
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