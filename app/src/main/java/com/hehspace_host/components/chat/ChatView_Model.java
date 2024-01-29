package com.hehspace_host.components.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.hehspace_host.model.ChatModel;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.network.RestApi;
import com.hehspace_host.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<ChatModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> livedatasendMessage = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<ChatModel>> livedata1 = new MutableLiveData<>();
    ApiResponse<ChatModel> apiResponse = null;
    ApiResponse<CommonModel> apiResponse1 = null;
    ApiResponse<ChatModel> apiResponse11 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;

    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse11 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public void getchatList(HashMap<String, String> reqData) {
        subscription = restApi.chatList(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<ChatModel>() {
                    @Override
                    public void accept(ChatModel chatModel) throws Exception {
                        livedata.postValue(apiResponse.success(chatModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public void getchatList1(HashMap<String, String> reqData) {
        subscription = restApi.chatList(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata1.postValue(apiResponse11.loading());
                    }
                })
                .subscribe(new Consumer<ChatModel>() {
                    @Override
                    public void accept(ChatModel chatModel) throws Exception {
                        livedata1.postValue(apiResponse11.success(chatModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata1.postValue(apiResponse11.error(throwable));
                    }
                });
    }


    public void sendMessage(HashMap<String, String> reqData) {
        subscription = restApi.sendMessage(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedatasendMessage.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel chatModel) throws Exception {
                        livedatasendMessage.postValue(apiResponse1.success(chatModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedatasendMessage.postValue(apiResponse1.error(throwable));
                    }
                });
    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
