
package com.watutz.app.objects;

import android.location.Location;

import java.util.Arrays;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.watutz.app.SharedObjects;
import com.watutz.app.datamanager.TotalDataManager;
import com.ypyproductions.location.utils.LocationUtils;
import com.ypyproductions.utils.StringUtils;

public class Home {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("location_name")
    @Expose
    private String location_name;
    @SerializedName("history")
    @Expose
    private String history;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("girls_0_5")
    @Expose
    private String girls_0_5;
    @SerializedName("boys_0_5")
    @Expose
    private String boys_0_5;
    @SerializedName("girls_6_13")
    @Expose
    private String girls_6_13;
    @SerializedName("boys_6_13")
    @Expose
    private String boys_6_13;
    @SerializedName("girls_14_18")
    @Expose
    private String girls_14_18;
    @SerializedName("boys_14_18")
    @Expose
    private String boys_14_18;
    @SerializedName("estate")
    @Expose
    private String estate;
    @SerializedName("need_urgency")
    @Expose
    private String need_urgency;
    @SerializedName("founded")
    @Expose
    private String founded;
    @SerializedName("mission")
    @Expose
    private String mission;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mpesa")
    @Expose
    private String mpesa;
    @SerializedName("bank_details")
    @Expose
    private String bank_details;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("age_range")
    @Expose
    private String age_range;
    @SerializedName("visits_count")
    @Expose
    private Integer visits_count;
    @SerializedName("images_count")
    @Expose
    private Integer images_count;
    @SerializedName("upcoming_visits_count")
    @Expose
    private Integer upcoming_visits_count;
    @SerializedName("home_needs")
    @Expose
    private List<Need> home_needs = null;
    public float distance=0;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Home() {
    }

    /**
     * 
     * @param total
     * @param phone
     * @param location_name
     * @param location
     * @param website
     * @param mpesa
     * @param need_urgency
     * @param image
     * @param girls_0_5
     * @param girls_14_18
     * @param age_range
     * @param bank_details
     * @param id
     * @param history
     * @param founded
     * @param upcoming_visits_count
     * @param email
     * @param address
     * @param mission
     * @param girls_6_13
     * @param boys_14_18
     * @param name
     * @param visits_count
     * @param boys_6_13
     * @param home_needs
     * @param images_count
     * @param estate
     * @param boys_0_5
     */
    public Home(Integer id, String name, String phone, String website, String location_name, String history, String image, String girls_0_5, String boys_0_5, String girls_6_13, String boys_6_13, String girls_14_18, String boys_14_18, String estate, String need_urgency, String founded, String mission, String address, String email, String mpesa, String bank_details, String location, Integer total, String age_range, Integer visits_count, Integer images_count, Integer upcoming_visits_count, List<Need> home_needs) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.location_name = location_name;
        this.history = history;
        this.image = image;
        this.girls_0_5 = girls_0_5;
        this.boys_0_5 = boys_0_5;
        this.girls_6_13 = girls_6_13;
        this.boys_6_13 = boys_6_13;
        this.girls_14_18 = girls_14_18;
        this.boys_14_18 = boys_14_18;
        this.estate = estate;
        this.need_urgency = need_urgency;
        this.founded = founded;
        this.mission = mission;
        this.address = address;
        this.email = email;
        this.mpesa = mpesa;
        this.bank_details = bank_details;
        this.location = location;
        this.total = total;
        this.age_range = age_range;
        this.visits_count = visits_count;
        this.images_count = images_count;
        this.upcoming_visits_count = upcoming_visits_count;
        this.home_needs = home_needs;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGirls_0_5() {
        return girls_0_5;
    }

    public void setGirls_0_5(String girls_0_5) {
        this.girls_0_5 = girls_0_5;
    }

    public String getBoys_0_5() {
        return boys_0_5;
    }

    public void setBoys_0_5(String boys_0_5) {
        this.boys_0_5 = boys_0_5;
    }

    public String getGirls_6_13() {
        return girls_6_13;
    }

    public void setGirls_6_13(String girls_6_13) {
        this.girls_6_13 = girls_6_13;
    }

    public String getBoys_6_13() {
        return boys_6_13;
    }

    public void setBoys_6_13(String boys_6_13) {
        this.boys_6_13 = boys_6_13;
    }

    public String getGirls_14_18() {
        return girls_14_18;
    }

    public void setGirls_14_18(String girls_14_18) {
        this.girls_14_18 = girls_14_18;
    }

    public String getBoys_14_18() {
        return boys_14_18;
    }

    public void setBoys_14_18(String boys_14_18) {
        this.boys_14_18 = boys_14_18;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getNeed_urgency() {
        return need_urgency;
    }

    public void setNeed_urgency(String need_urgency) {
        this.need_urgency = need_urgency;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMpesa() {
        return mpesa;
    }

    public void setMpesa(String mpesa) {
        this.mpesa = mpesa;
    }

    public String getBank_details() {
        return bank_details;
    }

    public void setBank_details(String bank_details) {
        this.bank_details = bank_details;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getAge_range() {
        return age_range;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }

    public Integer getVisits_count() {
        return visits_count;
    }

    public void setVisits_count(Integer visits_count) {
        this.visits_count = visits_count;
    }

    public Integer getImages_count() {
        return images_count;
    }

    public void setImages_count(Integer images_count) {
        this.images_count = images_count;
    }

    public Integer getUpcoming_visits_count() {
        return upcoming_visits_count;
    }

    public void setUpcoming_visits_count(Integer upcoming_visits_count) {
        this.upcoming_visits_count = upcoming_visits_count;
    }

    public List<Need> getHome_needs() {
        return home_needs;
    }

    public void setHome_needs(List<Need> home_needs) {
        this.home_needs = home_needs;
    }



    public void setDistance()
    {
        Location latLon = getActualLocation(getLocation());

        Location mVenueLocation;
        Location mCurrentLocation;

        mVenueLocation = new Location("Dummy Provider");
        mVenueLocation.setLatitude(latLon.getLatitude());
        mVenueLocation.setLongitude(latLon.getLongitude());
        TotalDataManager mTotalMng = new TotalDataManager(SharedObjects.context);
        mCurrentLocation = mTotalMng.getCurrentLocation();
        if(mCurrentLocation == null)
        {
            mCurrentLocation = new Location("Dummy Provider");
            mCurrentLocation.setLatitude(-1.2634456);
            mCurrentLocation.setLongitude(36.78454545);
        }

        this.distance = LocationUtils.calculateDistance(mCurrentLocation, mVenueLocation) / 1000f;


    }

    public float getDistance()
    {
        return distance;
    }

    public Location getActualLocation(String loc)
    {
        if(StringUtils.isStringEmpty(loc))
        {
            loc="-1.3032051,36.7073097";
            setLocation(loc);

        }
        Location theLoc = new Location ("POINT");

        List<String> location = Arrays.asList(loc.split("\\s*,\\s*"));
        theLoc.setLatitude(Double.valueOf(location.get(0)));
        theLoc.setLongitude(Double.valueOf(location.get(1)));

        return theLoc;


    }


}
