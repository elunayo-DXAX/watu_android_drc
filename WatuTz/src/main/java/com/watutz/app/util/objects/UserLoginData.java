
package com.watutz.app.util.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginData implements Parcelable
{

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("token")
    @Expose
    private String token;
    public final static Creator<UserLoginData> CREATOR = new Creator<UserLoginData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserLoginData createFromParcel(Parcel in) {
            return new UserLoginData(in);
        }

        public UserLoginData[] newArray(int size) {
            return (new UserLoginData[size]);
        }

    }
    ;

    protected UserLoginData(Parcel in) {
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        this.token = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserLoginData() {
    }

    /**
     * 
     * @param user
     * @param token
     */
    public UserLoginData(User user, String token) {
        super();
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user);
        dest.writeValue(token);
    }

    public int describeContents() {
        return  0;
    }

}
