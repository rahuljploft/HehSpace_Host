package com.hehspace_host.components.rating;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_host.R;
import com.hehspace_host.components.property.PropertDetailsActivity;
import com.hehspace_host.databinding.ActivityRatingReviewBinding;
import com.hehspace_host.databinding.ItemRatingBinding;
import com.hehspace_host.databinding.LayoutRatingBinding;
import com.hehspace_host.model.PropertyDetailModel;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.ItemClickListner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RatingReviewActivity extends BaseBindingActivity {

    ActivityRatingReviewBinding activityRatingReviewBinding;

    @Override
    protected void setBinding() {
    activityRatingReviewBinding = DataBindingUtil.setContentView(this,R.layout.activity_rating_review);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        activityRatingReviewBinding.rvRatings.setHasFixedSize(true);
        activityRatingReviewBinding.rvRatings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if(PropertDetailsActivity.propertyReviewList.size()>0){
            RatingAdapters ratingAdapters = new RatingAdapters(this, (type, pos) -> {

            });
            activityRatingReviewBinding.rvRatings.setAdapter(ratingAdapters);
            activityRatingReviewBinding.cardNoReview.setVisibility(View.GONE);
            activityRatingReviewBinding.rvRatings.setVisibility(View.VISIBLE);
        }
        else {
            activityRatingReviewBinding.rvRatings.setVisibility(View.GONE);
            activityRatingReviewBinding.cardNoReview.setVisibility(View.VISIBLE);
        }
        activityRatingReviewBinding.tvTotalRating.setText(PropertDetailsActivity.rate);
        activityRatingReviewBinding.tvTotalReviews.setText(PropertDetailsActivity.review);
        activityRatingReviewBinding.tvFiveStar.setText(PropertDetailsActivity.fivestart);
    }

    @Override
    protected void setListeners() {
    activityRatingReviewBinding.ivFilter.setOnClickListener(this);
    activityRatingReviewBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view.getId() == R.id.ivFilter){
            PopupMenu popup = new PopupMenu(this,  activityRatingReviewBinding.ivFilter);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.tvRecents){
                    Collections.sort(PropertDetailsActivity.propertyReviewList, (Comparator) (o1, o2) -> {
                        PropertyDetailModel.PropertyReviewListEntity p1 = (PropertyDetailModel.PropertyReviewListEntity) o1;
                        PropertyDetailModel.PropertyReviewListEntity p2 = (PropertyDetailModel.PropertyReviewListEntity) o2;
                        return p2.createdAt.compareToIgnoreCase(p1.createdAt);
                    });
                }
                if(item.getItemId() == R.id.tvLTH){
                    Collections.sort(PropertDetailsActivity.propertyReviewList, (m1, m2) -> {
                        String p1 = m1.rattingStar;
                        String p2 = m2.rattingStar;
                        if(p1 == null) return 1;
                        if(p2 == null) return -1;
                        if(p1.equals(p2)) return 0;
                        if(p1.equals("LOW") && (p2.equals("MEDIUM") || p2.equals("HIGH")))
                            return -1;
                        if(p1.equals("MEDIUM") && p2.equals("HIGH"))
                            return -1;
                        return 1;
                    });
                }
                if(item.getItemId() == R.id.tvHTL){
                    Collections.sort(PropertDetailsActivity.propertyReviewList, (m1, m2) -> {
                        String p1 = m1.rattingStar;
                        String p2 = m2.rattingStar;
                        if(p1 == null) return 1;
                        if(p2 == null) return -1;
                        if(p1.equals(p2)) return 0;
                        if(p1.equals("HIGH"))
                            return -1;
                        return 1;
                    });
                }
                return true;
            });

            popup.show(); //showing popup menu
        }
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    public int getMax(ArrayList<Integer> list){
        int max = Integer.MIN_VALUE;
        for(int i=0; i<list.size(); i++){
            if(list.get(i) > max){
                max = list.get(i);
            }
        }
        return max;
    }
    public class compareProduct implements Comparator<PropertyDetailModel.PropertyReviewListEntity> {
        public int compare(PropertyDetailModel.PropertyReviewListEntity a, PropertyDetailModel.PropertyReviewListEntity b) {
            if (Integer.parseInt(a.rattingStar) > Integer.parseInt(b.rattingStar))
                return -1; // highest value first
            if (Integer.parseInt(a.rattingStar) < Integer.parseInt(b.rattingStar))
                return 0;
            return 1;
        }
    }


    public static class RatingAdapters extends RecyclerView.Adapter<RatingAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public RatingAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public RatingAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemRatingBinding layoutAddonViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_rating, parent,
                    false);
            return new RatingAdapters.ViewHolder(layoutAddonViewBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RatingAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvName.setText(PropertDetailsActivity.propertyReviewList.get(position).fullName);
            holder.itemRowBinding.tvComment.setText(PropertDetailsActivity.propertyReviewList.get(position).rattingComment);
            holder.itemRowBinding.tvTime.setText(PropertDetailsActivity.propertyReviewList.get(position).createdAt);
            holder.itemRowBinding.rbrating.setRating(Float.parseFloat(PropertDetailsActivity.propertyReviewList.get(position).rattingStar));
            Glide.with(context)
                    .load(PropertDetailsActivity.propertyReviewList.get(position).userImage)
                    .error(R.drawable.user_dummy)
                    .into(holder.itemRowBinding.ivImage);
        }

        @Override
        public int getItemCount() {
            return PropertDetailsActivity.propertyReviewList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemRatingBinding itemRowBinding;
            public ViewHolder(ItemRatingBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }


}