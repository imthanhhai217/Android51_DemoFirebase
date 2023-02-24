package com.jaroid.android51_demofirebase;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        }
    }
}