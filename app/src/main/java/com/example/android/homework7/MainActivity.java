package com.example.android.homework7;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient googleClient;
    private CallbackManager mCallbackManager;

    private LoginButton fbLogin;

    private Firebase mRef, mRefUsers, mRefUsersChild;
    private ProgressDialog pd;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.signin_btn).setOnClickListener(this);
        findViewById(R.id.create_account_btn).setOnClickListener(this);

        mRef = new Firebase("https://homework7-425f5.firebaseio.com");
        mRefUsers = new Firebase("https://homework7-425f5.firebaseio.com/Users");

        pd = new ProgressDialog(MainActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Creating user profile...");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            }
        };

        FirebaseAuth.getInstance().signOut();
        // initialize facebook login
        mCallbackManager = CallbackManager.Factory.create();
        fbLogin = (LoginButton) findViewById(R.id.facebook_btn);
        fbLogin.setReadPermissions("email", "public_profile");
        fbLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(
                        loginResult.getAccessToken().getToken());
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = task.getResult().getUser();
                                    mRefUsersChild = mRefUsers.child(user.getUid());
                                    User u = new User(user.getDisplayName(), "na", "na", user.getUid());
                                    mRefUsersChild.setValue(u);
                                }
                            }
                        });
            }

            @Override
            public void onCancel() {
                Log.d("test", "facebook login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("test", "facebook login failed");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.server_token))
                .requestEmail()
                .build();

        googleClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult result) {
                        // yo yo yo
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.google_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signin_btn:
                normalSignIn();
                break;
            case R.id.create_account_btn:
                signUp();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                firebaseAuthWithGoogle(account);
            } else {
                Log.d("test", "account is null");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            mRefUsersChild = mRefUsers.child(user.getUid());
                            User u = new User(user.getDisplayName(), "na", "na", user.getUid());
                            mRefUsersChild.setValue(u);

                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        }
                    }
                });
    }

    private void signUp() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.signup_dialog_layout, null);
        dialog.setView(v);

        final EditText name = (EditText) v.findViewById(R.id.alert_name_et);
        final EditText email = (EditText) v.findViewById(R.id.alert_email_et);
        final EditText choose = (EditText) v.findViewById(R.id.alert_choose_et);
        final EditText retype = (EditText) v.findViewById(R.id.alerty_retype_et);
        final RadioGroup gender = (RadioGroup) v.findViewById(R.id.alert_radiogroup);

        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ((name.getText().length() == 0) || (email.getText().length() == 0) ||
                        !choose.getText().toString().equals(retype.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please fill out all attributes.",
                            Toast.LENGTH_SHORT).show();
                } else if ((choose.getText().length() < 6)) {
                    Toast.makeText(MainActivity.this, "Password must be greater than 6 characters.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // if all criteria is met create a new account and sign the user in
                    pd.show();
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(),
                            choose.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Sign-Up has failed",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    FirebaseUser u = task.getResult().getUser();
                                    String userId = u.getUid();
                                    mRefUsersChild = mRefUsers.child(userId);
                                    User user = new User(name.getText().toString(), ((RadioButton)
                                            v.findViewById(gender.getCheckedRadioButtonId())).getText().toString(),
                                            "na", userId);
                                    mRefUsersChild.setValue(user);
                                    startActivity(new Intent(MainActivity.this,
                                            ProfileActivity.class));
                                    pd.dismiss();
                                }

                            }
                        });
                    pd.dismiss();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Sign-Up was cancelled.", Toast.LENGTH_SHORT)
                        .show();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void normalSignIn() {
        // sign in user with email and password that already exists in database
        // if it does not exist offer the user a chance to sign-up
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.signin_alert_layout, null);
        dialog.setView(v);

        final EditText email = (EditText) v.findViewById(R.id.alert_name_et);
        final EditText password = (EditText) v.findViewById(R.id.alert_email_et);

        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String e = email.getText().toString();
                String p = password.getText().toString();

                mAuth.signInWithEmailAndPassword(e, p)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Log in has failed.", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                }
                            }
                        });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Sign-in was cancelled.", Toast.LENGTH_SHORT)
                        .show();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
