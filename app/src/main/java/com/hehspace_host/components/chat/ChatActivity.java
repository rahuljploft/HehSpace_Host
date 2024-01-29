package com.hehspace_host.components.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityChatBinding;
import com.hehspace_host.databinding.ItemMessageBinding;
import com.hehspace_host.model.ChatModel;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.OnBottomReachedListener;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends BaseBindingActivity {

    ActivityChatBinding activityChatBinding;
    String userid = "", bookingid = "", requestid = "", propertyid = "";
    ChatView_Model view_model;
    int currentpage = 1;
    private boolean loading = false;
    private boolean refreshauto = true;
    private String arrayclearstatus = "";
    List<ChatModel.DataEntity> list = new ArrayList<>();
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    ChatListAdapter chatListAdapter;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    int firstVisibleItem;

    @Override
    protected void setBinding() {
        activityChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        view_model = new ChatView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        userid = getIntent().getStringExtra("userid");
        bookingid = getIntent().getStringExtra("bookingid");
        requestid = getIntent().getStringExtra("requestid");
        propertyid = getIntent().getStringExtra("propertyid");

        Glide.with(this).load(getIntent().getStringExtra("image")).into(activityChatBinding.ivImage);
        activityChatBinding.tvPropertyName.setText(getIntent().getStringExtra("property"));
        activityChatBinding.username.setText(getIntent().getStringExtra("name"));

        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        view_model.livedata1.observe(this, modelApiResponse -> handleResult11(modelApiResponse));
        view_model.livedatasendMessage.observe(this, modelApiResponse -> handleResultMessage(modelApiResponse));
        if (Uitility.isOnline(this)) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", userid);
            if (bookingid == null) {
                hashMap.put("booking_id", "");
            } else hashMap.put("booking_id", bookingid);
            hashMap.put("request_id", requestid);
            hashMap.put("property_id", propertyid);
            hashMap.put("page", "1");
            view_model.getchatList(hashMap);
        } else {
            Uitility.nointernetDialog(this);
        }

        activityChatBinding.spRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Uitility.isOnline(mActivity)) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_id", userid);
                    if (bookingid == null) {
                        hashMap.put("booking_id", "");
                    } else hashMap.put("booking_id", bookingid);
                    hashMap.put("request_id", requestid);
                    hashMap.put("property_id", propertyid);
                    hashMap.put("page", "1");
                    view_model.getchatList(hashMap);
                } else {
                    Uitility.nointernetDialog(mActivity);
                }
            }
        });
        chatListAdapter = new ChatListAdapter(this, list, sessionManager.getUSER_ID());
        list.clear();
        //activityChatBinding.rvChat.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        activityChatBinding.rvChat.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        activityChatBinding.rvChat.setAdapter(chatListAdapter);
//        activityChatBinding.rvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                if (!loading) {
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
//                        //bottom of list!
//                        currentpage = currentpage + 1;
//                        Log.d("Sdsdfa", currentpage + "");
//                        if (Uitility.isOnline(mActivity)) {
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("user_id", userid);
//                            if (bookingid == null) {
//                                hashMap.put("booking_id", "");
//                            } else hashMap.put("booking_id", bookingid);
//                            hashMap.put("request_id", requestid);
//                            hashMap.put("property_id", propertyid);
//                            hashMap.put("page", currentpage + "");
//                            view_model.getchatList1(hashMap);
//                        } else {
//                            Uitility.nointernetDialog(mActivity);
//                        }
//                        loading = true;
//                    }
//                }
//            }
//        });


        chatListAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                //your code goes here
                if (position != 0) {
                    Log.d("recycelveiwstatus", position + "");
                    Log.d("recycelveiwstatus", loading + "");
                    if (position >= 9) {
                        if (loading) {
                            loading = false;
                            currentpage = currentpage + 1;
                            if (Uitility.isOnline(mActivity)) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("user_id", userid);
                                if (bookingid == null) {
                                    hashMap.put("booking_id", "");
                                } else hashMap.put("booking_id", bookingid);
                                hashMap.put("request_id", requestid);
                                hashMap.put("property_id", propertyid);
                                hashMap.put("page", currentpage + "");
                                view_model.getchatList1(hashMap);
                            } else {
                                Uitility.nointernetDialog(mActivity);
                            }

                        }
                    }


                }

            }
        });

    }

    @Override
    protected void setListeners() {
        activityChatBinding.ivBack.setOnClickListener(this);
        activityChatBinding.ivSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
        if (view.getId() == R.id.ivSend) {
            if (TextUtils.isEmpty(activityChatBinding.etMessage.getText().toString())) {
                Toast.makeText(mActivity, "Please Enter Message", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Uitility.isOnline(this)) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", userid);
                if (bookingid == null) {
                    hashMap.put("booking_id", "");
                } else hashMap.put("booking_id", bookingid);
                hashMap.put("request_id", requestid);
                hashMap.put("page", "1");
                hashMap.put("message", activityChatBinding.etMessage.getText().toString());
                view_model.sendMessage(hashMap);
            } else {
                Uitility.nointernetDialog(this);
            }

        }
    }

    private void handleResult(ApiResponse<ChatModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                activityChatBinding.spRefresh.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {
                        loading = true;
                        list.clear();
                        list.addAll(result.getData().data);
                        if (list.size() > 0) {
                            chatListAdapter.notifyDataSetChanged();
                            activityChatBinding.rvChat.setVisibility(View.VISIBLE);
                            activityChatBinding.rlNoFound.setVisibility(View.GONE);
                        } else {
                            activityChatBinding.rvChat.setVisibility(View.GONE);
                            activityChatBinding.rlNoFound.setVisibility(View.VISIBLE);
                        }

                    } else {
                        activityChatBinding.rvChat.setVisibility(View.GONE);
                        activityChatBinding.rlNoFound.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    private void handleResult11(ApiResponse<ChatModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {
                        list.addAll(result.getData().data);
                        if (result.getData().data.size() > 0) {
                            loading = true;
                            chatListAdapter.notifyDataSetChanged();
                            //activityChatBinding.rvChat.setVisibility(View.VISIBLE);
                            ///activityChatBinding.rlNoFound.setVisibility(View.GONE);
                        }else {
                            loading = false;
                        }

                    }
                }
                break;
        }
    }

    private void handleResultMessage(ApiResponse<CommonModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    activityChatBinding.etMessage.setText("");
                    currentpage=1;
                    if (Uitility.isOnline(this)) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("user_id", userid);
                        if (bookingid == null) {
                            hashMap.put("booking_id", "");
                        } else hashMap.put("booking_id", bookingid);
                        hashMap.put("request_id", requestid);
                        hashMap.put("property_id", propertyid);
                        hashMap.put("page", "1");
                        view_model.getchatList(hashMap);
                    } else {
                        Uitility.nointernetDialog(this);
                    }
                }
                break;
        }
    }

    public  class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

        Context context;
        List<ChatModel.DataEntity> list;
        String userid;

        public ChatListAdapter(Context context, List<ChatModel.DataEntity> list, String userid) {
            this.context = context;
            this.list = list;
            this.userid = userid;
        }

        OnBottomReachedListener onBottomReachedListener;

        public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {

            this.onBottomReachedListener = onBottomReachedListener;
        }

        @Override
        public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemMessageBinding rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_message, parent,
                    false);
            return new ChatListAdapter.ViewHolder(rowDrawerBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (position == list.size() - 1) {
                onBottomReachedListener.onBottomReached(position);
            } else if (position == 0) {

                onBottomReachedListener.onBottomReached(position);
            }
            holder.itemRowBinding.tvSelfmsg.setText(list.get(position).message);
            holder.itemRowBinding.tvName.setText(list.get(position).toUser.firstName);
            holder.itemRowBinding.tvReceiverMsg.setText(list.get(position).message);
            if (list.get(position).fromUserId.equals(userid)) {
                holder.itemRowBinding.layoutSelf.setVisibility(View.VISIBLE);
                holder.itemRowBinding.layoutOther.setVisibility(View.GONE);
            } else {
                holder.itemRowBinding.layoutSelf.setVisibility(View.GONE);
                holder.itemRowBinding.layoutOther.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemMessageBinding itemRowBinding;

            public ViewHolder(ItemMessageBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


}