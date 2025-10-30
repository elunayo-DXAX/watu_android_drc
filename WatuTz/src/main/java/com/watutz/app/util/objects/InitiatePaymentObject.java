package com.watutz.app.util.objects;

import com.google.gson.annotations.SerializedName;

public class InitiatePaymentObject{

	@SerializedName("success")
	private boolean success;

	@SerializedName("created")
	private String created;

	@SerializedName("paymentRequestId")
	private long paymentRequestId;

	@SerializedName("id")
	private long id;

	public void setId(long id){
		this.id = id;
	}

	public long getId(){
		return id;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setPaymentRequestId(long paymentRequestId){
		this.paymentRequestId = paymentRequestId;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}


	public long getPaymentRequestId(){
		return paymentRequestId;
	}
}