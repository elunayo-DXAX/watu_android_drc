package com.watutz.app.objects;

import com.google.gson.annotations.SerializedName;

public class Payment {

	@SerializedName("amount")
	private int amount;

	@SerializedName("paymentTypeName")
	private String paymentTypeName;

	@SerializedName("accountNumber")
	private String accountNumber;

	@SerializedName("trxId")
	private String trxId;

	@SerializedName("paymentInstId")
	private int paymentInstId;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("paymentTypeId")
	private int paymentTypeId;

	@SerializedName("paymentInstName")
	private String paymentInstName;

	@SerializedName("paymentDetails")
	private String paymentDetails;

	@SerializedName("paymentDate")
	private long paymentDate;

	@SerializedName("currencyCode")
	private String currencyCode;

	@SerializedName("loanProductId")
	private int loanProductId;

	@SerializedName("loanProductName")
	private String loanProductName;

	public void setAmount(int amount){
		this.amount = amount;
	}

	public int getAmount(){
		return amount;
	}

	public void setPaymentTypeName(String paymentTypeName){
		this.paymentTypeName = paymentTypeName;
	}

	public String getPaymentTypeName(){
		return paymentTypeName;
	}

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public void setTrxId(String trxId){
		this.trxId = trxId;
	}

	public String getTrxId(){
		return trxId;
	}

	public void setPaymentInstId(int paymentInstId){
		this.paymentInstId = paymentInstId;
	}

	public int getPaymentInstId(){
		return paymentInstId;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setPaymentTypeId(int paymentTypeId){
		this.paymentTypeId = paymentTypeId;
	}

	public int getPaymentTypeId(){
		return paymentTypeId;
	}

	public void setPaymentInstName(String paymentInstName){
		this.paymentInstName = paymentInstName;
	}

	public String getPaymentInstName(){
		return paymentInstName;
	}

	public void setPaymentDetails(String paymentDetails){
		this.paymentDetails = paymentDetails;
	}

	public String getPaymentDetails(){
		return paymentDetails;
	}

	public void setPaymentDate(long paymentDate){
		this.paymentDate = paymentDate;
	}

	public long getPaymentDate(){
		return paymentDate;
	}

	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public void setLoanProductId(int loanProductId){
		this.loanProductId = loanProductId;
	}

	public int getLoanProductId(){
		return loanProductId;
	}

	public void setLoanProductName(String loanProductName){
		this.loanProductName = loanProductName;
	}

	public String getLoanProductName(){
		return loanProductName;
	}
}