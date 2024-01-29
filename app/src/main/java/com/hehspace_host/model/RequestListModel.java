package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class RequestListModel {

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
        @SerializedName("user_image_url")
        public String userImageUrl;
        @Expose
        @SerializedName("full_name")
        public String fullName;
        @Expose
        @SerializedName("user_id")
        public String userId;
        @Expose
        @SerializedName("request_status")
        public String requestStatus;
        @Expose
        @SerializedName("property_time")
        public String propertyTime;
        @Expose
        @SerializedName("requested_time")
        public String requestedTime;
        @Expose
        @SerializedName("requested_date")
        public String requestedDate;
        @Expose
        @SerializedName("property_image_url")
        public String propertyImageUrl;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("request_number")
        public String requestNumber;
        @Expose
        @SerializedName("request_id")
        public String requestId;
    }
}
