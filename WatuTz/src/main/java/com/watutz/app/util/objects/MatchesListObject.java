
package com.watutz.app.util.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatchesListObject {

    @SerializedName("result_code")
    @Expose
    private String result_code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Date> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MatchesListObject() {
    }

    /**
     *
     * @param message
     * @param result_code
     * @param data
     */
    public MatchesListObject(String result_code, String message, List<Date> data) {
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

    public List<Date> getData() {
        return data;
    }

    public void setData(List<Date> data) {
        this.data = data;
    }

}
