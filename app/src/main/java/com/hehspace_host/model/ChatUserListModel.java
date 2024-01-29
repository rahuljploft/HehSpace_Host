package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class ChatUserListModel {


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
        @SerializedName("created_at")
        public String createdAt;
        @Expose
        @SerializedName("user_image_url")
        public String userImageUrl;
        @Expose
        @SerializedName("property_name")
        public String propertyName;
        @Expose
        @SerializedName("username")
        public String username;
        @Expose
        @SerializedName("user_id")
        public String userId;
        @Expose
        @SerializedName("booking_id")
        public String bookingId;
        @Expose
        @SerializedName("request_number")
        public String requestNumber;
        @Expose
        @SerializedName("request_id")
        public String requestId;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
        @Expose
        @SerializedName("message")
        public String message;
        @Expose
        @SerializedName("chat_id")
        public String chatId;
    }
}
