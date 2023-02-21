package com.jaroid.android51_demofirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.edtMessage)
    EditText edtMessage;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initFirebase();
        initView();
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void initView() {
        ButterKnife.bind(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToFirebase(firebaseDatabase.getReference("message"), edtMessage.getText().toString());
            }
        });

        firebaseDatabase.getReference("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                tvMessage.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessageToFirebase(DatabaseReference message, String s) {
        message.setValue(s);
    }
}