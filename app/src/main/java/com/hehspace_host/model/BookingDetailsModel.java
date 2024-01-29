package com.hehspace_host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class BookingDetailsModel {

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
        @SerializedName("price_calculation")
        public PriceCalculationEntity priceCalculation;
        @Expose
        @SerializedName("time_calculation")
        public TimeCalculationEntity timeCalculation;
        @Expose
        @SerializedName("property_rate")
        public PropertyRateEntity propertyRate;
        @Expose
        @SerializedName("addon_services")
        public List<AddonServicesEntity> addonServices;
        @Expose
        @SerializedName("number_of_guest")
        public String numberOfGuest;
        @Expose
        @SerializedName("property_time")
        public String propertyTime;
        @Expose
        @SerializedName("checkout_date")
        public String checkoutDate;
        @Expose
        @SerializedName("checkin_date")
        public String checkinDate;
        @Expose
        @SerializedName("property_category")
        public String propertyCategory;
        @Expose
        @SerializedName("property_review")
        public String propertyReview;
        @Expose
        @SerializedName("property_ratting")
        public String propertyRatting;
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
        @SerializedName("user_mobile")
        public String userMobile;
        @Expose
        @SerializedName("id_document")
        public String idDocument;
        @Expose
        @SerializedName("property_image_url")
        public String propertyImageUrl;
        @Expose
        @SerializedName("property_location")
        public String propertyLocation;
        @Expose
        @SerializedName("request_id")
        public String requestId;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("booking_id")
        public String bookingId;
    }

    public static class PriceCalculationEntity {
        @Expose
        @SerializedName("cleaner_fee")
        public String cleanerFee;
        @Expose
        @SerializedName("total_amount")
        public String totalAmount;
        @Expose
        @SerializedName("tax_amount")
        public String taxAmount;
        @Expose
        @SerializedName("services_amount")
        public String servicesAmount;
        @Expose
        @SerializedName("hourly_amount")
        public String hourlyAmount;
        @Expose
        @SerializedName("daily_amount")
        public String dailyAmount;
        @Expose
        @SerializedName("weekly_amount")
        public String weeklyAmount;
        @Expose
        @SerializedName("monthly_amount")
        public String monthlyAmount;
    }

    public static class TimeCalculationEntity {
        @Expose
        @SerializedName("stay_hours")
        public String stayHours;
        @Expose
        @SerializedName("stay_days")
        public String stayDays;
        @Expose
        @SerializedName("stay_weeks")
        public String stayWeeks;
        @Expose
        @SerializedName("stay_months")
        public String stayMonths;
    }

    public static class AddonServicesEntity {
        @Expose
        @SerializedName("services_rate")
        public String servicesRate;
        @Expose
        @SerializedName("services_title")
        public String servicesTitle;
    }


    public static class PropertyRateEntity {
        @Expose
        @SerializedName("hourly_rate")
        public String hourlyRate;
        @Expose
        @SerializedName("daily_rate")
        public String dailyRate;
        @Expose
        @SerializedName("weekly_rate")
        public String weeklyRate;
        @Expose
        @SerializedName("monthly_rate")
        public String monthlyRate;
    }
}
