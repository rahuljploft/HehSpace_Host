package com.hehspace_host.components.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hehspace_host.R;
import com.hehspace_host.components.chat.ChatActivity;
import com.hehspace_host.components.request.RequestActivity;
import com.hehspace_host.components.request.RequestDetailsActivity;
import com.hehspace_host.databinding.ActivityBookingBinding;
import com.hehspace_host.databinding.ActivityBookingDetailsBinding;
import com.hehspace_host.databinding.ItemBookingBinding;
import com.hehspace_host.databinding.ItemRequestBinding;
import com.hehspace_host.databinding.ItemViewAddonsRequestBinding;
import com.hehspace_host.model.BookingDetailsModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.ArrayList;
import java.util.List;

public class BookingDetailsActivity extends BaseBindingActivity {

    ActivityBookingDetailsBinding activityBookingBinding;
    BookingDetailView_Model view_model;
    public static List<BookingDetailsModel.AddonServicesEntity> list = new ArrayList<>();
    AddonAdapters addonAdapters;
    String property_id = "";
    String userid = "";
    String bookingid = "";
    String requestid = "";
    String name = "";
    String property = "";
    String image = "";
    String mobile = "";

    @Override
    protected void setBinding() {
        activityBookingBinding = DataBindingUtil.setContentView(this,R.layout.activity_booking_details);
        view_model = new BookingDetailView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
    mActivity = this;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.getBookingDetails(Integer.parseInt(getIntent().getStringExtra("bookingid")));
        } else {
            Uitility.nointernetDialog(this);
        }
        activityBookingBinding.rvAddon.setHasFixedSize(true);
        activityBookingBinding.rvAddon.setLayoutManager(new GridLayoutManager(this, 2));
        addonAdapters = new AddonAdapters(this, (type, pos) -> {

        });
        activityBookingBinding.rvAddon.setAdapter(addonAdapters);

    }

    @Override
    protected void setListeners() {
        activityBookingBinding.ivBack.setOnClickListener(this);
        activityBookingBinding.ivChat.setOnClickListener(this);
        activityBookingBinding.ivCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
        if(view.getId() == R.id.ivChat){
            startActivity(new Intent(this, ChatActivity.class)
                    .putExtra("userid",userid)
                    .putExtra("bookingid",bookingid)
                    .putExtra("requestid",requestid)
                    .putExtra("propertyid",property_id)
                    .putExtra("name",name)
                    .putExtra("property",property)
                    .putExtra("image",image)
            );

        }
        if(view.getId() == R.id.ivCall){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+mobile));
            startActivity(intent);
        }
    }

    public static class AddonAdapters extends RecyclerView.Adapter<AddonAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public AddonAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public AddonAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewAddonsRequestBinding itemViewAddonsRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_addons_request, parent,
                    false);
            return new AddonAdapters.ViewHolder(itemViewAddonsRequestBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AddonAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return 4;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewAddonsRequestBinding itemRowBinding;

            public ViewHolder(ItemViewAddonsRequestBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    private void handleResult(ApiResponse<BookingDetailsModel> result) {
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
                        Glide.with(this)
                                .load((result.getData().data.propertyImageUrl))
                                .error(R.drawable.logo_host)
                                .into(activityBookingBinding.ivImage);

                        activityBookingBinding.tvStatus.setText(getIntent().getStringExtra("bookingstatus"));
                        switch (getIntent().getStringExtra("bookingstatus")) {
                            case "COMPLETED":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.green_bg));
                                break;
                            case "UPCOMING":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.upcoming_bg));
                                break;
                            case "CONFIRMED":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.upcoming_bg));
                                break;
                            case "CANCELLED":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.red_bg));
                                break;
                            default:
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.yellow_bg));
                                break;
                        }

                        activityBookingBinding.tvTitle.setText(result.getData().data.propertyTitle);
                        activityBookingBinding.tvLocation.setText(result.getData().data.propertyLocation);
                        activityBookingBinding.tvReview.setText(result.getData().data.propertyRatting+"("+result.getData().data.propertyReview+")");

                        property_id = result.getData().data.propertyId;
                        userid = result.getData().data.userId;
                        bookingid = result.getData().data.bookingId;
                        requestid = result.getData().data.requestId;
                        name = result.getData().data.propertyTitle;
                        property = result.getData().data.fullName;
                        image = result.getData().data.userImageUrl;
                        mobile = result.getData().data.userMobile;

                        if(list.size()>0){
                            list = result.getData().data.addonServices;
                            addonAdapters.notifyDataSetChanged();
                            activityBookingBinding.rvAddon.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvadoontitle.setVisibility(View.VISIBLE);
                        }
                        else {
                            activityBookingBinding.rvAddon.setVisibility(View.GONE);
                            activityBookingBinding.tvadoontitle.setVisibility(View.GONE);
                        }

                        activityBookingBinding.tvcheckindatetime.setText(result.getData().data.checkinDate);
                        activityBookingBinding.tvcheckoutdatetime.setText(result.getData().data.checkoutDate);
                        activityBookingBinding.tvType.setText(result.getData().data.propertyCategory);
                        activityBookingBinding.tvGuestCount.setText(result.getData().data.numberOfGuest);
                        activityBookingBinding.tvTotalAmount.setText(result.getData().data.priceCalculation.totalAmount);
                        activityBookingBinding.tvGrandTotal.setText(result.getData().data.priceCalculation.totalAmount);

                        if(!result.getData().data.priceCalculation.weeklyAmount.equals("")){
                            activityBookingBinding.layoutWeekly.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvWeeklyPrice.setText(result.getData().data.priceCalculation.weeklyAmount);
                        }
                        else activityBookingBinding.layoutWeekly.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.dailyAmount.equals("")){
                            activityBookingBinding.layoutDaily.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvDailyPrice.setText(result.getData().data.priceCalculation.dailyAmount);
                        }
                        else activityBookingBinding.layoutDaily.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.hourlyAmount.equals("")){
                            activityBookingBinding.layoutHourly.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvHourlyPrice.setText(result.getData().data.priceCalculation.hourlyAmount);
                        }
                        else activityBookingBinding.layoutHourly.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.servicesAmount.equals("")){
                            activityBookingBinding.layoutExtra.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvextraService.setText(result.getData().data.priceCalculation.servicesAmount);
                        }
                        else activityBookingBinding.layoutExtra.setVisibility(View.GONE);

                        activityBookingBinding.tvCleaningFee.setText(result.getData().data.priceCalculation.cleanerFee);
                        activityBookingBinding.tvServiceTax.setText(result.getData().data.priceCalculation.taxAmount);

//                        if(result.getData().data.bookingStatus.equals("CONFIRMED")){
//                            activityBookingBinding.btnNext.setVisibility(View.VISIBLE);
//                            activityBookingBinding.tvCancel.setVisibility(View.VISIBLE);
//                            activityBookingBinding.layoutChat.setVisibility(View.VISIBLE);
//                            activityBookingBinding.layoutGoToMap.setVisibility(View.VISIBLE);
//                            activityBookingBinding.btnAddReview.setVisibility(View.GONE);
//                        }
//                        else if(result.getData().data.bookingStatus.equals("CANCELLED")){
//                            activityBookingBinding.btnNext.setVisibility(View.GONE);
//                            activityBookingBinding.tvCancel.setVisibility(View.GONE);
//                            activityBookingBinding.layoutChat.setVisibility(View.GONE);
//                            activityBookingBinding.layoutGoToMap.setVisibility(View.GONE);
//                            activityBookingBinding.btnAddReview.setVisibility(View.GONE);
//                        }
//                        else {
//                            activityBookingBinding.btnNext.setVisibility(View.GONE);
//                            activityBookingBinding.tvCancel.setVisibility(View.GONE);
//                            activityBookingBinding.layoutChat.setVisibility(View.GONE);
//                            activityBookingBinding.layoutGoToMap.setVisibility(View.GONE);
//                            if(result.getData().data.feedbackStatus.equals("true")){
//                                activityBookingBinding.btnAddReview.setVisibility(View.GONE);
//                            }
//                            else  activityBookingBinding.btnAddReview.setVisibility(View.VISIBLE);
//                        }
                    }
                    else{

                    }
                }
                break;
        }
    }


}