package com.hehspace_host.components.addproperty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.hehspace_host.components.addproperty.viewmodel.AddAmenitiesView_Model;
import com.hehspace_host.databinding.ActivityAddAmenitiesBinding;
import com.hehspace_host.databinding.ItemPropertyCategoryBinding;
import com.hehspace_host.model.PropertyAmenitiesModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.List;

public class AddAmenitiesActivity extends BaseBindingActivity {

    ActivityAddAmenitiesBinding activityAddAmenitiesBinding;
    AddAmenitiesView_Model view_model;

    @Override
    protected void setBinding() {
        activityAddAmenitiesBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_amenities);
        view_model = new AddAmenitiesView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.propertyAmenity();
        } else {
            Uitility.nointernetDialog(this);
        }
        activityAddAmenitiesBinding.rvAmenities.setHasFixedSize(true);
        activityAddAmenitiesBinding.rvAmenities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void setListeners() {
        activityAddAmenitiesBinding.btnNext.setOnClickListener(this);
        activityAddAmenitiesBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.btnNext) {

            Log.e("amenid", Constant.AmenitiesId.toString());
            Log.e("amenid", android.text.TextUtils.join(",", Constant.AmenitiesId));
            Constant.map.put("allowed_anenities", android.text.TextUtils.join(",", Constant.AmenitiesId));
            startActivity(new Intent(this, UploadPhotoActivity.class));
        }
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    private void handleResult(ApiResponse<PropertyAmenitiesModel> result) {
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
                            if (Constant.AmenitiesName.size() > 0) {
                                for (int i = 0; i < result.getData().data.size(); i++) {
                                    for (int j = 0; j < Constant.AmenitiesName.size(); j++) {
                                        if (result.getData().data.get(i).anenitiesTitle.equals(Constant.AmenitiesName.get(j))) {
                                            result.getData().data.get(i).isselected = true;
                                            Log.d("qwerty", result.getData().data.get(i).anenitiesTitle);
                                            Constant.AmenitiesId.add(result.getData().data.get(i).anenitiesId);
                                            Constant.Amenities.add(result.getData().data.get(i).anenitiesTitle);
                                            break;
                                        } else {
                                            result.getData().data.get(i).isselected = false;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            if (Constant.AmenitiesId.size() > 0) {
                                for (int i = 0; i < result.getData().data.size(); i++) {
                                    for (int j = 0; j < Constant.AmenitiesId.size(); j++) {
                                        if (result.getData().data.get(i).anenitiesId.equals(Constant.AmenitiesId.get(j))) {
                                            result.getData().data.get(i).isselected = true;
//                                            Log.d("qwerty", result.getData().data.get(i).anenitiesTitle);
//                                            Constant.AmenitiesId.add(result.getData().data.get(i).anenitiesId);
//                                            Constant.Amenities.add(result.getData().data.get(i).anenitiesTitle);
                                            break;
                                        } else {
                                            result.getData().data.get(i).isselected = false;
                                        }
                                    }
                                }
                            }

                        }
                        AmenitiesAdapters amenitiesAdapters = new AmenitiesAdapters(mActivity, result.getData().data);
                        activityAddAmenitiesBinding.rvAmenities.setAdapter(amenitiesAdapters);
                    } else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    public static class AmenitiesAdapters extends RecyclerView.Adapter<AmenitiesAdapters.ViewHolder> {

        Context context;
        List<PropertyAmenitiesModel.DataEntity> list;

        public AmenitiesAdapters(Context context, List<PropertyAmenitiesModel.DataEntity> list) {
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
            holder.itemRowBinding.tvCategory.setText(list.get(position).anenitiesTitle);
            if (list.get(position).isselected) {
                holder.itemRowBinding.rvLayout.setBackground(context.getResources().getDrawable(R.drawable.card_bg));
                holder.itemRowBinding.ivSelect.setVisibility(View.VISIBLE);
            } else {
                holder.itemRowBinding.rvLayout.setBackground(context.getResources().getDrawable(R.drawable.white_bg));
                holder.itemRowBinding.ivSelect.setVisibility(View.GONE);
            }

            holder.itemRowBinding.cardCategory.setOnClickListener(v -> {
                if (list.get(position).isselected) {
                    list.get(position).isselected = false;
                    Constant.AmenitiesId.remove(list.get(position).anenitiesId);
                    Constant.Amenities.remove(list.get(position).anenitiesTitle);
                } else {
                    list.get(position).isselected = true;
                    Constant.AmenitiesId.add(list.get(position).anenitiesId);
                    Constant.Amenities.add(list.get(position).anenitiesTitle);
                }
                notifyDataSetChanged();

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