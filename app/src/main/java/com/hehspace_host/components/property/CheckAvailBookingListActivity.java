package com.hehspace_host.components.property;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityCheckAvailBookingListBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;

public class CheckAvailBookingListActivity extends BaseBindingActivity {

    ActivityCheckAvailBookingListBinding activityCheckAvailBookingListBinding;
    @Override
    protected void setBinding() {
        activityCheckAvailBookingListBinding = DataBindingUtil.setContentView(this,R.layout.activity_check_avail_booking_list);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {

    }

    @Override
    protected void initializeObject() {

    }

    @Override
    protected void setListeners() {

    }

}