package com.hehspace_host.calender;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.DateListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyCalenderView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<DateListModel>> livedata = new MutableLiveData<>();
    ApiResponse<DateListModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;

    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public void getDateList(int reqData) {
        subscription = restApi.getDateList(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<DateListModel>() {
                    @Override
                    public void accept(DateListModel requestDetailsModel) throws Exception {
                        livedata.postValue(apiResponse.success(requestDetailsModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
