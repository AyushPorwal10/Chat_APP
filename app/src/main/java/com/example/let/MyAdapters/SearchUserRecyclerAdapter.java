package com.example.let.MyAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.let.Chat_Activity;
import com.example.let.MyModels.UserModel;
import com.example.let.databinding.ActivityMySearchBinding;
import com.example.let.databinding.SearchUserRecyclerRowBinding;
import com.example.let.utils.AndroidUtil;
import com.example.let.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel , SearchUserRecyclerAdapter.UserModelViewHolder>{

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.binding.userNameText.setText(model.getUseName());
        holder.binding.userPhoneText.setText(model.getPhoneNumber());
        if( model.getUserId().equals(FirebaseUtils.currentUserUid())){
            holder.binding.userNameText.setText(model.getUseName() + "(Me)");
        }
        FirebaseUtils.getOtherReference(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> t) {
                        if(t.isSuccessful()){
                            Uri uri1 = t.getResult();
                            AndroidUtil.setProfileImage(uri1,holder.binding.userNameText.getContext(),holder.binding.profilePic.profilePictureImageView);
                        }
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.binding.userNameText.getContext(), Chat_Activity.class);
                intent.putExtra("model",model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.binding.userNameText.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SearchUserRecyclerRowBinding binding = SearchUserRecyclerRowBinding.inflate(inflater,parent,false);
        return new UserModelViewHolder(binding);
    }

    public class UserModelViewHolder extends RecyclerView.ViewHolder{
        SearchUserRecyclerRowBinding binding ;
        UserModelViewHolder(@NonNull SearchUserRecyclerRowBinding binding){
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
