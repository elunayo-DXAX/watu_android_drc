
package com.watutz.app.objects;

import android.location.Location;


import com.watutz.app.SharedObjects;
import com.watutz.app.datamanager.TotalDataManager;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.ypyproductions.location.utils.LocationUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "user_id",
    "photo",
    "category_id",
    "price",
    "viability",
    "weight",
    "dimension_l",
    "dimension_w",
    "dimension_h",
    "status",
    "bid",
    "bid_start_date",
    "bid_end_date",
    "bid_status",
    "services",
    "delivery",
    "created_at",
    "updated_at",
    "meetup",
    "reduced_price",
    "category_name",
    "location",
    "location_name",
    "delivery_max_radius",
    "photos",
    "category",
    "user",
    "name",
    "description"
})
public class Potential {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("user_id")
    private Integer user_id;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("category_id")
    private Integer category_id;
    @JsonProperty("price")
    private String price;
    @JsonProperty("viability")
    private Integer viability;
    @JsonProperty("weight")
    private String weight;
    @JsonProperty("dimension_l")
    private String dimension_l;
    @JsonProperty("dimension_w")
    private String dimension_w;
    @JsonProperty("dimension_h")
    private String dimension_h;
    @JsonProperty("status")
    private String status;
    @JsonProperty("bid")
    private Boolean bid;
    @JsonProperty("bid_start_date")
    private String bid_start_date;
    @JsonProperty("bid_end_date")
    private String bid_end_date;
    @JsonProperty("bid_status")
    private String bid_status;
    @JsonProperty("delivery")
    private Boolean delivery;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("meetup")
    private Boolean meetup;
    @JsonProperty("reduced_price")
    private String reduced_price;
    @JsonProperty("category_name")
    private String category_name;
    @JsonProperty("location")
    private String location;
    @JsonProperty("location_name")
    private String location_name;
    @JsonProperty("delivery_max_radius")
    private Integer delivery_max_radius;
    @JsonProperty("user")
    private User user;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private float productdistance;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Potential() {
    }

    /**
     * 
     * @param weight
     * @param meetup
     * @param location
     * @param dimension_l
     * @param reduced_price
     * @param dimension_w
     * @param id
     * @param delivery_max_radius
     * @param viability
     * @param description
     * @param name
     * @param created_at
     * @param user_id
     * @param dimension_h
     * @param bid
     * @param location_name
     * @param status
     * @param bid_start_date
     * @param bid_end_date
     * @param bid_status
     * @param category_id
     * @param photo

     * @param price
     * @param updated_at
     * @param category_name
     * @param delivery
     * @param user
     */
    public Potential(Integer id, Integer user_id, String photo, Integer category_id, String price, Integer viability, String weight, String dimension_l, String dimension_w, String dimension_h, String status, Boolean bid, String bid_start_date, String bid_end_date, String bid_status, Boolean delivery, String created_at, String updated_at, Boolean meetup, String reduced_price, String category_name, String location, String location_name, Integer delivery_max_radius, User user, String name, String description) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.photo = photo;
        this.category_id = category_id;
        this.price = price;
        this.viability = viability;
        this.weight = weight;
        this.dimension_l = dimension_l;
        this.dimension_w = dimension_w;
        this.dimension_h = dimension_h;
        this.status = status;
        this.bid = bid;
        this.bid_start_date = bid_start_date;
        this.bid_end_date = bid_end_date;
        this.bid_status = bid_status;
        this.delivery = delivery;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.meetup = meetup;
        this.reduced_price = reduced_price;
        this.category_name = category_name;
        this.location = location;
        this.location_name = location_name;
        this.delivery_max_radius = delivery_max_radius;

        this.user = user;
        this.name = name;
        this.description = description;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("user_id")
    public Integer getUser_id() {
        return user_id;
    }

    @JsonProperty("user_id")
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @JsonProperty("photo")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @JsonProperty("category_id")
    public Integer getCategory_id() {
        return category_id;
    }

    @JsonProperty("category_id")
    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("viability")
    public Integer getViability() {
        return viability;
    }

    @JsonProperty("viability")
    public void setViability(Integer viability) {
        this.viability = viability;
    }

    @JsonProperty("weight")
    public String getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(String weight) {
        this.weight = weight;
    }

    @JsonProperty("dimension_l")
    public String getDimension_l() {
        return dimension_l;
    }

    @JsonProperty("dimension_l")
    public void setDimension_l(String dimension_l) {
        this.dimension_l = dimension_l;
    }

    @JsonProperty("dimension_w")
    public String getDimension_w() {
        return dimension_w;
    }

    @JsonProperty("dimension_w")
    public void setDimension_w(String dimension_w) {
        this.dimension_w = dimension_w;
    }

    @JsonProperty("dimension_h")
    public String getDimension_h() {
        return dimension_h;
    }

    @JsonProperty("dimension_h")
    public void setDimension_h(String dimension_h) {
        this.dimension_h = dimension_h;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("bid")
    public Boolean getBid() {
        return bid;
    }

    @JsonProperty("bid")
    public void setBid(Boolean bid) {
        this.bid = bid;
    }

    @JsonProperty("bid_start_date")
    public String getBid_start_date() {
        return bid_start_date;
    }

    @JsonProperty("bid_start_date")
    public void setBid_start_date(String bid_start_date) {
        this.bid_start_date = bid_start_date;
    }

    @JsonProperty("bid_end_date")
    public String getBid_end_date() {
        return bid_end_date;
    }

    @JsonProperty("bid_end_date")
    public void setBid_end_date(String bid_end_date) {
        this.bid_end_date = bid_end_date;
    }

    @JsonProperty("bid_status")
    public String getBid_status() {
        return bid_status;
    }

    @JsonProperty("bid_status")
    public void setBid_status(String bid_status) {
        this.bid_status = bid_status;
    }



    @JsonProperty("delivery")
    public Boolean getDelivery() {
        return delivery;
    }

    @JsonProperty("delivery")
    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    @JsonProperty("created_at")
    public String getCreated_at() {
        return created_at;
    }

    @JsonProperty("created_at")
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @JsonProperty("updated_at")
    public String getUpdated_at() {
        return updated_at;
    }

    @JsonProperty("updated_at")
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @JsonProperty("meetup")
    public Boolean getMeetup() {
        return meetup;
    }

    @JsonProperty("meetup")
    public void setMeetup(Boolean meetup) {
        this.meetup = meetup;
    }

    @JsonProperty("reduced_price")
    public String getReduced_price() {
        return reduced_price;
    }

    @JsonProperty("reduced_price")
    public void setReduced_price(String reduced_price) {
        this.reduced_price = reduced_price;
    }

    @JsonProperty("category_name")
    public String getCategory_name() {
        return category_name;
    }

    @JsonProperty("category_name")
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("location_name")
    public String getLocation_name() {
        return location_name;
    }

    @JsonProperty("location_name")
    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    @JsonProperty("delivery_max_radius")
    public Integer getDelivery_max_radius() {
        return delivery_max_radius;
    }

    @JsonProperty("delivery_max_radius")
    public void setDelivery_max_radius(Integer delivery_max_radius) {
        this.delivery_max_radius = delivery_max_radius;
    }



    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
	
	public float getProductdistance() {
    	setProductdistance();
		return productdistance;
	}

	public void setProductdistance() {

		TotalDataManager mTotalMng = new TotalDataManager(SharedObjects.context);
		Location latLon = getActualLocation(getLocation());

		Location mVenueLocation;
		Location mCurrentLocation;

		mVenueLocation = new Location("Dummy Provider");
		mVenueLocation.setLatitude(latLon.getLatitude());
		mVenueLocation.setLongitude(latLon.getLongitude());
		
		mCurrentLocation = mTotalMng.getCurrentLocation();

		float distance = LocationUtils.calculateDistance(mCurrentLocation, mVenueLocation) / 1000f;
		this.productdistance = distance;
	}

	public Location getActualLocation(String loc) {

		Location theLoc = new Location("POINT");

		List<String> location = Arrays.asList(loc.split("\\s*,\\s*"));

		theLoc.setLatitude(Double.valueOf(location.get(0)));

		theLoc.setLongitude(Double.valueOf(location.get(1)));

		return theLoc;

	}

}
