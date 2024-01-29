package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class ForgotPasswordModel {

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
        @SerializedName("email_address")
        public String emailAddress;
        @Expose
        @SerializedName("verifaction_otp")
        public String verifactionOtp;
        @Expose
        @SerializedName("user_id")
        public String userId;
    }
}
