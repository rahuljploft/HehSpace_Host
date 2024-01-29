package com.hehspace_host.components.addproperty.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_host.model.PropertyCategoryModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SelectCategoryView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<PropertyCategoryModel>> propertycategorylivedata = new MutableLiveData<>();
    ApiResponse<PropertyCategoryModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }
    public  void propertyCategory() {
        subscription = restApi.propertyCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<PropertyCategoryModel>() {
                    @Override
                    public void accept(PropertyCategoryModel propertyCategoryModel) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.success(propertyCategoryModel));
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