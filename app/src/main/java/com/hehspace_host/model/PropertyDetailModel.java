package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public  class PropertyDetailModel {


    @Expose
    @SerializedName("Data")
    public DataEntity data;
    @Expose
    @SerializedName("Message")
    public String message;
    @Expose
    @SerializedName("Status")
    public String status;
    @Expose
    @SerializedName("ResponseCode")
    public String responsecode;

    public static class DataEntity {
        @Expose
        @SerializedName("property_review_list")
        public ArrayList<PropertyReviewListEntity> propertyReviewList;
        @Expose
        @SerializedName("property_images")
        public ArrayList<PropertyImagesEntity> propertyImages;
        @Expose
        @SerializedName("property_services")
        public List<PropertyServicesEntity> propertyServices;
        @Expose
        @SerializedName("cleaner_fee")
        public String cleanerFee;
        @Expose
        @SerializedName("property_details")
        public String propertyDetails;
        @Expose
        @SerializedName("property_higlights")
        public String propertyHiglights;
        @Expose
        @SerializedName("property_anenities")
        public String propertyAnenities;
        @Expose
        @SerializedName("property_category")
        public String propertyCategory;
        @Expose
        @SerializedName("five_start_ratio")
        public String fiveStartRatio;
        @Expose
        @SerializedName("property_review")
        public String propertyReview;
        @Expose
        @SerializedName("property_ratting")
        public String propertyRatting;
        @Expose
        @SerializedName("property_status")
        public String propertyStatus;
        @Expose
        @SerializedName("country_name")
        public String countryName;
        @Expose
        @SerializedName("city_name")
        public String cityName;
        @Expose
        @SerializedName("property_location")
        public String propertyLocation;
        @Expose
        @SerializedName("allowed_guest")
        public String allowedGuest;
        @Expose
        @SerializedName("post_code")
        public String postCode;
        @Expose
        @SerializedName("state_name")
        public String stateName;
        @Expose
        @SerializedName("building_name")
        public String buildingName;
        @Expose
        @SerializedName("street_name")
        public String streetName;
        @Expose
        @SerializedName("longitude")
        public String longitude;
        @Expose
        @SerializedName("latitude")
        public String latitude;
        @Expose
        @SerializedName("property_address")
        public String propertyAddress;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("hire_photographer")
        public String hirePhotographer;
        @Expose
        @SerializedName("photographer_note")
        public String photographerNote;
        @Expose
        @SerializedName("photographer_status")
        public String photographerStatus;
        @Expose
        @SerializedName("property_time")
        public String propertyTime;
        @Expose
        @SerializedName("checkout_time")
        public String checkoutTime;
        @Expose
        @SerializedName("checkin_time")
        public String checkinTime;
        @Expose
        @SerializedName("monthly_rate")
        public String monthlyRate;
        @Expose
        @SerializedName("weekly_rate")
        public String weeklyRate;
        @Expose
        @SerializedName("daily_rate")
        public String dailyRate;
        @Expose
        @SerializedName("hourly_rate")
        public String hourlyRate;
        @Expose
        @SerializedName("category_id")
        public String categoryId;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
    }

    public static class PropertyReviewListEntity {
        @Expose
        @SerializedName("created_at")
        public String createdAt;
        @Expose
        @SerializedName("ratting_star")
        public String rattingStar;
        @Expose
        @SerializedName("ratting_comment")
        public String rattingComment;
        @Expose
        @SerializedName("user_image")
        public String userImage;
        @Expose
        @SerializedName("full_name")
        public String fullName;
    }

    public static class PropertyImagesEntity {
        @Expose
        @SerializedName("property_image_url")
        public String propertyImageUrl;
    }

    public static class PropertyServicesEntity {
        @Expose
        @SerializedName("services_rate")
        public String servicesRate;
        @Expose
        @SerializedName("services_title")
        public String servicesTitle;
        @Expose
        @SerializedName("service_id")
        public String serviceId;
    }
}
