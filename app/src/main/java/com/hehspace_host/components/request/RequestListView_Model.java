package com.hehspace_host.components.request;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.NotificationModel;
import com.hehspace_host.model.RequestListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RequestListView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<RequestListModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> Commonlivedata = new MutableLiveData<>();
    ApiResponse<RequestListModel> apiResponse = null;
    ApiResponse<CommonModel> apiResponse1 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void getRequestList() {
        subscription = restApi.getRequestList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> livedata.postValue(apiResponse.loading()))
                .subscribe(notificationModel -> livedata.postValue(apiResponse.success(notificationModel)), throwable -> livedata.postValue(apiResponse.error(throwable)));
    }

    public  void acceptReject(HashMap<String,String> hashMap, int reqId) {
        subscription = restApi.acceptReject(hashMap,reqId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> Commonlivedata.postValue(apiResponse1.loading()))
                .subscribe(commonModel -> Commonlivedata.postValue(apiResponse1.success(commonModel)), throwable -> Commonlivedata.postValue(apiResponse1.error(throwable)));
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
