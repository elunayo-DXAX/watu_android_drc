package com.watutz.app.objects;

import com.google.gson.annotations.SerializedName;

public class LoanListItemObject {

	@SerializedName("maturityDate")
	private long maturityDate;

	@SerializedName("disbursementDate")
	private long disbursementDate;

	@SerializedName("loanNumber")
	private String loanNumber;

	@SerializedName("loanProductName")
	private String loanProductName;

	@SerializedName("loanProductId")
	private int loanProductId;

	public void setMaturityDate(long maturityDate){
		this.maturityDate = maturityDate;
	}

	public long getMaturityDate(){
		return maturityDate;
	}

	public void setDisbursementDate(long disbursementDate){
		this.disbursementDate = disbursementDate;
	}

	public long getDisbursementDate(){
		return disbursementDate;
	}

	public void setLoanNumber(String loanNumber){
		this.loanNumber = loanNumber;
	}

	public String getLoanNumber(){
		return loanNumber;
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
}