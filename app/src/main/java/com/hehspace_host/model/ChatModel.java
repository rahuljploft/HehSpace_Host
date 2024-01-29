package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class ChatModel {

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
        @SerializedName("to_user")
        public ToUserEntity toUser;
        @Expose
        @SerializedName("from_user")
        public FromUserEntity fromUser;
        @Expose
        @SerializedName("created_at")
        public String createdAt;
        @Expose
        @SerializedName("image_name")
        public String imageName;
        @Expose
        @SerializedName("message")
        public String message;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
        @Expose
        @SerializedName("request_number")
        public String requestNumber;
        @Expose
        @SerializedName("request_id")
        public String requestId;
        @Expose
        @SerializedName("booking_id")
        public String bookingId;
        @Expose
        @SerializedName("to_user_id")
        public String toUserId;
        @Expose
        @SerializedName("from_user_id")
        public String fromUserId;
        @Expose
        @SerializedName("chat_id")
        public String chatId;
    }

    public static class ToUserEntity {
        @Expose
        @SerializedName("user_id")
        public String userId;
        @Expose
        @SerializedName("user_pic_name")
        public String userPicName;
        @Expose
        @SerializedName("last_name")
        public String lastName;
        @Expose
        @SerializedName("first_name")
        public String firstName;
    }

    public static class FromUserEntity {
        @Expose
        @SerializedName("user_id")
        public String userId;
        @Expose
        @SerializedName("user_pic_name")
        public String userPicName;
        @Expose
        @SerializedName("last_name")
        public String lastName;
        @Expose
        @SerializedName("first_name")
        public String firstName;
    }
}
