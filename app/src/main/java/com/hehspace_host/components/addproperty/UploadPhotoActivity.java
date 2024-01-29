package com.hehspace_host.components.addproperty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hehspace_host.BuildConfig;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.login.LoginActivity;
import com.hehspace_host.components.splash.SplashActivity;
import com.hehspace_host.databinding.ActivityUploadPhotoBinding;
import com.hehspace_host.databinding.LayoutImagesBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ItemClickListner;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.hehspace_host.util.custom_snackbar.CookieBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadPhotoActivity extends BaseBindingActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate {

    ActivityUploadPhotoBinding activityUploadPhotoBinding;
    List<Uri> uriList;
    ImageAdapters imageAdapters;
    LiveImageAdapters liveImageAdapters;
    @Override
    protected void setBinding() {
        activityUploadPhotoBinding = DataBindingUtil.setContentView(this, R.layout.activity_upload_photo);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        uriList = new ArrayList<>();
        activityUploadPhotoBinding.rvImages.setHasFixedSize(true);
        activityUploadPhotoBinding.rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        imageAdapters = new ImageAdapters(this, (type, pos) -> {
            if(type.equals("remove")){
                uriList.remove(pos);
                imageAdapters.notifyDataSetChanged();
            }

        });
        activityUploadPhotoBinding.rvImages.setAdapter(imageAdapters);

        activityUploadPhotoBinding.layoutAdd.setOnClickListener(v -> {
            BSImagePicker multiSelectionPicker = new BSImagePicker.Builder("com.hehspace_host.provider")
                    .setMinimumMultiSelectCount(1) //Default: 1.
                    .setMaximumMultiSelectCount(30)//Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                    .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                    .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                    .setMultiSelectDoneTextColor(R.color.colorPrimary) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                    .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                    .disableOverSelectionMessage() //You can also decide not to show this over select message.
                    .build();
            multiSelectionPicker.show(getSupportFragmentManager(), "picker");
        });


        if (Constant.ISEDIT.equals("yes")){
            if (Constant.imageList.size()>0){
               /* activityUploadPhotoBinding.rvLiveImages.setVisibility(View.VISIBLE);
                activityUploadPhotoBinding.rvLiveImages.setLayoutManager(new GridLayoutManager(this, 3));
                liveImageAdapters =  new LiveImageAdapters(this, (type, pos) -> {
                    if(type.equals("remove")){
                        uriList.remove(pos);
                        liveImageAdapters.notifyDataSetChanged();
                    }

                });
                activityUploadPhotoBinding.rvLiveImages.setAdapter(liveImageAdapters);*/

                for (int i =0; i<Constant.imageList.size(); i++){
                 //   uriList.add(Uri.parse( Constant.imageList.get(i).propertyImageUrl));
                   String url = Constant.imageList.get(i).propertyImageUrl;
                    ProgressDialog.showProgressDialog(this);
                    getImageBitmap(url);

                }


            }else{
                activityUploadPhotoBinding.rvLiveImages.setVisibility(View.GONE);
            }
        }else{
            if (Constant.imageData.size()>0){
               /* activityUploadPhotoBinding.rvLiveImages.setVisibility(View.VISIBLE);
                activityUploadPhotoBinding.rvLiveImages.setLayoutManager(new GridLayoutManager(this, 3));
                liveImageAdapters =  new LiveImageAdapters(this, (type, pos) -> {
                    if(type.equals("remove")){
                        uriList.remove(pos);
                        liveImageAdapters.notifyDataSetChanged();
                    }

                });
                activityUploadPhotoBinding.rvLiveImages.setAdapter(liveImageAdapters);*/

                for (int i =0; i<Constant.imageData.size(); i++){
                       uriList.add(Constant.imageData.get(i));
                    imageAdapters.notifyDataSetChanged();
                }


            }else{
                activityUploadPhotoBinding.rvLiveImages.setVisibility(View.GONE);
            }
//            activityUploadPhotoBinding.rvLiveImages.setVisibility(View.GONE);
        }

    }
    String path = "";
    private void getImageBitmap(String url) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Random rand = new Random();
                int randNo = rand.nextInt(1000);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title"+randNo, null);
                imageAdapters.notifyDataSetChanged();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                ProgressDialog.hideProgressDialog();
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
        new LoadImage().execute(url);
    }

    @Override
    protected void setListeners() {
        activityUploadPhotoBinding.btnNext.setOnClickListener(this);
        activityUploadPhotoBinding.layoutHire.setOnClickListener(this);
        activityUploadPhotoBinding.ivBack.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.layoutHire){
            if(Constant.HirePhotographer.equals("Yes")){
                Toast.makeText(mActivity, "Already Requested!", Toast.LENGTH_SHORT).show();
                return;
            }
            else alertView( );
        }
        if(view.getId() == R.id.btnNext){
            if (Constant.ISEDIT.equals("yes")){
                if(uriList.size() >= 3){
                    Constant.imageData = uriList;
                    Constant.fileToUpload = new MultipartBody.Part[uriList.size()];
                    for (int i = 0; i < uriList.size(); i++) {
                        String filePath = Uitility.getRealPathFromURI(this, uriList.get(i).toString());
                        Log.e("filepath", filePath);
                        File file = new File(filePath);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                        Constant.fileToUpload[i] = MultipartBody.Part.createFormData("images_data" + "[" + i + "]", file.getName(), requestBody);
                    }
                    startActivity(new Intent(this,PropertyDetailsActivity.class));
                }
                else {
                    CookieBar.build(mActivity)
                            .setTitle(R.string.app_name)
                            .setTitleColor(R.color.black)
                            .setMessageColor(R.color.black)
                            .setMessage("Please Add at least 3 photos")
                            .setBackgroundColor(R.color.colorPrimary)
                            .setIconAnimation(R.animator.icon_anim)
                            .setIcon(R.drawable.logo_black)
                            .setDuration(5000) // 5 seconds
                            .show();
                }
            }else{
                if(uriList.size() >= 3){
                    Constant.imageData = uriList;
                    Constant.fileToUpload = new MultipartBody.Part[uriList.size()];
                    for (int i = 0; i < uriList.size(); i++) {
                        String filePath = Uitility.getRealPathFromURI(this, uriList.get(i).toString());
                        Log.e("filepath", filePath);
                        File file = new File(filePath);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                        Constant.fileToUpload[i] = MultipartBody.Part.createFormData("images_data" + "[" + i + "]", file.getName(), requestBody);
                    }
                    startActivity(new Intent(this,PropertyDetailsActivity.class));
                }
                else {
                    CookieBar.build(mActivity)
                            .setTitle(R.string.app_name)
                            .setTitleColor(R.color.black)
                            .setMessageColor(R.color.black)
                            .setMessage("Please Add at least 3 photos")
                            .setBackgroundColor(R.color.colorPrimary)
                            .setIconAnimation(R.animator.icon_anim)
                            .setIcon(R.drawable.logo_black)
                            .setDuration(5000) // 5 seconds
                            .show();
                }
            }

        }
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    private void alertView( ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle( "Hire Photographer" )
                .setMessage("Do you want to Hire a Professional Photographer?")
                .setNegativeButton("NO", (dialoginterface, i) -> {
                    dialoginterface.cancel();
                    Constant.HirePhotographer = "No";
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                        Constant.HirePhotographer = "Yes";
                        Toast.makeText(mActivity, "Requested Successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }



    public class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public ImageAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutImagesBinding layoutImagesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_images, parent,
                    false);
            return new ViewHolder(layoutImagesBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.ivUpload.setOnClickListener(v -> {
                itemClickListner.onClick("upload", position);
            });
            holder.itemRowBinding.ivClose.setOnClickListener(v -> {
                itemClickListner.onClick("remove", position);
            });
            holder.itemRowBinding.ivImage.setImageURI(uriList.get(position));
            holder.itemRowBinding.ivUpload.setVisibility(View.GONE);
            holder.itemRowBinding.ivImage.setVisibility(View.VISIBLE);
            holder.itemRowBinding.ivClose.setVisibility(View.VISIBLE);
//            if (uriList.size() > position) {
//
//            } else {
//                holder.itemRowBinding.ivImage.setVisibility(View.GONE);
//                holder.itemRowBinding.ivUpload.setVisibility(View.VISIBLE);
//                holder.itemRowBinding.cardClose.setVisibility(View.GONE);
//            }


        }

        @Override
        public int getItemCount() {
            return uriList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutImagesBinding itemRowBinding;

            public ViewHolder(LayoutImagesBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }

    public class LiveImageAdapters extends RecyclerView.Adapter<LiveImageAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public LiveImageAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutImagesBinding layoutImagesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_images, parent,
                    false);
            return new ViewHolder(layoutImagesBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.ivUpload.setOnClickListener(v -> {
                itemClickListner.onClick("upload", position);
            });
            holder.itemRowBinding.ivClose.setOnClickListener(v -> {
                itemClickListner.onClick("remove", position);
            });
            Glide.with(context)
                    .load(Constant.imageList.get(position).propertyImageUrl)
                    .error(R.drawable.logo_host)
                    .into(holder.itemRowBinding.ivImage);
            holder.itemRowBinding.ivUpload.setVisibility(View.GONE);
            holder.itemRowBinding.ivImage.setVisibility(View.VISIBLE);
            holder.itemRowBinding.ivClose.setVisibility(View.VISIBLE);
//            if (uriList.size() > position) {
//
//            } else {
//                holder.itemRowBinding.ivImage.setVisibility(View.GONE);
//                holder.itemRowBinding.ivUpload.setVisibility(View.VISIBLE);
//                holder.itemRowBinding.cardClose.setVisibility(View.GONE);
//            }


        }

        @Override
        public int getItemCount() {
            return Constant.imageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutImagesBinding itemRowBinding;

            public ViewHolder(LayoutImagesBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }
    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
//         uri = FileProvider.getUriForFile(this,
//                 BuildConfig.APPLICATION_ID + ".provider", new File(String.valueOf(uri)));

        uriList.add(uri);
        imageAdapters.notifyDataSetChanged();
    }

//    @Override
//    public void onMultiImageSelected(List<Uri> uri, String tag) {
////        MultipartBody.Part fileToUpload9 = null;
////        if (imagepath.isEmpty()) {
////            fileToUpload9 = MultipartBody.Part.createFormData("user_photo", "file.getName()");
////        } else {
////            Log.d("qwerty", imagepath + "");
////            File file = new File(imagepath);
////            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
////            fileToUpload9 = MultipartBody.Part.createFormData("user_photo", file.getName(), requestBody);
//
//
////        }
//
//
//    }
    private class LoadImage extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(strings[0]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                ProgressDialog.hideProgressDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            Random rand = new Random();
            int randNo = rand.nextInt(1000);
            path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title"+randNo, null);
            uriList.add(Uri.parse(path));
            imageAdapters.notifyDataSetChanged();
        }
    }
}