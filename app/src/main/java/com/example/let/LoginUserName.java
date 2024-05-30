package com.example.let;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.let.MyModels.UserModel;
import com.example.let.databinding.ActivityLoginUserNameBinding;
import com.example.let.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.sql.Time;

public class LoginUserName extends AppCompatActivity {
    ActivityLoginUserNameBinding binding ;
    String phoneNumber;
    UserModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginUserNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phoneNumber = getIntent().getStringExtra("phone");
        getUserName();
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUserName = binding.loginUsername.getText().toString();
                setUserName(enteredUserName);
            }
        });

    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            binding.loginProgressBar.setVisibility(View.VISIBLE);
            binding.loginBtn.setVisibility(View.GONE);
        }
        else{
            binding.loginProgressBar.setVisibility(View.GONE);
            binding.loginBtn.setVisibility(View.VISIBLE);
        }
    }
    public void setUserName(String enteredUserName){

        if(enteredUserName.isEmpty() || enteredUserName.length() < 3){
            binding.loginUsername.setError("Username length should be more than 3 character");
            return ;
        }
        setInProgress(true);
         if(model != null){
            binding.loginUsername.setText(model.getUseName());
        }
        else{
            model = new UserModel(enteredUserName , phoneNumber , Timestamp.now(),FirebaseUtils.currentUserUid());
        }
        FirebaseUtils.currentUserDetails().set(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            setInProgress(false);
                            Intent intent = new Intent(LoginUserName.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });

    }
    public void getUserName(){
        setInProgress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                     model = task.getResult().toObject(UserModel.class);
                    if(model != null){
                        binding.loginUsername.setText(model.getUseName());
                    }
                }
            }
        });
    }
}