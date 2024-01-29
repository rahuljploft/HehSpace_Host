package com.hehspace_host.components.property;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.addproperty.SelectCategoryActivity;
import com.hehspace_host.components.rating.RatingReviewActivity;
import com.hehspace_host.components.splash.SplashActivity;
import com.hehspace_host.databinding.ActivityPropertDetailsBinding;
import com.hehspace_host.databinding.ItemAmenitiesBinding;
import com.hehspace_host.databinding.ItemRatingBinding;
import com.hehspace_host.databinding.ItemViewCategoryBinding;
import com.hehspace_host.databinding.LayoutAddonViewBinding;
import com.hehspace_host.databinding.LayoutGalleryBinding;
import com.hehspace_host.databinding.LayoutHighlightBinding;
import com.hehspace_host.databinding.LayoutImageSliderBinding;
import com.hehspace_host.model.AdditionalSeviceModel;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.PropertyDetailModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PropertDetailsActivity extends BaseBindingActivity {

    ActivityPropertDetailsBinding activityPropertDetailsBinding;
    PropertyDetailsView_Model view_model;
    TextView tvGuestCount;
    public static ArrayList<PropertyDetailModel.PropertyImagesEntity> imageList = new ArrayList<>();
    public static List<PropertyDetailModel.PropertyServicesEntity> propertyServicesList = new ArrayList<>();
    public static List<PropertyDetailModel.PropertyReviewListEntity> propertyReviewList = new ArrayList<>();
    public static List<String> categoryList = new ArrayList<>();
    public static List<String> highlightsList = new ArrayList<>();
    public static List<String> amenitiesList = new ArrayList<>();
    AmenitiesAdapters amenitiesAdapters;
    HighlightsAdapters highlightsAdapters;
    ImageAdapters imageAdapters;
    Image_Slider image_slider;
    AddonAdapters addonAdapters;
    RatingAdapters ratingAdapters;
    CategoryAdapters categoryAdapters;
    public  static  String rate="",review="",fivestart="",property_status="",property_statuspass="";

    @Override
    protected void setBinding() {
        activityPropertDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_propert_details);
        view_model = new PropertyDetailsView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        view_model.livedata1.observe(this, propertyCategoryModelApiResponse -> DeletehandleResult(propertyCategoryModelApiResponse));

        if (Uitility.isOnline(this)) {
            view_model.getPropertyDetails(Integer.parseInt(getIntent().getStringExtra("property_id")));
        } else {
            Uitility.nointernetDialog(this);
        }

        activityPropertDetailsBinding.rvCategory.setHasFixedSize(true);
        activityPropertDetailsBinding.rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapters = new CategoryAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvCategory.setAdapter(categoryAdapters);

        activityPropertDetailsBinding.rvHighlights.setHasFixedSize(true);
        activityPropertDetailsBinding.rvHighlights.setLayoutManager(new GridLayoutManager(this,2));
        highlightsAdapters = new HighlightsAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvHighlights.setAdapter(highlightsAdapters);

        activityPropertDetailsBinding.rvAmenities.setHasFixedSize(true);
        activityPropertDetailsBinding.rvAmenities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        amenitiesAdapters = new AmenitiesAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvAmenities.setAdapter(amenitiesAdapters);

        activityPropertDetailsBinding.rvAddons.setHasFixedSize(true);
        activityPropertDetailsBinding.rvAddons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addonAdapters = new AddonAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvAddons.setAdapter(addonAdapters);

        activityPropertDetailsBinding.rvRating.setHasFixedSize(true);
        activityPropertDetailsBinding.rvRating.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ratingAdapters = new RatingAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvRating.setAdapter(ratingAdapters);


        imageAdapters = new ImageAdapters(this, (type, pos) -> {
            startActivity(new Intent(this,ImageSliderActivity.class)
                    .putExtra("value","pro"));
        });
        activityPropertDetailsBinding.rvGallery.setAdapter(imageAdapters);

        activityPropertDetailsBinding.layoutRatingAll.setOnClickListener(v -> {
            startActivity(new Intent(this, RatingReviewActivity.class));
        });

        activityPropertDetailsBinding.layoutAddOnAll .setOnClickListener(v -> {
            startActivity(new Intent(this, AddOnServicesActivity.class));
        });


    }

    @Override
    protected void setListeners() {
        activityPropertDetailsBinding.ivBack.setOnClickListener(this);
        activityPropertDetailsBinding.layoutAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
        if(view.getId() == R.id.layoutAction){
            PopupMenu popup = new PopupMenu(this,  activityPropertDetailsBinding.layoutAction);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.action_popup, popup.getMenu());
            Menu menuOpts = popup.getMenu();

            if(property_status.equals("PUBLISHED")){
                menuOpts.getItem(2).setTitle("Unpublish");
                property_statuspass = "110";
            }
            else   {
                menuOpts.getItem(2).setTitle("Publish");
                property_statuspass = "109";
            }
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.one){
                        Constant.ISEDIT = "yes";
                        startActivity(new Intent(PropertDetailsActivity.this, SelectCategoryActivity.class));
                    }
                    if(item.getItemId() == R.id.two){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PropertDetailsActivity.this);
                        dialog.setTitle("Delete")
                                .setMessage("Do you want to Delete?")
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        dialoginterface.cancel();
                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        if (Uitility.isOnline(PropertDetailsActivity.this)) {
                                            view_model.propertyDelete(Integer.parseInt(getIntent().getStringExtra("property_id")));
                                        } else {
                                            Uitility.nointernetDialog(PropertDetailsActivity.this);
                                        }
                                    }
                                }).show();

                    }
                    if(item.getItemId() == R.id.three){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PropertDetailsActivity.this);
                        if(property_status.equals("PUBLISHED")){
                            dialog.setTitle("Unpublish")
                                    .setMessage("Do you want to Unpublish?")
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            dialoginterface.cancel();
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            if (Uitility.isOnline(PropertDetailsActivity.this)) {
                                                HashMap<String,String> hashMap = new HashMap<>();
                                                hashMap.put("property_status",property_statuspass);
                                                hashMap.put("_method","PUT");
                                                view_model.propertyUnpublish(hashMap,
                                                        Integer.parseInt(getIntent().getStringExtra("property_id")));
                                            } else {
                                                Uitility.nointernetDialog(PropertDetailsActivity.this);
                                            }
                                        }
                                    }).show();
                        }
                        else {
                            dialog.setTitle("Publish")
                                    .setMessage("Do you want to Publish?")
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            dialoginterface.cancel();
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            if (Uitility.isOnline(PropertDetailsActivity.this)) {
                                                HashMap<String,String> hashMap = new HashMap<>();
                                                hashMap.put("property_status",property_statuspass);
                                                hashMap.put("_method","PUT");
                                                view_model.propertyUnpublish(hashMap,
                                                        Integer.parseInt(getIntent().getStringExtra("property_id")));
                                            } else {
                                                Uitility.nointernetDialog(PropertDetailsActivity.this);
                                            }
                                        }
                                    }).show();
                        }



                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu
        }

    }

    private void handleResult(ApiResponse<PropertyDetailModel> result) {
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

                        propertyServicesList = result.getData().data.propertyServices;
                        propertyReviewList = result.getData().data.propertyReviewList;
                        activityPropertDetailsBinding.tvCatergory.setText(result.getData().data.propertyCategory);
                        activityPropertDetailsBinding.tvCat.setText(result.getData().data.propertyCategory);
                        activityPropertDetailsBinding.tvTitle.setText(result.getData().data.propertyTitle);
                        activityPropertDetailsBinding.tvLocation.setText(result.getData().data.propertyLocation);
                        activityPropertDetailsBinding.tvDesc.setText(result.getData().data.propertyDetails);
                        makeTextViewResizable(activityPropertDetailsBinding.tvDesc, 4, "See More", true);
                        activityPropertDetailsBinding.tvTotalRating.setText(result.getData().data.propertyRatting);
                        activityPropertDetailsBinding.tvTotalReviews.setText(result.getData().data.propertyReview);
                        activityPropertDetailsBinding.tvFiveStar.setText(result.getData().data.fiveStartRatio+" %");
                        rate = result.getData().data.propertyRatting;
                        review = result.getData().data.propertyReview;
                        fivestart = result.getData().data.fiveStartRatio+" %";
                        activityPropertDetailsBinding.tvPrice.setText(result.getData().data.hourlyRate+" /hour");
                        activityPropertDetailsBinding.tvTime.setText(result.getData().data.propertyTime);
                        property_status = result.getData().data.propertyStatus;
                        activityPropertDetailsBinding.tvRating.setText(result.getData().data.propertyRatting+" ( "+result.getData().data.propertyReview+" Reviews )");
                        categoryList = Arrays.asList(result.getData().data.propertyCategory.split("\\s*,\\s*"));
                        highlightsList = Arrays.asList(result.getData().data.propertyHiglights.split("\\s*,\\s*"));

                        amenitiesList = Arrays.asList(result.getData().data.propertyAnenities.split("\\s*,\\s*"));
                        image_slider = new Image_Slider(this,imageList);
                        activityPropertDetailsBinding.itemPicker.setSliderAdapter(image_slider);
                        if(propertyServicesList.size()>0){
                            addonAdapters.notifyDataSetChanged();
                            activityPropertDetailsBinding.rvAddons.setVisibility(View.VISIBLE);
                            activityPropertDetailsBinding.layoutAddOnAll.setVisibility(View.VISIBLE);
                        }
                        else {
                            activityPropertDetailsBinding.rvAddons.setVisibility(View.GONE);
                            activityPropertDetailsBinding.layoutAddOnAll.setVisibility(View.GONE);
                        }
                        amenitiesAdapters.notifyDataSetChanged();
                        highlightsAdapters.notifyDataSetChanged();
                        imageAdapters.notifyDataSetChanged();
                        ratingAdapters.notifyDataSetChanged();
                        categoryAdapters.notifyDataSetChanged();

                        // for edit property
                        Constant.PropertyId = result.getData().data.propertyId;
                        Constant.CategoryId.clear();
                        Constant.CategoryName.clear();
                        Constant.CategoryId.addAll(Arrays.asList(result.getData().data.categoryId.split("\\s*,\\s*")));
                        Constant.CategoryName.addAll(Arrays.asList(result.getData().data.propertyCategory.split("\\s*,\\s*")));
                        Log.d("qwertywertw",Constant.CategoryId.size()+"__");
                        Constant.imageList = result.getData().data.propertyImages;
                        Constant.PropertyAddress = result.getData().data.propertyAddress;
                        Constant.BuildingName = result.getData().data.buildingName;
                        Constant.StreetName = result.getData().data.streetName;
                        Constant.CityName = result.getData().data.cityName;
                        Constant.StateName = result.getData().data.stateName;
                        Constant.CountryName = result.getData().data.countryName;
                        Constant.PostCode = result.getData().data.postCode;
                        Constant.HirePhotographer = result.getData().data.hirePhotographer;


                        Constant.Latitude = result.getData().data.latitude;
                        Constant.Longitude = result.getData().data.longitude;
                        Constant.HighlightsLive = Arrays.asList(result.getData().data.propertyHiglights.split("\\s*,\\s*"));
                        Constant.AllowedGuest = result.getData().data.allowedGuest;

                        if (!result.getData().data.propertyAnenities.isEmpty()){
                            Constant.AmenitiesName = Arrays.asList(result.getData().data.propertyAnenities.split("\\s*,\\s*"));;
                        }

                        Constant.PropertyTitle = result.getData().data.propertyTitle;
                        Constant.PropertyDetails =  result.getData().data.propertyDetails;
                        Constant.CheckInTime = result.getData().data.checkinTime;
                        Constant.CheckOutTime = result.getData().data.checkoutTime;

                        if(!result.getData().data.hourlyRate.equals("")){

                            if(result.getData().data.hourlyRate.contains("$")){
                                String[] parts =result.getData().data.hourlyRate.split("\\$");
                                if(!parts[1].isEmpty()) {
                                    Constant.HourlyPrice = parts[1];
                                }

                            }

                        }

                        if(!result.getData().data.dailyRate.equals("")){
                            if(result.getData().data.dailyRate.equals("$")){
                                Constant.DailyPrice="";
                            }
                            else {
                                String[] parts =result.getData().data.dailyRate.split("\\$");
                                if(!parts[1].isEmpty()){
                                    Constant.DailyPrice = parts[1];
                                }
                            }
                        }
                        else       Constant.DailyPrice="";

                        if(!result.getData().data.weeklyRate.equals("")){
                            if(result.getData().data.weeklyRate.equals("$")){


                            }
                            else {
                                String[] parts =result.getData().data.weeklyRate.split("\\$");
                                if(!parts[1].isEmpty()) {
                                    Constant.WeeklyPrice = parts[1];
                                }
                            }

                        }else    Constant.WeeklyPrice="";

                        if(!result.getData().data.monthlyRate.equals("")){
                            if(result.getData().data.monthlyRate.equals("$")){
                                Constant.MonthlyPrice="";

                            }
                            else {
                                String[] parts =result.getData().data.monthlyRate.split("\\$");
                                if(!parts[1].isEmpty()){
                                    Constant.MonthlyPrice = parts[1];
                                    Log.e("parts",parts[0]+"======"+parts[1]);
                                }
                            }
                        }
                        else  Constant.MonthlyPrice="";



                        Constant.CleanerFee = result.getData().data.cleanerFee;
//                        AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
//                        additionalSeviceModel.setStringName("");
//                        additionalSeviceModel.setStringPrice("");
//                        additionalList.add(additionalList.size(), additionalSeviceModel);
//                        mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);

                        if (result.getData().data.propertyServices.size() > 0) {
                            Constant.servicessList.clear();
                            Constant.servicessList.addAll(result.getData().data.propertyServices);
                        }
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    private void DeletehandleResult(ApiResponse<CommonModel> result) {
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
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }



    //image slider
    public class Image_Slider extends SliderViewAdapter<Image_Slider.SliderAdapterVH> {
        Context context;
        LayoutInflater inflater;
        ArrayList<PropertyDetailModel.PropertyImagesEntity> imageList;

        Image_Slider(Context context,ArrayList<PropertyDetailModel.PropertyImagesEntity> imageList) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.imageList = imageList;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            LayoutImageSliderBinding imageSliderAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_image_slider, parent,
                    false);
            return new SliderAdapterVH(imageSliderAdapterBinding);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH holder, int position) {

            Glide.with(context)
                    .load(imageList.get(position).propertyImageUrl)
                    .error(R.drawable.logo_host)
                    .into(holder.imageSliderAdapterBinding.bannerImage);

            holder.imageSliderAdapterBinding.bannerImage.setOnClickListener(v ->
                    startActivity(new Intent(context,ImageSliderActivity.class)
                            .putExtra("value","pro")));



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

            holder.itemRowBinding.tvCategory.setText(categoryList.get(position));
        }

        @Override
        public int getItemCount() {
          /*if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return categoryList.size();
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
        ItemClickListner itemClickListner;

        public HighlightsAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public HighlightsAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutHighlightBinding layoutHighlightBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_highlight, parent,
                    false);
            return new HighlightsAdapters.ViewHolder(layoutHighlightBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull HighlightsAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvHighlights.setText(highlightsList.get(position));

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return highlightsList.size();
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
            LayoutAddonViewBinding layoutAddonViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_addon_view, parent,
                    false);
            return new AddonAdapters.ViewHolder(layoutAddonViewBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AddonAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvPrice.setText(propertyServicesList.get(position).servicesRate);
            holder.itemRowBinding.tvService.setText(propertyServicesList.get(position).servicesTitle);
        }

        @Override
        public int getItemCount() {
            return Math.min(propertyServicesList.size(), 2);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutAddonViewBinding itemRowBinding;
            public ViewHolder(LayoutAddonViewBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class RatingAdapters extends RecyclerView.Adapter<RatingAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public RatingAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public RatingAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemRatingBinding layoutAddonViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_rating, parent,
                    false);
            return new RatingAdapters.ViewHolder(layoutAddonViewBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RatingAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvName.setText(propertyReviewList.get(position).fullName);
            holder.itemRowBinding.tvComment.setText(propertyReviewList.get(position).rattingComment);
            holder.itemRowBinding.tvTime.setText(propertyReviewList.get(position).createdAt);
            holder.itemRowBinding.rbrating.setRating(Float.parseFloat(propertyReviewList.get(position).rattingStar));
            Glide.with(context)
                    .load(propertyReviewList.get(position).userImage)
                    .error(R.drawable.user_dummy)
                    .into(holder.itemRowBinding.ivImage);
        }

        @Override
        public int getItemCount() {
            return Math.min(propertyReviewList.size(), 2);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemRatingBinding itemRowBinding;
            public ViewHolder(ItemRatingBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }

    public static class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ViewHolder> {
        Context context;
        ItemClickListner itemClickListner;

        public ImageAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public ImageAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutGalleryBinding layoutGalleryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_gallery, parent,
                    false);
            return new ImageAdapters.ViewHolder(layoutGalleryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ImageAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context)
                    .load(imageList.get(position).propertyImageUrl)
                    .error(R.drawable.logo_host)
                    .into(holder.itemRowBinding.ivImage);

            holder.itemRowBinding.cardImage.setOnClickListener(v -> itemClickListner.onClick("",position));
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutGalleryBinding itemRowBinding;

            public ViewHolder(LayoutGalleryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#1b76d3"));
        }

        @Override
        public void onClick(View widget) {


        }
    }



}