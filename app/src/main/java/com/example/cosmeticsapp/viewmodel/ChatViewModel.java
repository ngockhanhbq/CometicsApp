package com.example.cosmeticsapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cosmeticsapp.entity.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatViewModel extends ViewModel {
    protected final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    protected final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    private String chatRoom;
    private MutableLiveData<ArrayList<Message>> messageLiveData;
    private ArrayList<Message> messages;
    private ValueEventListener seenListener;

    public ChatViewModel(){
        messageLiveData = new MutableLiveData<>();
        messages = new ArrayList<>();
    }

    public void setChatRoom(String sendUid, String receiveUid){
        if(sendUid.compareTo(receiveUid) > 0){
            chatRoom =  sendUid+receiveUid;
        }
        else{
            chatRoom =  receiveUid+sendUid;
        }
    }

    public void sendMessage(String senderUserUid,String receiverUserUid, String message, String timestamp){
        Message mess = new Message(message, senderUserUid, receiverUserUid, timestamp, false);
        reference.push().setValue(mess).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void loadMessage(){
        reference = firebaseDatabase.getReference().child("all-chat").child(chatRoom);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message mess = dataSnapshot.getValue(Message.class);
                    messages.add(mess);
                    DatabaseReference lastMessReference = firebaseDatabase
                            .getReference("all-chat")
                            .child("last_mess")
                            .child(chatRoom);
                    lastMessReference.setValue(mess);
                }
                messageLiveData.postValue(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void seenMess() {
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message mess = dataSnapshot.getValue(Message.class);
                    if (mess.getReceiverUid().equals(firebaseAuth.getCurrentUser().getUid())){
                        dataSnapshot.child("seen").getRef().setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeSeenListener() {
        reference.removeEventListener(seenListener);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public MutableLiveData<ArrayList<Message>> getMessageLiveData() {
        return messageLiveData;
    }
}