package com.watutz.app.objects;

import java.util.List;
import com.google.gson.annotations.SerializedName;

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