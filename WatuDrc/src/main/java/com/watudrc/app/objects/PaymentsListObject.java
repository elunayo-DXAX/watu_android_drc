package com.watudrc.app.objects;

import java.util.List;
import com.google.gson.annotations.SerializedName;

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