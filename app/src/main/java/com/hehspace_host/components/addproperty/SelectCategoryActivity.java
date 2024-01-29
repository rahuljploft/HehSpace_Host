package com.hehspace_host.components.addproperty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hehspace_host.R;
import com.hehspace_host.components.addproperty.viewmodel.SelectCategoryView_Model;
import com.hehspace_host.databinding.ActivitySelectCategoryBinding;
import com.hehspace_host.databinding.ItemPropertyCategoryBinding;
import com.hehspace_host.model.PropertyCategoryModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.List;
import java.util.Locale;

public class SelectCategoryActivity extends BaseBindingActivity {

    ActivitySelectCategoryBinding activitySelectCategoryBinding;
    SelectCategoryView_Model view_model;

    String city = "";
    String state = "";
    String country = "";
    String postalCode = "";
    String knownName = "";

    @Override
    protected void setBinding() {
        activitySelectCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_category);
        view_model = new SelectCategoryView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.propertycategorylivedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.propertyCategory();
        } else {
            Uitility.nointernetDialog(this);
        }
        activitySelectCategoryBinding.rvCategory.setHasFixedSize(true);
        activitySelectCategoryBinding.rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void setListeners() {
        activitySelectCategoryBinding.btnNext.setOnClickListener(this);
        activitySelectCategoryBinding.ivBack.setOnClickListener(this);

    }

    public String getAddress(Double latitude, Double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.e("qwert", addresses.get(0).toString());
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName(); // Only if ava
            Constant.PropertyAddress = address;


        } catch (Exception ignored) {

        }
        return "";

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.btnNext) {
            if (Constant.CategoryId.size() > 0) {
                if (Constant.ISEDIT.equals("yes")) {
                    Constant.map.put("category_id", android.text.TextUtils.join(",", Constant.CategoryId));
                    Constant.map.put("property_address", Constant.PropertyAddress);
                    Constant.map.put("latitude", Constant.Latitude);
                    Constant.map.put("longitude", Constant.Longitude);
                    startActivity(new Intent(this, ConfirmLocationActivity.class)
                            .putExtra("flat", Constant.BuildingName)
                            .putExtra("city", Constant.CityName)
                            .putExtra("state", Constant.StateName)
                            .putExtra("country", Constant.CountryName)
                            .putExtra("postalCode", Constant.PostCode)
                    );
                } else {
                    //  Constant.map.put("category_id", Constant.CategoryId);
                    startActivity(new Intent(this, SelectLocationActivity.class));
                }
                return;
            } else Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();


        }
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    private void handleResult(ApiResponse<PropertyCategoryModel> result) {
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
                        if (Constant.ISEDIT.equals("yes")) {
                            if (Constant.CategoryId.size() > 0) {
                                for (int i = 0; i < result.getData().data.size(); i++) {
                                    for (int j = 0; j < Constant.CategoryId.size(); j++) {
                                        Log.d("qwerty", result.getData().data.get(i).categoryId + "__" + Constant.CategoryId.get(j));
                                        if (result.getData().data.get(i).categoryId.equals(Constant.CategoryId.get(j))) {
                                            result.getData().data.get(i).isselected = true;
                                            break;
                                        } else {
                                            result.getData().data.get(i).isselected = false;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            if (Constant.CategoryId.size() > 0) {
                                for (int i = 0; i < result.getData().data.size(); i++) {
                                    for (int j = 0; j < Constant.CategoryId.size(); j++) {
                                        Log.d("qwerty", result.getData().data.get(i).categoryId + "__" + Constant.CategoryId.get(j));
                                        if (result.getData().data.get(i).categoryId.equals(Constant.CategoryId.get(j))) {
                                            result.getData().data.get(i).isselected = true;
                                            break;
                                        } else {
                                            result.getData().data.get(i).isselected = false;
                                        }
                                    }
                                }
                            }
                        }
                        Property_Category_Adapter property_category_adapter = new Property_Category_Adapter(mActivity, result.getData().data);
                        activitySelectCategoryBinding.rvCategory.setAdapter(property_category_adapter);
                    } else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    public static class Property_Category_Adapter extends RecyclerView.Adapter<Property_Category_Adapter.ViewHolder> {

        Context context;
        List<PropertyCategoryModel.DataEntity> list;

        public Property_Category_Adapter(Context context, List<PropertyCategoryModel.DataEntity> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemPropertyCategoryBinding rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_property_category, parent,
                    false);
            return new ViewHolder(rowDrawerBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvCategory.setText(list.get(position).categoryTitle);
            if (list.get(position).isselected) {
                holder.itemRowBinding.rvLayout.setBackground(context.getResources().getDrawable(R.drawable.card_bg));
                holder.itemRowBinding.ivSelect.setVisibility(View.VISIBLE);
            } else {
                holder.itemRowBinding.rvLayout.setBackground(context.getResources().getDrawable(R.drawable.white_bg));
                holder.itemRowBinding.ivSelect.setVisibility(View.GONE);
            }

            holder.itemRowBinding.cardCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).isselected) {
                        list.get(position).isselected = false;

                        if (Constant.CategoryId.contains(list.get(position).categoryId))
                            Constant.CategoryId.remove(list.get(position).categoryId);

                        if (Constant.CategoryName.contains(list.get(position).categoryTitle))
                            Constant.CategoryName.remove(list.get(position).categoryTitle);

                    } else {
                        if (Constant.CategoryId.contains(list.get(position).categoryId)) {

                        } else {
                            Constant.CategoryId.add(list.get(position).categoryId);
                        }

                        if (Constant.CategoryName.contains(list.get(position).categoryTitle)) {

                        } else {
                            Constant.CategoryName.add(list.get(position).categoryTitle);
                        }

                        list.get(position).isselected = true;
                    }
                    Property_Category_Adapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemPropertyCategoryBinding itemRowBinding;

            public ViewHolder(ItemPropertyCategoryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


}