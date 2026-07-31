package com.watudrc.app.objects;

import com.google.gson.annotations.SerializedName;

public class InitiatePaymentObject{

	@SerializedName("success")
	private boolean success;

	@SerializedName("paymentRequestId")
	private long paymentRequestId;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setPaymentRequestId(long paymentRequestId){
		this.paymentRequestId = paymentRequestId;
	}

	public long getPaymentRequestId(){
		return paymentRequestId;
	}
}