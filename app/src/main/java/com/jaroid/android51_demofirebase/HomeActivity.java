package com.jaroid.android51_demofirebase;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.edtMessage)
    EditText edtMessage;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.rvMessageHistory)
    RecyclerView rvMessageHistory;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ArrayList<ChatMessageModel> mListMessageHistory;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initFirebase();
        initData();
        initView();
    }

    private void initData() {
        mListMessageHistory = new ArrayList<>();
        mListMessageHistory.clear();
        String name = firebaseUser.getEmail().substring(0, firebaseUser.getEmail().indexOf("@"));
        historyAdapter = new HistoryAdapter(mListMessageHistory, name);
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference();
    }

    private void initView() {
        ButterKnife.bind(this);
        Log.d("TAG", "initView: " + firebaseUser);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToFirebase();
            }
        });
        rvMessageHistory.setAdapter(historyAdapter);

        DatabaseReference dataRefRoomChat = firebaseDatabase.getReference("room_chat");
        dataRefRoomChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (mListMessageHistory.size() == 0) {
//                    for (DataSnapshot data : snapshot.getChildren()) {
//                        ChatMessageModel chatMessageModel = data.getValue(ChatMessageModel.class);
//                        mListMessageHistory.add(chatMessageModel);
//                    }
//                    if (mListMessageHistory.size() > 0) {
//                        updateUI(mListMessageHistory);
//                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.getMessage());
            }
        });

        dataRefRoomChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessageModel chatMessageModel = snapshot.getValue(ChatMessageModel.class);
                Log.d("TAG", "onChildAdded: " + chatMessageModel.toString());
                mListMessageHistory.add(chatMessageModel);
                historyAdapter.addData(chatMessageModel);
                rvMessageHistory.scrollToPosition(mListMessageHistory.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Edit message
                ChatMessageModel chatMessageModel = snapshot.getValue(ChatMessageModel.class);
                Log.d("TAG", "onChildChanged: " + chatMessageModel.toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(ArrayList<ChatMessageModel> mListMessageHistory) {
        historyAdapter.updateData(mListMessageHistory);
    }

    private void sendMessageToFirebase() {
        String message = edtMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUid(firebaseUser.getUid());
            chatMessage.setEmail(firebaseUser.getEmail());
            chatMessage.setAvatar(firebaseUser.getPhotoUrl() + "");
            chatMessage.setMessage(message);

            Date date = Calendar.getInstance(TimeZone.getTimeZone("ICT")).getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
            chatMessage.setDate(simpleDateFormat.format(date));

            DatabaseReference dataRefRoomChat = firebaseDatabase.getReference("room_chat");

            String email = chatMessage.getEmail();
            String name = email.substring(0, email.indexOf("@"));

            ChatMessageModel chatMessageModel = new ChatMessageModel();
            chatMessageModel.setName(name);
            chatMessageModel.setChatMessage(chatMessage);

            dataRefRoomChat.push().setValue(chatMessageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        edtMessage.setText("");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onFailure: " + e.getMessage());
                }
            });
        }
    }
}