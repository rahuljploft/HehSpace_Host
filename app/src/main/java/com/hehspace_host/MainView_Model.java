package com.hehspace_host;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.EarningModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<CommonModel>> livedata = new MutableLiveData<>();
    ApiResponse<CommonModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void submitPhotographer(HashMap<String, String> reqData) {
        subscription = restApi.submitPhotographer(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> livedata.postValue(apiResponse.loading()))
                .subscribe(notificationModel -> livedata.postValue(apiResponse.success(notificationModel)), throwable -> livedata.postValue(apiResponse.error(throwable)));
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}