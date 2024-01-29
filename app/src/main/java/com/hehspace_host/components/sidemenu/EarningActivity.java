package com.hehspace_host.components.sidemenu;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.hehspace_host.R;
import com.hehspace_host.components.request.RequestDetailsActivity;
import com.hehspace_host.databinding.ActivityEarningBinding;
import com.hehspace_host.databinding.ItemViewCategoryBinding;
import com.hehspace_host.databinding.LayoutTransactionsBinding;
import com.hehspace_host.model.EarningModel;
import com.hehspace_host.model.RequestDetailsModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EarningActivity extends BaseBindingActivity {

    ActivityEarningBinding activityEarningBinding;
    EarningView_Model view_model;
    public  static List<EarningModel.TransactionDetail> list = new ArrayList<>();
    TransactionAdapter transactionAdapter;
    String from_date = "";
    String to_date = "";
    @Override
    protected void setBinding() {
        activityEarningBinding = DataBindingUtil.setContentView(this,R.layout.activity_earning);
        view_model = new EarningView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));

        if (Uitility.isOnline(this)) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("from_date",from_date);
            hashMap.put("to_date",to_date);
            view_model.walletDetails(hashMap);
        } else {
            Uitility.nointernetDialog(this);
        }

        activityEarningBinding.srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Uitility.isOnline(mActivity)) {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("from_date",from_date);
                    hashMap.put("to_date",to_date);
                    view_model.walletDetails(hashMap);
                } else {
                    Uitility.nointernetDialog(mActivity);
                }
            }
        });
        activityEarningBinding.rvTransactions.setHasFixedSize(true);
        activityEarningBinding.rvTransactions.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        transactionAdapter = new TransactionAdapter(this, new ItemClickListner() {
            @Override
            public void onClick(String type, int pos) {

            }
        });
        activityEarningBinding.rvTransactions.setAdapter(transactionAdapter);

        activityEarningBinding.layoutFrom.setOnClickListener(v -> {
            selectDate(activityEarningBinding.tvFromDate,"from");

        });
        activityEarningBinding.layoutTo.setOnClickListener(v -> {
            selectDate(activityEarningBinding.tvToDate,"to");
        });
    }


    public void check(){
        if(!from_date.equals("")){
            activityEarningBinding.tvTo.setVisibility(View.VISIBLE);
            activityEarningBinding.layoutTo.setVisibility(View.VISIBLE);
        }
    }
    public void selectDate(TextView selectDate,String value) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {

                    selectDate.setText(dayOfMonth+ "/" + (monthOfYear + 1) + "/" +year  );
                    if(value.equals("from")){
                        from_date = dayOfMonth+ "/" + (monthOfYear + 1) + "/" +year;
                        check();
                    }
                    else {
                        to_date = dayOfMonth+ "/" + (monthOfYear + 1) + "/" +year ;
                        if (Uitility.isOnline(EarningActivity.this)) {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("from_date",from_date);
                            hashMap.put("to_date",to_date);
                            view_model.walletDetails(hashMap);
                        } else {
                            Uitility.nointernetDialog(EarningActivity.this);
                        }
                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    @Override
    protected void setListeners() {
        activityEarningBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    private void handleResult(ApiResponse<EarningModel> result) {
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
                activityEarningBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        activityEarningBinding.tvTotalEarnings.setText(result.getData().data.totalEarning);
                        activityEarningBinding.tvTodaysEarning.setText(result.getData().data.todayEarning);
                        list = result.getData().data.transactionDetail;
                        if(list.size()>0){
                            transactionAdapter.notifyDataSetChanged();
                            activityEarningBinding.rvTransactions.setVisibility(View.VISIBLE);
                            activityEarningBinding.cardNoData.setVisibility(View.GONE);
                        }
                        else {
                            activityEarningBinding.rvTransactions.setVisibility(View.GONE);
                            activityEarningBinding.cardNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else{

                    }
                }
                break;
        }
    }

    public static class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
        Context context;
        ItemClickListner itemClickListner;
        public TransactionAdapter(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }
        @Override
        public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutTransactionsBinding itemViewCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_transactions, parent,
                    false);
            return new TransactionAdapter.ViewHolder(itemViewCategoryBinding);
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                          holder.itemRowBinding.tvBoookingId.setText("Booking Id: "+list.get(position).booking_number);
                          holder.itemRowBinding.tvDate.setText(list.get(position).transaction_date);
                          holder.itemRowBinding.tvPrice.setText(list.get(position).transaction_amount);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutTransactionsBinding itemRowBinding;
            public ViewHolder(LayoutTransactionsBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }
}