
package com.watutz.app.objects;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResetPasswordObject implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("errors")
    @Expose
    private List<String> errors = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("reset_password_token")
    @Expose
    private String reset_password_token;
    public final static Parcelable.Creator<UserResetPasswordObject> CREATOR = new Creator<UserResetPasswordObject>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserResetPasswordObject createFromParcel(Parcel in) {
            return new UserResetPasswordObject(in);
        }

        public UserResetPasswordObject[] newArray(int size) {
            return (new UserResetPasswordObject[size]);
        }

    }
    ;

    protected UserResetPasswordObject(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.errors, (java.lang.String.class.getClassLoader()));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.reset_password_token = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserResetPasswordObject() {
    }

    /**
     * 
     * @param success
     * @param message
     * @param errors
     * @param reset_password_token
     */
    public UserResetPasswordObject(Boolean success, List<String> errors, String message, String reset_password_token) {
        super();
        this.success = success;
        this.errors = errors;
        this.message = message;
        this.reset_password_token = reset_password_token;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReset_password_token() {
        return reset_password_token;
    }

    public void setReset_password_token(String reset_password_token) {
        this.reset_password_token = reset_password_token;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeList(errors);
        dest.writeValue(message);
        dest.writeValue(reset_password_token);
    }

    public int describeContents() {
        return  0;
    }

}
