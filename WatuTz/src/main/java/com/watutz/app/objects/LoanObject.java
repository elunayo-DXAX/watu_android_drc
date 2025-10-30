package com.watutz.app.objects;

import com.google.gson.annotations.SerializedName;

public class LoanObject{

	@SerializedName("dailyPayment")
	private String dailyPayment;

	@SerializedName("overdueAmount")
	private String overdueAmount;

	@SerializedName("loanType")
	private String loanType;

	@SerializedName("maturityDate")
	private String maturityDate;

	@SerializedName("deviceActiveTill")
	private String deviceActiveTill;

	@SerializedName("instalmentAmount")
	private String instalmentAmount;

	@SerializedName("instalmentPeriod")
	private String instalmentPeriod;

	@SerializedName("loanStatus")
	private String loanStatus;

	@SerializedName("disbursementDate")
	private String disbursementDate;

	@SerializedName("remainingBalance")
	private String remainingBalance;

	@SerializedName("currencyCode")
	private String currencyCode;

	@SerializedName("totalRepayment")
	private String totalRepayment;

	public void setDailyPayment(String dailyPayment){
		this.dailyPayment = dailyPayment;
	}

	public String getDailyPayment(){
		return dailyPayment;
	}

	public void setOverdueAmount(String overdueAmount){
		this.overdueAmount = overdueAmount;
	}

	public String getOverdueAmount(){
		return overdueAmount;
	}

	public void setLoanType(String loanType){
		this.loanType = loanType;
	}

	public String getLoanType(){
		return loanType;
	}

	public void setMaturityDate(String maturityDate){
		this.maturityDate = maturityDate;
	}

	public String getMaturityDate(){
		return maturityDate;
	}

	public void setDeviceActiveTill(String deviceActiveTill){
		this.deviceActiveTill = deviceActiveTill;
	}

	public String getDeviceActiveTill(){
		return deviceActiveTill;
	}

	public void setInstalmentAmount(String instalmentAmount){
		this.instalmentAmount = instalmentAmount;
	}

	public String getInstalmentAmount(){
		return instalmentAmount;
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

	public void setDisbursementDate(String disbursementDate){
		this.disbursementDate = disbursementDate;
	}

	public String getDisbursementDate(){
		return disbursementDate;
	}

	public void setRemainingBalance(String remainingBalance){
		this.remainingBalance = remainingBalance;
	}

	public String getRemainingBalance(){
		return remainingBalance;
	}

	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public void setTotalRepayment(String totalRepayment){
		this.totalRepayment = totalRepayment;
	}

	public String getTotalRepayment(){
		return totalRepayment;
	}
}