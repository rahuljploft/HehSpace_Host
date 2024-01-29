package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class EarningModel {

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
        @SerializedName("transaction_detail")
        public List<TransactionDetail> transactionDetail;
        @Expose
        @SerializedName("today_earning")
        public String todayEarning;
        @Expose
        @SerializedName("total_earning")
        public String totalEarning;
    }

    public static class TransactionDetail{
        @Expose
        @SerializedName("booking_number")
        public String booking_number;

        @Expose
        @SerializedName("full_name")
        public String full_name;

        @Expose
        @SerializedName("transaction_date")
        public String transaction_date;

        @Expose
        @SerializedName("transaction_amount")
        public String transaction_amount;
    }
}
