
package com.watutz.app.util.objects;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pagy implements Parcelable
{

    @SerializedName("scaffold_url")
    @Expose
    private String scaffold_url;
    @SerializedName("first_url")
    @Expose
    private String first_url;
    @SerializedName("prev_url")
    @Expose
    private String prev_url;
    @SerializedName("page_url")
    @Expose
    private String page_url;
    @SerializedName("next_url")
    @Expose
    private String next_url;
    @SerializedName("last_url")
    @Expose
    private String last_url;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("items")
    @Expose
    private Integer items;
    @SerializedName("vars")
    @Expose
    private Vars vars;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("last")
    @Expose
    private Integer last;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("prev")
    @Expose
    private String prev;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("series")
    @Expose
    private List<String> series = null;
    public final static Creator<Pagy> CREATOR = new Creator<Pagy>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Pagy createFromParcel(android.os.Parcel in) {
            return new Pagy(in);
        }

        public Pagy[] newArray(int size) {
            return (new Pagy[size]);
        }

    }
    ;

    protected Pagy(android.os.Parcel in) {
        this.scaffold_url = ((String) in.readValue((String.class.getClassLoader())));
        this.first_url = ((String) in.readValue((String.class.getClassLoader())));
        this.prev_url = ((String) in.readValue((String.class.getClassLoader())));
        this.page_url = ((String) in.readValue((String.class.getClassLoader())));
        this.next_url = ((String) in.readValue((String.class.getClassLoader())));
        this.last_url = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.items = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vars = ((Vars) in.readValue((Vars.class.getClassLoader())));
        this.pages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.last = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.from = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.to = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.prev = ((String) in.readValue((String.class.getClassLoader())));
        this.next = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.series, (String.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Pagy() {
    }

    /**
     * 
     * @param page_url
     * @param next
     * @param last
     * @param prev
     * @param next_url
     * @param count
     * @param prev_url
     * @param last_url
     * @param first_url
     * @param pages
     * @param series
     * @param from
     * @param page
     * @param vars
     * @param to
     * @param scaffold_url
     * @param items
     */
    public Pagy(String scaffold_url, String first_url, String prev_url, String page_url, String next_url, String last_url, Integer count, Integer page, Integer items, Vars vars, Integer pages, Integer last, Integer from, Integer to, String prev, String next, List<String> series) {
        super();
        this.scaffold_url = scaffold_url;
        this.first_url = first_url;
        this.prev_url = prev_url;
        this.page_url = page_url;
        this.next_url = next_url;
        this.last_url = last_url;
        this.count = count;
        this.page = page;
        this.items = items;
        this.vars = vars;
        this.pages = pages;
        this.last = last;
        this.from = from;
        this.to = to;
        this.prev = prev;
        this.next = next;
        this.series = series;
    }

    public String getScaffold_url() {
        return scaffold_url;
    }

    public void setScaffold_url(String scaffold_url) {
        this.scaffold_url = scaffold_url;
    }

    public String getFirst_url() {
        return first_url;
    }

    public void setFirst_url(String first_url) {
        this.first_url = first_url;
    }

    public String getPrev_url() {
        return prev_url;
    }

    public void setPrev_url(String prev_url) {
        this.prev_url = prev_url;
    }

    public String getPage_url() {
        return page_url;
    }

    public void setPage_url(String page_url) {
        this.page_url = page_url;
    }

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public String getLast_url() {
        return last_url;
    }

    public void setLast_url(String last_url) {
        this.last_url = last_url;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
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

    public Vars getVars() {
        return vars;
    }

    public void setVars(Vars vars) {
        this.vars = vars;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Pagy.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("scaffold_url");
        sb.append('=');
        sb.append(((this.scaffold_url == null)?"<null>":this.scaffold_url));
        sb.append(',');
        sb.append("first_url");
        sb.append('=');
        sb.append(((this.first_url == null)?"<null>":this.first_url));
        sb.append(',');
        sb.append("prev_url");
        sb.append('=');
        sb.append(((this.prev_url == null)?"<null>":this.prev_url));
        sb.append(',');
        sb.append("page_url");
        sb.append('=');
        sb.append(((this.page_url == null)?"<null>":this.page_url));
        sb.append(',');
        sb.append("next_url");
        sb.append('=');
        sb.append(((this.next_url == null)?"<null>":this.next_url));
        sb.append(',');
        sb.append("last_url");
        sb.append('=');
        sb.append(((this.last_url == null)?"<null>":this.last_url));
        sb.append(',');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        sb.append("page");
        sb.append('=');
        sb.append(((this.page == null)?"<null>":this.page));
        sb.append(',');
        sb.append("items");
        sb.append('=');
        sb.append(((this.items == null)?"<null>":this.items));
        sb.append(',');
        sb.append("vars");
        sb.append('=');
        sb.append(((this.vars == null)?"<null>":this.vars));
        sb.append(',');
        sb.append("pages");
        sb.append('=');
        sb.append(((this.pages == null)?"<null>":this.pages));
        sb.append(',');
        sb.append("last");
        sb.append('=');
        sb.append(((this.last == null)?"<null>":this.last));
        sb.append(',');
        sb.append("from");
        sb.append('=');
        sb.append(((this.from == null)?"<null>":this.from));
        sb.append(',');
        sb.append("to");
        sb.append('=');
        sb.append(((this.to == null)?"<null>":this.to));
        sb.append(',');
        sb.append("prev");
        sb.append('=');
        sb.append(((this.prev == null)?"<null>":this.prev));
        sb.append(',');
        sb.append("next");
        sb.append('=');
        sb.append(((this.next == null)?"<null>":this.next));
        sb.append(',');
        sb.append("series");
        sb.append('=');
        sb.append(((this.series == null)?"<null>":this.series));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(scaffold_url);
        dest.writeValue(first_url);
        dest.writeValue(prev_url);
        dest.writeValue(page_url);
        dest.writeValue(next_url);
        dest.writeValue(last_url);
        dest.writeValue(count);
        dest.writeValue(page);
        dest.writeValue(items);
        dest.writeValue(vars);
        dest.writeValue(pages);
        dest.writeValue(last);
        dest.writeValue(from);
        dest.writeValue(to);
        dest.writeValue(prev);
        dest.writeValue(next);
        dest.writeList(series);
    }

    public int describeContents() {
        return  0;
    }

}
