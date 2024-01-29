package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashBoardModel {

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
        @SerializedName("notification_count")
        public String notificationCount;

        public String getStripe_url() {
            return stripe_url;
        }

        public void setStripe_url(String stripe_url) {
            this.stripe_url = stripe_url;
        }

        @Expose
        @SerializedName("stripe_url")
        public String stripe_url;
        @Expose
        @SerializedName("stripe_status")
        public String stripeStatus;
        @Expose
        @SerializedName("request_list")
        public List<RequestListEntity> requestList;
        @Expose
        @SerializedName("properties_list")
        public List<PropertiesListEntity> propertiesList;
        @Expose
        @SerializedName("request_count")
        public String requestCount;
        @Expose
        @SerializedName("total_booking")
        public String totalBooking;
        @Expose
        @SerializedName("total_earning")
        public String totalEarning;
    }

    public static class RequestListEntity {
        @Expose
        @SerializedName("user_image_url")
        public String user_image_url;
        @Expose
        @SerializedName("full_name")
        public String full_name;
        @Expose
        @SerializedName("user_id")
        public String user_id;
        @Expose
        @SerializedName("request_status")
        public String request_status;
        @Expose
        @SerializedName("property_time")
        public String property_time;
        @Expose
        @SerializedName("requested_time")
        public String requested_time;
        @Expose
        @SerializedName("requested_date")
        public String requested_date;
        @Expose
        @SerializedName("property_image_url")
        public String property_image_url;
        @Expose
        @SerializedName("property_id")
        public String property_id;
        @Expose
        @SerializedName("property_title")
        public String property_title;
        @Expose
        @SerializedName("request_number")
        public String request_number;
        @Expose
        @SerializedName("request_id")
        public String request_id;
    }

    public static class PropertiesListEntity {
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
        @SerializedName("property_image_url")
        public String propertyImageUrl;
        @Expose
        @SerializedName("property_location")
        public String propertyLocation;
        @Expose
        @SerializedName("property_time")
        public String propertyTime;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
    }
}
