package com.example.let;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.let.MyAdapters.RecentChatAdapter;
import com.example.let.MyAdapters.SearchUserRecyclerAdapter;
import com.example.let.MyModels.ChatRoomModel;
import com.example.let.MyModels.UserModel;
import com.example.let.databinding.FragmentChatBinding;
import com.example.let.databinding.RecentRecyclerRowBinding;
import com.example.let.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class Chat_Fragment extends Fragment {
    RecyclerView recent_chat_recycler;
    FragmentChatBinding fragmentChatBinding;
    RecentChatAdapter adapter;
    public Chat_Fragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentChatBinding = FragmentChatBinding.inflate(inflater,container,false);
        setUpRecyclerView();
        return fragmentChatBinding.getRoot();
    }
    public void setUpRecyclerView(){
        Query query = FirebaseUtils.allChatRoomCollectionReference()
                .whereArrayContains("userIds",FirebaseUtils.currentUserUid())
                .orderBy("lastMessageTimeStamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel>options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query , ChatRoomModel.class)
                .build();

        adapter = new RecentChatAdapter(options);
        fragmentChatBinding.recentChatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentChatBinding.recentChatRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.notifyDataSetChanged();
            adapter.startListening();
        }
    }
    }
