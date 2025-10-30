
package com.watutz.app.util.objects;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Employed_occupational_detail implements Parcelable
{

    @SerializedName("employer")
    @Expose
    private String employer;
    @SerializedName("net_income")
    @Expose
    private Double net_income;
    @SerializedName("expenses")
    @Expose
    private Double expenses;
    @SerializedName("job_group")
    @Expose
    private String job_group;
    @SerializedName("job_function")
    @Expose
    private String job_function;
    @SerializedName("job_level")
    @Expose
    private String job_level;
    @SerializedName("employer_sector")
    @Expose
    private String employer_sector;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("payment_type")
    @Expose
    private String payment_type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("customer_id")
    @Expose
    private String customer_id;
    public final static Creator<Employed_occupational_detail> CREATOR = new Creator<Employed_occupational_detail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Employed_occupational_detail createFromParcel(android.os.Parcel in) {
            return new Employed_occupational_detail(in);
        }

        public Employed_occupational_detail[] newArray(int size) {
            return (new Employed_occupational_detail[size]);
        }

    }
    ;

    protected Employed_occupational_detail(android.os.Parcel in) {
        this.employer = ((String) in.readValue((String.class.getClassLoader())));
        this.net_income = ((Double) in.readValue((Double.class.getClassLoader())));
        this.expenses = ((Double) in.readValue((Double.class.getClassLoader())));
        this.job_group = ((String) in.readValue((String.class.getClassLoader())));
        this.job_function = ((String) in.readValue((String.class.getClassLoader())));
        this.job_level = ((String) in.readValue((String.class.getClassLoader())));
        this.employer_sector = ((String) in.readValue((String.class.getClassLoader())));
        this.created_at = ((String) in.readValue((String.class.getClassLoader())));
        this.updated_at = ((String) in.readValue((String.class.getClassLoader())));
        this.payment_type = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.customer_id = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Employed_occupational_detail() {
    }

    /**
     * 
     * @param payment_type
     * @param job_function
     * @param updated_at
     * @param job_group
     * @param employer
     * @param created_at
     * @param net_income
     * @param job_level
     * @param id
     * @param customer_id
     * @param expenses
     * @param employer_sector
     */
    public Employed_occupational_detail(String employer, Double net_income, Double expenses, String job_group, String job_function, String job_level, String employer_sector, String created_at, String updated_at, String payment_type, String id, String customer_id) {
        super();
        this.employer = employer;
        this.net_income = net_income;
        this.expenses = expenses;
        this.job_group = job_group;
        this.job_function = job_function;
        this.job_level = job_level;
        this.employer_sector = employer_sector;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.payment_type = payment_type;
        this.id = id;
        this.customer_id = customer_id;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public Double getNet_income() {
        return net_income;
    }

    public void setNet_income(Double net_income) {
        this.net_income = net_income;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }

    public String getJob_group() {
        return job_group;
    }

    public void setJob_group(String job_group) {
        this.job_group = job_group;
    }

    public String getJob_function() {
        return job_function;
    }

    public void setJob_function(String job_function) {
        this.job_function = job_function;
    }

    public String getJob_level() {
        return job_level;
    }

    public void setJob_level(String job_level) {
        this.job_level = job_level;
    }

    public String getEmployer_sector() {
        return employer_sector;
    }

    public void setEmployer_sector(String employer_sector) {
        this.employer_sector = employer_sector;
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

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Employed_occupational_detail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("employer");
        sb.append('=');
        sb.append(((this.employer == null)?"<null>":this.employer));
        sb.append(',');
        sb.append("net_income");
        sb.append('=');
        sb.append(((this.net_income == null)?"<null>":this.net_income));
        sb.append(',');
        sb.append("expenses");
        sb.append('=');
        sb.append(((this.expenses == null)?"<null>":this.expenses));
        sb.append(',');
        sb.append("job_group");
        sb.append('=');
        sb.append(((this.job_group == null)?"<null>":this.job_group));
        sb.append(',');
        sb.append("job_function");
        sb.append('=');
        sb.append(((this.job_function == null)?"<null>":this.job_function));
        sb.append(',');
        sb.append("job_level");
        sb.append('=');
        sb.append(((this.job_level == null)?"<null>":this.job_level));
        sb.append(',');
        sb.append("employer_sector");
        sb.append('=');
        sb.append(((this.employer_sector == null)?"<null>":this.employer_sector));
        sb.append(',');
        sb.append("created_at");
        sb.append('=');
        sb.append(((this.created_at == null)?"<null>":this.created_at));
        sb.append(',');
        sb.append("updated_at");
        sb.append('=');
        sb.append(((this.updated_at == null)?"<null>":this.updated_at));
        sb.append(',');
        sb.append("payment_type");
        sb.append('=');
        sb.append(((this.payment_type == null)?"<null>":this.payment_type));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("customer_id");
        sb.append('=');
        sb.append(((this.customer_id == null)?"<null>":this.customer_id));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(employer);
        dest.writeValue(net_income);
        dest.writeValue(expenses);
        dest.writeValue(job_group);
        dest.writeValue(job_function);
        dest.writeValue(job_level);
        dest.writeValue(employer_sector);
        dest.writeValue(created_at);
        dest.writeValue(updated_at);
        dest.writeValue(payment_type);
        dest.writeValue(id);
        dest.writeValue(customer_id);
    }

    public int describeContents() {
        return  0;
    }

}
