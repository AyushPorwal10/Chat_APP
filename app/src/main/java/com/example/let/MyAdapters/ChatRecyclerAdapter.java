package com.example.let.MyAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.let.Chat_Activity;
import com.example.let.MyModels.ChatMessageModel;
import com.example.let.databinding.ActivityMySearchBinding;
import com.example.let.databinding.ChatMessageRecyclerRowBinding;
import com.example.let.databinding.ChatMessageRecyclerRowBinding;
import com.example.let.utils.AndroidUtil;
import com.example.let.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel , ChatRecyclerAdapter.ChatModelViewHolder>{


    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
       if(model.getSenderId().equals(FirebaseUtils.currentUserUid())){
           Log.d("Got Current user ", "Current user");
           holder.binding.chatRightLayout.setVisibility(View.VISIBLE);
           holder.binding.rightChatTextview.setText(model.getMessage());
           holder.binding.chatLeftLayout.setVisibility(View.GONE);
       }
       else {
           holder.binding.chatLeftLayout.setVisibility(View.VISIBLE);
           holder.binding.leftChatTextview.setText(model.getMessage());
           holder.binding.chatRightLayout.setVisibility(View.GONE);
       }

    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ChatMessageRecyclerRowBinding binding = ChatMessageRecyclerRowBinding.inflate(inflater,parent,false);
        return new ChatModelViewHolder(binding);
    }

    public class ChatModelViewHolder extends RecyclerView.ViewHolder{
        ChatMessageRecyclerRowBinding binding ;
        ChatModelViewHolder(@NonNull ChatMessageRecyclerRowBinding binding){
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
