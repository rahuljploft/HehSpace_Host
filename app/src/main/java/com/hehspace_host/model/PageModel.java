package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class PageModel {

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
        @SerializedName("terms-and-conditions")
        public String terms_conditions;
        @Expose
        @SerializedName("refund-policy")
        public String refund_policy;
        @Expose
        @SerializedName("privacy-policy")
        public String privacy_policy;
    }
}
