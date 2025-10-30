package com.watutz.app.objects;


public class GroupObject{
	
	private String groupName;
	private String groupId;
	private String groupNumberOfMembers;
	private String groupAdminEmail;
	private String groupAdminName;
	private String groupLastUnreadMessage;
	private String groupPhone;
	private String groupIsGroup;
	private String groupAdmins;
	private String groupPrivate;

	private boolean isSelected;
	


	
	
	public GroupObject(String grName, String grId,String grNumberOfMembers,String grAdminEmail, String grAdminName, String grLastUnreadMessage, String grPhone, String isGroup, String grAdmins, String grPrivate) {
		
		this.groupName= grName;
		this.groupId= grId;
		this.groupNumberOfMembers= grNumberOfMembers;
		this.groupAdminEmail= grAdminEmail;
		this.groupAdminName= grAdminName;
		this.groupLastUnreadMessage= grLastUnreadMessage;
		this.groupPhone= grPhone;
		this.groupIsGroup= isGroup;
		this.groupAdmins= grAdmins;
		this.groupPrivate= grPrivate;

		
	}
	
	
	public GroupObject() {
		// TODO Auto-generated constructor stub
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String grName) {
		this.groupName = grName;
	}

	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String grId) {
		this.groupId = grId;
	}
	
	public String getGroupNumberOfMembers() {
		return groupNumberOfMembers;
	}
	public void setGroupNumberOfMembers(String grNumberOfMembers) {
		this.groupNumberOfMembers = grNumberOfMembers;
	}
	
	public String getGroupAdminEmail() {
		return groupAdminEmail;
	}
	public void setGroupAdminEmail(String grAdminEmail) {
		this.groupAdminEmail = grAdminEmail;
	}
	
	public String getGroupAdminName() {
		return groupAdminName;
	}
	public void setGroupAdminName(String grAdminName) {
		this.groupAdminName = grAdminName;
	}
	
	public String getGroupLastUnreadMessage() {
		return groupLastUnreadMessage;
	}
	public void setGroupLastUnreadMessage(String grLastUnreadMessage) {
		this.groupLastUnreadMessage = grLastUnreadMessage;
	}
	
	
	public String getGroupPhone() {
		return groupPhone;
	}
	public void setGroupPhone(String grPhone) {
		this.groupPhone = grPhone;
	}
	
	public String getGroupIsGroup() {
		return groupIsGroup;
	}
	public void setGroupIsGroup(String grIsGroup) {
		this.groupIsGroup= grIsGroup;
	}
	
	public String getGroupAdmins() {
		return groupAdmins;
	}
	public void setGroupAdmins(String grAdmins) {
		this.groupAdmins = grAdmins;
	}
	
	
	public String getGroupPrivate() {
		return groupPrivate;
	}
	public void setGroupPrivate(String grPrivate) {
		this.groupPrivate= grPrivate;
	}
	
	@Override
	public String toString() {
		return groupName;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}