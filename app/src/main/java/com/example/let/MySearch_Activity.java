package com.example.let;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.let.MyAdapters.SearchUserRecyclerAdapter;
import com.example.let.MyModels.UserModel;
import com.example.let.databinding.ActivityMySearchBinding;

import com.example.let.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class MySearch_Activity extends AppCompatActivity {
    ActivityMySearchBinding binding ;
    SearchUserRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchUsername.requestFocus();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.searchUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = binding.searchUsername.getText().toString();
                if(searchTerm.isEmpty() || searchTerm.length() < 3){
                    binding.searchUsername.setError("Invalid Username");
                    return ;
                }
                setUpRecyclerView(searchTerm);
            }
        });
    }
    public void setUpRecyclerView(String searchTerm){

        Query query = FirebaseUtils.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("useName",searchTerm)
                .whereLessThanOrEqualTo("useName",searchTerm + "\uf8ff");
        FirestoreRecyclerOptions<UserModel>options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query , UserModel.class)
                .build();

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Checking for result", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && !snapshot.isEmpty()) {
                    Log.d("Checking for result", "Current data: " + snapshot.getDocuments());
                } else {
                    Log.d("Checking for result", "Current data: null");
                }
            }
        });

        adapter = new SearchUserRecyclerAdapter(options);
        binding.searchUserRecyclerView.setLayoutManager(new LinearLayoutManager(MySearch_Activity.this));
        binding.searchUserRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.notifyDataSetChanged();
            adapter.startListening();
        }


    }
}