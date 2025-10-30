
package com.watutz.app.util.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserLoginObject implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("status")
    @Expose
    private Integer status;


    @SerializedName("errors")
    @Expose
    private List<String> errors = null;
    @SerializedName("data")
    @Expose
    private User data;
    public final static Creator<UserLoginObject> CREATOR = new Creator<UserLoginObject>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserLoginObject createFromParcel(Parcel in) {
            return new UserLoginObject(in);
        }

        public UserLoginObject[] newArray(int size) {
            return (new UserLoginObject[size]);
        }

    }
    ;

    protected UserLoginObject(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.errors, (String.class.getClassLoader()));
        this.data = ((User) in.readValue((User.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserLoginObject() {
    }

    /**
     * 
     * @param data
     * @param success
     * @param errors
     */
    public UserLoginObject(Boolean success,Integer status, List<String> errors, User data) {
        super();
        this.success = success;
        this.status = status;
        this.errors = errors;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(status);
        dest.writeList(errors);
        dest.writeValue(data);
    }

    public int describeContents() {
        return  0;
    }

}
