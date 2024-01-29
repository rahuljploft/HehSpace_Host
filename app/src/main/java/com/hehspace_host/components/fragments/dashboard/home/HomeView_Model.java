package com.hehspace_host.components.fragments.dashboard.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.DashBoardModel;
import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.PropertyListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeView_Model extends ViewModel {
    MutableLiveData<ApiResponse<PropertyListModel>> livedata = new MutableLiveData<>();
    MutableLiveData<ApiResponse<DashBoardModel>> livedatadashBoard = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> Commonlivedata = new MutableLiveData<>();
    ApiResponse<PropertyListModel> apiResponse = null;
    ApiResponse<DashBoardModel> apiResponse1 = null;
    ApiResponse<CommonModel> apiResponse2 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse2 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void getPropertyListing() {
        subscription = restApi.getPropertyListing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<PropertyListModel>() {
                    @Override
                    public void accept(PropertyListModel propertyListModel) throws Exception {
                        livedata.postValue(apiResponse.success(propertyListModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public  void dashBoard() {
        subscription = restApi.dashBoard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedatadashBoard.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<DashBoardModel>() {
                    @Override
                    public void accept(DashBoardModel propertyListModel) throws Exception {
                        livedatadashBoard.postValue(apiResponse1.success(propertyListModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedatadashBoard.postValue(apiResponse1.error(throwable));
                    }
                });
    }

    public  void acceptReject(HashMap<String,String> hashMap, int reqId) {
        subscription = restApi.acceptReject(hashMap,reqId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> Commonlivedata.postValue(apiResponse2.loading()))
                .subscribe(commonModel -> Commonlivedata.postValue(apiResponse2.success(commonModel)), throwable -> Commonlivedata.postValue(apiResponse2.error(throwable)));
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
