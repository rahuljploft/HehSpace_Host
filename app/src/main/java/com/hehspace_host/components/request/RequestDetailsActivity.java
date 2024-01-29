package com.hehspace_host.components.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.hehspace_host.components.addproperty.PropertySummaryActivity;
import com.hehspace_host.components.chat.ChatActivity;
import com.hehspace_host.components.fragments.dashboard.home.HomeFragment;
import com.hehspace_host.components.property.ImageSliderActivity;
import com.hehspace_host.components.property.PropertDetailsActivity;
import com.hehspace_host.databinding.ActivityRequestDetailsBinding;
import com.hehspace_host.databinding.ItemAmenitiesBinding;
import com.hehspace_host.databinding.ItemViewAddonsRequestBinding;
import com.hehspace_host.databinding.ItemViewCategoryBinding;
import com.hehspace_host.databinding.LayoutAddonViewBinding;
import com.hehspace_host.databinding.LayoutImageSliderBinding;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.PropertyDetailModel;
import com.hehspace_host.model.RequestDetailsModel;
import com.hehspace_host.model.RequestListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestDetailsActivity extends BaseBindingActivity {

    ActivityRequestDetailsBinding activityRequestDetailsBinding;
    RequestDetailsView_Model requestDetailsView_model;
    public static List<RequestDetailsModel.PropertyImagesEntity> imageList = new ArrayList<>();
    public static List<RequestDetailsModel.AddonServicesEntity> propertyServicesList = new ArrayList<>();
    Image_Slider image_slider;
    AddonAdapters addonAdapters;
    AmenitiesAdapters amenitiesAdapters;
    public static List<String> amenitiesList = new ArrayList<>();
    String property_id = "";
    String userid = "";
    String bookingid = "";
    String requestid = "";
    String name = "";
    String property = "";
    String image = "";

    @Override
    protected void setBinding() {
        activityRequestDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_request_details);
        requestDetailsView_model = new RequestDetailsView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
            mActivity = this;
    }

    @Override
    protected void initializeObject() {
        requestDetailsView_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        requestDetailsView_model.Commonlivedata.observe(this, modelApiResponse -> handleResultChangeStatus(modelApiResponse));

        if (Uitility.isOnline(this)) {
            requestDetailsView_model.getRequestDetails(Integer.parseInt(getIntent().getStringExtra("reqid")));
        } else {
            Uitility.nointernetDialog(this);
        }

        activityRequestDetailsBinding.layoutCheckAvailability.setOnClickListener(v -> {
            CalenderBottomSheet();
        });
        if(getIntent().getStringExtra("status").equals("ACCEPTED")){
            activityRequestDetailsBinding.bottomSheet.setVisibility(View.GONE);
        }
        else if(getIntent().getStringExtra("status").equals("REJECTED")){
            activityRequestDetailsBinding.bottomSheet.setVisibility(View.GONE);
        }
        else activityRequestDetailsBinding.bottomSheet.setVisibility(View.VISIBLE);

        activityRequestDetailsBinding.rvAmenities.setHasFixedSize(true);
        activityRequestDetailsBinding.rvAmenities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         amenitiesAdapters = new AmenitiesAdapters(this, (type, pos) -> {

        });
        activityRequestDetailsBinding.rvAmenities.setAdapter(amenitiesAdapters);

        activityRequestDetailsBinding.rvAddon.setHasFixedSize(true);
        activityRequestDetailsBinding.rvAddon.setLayoutManager(new GridLayoutManager(this, 2));
        addonAdapters = new AddonAdapters(this, (type, pos) -> {

        });
        activityRequestDetailsBinding.rvAddon.setAdapter(addonAdapters);

        activityRequestDetailsBinding.layoutChat.setOnClickListener(v -> {
            startActivity(new Intent(this,ChatActivity.class)
                    .putExtra("userid",userid)
                    .putExtra("bookingid",bookingid)
                    .putExtra("requestid",requestid)
                    .putExtra("propertyid",property_id)
                    .putExtra("name",name)
                    .putExtra("property",property)
                    .putExtra("image",image)
            );
        });

//        activityRequestDetailsBinding.rvCategory.setHasFixedSize(true);
//        activityRequestDetailsBinding.rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        CategoryAdapters categoryAdapters = new CategoryAdapters(this, (type, pos) -> {
//
//        });
//        activityRequestDetailsBinding.rvCategory.setAdapter(categoryAdapters);

    }

    @Override
    protected void setListeners() {
        activityRequestDetailsBinding.ivBack.setOnClickListener(this);
        activityRequestDetailsBinding.layoutReject.setOnClickListener(this);
        activityRequestDetailsBinding.layoutAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){onBackPressed();}

        if(view.getId() == R.id.layoutReject){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirm")
                    .setMessage("Do you want to reject this?")
                    .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                    .setPositiveButton("Yes", (dialoginterface, i) -> {
                        if (Uitility.isOnline(this)) {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("request_status","210");
                            hashMap.put("_method","PUT");
                            requestDetailsView_model.acceptReject(hashMap,Integer.parseInt(getIntent().getStringExtra("reqid")));
                        } else {
                            Uitility.nointernetDialog(this);
                        }
                    }).show();
        }
        if(view.getId() == R.id.layoutAccept){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirm")
                    .setMessage("Do you want to Accept this?")
                    .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                    .setPositiveButton("Yes", (dialoginterface, i) -> {
                        if (Uitility.isOnline(this)) {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("request_status","209");
                            hashMap.put("_method","PUT");
                            requestDetailsView_model.acceptReject(hashMap,Integer.parseInt(getIntent().getStringExtra("reqid")));
                        } else {
                            Uitility.nointernetDialog(this);
                        }
                    }).show();
        }
    }

    public void CalenderBottomSheet() {
        MyCalendarActivity addPhotoBottomDialogFragment =
                MyCalendarActivity.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                MyCalendarActivity.TAG);
    }


    private void handleResult(ApiResponse<RequestDetailsModel> result) {
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
                        imageList = result.getData().data.propertyImages;
                        activityRequestDetailsBinding.tvCatergory.setText(result.getData().data.propertyCategory);
                        activityRequestDetailsBinding.tvCat.setText(result.getData().data.propertyCategory);
                        activityRequestDetailsBinding.tvTitle.setText(result.getData().data.propertyTitle);
                        activityRequestDetailsBinding.tvLocation.setText(result.getData().data.propertyLocation);
                        activityRequestDetailsBinding.tvDesc.setText(result.getData().data.propertyDetails);
                        PropertDetailsActivity.makeTextViewResizable(activityRequestDetailsBinding.tvDesc, 4, "See More", true);
                        activityRequestDetailsBinding.tvRating.setText(result.getData().data.propertyRatting+" ( "+result.getData().data.propertyReview+" Reviews )");
                        activityRequestDetailsBinding.tvTime.setText(result.getData().data.propertyTime);
                        image_slider = new Image_Slider(this,imageList);
                        activityRequestDetailsBinding.itemPicker.setSliderAdapter(image_slider);
                        activityRequestDetailsBinding.tvHostName.setText(result.getData().data.fullName);
                        activityRequestDetailsBinding.tvType.setText(result.getData().data.propertyCategory);
                        activityRequestDetailsBinding.tvguestCount.setText(result.getData().data.numberOfGuest);
                        activityRequestDetailsBinding.tvHostName.setText(result.getData().data.fullName);
                        activityRequestDetailsBinding.tvCheckInTime.setText(result.getData().data.checkinDate+", "+result.getData().data.checkinTime);
                        activityRequestDetailsBinding.tvCheckOutTime.setText(result.getData().data.checkoutDate+", "+result.getData().data.checkoutTime);
                        activityRequestDetailsBinding.tvMobile.setText("Mobile: "+result.getData().data.mobileNumber);
                        property_id = result.getData().data.propertyId;
                        HomeFragment.propid =  result.getData().data.propertyId;
                        userid = result.getData().data.userId;
                        bookingid ="";
                        requestid = result.getData().data.requestId;
                        name = result.getData().data.fullName;
                        property = result.getData().data.propertyTitle;
                        image = result.getData().data.userImageUrl;
                        Glide.with(this)
                                .load(result.getData().data.userImageUrl)
                                .error(R.drawable.user_dummy)
                                .into(activityRequestDetailsBinding.ivImage);
                        if(propertyServicesList.size()>0){
                            addonAdapters.notifyDataSetChanged();
                            activityRequestDetailsBinding.rvAddon.setVisibility(View.VISIBLE);
                            activityRequestDetailsBinding.titleAddon.setVisibility(View.VISIBLE);
                        }
                        else {
                            activityRequestDetailsBinding.rvAddon.setVisibility(View.GONE);
                            activityRequestDetailsBinding.titleAddon.setVisibility(View.GONE);
                        }
                        amenitiesList = Arrays.asList(result.getData().data.propertyAnenities.split("\\s*,\\s*"));
                        amenitiesAdapters.notifyDataSetChanged();

                        activityRequestDetailsBinding.tvGrandTotal.setText(result.getData().data.priceCalculation.totalAmount);

                        if(!result.getData().data.priceCalculation.weeklyAmount.equals("")){
                            activityRequestDetailsBinding.layoutWeekly.setVisibility(View.VISIBLE);
                            activityRequestDetailsBinding.tvWeeklyPrice.setText(result.getData().data.priceCalculation.weeklyAmount);
                        }
                        else activityRequestDetailsBinding.layoutWeekly.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.dailyAmount.equals("")){
                            activityRequestDetailsBinding.layoutDaily.setVisibility(View.VISIBLE);
                            activityRequestDetailsBinding.tvDailyPrice.setText(result.getData().data.priceCalculation.dailyAmount);
                        }
                        else activityRequestDetailsBinding.layoutDaily.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.hourlyAmount.equals("")){
                            activityRequestDetailsBinding.layoutHourly.setVisibility(View.VISIBLE);
                            activityRequestDetailsBinding.tvHourlyPrice.setText(result.getData().data.priceCalculation.hourlyAmount);
                        }
                        else activityRequestDetailsBinding.layoutHourly.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.servicesAmount.equals("")){
                            activityRequestDetailsBinding.layoutExtra.setVisibility(View.VISIBLE);
                            activityRequestDetailsBinding.tvextraService.setText(result.getData().data.priceCalculation.servicesAmount);
                        }
                        else activityRequestDetailsBinding.layoutExtra.setVisibility(View.GONE);

                        activityRequestDetailsBinding.tvCleaningFee.setText(result.getData().data.priceCalculation.cleanerFee);
                        activityRequestDetailsBinding.tvServiceTax.setText(result.getData().data.priceCalculation.taxAmount);
                    }
                    else{

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
                       startActivity(new Intent(this, RequestActivity.class));
                    }
                    else{

                    }
                }
                break;
        }
    }

    //image slider
    public class Image_Slider extends SliderViewAdapter<Image_Slider.SliderAdapterVH> {
        Context context;
        LayoutInflater inflater;
        List<RequestDetailsModel.PropertyImagesEntity> imageList;

        Image_Slider(Context context,List<RequestDetailsModel.PropertyImagesEntity> imageList) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.imageList = imageList;
        }

        @Override
        public Image_Slider.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            LayoutImageSliderBinding imageSliderAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_image_slider, parent,
                    false);
            return new Image_Slider.SliderAdapterVH(imageSliderAdapterBinding);
        }

        @Override
        public void onBindViewHolder(Image_Slider.SliderAdapterVH holder, int position) {

            Glide.with(context)
                    .load(imageList.get(position).propertyImageUrl)
                    .error(R.drawable.logo_host)
                    .into(holder.imageSliderAdapterBinding.bannerImage);

            holder.imageSliderAdapterBinding.bannerImage.setOnClickListener(v ->
                    startActivity(new Intent(context, ImageSliderActivity.class).putExtra("value","req")));



        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return imageList.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            LayoutImageSliderBinding imageSliderAdapterBinding;

            public SliderAdapterVH(LayoutImageSliderBinding imageSliderAdapterBinding) {
                super(imageSliderAdapterBinding.getRoot());
                this.imageSliderAdapterBinding = imageSliderAdapterBinding;
            }

        }
    }

    public static class CategoryAdapters extends RecyclerView.Adapter<CategoryAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public CategoryAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public CategoryAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewCategoryBinding itemViewCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_category, parent,
                    false);
            return new CategoryAdapters.ViewHolder(itemViewCategoryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull CategoryAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return 6;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewCategoryBinding itemRowBinding;

            public ViewHolder(ItemViewCategoryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class AmenitiesAdapters extends RecyclerView.Adapter<AmenitiesAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public AmenitiesAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public AmenitiesAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemAmenitiesBinding itemAmenitiesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_amenities, parent,
                    false);
            return new AmenitiesAdapters.ViewHolder(itemAmenitiesBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AmenitiesAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvAmenities.setText(amenitiesList.get(position));

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return amenitiesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemAmenitiesBinding itemRowBinding;

            public ViewHolder(ItemAmenitiesBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

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
        holder.itemRowBinding.tvService.setText(propertyServicesList.get(position).servicesTitle);

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return propertyServicesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewAddonsRequestBinding itemRowBinding;

            public ViewHolder(ItemViewAddonsRequestBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

}