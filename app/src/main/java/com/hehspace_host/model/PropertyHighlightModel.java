package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertyHighlightModel {

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
        @SerializedName("higlights_title")
        public String higlightsTitle;
        @Expose
        @SerializedName("higlights_id")
        public String higlightsId;

        public boolean isselected = false;
    }
}
