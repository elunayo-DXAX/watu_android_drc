
package com.watudrc.app.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("relationship")
    @Expose
    private String relationship;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("languages")
    @Expose
    private String languages;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    public final static Creator<Profile> CREATOR = new Creator<Profile>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return (new Profile[size]);
        }

    }
    ;

    protected Profile(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.user_id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.surname = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.age = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.profile_picture = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.relationship = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.languages = ((String) in.readValue((String.class.getClassLoader())));
        this.created_at = ((String) in.readValue((String.class.getClassLoader())));
        this.updated_at = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Profile() {
    }

    /**
     * 
     * @param country
     * @param gender
     * @param languages
     * @param description
     * @param created_at
     * @param profile_picture
     * @param updated_at
     * @param user_id
     * @param phone
     * @param surname
     * @param name
     * @param id
     * @param relationship
     * @param age
     */
    public Profile(Integer id, Integer user_id, String name, String surname, String phone, String age, String gender, String profile_picture, String description, String relationship, String country, String languages, String created_at, String updated_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.profile_picture = profile_picture;
        this.description = description;
        this.relationship = relationship;
        this.country = country;
        this.languages = languages;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(user_id);
        dest.writeValue(name);
        dest.writeValue(surname);
        dest.writeValue(phone);
        dest.writeValue(age);
        dest.writeValue(gender);
        dest.writeValue(profile_picture);
        dest.writeValue(description);
        dest.writeValue(relationship);
        dest.writeValue(country);
        dest.writeValue(languages);
        dest.writeValue(created_at);
        dest.writeValue(updated_at);
    }

    public int describeContents() {
        return  0;
    }

}
