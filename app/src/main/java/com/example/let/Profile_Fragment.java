package com.example.let;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.w3c.dom.Document;


public class Profile_Fragment extends Fragment {
    FragmentProfileBinding binding;
    UserModel userModel;
    public Profile_Fragment() {

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
                updateUserDetails(userName);
            }
        });
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

            }
        });

        return binding.logoutBtn;
    }
    public void getUserData(){
        setProgressBar(true);
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
}