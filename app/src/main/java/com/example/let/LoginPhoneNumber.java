package com.example.let;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.let.databinding.ActivityLoginPhoneNumberBinding;
import com.example.let.utils.FirebaseUtils;
import com.google.firebase.Firebase;

public class LoginPhoneNumber extends AppCompatActivity {
    ActivityLoginPhoneNumberBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginProgressBar.setVisibility(View.GONE);
        binding.loginCountryCode.registerCarrierNumberEditText(binding.loginMobileNumber);
        binding.sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginProgressBar.setVisibility(View.VISIBLE);
                if(!binding.loginCountryCode.isValidFullNumber()){
                    binding.loginMobileNumber.setError("Invalid mobile number");
                    binding.loginProgressBar.setVisibility(View.GONE);
                }
                else{
                    Intent intent = new Intent(LoginPhoneNumber.this,OTP_activity.class);
                    intent.putExtra("phoneNumber",binding.loginCountryCode.getFullNumberWithPlus());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseUtils.isLoggedIn()) {
            startActivity(new Intent(LoginPhoneNumber.this, MainActivity.class));
        }


    }
}