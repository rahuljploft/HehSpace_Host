package com.hehspace_host.components.addproperty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityGuestDetailsBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.custom_snackbar.CookieBar;

public class GuestDetailsActivity extends BaseBindingActivity {

    ActivityGuestDetailsBinding activityGuestDetailsBinding;

    @Override
    protected void setBinding() {
        activityGuestDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_guest_details);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {

        if (Constant.ISEDIT.equals("yes")) {
            if (!Constant.AllowedGuest.isEmpty()){
                activityGuestDetailsBinding.etGuest.setText(Constant.AllowedGuest);
            }
        }
        else {
            if(!Constant.AllowedGuest.isEmpty()){
                activityGuestDetailsBinding.etGuest.setText(Constant.AllowedGuest);
            }
        }

    }

    @Override
    protected void setListeners() {
        activityGuestDetailsBinding.btnNext.setOnClickListener(this);
        activityGuestDetailsBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view.getId() == R.id.btnNext) {
            if (activityGuestDetailsBinding.etGuest.getText().toString().equals("")) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Please Enter Guest Count")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if (activityGuestDetailsBinding.etGuest.getText().toString().equals("0")) {
                Toast.makeText(this, "Guest count cannot be 0", Toast.LENGTH_SHORT).show();
                return;
            }
            Constant.AllowedGuest = activityGuestDetailsBinding.etGuest.getText().toString();
            Constant.map.put("allowed_guest", activityGuestDetailsBinding.etGuest.getText().toString());
            startActivity(new Intent(this, AddAmenitiesActivity.class));
        }
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }
}