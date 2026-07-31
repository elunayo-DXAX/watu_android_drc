package com.watudrc.app.objects;

import com.google.gson.annotations.SerializedName;

public class AssetObject{

	@SerializedName("vehicleTypeId")
	private int vehicleTypeId;

	@SerializedName("overdueAmount")
	private String overdueAmount;

	@SerializedName("assetTypeId")
	private int assetTypeId;

	@SerializedName("assetTypeName")
	private String assetTypeName;

	@SerializedName("maturityDate")
	private long maturityDate;

	@SerializedName("actualDate")
	private int actualDate;

	@SerializedName("instalmentAmount")
	private String instalmentAmount;

	@SerializedName("instalmentPeriod")
	private String instalmentPeriod;

	@SerializedName("loanStatus")
	private String loanStatus;

	@SerializedName("disbursementDate")
	private long disbursementDate;

	@SerializedName("remainingBalance")
	private String remainingBalance;

	@SerializedName("currencyCode")
	private String currencyCode;

	@SerializedName("loanProductName")
	private String loanProductName;

	@SerializedName("loanProductId")
	private int loanProductId;

	@SerializedName("vehicleTypeName")
	private String vehicleTypeName;

	@SerializedName("totalRepayment")
	private String totalRepayment;

	@SerializedName("deviceActiveTill")
	private long deviceActiveTill;

	@SerializedName("nextPaymentDueDate")
	private long nextPaymentDueDate;

	@SerializedName("dailyPayment")
	private String dailyPayment;


	@SerializedName("weeklyPayment")
	private String weeklyPayment;


	@SerializedName("monthlyPayment")
	private String monthlyPayment;

	@SerializedName("vehicleMakeName")
	private String vehicleMakeName;


	@SerializedName("vehicleModelName")
	private String vehicleModelName;

	@SerializedName("branchName")
	private String branchName;


	private DeviceUpgradeObject upgradeObject;
	private String loanNumber;

	private boolean isUpgradable;
	private boolean isSelected;

	public void setVehicleTypeId(int vehicleTypeId){
		this.vehicleTypeId = vehicleTypeId;
	}

	public int getVehicleTypeId(){
		return vehicleTypeId;
	}

	public void setOverdueAmount(String overdueAmount){
		this.overdueAmount = overdueAmount;
	}

	public String getOverdueAmount(){

		return overdueAmount ==null? "0":overdueAmount;
	}

	public void setAssetTypeId(int assetTypeId){
		this.assetTypeId = assetTypeId;
	}

	public int getAssetTypeId(){
		return assetTypeId;
	}

	public void setAssetTypeName(String assetTypeName){
		this.assetTypeName = assetTypeName;
	}

	public String getAssetTypeName(){
		return assetTypeName;
	}

	public void setMaturityDate(long maturityDate){
		this.maturityDate = maturityDate;
	}

	public long getMaturityDate(){
		return maturityDate;
	}

	public void setDeviceActiveTill(long deviceActiveTill){
		this.deviceActiveTill = deviceActiveTill;
	}

	public long getDeviceActiveTill(){
		return deviceActiveTill;
	}

	public void setNextPaymentDueDate(long nextPaymentDueDate){
		this.nextPaymentDueDate = nextPaymentDueDate;
	}

	public long getNextPaymentDueDate(){
		return nextPaymentDueDate;
	}

	public void setActualDate(int actualDate){
		this.actualDate = actualDate;
	}

	public int getActualDate(){
		return actualDate;
	}

	public void setInstalmentAmount(String instalmentAmount){
		this.instalmentAmount = instalmentAmount;
	}

	public String getInstalmentAmount(){
		return instalmentAmount ==null? "0": instalmentAmount;
	}

	public void setInstalmentPeriod(String instalmentPeriod){
		this.instalmentPeriod = instalmentPeriod;
	}

	public String getInstalmentPeriod(){
		return instalmentPeriod;
	}

	public void setLoanStatus(String loanStatus){
		this.loanStatus = loanStatus;
	}

	public String getLoanStatus(){
		return loanStatus;
	}

	public void setDisbursementDate(long disbursementDate){
		this.disbursementDate = disbursementDate;
	}

	public long getDisbursementDate(){
		return disbursementDate;
	}

	public void setRemainingBalance(String remainingBalance){
		this.remainingBalance = remainingBalance;
	}

	public String getRemainingBalance(){
		return remainingBalance ==null? "0": remainingBalance;
	}

	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public void setLoanProductName(String loanProductName){
		this.loanProductName = loanProductName;
	}

	public String getLoanProductName(){
		return loanProductName;
	}

	public void setLoanProductId(int loanProductId){
		this.loanProductId = loanProductId;
	}

	public int getLoanProductId(){
		return loanProductId;
	}

	public void setVehicleTypeName(String vehicleTypeName){
		this.vehicleTypeName = vehicleTypeName;
	}

	public String getVehicleTypeName(){
		return vehicleTypeName;
	}

	public void setTotalRepayment(String totalRepayment){
		this.totalRepayment = totalRepayment;
	}

	public String getTotalRepayment(){
		return totalRepayment ==null? "0": totalRepayment;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getLoanNumber(){
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber){
		this.loanNumber = loanNumber;
	}


	public boolean isUpgradable() {
		return isUpgradable;
	}
	public void setUpgradable(boolean isUpgradable) {
		this.isUpgradable = isUpgradable;
	}


	public void setDailyPayment(String dailyPayment){
		this.dailyPayment = dailyPayment;
	}

	public String getDailyPayment(){
		return dailyPayment ==null? getInstalmentAmount():dailyPayment;
	}


	public void setWeeklyPayment(String weeklyPayment){
		this.weeklyPayment = weeklyPayment;
	}

	public String getWeeklyPayment()
	{
		return weeklyPayment ==null? getInstalmentAmount():weeklyPayment;

	}


	public void setMonthlyPayment(String monthlyPayment){
		this.monthlyPayment = monthlyPayment;
	}

	public String getMonthlyPayment(){
		return monthlyPayment ==null? getInstalmentAmount():monthlyPayment;

	}


	public void setVehicleMakeName(String vehicleMakeName){
		this.vehicleMakeName = vehicleMakeName;
	}

	public String getVehicleMakeName(){
		return vehicleMakeName ;

	}

	public void setVehicleModelName(String vehicleModelName){
		this.vehicleModelName= vehicleModelName;
	}

	public String getVehicleModelName(){
		return vehicleModelName ;

	}


	public void setBranchName(String branchName){
		this.branchName = branchName;
	}

	public String getBranchName(){
		return branchName;
	}

	public void setDeviceUpgradeObject(DeviceUpgradeObject upgradeObject){
		this.upgradeObject = upgradeObject;
	}

	public DeviceUpgradeObject getDeviceUpgradeObject(){
		return this.upgradeObject;
	}
}