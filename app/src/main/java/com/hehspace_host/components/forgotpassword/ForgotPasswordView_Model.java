package com.hehspace_host.components.forgotpassword;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.ForgotPasswordModel;
import com.hehspace_host.model.LoginModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordView_Model extends ViewModel {

    MutableLiveData<ApiResponse<ForgotPasswordModel>> forgotpasswordlivedata = new MutableLiveData<>();
    ApiResponse<ForgotPasswordModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void forgotPassword(HashMap<String, String> reqData) {
        subscription = restApi.forgotPassword(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        forgotpasswordlivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<ForgotPasswordModel>() {
                    @Override
                    public void accept(ForgotPasswordModel forgotPasswordModel) throws Exception {
                        forgotpasswordlivedata.postValue(apiResponse.success(forgotPasswordModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        forgotpasswordlivedata.postValue(apiResponse.error(throwable));
                    }
                });
    }


}
