
package com.watutz.app.util.objects;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary implements Parcelable
{

    @SerializedName("is_repaying_loan")
    @Expose
    private Boolean is_repaying_loan;
    @SerializedName("loan_amount")
    @Expose
    private Double loan_amount;
    @SerializedName("amount_paid")
    @Expose
    private Integer amount_paid;
    @SerializedName("remaining_amount")
    @Expose
    private Double remaining_amount;
    public final static Creator<Summary> CREATOR = new Creator<Summary>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Summary createFromParcel(android.os.Parcel in) {
            return new Summary(in);
        }

        public Summary[] newArray(int size) {
            return (new Summary[size]);
        }

    }
    ;

    protected Summary(android.os.Parcel in) {
        this.is_repaying_loan = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.loan_amount = ((Double) in.readValue((Double.class.getClassLoader())));
        this.amount_paid = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.remaining_amount = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Summary() {
    }

    /**
     * 
     * @param amount_paid
     * @param remaining_amount
     * @param is_repaying_loan
     * @param loan_amount
     */
    public Summary(Boolean is_repaying_loan, Double loan_amount, Integer amount_paid, Double remaining_amount) {
        super();
        this.is_repaying_loan = is_repaying_loan;
        this.loan_amount = loan_amount;
        this.amount_paid = amount_paid;
        this.remaining_amount = remaining_amount;
    }

    public Boolean getIs_repaying_loan() {
        return is_repaying_loan;
    }

    public void setIs_repaying_loan(Boolean is_repaying_loan) {
        this.is_repaying_loan = is_repaying_loan;
    }

    public Double getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(Double loan_amount) {
        this.loan_amount = loan_amount;
    }

    public Integer getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(Integer amount_paid) {
        this.amount_paid = amount_paid;
    }

    public Double getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(Double remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Summary.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("is_repaying_loan");
        sb.append('=');
        sb.append(((this.is_repaying_loan == null)?"<null>":this.is_repaying_loan));
        sb.append(',');
        sb.append("loan_amount");
        sb.append('=');
        sb.append(((this.loan_amount == null)?"<null>":this.loan_amount));
        sb.append(',');
        sb.append("amount_paid");
        sb.append('=');
        sb.append(((this.amount_paid == null)?"<null>":this.amount_paid));
        sb.append(',');
        sb.append("remaining_amount");
        sb.append('=');
        sb.append(((this.remaining_amount == null)?"<null>":this.remaining_amount));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(is_repaying_loan);
        dest.writeValue(loan_amount);
        dest.writeValue(amount_paid);
        dest.writeValue(remaining_amount);
    }

    public int describeContents() {
        return  0;
    }

}
