package com.example.let;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;


import com.example.let.MyModels.UserModel;
import android.os.StrictMode;
import com.example.let.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;



public class MySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_splash);

        if(FirebaseUtils.isLoggedIn() && getIntent().getExtras() != null){
            //from notification
            String userId = getIntent().getExtras().getString("userId");
            FirebaseUtils.allUserCollectionReference().document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                UserModel userModel = task.getResult().toObject(UserModel.class);
                                Intent mainIntent = new Intent(getApplicationContext() , MainActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(mainIntent);


                                Intent intent = new Intent(getApplicationContext(),Chat_Activity.class);
                                intent.putExtra("notificationModel",userModel);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MySplash.this,LoginPhoneNumber.class));
                }
            },1000);
        }
    }
}