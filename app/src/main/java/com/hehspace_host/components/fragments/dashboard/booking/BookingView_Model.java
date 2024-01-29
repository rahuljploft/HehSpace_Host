package com.hehspace_host.components.fragments.dashboard.booking;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.BookingListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BookingView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<BookingListModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<BookingListModel>> livedata1 = new MutableLiveData<>();
    ApiResponse<BookingListModel> apiResponse = null;
    ApiResponse<BookingListModel> apiResponse1 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void getBookingList() {
        subscription = restApi.getBookingList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<BookingListModel>() {
                    @Override
                    public void accept(BookingListModel notificationModel) throws Exception {
                        livedata.postValue(apiResponse.success(notificationModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }
    public  void checkavailbook(HashMap<String,String> hashMap) {
        subscription = restApi.checkavailbook(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata1.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<BookingListModel>() {
                    @Override
                    public void accept(BookingListModel notificationModel) throws Exception {
                        livedata1.postValue(apiResponse1.success(notificationModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata1.postValue(apiResponse1.error(throwable));
                    }
                });
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}