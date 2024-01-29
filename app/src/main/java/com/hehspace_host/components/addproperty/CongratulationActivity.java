package com.hehspace_host.components.addproperty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityCongratulationBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;

import org.json.JSONArray;

import java.util.ArrayList;

public class CongratulationActivity extends BaseBindingActivity {

    ActivityCongratulationBinding activityCongratulationBinding;

    @Override
    protected void setBinding() {
        activityCongratulationBinding = DataBindingUtil.setContentView(this, R.layout.activity_congratulation);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {

    }

    @Override
    protected void setListeners() {
    activityCongratulationBinding.layoutJump.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.layoutJump){
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
            Constant.servicessList=new ArrayList<>();;
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        Constant.servicessList=new ArrayList<>();;
        startActivity(new Intent(this,MainActivity.class));
    }
}