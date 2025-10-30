
package com.watutz.app.util.objects;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurchasesListObject implements Parcelable
{

    @SerializedName("data")
    @Expose
    private List<Purchase> data = null;
    @SerializedName("pagy")
    @Expose
    private Pagy pagy;
    public final static Creator<PurchasesListObject> CREATOR = new Creator<PurchasesListObject>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PurchasesListObject createFromParcel(android.os.Parcel in) {
            return new PurchasesListObject(in);
        }

        public PurchasesListObject[] newArray(int size) {
            return (new PurchasesListObject[size]);
        }

    }
    ;

    protected PurchasesListObject(android.os.Parcel in) {
        in.readList(this.data, (Purchase.class.getClassLoader()));
        this.pagy = ((Pagy) in.readValue((Pagy.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public PurchasesListObject() {
    }

    /**
     * 
     * @param data
     * @param pagy
     */
    public PurchasesListObject(List<Purchase> data, Pagy pagy) {
        super();
        this.data = data;
        this.pagy = pagy;
    }

    public List<Purchase> getData() {
        return data;
    }

    public void setData(List<Purchase> data) {
        this.data = data;
    }

    public Pagy getPagy() {
        return pagy;
    }

    public void setPagy(Pagy pagy) {
        this.pagy = pagy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PurchasesListObject.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null)?"<null>":this.data));
        sb.append(',');
        sb.append("pagy");
        sb.append('=');
        sb.append(((this.pagy == null)?"<null>":this.pagy));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(data);
        dest.writeValue(pagy);
    }

    public int describeContents() {
        return  0;
    }

}
