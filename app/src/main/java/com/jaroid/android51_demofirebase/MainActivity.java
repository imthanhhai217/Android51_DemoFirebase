package com.jaroid.android51_demofirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnGoogleSignIn)
    SignInButton btnGoogleSignIn;

    private FirebaseAuth mAuth;
    private BeginSignInRequest beginSignInRequest;
    private SignInClient onTapClient;
    private static final int REQ_ONE_TAP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        onTapClient = Identity.getSignInClient(this);
        beginSignInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential signInCredential = onTapClient.getSignInCredentialFromIntent(data);
                String idToken = signInCredential.getGoogleIdToken();
                if (idToken != null) {
                    // Got an ID token from Google. Use it to authenticate
                    // with Firebase.
                    Log.d("TAG", "Got ID token. " + idToken);
                    loginWithIdToken(idToken);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void loginWithIdToken(String idToken) {
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d("TAG", "onComplete: login success " + user.getEmail());
                    bindViewLoginSuccess();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
                bindViewSignInFail();
                showAlertDialog(e.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //Goto HomeActivity
            Log.d("TAG", "onStart: " + currentUser.getEmail());
            bindViewLoginSuccess();
        } else {
            bindViewSignInFail();
        }
    }

    private void bindViewSignInFail() {
        btnRegister.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
    }

    private void bindViewLoginSuccess() {
        btnRegister.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);
        
        startActivity(new Intent(this,HomeActivity.class));
    }

    private void initView() {
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                signEmailAndPassword(email, password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                createAccount(email, password);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                bindViewSignInFail();
                showAlertDialog("Logout success.");
//                Toast.makeText(MainActivity.this, "Logout success.", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOneTap();
            }
        });
    }

    private void loginOneTap() {
        onTapClient.beginSignIn(beginSignInRequest).addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
            @Override
            public void onSuccess(BeginSignInResult beginSignInResult) {
                try {
                    startIntentSenderForResult(
                            beginSignInResult.getPendingIntent().getIntentSender(),
                            REQ_ONE_TAP,
                            null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", e.getLocalizedMessage());
            }
        });
    }

    private void signEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d("TAG", "onComplete: login success " + user.getEmail());
                    bindViewLoginSuccess();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
                bindViewSignInFail();
                showAlertDialog(e.getMessage());
            }
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TAG", "onComplete: " + user.getEmail());
                            bindViewLoginSuccess();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: " + e.getMessage());
                        bindViewSignInFail();
                        showAlertDialog(e.getMessage());
                    }
                });
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}