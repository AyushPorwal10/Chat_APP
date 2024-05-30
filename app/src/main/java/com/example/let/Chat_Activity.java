package com.example.let;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.let.MyAdapters.ChatRecyclerAdapter;
import com.example.let.MyModels.ChatMessageModel;
import com.example.let.MyModels.ChatRoomModel;
import com.example.let.MyModels.UserModel;
import com.example.let.databinding.ActivityChatBinding;
import com.example.let.utils.AndroidUtil;
import com.example.let.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import java.sql.Time;
import java.util.Arrays;

public class Chat_Activity extends AppCompatActivity {
    ActivityChatBinding binding ;
    UserModel userModel ;
    String chatRoomId;
    ChatRoomModel chatRoomModel;
    ChatRecyclerAdapter chatRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         userModel =(UserModel) getIntent().getParcelableExtra("model"); // here i am receing the object via intent

         binding.userName.setText(userModel.getUseName());
         Log.d("Checkinggggggggggg",userModel.getUserId() +"   "+ FirebaseUtils.currentUserUid());
        chatRoomId = FirebaseUtils.getChatRoomId(userModel.getUserId(),FirebaseUtils.currentUserUid());

         binding.backBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });
         binding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String message = binding.sendMessageEditText.getText().toString().trim();
                 if(message.isEmpty())
                     return;
                 sendMessageToUser(message);

             }
         });
         getOrCreateChatRoomModel();
         setUpChatRecylerView();
    }
    public void getOrCreateChatRoomModel(){
        FirebaseUtils.getChatRoomReference(chatRoomId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                             chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                             if(chatRoomModel == null){
                                 chatRoomModel = new ChatRoomModel(
                                         chatRoomId,
                                         Arrays.asList(FirebaseUtils.currentUserUid() , userModel.getUserId()),
                                         Timestamp.now(),
                                         "",
                                         ""
                                 );
                                 FirebaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel);
                             }
                        }
                    }
                });
    }
    public void sendMessageToUser(String message){
        chatRoomModel.setLastMessageTimeStamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(FirebaseUtils.currentUserUid());
        chatRoomModel.setLastMessage(message);
        FirebaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel);
        ChatMessageModel chatMessageModel = new ChatMessageModel(message , FirebaseUtils.currentUserUid() , Timestamp.now());
        FirebaseUtils.getChatRoomMessageReference(chatRoomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            binding.sendMessageEditText.setText("");
                        }
                    }
                });
    }
    public void setUpChatRecylerView(){
        Query query = FirebaseUtils.getChatRoomMessageReference(chatRoomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModel>options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class)
                .build();
        chatRecyclerAdapter = new ChatRecyclerAdapter(options);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setReverseLayout(true);
        binding.chatRecyclerView.setLayoutManager(manager);
        binding.chatRecyclerView.setAdapter(chatRecyclerAdapter);
        chatRecyclerAdapter.startListening();
        chatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.chatRecyclerView.smoothScrollToPosition(0);
            }
        });
    }
}
