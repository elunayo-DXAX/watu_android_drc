
package com.watutz.app.util.objects;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Purchase implements Parcelable
{

    @SerializedName("musoni_loan_id")
    @Expose
    private String musoni_loan_id;
    @SerializedName("principal_amount")
    @Expose
    private String principal_amount;
    @SerializedName("interest_rate")
    @Expose
    private Double interest_rate;
    @SerializedName("loan_duration")
    @Expose
    private Integer loan_duration;
    @SerializedName("facility_status")
    @Expose
    private String facility_status;
    @SerializedName("preferred_option")
    @Expose
    private String preferred_option;
    @SerializedName("facility_description")
    @Expose
    private String facility_description;
    @SerializedName("facility_plan")
    @Expose
    private Integer facility_plan;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("item_type")
    @Expose
    private String item_type;
    @SerializedName("item_brand")
    @Expose
    private String item_brand;
    @SerializedName("item_code")
    @Expose
    private String item_code;
    @SerializedName("store_name")
    @Expose
    private String store_name;
    @SerializedName("item_value")
    @Expose
    private Double item_value;
    @SerializedName("delivery_option")
    @Expose
    private String delivery_option;
    @SerializedName("item_description")
    @Expose
    private String item_description;
    @SerializedName("item_topup")
    @Expose
    private Double item_topup;
    @SerializedName("topup_ref")
    @Expose
    private String topup_ref;
    @SerializedName("upfront_fees")
    @Expose
    private Double upfront_fees;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("accepted_terms")
    @Expose
    private String accepted_terms;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("loan_application_detail_id")
    @Expose
    private String loan_application_detail_id;
    @SerializedName("partner_store_id")
    @Expose
    private String partner_store_id;
    @SerializedName("loan_product_id")
    @Expose
    private String loan_product_id;
    @SerializedName("order_id")
    @Expose
    private String order_id;
    @SerializedName("last_modified_by")
    @Expose
    private String last_modified_by;
    @SerializedName("markup")
    @Expose
    private Double markup;
    public final static Creator<Purchase> CREATOR = new Creator<Purchase>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Purchase createFromParcel(android.os.Parcel in) {
            return new Purchase(in);
        }

        public Purchase[] newArray(int size) {
            return (new Purchase[size]);
        }

    }
    ;

    protected Purchase(android.os.Parcel in) {
        this.musoni_loan_id = ((String) in.readValue((String.class.getClassLoader())));
        this.principal_amount = ((String) in.readValue((String.class.getClassLoader())));
        this.interest_rate = ((Double) in.readValue((Double.class.getClassLoader())));
        this.loan_duration = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.facility_status = ((String) in.readValue((String.class.getClassLoader())));
        this.preferred_option = ((String) in.readValue((String.class.getClassLoader())));
        this.facility_description = ((String) in.readValue((String.class.getClassLoader())));
        this.facility_plan = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.created_at = ((String) in.readValue((String.class.getClassLoader())));
        this.updated_at = ((String) in.readValue((String.class.getClassLoader())));
        this.item_type = ((String) in.readValue((String.class.getClassLoader())));
        this.item_brand = ((String) in.readValue((String.class.getClassLoader())));
        this.item_code = ((String) in.readValue((String.class.getClassLoader())));
        this.store_name = ((String) in.readValue((String.class.getClassLoader())));
        this.item_value = ((Double) in.readValue((Double.class.getClassLoader())));
        this.delivery_option = ((String) in.readValue((String.class.getClassLoader())));
        this.item_description = ((String) in.readValue((String.class.getClassLoader())));
        this.item_topup = ((Double) in.readValue((Double.class.getClassLoader())));
        this.topup_ref = ((String) in.readValue((String.class.getClassLoader())));
        this.upfront_fees = ((Double) in.readValue((Double.class.getClassLoader())));
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this.accepted_terms = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.loan_application_detail_id = ((String) in.readValue((String.class.getClassLoader())));
        this.partner_store_id = ((String) in.readValue((String.class.getClassLoader())));
        this.loan_product_id = ((String) in.readValue((String.class.getClassLoader())));
        this.order_id = ((String) in.readValue((String.class.getClassLoader())));
        this.last_modified_by = ((String) in.readValue((String.class.getClassLoader())));
        this.markup = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Purchase() {
    }

    /**
     * 
     * @param item_code
     * @param musoni_loan_id
     * @param item_type
     * @param facility_status
     * @param created_at
     * @param partner_store_id
     * @param principal_amount
     * @param loan_product_id
     * @param source
     * @param item_value
     * @param facility_plan
     * @param updated_at
     * @param accepted_terms
     * @param facility_description
     * @param topup_ref
     * @param interest_rate
     * @param store_name
     * @param id
     * @param markup
     * @param last_modified_by
     * @param loan_application_detail_id
     * @param item_brand
     * @param loan_duration
     * @param item_topup
     * @param upfront_fees
     * @param item_description
     * @param order_id
     * @param preferred_option
     * @param delivery_option
     */
    public Purchase(String musoni_loan_id, String principal_amount, Double interest_rate, Integer loan_duration, String facility_status, String preferred_option, String facility_description, Integer facility_plan, String created_at, String updated_at, String item_type, String item_brand, String item_code, String store_name, Double item_value, String delivery_option, String item_description, Double item_topup, String topup_ref, Double upfront_fees, String source, String accepted_terms, String id, String loan_application_detail_id, String partner_store_id, String loan_product_id, String order_id, String last_modified_by, Double markup) {
        super();
        this.musoni_loan_id = musoni_loan_id;
        this.principal_amount = principal_amount;
        this.interest_rate = interest_rate;
        this.loan_duration = loan_duration;
        this.facility_status = facility_status;
        this.preferred_option = preferred_option;
        this.facility_description = facility_description;
        this.facility_plan = facility_plan;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.item_type = item_type;
        this.item_brand = item_brand;
        this.item_code = item_code;
        this.store_name = store_name;
        this.item_value = item_value;
        this.delivery_option = delivery_option;
        this.item_description = item_description;
        this.item_topup = item_topup;
        this.topup_ref = topup_ref;
        this.upfront_fees = upfront_fees;
        this.source = source;
        this.accepted_terms = accepted_terms;
        this.id = id;
        this.loan_application_detail_id = loan_application_detail_id;
        this.partner_store_id = partner_store_id;
        this.loan_product_id = loan_product_id;
        this.order_id = order_id;
        this.last_modified_by = last_modified_by;
        this.markup = markup;
    }

    public String getMusoni_loan_id() {
        return musoni_loan_id;
    }

    public void setMusoni_loan_id(String musoni_loan_id) {
        this.musoni_loan_id = musoni_loan_id;
    }

    public String getPrincipal_amount() {
        return principal_amount;
    }

    public void setPrincipal_amount(String principal_amount) {
        this.principal_amount = principal_amount;
    }

    public Double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(Double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public Integer getLoan_duration() {
        return loan_duration;
    }

    public void setLoan_duration(Integer loan_duration) {
        this.loan_duration = loan_duration;
    }

    public String getFacility_status() {
        return facility_status;
    }

    public void setFacility_status(String facility_status) {
        this.facility_status = facility_status;
    }

    public String getPreferred_option() {
        return preferred_option;
    }

    public void setPreferred_option(String preferred_option) {
        this.preferred_option = preferred_option;
    }

    public String getFacility_description() {
        return facility_description;
    }

    public void setFacility_description(String facility_description) {
        this.facility_description = facility_description;
    }

    public Integer getFacility_plan() {
        return facility_plan;
    }

    public void setFacility_plan(Integer facility_plan) {
        this.facility_plan = facility_plan;
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

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_brand() {
        return item_brand;
    }

    public void setItem_brand(String item_brand) {
        this.item_brand = item_brand;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Double getItem_value() {
        return item_value;
    }

    public void setItem_value(Double item_value) {
        this.item_value = item_value;
    }

    public String getDelivery_option() {
        return delivery_option;
    }

    public void setDelivery_option(String delivery_option) {
        this.delivery_option = delivery_option;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public Double getItem_topup() {
        return item_topup;
    }

    public void setItem_topup(Double item_topup) {
        this.item_topup = item_topup;
    }

    public String getTopup_ref() {
        return topup_ref;
    }

    public void setTopup_ref(String topup_ref) {
        this.topup_ref = topup_ref;
    }

    public Double getUpfront_fees() {
        return upfront_fees;
    }

    public void setUpfront_fees(Double upfront_fees) {
        this.upfront_fees = upfront_fees;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAccepted_terms() {
        return accepted_terms;
    }

    public void setAccepted_terms(String accepted_terms) {
        this.accepted_terms = accepted_terms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoan_application_detail_id() {
        return loan_application_detail_id;
    }

    public void setLoan_application_detail_id(String loan_application_detail_id) {
        this.loan_application_detail_id = loan_application_detail_id;
    }

    public String getPartner_store_id() {
        return partner_store_id;
    }

    public void setPartner_store_id(String partner_store_id) {
        this.partner_store_id = partner_store_id;
    }

    public String getLoan_product_id() {
        return loan_product_id;
    }

    public void setLoan_product_id(String loan_product_id) {
        this.loan_product_id = loan_product_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }

    public Double getMarkup() {
        return markup;
    }

    public void setMarkup(Double markup) {
        this.markup = markup;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Purchase.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("musoni_loan_id");
        sb.append('=');
        sb.append(((this.musoni_loan_id == null)?"<null>":this.musoni_loan_id));
        sb.append(',');
        sb.append("principal_amount");
        sb.append('=');
        sb.append(((this.principal_amount == null)?"<null>":this.principal_amount));
        sb.append(',');
        sb.append("interest_rate");
        sb.append('=');
        sb.append(((this.interest_rate == null)?"<null>":this.interest_rate));
        sb.append(',');
        sb.append("loan_duration");
        sb.append('=');
        sb.append(((this.loan_duration == null)?"<null>":this.loan_duration));
        sb.append(',');
        sb.append("facility_status");
        sb.append('=');
        sb.append(((this.facility_status == null)?"<null>":this.facility_status));
        sb.append(',');
        sb.append("preferred_option");
        sb.append('=');
        sb.append(((this.preferred_option == null)?"<null>":this.preferred_option));
        sb.append(',');
        sb.append("facility_description");
        sb.append('=');
        sb.append(((this.facility_description == null)?"<null>":this.facility_description));
        sb.append(',');
        sb.append("facility_plan");
        sb.append('=');
        sb.append(((this.facility_plan == null)?"<null>":this.facility_plan));
        sb.append(',');
        sb.append("created_at");
        sb.append('=');
        sb.append(((this.created_at == null)?"<null>":this.created_at));
        sb.append(',');
        sb.append("updated_at");
        sb.append('=');
        sb.append(((this.updated_at == null)?"<null>":this.updated_at));
        sb.append(',');
        sb.append("item_type");
        sb.append('=');
        sb.append(((this.item_type == null)?"<null>":this.item_type));
        sb.append(',');
        sb.append("item_brand");
        sb.append('=');
        sb.append(((this.item_brand == null)?"<null>":this.item_brand));
        sb.append(',');
        sb.append("item_code");
        sb.append('=');
        sb.append(((this.item_code == null)?"<null>":this.item_code));
        sb.append(',');
        sb.append("store_name");
        sb.append('=');
        sb.append(((this.store_name == null)?"<null>":this.store_name));
        sb.append(',');
        sb.append("item_value");
        sb.append('=');
        sb.append(((this.item_value == null)?"<null>":this.item_value));
        sb.append(',');
        sb.append("delivery_option");
        sb.append('=');
        sb.append(((this.delivery_option == null)?"<null>":this.delivery_option));
        sb.append(',');
        sb.append("item_description");
        sb.append('=');
        sb.append(((this.item_description == null)?"<null>":this.item_description));
        sb.append(',');
        sb.append("item_topup");
        sb.append('=');
        sb.append(((this.item_topup == null)?"<null>":this.item_topup));
        sb.append(',');
        sb.append("topup_ref");
        sb.append('=');
        sb.append(((this.topup_ref == null)?"<null>":this.topup_ref));
        sb.append(',');
        sb.append("upfront_fees");
        sb.append('=');
        sb.append(((this.upfront_fees == null)?"<null>":this.upfront_fees));
        sb.append(',');
        sb.append("source");
        sb.append('=');
        sb.append(((this.source == null)?"<null>":this.source));
        sb.append(',');
        sb.append("accepted_terms");
        sb.append('=');
        sb.append(((this.accepted_terms == null)?"<null>":this.accepted_terms));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("loan_application_detail_id");
        sb.append('=');
        sb.append(((this.loan_application_detail_id == null)?"<null>":this.loan_application_detail_id));
        sb.append(',');
        sb.append("partner_store_id");
        sb.append('=');
        sb.append(((this.partner_store_id == null)?"<null>":this.partner_store_id));
        sb.append(',');
        sb.append("loan_product_id");
        sb.append('=');
        sb.append(((this.loan_product_id == null)?"<null>":this.loan_product_id));
        sb.append(',');
        sb.append("order_id");
        sb.append('=');
        sb.append(((this.order_id == null)?"<null>":this.order_id));
        sb.append(',');
        sb.append("last_modified_by");
        sb.append('=');
        sb.append(((this.last_modified_by == null)?"<null>":this.last_modified_by));
        sb.append(',');
        sb.append("markup");
        sb.append('=');
        sb.append(((this.markup == null)?"<null>":this.markup));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(musoni_loan_id);
        dest.writeValue(principal_amount);
        dest.writeValue(interest_rate);
        dest.writeValue(loan_duration);
        dest.writeValue(facility_status);
        dest.writeValue(preferred_option);
        dest.writeValue(facility_description);
        dest.writeValue(facility_plan);
        dest.writeValue(created_at);
        dest.writeValue(updated_at);
        dest.writeValue(item_type);
        dest.writeValue(item_brand);
        dest.writeValue(item_code);
        dest.writeValue(store_name);
        dest.writeValue(item_value);
        dest.writeValue(delivery_option);
        dest.writeValue(item_description);
        dest.writeValue(item_topup);
        dest.writeValue(topup_ref);
        dest.writeValue(upfront_fees);
        dest.writeValue(source);
        dest.writeValue(accepted_terms);
        dest.writeValue(id);
        dest.writeValue(loan_application_detail_id);
        dest.writeValue(partner_store_id);
        dest.writeValue(loan_product_id);
        dest.writeValue(order_id);
        dest.writeValue(last_modified_by);
        dest.writeValue(markup);
    }

    public int describeContents() {
        return  0;
    }

}
