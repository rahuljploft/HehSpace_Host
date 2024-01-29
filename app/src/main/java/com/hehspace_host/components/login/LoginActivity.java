package com.hehspace_host.components.login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.addproperty.SelectCategoryActivity;
import com.hehspace_host.components.forgotpassword.ForgotPasswordActivity;
import com.hehspace_host.components.otp.OTPActivity;
import com.hehspace_host.components.register.RegisterActivity;
import com.hehspace_host.components.register.RegisterView_Model;
import com.hehspace_host.databinding.ActivityLoginBinding;
import com.hehspace_host.databinding.EmaildailogBinding;
import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.VerifyOtpModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.hehspace_host.util.custom_snackbar.CookieBar;

import org.json.JSONException;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends BaseBindingActivity implements GoogleApiClient.OnConnectionFailedListener{

    ActivityLoginBinding activityLoginBinding;
    LoginView_Model view_model;
    String userType = "";
    CallbackManager callbackManager;
    String first_name,last_name,socialmediaId,Email;
    File image_file=null;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    @Override
    protected void setBinding() {
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        view_model = new LoginView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        view_model.loginlivedata.observe(this, login_modelApiResponse -> handleResult(login_modelApiResponse));
        view_model.loginlivedata1.observe(this, login_modelApiResponse -> handleResultFacebook(login_modelApiResponse));
        view_model.loginlivedata2.observe(this, login_modelApiResponse -> handleResultFacebookEmail(login_modelApiResponse));
        view_model.verifylivedata.observe(this, login_modelApiResponse -> handleResultVerify(login_modelApiResponse));
        activityLoginBinding.loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        activityLoginBinding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                Log.e("facebookData",AccessToken.getCurrentAccessToken()+"");
                //if (!loggedOut) {
                getUserProfile(AccessToken.getCurrentAccessToken());
//                }


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });



    }

    @Override
    protected void setListeners() {
        activityLoginBinding.btnLogin.setOnClickListener(this);
        activityLoginBinding.layoutRegister.setOnClickListener(this);
        activityLoginBinding.tvForgotPassword.setOnClickListener(this);
        activityLoginBinding.rlFacebook.setOnClickListener(this);
        activityLoginBinding.rlGoogle.setOnClickListener(this);
        activityLoginBinding.password.setOnClickListener(this);
    }

    public void ShowHidePass(View view) {

        if(view.getId()==R.id.password){
            if(activityLoginBinding.etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_eyehide);
                //Show Password
                activityLoginBinding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eyeshow);
                //Hide Password
                activityLoginBinding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view.getId() == R.id.btnLogin) {
            if (TextUtils.isEmpty(activityLoginBinding.etEmail.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Email Cannot be Blank")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if (!Uitility.isValidEmail(activityLoginBinding.etEmail.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Enter Valid Email")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if (TextUtils.isEmpty(activityLoginBinding.etPassword.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Password Cannot be Blank")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }

            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("email_address",activityLoginBinding.etEmail.getText().toString());
                reqData.put("user_password", activityLoginBinding.etPassword.getText().toString());
                reqData.put("device_token", sessionManager.getetDEVICE_TOKEN());
                reqData.put("user_type", Constant.USERTYPE);
                view_model.login(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }

        }
        if (view.getId() == R.id.layoutRegister) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
        if (view.getId() == R.id.tvForgotPassword) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        }
        if (view.getId() == R.id.rlFacebook) {
            activityLoginBinding.loginButton.performClick();
        }
        if (view.getId() == R.id.password) {
            ShowHidePass(activityLoginBinding.password);
        }
        if (view.getId() == R.id.rlGoogle) {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,RC_SIGN_IN);
        }
    }

    private void handleResult(ApiResponse<LoginModel> result) {
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
                        sessionManager.setLogin();
                        sessionManager.setUSER_ID(result.getData().data.profileData.userId);
                        sessionManager.setEMAIL(result.getData().data.profileData.emailAddress);
                        sessionManager.setNAME(result.getData().data.profileData.firstName +" "+ result.getData().data.profileData.lastName);
                        sessionManager.setPHONE(result.getData().data.profileData.mobileNumber);
                        sessionManager.setFNAME(result.getData().data.profileData.firstName.substring(0, 1).toUpperCase()+result.getData().data.profileData.firstName.substring(1).toLowerCase());
                        sessionManager.setLNAME(result.getData().data.profileData.lastName.substring(0, 1).toUpperCase()+result.getData().data.profileData.lastName.substring(1).toLowerCase());
                        sessionManager.setUSER_TYPE(userType);
                        sessionManager.setAuthToken(result.getData().data.accessToken);
                        sessionManager.setPROFILE_IMAGE(result.getData().data.profileData.userPicName);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    private void handleResultVerify(ApiResponse<VerifyOtpModel> result) {
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
                        if(image_file == null){
                            image_file = new File("");
                        }
                        startActivity(new Intent(LoginActivity.this, OTPActivity.class)
                                .putExtra("fname",first_name)
                                .putExtra("lname",last_name)
                                .putExtra("email",Email)
                                .putExtra("phone","")
                                .putExtra("password","")
                                .putExtra("imagefile",image_file.toString())
                                .putExtra("genderId","")
                                .putExtra("socialmediaID",socialmediaId)
                                .putExtra("otp",result.getData().data.verifactionOtp)
                        );
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }
    private void handleResultFacebook(ApiResponse<LoginModel> result) {
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
                        sessionManager.setLogin();
                        sessionManager.setUSER_ID(result.getData().data.profileData.userId);
                        sessionManager.setEMAIL(result.getData().data.profileData.emailAddress);
                        sessionManager.setNAME(result.getData().data.profileData.firstName +" "+ result.getData().data.profileData.lastName);
                        sessionManager.setFNAME(result.getData().data.profileData.firstName.substring(0, 1).toUpperCase()+result.getData().data.profileData.firstName.substring(1).toLowerCase());
                        sessionManager.setLNAME(result.getData().data.profileData.lastName.substring(0, 1).toUpperCase()+result.getData().data.profileData.lastName.substring(1).toLowerCase());
                        sessionManager.setPHONE(result.getData().data.profileData.mobileNumber);
                        sessionManager.setUSER_TYPE(userType);
                        sessionManager.setAuthToken(result.getData().data.accessToken);
                        sessionManager.setPROFILE_IMAGE(result.getData().data.profileData.userPicName);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else {
                        emailDailog();
                    }
                    Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void handleResultFacebookEmail(ApiResponse<LoginModel> result) {
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
                        sessionManager.setLogin();
                        sessionManager.setUSER_ID(result.getData().data.profileData.userId);
                        sessionManager.setEMAIL(result.getData().data.profileData.emailAddress);
                        sessionManager.setNAME(result.getData().data.profileData.firstName +" "+ result.getData().data.profileData.lastName);
                        sessionManager.setFNAME(result.getData().data.profileData.firstName.substring(0, 1).toUpperCase()+result.getData().data.profileData.firstName.substring(1).toLowerCase());
                        sessionManager.setLNAME(result.getData().data.profileData.lastName.substring(0, 1).toUpperCase()+result.getData().data.profileData.lastName.substring(1).toLowerCase());
                        sessionManager.setPHONE(result.getData().data.profileData.mobileNumber);
                        sessionManager.setUSER_TYPE(userType);
                        sessionManager.setAuthToken(result.getData().data.accessToken);
                        sessionManager.setPROFILE_IMAGE(result.getData().data.profileData.userPicName);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (Uitility.isOnline(this)) {
                            HashMap<String, String> reqData = new HashMap<>();
                            reqData.put("email_address",Email);
                            view_model.verifyOTP(reqData);
                        } else {
                            Uitility.nointernetDialog(this);
                        }
                    }

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            Log.e("dfghj",account.getId());
            Log.e("dfghj",account.getDisplayName());
            Log.e("dfghj",account.getEmail());
            socialmediaId = account.getId();
            String[] splited = account.getDisplayName().split("\\s+");
            first_name = splited[0];
            last_name = splited[1];
            Email = account.getEmail();
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("social_media_id",socialmediaId);
                reqData.put("device_token", sessionManager.getetDEVICE_TOKEN());
                reqData.put("user_type", Constant.USERTYPE);
                reqData.put("email_address", Email);
                view_model.socialLoginEmail(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, (object, response) -> {
                    Log.d("TAG", object.toString());
                    try {
                        first_name = object.getString("first_name");
                        last_name = object.getString("last_name");
                        //  String email = object.getString("email");
                        socialmediaId = object.getString("id");
                        String image_url = "https://graph.facebook.com/" + socialmediaId + "/picture?type=normal";
                        if (Uitility.isOnline(this)) {
                            HashMap<String, String> reqData = new HashMap<>();
                            reqData.put("social_media_id",socialmediaId);
                            reqData.put("device_token", sessionManager.getetDEVICE_TOKEN());
                            reqData.put("user_type", Constant.USERTYPE);
                            view_model.socialLogin(reqData);
                        } else {
                            Uitility.nointernetDialog(this);
                        }
//                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            txtEmail.setText(email);
//                            Picasso.with(LoginActivity.this).load(image_url).into(imageView);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }


    private void emailDailog() {
        Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        EmaildailogBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mActivity),
                R.layout.emaildailog,
                null,
                false
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialogBinding.tvTitle.setText(resources.getString(R.string.internet_issue))
//        dialogBinding.tvMessage.setText(resources.getString(R.string.please_check_your_internet))
        binding.ivClose.setOnClickListener(v ->{
            LoginManager.getInstance().logOut();
            dialog.dismiss();});

        binding.btnRegister.setOnClickListener(v -> {
            Email = binding.etEmail.getText().toString();
            if(TextUtils.isEmpty(Email)){
                Toast.makeText(this, "Email cannot be blank", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("social_media_id",socialmediaId);
                reqData.put("device_token", sessionManager.getetDEVICE_TOKEN());
                reqData.put("user_type", Constant.USERTYPE);
                reqData.put("email_address", Email);
                view_model.socialLoginEmail(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }

        });
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(binding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}