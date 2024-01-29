package com.hehspace_host;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.hehspace_host.Play_Store_Update.Constants;
import com.hehspace_host.Play_Store_Update.InAppUpdateManager;
import com.hehspace_host.Play_Store_Update.InAppUpdateStatus;
import com.hehspace_host.components.addproperty.SelectCategoryActivity;
import com.hehspace_host.components.fragments.dashboard.booking.BookingFragment;
import com.hehspace_host.components.fragments.dashboard.home.HomeFragment;
import com.hehspace_host.components.fragments.dashboard.inbox.InboxFragment;
import com.hehspace_host.components.fragments.dashboard.profile.ProfileFragment;
import com.hehspace_host.components.request.RequestActivity;
import com.hehspace_host.components.sidemenu.EarningActivity;
import com.hehspace_host.components.sidemenu.NotificationActivity;
import com.hehspace_host.components.sidemenu.SettingActivity;
import com.hehspace_host.components.splash.SplashActivity;
import com.hehspace_host.databinding.ActivityMainBinding;
import com.hehspace_host.databinding.DailogHirePhotographerBinding;
import com.hehspace_host.databinding.RowDrawerBinding;
import com.hehspace_host.databinding.SupportDailogBinding;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.DrawerData;
import com.hehspace_host.model.PropertyListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.hehspace_host.util.custom_snackbar.CookieBar;
import com.hehspace_host.util.utilss;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseBindingActivity implements InAppUpdateManager.InAppUpdateHandler ,HomeFragment.onSomeEventListener,
        ProfileFragment.onDataSomeEventListener{

    ActivityMainBinding activityMainBinding;
    HomeFragment homeFragment;
    BookingFragment bookingFragment;
    InboxFragment inboxFragment;
    ProfileFragment profileFragment;
    List<DrawerData> list;
    DrawerAdapter drawerAdapter;
    String value = "";
    String my = "";
    public static TextView tvNotCount;
    MainView_Model view_model;
    private InAppUpdateManager inAppUpdateManager;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    @Override
    protected void setBinding() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        view_model = new MainView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        if (getIntent().hasExtra("value")) {
            value = getIntent().getStringExtra("value");
        }
        if (getIntent().hasExtra("my")) {
            my = getIntent().getStringExtra("my");
        }
        activityMainBinding.username.setText(sessionManager.getNAME());
        Glide.with(this)
                .load(sessionManager.getPROFILE_IMAGE())
                .error(R.drawable.user_dummy)
                .into(activityMainBinding.ivUserIcon);
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        tvNotCount = findViewById(R.id.tvNotCount);
        activityMainBinding.layoutMain.backRel.setOnClickListener(v -> {
            activityMainBinding.drawerLayout.openDrawer(GravityCompat.START);
            activityMainBinding.username.setText(sessionManager.getNAME());
            Glide.with(this)
                    .load(sessionManager.getPROFILE_IMAGE())
                    .error(R.drawable.user_dummy)
                    .into(activityMainBinding.ivUserIcon);

        });
        activityMainBinding.layoutMain.ivNotification.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NotificationActivity.class)));
        activityMainBinding.close.setOnClickListener(v -> activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START));
        homeFragment = new HomeFragment();
        bookingFragment = new BookingFragment();
        inboxFragment = new InboxFragment();
        profileFragment = new ProfileFragment();

        setUpBottomBar();
        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true)
                .mode(Constants.UpdateMode.IMMEDIATE)
                .useCustomNotification(true)
                .handler(this);

        Log.e("token", sessionManager.getAuthToken());
    }

    @Override
    protected void setListeners() {

    }

    private void handleResult(ApiResponse<CommonModel> result) {
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
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        startActivity(new Intent(this, MainActivity.class));
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        transaction.addToBackStack(fragment.toString());
        transaction.commit();
    }

    private void loadFragment(Fragment fragment, Bundle bundle, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        fragment.setArguments(bundle);
        transaction.addToBackStack(fragment.toString());
        transaction.commit();
    }

    public void setUpBottomBar() {
        if (value.equals("booking")) {
            Bundle args = new Bundle();
            args.putString("my", my);
            Log.e("value", value);
            Log.e("value", my);
            loadFragment(bookingFragment, args, "book");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
            activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
        } else {
            loadFragment(homeFragment, "home");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.VISIBLE);
            activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        activityMainBinding.layoutMain.layoutBottom.layoutAdd.setOnClickListener(v -> {
            Constant.ISEDIT = "no";
            Constant.CategoryId = new ArrayList<>();
            Constant.CategoryName = new ArrayList<>();
            Constant.LocationTag = "0";
            Constant.PropertyAddress = "";
            Constant.BuildingName = "";
            Constant.StreetName = "";
            Constant.CityName = "";
            Constant.StateName = "";
            Constant.CountryName = "";
            Constant.PostCode = "";
            Constant.AllowedGuest = "";
            Constant.AmenitiesId = new ArrayList<>();
            Constant.AmenitiesName = new ArrayList<>();
            Constant.imageData = new ArrayList<>();
            Constant.PropertyTitle = "";
            Constant.PropertyDetails = "";
            Constant.HighlightId = new ArrayList<>();
            Constant.Highlights = new ArrayList<>();
            Constant.HourlyPrice = "";
            Constant.CheckInTime = "";
            Constant.CheckOutTime = "";
            Constant.DailyPrice = "";
            Constant.WeeklyPrice = "";
            Constant.MonthlyPrice = "";
            Constant.CleanerFee = "";
            Constant.additionalServices = new JSONArray();
            Constant.servicessList = new ArrayList<>();
            ;
            startActivity(new Intent(this, SelectCategoryActivity.class));
        });

        activityMainBinding.layoutMain.layoutBottom.layoutHome.setOnClickListener(v -> {
            loadFragment(homeFragment, "home");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.VISIBLE);
            activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

            activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        activityMainBinding.layoutMain.layoutBottom.layoutBooking.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("my", my);
            loadFragment(bookingFragment, args, "book");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
            activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        activityMainBinding.layoutMain.layoutBottom.layoutInbox.setOnClickListener(v -> {
            loadFragment(inboxFragment, "inbox");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        activityMainBinding.layoutMain.layoutBottom.layoutProfile.setOnClickListener(v -> {
            loadFragment(profileFragment, "pro");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
        });
    }

    private void setDrawerAdapter(String stripestatus, String stripurl1) {
        list = new ArrayList<>();
        String[] mTestArray = getResources().getStringArray(R.array.sidemenu);
        for (int i = 0; i < mTestArray.length; i++) {
            list.add(new DrawerData(i, mTestArray[i]));
        }
        drawerAdapter = new DrawerAdapter(list,stripestatus,stripurl1);
        activityMainBinding.navigationList.setAdapter(drawerAdapter);
    }

    @Override
    public void someEvent(String stripestatus, String stripurl1) {
        Log.d("asfafaFD",stripestatus);
        setDrawerAdapter(stripestatus,stripurl1);
    }

    @Override
    public void someDataEvent(String name, String photourl) {
        activityMainBinding.username.setText(name);
        Glide.with(this)
                .load(photourl)
                .error(R.drawable.user_dummy)
                .into(activityMainBinding.ivUserIcon);
    }

    public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
        List<DrawerData> list;
        String stripestatus;
        String stripurl1;
        public DrawerAdapter(List<DrawerData> list,String stripestatus, String stripurl1) {
            this.list = list;
            this.stripestatus = stripestatus;
            this.stripurl1 = stripurl1;
        }


        @Override
        public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RowDrawerBinding rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_drawer, parent,
                    false);
            return new DrawerAdapter.ViewHolder(rowDrawerBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull DrawerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            DrawerData drawerData = list.get(position);
            holder.itemRowBinding.txtDrawerName.setText(drawerData.name);
            if (position == 0) {
                holder.itemRowBinding.tvStatus.setVisibility(View.VISIBLE);
            } else {
                holder.itemRowBinding.tvStatus.setVisibility(View.GONE);
            }

            if (stripestatus.equals("Active")) {
                holder.itemRowBinding.tvStatus.setBackground(mActivity.getResources().getDrawable(R.drawable.green_bg));
                holder.itemRowBinding.tvStatus.setText("Verified");
            }else {
                holder.itemRowBinding.tvStatus.setBackground(mActivity.getResources().getDrawable(R.drawable.red_bg));
                holder.itemRowBinding.tvStatus.setText(sessionManager.getSStatus());
            }
            holder.itemRowBinding.llMain.setOnClickListener(v -> {
                switch (position) {
                    case 0:
                        if (stripestatus.equals("Active")) {
                            CookieBar.build(MainActivity.this)
                                    .setTitle(R.string.app_name)
                                    .setTitleColor(R.color.black)
                                    .setMessageColor(R.color.black)
                                    .setMessage("Payment profile already Verified!")
                                    .setBackgroundColor(R.color.colorPrimary)
                                    .setIconAnimation(R.animator.icon_anim)
                                    .setDuration(5000) // 5 seconds
                                    .show();
                        } else {
                            alertView(stripurl1);
                        }
                        break;

                    case 1:
                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, RequestActivity.class));
                        break;

                    case 2:
                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        //startActivity(new Intent(MainActivity.this, MainActivity.class).putExtra("value", "booking"));
                        Bundle args = new Bundle();
                        args.putString("my", my);
                        Log.e("value", value);
                        Log.e("value", my);
                        loadFragment(bookingFragment, args, "book");
                        activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
                        activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary),
                                android.graphics.PorterDuff.Mode.SRC_IN);
                        activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                        activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black),
                                android.graphics.PorterDuff.Mode.SRC_IN);
                        activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                        activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black),
                                android.graphics.PorterDuff.Mode.SRC_IN);
                        activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                        activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black),
                                android.graphics.PorterDuff.Mode.SRC_IN);
                        activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                        break;

                    case 3:
                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, EarningActivity.class));
                        break;

                    case 4:
                        if (HomeFragment.propertyList.size() > 0) {
                            showHirePhotographerDialog();
                        } else
                            Toast.makeText(mActivity, "You have no Properties", Toast.LENGTH_SHORT).show();

                        break;

                    case 5:
                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                        break;

                    case 6:
                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;

                    case 7:
                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportDialog();
                        break;

                    case 8:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Logout")
                                .setMessage("Are you sure, you want to Logout?")
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        dialoginterface.cancel();
                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                                        sessionManager.logout();
                                        LoginManager.getInstance().logOut();
//                                        Constant.CategoryId.clear();
//                                        Constant.CategoryName.clear();
//                                        Constant.servicessList.clear();
//                                        Constant.additionalServices = new JSONArray();
//                                        Constant.AmenitiesName.clear();
//                                        Constant.AmenitiesId.clear();
//                                        Constant.Highlights.clear();
//                                        Constant.HighlightId.clear();
//                                        Constant.HighlightsLive.clear();
//                                        Constant.HourlyPrice = "";
//                                        Constant.DailyPrice = "";
//                                        Constant.WeeklyPrice = "";
//                                        Constant.MonthlyPrice = "";
//                                        Constant.CleanerFee = "";
//                                        Constant.HirePhotographer = "";
//                                        Constant.CityName = "";
//                                        Constant.CountryName = "";
//                                        Constant.CheckInTime = "";
//                                        Constant.CheckOutTime = "";
//                                        Constant.PropertyTitle = "";
//                                        Constant.PropertyDetails = "";
                                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                                    }
                                }).show();
                        break;
                }

            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RowDrawerBinding itemRowBinding;

            public ViewHolder(RowDrawerBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }

    String pId = "";

    private void showHirePhotographerDialog() {
        Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DailogHirePhotographerBinding dailogHirePhotographerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mActivity),
                R.layout.dailog_hire_photographer,
                null,
                false
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialogBinding.tvTitle.setText(resources.getString(R.string.internet_issue))
//        dialogBinding.tvMessage.setText(resources.getString(R.string.please_check_your_internet))
        dailogHirePhotographerBinding.ivClose.setOnClickListener(v -> {
            HomeFragment.propertyList.remove(0);
            dialog.dismiss();
        });
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(dailogHirePhotographerBinding.getRoot());
        Spinner spProperty = dialog.findViewById(R.id.spProperty);

        CustomAdapter arrayAdapter = new
                CustomAdapter(this);
        //  spProperty.setAdapter(arrayAdapter);
        dailogHirePhotographerBinding.btnOK.setOnClickListener(v -> {
            if (pId.equals("0")) {
                Toast.makeText(this, "Please Select Property", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Uitility.isOnline(this)) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("property_id", pId);
                view_model.submitPhotographer(hashMap);
            } else {
                Uitility.nointernetDialog(this);
            }
            dialog.dismiss();
        });

        if (HomeFragment.propertyList.size() > 0) {
            PropertyListModel.DataEntity p = new PropertyListModel.DataEntity();
            p.propertyId = "0";
            p.propertyTitle = "Select Property";
            HomeFragment.propertyList.add(0, p);
            //     arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProperty.setAdapter(arrayAdapter);
            spProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    PropertyListModel.DataEntity p = arrayAdapter.getItem(position);
                    Log.e("sdff", p.propertyId);
                    pId = HomeFragment.propertyList.get(position).propertyId;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public class CustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext) {
            this.context = applicationContext;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return HomeFragment.propertyList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.spinner_row, null);
            //ImageView icon = (ImageView) view.findViewById(R.id.imageView);
            TextView names = view.findViewById(R.id.cust_view);
            // icon.setImageResource(flags[i]);
            names.setText(HomeFragment.propertyList.get(i).propertyTitle);
            return view;
        }
    }

    private void getSupportDialog() {
        Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        SupportDailogBinding dailogHirePhotographerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mActivity),
                R.layout.support_dailog,
                null,
                false
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialogBinding.tvTitle.setText(resources.getString(R.string.internet_issue))
//        dialogBinding.tvMessage.setText(resources.getString(R.string.please_check_your_internet))
        dailogHirePhotographerBinding.ivClose.setOnClickListener(v ->
                dialog.dismiss());
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        dailogHirePhotographerBinding.layoutEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{dailogHirePhotographerBinding.tvEmail.getText().toString()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
            startActivity(Intent.createChooser(intent, ""));
            dialog.dismiss();
        });
        dailogHirePhotographerBinding.layoutCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:7976232985"));
            startActivity(intent);
            dialog.dismiss();
        });

        dailogHirePhotographerBinding.layoutWeb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.hehspace.com"));
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(dailogHirePhotographerBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    private void alertView(String stripurl1) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Payment Profile!")
                .setMessage("Dear " + sessionManager.getNAME() + ", Please use your existing Email ID & Password for setup Payment Profile, Tap Yes to Continue !")
                .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                .setPositiveButton("Yes", (dialoginterface, i) -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stripurl1));
                    startActivity(browserIntent);
                }).show();
    }
    //@Override
//    public void onRestart() {
//        super.onRestart();
//        finish();
//        startActivity(getIntent());
//    }
//

    @Override
    public void onBackPressed() {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Log.d("qwerty", getVisibleFragment().getId() + "__");
            if (getVisibleFragment().getTag().equals("home")) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Exit App")
                        .setMessage("Are you sure, you want to exit App ?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                finishAffinity();
                            }
                        }).show();
            } else {
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.VISIBLE);
                activityMainBinding.layoutMain.layoutBottom.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

                activityMainBinding.layoutMain.layoutBottom.ivBooking.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvBooking.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
                loadFragment(homeFragment, "home");
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == utilss.PERMISSION_REUEST_CODE) {
            HashMap<String, Integer> permissionresult = new HashMap<>();
            int deniedcount = 0;
            //gather permission grant result
            for (int i = 0; i < grantResults.length; i++) {
                //add only permission which denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionresult.put(permissions[i], grantResults[i]);
                    deniedcount++;
                }
            }
            //if all permission are granted
            if (deniedcount == 0) {
                // Proced    ahead in the app
                // isStoragePermissionGranted();
            }
            //atleast one or all permissions are denied
            else {
                for (Map.Entry<String, Integer> entry : permissionresult.entrySet()) {
                    String permName = entry.getKey();
                    int permresult = entry.getValue();
                    //permission dnied when first time "never ask again" is not  check
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        //show dialog
                        // yes and another for dismiss
                        utilss.checkAndRequestPermissions(MainActivity.this);
                    }
                    //permission is dnied (and never ask again is check)
                    else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
    @Override
    public void onInAppUpdateError(int code, Throwable error) {
        Log.d("updatestatus", "code: " + code, error);
    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        if (status.isUpdateAvailable()) {
            Log.d("updatestatus", "dddddcode: " + status.isUpdateAvailable());
            showdialog();
        } else if (status.isDownloaded()) {
            Log.d("updatestatus", "dddddcode: " + status.isDownloaded());
            inAppUpdateManager.snackBarMessage("Complete your HehSpace Host app Update").completeUpdate();
        }else if (status.isFailed()) {
            showdialog();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                inAppUpdateManager.checkForAppUpdate();
                Log.d("updatestatus", "Update flow failed! Result code: " + resultCode);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void showdialog() {
        ViewGroup viewGroup = this.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.update_dialog, viewGroup, false);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        LinearLayout Update_Now = alertDialog.findViewById(R.id.Update_Now);
        LinearLayout Close_App = alertDialog.findViewById(R.id.Close_App);
        Update_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inAppUpdateManager.checkForAppUpdate();
                alertDialog.dismiss();
            }
        });
        Close_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // System.exit(0);
                alertDialog.dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Apply the newly created layout parameters to the alert dialog window
        alertDialog.getWindow().setAttributes(layoutParams);

    }
}