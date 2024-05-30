package com.example.let;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.let.databinding.ActivityOtpBinding;
import com.example.let.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OTP_activity extends AppCompatActivity {
    ActivityOtpBinding binding;
    String phoneNumber;
    Long timerSeconds = 60L;
    String verificationCode ;
    PhoneAuthProvider.ForceResendingToken forceResendingTokan;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent received = getIntent();
        phoneNumber = received.getStringExtra("phoneNumber");


        sendOtp(phoneNumber,false);
        setInProgress(true);
        binding.verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = binding.loginOtp.getText().toString();
                if(enteredOtp.length() < 6){
                    binding.loginOtp.setError("Invalid OTP");
                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode , enteredOtp);
                    signIn(credential);
                    setInProgress(true);
                }


            }
        });

        binding.resendOtpBtn.setOnClickListener(v -> {
            sendOtp(phoneNumber,true);
        });
    }
   void sendOtp(String phoneNumber , boolean isResend){
        startResendTime();
        setInProgress(true);
      PhoneAuthOptions.Builder builder =
              PhoneAuthOptions.newBuilder(mAuth)
               .setPhoneNumber(phoneNumber)
               .setTimeout(timerSeconds , TimeUnit.SECONDS)
               .setActivity(this)
               .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                   @Override
                   public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                       signIn(phoneAuthCredential);
                       setInProgress(false);
                   }

                   @Override
                   public void onVerificationFailed(@NonNull FirebaseException e) {
                       AndroidUtil.showToast(getApplicationContext(),"Something went wrong" + e.toString());
                       setInProgress(false);
                   }

                   @Override
                   public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       super.onCodeSent(s, forceResendingToken);
                       verificationCode = s;
                       forceResendingTokan = forceResendingToken;
                       AndroidUtil.showToast(getApplicationContext(),"OTP sent successfully");
                       setInProgress(false);
                   }
               });
      setInProgress(false);
       if(isResend){
           PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(forceResendingTokan).build());
       }
       else{
           PhoneAuthProvider.verifyPhoneNumber(builder.build());
       }
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            binding.loginProgressBar.setVisibility(View.VISIBLE);
            binding.verifyOtpBtn.setVisibility(View.GONE);
        }
        else{
            binding.loginProgressBar.setVisibility(View.GONE);
            binding.verifyOtpBtn.setVisibility(View.VISIBLE);
        }
    }
    public void signIn(PhoneAuthCredential phoneAuthCredential){
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(OTP_activity.this,LoginUserName.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                    finish();
                }
                else{
                    AndroidUtil.showToast(getApplicationContext(),"Otp verification failed");
                    setInProgress(false);
                }
            }
        });
    }
    public void startResendTime(){
        binding.resendOtpBtn.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerSeconds--;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.resendOtpBtn.setText("Resend OTP in " + timerSeconds + " seconds");
                    }
                });

                if(timerSeconds <= 0){
                    timerSeconds = 60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.resendOtpBtn.setEnabled(true);
                        }
                    });
                }

            }
        },0,1000);
    }
}