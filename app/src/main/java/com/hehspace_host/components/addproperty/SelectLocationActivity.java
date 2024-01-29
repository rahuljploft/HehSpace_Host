package com.hehspace_host.components.addproperty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivitySelectLocationBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.GpsTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends BaseBindingActivity implements OnMapReadyCallback {
    ActivitySelectLocationBinding activitySelectLocationBinding;
    private GoogleMap mMap;
    private Marker marker;
    String latittude = "";
    String longitude = "";
    String city = "";
    String state =  "";
    String country = "";
    String postalCode =  "";
    String knownName =  "";
    String street =  "";
    private final static int PLACE_PICKER_REQUEST = 999;
    PlacesClient placesClient;
     List<Place.Field> fields= new ArrayList<>();

    @Override
    protected void setBinding() {
        activitySelectLocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_location);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(SelectLocationActivity.this);
        // Fetching API_KEY which we wrapped

        String apiKey = getString(R.string.google_maps_key);
        fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        );

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.

        activitySelectLocationBinding.etLocation.setOnClickListener(v -> {
            Intent autocompleteIntent =
                    new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fields)
                    .build(mActivity);
            startActivityForResult(autocompleteIntent, PLACE_PICKER_REQUEST);
        });

//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                // TODO: Get info about the selected place.
//                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                // TODO: Handle the error.
//                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PLACE_PICKER_REQUEST  && resultCode  == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                String add = place.getAddress();
                activitySelectLocationBinding.etLocation.setText(add);
                assert latLng != null;
                Constant.Latitude = latLng.latitude+"";
                Constant.Longitude = latLng.longitude+"";
                Log.e("latLng",latLng.latitude+""+latLng.longitude+"");
                getAddress(latLng.latitude, latLng.longitude);


            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void setListeners() {
        activitySelectLocationBinding.btnNext.setOnClickListener(this);
        activitySelectLocationBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view.getId() == R.id.btnNext){
            if(Constant.PropertyAddress.equals("")){
                Toast.makeText(this, "Please Select Property Address", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                Constant.LocationTag = "1";
                Constant.map.put("property_address",Constant.PropertyAddress);
                Constant.map.put("latitude",Constant.Latitude);
                Constant.map.put("longitude",Constant.Longitude);
                if(Constant.ISEDIT.equals("yes")){

                        startActivity(new Intent(this,ConfirmLocationActivity.class)
                                .putExtra("flat",Constant.BuildingName)
                                .putExtra("city",Constant.CityName)
                                .putExtra("state",Constant.StateName)
                                .putExtra("street",Constant.StreetName)
                                .putExtra("country",Constant.CountryName)
                                .putExtra("postalCode",Constant.PostCode)
                        );

                }
                else {
                    if(Constant.LocationTag.equals("1")){
                        startActivity(new Intent(this,ConfirmLocationActivity.class)
                                .putExtra("flat",Constant.BuildingName)
                                .putExtra("city",Constant.CityName)
                                .putExtra("state",Constant.StateName)
                                .putExtra("street",Constant.StreetName)
                                .putExtra("country",Constant.CountryName)
                                .putExtra("postalCode",Constant.PostCode)
                        );
                    }
                    else {
                        startActivity(new Intent(this,ConfirmLocationActivity.class)
                                .putExtra("flat",knownName)
                                .putExtra("city",city)
                                .putExtra("state",state)
                                .putExtra("street",street)
                                .putExtra("country",country)
                                .putExtra("postalCode",postalCode)
                        );

                    }

                }
            }

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
//        mSocket!!.connect()
//        connectSocket()
//        mSocket!!.on("joined", joined)
//        //  mSocket.on("currentlocationupdate", currentlocationupdate);
//        mSocket!!.on("sendproviderlocation", sendproviderlocation)
        GpsTracker gps = new GpsTracker(this);
        if (gps.canGetLocation()) {
            latittude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("getLatitude()", "" + gps.getLatitude());
            Log.e("getLongitude()", "" + gps.getLongitude());
            Constant.Latitude = latittude;
            Constant.Longitude = longitude;
            if(Constant.LocationTag.equals("1")){
                activitySelectLocationBinding.etLocation.setText(Constant.PropertyAddress);
            }
            else getAddress(gps.getLatitude(), gps.getLongitude());

            LatLng coordinate = new LatLng(Double.parseDouble(latittude), Double.parseDouble(longitude));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    coordinate, 17));

            //  sendLocation(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()))
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
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                street = addresses.get(0).getThoroughfare();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                activitySelectLocationBinding.etLocation.setText(address);
                Constant.PropertyAddress = address;
                Constant.BuildingName = knownName;
                Constant.CityName = city;
                Constant.StateName = state;
                Constant.StreetName = street;
                Constant.CountryName = country;
                Constant.PostCode = postalCode;

            } catch (Exception ignored) {
                    Log.e("exce",ignored.toString());
            }
            return "";
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        permission();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));
                Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));
                getAddress(Double.parseDouble(String.valueOf(cameraPosition.target.latitude)), Double.parseDouble(String.valueOf(cameraPosition.target.longitude)));

                }
        });
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
                    if (ContextCompat.checkSelfPermission(SelectLocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        latLong();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}