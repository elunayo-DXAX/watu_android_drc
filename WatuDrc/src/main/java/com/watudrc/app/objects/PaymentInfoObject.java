package com.watudrc.app.objects;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PaymentInfoObject{

	@SerializedName("accounts")
	private List<AccountsItem> accounts;

	public void setAccounts(List<AccountsItem> accounts){
		this.accounts = accounts;
	}

	public List<AccountsItem> getAccounts(){
		return accounts;
	}
}