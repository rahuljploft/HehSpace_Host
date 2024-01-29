package com.hehspace_host.components.fragments.dashboard.inbox;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.hehspace_host.model.ChatUserListModel;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InboxView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<ChatUserListModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> livedata1 = new MutableLiveData<>();
    ApiResponse<ChatUserListModel> apiResponse = null;
    ApiResponse<CommonModel> apiResponse1 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }
    public  void getChatUserList() {
        subscription = restApi.getChatUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<ChatUserListModel>() {
                    @Override
                    public void accept(ChatUserListModel chatUserListModel) throws Exception {
                        livedata.postValue(apiResponse.success(chatUserListModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public  void deleteChat(HashMap<String,String> hashMap) {
        subscription = restApi.deleteChat(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata1.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel chatUserListModel) throws Exception {
                        livedata1.postValue(apiResponse1.success(chatUserListModel));
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