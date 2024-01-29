package com.hehspace_host.util;

import android.net.Uri;

import com.google.gson.JsonArray;
import com.hehspace_host.model.PropertyDetailModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;

public class Constant {
    public static String USERTYPE = "103";
    public static String MALE = "1001";
    public static String FEMALE = "1002";
    public static String ISEDIT = "no";
    //    ADD PROPERTY VALUES
    public  static  boolean notificationTest = false;
    public static HashMap<String, String> map = new HashMap<>();
   // public static String CategoryId = "";

    public static String PropertyId = "";
    public static String LocationTag = "0";
    public static String PropertyAddress = "";
    public static String BuildingName = "";
    public static String StreetName = "";
    public static String CityName = "";
    public static String StateName = "";
    public static String PostCode = "";
    public static String CountryName = "";
    public static String Latitude = "";
    public static String Longitude = "";
    public static String AllowedGuest = "";
    public static List<String> AmenitiesId = new ArrayList<>();
    public static List<String> AmenitiesName = new ArrayList<>();
    public static List<String> CategoryId = new ArrayList<>();
    public static List<String> CategoryName = new ArrayList<>();
    public static ArrayList<PropertyDetailModel.PropertyImagesEntity> imageList = new ArrayList<>();
    public static List<String> Amenities = new ArrayList<>();
    public static String PropertyTitle = "";
    public static List<String> PropertyHighlights = new ArrayList<>();
    public static String PropertyDetails = "";
    public static List<Uri> imageData = new ArrayList<>();
    public static MultipartBody.Part[] fileToUpload;
    public static List<String> HighlightId = new ArrayList<>();
    public static List<String> Highlights = new ArrayList<>();
    public static List<String> HighlightsLive = new ArrayList<>();
    public static String CheckInTime = "";
    public static String CheckOutTime = "";
    public static String HourlyPrice = "";
    public static String DailyPrice = "";
    public static String WeeklyPrice = "";
    public static String MonthlyPrice = "";
    public static String CleanerFee = "";
    public static String HirePhotographer = "No";
    public static JSONArray additionalServices = new JSONArray();
    public static ArrayList<PropertyDetailModel.PropertyServicesEntity> servicessList = new ArrayList<>();

}
