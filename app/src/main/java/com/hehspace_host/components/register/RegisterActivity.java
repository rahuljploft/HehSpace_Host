package com.hehspace_host.components.register;

import static android.icu.lang.UProperty.INT_START;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.login.LoginActivity;
import com.hehspace_host.components.login.LoginView_Model;
import com.hehspace_host.components.otp.OTPActivity;
import com.hehspace_host.databinding.ActivityRegisterBinding;
import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.PageModel;
import com.hehspace_host.model.VerifyOtpModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.hehspace_host.util.custom_snackbar.CookieBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterActivity extends BaseBindingActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate {

    ActivityRegisterBinding activityRegisterBinding;
    RegisterView_Model view_model;
    String gender = "";
    String imagepath = "";
    File image_file = null;
    int CAMERA_PIC_REQUEST = 100;
    boolean iscamera = false;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String genderId = "";
    String userType = "";
    String privacyurl = "";
    String termsurl = "";

    @Override
    protected void setBinding() {
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        view_model = new RegisterView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.verifylivedata.observe(this, login_modelApiResponse -> handleResult(login_modelApiResponse));
        checkAndRequestPermissions();
        view_model.livedata.observe(this, propertyCategoryModelApiResponse -> handleResultPage(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.getPages();
        } else {
            Uitility.nointernetDialog(this);
        }

        activityRegisterBinding.rbMale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(activityRegisterBinding.rbMale.isChecked()){
                gender = "male";
                genderId = Constant.MALE;
            }
            else  {
                gender = "female";
                genderId = Constant.FEMALE;
            }

        });
        activityRegisterBinding.rbFemale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(activityRegisterBinding.rbFemale.isChecked()){
                gender = "female";
                genderId = Constant.FEMALE;
            }
            else {
                gender = "male";
                genderId = Constant.MALE;
            }
        });

        SpannableString SpanString = new SpannableString(
                "I agree to the Terms & Conditions and Privacy policy");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(termsurl));
                startActivity(browserIntent);

            }
        };


        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyurl));
                startActivity(browserIntent);


            }
        };

        SpanString.setSpan(teremsAndCondition, 15, 33, 0);
        SpanString.setSpan(privacy, 38, 52, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLACK), 15, 33, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLACK), 38, 52, 0);
        SpanString.setSpan(new UnderlineSpan(), 15, 33, 0);
        SpanString.setSpan(new UnderlineSpan(), 38, 52, 0);
        SpanString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),15, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpanString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),38, 52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        activityRegisterBinding.cbCheck.setMovementMethod(LinkMovementMethod.getInstance());
        activityRegisterBinding.cbCheck.setText(SpanString, TextView.BufferType.SPANNABLE);
        activityRegisterBinding.cbCheck.setChecked(true);
    }

    private void handleResultPage(ApiResponse<PageModel> result) {
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
                        termsurl = result.getData().data.terms_conditions;
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    @Override
    protected void setListeners() {
        activityRegisterBinding.layoutUpload.setOnClickListener(this);
        activityRegisterBinding.btnRegister.setOnClickListener(this);
        activityRegisterBinding.layoutLogin.setOnClickListener(this);
        activityRegisterBinding.ivBack.setOnClickListener(this);
        activityRegisterBinding.password.setOnClickListener(this);
    }

    private void handleResult(ApiResponse<VerifyOtpModel> result) {
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
                        startActivity(new Intent(RegisterActivity.this, OTPActivity.class)
                                .putExtra("fname",activityRegisterBinding.etFirstName.getText().toString())
                                .putExtra("lname",activityRegisterBinding.etLastName.getText().toString())
                                .putExtra("email",activityRegisterBinding.etEmail.getText().toString())
                                .putExtra("phone",activityRegisterBinding.etPhone.getText().toString())
                                .putExtra("password",activityRegisterBinding.etPassword.getText().toString())
                                .putExtra("imagefile",image_file.toString())
                                .putExtra("genderId",genderId)
                                .putExtra("socialmediaID","")
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


    public void ShowHidePass(View view) {

        if(view.getId()==R.id.password){
            if(activityRegisterBinding.etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_eyehide);
                //Show Password
                activityRegisterBinding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eyeshow);
                //Hide Password
                activityRegisterBinding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.layoutUpload) {
            AlertDialog.Builder getImageFrom = new AlertDialog.Builder(this);
            getImageFrom.setTitle("Select:");
            final CharSequence[] opsChars = {"Open Camera", "Take Image"};
            getImageFrom.setItems(opsChars, (dialog, which) -> {
                if (which == 0) {
                    iscamera = true;
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } else if (which == 1) {
                    iscamera = false;
                    BSImagePicker multiSelectionPicker = new BSImagePicker.Builder("com.hehspace_host.provider")//Set this if you want to use multi selection mode.
                            .setMinimumMultiSelectCount(1) //Default: 1.
                            .setMaximumMultiSelectCount(1)//Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                            .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                            .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                            .setMultiSelectDoneTextColor(R.color.colorPrimary) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                            .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                            .disableOverSelectionMessage() //You can also decide not to show this over select message.
                            .build();
                    multiSelectionPicker.show(getSupportFragmentManager(), "picker");
                }
                dialog.dismiss();
            });
            getImageFrom.show();
            return;
        }
        if (view.getId() == R.id.btnRegister) {
            checkValidation();
        }
        if (view.getId() == R.id.layoutLogin) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        if (view.getId() == R.id.ivBack) {
           onBackPressed();
        }
        if (view.getId() == R.id.password) {
            ShowHidePass(activityRegisterBinding.password);
        }
    }

    private void checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                try {
                    assert data != null;
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    activityRegisterBinding.ivUploadSet.setImageBitmap(imageBitmap);
                    activityRegisterBinding.ivUploadSet.setVisibility(View.VISIBLE);
                    activityRegisterBinding.ivUpload.setVisibility(View.GONE);
                    Uri tempUri = getImageUri(this, imageBitmap);

                    image_file = new File(getRealPathFromURI(tempUri));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public void checkValidation() {
        if (TextUtils.isEmpty(activityRegisterBinding.etFirstName.getText().toString())) {
            CookieBar.build(mActivity)
                    .setTitle(R.string.app_name)
                    .setTitleColor(R.color.black)
                    .setMessageColor(R.color.black)
                    .setMessage("First Name Cannot be Blank")
                    .setBackgroundColor(R.color.colorPrimary)
                    .setIconAnimation(R.animator.icon_anim)
                    .setIcon(R.drawable.logo_black)
                    .setDuration(5000) // 5 seconds
                    .show();
            return;
        }
        if (TextUtils.isEmpty(activityRegisterBinding.etLastName.getText().toString())) {
            CookieBar.build(mActivity)
                    .setTitle(R.string.app_name)
                    .setTitleColor(R.color.black)
                    .setMessageColor(R.color.black)
                    .setMessage("Last Name Cannot be Blank")
                    .setBackgroundColor(R.color.colorPrimary)
                    .setIconAnimation(R.animator.icon_anim)
                    .setIcon(R.drawable.logo_black)
                    .setDuration(5000) // 5 seconds
                    .show();
            return;
        }
        if (TextUtils.isEmpty(activityRegisterBinding.etEmail.getText().toString())) {
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
        if (!Uitility.isValidEmail(activityRegisterBinding.etEmail.getText().toString())) {
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
        if (TextUtils.isEmpty(activityRegisterBinding.etPhone.getText().toString())) {
            CookieBar.build(mActivity)
                    .setTitle(R.string.app_name)
                    .setTitleColor(R.color.black)
                    .setMessageColor(R.color.black)
                    .setMessage("Phone Number Cannot be Blank")
                    .setBackgroundColor(R.color.colorPrimary)
                    .setIconAnimation(R.animator.icon_anim)
                    .setIcon(R.drawable.logo_black)
                    .setDuration(5000) // 5 seconds
                    .show();
            return;
        }
        if (TextUtils.isEmpty(activityRegisterBinding.etPassword.getText().toString())) {
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
        if (image_file==null) {
            CookieBar.build(mActivity)
                    .setTitle(R.string.app_name)
                    .setTitleColor(R.color.black)
                    .setMessageColor(R.color.black)
                    .setMessage("Please Upload your ID Proof")
                    .setBackgroundColor(R.color.colorPrimary)
                    .setIconAnimation(R.animator.icon_anim)
                    .setIcon(R.drawable.logo_black)
                    .setDuration(5000) // 5 seconds
                    .show();
            return;
        }
        if(!activityRegisterBinding.cbCheck.isChecked()){
            CookieBar.build(mActivity)
                    .setTitle(R.string.app_name)
                    .setTitleColor(R.color.black)
                    .setMessageColor(R.color.black)
                    .setMessage("Please Agree to the Terms & Conditions")
                    .setBackgroundColor(R.color.colorPrimary)
                    .setIconAnimation(R.animator.icon_anim)
                    .setIcon(R.drawable.logo_black)
                    .setDuration(5000) // 5 seconds
                    .show();
            return;

        }

        if (Uitility.isOnline(this)) {
            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("email_address",activityRegisterBinding.etEmail.getText().toString());
            view_model.verifyOTP(reqData);
        } else {
            Uitility.nointernetDialog(this);
        }
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
//        MultipartBody.Part fileToUpload9 = null;
//        if (imagepath.isEmpty()) {
//            fileToUpload9 = MultipartBody.Part.createFormData("user_photo", "file.getName()");
//        } else {
//            Log.d("qwerty", imagepath + "");
//            File file = new File(imagepath);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//            fileToUpload9 = MultipartBody.Part.createFormData("user_photo", file.getName(), requestBody);
//        }
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        imagepath = getRealPathFromURI(uri + "");
        activityRegisterBinding.tvUploadText.setText("ID Proof Selected");
        image_file = new File(imagepath);
        activityRegisterBinding.ivUploadSet.setImageURI(uri);
        activityRegisterBinding.ivUploadSet.setVisibility(View.VISIBLE);
        activityRegisterBinding.ivUpload.setVisibility(View.GONE);
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

}