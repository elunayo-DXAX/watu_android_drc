
package com.watutz.app.util.objects;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Match {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("home_id")
    @Expose
    private Integer home_id;
    @SerializedName("home_name")
    @Expose
    private String home_name;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("visit_actual_start")
    @Expose
    private String visit_actual_start;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("location_name")
    @Expose
    private String location_name;
    @SerializedName("images_count")
    @Expose
    private Integer images_count;
    @SerializedName("visitors_count")
    @Expose
    private Integer visitors_count;
    @SerializedName("user")
    @Expose
    private User user;
    private float distance =0;
    /**
     * No args constructor for use in serialization
     *
     */
    public Match() {
    }

    /**
     *
     * @param location_name
     * @param visit_actual_start
     * @param host
     * @param location
     * @param visitors_count
     * @param date
     * @param home_id
     * @param id
     * @param time
     * @param updated_at
     * @param details
     * @param name
     * @param poster
     * @param created_at
     * @param home_name
     * @param user_id
     * @param user
     * @param images_count
     */
    public Match(Integer id, String name, Integer home_id, String home_name, Integer user_id, String details, String date, String time, String visit_actual_start, String location, String poster, String host, String created_at, String updated_at, String location_name, Integer images_count, Integer visitors_count, User user) {
        super();
        this.id = id;
        this.name = name;
        this.home_id = home_id;
        this.home_name = home_name;
        this.user_id = user_id;
        this.details = details;
        this.date = date;
        this.time = time;
        this.visit_actual_start = visit_actual_start;
        this.location = location;
        this.poster = poster;
        this.host = host;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.location_name = location_name;
        this.images_count = images_count;
        this.visitors_count = visitors_count;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHome_id() {
        return home_id;
    }

    public void setHome_id(Integer home_id) {
        this.home_id = home_id;
    }

    public String getHome_name() {
        return home_name;
    }

    public void setHome_name(String home_name) {
        this.home_name = home_name;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVisit_actual_start() {
        return visit_actual_start;
    }

    public void setVisit_actual_start(String visit_actual_start) {
        this.visit_actual_start = visit_actual_start;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public Integer getImages_count() {
        return images_count;
    }

    public void setImages_count(Integer images_count) {
        this.images_count = images_count;
    }

    public Integer getVisitors_count() {
        return visitors_count;
    }

    public void setVisitors_count(Integer visitors_count) {
        this.visitors_count = visitors_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDistance()
    {
//        Location latLon = getActualLocation(getLocation());
//
//        Location mVenueLocation;
//        Location mCurrentLocation;
//
//        mVenueLocation = new Location("Dummy Provider");
//        mVenueLocation.setLatitude(latLon.getLatitude());
//        mVenueLocation.setLongitude(latLon.getLongitude());
//        TotalDataManager mTotalMng = new TotalDataManager(SharedObjects.context);
//        mCurrentLocation = mTotalMng.getCurrentLocation();
//        if(mCurrentLocation == null)
//        {
//            mCurrentLocation = new Location("Dummy Provider");
//            mCurrentLocation.setLatitude(-1.2634456);
//            mCurrentLocation.setLongitude(36.78454545);
//        }
//
//        this.distance = LocationUtils.calculateDistance(mCurrentLocation, mVenueLocation) / 1000f;


    }

    public float getDistance()
    {
        return distance;
    }

    public Location getActualLocation(String loc)
    {

        Location theLoc = new Location ("POINT");

        List<String> location = Arrays.asList(loc.split("\\s*,\\s*"));
        theLoc.setLatitude(Double.valueOf(location.get(0)));
        theLoc.setLongitude(Double.valueOf(location.get(1)));

        return theLoc;


    }

}
