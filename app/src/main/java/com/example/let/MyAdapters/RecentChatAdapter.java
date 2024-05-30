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
import com.example.let.MyModels.ChatRoomModel;
import com.example.let.MyModels.UserModel;
import com.example.let.databinding.ActivityMySearchBinding;
import com.example.let.databinding.RecentRecyclerRowBinding;
import com.example.let.utils.AndroidUtil;
import com.example.let.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class RecentChatAdapter extends FirestoreRecyclerAdapter<ChatRoomModel , RecentChatAdapter.ChatRoomViewHolder>{

    public RecentChatAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position, @NonNull ChatRoomModel model) {
      FirebaseUtils.getOtherUserId(model.getUserIds()).get()
              .addOnCompleteListener(task -> {
                  if(task.isSuccessful()){
                      boolean lastMessageSentByMe = FirebaseUtils.currentUserUid().equals(model.getLastMessageSenderId());
                      UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                      if(otherUserModel.getUserId().equals(FirebaseUtils.currentUserUid())){
                          holder.binding.userNameText.setText("Self (You)");
                      }
                      else{
                          holder.binding.userNameText.setText(otherUserModel.getUseName());
                      }
                      if(lastMessageSentByMe)
                          holder.binding.lastMessageText.setText("You : "+model.getLastMessage());
                      else{
                          holder.binding.lastMessageText.setText(model.getLastMessage());
                      }

                      holder.binding.lastMessageTime.setText(FirebaseUtils.timeStamptoTime(model.getLastMessageTimeStamp()));

                      holder.itemView.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              Intent intent = new Intent(holder.binding.lastMessageText.getContext(),Chat_Activity.class);
                              intent.putExtra("model",otherUserModel);
                              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              holder.binding.lastMessageText.getContext().startActivity(intent);
                          }
                      });
                  }
              });
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecentRecyclerRowBinding binding = RecentRecyclerRowBinding.inflate(inflater,parent,false);
        return new ChatRoomViewHolder(binding);
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder{
        RecentRecyclerRowBinding binding ;
        ChatRoomViewHolder(@NonNull RecentRecyclerRowBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
