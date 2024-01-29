package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModel {

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
        @SerializedName("stripe_status")
        public String stripeStatus;
        @Expose
        @SerializedName("gender_name")
        public String genderName;
        @Expose
        @SerializedName("gender_id")
        public String genderId;
        @Expose
        @SerializedName("user_pic_name")
        public String userPicName;
        @Expose
        @SerializedName("id_proof_url")
        public String idProofUrl;
        @Expose
        @SerializedName("mobile_number")
        public String mobileNumber;
        @Expose
        @SerializedName("email_address")
        public String emailAddress;
        @Expose
        @SerializedName("last_name")
        public String lastName;
        @Expose
        @SerializedName("first_name")
        public String firstName;
        @Expose
        @SerializedName("user_id")
        public String userId;
    }
}
