package com.watutz.app.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceUpgradeObject {

	@SerializedName("currentDevice")
	private CurrentDevice currentDevice;

	@SerializedName("upgradeDevices")
	private List<UpgradeDevicesItem> upgradeDevices;

	public void setCurrentDevice(CurrentDevice currentDevice){
		this.currentDevice = currentDevice;
	}

	public CurrentDevice getCurrentDevice(){
		return currentDevice;
	}

	public void setUpgradeDevices(List<UpgradeDevicesItem> upgradeDevices){
		this.upgradeDevices = upgradeDevices;
	}

	public List<UpgradeDevicesItem> getUpgradeDevices(){
		return upgradeDevices;
	}
}