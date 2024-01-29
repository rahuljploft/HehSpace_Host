package com.hehspace_host.components.addproperty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.addproperty.viewmodel.PropertySummaryView_Model;
import com.hehspace_host.components.property.PropertDetailsActivity;
import com.hehspace_host.databinding.ActivityPropertySummaryBinding;
import com.hehspace_host.databinding.ItemAmenitiesBinding;
import com.hehspace_host.databinding.ItemViewCategoryBinding;
import com.hehspace_host.databinding.LayoutAddonViewBinding;
import com.hehspace_host.databinding.LayoutGalleryBinding;
import com.hehspace_host.databinding.LayoutHighlightBinding;
import com.hehspace_host.model.AdditionalSeviceModel;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.PropertyDetailModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PropertySummaryActivity extends BaseBindingActivity {

    ActivityPropertySummaryBinding propertySummaryBinding;
    PropertySummaryView_Model view_model;
    public  static  List<AdditionalSeviceModel> list = new ArrayList<>();
    AddonAdapters addonAdapters;

    @Override
    protected void setBinding() {
        propertySummaryBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_summary);
        view_model = new PropertySummaryView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.propertycategorylivedata.observe(this, login_modelApiResponse -> handleResult(login_modelApiResponse));

        propertySummaryBinding.rvCategory.setHasFixedSize(true);
        propertySummaryBinding.rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapters categoryAdapters = new CategoryAdapters(this, Constant.CategoryName,(type, pos) -> {

        });
        propertySummaryBinding.rvCategory.setAdapter(categoryAdapters);
        list.clear();
        if(Constant.ISEDIT.equals("yes")){
            propertySummaryBinding.tvtitle.setText("Update Publish");
        }
        else  propertySummaryBinding.tvtitle.setText("Create Listing");

        propertySummaryBinding.rvHighlights.setHasFixedSize(true);
        propertySummaryBinding.rvHighlights.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        HighlightsAdapters highlightsAdapters = new HighlightsAdapters(this, Constant.Highlights,(type, pos) -> {

        });
        propertySummaryBinding.rvHighlights.setAdapter(highlightsAdapters);

        propertySummaryBinding.rvAmenities.setHasFixedSize(true);
        propertySummaryBinding.rvAmenities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        AmenitiesAdapters amenitiesAdapters = new AmenitiesAdapters(this,Constant.Amenities, (type, pos) -> {

        });
        propertySummaryBinding.rvAmenities.setAdapter(amenitiesAdapters);

        propertySummaryBinding.rvAddons.setHasFixedSize(true);
        propertySummaryBinding.rvAddons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addonAdapters = new AddonAdapters(this, (type, pos) -> {

        });
        propertySummaryBinding.rvAddons.setAdapter(addonAdapters);



        propertySummaryBinding.rvGallery.setHasFixedSize(true);
        propertySummaryBinding.rvGallery.setLayoutManager(new GridLayoutManager(this, 3));
        propertySummaryBinding.tvPropertTitle.setText(Constant.PropertyTitle);
        propertySummaryBinding.tvAddress.setText(Constant.CityName+", "+Constant.CountryName);
        propertySummaryBinding.tvTime.setText("Open "+Constant.CheckInTime+" - "+Constant.CheckOutTime);
        propertySummaryBinding.tvdescription.setText(Constant.PropertyDetails);
        PropertDetailsActivity.makeTextViewResizable(propertySummaryBinding.tvdescription, 4, "See More", true);
        propertySummaryBinding.tvCleaningFee.setText("$"+Constant.CleanerFee);
        propertySummaryBinding.tvPrice.setText("$"+Constant.HourlyPrice+" /hour");
        Glide.with(this).load(Constant.imageData.get(0)).into(propertySummaryBinding.ivImage);
        ImageAdapters imageAdapters = new ImageAdapters(this, Constant.imageData, (type, pos) -> {

        });
        propertySummaryBinding.rvGallery.setAdapter(imageAdapters);

        for (int i=0; i<Constant.additionalServices.length();i++){
            try {
                JSONObject jsonObject = Constant.additionalServices.getJSONObject(i);
                AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                additionalSeviceModel.setStringName(jsonObject.getString("services_title"));
                additionalSeviceModel.setStringPrice(jsonObject.getString("services_rate"));
                list.add(additionalSeviceModel);
                if(list.size()>0){
                    addonAdapters.notifyDataSetChanged();
                    propertySummaryBinding.rvAddons.setVisibility(View.VISIBLE);
                    propertySummaryBinding.layoutAddon.setVisibility(View.VISIBLE);
                }
                else {

                    propertySummaryBinding.rvAddons.setVisibility(View.GONE);
                    propertySummaryBinding.layoutAddon.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void setListeners() {
        propertySummaryBinding.layoutPublish.setOnClickListener(this);
        propertySummaryBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view.getId() == R.id.layoutPublish){
            if (Uitility.isOnline(this)) {
                if(Constant.ISEDIT.equals("yes")){
                    view_model.UpdateProperty(Integer.parseInt(Constant.PropertyId));
                }
                else view_model.createProperty();
            } else {
                Uitility.nointernetDialog(this);
            }
        }
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    private void handleResult(ApiResponse<CommonModel> result) {
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
                        if(Constant.ISEDIT.equals("yes")){
                            Constant.CategoryId=new ArrayList<>();
                            Constant.CategoryName=new ArrayList<>();
                            Constant.LocationTag="0";
                            Constant.PropertyAddress="";
                            Constant.BuildingName="";
                            Constant.StreetName="";
                            Constant.CityName="";
                            Constant.StateName="";
                            Constant.CountryName="";
                            Constant.PostCode="";
                            Constant.AllowedGuest="";
                            Constant.AmenitiesId=new ArrayList<>();
                            Constant.AmenitiesName=new ArrayList<>();
                            Constant.imageData=new ArrayList<>();
                            Constant.PropertyTitle="";
                            Constant.PropertyDetails="";
                            Constant.HighlightId=new ArrayList<>();
                            Constant.Highlights=new ArrayList<>();
                            Constant.HourlyPrice="";
                            Constant.CheckInTime="";
                            Constant.CheckOutTime="";
                            Constant.DailyPrice="";
                            Constant.WeeklyPrice="";
                            Constant.MonthlyPrice="";
                            Constant.CleanerFee = "";
                            Constant.additionalServices = new JSONArray();
                            Constant.servicessList=new ArrayList<>();
                            startActivity(new Intent(this, MainActivity.class));
                        }
                      else   startActivity(new Intent(this,CongratulationActivity.class));
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    public static class CategoryAdapters extends RecyclerView.Adapter<CategoryAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;
        List<String> list;

        public CategoryAdapters(Context context,  List<String> list,ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewCategoryBinding itemViewCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_category, parent,
                    false);
            return new ViewHolder(itemViewCategoryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvCategory.setText(list.get(position));

        }

        @Override
        public int getItemCount() {
          /*if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewCategoryBinding itemRowBinding;

            public ViewHolder(ItemViewCategoryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class HighlightsAdapters extends RecyclerView.Adapter<HighlightsAdapters.ViewHolder> {

        Context context;
        List<String> list;
        ItemClickListner itemClickListner;

        public HighlightsAdapters(Context context, List<String> list,ItemClickListner itemClickListner) {
            this.context = context;
            this.list = list;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutHighlightBinding layoutHighlightBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_highlight, parent,
                    false);
            return new ViewHolder(layoutHighlightBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvHighlights.setText(list.get(position));

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutHighlightBinding itemRowBinding;

            public ViewHolder(LayoutHighlightBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class AmenitiesAdapters extends RecyclerView.Adapter<AmenitiesAdapters.ViewHolder> {

        Context context;
        List<String> list;
        ItemClickListner itemClickListner;

        public AmenitiesAdapters(Context context,List<String> list, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemAmenitiesBinding itemAmenitiesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_amenities, parent,
                    false);
            return new ViewHolder(itemAmenitiesBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvAmenities.setText(list.get(position));

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return list.size();
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutAddonViewBinding layoutAddonViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_addon_view, parent,
                    false);
            return new ViewHolder(layoutAddonViewBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvService.setText(list.get(position).getStringName());
            holder.itemRowBinding.tvPrice.setText("$"+list.get(position).getStringPrice());
        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutAddonViewBinding itemRowBinding;

            public ViewHolder(LayoutAddonViewBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ViewHolder> {

        Context context;
        List<Uri> imageData;
        ItemClickListner itemClickListner;

        public ImageAdapters(Context context,   List<Uri> imageData, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
            this.imageData = imageData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutGalleryBinding layoutGalleryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_gallery, parent,
                    false);
            return new ViewHolder(layoutGalleryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context).load(imageData.get(position)).into(holder.itemRowBinding.ivImage);
        }

        @Override
        public int getItemCount() {
          return imageData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutGalleryBinding itemRowBinding;

            public ViewHolder(LayoutGalleryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class ImageAdapters1 extends RecyclerView.Adapter<ImageAdapters1.ViewHolder> {

        Context context;
        List<PropertyDetailModel.PropertyImagesEntity> imageData;
        ItemClickListner itemClickListner;

        public ImageAdapters1(Context context,   List<PropertyDetailModel.PropertyImagesEntity> imageData, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
            this.imageData = imageData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutGalleryBinding layoutGalleryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_gallery, parent,
                    false);
            return new ViewHolder(layoutGalleryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context).load(imageData.get(position).propertyImageUrl).into(holder.itemRowBinding.ivImage);
        }

        @Override
        public int getItemCount() {
          return imageData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutGalleryBinding itemRowBinding;

            public ViewHolder(LayoutGalleryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

}