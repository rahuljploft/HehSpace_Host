package com.hehspace_host.components.addproperty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityAddOnServiceBinding;
import com.hehspace_host.databinding.ItemAddonBinding;
import com.hehspace_host.model.AdditionalSeviceModel;
import com.hehspace_host.model.PropertyDetailModel;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.custom_snackbar.CookieBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddOnServiceActivity extends BaseBindingActivity {

    ActivityAddOnServiceBinding activityAddOnServiceBinding;
    AddOnAdapter addOnAdapter;
    List<AdditionalSeviceModel> additionalList;
    List<AdditionalSeviceModel> mainadditionalList;
    List<AdditionalSeviceModel> finaladditionalList;

    @Override
    protected void setBinding() {
        activityAddOnServiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_on_service);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        additionalList = new ArrayList<>();
        finaladditionalList = new ArrayList<>();
        mainadditionalList = new ArrayList<>();
//        activityAddOnServiceBinding.rvAddon.setHasFixedSize(true);
        activityAddOnServiceBinding.rvAddon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
       /* addOnAdapter = new AddOnAdapter(this, new ItemClickListner() {
            @Override
            public void onClick(String type, int pos) {
                if (type.equals("remove")) {
                    additionalList.remove(pos);
                    addOnAdapter.notifyDataSetChanged();
                }
            }
        });*/
        addOnAdapter = new AddOnAdapter(this);
        activityAddOnServiceBinding.rvAddon.setAdapter(addOnAdapter);

        if (Constant.ISEDIT.equals("yes")) {
            activityAddOnServiceBinding.etCleaningFees.setText(Constant.CleanerFee);
            for (int i = 0; i < Constant.servicessList.size(); i++) {
                AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                additionalSeviceModel.setStringName(Constant.servicessList.get(i).servicesTitle);
                if(Constant.servicessList.get(i).servicesRate.contains("$")){
                    String[] parts = Constant.servicessList.get(i).servicesRate.split("\\$");
                    additionalSeviceModel.setStringPrice(parts[1]);
                }
                else {
                    additionalSeviceModel.setStringPrice( Constant.servicessList.get(i).servicesRate);
                }

                additionalList.add(additionalList.size(), additionalSeviceModel);
                mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);
                addOnAdapter.notifyDataSetChanged();
            }
        }
        else {
             activityAddOnServiceBinding.etCleaningFees.setText(Constant.CleanerFee);
            for (int i = 0; i < Constant.servicessList.size(); i++) {
                AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                additionalSeviceModel.setStringName(Constant.servicessList.get(i).servicesTitle);
                additionalSeviceModel.setStringPrice(Constant.servicessList.get(i).servicesRate);
                additionalList.add(additionalList.size(), additionalSeviceModel);
                mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);
                addOnAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void setListeners() {
        activityAddOnServiceBinding.layoutAdd.setOnClickListener(this);
        activityAddOnServiceBinding.btnNext.setOnClickListener(this);
        activityAddOnServiceBinding.ivBack.setOnClickListener(this);
    }
    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view.getId() == R.id.layoutAdd) {
            try {
                if (Constant.ISEDIT.equals("yes")) {
                List<AdditionalSeviceModel> mainadditionalListnew = new ArrayList<>();
                mainadditionalListnew.addAll(mainadditionalList);
                mainadditionalList.clear();
                additionalList.clear();
                for (int i = 0; i < mainadditionalListnew.size(); i++) {
                    View row = activityAddOnServiceBinding.rvAddon.getLayoutManager().findViewByPosition(i);
                    EditText textView = row.findViewById(R.id.etServiceName);
                    EditText textView1 = row.findViewById(R.id.etServicePrice);
                    Log.d("978546132", textView.getText().toString() + "___");
                    Log.d("978546132", textView1.getText().toString() + "___");
                    AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                    additionalSeviceModel.setStringName(textView.getText().toString());
                    additionalSeviceModel.setStringPrice(textView1.getText().toString());
                    additionalList.add(additionalList.size(), additionalSeviceModel);
                    mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);
                }
                AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                additionalSeviceModel.setStringName("");
                additionalSeviceModel.setStringPrice("");
                additionalList.add(additionalList.size(), additionalSeviceModel);
                mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);
            }
            else {
                List<AdditionalSeviceModel> mainadditionalListnew = new ArrayList<>(mainadditionalList);
                mainadditionalList.clear();
                additionalList.clear();
                View row=null;

                for (int i = 0; i < mainadditionalListnew.size(); i++) {
                   // View row = activityAddOnServiceBinding.rvAddon.getLayoutManager().findViewByPosition(i);
                    // row = activityAddOnServiceBinding.rvAddon.findViewHolderForAdapterPosition(i).itemView;

                    row=activityAddOnServiceBinding.rvAddon.getLayoutManager().getChildAt(i);
                    Log.d("aswfASfasfa",row+""+mainadditionalListnew.size()+"");
                    EditText  textView = row.findViewById(R.id.etServiceName);

                    EditText  textView1 = row.findViewById(R.id.etServicePrice);
                    AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                    if (textView != null) {
                        additionalSeviceModel.setStringName(textView.getText().toString());
                    }
                    if (textView1 != null) {
                        additionalSeviceModel.setStringPrice(textView1.getText().toString());
                    }
                    additionalList.add(additionalList.size(), additionalSeviceModel);
                    mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);
                }
                AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                additionalSeviceModel.setStringName("");
                additionalSeviceModel.setStringPrice("");
                additionalList.add(additionalList.size(), additionalSeviceModel);
                mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);
            }
            addOnAdapter.notifyDataSetChanged();
            }
            catch (Exception e){

            }
        }
        JSONArray jsonArray;
        if (view.getId() == R.id.btnNext) {
            jsonArray = new JSONArray();
            if (mainadditionalList.size() > 0) {
                for (int i = 0; i < mainadditionalList.size(); i++) {
                    View row = activityAddOnServiceBinding.rvAddon.getLayoutManager().findViewByPosition(i);
                    EditText textView = row.findViewById(R.id.etServiceName);
                    EditText textView1 = row.findViewById(R.id.etServicePrice);
                    Log.d("978546132", textView.getText().toString() + "___");
                    Log.d("978546132", textView1.getText().toString() + "___");
                   /* AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                    additionalSeviceModel.setStringName(textView.getText().toString());
                    additionalSeviceModel.setStringPrice(textView1.getText().toString());
                    mainadditionalList.add(additionalSeviceModel);*/

                    JSONObject myJsonObject = new JSONObject();
                    try {
                        PropertyDetailModel.PropertyServicesEntity propertyServicesEntity = new PropertyDetailModel.PropertyServicesEntity();
                        if(TextUtils.isEmpty(textView.getText().toString())){
                            Toast.makeText(mActivity, "Please Add Service Title", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(textView1.getText().toString())){
                            Toast.makeText(mActivity, "Please Add Service Rate", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myJsonObject.put("services_title", textView.getText().toString());
                        myJsonObject.put("services_rate", textView1.getText().toString());
                        jsonArray.put(myJsonObject);
                        propertyServicesEntity.servicesTitle=textView.getText().toString();
                        propertyServicesEntity.servicesRate=textView1.getText().toString();
                        Constant.servicessList.add(propertyServicesEntity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
            Log.d("qwerty", jsonArray.toString());
            Constant.additionalServices = new JSONArray();
            Constant.additionalServices = jsonArray;
            if (TextUtils.isEmpty(activityAddOnServiceBinding.etCleaningFees.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Cleaning Fees Cannot be Empty")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            Constant.CleanerFee = activityAddOnServiceBinding.etCleaningFees.getText().toString();
            Constant.map.put("cleaner_fee", activityAddOnServiceBinding.etCleaningFees.getText().toString());
            Log.e("map", Constant.map.toString());
            Log.e("imagedata", Constant.imageData.toString());
            startActivity(new Intent(this, PropertySummaryActivity.class));
        }
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    public class AddOnAdapter extends RecyclerView.Adapter<AddOnAdapter.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;
        // ItemClickListner itemClickListner
        public AddOnAdapter(Context context) {
            this.context = context;
            // this.itemClickListner = itemClickListner;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemAddonBinding itemAddonBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_addon, parent,
                    false);
            return new ViewHolder(itemAddonBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            // Log.d("qwertyhgvfesdcv", mainadditionalList.get(position).getStringName() + "____");

                if (!additionalList.get(position).getStringName().isEmpty()) {
                    holder.itemRowBinding.etServiceName.setText(additionalList.get(position).getStringName());
                } else {
                    holder.itemRowBinding.etServiceName.setText("");
                }

                if (!additionalList.get(position).getStringPrice().isEmpty()) {
                    holder.itemRowBinding.etServicePrice.setText(additionalList.get(position).getStringPrice());
                } else {
                    holder.itemRowBinding.etServicePrice.setText("");
                }

                holder.itemRowBinding.iconRemove.setOnClickListener(v -> {
                List<AdditionalSeviceModel> mainadditionalListnew = new ArrayList<>();
                mainadditionalListnew.addAll(mainadditionalList);
                mainadditionalList.clear();
                additionalList.clear();
                for (int i = 0; i < mainadditionalListnew.size(); i++) {
                    View row = activityAddOnServiceBinding.rvAddon.getLayoutManager().findViewByPosition(i);
                    EditText textView = row.findViewById(R.id.etServiceName);
                    EditText textView1 = row.findViewById(R.id.etServicePrice);
                    Log.d("978546132", textView.getText().toString() + "___");
                    Log.d("978546132", textView1.getText().toString() + "___");
                    AdditionalSeviceModel additionalSeviceModel = new AdditionalSeviceModel();
                    additionalSeviceModel.setStringName(textView.getText().toString());
                    additionalSeviceModel.setStringPrice(textView1.getText().toString());
                    additionalList.add(additionalList.size(), additionalSeviceModel);
                    mainadditionalList.add(mainadditionalList.size(), additionalSeviceModel);
                }
                mainadditionalList.remove(position);
                additionalList.clear();
                additionalList.addAll(mainadditionalList);
                //mainadditionalList.clear();
                /// additionalList.clear();
                Log.d("listdata",additionalList.size()+"");
                for (int i=0;i<mainadditionalList.size();i++){
                    Log.d("listdata",mainadditionalList.get(i).getStringName()+"-------------"+mainadditionalList.get(i).getStringPrice());
                }
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return additionalList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemAddonBinding itemRowBinding;

            public ViewHolder(ItemAddonBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }

    }

}