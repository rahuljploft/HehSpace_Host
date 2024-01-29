package com.hehspace_host.components.addproperty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityConfirmLocationBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.GpsTracker;

import java.util.List;
import java.util.Locale;

public class ConfirmLocationActivity extends BaseBindingActivity implements OnMapReadyCallback {

    ActivityConfirmLocationBinding activityConfirmLocationBinding;
    private GoogleMap mMap;
    private Marker marker;
    String latittude = "";
    String longitude = "";
    String city = "";
    String state =  "";
    String country = "";
    String postalCode =  "";
    String knownName =  "";

    @Override
    protected void setBinding() {
        activityConfirmLocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_location);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(ConfirmLocationActivity.this);
        activityConfirmLocationBinding.etFlat.setText(getIntent().getStringExtra("flat"));
        activityConfirmLocationBinding.etCity.setText(getIntent().getStringExtra("city"));
        activityConfirmLocationBinding.etStreet.setText(getIntent().getStringExtra("street"));
        activityConfirmLocationBinding.etState.setText(getIntent().getStringExtra("state"));
        activityConfirmLocationBinding.etCountry.setText(getIntent().getStringExtra("country"));
        activityConfirmLocationBinding.etPostcode.setText(getIntent().getStringExtra("postalCode"));
//        activityConfirmLocationBinding.etStreet.setText(Constant.StreetName);
        if(Constant.ISEDIT.equals("yes")){
            activityConfirmLocationBinding.layoutGoToMap.setVisibility(View.VISIBLE);
        }
        else activityConfirmLocationBinding.layoutGoToMap.setVisibility(View.GONE);

        activityConfirmLocationBinding.layoutGoToMap.setOnClickListener(v -> {
            startActivity(new Intent(this,SelectLocationActivity.class));
        });
    }

    @Override
    protected void setListeners() {
        activityConfirmLocationBinding.btnNext.setOnClickListener(this);
        activityConfirmLocationBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.btnNext){
            Constant.BuildingName = activityConfirmLocationBinding.etFlat.getText().toString();
            Constant.StreetName = activityConfirmLocationBinding.etStreet.getText().toString();
            Constant.CityName = activityConfirmLocationBinding.etCity.getText().toString();
            Constant.StateName = "state";
            Constant.PostCode = activityConfirmLocationBinding.etPostcode.getText().toString();
            Constant.CountryName = activityConfirmLocationBinding.etCountry.getText().toString();
            Constant.PropertyAddress = Constant.BuildingName+" "+ Constant.StreetName+" "+Constant.CityName+" "+Constant.StateName+" "+Constant.CountryName+" "+Constant.PostCode;
            Constant.map.put("building_name",Constant.BuildingName);
            Constant.map.put("street_name",Constant.StreetName);
            Constant.map.put("city_name",Constant.CityName);
            Constant.map.put("state_name","state");
            Constant.map.put("post_code",Constant.PostCode);
            Constant.map.put("country_name",Constant.CountryName);
            startActivity(new Intent(this,GuestDetailsActivity.class));
        }
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    101
            );

        }
        else latLong();
    }

    private void latLong() {
        GpsTracker gps = new GpsTracker(this);
        if (gps.canGetLocation()) {
            latittude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("getLatitude()", "" + gps.getLatitude());
            Log.e("getLongitude()", "" + gps.getLongitude());
            getAddress(gps.getLatitude(), gps.getLongitude());

            LatLng coordinate = new LatLng(Double.parseDouble(latittude), Double.parseDouble(longitude));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    coordinate, 17));

        } else {
            gps.showSettingsAlert();
        }
    }


    public String getAddress(Double latitude,  Double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.e("qwert",addresses.get(0).toString());
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        } catch (Exception ignored) {

        }
        return "";
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Constant.ISEDIT.equals("yes")){
           // permission();
        }else{
            permission();
        }

//        if (marker == null) {
//            LatLng userLatLng = new LatLng(26.8505978, 75.7616719);
//            marker = mMap.addMarker(new MarkerOptions().position(userLatLng));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    userLatLng, 17));
//        }
//        LatLng coordinate = new LatLng(Double.parseDouble(latittude), Double.parseDouble(longitude));


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(ConfirmLocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        latLong();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}