package com.watutz.app.adapters;


public class GooglePlaceListItem{
	
	private String placeDescription;
	private String placeId;
	


	private boolean isSelected;
	


	
	
	public GooglePlaceListItem(String plId, String plDesc) {
		
		this.placeId= plId;
		this.placeDescription= plDesc;
	
	}
	
	
	public GooglePlaceListItem() {
		// TODO Auto-generated constructor stub
	}
	
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String plId) {
		this.placeId = plId;
	}
	
	public String getPlaceDescription() {
		return placeDescription;
	}
	public void setPlaceDescription(String plDesc) {
		this.placeDescription = plDesc;
	}

	
	
	@Override
	public String toString() {
		return placeDescription;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}