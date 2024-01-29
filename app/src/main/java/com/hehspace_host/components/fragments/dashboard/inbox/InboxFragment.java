package com.hehspace_host.components.fragments.dashboard.inbox;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.chat.ChatActivity;
import com.hehspace_host.components.sidemenu.NotificationActivity;
import com.hehspace_host.databinding.FragmentInboxBinding;
import com.hehspace_host.databinding.ItemChatBinding;
import com.hehspace_host.model.ChatUserListModel;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseFragment;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InboxFragment extends BaseFragment {

    FragmentInboxBinding fragmentInboxBinding;
    InboxView_Model inboxView_model;
    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        fragmentInboxBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false);
        fragmentInboxBinding.setLifecycleOwner(this);
        inboxView_model = new InboxView_Model();
        return fragmentInboxBinding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        inboxView_model.livedata.observe(requireActivity(), modelApiResponse -> handleResult(modelApiResponse));
        inboxView_model.livedata1.observe(requireActivity(), modelApiResponse -> handleResultDelete(modelApiResponse));
        if (Uitility.isOnline(requireContext())) {
            inboxView_model.getChatUserList();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }
        fragmentInboxBinding.rvInbox.setHasFixedSize(true);
        fragmentInboxBinding.rvInbox.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        fragmentInboxBinding.srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Uitility.isOnline(requireContext())) {
                    inboxView_model.getChatUserList();
                } else {
                    Uitility.nointernetDialog(requireActivity());
                }

            }
        });

    }

    @Override
    protected void initializeOnCreateObject() {

    }

    @Override
    protected void setListeners() {
        fragmentInboxBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }

    private void handleResult(ApiResponse<ChatUserListModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                fragmentInboxBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {
                        if(result.getData().data.size()>0) {
                            ChatUserListAdapter chatUserListAdapter = new ChatUserListAdapter(requireContext(), result.getData().data);
                            fragmentInboxBinding.rvInbox.setAdapter(chatUserListAdapter);
                            fragmentInboxBinding.rvInbox.setVisibility(View.VISIBLE);
                            fragmentInboxBinding.cardNoBooking.setVisibility(View.GONE);
                        }
                        else {
                            fragmentInboxBinding.rvInbox.setVisibility(View.GONE);
                            fragmentInboxBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        }
                    } else {

                    }
                }
                break;
        }
    }
    private void handleResultDelete(ApiResponse<CommonModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {
                        if (Uitility.isOnline(requireContext())) {
                            inboxView_model.getChatUserList();
                        } else {
                            Uitility.nointernetDialog(requireActivity());
                        }
                    } else {
                        Toast.makeText(mActivity, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ViewHolder> {

        Context context;
        List<ChatUserListModel.DataEntity> list;
        public ChatUserListAdapter(Context context, List<ChatUserListModel.DataEntity> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public ChatUserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemChatBinding rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_chat, parent,
                    false);
            return new ChatUserListAdapter.ViewHolder(rowDrawerBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ChatUserListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context).load(list.get(position).userImageUrl).into(holder.itemRowBinding.ivImage);
            holder.itemRowBinding.username.setText(list.get(position).username);
            holder.itemRowBinding.tvHostName.setText(list.get(position).propertyName);
            holder.itemRowBinding.tvdate.setText(list.get(position).createdAt);
            holder.itemRowBinding.tvRequestNo.setText("Request id: "+list.get(position).requestNumber);

            holder.itemRowBinding.deleteText.setOnClickListener(v -> {
                deleteshowdialog(list.get(position).userId,list.get(position).requestId);
            });

            holder.itemRowBinding.layoutChat.setOnClickListener(v -> {
                context.startActivity(new Intent(context,ChatActivity.class)
                        .putExtra("userid",list.get(position).userId)
                        .putExtra("bookingid",list.get(position).bookingId)
                        .putExtra("requestid",list.get(position).requestId)
                        .putExtra("propertyid",list.get(position).propertyId)
                        .putExtra("name",list.get(position).propertyName)
                        .putExtra("property",list.get(position).username)
                        .putExtra("image",list.get(position).userImageUrl)
                );
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemChatBinding itemRowBinding;
            public ViewHolder(ItemChatBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public void deleteshowdialog(String user_id,String request_id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle("Confirm")
                .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                .setPositiveButton("Yes", (dialoginterface, i) -> {
                    dialoginterface.cancel();
                    if (Uitility.isOnline(requireContext())) {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("user_id",user_id);
                        hashMap.put("request_id",request_id);
                        inboxView_model.deleteChat(hashMap);
                    } else {
                        Uitility.nointernetDialog(requireActivity());
                    }
                }).show();
    }


}