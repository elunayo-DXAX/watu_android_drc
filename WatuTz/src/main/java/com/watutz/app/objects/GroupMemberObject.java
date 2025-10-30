package com.watutz.app.objects;


public class GroupMemberObject{
	
	private String groupMemberGroupId;
	private String groupMemberKey;
	private String groupMemberName;
	private String groupMemberPhone;
	private String groupMemberEmail;
	private String groupMemberProfilePic;
	

	private boolean isSelected;
	private boolean isGroupAdmin;
	


	
	
	public GroupMemberObject(String grId, String mKey, String mName, String mPhone,String mEmail, String mProfPic) {
		
		this.groupMemberGroupId= grId;
		this.groupMemberKey= mKey;
		this.groupMemberName= mName;
		this.groupMemberPhone= mPhone;
		this.groupMemberEmail= mEmail;
		this.groupMemberProfilePic= mProfPic;
	
		
	}
	
	
	public GroupMemberObject() {
		// TODO Auto-generated constructor stub
	}
	
	public String getGroupMemberGroupId() {
		return groupMemberGroupId;
	}
	public void setGroupMemberGroupId(String grId) {
		this.groupMemberGroupId = grId;
	}
	
	
	public String getGroupMemberKey() {
		return groupMemberKey;
	}
	public void setGroupMemberKey(String mKey) {
		this.groupMemberKey = mKey;
	}
	
	public String getGroupMemberName() {
		return groupMemberName;
	}
	public void setGroupMemberName(String mName) {
		this.groupMemberName = mName;
	}

	public String getGroupMemberPhone() {
		return groupMemberPhone;
	}
	public void setGroupMemberPhone(String mPhone) {
		this.groupMemberPhone= mPhone;
	}
	
	public String getGroupMemberEmail() {
		return groupMemberEmail;
	}
	public void setGroupMemberEmail(String mEmail) {
		this.groupMemberEmail = mEmail;
	}
	
	public String getGroupMemberProfilePic() {
		return groupMemberProfilePic;
	}
	public void setGroupMemberProfilePic(String mProfPic) {
		this.groupMemberProfilePic = mProfPic;
	}
	
		
	@Override
	public String toString() {
		return groupMemberName;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
	public boolean isGroupAdmin() {
		return isGroupAdmin;
	}

	public void setGroupAdmin(boolean isGroupAdmin) {
		this.isGroupAdmin = isGroupAdmin;
	}
}