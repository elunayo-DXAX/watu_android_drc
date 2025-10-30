
package com.watutz.app.util.objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "path",
    "thumbnail_path",
    "user_id",
    "updated_at",
    "created_at",
    "id"
})
public class Image {

    @JsonProperty("name")
    private String name;
    @JsonProperty("path")
    private String path;
    @JsonProperty("thumbnail_path")
    private String thumbnail_path;
    @JsonProperty("user_id")
    private Integer user_id;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("id")
    private Integer id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Image() {
    }

    /**
     * 
     * @param id
     * @param updated_at
     * @param thumbnail_path
     * @param name
     * @param path
     * @param created_at
     * @param user_id
     */
    public Image(String name, String path, String thumbnail_path, Integer user_id, String updated_at, String created_at, Integer id) {
        super();
        this.name = name;
        this.path = path;
        this.thumbnail_path = thumbnail_path;
        this.user_id = user_id;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("thumbnail_path")
    public String getThumbnail_path() {
        return thumbnail_path;
    }

    @JsonProperty("thumbnail_path")
    public void setThumbnail_path(String thumbnail_path) {
        this.thumbnail_path = thumbnail_path;
    }

    @JsonProperty("user_id")
    public Integer getUser_id() {
        return user_id;
    }

    @JsonProperty("user_id")
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @JsonProperty("updated_at")
    public String getUpdated_at() {
        return updated_at;
    }

    @JsonProperty("updated_at")
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @JsonProperty("created_at")
    public String getCreated_at() {
        return created_at;
    }

    @JsonProperty("created_at")
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
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
