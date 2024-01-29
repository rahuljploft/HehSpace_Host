package com.hehspace_host.components.addproperty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hehspace_host.R;
import com.hehspace_host.components.addproperty.viewmodel.PropertyDetailsView_Model;
import com.hehspace_host.databinding.ActivityPropertyDetailsBinding;
import com.hehspace_host.databinding.LayoutHighlightBinding;
import com.hehspace_host.model.PropertyHighlightModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.List;

public class PropertyDetailsActivity extends BaseBindingActivity {

    ActivityPropertyDetailsBinding activityPropertyDetailsBinding;
    PropertyDetailsView_Model view_model;
    HighlightAdapters highlightAdapters;
    int DescLength = 0;


    @Override
    protected void setBinding() {
        activityPropertyDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_details);
        view_model = new PropertyDetailsView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));

        if (Uitility.isOnline(this)) {
            view_model.getPropertyHighlights();
        } else {
            Uitility.nointernetDialog(this);
        }

        if (Constant.ISEDIT.equals("yes")){
            activityPropertyDetailsBinding.etTitle.setText(Constant.PropertyTitle);
            activityPropertyDetailsBinding.tvCount.setText(Constant.PropertyTitle.length()+"");
            activityPropertyDetailsBinding.etDescription.setText(Constant.PropertyDetails);
            activityPropertyDetailsBinding.tvCountdesc.setText(Constant.PropertyDetails.length()+"");
        }
        else {
            activityPropertyDetailsBinding.etTitle.setText(Constant.PropertyTitle);
            activityPropertyDetailsBinding.tvCount.setText(Constant.PropertyTitle.length()+"");
            activityPropertyDetailsBinding.etDescription.setText(Constant.PropertyDetails);
            activityPropertyDetailsBinding.tvCountdesc.setText(Constant.PropertyDetails.length()+"");
        }
        activityPropertyDetailsBinding.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityPropertyDetailsBinding.tvCount.setText(s.length()+"");
            }
        });
        activityPropertyDetailsBinding.etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                activityPropertyDetailsBinding.tvCountdesc.setText(s.length()+"");
            }
        });

        activityPropertyDetailsBinding.rvHighlight.setHasFixedSize(true);
        activityPropertyDetailsBinding.rvHighlight.setLayoutManager(new GridLayoutManager(this,2));

    }

    @Override
    protected void setListeners() {
        activityPropertyDetailsBinding.btnNext.setOnClickListener(this);
        activityPropertyDetailsBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.btnNext){
            if(TextUtils.isEmpty(activityPropertyDetailsBinding.etTitle.getText().toString())){
                Toast.makeText(this, "Please Enter Title", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(activityPropertyDetailsBinding.etDescription.getText().toString())){
                Toast.makeText(this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                return;
            }
            if(activityPropertyDetailsBinding.etDescription.getText().toString().length() < 30){
                Toast.makeText(this, "Description is too short, min 30 character required !", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Constant.HighlightId.isEmpty()){
                Toast.makeText(this, "Please select at least 1 highlight", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.e("highlights", TextUtils.join(",", Constant.HighlightId));
            Constant.map.put("property_title",activityPropertyDetailsBinding.etTitle.getText().toString());
            Constant.map.put("property_highlights", TextUtils.join(",", Constant.HighlightId));
            Constant.map.put("property_details",activityPropertyDetailsBinding.etDescription.getText().toString());
            Constant.PropertyTitle = activityPropertyDetailsBinding.etTitle.getText().toString();
            Constant.PropertyDetails = activityPropertyDetailsBinding.etDescription.getText().toString();
            startActivity(new Intent(this,PropertyPriceActivity.class));
        }
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    private void handleResult(ApiResponse<PropertyHighlightModel> result) {
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

                        if (Constant.ISEDIT.equals("yes")) {
                            if (Constant.HighlightsLive.size() > 0) {
                                for (int i = 0; i < result.getData().data.size(); i++) {
                                    for (int j = 0; j < Constant.HighlightsLive.size(); j++) {
                                        if (result.getData().data.get(i).higlightsTitle.equals(Constant.HighlightsLive.get(j))) {
                                            result.getData().data.get(i).isselected = true;
                                            Log.d("qwerty", result.getData().data.get(i).higlightsTitle);
                                            Constant.HighlightId.add(result.getData().data.get(i).higlightsId);
                                            Constant.Highlights.add(result.getData().data.get(i).higlightsTitle);
                                            break;
                                        } else {
                                            result.getData().data.get(i).isselected = false;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            if (Constant.HighlightId.size() > 0) {
                                for (int i = 0; i < result.getData().data.size(); i++) {
                                    for (int j = 0; j < Constant.HighlightId.size(); j++) {
                                        if (result.getData().data.get(i).higlightsId.equals(Constant.HighlightId.get(j))) {
                                            result.getData().data.get(i).isselected = true;
//                                            Log.d("qwerty", result.getData().data.get(i).higlightsTitle);
//                                            Constant.HighlightId.add(result.getData().data.get(i).higlightsId);
//                                            Constant.Highlights.add(result.getData().data.get(i).higlightsTitle);
                                            break;
                                        } else {
                                            result.getData().data.get(i).isselected = false;
                                        }
                                    }
                                }
                            }
                        }

                         highlightAdapters = new HighlightAdapters(this, result.getData().data ,(type, pos) -> {
                             if(type.equals("select")){
                                 if( result.getData().data.get(pos).isselected){
                                     result.getData().data.get(pos).isselected = false;
                                     Constant.HighlightId.remove( result.getData().data.get(pos).higlightsId);
                                     Constant.Highlights.remove( result.getData().data.get(pos).higlightsTitle);
                                 }
                                 else {
                                     result.getData().data.get(pos).isselected = true;
                                     Constant.HighlightId.add( result.getData().data.get(pos).higlightsId);
                                     Constant.Highlights.add( result.getData().data.get(pos).higlightsTitle);
                                 }
                                 highlightAdapters.notifyDataSetChanged();
                             }
                        });
                        activityPropertyDetailsBinding.rvHighlight.setAdapter(highlightAdapters);
                    }

                }
                break;
        }
    }


    public static class HighlightAdapters extends RecyclerView.Adapter<HighlightAdapters.ViewHolder> {
        Context context;
        ItemClickListner itemClickListner;
        List<PropertyHighlightModel.DataEntity> list;

        public HighlightAdapters(Context context,  List<PropertyHighlightModel.DataEntity> list,ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
            this.list = list;
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
            holder.itemRowBinding.tvHighlights.setText(list.get(position).higlightsTitle);
            if (list.get(position).isselected) {
                holder.itemRowBinding.tvHighlights.setBackground(context.getResources().getDrawable(R.drawable.app_btn));
                holder.itemRowBinding.tvHighlights.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.itemRowBinding.tvHighlights.setBackground(context.getResources().getDrawable(R.drawable.et_bg));
                holder.itemRowBinding.tvHighlights.setTextColor(context.getResources().getColor(R.color.black));
            }

                holder.itemRowBinding.tvHighlights.setOnClickListener(v -> itemClickListner.onClick("select",position));


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

}