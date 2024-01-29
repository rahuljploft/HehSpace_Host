package com.hehspace_host.components.addproperty.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.CommonModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;
import com.hehspace_host.util.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class PropertySummaryView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<CommonModel>> propertycategorylivedata = new MutableLiveData<>();
    ApiResponse<CommonModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }
    public  void createProperty() {
        MultipartBody.Part mpcategory_id = MultipartBody.Part.createFormData("category_id", android.text.TextUtils.join(",", Constant.CategoryId) );
        MultipartBody.Part mpproperty_address = MultipartBody.Part.createFormData("property_address", Constant.PropertyAddress);
        MultipartBody.Part mpstreet_name = MultipartBody.Part.createFormData("street_name", Constant.StreetName);
        MultipartBody.Part mpbuilding_name = MultipartBody.Part.createFormData("building_name", Constant.BuildingName);
        MultipartBody.Part mpcity_name = MultipartBody.Part.createFormData("city_name", Constant.CityName);
        MultipartBody.Part mpstate_name = MultipartBody.Part.createFormData("state_name", Constant.StateName);
        MultipartBody.Part mppost_code = MultipartBody.Part.createFormData("post_code", Constant.PostCode);
        MultipartBody.Part mpcountry_name = MultipartBody.Part.createFormData("country_name", Constant.CountryName);
        MultipartBody.Part mpallowed_guest = MultipartBody.Part.createFormData("allowed_guest", Constant.AllowedGuest);
        MultipartBody.Part mpallowed_anenities = MultipartBody.Part.createFormData("allowed_anenities", android.text.TextUtils.join(",", Constant.AmenitiesId));
        MultipartBody.Part mpproperty_title = MultipartBody.Part.createFormData("property_title", Constant.PropertyTitle);
        MultipartBody.Part mpproperty_highlights = MultipartBody.Part.createFormData("property_highlights",android.text.TextUtils.join(",", Constant.HighlightId));
        MultipartBody.Part mpproperty_details = MultipartBody.Part.createFormData("property_details", Constant.PropertyDetails);
        MultipartBody.Part mphourly_rate = MultipartBody.Part.createFormData("hourly_rate", Constant.HourlyPrice);
        MultipartBody.Part mpdaily_rate = MultipartBody.Part.createFormData("daily_rate", Constant.DailyPrice);
        MultipartBody.Part mpweekly_rate = MultipartBody.Part.createFormData("weekly_rate", Constant.WeeklyPrice);
        MultipartBody.Part mpmonthly_rate = MultipartBody.Part.createFormData("monthly_rate", Constant.MonthlyPrice);
        MultipartBody.Part mpcheckin_time = MultipartBody.Part.createFormData("checkin_time", Constant.CheckInTime);
        MultipartBody.Part mpcheckout_time = MultipartBody.Part.createFormData("checkout_time", Constant.CheckOutTime);
        MultipartBody.Part mphire_photographer = MultipartBody.Part.createFormData("hire_photographer", Constant.HirePhotographer);
        MultipartBody.Part mpadditionalservice = MultipartBody.Part.createFormData("additional_services", String.valueOf(Constant.additionalServices));
        MultipartBody.Part mpcleaner_fee = MultipartBody.Part.createFormData("cleaner_fee", Constant.CleanerFee);
        MultipartBody.Part mplatitude = MultipartBody.Part.createFormData("latitude", Constant.Latitude);
        MultipartBody.Part mplongitude = MultipartBody.Part.createFormData("longitude", Constant.Longitude);

        subscription = restApi.createProperty(mpcategory_id,mpproperty_address,mpstreet_name,mpbuilding_name,mpcity_name,mpstate_name,mppost_code,mpcountry_name
                ,mpallowed_guest,mpallowed_anenities,mpproperty_title,mpproperty_highlights,mpproperty_details,mphourly_rate,mpdaily_rate,mpweekly_rate,
                mpmonthly_rate,mpcheckin_time,mpcheckout_time,mpadditionalservice,mphire_photographer,mpcleaner_fee,mplatitude,mplongitude,Constant.fileToUpload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.error(throwable));
                    }
                });
    }
    public  void UpdateProperty(int propertyid) {
        MultipartBody.Part mpcategory_id = MultipartBody.Part.createFormData("category_id", android.text.TextUtils.join(",", Constant.CategoryId));
        MultipartBody.Part mpproperty_address = MultipartBody.Part.createFormData("property_address", Constant.PropertyAddress);
        MultipartBody.Part mpstreet_name = MultipartBody.Part.createFormData("street_name", Constant.StreetName);
        MultipartBody.Part mpbuilding_name = MultipartBody.Part.createFormData("building_name", Constant.BuildingName);
        MultipartBody.Part mpcity_name = MultipartBody.Part.createFormData("city_name", Constant.CityName);
        MultipartBody.Part mpstate_name = MultipartBody.Part.createFormData("state_name", Constant.StateName);
        MultipartBody.Part mppost_code = MultipartBody.Part.createFormData("post_code", Constant.PostCode);
        MultipartBody.Part mpcountry_name = MultipartBody.Part.createFormData("country_name", Constant.CountryName);
        MultipartBody.Part mpallowed_guest = MultipartBody.Part.createFormData("allowed_guest", Constant.AllowedGuest);
        MultipartBody.Part mpallowed_anenities = MultipartBody.Part.createFormData("allowed_anenities", android.text.TextUtils.join(",", Constant.AmenitiesId));
        MultipartBody.Part mpproperty_title = MultipartBody.Part.createFormData("property_title", Constant.PropertyTitle);
        MultipartBody.Part mpproperty_highlights = MultipartBody.Part.createFormData("property_highlights",android.text.TextUtils.join(",", Constant.HighlightId));
        MultipartBody.Part mpproperty_details = MultipartBody.Part.createFormData("property_details", Constant.PropertyDetails);
        MultipartBody.Part mphourly_rate = MultipartBody.Part.createFormData("hourly_rate", Constant.HourlyPrice);
        MultipartBody.Part mpdaily_rate = MultipartBody.Part.createFormData("daily_rate", Constant.DailyPrice);
        MultipartBody.Part mpweekly_rate = MultipartBody.Part.createFormData("weekly_rate", Constant.WeeklyPrice);
        MultipartBody.Part mpmonthly_rate = MultipartBody.Part.createFormData("monthly_rate", Constant.MonthlyPrice);
        MultipartBody.Part mpcheckin_time = MultipartBody.Part.createFormData("checkin_time", Constant.CheckInTime);
        MultipartBody.Part mpcheckout_time = MultipartBody.Part.createFormData("checkout_time", Constant.CheckOutTime);
        MultipartBody.Part mphire_photographer = MultipartBody.Part.createFormData("hire_photographer", Constant.HirePhotographer);
        MultipartBody.Part mpadditionalservice = MultipartBody.Part.createFormData("additional_services", String.valueOf(Constant.additionalServices));
        MultipartBody.Part mpcleaner_fee = MultipartBody.Part.createFormData("cleaner_fee", Constant.CleanerFee);
        MultipartBody.Part mp_method = MultipartBody.Part.createFormData("_method", "PUT");
        MultipartBody.Part mplatitude = MultipartBody.Part.createFormData("latitude", Constant.Latitude);
        MultipartBody.Part mplongitude = MultipartBody.Part.createFormData("longitude", Constant.Longitude);

        subscription = restApi.UpdateProperty(propertyid,mpcategory_id,mpproperty_address,mpstreet_name,mpbuilding_name,mpcity_name,mpstate_name,mppost_code,mpcountry_name
                ,mpallowed_guest,mpallowed_anenities,mpproperty_title,mpproperty_highlights,mpproperty_details,mphourly_rate,mpdaily_rate,mpweekly_rate,
                mpmonthly_rate,mpcheckin_time,mpcheckout_time,mpadditionalservice,mphire_photographer,mpcleaner_fee,mp_method,mplatitude,mplongitude,Constant.fileToUpload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
