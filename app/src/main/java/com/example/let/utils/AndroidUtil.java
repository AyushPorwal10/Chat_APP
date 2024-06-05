package com.example.let.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.let.MyModels.UserModel;

public class AndroidUtil {
    public static void showToast(Context context , String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void passUserModelIntent(Intent intent , UserModel model){
        intent.putExtra("username",model.getUseName());
        intent.putExtra("phone",model.getPhoneNumber());
        intent.putExtra("userid",model.getUserId());
    }
    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel model = new UserModel();
        model.setUseName(intent.getStringExtra("username"));
        model.setUserId(intent.getStringExtra("userId"));
        model.setPhoneNumber(intent.getStringExtra("phone"));
        return model;
    }
    public static void setProfileImage(Uri uri, Context context, ImageView imageView){
        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

}
