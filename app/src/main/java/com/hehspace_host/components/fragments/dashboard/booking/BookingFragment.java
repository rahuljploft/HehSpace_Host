package com.hehspace_host.components.fragments.dashboard.booking;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.calender.MyCalendarActivity;
import com.hehspace_host.components.booking.BookingDetailsActivity;
import com.hehspace_host.components.fragments.dashboard.home.HomeFragment;
import com.hehspace_host.components.request.RequestDetailsActivity;
import com.hehspace_host.databinding.FragmentBookingBinding;
import com.hehspace_host.databinding.ItemBookingBinding;
import com.hehspace_host.databinding.MyCalendarViewBinding;
import com.hehspace_host.model.BookingListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseFragment;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingFragment extends BaseFragment {

    FragmentBookingBinding fragmentBookingBinding;
    BookingAdapter bookingAdapter;
    BookingView_Model bookingView_model;
    String my = "";
    public static List<BookingListModel.DataEntity> oldlist = new ArrayList<>();
    public static List<BookingListModel.DataEntity> finallist = new ArrayList<>();
    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        fragmentBookingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking, container, false);
        fragmentBookingBinding.setLifecycleOwner(this);
        bookingView_model = new BookingView_Model();
        return fragmentBookingBinding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        Bundle bundle = getArguments();
        assert bundle != null;
        my = bundle.getString("my");


        if(my.equals("no")){
            if (Uitility.isOnline(requireActivity())) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("property_id", HomeFragment.propid);
                hashMap.put("checkin_date", MyCalendarActivity.finaldate);
                bookingView_model.checkavailbook(hashMap);
            } else {
                Uitility.nointernetDialog(requireActivity());
            }

        }
        else {
            if (Uitility.isOnline(requireActivity())) {
                bookingView_model.getBookingList();
            } else {
                Uitility.nointernetDialog(requireActivity());
            }
        }

        fragmentBookingBinding.srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(my.equals("no")){
                    if (Uitility.isOnline(requireActivity())) {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("property_id", HomeFragment.propid);
                        hashMap.put("checkin_date", MyCalendarActivity.finaldate);
                        bookingView_model.checkavailbook(hashMap);
                    } else {
                        Uitility.nointernetDialog(requireActivity());
                    }

                }
                else {
                    if (Uitility.isOnline(requireActivity())) {
                        bookingView_model.getBookingList();
                    } else {
                        Uitility.nointernetDialog(requireActivity());
                    }
                }

            }
        });
        bookingView_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        bookingView_model.livedata1.observe(this, modelApiResponse -> handleResult1(modelApiResponse));

        fragmentBookingBinding.rvBooking.setHasFixedSize(true);
        fragmentBookingBinding.rvBooking.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));

        bookingAdapter = new BookingAdapter(mActivity, (type, pos) -> {
            if(type.equals("view")) {
                startActivity(new Intent(mActivity, BookingDetailsActivity.class)
                        .putExtra("bookingid",finallist.get(pos).bookingId)
                        .putExtra("bookingstatus",finallist.get(pos).bookingStatus)
                );
            }
        });
        fragmentBookingBinding.rvBooking.setAdapter(bookingAdapter);

        fragmentBookingBinding.layoutAll.setOnClickListener(v -> {
            finallist.clear();
            fragmentBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.app_btn));
            fragmentBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.white));
            fragmentBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.black));
            finallist.addAll(oldlist);
            if(finallist.size()>0){
                fragmentBookingBinding.cardNoBooking.setVisibility(View.GONE);
                fragmentBookingBinding.srlayout.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            }
            else {
                fragmentBookingBinding.srlayout.setVisibility(View.GONE);
                fragmentBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });

        fragmentBookingBinding.layoutUpcoming.setOnClickListener(v -> {
            finallist.clear();
            fragmentBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.app_btn));
            fragmentBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.white));
            fragmentBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.black));
            for (int i =0; i<oldlist.size();i++){
                if(oldlist.get(i).bookingStatus.equals("CONFIRMED")){
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist",finallist.size()+"");
            if(finallist.size()>0){
                fragmentBookingBinding.cardNoBooking.setVisibility(View.GONE);
                fragmentBookingBinding.srlayout.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            }
            else {
                fragmentBookingBinding.srlayout.setVisibility(View.GONE);
                fragmentBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
        fragmentBookingBinding.layoutCompleted.setOnClickListener(v -> {
            finallist.clear();
            fragmentBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.app_btn));
            fragmentBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.white));
            fragmentBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.black));
            for (int i =0; i<oldlist.size();i++){
                if(oldlist.get(i).bookingStatus.equals("COMPLETED")){
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist",finallist.size()+"");
            if(finallist.size()>0){
                fragmentBookingBinding.cardNoBooking.setVisibility(View.GONE);
                fragmentBookingBinding.srlayout.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            }
            else {
                fragmentBookingBinding.srlayout.setVisibility(View.GONE);
                fragmentBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
        fragmentBookingBinding.layoutCancle.setOnClickListener(v -> {
            finallist.clear();
            fragmentBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            fragmentBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.app_btn));
            fragmentBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
            fragmentBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.white));
            for (int i =0; i<oldlist.size();i++){
                if(oldlist.get(i).bookingStatus.equals("CANCELLED")){
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist",finallist.size()+"");
            if(finallist.size()>0){
                fragmentBookingBinding.cardNoBooking.setVisibility(View.GONE);
                fragmentBookingBinding.srlayout.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            }
            else {
                fragmentBookingBinding.srlayout.setVisibility(View.GONE);
                fragmentBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void initializeOnCreateObject() {

    }

    @Override
    protected void setListeners() {
        fragmentBookingBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
                startActivity(new Intent(requireContext(), MainActivity.class));
        }
    }

    private void handleResult(ApiResponse<BookingListModel> result) {
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
                fragmentBookingBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        oldlist.clear();
                        oldlist = result.getData().data;
                        finallist.clear();
                        finallist.addAll(oldlist);
                        if(oldlist.size()>0){
                            fragmentBookingBinding.cardNoBooking.setVisibility(View.GONE);
                            fragmentBookingBinding.srlayout.setVisibility(View.VISIBLE);
                            bookingAdapter.notifyDataSetChanged();
                        }
                        else {
                            fragmentBookingBinding.srlayout.setVisibility(View.GONE);
                            fragmentBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        }
                    }
                    else{

                    }
                }
                break;
        }
    }
    private void handleResult1(ApiResponse<BookingListModel> result) {
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
                fragmentBookingBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        oldlist.clear();
                        oldlist = result.getData().data;
                        finallist.clear();
                        finallist.addAll(oldlist);
                        if(oldlist.size()>0){
                            fragmentBookingBinding.cardNoBooking.setVisibility(View.GONE);
                            fragmentBookingBinding.srlayout.setVisibility(View.VISIBLE);
                            bookingAdapter.notifyDataSetChanged();
                        }
                        else {
                            fragmentBookingBinding.srlayout.setVisibility(View.GONE);
                            fragmentBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        }
                    }
                    else{

                    }
                }
                break;
        }
    }


    public static class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public BookingAdapter(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public BookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemBookingBinding itemBookingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_booking, parent,
                    false);
            return new BookingAdapter.ViewHolder(itemBookingBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.llMain.setOnClickListener(v -> {
                itemClickListner.onClick("view",position);
            });
            holder.itemRowBinding.tvStatus.setVisibility(View.VISIBLE);
            holder.itemRowBinding.tvStatus.setText(finallist.get(position).bookingStatus);
            if(finallist.get(position).bookingStatus.equals("COMPLETED")){
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
            }
            else if(finallist.get(position).bookingStatus.equals("UPCOMING")){
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.upcoming_bg));
            }
            else if(finallist.get(position).bookingStatus.equals("CONFIRMED")){
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.upcoming_bg));
            }
            else if(finallist.get(position).bookingStatus.equals("CANCELLED")){
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
            }
            else holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.yellow_bg));
            Glide.with(context)
                    .load((finallist.get(position).propertyImageUrl))
                    .error(R.drawable.logo_host)
                    .into(holder.itemRowBinding.ivImage);
            holder.itemRowBinding.tvRequestNo.setText("Booking No: "+finallist.get(position).bookingNumber);
            holder.itemRowBinding.tvTitle.setText(finallist.get(position).propertyTitle);
            holder.itemRowBinding.tvName.setText(finallist.get(position).fullName);
            holder.itemRowBinding.tvDate.setText(finallist.get(position).checkinDate+" to "+finallist.get(position).checkoutDate);
            holder.itemRowBinding.tvTime.setText(finallist.get(position).checkinTime+" to "+finallist.get(position).checkoutTime);


        }

        @Override
        public int getItemCount() {
            return finallist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemBookingBinding itemRowBinding;

            public ViewHolder(ItemBookingBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

}