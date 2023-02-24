package com.jaroid.android51_demofirebase;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initFirebase();
        initView();
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

    }

    private void sendMessageToFirebase() {
        String message = edtMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            ChatMessageModel chatMessageModel = new ChatMessageModel();
            chatMessageModel.setUid(firebaseUser.getUid());
            chatMessageModel.setEmail(firebaseUser.getEmail());
            chatMessageModel.setAvatar(firebaseUser.getPhotoUrl() + "");

            Date date = Calendar.getInstance(TimeZone.getTimeZone("ICT")).getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
            chatMessageModel.setDate(simpleDateFormat.format(date));

            DatabaseReference dataRefRoomChat = firebaseDatabase.getReference("room_chat");

            String email = chatMessageModel.getEmail();
            String name = email.substring(0, email.indexOf("@"));
            dataRefRoomChat.push().child(name).setValue(chatMessageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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