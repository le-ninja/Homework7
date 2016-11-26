package com.example.android.homework7;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Firebase mRefUsers, mRefMessages, mRef, mRefUsersInfo, mRefMessagesUser;
    private FirebaseUser user;

    private TextView userName, userGender;
    private ImageView userAvatar;

    DatabaseReference rootRef;

    private UsersFragment uf;
    private MessagesFragment mf;

    private String passedUserName = "";
    ArrayList<User> usersList = new ArrayList<>();
    ArrayList<Message> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = (TextView) findViewById(R.id.profile_name_tv);
        userGender = (TextView) findViewById(R.id.profile_gender_tv);
        userAvatar = (ImageView) findViewById(R.id.profile_avatar_iv);

        mAuth = FirebaseAuth.getInstance();
        mRef = new Firebase("https://homework7-425f5.firebaseio.com");
        mRefUsers = mRef.child("Users");
        mRefMessages = mRef.child("Messages");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                mRefMessages = mRef.child("Messages");
                if (user != null) {
                    String currentUserId = user.getUid();
                    mRefUsersInfo = mRefUsers.child(currentUserId);
                    mRefMessagesUser = mRefMessages.child(currentUserId);

                    mRefUsersInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User u = dataSnapshot.getValue(User.class);
                            userName.setText(u.getName());
                            userGender.setText(u.getGender());

                            if (u.getGender().equals("na")) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                                LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
                                final View v = inflater.inflate(R.layout.profile_gender_alert_layout, null);
                                dialog.setView(v);

                                final RadioGroup rg = (RadioGroup) v.findViewById(R.id.profile_alert_rg);
                                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (rg.getCheckedRadioButtonId() != -1) {
                                            String gender = ((RadioButton) v.findViewById(rg.getCheckedRadioButtonId()))
                                                    .getText().toString();
                                            mRefUsersInfo.child("gender").setValue(gender);
                                            userGender.setText(gender);
                                        }
                                    }
                                });
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        };

        ViewPager vp = (ViewPager) findViewById(R.id.profile_viewpager);
        PagerAdapter adapter =
                new PagerAdapter(getSupportFragmentManager(), ProfileActivity.this);
        vp.setAdapter(adapter);

        TabLayout tl = (TabLayout) findViewById(R.id.profile_tablayout);
        tl.setupWithViewPager(vp);

        for (int i = 0; i < tl.getTabCount(); i++) {
            TabLayout.Tab tab = tl.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        mRefUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String dataReference = dataSnapshot.getRef().toString();
                String [] arr = dataReference.split("/");
                if (!user.getUid().equals(arr[arr.length - 1])) {
                    User u = dataSnapshot.getValue(User.class);
                    usersList.add(u);
                    uf.updateList(usersList);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String dataReference = dataSnapshot.getRef().toString();
                String [] arr = dataReference.split("/");
                if (!user.getUid().equals(arr[arr.length - 1])) {
                    User u = dataSnapshot.getValue(User.class);
                    usersList.add(u);
                    uf.updateList(usersList);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String dataReference = dataSnapshot.getRef().toString();
                String [] arr = dataReference.split("/");
                if (!user.getUid().equals(arr[arr.length - 1])) {
                    User u = dataSnapshot.getValue(User.class);
                    usersList.add(u);
                    uf.updateList(usersList);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        /*mRefMessagesUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message m = dataSnapshot.getValue(Message.class);
                messagesList.add(m);
                mf.updateList(messagesList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_item:
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    class PagerAdapter extends FragmentPagerAdapter {
        String [] tabs = new String[] {getResources().getString(R.string.users_btn),
            getResources().getString(R.string.messages_btn)};
        Context context;

        private PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    Bundle extras1 = new Bundle();
                    extras1.putParcelableArrayList("USERS_LIST", usersList);
                    uf = new UsersFragment();
                    uf.setArguments(extras1);
                    return uf;
                case 1:
                    Bundle extras2 = new Bundle();
                    extras2.putParcelableArrayList("USERS_LIST", messagesList);
                    mf = new MessagesFragment();
                    mf.setArguments(extras2);
                    return mf;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // generate title based on item position
            return tabs[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(ProfileActivity.this).inflate(
                    R.layout.profile_custom_tab_layout, null);
            TextView tabTitle = (TextView) tab.findViewById(R.id.custom_text);
            tabTitle.setText(tabs[position]);
            return tab;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


