package com.hehspace_host.components.fragments.dashboard.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.ProfileModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;
import com.hehspace_host.util.Constant;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public  class ProfileView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<ProfileModel>> livedata = new MutableLiveData<>();
    ApiResponse<ProfileModel> apiResponse = null;
    public MutableLiveData<ApiResponse<ProfileModel>> updatelivedata = new MutableLiveData<>();
    ApiResponse<ProfileModel> updateapiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        updateapiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void getProfileDetails() {
        subscription = restApi.profileDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> livedata.postValue(apiResponse.loading()))
                .subscribe(notificationModel -> livedata.postValue(apiResponse.success(notificationModel)),
                        throwable -> livedata.postValue(apiResponse.error(throwable)));
    }
    public final void updateProfileDetails(String first_name, String last_name, String email_address, String mobile_number,
                                           String gender_id, File user_photo, File id_document) {
        MultipartBody.Part mp_first_name = MultipartBody.Part.createFormData("first_name", first_name);
        MultipartBody.Part mp_last_name = MultipartBody.Part.createFormData("last_name", last_name);
        MultipartBody.Part mp_email_address = MultipartBody.Part.createFormData("email_address", email_address);
        MultipartBody.Part mp_mobile_number = MultipartBody.Part.createFormData("mobile_number", mobile_number);
        MultipartBody.Part mp_gender_id = MultipartBody.Part.createFormData("gender_id", gender_id);
        MultipartBody.Part mp_user_type = MultipartBody.Part.createFormData("user_type", Constant.USERTYPE);
        MultipartBody.Part mp_method = MultipartBody.Part.createFormData("_method", "PUT");
        MultipartBody.Part mprofilephoto;
        MultipartBody.Part id_documentphoto;


        if (user_photo != null) {
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), user_photo);
            mprofilephoto = MultipartBody.Part.createFormData("user_photo", user_photo.getName(), requestBody1);
        } else {
            mprofilephoto = MultipartBody.Part.createFormData("user_photo", "");
        }


        if (id_document != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), id_document);
            id_documentphoto = MultipartBody.Part.createFormData("id_document", id_document.getName(), requestBody);
        } else {
            id_documentphoto = MultipartBody.Part.createFormData("id_document", "");
        }


        subscription = restApi.updateprofileDetails(mp_first_name,mp_last_name,mp_email_address,mp_mobile_number,mp_gender_id,mp_user_type
                ,mprofilephoto,id_documentphoto,mp_method)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        updatelivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<ProfileModel>() {
                    @Override
                    public void accept(ProfileModel profileModel) throws Exception {
                        updatelivedata.postValue(apiResponse.success(profileModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        updatelivedata.postValue(apiResponse.error(throwable));
                    }
                });
    }



    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
