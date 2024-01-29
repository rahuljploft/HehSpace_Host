package com.hehspace_host.components.property;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityAddOnServicesBinding;
import com.hehspace_host.databinding.LayoutAddonViewBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.ItemClickListner;

public class AddOnServicesActivity extends BaseBindingActivity {
    ActivityAddOnServicesBinding activityAddOnServicesBinding;

    @Override
    protected void setBinding() {
        activityAddOnServicesBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_on_services);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        activityAddOnServicesBinding.rvServices.setHasFixedSize(true);
        activityAddOnServicesBinding.rvServices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (PropertDetailsActivity.propertyServicesList.size() > 0) {
            AddonAdapters addonAdapters = new AddonAdapters(this, (type, pos) -> {

            });
            activityAddOnServicesBinding.rvServices.setAdapter(addonAdapters);
            activityAddOnServicesBinding.rvServices.setVisibility(View.VISIBLE);
        } else {
            activityAddOnServicesBinding.rvServices.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setListeners() {
        activityAddOnServicesBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
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

            holder.itemRowBinding.tvPrice.setText(PropertDetailsActivity.propertyServicesList.get(position).servicesRate+" /day");
            holder.itemRowBinding.tvService.setText(PropertDetailsActivity.propertyServicesList.get(position).servicesTitle);

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return PropertDetailsActivity.propertyServicesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutAddonViewBinding itemRowBinding;

            public ViewHolder(LayoutAddonViewBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

}