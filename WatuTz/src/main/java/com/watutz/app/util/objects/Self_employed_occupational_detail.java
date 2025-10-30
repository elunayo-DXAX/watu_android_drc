
package com.watutz.app.util.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Self_employed_occupational_detail implements Parcelable
{

    @SerializedName("business_type")
    @Expose
    private String business_type;
    @SerializedName("business_name")
    @Expose
    private String business_name;
    @SerializedName("business_location")
    @Expose
    private String business_location;
    @SerializedName("business_payment_type")
    @Expose
    private String business_payment_type;
    @SerializedName("gross_monthly_revenue")
    @Expose
    private Double gross_monthly_revenue;
    @SerializedName("average_monthly_expenses")
    @Expose
    private Double average_monthly_expenses;
    @SerializedName("average_monthly_personal_expense")
    @Expose
    private Double average_monthly_personal_expense;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("customer_id")
    @Expose
    private String customer_id;
    @SerializedName("business_industry")
    @Expose
    private String business_industry;
    @SerializedName("business_registered")
    @Expose
    private Boolean business_registered;
    public final static Creator<Self_employed_occupational_detail> CREATOR = new Creator<Self_employed_occupational_detail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Self_employed_occupational_detail createFromParcel(Parcel in) {
            return new Self_employed_occupational_detail(in);
        }

        public Self_employed_occupational_detail[] newArray(int size) {
            return (new Self_employed_occupational_detail[size]);
        }

    }
    ;

    protected Self_employed_occupational_detail(Parcel in) {
        this.business_type = ((String) in.readValue((String.class.getClassLoader())));
        this.business_name = ((String) in.readValue((String.class.getClassLoader())));
        this.business_location = ((String) in.readValue((String.class.getClassLoader())));
        this.business_payment_type = ((String) in.readValue((String.class.getClassLoader())));
        this.gross_monthly_revenue = ((Double) in.readValue((Double.class.getClassLoader())));
        this.average_monthly_expenses = ((Double) in.readValue((Double.class.getClassLoader())));
        this.average_monthly_personal_expense = ((Double) in.readValue((Double.class.getClassLoader())));
        this.created_at = ((String) in.readValue((String.class.getClassLoader())));
        this.updated_at = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.customer_id = ((String) in.readValue((String.class.getClassLoader())));
        this.business_industry = ((String) in.readValue((String.class.getClassLoader())));
        this.business_registered = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Self_employed_occupational_detail() {
    }

    /**
     * 
     * @param business_name
     * @param business_registered
     * @param created_at
     * @param business_industry
     * @param business_payment_type
     * @param average_monthly_expenses
     * @param updated_at
     * @param business_type
     * @param average_monthly_personal_expense
     * @param id
     * @param customer_id
     * @param gross_monthly_revenue
     * @param business_location
     */
    public Self_employed_occupational_detail(String business_type, String business_name, String business_location, String business_payment_type, Double gross_monthly_revenue, Double average_monthly_expenses, Double average_monthly_personal_expense, String created_at, String updated_at, String id, String customer_id, String business_industry, Boolean business_registered) {
        super();
        this.business_type = business_type;
        this.business_name = business_name;
        this.business_location = business_location;
        this.business_payment_type = business_payment_type;
        this.gross_monthly_revenue = gross_monthly_revenue;
        this.average_monthly_expenses = average_monthly_expenses;
        this.average_monthly_personal_expense = average_monthly_personal_expense;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.id = id;
        this.customer_id = customer_id;
        this.business_industry = business_industry;
        this.business_registered = business_registered;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_location() {
        return business_location;
    }

    public void setBusiness_location(String business_location) {
        this.business_location = business_location;
    }

    public String getBusiness_payment_type() {
        return business_payment_type;
    }

    public void setBusiness_payment_type(String business_payment_type) {
        this.business_payment_type = business_payment_type;
    }

    public Double getGross_monthly_revenue() {
        return gross_monthly_revenue;
    }

    public void setGross_monthly_revenue(Double gross_monthly_revenue) {
        this.gross_monthly_revenue = gross_monthly_revenue;
    }

    public Double getAverage_monthly_expenses() {
        return average_monthly_expenses;
    }

    public void setAverage_monthly_expenses(Double average_monthly_expenses) {
        this.average_monthly_expenses = average_monthly_expenses;
    }

    public Double getAverage_monthly_personal_expense() {
        return average_monthly_personal_expense;
    }

    public void setAverage_monthly_personal_expense(Double average_monthly_personal_expense) {
        this.average_monthly_personal_expense = average_monthly_personal_expense;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getBusiness_industry() {
        return business_industry;
    }

    public void setBusiness_industry(String business_industry) {
        this.business_industry = business_industry;
    }

    public Boolean getBusiness_registered() {
        return business_registered;
    }

    public void setBusiness_registered(Boolean business_registered) {
        this.business_registered = business_registered;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(business_type);
        dest.writeValue(business_name);
        dest.writeValue(business_location);
        dest.writeValue(business_payment_type);
        dest.writeValue(gross_monthly_revenue);
        dest.writeValue(average_monthly_expenses);
        dest.writeValue(average_monthly_personal_expense);
        dest.writeValue(created_at);
        dest.writeValue(updated_at);
        dest.writeValue(id);
        dest.writeValue(customer_id);
        dest.writeValue(business_industry);
        dest.writeValue(business_registered);
    }

    public int describeContents() {
        return  0;
    }

}
