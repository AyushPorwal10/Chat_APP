package com.example.let;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.let.databinding.ActivityMainBinding;
import com.example.let.utils.AndroidUtil;
import com.example.let.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding ;
    ExecutorService executorService;
    String ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());












        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MySearch_Activity.class));
            }
        });
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()== R.id.menu_chats){
                    replaceFragment(new Chat_Fragment());
                    return true;
                }

                else if(item.getItemId()==R.id.menu_profile){
                    replaceFragment(new Profile_Fragment());
                    return true;
                }
                return false;
            }
        });

        if(savedInstanceState==null){
            binding.bottomNavigation.setSelectedItemId(R.id.menu_chats);
        }
    }
    private void replaceFragment(Fragment fragment) {
        Fragment existingFragment = getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);
        if (existingFragment != null && existingFragment.getClass().equals(fragment.getClass())) {
            // Fragment already exists, no need to replace
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        executorService.shutdown();

    }
}