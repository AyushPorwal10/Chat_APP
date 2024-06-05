package com.example.let.MyModels;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserModel implements Parcelable   {
    private String useName ;
    private String phoneNumber ;
    private Timestamp timestamp ;
    private String userId ;
    private String fcmToken;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public UserModel() {
    }

    public UserModel(String useName, String phoneNumber, Timestamp timestamp,String userId) {
        this.useName = useName;
        this.phoneNumber = phoneNumber;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    protected UserModel(Parcel in){
        useName = in.readString();
        phoneNumber = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        userId = in.readString();
    }
    public static final Creator<UserModel> CREATOR  = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(useName);
        dest.writeString(phoneNumber);
        dest.writeParcelable(timestamp, flags);
        dest.writeString(userId);
    }
}
