package com.hehspace_host.network;


import com.hehspace_host.components.fragments.dashboard.profile.ProfileView_Model;
import com.hehspace_host.model.BookingDetailsModel;
import com.hehspace_host.model.BookingListModel;
import com.hehspace_host.model.ChatModel;
import com.hehspace_host.model.ChatUserListModel;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.DashBoardModel;
import com.hehspace_host.model.DateListModel;
import com.hehspace_host.model.EarningModel;
import com.hehspace_host.model.ForgotPasswordModel;
import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.NotificationModel;
import com.hehspace_host.model.PageModel;
import com.hehspace_host.model.ProfileModel;
import com.hehspace_host.model.PropertyAmenitiesModel;
import com.hehspace_host.model.PropertyCategoryModel;
import com.hehspace_host.model.PropertyDetailModel;
import com.hehspace_host.model.PropertyHighlightModel;
import com.hehspace_host.model.PropertyListModel;
import com.hehspace_host.model.RequestDetailsModel;
import com.hehspace_host.model.RequestListModel;
import com.hehspace_host.model.VerifyOtpModel;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestApi {

    @Multipart
    @POST("/api/create_account")
    Observable<LoginModel> createAccount(
            @Part MultipartBody.Part first_name,
            @Part MultipartBody.Part last_name,
            @Part MultipartBody.Part email_address,
            @Part MultipartBody.Part mobile_number,
            @Part MultipartBody.Part gender_id,
            @Part MultipartBody.Part user_password,
            @Part MultipartBody.Part social_media_id,
            @Part MultipartBody.Part user_type,
            @Part MultipartBody.Part id_document,
            @Part MultipartBody.Part device_token
    );

    @FormUrlEncoded
    @POST("/api/sent_otp")
    Observable<VerifyOtpModel> verifyOTP(@FieldMap HashMap<String, String> reqData);

    @GET("/api/get_pages")
    Observable<PageModel> getPages();

    @FormUrlEncoded
    @POST("/api/login_account")
    Observable<LoginModel> loginApi(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/login_social_media")
    Observable<LoginModel> socialloginApi(@FieldMap HashMap<String, String> reqData);


    @FormUrlEncoded
    @POST("/api/forgot_password")
    Observable<ForgotPasswordModel> forgotPassword(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/reset_password")
    Observable<CommonModel> resetPassword(@FieldMap HashMap<String, String> reqData);

    @GET("/api/host/dashboard")
    Observable<DashBoardModel> dashBoard();

    @GET("/api/property/category")
    Observable<PropertyCategoryModel> propertyCategory();

    @GET("/api/property/anenities")
    Observable<PropertyAmenitiesModel> propertyAmenities();

    @GET("/api/notification/list")
    Observable<NotificationModel> notificationList();


    @GET("/api/notification/delete")
    Observable<CommonModel> deleteNotification();

    @GET("/api/notification/seen")
    Observable<CommonModel> seenNotification();

    @GET("/api/profile")
    Observable<ProfileModel> profileDetails();



    @Multipart
    @POST("/api/update_profile")
    Observable<ProfileModel> updateprofileDetails(
            @Part MultipartBody.Part first_name,
            @Part MultipartBody.Part last_name,
            @Part MultipartBody.Part email_address,
            @Part MultipartBody.Part mobile_number,
            @Part MultipartBody.Part gender_id,
            @Part MultipartBody.Part user_type,
            @Part MultipartBody.Part user_photo,
            @Part MultipartBody.Part id_document,
            @Part MultipartBody.Part _method
    );

    @Multipart
    @POST("/api/host/property/create")
    Observable<CommonModel> createProperty(
            @Part MultipartBody.Part category_id,
            @Part MultipartBody.Part property_address,
            @Part MultipartBody.Part street_name,
            @Part MultipartBody.Part building_name,
            @Part MultipartBody.Part city_name,
            @Part MultipartBody.Part state_name,
            @Part MultipartBody.Part post_code,
            @Part MultipartBody.Part country_name,
            @Part MultipartBody.Part allowed_guest,
            @Part MultipartBody.Part allowed_anenities,
            @Part MultipartBody.Part property_title,
            @Part MultipartBody.Part property_highlights,
            @Part MultipartBody.Part property_details,
            @Part MultipartBody.Part hourly_rate,
            @Part MultipartBody.Part daily_rate,
            @Part MultipartBody.Part weekly_rate,
            @Part MultipartBody.Part monthly_rate,
            @Part MultipartBody.Part checkin_time,
            @Part MultipartBody.Part checkout_time,
            @Part MultipartBody.Part additional_services,
            @Part MultipartBody.Part hire_photographer,
            @Part MultipartBody.Part cleaner_fee,
            @Part MultipartBody.Part latitude,
            @Part MultipartBody.Part longitude,
            @Part MultipartBody.Part[] fileToUpload
    );
    @Multipart
    @POST("/api/host/property/update/{property_id}")
    Observable<CommonModel> UpdateProperty(
            @Path("property_id") int id,
            @Part MultipartBody.Part category_id,
            @Part MultipartBody.Part property_address,
            @Part MultipartBody.Part street_name,
            @Part MultipartBody.Part building_name,
            @Part MultipartBody.Part city_name,
            @Part MultipartBody.Part state_name,
            @Part MultipartBody.Part post_code,
            @Part MultipartBody.Part country_name,
            @Part MultipartBody.Part allowed_guest,
            @Part MultipartBody.Part allowed_anenities,
            @Part MultipartBody.Part property_title,
            @Part MultipartBody.Part property_highlights,
            @Part MultipartBody.Part property_details,
            @Part MultipartBody.Part hourly_rate,
            @Part MultipartBody.Part daily_rate,
            @Part MultipartBody.Part weekly_rate,
            @Part MultipartBody.Part monthly_rate,
            @Part MultipartBody.Part checkin_time,
            @Part MultipartBody.Part checkout_time,
            @Part MultipartBody.Part additional_services,
            @Part MultipartBody.Part hire_photographer,
            @Part MultipartBody.Part cleaner_fee,
            @Part MultipartBody.Part _method,
            @Part MultipartBody.Part latitude,
            @Part MultipartBody.Part longitude,
            @Part MultipartBody.Part[] fileToUpload
    );


    @GET("/api/host/property/higlights")
    Observable<PropertyHighlightModel> propertyHighlights();

    @GET("/api/host/property/listing")
    Observable<PropertyListModel> getPropertyListing();


    @GET("/api/host/property/details/{property_id}")
    Observable<PropertyDetailModel> propertyDetails(@Path("property_id") int itemId);


    @DELETE("/api/host/property/delete/{property_id}")
    Observable<CommonModel> propertyDelete(@Path("property_id") int itemId);

    @FormUrlEncoded
    @POST("/api/host/property/change_status/{property_id}")
    Observable<CommonModel> propertyUnpublish(@FieldMap HashMap<String, String> reqData,@Path("property_id") int itemId);

    @GET("/api/host/request/list")
    Observable<RequestListModel> getRequestList();

    @GET("/api/host/request/details/{request_id}")
    Observable<RequestDetailsModel> getRequestDetails(@Path("request_id") int itemId);

    @FormUrlEncoded
    @POST("/api/host/request/change_status/{request_id}")
    Observable<CommonModel> acceptReject(@FieldMap HashMap<String, String> reqData,@Path("request_id") int itemId);

    @GET("/api/host/booking/list")
    Observable<BookingListModel> getBookingList();

    @FormUrlEncoded
    @POST("/api/host/booking/list_book_date")
    Observable<BookingListModel> checkavailbook(@FieldMap HashMap<String, String> reqData);

    @GET("/api/host/booking/details/{booking_id}")
    Observable<BookingDetailsModel> getBookingDetails(@Path("booking_id") int itemId);

    @GET("/api/chat_user_list")
    Observable<ChatUserListModel> getChatUserList();

    @GET("/api/booked_dates/{property_id}")
    Observable<DateListModel> getDateList(@Path("property_id") int itemId);

    @FormUrlEncoded
    @POST("/api/chat_list")
    Observable<ChatModel> chatList(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/create_chat")
    Observable<CommonModel> sendMessage(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/host/wallet")
    Observable<EarningModel> walletDetails(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/host/property/photo-grapher-request")
    Observable<CommonModel> submitPhotographer(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/delete_chat")
    Observable<CommonModel> deleteChat(@FieldMap HashMap<String, String> reqData);
}
