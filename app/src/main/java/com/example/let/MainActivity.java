package com.example.let;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.let.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding ;

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
}