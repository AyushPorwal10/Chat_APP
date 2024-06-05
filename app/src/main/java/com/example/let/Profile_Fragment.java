package com.example.let;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsService;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.let.MyModels.UserModel;
import com.example.let.databinding.FragmentProfileBinding;
import com.example.let.utils.AndroidUtil;
import com.example.let.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;


public class Profile_Fragment extends Fragment {
    FragmentProfileBinding binding;
    UserModel userModel;
    Uri uri ;

    public Profile_Fragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater);
        getUserData();



        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = String.valueOf(binding.updateUserName.getText());
                if(userName.length() < 3){
                    binding.updateUserName.setError("Username should greater than 3 character");
                    return;
                }
                setProgressBar(true);
                if(uri != null){
                    FirebaseUtils.getStorageReference().putFile(uri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()){
                                        updateUserDetails(userName);
                                        AndroidUtil.showToast(getContext(),"Profile updated");
                                    }
                                    else {
                                        AndroidUtil.showToast(getContext(),"Something went wrong");
                                        updateUserDetails(userName);
                                    }
                                    setProgressBar(false);
                                }
                            });
                }
            }
        });
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().deleteToken()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(getContext(),LoginPhoneNumber.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });


            }
        });
        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);
            }
        });

        return binding.getRoot();
    }
    public void getUserData(){
        setProgressBar(true);
        FirebaseUtils.getStorageReference().getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    Uri uri1 = task.getResult();
                                    AndroidUtil.setProfileImage(uri1,getContext(),binding.profileImageView);
                                }
                            }
                        });
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    setProgressBar(false);
                     userModel = task.getResult().toObject(UserModel.class);
                    binding.updateUserName.setText(userModel.getUseName());
                    binding.updatePhone.setText(userModel.getPhoneNumber());

                }
            }
        });
    }
    public void setProgressBar(boolean set){
        if(set){
            binding.updateBtn.setVisibility(View.GONE);
            binding.updateProgressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.updateBtn.setVisibility(View.VISIBLE);
            binding.updateProgressBar.setVisibility(View.GONE);
        }
    }
    public void updateUserDetails(String username){
        setProgressBar(true);
        userModel.setUseName(username);
        FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    setProgressBar(false);
                    AndroidUtil.showToast(getContext(),"Updated successfully");
                }
                else{
                    AndroidUtil.showToast(getContext(),"Update Failed");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(data != null && data.getData() != null){
                uri = data.getData();
                AndroidUtil.setProfileImage(uri,getContext(),binding.profileImageView);
            }
            else{
                AndroidUtil.showToast(getContext(),"No image selected");
            }


        }
        else {

        }
    }

}