package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class BookingListModel {

    @Expose
    @SerializedName("Data")
    public List<DataEntity> data;
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
        @SerializedName("booking_status")
        public String bookingStatus;
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
        @SerializedName("checkout_date")
        public String checkoutDate;
        @Expose
        @SerializedName("checkin_date")
        public String checkinDate;
        @Expose
        @SerializedName("property_image_url")
        public String propertyImageUrl;
        @Expose
        @SerializedName("full_name")
        public String fullName;
        @Expose
        @SerializedName("booking_number")
        public String bookingNumber;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("booking_id")
        public String bookingId;
    }
}
