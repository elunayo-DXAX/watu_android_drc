package com.watudrc.app.objects;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AccountsItem{

	@SerializedName("paymentProvider")
	private String paymentProvider;

	@SerializedName("accountNumber")
	private String accountNumber;

	@SerializedName("list")
	private List<ListItem> list;

	public void setPaymentProvider(String paymentProvider){
		this.paymentProvider = paymentProvider;
	}

	public String getPaymentProvider(){
		return paymentProvider;
	}

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public void setList(List<ListItem> list){
		this.list = list;
	}

	public List<ListItem> getList(){
		return list;
	}
}