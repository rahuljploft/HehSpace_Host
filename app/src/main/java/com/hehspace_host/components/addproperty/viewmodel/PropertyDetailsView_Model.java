package com.hehspace_host.components.addproperty.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.PropertyHighlightModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PropertyDetailsView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<PropertyHighlightModel>> livedata = new MutableLiveData<>();
    ApiResponse<PropertyHighlightModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void getPropertyHighlights() {
        subscription = restApi.propertyHighlights()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> livedata.postValue(apiResponse.loading()))
                .subscribe(notificationModel -> livedata.postValue(apiResponse.success(notificationModel)),
                        throwable -> livedata.postValue(apiResponse.error(throwable)));
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
