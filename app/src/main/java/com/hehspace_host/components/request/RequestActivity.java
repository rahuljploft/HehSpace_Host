package com.hehspace_host.components.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.chat.ChatActivity;
import com.hehspace_host.components.fragments.dashboard.home.HomeFragment;
import com.hehspace_host.components.sidemenu.NotificationActivity;
import com.hehspace_host.databinding.ActivityRequestBinding;
import com.hehspace_host.databinding.ItemRequestBinding;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.NotificationModel;
import com.hehspace_host.model.RequestListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestActivity extends BaseBindingActivity {

    ActivityRequestBinding activityRequestBinding;
    RequestAdapter requestAdapter;
    RequestListView_Model requestListView_model;
    public static List<RequestListModel.DataEntity> oldlist = new ArrayList<>();
    public static List<RequestListModel.DataEntity> finallist = new ArrayList<>();

    @Override
    protected void setBinding() {
        activityRequestBinding = DataBindingUtil.setContentView(this,R.layout.activity_request);
        requestListView_model = new RequestListView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
    mActivity = this;
    }

    @SuppressLint({"UseCompatLoadingForDrawables"})
    @Override
    protected void initializeObject() {
        requestListView_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        requestListView_model.Commonlivedata.observe(this, modelApiResponse -> handleResultChangeStatus(modelApiResponse));
        if (Uitility.isOnline(this)) {
            requestListView_model.getRequestList();
        } else {
            Uitility.nointernetDialog(this);
        }

        activityRequestBinding.srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activityRequestBinding.layoutNew.setBackground(getResources().getDrawable(R.drawable.app_btn));
                activityRequestBinding.layoutAccept.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                activityRequestBinding.layoutReject.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                activityRequestBinding.tvNew.setTextColor(getResources().getColor(R.color.white));
                activityRequestBinding.tvAccept.setTextColor(getResources().getColor(R.color.black));
                activityRequestBinding.tvReject.setTextColor(getResources().getColor(R.color.black));
                if (Uitility.isOnline(mActivity)) {
                    requestListView_model.getRequestList();
                } else {
                    Uitility.nointernetDialog(mActivity);
                }
            }
        });
        activityRequestBinding.rvRequest.setHasFixedSize(true);
        activityRequestBinding.rvRequest.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        finallist.clear();
        requestAdapter = new RequestAdapter(this, (type, pos) -> {
            if(type.equals("view")){
                startActivity(new Intent(this,RequestDetailsActivity.class)
                        .putExtra("reqid",finallist.get(pos).requestId)
                        .putExtra("status",finallist.get(pos).requestStatus)
                );
            }
            if(type.equals("accept")){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Confirm")
                        .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                        .setPositiveButton("Yes", (dialoginterface, i) -> {
                            if (Uitility.isOnline(this)) {
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("request_status","209");
                                hashMap.put("_method","PUT");
                                requestListView_model.acceptReject(hashMap,Integer.parseInt(finallist.get(pos).requestId));
                            } else {
                                Uitility.nointernetDialog(this);
                            }
                        }).show();
            }
            if(type.equals("reject")){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Confirm")
                        .setMessage("Do you want to reject this?")
                        .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                        .setPositiveButton("Yes", (dialoginterface, i) -> {
                            if (Uitility.isOnline(this)) {
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("request_status","210");
                                hashMap.put("_method","PUT");
                                requestListView_model.acceptReject(hashMap,Integer.parseInt(finallist.get(pos).requestId));
                            } else {
                                Uitility.nointernetDialog(this);
                            }
                        }).show();
            }
        });
        activityRequestBinding.rvRequest.setAdapter(requestAdapter);

        activityRequestBinding.layoutNew.setOnClickListener(v -> {
            finallist.clear();
            activityRequestBinding.layoutNew.setBackground(getResources().getDrawable(R.drawable.app_btn));
            activityRequestBinding.layoutAccept.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityRequestBinding.layoutReject.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityRequestBinding.tvNew.setTextColor(getResources().getColor(R.color.white));
            activityRequestBinding.tvAccept.setTextColor(getResources().getColor(R.color.black));
            activityRequestBinding.tvReject.setTextColor(getResources().getColor(R.color.black));
            for (int i =0; i<oldlist.size();i++){
                if(oldlist.get(i).requestStatus.equals("PENDING")){
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist",finallist.size()+"");
            if(finallist.size()>0){
                activityRequestBinding.cardNoBooking.setVisibility(View.GONE);
                activityRequestBinding.rvRequest.setVisibility(View.VISIBLE);
                requestAdapter.notifyDataSetChanged();
            }
            else {
                activityRequestBinding.rvRequest.setVisibility(View.GONE);
                activityRequestBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
        activityRequestBinding.layoutAccept.setOnClickListener(v -> {
            finallist.clear();
            activityRequestBinding.layoutNew.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityRequestBinding.layoutAccept.setBackground(getResources().getDrawable(R.drawable.app_btn));
            activityRequestBinding.layoutReject.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityRequestBinding.tvNew.setTextColor(getResources().getColor(R.color.black));
            activityRequestBinding.tvAccept.setTextColor(getResources().getColor(R.color.white));
            activityRequestBinding.tvReject.setTextColor(getResources().getColor(R.color.black));
            for (int i =0; i<oldlist.size();i++){
                if(oldlist.get(i).requestStatus.equals("ACCEPTED")){
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist",finallist.size()+"");
            if(finallist.size()>0){
                activityRequestBinding.cardNoBooking.setVisibility(View.GONE);
                activityRequestBinding.rvRequest.setVisibility(View.VISIBLE);
                requestAdapter.notifyDataSetChanged();
            }
            else {
                activityRequestBinding.rvRequest.setVisibility(View.GONE);
                activityRequestBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
        activityRequestBinding.layoutReject.setOnClickListener(v -> {
            finallist.clear();
            activityRequestBinding.layoutNew.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityRequestBinding.layoutAccept.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityRequestBinding.layoutReject.setBackground(getResources().getDrawable(R.drawable.app_btn));
            activityRequestBinding.tvNew.setTextColor(getResources().getColor(R.color.black));
            activityRequestBinding.tvAccept.setTextColor(getResources().getColor(R.color.black));
            activityRequestBinding.tvReject.setTextColor(getResources().getColor(R.color.white));
            for (int i =0; i<oldlist.size();i++){
                if(oldlist.get(i).requestStatus.equals("REJECTED")){
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist",finallist.size()+"");
            if(finallist.size()>0){
                activityRequestBinding.cardNoBooking.setVisibility(View.GONE);
                activityRequestBinding.rvRequest.setVisibility(View.VISIBLE);
                requestAdapter.notifyDataSetChanged();
            }
            else {
                activityRequestBinding.rvRequest.setVisibility(View.GONE);
                activityRequestBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void setListeners() {
        activityRequestBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void handleResult(ApiResponse<RequestListModel> result) {
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
                activityRequestBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        oldlist.clear();
                        oldlist = result.getData().data;
                        finallist.clear();
                        for (int i =0; i<oldlist.size();i++){
                            if(oldlist.get(i).requestStatus.equals("PENDING")){
                                finallist.add(oldlist.get(i));
                            }
                        }
                        Log.e("finallist",finallist.size()+"");
                        if(finallist.size()>0){
                            activityRequestBinding.cardNoBooking.setVisibility(View.GONE);
                            activityRequestBinding.rvRequest.setVisibility(View.VISIBLE);
                            requestAdapter.notifyDataSetChanged();
                        }
                        else {
                            activityRequestBinding.rvRequest.setVisibility(View.GONE);
                            activityRequestBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        }

                    }
                    else{
                        activityRequestBinding.rvRequest.setVisibility(View.GONE);
                        activityRequestBinding.cardNoBooking.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    private void handleResultChangeStatus(ApiResponse<CommonModel> result) {
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
                    if(result.getData().status.equals("true")){
                        if (Uitility.isOnline(this)) {
                            requestListView_model.getRequestList();
                        } else {
                            Uitility.nointernetDialog(this);
                        }
                    }
                    else{

                    }
                }
                break;
        }
    }



    public static class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public RequestAdapter(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemRequestBinding itemRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_request, parent,
                    false);
            return new RequestAdapter.ViewHolder(itemRequestBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvTitle.setText(finallist.get(position).propertyTitle);
            holder.itemRowBinding.tvrequestId.setText("Request No: "+finallist.get(position).requestNumber);
            holder.itemRowBinding.tvCustomername.setText(finallist.get(position).fullName);
            holder.itemRowBinding.tvrequestedDate.setText(finallist.get(position).requestedDate);
            holder.itemRowBinding.tvRequestedTime.setText(finallist.get(position).requestedTime);
            holder.itemRowBinding.cardRequest.setOnClickListener(v -> itemClickListner.onClick("view",position));
            holder.itemRowBinding.tvAccept.setOnClickListener(v -> itemClickListner.onClick("accept",position));
            holder.itemRowBinding.tvReject.setOnClickListener(v -> itemClickListner.onClick("reject",position));
            Glide.with(context)
                    .load((finallist.get(position).propertyImageUrl))
                    .error(R.drawable.logo_host)
                    .into(holder.itemRowBinding.ivImage);

            if(finallist.get(position).requestStatus.equals("REJECTED")){
                    holder.itemRowBinding.clickRel.setVisibility(View.GONE);
                    holder.itemRowBinding.view.setVisibility(View.GONE);
                    holder.itemRowBinding.tvStatus.setText(finallist.get(position).requestStatus);
                    holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
                     holder.itemRowBinding.tvStatus.setVisibility(View.VISIBLE );
            }
            else if(finallist.get(position).requestStatus.equals("ACCEPTED")){
                    holder.itemRowBinding.clickRel.setVisibility(View.GONE);
                    holder.itemRowBinding.view.setVisibility(View.GONE);
                    holder.itemRowBinding.tvStatus.setText(finallist.get(position).requestStatus);
                    holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
                holder.itemRowBinding.tvStatus.setVisibility(View.VISIBLE );
            }
            else {
                holder.itemRowBinding.clickRel.setVisibility(View.VISIBLE);
                holder.itemRowBinding.view.setVisibility(View.VISIBLE);
                holder.itemRowBinding.tvStatus.setVisibility(View.GONE);
            }
            holder.itemRowBinding.layoutChat.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ChatActivity.class)
                        .putExtra("userid",finallist.get(position).userId)
                        .putExtra("bookingid","")
                        .putExtra("requestid",finallist.get(position).requestId)
                        .putExtra("propertyid",finallist.get(position).propertyId)
                        .putExtra("name",finallist.get(position).propertyTitle)
                        .putExtra("property",finallist.get(position).fullName)
                        .putExtra("image",finallist.get(position).userImageUrl)
                );
            });


        }

        @Override
        public int getItemCount() {
            return finallist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemRequestBinding itemRowBinding;

            public ViewHolder(ItemRequestBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


}