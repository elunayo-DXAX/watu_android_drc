
package com.watutz.app.objects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomesListObject {

    @SerializedName("result_code")
    @Expose
    private String result_code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Home> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public HomesListObject() {
    }

    /**
     * 
     * @param message
     * @param result_code
     * @param data
     */
    public HomesListObject(String result_code, String message, List<Home> data) {
        super();
        this.result_code = result_code;
        this.message = message;
        this.data = data;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Home> getData() {
        return data;
    }

    public void setData(List<Home> data) {
        this.data = data;
    }

}
