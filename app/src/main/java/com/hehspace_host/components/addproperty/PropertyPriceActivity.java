package com.hehspace_host.components.addproperty;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityPropertyPriceBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.custom_snackbar.CookieBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PropertyPriceActivity extends BaseBindingActivity {

    ActivityPropertyPriceBinding activityPropertyPriceBinding;
    int i = 2;
    int j = 2;
    int k = 2;
    int l = 2;

    @Override
    protected void setBinding() {
        activityPropertyPriceBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_price);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {

        activityPropertyPriceBinding.cbDaily.setChecked(false);
        activityPropertyPriceBinding.cbWeekly.setChecked(false);
        activityPropertyPriceBinding.cbMonthly.setChecked(false);

        activityPropertyPriceBinding.cbDaily.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(activityPropertyPriceBinding.cbDaily.isChecked()){
                activityPropertyPriceBinding.layoutDailyPrice.setVisibility(View.VISIBLE);
            }
            else {
                activityPropertyPriceBinding.layoutDailyPrice.setVisibility(View.GONE);
                activityPropertyPriceBinding.etDailyPrice.setText("");
            }
        });
        activityPropertyPriceBinding.cbWeekly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(activityPropertyPriceBinding.cbWeekly.isChecked()){
                activityPropertyPriceBinding.layoutWeeklyPrice.setVisibility(View.VISIBLE);
            }
            else {
                activityPropertyPriceBinding.layoutWeeklyPrice.setVisibility(View.GONE);
                activityPropertyPriceBinding.etWeeklyPrice.setText("");
            }
        });
        activityPropertyPriceBinding.cbMonthly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(activityPropertyPriceBinding.cbMonthly.isChecked()){
                activityPropertyPriceBinding.layoutMonthlyPrice.setVisibility(View.VISIBLE);
            }
            else{
                activityPropertyPriceBinding.layoutMonthlyPrice.setVisibility(View.GONE);
                activityPropertyPriceBinding.etMonthlyPrice.setText("");
            }
        });

//        HORLYADDMINUS
        activityPropertyPriceBinding.HourlyAdd.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etPropertyPrice.getText().toString())){
                activityPropertyPriceBinding.etPropertyPrice.setText(String.valueOf(i));
            }
            else {
                i = Integer.parseInt(activityPropertyPriceBinding.etPropertyPrice.getText().toString());
            }
            i++;
            activityPropertyPriceBinding.etPropertyPrice.setText(String.valueOf(i));
        });
        activityPropertyPriceBinding.HourlyMinus.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etPropertyPrice.getText().toString())){
                if(i<1){
                    Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    activityPropertyPriceBinding.etPropertyPrice.setText(String.valueOf(i));
                }

            }
            else {
                i = Integer.parseInt(activityPropertyPriceBinding.etPropertyPrice.getText().toString());
            }
            if(i<1){
                Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                return;
            }
            else i--;
            activityPropertyPriceBinding.etPropertyPrice.setText(String.valueOf(i));
        });


//      DAILYADDMINUS
        activityPropertyPriceBinding.DailyAdd.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etDailyPrice.getText().toString())){
                activityPropertyPriceBinding.etDailyPrice.setText(String.valueOf(j));
            }
            else {
                j = Integer.parseInt(activityPropertyPriceBinding.etDailyPrice.getText().toString());
            }
            j++;
            activityPropertyPriceBinding.etDailyPrice.setText(String.valueOf(j));
        });

        activityPropertyPriceBinding.DailyMinus.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etDailyPrice.getText().toString())){
                if(j<1){
                    Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    activityPropertyPriceBinding.etDailyPrice.setText(String.valueOf(j));
                }

            }
            else {
                j = Integer.parseInt(activityPropertyPriceBinding.etDailyPrice.getText().toString());
            }
            if(j<1){
                Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                return;
            }
            else j--;
            activityPropertyPriceBinding.etDailyPrice.setText(String.valueOf(j));
        });

        //      WEEKLYADDMINUS

        activityPropertyPriceBinding.WeeklyAdd.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etWeeklyPrice.getText().toString())){
                activityPropertyPriceBinding.etWeeklyPrice.setText(String.valueOf(k));
            }
            else {
                k = Integer.parseInt(activityPropertyPriceBinding.etWeeklyPrice.getText().toString());
            }
            k++;
            activityPropertyPriceBinding.etWeeklyPrice.setText(String.valueOf(k));
        });

        activityPropertyPriceBinding.WeeklyMinus.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etWeeklyPrice.getText().toString())){
                if(k<1){
                    Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    activityPropertyPriceBinding.etWeeklyPrice.setText(String.valueOf(k));
                }

            }
            else {
                k = Integer.parseInt(activityPropertyPriceBinding.etWeeklyPrice.getText().toString());
            }
            if(k<1){
                Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                return;
            }
            else k--;
            activityPropertyPriceBinding.etWeeklyPrice.setText(String.valueOf(k));
        });

        //      MONTHLYADDMINUS
        activityPropertyPriceBinding.MonthlyAdd.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etMonthlyPrice.getText().toString())){
                activityPropertyPriceBinding.etMonthlyPrice.setText(String.valueOf(l));
            }
            else {
                l = Integer.parseInt(activityPropertyPriceBinding.etMonthlyPrice.getText().toString());
            }
            l++;
            activityPropertyPriceBinding.etMonthlyPrice.setText(String.valueOf(l));
        });

        activityPropertyPriceBinding.MonthlyMinus.setOnClickListener(v -> {
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etMonthlyPrice.getText().toString())){
                if(l<1){
                    Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    activityPropertyPriceBinding.etMonthlyPrice.setText(String.valueOf(l));
                }

            }
            else {
                l = Integer.parseInt(activityPropertyPriceBinding.etMonthlyPrice.getText().toString());
            }
            if(l<1){
                Toast.makeText(mActivity, "Price cannot be less than 1", Toast.LENGTH_SHORT).show();
                return;
            }
            else l--;
            activityPropertyPriceBinding.etMonthlyPrice.setText(String.valueOf(l));
        });

        if (Constant.ISEDIT.equals("yes")){
            activityPropertyPriceBinding.etPropertyPrice.setText(Constant.HourlyPrice);
            activityPropertyPriceBinding.tvCheckInTime.setText(Constant.CheckInTime);
            activityPropertyPriceBinding.tvCheckOutTime.setText(Constant.CheckOutTime);
            Log.e("dddf",Constant.DailyPrice);
            Log.e("dddf",Constant.WeeklyPrice);
            Log.e("dddf",Constant.MonthlyPrice);
            if(!Constant.DailyPrice.equals("")){
                activityPropertyPriceBinding.cbDaily.setChecked(true);
                activityPropertyPriceBinding.layoutDailyPrice.setVisibility(View.VISIBLE);
                activityPropertyPriceBinding.etDailyPrice.setText(Constant.DailyPrice);
            }
            else {
                activityPropertyPriceBinding.cbDaily.setChecked(false);
                activityPropertyPriceBinding.layoutDailyPrice.setVisibility(View.GONE);
            }
            if(!Constant.WeeklyPrice.equals("")){
                activityPropertyPriceBinding.cbWeekly.setChecked(true);
                activityPropertyPriceBinding.layoutWeeklyPrice.setVisibility(View.VISIBLE);
                activityPropertyPriceBinding.etWeeklyPrice.setText(Constant.WeeklyPrice);
            }
            else {
                activityPropertyPriceBinding.cbWeekly.setChecked(false);
                activityPropertyPriceBinding.layoutWeeklyPrice.setVisibility(View.GONE);
            }
            if(!Constant.MonthlyPrice.equals("")){
                activityPropertyPriceBinding.cbMonthly.setChecked(true);
                activityPropertyPriceBinding.layoutMonthlyPrice.setVisibility(View.VISIBLE);
                activityPropertyPriceBinding.etMonthlyPrice.setText(Constant.MonthlyPrice);
            }
            else {
                activityPropertyPriceBinding.cbMonthly.setChecked(false);
                activityPropertyPriceBinding.layoutMonthlyPrice.setVisibility(View.GONE);
            }
        }
        else {
            activityPropertyPriceBinding.etPropertyPrice.setText(Constant.HourlyPrice);
            activityPropertyPriceBinding.tvCheckInTime.setText(Constant.CheckInTime);
            activityPropertyPriceBinding.tvCheckOutTime.setText(Constant.CheckOutTime);
            if(!Constant.DailyPrice.equals("")){
                activityPropertyPriceBinding.cbDaily.setChecked(true);
                activityPropertyPriceBinding.layoutDailyPrice.setVisibility(View.VISIBLE);
                activityPropertyPriceBinding.etDailyPrice.setText(Constant.DailyPrice);
            }
            else {
                activityPropertyPriceBinding.cbDaily.setChecked(false);
                activityPropertyPriceBinding.layoutDailyPrice.setVisibility(View.GONE);
            }
            if(!Constant.WeeklyPrice.equals("")){
                activityPropertyPriceBinding.cbWeekly.setChecked(true);
                activityPropertyPriceBinding.layoutWeeklyPrice.setVisibility(View.VISIBLE);
                activityPropertyPriceBinding.etWeeklyPrice.setText(Constant.WeeklyPrice);
            }
            else {
                activityPropertyPriceBinding.cbWeekly.setChecked(false);
                activityPropertyPriceBinding.layoutWeeklyPrice.setVisibility(View.GONE);
            }
            if(!Constant.MonthlyPrice.equals("")){
                activityPropertyPriceBinding.cbMonthly.setChecked(true);
                activityPropertyPriceBinding.layoutMonthlyPrice.setVisibility(View.VISIBLE);
                activityPropertyPriceBinding.etMonthlyPrice.setText(Constant.MonthlyPrice);
            }
            else {
                activityPropertyPriceBinding.cbMonthly.setChecked(false);
                activityPropertyPriceBinding.layoutMonthlyPrice.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void setListeners() {
        activityPropertyPriceBinding.layoutCheckIn.setOnClickListener(this);
        activityPropertyPriceBinding.layoutCheckOut.setOnClickListener(this);
        activityPropertyPriceBinding.btnNext.setOnClickListener(this);
        activityPropertyPriceBinding.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.layoutCheckIn){
            openClock(activityPropertyPriceBinding.tvCheckInTime);
        }
        if(view.getId() == R.id.layoutCheckOut){
            openClock(activityPropertyPriceBinding.tvCheckOutTime);
        }
        if(view.getId() == R.id.btnNext){
            if(TextUtils.isEmpty(activityPropertyPriceBinding.etPropertyPrice.getText().toString())){
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Please add Hourly rate!")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if(activityPropertyPriceBinding.etPropertyPrice.getText().toString().equals("0")){
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Price cannot be 0")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if(TextUtils.isEmpty(activityPropertyPriceBinding.tvCheckInTime.getText().toString())){
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Please Select Check-In Time")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if(TextUtils.isEmpty(activityPropertyPriceBinding.tvCheckOutTime.getText().toString())){
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Please Enter Check-Out Time")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }

            String hour = "";

//            if(activityPropertyPriceBinding.etPropertyPrice.getText().toString().contains("$")){
//                String[] parts = activityPropertyPriceBinding.etPropertyPrice.getText().toString().split("$");
//                hour = parts[1];
//                Log.e("parts",parts[0]+"======"+parts[1]);
//            }
//            else  hour  = activityPropertyPriceBinding.etPropertyPrice.getText().toString();

            Constant.CheckInTime = activityPropertyPriceBinding.tvCheckInTime.getText().toString();
            Constant.CheckOutTime = activityPropertyPriceBinding.tvCheckOutTime.getText().toString();
            Constant.HourlyPrice = activityPropertyPriceBinding.etPropertyPrice.getText().toString();
            Constant.DailyPrice = activityPropertyPriceBinding.etDailyPrice.getText().toString();
            Constant.WeeklyPrice = activityPropertyPriceBinding.etWeeklyPrice.getText().toString();
            Constant.MonthlyPrice = activityPropertyPriceBinding.etMonthlyPrice.getText().toString();

            Constant.map.put("checkin_time",activityPropertyPriceBinding.tvCheckInTime.getText().toString());
            Constant.map.put("checkout_time",activityPropertyPriceBinding.tvCheckOutTime.getText().toString());
            Constant.map.put("hourly_rate",activityPropertyPriceBinding.etPropertyPrice.getText().toString());
            Constant.map.put("daily_rate",activityPropertyPriceBinding.etDailyPrice.getText().toString());
            Constant.map.put("weekly_rate",activityPropertyPriceBinding.etWeeklyPrice.getText().toString());
            Constant.map.put("monthly_rate",activityPropertyPriceBinding.etMonthlyPrice.getText().toString());

            startActivity(new Intent(this,AddOnServiceActivity.class));
        }
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }


    public void openClock(TextView textView){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
            mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            mcurrentTime.set(Calendar.MINUTE, selectedMinute);
            SimpleDateFormat  mFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            boolean isPM = (selectedHour >= 12);
            textView.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
//            textView.setText(mFormat.format(mcurrentTime.getTime()));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

}