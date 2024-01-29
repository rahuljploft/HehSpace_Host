package com.hehspace_host.components.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.PageModel;
import com.hehspace_host.model.VerifyOtpModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.io.File;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<VerifyOtpModel>> verifylivedata = new MutableLiveData<>();
    ApiResponse<VerifyOtpModel> verifyapiResponse = null;

    public MutableLiveData<ApiResponse<PageModel>> livedata = new MutableLiveData<>();
    ApiResponse<PageModel> apiResponsepage = null;

    public MutableLiveData<ApiResponse<LoginModel>> registerlivedata = new MutableLiveData<>();
    ApiResponse<LoginModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        verifyapiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponsepage = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void verifyOTP(HashMap<String, String> reqData) {
        subscription = restApi.verifyOTP(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        verifylivedata.postValue(verifyapiResponse.loading());
                    }
                })
                .subscribe(new Consumer<VerifyOtpModel>() {
                    @Override
                    public void accept(VerifyOtpModel verifyOtpModel) throws Exception {
                        verifylivedata.postValue(verifyapiResponse.success(verifyOtpModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        verifylivedata.postValue(verifyapiResponse.error(throwable));
                    }
                });
    }

    public  void getPages() {
        subscription = restApi.getPages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponsepage.loading());
                    }
                })
                .subscribe(new Consumer<PageModel>() {
                    @Override
                    public void accept(PageModel commonModel) throws Exception {
                        livedata.postValue(apiResponsepage.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponsepage.error(throwable));
                    }
                });
    }



    public final void createAccount(String first_name, String last_name, String email_address, String mobile_number, String gender_id, String user_password,
                                     String social_media_id, String user_type,File id_document,String nofile,String device_token,boolean isSocial) {
        MultipartBody.Part mp_first_name = MultipartBody.Part.createFormData("first_name", first_name);
        MultipartBody.Part mp_last_name = MultipartBody.Part.createFormData("last_name", last_name);
        MultipartBody.Part mp_email_address = MultipartBody.Part.createFormData("email_address", email_address);
        MultipartBody.Part mp_mobile_number = null;
        if(!isSocial){
            mp_mobile_number = MultipartBody.Part.createFormData("mobile_number", mobile_number);
        }
        MultipartBody.Part mp_gender_id = MultipartBody.Part.createFormData("gender_id", gender_id);
        MultipartBody.Part mp_user_password = MultipartBody.Part.createFormData("user_password", user_password);

        MultipartBody.Part mp_social_media_id = MultipartBody.Part.createFormData("social_media_id", social_media_id);
        MultipartBody.Part mp_user_type = MultipartBody.Part.createFormData("user_type", user_type);
        MultipartBody.Part mp_device_token = MultipartBody.Part.createFormData("device_token", device_token);
        MultipartBody.Part mprofilephoto;
        File file = new File(nofile);

        if (!nofile.isEmpty()) {
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
            mprofilephoto = MultipartBody.Part.createFormData("id_document", file.getName(), requestBody1);
        } else mprofilephoto = MultipartBody.Part.createFormData("id_document", "");


        subscription = restApi.createAccount(mp_first_name,mp_last_name,mp_email_address,mp_mobile_number,mp_gender_id,mp_user_password,
                mp_social_media_id,mp_user_type,mprofilephoto,mp_device_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        registerlivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<LoginModel>() {
                    @Override
                    public void accept(LoginModel loginModel) throws Exception {
                        registerlivedata.postValue(apiResponse.success(loginModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        registerlivedata.postValue(apiResponse.error(throwable));
                    }
                });
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }



}
