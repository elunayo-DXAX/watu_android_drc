
package com.watutz.app.objects;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result_code",
    "message",
    "data"
})
public class ImageObject {

    @JsonProperty("result_code")
    private Integer result_code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Image data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ImageObject() {
    }

    /**
     * 
     * @param message
     * @param result_code
     * @param data
     */
    public ImageObject(Integer result_code, String message, Image data) {
        super();
        this.result_code = result_code;
        this.message = message;
        this.data = data;
    }

    @JsonProperty("result_code")
    public Integer getResult_code() {
        return result_code;
    }

    @JsonProperty("result_code")
    public void setResult_code(Integer result_code) {
        this.result_code = result_code;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public Image getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Image data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
