
package com.watutz.app.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Need {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("home_id")
    @Expose
    private Integer home_id;
    @SerializedName("need_id")
    @Expose
    private Integer need_id;
    @SerializedName("need_name")
    @Expose
    private String need_name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Need() {
    }

    /**
     * 
     * @param id
     * @param updated_at
     * @param need_id
     * @param status
     * @param description
     * @param created_at
     * @param need_name
     * @param home_id
     */
    public Need(Integer id, Integer home_id, Integer need_id, String need_name, String status, String description, String created_at, String updated_at) {
        super();
        this.id = id;
        this.home_id = home_id;
        this.need_id = need_id;
        this.need_name = need_name;
        this.status = status;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHome_id() {
        return home_id;
    }

    public void setHome_id(Integer home_id) {
        this.home_id = home_id;
    }

    public Integer getNeed_id() {
        return need_id;
    }

    public void setNeed_id(Integer need_id) {
        this.need_id = need_id;
    }

    public String getNeed_name() {
        return need_name;
    }

    public void setNeed_name(String need_name) {
        this.need_name = need_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
