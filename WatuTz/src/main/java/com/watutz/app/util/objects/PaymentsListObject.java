package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentsListObject{

	@SerializedName("transactionList")
	private List<Payment> transactionList;

	@SerializedName("actualDate")
	private long actualDate;

	public void setTransactionList(List<Payment> transactionList){
		this.transactionList = transactionList;
	}

	public List<Payment> getTransactionList(){
		return transactionList;
	}

	public void setActualDate(long actualDate){
		this.actualDate = actualDate;
	}

	public long getActualDate(){
		return actualDate;
	}
}