
package com.watudrc.app.objects;

import java.util.List;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vars implements Parcelable
{

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("items")
    @Expose
    private Integer items;
    @SerializedName("outset")
    @Expose
    private Integer outset;
    @SerializedName("size")
    @Expose
    private List<Integer> size = null;
    @SerializedName("page_param")
    @Expose
    private String page_param;
    @SerializedName("params")
    @Expose
    private Params params;
    @SerializedName("anchor")
    @Expose
    private String anchor;
    @SerializedName("link_extra")
    @Expose
    private String link_extra;
    @SerializedName("i18n_key")
    @Expose
    private String i18n_key;
    @SerializedName("cycle")
    @Expose
    private Boolean cycle;
    @SerializedName("metadata")
    @Expose
    private List<String> metadata = null;
    @SerializedName("count")
    @Expose
    private Integer count;
    public final static Creator<Vars> CREATOR = new Creator<Vars>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Vars createFromParcel(android.os.Parcel in) {
            return new Vars(in);
        }

        public Vars[] newArray(int size) {
            return (new Vars[size]);
        }

    }
    ;

    protected Vars(android.os.Parcel in) {
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.items = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.outset = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.size, (Integer.class.getClassLoader()));
        this.page_param = ((String) in.readValue((String.class.getClassLoader())));
        this.params = ((Params) in.readValue((Params.class.getClassLoader())));
        this.anchor = ((String) in.readValue((String.class.getClassLoader())));
        this.link_extra = ((String) in.readValue((String.class.getClassLoader())));
        this.i18n_key = ((String) in.readValue((String.class.getClassLoader())));
        this.cycle = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.metadata, (String.class.getClassLoader()));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Vars() {
    }

    /**
     * 
     * @param metadata
     * @param size
     * @param outset
     * @param link_extra
     * @param anchor
     * @param count
     * @param i18n_key
     * @param page
     * @param params
     * @param items
     * @param cycle
     * @param page_param
     */
    public Vars(Integer page, Integer items, Integer outset, List<Integer> size, String page_param, Params params, String anchor, String link_extra, String i18n_key, Boolean cycle, List<String> metadata, Integer count) {
        super();
        this.page = page;
        this.items = items;
        this.outset = outset;
        this.size = size;
        this.page_param = page_param;
        this.params = params;
        this.anchor = anchor;
        this.link_extra = link_extra;
        this.i18n_key = i18n_key;
        this.cycle = cycle;
        this.metadata = metadata;
        this.count = count;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public Integer getOutset() {
        return outset;
    }

    public void setOutset(Integer outset) {
        this.outset = outset;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

    public String getPage_param() {
        return page_param;
    }

    public void setPage_param(String page_param) {
        this.page_param = page_param;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getLink_extra() {
        return link_extra;
    }

    public void setLink_extra(String link_extra) {
        this.link_extra = link_extra;
    }

    public String getI18n_key() {
        return i18n_key;
    }

    public void setI18n_key(String i18n_key) {
        this.i18n_key = i18n_key;
    }

    public Boolean getCycle() {
        return cycle;
    }

    public void setCycle(Boolean cycle) {
        this.cycle = cycle;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Vars.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("page");
        sb.append('=');
        sb.append(((this.page == null)?"<null>":this.page));
        sb.append(',');
        sb.append("items");
        sb.append('=');
        sb.append(((this.items == null)?"<null>":this.items));
        sb.append(',');
        sb.append("outset");
        sb.append('=');
        sb.append(((this.outset == null)?"<null>":this.outset));
        sb.append(',');
        sb.append("size");
        sb.append('=');
        sb.append(((this.size == null)?"<null>":this.size));
        sb.append(',');
        sb.append("page_param");
        sb.append('=');
        sb.append(((this.page_param == null)?"<null>":this.page_param));
        sb.append(',');
        sb.append("params");
        sb.append('=');
        sb.append(((this.params == null)?"<null>":this.params));
        sb.append(',');
        sb.append("anchor");
        sb.append('=');
        sb.append(((this.anchor == null)?"<null>":this.anchor));
        sb.append(',');
        sb.append("link_extra");
        sb.append('=');
        sb.append(((this.link_extra == null)?"<null>":this.link_extra));
        sb.append(',');
        sb.append("i18n_key");
        sb.append('=');
        sb.append(((this.i18n_key == null)?"<null>":this.i18n_key));
        sb.append(',');
        sb.append("cycle");
        sb.append('=');
        sb.append(((this.cycle == null)?"<null>":this.cycle));
        sb.append(',');
        sb.append("metadata");
        sb.append('=');
        sb.append(((this.metadata == null)?"<null>":this.metadata));
        sb.append(',');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeValue(items);
        dest.writeValue(outset);
        dest.writeList(size);
        dest.writeValue(page_param);
        dest.writeValue(params);
        dest.writeValue(anchor);
        dest.writeValue(link_extra);
        dest.writeValue(i18n_key);
        dest.writeValue(cycle);
        dest.writeList(metadata);
        dest.writeValue(count);
    }

    public int describeContents() {
        return  0;
    }

}
