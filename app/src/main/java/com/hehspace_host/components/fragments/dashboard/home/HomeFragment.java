package com.hehspace_host.components.fragments.dashboard.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.Dash;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.calender.MyCalendarActivity;
import com.hehspace_host.components.addproperty.PropertySummaryActivity;
import com.hehspace_host.components.addproperty.SelectCategoryActivity;
import com.hehspace_host.components.chat.ChatActivity;
import com.hehspace_host.components.login.LoginActivity;
import com.hehspace_host.components.property.PropertDetailsActivity;
import com.hehspace_host.components.request.RequestActivity;
import com.hehspace_host.components.request.RequestDetailsActivity;
import com.hehspace_host.components.sidemenu.EarningActivity;
import com.hehspace_host.databinding.FragmentHomeBinding;
import com.hehspace_host.databinding.ItemAmenitiesBinding;
import com.hehspace_host.databinding.ItemPropertyBinding;
import com.hehspace_host.databinding.ItemRequestBinding;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.DashBoardModel;
import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.PropertyListModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseFragment;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends BaseFragment {
    public interface onSomeEventListener {
        public void someEvent(String name,String photo);
    }
    onSomeEventListener someEventListener;
    FragmentHomeBinding fragmentHomeBinding;
    HomeView_Model view_model;
    RequestAdapter requestAdapter;
   public static List<DashBoardModel.RequestListEntity> requestlist = new ArrayList<>();
   public static List<PropertyListModel.DataEntity> propertyList = new ArrayList<>();
   public static String propid = "";


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        fragmentHomeBinding.setLifecycleOwner(this);
        view_model= new HomeView_Model();
        return fragmentHomeBinding;
    }

    @Override
    protected void createActivityObject() {

        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, propertyListModelApiResponse -> handleResult(propertyListModelApiResponse));
        view_model.livedatadashBoard.observe(this, propertyListModelApiResponse -> handleResultDashBoard(propertyListModelApiResponse));
        view_model.Commonlivedata.observe(this, modelApiResponse -> handleResultChangeStatus(modelApiResponse));
        if (Uitility.isOnline(requireContext())) {
            view_model.getPropertyListing();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }

        fragmentHomeBinding.srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Uitility.isOnline(requireContext())) {
                    view_model.getPropertyListing();
                } else {
                    Uitility.nointernetDialog(requireActivity());
                }
            }
        });


    fragmentHomeBinding.rvPropertyList.setHasFixedSize(true);
    fragmentHomeBinding.rvPropertyList.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));

    fragmentHomeBinding.rvRequest.setHasFixedSize(true);
    fragmentHomeBinding.rvRequest.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
     requestAdapter = new RequestAdapter(requireContext(), (type, pos) -> {
         if(type.equals("view")){
             startActivity(new Intent(requireContext(), RequestDetailsActivity.class)
                     .putExtra("reqid",requestlist.get(pos).request_id)
                     .putExtra("status",requestlist.get(pos).request_status)
             );
         }
         if(type.equals("accept")){
             AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
             dialog.setTitle("Confirm")
                     .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                     .setPositiveButton("Yes", (dialoginterface, i) -> {
                         if (Uitility.isOnline(getContext())) {
                             HashMap<String,String> hashMap = new HashMap<>();
                             hashMap.put("request_status","209");
                             hashMap.put("_method","PUT");
                             view_model.acceptReject(hashMap,Integer.parseInt(requestlist.get(pos).request_id));
                         } else {
                             Uitility.nointernetDialog(requireActivity());
                         }
                     }).show();
         }
         if(type.equals("reject")){
             AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
             dialog.setTitle("Confirm")
                     .setNegativeButton("NO", (dialoginterface, i) -> dialoginterface.cancel())
                     .setPositiveButton("Yes", (dialoginterface, i) -> {
                         if (Uitility.isOnline(requireContext())) {
                             HashMap<String,String> hashMap = new HashMap<>();
                             hashMap.put("request_status","210");
                             hashMap.put("_method","PUT");
                             view_model.acceptReject(hashMap,Integer.parseInt(requestlist.get(pos).request_id));
                         } else {
                             Uitility.nointernetDialog(requireActivity());
                         }
                     }).show();
         }

    });

    fragmentHomeBinding.rvRequest.setAdapter(requestAdapter);

    fragmentHomeBinding.layoutEarning.setOnClickListener(v -> {
        startActivity(new Intent(requireContext(), EarningActivity.class));
    });
    fragmentHomeBinding.layoutRequest.setOnClickListener(v -> {
        startActivity(new Intent(requireContext(), RequestActivity.class));
    });
    fragmentHomeBinding.layoutBooking.setOnClickListener(v -> {
        startActivity(new Intent(requireContext(), MainActivity.class).putExtra("value","booking"));
    });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if (Uitility.isOnline(requireContext())) {
                view_model.getPropertyListing();
            } else {
                Uitility.nointernetDialog(requireActivity());
            }
        }

    }

    @Override
    protected void initializeOnCreateObject() {

    }

    @Override
    protected void setListeners() {
    fragmentHomeBinding.cardAddProperty.setOnClickListener(this);
    fragmentHomeBinding.tvViewAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.cardAddProperty){
            startActivity(new Intent(requireContext(), SelectCategoryActivity.class));
        }
        if(view.getId() == R.id.tvViewAll){
            startActivity(new Intent(requireContext(), RequestActivity.class));
        }
    }

    private void handleResult(ApiResponse<PropertyListModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                fragmentHomeBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if (Uitility.isOnline(requireContext())) {
                        view_model.dashBoard();
                    } else {
                        Uitility.nointernetDialog(requireActivity());
                    }
                    if(result.getData().status.equals("true")){

                        propertyList =result.getData().data;

                        if(result.getData().data.size() > 0){

                            PropertyListAdapter propertyListAdapter = new PropertyListAdapter(requireContext(), result.getData().data,
                                    (type, pos) -> {
                                propid = result.getData().data.get(pos).propertyId;
                                if(type.equals("check")){
                                    CalenderBottomSheet();
                                }
                            });
                            fragmentHomeBinding.rvPropertyList.setAdapter(propertyListAdapter);
                            fragmentHomeBinding.cardAddProperty.setVisibility(View.GONE);
                            fragmentHomeBinding.rvPropertyList.setVisibility(View.VISIBLE);
                        }
                        else {

                            fragmentHomeBinding.rvPropertyList.setVisibility(View.GONE);
                        }

                    }
                    else{
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                        fragmentHomeBinding.cardAddProperty.setVisibility(View.VISIBLE);
                        fragmentHomeBinding.rvPropertyList.setVisibility(View.GONE);
                    }

                }
                break;
        }
    }

    public void CalenderBottomSheet() {
        MyCalendarActivity addPhotoBottomDialogFragment =
                MyCalendarActivity.newInstance();
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                MyCalendarActivity.TAG);
    }

    private void handleResultDashBoard(ApiResponse<DashBoardModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                         sessionManager.setSStatus(result.getData().data.stripeStatus);
                         sessionManager.setStripeURL(result.getData().data.stripe_url);
                        someEventListener.someEvent(result.getData().data.stripeStatus,result.getData().data.stripe_url);
                        requestlist = result.getData().data.requestList;
                        if(requestlist.size()>0){
                            fragmentHomeBinding.cardNoBooking.setVisibility(View.GONE);
                            fragmentHomeBinding.rvRequest.setVisibility(View.VISIBLE);
                            requestAdapter.notifyDataSetChanged();
                        }
                        else {
                            fragmentHomeBinding.rvRequest.setVisibility(View.GONE);
                            fragmentHomeBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        }

                        fragmentHomeBinding.tvEarning.setText(result.getData().data.totalEarning);
                        fragmentHomeBinding.tvBooking.setText(result.getData().data.totalBooking);
                        fragmentHomeBinding.tvRequest.setText(result.getData().data.requestCount);
                        if(result.getData().data.notificationCount.equals("0")){
                            MainActivity.tvNotCount.setVisibility(View.GONE);
                        }
                        else {
                            MainActivity.tvNotCount.setVisibility(View.VISIBLE);
                            MainActivity.tvNotCount.setText(result.getData().data.notificationCount);
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    private void handleResultChangeStatus(ApiResponse<CommonModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        if (Uitility.isOnline(requireContext())) {
                            view_model.dashBoard();
                        } else {
                            Uitility.nointernetDialog(requireActivity());
                        }
                        Toast.makeText(getContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }



    public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder> {

        Context context;
        List<PropertyListModel.DataEntity> list;
        ItemClickListner itemClickListner;

        public PropertyListAdapter(Context context, List<PropertyListModel.DataEntity> list,ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
            this.list = list;
        }

        @Override
        public PropertyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemPropertyBinding itemPropertyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_property, parent,
                    false);
            return new PropertyListAdapter.ViewHolder(itemPropertyBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull PropertyListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvPropertTitle.setText(list.get(position).propertyTitle);
            holder.itemRowBinding.tvPropertyLocation.setText(list.get(position).propertyLocation);
            if(list.get(position).propertyStatus.equals("UNPUBLISHED")){
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
            }
            else  holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
            holder.itemRowBinding.tvStatus.setText(list.get(position).propertyStatus);
            holder.itemRowBinding.tvRatings.setText(list.get(position).propertyRatting+" ( "+list.get(position).propertyReview+" Reviews )");
            Glide.with(context).load(list.get(position).propertyImageUrl).into( holder.itemRowBinding.ivPropertyImage);
            holder.itemRowBinding.layoutManage.setOnClickListener(v -> {
                context.startActivity(new Intent(context, PropertDetailsActivity.class).putExtra("property_id",list.get(position).propertyId));
            });
            holder.itemRowBinding.layoutCheckAvailability.setOnClickListener(v -> {
               itemClickListner.onClick("check",position);
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemPropertyBinding itemRowBinding;

            public ViewHolder(ItemPropertyBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }


    }



    public static class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public RequestAdapter(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemRequestBinding itemRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_request, parent,
                    false);
            return new RequestAdapter.ViewHolder(itemRequestBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvTitle.setText(requestlist.get(position).property_title);
            holder.itemRowBinding.tvrequestId.setText("Request No: "+requestlist.get(position).request_number);
            holder.itemRowBinding.tvCustomername.setText(requestlist.get(position).full_name);
            holder.itemRowBinding.tvrequestedDate.setText(requestlist.get(position).requested_date);
            holder.itemRowBinding.tvRequestedTime.setText(requestlist.get(position).requested_time);
            holder.itemRowBinding.cardRequest.setOnClickListener(v -> itemClickListner.onClick("view",position));
            holder.itemRowBinding.tvAccept.setOnClickListener(v -> itemClickListner.onClick("accept",position));
            holder.itemRowBinding.tvReject.setOnClickListener(v -> itemClickListner.onClick("reject",position));
            Glide.with(context)
                    .load((requestlist.get(position).property_image_url))
                    .error(R.drawable.logo_host)
                    .into(holder.itemRowBinding.ivImage);

            if(requestlist.get(position).request_status.equals("REJECTED")){
                holder.itemRowBinding.clickRel.setVisibility(View.GONE);
                holder.itemRowBinding.view.setVisibility(View.GONE);
                holder.itemRowBinding.tvStatus.setText(requestlist.get(position).request_status);
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
                holder.itemRowBinding.tvStatus.setVisibility(View.VISIBLE );
            }
            else if(requestlist.get(position).request_status.equals("ACCEPTED")){
                holder.itemRowBinding.clickRel.setVisibility(View.GONE);
                holder.itemRowBinding.view.setVisibility(View.GONE);
                holder.itemRowBinding.tvStatus.setText(requestlist.get(position).request_status);
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
                holder.itemRowBinding.tvStatus.setVisibility(View.VISIBLE );
            }
            else {
                holder.itemRowBinding.clickRel.setVisibility(View.VISIBLE);
                holder.itemRowBinding.view.setVisibility(View.VISIBLE);
                holder.itemRowBinding.tvStatus.setVisibility(View.GONE);
            }
            holder.itemRowBinding.layoutChat.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ChatActivity.class)
                        .putExtra("userid",requestlist.get(position).user_id)
                        .putExtra("bookingid","")
                        .putExtra("requestid",requestlist.get(position).request_id)
                        .putExtra("propertyid",requestlist.get(position).property_id)
                        .putExtra("name",requestlist.get(position).property_title)
                        .putExtra("property",requestlist.get(position).full_name)
                        .putExtra("image",requestlist.get(position).user_image_url)
                );
            });

        }

        @Override
        public int getItemCount() {
            return requestlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemRequestBinding itemRowBinding;
            public ViewHolder(ItemRequestBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


}