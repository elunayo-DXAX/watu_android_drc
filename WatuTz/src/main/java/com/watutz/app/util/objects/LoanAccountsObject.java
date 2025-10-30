package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoanAccountsObject{

	@SerializedName("loanList")
	private List<LoanListItemObject> loanList;

	public void setLoanList(List<LoanListItemObject> loanList){
		this.loanList = loanList;
	}

	public List<LoanListItemObject> getLoanList(){
		return loanList;
	}
}