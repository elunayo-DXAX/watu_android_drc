
package com.watutz.app.util.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Credit_limit_detail implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("available_limit")
    @Expose
    private Double available_limit;
    @SerializedName("credit_limit")
    @Expose
    private Double credit_limit;
    @SerializedName("credit_score_report")
    @Expose
    private String credit_score_report;
    @SerializedName("credit_score_notes")
    @Expose
    private String credit_score_notes;
    @SerializedName("credit_limit_status")
    @Expose
    private String credit_limit_status;
    @SerializedName("credit_option")
    @Expose
    private String credit_option;
    @SerializedName("approval_type")
    @Expose
    private String approval_type;
    @SerializedName("customer_id")
    @Expose
    private String customer_id;
    @SerializedName("approved_at")
    @Expose
    private String approved_at;
    @SerializedName("limit_activated_at")
    @Expose
    private String limit_activated_at;
    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    public final static Creator<Credit_limit_detail> CREATOR = new Creator<Credit_limit_detail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Credit_limit_detail createFromParcel(Parcel in) {
            return new Credit_limit_detail(in);
        }

        public Credit_limit_detail[] newArray(int size) {
            return (new Credit_limit_detail[size]);
        }

    }
    ;

    protected Credit_limit_detail(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.available_limit = ((Double) in.readValue((Double.class.getClassLoader())));
        this.credit_limit = ((Double) in.readValue((Double.class.getClassLoader())));
        this.credit_score_report = ((String) in.readValue((String.class.getClassLoader())));
        this.credit_score_notes = ((String) in.readValue((String.class.getClassLoader())));
        this.credit_limit_status = ((String) in.readValue((String.class.getClassLoader())));
        this.credit_option = ((String) in.readValue((String.class.getClassLoader())));
        this.approval_type = ((String) in.readValue((String.class.getClassLoader())));
        this.customer_id = ((String) in.readValue((String.class.getClassLoader())));
        this.approved_at = ((String) in.readValue((String.class.getClassLoader())));
        this.limit_activated_at = ((String) in.readValue((String.class.getClassLoader())));
        this.deleted_at = ((String) in.readValue((String.class.getClassLoader())));
        this.created_at = ((String) in.readValue((String.class.getClassLoader())));
        this.updated_at = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Credit_limit_detail() {
    }

    /**
     * 
     * @param limit_activated_at
     * @param approval_type
     * @param created_at
     * @param deleted_at
     * @param credit_limit_status
     * @param available_limit
     * @param credit_score_notes
     * @param updated_at
     * @param approved_at
     * @param credit_limit
     * @param id
     * @param customer_id
     * @param credit_score_report
     * @param credit_option
     */
    public Credit_limit_detail(String id, Double available_limit, Double credit_limit, String credit_score_report, String credit_score_notes, String credit_limit_status, String credit_option, String approval_type, String customer_id, String approved_at, String limit_activated_at, String deleted_at, String created_at, String updated_at) {
        super();
        this.id = id;
        this.available_limit = available_limit;
        this.credit_limit = credit_limit;
        this.credit_score_report = credit_score_report;
        this.credit_score_notes = credit_score_notes;
        this.credit_limit_status = credit_limit_status;
        this.credit_option = credit_option;
        this.approval_type = approval_type;
        this.customer_id = customer_id;
        this.approved_at = approved_at;
        this.limit_activated_at = limit_activated_at;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAvailable_limit() {
        return available_limit;
    }

    public void setAvailable_limit(Double available_limit) {
        this.available_limit = available_limit;
    }

    public Double getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(Double credit_limit) {
        this.credit_limit = credit_limit;
    }

    public String getCredit_score_report() {
        return credit_score_report;
    }

    public void setCredit_score_report(String credit_score_report) {
        this.credit_score_report = credit_score_report;
    }

    public String getCredit_score_notes() {
        return credit_score_notes;
    }

    public void setCredit_score_notes(String credit_score_notes) {
        this.credit_score_notes = credit_score_notes;
    }

    public String getCredit_limit_status() {
        return credit_limit_status;
    }

    public void setCredit_limit_status(String credit_limit_status) {
        this.credit_limit_status = credit_limit_status;
    }

    public String getCredit_option() {
        return credit_option;
    }

    public void setCredit_option(String credit_option) {
        this.credit_option = credit_option;
    }

    public String getApproval_type() {
        return approval_type;
    }

    public void setApproval_type(String approval_type) {
        this.approval_type = approval_type;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getApproved_at() {
        return approved_at;
    }

    public void setApproved_at(String approved_at) {
        this.approved_at = approved_at;
    }

    public String getLimit_activated_at() {
        return limit_activated_at;
    }

    public void setLimit_activated_at(String limit_activated_at) {
        this.limit_activated_at = limit_activated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(available_limit);
        dest.writeValue(credit_limit);
        dest.writeValue(credit_score_report);
        dest.writeValue(credit_score_notes);
        dest.writeValue(credit_limit_status);
        dest.writeValue(credit_option);
        dest.writeValue(approval_type);
        dest.writeValue(customer_id);
        dest.writeValue(approved_at);
        dest.writeValue(limit_activated_at);
        dest.writeValue(deleted_at);
        dest.writeValue(created_at);
        dest.writeValue(updated_at);
    }

    public int describeContents() {
        return  0;
    }

}
