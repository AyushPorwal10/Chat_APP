package com.example.let.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

import kotlin.contracts.Returns;

public class FirebaseUtils {
    public static String currentUserUid(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserUid());
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static DocumentReference getChatRoomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }
    public static CollectionReference getChatRoomMessageReference(String chatRoomId){
        return getChatRoomReference(chatRoomId).collection("chats");
    }
    public static CollectionReference allChatRoomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }
    public static DocumentReference getOtherUserId(List<String> userIds){
        if(userIds.get(0).equals(FirebaseUtils.currentUserUid())){
            return allUserCollectionReference().document(userIds.get(1));
        }
        else return allUserCollectionReference().document(userIds.get(0));
    }
    public static String timeStamptoTime(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
    public static String getChatRoomId(String u1 , String u2){
        if(u1.hashCode()<u2.hashCode())
            return u1+"_"+u2;
        return u2+"_"+u1;
    }
    public static boolean isLoggedIn(){
        if(currentUserUid() != null){
            return true;
        }
        return false;
    }
}
