package com.hehspace_host.components.sidemenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.hehspace_host.BuildConfig;
import com.hehspace_host.R;
import com.hehspace_host.components.forgotpassword.SetPasswordActivity;
import com.hehspace_host.databinding.ActivitySettingBinding;
import com.hehspace_host.model.PageModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

public class SettingActivity extends BaseBindingActivity {

    ActivitySettingBinding activitySettingsBinding;
    SettingView_Model view_model;
    String privacyurl = "";
    @Override
    protected void setBinding() {
        activitySettingsBinding = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        view_model = new SettingView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.getPages();
        } else {
            Uitility.nointernetDialog(this);
        }
        findViewById(R.id.ivBack).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.layoutChangePassword).setOnClickListener(v ->
        {
            if(sessionManager.isLogin()){
                startActivity(new Intent(this, SetPasswordActivity.class));
            }
        });

        findViewById(R.id.layoutPP).setOnClickListener(v ->
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyurl));
            startActivity(browserIntent);
        });
        if (BuildConfig.BUILD_TYPE.equals("debug")){
            activitySettingsBinding.versioncode.setText(BuildConfig.VERSION_NAME+""+"(Debug)");
        }else {
            activitySettingsBinding.versioncode.setText(BuildConfig.VERSION_NAME+""+"(Release)");
        }
    }

    @Override
    protected void setListeners() {

    }

    private void handleResult(ApiResponse<PageModel> result) {
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
                        privacyurl = result.getData().data.privacy_policy;
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}